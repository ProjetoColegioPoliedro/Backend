package main.classes;

public class Alternativa {
    private int idAlternativa; // Corresponde a id_alternativa (PK)
    private String texto; // Corresponde a texto

    public Alternativa() {
    }

    public Alternativa(int idAlternativa, String texto) {
        this.idAlternativa = idAlternativa;
        this.texto = texto;
    }

    public Alternativa(String texto) {
        this.texto = texto;
    }

    // Getters e Setters
    public int getIdAlternativa() { return idAlternativa; }
    public void setIdAlternativa(int idAlternativa) { this.idAlternativa = idAlternativa; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    @Override
    public String toString() {
        return "Alternativa [idAlternativa=" + idAlternativa + ", texto='" + texto + "']";
    }
}