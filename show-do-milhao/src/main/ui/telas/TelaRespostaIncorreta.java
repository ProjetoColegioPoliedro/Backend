import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaRespostaIncorreta extends JFrame {
    TelaRespostaIncorreta(){
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
        var respIncorreta = new JLabel("Resposta incorreta 😞");
        respIncorreta.setBounds(600, 290, 500, 70);
        respIncorreta.setFont(new Font("Montserrat", Font.BOLD, 40));
        respIncorreta.setForeground(Color.WHITE);
        corFundo.add(respIncorreta);
        
        var verSolucao = new JLabel("Ver solução");
        verSolucao.setBounds(700, 340, 500, 70);
        verSolucao.setFont(new Font("Montserrat", Font.ITALIC, 35));
        verSolucao.setForeground(Color.WHITE);
        corFundo.add(verSolucao);
        verSolucao.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                var tSQ = new TelaSolucaoQuestao();
                tSQ.setVisible(true);
                dispose();
            }
        });
        
        setVisible(true);
    }
}
