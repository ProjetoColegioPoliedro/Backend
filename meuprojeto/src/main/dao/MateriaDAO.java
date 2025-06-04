package dao;

import model.Materia; // Importa a classe Materia do seu pacote model
import connectionFactory.ConnectionFactory; // Importa sua classe de conexão

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {

    // SQL para inserir uma nova matéria
    // Assumindo que id_materia é auto-incrementável no banco
    private static final String INSERIR_MATERIA_SQL = "INSERT INTO materia (nome) VALUES (?);";
    // SQL para selecionar uma matéria pelo ID
    private static final String SELECIONAR_MATERIA_POR_ID_SQL = "SELECT id_materia, nome FROM materia WHERE id_materia = ?;";
    // SQL para selecionar uma matéria pelo nome (útil para verificações ou buscas)
    private static final String SELECIONAR_MATERIA_POR_NOME_SQL = "SELECT id_materia, nome FROM materia WHERE nome = ?;";
    // SQL para selecionar todas as matérias
    private static final String SELECIONAR_TODAS_MATERIAS_SQL = "SELECT id_materia, nome FROM materia ORDER BY nome;";
    // SQL para deletar uma matéria pelo ID
    private static final String DELETAR_MATERIA_SQL = "DELETE FROM materia WHERE id_materia = ?;";
    // SQL para atualizar os dados de uma matéria
    private static final String ATUALIZAR_MATERIA_SQL = "UPDATE materia SET nome = ? WHERE id_materia = ?;";

    /**
     * Insere uma nova matéria no banco de dados.
     *
     * @param materia O objeto Materia a ser inserido.
     * @return O ID da matéria inserida, ou -1 em caso de falha.
     */
    public int inserirMateria(Materia materia) {
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (materia == null || materia.getNome() == null || materia.getNome().trim().isEmpty()) {
            System.err.println("Tentativa de inserir matéria nula ou com nome vazio.");
            return -1;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(INSERIR_MATERIA_SQL, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, materia.getNome());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    materia.setIdMateria(idGerado);
                    System.out.println("Matéria inserida com sucesso! ID: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir matéria.");
            }

        } catch (SQLException e) {
            // Tratar erro de chave duplicada (nome da matéria já existe)
            if (e.getSQLState().startsWith("23")) { // Códigos de erro SQL para violação de constraint
                 System.err.println("Erro SQL: Matéria com o nome '" + materia.getNome() + "' já existe. " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao inserir matéria: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }

    /**
     * Busca uma matéria pelo seu ID.
     *
     * @param idMateria O ID da matéria a ser buscada.
     * @return Um objeto Materia se encontrado, caso contrário null.
     */
    public Materia buscarMateriaPorId(int idMateria) {
        Materia materia = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_MATERIA_POR_ID_SQL);
            pstmt.setInt(1, idMateria);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                materia = mapResultSetToMateria(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar matéria por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return materia;
    }

    /**
     * Busca uma matéria pelo seu nome.
     *
     * @param nome O nome da matéria a ser buscada.
     * @return Um objeto Materia se encontrado, caso contrário null.
     */
    public Materia buscarMateriaPorNome(String nome) {
        Materia materia = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (nome == null || nome.trim().isEmpty()) {
            System.err.println("Tentativa de buscar matéria com nome nulo ou vazio.");
            return null;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_MATERIA_POR_NOME_SQL);
            pstmt.setString(1, nome);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                materia = mapResultSetToMateria(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar matéria por nome: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return materia;
    }

    /**
     * Lista todas as matérias cadastradas no banco de dados.
     *
     * @return Uma lista de objetos Materia, ordenada pelo nome.
     */
    public List<Materia> listarTodasMaterias() {
        List<Materia> materias = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_TODAS_MATERIAS_SQL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                materias.add(mapResultSetToMateria(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar todas as matérias: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return materias;
    }

    /**
     * Atualiza os dados de uma matéria existente no banco de dados.
     *
     * @param materia O objeto Materia com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarMateria(Materia materia) {
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (materia == null || materia.getIdMateria() <= 0 || materia.getNome() == null || materia.getNome().trim().isEmpty()) {
            System.err.println("Tentativa de atualizar matéria nula, com ID inválido ou nome vazio.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(ATUALIZAR_MATERIA_SQL);

            pstmt.setString(1, materia.getNome());
            pstmt.setInt(2, materia.getIdMateria());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Matéria atualizada com sucesso! ID: " + materia.getIdMateria());
            } else {
                System.out.println("Nenhuma matéria encontrada com o ID: " + materia.getIdMateria() + " para atualização.");
            }

        } catch (SQLException e) {
             if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro SQL: Não foi possível atualizar. Matéria com o nome '" + materia.getNome() + "' já pode existir. " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao atualizar matéria: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }

    /**
     * Exclui uma matéria do banco de dados pelo seu ID.
     *
     * @param idMateria O ID da matéria a ser excluída.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     */
    public boolean excluirMateria(int idMateria) {
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idMateria <= 0) {
             System.err.println("Tentativa de excluir matéria com ID inválido.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_MATERIA_SQL);
            pstmt.setInt(1, idMateria);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                excluido = true;
                System.out.println("Matéria excluída com sucesso! ID: " + idMateria);
            } else {
                 System.out.println("Nenhuma matéria encontrada com o ID: " + idMateria + " para exclusão.");
            }
        } catch (SQLException e) {
            // Tratar erro de FK constraint violation se a matéria estiver sendo usada em outra tabela
            if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro SQL: Não foi possível excluir a matéria ID " + idMateria + ". Ela pode estar associada a outros registros (ex: Questões). " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao excluir matéria: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return excluido;
    }

    /**
     * Mapeia uma linha do ResultSet para um objeto Materia.
     * @param rs O ResultSet contendo os dados da matéria.
     * @return Um objeto Materia.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private Materia mapResultSetToMateria(ResultSet rs) throws SQLException {
        int idMateria = rs.getInt("id_materia");
        String nome = rs.getString("nome");
        return new Materia(idMateria, nome);
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