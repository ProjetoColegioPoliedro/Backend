package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaEditaPergunta extends JFrame{
    public TelaEditaPergunta(Runnable telaAreaRes){
        var roxo = new Color(20, 14, 40);

        // Criação da tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(710, 800);
        setLocationRelativeTo(null);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setResizable(false);

        var corFundo = new JPanel(null);
        add(corFundo);
        corFundo.setBackground(roxo);
        corFundo.setSize(800, 800);

        var icone = new ImageIcon("C:/Users/Admin/Downloads/Refatoracao/src/main/assets/seta.png");
        var imagem = icone.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        var seta = new JLabel(new ImageIcon(imagem));
        corFundo.add(seta);
        seta.setBounds(20, 20, 60, 60);
        seta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                telaAreaRes.run();
                dispose();
            }
        });

        // Pastas
        var primeiraPasta = new ImageIcon("C:/Users/Admin/Downloads/Refatoracao/src/main/assets/pastaCiana.png");
        var pP = primeiraPasta.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        var pastaCiana = new JLabel(new ImageIcon(pP));
        corFundo.add(pastaCiana);
        pastaCiana.setBounds(220, 230, 300, 300);

        JLabel pastaTxt = new JLabel("Fácil");
        pastaTxt.setFont(new Font("Montserrat", Font.BOLD, 40));
        pastaTxt.setForeground(Color.WHITE);
        pastaTxt.setBounds(100, 90, 150, 150);
        pastaCiana.add(pastaTxt);

        var segundaPasta = new ImageIcon("C:/Users/Admin/Downloads/Refatoracao/src/main/assets/pastaAmarela.png");
        var sP = segundaPasta.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        var pastaAmarela = new JLabel(new ImageIcon(sP));
        corFundo.add(pastaAmarela);
        pastaAmarela.setBounds(620, 230, 300, 300);

        JLabel pastaTxt1 = new JLabel("Médio");
        pastaTxt1.setFont(new Font("Montserrat", Font.BOLD, 40));
        pastaTxt1.setForeground(Color.WHITE);
        pastaTxt1.setBounds(90, 90, 150, 150);
        pastaAmarela.add(pastaTxt1);

        var terceiraPasta = new ImageIcon("C:/Users/Admin/Downloads/Refatoracao/src/main/assets/pastaVermelha.png");
        var tP = terceiraPasta.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        var pastaVermelha = new JLabel(new ImageIcon(tP));
        corFundo.add(pastaVermelha);
        pastaVermelha.setBounds(1020, 230, 300, 300);

        JLabel pastaTxt2 = new JLabel("Difícil");
        pastaTxt2.setFont(new Font("Montserrat", Font.BOLD, 40));
        pastaTxt2.setForeground(Color.WHITE);
        pastaTxt2.setBounds(90,90, 150, 150);
        pastaVermelha.add(pastaTxt2);

    }
}
