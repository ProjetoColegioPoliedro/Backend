package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Certifique-se de que a interface LoginAuthCallback foi criada no pacote 'ui'
// import ui.LoginAuthCallback; // Se estiver em outro pacote, ajuste o import

public class TelaLogin extends JFrame {

    private JTextField campoLogin;
    private JPasswordField campoSenha;

    // Cores (ajuste conforme suas cores globais se tiver)
    private final Color ROXO = new Color(20, 14, 40);
    private final Color ROSA = new Color(238, 33, 82);
    private final Color CINZA = new Color(217, 217, 217);
    private final Color BRANCO = Color.WHITE;
    private final Color PRETO = Color.BLACK;
    private final Color CINZA_TEXTO_PADRAO = Color.GRAY;


    // Construtor com a nova interface de callback para autenticação
    // Ele recebe 3 Runnables (voltar, recuperar, configs) e 1 LoginAuthCallback
    public TelaLogin(Runnable voltaTelaInicial, Runnable recuperacaoSenha, Runnable configs, LoginAuthCallback authCallback) {
        // Configurações básicas da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(710, 800);
        setLocationRelativeTo(null);
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza a janela

        JPanel painelInicial = new JPanel(null);
        painelInicial.setBackground(ROXO);
        setContentPane(painelInicial);

        JPanel painelCentral = new JPanel(null);
        painelCentral.setBackground(BRANCO);
        painelCentral.setBounds(0, 0, 500, 500); // Tamanho fixo, será centralizado
        painelInicial.add(painelCentral);

        // Listener para centralizar o painelCentral ao redimensionar
        painelInicial.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evento) {
                int frameWidth = getWidth();
                int frameHeight = getHeight();
                int panelWidth = painelCentral.getWidth();
                int panelHeight = painelCentral.getHeight();
                int x = (frameWidth - panelWidth) / 2;
                int y = (frameHeight - panelHeight) / 2;
                painelCentral.setLocation(x, y);
            }
        });

        // Título "Login"
        JLabel tituloLogin = new JLabel("Login");
        tituloLogin.setFont(new Font("Montserrat", Font.BOLD, 28));
        tituloLogin.setForeground(ROXO);
        tituloLogin.setBounds(200, 80, 150, 40);
        painelCentral.add(tituloLogin);

        // Campos de Login e Senha
        JLabel labelLogin = new JLabel("Login:");
        labelLogin.setFont(new Font("Montserrat", Font.BOLD, 14));
        labelLogin.setBounds(90, 150, 100, 20);
        painelCentral.add(labelLogin);

        campoLogin = criarCampoTexto(painelCentral, "Digite seu login", 90, 170, 320, 40);

        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(new Font("Montserrat", Font.BOLD, 14));
        labelSenha.setBounds(90, 230, 100, 20);
        painelCentral.add(labelSenha);

        campoSenha = criarCampoSenha(painelCentral, "Digite sua senha", 90, 250, 320, 40);

        // Botão de Login
        JButton btnLogin = criarBotao(painelCentral, "Entrar", 200, 320, 100, 30, ROSA, BRANCO, e -> {
            String login = campoLogin.getText().trim();
            String senha = new String(campoSenha.getPassword());
            authCallback.authenticate(login, senha); // <--- Chama o callback de autenticação

            // IMPORTANTE: A TelaLogin deve ser fechada APENAS se a autenticação for bem-sucedida,
            // ou se o callback indicar que ela deve ser fechada.
            // Para simplificar, pode-se fechar aqui se o callback 'authenticate' não lançar exceção
            // ou se você tiver um retorno do callback.
            // Por enquanto, vamos deixar a decisão de fechar para o Navegador, após o resultado do callback.
            // Se o Navegador decidir mudar de tela, ele chamará dispose() na TelaLogin.
        });

        // Botões de navegação secundários
        JButton btnRecuperacaoSenha = criarBotao(painelCentral, "Recuperar Senha", 90, 370, 150, 30, ROSA, BRANCO, e -> {
            recuperacaoSenha.run();
            dispose();
        });
        JButton btnConfiguracoes = criarBotao(painelCentral, "Configurações", 250, 370, 150, 30, ROSA, BRANCO, e -> {
            configs.run();
            dispose();
        });
        JButton btnVoltar = criarBotao(painelCentral, "Voltar", 200, 420, 100, 30, ROSA, BRANCO, e -> {
            voltaTelaInicial.run();
            dispose();
        });
    }

    // --- Métodos Auxiliares para Criação de Componentes e Placeholders ---
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

    private JPasswordField criarCampoSenha(JPanel parentPanel, String placeholder, int x, int y, int width, int height) {
        JPasswordField campo = new JPasswordField(placeholder);
        campo.setBounds(x, y, width, height);
        campo.setBackground(CINZA);
        campo.setForeground(CINZA_TEXTO_PADRAO);
        campo.setFont(new Font("Montserrat", Font.PLAIN, 14));
        parentPanel.add(campo);
        adicionarPlaceholderListener(campo, placeholder);
        return campo;
    }

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
}
