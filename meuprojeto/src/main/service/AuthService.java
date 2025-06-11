package service;

import java.sql.SQLException;
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
    public int autenticarUsuario(String login, String senha) {
    try {
        // Tenta autenticar como Aluno
        Aluno aluno = alunoDAO.buscarAlunoPorLoginESenha(login, senha);
        if (aluno != null) {
            System.out.println("DEBUG: Aluno autenticado: " + aluno.getNome() + " (Login: " + aluno.getLoginAluno() + ")");
            return 0; 
        }
        // Se não for Aluno, tenta autenticar como Professor/Admin
        Professor professor = professorDAO.buscarProfessorPorLoginESenha(login, senha);
        if (professor != null) {
            System.out.println("DEBUG: Professor/Admin autenticado: " + professor.getNome() + " (Login: " + professor.getLoginProfessor() + ")");
            return 1; // Tipo 1 para Professor/Admin
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    System.out.println("DEBUG: Falha na autenticação para login: " + login);
    return -1; // Falha na autenticação
}
}