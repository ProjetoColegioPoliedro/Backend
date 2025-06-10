package model;

import java.util.List;
import java.util.ArrayList; 
import java.util.Collections; 


public class Questao {

    /**
     * Verifica se o texto de uma alternativa fornecida corresponde ao da alternativa correta.
     * @param textoAlternativa O texto da alternativa selecionada pelo usuário.
     * @return true se for a alternativa correta, false caso contrário.
     */
    public boolean isAlternativaCorreta(String textoAlternativa) {
        if (alternativaCorreta == null || textoAlternativa == null) {
            return false;
        }
        return alternativaCorreta.getTexto().trim().equals(textoAlternativa.trim());
    }

    private int idQuestao;
    private String enunciado;
    private String explicacaoErro;
    private int anoLetivo;
    private int idNivel;
    private int idMateria;
   
    private List<Alternativa> alternativas; // Lista de todas as alternativas para esta questão
    private Alternativa alternativaCorreta; // A alternativa correta

    public Questao() {
    }

    public Questao(int idQuestao, String enunciado, String explicacaoErro, int anoLetivo, int idNivel, int idMateria) {
        this.idQuestao = idQuestao;
        this.enunciado = enunciado;
        this.explicacaoErro = explicacaoErro;
        this.anoLetivo = anoLetivo;
        this.idNivel = idNivel;
        this.idMateria = idMateria;
    }

    // Construtor sem ID para questões novas
    public Questao(String enunciado, String explicacaoErro, int anoLetivo, int idNivel, int idMateria) {
        this.enunciado = enunciado;
        this.explicacaoErro = explicacaoErro;
        this.anoLetivo = anoLetivo;
        this.idNivel = idNivel;
        this.idMateria = idMateria;
        
    }

    // Getters e Setters existentes
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

    
    public List<Alternativa> getAlternativas() { return alternativas; }
    public void setAlternativas(List<Alternativa> alternativas) { this.alternativas = alternativas; }
    public Alternativa getAlternativaCorreta() { return alternativaCorreta; }
    public void setAlternativaCorreta(Alternativa alternativaCorreta) { this.alternativaCorreta = alternativaCorreta; }

    /**
     * Retorna o texto da alternativa correta.
     * @return O texto da alternativa correta, ou null se não definida.
     */
    public String getTextoAlternativaCorreta() {
        return alternativaCorreta != null ? alternativaCorreta.getTexto() : null;
    }

    /**
     * Retorna uma lista dos textos das alternativas para exibição (útil para a UI).
     * Pode embaralhar a ordem se desejar.
     * @param embaralhar Se true, embaralha a ordem das alternativas.
     * @return Uma lista de Strings com o texto de cada alternativa.
     */
    public List<String> getTextosAlternativas(boolean embaralhar) {
        List<String> textos = new ArrayList<>();
        if (alternativas != null) {
            for (Alternativa alt : alternativas) {
                textos.add(alt.getTexto());
            }
            if (embaralhar) {
                Collections.shuffle(textos);
            }
        }
        return textos;
    }
}