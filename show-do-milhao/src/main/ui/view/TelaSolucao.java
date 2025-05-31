package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaSolucao extends JFrame{
        public TelaSolucao(Runnable telaMenu){
            var rosa = new Color(238, 33, 82);
            var roxo = new Color(20, 14, 40);

            // Criação da tela
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1000, 1000);
            setExtendedState(Frame.MAXIMIZED_BOTH);
            setLocationRelativeTo(null);
            setResizable(false);

            var fundoTela = new JPanel(null, true);
            fundoTela.setBackground(roxo);
            setContentPane(fundoTela);

            // Label da solução
            var labelSol = new JLabel("");
            labelSol.setBounds(70, 90, 1400, 650);
            labelSol.setFont(new Font("Montserrat", Font.ITALIC, 20));
            labelSol.setForeground(Color.WHITE);
            labelSol.setBorder(BorderFactory.createLineBorder(rosa, 7));
            fundoTela.add(labelSol);

            var respCorreta = new JLabel("Alternativa correta: X");
            labelSol.add(respCorreta);
            respCorreta.setFont(new Font("Montserrat", Font.BOLD, 20));
            respCorreta.setForeground(Color.WHITE);
            respCorreta.setBounds(600, 580, 300, 60);

            var txtSolucao = new JLabel("Texto da solução");
            labelSol.add(txtSolucao);
            txtSolucao.setFont(new Font("Montserrat", Font.PLAIN, 30));
            txtSolucao.setForeground(Color.WHITE);
            txtSolucao.setBounds(20, 10, 300, 60);

            var voltaMenu = new JButton("Jogar novamente");
            voltaMenu.setFont(new Font("Montserrat", Font.BOLD, 19));
            voltaMenu.setBackground(rosa);
            voltaMenu.setForeground(Color.WHITE);
            voltaMenu.setBounds(670, 770, 200, 40);
            fundoTela.add(voltaMenu);
            voltaMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    telaMenu.run();
                    dispose();
                }
            });
        }
    }
