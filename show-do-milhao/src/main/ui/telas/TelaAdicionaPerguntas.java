import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TelaAdicionaPerguntas extends JFrame{
    TelaAdicionaPerguntas(){
        var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40);
        var ciano = new Color(30, 180, 195);
        var vermelho = new Color(224, 73, 73);
        var verde = new Color(81, 207, 123);

        // Criação da tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(false);

        var fundoTela = new JPanel(null, true);
        fundoTela.setBackground(roxo);
        setContentPane(fundoTela);

        // Caixas
        var caixaRespA = new JLabel("Resposta");
        caixaRespA.setBounds(300, 220, 1000, 80);
        caixaRespA.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaRespA.setForeground(Color.WHITE);
        caixaRespA.setBorder(BorderFactory.createLineBorder(ciano, 7));
        fundoTela.add(caixaRespA);

        var caixaRespB = new JLabel("Resposta");
        caixaRespB.setBounds(300, 330, 1000, 80);
        caixaRespB.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaRespB.setForeground(Color.WHITE);
        caixaRespB.setBorder(BorderFactory.createLineBorder(ciano, 7));
        fundoTela.add(caixaRespB);

        var caixaRespC = new JLabel("Resposta");
        caixaRespC.setBounds(300, 440, 1000, 80);
        caixaRespC.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaRespC.setForeground(Color.WHITE);
        caixaRespC.setBorder(BorderFactory.createLineBorder(ciano, 7));
        fundoTela.add(caixaRespC);

        var caixaRespD = new JLabel("Resposta");
        caixaRespD.setBounds(300, 550, 1000, 80);
        caixaRespD.setFont(new Font("Montserrat", Font.ITALIC, 20));
        caixaRespD.setForeground(Color.WHITE);
        caixaRespD.setBorder(BorderFactory.createLineBorder(ciano, 7));
        fundoTela.add(caixaRespD);

        var caixaPerg = new JLabel("Pergunta");
        caixaPerg.setBounds(180, 70, 1200, 80);
        caixaPerg.setFont(new Font("Montserrat", Font.ITALIC,20));
        caixaPerg.setForeground(Color.WHITE);
        caixaPerg.setBorder(BorderFactory.createLineBorder(rosa, 7));
        fundoTela.add(caixaPerg);

        // Guardando em lista
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

        // Arternativas
        var circulo = new ImageIcon("circuloAlternativa.png");
        var c = circulo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        var circ = new JLabel(new ImageIcon(c));
        fundoTela.add(circ);
        circ.setBounds(220, 235, 50, 50);

        var letraA = new JLabel("A");
        letraA.setBounds(11, 0, 50, 50);
        letraA.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraA.setForeground(Color.WHITE);
        circ.add(letraA);

        var circulo1 = new JLabel(new ImageIcon(c));
        fundoTela.add(circulo1);
        circulo1.setBounds(220, 345, 50, 50);

        var letraB = new JLabel("B");
        letraB.setBounds(11, 0, 50, 50);
        letraB.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraB.setForeground(Color.WHITE);
        circulo1.add(letraB);

        var circulo2 = new JLabel(new ImageIcon(c));
        fundoTela.add(circulo2);
        circulo2.setBounds(220, 460, 50, 50);

        var letraC = new JLabel("C");
        letraC.setBounds(11, 0, 50, 50);
        letraC.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraC.setForeground(Color.WHITE);
        circulo2.add(letraC);

        var circulo3 = new JLabel(new ImageIcon(c));
        fundoTela.add(circulo3);
        circulo3.setBounds(220, 560, 50, 50);

        var letraD = new JLabel("D");
        letraD.setBounds(11, 0, 50, 50);
        letraD.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraD.setForeground(Color.WHITE);
        circulo3.add(letraD);

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

        // Botões
        var cancelar = new JButton("Cancelar");
        fundoTela.add(cancelar);
        cancelar.setBounds(470, 720, 150, 40);
        cancelar.setBackground(vermelho);
        cancelar.setFont(new Font("Montserrat", Font.BOLD, 20));
        cancelar.setForeground(Color.WHITE);
        cancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                var tAR = new TelaAreaRestrita();
                tAR.setVisible(true);
                dispose();
            }
        });

        var salvar = new JButton("Salvar");
        fundoTela.add(salvar);
        salvar.setBounds(920, 720, 150, 40);
        salvar.setBackground(verde);
        salvar.setFont(new Font("Montserrat", Font.BOLD, 20));
        salvar.setForeground(Color.WHITE);
        salvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(null, "Pergunta adicionada com sucesso!", "Confirmação", 2);
                var tAR = new TelaAreaRestrita();
                tAR.setVisible(true);
                dispose();
            }
        });

        // Filtro
        var dificuldade = new JLabel("Dificuldade: ");
        dificuldade.setBounds(270, 133, 200, 100);
        dificuldade.setFont(new Font("Montserrat", Font.BOLD, 20));
        dificuldade.setForeground(Color.WHITE);
        fundoTela.add(dificuldade);

        String [] opcoes = {"Fácil", "Médio", "Difícil"};
        var nivelDif = new JComboBox<>(opcoes);
        fundoTela.add(nivelDif);
        nivelDif.setBounds(390, 170, 100, 30);
        nivelDif.setBackground(rosa);

        var materia = new JLabel("Materia: ");
        materia.setBounds(1010, 133, 200, 100);
        materia.setFont(new Font("Montserrat", Font.BOLD, 20));
        materia.setForeground(Color.WHITE);
        fundoTela.add(materia);

        String [] materiaOpcoes = {"Português", "Matemática", "Inglês", "Ciências Humanas", "Ciências da natureza"};
        var materias = new JComboBox<>(materiaOpcoes);
        fundoTela.add(materias);
        materias.setBounds(1100, 170, 120, 30);
        materias.setBackground(rosa);

        setVisible(true);
    }
}