package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaHistAdmin extends JFrame{
    public TelaHistAdmin(Runnable telaMenuAdmin, Runnable configs){
        var roxo = new Color(20, 14, 40);

        // Tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000, 2000);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo);

        // Título
        var histJogo = new JLabel("Histórico de Jogo");
        histJogo.setBounds(600, 20, 500, 70);
        histJogo.setFont(new Font("Montserrat", Font.BOLD, 45));
        histJogo.setForeground(Color.WHITE);
        corFundo.add(histJogo);

        // Rolagem de tela - ADICIONAR ELEMENTOS PARA FUNCIONAR
        var rolagemDeTela = new JScrollPane();
        corFundo.add(rolagemDeTela);

        // Voltar
        var icone = new ImageIcon("C:/Users/Admin/Downloads/Refatoracao/src/main/assets/seta.png");
        var imagem = icone.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        var seta = new JLabel(new ImageIcon(imagem));
        corFundo.add(seta);
        seta.setBounds(20, 20, 60, 60);
        seta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                telaMenuAdmin.run();
                dispose();
            }
        });

//         Configurações
         var iconeConfig = new ImageIcon("settings.png");
         var image = iconeConfig.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
         var config = new JLabel(new ImageIcon(image));
         corFundo.add(config);
         config.setBounds(1460, 20, 60, 60);
         config.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e){
                 configs.run();
                 dispose();
             }
         });
    }
}