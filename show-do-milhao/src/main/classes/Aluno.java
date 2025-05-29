package main.classes;

public class Aluno {
    private int idAluno; // Corresponde a id_aluno (PK)
    private String nome; // Corresponde a nome
    private String loginAluno; // Corresponde a login_aluno
    private String senha; // Corresponde a senha
    private int anoLetivo; // Corresponde a ano_letivo

    // Construtor padrão
    public Aluno() {
    }

    // Construtor completo (pode ser útil para criar objetos rapidamente)
    public Aluno(int idAluno, String nome, String loginAluno, String senha, int anoLetivo) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.loginAluno = loginAluno;
        this.senha = senha;
        this.anoLetivo = anoLetivo;
    }

    // Construtor sem ID (para novos cadastros onde o ID é gerado pelo banco)
    public Aluno(String nome, String loginAluno, String senha, int anoLetivo) {
        this.nome = nome;
        this.loginAluno = loginAluno;
        this.senha = senha;
        this.anoLetivo = anoLetivo;
    }

    // --- Getters e Setters ---
    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLoginAluno() {
        return loginAluno;
    }

    public void setLoginAluno(String loginAluno) {
        this.loginAluno = loginAluno;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    @Override
    public String toString() {
        return "Aluno [idAluno=" + idAluno + ", nome=" + nome + ", loginAluno=" + loginAluno + ", anoLetivo=" + anoLetivo + "]";
    }
}
