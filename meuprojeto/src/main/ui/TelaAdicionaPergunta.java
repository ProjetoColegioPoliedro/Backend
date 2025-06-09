package ui;

import service.QuestaoService;
import model.Questao;
import model.Alternativa;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TelaAdicionaPergunta extends JFrame {

    private QuestaoService questaoService;
    private JTextField campoPerg;
    private JTextField campoRespA;
    private JTextField campoRespB;
    private JTextField campoRespC;
    private JTextField campoRespD;
    private JComboBox<String> nivelDif;
    private JComboBox<String> materias;

    private JTextField campoCorretoSelecionado = null; // Armazena o JTextField da alternativa correta
    private Map<JTextField, Border> bordasPadraoRespostas = new HashMap<>(); // Guarda as bordas iniciais

    public TelaAdicionaPergunta(Runnable telaAreaRes, QuestaoService questaoService) {
        this.questaoService = questaoService;

        var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40);
        var ciano = new Color(30, 180, 195);
        var vermelho = new Color(224, 73, 73);
        var verde = new Color(81, 207, 123);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(false);

        var fundoTela = new JPanel(null, true);
        fundoTela.setBackground(roxo);
        setContentPane(fundoTela);

        campoPerg = new JTextField("Digite a pergunta aqui...");
        campoPerg.setBounds(180, 70, 1200, 80);
        campoPerg.setFont(new Font("Montserrat", Font.ITALIC, 20));
        campoPerg.setForeground(Color.BLACK);
        campoPerg.setBackground(Color.WHITE);
        campoPerg.setBorder(BorderFactory.createLineBorder(rosa, 7));
        fundoTela.add(campoPerg);

        campoRespA = new JTextField("Resposta A");
        campoRespA.setBounds(300, 220, 1000, 80);
        campoRespA.setFont(new Font("Montserrat", Font.ITALIC, 20));
        campoRespA.setForeground(Color.BLACK);
        campoRespA.setBackground(Color.WHITE);
        campoRespA.setBorder(BorderFactory.createLineBorder(ciano, 7));
        fundoTela.add(campoRespA);

        campoRespB = new JTextField("Resposta B");
        campoRespB.setBounds(300, 330, 1000, 80);
        campoRespB.setFont(new Font("Montserrat", Font.ITALIC, 20));
        campoRespB.setForeground(Color.BLACK);
        campoRespB.setBackground(Color.WHITE);
        campoRespB.setBorder(BorderFactory.createLineBorder(ciano, 7));
        fundoTela.add(campoRespB);

        campoRespC = new JTextField("Resposta C");
        campoRespC.setBounds(300, 440, 1000, 80);
        campoRespC.setFont(new Font("Montserrat", Font.ITALIC, 20));
        campoRespC.setForeground(Color.BLACK);
        campoRespC.setBackground(Color.WHITE);
        campoRespC.setBorder(BorderFactory.createLineBorder(ciano, 7));
        fundoTela.add(campoRespC);

        campoRespD = new JTextField("Resposta D");
        campoRespD.setBounds(300, 550, 1000, 80);
        campoRespD.setFont(new Font("Montserrat", Font.ITALIC, 20));
        campoRespD.setForeground(Color.BLACK);
        campoRespD.setBackground(Color.WHITE);
        campoRespD.setBorder(BorderFactory.createLineBorder(ciano, 7));
        fundoTela.add(campoRespD);

        bordasPadraoRespostas.put(campoRespA, BorderFactory.createLineBorder(ciano, 7));
        bordasPadraoRespostas.put(campoRespB, BorderFactory.createLineBorder(ciano, 7));
        bordasPadraoRespostas.put(campoRespC, BorderFactory.createLineBorder(ciano, 7));
        bordasPadraoRespostas.put(campoRespD, BorderFactory.createLineBorder(ciano, 7));

        MouseAdapter respostaClickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Map.Entry<JTextField, Border> entry : bordasPadraoRespostas.entrySet()) {
                    entry.getKey().setBorder(entry.getValue());
                }
                JTextField clicado = (JTextField) e.getSource();
                clicado.setBorder(BorderFactory.createLineBorder(verde, 7));
                campoCorretoSelecionado = clicado;
            }
        };

        campoRespA.addMouseListener(respostaClickListener);
        campoRespB.addMouseListener(respostaClickListener);
        campoRespC.addMouseListener(respostaClickListener);
        campoRespD.addMouseListener(respostaClickListener);

        var circulo = new ImageIcon("assets\\circuloAlternativa.png");
        var c = circulo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        adicionarCirculoComLetra(fundoTela, c, 220, 235, "A");
        adicionarCirculoComLetra(fundoTela, c, 220, 345, "B");
        adicionarCirculoComLetra(fundoTela, c, 220, 460, "C");
        adicionarCirculoComLetra(fundoTela, c, 220, 560, "D");

        var edit = new ImageIcon("assets\\edit.png");
        var edicao = edit.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        var iconeEdicaoPerg = new JLabel(new ImageIcon(edicao));
        fundoTela.add(iconeEdicaoPerg);
        iconeEdicaoPerg.setBounds(1390, 95, 30, 30);

        var cancelar = new JButton("Cancelar");
        fundoTela.add(cancelar);
        cancelar.setBounds(470, 720, 150, 40);
        cancelar.setBackground(vermelho);
        cancelar.setFont(new Font("Montserrat", Font.BOLD, 20));
        cancelar.setForeground(Color.WHITE);
        cancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                telaAreaRes.run();
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
                salvarNovaPergunta(telaAreaRes);
            }
        });

        var dificuldadeLabel = new JLabel("Dificuldade: ");
        dificuldadeLabel.setBounds(270, 133, 200, 100);
        dificuldadeLabel.setFont(new Font("Montserrat", Font.BOLD, 20));
        dificuldadeLabel.setForeground(Color.WHITE);
        fundoTela.add(dificuldadeLabel);

        String [] opcoesDificuldade = {"Fácil", "Médio", "Difícil"};
        nivelDif = new JComboBox<>(opcoesDificuldade);
        fundoTela.add(nivelDif);
        nivelDif.setBounds(390, 170, 100, 30);
        nivelDif.setBackground(rosa);

        var materiaLabel = new JLabel("Materia: ");
        materiaLabel.setBounds(1010, 133, 200, 100);
        materiaLabel.setFont(new Font("Montserrat", Font.BOLD, 20));
        materiaLabel.setForeground(Color.WHITE);
        fundoTela.add(materiaLabel);

        String [] materiaOpcoes = {"Português", "Matemática", "Inglês", "Ciências Humanas", "Ciências da Natureza"};
        materias = new JComboBox<>(materiaOpcoes);
        fundoTela.add(materias);
        materias.setBounds(1100, 170, 180, 30);
        materias.setBackground(rosa);
    }

    private void adicionarCirculoComLetra(JPanel panel, Image img, int x, int y, String letra) {
        var circuloLabel = new JLabel(new ImageIcon(img));
        panel.add(circuloLabel);
        circuloLabel.setBounds(x, y, 50, 50);

        var letraLabel = new JLabel(letra);
        letraLabel.setBounds(11, 0, 50, 50);
        letraLabel.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraLabel.setForeground(Color.WHITE);
        circuloLabel.add(letraLabel);
    }

    // --- Lógica para Salvar a Nova Pergunta ---
    private void salvarNovaPergunta(Runnable telaAreaRes) {
        String enunciado = campoPerg.getText().trim();
        String respA = campoRespA.getText().trim();
        String respB = campoRespB.getText().trim();
        String respC = campoRespC.getText().trim();
        String respD = campoRespD.getText().trim();
        
        String dificuldadeSelecionada = (String) nivelDif.getSelectedItem();
        String materiaSelecionada = (String) materias.getSelectedItem();

        // 1. Validação dos campos
        if (enunciado.isEmpty() || respA.isEmpty() || respB.isEmpty() || respC.isEmpty() || respD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos de pergunta e respostas devem ser preenchidos.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (campoCorretoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione a alternativa correta (clique na borda da resposta).", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- Mapeamento de String para ID (Dificuldade e Matéria) ---
        int idNivelMapped = mapDificuldadeToId(dificuldadeSelecionada);
        int idMateriaMapped = mapMateriaToId(materiaSelecionada);

        // 2. Coletar alternativas e identificar a correta
        List<Alternativa> alternativas = new ArrayList<>();
        Alternativa altCorretaParaSalvar = null;

        if (!respA.isEmpty()) {
            Alternativa alt = new Alternativa(respA, (campoRespA == campoCorretoSelecionado)); // Construtor Alternativa(String, boolean)
            alternativas.add(alt);
            if (campoRespA == campoCorretoSelecionado) altCorretaParaSalvar = alt;
        }
        if (!respB.isEmpty()) {
            Alternativa alt = new Alternativa(respB, (campoRespB == campoCorretoSelecionado));
            alternativas.add(alt);
            if (campoRespB == campoCorretoSelecionado) altCorretaParaSalvar = alt;
        }
        if (!respC.isEmpty()) {
            Alternativa alt = new Alternativa(respC, (campoRespC == campoCorretoSelecionado));
            alternativas.add(alt);
            if (campoRespC == campoCorretoSelecionado) altCorretaParaSalvar = alt;
        }
        if (!respD.isEmpty()) {
            Alternativa alt = new Alternativa(respD, (campoRespD == campoCorretoSelecionado));
            alternativas.add(alt);
            if (campoRespD == campoCorretoSelecionado) altCorretaParaSalvar = alt;
        }

        if (altCorretaParaSalvar == null) {
             JOptionPane.showMessageDialog(this, "Erro interno: Não foi possível identificar a alternativa correta selecionada.", "Erro", JOptionPane.ERROR_MESSAGE);
             return;
        }

        // 3. Criar o objeto Questao (usando o construtor que aceita int para Nível e Matéria)
        // Você precisará definir explicacaoErro, anoLetivo, ajuda ou passá-los como null/0
        Questao novaQuestao = new Questao(
            enunciado,              // String enunciado
            null,                   // String explicacaoErro (não capturado na tela)
            0,                      // int anoLetivo (não capturado na tela)
            idNivelMapped,          // int idNivel
            idMateriaMapped         // int idMateria
        );
        
        // As alternativas e a alternativa correta são setadas *após* a criação do objeto Questao
        novaQuestao.setAlternativas(alternativas);
        novaQuestao.setAlternativaCorreta(altCorretaParaSalvar);


        try {
            questaoService.adicionarQuestao(novaQuestao);
            JOptionPane.showMessageDialog(this, "Pergunta adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            telaAreaRes.run();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar pergunta: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        campoPerg.setText("Digite a pergunta aqui...");
        campoRespA.setText("Resposta A");
        campoRespB.setText("Resposta B");
        campoRespC.setText("Resposta C");
        campoRespD.setText("Resposta D");

        Color ciano = new Color(30, 180, 195);
        campoRespA.setBorder(BorderFactory.createLineBorder(ciano, 7));
        campoRespB.setBorder(BorderFactory.createLineBorder(ciano, 7));
        campoRespC.setBorder(BorderFactory.createLineBorder(ciano, 7));
        campoRespD.setBorder(BorderFactory.createLineBorder(ciano, 7));

        campoCorretoSelecionado = null;
        nivelDif.setSelectedIndex(0);
        materias.setSelectedIndex(0);
    }
    
    // --- Mapeamento de String para ID (Dificuldade e Matéria) ---
    // Estes métodos devem ser adicionados à classe TelaAdicionaPergunta
    private int mapDificuldadeToId(String dificuldadeNome) {
        switch (dificuldadeNome) {
            case "Fácil": return 1;
            case "Médio": return 2;
            case "Difícil": return 3;
            default: return 0; // Ou lance uma exceção para dificuldade inválida
        }
    }

    private int mapMateriaToId(String materiaNome) {
        switch (materiaNome) {
            case "Português": return 1;
            case "Matemática": return 2;
            case "Inglês": return 3;
            case "Ciências Humanas": return 4;
            case "Ciências da Natureza": return 5;
            default: return 0; // Ou lance uma exceção para matéria inválida
        }
    }
}