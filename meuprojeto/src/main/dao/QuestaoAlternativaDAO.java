package dao;

import model.QuestaoAlternativa; // Importa a classe QuestaoAlternativa do seu pacote model
// Supondo que você também tenha as classes Questao e Alternativa em model para testes ou buscas mais complexas
// import model.Questao;
// import model.Alternativa;
import connectionFactory.ConnectionFactory; // Importa sua classe de conexão

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QuestaoAlternativaDAO {

    // SQL para inserir uma nova associação Questao-Alternativa
    // Assumindo que id_qa é auto-incrementável no banco
    private static final String INSERIR_QA_SQL = "INSERT INTO questao_alternativa (id_questao, id_alternativa, correta) VALUES (?, ?, ?);";
    // SQL para selecionar uma associação pelo ID
    private static final String SELECIONAR_QA_POR_ID_SQL = "SELECT id_qa, id_questao, id_alternativa, correta FROM questao_alternativa WHERE id_qa = ?;";
    // SQL para selecionar todas as alternativas de uma questão
    private static final String SELECIONAR_ALTERNATIVAS_POR_QUESTAO_SQL = "SELECT id_qa, id_questao, id_alternativa, correta FROM questao_alternativa WHERE id_questao = ?;";
    // SQL para selecionar a(s) alternativa(s) correta(s) de uma questão
    private static final String SELECIONAR_CORRETA_POR_QUESTAO_SQL = "SELECT id_qa, id_questao, id_alternativa, correta FROM questao_alternativa WHERE id_questao = ? AND correta = TRUE;";
    // SQL para selecionar todas as associações (geralmente não muito útil sem filtros, mas pode ser para admin)
    @SuppressWarnings("unused")
    private static final String SELECIONAR_TODAS_QA_SQL = "SELECT id_qa, id_questao, id_alternativa, correta FROM questao_alternativa;";
    // SQL para deletar uma associação pelo ID
    private static final String DELETAR_QA_POR_ID_SQL = "DELETE FROM questao_alternativa WHERE id_qa = ?;";
    // SQL para deletar todas as associações de uma questão específica
    private static final String DELETAR_QA_POR_QUESTAO_SQL = "DELETE FROM questao_alternativa WHERE id_questao = ?;";
    // SQL para deletar todas as associações de uma alternativa específica
    @SuppressWarnings("unused")
    private static final String DELETAR_QA_POR_ALTERNATIVA_SQL = "DELETE FROM questao_alternativa WHERE id_alternativa = ?;";
    // SQL para atualizar os dados de uma associação
    private static final String ATUALIZAR_QA_SQL = "UPDATE questao_alternativa SET id_questao = ?, id_alternativa = ?, correta = ? WHERE id_qa = ?;";
    // SQL para desmarcar todas as alternativas como corretas para uma questão (antes de marcar uma nova como correta)
    private static final String DESMARCAR_CORRETAS_POR_QUESTAO_SQL = "UPDATE questao_alternativa SET correta = FALSE WHERE id_questao = ?;";


    /**
     * Insere uma nova associação entre Questão e Alternativa.
     *
     * @param qa O objeto QuestaoAlternativa a ser inserido.
     * @return O ID da associação inserida (id_qa), ou -1 em caso de falha.
     */
    public int inserirQuestaoAlternativa(QuestaoAlternativa qa) {
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (qa == null || qa.getIdQuestao() <= 0 || qa.getIdAlternativa() <= 0) {
            System.err.println("Tentativa de inserir QuestaoAlternativa nula ou com IDs de questão/alternativa inválidos.");
            return -1;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            // Se uma questão só pode ter uma alternativa correta,
            // pode ser necessário desmarcar outras antes de inserir/marcar uma nova como correta.
            // Isso pode ser feito aqui ou na lógica de serviço.
            // Exemplo: if (qa.isCorreta()) { desmarcarCorretasParaQuestao(conexao, qa.getIdQuestao()); }


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

        } catch (SQLException e) {
            // Verificar se a combinação (id_questao, id_alternativa) já existe se houver uma constraint UNIQUE
            if (e.getSQLState().startsWith("23")) {
                 System.err.println("Erro SQL: A associação entre Questão ID " + qa.getIdQuestao() +
                                    " e Alternativa ID " + qa.getIdAlternativa() + " já pode existir. " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao inserir associação Questão-Alternativa: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }

    /**
     * Desmarca todas as alternativas como corretas para uma determinada questão.
     * Útil antes de definir uma nova alternativa correta para garantir que apenas uma seja marcada.
     * @param idQuestao O ID da questão.
     * @return true se alguma linha foi atualizada, false caso contrário.
     */
    public boolean desmarcarCorretasParaQuestao(int idQuestao) {
        Connection conexao = null;
        PreparedStatement pstmt = null;
        boolean sucesso = false;
        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DESMARCAR_CORRETAS_POR_QUESTAO_SQL);
            pstmt.setInt(1, idQuestao);
            int linhasAfetadas = pstmt.executeUpdate();
            sucesso = linhasAfetadas >= 0; // Mesmo 0 linhas afetadas pode ser sucesso se não havia nenhuma marcada
            if(linhasAfetadas > 0) {
                 System.out.println(linhasAfetadas + " alternativa(s) desmarcada(s) como correta(s) para a questão ID: " + idQuestao);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao desmarcar alternativas corretas para a questão ID " + idQuestao + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return sucesso;
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
     */
    public boolean atualizarQuestaoAlternativa(QuestaoAlternativa qa) {
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (qa == null || qa.getIdQa() <= 0 || qa.getIdQuestao() <= 0 || qa.getIdAlternativa() <= 0) {
            System.err.println("Tentativa de atualizar QuestaoAlternativa nula ou com IDs inválidos.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
             // Se estiver marcando esta como correta, e só pode haver uma, desmarque as outras primeiro
            if (qa.isCorreta()) {
                desmarcarCorretasParaQuestao(qa.getIdQuestao()); // Usa o método público que já tem o try-catch
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

        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar associação Questão-Alternativa: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }

    /**
     * Exclui uma associação Questao-Alternativa pelo seu ID (id_qa).
     *
     * @param idQa O ID da associação a ser excluída.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     */
    public boolean excluirQuestaoAlternativaPorIdQa(int idQa) {
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idQa <= 0) {
            System.err.println("Tentativa de excluir QuestaoAlternativa com ID_QA inválido.");
            return false;
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
        } catch (SQLException e) {
            System.err.println("Erro SQL ao excluir associação Questão-Alternativa por ID_QA: " + e.getMessage());
            e.printStackTrace();
        } finally {
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
     */
    public int excluirAssociacoesPorQuestao(int idQuestao) {
        int linhasAfetadas = 0;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_QA_POR_QUESTAO_SQL);
            pstmt.setInt(1, idQuestao);
            linhasAfetadas = pstmt.executeUpdate();
            System.out.println(linhasAfetadas + " associação(ões) excluída(s) para a questão ID: " + idQuestao);
        } catch (SQLException e) {
            System.err.println("Erro SQL ao excluir associações por questão: " + e.getMessage());
            e.printStackTrace();
        } finally {
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
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
        }
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar Connection: " + e.getMessage());
        }
    }
}