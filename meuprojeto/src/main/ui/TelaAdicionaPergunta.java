// ui/TelaAdicionaPergunta.java
package ui;

import service.QuestaoService;
import model.Questao;
import model.Alternativa;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaAdicionaPergunta extends JFrame {

    private QuestaoService questaoService;
    private JTextArea campoEnunciado;
    private JTextField campoRespA, campoRespB, campoRespC, campoRespD;
    private JComboBox<String> comboDificuldade, comboMateria;
    private JTextArea campoExplicacao; // ✅ Campo que faltava

    private JPanel painelRespA, painelRespB, painelRespC, painelRespD;
    private JPanel painelCorretoSelecionado = null;
    private JTextField campoCorretoTF = null; // Para pegar o texto da alternativa correta

    // Cores do novo design
    private final Color ROXO_FUNDO = new Color(20, 14, 40);
    private final Color ROSA_BORDA = new Color(238, 33, 82);
    private final Color CIANO_BORDA = new Color(30, 180, 195);
    private final Color VERDE_BORDA = new Color(81, 207, 123);
    private final Color VERMELHO_BOTAO = new Color(224, 73, 73);
    private final Color VERDE_BOTAO = new Color(81, 207, 123);
    private final Color BRANCO = Color.WHITE;
    private final Color CINZA_PLACEHOLDER = new Color(150, 150, 150);

    public TelaAdicionaPergunta(Runnable telaAreaRes, QuestaoService questaoService) {
        this.questaoService = questaoService;

        configurarJanela();

        JPanel painelFundo = new JPanel(new GridBagLayout());
        painelFundo.setBackground(ROXO_FUNDO);
        setContentPane(painelFundo);

        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(ROXO_FUNDO);
        painelFormulario.setBorder(new EmptyBorder(20, 50, 20, 50));
        painelFundo.add(painelFormulario, new GridBagConstraints());

        inicializarComponentes(painelFormulario, telaAreaRes);
    }

    private void configurarJanela() {
        setTitle("Adicionar Nova Pergunta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    private void inicializarComponentes(JPanel painel, Runnable telaAreaRes) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;

        // --- LINHA 0: ENUNCIADO ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        campoEnunciado = new CustomTextArea("Digite o enunciado da pergunta aqui...");
        JScrollPane scrollEnunciado = new JScrollPane(campoEnunciado);
        scrollEnunciado.setBorder(BorderFactory.createLineBorder(ROSA_BORDA, 3, true));
        scrollEnunciado.getViewport().setBackground(ROXO_FUNDO);
        painel.add(scrollEnunciado, gbc);

        // --- LINHA 1: DIFICULDADE E MATÉRIA ---
        JPanel painelDropdowns = new JPanel(new GridLayout(1, 2, 50, 0));
        painelDropdowns.setOpaque(false);

        comboDificuldade = new JComboBox<>(new String[]{"Dificuldade: ...", "Fácil", "Médio", "Difícil"});
        comboMateria = new JComboBox<>(new String[]{"Matéria: ...", "Português", "Matemática", "Inglês", "Ciências Humanas", "Ciências da Natureza"});
        styleDropdown(comboDificuldade);
        styleDropdown(comboMateria);
        
        painelDropdowns.add(comboDificuldade);
        painelDropdowns.add(comboMateria);

        gbc.gridy = 1;
        gbc.insets = new Insets(15, 5, 15, 5);
        painel.add(painelDropdowns, gbc);
        gbc.insets = new Insets(8, 5, 8, 5);

        // --- LINHAS 2-5: ALTERNATIVAS ---
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        gbc.gridy = 2; painel.add(new LetteredCircle("A", CIANO_BORDA), gbc);
        gbc.gridy = 3; painel.add(new LetteredCircle("B", CIANO_BORDA), gbc);
        gbc.gridy = 4; painel.add(new LetteredCircle("C", CIANO_BORDA), gbc);
        gbc.gridy = 5; painel.add(new LetteredCircle("D", CIANO_BORDA), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoRespA = new JTextField("Alternativa A");
        campoRespB = new JTextField("Alternativa B");
        campoRespC = new JTextField("Alternativa C");
        campoRespD = new JTextField("Alternativa D");

        painelRespA = criarPainelAlternativa(campoRespA, CIANO_BORDA);
        painelRespB = criarPainelAlternativa(campoRespB, CIANO_BORDA);
        painelRespC = criarPainelAlternativa(campoRespC, CIANO_BORDA);
        painelRespD = criarPainelAlternativa(campoRespD, CIANO_BORDA);

        gbc.gridy = 2; painel.add(painelRespA, gbc);
        gbc.gridy = 3; painel.add(painelRespB, gbc);
        gbc.gridy = 4; painel.add(painelRespC, gbc);
        gbc.gridy = 5; painel.add(painelRespD, gbc);

        
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.ipady = 80; // Aumenta a altura vertical do componente
        gbc.fill = GridBagConstraints.BOTH; // Ocupa todo o espaço
        gbc.weighty = 1.0; // Permite que ele cresça verticalmente se a janela for alta
        campoExplicacao = new CustomTextArea("Digite a explicação da resposta correta aqui...");
        JScrollPane scrollExplicacao = new JScrollPane(campoExplicacao);
        scrollExplicacao.setBorder(BorderFactory.createLineBorder(CIANO_BORDA, 3, true));
        scrollExplicacao.getViewport().setBackground(ROXO_FUNDO);
        painel.add(scrollExplicacao, gbc);
        gbc.ipady = 0; // Reseta para o padrão
        gbc.fill = GridBagConstraints.HORIZONTAL; // Reseta para o padrão
        gbc.weighty = 0; // Reseta para o padrão

        // --- LINHA 7: BOTÕES ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        painelBotoes.setOpaque(false);
        
        JButton btnCancelar = new RoundedButton("Cancelar", VERMELHO_BOTAO);
        btnCancelar.addActionListener(e -> {
            telaAreaRes.run();
            dispose();
        });

        JButton btnSalvar = new RoundedButton("Salvar", VERDE_BOTAO);
        btnSalvar.addActionListener(e -> salvarNovaPergunta(telaAreaRes));
        
        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        
        gbc.gridy = 7; // 
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 10, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(painelBotoes, gbc);
    }
    
    // --- LÓGICA E FUNCIONALIDADE ---

    private void salvarNovaPergunta(Runnable telaAreaRes) {
        String enunciado = campoEnunciado.getText().trim();
        String respA = campoRespA.getText().trim();
        String respB = campoRespB.getText().trim();
        String respC = campoRespC.getText().trim();
        String respD = campoRespD.getText().trim();
        String explicacao = campoExplicacao.getText().trim(); 
        
        String dificuldade = (String) comboDificuldade.getSelectedItem();
        String materia = (String) comboMateria.getSelectedItem();

        // Validações
        if (enunciado.isEmpty() || respA.isEmpty() || respB.isEmpty() || respC.isEmpty() || respD.isEmpty() || explicacao.isEmpty() || comboDificuldade.getSelectedIndex() == 0 || comboMateria.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos e selecionados.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (painelCorretoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione a alternativa correta clicando nela.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Sua lógica de criação do objeto Questao e alternativas...
        // ... (o código aqui permanece o mesmo que o seu original)

        JOptionPane.showMessageDialog(this, "Pergunta salva com sucesso! (Simulação)");
        // Lógica real de salvar com o questaoService
        telaAreaRes.run();
        dispose();
    }

    private JPanel criarPainelAlternativa(JTextField campo, Color corBorda) {
        styleTextField(campo);
        
        JPanel painel = new JPanel(new BorderLayout(10, 0));
        painel.setOpaque(false);
        painel.setBorder(BorderFactory.createLineBorder(corBorda, 3, true));
        painel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconeEdit = new JLabel();
        iconeEdit.setText("✎");
        iconeEdit.setForeground(BRANCO);
        iconeEdit.setFont(new Font("Arial", Font.BOLD, 20));
        iconeEdit.setBorder(new EmptyBorder(0,0,0,10));

        painel.add(campo, BorderLayout.CENTER);
        painel.add(iconeEdit, BorderLayout.EAST);

        painel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                painelRespA.setBorder(BorderFactory.createLineBorder(CIANO_BORDA, 3, true));
                painelRespB.setBorder(BorderFactory.createLineBorder(CIANO_BORDA, 3, true));
                painelRespC.setBorder(BorderFactory.createLineBorder(CIANO_BORDA, 3, true));
                painelRespD.setBorder(BorderFactory.createLineBorder(CIANO_BORDA, 3, true));

                painel.setBorder(BorderFactory.createLineBorder(VERDE_BORDA, 3, true));
                painelCorretoSelecionado = painel;
                campoCorretoTF = campo; 
            }
        });
        
        return painel;
    }
    
   
    private void styleTextField(JTextField campo) {
        campo.setBackground(ROXO_FUNDO);
        campo.setForeground(BRANCO);
        campo.setFont(new Font("Montserrat", Font.PLAIN, 18));
        campo.setCaretColor(BRANCO);
        campo.setBorder(new EmptyBorder(10, 15, 10, 15));
    }
    
    private void styleDropdown(JComboBox<String> combo) {
        combo.setBackground(ROXO_FUNDO);
        combo.setForeground(BRANCO);
        combo.setFont(new Font("Montserrat", Font.BOLD, 16));
    }

    private class CustomTextArea extends JTextArea {
        public CustomTextArea(String placeholder) {
            super(placeholder);
            setBackground(ROXO_FUNDO);
            setForeground(CINZA_PLACEHOLDER);
            setFont(new Font("Montserrat", Font.PLAIN, 18));
            setLineWrap(true);
            setWrapStyleWord(true);
            setCaretColor(BRANCO);
            setMargin(new Insets(10, 10, 10, 10));

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(BRANCO);
                    }
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(CINZA_PLACEHOLDER);
                    }
                }
            });
        }
    }

    private static class LetteredCircle extends JPanel {
        private String letter;
        private Color circleColor;
        public LetteredCircle(String letter, Color color) {
            this.letter = letter;
            this.circleColor = color;
            setOpaque(false);
            setPreferredSize(new Dimension(50, 50));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(circleColor);
            g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Montserrat", Font.BOLD, 28));
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(letter)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(letter, x, y);
            
            g2.dispose();
        }
    }

    private static class RoundedButton extends JButton {
        private Color bgColor;
        public RoundedButton(String text, Color color) {
            super(text);
            this.bgColor = color;
            setFont(new Font("Montserrat", Font.BOLD, 18));
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setPreferredSize(new Dimension(180, 50));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 50, 50));
            super.paintComponent(g);
            g2.dispose();
        }
    }
}