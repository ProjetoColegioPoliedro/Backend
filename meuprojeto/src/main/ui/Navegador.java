package ui;

import service.QuestaoService;
import model.Questao;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Navegador {
    private QuestaoService questaoService;
    private Runnable ultimaTelaMenu;

    public Navegador() {
        this.questaoService = new QuestaoService();
    }

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
        this.ultimaTelaMenu = this::showTelaMenuEstudante;
        var telaMenuEst = new TelaMenuEstudante(() -> showTelaConfiguracoes(this::showTelaMenuEstudante), () -> showTelaTemaPerguntas(this::showTelaMenuEstudante), this::showTelaHistoricoEst);
        telaMenuEst.setVisible(true);
    }

    private void showTelaMenuAdmin(){
        this.ultimaTelaMenu = this::showTelaMenuAdmin;
        TelaMenuAdmin telaMenuAdmin = new TelaMenuAdmin(()->showTelaConfiguracoes(this::showTelaMenuAdmin), ()->showTelaTemaPerguntas(this::showTelaMenuAdmin), this::showTelaHistoricoAdmin, this::showTelaAreaRestrita);
        telaMenuAdmin.setVisible(true);
    }

    private void showTelaTemaPerguntas(Runnable voltarParaMenu) {
        TelaTemaPerguntas telaTemaP = new TelaTemaPerguntas(voltarParaMenu, () -> showTelaPartida(() -> showTelaTemaPerguntas(voltarParaMenu)));
        telaTemaP.setVisible(true);
    }

    /**
     * Exibe a Tela de Partida, buscando uma questão do banco de dados.
     * Define as ações a serem tomadas após o tempo esgotar, o jogador parar,
     * ou a resposta ser correta/incorreta.
     * @param voltarParaMenu Runnable para a ação de voltar ao menu (ou tela anterior), usado como base para reiniciar a partida.
     */
    private void showTelaPartida(Runnable voltarParaMenu) {
        Questao questaoParaPartida = questaoService.buscarQuestaoAleatoria();

        if (questaoParaPartida != null) {
            // Define o Runnable para iniciar uma nova partida (usado após a tela de solução/feedback)
            Runnable iniciarProximaPartida = voltarParaMenu;

            TelaPartida telaPartida = new TelaPartida(
                questaoParaPartida, // A questão completa a ser exibida

                
                // Ação para quando o tempo acabar na TelaPartida
                // Leva para TelaTempoEncerrado, que por sua vez levará para a solução, e a solução para a próxima partida.
                () -> showTelaTempoEncerrado(questaoParaPartida, iniciarProximaPartida), 
                
                //Ação para quando o botão "parar" for clicado. Leva para o menu incial.
                this.ultimaTelaMenu,
                
                // Ação para quando o jogador clica em "Parar" na TelaPartida
                // Leva diretamente para a TelaSolucao, e a solução para a próxima partida.
                () -> showTelaSolucao(questaoParaPartida, iniciarProximaPartida), 

                // Ação para quando a resposta é CORRETA:
                () -> {
                    // Após uma resposta correta, exibe a tela de resposta correta.
                    // O Runnable passado para showTelaRespostaCorreta define o que acontece DEPOIS: iniciar próxima partida.
                    this.showTelaRespostaCorreta(iniciarProximaPartida); 
                },

                // Ação para quando a resposta é INCORRETA:
                () -> {
                    // Após uma resposta incorreta, exibe a tela de resposta incorreta.
                    // Passa a Questao e a ação para ir para a solução, que levará à próxima partida.
                    this.showTelaRespostaIncorreta(questaoParaPartida, () -> showTelaSolucao(questaoParaPartida, iniciarProximaPartida)); 
                }
            );
            telaPartida.setVisible(true);
            telaPartida.iniciarCronometro();
        } else {
            JOptionPane.showMessageDialog(null, "Não foi possível carregar uma questão para a partida. Verifique o banco de dados e as questões cadastradas.", "Erro ao Iniciar Partida", JOptionPane.ERROR_MESSAGE);
            voltarParaMenu.run(); // Volta para a tela anterior (geralmente o menu)
        }
    }

    /**
     * Exibe a Tela de Tempo Encerrado.
     * @param questao A questão atual para a qual o tempo esgotou (útil para passar para a solução).
     * @param proximaAcao Runnable que define o que acontece após esta tela ser fechada/concluída (ex: ir para a solução).
     */
    private void showTelaTempoEncerrado(Questao questao, Runnable proximaAcao) { 
        // A TelaTempoEncerrado recebe 'proximaAcao' que é o Runnable para ir para a solução.
        TelaTempoEncerrado telaTempo = new TelaTempoEncerrado(questao, proximaAcao);
        telaTempo.setVisible(true);
    }

    /**
     * Exibe a Tela de Solução para uma questão específica.
     * @param questao A questão cuja solução será exibida.
     * @param proximaPartidaAction Runnable para a ação de iniciar uma nova partida (ou voltar ao menu).
     */
    private void showTelaSolucao(Questao questao, Runnable proximaPartidaAction){
        // A TelaSolucao receberá a 'Questao' para exibir seus detalhes e a 'proximaPartidaAction'
        // para o botão de "Jogar novamente" (ou "Próxima Questão").
        TelaSolucao telaSolucao = new TelaSolucao(questao, proximaPartidaAction); 
        telaSolucao.setVisible(true);
    }

    /**
     * Exibe a Tela de Resposta Correta.
     * @param proximaAcao Runnable que define o que acontece após esta tela ser fechada/concluída (ex: iniciar próxima partida).
     */
    private void showTelaRespostaCorreta(Runnable proximaAcao) { 
        // A TelaRespostaCorreta receberá o 'proximaAcao' e terá um botão para executá-lo.
        TelaRespostaCorreta telaRespCor = new TelaRespostaCorreta(proximaAcao);
        telaRespCor.setVisible(true);
    }

    /**
     * Exibe a Tela de Resposta Incorreta.
     * @param questao A questão que foi respondida incorretamente (para passar para a solução).
     * @param proximaAcao Runnable que define o que acontece após esta tela ser fechada/concluída (neste caso, ir para a solução).
     */
    private void showTelaRespostaIncorreta(Questao questao, Runnable proximaAcao) { 
        // A TelaRespostaIncorreta receberá a 'Questao' e o 'proximaAcao' (para exibir a solução)
        TelaRespostaIncorreta telaRespInc = new TelaRespostaIncorreta(questao, proximaAcao); 
        telaRespInc.setVisible(true);
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

    /**
     * Método público para iniciar a navegação do aplicativo.
     * Garante que a inicialização da UI ocorra na Event Dispatch Thread do Swing.
     */
    public void start(){
        SwingUtilities.invokeLater(this::showTelaInicial);
    }
}