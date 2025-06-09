package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Questao;

public class TelaPartida extends JFrame {
    private JLabel caixaPerg;
    private JLabel caixaRespA, caixaRespB, caixaRespC, caixaRespD;
    private List<JLabel> labelsRespostas;
    private Questao questaoAtual;
    private boolean ajuda5050Usada = false;
    private JButton eliminDuas;

    public TelaPartida(Questao questao, Runnable acaoAoPular, Runnable respostaCorretaAction, Runnable respostaIncorretaAction, Runnable configsAction, Runnable pararJogoAction) {
        this.questaoAtual = questao;
        
        // Configurações de cores e da janela
        var roxo = new Color(20, 14, 40);
        var laranja = new Color(250, 164, 31);
        var ciano = new Color(30, 180, 195);
        var rosa = new Color(238, 33, 82);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(false);
        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo);

        // --- COMPONENTES DA UI ---
        // Botão Configurações
        var config = new JLabel(new ImageIcon(new ImageIcon("assets/settings.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        config.setBounds(1460, 20, 60, 60);
        config.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                configsAction.run(); // Apenas chama a ação, sem destruir a tela
            }
        });
        corFundo.add(config);

        // Labels de Pergunta e Respostas
        caixaPerg = new JLabel();
        caixaPerg.setBounds(180, 50, 1200, 80);
        caixaPerg.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaPerg.setForeground(Color.WHITE);
        caixaPerg.setBorder(BorderFactory.createLineBorder(rosa, 7));
        corFundo.add(caixaPerg);

        labelsRespostas = new ArrayList<>();
        caixaRespA = new JLabel();
        caixaRespB = new JLabel();
        caixaRespC = new JLabel();
        caixaRespD = new JLabel();
        labelsRespostas.add(caixaRespA);
        labelsRespostas.add(caixaRespB);
        labelsRespostas.add(caixaRespC);
        labelsRespostas.add(caixaRespD);
        
        // Configura cada label de resposta
        configuraLabelResposta(caixaRespA, 300, 200, ciano, respostaCorretaAction, respostaIncorretaAction);
        configuraLabelResposta(caixaRespB, 300, 310, ciano, respostaCorretaAction, respostaIncorretaAction);
        configuraLabelResposta(caixaRespC, 300, 420, ciano, respostaCorretaAction, respostaIncorretaAction);
        configuraLabelResposta(caixaRespD, 300, 530, ciano, respostaCorretaAction, respostaIncorretaAction);

        // Botões de Ajuda (Pular, 50/50, Parar)
        setupBotoesAcao(corFundo, laranja, acaoAoPular, pararJogoAction);

        // Carrega a primeira questão na tela
        carregarQuestaoNaTela(this.questaoAtual);
    }

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
                    // Limpa tags HTML para obter o texto puro da alternativa
                    String textoSelecionado = label.getText().replaceAll("<[^>]*>", "").trim();
                    
                    // Usa o novo método do modelo Questao para verificar a resposta
                    if (questaoAtual.isAlternativaCorreta(textoSelecionado)) {
                        respostaCorretaAction.run();
                    } else {
                        respostaIncorretaAction.run();
                    }
                }
            }
        });
    }

    public void carregarQuestaoNaTela(Questao novaQuestao) {
        if (novaQuestao == null) return;
        
        this.questaoAtual = novaQuestao;
        caixaPerg.setText("<html>" + novaQuestao.getEnunciado() + "</html>"); 

        for (JLabel label : labelsRespostas) {
            label.setVisible(true);
        }
    
        List<String> textosAlternativas = novaQuestao.getTextosAlternativas(true); // Embaralha as alternativas
        
        for (int i = 0; i < labelsRespostas.size(); i++) {
            if (i < textosAlternativas.size()) {
                labelsRespostas.get(i).setText("<html>" + textosAlternativas.get(i) + "</html>");
            } else {
                labelsRespostas.get(i).setText(""); 
            }
        }
    }
    
    private void usarAjuda5050() {
        if (ajuda5050Usada) {
            JOptionPane.showMessageDialog(this, "A ajuda 50/50 já foi usada nesta partida!", "Ajuda Esgotada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja usar a ajuda 50/50?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ajuda5050Usada = true;
            eliminDuas.setEnabled(false);

            List<JLabel> alternativasIncorretasLabels = new ArrayList<>();
            for (JLabel currentLabel : labelsRespostas) {
                String textoLabelLimpo = currentLabel.getText().replaceAll("<[^>]*>", "").trim();
                if (!questaoAtual.isAlternativaCorreta(textoLabelLimpo)) {
                    alternativasIncorretasLabels.add(currentLabel);
                }
            }

            if (alternativasIncorretasLabels.size() >= 2) {
                Collections.shuffle(alternativasIncorretasLabels);
                alternativasIncorretasLabels.get(0).setVisible(false);
                alternativasIncorretasLabels.get(1).setVisible(false);
            }
        }
    }
    
    private void setupBotoesAcao(JPanel panel, Color cor, Runnable acaoPular, Runnable acaoParar) {
        // Botão Pular
        var pularQuestao = new JButton(">>");
        pularQuestao.setBounds(340, 650, 60, 60);
        pularQuestao.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Pular esta questão?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                acaoPular.run();
        });
        panel.add(pularQuestao);
        
        // Botão 50/50
        eliminDuas = new JButton("1/2");
        eliminDuas.setBounds(820, 650, 60, 60);
        eliminDuas.addActionListener(e -> usarAjuda5050());
        panel.add(eliminDuas);

        // Botão Parar
        var parar = new JButton("C");
        parar.setBounds(1404, 500, 60, 60);
        parar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Deseja parar o jogo?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                acaoParar.run();
        });
        panel.add(parar);
        
        // Adicionar outros elementos visuais (textos, etc.) aqui
    }
}