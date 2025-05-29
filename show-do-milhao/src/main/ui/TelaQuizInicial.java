import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaQuizInicial {
    private static final int LARGURA_ESQUERDA = 800;

    public TelaQuizInicial() {
        JFrame tela = new JFrame("Quiz da Fortuna");
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel painelPrincipal = new JPanel(null);
        tela.setContentPane(painelPrincipal);

        Color amarelo = new Color(255, 221, 0);
        Color roxoEscuro = new Color(20, 20, 50);
        Color rosa = new Color(255, 64, 96);
        Color branco = Color.WHITE;

        JPanel painelEsquerdo = new JPanel(null);
        painelEsquerdo.setBackground(amarelo);
        painelPrincipal.add(painelEsquerdo);

        JPanel painelDireito = new JPanel(null);
        painelDireito.setBackground(roxoEscuro);
        painelPrincipal.add(painelDireito);

        ImageIcon logoIcon = new ImageIcon("../assets/logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        painelEsquerdo.add(logoLabel);

        ImageIcon poliedroIconOriginal = new ImageIcon("../assets/logo-poliedro-2.png");
        Image imagemPoliedroOriginal = poliedroIconOriginal.getImage();
        int novaLarguraPoliedro = 70;
        int novaAlturaPoliedro = (novaLarguraPoliedro * poliedroIconOriginal.getIconHeight()) / poliedroIconOriginal.getIconWidth();
        Image imagemPoliedroRedimensionada = imagemPoliedroOriginal.getScaledInstance(novaLarguraPoliedro, novaAlturaPoliedro, Image.SCALE_SMOOTH);
        ImageIcon poliedroIcon = new ImageIcon(imagemPoliedroRedimensionada);
        JLabel poliedroLabel = new JLabel(poliedroIcon);
        painelEsquerdo.add(poliedroLabel);

        BotaoArredondado jogar = new BotaoArredondado("Jogar", rosa, branco);
        painelDireito.add(jogar);
        jogar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        BotaoArredondado historico = new BotaoArredondado("Histórico de jogo", rosa, branco);
        painelDireito.add(historico);
        historico.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ImageIcon engrenagemIcon = new ImageIcon("../assets/icon-engrenagem.png");
        Image engrenagemImg = engrenagemIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton btnEngrenagem = new JButton(new ImageIcon(engrenagemImg));
        btnEngrenagem.setContentAreaFilled(false);
        btnEngrenagem.setBorderPainted(false);
        btnEngrenagem.setFocusPainted(false);
        painelDireito.add(btnEngrenagem);
        btnEngrenagem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        tela.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int largura = tela.getWidth();
                int altura = tela.getHeight();

                painelEsquerdo.setBounds(0, 0, LARGURA_ESQUERDA, altura);
                painelDireito.setBounds(LARGURA_ESQUERDA, 0, largura - LARGURA_ESQUERDA, altura);

                int logoX = (LARGURA_ESQUERDA - logoIcon.getIconWidth()) / 2;
                logoLabel.setBounds(logoX, 50, logoIcon.getIconWidth(), logoIcon.getIconHeight());

                int poliedroX = LARGURA_ESQUERDA - poliedroIcon.getIconWidth() - 20;
                int poliedroY = altura - poliedroIcon.getIconHeight() - 55;
                poliedroLabel.setBounds(poliedroX, poliedroY, poliedroIcon.getIconWidth(), poliedroIcon.getIconHeight());

                int larguraBotao = 300;
                int alturaBotao = 60;
                int xCentral = (largura - LARGURA_ESQUERDA - larguraBotao) / 2;
                int painelDireitoOffsetY = altura / 2 - 70;

                jogar.setBounds(xCentral, painelDireitoOffsetY, larguraBotao, alturaBotao);
                historico.setBounds(xCentral, painelDireitoOffsetY + 80, larguraBotao, alturaBotao);
                btnEngrenagem.setBounds((largura - LARGURA_ESQUERDA) - 60, 20, 40, 40);
            }
        });

        tela.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaQuizInicial());
    }
}