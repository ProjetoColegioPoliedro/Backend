import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Frame;
import java.awt.event.*;
import javax.swing.*;
public class InterfaceCadastro {
    public static void main(String[] args) {
        // Cores adicionais
        Color ciano = new Color(28, 180, 194);
        Color cinza = new Color(217, 217, 217);

        // Criação da tela
        JFrame frame = new JFrame("Interface Poliedro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(710, 800);
        frame.setLocationRelativeTo(null);
        
        JPanel painelInicial = new JPanel(null, true);
        painelInicial.setSize(800, 800);
        painelInicial.setBackground(ciano);
        frame.add(painelInicial);

        JPanel painelMenor = new JPanel(null);
        painelMenor.setBackground(Color.WHITE);
        painelMenor.setBounds(100, 100, 500, 550);
        painelInicial.add(painelMenor, BorderLayout.CENTER);

        // Usuário, e-mail e senha
        JTextField digiteUsuario = new JTextField();
        digiteUsuario.setColumns(30);
        digiteUsuario.setBackground(cinza);
        digiteUsuario.setBounds(90, 210, 320, 40);
        painelMenor.add(digiteUsuario);
        digiteUsuario.setFont(new Font("New Cordial", Font.ITALIC, 10));

        JLabel usuario = new JLabel("Usuário");
        usuario.setFont(new Font("Roboto", Font.BOLD, 11));
        usuario.setBounds(90, 190, 200, 20);
        painelMenor.add(usuario);

        JTextField digiteEmail = new JTextField();
        digiteEmail.setColumns(30);
        digiteEmail.setBackground(cinza);
        digiteEmail.setBounds(90, 280, 320, 40);
        painelMenor.add(digiteEmail);
        digiteEmail.setFont(new Font("New Cordial", Font.ITALIC, 10));
        
        JLabel email = new JLabel("E-mail");
        email.setFont(new Font("Roboto", Font.BOLD, 11));
        email.setBounds(90, 260, 200, 20); // ajuste a altura
        painelMenor.add(email);

        JPasswordField digiteSenha = new JPasswordField();
        digiteSenha.setColumns(30);
        digiteSenha.setBackground(cinza);
        digiteSenha.setBounds(90, 350, 320, 40);
        painelMenor.add(digiteSenha);
        digiteSenha.setFont(new Font("Roboto", Font.ITALIC, 10));

        JLabel senha = new JLabel("Senha");
        senha.setFont(new Font("Roboto Thin", Font.BOLD, 11));
        senha.setBounds(90, 330, 200, 20); 
        painelMenor.add(senha);


        // Botôes de cancelamento e cadastro.
        JButton cancelar = new JButton("Cancelar");
        painelMenor.add(cancelar);
        cancelar.setBounds(90, 420, 110, 30);
        cancelar.setBackground(Color.BLACK);
        cancelar.setFont(new Font("Roboto", Font.BOLD, 11));
        cancelar.setForeground(Color.WHITE);

        JButton cadastro = new JButton("Cadastre-se");
        painelMenor.add(cadastro);
        cadastro.setBounds(300, 420, 110, 30);
        cadastro.setBackground(Color.BLACK);
        cadastro.setFont(new Font("Roboto", Font.BOLD, 11));
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
        JLabel login = new JLabel("Cadastrar");
        painelMenor.add(login);
        login.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));
        login.setBounds(90, 120, 200, 100);
        
        // Logo - Poliedro
        ImageIcon icon = new ImageIcon("image.png");
        Image poliedro = icon.getImage().getScaledInstance(140, 70, Image.SCALE_SMOOTH);
        JLabel imagemPoliedro = new JLabel(new ImageIcon(poliedro));
        painelMenor.add(imagemPoliedro);
        imagemPoliedro.setBounds(170, 40, 150, 80);

        // Dimensionamento de tela
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evento) {
                int frameWidth = frame.getWidth();
                int frameHeight = frame.getHeight();
                int painelMenorWidth = painelMenor.getWidth();
                int painelMenorHeight = painelMenor.getHeight();
                int x = (frameWidth - painelMenorWidth) / 2;
                int y = (frameHeight - painelMenorHeight) / 2;
                painelMenor.setLocation(x, y);
            }
        });
        
        frame.setVisible(true);
        
        }   
    }
