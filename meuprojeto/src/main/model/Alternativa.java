package model;

public class Alternativa {
    private int idAlternativa;
    private String texto;
    private boolean correta; 

    public Alternativa() {
    }

    public Alternativa(int idAlternativa, String texto, boolean correta) {
        this.idAlternativa = idAlternativa;
        this.texto = texto;
        this.correta = correta;}
    public Alternativa(int idAlternativa, String texto) {
        this.idAlternativa = idAlternativa;
        this.texto = texto;
        this.correta = false;}

    public Alternativa(String texto, boolean correta) {
        this.texto = texto;
        this.correta = correta;
        this.idAlternativa = 0;}
    public int getIdAlternativa() { return idAlternativa; }
    public void setIdAlternativa(int idAlternativa) { this.idAlternativa = idAlternativa; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public boolean isCorreta() { return correta; }
    public void setCorreta(boolean correta) { this.correta = correta; }

    @Override
    public String toString() {
        return "Alternativa [idAlternativa=" + idAlternativa + ", texto='" + texto + "', correta=" + correta + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alternativa that = (Alternativa) o;
        if (idAlternativa != 0 && that.idAlternativa != 0) {
            return idAlternativa == that.idAlternativa;
        } else {
            return texto != null ? texto.equals(that.texto) : that.texto == null;
        }
    }

    @Override
    public int hashCode() {
        return idAlternativa != 0 ? Integer.hashCode(idAlternativa) : (texto != null ? texto.hashCode() : 0);
    }
}