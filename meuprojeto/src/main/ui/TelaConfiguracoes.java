package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaConfiguracoes extends JFrame{
	
	public TelaConfiguracoes(Runnable telaInicial, Runnable voltaUltimaTela){

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
		painelMenor.setBackground(Color.cyan);
		corFundo.add(painelMenor, BorderLayout.CENTER);
		
		// "Configurações"
		var configuracoes = new JLabel("Configurações");
		configuracoes.setBounds(100, 20, 500, 70);
		configuracoes.setFont(new Font("Montserrat", Font.BOLD, 45));
		configuracoes.setForeground(Color.WHITE);
		painelMenor.add(configuracoes);
		
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
		// Botões
		var sair = new JButton("Sair");
		sair.setFont(new Font("Montserrat", Font.BOLD, 20));
		sair.setForeground(Color.WHITE);
		sair.setBackground(rosa);
		painelMenor.add(sair);
		sair.setBounds(170, 540, 200, 60);
		sair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		var desconectar = new JButton("Desconectar");
		desconectar.setFont(new Font("Montserrat", Font.BOLD, 20));
		desconectar.setForeground(Color.WHITE);
		desconectar.setBackground(rosa);
		painelMenor.add(desconectar);
		desconectar.setBounds(170, 610, 200, 60);
		desconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent sair) {
				telaInicial.run();// vai para tela inicial
				dispose();
			}
		});
		
		var voltar = new JButton("Voltar");
		voltar.setFont(new Font("Montserrat", Font.BOLD, 20));
		voltar.setForeground(Color.WHITE);
		voltar.setBackground(rosa);
		painelMenor.add(voltar);
		voltar.setBounds(170, 680, 200, 60);
		voltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent voltarTela) {
				voltaUltimaTela.run();
				dispose();
			}
		});
	}
}

