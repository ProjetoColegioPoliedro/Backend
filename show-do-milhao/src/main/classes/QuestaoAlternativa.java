package main.classes;

public class QuestaoAlternativa {
    private int idQa; // Corresponde a id_qa (PK)
    private int idQuestao; // Corresponde a id_questao (FK)
    private int idAlternativa; // Corresponde a id_alternativa (FK)
    private boolean correta; // Corresponde a correta

    // Opcional: Você pode adicionar os objetos Questao e Alternativa aqui para facilitar
    // private Questao questao;
    // private Alternativa alternativa;

    public QuestaoAlternativa() {
    }

    public QuestaoAlternativa(int idQa, int idQuestao, int idAlternativa, boolean correta) {
        this.idQa = idQa;
        this.idQuestao = idQuestao;
        this.idAlternativa = idAlternativa;
        this.correta = correta;
    }

    public QuestaoAlternativa(int idQuestao, int idAlternativa, boolean correta) {
        this.idQuestao = idQuestao;
        this.idAlternativa = idAlternativa;
        this.correta = correta;
    }

    // Getters e Setters
    public int getIdQa() { return idQa; }
    public void setIdQa(int idQa) { this.idQa = idQa; }
    public int getIdQuestao() { return idQuestao; }
    public void setIdQuestao(int idQuestao) { this.idQuestao = idQuestao; }
    public int getIdAlternativa() { return idAlternativa; }
    public void setIdAlternativa(int idAlternativa) { this.idAlternativa = idAlternativa; }
    public boolean isCorreta() { return correta; }
    public void setCorreta(boolean correta) { this.correta = correta; }

    @Override
    public String toString() {
        return "QuestaoAlternativa [idQa=" + idQa + ", idQuestao=" + idQuestao + ", idAlternativa=" + idAlternativa + ", correta=" + correta + "]";
    }
}