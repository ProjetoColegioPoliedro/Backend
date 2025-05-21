import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaAreaRestrita extends JFrame {
    TelaAreaRestrita() {
        var cinza = new Color(217, 217, 217);
        var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40);

        // Criação da tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(710, 800);
        setLocationRelativeTo(null);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        
        var corFundo = new JPanel(null);
        add(corFundo);
        corFundo.setBackground(roxo);
        corFundo.setSize(800, 800);
        
        var painelBranco = new JPanel(null);
        painelBranco.setBackground(cinza);
        painelBranco.setBounds(100, 100, 500, 550);
        corFundo.add(painelBranco, BorderLayout.CENTER);
        
        // Adicionando imagens 
        var icone = new ImageIcon("image.png");
        var poliedro = icone.getImage().getScaledInstance(140, 70, Image.SCALE_SMOOTH);
        var imagemPoliedro = new JLabel(new ImageIcon(poliedro));
        painelBranco.add(imagemPoliedro);
        imagemPoliedro.setBounds(170, 21, 150, 80);
        
        var iconeConfig = new ImageIcon("settings.png");
        var image = iconeConfig.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        var config = new JLabel(new ImageIcon(image));
        corFundo.add(config);
        config.setBounds(1460, 20, 60, 60);
        config.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                var tC = new TelaConfiguracoes();
                tC.setVisible(true);
                dispose();
            }
        });
        
        // Ajuste nas dimensões
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evento) {
                int frameWidth = getWidth();
                int frameHeight = getHeight();
                int painelBrancoWidth = painelBranco.getWidth();
                int painelBrancoHeight = painelBranco.getHeight();
                int x = (frameWidth - painelBrancoWidth) / 2;
                int y = (frameHeight - painelBrancoHeight) / 2;
                painelBranco.setLocation(x, y);

                int margemDireita = 30;
                int margemSuperior = 20;
                config.setBounds(
                frameWidth - 60 - margemDireita,
                margemSuperior,
                60, 60);
            }
        });

        
        // Botões
        var addPergunta = new JButton("Adicionar pergunta");
        painelBranco.add(addPergunta);
        addPergunta.setBounds(125, 120, 250, 70);
        addPergunta.setBackground(rosa);
        addPergunta.setForeground(Color.WHITE);
        addPergunta.setFont(new Font("Montserrat", Font.BOLD, 18));
        addPergunta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent mudaTela){
                var tAP = new TelaAdicionaPerguntas();
                tAP.setVisible(true);
                dispose();
            }
        });

        var edPergunta = new JButton("Editar pergunta");
        painelBranco.add(edPergunta);
        edPergunta.setBounds(125, 240, 250, 70);
        edPergunta.setFont(new Font("Montserrat", Font.BOLD, 18));
        edPergunta.setBackground(rosa);
        edPergunta.setForeground(Color.WHITE);
        edPergunta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent novaTela){
                var tEP = new TelaEditaPergunta();
                tEP.setVisible(true);
                dispose();
            }
        });

        var conRank = new JButton("Consultar ranking");
        painelBranco.add(conRank);
        conRank.setFont(new Font("Montserrat", Font.BOLD, 18));
        conRank.setBounds(125, 360, 250, 70);
        conRank.setBackground(rosa);
        conRank.setForeground(Color.WHITE);
        conRank.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent mudaTela) {
                var tCR = new TelaConsultaRanking();
                tCR.setVisible(true);
                dispose();
            }
        });
        
        // var ativAlunos = new JButton("Cadastrar aluno");
        // painelBranco.add(ativAlunos);
        // ativAlunos.setFont(new Font("Montserrat", Font.BOLD, 18));
        // ativAlunos.setBounds(125, 390, 250, 70);
        // ativAlunos.setBackground(rosa);
        // ativAlunos.setForeground(Color.WHITE);

        var voltar = new JButton("Voltar");
        painelBranco.add(voltar);
        voltar.setFont(new Font("Montserrat", Font.PLAIN, 18));
        voltar.setBounds(190, 470, 110, 30);
        voltar.setBackground(Color.BLACK);
        voltar.setForeground(Color.WHITE);
        voltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent voltarTela) {
                var tMIA = new TelaMenuInicialAdmin();
                tMIA.setVisible(true);
                dispose();
            }
        });

        setVisible(true);
    }
}
