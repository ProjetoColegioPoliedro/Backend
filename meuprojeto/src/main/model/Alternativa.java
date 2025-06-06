package model;

public class Alternativa {
    private int idAlternativa;
    private String texto;

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

  
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Se forem o mesmo objeto em memória, são iguais
        if (o == null || getClass() != o.getClass()) return false; // Se nulo ou de classe diferente, não são iguais

        Alternativa that = (Alternativa) o; // Faz o cast para Alternativa

        // Compara pelo ID. Se IDs são iguais, as alternativas são as mesmas.
        // Se idAlternativa for 0 (alternativa nova ou não persistida), compare pelo texto.
        if (idAlternativa != 0 && that.idAlternativa != 0) {
            return idAlternativa == that.idAlternativa;
        } else {
            // Caso um dos IDs seja 0 (não persistido ou mock), compare pelo texto
            return texto != null ? texto.equals(that.texto) : that.texto == null;
        }
    }

    @Override
    public int hashCode() {
        // Gera um hashcode baseado no ID. Se o ID for 0, use o texto (para consistência com equals)
        return idAlternativa != 0 ? Integer.hashCode(idAlternativa) : (texto != null ? texto.hashCode() : 0);
    }
}