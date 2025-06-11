package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class TelaLogin extends JFrame {

    private JTextField campoEmail;
    private JPasswordField campoSenha;

    
    private final Color ROXO_FUNDO = new Color(20, 14, 40);
    private final Color ROSA_BOTAO = new Color(238, 33, 82);
    private final Color CINZA_CAMPO = new Color(230, 230, 230);
    private final Color AZUL_LINK = new Color(0, 122, 255);
    private final Color BRANCO = Color.WHITE;
    private final Color PRETO = Color.BLACK;
    private final Color CINZA_PLACEHOLDER = Color.GRAY;

   
    public TelaLogin(Runnable recuperacaoSenha, Runnable configs, LoginAuthCallback authCallback) {

        // --- CONFIGURAÇÃO DA JANELA PRINCIPAL ---

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH); // Inicia em tela cheia

        // Painel de fundo que ocupa toda a tela
        JPanel painelFundo = new JPanel(new GridBagLayout());
        painelFundo.setBackground(ROXO_FUNDO);
        setContentPane(painelFundo);

        // --- ÍCONE DE CONFIGURAÇÕES ---
        // Fica no canto superior direito do painel de fundo
        JLabel iconeConfig = new JLabel(); 
        iconeConfig.setIcon(new ImageIcon("assets\\settings.png")); 
        iconeConfig.setForeground(BRANCO);
        iconeConfig.setFont(new Font("Arial", Font.BOLD, 24));
        iconeConfig.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iconeConfig.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                configs.run();
                dispose();
            }
        });

        // Posiciona o ícone no canto superior direito
        GridBagConstraints gbcIcone = new GridBagConstraints();
        gbcIcone.anchor = GridBagConstraints.NORTHEAST;
        gbcIcone.insets = new Insets(20, 0, 0, 20);
        gbcIcone.weightx = 1.0;
        gbcIcone.weighty = 1.0;
        gbcIcone.gridx = 1;
        gbcIcone.gridy = 0;
        painelFundo.add(iconeConfig, gbcIcone);


        // --- PAINEL DE LOGIN CENTRAL COM CANTOS ARREDONDADOS ---
        RoundedPanel painelLogin = new RoundedPanel(50, BRANCO); // 50 de raio de arredondamento
        painelLogin.setLayout(new GridBagLayout());
        painelLogin.setPreferredSize(new Dimension(450, 550));
        painelLogin.setMaximumSize(new Dimension(450, 550));
        painelLogin.setMinimumSize(new Dimension(450, 550));

        // Adiciona o painel de login ao painel de fundo (ele ficará centralizado)
        GridBagConstraints gbcPainelLogin = new GridBagConstraints();
        gbcPainelLogin.gridx = 0;
        gbcPainelLogin.gridy = 0;
        gbcPainelLogin.gridwidth = 2; // Ocupa as duas colunas do grid para centralizar
        painelFundo.add(painelLogin, gbcPainelLogin);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 40, 10, 40); // Espaçamento interno
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;


        // --- ELEMENTOS DENTRO DO PAINEL DE LOGIN ---

        
        gbc.gridy = 0;
        gbc.weighty = 0.2; // Dá um pouco de espaço no topo
        gbc.anchor = GridBagConstraints.PAGE_END;
        JLabel logoLabel = new JLabel("", SwingConstants.CENTER);
        logoLabel.setIcon(new ImageIcon("assets\\logo-poliedro.png")); 
        logoLabel.setFont(new Font("Montserrat", Font.BOLD, 20));
        painelLogin.add(logoLabel, gbc);

        // TÍTULO "Faça o seu login"
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titulo = new JLabel("Faça o seu login", SwingConstants.CENTER);
        titulo.setFont(new Font("Montserrat", Font.BOLD, 28));
        titulo.setForeground(ROXO_FUNDO);
        painelLogin.add(titulo, gbc);

        // LABEL E-MAIL
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(20, 40, 0, 40); // Menos espaço inferior
        JLabel labelEmail = new JLabel("E-mail");
        labelEmail.setFont(new Font("Montserrat", Font.BOLD, 14));
        painelLogin.add(labelEmail, gbc);

        // CAMPO E-MAIL
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 40, 10, 40);
        campoEmail = new RoundedTextField("Digite seu e-mail");
        painelLogin.add(campoEmail, gbc);

        // LABEL SENHA
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 40, 0, 40);
        JLabel labelSenha = new JLabel("Senha");
        labelSenha.setFont(new Font("Montserrat", Font.BOLD, 14));
        painelLogin.add(labelSenha, gbc);

        // CAMPO SENHA
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 40, 10, 40);
        campoSenha = new RoundedPasswordField("Digite sua senha");
        painelLogin.add(campoSenha, gbc);

        // BOTÃO ENTRAR
        gbc.gridy = 6;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(20, 100, 10, 100); // Mais espaçamento lateral para afinar
        gbc.fill = GridBagConstraints.BOTH;
        JButton btnEntrar = new RoundedButton("Entrar");
        btnEntrar.setBackground(ROSA_BOTAO);
        btnEntrar.setForeground(BRANCO);
        btnEntrar.setFont(new Font("Montserrat", Font.BOLD, 16));
        btnEntrar.addActionListener(e -> {
            String login = campoEmail.getText().trim();
            String senha = new String(campoSenha.getPassword());
            authCallback.authenticate(login, senha);
        });
        painelLogin.add(btnEntrar, gbc);

        // LINK ESQUECEU A SENHA
        gbc.gridy = 7;
        gbc.weighty = 0.2; // Empurra para baixo
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(5, 40, 10, 40);
        JLabel linkSenha = new JLabel("Esqueceu a senha?", SwingConstants.CENTER);
        linkSenha.setFont(new Font("Montserrat", Font.PLAIN, 13));
        linkSenha.setForeground(AZUL_LINK);
        linkSenha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkSenha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                recuperacaoSenha.run();
                dispose();
            }
        });
        painelLogin.add(linkSenha, gbc);
    }

    // --- CLASSES INTERNAS PARA COMPONENTES CUSTOMIZADOS ---

    // Painel com cantos arredondados
    private static class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;
        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false); // Para permitir o fundo customizado
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        }
    }

    // Botão com cantos arredondados
    private static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40));
            g2.setColor(getForeground());
            g2.drawString(getText(), (getWidth() - g2.getFontMetrics().stringWidth(getText())) / 2, (getHeight() + g2.getFontMetrics().getAscent()) / 2 - g2.getFontMetrics().getDescent());
            g2.dispose();
        }
    }
    
    // Campo de texto com cantos arredondados e placeholder
    private class RoundedTextField extends JTextField {
        private String placeholder;

        public RoundedTextField(String placeholder) {
            super();
            this.placeholder = placeholder;
            setFont(new Font("Montserrat", Font.PLAIN, 14));
            setOpaque(false);
            setBorder(new EmptyBorder(5, 15, 5, 15)); // Margem interna
            setForeground(CINZA_PLACEHOLDER);
            setText(placeholder);
            
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(PRETO);
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
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CINZA_CAMPO);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40));
            super.paintComponent(g);
            g2.dispose();
        }
    }
    
    // Campo de senha com cantos arredondados e placeholder
    private class RoundedPasswordField extends JPasswordField {
        private String placeholder;

        public RoundedPasswordField(String placeholder) {
            super();
            this.placeholder = placeholder;
            setFont(new Font("Montserrat", Font.PLAIN, 14));
            setOpaque(false);
            setBorder(new EmptyBorder(5, 15, 5, 15));
            setForeground(CINZA_PLACEHOLDER);
            setEchoChar((char) 0);
            setText(placeholder);

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (String.valueOf(getPassword()).equals(placeholder)) {
                        setText("");
                        setEchoChar('*');
                        setForeground(PRETO);
                    }
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (String.valueOf(getPassword()).isEmpty()) {
                        setText(placeholder);
                        setEchoChar((char) 0);
                        setForeground(CINZA_PLACEHOLDER);
                    }
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(CINZA_CAMPO);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40));
            super.paintComponent(g);
            g2.dispose();
        }
    }
}