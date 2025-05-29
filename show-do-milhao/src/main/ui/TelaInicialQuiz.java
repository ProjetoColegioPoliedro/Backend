//Tela de menu inicial
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaInicialQuiz {
    public TelaInicialQuiz() {
        // Cor de fundo 
        Color roxoEscuro = new Color(20, 20, 50);
        Color rosa = new Color(255, 64, 96);
        Color branco = Color.WHITE;

        JFrame tela = new JFrame("Quiz da Fortuna");
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setSize(710, 800);
        tela.setExtendedState(Frame.MAXIMIZED_BOTH);
        tela.setLocationRelativeTo(null);
        
        JPanel corFundo = new JPanel(null);
        corFundo.setBackground(roxoEscuro);
        tela.setContentPane(corFundo);
        
        JPanel painelMenor = new JPanel(null);
        painelMenor.setSize(500, 550);
        painelMenor.setOpaque(false);
        corFundo.add(painelMenor);
        
        ImageIcon logo = new ImageIcon("../assets/logo.png");
        Image imagemOriginal = logo.getImage();

        int larguraOriginal = logo.getIconWidth();
        int alturaOriginal = logo.getIconHeight();
        int novaLargura = 400;
        int novaAltura = (novaLargura * alturaOriginal) / larguraOriginal;
        Image logoRedimensionada = imagemOriginal.getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);

        JLabel logoLabel = new JLabel(new ImageIcon(logoRedimensionada));
        corFundo.add(logoLabel);
        logoLabel.setBounds(0, 0, novaLargura, novaAltura); 

        tela.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evento) {
                int frameWidth = tela.getWidth();
                int frameHeight = tela.getHeight();

                // Centralizar logo
                int logoX = (frameWidth - novaLargura) / 2;
                int logoY = 20;
                logoLabel.setLocation(logoX, logoY);

                // Centralizar painelMenor
                int painelMenorWidth = painelMenor.getWidth();
                int painelMenorHeight = painelMenor.getHeight();
                int painelX = (frameWidth - painelMenorWidth) / 2;
                int painelY = (frameHeight - painelMenorHeight) / 2 + 50;
                painelMenor.setLocation(painelX, painelY);
            }
        });

        //botões login e cadastro
        BotaoArredondado login = new BotaoArredondado("Login", rosa, branco);
        login.setBounds(100, 250, 300, 60);
        painelMenor.add(login);
        login.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tela.dispose();
                new TelaLogin();
            }
        });

        BotaoArredondado cadastro = new BotaoArredondado("Cadastre-se", rosa, branco);
        cadastro.setBounds(100, 330, 300, 60);
        painelMenor.add(cadastro);
        cadastro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        cadastro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tela.dispose();
                new TelaCadastro();
            }
        });

        // Botão de engrenagem
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
        corFundo.add(btnEngrenagem);
        btnEngrenagem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Centralizar
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

                // Engrenagem
                btnEngrenagem.setLocation(frameWidth - larguraBotao - margemDireita, margemSuperior);
            }
        });

        tela.setVisible(true);
    }

    public static void main(String[] args) {
        new TelaInicialQuiz();
    }
}

class BotaoArredondado extends JButton {
    private int arcWidth = 45;
    private int arcHeight = 45;
    private Color corFundo;
    private Color corTexto;

    public BotaoArredondado(String texto, Color corFundo, Color corTexto) {
        super(texto);
        this.corFundo = corFundo;
        this.corTexto = corTexto;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(corTexto);
        setFont(new Font("Arial", Font.BOLD, 28));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(corFundo);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
    }

    @Override
    public boolean isOpaque() {
        return false;
    }
}