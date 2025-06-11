package model;

public class Materia {
    private int idMateria; // Corresponde a id_materia (PK)
    private String nome; // Corresponde a nome

    public Materia() {}

    public Materia(int idMateria, String nome) {
        this.idMateria = idMateria;
        this.nome = nome;
    }

    public Materia(String nome) {
        this.nome = nome;
    }

    // Getters e Setters
    public int getIdMateria() { return idMateria; }
    public void setIdMateria(int idMateria) { this.idMateria = idMateria; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() {
        return "Materia [idMateria=" + idMateria + ", nome='" + nome + "']";
    }
}