package model;

public class Alternativa {
    private int idAlternativa;
    private String texto;
    private boolean correta; // Atributo para indicar se a alternativa é a correta

    public Alternativa() {
    }

    // Construtor completo para alternativas existentes (com ID e flag de correta)
    public Alternativa(int idAlternativa, String texto, boolean correta) {
        this.idAlternativa = idAlternativa;
        this.texto = texto;
        this.correta = correta;
    }

    // Construtor para carregar alternativas que só têm ID e texto (sem 'correta' no SELECT inicial da tabela 'alternativa')
    // Este é o construtor que o AlternativaDAO.buscarAlternativaPorId e .listarTodasAlternativas precisam.
    public Alternativa(int idAlternativa, String texto) {
        this.idAlternativa = idAlternativa;
        this.texto = texto;
        this.correta = false; // Por padrão, ao carregar da tabela 'alternativa', não sabemos se é a correta
                              // A informação 'correta' é obtida da tabela 'questao_alternativa'
    }

    // Construtor para alternativas novas (sem ID, mas com flag de correta)
    // Este é o construtor que a TelaAdicionaPergunta está usando para novas alternativas.
    public Alternativa(String texto, boolean correta) {
        this.texto = texto;
        this.correta = correta;
        this.idAlternativa = 0; // Inicializa com 0 para indicar que não tem ID ainda
    }

    // --- Getters e Setters ---
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

        // Compara pelo ID se ambos tiverem um ID válido, caso contrário, compara pelo texto.
        // A flag 'correta' não é usada para igualdade, apenas para o estado da alternativa.
        if (idAlternativa != 0 && that.idAlternativa != 0) {
            return idAlternativa == that.idAlternativa;
        } else {
            return texto != null ? texto.equals(that.texto) : that.texto == null;
        }
    }

    @Override
    public int hashCode() {
        // Gera um hashcode baseado no ID, se válido; caso contrário, baseado no texto.
        return idAlternativa != 0 ? Integer.hashCode(idAlternativa) : (texto != null ? texto.hashCode() : 0);
    }
}