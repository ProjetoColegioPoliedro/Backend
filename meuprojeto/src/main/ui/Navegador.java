package ui;

import model.Aluno;
import model.HistoricoJogo;
import model.Questao;
import service.AlunoService;
import service.AuthService;
import service.HistoricoService;
import service.QuestaoService;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Navegador {
    private QuestaoService questaoService;
    private AuthService authService;
    private AlunoService alunoService;
    private Runnable ultimaTelaMenu;
    private Integer idAlunoLogado = null; // Armazena o ID do aluno logado

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

    public void start() {
        SwingUtilities.invokeLater(this::showTelaInicial);
    }

    // --- MÉTODOS DE NAVEGAÇÃO PRINCIPAL ---

    private void showTelaInicial() {
        var telaInicial = new TelaInicial(this::showTelaLogin, () -> showTelaConfiguracoes(this::showTelaInicial));
        telaInicial.setVisible(true);
    }

    private void showTelaLogin() {
        final TelaLogin[] telaLoginHolder = new TelaLogin[1];
        telaLoginHolder[0] = new TelaLogin(
            this::showTelaInicial,
            this::showTelaRecuperacaoSenha,
            () -> showTelaConfiguracoes(this::showTelaLogin),
            (login, senha) -> {
                try {
                    int tipoUsuario = authService.autenticarUsuario(login, senha);
                    if (tipoUsuario == 0) { // Aluno
                        Aluno aluno = alunoService.buscarPorLogin(login);
                        if (aluno != null) {
                            this.idAlunoLogado = aluno.getIdAluno();
                            JOptionPane.showMessageDialog(null, "Login de Aluno realizado com sucesso!", "Autenticação", JOptionPane.INFORMATION_MESSAGE);
                            telaLoginHolder[0].dispose();
                            showTelaMenuEstudante();
                        } else {
                            JOptionPane.showMessageDialog(null, "Erro: Usuário autenticado, mas dados do aluno não encontrados.", "Erro de Dados", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (tipoUsuario == 1) { // Professor/Admin
                        this.idAlunoLogado = null;
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

    private void showTelaRecuperacaoSenha() {
        var telaRecSenha = new TelaRecuperacaoSenha(this::showTelaLogin, () -> showTelaConfiguracoes(this::showTelaRecuperacaoSenha));
        telaRecSenha.setVisible(true);
    }

    private void showTelaMenuEstudante() {
        this.ultimaTelaMenu = this::showTelaMenuEstudante;
        var telaMenuEst = new TelaMenuEstudante(() -> showTelaConfiguracoes(this::showTelaMenuEstudante), () -> showTelaTemaPerguntas(this::showTelaMenuEstudante), this::showTelaHistoricoEst);
        telaMenuEst.setVisible(true);
    }

    private void showTelaMenuAdmin() {
        this.ultimaTelaMenu = this::showTelaMenuAdmin;
        TelaMenuAdmin telaMenuAdmin = new TelaMenuAdmin(() -> showTelaConfiguracoes(this::showTelaMenuAdmin), () -> showTelaTemaPerguntas(this::showTelaMenuAdmin), this::showTelaHistoricoAdmin, this::showTelaAreaRestrita);
        telaMenuAdmin.setVisible(true);
    }

    private void showTelaTemaPerguntas(Runnable voltarParaMenu) {
        TelaTemaPerguntas telaTemaP = new TelaTemaPerguntas(
            voltarParaMenu,
            temasSelecionados -> iniciarNovaPartida(voltarParaMenu, temasSelecionados)
        );
        telaTemaP.setVisible(true);
    }

    private void showTelaHistoricoEst() {
        if (this.idAlunoLogado == null) {
            JOptionPane.showMessageDialog(null, "Nenhum aluno logado para exibir o histórico.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        TelaHistorico telaHistEst = new TelaHistorico(
            this::showTelaMenuEstudante,
            () -> showTelaConfiguracoes(this::showTelaHistoricoEst),
            this.idAlunoLogado,
            new HistoricoService()
        );
        telaHistEst.setVisible(true);
    }

    private void showTelaHistoricoAdmin() {
        TelaHistAdmin telaHistAdmin = new TelaHistAdmin(
            this::showTelaMenuAdmin,
            () -> showTelaConfiguracoes(this::showTelaHistoricoAdmin),
            new HistoricoService(),
            new AlunoService()
        );
        telaHistAdmin.setVisible(true);
    }

    private void showTelaAreaRestrita() {
        TelaAreaRestrita telaAreaRes = new TelaAreaRestrita(this::showTelaMenuAdmin, () -> showTelaConfiguracoes(this::showTelaAreaRestrita),
            this::showTelaAdicionaPergunta, this::showTelaConsultaRanking, this::showTelaCadastro);
        telaAreaRes.setVisible(true);
    }

    private void showTelaAdicionaPergunta() {
        TelaAdicionaPergunta telaAddPerg = new TelaAdicionaPergunta(this::showTelaAreaRestrita, this.questaoService);
        telaAddPerg.setVisible(true);
    }

    private void showTelaConsultaRanking() {
        TelaConsultaRanking telaConsRank = new TelaConsultaRanking(this::showTelaAreaRestrita);
        telaConsRank.setVisible(true);
    }

    private void showTelaCadastro() {
        TelaCadastro telaCad = new TelaCadastro(this::showTelaAreaRestrita, this.alunoService);
        telaCad.setVisible(true);
    }

    private void iniciarNovaPartida(Runnable voltarParaMenu, List<String> temas) {
        try {
            this.questoesDaPartidaAtual = questaoService.buscarQuestoesPorTemas(temas, MAX_QUESTOES);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro de banco de dados ao carregar questões: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            voltarParaMenu.run();
            return;
        }

        if (questoesDaPartidaAtual == null || questoesDaPartidaAtual.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não foi possível carregar questões para as matérias selecionadas.", "Sem Questões", JOptionPane.WARNING_MESSAGE);
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
            questoesDaPartidaAtual.get(indiceQuestaoAtual),    // 1. A questão a ser exibida
            this::processarProximaQuestao,                     // 2. Ação para o botão "Pular"
            () -> processarResposta(true),                     // 3. Ação para quando a resposta for CORRETA
            () -> processarResposta(false),                    // 4. Ação para quando a resposta for INCORRETA
            () -> showTelaConfiguracoes(() -> telaDePartidaUnica.setVisible(true)), // 5. Ação para o botão de "Configurações"
            this::finalizarPartida                             // 6. Ação para o botão "Parar Jogo"
        );

        telaDePartidaUnica.setVisible(true);
    }

    private void processarResposta(boolean acertou) {
        int totalQuestoesNaPartida = this.questoesDaPartidaAtual.size();
        String proximaAcaoTextoBotao;

        if (indiceQuestaoAtual == totalQuestoesNaPartida - 2) {
            proximaAcaoTextoBotao = "Questão final";
        } else if (indiceQuestaoAtual + 1 >= totalQuestoesNaPartida) {
            proximaAcaoTextoBotao = "Fim de Jogo!";
        } else {
            proximaAcaoTextoBotao = "Próxima Questão";
        }

        if (acertou) {
            this.pontuacaoAtual++;
            showTelaRespostaCorreta(this::processarProximaQuestao, proximaAcaoTextoBotao);
        } else {
            Runnable acaoJogarNovamente = () -> {
                salvarHistoricoDaPartidaAtual();
                if (telaDePartidaUnica != null) {
                    telaDePartidaUnica.dispose();
                    telaDePartidaUnica = null;
                }
                showTelaTemaPerguntas(this.ultimaTelaMenu);
            };

            Runnable acaoVerSolucao = () -> showTelaSolucao(
                questoesDaPartidaAtual.get(indiceQuestaoAtual),
                acaoJogarNovamente,
                "Jogar Novamente"
            );
            
            showTelaRespostaIncorreta(
                questoesDaPartidaAtual.get(indiceQuestaoAtual),
                acaoVerSolucao,
                "Ver Solução"
            );
        }
    }

    private void processarProximaQuestao() {
        int totalQuestoesNaPartida = this.questoesDaPartidaAtual.size();
        this.indiceQuestaoAtual++;
        if (indiceQuestaoAtual >= totalQuestoesNaPartida) {
            finalizarPartida();
        } else {
            Questao proximaQuestao = questoesDaPartidaAtual.get(indiceQuestaoAtual);
            telaDePartidaUnica.carregarQuestaoNaTela(proximaQuestao);
        }
    }

    private void salvarHistoricoDaPartidaAtual() {
        if (this.idAlunoLogado != null) {
            try {
                HistoricoJogo novoHistorico = new HistoricoJogo(
                    LocalDate.now(),
                    this.pontuacaoAtual,
                    this.questoesDaPartidaAtual.size() - this.pontuacaoAtual,
                    "Fim de Jogo",
                    this.pontuacaoAtual,
                    this.idAlunoLogado
                );
                new HistoricoService().salvar(novoHistorico);
                System.out.println("Histórico da partida salvo para o aluno ID: " + this.idAlunoLogado);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Atenção: Não foi possível salvar o histórico da partida.", "Erro de Histórico", JOptionPane.WARNING_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void finalizarPartida() {
        if (telaDePartidaUnica != null) {
            telaDePartidaUnica.dispose();
            telaDePartidaUnica = null;
        }

        salvarHistoricoDaPartidaAtual();

        JOptionPane.showMessageDialog(null, "Fim de Jogo!\nSua pontuação: " + pontuacaoAtual + " de " + this.questoesDaPartidaAtual.size(), "Partida Finalizada", JOptionPane.INFORMATION_MESSAGE);

        if (pontuacaoAtual == this.questoesDaPartidaAtual.size() && !this.questoesDaPartidaAtual.isEmpty()) { // Venceu o jogo
            showTelaVencedor(ultimaTelaMenu);
        } else if (this.ultimaTelaMenu != null) {
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

    private void showTelaSolucao(Questao questao, Runnable proximaPartidaAction, String buttonText) {
        TelaSolucao telaSolucao = new TelaSolucao(questao, proximaPartidaAction);
        telaSolucao.setVisible(true);
    }

    private void showTelaRespostaIncorreta(Questao questao, Runnable proximaAcao, String buttonText) {
        TelaRespostaIncorreta telaRespInc = new TelaRespostaIncorreta(questao, proximaAcao, buttonText);
        telaRespInc.setVisible(true);
    }

    private void showTelaVencedor(Runnable ultimaTelaMenu) {
        TelaVencedor telaVenc = new TelaVencedor(ultimaTelaMenu, () -> showTelaTemaPerguntas(this.ultimaTelaMenu));
        telaVenc.setVisible(true);
    }
}