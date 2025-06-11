package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectionFactory.ConnectionFactory;
import model.Alternativa;

public class AlternativaDAO {

    // SQL para inserir uma nova alternativa
    private static final String INSERIR_ALTERNATIVA_SQL = "INSERT INTO alternativa (texto) VALUES (?);";
    // SQL para selecionar uma alternativa pelo ID
    private static final String SELECIONAR_ALTERNATIVA_POR_ID_SQL = "SELECT id_alternativa, texto FROM alternativa WHERE id_alternativa = ?;";
    // SQL para selecionar todas as alternativas
    private static final String SELECIONAR_TODAS_ALTERNATIVAS_SQL = "SELECT id_alternativa, texto FROM alternativa;";
    // SQL para deletar uma alternativa pelo ID
    private static final String DELETAR_ALTERNATIVA_SQL = "DELETE FROM alternativa WHERE id_alternativa = ?;";
    // SQL para atualizar os dados de uma alternativa
    private static final String ATUALIZAR_ALTERNATIVA_SQL = "UPDATE alternativa SET texto = ? WHERE id_alternativa = ?;";

    public int adicionarAlternativa(Alternativa alternativa) throws SQLException { // <-- Nome e throws ajustados
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(INSERIR_ALTERNATIVA_SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, alternativa.getTexto());
            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    alternativa.setIdAlternativa(idGerado);
                    System.out.println("Alternativa inserida com sucesso! ID: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir alternativa.");
            }
        } finally { 
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }
    public Alternativa buscarAlternativaPorId(int idAlternativa) {
        Alternativa alternativa = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_ALTERNATIVA_POR_ID_SQL);
            pstmt.setInt(1, idAlternativa);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String texto = rs.getString("texto");
                alternativa = new Alternativa(idAlternativa, texto); 
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar alternativa por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return alternativa;
    }

    public List<Alternativa> listarTodasAlternativas() {
        List<Alternativa> alternativas = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_TODAS_ALTERNATIVAS_SQL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int idAlternativa = rs.getInt("id_alternativa");
                String texto = rs.getString("texto");
                alternativas.add(new Alternativa(idAlternativa, texto)); 
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar todas as alternativas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return alternativas;
    }

    public boolean atualizarAlternativa(Alternativa alternativa) throws SQLException { 
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (alternativa == null || alternativa.getIdAlternativa() <= 0) {
            System.err.println("Tentativa de atualizar alternativa nula ou com ID inválido.");
            throw new IllegalArgumentException("Alternativa inválida para atualização."); 
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(ATUALIZAR_ALTERNATIVA_SQL);
            pstmt.setString(1, alternativa.getTexto());
            pstmt.setInt(2, alternativa.getIdAlternativa());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Alternativa atualizada com sucesso! ID: " + alternativa.getIdAlternativa());
            } else {
                System.out.println("Nenhuma alternativa encontrada com o ID: " + alternativa.getIdAlternativa() + " para atualização.");
            }

        } finally { 
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }
    public boolean excluirAlternativa(int idAlternativa) throws SQLException { 
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idAlternativa <= 0) {
            System.err.println("Tentativa de excluir alternativa com ID inválido.");
            throw new IllegalArgumentException("ID de alternativa inválido para exclusão.");
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_ALTERNATIVA_SQL);
            pstmt.setInt(1, idAlternativa);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                excluido = true;
                System.out.println("Alternativa excluída com sucesso! ID: " + idAlternativa);
            } else {
                System.out.println("Nenhuma alternativa encontrada com o ID: " + idAlternativa + " para exclusão.");
            }

        } finally { 
            closeResources(conexao, pstmt, null);
        }
        return excluido;
    }
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