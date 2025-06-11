package model;

public class Professor { // Ou Administrador
    private int idProfessor; // Corresponde a id_professor (PK)
    private String nome; // Corresponde a nome
    private String loginProfessor; // Corresponde a login_professor
    private String senha; // Corresponde a senha

    // Construtor padrão
    public Professor() {
    }

    // Construtor completo
    public Professor(int idProfessor, String nome, String loginProfessor, String senha) {
        this.idProfessor = idProfessor;
        this.nome = nome;
        this.loginProfessor = loginProfessor;
        this.senha = senha;
    }

    // Construtor sem ID
    public Professor(String nome, String loginProfessor, String senha) {
        this.nome = nome;
        this.loginProfessor = loginProfessor;
        this.senha = senha;
    }

    // Getters e Setters 
    public int getIdProfessor() {
        return idProfessor;}

    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;}

    public String getNome() {
        return nome;}

    public void setNome(String nome) {
        this.nome = nome;}

    public String getLoginProfessor() {
        return loginProfessor;}

    public void setLoginProfessor(String loginProfessor) {
        this.loginProfessor = loginProfessor;}

    public String getSenha() {
        return senha;}

    public void setSenha(String senha) {
        this.senha = senha;}

    @Override
    public String toString() {
        return "Professor [idProfessor=" + idProfessor + ", nome=" + nome + ", loginProfessor=" + loginProfessor + "]";
    }
}