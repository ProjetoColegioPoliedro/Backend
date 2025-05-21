import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaEditaPergunta extends JFrame{
    TelaEditaPergunta(){
        var roxo = new Color(20, 14, 40);
        var rosa = new Color(238, 33, 82);

        // Criação da tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(710, 800);
        setLocationRelativeTo(null);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        
        var corFundo = new JPanel(null);
        add(corFundo);
        corFundo.setBackground(roxo);
        corFundo.setSize(800, 800);

        var icone = new ImageIcon("seta.png");
        var imagem = icone.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        var seta = new JLabel(new ImageIcon(imagem));
        corFundo.add(seta);
        seta.setBounds(20, 20, 60, 60);
        seta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                var tAR = new TelaAreaRestrita();
                tAR.setVisible(true);
                dispose();
            }
        });
    }
}
