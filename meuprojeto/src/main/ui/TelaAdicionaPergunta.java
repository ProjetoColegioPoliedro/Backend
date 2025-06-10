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
    private JTextArea campoExplicacaoErro;

    private JTextField campoCorretoSelecionado = null; // Armazena o JTextField da alternativa correta
    private Map<JTextField, Border> bordasPadraoRespostas = new HashMap<>(); // Guarda as bordas iniciais para resetar

    // Cores usadas na UI
    private final Color ROSA = new Color(238, 33, 82);
    private final Color ROXO = new Color(20, 14, 40);
    private final Color CIANO = new Color(30, 180, 195);
    private final Color VERMELHO = new Color(224, 73, 73);
    private final Color VERDE = new Color(81, 207, 123);
    private final Color PRETO = Color.BLACK;
    private final Color BRANCO = Color.WHITE;
    private final Color CINZA_TEXTO_PADRAO = Color.GRAY;

    public TelaAdicionaPergunta(Runnable telaAreaRes, QuestaoService questaoService) {
        this.questaoService = questaoService;

        configurarJanela();
        
        // O painel principal usará GridBagLayout para controle total
        JPanel fundoTela = new JPanel(new GridBagLayout()); 
        fundoTela.setBackground(ROXO);
        setContentPane(fundoTela);

        inicializarComponentes(fundoTela, telaAreaRes);
        adicionarListenersDeInteratividade();
        adicionarCirculosEIcones(fundoTela);
    }

    // --- Métodos de Configuração da Janela ---
    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000); // Tamanho inicial pode ser flexível agora
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        // setResizable(false); // Remova esta linha se quiser que a janela seja redimensionável pelo usuário
    }

    // --- Métodos de Inicialização de Componentes e Layout ---
    private void inicializarComponentes(JPanel panel, Runnable telaAreaRes) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre componentes

        // Campo da Pergunta (linha 0)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5; // Ocupa 5 colunas
        gbc.fill = GridBagConstraints.HORIZONTAL; // Preenche horizontalmente
        gbc.weightx = 1.0; // Expande horizontalmente
        campoPerg = criarCampoTexto("Digite a pergunta aqui...", ROSA);
        panel.add(campoPerg, gbc);

        // Ícone de Edição para a Pergunta (coluna separada ao lado, mas visualmente próximo)
        gbc.gridx = 5; // Coluna após o campo de pergunta
        gbc.gridy = 0;
        gbc.gridwidth = 1; // Ocupa 1 coluna
        gbc.fill = GridBagConstraints.NONE; // Não expande
        gbc.weightx = 0; // Não expande
        gbc.anchor = GridBagConstraints.WEST; // Alinha à esquerda
        ImageIcon editIcon = new ImageIcon("assets\\edit.png");
        Image edicao = editIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel iconeEdicaoPerg = new JLabel(new ImageIcon(edicao));
        panel.add(iconeEdicaoPerg, gbc);


        // Dificuldade e Matéria (linha 1)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST; // Alinha o label à direita
        panel.add(criarLabel("Dificuldade: ", 20, Font.BOLD, BRANCO), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        String [] opcoesDificuldade = {"Fácil", "Médio", "Difícil"};
        nivelDif = criarComboBox(opcoesDificuldade, ROSA);
        panel.add(nivelDif, gbc);

        gbc.gridx = 2; // Coluna vazia para espaçamento ou outro componente
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5; // Dá um pouco de peso para expandir
        panel.add(Box.createHorizontalStrut(10), gbc); // Espaçador invisível

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(criarLabel("Materia: ", 20, Font.BOLD, BRANCO), gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Ocupa 2 colunas para o combobox
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        String [] materiaOpcoes = {"Português", "Matemática", "Inglês", "Ciências Humanas", "Ciências da Natureza"};
        materias = criarComboBox(materiaOpcoes, ROSA);
        panel.add(materias, gbc);

        // Campos das Respostas (linhas 2 a 5)
        gbc.gridx = 1; // Começa na coluna 1 (após a letra do círculo)
        gbc.gridwidth = 4; // Ocupa 4 colunas para o campo de texto da resposta
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Expande horizontalmente
        gbc.anchor = GridBagConstraints.CENTER; // Alinha o campo no centro da célula

        gbc.gridy = 2; campoRespA = criarCampoTexto("Resposta A", CIANO); panel.add(campoRespA, gbc);
        gbc.gridy = 3; campoRespB = criarCampoTexto("Resposta B", CIANO); panel.add(campoRespB, gbc);
        gbc.gridy = 4; campoRespC = criarCampoTexto("Resposta C", CIANO); panel.add(campoRespC, gbc);
        gbc.gridy = 5; campoRespD = criarCampoTexto("Resposta D", CIANO); panel.add(campoRespD, gbc);

        // Armazena as bordas padrão para resetar
        bordasPadraoRespostas.put(campoRespA, BorderFactory.createLineBorder(CIANO, 7));
        bordasPadraoRespostas.put(campoRespB, BorderFactory.createLineBorder(CIANO, 7));
        bordasPadraoRespostas.put(campoRespC, BorderFactory.createLineBorder(CIANO, 7));
        bordasPadraoRespostas.put(campoRespD, BorderFactory.createLineBorder(CIANO, 7));

        // Campo de Explicação/Resolução (linhas 6 e 7)
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(criarLabel("Explicação/Resolução:", 20, Font.BOLD, BRANCO), gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 6; // Ocupa todas as colunas
        gbc.fill = GridBagConstraints.BOTH; // Preenche em ambas as direções
        gbc.weighty = 1.0; // Permite expandir verticalmente (importante para JTextArea em JScrollPane)
        campoExplicacaoErro = criarAreaTexto("Detalhes sobre a resolução da questão ou explicação do erro...", ROXO, 3);
        panel.add(new JScrollPane(campoExplicacaoErro), gbc); // Adiciona o JScrollPane

        // Botões (linha 8)
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 3; // Ocupa 3 colunas (metade da largura)
        gbc.fill = GridBagConstraints.NONE; // Não preenche
        gbc.weightx = 0.5; // Divide o espaço horizontalmente
        gbc.weighty = 0; // Não expande verticalmente
        gbc.anchor = GridBagConstraints.EAST; // Alinha à direita na primeira metade
        criarBotao(panel, "Cancelar", VERMELHO, BRANCO, e -> {
            telaAreaRes.run();
            dispose();
        }, gbc); // Passa gbc aqui

        gbc.gridx = 3; // Começa na 4ª coluna (para centralizar o grupo ou à direita)
        gbc.gridy = 8;
        gbc.gridwidth = 3; // Ocupa 3 colunas
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST; // Alinha à esquerda na segunda metade
        criarBotao(panel, "Salvar", VERDE, BRANCO, e -> salvarNovaPergunta(telaAreaRes), gbc); // Passa gbc aqui
    }

    // --- Métodos de Criação de Componentes Auxiliares Refatorados ---
    // Retorna o JTextField criado, para poder adicioná-lo ao painel com GBC
    private JTextField criarCampoTexto(String textoPadrao, Color borderColor) {
        JTextField campo = new JTextField(textoPadrao);
        campo.setFont(new Font("Montserrat", Font.ITALIC, 20));
        campo.setForeground(CINZA_TEXTO_PADRAO);
        campo.setBackground(BRANCO);
        campo.setBorder(BorderFactory.createLineBorder(borderColor, 7));
        
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(textoPadrao)) {
                    campo.setText("");
                    campo.setForeground(PRETO);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(textoPadrao);
                    campo.setForeground(CINZA_TEXTO_PADRAO);
                }
            }
        });
        return campo;
    }

    // Retorna o JTextArea criado, para poder adicioná-lo ao painel com GBC
    private JTextArea criarAreaTexto(String textoPadrao, Color borderColor, int borderWidth) {
        JTextArea area = new JTextArea(textoPadrao);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Montserrat", Font.PLAIN, 16));
        area.setForeground(CINZA_TEXTO_PADRAO);
        area.setBackground(BRANCO);
        area.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
        
        area.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (area.getText().equals(textoPadrao)) {
                    area.setText("");
                    area.setForeground(PRETO);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (area.getText().isEmpty()) {
                    area.setText(textoPadrao);
                    area.setForeground(CINZA_TEXTO_PADRAO);
                }
            }
        });
        return area;
    }

    // Retorna o JLabel criado, para poder adicioná-lo ao painel com GBC
    private JLabel criarLabel(String texto, int fontSize, int fontStyle, Color foregroundColor) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Montserrat", fontStyle, fontSize));
        label.setForeground(foregroundColor);
        return label;
    }

    // Cria e adiciona o botão diretamente, usando as GBCs passadas
    private void criarBotao(JPanel panel, String texto, Color bgColor, Color fgColor, ActionListener actionListener, GridBagConstraints gbc) {
        JButton botao = new JButton(texto);
        botao.setBackground(bgColor);
        botao.setForeground(fgColor);
        botao.setFont(new Font("Montserrat", Font.BOLD, 20));
        panel.add(botao, gbc); // Adiciona com as GBCs
        botao.addActionListener(actionListener);
    }

    // Retorna o JComboBox criado, para poder adicioná-lo ao painel com GBC
    private JComboBox<String> criarComboBox(String[] opcoes, Color bgColor) {
        JComboBox<String> comboBox = new JComboBox<>(opcoes);
        comboBox.setBackground(bgColor);
        comboBox.setForeground(BRANCO);
        comboBox.setFont(new Font("Montserrat", Font.PLAIN, 16));
        return comboBox;
    }

    // --- Métodos de Adição de Listeners e Elementos Visuais ---
    private void adicionarListenersDeInteratividade() {
        MouseAdapter respostaClickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Map.Entry<JTextField, Border> entry : bordasPadraoRespostas.entrySet()) {
                    entry.getKey().setBorder(entry.getValue());
                }
                JTextField clicado = (JTextField) e.getSource();
                clicado.setBorder(BorderFactory.createLineBorder(VERDE, 7));
                campoCorretoSelecionado = clicado;
            }
        };

        campoRespA.addMouseListener(respostaClickListener);
        campoRespB.addMouseListener(respostaClickListener);
        campoRespC.addMouseListener(respostaClickListener);
        campoRespD.addMouseListener(respostaClickListener);
    }

    private void adicionarCirculosEIcones(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Espaçamento (top, left, bottom, right)
        gbc.fill = GridBagConstraints.NONE; // Não preenche
        gbc.anchor = GridBagConstraints.CENTER; // Centraliza dentro da célula

        // Círculos e letras para as respostas (coluna 0)
        ImageIcon circuloIcon = new ImageIcon("assets\\circuloAlternativa.png");
        Image c = circuloIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        gbc.gridx = 0; gbc.gridy = 2; adicionarCirculoComLetra(panel, c, "A", gbc);
        gbc.gridy = 3; adicionarCirculoComLetra(panel, c, "B", gbc);
        gbc.gridy = 4; adicionarCirculoComLetra(panel, c, "C", gbc);
        gbc.gridy = 5; adicionarCirculoComLetra(panel, c, "D", gbc);
    }

    // Refatorado para adicionar com GridBagConstraints
    private void adicionarCirculoComLetra(JPanel panel, Image img, String letra, GridBagConstraints gbc) {
        JLabel circuloLabel = new JLabel(new ImageIcon(img));
        
        JLabel letraLabel = new JLabel(letra);
        letraLabel.setBounds(11, 0, 50, 50); // Posição relativa ao circuloLabel (dentro dele)
        letraLabel.setFont(new Font("Montserrat", Font.BOLD, 40));
        letraLabel.setForeground(BRANCO);
        circuloLabel.add(letraLabel);

        // Ajusta as insets para o círculo
        gbc.insets = new Insets(10, 10, 10, 5); // Espaçamento menor à direita
        panel.add(circuloLabel, gbc);
    }

    // --- Lógica para Salvar a Nova Pergunta ---
    private void salvarNovaPergunta(Runnable telaAreaRes) {
        String enunciado = campoPerg.getText().trim();
        String respA = campoRespA.getText().trim();
        String respB = campoRespB.getText().trim();
        String respC = campoRespC.getText().trim();
        String respD = campoRespD.getText().trim();
        String explicacaoErro = campoExplicacaoErro.getText().trim();

        String dificuldadeSelecionada = (String) nivelDif.getSelectedItem();
        String materiaSelecionada = (String) materias.getSelectedItem();

        // 1. Validação dos campos
        if (enunciado.isEmpty() || respA.isEmpty() || respB.isEmpty() || respC.isEmpty() || respD.isEmpty() || explicacaoErro.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos (pergunta, respostas e explicação) devem ser preenchidos.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
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
            Alternativa alt = new Alternativa(respA, (campoRespA == campoCorretoSelecionado));
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

        // 3. Criar o objeto Questao
        Questao novaQuestao = new Questao(
            enunciado,              // String enunciado
            explicacaoErro,         // String explicacaoErro
            0,                      // int anoLetivo (não capturado na tela)
            idNivelMapped,          // int idNivel
            idMateriaMapped         // int idMateria
        );
        
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

    // --- Métodos de Limpeza ---
    private void limparCampos() {
        campoPerg.setText("Digite a pergunta aqui...");
        campoPerg.setForeground(CINZA_TEXTO_PADRAO);
        campoRespA.setText("Resposta A");
        campoRespA.setForeground(CINZA_TEXTO_PADRAO);
        campoRespB.setText("Resposta B");
        campoRespB.setForeground(CINZA_TEXTO_PADRAO);
        campoRespC.setText("Resposta C");
        campoRespC.setForeground(CINZA_TEXTO_PADRAO);
        campoRespD.setText("Resposta D");
        campoRespD.setForeground(CINZA_TEXTO_PADRAO);
        campoExplicacaoErro.setText("Detalhes sobre a resolução da questão ou explicação do erro...");
        campoExplicacaoErro.setForeground(CINZA_TEXTO_PADRAO);

        // Usar CIANO que é uma constante de classe
        campoRespA.setBorder(BorderFactory.createLineBorder(CIANO, 7));
        campoRespB.setBorder(BorderFactory.createLineBorder(CIANO, 7));
        campoRespC.setBorder(BorderFactory.createLineBorder(CIANO, 7));
        campoRespD.setBorder(BorderFactory.createLineBorder(CIANO, 7));

        campoCorretoSelecionado = null;
        nivelDif.setSelectedIndex(0);
        materias.setSelectedIndex(0);
    }
    
    // --- Mapeamento de String para ID (Dificuldade e Matéria) ---
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
