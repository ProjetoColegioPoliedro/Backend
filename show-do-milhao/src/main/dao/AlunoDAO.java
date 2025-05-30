package dao;

import model.Aluno; // Importa a classe Aluno do seu pacote model
import connectionFactory.ConnectionFactory; // Importa sua classe de conexão

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    // SQL para inserir um novo aluno
    // Assumindo que id_aluno é auto-incrementável no banco
    private static final String INSERIR_ALUNO_SQL = "INSERT INTO aluno (nome, login_aluno, senha, ano_letivo) VALUES (?, ?, ?, ?);";
    // SQL para selecionar um aluno pelo ID
    private static final String SELECIONAR_ALUNO_POR_ID_SQL = "SELECT id_aluno, nome, login_aluno, senha, ano_letivo FROM aluno WHERE id_aluno = ?;";
    // SQL para selecionar um aluno pelo login
    private static final String SELECIONAR_ALUNO_POR_LOGIN_SQL = "SELECT id_aluno, nome, login_aluno, senha, ano_letivo FROM aluno WHERE login_aluno = ?;";
    // SQL para selecionar todos os alunos
    private static final String SELECIONAR_TODOS_ALUNOS_SQL = "SELECT id_aluno, nome, login_aluno, senha, ano_letivo FROM aluno;";
    // SQL para deletar um aluno pelo ID
    private static final String DELETAR_ALUNO_SQL = "DELETE FROM aluno WHERE id_aluno = ?;";
    // SQL para atualizar os dados de um aluno
    private static final String ATUALIZAR_ALUNO_SQL = "UPDATE aluno SET nome = ?, login_aluno = ?, senha = ?, ano_letivo = ? WHERE id_aluno = ?;";

    /**
     * Insere um novo aluno no banco de dados.
     * A senha deve ser tratada com hashing antes de ser persistida em um ambiente real.
     *
     * @param aluno O objeto Aluno a ser inserido (sem o id, se for auto-incrementável).
     * @return O ID do aluno inserido, ou -1 em caso de falha.
     */
    public int inserirAluno(Aluno aluno) {
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(INSERIR_ALUNO_SQL, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, aluno.getNome());
            pstmt.setString(2, aluno.getLoginAluno());
            pstmt.setString(3, aluno.getSenha()); // ATENÇÃO: Armazenar senhas em texto plano é inseguro. Use hashing.
            pstmt.setInt(4, aluno.getAnoLetivo());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    aluno.setIdAluno(idGerado); // Atualiza o objeto Aluno com o ID gerado
                    System.out.println("Aluno inserido com sucesso! ID: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir aluno.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir aluno: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }

    /**
     * Busca um aluno pelo seu ID.
     *
     * @param idAluno O ID do aluno a ser buscado.
     * @return Um objeto Aluno se encontrado, caso contrário null.
     */
    public Aluno buscarAlunoPorId(int idAluno) {
        Aluno aluno = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_ALUNO_POR_ID_SQL);
            pstmt.setInt(1, idAluno);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                aluno = mapResultSetToAluno(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar aluno por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return aluno;
    }

    /**
     * Busca um aluno pelo seu login.
     *
     * @param login O login do aluno a ser buscado.
     * @return Um objeto Aluno se encontrado, caso contrário null.
     */
    public Aluno buscarAlunoPorLogin(String login) {
        Aluno aluno = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_ALUNO_POR_LOGIN_SQL);
            pstmt.setString(1, login);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                aluno = mapResultSetToAluno(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar aluno por login: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return aluno;
    }

    /**
     * Lista todos os alunos cadastrados no banco de dados.
     *
     * @return Uma lista de objetos Aluno.
     */
    public List<Aluno> listarTodosAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_TODOS_ALUNOS_SQL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                alunos.add(mapResultSetToAluno(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar todos os alunos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return alunos;
    }

    /**
     * Atualiza os dados de um aluno existente no banco de dados.
     *
     * @param aluno O objeto Aluno com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarAluno(Aluno aluno) {
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (aluno == null || aluno.getIdAluno() <= 0) {
            System.err.println("Tentativa de atualizar aluno nulo ou com ID inválido.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(ATUALIZAR_ALUNO_SQL);

            pstmt.setString(1, aluno.getNome());
            pstmt.setString(2, aluno.getLoginAluno());
            pstmt.setString(3, aluno.getSenha()); // ATENÇÃO: Segurança de senha
            pstmt.setInt(4, aluno.getAnoLetivo());
            pstmt.setInt(5, aluno.getIdAluno());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Aluno atualizado com sucesso! ID: " + aluno.getIdAluno());
            } else {
                System.out.println("Nenhum aluno encontrado com o ID: " + aluno.getIdAluno() + " para atualização.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar aluno: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, null); // ResultSet não é usado aqui
        }
        return atualizado;
    }

    /**
     * Exclui um aluno do banco de dados pelo seu ID.
     *
     * @param idAluno O ID do aluno a ser excluído.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     */
    public boolean excluirAluno(int idAluno) {
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idAluno <= 0) {
             System.err.println("Tentativa de excluir aluno com ID inválido.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_ALUNO_SQL);
            pstmt.setInt(1, idAluno);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                excluido = true;
                System.out.println("Aluno excluído com sucesso! ID: " + idAluno);
            } else {
                 System.out.println("Nenhum aluno encontrado com o ID: " + idAluno + " para exclusão.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao excluir aluno: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, null); // ResultSet não é usado aqui
        }
        return excluido;
    }

    /**
     * Mapeia uma linha do ResultSet para um objeto Aluno.
     * @param rs O ResultSet contendo os dados do aluno.
     * @return Um objeto Aluno.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private Aluno mapResultSetToAluno(ResultSet rs) throws SQLException {
        int idAluno = rs.getInt("id_aluno");
        String nome = rs.getString("nome");
        String loginAluno = rs.getString("login_aluno");
        String senha = rs.getString("senha"); // Lembre-se da segurança de senhas
        int anoLetivo = rs.getInt("ano_letivo");
        return new Aluno(idAluno, nome, loginAluno, senha, anoLetivo);
    }

    /**
     * Método utilitário para fechar os recursos JDBC.
     * @param conn A conexão com o banco.
     * @param stmt O PreparedStatement.
     * @param rs O ResultSet.
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