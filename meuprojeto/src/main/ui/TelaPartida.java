package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections; // Import para Collections.shuffle

import model.Questao;
import model.Alternativa;

public class TelaPartida extends JFrame {
    private JLabel caixaPerg;
    private JLabel caixaRespA;
    private JLabel caixaRespB;
    private JLabel caixaRespC;
    private JLabel caixaRespD;
    
    private Questao questaoAtual;
    private List<JLabel> labelsRespostas; // Para facilitar o acesso aos JLabels das respostas
    private boolean ajuda5050Usada = false; 

    // Construtor com Runnables 
    public TelaPartida(Questao questao, Runnable acaoAoEncerrarQuestao, Runnable respostaCorretaAction, Runnable respostaIncorretaAction, Runnable configs) {
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

        var icone = new ImageIcon("assets\\settings.png");
		var imagem = icone.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		var config = new JLabel(new ImageIcon(imagem));
		corFundo.add(config);
		config.setBounds(1460, 20, 60, 60);
		config.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				configs.run(); // abre a tela de configurações
				dispose();
			}
		});

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
        // Cada label de resposta precisa saber as ações para quando for clicado.
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

        carregarQuestaoNaTela(this.questaoAtual);

        // Botão Pular Questão
        var pularQuestao = new JButton(">>");
        pularQuestao.setFont(new Font("Montserrat", Font.BOLD, 22));
        pularQuestao.setBackground(laranja);
        pularQuestao.setForeground(Color.BLACK);
        corFundo.add(pularQuestao);
        pularQuestao.setBounds(340, 650, 60, 60);
        pularQuestao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Você tem certeza que quer pular esta questão?", "Pular Questão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    acaoAoEncerrarQuestao.run();
                }
            }
        });

        var pulQues = new JLabel("Pular questâo");
        pulQues.setBounds(410, 645, 500, 70);
        pulQues.setFont(new Font("Montserrat", Font.BOLD, 25));
        pulQues.setForeground(Color.WHITE);
        corFundo.add(pulQues);

        // Botão Eliminar Duas Alternativas (1/2) - Lógica adicionada aqui
        var eliminDuas = new JButton("1/2");
        eliminDuas.setFont(new Font("Montserrat", Font.BOLD, 19));
        eliminDuas.setBackground(laranja);
        eliminDuas.setForeground(Color.BLACK);
        corFundo.add(eliminDuas);
        eliminDuas.setBounds(820, 650, 60, 60);
        eliminDuas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica se a ajuda já foi usada nesta partida
                if (ajuda5050Usada) {
                    JOptionPane.showMessageDialog(null, "A ajuda 50/50 já foi usada nesta partida!", "Ajuda Esgotada", JOptionPane.WARNING_MESSAGE);
                    return; // Sai do método se a ajuda já foi usada
                }

                int confirm = JOptionPane.showConfirmDialog(null, "Você tem certeza que quer usar a ajuda 50/50?", "Confirmação de Ajuda", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Validação básica para garantir que a questão está carregada corretamente
                    if (questaoAtual == null || questaoAtual.getAlternativas() == null || questaoAtual.getAlternativaCorreta() == null) {
                        JOptionPane.showMessageDialog(null, "Não foi possível usar a ajuda 50/50. Dados da questão incompletos.", "Erro na Ajuda", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Marca a ajuda como usada para esta partida
                    ajuda5050Usada = true; 
                    eliminDuas.setEnabled(false); // Desabilita o botão após o uso

                    List<JLabel> alternativasIncorretasLabels = new ArrayList<>();
                    
                    // Itera sobre os JLabels das respostas para encontrar quais representam alternativas incorretas
                    for (JLabel currentLabel : labelsRespostas) { 
                        // Limpa o texto do JLabel (removendo tags HTML e espaços em branco desnecessários) para comparação
                        String textoLabelLimpo = currentLabel.getText().replaceAll("<html>", "").replaceAll("</html>", "").trim();

                        // Encontra o objeto Alternativa correspondente ao texto deste JLabel
                        Alternativa altObjeto = null;
                        for (Alternativa alt : questaoAtual.getAlternativas()) {
                            if (alt.getTexto().trim().equals(textoLabelLimpo)) {
                                altObjeto = alt;
                                break;
                            }
                        }

                        // Se encontrou o objeto Alternativa correspondente E ele não é a alternativa correta,
                        // adiciona o JLabel à lista de incorretas.
                        if (altObjeto != null && !altObjeto.equals(questaoAtual.getAlternativaCorreta())) {
                            alternativasIncorretasLabels.add(currentLabel);
                        }
                    }

                    // Verifica se há pelo menos duas alternativas incorretas para remover
                    if (alternativasIncorretasLabels.size() >= 2) {
                        Collections.shuffle(alternativasIncorretasLabels); // Embaralha a lista para remover aleatoriamente
                        
                        // Esconde visualmente as duas primeiras alternativas incorretas da lista embaralhada
                        alternativasIncorretasLabels.get(0).setVisible(false);
                        alternativasIncorretasLabels.get(1).setVisible(false);
                        
                        JOptionPane.showMessageDialog(null, "Duas alternativas incorretas foram eliminadas!", "Ajuda 50/50 Usada", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Não foi possível eliminar duas alternativas. Poucas alternativas incorretas disponíveis ou erro na lógica.", "Erro na Ajuda", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        var eliDuasTxt = new JLabel("Eliminar duas alternativas");
        eliDuasTxt.setBounds(890, 645, 500, 70);
        eliDuasTxt.setFont(new Font("Montserrat", Font.BOLD, 25));
        eliDuasTxt.setForeground(Color.WHITE);
        corFundo.add(eliDuasTxt);

        // Visores (contadores de acertos/erros) - sem alterações na funcionalidade aqui
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

        // Botão "Parar" o jogo
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
                    dispose();
                    acaoAoEncerrarQuestao.run(); 
                }
            }
        });

        parar.setBounds(1404, 500, 60, 60);
        var txtParar = new JLabel("Parar");
        txtParar.setBounds(1410, 540, 500, 70);
        txtParar.setFont(new Font("Montserrat", Font.BOLD, 19));
        txtParar.setForeground(Color.WHITE);
        corFundo.add(txtParar);

        // Alternativas visuais (círculos A, B, C, D)
        var circulo = new ImageIcon("assets\\circuloAlternativa.png");
        var imgCirculo = circulo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        
        // Chamadas para o método auxiliar que adiciona os círculos
        adicionarCirculoAlternativa(corFundo, imgCirculo, 220, 215, "A"); 
        adicionarCirculoAlternativa(corFundo, imgCirculo, 220, 325, "B");
        adicionarCirculoAlternativa(corFundo, imgCirculo, 220, 440, "C");
        adicionarCirculoAlternativa(corFundo, imgCirculo, 220, 540, "D");

    } // Faltava esta chave para fechar o construtor!

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
                        if (altSelecionada.equals(questaoAtual.getAlternativaCorreta())) { 
                            JOptionPane.showMessageDialog(null, "Resposta Correta!", "Acerto", JOptionPane.INFORMATION_MESSAGE);
                            dispose(); 
                            respostaCorretaAction.run(); 
                        } else {
                            JOptionPane.showMessageDialog(null, "Resposta Incorreta! A correta era: " + questaoAtual.getTextoAlternativaCorreta(), "Erro", JOptionPane.ERROR_MESSAGE);
                            dispose(); 
                            respostaIncorretaAction.run(); 
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Erro interno: Questão ou alternativa correta não carregada corretamente para verificação.", "Erro de Lógica", JOptionPane.ERROR_MESSAGE);
                        dispose(); 
                    }
                }
            }
        });
    }
    
    private void adicionarCirculoAlternativa(JPanel panel, Image img, int x, int y, String letra) {
        var circuloLabel = new JLabel(new ImageIcon(img));
        panel.add(circuloLabel);
        circuloLabel.setBounds(x, y, 50, 50); 

        var letraLabel = new JLabel(letra);
        letraLabel.setBounds(11, 0, 50, 50); 
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
    
        caixaPerg.setText("<html>" + novaQuestao.getEnunciado() + "</html>"); 

        for (JLabel label : labelsRespostas) {
            label.setVisible(true);
        }
    
        List<String> textosAlternativas = novaQuestao.getTextosAlternativas(true); 
        
        while (textosAlternativas.size() < 4) {
            textosAlternativas.add(" "); 
        }

        for (int i = 0; i < labelsRespostas.size(); i++) {
            labelsRespostas.get(i).setText("<html>" + textosAlternativas.get(i) + "</html>");
        }
    } 
} 