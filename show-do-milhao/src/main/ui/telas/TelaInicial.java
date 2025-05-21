import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaInicial extends JFrame {
    TelaInicial() {
        var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40);

        // Criação da tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(710, 800);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo);
        
        var painelMenor = new JPanel(null);
        painelMenor.setSize(500, 800);
        painelMenor.setOpaque(false);
        corFundo.add(painelMenor);
        
        // Adicionando imagens
        var logoMilhao = new ImageIcon("logoPrincipal.png");
        var iconeTelaInicial = logoMilhao.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        var showMilhao = new JLabel(new ImageIcon(iconeTelaInicial));
        showMilhao.setBounds(-60, -40, 600, 600);
        painelMenor.add(showMilhao);

        var icon = new ImageIcon("settings.png");
        var image = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        var config = new JLabel(new ImageIcon(image));
        corFundo.add(config);
        config.setBounds(1460, 20, 60, 60);
        config.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                var tC = new TelaConfiguracoes();
                tC.setVisible(true);
                dispose();
            }
        });

        // botões
        var login = new JButton("Login");
        painelMenor.add(login);
        login.setFont(new Font("Montserrat", Font.BOLD, 40));
        login.setBounds(50, 520, 400, 80);
        login.setBackground(rosa);
        login.setForeground(Color.WHITE);
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                var tl = new TelaLogin();
                tl.setVisible(true);
                dispose();
            }
        }); 
        
        var cadastro = new JButton("Cadastre-se");
        painelMenor.add(cadastro);
        cadastro.setFont(new Font("Montserrat", Font.BOLD, 40));
        cadastro.setBounds(50, 620, 400, 80); 
        cadastro.setBackground(rosa);
        cadastro.setForeground(Color.WHITE);
        cadastro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                var tc = new TelaCadastro();
                tc.setVisible(true);
                dispose();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evento) {
                int frameWidth = getWidth();
                int frameHeight = getHeight();
                int painelMenorWidth = painelMenor.getWidth();
                int painelMenorHeight = painelMenor.getHeight();
                int x = (frameWidth - painelMenorWidth) / 2;
                int y = (frameHeight - painelMenorHeight) / 2;
                painelMenor.setLocation(x, y);

                int margemDireita = 30;
                int margemSuperior = 20;
                config.setBounds(
                frameWidth - 60 - margemDireita,
                margemSuperior,
                60, 60);

            }
        });


        setVisible(true);
    }
}
