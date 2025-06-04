package dao;
import model.HistoricoJogo; // Importa a classe HistoricoJogo do seu pacote model
import connectionFactory.ConnectionFactory; // Importa sua classe de conexão

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date; // Import para java.sql.Date
import java.time.LocalDate; // Import para java.time.LocalDate
import java.util.ArrayList;
import java.util.List;

public class HistoricoJogoDAO {

    // SQL para inserir um novo histórico de jogo
    // Assumindo que id_historico é auto-incrementável no banco
    private static final String INSERIR_HISTORICO_SQL = "INSERT INTO historico_jogo (data_partida, acertos, erros, checkpoint_alcancado, pontuacao_total, id_aluno) VALUES (?, ?, ?, ?, ?, ?);";
    // SQL para selecionar um histórico pelo ID
    private static final String SELECIONAR_HISTORICO_POR_ID_SQL = "SELECT id_historico, data_partida, acertos, erros, checkpoint_alcancado, pontuacao_total, id_aluno FROM historico_jogo WHERE id_historico = ?;";
    // SQL para selecionar todos os históricos
    private static final String SELECIONAR_TODOS_HISTORICOS_SQL = "SELECT id_historico, data_partida, acertos, erros, checkpoint_alcancado, pontuacao_total, id_aluno FROM historico_jogo;";
    // SQL para selecionar todos os históricos de um aluno específico
    private static final String SELECIONAR_HISTORICOS_POR_ALUNO_SQL = "SELECT id_historico, data_partida, acertos, erros, checkpoint_alcancado, pontuacao_total, id_aluno FROM historico_jogo WHERE id_aluno = ? ORDER BY data_partida DESC;";
    // SQL para deletar um histórico pelo ID
    private static final String DELETAR_HISTORICO_SQL = "DELETE FROM historico_jogo WHERE id_historico = ?;";
    // SQL para atualizar os dados de um histórico
    private static final String ATUALIZAR_HISTORICO_SQL = "UPDATE historico_jogo SET data_partida = ?, acertos = ?, erros = ?, checkpoint_alcancado = ?, pontuacao_total = ?, id_aluno = ? WHERE id_historico = ?;";

    /**
     * Insere um novo registro de histórico de jogo no banco de dados.
     *
     * @param historico O objeto HistoricoJogo a ser inserido.
     * @return O ID do histórico inserido, ou -1 em caso de falha.
     */
    public int inserirHistoricoJogo(HistoricoJogo historico) {
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (historico == null) {
            System.err.println("Tentativa de inserir um HistoricoJogo nulo.");
            return -1;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(INSERIR_HISTORICO_SQL, Statement.RETURN_GENERATED_KEYS);

            pstmt.setDate(1, Date.valueOf(historico.getDataPartida())); // Converte LocalDate para java.sql.Date
            pstmt.setInt(2, historico.getAcertos());
            pstmt.setInt(3, historico.getErros());
            pstmt.setString(4, historico.getCheckpointAlcancado());
            pstmt.setInt(5, historico.getPontuacaoTotal());
            pstmt.setInt(6, historico.getIdAluno());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    historico.setIdHistorico(idGerado);
                    System.out.println("Histórico de jogo inserido com sucesso! ID: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir histórico de jogo.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir histórico de jogo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }

    /**
     * Busca um histórico de jogo pelo seu ID.
     *
     * @param idHistorico O ID do histórico a ser buscado.
     * @return Um objeto HistoricoJogo se encontrado, caso contrário null.
     */
    public HistoricoJogo buscarHistoricoJogoPorId(int idHistorico) {
        HistoricoJogo historico = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_HISTORICO_POR_ID_SQL);
            pstmt.setInt(1, idHistorico);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                historico = mapResultSetToHistoricoJogo(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar histórico de jogo por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return historico;
    }

    /**
     * Lista todos os históricos de jogos cadastrados.
     *
     * @return Uma lista de objetos HistoricoJogo.
     */
    public List<HistoricoJogo> listarTodosHistoricosJogo() {
        List<HistoricoJogo> historicos = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_TODOS_HISTORICOS_SQL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                historicos.add(mapResultSetToHistoricoJogo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar todos os históricos de jogos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return historicos;
    }

    /**
     * Lista todos os históricos de jogos de um aluno específico.
     *
     * @param idAluno O ID do aluno.
     * @return Uma lista de objetos HistoricoJogo para o aluno especificado.
     */
    public List<HistoricoJogo> listarHistoricosJogoPorAluno(int idAluno) {
        List<HistoricoJogo> historicos = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_HISTORICOS_POR_ALUNO_SQL);
            pstmt.setInt(1, idAluno);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                historicos.add(mapResultSetToHistoricoJogo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar históricos de jogos por aluno: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return historicos;
    }


    /**
     * Atualiza os dados de um histórico de jogo existente.
     *
     * @param historico O objeto HistoricoJogo com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarHistoricoJogo(HistoricoJogo historico) {
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (historico == null || historico.getIdHistorico() <= 0) {
            System.err.println("Tentativa de atualizar histórico nulo ou com ID inválido.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(ATUALIZAR_HISTORICO_SQL);

            pstmt.setDate(1, Date.valueOf(historico.getDataPartida()));
            pstmt.setInt(2, historico.getAcertos());
            pstmt.setInt(3, historico.getErros());
            pstmt.setString(4, historico.getCheckpointAlcancado());
            pstmt.setInt(5, historico.getPontuacaoTotal());
            pstmt.setInt(6, historico.getIdAluno());
            pstmt.setInt(7, historico.getIdHistorico());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Histórico de jogo atualizado com sucesso! ID: " + historico.getIdHistorico());
            } else {
                System.out.println("Nenhum histórico de jogo encontrado com o ID: " + historico.getIdHistorico() + " para atualização.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar histórico de jogo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }

    /**
     * Exclui um histórico de jogo do banco de dados pelo seu ID.
     *
     * @param idHistorico O ID do histórico a ser excluído.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     */
    public boolean excluirHistoricoJogo(int idHistorico) {
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idHistorico <= 0) {
             System.err.println("Tentativa de excluir histórico com ID inválido.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_HISTORICO_SQL);
            pstmt.setInt(1, idHistorico);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                excluido = true;
                System.out.println("Histórico de jogo excluído com sucesso! ID: " + idHistorico);
            } else {
                 System.out.println("Nenhum histórico de jogo encontrado com o ID: " + idHistorico + " para exclusão.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao excluir histórico de jogo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return excluido;
    }

    /**
     * Mapeia uma linha do ResultSet para um objeto HistoricoJogo.
     * @param rs O ResultSet contendo os dados do histórico.
     * @return Um objeto HistoricoJogo.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private HistoricoJogo mapResultSetToHistoricoJogo(ResultSet rs) throws SQLException {
        int idHistorico = rs.getInt("id_historico");
        LocalDate dataPartida = rs.getDate("data_partida").toLocalDate(); // Converte java.sql.Date para LocalDate
        int acertos = rs.getInt("acertos");
        int erros = rs.getInt("erros");
        String checkpointAlcancado = rs.getString("checkpoint_alcancado");
        int pontuacaoTotal = rs.getInt("pontuacao_total");
        int idAluno = rs.getInt("id_aluno");
        return new HistoricoJogo(idHistorico, dataPartida, acertos, erros, checkpointAlcancado, pontuacaoTotal, idAluno);
    }

    /**
     * Método utilitário para fechar os recursos JDBC.
     */
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar ResultSet: " + e.getMessage());
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
        }
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar Connection: " + e.getMessage());
        }
    }
}