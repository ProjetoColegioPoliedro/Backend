package ui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import service.AuthService; // Importa a classe AuthService para usar o serviço de autenticação

public class TelaLogin extends JFrame{
    // Adiciona uma instância do serviço de autenticação, que será usada para verificar credenciais
    private AuthService authService; 

    // Construtor da TelaLogin. Recebe Runnables para as ações de navegação.
    public TelaLogin(Runnable voltaTelaAnterior, Runnable fgtSenha, Runnable configs, Runnable showTelaAdmin, Runnable showTelaMenuEstudante){
        // Inicializa o AuthService. Ele será responsável por interagir com os DAOs.
        this.authService = new AuthService(); 

        // Configurações básicas da janela (JFrame)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(710, 800);
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setExtendedState(Frame.MAXIMIZED_BOTH); // Maximiza a janela para preencher a tela
        
        // Definição de cores personalizadas
        var cinza = new Color(217, 217, 217);
        var rosa = new Color(238, 33, 82);
        var roxo = new Color(20, 14, 40);
        
        // Painel de fundo da tela (maior), usando layout nulo para posicionamento absoluto
        var painelInicial = new JPanel(null, true);
        painelInicial.setSize(800, 800); // Tamanho inicial, ajustado pelo redimensionamento
        painelInicial.setBackground(roxo);
        add(painelInicial); // Adiciona o painel ao JFrame
        
        // Painel menor (onde ficam os campos de login e botões), centralizado
        var painelMenor = new JPanel(null);
        painelMenor.setBackground(Color.WHITE);
        painelMenor.setBounds(100, 100, 500, 550); // Posição e tamanho iniciais
        painelInicial.add(painelMenor, BorderLayout.CENTER); // Adiciona o painel menor ao painel inicial
        
        // Título da tela de login
        var login = new JLabel("Faça o seu login");
        painelMenor.add(login);
        login.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 25));
        login.setBounds(90, 120, 200, 100);
        
        // Campo de texto para o E-mail (login)
        var digiteEmail = new JTextField();
        digiteEmail.setColumns(30);
        digiteEmail.setBackground(cinza);
        digiteEmail.setBounds(90, 230, 320, 40);
        painelMenor.add(digiteEmail);
        digiteEmail.setFont(new Font("New Cordial", Font.ITALIC, 10));
        
        var email = new JLabel("E-mail");
        email.setFont(new Font("Cordia New", Font.BOLD, 12));
        email.setBounds(90, 210, 200, 20);
        painelMenor.add(email);
        
        // Campo de senha (JPasswordField para ocultar a senha digitada)
        var digiteSenha = new JPasswordField(); // Importante: Usar JPasswordField para senhas
        digiteSenha.setColumns(30);
        digiteSenha.setBackground(cinza);
        digiteSenha.setBounds(90, 310, 320, 40);
        painelMenor.add(digiteSenha);
        digiteSenha.setFont(new Font("Cordia New", Font.ITALIC, 10));
        
        var senha = new JLabel("Senha");
        senha.setFont(new Font("Cordia New", Font.BOLD, 12));
        senha.setBounds(90, 290, 200, 20);
        painelMenor.add(senha);
        
        // Botão "Entrar" - Ação de autenticação principal
        var entrar = new JButton("Entrar");
        painelMenor.add(entrar);
        entrar.setFont(new Font("Cordia New", Font.BOLD, 14));
        entrar.setBounds(300, 390, 110, 30);
        entrar.setBackground(rosa);
        entrar.setForeground(Color.WHITE);
        
        // ActionListener para o botão "Entrar"
        entrar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evento){
                String loginDigitado = digiteEmail.getText().trim(); // Pega o texto do email e remove espaços
                // Converte a senha de char[] (seguro) para String (para passar ao AuthService)
                String senhaDigitada = new String(digiteSenha.getPassword()).trim(); 

                // Validação de campos vazios
                if (loginDigitado.isEmpty() || senhaDigitada.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos!", "ERRO - CAMPO VAZIO", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    // Chama o serviço de autenticação para verificar as credenciais
                    int userType = authService.autenticarUsuario(loginDigitado, senhaDigitada);

                    // Processa o resultado da autenticação
                    if (userType == 0){ // Retorno 0 significa Aluno autenticado
                        JOptionPane.showMessageDialog(null, "Login de Aluno efetuado com sucesso!", "Confirmação de Login", JOptionPane.INFORMATION_MESSAGE);
                        showTelaMenuEstudante.run(); // Executa o Runnable para abrir a tela do menu de estudante
                        dispose(); // Fecha a tela de login
                    } else if (userType == 1){ // Retorno 1 significa Professor/Admin autenticado
                        JOptionPane.showMessageDialog(null, "Login de Professor/Admin efetuado com sucesso!", "Confirmação de Login", JOptionPane.INFORMATION_MESSAGE);
                        showTelaAdmin.run(); // Executa o Runnable para abrir a tela do menu de administrador
                        dispose(); // Fecha a tela de login
                    } else { // Retorno -1 significa falha na autenticação
                        JOptionPane.showMessageDialog(null, "Login ou senha inválidos. Tente novamente.", "Falha na Autenticação", JOptionPane.ERROR_MESSAGE);
                        digiteSenha.setText(""); // Limpa o campo de senha por segurança
                    }
                }
            }
        });
        
        // Botão "Cancelar"
        var cancelar = new JButton("Cancelar");
        painelMenor.add(cancelar);
        cancelar.setFont(new Font("Cordia New", Font.BOLD, 14));
        cancelar.setBounds(90, 390, 110, 30);
        cancelar.setBackground(rosa);
        cancelar.setForeground(Color.WHITE);
        cancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent volta) {
                voltaTelaAnterior.run(); // Executa o Runnable para voltar à tela anterior (ex: Tela Inicial)
                dispose(); // Fecha a tela de login
            }
        });
        
        // Link "Esqueceu a senha?"
        var esqueceuSenha = new JLabel("Esqueceu a senha?");
        painelMenor.add(esqueceuSenha);
        esqueceuSenha.setFont(new Font("Arial", Font.ITALIC, 10));
        esqueceuSenha.setBounds(209, 420, 200, 100);
        esqueceuSenha.setForeground(Color.BLUE);
        // MouseListener para o link "Esqueceu a senha?"
        esqueceuSenha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                fgtSenha.run(); // Executa o Runnable para abrir a tela de recuperação de senha
                dispose(); // Fecha a tela de login
            }
        });
        
        // Imagens (logo e ícone de configurações)
        var icon = new ImageIcon("assets\\logo-poliedro-2.png");
        var poliedro = icon.getImage().getScaledInstance(150, 80, Image.SCALE_SMOOTH);
        var imagemPoliedro = new JLabel(new ImageIcon(poliedro));
        painelMenor.add(imagemPoliedro);
        imagemPoliedro.setBounds(170, 50, 150, 80);
        
        var icone = new ImageIcon("assets\\settings.png");
        var image = icone.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        var config = new JLabel(new ImageIcon(image));
        painelInicial.add(config);
        config.setBounds(1460, 20, 60, 60);
        // MouseListener para o ícone de configurações
        config.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                configs.run(); // Executa o Runnable para abrir a tela de configurações
                dispose(); // Fecha a tela de login
            }
        });
        
        // Listener para redimensionamento da janela (centraliza o painel menor)
        addComponentListener(new ComponentAdapter() {
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
    }
}