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
    public Aluno cadastrarAluno(String nome, String loginAluno, String senha, int anoLetivo) throws IllegalArgumentException, SQLException, NoSuchAlgorithmException {
        //Validação de campos obrigatórios
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório.");
        }
        if (loginAluno == null || loginAluno.trim().isEmpty()) {
            throw new IllegalArgumentException("O login do aluno é obrigatório.");
        }
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }
        //Validação de unicidade do login
        if (alunoDAO.buscarAlunoPorLogin(loginAluno) != null) {
            throw new IllegalArgumentException("O login '" + loginAluno + "' já está em uso. Por favor, escolha outro.");
        }
        //Hash da senha
        String senhaHash = hashSenha(senha);
        //Aluno com o hash da senha
        Aluno novoAluno = new Aluno(nome, loginAluno, senhaHash, anoLetivo);
        //Adicionar ao banco de dados via DAO
        int idGerado = alunoDAO.inserirAluno(novoAluno);
        if (idGerado > 0) {
            novoAluno.setIdAluno(idGerado);
            return novoAluno;
        } else {
            throw new SQLException("Falha ao cadastrar aluno: não foi possível obter o ID gerado.");
        }
    }
    public Aluno buscarPorLogin(String login) throws SQLException {
        return alunoDAO.buscarAlunoPorLogin(login);
    }
    public Aluno buscarPorId(int id) throws SQLException {
        return alunoDAO.buscarAlunoPorId(id);
    }
    public List<Aluno> buscarTodos() throws SQLException {
        return alunoDAO.listarTodosAlunos();
    }
    private String hashSenha(String senhaTextoPuro) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(senhaTextoPuro.getBytes());
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}