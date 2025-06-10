package ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TelaRespostaCorreta extends JFrame {

    // Construtor ajustado para receber o Runnable da próxima ação e o texto do botão
    public TelaRespostaCorreta(Runnable proximaAcao, String buttonText) { // <-- Assinatura ajustada
        var roxo = new Color(20, 14, 40);

        // Configurações básicas da tela JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000, 2000); // Tamanho inicial
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza a janela
        setLocationRelativeTo(null); // Centraliza a janela na tela

        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo);

        // Label para a mensagem "Resposta correta"
        var respCorreta = new JLabel("Resposta correta 🎉");
        respCorreta.setBounds(600, 290, 500, 70);
        respCorreta.setFont(new Font("Montserrat", Font.BOLD, 40));
        respCorreta.setForeground(Color.WHITE);
        corFundo.add(respCorreta);

        // Label/Botão para a próxima ação
        // Usa o texto passado como parâmetro, que já vem formatado do Navegador ("Próxima Questão", "Fim de Jogo!", etc.)
        var proxQuestao = new JLabel(buttonText); 
        proxQuestao.setBounds(650, 340, 500, 70); // Ajuste a posição
        proxQuestao.setFont(new Font("Montserrat", Font.ITALIC, 35));
        proxQuestao.setForeground(Color.WHITE);
        corFundo.add(proxQuestao);

        // Adiciona o MouseListener ao JLabel, independentemente do texto
        proxQuestao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Fecha a tela de resposta correta
                proximaAcao.run(); // Executa a ação recebida (que no Navegador é processarProximaQuestao ou finalizarPartida)
            }
        });
    }
}