package dao;

import model.Ranking; 
import connectionFactory.ConnectionFactory; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp; 
import java.time.LocalDateTime; 
import java.util.ArrayList;
import java.util.List;

public class RankingDAO {

    // SQL para inserir um novo registro de ranking
    private static final String INSERIR_RANKING_SQL = "INSERT INTO ranking (id_aluno, pontuacao, ultima_atualizacao) VALUES (?, ?, ?);";
    // SQL para selecionar um ranking pelo ID (id_ranking)
    private static final String SELECIONAR_RANKING_POR_ID_SQL = "SELECT id_ranking, id_aluno, pontuacao, ultima_atualizacao FROM ranking WHERE id_ranking = ?;";
    // SQL para selecionar o ranking atual/melhor de um aluno específico
    // Ordena pela pontuação (maior primeiro) e depois pela atualização mais recente (para desempate, se necessário, ou apenas para pegar o mais recente)
    private static final String SELECIONAR_RANKING_POR_ALUNO_SQL = "SELECT id_ranking, id_aluno, pontuacao, ultima_atualizacao FROM ranking WHERE id_aluno = ? ORDER BY pontuacao DESC, ultima_atualizacao DESC LIMIT 1;";
    // SQL para selecionar os top N rankings (leaderboard)
    // Desempate pela data de atualização (mais antiga primeiro, para quem alcançou antes)
    private static final String SELECIONAR_TOP_N_RANKING_SQL = "SELECT id_ranking, id_aluno, pontuacao, ultima_atualizacao FROM ranking ORDER BY pontuacao DESC, ultima_atualizacao ASC LIMIT ?;";
    // SQL para deletar um ranking pelo ID
    private static final String DELETAR_RANKING_SQL = "DELETE FROM ranking WHERE id_ranking = ?;";
    // SQL para atualizar os dados de um registro de ranking
    private static final String ATUALIZAR_RANKING_SQL = "UPDATE ranking SET id_aluno = ?, pontuacao = ?, ultima_atualizacao = ? WHERE id_ranking = ?;";
     // SQL para atualizar a pontuação de um aluno (se o aluno já tiver um registro no ranking)
    // Esta é uma alternativa se id_aluno for UNIQUE na tabela ranking.
    // private static final String ATUALIZAR_PONTUACAO_POR_ALUNO_SQL = "UPDATE ranking SET pontuacao = ?, ultima_atualizacao = ? WHERE id_aluno = ?;";

    public int inserirRanking(Ranking ranking) {
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (ranking == null || ranking.getIdAluno() <= 0) {
            System.err.println("Tentativa de inserir Ranking nulo ou com idAluno inválido.");
            return -1;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(INSERIR_RANKING_SQL, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, ranking.getIdAluno());
            pstmt.setInt(2, ranking.getPontuacao());
            pstmt.setTimestamp(3, Timestamp.valueOf(ranking.getUltimaAtualizacao()));

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    ranking.setIdRanking(idGerado);
                    System.out.println("Registro de ranking inserido com sucesso! ID Ranking: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir registro de ranking.");
            }

        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro SQL: Aluno com ID " + ranking.getIdAluno() + " já pode ter um registro no ranking (se id_aluno for UNIQUE). " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao inserir registro de ranking: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }

    public boolean atualizarRanking(Ranking ranking) {
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (ranking == null || ranking.getIdRanking() <= 0 || ranking.getIdAluno() <= 0) {
            System.err.println("Tentativa de atualizar Ranking nulo ou com IDs inválidos.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(ATUALIZAR_RANKING_SQL);

            pstmt.setInt(1, ranking.getIdAluno());
            pstmt.setInt(2, ranking.getPontuacao());
            pstmt.setTimestamp(3, Timestamp.valueOf(ranking.getUltimaAtualizacao()));
            pstmt.setInt(4, ranking.getIdRanking());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Registro de ranking atualizado com sucesso! ID Ranking: " + ranking.getIdRanking());
            } else {
                System.out.println("Nenhum registro de ranking encontrado com ID: " + ranking.getIdRanking() + " para atualização.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar registro de ranking: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }
    
    public Ranking buscarRankingPorId(int idRanking) {
        Ranking ranking = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_RANKING_POR_ID_SQL);
            pstmt.setInt(1, idRanking);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                ranking = mapResultSetToRanking(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar ranking por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return ranking;
    }
    public Ranking buscarRankingPorAluno(int idAluno) {
        Ranking ranking = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_RANKING_POR_ALUNO_SQL);
            pstmt.setInt(1, idAluno);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                ranking = mapResultSetToRanking(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar ranking por aluno: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return ranking;
    }
    public List<Ranking> listarTopNRankings(int limite) {
        List<Ranking> rankings = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (limite <= 0) {
            System.err.println("Limite para listar Top N deve ser positivo.");
            return rankings; // Retorna lista vazia
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_TOP_N_RANKING_SQL);
            pstmt.setInt(1, limite);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                rankings.add(mapResultSetToRanking(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar top N rankings: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return rankings;
    }
    public boolean excluirRanking(int idRanking) {
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idRanking <= 0) {
             System.err.println("Tentativa de excluir Ranking com ID inválido.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_RANKING_SQL);
            pstmt.setInt(1, idRanking);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                excluido = true;
                System.out.println("Registro de ranking excluído com sucesso! ID Ranking: " + idRanking);
            } else {
                 System.out.println("Nenhum registro de ranking encontrado com ID: " + idRanking + " para exclusão.");
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao excluir registro de ranking: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return excluido;
    }
    private Ranking mapResultSetToRanking(ResultSet rs) throws SQLException {
        int idRanking = rs.getInt("id_ranking");
        int idAluno = rs.getInt("id_aluno");
        int pontuacao = rs.getInt("pontuacao");
        LocalDateTime ultimaAtualizacao = rs.getTimestamp("ultima_atualizacao").toLocalDateTime();
        return new Ranking(idRanking, idAluno, pontuacao, ultimaAtualizacao);
    }
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


