package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaRecuperacaoSenha extends JFrame{
	public TelaRecuperacaoSenha(Runnable voltaTela, Runnable configs){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(710, 800);
		setLocationRelativeTo(null);
		setExtendedState(MAXIMIZED_BOTH);
		
		var cinza = new Color(217, 217, 217);
		var rosa = new Color(238, 33, 82);
		var roxo = new Color(20, 14, 40);
		
		var painelInicial = new JPanel(null, true);
		painelInicial.setSize(800, 800);
		painelInicial.setBackground(roxo);
		add(painelInicial);
		
		var painelMenor = new JPanel(null);
		painelMenor.setBackground(Color.WHITE);
		painelMenor.setBounds(100, 100, 500, 550);
		painelInicial.add(painelMenor, BorderLayout.CENTER);
		
		// E-mail
		var digiteEmail = new JTextField();
		digiteEmail.setColumns(30);
		
		digiteEmail.setBackground(cinza);
		digiteEmail.setBounds(90, 270, 320, 40);
		painelMenor.add(digiteEmail);
		digiteEmail.setFont(new Font("New Cordial", Font.ITALIC, 10));
		
		var email = new JLabel("Digite o e-mail cadastrado:");
		email.setFont(new Font("Cordia New", Font.BOLD, 14));
		email.setBounds(90, 248, 200, 20);
		painelMenor.add(email);
		
		// Botôes
		var enviar = new JButton("Enviar");
		painelMenor.add(enviar);
		enviar.setFont(new Font("Cordia New", Font.BOLD, 16));
		enviar.setBounds(310, 390, 100, 40);
		enviar.setBackground(rosa);
		enviar.setForeground(Color.WHITE);
		
		var voltar = new JButton("Voltar");
		painelMenor.add(voltar);
		voltar.setFont(new Font("Cordia New", Font.BOLD, 16));
		voltar.setBounds(90, 390, 100, 40);
		voltar.setBackground(rosa);
		voltar.setForeground(Color.WHITE);
		voltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent volta) {
				voltaTela.run(); //volta para tela login
				dispose();
			}
			
		});
		
		// Mensagem de aviso
		enviar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evento){
				if (digiteEmail.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!", "ERRO - CAMPO VAZIO", 2);
				}
				else {
					JOptionPane.showMessageDialog(null, "E-mail enviado!", "Confimação de envio", 1);
				}
			}
		});
		
		// Título
		var login = new JLabel("Recuperação de senha");
		painelMenor.add(login);
		login.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 26));
		login.setBounds(90, 120, 400, 100);
		
		// Adicionando imagens
		var icon = new ImageIcon("assets\\image.png");
		var poliedro = icon.getImage().getScaledInstance(150, 80, Image.SCALE_SMOOTH);
		var imagemPoliedro = new JLabel(new ImageIcon(poliedro));
		painelMenor.add(imagemPoliedro);
		imagemPoliedro.setBounds(160, 50, 170, 70);
		
		 var icone = new ImageIcon("assets\\settings.png");
		 var image = icone.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		 var config = new JLabel(new ImageIcon(image));
		 painelInicial.add(config);
		 config.setBounds(1460, 20, 60, 60);
		 config.addMouseListener(new MouseAdapter() {
		     @Override
		     public void mouseClicked(MouseEvent e){
		         configs.run();
		         dispose();
		     }
		 });
		
		// Dimensionamento
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
			}
		});		
	}
}
