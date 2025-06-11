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
    public void salvar(HistoricoJogo historico) throws SQLException {
        if (historico == null) {
            throw new IllegalArgumentException("O objeto de histórico não pode ser nulo.");
        }
        historicoDAO.inserirHistoricoJogo(historico);
    }
    public List<HistoricoJogo> buscarPorAluno(int idAluno) throws SQLException {
        if (idAluno <= 0) {
            return Collections.emptyList();
        }
        return historicoDAO.listarHistoricosJogoPorAluno(idAluno);
    }
    public List<HistoricoJogo> buscarTodos() throws SQLException {
        return historicoDAO.listarTodosHistoricosJogo();
    }
}