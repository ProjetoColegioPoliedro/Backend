package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import model.Questao; // Adicione este import

public class TelaRespostaIncorreta extends JFrame {
    private Questao questaoAtual; // Adicione este atributo para armazenar a questão

    // Construtor modificado para receber a Questao
    public TelaRespostaIncorreta(Questao questao, Runnable contQuestao, boolean penultimaQuestao, boolean ultimaQuestão){ // Modificado aqui
        this.questaoAtual = questao; // Armazena a questão atual

        var roxo = new Color(20, 14, 40);

        // Configurações básicas da tela JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000, 2000); // Tamanho inicial
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza a janela
        setLocationRelativeTo(null); // Centraliza a janela na tela

        var corFundo = new JPanel(null, true);
        corFundo.setBackground(roxo);
        setContentPane(corFundo);

        // Label para a mensagem "Resposta incorreta"
        var respIncorreta = new JLabel("Resposta incorreta 😞");
        respIncorreta.setBounds(600, 290, 500, 70); // Ajuste a posição conforme sua UI
        respIncorreta.setFont(new Font("Montserrat", Font.BOLD, 40));
        respIncorreta.setForeground(Color.WHITE);
        corFundo.add(respIncorreta);

         // Possibilidade de resposta com operador ternário
        String mensagem = ultimaQuestão ? "Fim de jogo!\tVoltar para o menu." : "Ver solução";

        // Label/Botão "Ver solução"
        var verSolucao = new JLabel(mensagem); // Pode ser um JLabel com MouseListener ou um JButton
        verSolucao.setBounds(700, 340, 500, 70); // Ajuste a posição
        verSolucao.setFont(new Font("Montserrat", Font.ITALIC, 35));
        verSolucao.setForeground(Color.WHITE);
        corFundo.add(verSolucao);

        if(!ultimaQuestão){
            // Adiciona o MouseListener ao JLabel "Ver solução"
            verSolucao.addMouseListener(new MouseAdapter() {
                @Override // É bom manter o @Override para métodos sobrescritos
                public void mouseClicked(MouseEvent e){
                    dispose(); // Fecha a tela de resposta incorreta
                    // Executa o Runnable 'solucao' que foi passado, que por sua vez
                    // no Navegador, está configurado para chamar showTelaSolucao(questaoAtual, voltarParaMenu).
                    contQuestao.run(); 
                }
            });
        } else{
            verSolucao.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    dispose();
                    contQuestao.run();
                }
            });
        }
    }
}