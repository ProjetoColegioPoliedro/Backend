package dao;

import model.Questao; // Importa a classe Questao do seu pacote model
import connectionFactory.ConnectionFactory; // Importa sua classe de conexão

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QuestaoDAO {

    // SQL para inserir uma nova questão
    private static final String INSERIR_QUESTAO_SQL = "INSERT INTO questao (enunciado, explicacao_erro, ano_letivo, id_nivel, id_materia, ajuda) VALUES (?, ?, ?, ?, ?, ?);";
    // SQL para selecionar uma questão pelo ID
    private static final String SELECIONAR_QUESTAO_POR_ID_SQL = "SELECT id_questao, enunciado, explicacao_erro, ano_letivo, id_nivel, id_materia, ajuda FROM questao WHERE id_questao = ?;";
    // SQL para selecionar todas as questões
    private static final String SELECIONAR_TODAS_QUESTOES_SQL = "SELECT id_questao, enunciado, explicacao_erro, ano_letivo, id_nivel, id_materia, ajuda FROM questao ORDER BY id_materia, ano_letivo, id_nivel;";
    // SQL para selecionar questões por matéria
    private static final String SELECIONAR_QUESTOES_POR_MATERIA_SQL = "SELECT id_questao, enunciado, explicacao_erro, ano_letivo, id_nivel, id_materia, ajuda FROM questao WHERE id_materia = ? ORDER BY ano_letivo, id_nivel;";
    // SQL para selecionar questões por nível
    private static final String SELECIONAR_QUESTOES_POR_NIVEL_SQL = "SELECT id_questao, enunciado, explicacao_erro, ano_letivo, id_nivel, id_materia, ajuda FROM questao WHERE id_nivel = ? ORDER BY id_materia, ano_letivo;";
    // SQL para selecionar questões por ano letivo
    private static final String SELECIONAR_QUESTOES_POR_ANO_LETIVO_SQL = "SELECT id_questao, enunciado, explicacao_erro, ano_letivo, id_nivel, id_materia, ajuda FROM questao WHERE ano_letivo = ? ORDER BY id_materia, id_nivel;";
    // SQL para deletar uma questão pelo ID
    private static final String DELETAR_QUESTAO_SQL = "DELETE FROM questao WHERE id_questao = ?;";
    // SQL para atualizar os dados de uma questão
    private static final String ATUALIZAR_QUESTAO_SQL = "UPDATE questao SET enunciado = ?, explicacao_erro = ?, ano_letivo = ?, id_nivel = ?, id_materia = ?, ajuda = ? WHERE id_questao = ?;";

    /**
     * Insere uma nova questão no banco de dados.
     *
     * @param questao O objeto Questao a ser inserido.
     * @return O ID da questão inserida, ou -1 em caso de falha.
     */
    public int inserirQuestao(Questao questao) {
        int idGerado = -1;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (questao == null || questao.getEnunciado() == null || questao.getEnunciado().trim().isEmpty() ||
            questao.getIdNivel() <= 0 || questao.getIdMateria() <= 0) {
            System.err.println("Tentativa de inserir Questao nula, com enunciado vazio ou IDs de nível/matéria inválidos.");
            return -1;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(INSERIR_QUESTAO_SQL, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, questao.getEnunciado());
            pstmt.setString(2, questao.getExplicacaoErro());
            pstmt.setInt(3, questao.getAnoLetivo());
            pstmt.setInt(4, questao.getIdNivel());
            pstmt.setInt(5, questao.getIdMateria());
            pstmt.setString(6, questao.getAjuda());

            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    questao.setIdQuestao(idGerado);
                    System.out.println("Questão inserida com sucesso! ID: " + idGerado);
                }
            } else {
                System.err.println("Nenhuma linha afetada ao inserir questão.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir questão: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return idGerado;
    }

    /**
     * Busca uma questão pelo seu ID.
     *
     * @param idQuestao O ID da questão a ser buscada.
     * @return Um objeto Questao se encontrado, caso contrário null.
     */
    public Questao buscarQuestaoPorId(int idQuestao) {
        Questao questao = null;
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_QUESTAO_POR_ID_SQL);
            pstmt.setInt(1, idQuestao);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                questao = mapResultSetToQuestao(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar questão por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return questao;
    }

    /**
     * Lista todas as questões cadastradas.
     *
     * @return Uma lista de objetos Questao.
     */
    public List<Questao> listarTodasQuestoes() {
        List<Questao> questoes = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_TODAS_QUESTOES_SQL);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                questoes.add(mapResultSetToQuestao(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar todas as questões: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return questoes;
    }

    /**
     * Lista todas as questões de uma matéria específica.
     *
     * @param idMateria O ID da matéria.
     * @return Uma lista de objetos Questao.
     */
    public List<Questao> listarQuestoesPorMateria(int idMateria) {
        List<Questao> questoes = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_QUESTOES_POR_MATERIA_SQL);
            pstmt.setInt(1, idMateria);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                questoes.add(mapResultSetToQuestao(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar questões por matéria: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return questoes;
    }

    /**
     * Lista todas as questões de um nível de dificuldade específico.
     *
     * @param idNivel O ID do nível de dificuldade.
     * @return Uma lista de objetos Questao.
     */
    public List<Questao> listarQuestoesPorNivel(int idNivel) {
        List<Questao> questoes = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_QUESTOES_POR_NIVEL_SQL);
            pstmt.setInt(1, idNivel);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                questoes.add(mapResultSetToQuestao(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar questões por nível: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return questoes;
    }
    
    /**
     * Lista todas as questões de um ano letivo específico.
     *
     * @param anoLetivo O ano letivo.
     * @return Uma lista de objetos Questao.
     */
    public List<Questao> listarQuestoesPorAnoLetivo(int anoLetivo) {
        List<Questao> questoes = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(SELECIONAR_QUESTOES_POR_ANO_LETIVO_SQL);
            pstmt.setInt(1, anoLetivo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                questoes.add(mapResultSetToQuestao(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar questões por ano letivo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return questoes;
    }

    /**
     * Lista questões filtrando por matéria, nível e ano letivo.
     * Parâmetros <= 0 ou null/empty para Strings são ignorados no filtro.
     *
     * @param idMateria O ID da matéria (0 ou negativo para não filtrar).
     * @param idNivel O ID do nível (0 ou negativo para não filtrar).
     * @param anoLetivo O ano letivo (0 ou negativo para não filtrar).
     * @return Uma lista de objetos Questao.
     */
    public List<Questao> listarQuestoesFiltradas(int idMateria, int idNivel, int anoLetivo) {
        List<Questao> questoes = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        StringBuilder sqlBuilder = new StringBuilder("SELECT id_questao, enunciado, explicacao_erro, ano_letivo, id_nivel, id_materia, ajuda FROM questao WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (idMateria > 0) {
            sqlBuilder.append(" AND id_materia = ?");
            params.add(idMateria);
        }
        if (idNivel > 0) {
            sqlBuilder.append(" AND id_nivel = ?");
            params.add(idNivel);
        }
        if (anoLetivo > 0) {
            sqlBuilder.append(" AND ano_letivo = ?");
            params.add(anoLetivo);
        }
        sqlBuilder.append(" ORDER BY id_materia, ano_letivo, id_nivel;");

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(sqlBuilder.toString());

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                questoes.add(mapResultSetToQuestao(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar questões filtradas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, rs);
        }
        return questoes;
    }


    /**
     * Atualiza os dados de uma questão existente.
     *
     * @param questao O objeto Questao com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarQuestao(Questao questao) {
        boolean atualizado = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (questao == null || questao.getIdQuestao() <= 0 ||
            questao.getEnunciado() == null || questao.getEnunciado().trim().isEmpty() ||
            questao.getIdNivel() <= 0 || questao.getIdMateria() <= 0) {
            System.err.println("Tentativa de atualizar Questao nula, com ID inválido, enunciado vazio ou IDs de nível/matéria inválidos.");
            return false;
        }

        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(ATUALIZAR_QUESTAO_SQL);

            pstmt.setString(1, questao.getEnunciado());
            pstmt.setString(2, questao.getExplicacaoErro());
            pstmt.setInt(3, questao.getAnoLetivo());
            pstmt.setInt(4, questao.getIdNivel());
            pstmt.setInt(5, questao.getIdMateria());
            pstmt.setString(6, questao.getAjuda());
            pstmt.setInt(7, questao.getIdQuestao());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                atualizado = true;
                System.out.println("Questão atualizada com sucesso! ID: " + questao.getIdQuestao());
            } else {
                System.out.println("Nenhuma questão encontrada com o ID: " + questao.getIdQuestao() + " para atualização.");
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar questão: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return atualizado;
    }

    /**
     * Exclui uma questão do banco de dados pelo seu ID.
     * Antes de excluir uma questão, as associações em questao_alternativa devem ser removidas.
     *
     * @param idQuestao O ID da questão a ser excluída.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     */
    public boolean excluirQuestao(int idQuestao) {
        boolean excluido = false;
        Connection conexao = null;
        PreparedStatement pstmt = null;

        if (idQuestao <= 0) {
             System.err.println("Tentativa de excluir Questao com ID inválido.");
            return false;
        }
        
        // É crucial que as associações em questao_alternativa sejam excluídas ANTES.
        // Isso pode ser feito aqui, ou na camada de serviço, ou por ON DELETE CASCADE no DB.
        // Exemplo de chamada ao DAO correspondente (se existir e for apropriado):
        // QuestaoAlternativaDAO qaDAO = new QuestaoAlternativaDAO();
        // qaDAO.excluirAssociacoesPorQuestao(idQuestao);


        try {
            conexao = ConnectionFactory.getConnection();
            pstmt = conexao.prepareStatement(DELETAR_QUESTAO_SQL);
            pstmt.setInt(1, idQuestao);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                excluido = true;
                System.out.println("Questão excluída com sucesso! ID: " + idQuestao);
            } else {
                 System.out.println("Nenhuma questão encontrada com o ID: " + idQuestao + " para exclusão.");
            }
        } catch (SQLException e) {
             // Se houver FKs para esta questão em outras tabelas (além de questao_alternativa que deveria ser tratada antes)
            if (e.getSQLState().startsWith("23")) { 
                 System.err.println("Erro SQL: Não foi possível excluir a Questão ID " + idQuestao + ". Ela pode estar associada a outros registros. " + e.getMessage());
            } else {
                System.err.println("Erro SQL ao excluir questão: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            closeResources(conexao, pstmt, null);
        }
        return excluido;
    }

    /**
     * Mapeia uma linha do ResultSet para um objeto Questao.
     */
    private Questao mapResultSetToQuestao(ResultSet rs) throws SQLException {
        int idQuestao = rs.getInt("id_questao");
        String enunciado = rs.getString("enunciado");
        String explicacaoErro = rs.getString("explicacao_erro");
        int anoLetivo = rs.getInt("ano_letivo");
        int idNivel = rs.getInt("id_nivel");
        int idMateria = rs.getInt("id_materia");
        String ajuda = rs.getString("ajuda");
        return new Questao(idQuestao, enunciado, explicacaoErro, anoLetivo, idNivel, idMateria, ajuda);
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


