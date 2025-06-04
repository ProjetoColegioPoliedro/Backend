package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaTempoEncerrado extends JFrame{
    public TelaTempoEncerrado(Runnable telaSolucao){
        // var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40);

        // Tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000, 2000);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo);

        // Voltar
        var tempoAcaou = new JLabel("O tempo acabou \uD83D\uDE2D⏳");
        tempoAcaou.setBounds(600, 290, 500, 70);
        tempoAcaou.setFont(new Font("Montserrat", Font.BOLD, 40));
        tempoAcaou.setForeground(Color.WHITE);
        corFundo.add(tempoAcaou);

        var mostraSolucao = new JLabel("Ver Solução");
        mostraSolucao.setBounds(670, 340, 500, 70);
        mostraSolucao.setFont(new Font("Montserrat", Font.ITALIC, 35));
        mostraSolucao.setForeground(Color.WHITE);
        corFundo.add(mostraSolucao);
        mostraSolucao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                telaSolucao.run();
                dispose();
            }
        });
    }
}