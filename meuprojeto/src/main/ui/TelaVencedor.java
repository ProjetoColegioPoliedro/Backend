package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaVencedor extends JFrame{
    public TelaVencedor(Runnable telaMenu, Runnable telaTemaPerguntas){
        var roxo = new Color(20, 14, 40);
        var rosa = new Color(238, 33, 82);
        var verde = new Color(0, 228, 11);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000, 2000); // Tamanho inicial
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza a janela
        setLocationRelativeTo(null); // Centraliza a janela na tela

        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo);

        // "Resposta correta"
        var vencedor = new JLabel("VENCEDOR! 👑");
        vencedor.setBounds(580, 40, 500, 120);
        vencedor.setFont(new Font("Montserrat", Font.BOLD, 60));
        vencedor.setForeground(Color.WHITE);
        corFundo.add(vencedor);
        var premioFinal = new JLabel("Gênio detectado: 100% de acertos!");
        premioFinal.setBounds(360, 150, 1000, 120);
        premioFinal.setFont(new Font("Montserrat", Font.BOLD, 50));
        premioFinal.setForeground(verde);
        corFundo.add(premioFinal);
        var jogarNovamente = new JButton("Jogar novamente");
        jogarNovamente.setFont(new Font("Montserrat", Font.BOLD, 37));
        jogarNovamente.setBackground(rosa);
        jogarNovamente.setForeground(Color.BLACK);
        jogarNovamente.setBounds(620, 520, 350, 60);
        corFundo.add(jogarNovamente);
        jogarNovamente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                telaTemaPerguntas.run(); // Executa a ação para abrir a tela de tema de perguntas
                dispose(); // Fecha a tela de solução
            }
        });
        
        var voltaMenu = new JButton("Voltar ao menu");
        voltaMenu.setFont(new Font("Montserrat", Font.BOLD, 37));
        voltaMenu.setBackground(rosa);
        voltaMenu.setForeground(Color.BLACK);
        voltaMenu.setBounds(620, 620, 350, 60);
        corFundo.add(voltaMenu);
        voltaMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                telaMenu.run(); // Executa a ação para abrir o menu
                dispose(); // Fecha a tela de solução
            }
        });
    } 
}
