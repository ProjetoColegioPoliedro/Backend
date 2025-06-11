// Substitua todo o arquivo ui/TelaHistorico.java
package ui;

import model.HistoricoJogo;
import service.HistoricoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaHistorico extends JFrame {

    public TelaHistorico(Runnable telaMenuEst, Runnable configs, int idAluno, HistoricoService historicoService) {
        
        var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40);
        var roxoClaro = new Color(30, 24, 60);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setTitle("Meu Histórico de Jogo");

        var corFundo = new JPanel(new BorderLayout());
        corFundo.setBackground(roxo);
        setContentPane(corFundo);

        JPanel painelCabecalho = new JPanel(null);
        painelCabecalho.setBackground(roxo);
        painelCabecalho.setPreferredSize(new Dimension(0, 100));
        corFundo.add(painelCabecalho, BorderLayout.NORTH);

        var histJogo = new JLabel("Meu Histórico");
        histJogo.setBounds(600, 20, 500, 70);
        histJogo.setFont(new Font("Montserrat", Font.BOLD, 45));
        histJogo.setForeground(Color.WHITE);
        painelCabecalho.add(histJogo);

        var icone = new ImageIcon("assets\\seta.png");
        var imagem = icone.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        var seta = new JLabel(new ImageIcon(imagem));
        seta.setBounds(20, 20, 60, 60);
        seta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        painelCabecalho.add(seta);
        seta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                telaMenuEst.run();
                dispose();
            }
        });
        

        JPanel painelConteudoHistorico = new JPanel();
        painelConteudoHistorico.setLayout(new BoxLayout(painelConteudoHistorico, BoxLayout.Y_AXIS));
        painelConteudoHistorico.setBackground(roxo);
        painelConteudoHistorico.setBorder(new EmptyBorder(20, 50, 20, 50));

        JScrollPane rolagemDeTela = new JScrollPane(painelConteudoHistorico);
        rolagemDeTela.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rolagemDeTela.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rolagemDeTela.setBorder(null);
        corFundo.add(rolagemDeTela, BorderLayout.CENTER);

        try {
            List<HistoricoJogo> historicos = historicoService.buscarPorAluno(idAluno);

            if (historicos.isEmpty()) {
                JLabel semHistoricoLabel = new JLabel("Nenhuma partida encontrada no seu histórico.");
                semHistoricoLabel.setFont(new Font("Montserrat", Font.ITALIC, 24));
                semHistoricoLabel.setForeground(Color.GRAY);
                painelConteudoHistorico.add(semHistoricoLabel);
            } else {
                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                for (HistoricoJogo hist : historicos) {
                    JPanel painelDoHistorico = new JPanel(new GridLayout(1, 2, 20, 0));
                    painelDoHistorico.setBackground(roxoClaro);
                    painelDoHistorico.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(rosa, 2), new EmptyBorder(15, 20, 15, 20)));
                    painelDoHistorico.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
                    
                    JLabel infoData = new JLabel("Data: " + hist.getDataPartida().format(formatador));
                    infoData.setFont(new Font("Montserrat", Font.BOLD, 20));
                    infoData.setForeground(Color.WHITE);

                    JLabel infoPontos = new JLabel("Acertos: " + hist.getAcertos() + " | Erros: " + hist.getErros());
                    infoPontos.setFont(new Font("Montserrat", Font.BOLD, 20));
                    infoPontos.setForeground(Color.WHITE);
                    infoPontos.setHorizontalAlignment(SwingConstants.RIGHT);

                    painelDoHistorico.add(infoData);
                    painelDoHistorico.add(infoPontos);
                    
                    painelConteudoHistorico.add(painelDoHistorico);
                    painelConteudoHistorico.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
    }
}
