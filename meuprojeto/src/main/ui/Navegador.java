package ui;

import service.QuestaoService;
import service.AuthService;
import service.AlunoService;
import model.Questao;
import model.SessaoJogo;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.SQLException;

public class Navegador {
    private QuestaoService questaoService;
    private AuthService authService;
    private AlunoService alunoService;
    private Runnable ultimaTelaMenu;

    // --- VARIÁVEIS DE ESTADO DA PARTIDA ---
    private List<Questao> questoesDaPartidaAtual;
    private int indiceQuestaoAtual;
    private int pontuacaoAtual;
    private TelaPartida telaDePartidaUnica; // Mantém uma única instância da TelaPartida
    private final int MAX_QUESTOES = 13; // Número máximo de questões por partida
    private LocalDateTime inicioDaSessao;

    public Navegador() {
        this.questaoService = new QuestaoService();
        this.authService = new AuthService();
        this.alunoService = new AlunoService();
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
        final TelaLogin[] telaLoginHolder = new TelaLogin[1];
        telaLoginHolder[0] = new TelaLogin( 
            this::showTelaInicial, 
            this::showTelaRecuperacaoSenha, 
            () -> showTelaConfiguracoes(this::showTelaLogin), 
            (login, senha) -> { 
                try {
                    int tipoUsuario = authService.autenticarUsuario(login, senha);
                    if (tipoUsuario == 0) { // Aluno
                        JOptionPane.showMessageDialog(null, "Login de Aluno realizado com sucesso!", "Autenticação", JOptionPane.INFORMATION_MESSAGE);
                        telaLoginHolder[0].dispose(); 
                        showTelaMenuEstudante();
                    } else if (tipoUsuario == 1) { // Professor/Admin
                        JOptionPane.showMessageDialog(null, "Login de Professor/Admin realizado com sucesso!", "Autenticação", JOptionPane.INFORMATION_MESSAGE);
                        telaLoginHolder[0].dispose(); 
                        showTelaMenuAdmin();
                    } else { // Falha na autenticação
                        JOptionPane.showMessageDialog(null, "Login ou senha inválidos.", "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    System.err.println("Erro na autenticação: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro no servidor ao tentar autenticar: " + e.getMessage(), "Erro de Servidor", JOptionPane.ERROR_MESSAGE);
                }
            }
        );
        telaLoginHolder[0].setVisible(true);
    }

    private void showTelaConfiguracoes(Runnable voltaTelaAnterior) {
        TelaConfiguracoes telaConfig = new TelaConfiguracoes(this::showTelaInicial, voltaTelaAnterior);
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
        TelaTemaPerguntas telaTemaP = new TelaTemaPerguntas(voltarParaMenu, () -> iniciarNovaPartida(voltarParaMenu));
        telaTemaP.setVisible(true);
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
        TelaConsultaRanking telaConsRank = new TelaConsultaRanking(this::showTelaAreaRestrita); // Passe o callback ou argumentos necessários conforme o construtor definido
        telaConsRank.setVisible(true);
    }

    private void showTelaCadastro(){
        TelaCadastro telaCad = new TelaCadastro(this::showTelaAreaRestrita, this.alunoService); 
        telaCad.setVisible(true);
    }


    // --- NOVA LÓGICA DA PARTIDA ---

    private void iniciarNovaPartida(Runnable voltarParaMenu) {
        try {
            this.questoesDaPartidaAtual = questaoService.buscarQuestoesAleatorias(MAX_QUESTOES); 
        } catch (SQLException e) {
            System.err.println("Erro SQL ao carregar questões para a partida: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro de banco de dados ao carregar questões: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            voltarParaMenu.run(); 
            return; 
        }
        
        if (questoesDaPartidaAtual == null || questoesDaPartidaAtual.size() < MAX_QUESTOES) {
            JOptionPane.showMessageDialog(null, "Não foi possível carregar questões suficientes para a partida. Verifique o banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
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
            questoesDaPartidaAtual.get(indiceQuestaoAtual),    // Questão atual
            this::processarProximaQuestao,                     // Ação ao pular questão (e para avançar)
            () -> processarResposta(true),                     // Ação ao acertar
            () -> processarResposta(false),                    // Ação ao errar
            () -> showTelaConfiguracoes(() -> telaDePartidaUnica.setVisible(true)), // Para abrir configs e voltar à partida
            this::finalizarPartida                             // Ação ao parar jogo (retornar ao menu)
        );
        
        telaDePartidaUnica.setVisible(true);
    }

    private void processarResposta(boolean acertou) {
        // proximaAcaoTextoBotao é para o botão da TelaRespostaCorreta/TelaSolucao
        String proximaAcaoTextoBotaoNaSolucaoOuProximaQuestao; 
        if (indiceQuestaoAtual + 1 >= MAX_QUESTOES) { 
            proximaAcaoTextoBotaoNaSolucaoOuProximaQuestao = "Fim de Jogo!";
        } else {
            proximaAcaoTextoBotaoNaSolucaoOuProximaQuestao = "Próxima Questão";
        }

        if (acertou) {
            this.pontuacaoAtual++;
            showTelaRespostaCorreta(this::processarProximaQuestao, proximaAcaoTextoBotaoNaSolucaoOuProximaQuestao); 
        } else {
            // CORREÇÃO: Ação após errar deve ser IR PARA A TELA DE SOLUÇÃO.
            // O botão na TelaRespostaIncorreta SEMPRE dirá "Ver solução".
            String textoBotaoNaTelaIncorreta = "Ver solução";

            // Ação que o botão da TelaRespostaIncorreta vai disparar: mostrar a TelaSolucao.
            Runnable acaoBotaoVerSolucao = () -> showTelaSolucao(
                questoesDaPartidaAtual.get(indiceQuestaoAtual), 
                this::processarProximaQuestao, // Ação do botão da TelaSolucao
                proximaAcaoTextoBotaoNaSolucaoOuProximaQuestao // Texto do botão da TelaSolucao
            );

            // Chama a TelaRespostaIncorreta com o texto "Ver solução" e a ação de ir para a solução
            showTelaRespostaIncorreta(questoesDaPartidaAtual.get(indiceQuestaoAtual), acaoBotaoVerSolucao, textoBotaoNaTelaIncorreta); 
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
            telaDePartidaUnica = null; 
        }

        // TODO: LÓGICA PARA SALVAR O HISTÓRICO DA SESSÃO DE JOGO (precisa de idDoAlunoLogado)
        // int idDoAlunoLogado = ...; 
        // SessaoJogo novaSessao = new SessaoJogo(this.inicioDaSessao, LocalDateTime.now(), false, this.pontuacaoAtual, idDoAlunoLogado);
        // new HistoricoService().salvar(novaSessao); 

        System.out.println("Fim de Jogo! Pontuação: " + pontuacaoAtual + "/" + MAX_QUESTOES);
        JOptionPane.showMessageDialog(null, "Fim de Jogo!\nSua pontuação: " + pontuacaoAtual + " de " + MAX_QUESTOES, "Partida Finalizada", JOptionPane.INFORMATION_MESSAGE);

        if (this.ultimaTelaMenu != null) {
            this.ultimaTelaMenu.run();
        } else {
            showTelaInicial(); 
        }
    }
    
    // Outros métodos de feedback
    private void showTelaRespostaCorreta(Runnable proximaAcao, String buttonText) { 
        TelaRespostaCorreta telaRespCor = new TelaRespostaCorreta(proximaAcao, buttonText);
        telaRespCor.setVisible(true);
    }

    // MODIFICADO: showTelaSolucao agora recebe o texto do botão para a TelaSolucao
    private void showTelaSolucao(Questao questao, Runnable proximaPartidaAction, String buttonText){ // <--- Assinatura ajustada
        TelaSolucao telaSolucao = new TelaSolucao(questao, proximaPartidaAction); // Ajustado para o construtor existente
        telaSolucao.setVisible(true);
    }

    private void showTelaRespostaIncorreta(Questao questao, Runnable proximaAcao, String buttonText) { 
        TelaRespostaIncorreta telaRespInc = new TelaRespostaIncorreta(questao, proximaAcao, buttonText); 
        telaRespInc.setVisible(true);
    }
}