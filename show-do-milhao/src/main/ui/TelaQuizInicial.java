import javax.swing.*;
import java.awt.*;

public class TelaQuizInicial {

    public TelaQuizInicial() {
        JFrame tela = new JFrame("Quiz da Fortuna");
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel painelPrincipal = new JPanel(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int largura = screenSize.width;
        int altura = screenSize.height;
        int larguraEsquerda = 750;

        // Painel amarelo
        JPanel painelEsquerdo = new JPanel(null);
        painelEsquerdo.setBackground(new Color(255, 221, 0));
        painelEsquerdo.setBounds(0, 0, larguraEsquerda, altura);
        painelPrincipal.add(painelEsquerdo);

        // Painel roxo escuro
        JPanel painelDireito = new JPanel(null);
        painelDireito.setBackground(new Color(20, 20, 50));
        painelDireito.setBounds(larguraEsquerda, 0, largura - larguraEsquerda, altura);
        painelPrincipal.add(painelDireito);

        // Logo
        ImageIcon logoIcon = new ImageIcon("../assets/logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(50, 50, logoIcon.getIconWidth(), logoIcon.getIconHeight());
        painelEsquerdo.add(logoLabel);

        // Logo Poliedro
        ImageIcon poliedroIcon = new ImageIcon("../assets/logo-poliedro-2.png");
        JLabel poliedroLabel = new JLabel(poliedroIcon);
        poliedroLabel.setBounds(580, altura - 200, poliedroIcon.getIconWidth(), poliedroIcon.getIconHeight());
        painelEsquerdo.add(poliedroLabel);

        // Botões centralizados no painel roxo
        Color rosa = new Color(255, 64, 96);
        Color branco = Color.WHITE;

        int larguraBotao = 300;
        int alturaBotao = 60;
        int xCentral = ((largura - larguraEsquerda) - larguraBotao) / 2;
        
        BotaoArredondado jogar = new BotaoArredondado("Jogar", rosa, branco);
        jogar.setBounds(xCentral, 380, larguraBotao, alturaBotao);
        painelDireito.add(jogar);
        jogar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // jogar.addActionListener(new ActionListener()) {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         tela.dispose();
        //         new TelaJogar();
        //     }
        // }

        BotaoArredondado historico = new BotaoArredondado("Histórico de jogo", rosa, branco);
        historico.setBounds(xCentral, 460, larguraBotao, alturaBotao);
        painelDireito.add(historico);
        historico.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // historico.addActionListener(new ActionListener()) {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         tela.dispose();
        //         new TelaHistorico();
        //     }
        // }

        // Engrenagem
        ImageIcon engrenagemIcon = new ImageIcon("../assets/icon-engrenagem.png");
        Image engrenagemImg = engrenagemIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton btnEngrenagem = new JButton(new ImageIcon(engrenagemImg));
        btnEngrenagem.setBounds((largura - larguraEsquerda) - 60, 20, 40, 40);
        btnEngrenagem.setContentAreaFilled(false);
        btnEngrenagem.setBorderPainted(false);
        btnEngrenagem.setFocusPainted(false);
        painelDireito.add(btnEngrenagem);

        tela.setContentPane(painelPrincipal);
        tela.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaQuizInicial());
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
        setFont(new Font("Arial", Font.BOLD, 24));
        setHorizontalAlignment(SwingConstants.CENTER);
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
    public boolean isOpaque() {
        return false;
    }
}