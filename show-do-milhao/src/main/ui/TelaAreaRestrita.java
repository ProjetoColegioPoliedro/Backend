import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaAreaRestrita {
    public static void main(String[] args) {
        Color ciano = new Color(28, 180, 194);
        Color cinza = new Color(217, 217, 217);

        // Criação da tela
        JFrame tela = new JFrame("Área restrita");
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setSize(710, 800);
        tela.setLocationRelativeTo(null);
        tela.setExtendedState(Frame.MAXIMIZED_BOTH);
        
        JPanel corFundo = new JPanel(null);
        tela.add(corFundo);
        corFundo.setBackground(ciano);
        corFundo.setSize(800, 800);
        
        JPanel painelBranco = new JPanel(null);
        painelBranco.setBackground(cinza);
        painelBranco.setBounds(100, 100, 500, 550);
        corFundo.add(painelBranco, BorderLayout.CENTER);
        
        // Ajuste nas dimensões
        tela.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evento) {
                int frameWidth = tela.getWidth();
                int frameHeight = tela.getHeight();
                int painelBrancoWidth = painelBranco.getWidth();
                int painelBrancoHeight = painelBranco.getHeight();
                int x = (frameWidth - painelBrancoWidth) / 2;
                int y = (frameHeight - painelBrancoHeight) / 2;
                painelBranco.setLocation(x, y);
            }
        });

        // Logo do poliedro
        ImageIcon icone = new ImageIcon("image.png");
        Image poliedro = icone.getImage().getScaledInstance(140, 70, Image.SCALE_SMOOTH);
        JLabel imagemPoliedro = new JLabel(new ImageIcon(poliedro));
        painelBranco.add(imagemPoliedro);
        imagemPoliedro.setBounds(170, 40, 150, 80);
        
        // Botões
        JButton addPergunta = new JButton("Adicionar pergunta");
        painelBranco.add(addPergunta);
        addPergunta.setBounds(100, 150, 300, 70);
        addPergunta.setBackground(Color.WHITE);
        addPergunta.setForeground(Color.BLACK);
        addPergunta.setFont(new Font("Inter", Font.PLAIN, 18));

        JButton edPergunta = new JButton("Editar pergunta");
        painelBranco.add(edPergunta);
        edPergunta.setBounds(100, 250, 300, 70);
        edPergunta.setFont(new Font("Inter", Font.PLAIN, 18));
        edPergunta.setBackground(Color.WHITE);
        edPergunta.setForeground(Color.BLACK);

        JButton cadAluno = new JButton("Cadastrar aluno");
        painelBranco.add(cadAluno);
        cadAluno.setFont(new Font("Inter", Font.PLAIN, 18));
        cadAluno.setBounds(100, 350, 300, 70);
        cadAluno.setBackground(Color.WHITE);
        cadAluno.setForeground(Color.BLACK);

        JButton voltar = new JButton("Voltar");
        painelBranco.add(voltar);
        voltar.setFont(new Font("Inter", Font.PLAIN, 18));
        voltar.setBounds(190, 470, 110, 30);
        voltar.setBackground(Color.BLACK);
        voltar.setForeground(Color.WHITE);

        
        tela.setVisible(true);
    }
}
