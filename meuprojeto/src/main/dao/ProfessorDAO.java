package dao;

import model.Professor; // Importa a classe Professor do seu pacote model
import connectionFactory.ConnectionFactory; // Importa sua classe de conexão

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {

    // SQL para inserir um novo professor
    // Assumindo que id_professor é auto-incrementável no banco
    private static final String INSERIR_PROFESSOR_SQL = "INSERT INTO professor (nome, login_professor, senha) VALUES (?, ?, ?);";
    // SQL para selecionar um professor pelo ID
    private static final String SELECIONAR_PROFESSOR_POR_ID_SQL = "SELECT id_professor, nome, login_professor, senha FROM professor WHERE id_professor = ?;";
    // SQL para selecionar um professor pelo login
    private static final String SELECIONAR_PROFESSOR_POR_LOGIN_SQL = "SELECT id_professor, nome, login_professor, senha FROM professor WHERE login_professor = ?;";
    // SQL para selecionar todos os professores
    private static final String SELECIONAR_TODOS_PROFESSORES_SQL = "SELECT id_professor, nome, login_professor, senha FROM professor ORDER BY nome;";
    // SQL para deletar um professor pelo ID
    private static final String DELETAR_PROFESSOR_SQL = "DELETE FROM professor WHERE id_professor = ?;";
    // SQL para atualizar os dados de um professor
    private static final String ATUALIZAR_PROFESSOR_SQL = "UPDATE professor SET nome = ?, login_professor = ?, senha = ? WHERE id_professor = ?;";

    /**
     * Insere um novo professor no banco de dados.
     * A senha deve ser tratada com hashing antes de ser persistida em um ambiente real.
     *
     * @param professor O objeto Professor a ser inserido.
     * @return O ID do professor inserido, ou -1 em caso de falha.
     */
    public int inserirProfessor(Professor professor) {
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (professor == null || professor.getNome() == null || professor.getNome().trim().isEmpty() ||
            professor.getLoginProfessor() == null || professor.getLoginProfessor().trim().isEmpty() ||
            professor.getSenha() == null || professor.getSenha().isEmpty()) {
            System.err.println("Tentativa de inserir Professor nulo ou com campos obrigatórios (nome, login, senha) vazios.");
            return -1;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(INSERIR_PROFESSOR_SQL, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, professor.getNome());
            pstmt.setString(2, professor.getLoginProfessor());
            pstmt.setString(3, professor.getSenha()); // ATENÇÃO: Armazenar senhas em texto plano é inseguro. Use hashing.

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    professor.setIdProfessor(idGerado);
                    System.out.println("Professor inserido com sucesso! ID: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir professor.");
            }

        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { // Violação de constraint (ex: UNIQUE no login_professor)
                 System.err.println("Erro SQL: Professor com o login '" + professor.getLoginProfessor() + "' já existe. " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao inserir professor: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }

    /**
     * Busca um professor pelo seu ID.
     *
     * @param idProfessor O ID do professor a ser buscado.
     * @return Um objeto Professor se encontrado, caso contrário null.
     */
    public Professor buscarProfessorPorId(int idProfessor) {
        Professor professor = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_PROFESSOR_POR_ID_SQL);
            pstmt.setInt(1, idProfessor);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                professor = mapResultSetToProfessor(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar professor por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return professor;
    }

    /**
     * Busca um professor pelo seu login.
     *
     * @param login O login do professor a ser buscado.
     * @return Um objeto Professor se encontrado, caso contrário null.
     */
    public Professor buscarProfessorPorLogin(String login) {
        Professor professor = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (login == null || login.trim().isEmpty()) {
            System.err.println("Tentativa de buscar Professor com login nulo ou vazio.");
            return null;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_PROFESSOR_POR_LOGIN_SQL);
            pstmt.setString(1, login);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                professor = mapResultSetToProfessor(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar professor por login: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return professor;
    }

    /**
     * Lista todos os professores cadastrados no banco de dados.
     *
     * @return Uma lista de objetos Professor.
     */
    public List<Professor> listarTodosProfessores() {
        List<Professor> professores = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_TODOS_PROFESSORES_SQL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                professores.add(mapResultSetToProfessor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar todos os professores: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return professores;
    }

    /**
     * Atualiza os dados de um professor existente no banco de dados.
     *
     * @param professor O objeto Professor com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarProfessor(Professor professor) {
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (professor == null || professor.getIdProfessor() <= 0 ||
            professor.getNome() == null || professor.getNome().trim().isEmpty() ||
            professor.getLoginProfessor() == null || professor.getLoginProfessor().trim().isEmpty() ||
            professor.getSenha() == null || professor.getSenha().isEmpty()) {
            System.err.println("Tentativa de atualizar Professor nulo, com ID inválido ou campos obrigatórios vazios.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(ATUALIZAR_PROFESSOR_SQL);

            pstmt.setString(1, professor.getNome());
            pstmt.setString(2, professor.getLoginProfessor());
            pstmt.setString(3, professor.getSenha()); // ATENÇÃO: Segurança de senha
            pstmt.setInt(4, professor.getIdProfessor());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Professor atualizado com sucesso! ID: " + professor.getIdProfessor());
            } else {
                System.out.println("Nenhum professor encontrado com o ID: " + professor.getIdProfessor() + " para atualização.");
            }

        } catch (SQLException e) {
             if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro SQL: Não foi possível atualizar. Professor com o login '" + professor.getLoginProfessor() + "' já pode existir. " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao atualizar professor: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }

    /**
     * Exclui um professor do banco de dados pelo seu ID.
     *
     * @param idProfessor O ID do professor a ser excluído.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     */
    public boolean excluirProfessor(int idProfessor) {
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idProfessor <= 0) {
             System.err.println("Tentativa de excluir Professor com ID inválido.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_PROFESSOR_SQL);
            pstmt.setInt(1, idProfessor);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                excluido = true;
                System.out.println("Professor excluído com sucesso! ID: " + idProfessor);
            } else {
                 System.out.println("Nenhum professor encontrado com o ID: " + idProfessor + " para exclusão.");
            }
        } catch (SQLException e) {
            // Tratar erro de FK constraint violation se o professor estiver sendo usado
            if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro SQL: Não foi possível excluir o Professor ID " + idProfessor + ". Ele pode estar associado a outros registros. " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao excluir professor: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return excluido;
    }

    /**
     * Mapeia uma linha do ResultSet para um objeto Professor.
     */
    private Professor mapResultSetToProfessor(ResultSet rs) throws SQLException {
        int idProfessor = rs.getInt("id_professor");
        String nome = rs.getString("nome");
        String loginProfessor = rs.getString("login_professor");
        String senha = rs.getString("senha"); // Lembre-se da segurança de senhas
        return new Professor(idProfessor, nome, loginProfessor, senha);
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

