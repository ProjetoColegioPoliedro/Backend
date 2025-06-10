package dao;

import model.Aluno;
import connectionFactory.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    private static final String INSERIR_ALUNO_SQL = "INSERT INTO aluno (nome, login_aluno, senha, ano_letivo) VALUES (?, ?, ?, ?);";
    private static final String SELECIONAR_ALUNO_POR_ID_SQL = "SELECT id_aluno, nome, login_aluno, senha, ano_letivo FROM aluno WHERE id_aluno = ?;";
    private static final String SELECIONAR_ALUNO_POR_LOGIN_SQL = "SELECT id_aluno, nome, login_aluno, senha, ano_letivo FROM aluno WHERE login_aluno = ?;";
    private static final String SELECIONAR_ALUNO_POR_LOGIN_E_SENHA = "SELECT id_aluno, nome, login_aluno, senha, ano_letivo FROM aluno WHERE login_aluno = ? AND senha = ?;";
    private static final String SELECIONAR_TODOS_ALUNOS_SQL = "SELECT id_aluno, nome, login_aluno, senha, ano_letivo FROM aluno;";
    private static final String DELETAR_ALUNO_SQL = "DELETE FROM aluno WHERE id_aluno = ?;";
    private static final String ATUALIZAR_ALUNO_SQL = "UPDATE aluno SET nome = ?, login_aluno = ?, senha = ?, ano_letivo = ? WHERE id_aluno = ?;";

    /**
     * Insere um novo aluno no banco de dados.
     * @param aluno O objeto Aluno a ser inserido.
     * @return O ID do aluno inserido.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public int inserirAluno(Aluno aluno) throws SQLException { // <--- throws SQLException
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(INSERIR_ALUNO_SQL, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, aluno.getNome());
            pstmt.setString(2, aluno.getLoginAluno());
            pstmt.setString(3, aluno.getSenha()); // A senha deve ser um hash aqui!
            pstmt.setInt(4, aluno.getAnoLetivo());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    aluno.setIdAluno(idGerado);
                    System.out.println("Aluno inserido com sucesso! ID: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir aluno.");
            }

        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }

    /**
     * Busca um aluno pelo seu ID.
     * @param idAluno O ID do aluno a ser buscado.
     * @return Um objeto Aluno se encontrado, caso contrário null.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public Aluno buscarAlunoPorId(int idAluno) throws SQLException { // <--- throws SQLException
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
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return aluno;
    }

    /**
     * Busca um aluno pelo seu login.
     * @param login O login do aluno a ser buscado.
     * @return Um objeto Aluno se encontrado, caso contrário null.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public Aluno buscarAlunoPorLogin(String login) throws SQLException { // <--- throws SQLException
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
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return aluno;
    }

    /**
     * Busca um aluno pelo seu login e senha.
     * @param login O login do aluno.
     * @param senha A senha do aluno (espera-se que seja o hash da senha).
     * @return Um objeto Aluno se encontrado e as credenciais baterem, caso contrário null.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public Aluno buscarAlunoPorLoginESenha(String login, String senha) throws SQLException { // <--- throws SQLException
        Aluno aluno = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Validação básica pode ser feita na camada de serviço ou UI
        if (login == null || login.trim().isEmpty() || senha == null || senha.isEmpty()) {
            System.err.println("Tentativa de buscar Aluno com login/senha nulos ou vazios.");
            return null; // ou throw new IllegalArgumentException
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_ALUNO_POR_LOGIN_E_SENHA);
            pstmt.setString(1, login);
            pstmt.setString(2, senha);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                aluno = mapResultSetToAluno(rs);
            }
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return aluno;
    }

    /**
     * Lista todos os alunos cadastrados no banco de dados.
     * @return Uma lista de objetos Aluno.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public List<Aluno> listarTodosAlunos() throws SQLException { // <--- throws SQLException
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
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return alunos;
    }

    /**
     * Atualiza os dados de um aluno existente no banco de dados.
     * @param aluno O objeto Aluno com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public boolean atualizarAluno(Aluno aluno) throws SQLException { // <--- throws SQLException
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (aluno == null || aluno.getIdAluno() <= 0) {
            System.err.println("Tentativa de atualizar aluno nulo ou com ID inválido.");
            throw new IllegalArgumentException("Aluno inválido para atualização.");
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(ATUALIZAR_ALUNO_SQL);

            pstmt.setString(1, aluno.getNome());
            pstmt.setString(2, aluno.getLoginAluno());
            pstmt.setString(3, aluno.getSenha()); // A senha deve ser um hash aqui!
            pstmt.setInt(4, aluno.getAnoLetivo());
            pstmt.setInt(5, aluno.getIdAluno());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Aluno atualizado com sucesso! ID: " + aluno.getIdAluno());
            } else {
                System.out.println("Nenhum aluno encontrado com o ID: " + aluno.getIdAluno() + " para atualização.");
            }

        } finally {
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }

    /**
     * Exclui um aluno do banco de dados pelo seu ID.
     * @param idAluno O ID do aluno a ser excluído.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public boolean excluirAluno(int idAluno) throws SQLException { // <--- throws SQLException
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idAluno <= 0) {
            System.err.println("Tentativa de excluir aluno com ID inválido.");
            throw new IllegalArgumentException("ID de aluno inválido para exclusão.");
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

        } finally {
            closeResources(conexao, pstmt, null);
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
        String senha = rs.getString("senha"); // ATENÇÃO: Se armazenar hash, isso é o hash
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