package model;

public class Questao {

    private int idQuestao;          // Corresponde a id_questao (PK no banco)
    private String enunciado;       // Corresponde a enunciado (a pergunta em si)
    private String explicacaoErro;  // Corresponde a explicacao_erro (o feedback ao errar)
    private int anoLetivo;          // Corresponde a ano_letivo (a qual ano se destina a questão)
    private int idNivel;            // Corresponde a id_nivel (FK para Nível de Dificuldade)
    private int idMateria;          // Corresponde a id_materia (FK para Matéria)
    private String ajuda;           // Corresponde a ajuda (texto de dica)

    public Questao() {
    }

    public Questao(int idQuestao, String enunciado, String explicacaoErro, int anoLetivo, int idNivel, int idMateria, String ajuda) {
        this.idQuestao = idQuestao;
        this.enunciado = enunciado;
        this.explicacaoErro = explicacaoErro;
        this.anoLetivo = anoLetivo;
        this.idNivel = idNivel;
        this.idMateria = idMateria;
        this.ajuda = ajuda;
    }

    // Construtor sem ID para questões novas
    public Questao(String enunciado, String explicacaoErro, int anoLetivo, int idNivel, int idMateria, String ajuda) {
        this.enunciado = enunciado;
        this.explicacaoErro = explicacaoErro;
        this.anoLetivo = anoLetivo;
        this.idNivel = idNivel;
        this.idMateria = idMateria;
        this.ajuda = ajuda;
    }

    // Getters e Setters

    public int getIdQuestao() { return idQuestao; }
    public void setIdQuestao(int idQuestao) { this.idQuestao = idQuestao; }
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getExplicacaoErro() { return explicacaoErro; }
    public void setExplicacaoErro(String explicacaoErro) { this.explicacaoErro = explicacaoErro; }
    public int getAnoLetivo() { return anoLetivo; }
    public void setAnoLetivo(int anoLetivo) { this.anoLetivo = anoLetivo; }
    public int getIdNivel() { return idNivel; }
    public void setIdNivel(int idNivel) { this.idNivel = idNivel; }
    public int getIdMateria() { return idMateria; }
    public void setIdMateria(int idMateria) { this.idMateria = idMateria; }
    public String getAjuda() { return ajuda; }
    public void setAjuda(String ajuda) { this.ajuda = ajuda; }
}
