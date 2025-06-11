package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import model.Questao; 

public class TelaRespostaIncorreta extends JFrame {
    private Questao questaoAtual; // Armazena a questão atual

    public TelaRespostaIncorreta(Questao questao, Runnable proximaAcao, String buttonText) { 
        this.questaoAtual = questao; // Armazena a questão atual
        var roxo = new Color(20, 14, 40);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000, 2000); // Tamanho inicial
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza a janela
        setLocationRelativeTo(null); // Centraliza a janela na tela

        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo);
        //"Resposta incorreta"
        var respIncorreta = new JLabel("Resposta incorreta 😞");
        respIncorreta.setBounds(600, 290, 500, 70); // Ajuste a posição conforme sua UI
        respIncorreta.setFont(new Font("Montserrat", Font.BOLD, 40));
        respIncorreta.setForeground(Color.WHITE);
        corFundo.add(respIncorreta);
        //"Ver solução" ou "Fim de jogo!"
        var verSolucao = new JLabel(buttonText); 
        verSolucao.setBounds(700, 340, 500, 70); 
        verSolucao.setFont(new Font("Montserrat", Font.ITALIC, 35));
        verSolucao.setForeground(Color.WHITE);
        corFundo.add(verSolucao);
        verSolucao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                dispose(); 
                proximaAcao.run(); 
            }
        });
    }
}