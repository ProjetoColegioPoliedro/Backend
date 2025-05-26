// Conexão com MySQL
import java.sql.Connection;
import java.sql.DriverManager;
public class ConnectionFactory{

    private String host = "show-do-milhao-show-do-milhao.l.aivencloud.com";
    private String port = "15138";
    private String db = "quiz_fortuna";
    private String user = "avnadmin";
    private String password = "AVNS_sN0NV7lU2hof1fe1Yas";

    public Connection obterConexao() throws Exception{
        var s = String.format(
            "jdbc:mysql://%s:%s/%s",
            host, port, db
        );
        Connection c = DriverManager.getConnection(s, user, password);
        return c;
    }

    public static void main(String[] args) throws Exception{
        var fabricaDeConexoes = new ConnectionFactory();
        Connection conexao = fabricaDeConexoes.obterConexao();
        if(conexao != null){
            System.out.println("Conectou!");
        }
        else{
            System.out.println("Não Conectou");
        }
    }
}
