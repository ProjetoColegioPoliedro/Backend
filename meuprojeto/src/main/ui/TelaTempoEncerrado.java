package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import model.Questao; 

public class TelaTempoEncerrado extends JFrame {
    private Questao questaoAtual; 

    public TelaTempoEncerrado(Questao questao, Runnable solucao){ 
        this.questaoAtual = questao; // Armazena a questão atual

        var roxo = new Color(20, 14, 40);

        // Configurações básicas da tela JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000, 2000); // Tamanho inicial
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza a janela
        setLocationRelativeTo(null); // Centraliza a janela na tela

        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo);

        // Label para a mensagem "Tempo encerrado!"
        var tempoEncerradoLabel = new JLabel("Tempo encerrado! ⏰");
        tempoEncerradoLabel.setBounds(600, 290, 500, 70); 
        tempoEncerradoLabel.setFont(new Font("Montserrat", Font.BOLD, 40));
        tempoEncerradoLabel.setForeground(Color.WHITE);
        corFundo.add(tempoEncerradoLabel);

        // Label/Botão "Ver solução"
        var verSolucao = new JLabel("Ver solução"); 
        verSolucao.setBounds(700, 340, 500, 70); 
        verSolucao.setFont(new Font("Montserrat", Font.ITALIC, 35));
        verSolucao.setForeground(Color.WHITE);
        corFundo.add(verSolucao);

        // Adiciona o MouseListener ao JLabel "Ver solução"
        verSolucao.addMouseListener(new MouseAdapter() {
            @Override 
            public void mouseClicked(MouseEvent e){
                dispose(); // Fecha a tela de tempo encerrado
                // Executa o Runnable 'solucao' que foi passado, que por sua vez
                // no Navegador, está configurado para chamar showTelaSolucao(questaoAtual, voltarParaMenu).
                solucao.run(); 
            }
        });
    }
}