package dao;

import model.NivelDificuldade; 
import connectionFactory.ConnectionFactory; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NivelDificuldadeDAO {

    // SQL para inserir um novo nível de dificuldade
    private static final String INSERIR_NIVEL_SQL = "INSERT INTO nivel_dificuldade (descricao) VALUES (?);";
    // SQL para selecionar um nível pelo ID
    private static final String SELECIONAR_NIVEL_POR_ID_SQL = "SELECT id_nivel, descricao FROM nivel_dificuldade WHERE id_nivel = ?;";
    // SQL para selecionar um nível pela descrição
    private static final String SELECIONAR_NIVEL_POR_DESCRICAO_SQL = "SELECT id_nivel, descricao FROM nivel_dificuldade WHERE descricao = ?;";
    // SQL para selecionar todos os níveis
    private static final String SELECIONAR_TODOS_NIVEIS_SQL = "SELECT id_nivel, descricao FROM nivel_dificuldade ORDER BY id_nivel;"; // Ou ORDER BY descricao
    // SQL para deletar um nível pelo ID
    private static final String DELETAR_NIVEL_SQL = "DELETE FROM nivel_dificuldade WHERE id_nivel = ?;";
    // SQL para atualizar os dados de um nível
    private static final String ATUALIZAR_NIVEL_SQL = "UPDATE nivel_dificuldade SET descricao = ? WHERE id_nivel = ?;";

    public int inserirNivelDificuldade(NivelDificuldade nivel) {
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (nivel == null || nivel.getDescricao() == null || nivel.getDescricao().trim().isEmpty()) {
            System.err.println("Tentativa de inserir NivelDificuldade nulo ou com descrição vazia.");
            return -1;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(INSERIR_NIVEL_SQL, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, nivel.getDescricao());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    nivel.setIdNivel(idGerado);
                    System.out.println("Nível de dificuldade inserido com sucesso! ID: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir nível de dificuldade.");
            }

        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                 System.err.println("Erro SQL: Nível de dificuldade com a descrição '" + nivel.getDescricao() + "' já existe. " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao inserir nível de dificuldade: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }

    public NivelDificuldade buscarNivelDificuldadePorId(int idNivel) {
        NivelDificuldade nivel = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_NIVEL_POR_ID_SQL);
            pstmt.setInt(1, idNivel);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                nivel = mapResultSetToNivelDificuldade(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar nível de dificuldade por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return nivel;
    }

    public NivelDificuldade buscarNivelDificuldadePorDescricao(String descricao) {
        NivelDificuldade nivel = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (descricao == null || descricao.trim().isEmpty()) {
            System.err.println("Tentativa de buscar NivelDificuldade com descrição nula ou vazia.");
            return null;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_NIVEL_POR_DESCRICAO_SQL);
            pstmt.setString(1, descricao);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                nivel = mapResultSetToNivelDificuldade(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar nível de dificuldade por descrição: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return nivel;
    }

    public List<NivelDificuldade> listarTodosNiveisDificuldade() {
        List<NivelDificuldade> niveis = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_TODOS_NIVEIS_SQL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                niveis.add(mapResultSetToNivelDificuldade(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar todos os níveis de dificuldade: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return niveis;
    }

    public boolean atualizarNivelDificuldade(NivelDificuldade nivel) {
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (nivel == null || nivel.getIdNivel() <= 0 || nivel.getDescricao() == null || nivel.getDescricao().trim().isEmpty()) {
            System.err.println("Tentativa de atualizar NivelDificuldade nulo, com ID inválido ou descrição vazia.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(ATUALIZAR_NIVEL_SQL);

            pstmt.setString(1, nivel.getDescricao());
            pstmt.setInt(2, nivel.getIdNivel());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Nível de dificuldade atualizado com sucesso! ID: " + nivel.getIdNivel());
            } else {
                System.out.println("Nenhum nível de dificuldade encontrado com o ID: " + nivel.getIdNivel() + " para atualização.");
            }

        } catch (SQLException e) {
             if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro SQL: Não foi possível atualizar. Nível com a descrição '" + nivel.getDescricao() + "' já pode existir. " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao atualizar nível de dificuldade: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }

    public boolean excluirNivelDificuldade(int idNivel) {
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idNivel <= 0) {
             System.err.println("Tentativa de excluir NivelDificuldade com ID inválido.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_NIVEL_SQL);
            pstmt.setInt(1, idNivel);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                excluido = true;
                System.out.println("Nível de dificuldade excluído com sucesso! ID: " + idNivel);
            } else {
                 System.out.println("Nenhum nível de dificuldade encontrado com o ID: " + idNivel + " para exclusão.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro SQL: Não foi possível excluir o Nível ID " + idNivel + ". Ele pode estar associado a outros registros (ex: Questões). " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao excluir nível de dificuldade: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return excluido;
    }

    private NivelDificuldade mapResultSetToNivelDificuldade(ResultSet rs) throws SQLException {
        int idNivel = rs.getInt("id_nivel");
        String descricao = rs.getString("descricao");
        return new NivelDificuldade(idNivel, descricao);
    }

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
