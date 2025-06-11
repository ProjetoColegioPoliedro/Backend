package dao;

import model.SessaoJogo; 
import connectionFactory.ConnectionFactory; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp; 
import java.sql.Types;    
import java.time.LocalDateTime; 
import java.util.ArrayList;
import java.util.List;

public class SessaoJogoDAO {

    // SQL para inserir uma nova sessão de jogo
    private static final String INSERIR_SESSAO_SQL = "INSERT INTO sessao_jogo (data_inicio, data_fim, modo_pratica, pontuacao_total, id_aluno) VALUES (?, ?, ?, ?, ?);";
    // SQL para selecionar uma sessão pelo ID
    private static final String SELECIONAR_SESSAO_POR_ID_SQL = "SELECT id_sessao, data_inicio, data_fim, modo_pratica, pontuacao_total, id_aluno FROM sessao_jogo WHERE id_sessao = ?;";
    // SQL para selecionar todas as sessões de um aluno específico, ordenadas pela mais recente primeiro
    private static final String SELECIONAR_SESSOES_POR_ALUNO_SQL = "SELECT id_sessao, data_inicio, data_fim, modo_pratica, pontuacao_total, id_aluno FROM sessao_jogo WHERE id_aluno = ? ORDER BY data_inicio DESC;";
    // SQL para deletar uma sessão pelo ID
    private static final String DELETAR_SESSAO_SQL = "DELETE FROM sessao_jogo WHERE id_sessao = ?;";
    // SQL para atualizar os dados de uma sessão (ex: para registrar data_fim e pontuacao_total)
    private static final String ATUALIZAR_SESSAO_SQL = "UPDATE sessao_jogo SET data_inicio = ?, data_fim = ?, modo_pratica = ?, pontuacao_total = ?, id_aluno = ? WHERE id_sessao = ?;";

    public int inserirSessaoJogo(SessaoJogo sessao) {
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (sessao == null || sessao.getIdAluno() <= 0 || sessao.getDataInicio() == null) {
            System.err.println("Tentativa de inserir SessaoJogo nula, com idAluno inválido ou dataInicio nula.");
            return -1;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(INSERIR_SESSAO_SQL, Statement.RETURN_GENERATED_KEYS);

            pstmt.setTimestamp(1, Timestamp.valueOf(sessao.getDataInicio()));
            if (sessao.getDataFim() != null) {
                pstmt.setTimestamp(2, Timestamp.valueOf(sessao.getDataFim()));
            } else {
                pstmt.setNull(2, Types.TIMESTAMP);
            }
            pstmt.setBoolean(3, sessao.isModoPratica());
            pstmt.setInt(4, sessao.getPontuacaoTotal());
            pstmt.setInt(5, sessao.getIdAluno());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    sessao.setIdSessao(idGerado);
                    System.out.println("Sessão de jogo inserida com sucesso! ID Sessão: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir sessão de jogo.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir sessão de jogo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }
    public boolean atualizarSessaoJogo(SessaoJogo sessao) {
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (sessao == null || sessao.getIdSessao() <= 0 || sessao.getIdAluno() <= 0 || sessao.getDataInicio() == null) {
            System.err.println("Tentativa de atualizar SessaoJogo nula ou com IDs/dataInicio inválidos.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(ATUALIZAR_SESSAO_SQL);

            pstmt.setTimestamp(1, Timestamp.valueOf(sessao.getDataInicio()));
            if (sessao.getDataFim() != null) {
                pstmt.setTimestamp(2, Timestamp.valueOf(sessao.getDataFim()));
            } else {
                pstmt.setNull(2, Types.TIMESTAMP);
            }
            pstmt.setBoolean(3, sessao.isModoPratica());
            pstmt.setInt(4, sessao.getPontuacaoTotal());
            pstmt.setInt(5, sessao.getIdAluno());
            pstmt.setInt(6, sessao.getIdSessao());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Sessão de jogo atualizada com sucesso! ID Sessão: " + sessao.getIdSessao());
            } else {
                System.out.println("Nenhuma sessão de jogo encontrada com ID: " + sessao.getIdSessao() + " para atualização.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar sessão de jogo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }
    public SessaoJogo buscarSessaoJogoPorId(int idSessao) {
        SessaoJogo sessao = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_SESSAO_POR_ID_SQL);
            pstmt.setInt(1, idSessao);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                sessao = mapResultSetToSessaoJogo(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar sessão de jogo por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return sessao;}
    public List<SessaoJogo> listarSessoesJogoPorAluno(int idAluno) {
        List<SessaoJogo> sessoes = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_SESSOES_POR_ALUNO_SQL);
            pstmt.setInt(1, idAluno);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                sessoes.add(mapResultSetToSessaoJogo(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar sessões de jogo por aluno: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);}
        return sessoes;
    }

    public boolean excluirSessaoJogo(int idSessao) {
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idSessao <= 0) {
             System.err.println("Tentativa de excluir SessaoJogo com ID inválido.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_SESSAO_SQL);
            pstmt.setInt(1, idSessao);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                excluido = true;
                System.out.println("Sessão de jogo excluída com sucesso! ID Sessão: " + idSessao);
            } else {
                 System.out.println("Nenhuma sessão de jogo encontrada com ID: " + idSessao + " para exclusão.");
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao excluir sessão de jogo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return excluido;
    }

    private SessaoJogo mapResultSetToSessaoJogo(ResultSet rs) throws SQLException {
        int idSessao = rs.getInt("id_sessao");
        LocalDateTime dataInicio = rs.getTimestamp("data_inicio").toLocalDateTime();
        
        Timestamp dataFimTimestamp = rs.getTimestamp("data_fim");
        LocalDateTime dataFim = (dataFimTimestamp != null) ? dataFimTimestamp.toLocalDateTime() : null;
        
        boolean modoPratica = rs.getBoolean("modo_pratica");
        int pontuacaoTotal = rs.getInt("pontuacao_total");
        int idAluno = rs.getInt("id_aluno");
        
        return new SessaoJogo(idSessao, dataInicio, dataFim, modoPratica, pontuacaoTotal, idAluno);
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