import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaCadastro {
    public TelaCadastro() {
        criarTelaCadastro();
    }

    private void criarTelaCadastro() {
        Color roxoEscuro = new Color(20, 20, 50);
        Color rosa = new Color(255, 64, 96);
        Color branco = Color.WHITE;
        Color cinzaCampo = new Color(230, 230, 230);
        Font fonteTexto = new Font("Arial", Font.PLAIN, 20);
        Font fonteTitulo = new Font("Arial", Font.BOLD, 26);

        JFrame tela = new JFrame("Cadastro");
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

        ImageIcon logoIcon = new ImageIcon("../assets/logo-poliedro.png");
        Image imagemOriginal = logoIcon.getImage();
        int novaLargura = 180;
        int novaAltura = (novaLargura * logoIcon.getIconHeight()) / logoIcon.getIconWidth();
        Image imagemRedimensionada = imagemOriginal.getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(imagemRedimensionada));
        logo.setBounds((500 - novaLargura) / 2, 60, novaLargura, novaAltura);
        painelMenor.add(logo);

        JLabel titulo = new JLabel("Cadastre-se");
        titulo.setFont(fonteTitulo);
        titulo.setBounds(50, 135, 400, 35);
        painelMenor.add(titulo);

        JLabel usuarioLabel = new JLabel("Usuário");
        usuarioLabel.setBounds(50, 185, 300, 20);
        painelMenor.add(usuarioLabel);

        RoundedTextField campoUsuario = new RoundedTextField(30);
        campoUsuario.setBounds(50, 210, 400, 45);
        campoUsuario.setFont(fonteTexto);
        campoUsuario.setBackground(cinzaCampo);
        campoUsuario.setPlaceholder("Digite seu nome de usuário");
        campoUsuario.setMargin(new Insets(0, 10, 0, 0));
        painelMenor.add(campoUsuario);

        JLabel emailLabel = new JLabel("E-mail");
        emailLabel.setBounds(50, 270, 300, 20);
        painelMenor.add(emailLabel);

        RoundedTextField campoEmail = new RoundedTextField(30);
        campoEmail.setBounds(50, 290, 400, 45);
        campoEmail.setFont(fonteTexto);
        campoEmail.setBackground(cinzaCampo);
        campoEmail.setPlaceholder("Digite seu e-mail");
        campoEmail.setMargin(new Insets(0, 10, 0, 0));
        painelMenor.add(campoEmail);

        JLabel senhaLabel = new JLabel("Senha");
        senhaLabel.setBounds(50, 350, 300, 20);
        painelMenor.add(senhaLabel);

        RoundedPasswordField campoSenha = new RoundedPasswordField(30);
        campoSenha.setBounds(50, 370, 400, 45);
        campoSenha.setFont(fonteTexto);
        campoSenha.setBackground(cinzaCampo);
        campoSenha.setPlaceholder("Digite sua senha");
        campoSenha.setMargin(new Insets(0, 10, 0, 0));
        painelMenor.add(campoSenha);

        BotaoArredondado btnCancelar = new BotaoArredondado("Cancelar", rosa, branco);
        btnCancelar.setBounds(50, 445, 170, 50);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        painelMenor.add(btnCancelar);

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tela.dispose();
                new TelaInicialQuiz();
            }
        });

        BotaoArredondado btnCadastrar = new BotaoArredondado("Cadastrar", rosa, branco);
        btnCadastrar.setBounds(280, 445, 170, 50);
        btnCadastrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        painelMenor.add(btnCadastrar);

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = campoUsuario.getText().trim();
                String email = campoEmail.getText().trim();
                String senha = new String(campoSenha.getPassword()).trim();
            }
        });

        int margemSuperior = 20;
        int margemDireita = 30;
        int larguraBotao = 40;
        int alturaBotao = 40;

        ImageIcon engrenagemIcone = new ImageIcon("../assets/icon-engrenagem.png");
        Image engrenagemImg = engrenagemIcone.getImage().getScaledInstance(larguraBotao, alturaBotao, Image.SCALE_SMOOTH);
        JButton btnEngrenagem = new JButton(new ImageIcon(engrenagemImg));
        btnEngrenagem.setBounds(tela.getWidth() - larguraBotao - margemDireita, margemSuperior, larguraBotao, alturaBotao);
        btnEngrenagem.setContentAreaFilled(false);
        btnEngrenagem.setBorderPainted(false);
        btnEngrenagem.setFocusPainted(false);
        btnEngrenagem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        corFundo.add(btnEngrenagem);

        tela.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evento) {
                int frameWidth = tela.getWidth();
                int frameHeight = tela.getHeight();
                int painelMenorWidth = painelMenor.getWidth();
                int painelMenorHeight = painelMenor.getHeight();
                int x = (frameWidth - painelMenorWidth) / 2;
                int y = (frameHeight - painelMenorHeight) / 2;
                painelMenor.setLocation(x, y);
                btnEngrenagem.setLocation(frameWidth - larguraBotao - margemDireita, margemSuperior);
            }
        });

        corFundo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                corFundo.requestFocusInWindow();
            }
        });

        tela.setVisible(true);
    }

    public static void main(String[] args) {
        new TelaCadastro();
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