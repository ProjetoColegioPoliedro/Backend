import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TelaEscolhaMateria {
    public TelaEscolhaMateria() {
        criarTela();
    }

    private void criarTela() {
        Color roxoEscuro = new Color(20, 20, 50);
        Color rosa = new Color(255, 64, 96);
        Color verde = new Color(0, 160, 0);
        Color branco = Color.WHITE;
        Font fonteTexto = new Font("Arial", Font.BOLD, 22);
        Font fonteTitulo = new Font("Arial", Font.BOLD, 28);

        JFrame tela = new JFrame("Escolha as matérias");
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel corFundo = new JPanel(null);
        corFundo.setBackground(roxoEscuro);
        tela.setContentPane(corFundo);

        RoundedPanel painelCentral = new RoundedPanel(30);
        painelCentral.setLayout(null);
        painelCentral.setBackground(branco);
        painelCentral.setBounds(0, 0, 550, 330);
        corFundo.add(painelCentral);

        JLabel titulo = new JLabel("Escolha as matérias:", SwingConstants.CENTER);
        titulo.setFont(fonteTitulo);
        titulo.setBounds(0, 30, painelCentral.getWidth(), 40);
        painelCentral.add(titulo);

        String[] materias = {
            "Português", "Matemática", "Inglês",
            "Ciências da Natureza", "Ciências Humanas"
        };

        ArrayList<JLabel> listaMaterias = new ArrayList<>();
        int y = 90;
        for (String materia : materias) {
            JLabel label = new JLabel("<html><i>" + materia + "</i> <font color='green'>✔</font></html>");
            label.setFont(fonteTexto);
            label.setBounds(50, y, 450, 30);
            painelCentral.add(label);
            y += 45;
        }

        BotaoArredondado btnIniciar = new BotaoArredondado("Iniciar partida", rosa, branco);
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 18));
        btnIniciar.setBounds(320, 310, 180, 50);
        btnIniciar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        corFundo.add(btnIniciar);

        ImageIcon engrenagemIcone = new ImageIcon("../assets/icon-engrenagem.png");
        Image engrenagemImg = engrenagemIcone.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JButton btnEngrenagem = new JButton(new ImageIcon(engrenagemImg));
        btnEngrenagem.setBounds(tela.getWidth() - 70, 20, 40, 40);
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
                int painelWidth = painelCentral.getWidth();
                int painelHeight = painelCentral.getHeight();
                
                int x = (frameWidth - painelWidth) / 2;
                int y = (frameHeight - painelHeight) / 2;
                painelCentral.setLocation(x, y);

                btnEngrenagem.setLocation(frameWidth - 70, 20);

                int margemDireita = 50;
                int margemInferior = 70;
                int larguraBotao = 180;
                int alturaBotao = 50;
                int margem = 40;
                btnIniciar.setBounds(
                    frameWidth - larguraBotao - margemDireita,
                    frameHeight - alturaBotao - margemInferior,
                    larguraBotao,
                    alturaBotao
                );

                btnIniciar.setLocation(
                    frameWidth - btnIniciar.getWidth() - margemDireita,
                    frameHeight - btnIniciar.getHeight() - margemInferior);

            }
        });

        tela.setVisible(true);
    }

    public static void main(String[] args) {
        new TelaEscolhaMateria();
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