package ui;

import service.QuestaoService;
import model.Questao;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Navegador {
    private QuestaoService questaoService;
    // O 'ultimaTelaMenu' é um atributo para guardar a referência do último menu visitado,
    // útil para ações de "voltar ao menu" após certas operações (como login).
    private Runnable ultimaTelaMenu; 

    private int questaoAtual = 1;
    private final int TOTAL_QUESTOES = 13;

    public Navegador() {
        this.questaoService = new QuestaoService();
    }

    // Métodos para exibir as diferentes telas da aplicação
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
        // Atribui a tela atual ao 'ultimaTelaMenu'
        this.ultimaTelaMenu = this::showTelaMenuEstudante; 
        var telaMenuEst = new TelaMenuEstudante(() -> showTelaConfiguracoes(this::showTelaMenuEstudante), () -> showTelaTemaPerguntas(this::showTelaMenuEstudante), this::showTelaHistoricoEst);
        telaMenuEst.setVisible(true);
    }

    private void showTelaMenuAdmin(){
        // Atribui a tela atual ao 'ultimaTelaMenu'
        this.ultimaTelaMenu = this::showTelaMenuAdmin; 
        TelaMenuAdmin telaMenuAdmin = new TelaMenuAdmin(()->showTelaConfiguracoes(this::showTelaMenuAdmin), ()->showTelaTemaPerguntas(this::showTelaMenuAdmin), this::showTelaHistoricoAdmin, this::showTelaAreaRestrita);
        telaMenuAdmin.setVisible(true);
    }

    private void showTelaTemaPerguntas(Runnable voltarParaMenu) {
        // O segundo Runnable (partida) chama showTelaPartida. O 'voltarParaMenu' neste contexto é showTelaTemaPerguntas.
        TelaTemaPerguntas telaTemaP = new TelaTemaPerguntas(voltarParaMenu, () -> showTelaPartida(() -> showTelaTemaPerguntas(voltarParaMenu)));
        telaTemaP.setVisible(true);
    }

    private void resetarPartida(){
        this.questaoAtual = 1;
    }

    /**
     * Exibe a Tela de Partida, buscando uma questão do banco de dados.
     * Define as ações a serem tomadas quando o jogador parar, ou a resposta for correta/incorreta.
     * (O tempo esgotado foi removido com o cronômetro, então o parâmetro 'acabouTempo' não é mais usado na TelaPartida).
     * @param voltarParaMenu Runnable para a ação de voltar ao menu (ou tela anterior), usado como base para reiniciar a partida.
     */
    private void showTelaPartida(Runnable voltarParaMenu) {
        if(questaoAtual > TOTAL_QUESTOES){
            questaoAtual = 1;
        }
        // 1. Tenta buscar uma questão aleatória do banco de dados através do QuestaoService
        Questao questaoParaPartida = questaoService.buscarQuestaoAleatoria();

        // 2. Verifica se uma questão foi encontrada
        if (questaoParaPartida != null) {
            // mostra no terminal o numero de rodadas
            System.out.println("Rodada: " + questaoAtual); 
    
            boolean penultimaQuestao = (questaoAtual == TOTAL_QUESTOES - 1);
            boolean ultimaQuestão = (questaoAtual == TOTAL_QUESTOES);

            // Runnable para contar a quantidade de questões da partida
            Runnable contaQuestao = () -> {
                // Adiciona um a cada rodada que passa
                questaoAtual++;

                //"if" para caso não seja última questão (inicia outra rodada), "else" para encerrar a partida
                if(!ultimaQuestão){
                    showTelaPartida(voltarParaMenu); 
                } else {
                    System.out.println("Fim de jogo!");
                    voltarParaMenu.run();
                }
            };
            
            // Esta ação leva à tela de solução da questão atual, e de lá o jogador pode ir para a próxima rodada.
            Runnable acaoRespostaIncorreta = () -> showTelaSolucao(questaoParaPartida, contaQuestao);

            // Instancia a TelaPartida com os 4 parâmetros esperados (Questao, acaoAoEncerrarQuestao, respostaCorretaAction, respostaIncorretaAction)
            TelaPartida telaPartida = new TelaPartida(
                questaoParaPartida, // 1º parâmetro: Objeto Questao a ser exibido na tela

                contaQuestao, // 2º parâmetro: Runnable para ação ao encerrar a questão (usado pelo botão "Pular")

                // 3º parâmetro: Runnable respostaCorretaAction (ação a ser executada após uma resposta correta)
                () -> {
                    // Após uma resposta correta, exibe a tela de resposta correta.
                    // O Runnable passado para showTelaRespostaCorreta define o que acontece DEPOIS: iniciar próxima partida.
                    this.showTelaRespostaCorreta(contaQuestao, penultimaQuestao, ultimaQuestão); 
                },

                // 4º parâmetro: Runnable respostaIncorretaAction (ação a ser executada após uma resposta incorreta)
                () -> {
                    // Após uma resposta incorreta, exibe a tela de resposta incorreta.
                    // O Runnable passado para showTelaRespostaIncorreta levará à tela de solução,
                    // e da tela de solução para a próxima partida.
                    this.showTelaRespostaIncorreta(questaoParaPartida, acaoRespostaIncorreta, penultimaQuestao, ultimaQuestão); 
                },

                // 5º parâmetro: Runnable configs (inicia a tela de configurações)
                () -> showTelaConfiguracoes(() -> showTelaPartida(voltarParaMenu)),

                // 6º parâmetro: Runnable pararJogo (ação para quando o botão "parar" for clicado. Leva para o menu incial e reseta a partida).
                () -> {
                    resetarPartida();
                    this.ultimaTelaMenu.run();
                } 
            );
            telaPartida.setVisible(true); // Torna a tela de partida visível
            // Não há mais chamada para telaPartida.iniciarCronometro(); aqui, pois o cronômetro foi removido.
        } else {
            // Se nenhuma questão for carregada do banco de dados (ex: banco vazio, erro de conexão)
            JOptionPane.showMessageDialog(null, "Não foi possível carregar uma questão para a partida. Verifique o banco de dados e as questões cadastradas.", "Erro ao Iniciar Partida", JOptionPane.ERROR_MESSAGE);
            voltarParaMenu.run(); // Retorna para a tela anterior (geralmente o menu ou seleção de tema)
        }
    }

    /**
     * Exibe a Tela de Tempo Encerrado.
     * (Este método ainda existe, mas a TelaPartida não o chama mais diretamente sem o cronômetro).
     * @param questao A questão atual para a qual o tempo esgotou (útil para passar para a solução).
     * @param proximaAcao Runnable que define o que acontece após esta tela ser fechada/concluída (ex: ir para a solução).
     */

    /**
     * Exibe a Tela de Solução para uma questão específica.
     * @param questao A questão cuja solução será exibida.
     * @param proximaPartidaAction Runnable para a ação de iniciar uma nova partida (ou voltar ao menu).
     */
    private void showTelaSolucao(Questao questao, Runnable contaQuestao){
        TelaSolucao telaSolucao = new TelaSolucao(questao, contaQuestao); 
        telaSolucao.setVisible(true);
    }

    /**
     * Exibe a Tela de Resposta Correta.
     * @param proximaAcao Runnable que define o que acontece após esta tela ser fechada/concluída (ex: iniciar próxima partida).
     */
    private void showTelaRespostaCorreta(Runnable proximaAcao, boolean penultimaQuestao, boolean ultimaQuestão) {
        TelaRespostaCorreta telaRespCor = new TelaRespostaCorreta(proximaAcao, penultimaQuestao, ultimaQuestão);
        telaRespCor.setVisible(true);
    }

    /**
     * Exibe a Tela de Resposta Incorreta.
     * @param questao A questão que foi respondida incorretamente (para passar para a solução).
     * @param proximaAcao Runnable que define o que acontece após esta tela ser fechada/concluída (neste caso, ir para a solução).
     */
    private void showTelaRespostaIncorreta(Questao questao, Runnable proximaAcao, boolean penultimaQuestao, boolean ultimaQuestãoo) { 
        TelaRespostaIncorreta telaRespInc = new TelaRespostaIncorreta(questao, proximaAcao, penultimaQuestao, ultimaQuestãoo); 
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
                this::showTelaAdicionaPergunta, this::showTelaConsultaRanking, this::showTelaCadastro);
        telaAreaRes.setVisible(true);
    }

    private void showTelaAdicionaPergunta(){
    TelaAdicionaPergunta telaAddPerg = new TelaAdicionaPergunta(this::showTelaAreaRestrita, this.questaoService);
    telaAddPerg.setVisible(true);
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