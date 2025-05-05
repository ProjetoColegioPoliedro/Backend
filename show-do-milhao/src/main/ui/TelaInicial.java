import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaInicial {
    public static void main(String[] args) {

        Color ciano = new Color(28, 180, 194);

        // Criação da tela
        JFrame tela = new JFrame("TELA INICIAL");
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setSize(710, 800);
        tela.setExtendedState(Frame.MAXIMIZED_BOTH);
        tela.setLocationRelativeTo(null);
        
        JPanel corFundo = new JPanel(null, true);
        corFundo.setBackground(ciano);
        tela.setContentPane(corFundo);
        
        JPanel painelMenor = new JPanel(null);
        painelMenor.setSize(500, 550);
        painelMenor.setOpaque(false);
        corFundo.add(painelMenor);
        
        // Adicionando a logo
        ImageIcon logoMilhao = new ImageIcon("show_do_milhao.png");
        Image iconeTelaInicial = logoMilhao.getImage().getScaledInstance(378, 196, Image.SCALE_SMOOTH);
        JLabel showMilhao = new JLabel(new ImageIcon(iconeTelaInicial));
        painelMenor.add(showMilhao);
        showMilhao.setBounds(60, 30, 378, 196);

        // botões
        JButton login = new JButton("Login");
        painelMenor.add(login);
        login.setFont(new Font("Inter", Font.PLAIN, 32));
        login.setBounds(100, 250, 278, 55);
        login.setBackground(Color.WHITE);
        
        JButton cadastro = new JButton("Cadastre-se");
        painelMenor.add(cadastro);
        cadastro.setFont(new Font("Inter", Font.PLAIN, 32));
        cadastro.setBounds(100, 330, 278, 55); 
        cadastro.setBackground(Color.WHITE);
        
        JButton config = new JButton("Configurações");
        painelMenor.add(config);
        config.setFont(new Font("Inter", Font.PLAIN, 32));
        config.setBounds(100, 410, 278, 55);
        config.setBackground(Color.WHITE);

        // Area do professor
        JLabel areaProfessor = new JLabel("Área do professor");
        corFundo.add(areaProfessor, BorderLayout.CENTER);
        areaProfessor.setFont(new Font("Inter", Font.BOLD, 15));
        areaProfessor.setSize(200, 100);

        tela.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evento) {
                int frameWidth = tela.getWidth();
                int frameHeight = tela.getHeight();
                int painelMenorWidth = painelMenor.getWidth();
                int painelMenorHeight = painelMenor.getHeight();
                int x = (frameWidth - painelMenorWidth) / 2;
                int y = (frameHeight - painelMenorHeight) / 2;
                painelMenor.setLocation(x, y);
        
                // arrumando posicionamento do texto
                int margemDireita = 20;
                int margemInferior = 50;
                areaProfessor.setBounds(
                    frameWidth - 200 + (margemDireita),
                    frameHeight - 30 - margemInferior,
                    200, 30);
            }
        });


        tela.setVisible(true);
    }
}
