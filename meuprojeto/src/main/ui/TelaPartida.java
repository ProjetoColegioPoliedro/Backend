package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;   
import java.util.List;        
import java.util.Collections;  
import javax.swing.Timer;

import model.Questao;
import model.Alternativa;

public class TelaPartida extends JFrame {
    private int segundos = 45;
    private JLabel caixaPerg;
    private JLabel caixaRespA;
    private JLabel caixaRespB;
    private JLabel caixaRespC;
    private JLabel caixaRespD;
    private Timer timer;
    
    private Questao questaoAtual;
    private List<JLabel> labelsRespostas;

    // Construtor com Runnables para feedback de tempo e solução, e para ações pós-resposta
    public TelaPartida(Questao questao, Runnable acabouTempo, Runnable solucao, Runnable respostaCorretaAction, Runnable respostaIncorretaAction) {
        this.questaoAtual = questao;
        
        var roxo = new Color(20, 14, 40);
        var laranja = new Color(250, 164, 31);
        var ciano = new Color(30, 180, 195);
        var rosa = new Color(238, 33, 82);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1500);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(false);

        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo);

        // Inicializa os JLabels das respostas e da pergunta
        caixaRespA = new JLabel("");
        caixaRespB = new JLabel("");
        caixaRespC = new JLabel("");
        caixaRespD = new JLabel("");
        caixaPerg = new JLabel("");

        labelsRespostas = new ArrayList<>();
        labelsRespostas.add(caixaRespA);
        labelsRespostas.add(caixaRespB);
        labelsRespostas.add(caixaRespC);
        labelsRespostas.add(caixaRespD);
        
        // Configuração dos JLabels de Respostas
        configuraLabelResposta(caixaRespA, 300, 200, ciano, respostaCorretaAction, respostaIncorretaAction);
        configuraLabelResposta(caixaRespB, 300, 310, ciano, respostaCorretaAction, respostaIncorretaAction);
        configuraLabelResposta(caixaRespC, 300, 420, ciano, respostaCorretaAction, respostaIncorretaAction);
        configuraLabelResposta(caixaRespD, 300, 530, ciano, respostaCorretaAction, respostaIncorretaAction);

        // Configuração do JLabel da Pergunta
        caixaPerg.setBounds(180, 50, 1200, 80);
        caixaPerg.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaPerg.setForeground(Color.WHITE);
        caixaPerg.setBorder(BorderFactory.createLineBorder(rosa, 7));
        corFundo.add(caixaPerg);

        // Chame este método para popular a tela com a questão inicial
        carregarQuestaoNaTela(this.questaoAtual);

        // Botões
        var pularQuestao = new JButton(">>");
        pularQuestao.setFont(new Font("Montserrat", Font.BOLD, 22));
        pularQuestao.setBackground(laranja);
        pularQuestao.setForeground(Color.BLACK);
        corFundo.add(pularQuestao);
        pularQuestao.setBounds(340, 650, 60, 60);
        pularQuestao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ação para pular questão (a ser implementada, talvez com showTelaSolucao ou próxima questão)
                int confirm = JOptionPane.showConfirmDialog(null, "Você tem certeza que quer pular esta questão?", "Pular Questão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    pararCronometro();
                    dispose(); // Fecha a tela atual
                    // Lógica para pular: poderia ir para a próxima questão ou mostrar a solução
                    solucao.run(); // Por exemplo, mostra a solução da questão pulada
                }
            }
        });

        var pulQues = new JLabel("Pular questâo");
        pulQues.setBounds(410, 645, 500, 70);
        pulQues.setFont(new Font("Montserrat", Font.BOLD, 25));
        pulQues.setForeground(Color.WHITE);
        corFundo.add(pulQues);

        var eliminDuas = new JButton("1/2");
        eliminDuas.setFont(new Font("Montserrat", Font.BOLD, 19));
        eliminDuas.setBackground(laranja);
        eliminDuas.setForeground(Color.BLACK);
        corFundo.add(eliminDuas);
        eliminDuas.setBounds(820, 650, 60, 60);
        eliminDuas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para eliminar duas alternativas (a ser implementada)
                JOptionPane.showConfirmDialog(null, "Você tem certeza que quer usar o 1/2?", "Ajuda 50/50", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            }
        });

        var eliDuasTxt = new JLabel("Eliminar duas alternativas");
        eliDuasTxt.setBounds(890, 645, 500, 70);
        eliDuasTxt.setFont(new Font("Montserrat", Font.BOLD, 25));
        eliDuasTxt.setForeground(Color.WHITE);
        corFundo.add(eliDuasTxt);

        // Visores (Sem alterações)
        var txtErrar = new JLabel("");
        txtErrar.setFont(new Font("Montserrat", Font.BOLD, 19));
        txtErrar.setBackground(laranja);
        txtErrar.setForeground(Color.BLACK);
        corFundo.add(txtErrar);

        var errar = new ImageIcon("assets\\retanguloLateral.png");
        var circErrar = errar.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        var retanguloUm = new JLabel(new ImageIcon(circErrar));
        corFundo.add(retanguloUm);
        retanguloUm.setBounds(1400, 250, 70, 70);

        var acertar = new ImageIcon("assets\\retanguloLateral.png");
        var circAcertar = acertar.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        var retanguloDois = new JLabel(new ImageIcon(circAcertar));
        corFundo.add(retanguloDois);
        retanguloDois.setBounds(1400, 370, 70, 70);

        var txtAcertar = new JLabel("");
        txtAcertar.setBounds(1393, 400, 500, 70);
        txtAcertar.setFont(new Font("Montserrat", Font.BOLD, 19));
        txtAcertar.setForeground(Color.WHITE);
        corFundo.add(txtAcertar);

        // Botão "Parar"
        var parar = new JButton("C");
        parar.setFont(new Font("Montserrat", Font.BOLD, 19));
        parar.setBackground(laranja);
        parar.setForeground(Color.BLACK);
        corFundo.add(parar);
        parar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Você tem certeza que quer parar o jogo?", "Parar Jogo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(confirm == JOptionPane.YES_OPTION){
                    pararCronometro();
                    dispose();
                    solucao.run(); // Assume que "solucao" aqui significa ir para a tela de solução e/ou encerrar a partida
                }
            }
        });

        parar.setBounds(1404, 500, 60, 60);
        var txtParar = new JLabel("Parar");
        txtParar.setBounds(1410, 540, 500, 70);
        txtParar.setFont(new Font("Montserrat", Font.BOLD, 19));
        txtParar.setForeground(Color.WHITE);
        corFundo.add(txtParar);

        // Alternativas (com lógica para as letras A, B, C, D)
        var circulo = new ImageIcon("assets\\circuloAlternativa.png");
        var c = circulo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        
        adicionarCirculoAlternativa(corFundo, c, 220, 215, "A");
        adicionarCirculoAlternativa(corFundo, c, 220, 325, "B");
        adicionarCirculoAlternativa(corFundo, c, 220, 440, "C");
        adicionarCirculoAlternativa(corFundo, c, 220, 540, "D");

        // Cronômetro
        var circuloCrono = new ImageIcon("C:/Users/Admin/Downloads/Refatoracao/src/main/assets/circuloCrono.png");
        var circC = circuloCrono.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        var circuloFinal = new JLabel(new ImageIcon(circC));
        circuloFinal.setBounds(60, 50, 90, 90);
        corFundo.add(circuloFinal);

        JLabel cronometro = new JLabel("45");
        cronometro.setFont(new Font("Montserrat", Font.BOLD, 40));
        cronometro.setForeground(Color.WHITE);
        cronometro.setBounds(22, 5, 80, 80);
        circuloFinal.add(cronometro);

        timer = new Timer(1000, e -> {
            if (segundos > 0) {
                segundos--;
                cronometro.setText(String.format("%02d", segundos % 60));
            } else {
                timer.stop();
                JOptionPane.showMessageDialog(null, "Tempo encerrado!", "Fim do tempo de questão", 2);
                acabouTempo.run(); // Chamada para a ação de tempo esgotado (ex: ir para tela de tempo encerrado)
                dispose(); // Fecha a tela de partida
            }
        });
    }

    // Método auxiliar para configurar JLabels de resposta e adicionar listener
    private void configuraLabelResposta(JLabel label, int x, int y, Color borderColor, Runnable respostaCorretaAction, Runnable respostaIncorretaAction) {
        label.setBounds(x, y, 1000, 80);
        label.setFont(new Font("Montserrat", Font.ITALIC, 20));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(borderColor, 7));
        getContentPane().add(label);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent clique) {
                int confirm = JOptionPane.showConfirmDialog(null, "Você tem certeza?", "Confirmação de resposta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    Alternativa altSelecionada = null;
                    String textoSelecionado = label.getText().replaceAll("<html>", "").replaceAll("</html>", "").trim(); 
                    
                    if (questaoAtual != null && questaoAtual.getAlternativas() != null) {
                        for (Alternativa alt : questaoAtual.getAlternativas()) {
                            if (alt.getTexto().trim().equals(textoSelecionado)) {
                                altSelecionada = alt;
                                break;
                            }
                        }
                    }

                    if (altSelecionada != null && questaoAtual != null && questaoAtual.getAlternativaCorreta() != null) {
                        if (altSelecionada.equals(questaoAtual.getAlternativaCorreta())) { // Aqui o equals() de Alternativa será usado
                            JOptionPane.showMessageDialog(null, "Resposta Correta!", "Acerto", JOptionPane.INFORMATION_MESSAGE);
                            pararCronometro();
                            dispose(); // Fecha a tela de partida
                            respostaCorretaAction.run(); // Executa a ação de resposta correta
                        } else {
                            JOptionPane.showMessageDialog(null, "Resposta Incorreta! A correta era: " + questaoAtual.getTextoAlternativaCorreta(), "Erro", JOptionPane.ERROR_MESSAGE);
                            pararCronometro();
                            dispose(); // Fecha a tela de partida
                            respostaIncorretaAction.run(); // Executa a ação de resposta incorreta
                        }
                    } else {
                        // Caso de erro na busca da questão (altSelecionada, questaoAtual ou alternativaCorreta nulos)
                        JOptionPane.showMessageDialog(null, "Erro interno: Questão ou alternativa correta não carregada corretamente.", "Erro de Lógica", JOptionPane.ERROR_MESSAGE);
                       
                        dispose(); 
                    }
                }
            }
        });
    }
    
    // Método auxiliar para adicionar os círculos das letras (A, B, C, D)
    private void adicionarCirculoAlternativa(JPanel panel, Image img, int x, int y, String letra) {
        var circuloLabel = new JLabel(new ImageIcon(img));
        panel.add(circuloLabel);
        circuloLabel.setBounds(x, y, 50, 50);
        var letraLabel = new JLabel(letra);
        letraLabel.setBounds(11, 0, 50, 50); // Posição relativa ao circuloLabel
        letraLabel.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraLabel.setForeground(Color.WHITE);
        circuloLabel.add(letraLabel);
    }

    /**
     * Carrega uma nova questão na tela, atualizando o enunciado e as alternativas.
     * @param novaQuestao O objeto Questao a ser exibido.
     */
    public void carregarQuestaoNaTela(Questao novaQuestao) {
        if (novaQuestao == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma questão para exibir!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        this.questaoAtual = novaQuestao;
        this.segundos = 45; 
        caixaPerg.setText("<html>" + novaQuestao.getEnunciado() + "</html>"); // Usa HTML para quebrar linha se o texto for longo

        List<String> textosAlternativas = novaQuestao.getTextosAlternativas(true); // Embaralha as alternativas
        
        // Garante que haja pelo menos 4 alternativas para preencher todos os labels
        while (textosAlternativas.size() < 4) {
            textosAlternativas.add(" "); // Adiciona espaços em branco se não houver 4 alternativas
        }

        // Preenche os JLabels das respostas com os textos embaralhados
        for (int i = 0; i < labelsRespostas.size(); i++) {
            labelsRespostas.get(i).setText("<html>" + textosAlternativas.get(i) + "</html>");
        }
        
        // Reinicia e inicia o cronômetro, se ele estiver parado
        if (timer != null) {
            timer.stop(); // Para o timer anterior, se houver
            timer.start(); // Inicia o timer para a nova questão
        }
    }

    public void iniciarCronometro() {
        if (timer != null) {
            timer.start();
        }
    }
    
    // Método para parar o cronômetro (útil ao responder uma questão)
    public void pararCronometro() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
}
