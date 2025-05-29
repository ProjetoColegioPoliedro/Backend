// Conexão com MySQL

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;

public class ConnectionFactory {
    private String host;
    private String port;
    private String db;
    private String user;
    private String password;

    public ConnectionFactory() throws Exception {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        }

        this.host = props.getProperty("db.host");
        this.port = props.getProperty("db.port");
        this.db = props.getProperty("db.name");
        this.user = props.getProperty("db.user");
        this.password = props.getProperty("db.password");
    }

    public Connection obterConexao() throws Exception {
        // Coloquei um parâmetro de fuso para o MySQL
        var s = String.format("jdbc:mysql://%s:%s/%s?useTimezone=true&serverTimezone=America/Sao_Paulo", host, port, db);
        Connection c = DriverManager.getConnection(s, user, password);
        return c;
    }

    public static void fecharConexao(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                // System.out.println("Conexão fechada."); // Opcional para depuração
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }

    public static void fecharConexao(Connection conn, PreparedStatement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
                // System.out.println("PreparedStatement fechado."); // Opcional para depuração
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar o PreparedStatement: " + e.getMessage());
        } finally {
            fecharConexao(conn); 
        }
    }

    public static void fecharConexao(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
                
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar o ResultSet: " + e.getMessage());
        } finally {
            fecharConexao(conn, stmt); 
        }
    }

    public static void main(String[] args) throws Exception {
        var fabricaDeConexoes = new ConnectionFactory();
        Connection conexao = fabricaDeConexoes.obterConexao();
        if (conexao != null) {
            System.out.println("Conectou!");
        } else {
            System.out.println("Não Conectou");
        }
    }
}