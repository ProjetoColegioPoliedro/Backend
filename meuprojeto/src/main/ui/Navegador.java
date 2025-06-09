package ui;

import service.QuestaoService;
import model.Questao;
import model.SessaoJogo; // Importe o modelo de sessão para o histórico
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.List;

public class Navegador {
    private QuestaoService questaoService;
    private Runnable ultimaTelaMenu;

    // --- VARIÁVEIS DE ESTADO DA PARTIDA ---
    private List<Questao> questoesDaPartidaAtual;
    private int indiceQuestaoAtual;
    private int pontuacaoAtual;
    private TelaPartida telaDePartidaUnica;
    private final int MAX_QUESTOES = 13;
    private LocalDateTime inicioDaSessao;

    public Navegador() {
        this.questaoService = new QuestaoService();
    }
    
    public void start(){
        SwingUtilities.invokeLater(this::showTelaInicial);
    }

    // --- MÉTODOS DE NAVEGAÇÃO PRINCIPAL ---
    
    private void showTelaInicial(){
        var telaInicial = new TelaInicial(this::showTelaLogin, () -> showTelaConfiguracoes(this::showTelaInicial));
        telaInicial.setVisible(true);
    }

    private void showTelaLogin(){
        var telaLogin = new TelaLogin(this::showTelaInicial, this::showTelaRecuperacaoSenha, () -> showTelaConfiguracoes(this::showTelaLogin), this::showTelaMenuAdmin, this::showTelaMenuEstudante);
        telaLogin.setVisible(true);
    }

    private void showTelaMenuEstudante(){
        this.ultimaTelaMenu = this::showTelaMenuEstudante; 
        var telaMenuEst = new TelaMenuEstudante(() -> showTelaConfiguracoes(this::showTelaMenuEstudante), () -> showTelaTemaPerguntas(this::showTelaMenuEstudante), this::showTelaHistoricoEst);
        telaMenuEst.setVisible(true);
    }
    
    // Adicione aqui seus outros métodos de navegação (showTelaMenuAdmin, showTelaHistoricoEst, etc.)

    private void showTelaTemaPerguntas(Runnable voltarParaMenu) {
        TelaTemaPerguntas telaTemaP = new TelaTemaPerguntas(voltarParaMenu, () -> iniciarNovaPartida(voltarParaMenu));
        telaTemaP.setVisible(true);
    }
    
    /**
     * MÉTODO QUE ESTAVA FALTANDO.
     * Exibe a tela de configurações e sabe como voltar para a tela anterior.
     */
    private void showTelaConfiguracoes(Runnable voltaTelaAnterior) {
        TelaConfiguracoes telaConfig = new TelaConfiguracoes(this::showTelaInicial, voltaTelaAnterior);
        telaConfig.setVisible(true);
    }


    // --- NOVA LÓGICA DA PARTIDA ---

    private void iniciarNovaPartida(Runnable voltarParaMenu) {
        this.questoesDaPartidaAtual = questaoService.buscarQuestoesParaPartida();

        if (questoesDaPartidaAtual == null || questoesDaPartidaAtual.size() < MAX_QUESTOES) {
            JOptionPane.showMessageDialog(null, "Não foi possível carregar questões suficientes para a partida.", "Erro", JOptionPane.ERROR_MESSAGE);
            voltarParaMenu.run();
            return;
        }

        this.indiceQuestaoAtual = 0;
        this.pontuacaoAtual = 0;
        this.inicioDaSessao = LocalDateTime.now();

        if (telaDePartidaUnica != null) {
            telaDePartidaUnica.dispose();
        }

        this.telaDePartidaUnica = new TelaPartida(
            questoesDaPartidaAtual.get(0),              
            this::processarProximaQuestao,                 
            () -> processarResposta(true),                 
            () -> processarResposta(false),                
            () -> showTelaConfiguracoes(() -> telaDePartidaUnica.setVisible(true)), // Esta chamada agora funcionará
            this::finalizarPartida                         
        );
        
        telaDePartidaUnica.setVisible(true);
    }

    private void processarResposta(boolean acertou) {
        if (acertou) {
            this.pontuacaoAtual++;
            showTelaRespostaCorreta(this::processarProximaQuestao);
        } else {
            Runnable acaoAposSolucao = this::processarProximaQuestao;
            Runnable acaoAposErro = () -> showTelaSolucao(questoesDaPartidaAtual.get(indiceQuestaoAtual), acaoAposSolucao);
            showTelaRespostaIncorreta(questoesDaPartidaAtual.get(indiceQuestaoAtual), acaoAposErro);
        }
    }

    private void processarProximaQuestao() {
        this.indiceQuestaoAtual++;
        if (indiceQuestaoAtual >= MAX_QUESTOES) {
            finalizarPartida();
        } else {
            Questao proximaQuestao = questoesDaPartidaAtual.get(indiceQuestaoAtual);
            telaDePartidaUnica.carregarQuestaoNaTela(proximaQuestao);
        }
    }
    
    private void finalizarPartida() {
        if(telaDePartidaUnica != null) {
            telaDePartidaUnica.dispose();
        }

        // TODO: LÓGICA PARA SALVAR O HISTÓRICO
        System.out.println("Fim de Jogo! Pontuação: " + pontuacaoAtual + "/" + MAX_QUESTOES);
        // Exemplo de como salvar:
        // SessaoJogo novaSessao = new SessaoJogo(this.inicioDaSessao, LocalDateTime.now(), false, this.pontuacaoAtual, idDoAlunoLogado);
        // new HistoricoService().salvar(novaSessao);

        JOptionPane.showMessageDialog(null, "Fim de Jogo!\nSua pontuação: " + pontuacaoAtual + " de " + MAX_QUESTOES);

        if (this.ultimaTelaMenu != null) {
            this.ultimaTelaMenu.run();
        } else {
            showTelaInicial();
        }
    }
    
    // Outros métodos de feedback (showTelaRespostaCorreta, etc.) que você já tem
    private void showTelaRespostaCorreta(Runnable proximaAcao) { 
        TelaRespostaCorreta telaRespCor = new TelaRespostaCorreta(proximaAcao);
        telaRespCor.setVisible(true);
    }

    private void showTelaSolucao(Questao questao, Runnable proximaPartidaAction){
        TelaSolucao telaSolucao = new TelaSolucao(questao, proximaPartidaAction); 
        telaSolucao.setVisible(true);
    }

    private void showTelaRespostaIncorreta(Questao questao, Runnable proximaAcao) { 
        TelaRespostaIncorreta telaRespInc = new TelaRespostaIncorreta(questao, proximaAcao); 
        telaRespInc.setVisible(true);
    }
    
    private void showTelaRecuperacaoSenha() { /* ... seu código ... */ }
    private void showTelaMenuAdmin() { /* ... seu código ... */ }
    private void showTelaHistoricoEst() { /* ... seu código ... */ }
}