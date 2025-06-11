package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaAreaRestrita extends JFrame {
    public TelaAreaRestrita(Runnable telaMenuAdmin, Runnable configs,
                            Runnable telaAdicionaquest, Runnable telaConsRank, Runnable telaCadastro) {
        var cinza = new Color(217, 217, 217);
        var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40);

        // Criação da tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(710, 800);
        setLocationRelativeTo(null);
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza a janela ao abrir

        var corFundo = new JPanel(null); // Painel de fundo com layout nulo para posicionamento manual
        add(corFundo);
        corFundo.setBackground(roxo);
        corFundo.setSize(800, 800); // Tamanho inicial, ajustado pelo setExtendedState

        var painelBranco = new JPanel(null); // Painel central para os botões
        painelBranco.setBackground(cinza);
        painelBranco.setBounds(100, 100, 500, 550); // Posição e tamanho fixos
        corFundo.add(painelBranco, BorderLayout.CENTER);

        // Adicionando imagem de poliedro
        var icone = new ImageIcon("assets\\image.png");
        var poliedro = icone.getImage().getScaledInstance(140, 70, Image.SCALE_SMOOTH);
        var imagemPoliedro = new JLabel(new ImageIcon(poliedro));
        painelBranco.add(imagemPoliedro);
        imagemPoliedro.setBounds(170, 21, 150, 80); // Posição dentro do painel branco

        // Adicionando ícone de configurações
        var iconeConfig = new ImageIcon("assets\\settings.png");
        var image = iconeConfig.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        var config = new JLabel(new ImageIcon(image));
        corFundo.add(config);
        config.setBounds(1460, 20, 60, 60); // Posição fixa no canto superior direito (assumindo tela grande)
        config.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                configs.run(); // Executa o Runnable de configurações
                dispose(); 
            }
        });
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
            }
        });       
        var addPergunta = new JButton("Adicionar pergunta");
        painelBranco.add(addPergunta);
        addPergunta.setBounds(125, 120, 250, 70); 
        addPergunta.setBackground(rosa);
        addPergunta.setForeground(Color.WHITE);
        addPergunta.setFont(new Font("Montserrat", Font.BOLD, 18));
        addPergunta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent mudaTela){
                telaAdicionaquest.run(); 
                dispose();
            }
        });       
        var conRank = new JButton("Consultar ranking");
        painelBranco.add(conRank);
        conRank.setFont(new Font("Montserrat", Font.BOLD, 18));
        conRank.setBounds(125, 210, 250, 70); 
        conRank.setBackground(rosa);
        conRank.setForeground(Color.WHITE);
        conRank.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent mudaTela) {
                telaConsRank.run(); 
                dispose();
            }
        });       
        var cadAluno = new JButton("Cadastrar aluno");
        painelBranco.add(cadAluno);
        cadAluno.setFont(new Font("Montserrat", Font.BOLD, 18));
        cadAluno.setBounds(125, 300, 250, 70); 
        cadAluno.setBackground(rosa);
        cadAluno.setForeground(Color.WHITE);
        cadAluno.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent voltarTela) {
                telaCadastro.run(); 
                dispose();
            }
        });
        var voltar = new JButton("Voltar");
        painelBranco.add(voltar);
        voltar.setFont(new Font("Montserrat", Font.PLAIN, 18));
        voltar.setBounds(190, 420, 110, 30); 
        voltar.setBackground(rosa);
        voltar.setForeground(Color.WHITE);
        voltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent voltarTela) {
                telaMenuAdmin.run(); 
                dispose();
            }
        });
    }
}