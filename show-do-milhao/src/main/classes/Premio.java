package main.classes;

public class Premio {
    private int idPremio; // Corresponde a id_premio (PK)
    private String descricao; // Corresponde a descricao
    private String faixasAcertos; // Corresponde a faixas_acertos (se for um texto, ou ajuste para tipo numérico se for um range)
    private double valorSimbolico; // Corresponde a valor_simbolico

    public Premio() {
    }

    public Premio(int idPremio, String descricao, String faixasAcertos, double valorSimbolico) {
        this.idPremio = idPremio;
        this.descricao = descricao;
        this.faixasAcertos = faixasAcertos;
        this.valorSimbolico = valorSimbolico;
    }

    public Premio(String descricao, String faixasAcertos, double valorSimbolico) {
        this.descricao = descricao;
        this.faixasAcertos = faixasAcertos;
        this.valorSimbolico = valorSimbolico;
    }

    // Getters e Setters
    public int getIdPremio() { return idPremio; }
    public void setIdPremio(int idPremio) { this.idPremio = idPremio; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getFaixasAcertos() { return faixasAcertos; }
    public void setFaixasAcertos(String faixasAcertos) { this.faixasAcertos = faixasAcertos; }
    public double getValorSimbolico() { return valorSimbolico; }
    public void setValorSimbolico(double valorSimbolico) { this.valorSimbolico = valorSimbolico; }

    @Override
    public String toString() {
        return "Premio [idPremio=" + idPremio + ", descricao='" + descricao + "', faixasAcertos='" + faixasAcertos + "']";
    }
}