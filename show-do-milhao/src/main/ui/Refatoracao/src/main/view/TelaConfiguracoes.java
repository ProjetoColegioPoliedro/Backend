package main.view;

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
		
//		var txtMusica = new JLabel("Música");
//		txtMusica.setBounds(150, 310, 500, 70);
//		txtMusica.setFont(new Font("Montserrat", Font.ITALIC, 35));
//		txtMusica.setForeground(Color.WHITE);
//		painelMenor.add(txtMusica);
		
//		var musica = new JToggleButton();
//		painelMenor.add(musica);
//		musica.setSelected(true);
//		musica.setOpaque(true);
//		musica.setBounds(280, 335, 60, 30);
//		musica.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent e) {
//				if(musica.isSelected()){
//					musica.setBackground(Color.GREEN);
//				} else{
//					musica.setBackground(rosa);
//				}
//			}
//		});
		
//		var txtEfeitoSom = new JLabel("Efeito sonoro");
//		txtEfeitoSom.setBounds(150, 360, 500, 70);
//		txtEfeitoSom.setFont(new Font("Montserrat", Font.ITALIC, 35));
//		txtEfeitoSom.setForeground(Color.WHITE);
//		painelMenor.add(txtEfeitoSom);
//
//		var efeitoSonoro = new JToggleButton();
//		painelMenor.add(efeitoSonoro);
//		efeitoSonoro.setSelected(true);
//		efeitoSonoro.setOpaque(true);
//		efeitoSonoro.setBounds(375, 383, 60, 30);
//		efeitoSonoro.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent e) {
//				if(efeitoSonoro.isSelected()){
//					efeitoSonoro.setBackground(Color.GREEN);
//				} else{
//					efeitoSonoro.setBackground(rosa);
//				}
//			}
//		});
		
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
		
		// var aMaior = new JLabel("A+");
		// aMaior.setBounds(20, 20, 500, 70);
		// aMaior.setFont(new Font("Montserrat", Font.ITALIC, 60));
		// aMaior.setForeground(Color.WHITE);
		// corFundo.add(aMaior);
		
		// var aMenor = new JLabel("A-");
		// aMenor.setBounds(20, 80, 500, 70);
		// aMenor.setFont(new Font("Montserrat", Font.ITALIC, 40));
		// aMenor.setForeground(Color.WHITE);
		// corFundo.add(aMenor);
		
	}
}

