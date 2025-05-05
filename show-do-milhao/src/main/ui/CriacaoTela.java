//ISSO É UM TESTE. NÃO FOI FINALIZADO.

import javax.swing.*;
import java.awt.*;

public class CriacaoTela {
    String nome, materia;
    Color ciano = new Color(28, 180, 194);
    // Criar um atributo comum entre as classes 

    void aparenciaTela() {
        JFrame tela = new JFrame("TELA INICIAL");
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setSize(710, 800);
        tela.setExtendedState(Frame.MAXIMIZED_BOTH);
        tela.setLocationRelativeTo(null);

        JPanel corFundo = new JPanel(null, true);
        corFundo.setSize(800, 800);
        corFundo.setBackground(ciano);
        tela.add(corFundo);

        tela.setVisible(true);
        }

    void aparencia1(){
        JButton b = new JButton(materia);
        b.setBounds(100, 100, 200, 50);
        b.setFont(new Font("Inter", Font.PLAIN, 22));
        b.setBackground(Color.WHITE);
        b.setVisible(true);
        }
    }

