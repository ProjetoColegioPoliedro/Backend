package main.classes;

import java.time.LocalDate; // Ou java.sql.Date

public class HistoricoJogo {
    private int idHistorico; // Corresponde a id_historico (PK)
    private LocalDate dataPartida; // Corresponde a data_partida (DATE)
    private int acertos; // Corresponde a acertos
    private int erros; // Corresponde a erros
    private String checkpointAlcancado; // Corresponde a checkpoint_alcancado
    private int pontuacaoTotal; // Corresponde a pontuacao_total
    private int idAluno; // Corresponde a id_aluno (FK)

    // Opcional: private Aluno aluno;

    public HistoricoJogo() {
    }

    public HistoricoJogo(int idHistorico, LocalDate dataPartida, int acertos, int erros, String checkpointAlcancado, int pontuacaoTotal, int idAluno) {
        this.idHistorico = idHistorico;
        this.dataPartida = dataPartida;
        this.acertos = acertos;
        this.erros = erros;
        this.checkpointAlcancado = checkpointAlcancado;
        this.pontuacaoTotal = pontuacaoTotal;
        this.idAluno = idAluno;
    }

    public HistoricoJogo(LocalDate dataPartida, int acertos, int erros, String checkpointAlcancado, int pontuacaoTotal, int idAluno) {
        this.dataPartida = dataPartida;
        this.acertos = acertos;
        this.erros = erros;
        this.checkpointAlcancado = checkpointAlcancado;
        this.pontuacaoTotal = pontuacaoTotal;
        this.idAluno = idAluno;
    }

    // Getters e Setters
    public int getIdHistorico() { return idHistorico; }
    public void setIdHistorico(int idHistorico) { this.idHistorico = idHistorico; }
    public LocalDate getDataPartida() { return dataPartida; }
    public void setDataPartida(LocalDate dataPartida) { this.dataPartida = dataPartida; }
    public int getAcertos() { return acertos; }
    public void setAcertos(int acertos) { this.acertos = acertos; }
    public int getErros() { return erros; }
    public void setErros(int erros) { this.erros = erros; }
    public String getCheckpointAlcancado() { return checkpointAlcancado; }
    public void setCheckpointAlcancado(String checkpointAlcancado) { this.checkpointAlcancado = checkpointAlcancado; }
    public int getPontuacaoTotal() { return pontuacaoTotal; }
    public void setPontuacaoTotal(int pontuacaoTotal) { this.pontuacaoTotal = pontuacaoTotal; }
    public int getIdAluno() { return idAluno; }
    public void setIdAluno(int idAluno) { this.idAluno = idAluno; }

    @Override
    public String toString() {
        return "HistoricoJogo [idHistorico=" + idHistorico + ", dataPartida=" + dataPartida + ", pontuacaoTotal=" + pontuacaoTotal + "]";
    }
}