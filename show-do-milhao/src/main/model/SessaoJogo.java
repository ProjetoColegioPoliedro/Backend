import java.time.LocalDateTime; 

public class SessaoJogo {
    private int idSessao; // Corresponde a id_sessao (PK)
    private LocalDateTime dataInicio; // Corresponde a data_inicio
    private LocalDateTime dataFim; // Corresponde a data_fim
    private boolean modoPratica; // Corresponde a modo_pratica
    private int pontuacaoTotal; // Corresponde a pontuacao_total
    private int idAluno; // Corresponde a id_aluno (FK)

    public SessaoJogo() {
    }

    public SessaoJogo(int idSessao, LocalDateTime dataInicio, LocalDateTime dataFim, boolean modoPratica, int pontuacaoTotal, int idAluno) {
        this.idSessao = idSessao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.modoPratica = modoPratica;
        this.pontuacaoTotal = pontuacaoTotal;
        this.idAluno = idAluno;
    }

    public SessaoJogo(LocalDateTime dataInicio, LocalDateTime dataFim, boolean modoPratica, int pontuacaoTotal, int idAluno) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.modoPratica = modoPratica;
        this.pontuacaoTotal = pontuacaoTotal;
        this.idAluno = idAluno;
    }

    // Getters e Setters
    public int getIdSessao() { return idSessao; }
    public void setIdSessao(int idSessao) { this.idSessao = idSessao; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public boolean isModoPratica() { return modoPratica; } // Convenção para boolean: is<NomeAtributo>
    public void setModoPratica(boolean modoPratica) { this.modoPratica = modoPratica; }
    public int getPontuacaoTotal() { return pontuacaoTotal; }
    public void setPontuacaoTotal(int pontuacaoTotal) { this.pontuacaoTotal = pontuacaoTotal; }
    public int getIdAluno() { return idAluno; }
    public void setIdAluno(int idAluno) { this.idAluno = idAluno; }

    @Override
    public String toString() {
        return "SessaoJogo [idSessao=" + idSessao + ", dataInicio=" + dataInicio + ", pontuacaoTotal=" + pontuacaoTotal + "]";
    }
}