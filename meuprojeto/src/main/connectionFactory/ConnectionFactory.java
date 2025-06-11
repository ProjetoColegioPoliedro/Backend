package connectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;

public class ConnectionFactory {
    private static String host; 
    private static String port; 
    private static String db;   
    private static String user; 
    private static String password; 
    private static String sslMode; 
    private static String serverTimezone; 

    private static boolean propertiesLoaded = false;

    private ConnectionFactory() {}
    private static void loadProperties() throws IOException {
        if (!propertiesLoaded) {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("config.properties")) {
                props.load(fis);
            }

            host = props.getProperty("db.host");
            port = props.getProperty("db.port");
            db = props.getProperty("db.name");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
            sslMode = props.getProperty("db.sslMode", "REQUIRED");
            serverTimezone = props.getProperty("db.serverTimezone", "UTC");
            propertiesLoaded = true; 
        }
    }
    public static Connection getConnection() throws SQLException {
        try {
            loadProperties(); 
        } catch (IOException e) {
            throw new SQLException("Não foi possível carregar as configurações do banco de dados.", e);
        }
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
        Connection conexao = null;
        Scanner userInputScanner = null;

        try {
            System.out.println("Tentando carregar configurações de config.properties...");
            
            System.out.println("Configurações carregadas. Tentando obter conexão...");
            conexao = ConnectionFactory.getConnection(); 

            if (conexao != null && !conexao.isClosed()) {
                System.out.println("SUCESSO: Conectado ao banco de dados!");
                System.out.println("A conexão permanecerá aberta.");

                if (args.length == 0 || !"no-wait".equalsIgnoreCase(args.length > 0 ? args[0] : "")) {
                    System.out.println("O programa principal está rodando. A conexão com o BD está ativa.");
                    System.out.println("Pressione Enter para finalizar o programa e fechar os recursos...");
                    userInputScanner = new Scanner(System.in);
                    userInputScanner.nextLine();
                } else {
                    System.out.println("Rodando em modo 'no-wait'. A conexão será fechada em breve se não houver mais lógica.");
                }
            } else {
                System.out.println("FALHA: Não conectou (conexão nula ou já fechada).");
            }
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
                ConnectionFactory.fecharConexao(conexao);
            }
            if (userInputScanner != null) {
                System.out.println("Fechando o Scanner...");
                userInputScanner.close();
            }
            System.out.println("Recursos fechados. Programa encerrado.");
        }
    }

}