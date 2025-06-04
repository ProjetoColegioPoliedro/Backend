package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaMenuEstudante extends JFrame{
	public TelaMenuEstudante(Runnable confCommand, Runnable telaTema, Runnable historico){
		var rosa = new Color(238, 33, 82);
		var roxo = new Color(20, 14, 40);
		var amarelo = new Color(247, 215, 60);
		
		// Criação da tela
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(710, 800);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setResizable(false);
		
		// var ladoDireito = new JPanel();
		// ladoDireito.setOpaque(false);
		// add(ladoDireito);
		
		var ladoDireito = new JPanel(null, true);
		ladoDireito.setBackground(roxo);
		ladoDireito.setSize(500, 800);
		setContentPane(ladoDireito);
		
		var ladoEsquerdo = new JPanel(null);
		ladoEsquerdo.setSize(600, 1000);
		add(ladoEsquerdo);
		ladoEsquerdo.setBackground(amarelo);
		
		// Adicionando imagens
		var logoMilhao = new ImageIcon("assets\\logoPrincipal.png");
		var iconeTelaInicial = logoMilhao.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
		var showMilhao = new JLabel(new ImageIcon(iconeTelaInicial));
		showMilhao.setBounds(-10, -40, 600, 600);
		ladoEsquerdo.add(showMilhao);
		
		var icone = new ImageIcon("assets\\settings.png");
		var imagem = icone.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		var config = new JLabel(new ImageIcon(imagem));
		ladoDireito.add(config);
		config.setBounds(1460, 20, 60, 60);
		config.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				confCommand.run(); // abre tela configuração
				dispose();
			}
		});
		
		
		var segundoIcone = new ImageIcon("assets\\poliedroFundoBranco.png");
		var poliedro = segundoIcone.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH);
		var imagemPoliedro = new JLabel(new ImageIcon(poliedro));
		ladoEsquerdo.add(imagemPoliedro);
		imagemPoliedro.setBounds(400, 600, 170, 170);
		
		// botões
		var jogar = new JButton("Jogar");
		ladoDireito.add(jogar);
		jogar.setFont(new Font("Montserrat", Font.BOLD, 40));
		jogar.setBounds(870, 320, 400, 80);
		jogar.setBackground(rosa);
		jogar.setForeground(Color.WHITE);
		jogar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				telaTema.run();
				dispose();
			}
		});
		
		var histJogo = new JButton("Histórico de jogo");
		ladoDireito.add(histJogo);
		histJogo.setFont(new Font("Montserrat", Font.BOLD, 40));
		histJogo.setBounds(870, 420, 400, 80);
		histJogo.setBackground(rosa);
		histJogo.setForeground(Color.WHITE);
		histJogo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				historico.run();
				dispose();
			}
		});
		
		
		
	}
}
