import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class TelaCadastro extends JFrame{
    TelaCadastro(){
        // Cores adicionais
        var cinza = new Color(217, 217, 217);
        var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40);

        // Criação da tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(710, 800);
        setLocationRelativeTo(null);
        
        var painelInicial = new JPanel(null, true);
        painelInicial.setSize(800, 800);
        painelInicial.setBackground(roxo);
        add(painelInicial);

        var painelMenor = new JPanel(null);
        painelMenor.setBackground(Color.WHITE);
        painelMenor.setBounds(100, 100, 500, 550);
        painelInicial.add(painelMenor, BorderLayout.CENTER);

        // Usuário, e-mail e senha
        var digiteUsuario = new JTextField();
        digiteUsuario.setColumns(30);
        digiteUsuario.setBackground(cinza);
        digiteUsuario.setForeground(rosa);
        digiteUsuario.setBounds(90, 200, 320, 40);
        painelMenor.add(digiteUsuario);
        digiteUsuario.setFont(new Font("New Cordial", Font.ITALIC, 10));

        var usuario = new JLabel("Usuário");
        usuario.setFont(new Font("Roboto", Font.BOLD, 12));
        usuario.setBounds(90, 180, 200, 20);
        painelMenor.add(usuario);

        var digiteEmail = new JTextField();
        digiteEmail.setColumns(30);
        digiteEmail.setBackground(cinza);
        digiteEmail.setForeground(rosa);
        digiteEmail.setBounds(90, 270, 320, 40);
        painelMenor.add(digiteEmail);
        digiteEmail.setFont(new Font("New Cordial", Font.ITALIC, 10));
        
        var email = new JLabel("E-mail");
        email.setFont(new Font("Roboto", Font.BOLD, 12));
        email.setBounds(90, 250, 200, 20); // ajuste a altura
        painelMenor.add(email);

        var digiteSenha = new JPasswordField();
        digiteSenha.setColumns(30);
        digiteSenha.setBackground(cinza);
        digiteSenha.setForeground(rosa);
        digiteSenha.setBounds(90, 340, 320, 40);
        painelMenor.add(digiteSenha);
        digiteSenha.setFont(new Font("Roboto", Font.ITALIC, 10));

        var senha = new JLabel("Senha");
        senha.setFont(new Font("Roboto Thin", Font.BOLD, 12));
        senha.setBounds(90, 320, 200, 20); 
        painelMenor.add(senha);


        // Botôes de cancelamento e cadastro.
        var cancelar = new JButton("Cancelar");
        painelMenor.add(cancelar);
        cancelar.setBounds(90, 420, 110, 30);
        cancelar.setBackground(rosa);
        cancelar.setFont(new Font("Roboto", Font.BOLD, 13));
        cancelar.setForeground(Color.WHITE);
        cancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                var tAR = new TelaAreaRestrita();
                tAR.setVisible(true);
                dispose();
            }
        });

        var cadastro = new JButton("Cadastre-se");
        painelMenor.add(cadastro);
        cadastro.setBounds(300, 420, 110, 30);
        cadastro.setBackground(rosa);
        cadastro.setFont(new Font("Roboto", Font.BOLD, 13));
        cadastro.setForeground(Color.WHITE);

        // Evento do botão
        cadastro.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evento){
            if ((digiteEmail.getText().trim().isEmpty()) || (digiteSenha.getPassword().length == 0)){
                JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!", "ERRO - CAMPO VAZIO", JOptionPane.WARNING_MESSAGE);
                    }
            else {
                JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!", "Confimação de cadastro", JOptionPane.INFORMATION_MESSAGE);
            } 
        }
        });
        
        // Cadastro
        var cadastrar = new JLabel("Cadastrar");
        painelMenor.add(cadastrar);
        cadastrar.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));
        cadastrar.setBounds(90, 100, 200, 100);
        
        // Logo - Poliedro
        var icone = new ImageIcon("image.png");
        var poliedro = icone.getImage().getScaledInstance(140, 70, Image.SCALE_SMOOTH);
        var imagemPoliedro = new JLabel(new ImageIcon(poliedro));
        painelMenor.add(imagemPoliedro);
        imagemPoliedro.setBounds(170, 40, 150, 80);

        // var icon = new ImageIcon("settings.png");
        // var image = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        // var config = new JLabel(new ImageIcon(image));
        // painelInicial.add(config);
        // config.setBounds(1460, 20, 60, 60);
        // config.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent e){
        //         var tC = new TelaConfiguracoes();
        //         tC.setVisible(true);
        //         dispose();
        //     }
        // });


        // Dimensionamento de tela
        setExtendedState(Frame.MAXIMIZED_BOTH);
        
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

                // int margemDireita = 30;
                // int margemSuperior = 20;
                // config.setBounds(
                // frameWidth - 60 - margemDireita,
                // margemSuperior,
                // 60, 60);
            }
        });

        // Mensagem
        var msgm = new JLabel("*Apenas o professor pode realizar o cadastro");
        painelMenor.add(msgm);
        msgm.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
        msgm.setForeground(rosa);
        msgm.setBounds(90, 350, 300, 100);
        
        setVisible(true);
        
        }   
    }
