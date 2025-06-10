package dao;

import model.QuestaoAlternativa;
import connectionFactory.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QuestaoAlternativaDAO {

    private static final String INSERIR_QA_SQL = "INSERT INTO questao_alternativa (id_questao, id_alternativa, correta) VALUES (?, ?, ?);";
    private static final String SELECIONAR_QA_POR_ID_SQL = "SELECT id_qa, id_questao, id_alternativa, correta FROM questao_alternativa WHERE id_qa = ?;";
    private static final String SELECIONAR_ALTERNATIVAS_POR_QUESTAO_SQL = "SELECT id_qa, id_questao, id_alternativa, correta FROM questao_alternativa WHERE id_questao = ?;";
    private static final String SELECIONAR_CORRETA_POR_QUESTAO_SQL = "SELECT id_qa, id_questao, id_alternativa, correta FROM questao_alternativa WHERE id_questao = ? AND correta = TRUE;";
    @SuppressWarnings("unused")
    private static final String SELECIONAR_TODAS_QA_SQL = "SELECT id_qa, id_questao, id_alternativa, correta FROM questao_alternativa;";
    private static final String DELETAR_QA_POR_ID_SQL = "DELETE FROM questao_alternativa WHERE id_qa = ?;";
    private static final String DELETAR_QA_POR_QUESTAO_SQL = "DELETE FROM questao_alternativa WHERE id_questao = ?;";
    @SuppressWarnings("unused")
    private static final String DELETAR_QA_POR_ALTERNATIVA_SQL = "DELETE FROM questao_alternativa WHERE id_alternativa = ?;";
    private static final String ATUALIZAR_QA_SQL = "UPDATE questao_alternativa SET id_questao = ?, id_alternativa = ?, correta = ? WHERE id_qa = ?;";
    private static final String DESMARCAR_CORRETAS_POR_QUESTAO_SQL = "UPDATE questao_alternativa SET correta = FALSE WHERE id_questao = ?;";


    /**
     * Adiciona uma nova associação entre Questão e Alternativa.
     * Renomeado de 'inserirQuestaoAlternativa' para 'adicionarQuestaoAlternativa' e adicionado throws SQLException.
     *
     * @param qa O objeto QuestaoAlternativa a ser inserido.
     * @return O ID da associação inserida (id_qa), ou -1 em caso de falha.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public int adicionarQuestaoAlternativa(QuestaoAlternativa qa) throws SQLException { // <-- Nome e throws ajustados
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (qa == null || qa.getIdQuestao() <= 0 || qa.getIdAlternativa() <= 0) {
            System.err.println("Tentativa de inserir QuestaoAlternativa nula ou com IDs de questão/alternativa inválidos.");
            throw new IllegalArgumentException("Associação Questão-Alternativa inválida.");
        }

        try {
            conexao = ConnectionFactory.getConnection();
            
            // Se esta associação está marcando a alternativa como correta, e só pode haver uma correta por questão,
            // desmarcamos as outras ANTES de inserir/atualizar esta.
            // É importante fazer isso *antes* de inserir a nova para evitar ter múltiplas corretas momentaneamente.
            if (qa.isCorreta()) {
                desmarcarCorretasParaQuestao(conexao, qa.getIdQuestao()); // Passa a conexão interna
            }

            pstmt = conexao.prepareStatement(INSERIR_QA_SQL, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, qa.getIdQuestao());
            pstmt.setInt(2, qa.getIdAlternativa());
            pstmt.setBoolean(3, qa.isCorreta());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    qa.setIdQa(idGerado);
                    System.out.println("Associação Questão-Alternativa inserida com sucesso! ID_QA: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir associação Questão-Alternativa.");
            }

        } finally { // O try-catch foi removido daqui
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }

    /**
     * Desmarca todas as alternativas como corretas para uma determinada questão.
     * Este método é private e aceita uma Connection, sendo usado internamente para transações.
     *
     * @param conexao A conexão JDBC a ser usada (não será fechada por este método).
     * @param idQuestao O ID da questão.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    private void desmarcarCorretasParaQuestao(Connection conexao, int idQuestao) throws SQLException { // <-- Alterado para private e exige Connection
        PreparedStatement pstmtInterno = null;
        try {
            pstmtInterno = conexao.prepareStatement(DESMARCAR_CORRETAS_POR_QUESTAO_SQL);
            pstmtInterno.setInt(1, idQuestao);
            int linhasAfetadas = pstmtInterno.executeUpdate();
            if(linhasAfetadas > 0) {
                System.out.println(linhasAfetadas + " alternativa(s) desmarcada(s) como correta(s) para a questão ID: " + idQuestao + " (transação).");
            }
        } finally {
            // NÃO FECHAR A CONEXÃO AQUI! Ela veio de fora.
            try {
                if (pstmtInterno != null) pstmtInterno.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar PreparedStatement interno: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }


    /**
     * Busca uma associação QuestaoAlternativa pelo seu ID (id_qa).
     *
     * @param idQa O ID da associação.
     * @return Um objeto QuestaoAlternativa se encontrado, caso contrário null.
     */
    public QuestaoAlternativa buscarPorIdQa(int idQa) {
        QuestaoAlternativa qa = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_QA_POR_ID_SQL);
            pstmt.setInt(1, idQa);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                qa = mapResultSetToQuestaoAlternativa(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar QuestaoAlternativa por ID_QA: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return qa;
    }

    /**
     * Lista todas as associações de alternativas para uma questão específica.
     *
     * @param idQuestao O ID da questão.
     * @return Uma lista de objetos QuestaoAlternativa.
     */
    public List<QuestaoAlternativa> buscarAlternativasPorQuestao(int idQuestao) {
        List<QuestaoAlternativa> listaQa = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_ALTERNATIVAS_POR_QUESTAO_SQL);
            pstmt.setInt(1, idQuestao);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                listaQa.add(mapResultSetToQuestaoAlternativa(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar alternativas por questão: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return listaQa;
    }

    /**
     * Busca a(s) associação(ões) que marca(m) a(s) alternativa(s) correta(s) para uma questão.
     * Se o design garantir apenas uma correta, esta lista terá no máximo um elemento.
     *
     * @param idQuestao O ID da questão.
     * @return Uma lista de QuestaoAlternativa marcadas como corretas.
     */
    public List<QuestaoAlternativa> buscarCorretasPorQuestao(int idQuestao) {
        List<QuestaoAlternativa> corretas = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_CORRETA_POR_QUESTAO_SQL);
            pstmt.setInt(1, idQuestao);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                corretas.add(mapResultSetToQuestaoAlternativa(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar alternativa(s) correta(s) por questão: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return corretas;
    }
    
    /**
     * Busca a associação que marca a alternativa correta para uma questão.
     * Assume que há apenas uma alternativa correta por questão.
     *
     * @param idQuestao O ID da questão.
     * @return O objeto QuestaoAlternativa da alternativa correta, ou null se não houver ou se houver múltiplas (nesse caso, use buscarCorretasPorQuestao).
     */
    public QuestaoAlternativa buscarUnicaCorretaPorQuestao(int idQuestao) {
        List<QuestaoAlternativa> corretas = buscarCorretasPorQuestao(idQuestao);
        if (corretas.size() == 1) {
            return corretas.get(0);
        } else if (corretas.isEmpty()) {
            System.out.println("Nenhuma alternativa correta encontrada para a questão ID: " + idQuestao);
        } else {
            System.err.println("Múltiplas alternativas corretas encontradas para a questão ID: " + idQuestao + ". Verifique a consistência dos dados.");
        }
        return null; 
    }

    /**
     * Atualiza uma associação Questao-Alternativa existente.
     *
     * @param qa O objeto QuestaoAlternativa com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public boolean atualizarQuestaoAlternativa(QuestaoAlternativa qa) throws SQLException { // <-- Adicionado throws SQLException
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (qa == null || qa.getIdQa() <= 0 || qa.getIdQuestao() <= 0 || qa.getIdAlternativa() <= 0) {
            System.err.println("Tentativa de atualizar QuestaoAlternativa nula ou com IDs inválidos.");
            throw new IllegalArgumentException("Associação Questão-Alternativa inválida para atualização.");
        }

        try {
            conexao = ConnectionFactory.getConnection();
            
            // Se estiver marcando esta como correta, e só pode haver uma, desmarque as outras primeiro
            if (qa.isCorreta()) {
                desmarcarCorretasParaQuestao(conexao, qa.getIdQuestao()); // Usa o método privado auxiliar
            }

            pstmt = conexao.prepareStatement(ATUALIZAR_QA_SQL);
            pstmt.setInt(1, qa.getIdQuestao());
            pstmt.setInt(2, qa.getIdAlternativa());
            pstmt.setBoolean(3, qa.isCorreta());
            pstmt.setInt(4, qa.getIdQa());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Associação Questão-Alternativa atualizada com sucesso! ID_QA: " + qa.getIdQa());
            } else {
                System.out.println("Nenhuma associação Questão-Alternativa encontrada com ID_QA: " + qa.getIdQa() + " para atualização.");
            }

        } finally { // O try-catch foi removido daqui
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }

    /**
     * Exclui uma associação Questao-Alternativa pelo seu ID (id_qa).
     *
     * @param idQa O ID da associação a ser excluída.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public boolean excluirQuestaoAlternativaPorIdQa(int idQa) throws SQLException { // <-- Adicionado throws SQLException
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idQa <= 0) {
            System.err.println("Tentativa de excluir QuestaoAlternativa com ID_QA inválido.");
            throw new IllegalArgumentException("ID de associação inválido para exclusão.");
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_QA_POR_ID_SQL);
            pstmt.setInt(1, idQa);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                excluido = true;
                System.out.println("Associação Questão-Alternativa excluída com sucesso! ID_QA: " + idQa);
            } else {
                System.out.println("Nenhuma associação Questão-Alternativa encontrada com ID_QA: " + idQa + " para exclusão.");
            }

        } finally { // O try-catch foi removido daqui
            closeResources(conexao, pstmt, null);
        }
        return excluido;
    }

    /**
     * Exclui todas as associações de alternativas para uma questão específica.
     * Útil ao excluir uma questão.
     *
     * @param idQuestao O ID da questão.
     * @return O número de associações excluídas.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public int excluirAssociacoesPorQuestao(int idQuestao) throws SQLException { // <-- Adicionado throws SQLException
        int linhasAfetadas = 0;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_QA_POR_QUESTAO_SQL);
            pstmt.setInt(1, idQuestao);
            linhasAfetadas = pstmt.executeUpdate();
            System.out.println(linhasAfetadas + " associação(ões) excluída(s) para a questão ID: " + idQuestao);
        } finally { // O try-catch foi removido daqui
            closeResources(conexao, pstmt, null);
        }
        return linhasAfetadas;
    }


    /**
     * Mapeia uma linha do ResultSet para um objeto QuestaoAlternativa.
     */
    private QuestaoAlternativa mapResultSetToQuestaoAlternativa(ResultSet rs) throws SQLException {
        int idQa = rs.getInt("id_qa");
        int idQuestao = rs.getInt("id_questao");
        int idAlternativa = rs.getInt("id_alternativa");
        boolean correta = rs.getBoolean("correta");
        return new QuestaoAlternativa(idQa, idQuestao, idAlternativa, correta);
    }

    /**
     * Método utilitário para fechar os recursos JDBC.
     */
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar ResultSet: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar Connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}