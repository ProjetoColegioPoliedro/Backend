package dao;

import model.Premio; 
import connectionFactory.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PremioDAO {

    // SQL para inserir um novo prêmio
    private static final String INSERIR_PREMIO_SQL = "INSERT INTO premio (descricao, faixas_acertos, valor_simbolico) VALUES (?, ?, ?);";
    // SQL para selecionar um prêmio pelo ID
    private static final String SELECIONAR_PREMIO_POR_ID_SQL = "SELECT id_premio, descricao, faixas_acertos, valor_simbolico FROM premio WHERE id_premio = ?;";
    // SQL para selecionar um prêmio pela descrição
    private static final String SELECIONAR_PREMIO_POR_DESCRICAO_SQL = "SELECT id_premio, descricao, faixas_acertos, valor_simbolico FROM premio WHERE descricao = ?;";
    // SQL para selecionar todos os prêmios
    private static final String SELECIONAR_TODOS_PREMIOS_SQL = "SELECT id_premio, descricao, faixas_acertos, valor_simbolico FROM premio ORDER BY valor_simbolico DESC, id_premio;";
    // SQL para deletar um prêmio pelo ID
    private static final String DELETAR_PREMIO_SQL = "DELETE FROM premio WHERE id_premio = ?;";
    // SQL para atualizar os dados de um prêmio
    private static final String ATUALIZAR_PREMIO_SQL = "UPDATE premio SET descricao = ?, faixas_acertos = ?, valor_simbolico = ? WHERE id_premio = ?;";

    public int inserirPremio(Premio premio) {
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (premio == null || premio.getDescricao() == null || premio.getDescricao().trim().isEmpty() ||
            premio.getFaixasAcertos() == null || premio.getFaixasAcertos().trim().isEmpty()) {
            System.err.println("Tentativa de inserir Premio nulo ou com descrição/faixas de acertos vazias.");
            return -1;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(INSERIR_PREMIO_SQL, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, premio.getDescricao());
            pstmt.setString(2, premio.getFaixasAcertos());
            pstmt.setDouble(3, premio.getValorSimbolico());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    premio.setIdPremio(idGerado);
                    System.out.println("Prêmio inserido com sucesso! ID: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir prêmio.");
            }

        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro SQL: Prêmio com a descrição '" + premio.getDescricao() + "' já pode existir. " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao inserir prêmio: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }

    public Premio buscarPremioPorId(int idPremio) {
        Premio premio = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_PREMIO_POR_ID_SQL);
            pstmt.setInt(1, idPremio);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                premio = mapResultSetToPremio(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar prêmio por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return premio;
    }

    public Premio buscarPremioPorDescricao(String descricao) {
        Premio premio = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (descricao == null || descricao.trim().isEmpty()) {
            System.err.println("Tentativa de buscar Premio com descrição nula ou vazia.");
            return null;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_PREMIO_POR_DESCRICAO_SQL);
            pstmt.setString(1, descricao);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                premio = mapResultSetToPremio(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar prêmio por descrição: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return premio;
    }
    public List<Premio> listarTodosPremios() {
        List<Premio> premios = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_TODOS_PREMIOS_SQL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                premios.add(mapResultSetToPremio(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar todos os prêmios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return premios;
    }
    public boolean atualizarPremio(Premio premio) {
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (premio == null || premio.getIdPremio() <= 0 ||
            premio.getDescricao() == null || premio.getDescricao().trim().isEmpty() ||
            premio.getFaixasAcertos() == null || premio.getFaixasAcertos().trim().isEmpty()) {
            System.err.println("Tentativa de atualizar Premio nulo, com ID inválido, ou descrição/faixas de acertos vazias.");
            return false;
        }
        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(ATUALIZAR_PREMIO_SQL);

            pstmt.setString(1, premio.getDescricao());
            pstmt.setString(2, premio.getFaixasAcertos());
            pstmt.setDouble(3, premio.getValorSimbolico());
            pstmt.setInt(4, premio.getIdPremio());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Prêmio atualizado com sucesso! ID: " + premio.getIdPremio());
            } else {
                System.out.println("Nenhum prêmio encontrado com o ID: " + premio.getIdPremio() + " para atualização.");
            }

        } catch (SQLException e) {
             if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro SQL: Não foi possível atualizar. Prêmio com a descrição '" + premio.getDescricao() + "' já pode existir. " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao atualizar prêmio: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }
    public boolean excluirPremio(int idPremio) {
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idPremio <= 0) {
             System.err.println("Tentativa de excluir Premio com ID inválido.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_PREMIO_SQL);
            pstmt.setInt(1, idPremio);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                excluido = true;
                System.out.println("Prêmio excluído com sucesso! ID: " + idPremio);
            } else {
                 System.out.println("Nenhum prêmio encontrado com o ID: " + idPremio + " para exclusão.");
            }
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro SQL: Não foi possível excluir o Prêmio ID " + idPremio + ". Ele pode estar associado a outros registros. " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao excluir prêmio: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return excluido;
    }
    private Premio mapResultSetToPremio(ResultSet rs) throws SQLException {
        int idPremio = rs.getInt("id_premio");
        String descricao = rs.getString("descricao");
        String faixasAcertos = rs.getString("faixas_acertos");
        double valorSimbolico = rs.getDouble("valor_simbolico");
        return new Premio(idPremio, descricao, faixasAcertos, valorSimbolico);
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