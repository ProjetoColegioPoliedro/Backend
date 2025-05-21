import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TelaTemaPeguntas extends JFrame{
    TelaTemaPeguntas(){
        var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40); 
        var check = new ImageIcon("Check.png");
        var xIcon = new ImageIcon("x.png");
        
        // Criação da tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(false);
        
        var fundoTela = new JPanel(null, true);
        fundoTela.setBackground(roxo);
        setContentPane(fundoTela);
        
        var painelMenor = new JPanel(null);
        painelMenor.setBounds(70, 90, 1400, 650);
        painelMenor.setBackground(Color.WHITE);
        fundoTela.add(painelMenor);
                
        // "Tema das perguntas"
        var temaPerguntas = new JLabel("Escolha as matérias: ");
        temaPerguntas.setBounds(500, 15, 500, 40);
        temaPerguntas.setFont(new Font("Montserrat", Font.BOLD, 45));
        temaPerguntas.setForeground(Color.BLACK);
        painelMenor.add(temaPerguntas);

        // Caixa de seleção 
        var portugues = new JCheckBox();
        painelMenor.add(portugues);
        portugues.setBounds(250, 105, 50, 50);
        portugues.setBackground(Color.WHITE);
        portugues.setSelected(true);
        portugues.setIcon(xIcon);
        portugues.setSelectedIcon(check);

        var txtPort = new JLabel("Português");
        painelMenor.add(txtPort);
        txtPort.setFont(new Font("Montserrat", Font.ITALIC, 40));
        txtPort.setForeground(Color.BLACK);
        txtPort.setBounds(65, 95, 300, 60);
        
        var matematica = new JCheckBox();
        painelMenor.add(matematica);
        matematica.setBounds(273, 202, 50, 50);
        matematica.setBackground(Color.WHITE);
        matematica.setSelected(true);
        matematica.setIcon(xIcon);
        matematica.setSelectedIcon(check);

        var txtMat = new JLabel("Matemática");
        painelMenor.add(txtMat);
        txtMat.setFont(new Font("Montserrat", Font.ITALIC, 40));
        txtMat.setForeground(Color.BLACK);
        txtMat.setBounds(65, 190, 300, 60);
        
        var ingles = new JCheckBox("Inglês");
        painelMenor.add(ingles);
        ingles.setBounds(174, 295, 50, 50);
        ingles.setBackground(Color.WHITE);
        ingles.setSelected(true);
        ingles.setIcon(xIcon);
        ingles.setSelectedIcon(check);

        var txtIng = new JLabel("Inglês");
        painelMenor.add(txtIng);
        txtIng.setFont(new Font("Montserrat", Font.ITALIC, 40));
        txtIng.setForeground(Color.BLACK);
        txtIng.setBounds(65, 285, 300, 60);
        
        var cNatureza = new JCheckBox();
        painelMenor.add(cNatureza);
        cNatureza.setBounds(445, 390, 50, 50);
        cNatureza.setBackground(Color.WHITE);
        cNatureza.setSelected(true);
        cNatureza.setIcon(xIcon);
        cNatureza.setSelectedIcon(check);

        var txtCNa = new JLabel("Ciências da Natureza");
        painelMenor.add(txtCNa);
        txtCNa.setFont(new Font("Montserrat", Font.ITALIC, 40));
        txtCNa.setForeground(Color.BLACK);
        txtCNa.setBounds(65, 380, 400, 60);
        
        var cHumanas = new JCheckBox();
        painelMenor.add(cHumanas);
        cHumanas.setBounds(400, 485, 50, 50);
        cHumanas.setBackground(Color.WHITE);
        cHumanas.setSelected(true);
        cHumanas.setIcon(xIcon);
        cHumanas.setSelectedIcon(check);

        var txtChu = new JLabel("Ciências Humanas");
        painelMenor.add(txtChu);
        txtChu.setFont(new Font("Montserrat", Font.ITALIC, 40));
        txtChu.setForeground(Color.BLACK);
        txtChu.setBounds(65, 475, 400, 60);

        var iPartida = new JButton("Iniciar partida");
        iPartida.setFont(new Font("Montserrat", Font.BOLD, 20));
        iPartida.setForeground(Color.BLACK);
        iPartida.setBackground(rosa);
        painelMenor.add(iPartida);
        iPartida.setBounds(1170, 560, 200, 60);
        iPartida.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                var tP = new TelaPartida();
                tP.setVisible(true);
                dispose();
            }
        }); 

        var icone = new ImageIcon("seta.png");
        var imagem = icone.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        var seta = new JLabel(new ImageIcon(imagem));
        fundoTela.add(seta);
        seta.setBounds(20, 20, 60, 60);
        seta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                var tMI = new TelaMenuInicial();
                tMI.setVisible(true);
                dispose();
            }
        });



        setVisible(true);
        }
}