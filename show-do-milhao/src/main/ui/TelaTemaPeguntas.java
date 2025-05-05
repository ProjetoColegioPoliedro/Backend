import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
// import java.awt.event.*;

public class TelaTemaPeguntas {
    public static void main(String[] args){
        Color ciano = new Color(28, 180, 194);

        // Criação da tela
        JFrame tela = new JFrame("TELA INICIAL");
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setSize(710, 800);
        tela.setExtendedState(Frame.MAXIMIZED_BOTH);
        tela.setLocationRelativeTo(null);
        
        JPanel fundoTela = new JPanel(null, true);
        fundoTela.setBackground(ciano);
        tela.setContentPane(fundoTela);
        
        JPanel painelMenor = new JPanel(null);
        painelMenor.setSize(500, 550);
        painelMenor.setOpaque(false);
        fundoTela.add(painelMenor);
        
        
        // Botões do app
        JButton portugues = new JButton("Português");
        portugues.setBounds(460, 240, 150, 50);
        portugues.setFont(new Font("Inter", Font.PLAIN, 22));
        portugues.setBackground(Color.WHITE);
        fundoTela.add(portugues);
        
        JButton matematica = new JButton("Matemática");
        fundoTela.add(matematica);
        matematica.setBounds(700, 240, 150, 50);
        matematica.setFont(new Font("Inter", Font.PLAIN, 22));
        matematica.setBackground(Color.WHITE);

        JButton historia = new JButton("História");
        fundoTela.add(historia);
        historia.setBounds(940, 240, 150, 50);
        historia.setFont(new Font("Inter", Font.PLAIN, 22));
        historia.setBackground(Color.WHITE);
        
        JButton geografia = new JButton("Geografia");
        fundoTela.add(geografia);
        geografia.setBounds(460, 350, 150, 50);
        geografia.setFont(new Font("Inter", Font.PLAIN, 22));
        geografia.setBackground(Color.WHITE);
        
        JButton biologia = new JButton("Biologia");
        fundoTela.add(biologia);
        biologia.setBounds(700, 350, 150, 50);
        biologia.setFont(new Font("Inter", Font.PLAIN, 22));
        biologia.setBackground(Color.WHITE);
        
        JButton ingles = new JButton("Inglês");
        fundoTela.add(ingles);
        ingles.setBounds(940, 350, 150, 50);
        ingles.setFont(new Font("Inter", Font.PLAIN, 22));
        ingles.setBackground(Color.WHITE);
        
        JButton fisica = new JButton("Física");
        fundoTela.add(fisica);
        fisica.setBounds(460, 460, 150, 50);
        fisica.setFont(new Font("Inter", Font.PLAIN, 22));
        fisica.setBackground(Color.WHITE);

        JButton quimica = new JButton("Química");
        fundoTela.add(quimica);
        quimica.setBounds(700, 460, 150, 50);
        quimica.setFont(new Font("Inter", Font.PLAIN, 22));
        quimica.setBackground(Color.WHITE);
        
        JButton jogar = new JButton("Jogar");
        fundoTela.add(jogar);
        jogar.setBounds(1320, 680, 150, 50);
        jogar.setFont(new Font("Inter", Font.PLAIN, 22));
        jogar.setBackground(Color.WHITE);

        // JButton [] botoes = {portugues, matematica, historia, geografia, ingles, biologia, fisica, quimica, jogar};
        
        // "Tema das perguntas"
        JLabel temaPerguntas = new JLabel("Tema das perguntas");
        temaPerguntas.setBounds(30, 10, 250, 40);
        temaPerguntas.setFont(new Font("Inter", Font.PLAIN, 25));
        fundoTela.add(temaPerguntas);
        
        // Logo
        ImageIcon showMilhao = new ImageIcon("show_do_milhao.png");
        Image logo = showMilhao.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        JLabel imagemMilhao = new JLabel(new ImageIcon(logo));
        fundoTela.add(imagemMilhao);
        imagemMilhao.setBounds(1200, 30, 300, 150);
        
        // int largura = tela.getContentPane().getSize().width;
        // int altura = tela.getContentPane().getSize().height;
        // System.out.println(largura + "x" + altura);

        // tela.addComponentListener(new ComponentAdapter() {
        //     @Override
        //     public void componentResized(ComponentEvent evento) {
        //       // fazer calculo para corrigir a posição dos componentes quando o tamanho da tela for alterado.
        //     }
        // });
        

        tela.setVisible(true);
    }
}
