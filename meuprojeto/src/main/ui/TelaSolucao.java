package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import model.Questao;

public class TelaSolucao extends JFrame{
    private Questao questaoAtual;

    // Construtor: 'proximaPartidaAction' é o Runnable que inicia uma nova partida
    public TelaSolucao(Questao questao, Runnable proximaPartidaAction){ 
        this.questaoAtual = questao;
        
        var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(false);

        var fundoTela = new JPanel(null, true);
        fundoTela.setBackground(roxo);
        setContentPane(fundoTela);

        var painelSolucaoConteudo = new JPanel(null);
        painelSolucaoConteudo.setBounds(70, 90, 1400, 650);
        painelSolucaoConteudo.setBackground(new Color(20, 14, 40));
        painelSolucaoConteudo.setBorder(BorderFactory.createLineBorder(rosa, 7));
        fundoTela.add(painelSolucaoConteudo);

        var txtTituloSolucao = new JLabel("Explicação da Questão:");
        txtTituloSolucao.setFont(new Font("Montserrat", Font.BOLD, 35));
        txtTituloSolucao.setForeground(Color.WHITE);
        txtTituloSolucao.setBounds(20, 10, 800, 50);
        painelSolucaoConteudo.add(txtTituloSolucao);
        
        JTextArea areaExplicacaoErro = new JTextArea();
        areaExplicacaoErro.setLineWrap(true);
        areaExplicacaoErro.setWrapStyleWord(true);
        areaExplicacaoErro.setFont(new Font("Montserrat", Font.PLAIN, 20));
        areaExplicacaoErro.setForeground(Color.WHITE);
        areaExplicacaoErro.setBackground(painelSolucaoConteudo.getBackground());
        areaExplicacaoErro.setEditable(false);
        
        JScrollPane scrollExplicacao = new JScrollPane(areaExplicacaoErro);
        scrollExplicacao.setBounds(20, 70, 1360, 400);
        scrollExplicacao.setBorder(BorderFactory.createEmptyBorder());
        painelSolucaoConteudo.add(scrollExplicacao);

        var respCorreta = new JLabel("Alternativa correta: ");
        respCorreta.setFont(new Font("Montserrat", Font.BOLD, 25));
        respCorreta.setForeground(new Color(30, 180, 195));
        respCorreta.setBounds(20, 490, 800, 40);
        painelSolucaoConteudo.add(respCorreta);

        if (this.questaoAtual != null) {
            areaExplicacaoErro.setText(this.questaoAtual.getExplicacaoErro());
            
            if (this.questaoAtual.getAlternativaCorreta() != null) {
                respCorreta.setText("Alternativa Correta: " + this.questaoAtual.getAlternativaCorreta().getTexto());
            } else {
                respCorreta.setText("Alternativa Correta: N/A (informação ausente)");
            }
        } else {
            areaExplicacaoErro.setText("Não foi possível carregar a explicação da solução para esta questão.");
            respCorreta.setText("Alternativa Correta: N/A (erro na carga)");
            txtTituloSolucao.setText("Erro ao exibir solução");
        }

        // Botão "Jogar novamente" 
        var voltaMenu = new JButton("Jogar novamente");
        voltaMenu.setFont(new Font("Montserrat", Font.BOLD, 19));
        voltaMenu.setBackground(rosa);
        voltaMenu.setForeground(Color.WHITE);
        voltaMenu.setBounds(670, 770, 200, 40);
        fundoTela.add(voltaMenu);
        voltaMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                dispose(); // Fecha a tela de solução
                proximaPartidaAction.run(); // Executa a ação para iniciar uma nova partida
            }
        });
    }
}