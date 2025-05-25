import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaRecuperarSenha {

    public TelaRecuperarSenha() {
        criarTelaRecuperar();
    }

    private void criarTelaRecuperar() {
        Color roxoEscuro = new Color(20, 20, 50);
        Color rosa = new Color(255, 64, 96);
        Color branco = Color.WHITE;
        Color cinzaCampo = new Color(230, 230, 230);
        Font fonteTexto = new Font("Arial", Font.PLAIN, 20);
        Font fonteTitulo = new Font("Arial", Font.BOLD, 28);

        JFrame tela = new JFrame("Recuperar Senha");
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel corFundo = new JPanel(null);
        corFundo.setBackground(roxoEscuro);
        tela.setContentPane(corFundo);

        RoundedPanel painelMenor = new RoundedPanel(30);
        painelMenor.setLayout(null);
        painelMenor.setBackground(branco);
        painelMenor.setBounds(0, 0, 500, 530);
        corFundo.add(painelMenor);

        // Logo
        ImageIcon logoIcon = new ImageIcon("../assets/logo-poliedro.png");
        Image imagemOriginal = logoIcon.getImage();
        int novaLargura = 180;
        int novaAltura = (novaLargura * logoIcon.getIconHeight()) / logoIcon.getIconWidth();
        Image imagemRedimensionada = imagemOriginal.getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(imagemRedimensionada));
        logo.setBounds((500 - novaLargura) / 2, 60, novaLargura, novaAltura);
        painelMenor.add(logo);

        // Título
        JLabel titulo = new JLabel("Redefinir senha");
        titulo.setFont(fonteTitulo);
        titulo.setBounds(50, 160, 330, 30);
        painelMenor.add(titulo);

        // Texto campo
        JLabel emailLabel = new JLabel("E-mail");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setBounds(50, 210, 300, 20);
        painelMenor.add(emailLabel);

        // Campo email
        RoundedTextField campoEmail = new RoundedTextField(30);
        campoEmail.setBounds(50, 235, 400, 45);
        campoEmail.setFont(fonteTexto);
        campoEmail.setBackground(cinzaCampo);
        campoEmail.setPlaceholder("Digite o e-mail cadastrado");
        campoEmail.setMargin(new Insets(0, 10, 0, 0));
        campoEmail.setFocusable(false);
        campoEmail.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                campoEmail.setFocusable(true);
                campoEmail.requestFocusInWindow();
            }
        });

        painelMenor.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                painelMenor.requestFocusInWindow();
            }
        });
        painelMenor.add(campoEmail);

        // Botão enviar
        BotaoArredondado btnEnviar = new BotaoArredondado("Enviar", rosa, branco);
        btnEnviar.setBounds(150, 295, 200, 50);
        btnEnviar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        painelMenor.add(btnEnviar);

        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = campoEmail.getText();
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(tela, "Digite o e-mail para recuperar a senha.", "Campo vazio", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(tela, "Se o e-mail estiver cadastrado, enviaremos um link de recuperação.", "Recuperação", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        corFundo.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                corFundo.requestFocusInWindow();
            }
        });

        painelMenor.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                painelMenor.requestFocusInWindow();
            }
        });

        // engrenagem
        int larguraBotao = 40;
        int alturaBotao = 40;
        int margemSuperior = 20;
        int margemDireita = 30;

        ImageIcon engrenagemIcone = new ImageIcon("../assets/icon-engrenagem.png");
        Image engrenagemImg = engrenagemIcone.getImage().getScaledInstance(larguraBotao, alturaBotao, Image.SCALE_SMOOTH);
        JButton btnEngrenagem = new JButton(new ImageIcon(engrenagemImg));
        btnEngrenagem.setBounds(tela.getWidth() - larguraBotao - margemDireita, margemSuperior, larguraBotao, alturaBotao);
        btnEngrenagem.setContentAreaFilled(false);
        btnEngrenagem.setBorderPainted(false);
        btnEngrenagem.setFocusPainted(false);
        btnEngrenagem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        corFundo.add(btnEngrenagem);

        // Centraliza o painel menor
        tela.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evento) {
                int frameWidth = tela.getWidth();
                int frameHeight = tela.getHeight();
                painelMenor.setLocation((frameWidth - painelMenor.getWidth()) / 2, (frameHeight - painelMenor.getHeight()) / 2);
                btnEngrenagem.setLocation(frameWidth - larguraBotao - margemDireita, margemSuperior);
            }
        });

        tela.setVisible(true);
    }

    public static void main(String[] args) {
        new TelaRecuperarSenha();
    }

    public class RoundedPanel extends JPanel {
        private int arc;

        public RoundedPanel(int arc) {
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public class RoundedTextField extends JTextField {
        private int arc;

        public RoundedTextField(int arc) {
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
        }

        public void setPlaceholder(String text) {
            setText(text);
            setForeground(Color.GRAY);
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (getText().equals(text)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }

                public void focusLost(java.awt.event.FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(text);
                        setForeground(Color.GRAY);
                    }
                }
            });
        }
    }

    public class RoundedPasswordField extends JPasswordField {
        private int arc;

        public RoundedPasswordField(int arc) {
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
        }

        public void setPlaceholder(String text) {
            setEchoChar((char) 0);
            setText(text);
            setForeground(Color.GRAY);
            addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (new String(getPassword()).equals(text)) {
                        setText("");
                        setEchoChar('•');
                        setForeground(Color.BLACK);
                    }
                }

                public void focusLost(java.awt.event.FocusEvent e) {
                    if (new String(getPassword()).isEmpty()) {
                        setEchoChar((char) 0);
                        setText(text);
                        setForeground(Color.GRAY);
                    }
                }
            });
        }
    }
}