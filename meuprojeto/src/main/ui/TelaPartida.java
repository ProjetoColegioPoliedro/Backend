package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Questao;
import model.Alternativa; // Importe a classe Alternativa

public class TelaPartida extends JFrame {
    private JLabel caixaPerg;
    private JLabel caixaRespA, caixaRespB, caixaRespC, caixaRespD;
    private List<JLabel> labelsRespostas;
    private Questao questaoAtual;
    private boolean ajuda5050Usada = false;
    private JButton eliminDuas; // Para poder desabilitar

    // Construtor com todos os Runnables necessários
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
        setResizable(false); // Mantido false, mas para layouts adaptáveis seria true
        
        // Painel de fundo, todos os componentes devem ser adicionados AQUI
        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo); // Define este painel como o conteúdo principal do JFrame

        // --- COMPONENTES DA UI ---
        // Botão Configurações
        // Carrega a imagem uma vez e a escala.
        ImageIcon settingsIcon = new ImageIcon("assets/settings.png");
        Image scaledSettingsImage = settingsIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel config = new JLabel(new ImageIcon(scaledSettingsImage));
        config.setBounds(1460, 20, 60, 60);
        config.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                configsAction.run(); // Apenas chama a ação, sem destruir a tela de partida
            }
        });
        corFundo.add(config); // Adicionado ao corFundo

        // Labels de Pergunta e Respostas
        caixaPerg = new JLabel();
        caixaPerg.setBounds(180, 50, 1200, 80);
        caixaPerg.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaPerg.setForeground(Color.WHITE);
        caixaPerg.setBorder(BorderFactory.createLineBorder(rosa, 7));
        corFundo.add(caixaPerg); // Adicionado ao corFundo

        labelsRespostas = new ArrayList<>();
        caixaRespA = new JLabel();
        caixaRespB = new JLabel();
        caixaRespC = new JLabel();
        caixaRespD = new JLabel();
        labelsRespostas.add(caixaRespA);
        labelsRespostas.add(caixaRespB);
        labelsRespostas.add(caixaRespC);
        labelsRespostas.add(caixaRespD);
        
        // Configura cada label de resposta, passando o corFundo para adicionar
        configuraLabelResposta(caixaRespA, corFundo, 300, 200, ciano, respostaCorretaAction, respostaIncorretaAction);
        configuraLabelResposta(caixaRespB, corFundo, 300, 310, ciano, respostaCorretaAction, respostaIncorretaAction);
        configuraLabelResposta(caixaRespC, corFundo, 300, 420, ciano, respostaCorretaAction, respostaIncorretaAction);
        configuraLabelResposta(caixaRespD, corFundo, 300, 530, ciano, respostaCorretaAction, respostaIncorretaAction);

        // Botões de Ajuda (Pular, 50/50, Parar)
        setupBotoesAcao(corFundo, laranja, acaoAoPular, pararJogoAction);

        // Adiciona os círculos com as letras (A, B, C, D)
        ImageIcon circuloIcon = new ImageIcon("assets\\circuloAlternativa.png");
        Image scaledCirculoImage = circuloIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        adicionarCirculoAlternativa(corFundo, scaledCirculoImage, 220, 215, "A"); 
        adicionarCirculoAlternativa(corFundo, scaledCirculoImage, 220, 325, "B");
        adicionarCirculoAlternativa(corFundo, scaledCirculoImage, 220, 440, "C");
        adicionarCirculoAlternativa(corFundo, scaledCirculoImage, 220, 540, "D");

        // Carrega a questão inicial na tela
        carregarQuestaoNaTela(this.questaoAtual);
    }

    public TelaPartida(Questao questao, Runnable acaoAoPular, Runnable respostaCorretaAction,
            Runnable respostaIncorretaAction, Runnable configsAction, Object pararJogoAction) {
        //TODO Auto-generated constructor stub
    }

    // Método auxiliar para configurar e adicionar JLabels de resposta
    private void configuraLabelResposta(JLabel label, JPanel parentPanel, int x, int y, Color borderColor, Runnable respostaCorretaAction, Runnable respostaIncorretaAction) {
        label.setBounds(x, y, 1000, 80);
        label.setFont(new Font("Montserrat", Font.ITALIC, 20));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(borderColor, 7));
        parentPanel.add(label); // <--- Adicionado ao painel 'corFundo' (parentPanel)
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent clique) {
                int confirm = JOptionPane.showConfirmDialog(null, "Você tem certeza?", "Confirmação de resposta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Limpa tags HTML para obter o texto puro da alternativa para comparação
                    String textoSelecionado = label.getText().replaceAll("<[^>]*>", "").trim();
                    
                    // Verifica se a alternativa selecionada é a correta
                    // Correção: Usa o método isAlternativaCorreta do modelo Questao (que precisa ser adicionado lá)
                    if (questaoAtual.isAlternativaCorreta(textoSelecionado)) {
                        respostaCorretaAction.run();
                    } else {
                        respostaIncorretaAction.run();
                    }
                }
            }
        });
    }

    // Método para carregar uma nova questão na tela
    public void carregarQuestaoNaTela(Questao novaQuestao) {
        if (novaQuestao == null) {
            // O Navegador já deve tratar questões nulas antes de chegar aqui
            JOptionPane.showMessageDialog(this, "Nenhuma questão para exibir!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        this.questaoAtual = novaQuestao;
        // Usa HTML para permitir quebra de linha se o enunciado for longo
        caixaPerg.setText("<html>" + novaQuestao.getEnunciado() + "</html>"); 

        // Garante que todos os JLabels das alternativas estejam visíveis
        // (importante para resetar o efeito do 50/50 de uma questão anterior)
        for (JLabel label : labelsRespostas) {
            label.setVisible(true);
            // Também reseta a borda para a cor padrão
            label.setBorder(BorderFactory.createLineBorder(new Color(30, 180, 195), 7)); 
        }
    
        List<String> textosAlternativas = novaQuestao.getTextosAlternativas(true); // Embaralha as alternativas
        
        // Preenche os JLabels das respostas com os textos embaralhados
        for (int i = 0; i < labelsRespostas.size(); i++) {
            if (i < textosAlternativas.size()) {
                labelsRespostas.get(i).setText("<html>" + textosAlternativas.get(i) + "</html>");
            } else {
                labelsRespostas.get(i).setText(""); // Limpa o texto se não houver alternativa correspondente
                labelsRespostas.get(i).setVisible(false); // Ou esconde o label se não tiver alternativa
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
            eliminDuas.setEnabled(false); // Desabilita o botão após o uso

            List<JLabel> alternativasIncorretasLabels = new ArrayList<>();
            for (JLabel currentLabel : labelsRespostas) {
                // Pega o texto puro para comparação
                String textoLabelLimpo = currentLabel.getText().replaceAll("<[^>]*>", "").trim();
                // Verifica se a alternativa é incorreta e se está visível
                if (currentLabel.isVisible() && !questaoAtual.isAlternativaCorreta(textoLabelLimpo)) {
                    alternativasIncorretasLabels.add(currentLabel);
                }
            }

            // Remove duas alternativas incorretas aleatoriamente
            if (alternativasIncorretasLabels.size() >= 2) {
                Collections.shuffle(alternativasIncorretasLabels);
                alternativasIncorretasLabels.get(0).setVisible(false);
                alternativasIncorretasLabels.get(1).setVisible(false);
                JOptionPane.showMessageDialog(this, "Duas alternativas incorretas foram eliminadas!", "Ajuda 50/50 Usada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível eliminar duas alternativas. Poucas alternativas incorretas disponíveis ou erro na lógica.", "Erro na Ajuda", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void setupBotoesAcao(JPanel panel, Color cor, Runnable acaoPular, Runnable acaoParar) {
        // Botão Pular Questão
        JButton pularQuestao = new JButton(">>");
        pularQuestao.setFont(new Font("Montserrat", Font.BOLD, 22));
        pularQuestao.setBackground(cor);
        pularQuestao.setForeground(Color.BLACK);
        pularQuestao.setBounds(340, 650, 60, 60); // Posição fixa
        pularQuestao.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Você tem certeza que quer pular esta questão?", "Pular Questão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                acaoPular.run(); // Chama a ação de pular (que já avança a questão no Navegador)
                pularQuestao.setEnabled(false);
            }
        });
        panel.add(pularQuestao);

        JLabel pulQuesTxt = new JLabel("Pular questão");
        pulQuesTxt.setBounds(410, 645, 500, 70); // Posição fixa
        pulQuesTxt.setFont(new Font("Montserrat", Font.BOLD, 25));
        pulQuesTxt.setForeground(Color.WHITE);
        panel.add(pulQuesTxt);
        
        // Botão Eliminar Duas Alternativas (50/50)
        eliminDuas = new JButton("1/2"); // Atribui ao atributo de classe
        eliminDuas.setFont(new Font("Montserrat", Font.BOLD, 19));
        eliminDuas.setBackground(cor);
        eliminDuas.setForeground(Color.BLACK);
        eliminDuas.setBounds(820, 650, 60, 60); // Posição fixa
        eliminDuas.addActionListener(e -> usarAjuda5050());
        panel.add(eliminDuas);

        JLabel eliDuasTxt = new JLabel("Eliminar duas alternativas");
        eliDuasTxt.setBounds(890, 645, 500, 70); // Posição fixa
        eliDuasTxt.setFont(new Font("Montserrat", Font.BOLD, 25));
        eliDuasTxt.setForeground(Color.WHITE);
        panel.add(eliDuasTxt);

        // Botão "Parar" o jogo
        JButton parar = new JButton("C");
        parar.setFont(new Font("Montserrat", Font.BOLD, 19));
        parar.setBackground(cor);
        parar.setForeground(Color.BLACK);
        parar.setBounds(1404, 500, 60, 60); // Posição fixa
        parar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Você tem certeza que quer parar o jogo?", "Parar Jogo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                acaoParar.run(); // Chama a ação de parar o jogo
            }
        });
        panel.add(parar);

        JLabel txtParar = new JLabel("Parar");
        txtParar.setBounds(1410, 540, 500, 70); // Posição fixa
        txtParar.setFont(new Font("Montserrat", Font.BOLD, 19));
        txtParar.setForeground(Color.WHITE);
        panel.add(txtParar);
    }

    // Método auxiliar para adicionar os círculos das letras (A, B, C, D)
    // Corrigido para adicionar ao painel passado
    private void adicionarCirculoAlternativa(JPanel panel, Image img, int x, int y, String letra) {
        JLabel circuloLabel = new JLabel(new ImageIcon(img));
        panel.add(circuloLabel); // <--- Adicionado ao painel passado
        circuloLabel.setBounds(x, y, 50, 50); 

        JLabel letraLabel = new JLabel(letra);
        letraLabel.setBounds(11, 0, 50, 50); // Posição relativa ao circuloLabel (dentro dele)
        letraLabel.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraLabel.setForeground(Color.WHITE);
        circuloLabel.add(letraLabel);
    }
}