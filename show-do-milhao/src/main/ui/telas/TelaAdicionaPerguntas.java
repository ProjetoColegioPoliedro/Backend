import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TelaAdicionaPerguntas extends JFrame{
    TelaAdicionaPerguntas(){
        var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40); 
        var ciano = new Color(30, 180, 195);
       
        // Criação da tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(false);
        
        var fundoTela = new JPanel(null, true);
        fundoTela.setBackground(roxo);
        setContentPane(fundoTela);

        var caixaRespA = new JLabel("Resposta");
        caixaRespA.setBounds(300, 200, 1000, 80);
        caixaRespA.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaRespA.setForeground(Color.WHITE);
        caixaRespA.setBorder(BorderFactory.createLineBorder(ciano, 7));
        fundoTela.add(caixaRespA);
        
        var caixaRespB = new JLabel("Resposta");
        caixaRespB.setBounds(300, 310, 1000, 80);
        caixaRespB.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaRespB.setForeground(Color.WHITE);
        caixaRespB.setBorder(BorderFactory.createLineBorder(ciano, 7));
        fundoTela.add(caixaRespB);
        
        var caixaRespC = new JLabel("Resposta");
        caixaRespC.setBounds(300, 420, 1000, 80);
        caixaRespC.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaRespC.setForeground(Color.WHITE);
        caixaRespC.setBorder(BorderFactory.createLineBorder(ciano, 7));
        fundoTela.add(caixaRespC);
        
        var caixaRespD = new JLabel("Resposta");
        caixaRespD.setBounds(300, 530, 1000, 80);
        caixaRespD.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaRespD.setForeground(Color.WHITE);
        caixaRespD.setBorder(BorderFactory.createLineBorder(ciano, 7));
        fundoTela.add(caixaRespD);

        var respostas = new ArrayList<JLabel>();
        respostas.add(caixaRespA);
        respostas.add(caixaRespB);
        respostas.add(caixaRespC);
        respostas.add(caixaRespD);

        // percorrer lista com for e adiconar evento (?)

        // var labelVerde = new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent caixaSelecao){
        //     {
        //             r.setBorder(BorderFactory.createLineBorder(ciano, 7));
        //         }
        //         JLabel clicado = (Jlabel) caixaSelecao.getSource();
        //         clicado.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        //     }
        // };

    
        var caixaPerg = new JLabel("Pergunta");
        caixaPerg.setBounds(180, 50, 1200, 80);
        caixaPerg.setFont(new Font("Montserrat", Font.ITALIC,20));
        caixaPerg.setForeground(Color.WHITE);
        caixaPerg.setBorder(BorderFactory.createLineBorder(rosa, 7));
        fundoTela.add(caixaPerg);

        var circulo = new ImageIcon("circuloAlternativa.png");
        var c = circulo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        var circ = new JLabel(new ImageIcon(c));
        fundoTela.add(circ);
        circ.setBounds(220, 215, 50, 50);

        var letraA = new JLabel("A");
        letraA.setBounds(11, 0, 50, 50);
        letraA.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraA.setForeground(Color.WHITE);
        circ.add(letraA);
       
        var circulo1 = new JLabel(new ImageIcon(c));
        fundoTela.add(circulo1);
        circulo1.setBounds(220, 325, 50, 50);

        var letraB = new JLabel("B");
        letraB.setBounds(11, 0, 50, 50);
        letraB.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraB.setForeground(Color.WHITE);
        circulo1.add(letraB);
       
        var circulo2 = new JLabel(new ImageIcon(c));
        fundoTela.add(circulo2);
        circulo2.setBounds(220, 440, 50, 50);

        var letraC = new JLabel("C");
        letraC.setBounds(11, 0, 50, 50);
        letraC.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraC.setForeground(Color.WHITE);
        circulo2.add(letraC);
       
        var circulo3 = new JLabel(new ImageIcon(c));
        fundoTela.add(circulo3);
        circulo3.setBounds(220, 540, 50, 50);

        var letraD = new JLabel("D");
        letraD.setBounds(11, 0, 50, 50);
        letraD.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraD.setForeground(Color.WHITE);
        circulo3.add(letraD);

        var icone = new ImageIcon("seta.png");
        var imagem = icone.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        var seta = new JLabel(new ImageIcon(imagem));
        fundoTela.add(seta);
        seta.setBounds(20, 20, 60, 60);
        seta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                var tAR = new TelaAreaRestrita();
                tAR.setVisible(true);
                dispose();
            }
        });

        var edit = new ImageIcon("edit.png");
        var edicao = edit.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        var iconeEdicao = new JLabel(new ImageIcon(edicao));
        caixaRespA.add(iconeEdicao);
        iconeEdicao.setBounds(935, 12, 50, 50);

        var icone2 = new JLabel(new ImageIcon(edicao));
        caixaRespB.add(icone2);
        icone2.setBounds(935, 12, 50, 50);
        
        var icone3 = new JLabel(new ImageIcon(edicao));
        caixaRespC.add(icone3);
        icone3.setBounds(935, 12, 50, 50);
        
        var icone4 = new JLabel(new ImageIcon(edicao));
        caixaRespD.add(icone4);
        icone4.setBounds(935, 12, 50, 50);

        setVisible(true);
    }
}
