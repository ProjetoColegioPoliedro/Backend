import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Frame;
import java.awt.event.*;
import javax.swing.*;

public class TesteInterface{
    public static void main(String [] args){
        Color ciano = new Color(28, 180, 194);
        Color cinza = new Color(217, 217, 217);

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

        JTextField digiteEmail = new JTextField();
        digiteEmail.setColumns(30);
        digiteEmail.setBackground(cinza);
        digiteEmail.setBounds(90, 230, 320, 40);
        painelMenor.add(digiteEmail);
        digiteEmail.setFont(new Font("New Cordial", Font.ITALIC, 10));
        
        JLabel email = new JLabel("E-mail");
        email.setFont(new Font("Cordia New", Font.BOLD, 10));
        email.setBounds(90, 210, 200, 20); // ajuste a altura
        painelMenor.add(email);

        JPasswordField digiteSenha = new JPasswordField();
        digiteSenha.setColumns(30);
        digiteSenha.setBackground(cinza);
        digiteSenha.setBounds(90, 310, 320, 40);
        painelMenor.add(digiteSenha);
        digiteSenha.setFont(new Font("Cordia New", Font.ITALIC, 10));

        JLabel senha = new JLabel("Senha");
        senha.setFont(new Font("Cordia New", Font.BOLD, 10));
        senha.setBounds(90, 250, 200, 100); 
        painelMenor.add(senha);

        JButton entrar = new JButton("Entrar");
        painelMenor.add(entrar);
        entrar.setBounds(210, 390, 70, 30);
        entrar.setBackground(cinza);

        entrar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evento){
            if ((digiteEmail.getText().trim().isEmpty() == true) || (digiteSenha.getPassword().length == 0)){
                JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!", "ERRO - CAMPO VAZIO", 2);
                    }
            else {
                JOptionPane.showMessageDialog(null, "Login efetuado com sucesso!", "Confimação de login", 1);
            } 
        }
        });
    
        JLabel login = new JLabel("Faça o seu login");
        painelMenor.add(login);
        login.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 20));
        login.setBounds(90, 120, 200, 100);
        
        
        JLabel esqueceuSenha = new JLabel("Esqueceu a senha?");
        painelMenor.add(esqueceuSenha);
        esqueceuSenha.setFont(new Font("Arial", Font.ITALIC, 10));
        esqueceuSenha.setBounds(199, 380, 200, 100);
        // esqueceuSenha.setForeground(Color.BLACK);
        
        ImageIcon icon = new ImageIcon("image.png");
        Image poliedro = icon.getImage().getScaledInstance(150, 80, Image.SCALE_SMOOTH);
        JLabel imagemPoliedro = new JLabel(new ImageIcon(poliedro));
        painelMenor.add(imagemPoliedro);
        imagemPoliedro.setBounds(170, 40, 150, 80);

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