package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

public class TelaPartida extends JFrame {
    private int segundos = 45;
    public TelaPartida(Runnable acabouTempo, Runnable solucao) {
        var roxo = new Color(20, 14, 40);
        var laranja = new Color(250, 164, 31);
        var ciano = new Color(30, 180, 195);
        var rosa = new Color(238, 33, 82);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1500);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(false);

        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo);


        // Caixas de respostas
        var caixaRespA = new JLabel("Resposta");
        caixaRespA.setBounds(300, 200, 1000, 80);
        caixaRespA.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaRespA.setForeground(Color.WHITE);
        caixaRespA.setBorder(BorderFactory.createLineBorder(ciano, 7));
        corFundo.add(caixaRespA);
        caixaRespA.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent clique) {
                JOptionPane.showConfirmDialog(null, "Você tem certeza?", "Confirmação de resposta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            }
        });

        var caixaRespB = new JLabel("Resposta");
        caixaRespB.setBounds(300, 310, 1000, 80);
        caixaRespB.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaRespB.setForeground(Color.WHITE);
        caixaRespB.setBorder(BorderFactory.createLineBorder(ciano, 7));
        corFundo.add(caixaRespB);
        caixaRespB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent clique) {
                JOptionPane.showConfirmDialog(null, "Você tem certeza?", "Confirmação de resposta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            }
        });

        var caixaRespC = new JLabel("Resposta");
        caixaRespC.setBounds(300, 420, 1000, 80);
        caixaRespC.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaRespC.setForeground(Color.WHITE);
        caixaRespC.setBorder(BorderFactory.createLineBorder(ciano, 7));
        corFundo.add(caixaRespC);
        caixaRespC.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent clique) {
                JOptionPane.showConfirmDialog(null, "Você tem certeza?", "Confirmação de resposta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            }
        });

        var caixaRespD = new JLabel("Resposta");
        caixaRespD.setBounds(300, 530, 1000, 80);
        caixaRespD.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaRespD.setForeground(Color.WHITE);
        caixaRespD.setBorder(BorderFactory.createLineBorder(ciano, 7));
        corFundo.add(caixaRespD);
        caixaRespD.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent clique) {
                JOptionPane.showConfirmDialog(null, "Você tem certeza?", "Confirmação de resposta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            }
        });

        // lista de possíveis respostas
        var respostas = new ArrayList<>();
        respostas.add(caixaRespA);
        respostas.add(caixaRespB);
        respostas.add(caixaRespC);
        respostas.add(caixaRespD);

        // Pergunta
        var caixaPerg = new JLabel("Pergunta");
        caixaPerg.setBounds(180, 50, 1200, 80);
        caixaPerg.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaPerg.setForeground(Color.WHITE);
        caixaPerg.setBorder(BorderFactory.createLineBorder(rosa, 7));
        corFundo.add(caixaPerg);

        // Botôes
        var pularQuestao = new JButton(">>");
        pularQuestao.setFont(new Font("Montserrat", Font.BOLD, 22));
        pularQuestao.setBackground(laranja);
        pularQuestao.setForeground(Color.BLACK);
        ;
        corFundo.add(pularQuestao);
        pularQuestao.setBounds(340, 650, 60, 60);
        pularQuestao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(null, "Você tem certeza?", "Confirmação de resposta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            }
        });

        var pulQues = new JLabel("Pular questâo");
        pulQues.setBounds(410, 645, 500, 70);
        pulQues.setFont(new Font("Montserrat", Font.BOLD, 25));
        pulQues.setForeground(Color.WHITE);
        corFundo.add(pulQues);

        var eliminDuas = new JButton("1/2");
        eliminDuas.setFont(new Font("Montserrat", Font.BOLD, 19));
        eliminDuas.setBackground(laranja);
        eliminDuas.setForeground(Color.BLACK);
        ;
        corFundo.add(eliminDuas);
        eliminDuas.setBounds(820, 650, 60, 60);
        eliminDuas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(null, "Você tem certeza?", "Confirmação de resposta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            }
        });

        var eliDuasTxt = new JLabel("Eliminar duas alternativas");
        eliDuasTxt.setBounds(890, 645, 500, 70);
        eliDuasTxt.setFont(new Font("Montserrat", Font.BOLD, 25));
        eliDuasTxt.setForeground(Color.WHITE);
        corFundo.add(eliDuasTxt);

        // Arrumar - não são botôes --> Visor
        var txtErrar = new JLabel("");
        txtErrar.setFont(new Font("Montserrat", Font.BOLD, 19));
        txtErrar.setBackground(laranja);
        txtErrar.setForeground(Color.BLACK);
        ;
        corFundo.add(txtErrar);

        var errar = new ImageIcon("C:/Users/Admin/Downloads/Refatoracao/src/main/assets/retanguloLateral.png");
        var circErrar = errar.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        var retanguloUm = new JLabel(new ImageIcon(circErrar));
        corFundo.add(retanguloUm);
        retanguloUm.setBounds(1400, 250, 70, 70);

        // errar.setBounds(1400, 220, 50, 50);
        // var txtErrar = new JLabel("Errar");
        // txtErrar.setBounds(1403, 250, 500, 70);
        // txtErrar.setFont(new Font("Montserrat", Font.BOLD, 19));
        // txtErrar.setForeground(Color.WHITE);
        // corFundo.add(txtErrar);

        var acertar = new ImageIcon("C:/Users/Admin/Downloads/Refatoracao/src/main/assets/retanguloLateral.png");
        var circAcertar = acertar.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        var retanguloDois = new JLabel(new ImageIcon(circAcertar));
        corFundo.add(retanguloDois);
        retanguloDois.setBounds(1400, 370, 70, 70);

        var txtAcertar = new JLabel("");
        txtAcertar.setBounds(1393, 400, 500, 70);
        txtAcertar.setFont(new Font("Montserrat", Font.BOLD, 19));
        txtAcertar.setForeground(Color.WHITE);
        corFundo.add(txtAcertar);

        // ESSE É BOTÃO
        var parar = new JButton("C");
        parar.setFont(new Font("Montserrat", Font.BOLD, 19));
        parar.setBackground(laranja);
        parar.setForeground(Color.BLACK);
        ;
        corFundo.add(parar);
        parar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showConfirmDialog(null, "Você tem certeza?", "Confirmação de resposta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                solucao.run();
            }
        });

        parar.setBounds(1404, 500, 60, 60);
        var txtParar = new JLabel("Parar");
        txtParar.setBounds(1410, 540, 500, 70);
        txtParar.setFont(new Font("Montserrat", Font.BOLD, 19));
        txtParar.setForeground(Color.WHITE);
        corFundo.add(txtParar);

        // Alternativas
        var circulo = new ImageIcon("C:/Users/Admin/Downloads/Refatoracao/src/main/assets/circuloAlternativa.png");
        var c = circulo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        var circ = new JLabel(new ImageIcon(c));
        corFundo.add(circ);
        circ.setBounds(220, 215, 50, 50);

        var letraA = new JLabel("A");
        letraA.setBounds(11, 0, 50, 50);
        letraA.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraA.setForeground(Color.WHITE);
        circ.add(letraA);

        var circulo1 = new JLabel(new ImageIcon(c));
        corFundo.add(circulo1);
        circulo1.setBounds(220, 325, 50, 50);
        var letraB = new JLabel("B");
        letraB.setBounds(11, 0, 50, 50);
        letraB.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraB.setForeground(Color.WHITE);
        circulo1.add(letraB);

        var circulo2 = new JLabel(new ImageIcon(c));
        corFundo.add(circulo2);
        circulo2.setBounds(220, 440, 50, 50);
        var letraC = new JLabel("C");
        letraC.setBounds(11, 0, 50, 50);
        letraC.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraC.setForeground(Color.WHITE);
        circulo2.add(letraC);

        var circulo3 = new JLabel(new ImageIcon(c));
        corFundo.add(circulo3);
        circulo3.setBounds(220, 540, 50, 50);
        var letraD = new JLabel("D");
        letraD.setBounds(11, 0, 50, 50);
        letraD.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraD.setForeground(Color.WHITE);
        circulo3.add(letraD);


        // var a = new JButton("A");
        // corFundo.add(a);
        // a.setFont(new Font("Montserrat", Font.BOLD, 20));
        // a.setBounds(220, 215, 50, 50);
        // a.setBackground(ciano);
        // var b = new JButton("B");
        // corFundo.add(b);
        // b.setFont(new Font("Montserrat", Font.BOLD, 20));
        // b.setBounds(220, 325, 50, 50);
        // b.setBackground(ciano);
        // var ca = new JButton("C");
        // corFundo.add(c);
        // c.setFont(new Font("Montserrat", Font.BOLD, 20));
        // c.setBounds(220, 435, 50, 50);
        // c.setBackground(ciano);
        // var d = new JButton("D");
        // corFundo.add(d);
        // d.setFont(new Font("Montserrat", Font.BOLD, 20));
        // d.setBounds(220, 545, 50, 50);
        // d.setBackground(ciano);

        // Cronômetro
        var circuloCrono = new ImageIcon("C:/Users/Admin/Downloads/Refatoracao/src/main/assets/circuloCrono.png");
        var circC = circuloCrono.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        var circuloFinal = new JLabel(new ImageIcon(circC));
        circuloFinal.setBounds(60, 50, 90, 90);
        corFundo.add(circuloFinal);

        JLabel cronometro = new JLabel("45");
        cronometro.setFont(new Font("Montserrat", Font.BOLD, 40));
        cronometro.setForeground(Color.WHITE);
        cronometro.setBounds(22, 5, 80, 80);
        circuloFinal.add(cronometro);

        final Timer[] timer = new Timer[1];

        timer[0] = new Timer(1000, e -> {
            if (segundos > 0) {
                segundos--;
                cronometro.setText(String.format("%02d", segundos % 60));
            } else {
                timer[0].stop();
                JOptionPane.showMessageDialog(null, "Tempo encerrado!", "Fim do tempo de questão", 2);
                acabouTempo.run();
                dispose();
            }
        });

        timer[0].start();

    }
}