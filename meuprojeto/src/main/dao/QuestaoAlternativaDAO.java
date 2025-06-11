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

    public int adicionarQuestaoAlternativa(QuestaoAlternativa qa) throws SQLException { 
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

            if (qa.isCorreta()) {
                desmarcarCorretasParaQuestao(conexao, qa.getIdQuestao());
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

        } finally { 
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }

    private void desmarcarCorretasParaQuestao(Connection conexao, int idQuestao) throws SQLException { 
        PreparedStatement pstmtInterno = null;
        try {
            pstmtInterno = conexao.prepareStatement(DESMARCAR_CORRETAS_POR_QUESTAO_SQL);
            pstmtInterno.setInt(1, idQuestao);
            int linhasAfetadas = pstmtInterno.executeUpdate();
            if(linhasAfetadas > 0) {
                System.out.println(linhasAfetadas + " alternativa(s) desmarcada(s) como correta(s) para a questão ID: " + idQuestao + " (transação).");
            }
        } finally {
            try {
                if (pstmtInterno != null) pstmtInterno.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar PreparedStatement interno: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

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

    public boolean atualizarQuestaoAlternativa(QuestaoAlternativa qa) throws SQLException { 
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (qa == null || qa.getIdQa() <= 0 || qa.getIdQuestao() <= 0 || qa.getIdAlternativa() <= 0) {
            System.err.println("Tentativa de atualizar QuestaoAlternativa nula ou com IDs inválidos.");
            throw new IllegalArgumentException("Associação Questão-Alternativa inválida para atualização.");
        }

        try {
            conexao = ConnectionFactory.getConnection();
            
            if (qa.isCorreta()) {
                desmarcarCorretasParaQuestao(conexao, qa.getIdQuestao()); 
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

        } finally { 
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }

    public boolean excluirQuestaoAlternativaPorIdQa(int idQa) throws SQLException {
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

        } finally { 
            closeResources(conexao, pstmt, null);
        }
        return excluido;
    }
    public int excluirAssociacoesPorQuestao(int idQuestao) throws SQLException { 
        int linhasAfetadas = 0;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_QA_POR_QUESTAO_SQL);
            pstmt.setInt(1, idQuestao);
            linhasAfetadas = pstmt.executeUpdate();
            System.out.println(linhasAfetadas + " associação(ões) excluída(s) para a questão ID: " + idQuestao);
        } finally { 
            closeResources(conexao, pstmt, null);
        }
        return linhasAfetadas;
    }

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