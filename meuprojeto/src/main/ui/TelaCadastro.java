package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.SQLException;
import service.AlunoService; // Importar o serviço de aluno
import model.Aluno;        // Importar o modelo de aluno

public class TelaCadastro extends JFrame {

    private AlunoService alunoService; // Atributo para o serviço de aluno
    private JTextField digiteNome; // NOVO: Para o nome completo (se diferente do login)
    private JTextField digiteLogin; // O antigo 'digiteUsuario'
    private JPasswordField digiteSenha;
    private JTextField digiteAnoLetivo; // NOVO: Para o ano letivo

    // Cores adicionais
    private final Color CINZA = new Color(217, 217, 217);
    private final Color ROSA = new Color(238, 33, 82);
    private final Color ROXO = new Color(20, 14, 40);
    private final Color PRETO = Color.BLACK; // Para texto digitado
    private final Color CINZA_TEXTO_PADRAO = Color.GRAY; // Para placeholders

    // Construtor ajustado para receber o Runnable e o AlunoService
    public TelaCadastro(Runnable telaAreaRes, AlunoService alunoService) {
        this.alunoService = alunoService; // Inicializa o serviço

        // Configurações básicas da tela JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(710, 800); // Tamanho inicial
        setLocationRelativeTo(null); // Centraliza a janela
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza a janela (apenas uma vez)

        // Painel de fundo
        JPanel painelInicial = new JPanel(null, true);
        painelInicial.setBackground(ROXO);
        setContentPane(painelInicial); // Usar setContentPane é preferível a add(painelInicial)

        // Painel menor branco centralizado
        JPanel painelMenor = new JPanel(null);
        painelMenor.setBackground(Color.WHITE);
        painelMenor.setBounds(0, 0, 500, 600); // Tamanho fixo, será centralizado
        painelInicial.add(painelMenor);

        // Adiciona um listener para centralizar o painel menor quando a janela for redimensionada
        painelInicial.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evento) {
                int frameWidth = getWidth();
                int frameHeight = getHeight();
                int painelMenorWidth = painelMenor.getWidth();
                int painelMenorHeight = painelMenor.getHeight();
                int x = (frameWidth - painelMenorWidth) / 2;
                int y = (frameHeight - painelMenorHeight) / 2;
                painelMenor.setLocation(x, y);
            }
        });

        // --- CAMPOS DE ENTRADA ---
        // Label "Cadastrar"
        JLabel cadastrarLabel = new JLabel("Cadastrar");
        cadastrarLabel.setFont(new Font("Montserrat", Font.BOLD, 20));
        cadastrarLabel.setBounds(90, 50, 200, 50); // Posição ajustada
        painelMenor.add(cadastrarLabel);

        // Logo - Poliedro (Caminho relativo)
        ImageIcon icone = new ImageIcon("assets/image.png"); // <--- Caminho corrigido!
        Image poliedro = icone.getImage().getScaledInstance(140, 70, Image.SCALE_SMOOTH);
        JLabel imagemPoliedro = new JLabel(new ImageIcon(poliedro));
        imagemPoliedro.setBounds(170, 0, 150, 80); // Posição ajustada
        painelMenor.add(imagemPoliedro);


        // Campo Nome
        JLabel nomeLabel = new JLabel("Nome Completo");
        nomeLabel.setFont(new Font("Montserrat", Font.BOLD, 12));
        nomeLabel.setBounds(90, 120, 200, 20);
        painelMenor.add(nomeLabel);
        digiteNome = criarCampoTexto(painelMenor, "Digite o nome completo", 90, 140, 320, 40);

        // Campo Login (era 'digiteUsuario')
        JLabel loginLabel = new JLabel("Login de Aluno");
        loginLabel.setFont(new Font("Montserrat", Font.BOLD, 12));
        loginLabel.setBounds(90, 190, 200, 20);
        painelMenor.add(loginLabel);
        digiteLogin = criarCampoTexto(painelMenor, "Digite seu login de aluno", 90, 210, 320, 40);

        // Campo Senha
        JLabel senhaLabel = new JLabel("Senha");
        senhaLabel.setFont(new Font("Montserrat", Font.BOLD, 12));
        senhaLabel.setBounds(90, 260, 200, 20);
        painelMenor.add(senhaLabel);
        digiteSenha = criarCampoSenha(painelMenor, "Digite sua senha", 90, 280, 320, 40);

        // Campo Ano Letivo
        JLabel anoLetivoLabel = new JLabel("Ano Letivo");
        anoLetivoLabel.setFont(new Font("Montserrat", Font.BOLD, 12));
        anoLetivoLabel.setBounds(90, 330, 200, 20);
        painelMenor.add(anoLetivoLabel);
        digiteAnoLetivo = criarCampoTexto(painelMenor, "Ex: 2023", 90, 350, 320, 40);


        // Botão Cancelar
        JButton cancelar = criarBotao(painelMenor, "Cancelar", 90, 430, 110, 30, ROSA, Color.WHITE, e -> { // Lambda com ActionEvent
            telaAreaRes.run();
            dispose();
        });

        // Botão Cadastre-se
        JButton cadastro = criarBotao(painelMenor, "Cadastre-se", 300, 430, 110, 30, ROSA, Color.WHITE, e -> { // Lambda com ActionEvent
            cadastrarAluno(telaAreaRes); // Chama o novo método de cadastro
        });

        // Mensagem de rodapé (reposicionada)
        JLabel msgm = new JLabel("*Apenas o professor pode realizar o cadastro");
        msgm.setFont(new Font("Montserrat", Font.BOLD, 12));
        msgm.setForeground(ROSA);
        msgm.setBounds(90, 470, 320, 20); // Posição ajustada
        painelMenor.add(msgm);
    }

    // --- Métodos Auxiliares para Criação de Componentes (melhorado) ---

    // Cria JTextField com placeholder
    private JTextField criarCampoTexto(JPanel parentPanel, String placeholder, int x, int y, int width, int height) {
        JTextField campo = new JTextField(placeholder);
        campo.setBounds(x, y, width, height);
        campo.setBackground(CINZA);
        campo.setForeground(CINZA_TEXTO_PADRAO);
        campo.setFont(new Font("Montserrat", Font.PLAIN, 14));
        parentPanel.add(campo);
        adicionarPlaceholderListener(campo, placeholder);
        return campo;
    }

    // Cria JPasswordField com placeholder
    private JPasswordField criarCampoSenha(JPanel parentPanel, String placeholder, int x, int y, int width, int height) {
        JPasswordField campo = new JPasswordField(placeholder);
        campo.setBounds(x, y, width, height);
        campo.setBackground(CINZA);
        campo.setForeground(CINZA_TEXTO_PADRAO);
        campo.setFont(new Font("Montserrat", Font.PLAIN, 14));
        parentPanel.add(campo);
        adicionarPlaceholderListener(campo, placeholder); // Usa o placeholder listener para senha
        return campo;
    }

    // Cria JButton
    private JButton criarBotao(JPanel parentPanel, String texto, int x, int y, int width, int height, Color bgColor, Color fgColor, ActionListener action) {
        JButton botao = new JButton(texto);
        botao.setBounds(x, y, width, height);
        botao.setBackground(bgColor);
        botao.setForeground(fgColor);
        botao.setFont(new Font("Montserrat", Font.BOLD, 13));
        botao.addActionListener(action);
        parentPanel.add(botao);
        return botao;
    }

    // Adiciona o comportamento de placeholder para JTextFields
    private void adicionarPlaceholderListener(JTextField campo, String placeholder) {
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder) && campo.getForeground().equals(CINZA_TEXTO_PADRAO)) {
                    campo.setText("");
                    campo.setForeground(PRETO);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(CINZA_TEXTO_PADRAO);
                }
            }
        });
    }

    // Adiciona o comportamento de placeholder para JPasswordFields
    private void adicionarPlaceholderListener(JPasswordField campo, String placeholder) {
        campo.setEchoChar((char) 0); // Torna o placeholder visível
        campo.setForeground(CINZA_TEXTO_PADRAO);
        campo.setText(placeholder); // Setando o texto diretamente

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String currentText = new String(campo.getPassword()); // Pega o texto atual
                if (currentText.equals(placeholder)) {
                    campo.setText("");
                    campo.setEchoChar('*'); // Volta para o caractere de senha
                    campo.setForeground(PRETO);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String currentText = new String(campo.getPassword()); // Pega o texto atual
                if (currentText.isEmpty()) {
                    campo.setText(placeholder);
                    campo.setEchoChar((char) 0); // Torna o placeholder visível
                    campo.setForeground(CINZA_TEXTO_PADRAO);
                }
            }
        });
    }

    // --- Lógica de Cadastro de Aluno ---
    private void cadastrarAluno(Runnable telaAreaRes) {
        String nome = digiteNome.getText().trim();
        String login = digiteLogin.getText().trim();
        String senha = new String(digiteSenha.getPassword());
        String anoLetivoStr = digiteAnoLetivo.getText().trim();

        // 1. Validação de campos na UI
        if (nome.isEmpty() || login.isEmpty() || senha.isEmpty() || anoLetivoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos obrigatórios!", "ERRO - CAMPO VAZIO", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int anoLetivo;
        try {
            anoLetivo = Integer.parseInt(anoLetivoStr);
            if (anoLetivo <= 0) {
                 JOptionPane.showMessageDialog(this, "Ano letivo inválido. Por favor, digite um número positivo.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                 return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ano letivo inválido. Por favor, digite um número.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Tenta cadastrar o aluno usando o serviço
            // O AlunoService vai lidar com a unicidade do login e o hash da senha
            Aluno novoAluno = alunoService.cadastrarAluno(nome, login, senha, anoLetivo);

            if (novoAluno != null) {
                JOptionPane.showMessageDialog(this, "Cadastro de aluno realizado com sucesso!", "Confirmação de cadastro", JOptionPane.INFORMATION_MESSAGE);
                limparCampos(); // Limpa os campos após o sucesso
                telaAreaRes.run(); // Volta para a tela anterior
                dispose();
            } else {
                // Isso só deve acontecer se o serviço não lançar exceção mas retornar null
                JOptionPane.showMessageDialog(this, "Falha ao cadastrar aluno.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            // Erros de validação (campos obrigatórios, login já existente, formato de email)
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            // Erros de banco de dados
            System.err.println("Erro SQL ao cadastrar aluno: " + ex.getMessage());
            ex.printStackTrace(); // Imprime a pilha de erros para depuração
            JOptionPane.showMessageDialog(this, "Erro de banco de dados ao cadastrar aluno: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Outros erros inesperados (ex: NoSuchAlgorithmException no hash da senha)
            System.err.println("Erro inesperado ao cadastrar: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado ao cadastrar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpar os campos após um cadastro bem-sucedido
    private void limparCampos() {
        digiteNome.setText("Digite o nome completo");
        digiteNome.setForeground(CINZA_TEXTO_PADRAO);
        digiteLogin.setText("Digite seu login de aluno");
        digiteLogin.setForeground(CINZA_TEXTO_PADRAO);
        digiteSenha.setText("Digite sua senha");
        digiteSenha.setForeground(CINZA_TEXTO_PADRAO);
        digiteSenha.setEchoChar((char) 0); // Torna o placeholder visível
        digiteAnoLetivo.setText("Ex: 2023");
        digiteAnoLetivo.setForeground(CINZA_TEXTO_PADRAO);
    }
}
