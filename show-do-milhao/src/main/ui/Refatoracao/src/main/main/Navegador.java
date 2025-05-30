package main.main;

import main.view.*;

public class Navegador {
    private void showTelaInicial(){
        var telaInicial = new TelaInicial(this::showTelaLogin, () -> showTelaConfiguracoes(this::showTelaInicial));
        telaInicial.setVisible(true);
    }

    private void showTelaLogin(){
        var telaLogin = new TelaLogin(this::showTelaInicial, this::showTelaRecuperacaoSenha, () -> showTelaConfiguracoes(this::showTelaLogin), this::showTelaMenuAdmin, this::showTelaMenuEstudante);
        telaLogin.setVisible(true);
    }

    private void showTelaConfiguracoes(Runnable voltaTelaAnterior){
        var telaConfig = new TelaConfiguracoes(this::showTelaInicial, voltaTelaAnterior);
        telaConfig.setVisible(true);
    }

    private void showTelaRecuperacaoSenha(){
        var telaRecSenha = new TelaRecuperacaoSenha(this::showTelaLogin, () -> showTelaConfiguracoes(this::showTelaRecuperacaoSenha));
        telaRecSenha.setVisible(true);
    }

    private void showTelaMenuEstudante(){
        var telaMenuEst = new TelaMenuEstudante(() -> showTelaConfiguracoes(this::showTelaMenuEstudante), () -> showTelaTemaPerguntas(this::showTelaMenuEstudante), this::showTelaHistoricoEst);
        telaMenuEst.setVisible(true);
    }

    private void showTelaMenuAdmin(){
        TelaMenuAdmin telaMenuAdmin = new TelaMenuAdmin(()->showTelaConfiguracoes(this::showTelaMenuAdmin), ()->showTelaTemaPerguntas(this::showTelaMenuAdmin), this::showTelaHistoricoAdmin, this::showTelaAreaRestrita);
        telaMenuAdmin.setVisible(true);
    }

    private void showTelaTemaPerguntas(Runnable voltarParaMenu) {
        TelaTemaPerguntas telaTemaP = new TelaTemaPerguntas(voltarParaMenu, () -> showTelaPartida(voltarParaMenu));
        telaTemaP.setVisible(true);
    }

    private void showTelaPartida(Runnable voltarParaMenu) {
        TelaPartida telaPartida = new TelaPartida(() -> showTelaTempoEncerrado(voltarParaMenu), () -> showTelaSolucao(voltarParaMenu));
        telaPartida.setVisible(true);
    }

    private void showTelaTempoEncerrado(Runnable voltarParaMenu) {
        TelaTempoEncerrado telaTempo = new TelaTempoEncerrado(() -> showTelaSolucao(voltarParaMenu));
        telaTempo.setVisible(true);
    }

    private void showTelaSolucao(Runnable voltarParaMenu){
        TelaSolucao telaSolucao = new TelaSolucao(voltarParaMenu);
        telaSolucao.setVisible(true);
    }

    private void showTelaHistoricoEst(){
        TelaHistorico telaHistEst = new TelaHistorico(this::showTelaMenuEstudante, () -> showTelaConfiguracoes(this::showTelaHistoricoEst));
        telaHistEst.setVisible(true);
    }

    private void showTelaHistoricoAdmin(){
        TelaHistAdmin telaHistAdmin = new TelaHistAdmin(this::showTelaMenuAdmin, () -> showTelaConfiguracoes(this::showTelaHistoricoAdmin));
        telaHistAdmin.setVisible(true);
    }

    private void showTelaAreaRestrita(){
        TelaAreaRestrita telaAreaRes = new TelaAreaRestrita(this::showTelaMenuAdmin, () -> showTelaConfiguracoes(this::showTelaAreaRestrita),
                this::showTelaAdicionaPergunta, this::showTelaEditaPergunta, this::showTelaConsultaRanking, this::showTelaCadastro);
        telaAreaRes.setVisible(true);
    }

    private void showTelaAdicionaPergunta(){
        TelaAdicionaPergunta telaAddPerg = new TelaAdicionaPergunta(this::showTelaAreaRestrita);
        telaAddPerg.setVisible(true);
    }

    private void showTelaEditaPergunta(){
        TelaEditaPergunta telaEdPerg = new TelaEditaPergunta(this::showTelaAreaRestrita);
        telaEdPerg.setVisible(true);
    }

    private void showTelaConsultaRanking(){
        TelaConsultaRanking telaConsRank = new TelaConsultaRanking(this::showTelaAreaRestrita);
        telaConsRank.setVisible(true);
    }

    private void showTelaCadastro(){
        TelaCadastro telaCad = new TelaCadastro(this::showTelaAreaRestrita);
        telaCad.setVisible(true);
    }

    private void showTelaRespostaIncorreta(Runnable voltarParaMenu){
        TelaRespostaIncorreta telaRespInc = new TelaRespostaIncorreta(() -> showTelaSolucao(voltarParaMenu));
        telaRespInc.setVisible(true);
    }

    private void showTelaRespostaCorreta(){
        TelaRespostaCorreta telaRespCor = new TelaRespostaCorreta();
        telaRespCor.setVisible(true);
    }

    public void start(){
        this.showTelaInicial();
    }
}


