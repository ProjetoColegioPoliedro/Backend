package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaRespostaCorreta extends JFrame{
    public TelaRespostaCorreta(){
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
        var respCorreta = new JLabel("Resposta correta 🎉");
        respCorreta.setBounds(600, 290, 500, 70);
        respCorreta.setFont(new Font("Montserrat", Font.BOLD, 40));
        respCorreta.setForeground(Color.WHITE);
        corFundo.add(respCorreta);

        var proxQuestao = new JLabel("Próxima questão");
        proxQuestao.setBounds(650, 340, 500, 70);
        proxQuestao.setFont(new Font("Montserrat", Font.ITALIC, 35));
        proxQuestao.setForeground(Color.WHITE);
        corFundo.add(proxQuestao);
        proxQuestao.addMouseListener(new MouseAdapter() {

        });
    }
}