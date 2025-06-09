package ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TelaRespostaCorreta extends JFrame{

    public TelaRespostaCorreta(Runnable contQuestao, boolean penultimaQuestao, boolean ultimaQuestão){ // Modificado aqui
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

        // Possibilidade de resposta com operador ternário
        String mensagem = ultimaQuestão ? "Fim de jogo!" : penultimaQuestao ? "Questão final" : "Próxima questão";

        var proxQuestao = new JLabel(mensagem);
        proxQuestao.setBounds(650, 340, 500, 70);
        proxQuestao.setFont(new Font("Montserrat", Font.ITALIC, 35));
        proxQuestao.setForeground(Color.WHITE);
        corFundo.add(proxQuestao);
        if(!ultimaQuestão){
        proxQuestao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Fecha a tela de resposta correta
                contQuestao.run(); // Executa a ação recebida (que no Navegador é showTelaPartida)
                }
            });
        } else{
            proxQuestao.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    dispose();
                    contQuestao.run();
                }
            });
        }
    }
}