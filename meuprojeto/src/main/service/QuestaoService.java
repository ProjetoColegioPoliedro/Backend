package service; 

import dao.QuestaoDAO;
import dao.AlternativaDAO;
import dao.QuestaoAlternativaDAO;
import model.Questao;
import model.Alternativa;
import model.QuestaoAlternativa;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections; // Para Collections.shuffle

public class QuestaoService {

    private QuestaoDAO questaoDAO;
    private AlternativaDAO alternativaDAO;
    private QuestaoAlternativaDAO qaDAO;

    public QuestaoService() {
        this.questaoDAO = new QuestaoDAO();
        this.alternativaDAO = new AlternativaDAO();
        this.qaDAO = new QuestaoAlternativaDAO();
    }

    /**
     * Busca uma questão completa pelo seu ID, incluindo todas as suas alternativas
     * e qual delas é a correta.
     * @param idQuestao O ID da questão a ser buscada.
     * @return Um objeto Questao completo, ou null se não encontrada.
     */
    public Questao buscarQuestaoCompletaPorId(int idQuestao) {
        Questao questao = questaoDAO.buscarQuestaoPorId(idQuestao); // Busca o enunciado e metadados
        
        if (questao != null) {
            // Busca as associações (id_alternativa e se é correta) para esta questão
            List<QuestaoAlternativa> qas = qaDAO.buscarAlternativasPorQuestao(idQuestao);
            
            List<Alternativa> alternativasDaQuestao = new ArrayList<>();
            Alternativa alternativaCorreta = null;

            for (QuestaoAlternativa qa : qas) {
                // Para cada associação, busca o objeto Alternativa completo (o texto)
                Alternativa alt = alternativaDAO.buscarAlternativaPorId(qa.getIdAlternativa());
                if (alt != null) {
                    alternativasDaQuestao.add(alt);
                    if (qa.isCorreta()) {
                        alternativaCorreta = alt; // Encontrou a alternativa correta
                    }
                }
            }
            
            // Define as alternativas e a correta na Questao
            questao.setAlternativas(alternativasDaQuestao);
            questao.setAlternativaCorreta(alternativaCorreta);
        }
        return questao;
    }

    /**
     * Busca uma lista de questões completas (com alternativas) por matéria.
     * Pode ser adaptado para buscar por nível, ano, ou de forma aleatória.
     * @param idMateria O ID da matéria.
     * @return Uma lista de objetos Questao completos.
     */
    public List<Questao> buscarQuestoesCompletasPorMateria(int idMateria) {
        List<Questao> questoesBasicas = questaoDAO.listarQuestoesPorMateria(idMateria);
        List<Questao> questoesCompletas = new ArrayList<>();

        for (Questao qBasica : questoesBasicas) {
            Questao questaoCompleta = buscarQuestaoCompletaPorId(qBasica.getIdQuestao());
            if (questaoCompleta != null) {
                questoesCompletas.add(questaoCompleta);
            }
        }
        return questoesCompletas;
    }
    
    /**
     * Retorna uma única questão aleatória de todas as questões disponíveis.
     * Esta é uma forma simples; para jogos mais complexos, considere filtros e histórico.
     * @return Uma Questao aleatória e completa, ou null se não houver questões.
     */
    public Questao buscarQuestaoAleatoria() {
        List<Questao> todasQuestoesBasicas = questaoDAO.listarTodasQuestoes();
        if (todasQuestoesBasicas.isEmpty()) {
            return null;
        }
        // Embaralha a lista e pega a primeira
        Collections.shuffle(todasQuestoesBasicas);
        int idQuestaoAleatoria = todasQuestoesBasicas.get(0).getIdQuestao();
        
        return buscarQuestaoCompletaPorId(idQuestaoAleatoria);
    }
}