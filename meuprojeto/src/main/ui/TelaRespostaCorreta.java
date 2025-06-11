package ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TelaRespostaCorreta extends JFrame {

    public TelaRespostaCorreta(Runnable proximaAcao, String buttonText) { 
        var roxo = new Color(20, 14, 40);

        // Configurações básicas da tela JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000, 2000); // Tamanho inicial
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza a janela
        setLocationRelativeTo(null); // Centraliza a janela na tela

        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo);

        //"Resposta correta"
        var respCorreta = new JLabel("Resposta correta 🎉");
        respCorreta.setBounds(600, 290, 500, 70);
        respCorreta.setFont(new Font("Montserrat", Font.BOLD, 40));
        respCorreta.setForeground(Color.WHITE);
        corFundo.add(respCorreta);

        // Label/Botão para a próxima ação
        var proxQuestao = new JLabel(buttonText); 
        proxQuestao.setBounds(650, 340, 500, 70); // Ajuste a posição
        proxQuestao.setFont(new Font("Montserrat", Font.ITALIC, 35));
        proxQuestao.setForeground(Color.WHITE);
        corFundo.add(proxQuestao);

        proxQuestao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Fecha a tela de resposta correta
                proximaAcao.run(); 
            }
        });
    }
}