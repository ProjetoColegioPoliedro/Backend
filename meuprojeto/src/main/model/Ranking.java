package model;

import java.time.LocalDateTime;

public class Ranking {
    private int idRanking; // Corresponde a id_ranking (PK)
    private int idAluno; // Corresponde a id_aluno (FK)
    private int pontuacao; // Corresponde a pontuacao
    private LocalDateTime ultimaAtualizacao; // Corresponde a ultima_atualizacao (TIMESTAMP)

    public Ranking() {
    }

    public Ranking(int idRanking, int idAluno, int pontuacao, LocalDateTime ultimaAtualizacao) {
        this.idRanking = idRanking;
        this.idAluno = idAluno;
        this.pontuacao = pontuacao;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public Ranking(int idAluno, int pontuacao, LocalDateTime ultimaAtualizacao) {
        this.idAluno = idAluno;
        this.pontuacao = pontuacao;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    // Getters e Setters
    public int getIdRanking() { return idRanking; }
    public void setIdRanking(int idRanking) { this.idRanking = idRanking; }
    public int getIdAluno() { return idAluno; }
    public void setIdAluno(int idAluno) { this.idAluno = idAluno; }
    public int getPontuacao() { return pontuacao; }
    public void setPontuacao(int pontuacao) { this.pontuacao = pontuacao; }
    public LocalDateTime getUltimaAtualizacao() { return ultimaAtualizacao; }
    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) { this.ultimaAtualizacao = ultimaAtualizacao; }

    @Override
    public String toString() {
        return "Ranking [idRanking=" + idRanking + ", idAluno=" + idAluno + ", pontuacao=" + pontuacao + ", ultimaAtualizacao=" + ultimaAtualizacao + "]";
    }
}