// Crie este arquivo em: service/HistoricoService.java
package service;

import dao.HistoricoJogoDAO;
import model.HistoricoJogo;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class HistoricoService {

    private HistoricoJogoDAO historicoDAO;

    public HistoricoService() {
        this.historicoDAO = new HistoricoJogoDAO();
    }

    /**
     * Salva um novo registro de histórico de jogo.
     * @param historico O objeto HistoricoJogo a ser salvo.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     */
    public void salvar(HistoricoJogo historico) throws SQLException {
        if (historico == null) {
            throw new IllegalArgumentException("O objeto de histórico não pode ser nulo.");
        }
        historicoDAO.inserirHistoricoJogo(historico);
    }

    /**
     * Busca todos os registros de histórico de um aluno específico.
     * @param idAluno O ID do aluno.
     * @return Uma lista de HistoricoJogo.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     */
    public List<HistoricoJogo> buscarPorAluno(int idAluno) throws SQLException {
        if (idAluno <= 0) {
            return Collections.emptyList();
        }
        return historicoDAO.listarHistoricosJogoPorAluno(idAluno);
    }
    
    /**
     * Busca todos os registros de histórico de todos os alunos.
     * @return Uma lista com todos os HistoricoJogo.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     */
    public List<HistoricoJogo> buscarTodos() throws SQLException {
        return historicoDAO.listarTodosHistoricosJogo();
    }
}