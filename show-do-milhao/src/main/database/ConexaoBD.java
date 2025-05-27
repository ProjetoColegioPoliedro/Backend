//conexão com o bando de dados
import java.sql.Connection;
import java.sql.DriverManager;
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
        props.load(new FileInputStream("config.properties"));

        this.host = props.getProperty("db.host");
        this.port = props.getProperty("db.port");
        this.db = props.getProperty("db.name");
        this.user = props.getProperty("db.user");
        this.password = props.getProperty("db.password");
    }

    public Connection obterConexao() throws Exception {
        var s = String.format("jdbc:mysql://%s:%s/%s", host, port, db);
        Connection c = DriverManager.getConnection(s, user, password);
        return c;
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
