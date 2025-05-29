package main.classes;

public class NivelDificuldade {
    private int idNivel; // Corresponde a id_nivel (PK)
    private String descricao; // Corresponde a descricao

    public NivelDificuldade() {
    }

    public NivelDificuldade(int idNivel, String descricao) {
        this.idNivel = idNivel;
        this.descricao = descricao;
    }

    public NivelDificuldade(String descricao) {
        this.descricao = descricao;
    }

    // Getters e Setters
    public int getIdNivel() { return idNivel; }
    public void setIdNivel(int idNivel) { this.idNivel = idNivel; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        return "NivelDificuldade [idNivel=" + idNivel + ", descricao='" + descricao + "']";
    }
}
