package service;

import dao.AlunoDAO;
import dao.ProfessorDAO;
import model.Aluno;
import model.Professor;

public class AuthService {

    private AlunoDAO alunoDAO;
    private ProfessorDAO professorDAO;

    public AuthService() {
        this.alunoDAO = new AlunoDAO();
        this.professorDAO = new ProfessorDAO();
    }

    /**
     * Autentica um usuário e retorna seu tipo (0 para Aluno, 1 para Professor/Admin, -1 para falha).
     * @param login O login do usuário.
     * @param senha A senha do usuário.
     * @return 0 se for Aluno, 1 se for Professor/Admin, -1 se a autenticação falhar.
     */
    public int autenticarUsuario(String login, String senha) {
        // Tenta autenticar como Aluno
        Aluno aluno = alunoDAO.buscarAlunoPorLoginESenha(login, senha);
        if (aluno != null) {
            System.out.println("DEBUG: Aluno autenticado: " + aluno.getNome() + " (Login: " + aluno.getLoginAluno() + ")");
            return 0; // Tipo 0 para Aluno
        }

        // Se não for Aluno, tenta autenticar como Professor/Admin
        Professor professor = professorDAO.buscarProfessorPorLoginESenha(login, senha);
        if (professor != null) {
            System.out.println("DEBUG: Professor/Admin autenticado: " + professor.getNome() + " (Login: " + professor.getLoginProfessor() + ")");
            return 1; // Tipo 1 para Professor/Admin
        }

        System.out.println("DEBUG: Falha na autenticação para login: " + login);
        return -1; // Falha na autenticação
    }
}