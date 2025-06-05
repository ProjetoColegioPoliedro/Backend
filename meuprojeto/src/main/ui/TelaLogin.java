package ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import model.MockDB;

public class TelaLogin extends JFrame{
	public TelaLogin(Runnable voltaTelaAnterior, Runnable fgtSenha, Runnable configs, Runnable showTelaAdmin, Runnable showTelaMenuEstudante){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(710, 800);
		setLocationRelativeTo(null);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		
		
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
		
		// Título
		var login = new JLabel("Faça o seu login");
		painelMenor.add(login);
		login.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 25));
		login.setBounds(90, 120, 200, 100);
		
		// E-mail e senha
		var digiteEmail = new JTextField();
		digiteEmail.setColumns(30);
		digiteEmail.setBackground(cinza);
		digiteEmail.setBounds(90, 230, 320, 40);
		painelMenor.add(digiteEmail);
		digiteEmail.setFont(new Font("New Cordial", Font.ITALIC, 10));
		
		var email = new JLabel("E-mail");
		email.setFont(new Font("Cordia New", Font.BOLD, 12));
		email.setBounds(90, 210, 200, 20); // ajuste a altura
		painelMenor.add(email);
		
		var digiteSenha = new JTextField();
		digiteSenha.setColumns(30);
		digiteSenha.setBackground(cinza);
		digiteSenha.setBounds(90, 310, 320, 40);
		painelMenor.add(digiteSenha);
		digiteSenha.setFont(new Font("Cordia New", Font.ITALIC, 10));
		
		var senha = new JLabel("Senha");
		senha.setFont(new Font("Cordia New", Font.BOLD, 12));
		senha.setBounds(90, 290, 200, 20);
		painelMenor.add(senha);
		
		// Botôes
		var entrar = new JButton("Entrar");
		painelMenor.add(entrar);
		entrar.setFont(new Font("Cordia New", Font.BOLD, 14));
		entrar.setBounds(300, 390, 110, 30);
		entrar.setBackground(rosa);
		entrar.setForeground(Color.WHITE);
		// Mensagem de aviso
		entrar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evento){
				if ((digiteEmail.getText().trim().isEmpty()) || (digiteSenha.getText().trim().isEmpty())){
					JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!", "ERRO - CAMPO VAZIO", JOptionPane.WARNING_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "Login efetuado com sucesso!", "Confimação de login", JOptionPane.INFORMATION_MESSAGE);

					//função retorna se é administrador ou nao (1 ou 0)

					int type;
					MockDB mock = new MockDB();
					type = mock.verify(digiteEmail.getText(), digiteSenha.getText());

					if(type == 1){
						showTelaAdmin.run();
						dispose();
					}
					else{
						showTelaMenuEstudante.run();
						dispose();
					}
				}
			}
		});
		
		var cancelar = new JButton("Cancelar");
		painelMenor.add(cancelar);
		cancelar.setFont(new Font("Cordia New", Font.BOLD, 14));
		cancelar.setBounds(90, 390, 110, 30);
		cancelar.setBackground(rosa);
		cancelar.setForeground(Color.WHITE);
		cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent volta) {
				voltaTelaAnterior.run();//retorna para tela principal
				dispose();
			}
			
		});
		
		// "Esqueceu a senha?"
		var esqueceuSenha = new JLabel("Esqueceu a senha?");
		painelMenor.add(esqueceuSenha);
		esqueceuSenha.setFont(new Font("Arial", Font.ITALIC, 10));
		esqueceuSenha.setBounds(209, 420, 200, 100);
		esqueceuSenha.setForeground(Color.BLUE);
		esqueceuSenha.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				fgtSenha.run();//vai para tela de recuperação de senha
				dispose();
			}
		});
		
		// Adicionando imagens
		var icon = new ImageIcon("assets\\logo-poliedro-2.png");
		var poliedro = icon.getImage().getScaledInstance(150, 80, Image.SCALE_SMOOTH);
		var imagemPoliedro = new JLabel(new ImageIcon(poliedro));
		painelMenor.add(imagemPoliedro);
		imagemPoliedro.setBounds(170, 50, 150, 80);
		
		var icone = new ImageIcon("assets\\settings.png");
		var image = icone.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		var config = new JLabel(new ImageIcon(image));
		painelInicial.add(config);
		config.setBounds(1460, 20, 60, 60);
		config.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e){
			configs.run(); //abre tela de configuração
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
				
				// int margemDireita = 30;
				// int margemSuperior = 20;
				// config.setBounds(
				//     frameWidth - 60 - margemDireita,
				//     margemSuperior,
				//     60, 60);
			}
		});
	};
}
