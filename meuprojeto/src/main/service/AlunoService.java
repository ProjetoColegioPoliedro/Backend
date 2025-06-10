package service;

import dao.AlunoDAO;
import model.Aluno;

import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List; // Importar a lista para o novo método

public class AlunoService {

    private AlunoDAO alunoDAO;

    public AlunoService() {
        this.alunoDAO = new AlunoDAO();
    }

    /**
     * Cadastra um novo aluno no sistema.
     * Realiza validações e faz o hash da senha antes de persistir.
     */
    public Aluno cadastrarAluno(String nome, String loginAluno, String senha, int anoLetivo) throws IllegalArgumentException, SQLException, NoSuchAlgorithmException {
        // 1. Validação de campos obrigatórios
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório.");
        }
        if (loginAluno == null || loginAluno.trim().isEmpty()) {
            throw new IllegalArgumentException("O login do aluno é obrigatório.");
        }
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }

        // 2. Validação de unicidade do login
        if (alunoDAO.buscarAlunoPorLogin(loginAluno) != null) {
            throw new IllegalArgumentException("O login '" + loginAluno + "' já está em uso. Por favor, escolha outro.");
        }

        // 3. Hash da senha
        String senhaHash = hashSenha(senha);

        // 4. Criar objeto Aluno com o hash da senha
        Aluno novoAluno = new Aluno(nome, loginAluno, senhaHash, anoLetivo);

        // 5. Adicionar ao banco de dados via DAO
        int idGerado = alunoDAO.inserirAluno(novoAluno);
        if (idGerado > 0) {
            novoAluno.setIdAluno(idGerado);
            return novoAluno;
        } else {
            throw new SQLException("Falha ao cadastrar aluno: não foi possível obter o ID gerado.");
        }
    }
    
    // =======================================================================
    // ✅ MÉTODOS ADICIONADOS PARA SUPORTE AO LOGIN E HISTÓRICO
    // =======================================================================

    /**
     * Busca um aluno pelo seu login.
     * Essencial para o processo de login no Navegador.
     *
     * @param login O login do aluno a ser buscado.
     * @return O objeto Aluno se encontrado, caso contrário null.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     */
    public Aluno buscarPorLogin(String login) throws SQLException {
        return alunoDAO.buscarAlunoPorLogin(login);
    }

    /**
     * Busca um aluno pelo seu ID.
     * Essencial para a TelaHistAdmin exibir o nome do aluno.
     *
     * @param id O ID do aluno a ser buscado.
     * @return O objeto Aluno se encontrado, caso contrário null.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     */
    public Aluno buscarPorId(int id) throws SQLException {
        return alunoDAO.buscarAlunoPorId(id);
    }
    
    /**
     * Busca todos os alunos cadastrados no sistema.
     * Útil para funcionalidades de admin.
     * @return Uma lista de todos os alunos.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     */
    public List<Aluno> buscarTodos() throws SQLException {
        return alunoDAO.listarTodosAlunos();
    }
    
    // =======================================================================

    /**
     * Realiza o hash de uma senha usando SHA-256 e Base64.
     */
    private String hashSenha(String senhaTextoPuro) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(senhaTextoPuro.getBytes());
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}