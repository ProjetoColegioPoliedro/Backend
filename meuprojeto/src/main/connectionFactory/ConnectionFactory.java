package connectionFactory;

// Conexão com MySQL

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner; // Importado para usar apenas Scanner
import java.io.FileInputStream;
import java.io.IOException;

public class ConnectionFactory {
    private String host;
    private String port;
    private String db;
    private String user;
    private String password;
    private String sslMode;
    private String serverTimezone;

    public ConnectionFactory() throws IOException {
        Properties props = new Properties();
        // try-with-resources garante que fis seja fechado. Este NÃO deve ser o local do aviso.
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        }

        this.host = props.getProperty("db.host");
        this.port = props.getProperty("db.port");
        this.db = props.getProperty("db.name");
        this.user = props.getProperty("db.user");
        this.password = props.getProperty("db.password");
        this.sslMode = props.getProperty("db.sslMode", "REQUIRED");
        this.serverTimezone = props.getProperty("db.serverTimezone", "UTC");
    }

    public Connection obterConexao() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%s/%s?sslMode=%s&serverTimezone=%s",
                host,
                port,
                db,
                sslMode,
                serverTimezone);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC do MySQL não encontrado no classpath.", e);
        }
        return DriverManager.getConnection(url, user, password);
    }

    public static void fecharConexao(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    // System.out.println("Conexão fechada.");
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    public static void fecharConexao(Connection conn, PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o PreparedStatement: " + e.getMessage());
            }
        }
        fecharConexao(conn);
    }

    public static void fecharConexao(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar o ResultSet: " + e.getMessage());
            }
        }
        fecharConexao(conn, stmt);
    }

    public static void main(String[] args) {
        ConnectionFactory fabricaDeConexoes = null;
        Connection conexao = null;
        Scanner userInputScanner = null; // Declarar o Scanner aqui

        try {
            System.out.println("Tentando carregar configurações de config.properties...");
            fabricaDeConexoes = new ConnectionFactory();
            System.out.println("Configurações carregadas. Tentando obter conexão...");
            conexao = fabricaDeConexoes.obterConexao();

            if (conexao != null && !conexao.isClosed()) {
                System.out.println("SUCESSO: Conectado ao banco de dados!");
                System.out.println("A conexão permanecerá aberta.");

                if (args.length == 0 || !"no-wait".equalsIgnoreCase(args.length > 0 ? args[0] : "")) { // Permite rodar sem esperar se um argumento "no-wait" for passado
                    System.out.println("O programa principal está rodando. A conexão com o BD está ativa.");
                    System.out.println("Pressione Enter para finalizar o programa e fechar os recursos...");
                    userInputScanner = new Scanner(System.in); // Atribuir o Scanner
                    userInputScanner.nextLine(); // Esperar pelo Enter
                } else {
                    System.out.println("Rodando em modo 'no-wait'. A conexão será fechada em breve se não houver mais lógica.");
                    // Se houver outra lógica que mantém o programa vivo, ela continuaria aqui.
                    // Caso contrário, o programa pode terminar e o 'finally' abaixo será executado.
                }

            } else {
                System.out.println("FALHA: Não conectou (conexão nula ou já fechada).");
            }
        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO: Não foi possível carregar o arquivo 'config.properties'.");
            System.err.println("Detalhes: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("ERRO AO CONECTAR: Falha ao obter conexão com o banco de dados.");
            System.err.println("Detalhes da SQLException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERRO INESPERADO: Ocorreu um erro não previsto.");
            System.err.println("Detalhes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Bloco finally alcançado. Finalizando o programa...");
            if (conexao != null) {
                System.out.println("Fechando a conexão do BD...");
                fecharConexao(conexao);
            }
            if (userInputScanner != null) {
                System.out.println("Fechando o Scanner...");
                userInputScanner.close(); // Fechar o Scanner
            }
            System.out.println("Recursos fechados. Programa encerrado.");
        }
    }

    public static Connection getConnection() {
        throw new UnsupportedOperationException("Unimplemented method 'getConnection'");
    }
}