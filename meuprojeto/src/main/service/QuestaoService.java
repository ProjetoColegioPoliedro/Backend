package service; 

import dao.QuestaoDAO;
import dao.AlternativaDAO;
import dao.QuestaoAlternativaDAO;
import model.Questao;
import model.Alternativa;
import model.QuestaoAlternativa;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections; 

public class QuestaoService {

    /**
     * Busca uma lista de questões aleatórias para formar uma partida completa.
     * @param quantidade O número de questões desejadas para a partida.
     * @return Uma lista de objetos Questao completos e aleatórios.
     */
    public List<Questao> buscarQuestoesParaPartida() {
        List<Questao> questoesDaPartidaFinal = new ArrayList<>();

        // 1. Busca e adiciona 5 questões de NÍVEL 1
        System.out.println("Buscando questões de nível 1...");
        List<Questao> questoesNivel1 = questaoDAO.listarQuestoesPorNivel(1);
        Collections.shuffle(questoesNivel1); // Embaralha para pegar aleatórias
        int qtdNivel1 = Math.min(5, questoesNivel1.size()); // Pega 5 ou o máximo que tiver
        questoesDaPartidaFinal.addAll(questoesNivel1.subList(0, qtdNivel1));
        System.out.println("Adicionadas " + qtdNivel1 + " questões de nível 1.");

        // 2. Busca e adiciona 4 questões de NÍVEL 2
        System.out.println("Buscando questões de nível 2...");
        List<Questao> questoesNivel2 = questaoDAO.listarQuestoesPorNivel(2);
        Collections.shuffle(questoesNivel2);
        int qtdNivel2 = Math.min(4, questoesNivel2.size());
        questoesDaPartidaFinal.addAll(questoesNivel2.subList(0, qtdNivel2));
        System.out.println("Adicionadas " + qtdNivel2 + " questões de nível 2.");

        // 3. Busca e adiciona 4 questões de NÍVEL 3
        System.out.println("Buscando questões de nível 3...");
        List<Questao> questoesNivel3 = questaoDAO.listarQuestoesPorNivel(3);
        Collections.shuffle(questoesNivel3);
        int qtdNivel3 = Math.min(4, questoesNivel3.size());
        questoesDaPartidaFinal.addAll(questoesNivel3.subList(0, qtdNivel3));
        System.out.println("Adicionadas " + qtdNivel3 + " questões de nível 3.");

        // 4. Embaralha a lista final para misturar os níveis durante o jogo
        Collections.shuffle(questoesDaPartidaFinal);

        System.out.println("Total de questões para a partida: " + questoesDaPartidaFinal.size());
        return questoesDaPartidaFinal;
    }

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
                // Para cada associação, busca o objeto Alternativa completo (o texto).
                // O construtor da Alternativa com (id, texto) assume 'correta=false'.
                Alternativa alt = alternativaDAO.buscarAlternativaPorId(qa.getIdAlternativa());
                if (alt != null) {
                    // Atualiza a flag 'correta' da alternativa com a informação da associação.
                    alt.setCorreta(qa.isCorreta()); // Importante para que a alternativa saiba se é a correta.
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

    /**
     * Adiciona uma nova questão ao banco de dados, incluindo suas alternativas.
     * Este método coordena o salvamento da Questao, Alternativas e suas associações.
     * @param questao A Questao completa a ser adicionada.
     * @throws Exception Se ocorrer um erro durante o processo de persistência (propagado dos DAOs).
     */
    public void adicionarQuestao(Questao questao) throws Exception { // <-- ESTE MÉTODO É O QUE ESTAVA FALTANDO!
        // 1. Salva a Questao (enunciado, dificuldade, matéria, etc.) e obtém o ID gerado.
        // O QuestaoDAO.adicionarQuestao deve retornar o ID gerado pelo banco.
        int idQuestaoGerado = questaoDAO.adicionarQuestao(questao);
        questao.setIdQuestao(idQuestaoGerado); // Atualiza o ID da questão no objeto

        // 2. Salva cada Alternativa e cria a associação QuestaoAlternativa
        // Certifique-se de que a lista de alternativas da Questao NÃO é nula.
        if (questao.getAlternativas() != null) {
            for (Alternativa alt : questao.getAlternativas()) {
                // Salva a Alternativa e obtém o ID gerado.
                int idAlternativaGerado = alternativaDAO.adicionarAlternativa(alt);
                alt.setIdAlternativa(idAlternativaGerado); // Atualiza o ID da alternativa no objeto

                // 3. Cria e salva a associação QuestaoAlternativa (ligando questão, alternativa e se é correta).
                QuestaoAlternativa qa = new QuestaoAlternativa(
                    0, // O ID da associação (idQa) será gerado pelo banco, então pode ser 0 aqui para um novo registro.
                    questao.getIdQuestao(),
                    alt.getIdAlternativa(),
                    alt.isCorreta() // Usa o novo atributo 'correta' da Alternativa
                );
                qaDAO.adicionarQuestaoAlternativa(qa);
            }
        } else {
            // Lançar uma exceção ou logar um erro se a questão não tiver alternativas.
            throw new IllegalArgumentException("A questão não possui alternativas para serem salvas.");
        }
    }
}