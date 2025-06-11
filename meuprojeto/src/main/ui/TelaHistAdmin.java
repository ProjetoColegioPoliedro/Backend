// Substitua todo o arquivo ui/TelaHistAdmin.java
package ui;

import model.Aluno;
import model.HistoricoJogo;
import service.AlunoService;
import service.HistoricoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaHistAdmin extends JFrame {

    public TelaHistAdmin(Runnable telaMenuAdmin, Runnable configs, HistoricoService historicoService, AlunoService alunoService) {
        var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40);
        var roxoClaro = new Color(30, 24, 60);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setTitle("Histórico Geral de Jogos");

        var corFundo = new JPanel(new BorderLayout());
        corFundo.setBackground(roxo);
        setContentPane(corFundo);

        JPanel painelCabecalho = new JPanel(null);
        painelCabecalho.setBackground(roxo);
        painelCabecalho.setPreferredSize(new Dimension(0, 100));
        corFundo.add(painelCabecalho, BorderLayout.NORTH);
        
        var histJogo = new JLabel("Histórico Geral");
        histJogo.setHorizontalAlignment(SwingConstants.CENTER);
        histJogo.setBounds(0, 20, 1536, 70); // Centralizado na largura da tela
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
                telaMenuAdmin.run();
                dispose();
            }
        });

        var iconeConfig = new ImageIcon("assets\\settings.png");
        var image = iconeConfig.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        var config = new JLabel(new ImageIcon(image));
        config.setBounds(1460, 20, 60, 60);
        config.setCursor(new Cursor(Cursor.HAND_CURSOR));
        painelCabecalho.add(config);
        config.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                configs.run();
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
            List<HistoricoJogo> historicos = historicoService.buscarTodos();

            if (historicos.isEmpty()) {
                JLabel semHistoricoLabel = new JLabel("Nenhum histórico encontrado.");
                semHistoricoLabel.setFont(new Font("Montserrat", Font.ITALIC, 24));
                semHistoricoLabel.setForeground(Color.GRAY);
                semHistoricoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                painelConteudoHistorico.add(semHistoricoLabel);
            } else {
                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                for (HistoricoJogo hist : historicos) {
                    String nomeAluno = "Aluno não encontrado";
                    try {
                        Aluno aluno = alunoService.buscarPorId(hist.getIdAluno());
                        if (aluno != null) {
                            nomeAluno = aluno.getNome();
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao buscar nome do aluno com ID: " + hist.getIdAluno());
                    }
            
                    JPanel painelDoHistorico = new JPanel(new BorderLayout(20, 5));
                    painelDoHistorico.setBackground(roxoClaro);
                    painelDoHistorico.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(rosa, 2), new EmptyBorder(10, 20, 10, 20)));
                    painelDoHistorico.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

                    JLabel infoAluno = new JLabel(nomeAluno);
                    infoAluno.setFont(new Font("Montserrat", Font.BOLD, 22));
                    infoAluno.setForeground(Color.WHITE);
                    painelDoHistorico.add(infoAluno, BorderLayout.NORTH);

                    JPanel painelDetalhes = new JPanel(new GridLayout(1, 2));
                    painelDetalhes.setOpaque(false);

                    JLabel infoData = new JLabel("Data: " + hist.getDataPartida().format(formatador));
                    infoData.setFont(new Font("Montserrat", Font.PLAIN, 18));
                    infoData.setForeground(Color.LIGHT_GRAY);
                    painelDetalhes.add(infoData);
                    
                    JLabel infoPontos = new JLabel("Acertos: " + hist.getAcertos() + " | Erros: " + hist.getErros());
                    infoPontos.setFont(new Font("Montserrat", Font.PLAIN, 18));
                    infoPontos.setForeground(Color.LIGHT_GRAY);
                    infoPontos.setHorizontalAlignment(SwingConstants.RIGHT);
                    painelDetalhes.add(infoPontos);

                    painelDoHistorico.add(painelDetalhes, BorderLayout.CENTER);

                    painelConteudoHistorico.add(painelDoHistorico);
                    painelConteudoHistorico.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JLabel erroLabel = new JLabel("Erro ao carregar o histórico do banco de dados.");
            erroLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
            erroLabel.setForeground(Color.RED);
            painelConteudoHistorico.add(erroLabel);
        }
    }
}