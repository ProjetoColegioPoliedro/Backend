package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectionFactory.ConnectionFactory;
import model.Alternativa;

public class AlternativaDAO {

    // SQL para inserir uma nova alternativa
    // Assumindo que id_alternativa é auto-incrementável no banco
    private static final String INSERIR_ALTERNATIVA_SQL = "INSERT INTO alternativa (texto) VALUES (?);";
    // SQL para selecionar uma alternativa pelo ID
    private static final String SELECIONAR_ALTERNATIVA_POR_ID_SQL = "SELECT id_alternativa, texto FROM alternativa WHERE id_alternativa = ?;";
    // SQL para selecionar todas as alternativas
    private static final String SELECIONAR_TODAS_ALTERNATIVAS_SQL = "SELECT id_alternativa, texto FROM alternativa;";
    // SQL para deletar uma alternativa pelo ID
    private static final String DELETAR_ALTERNATIVA_SQL = "DELETE FROM alternativa WHERE id_alternativa = ?;";
    // SQL para atualizar os dados de uma alternativa
    private static final String ATUALIZAR_ALTERNATIVA_SQL = "UPDATE alternativa SET texto = ? WHERE id_alternativa = ?;";

    /**
     * Insere uma nova alternativa no banco de dados.
     * @param <Alternativa>
     *
     * @param alternativa O objeto Alternativa a ser inserido (sem o id, se for auto-incrementável).
     * @return O ID da alternativa inserida, ou -1 em caso de falha.
     */

    public int inserirAlternativa(Alternativa alternativa) {
        int idGerado = -1; // Inicializa com um valor que indica falha
        // Declara os recursos JDBC fora do try para que possam ser fechados no finally
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 1. Obter a conexão do ConnectionFactory
            conexao = ConnectionFactory.getConnection(); // Use o nome da sua classe e método aqui

            // 2. Preparar a instrução SQL, retornando as chaves geradas
            pstmt = conexao.prepareStatement(INSERIR_ALTERNATIVA_SQL, Statement.RETURN_GENERATED_KEYS);

            // 3. Definir os parâmetros da instrução SQL
            pstmt.setString(1, alternativa.getTexto());

            // 4. Executar a instrução SQL
            int linhasAfetadas = pstmt.executeUpdate();

            // 5. Processar o resultado (obter o ID gerado)
            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys(); // Recupera as chaves geradas
                if (rs.next()) {
                    idGerado = rs.getInt(1); // Pega o ID da primeira coluna do ResultSet
                    alternativa.setIdAlternativa(idGerado); // Atualiza o objeto com o ID gerado
                    System.out.println("Alternativa inserida com sucesso! ID: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir alternativa.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir alternativa: " + e.getMessage());
            e.printStackTrace(); // Importante para depuração
            // Considere lançar uma exceção personalizada aqui se preferir
        } finally {
            // 6. Fechar os recursos no bloco finally
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos JDBC: " + ex.getMessage());
            }
        }
        return idGerado; // Retorna o ID gerado ou -1 em caso de falha
    }

    /**
     * Busca uma alternativa pelo seu ID.
     *
     * @param idAlternativa O ID da alternativa a ser buscada.
     * @return Um objeto Alternativa se encontrado, caso contrário null.
     */
    public Alternativa buscarAlternativaPorId(int idAlternativa) {
        Alternativa alternativa = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 1. Obter conexão
            conexao = ConnectionFactory.getConnection();

            // 2. Preparar SQL
            pstmt = conexao.prepareStatement(SELECIONAR_ALTERNATIVA_POR_ID_SQL);

            // 3. Definir parâmetro
            pstmt.setInt(1, idAlternativa);

            // 4. Executar consulta
            rs = pstmt.executeQuery();

            // 5. Processar ResultSet
            if (rs.next()) {
                String texto = rs.getString("texto");
                // Cria o objeto Alternativa com os dados do banco
                alternativa = new Alternativa(idAlternativa, texto);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar alternativa por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 6. Fechar recursos
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos JDBC: " + ex.getMessage());
            }
        }
        return alternativa;
    }

    /**
     * Lista todas as alternativas cadastradas no banco de dados.
     *
     * @return Uma lista de objetos Alternativa.
     */
    public List<Alternativa> listarTodasAlternativas() {
        List<Alternativa> alternativas = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null; // Usar PreparedStatement mesmo sem parâmetros para consistência
        ResultSet rs = null;

        try {
            // 1. Obter conexão
            conexao = ConnectionFactory.getConnection();

            // 2. Preparar SQL
            pstmt = conexao.prepareStatement(SELECIONAR_TODAS_ALTERNATIVAS_SQL);

            // 3. Executar consulta
            rs = pstmt.executeQuery();

            // 4. Processar ResultSet
            while (rs.next()) {
                int idAlternativa = rs.getInt("id_alternativa");
                String texto = rs.getString("texto");
                alternativas.add(new Alternativa(idAlternativa, texto));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar todas as alternativas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 5. Fechar recursos
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos JDBC: " + ex.getMessage());
            }
        }
        return alternativas;
    }

    /**
     * Atualiza os dados de uma alternativa existente no banco de dados.
     *
     * @param alternativa O objeto Alternativa com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarAlternativa(Alternativa alternativa) {
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        // Validação básica para garantir que a alternativa e o ID são válidos
        if (alternativa == null || alternativa.getIdAlternativa() <= 0) {
            System.err.println("Tentativa de atualizar alternativa nula ou com ID inválido.");
            return false;
        }

        try {
            // 1. Obter conexão
            conexao = ConnectionFactory.getConnection();

            // 2. Preparar SQL
            pstmt = conexao.prepareStatement(ATUALIZAR_ALTERNATIVA_SQL);

            // 3. Definir parâmetros
            pstmt.setString(1, alternativa.getTexto());
            pstmt.setInt(2, alternativa.getIdAlternativa());

            // 4. Executar atualização
            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Alternativa atualizada com sucesso! ID: " + alternativa.getIdAlternativa());
            } else {
                 System.out.println("Nenhuma alternativa encontrada com o ID: " + alternativa.getIdAlternativa() + " para atualização.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar alternativa: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 5. Fechar recursos
            try {
                if (pstmt != null) pstmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos JDBC: " + ex.getMessage());
            }
        }
        return atualizado;
    }

    /**
     * Exclui uma alternativa do banco de dados pelo seu ID.
     *
     * @param idAlternativa O ID da alternativa a ser excluída.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     */
    public boolean excluirAlternativa(int idAlternativa) {
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idAlternativa <= 0) {
            System.err.println("Tentativa de excluir alternativa com ID inválido.");
            return false;
        }

        try {
            // 1. Obter conexão
            conexao = ConnectionFactory.getConnection();

            // 2. Preparar SQL
            pstmt = conexao.prepareStatement(DELETAR_ALTERNATIVA_SQL);

            // 3. Definir parâmetro
            pstmt.setInt(1, idAlternativa);

            // 4. Executar exclusão
            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                excluido = true;
                System.out.println("Alternativa excluída com sucesso! ID: " + idAlternativa);
            } else {
                System.out.println("Nenhuma alternativa encontrada com o ID: " + idAlternativa + " para exclusão.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao excluir alternativa: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 5. Fechar recursos
            try {
                if (pstmt != null) pstmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar recursos JDBC: " + ex.getMessage());
            }
        }
        return excluido;
    }   
}    