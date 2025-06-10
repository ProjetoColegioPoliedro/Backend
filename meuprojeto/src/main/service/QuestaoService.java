package service; 

import dao.QuestaoDAO;
import dao.AlternativaDAO;
import dao.QuestaoAlternativaDAO;
import model.Questao;
import model.Alternativa;
import model.QuestaoAlternativa;

import java.sql.SQLException; // Importado para tratamento de exceções
import java.util.ArrayList;
import java.util.List;
import java.util.Collections; 

public class QuestaoService {

    // --- DAOs DECLARADOS NO INÍCIO ---
    private QuestaoDAO questaoDAO;
    private AlternativaDAO alternativaDAO;
    private QuestaoAlternativaDAO qaDAO;

    // Construtor do serviço: inicializa os DAOs
    public QuestaoService() {
        this.questaoDAO = new QuestaoDAO();
        this.alternativaDAO = new AlternativaDAO();
        this.qaDAO = new QuestaoAlternativaDAO();
    }

    /**
     * Busca uma lista de questões aleatórias para formar uma partida completa,
     * baseada em níveis de dificuldade (5 fácil, 4 médio, 4 difícil).
     *
     * @param quantidadePartida O número total de questões desejadas para a partida (será o MAX_QUESTOES do Navegador).
     * A lógica interna ainda pega 5-4-4, mas o parâmetro é para compatibilidade e futura expansão.
     * @return Uma lista de objetos Questao completos e aleatórios.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados durante a busca.
     */
    public List<Questao> buscarQuestoesAleatorias(int quantidadePartida) throws SQLException {
        List<Questao> questoesBasicasColetadas = new ArrayList<>(); // Coleta as questões básicas de todos os níveis

        System.out.println("DEBUG (QuestaoService): Iniciando busca de questões aleatórias para partida.");
        
        // 1. Busca e adiciona 5 questões de NÍVEL 1
        System.out.println("DEBUG (QuestaoService): Buscando questões de nível 1...");
        List<Questao> questoesNivel1 = questaoDAO.listarQuestoesPorNivel(1);
        Collections.shuffle(questoesNivel1);
        int qtdNivel1 = Math.min(5, questoesNivel1.size());
        questoesBasicasColetadas.addAll(questoesNivel1.subList(0, qtdNivel1));
        System.out.println("DEBUG (QuestaoService): Adicionadas " + qtdNivel1 + " questões básicas de nível 1.");

        // 2. Busca e adiciona 4 questões de NÍVEL 2
        System.out.println("DEBUG (QuestaoService): Buscando questões de nível 2...");
        List<Questao> questoesNivel2 = questaoDAO.listarQuestoesPorNivel(2);
        Collections.shuffle(questoesNivel2);
        int qtdNivel2 = Math.min(4, questoesNivel2.size());
        questoesBasicasColetadas.addAll(questoesNivel2.subList(0, qtdNivel2));
        System.out.println("DEBUG (QuestaoService): Adicionadas " + qtdNivel2 + " questões básicas de nível 2.");

        // 3. Busca e adiciona 4 questões de NÍVEL 3
        System.out.println("DEBUG (QuestaoService): Buscando questões de nível 3...");
        List<Questao> questoesNivel3 = questaoDAO.listarQuestoesPorNivel(3);
        Collections.shuffle(questoesNivel3);
        int qtdNivel3 = Math.min(4, questoesNivel3.size());
        questoesBasicasColetadas.addAll(questoesNivel3.subList(0, qtdNivel3));
        System.out.println("DEBUG (QuestaoService): Adicionadas " + qtdNivel3 + " questões básicas de nível 3.");

        System.out.println("DEBUG (QuestaoService): Total de questões básicas encontradas: " + questoesBasicasColetadas.size());

        // --- NOVO: POPULAR AS ALTERNATIVAS PARA CADA QUESTÃO BÁSICA ---
        List<Questao> questoesCompletasParaPartida = new ArrayList<>();
        if (questoesBasicasColetadas.isEmpty()) {
            System.out.println("DEBUG (QuestaoService): Nenhuma questão básica encontrada para completar.");
            return questoesCompletasParaPartida; // Retorna lista vazia
        }

        for (Questao qBasica : questoesBasicasColetadas) {
            // Chama buscarQuestaoCompletaPorId para cada questão básica
            Questao questaoCompleta = buscarQuestaoCompletaPorId(qBasica.getIdQuestao());
            if (questaoCompleta != null) {
                questoesCompletasParaPartida.add(questaoCompleta);
            } else {
                System.out.println("DEBUG (QuestaoService): Aviso: Questão ID " + qBasica.getIdQuestao() + " não pôde ser completada (alternativas não encontradas).");
            }
        }
        

        

        System.out.println("DEBUG (QuestaoService): Total de questões COMPLETAS para a partida: " + questoesCompletasParaPartida.size());
        return questoesCompletasParaPartida;
    }

    /**
     * Busca uma questão completa pelo seu ID, incluindo todas as suas alternativas
     * e qual delas é a correta.
     * @param idQuestao O ID da questão a ser buscada.
     * @return Um objeto Questao completo, ou null se não encontrada.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public Questao buscarQuestaoCompletaPorId(int idQuestao) throws SQLException {
        System.out.println("DEBUG (QuestaoService): Buscando detalhes para Questão ID: " + idQuestao);
        Questao questao = questaoDAO.buscarQuestaoPorId(idQuestao); // Pode lançar SQLException
        
        if (questao != null) {
            System.out.println("DEBUG (QuestaoService): Questão ID " + idQuestao + " encontrada. Enunciado: " + questao.getEnunciado());
            List<QuestaoAlternativa> qas = qaDAO.buscarAlternativasPorQuestao(idQuestao); // Pode lançar SQLException
            System.out.println("DEBUG (QuestaoService):    Associações QA encontradas para ID " + idQuestao + ": " + (qas != null ? qas.size() : 0));
            
            List<Alternativa> alternativasDaQuestao = new ArrayList<>();
            Alternativa alternativaCorreta = null;

            if (qas != null) { // Garante que qas não é nulo antes de iterar
                for (QuestaoAlternativa qa : qas) {
                    Alternativa alt = alternativaDAO.buscarAlternativaPorId(qa.getIdAlternativa()); // Pode lançar SQLException
                    if (alt != null) {
                        System.out.println("DEBUG (QuestaoService):        Alternativa ID " + alt.getIdAlternativa() + " Texto: '" + alt.getTexto() + "' (Correta na QA: " + qa.isCorreta() + ")");
                        alt.setCorreta(qa.isCorreta()); // Atualiza a flag 'correta' da alternativa
                        alternativasDaQuestao.add(alt);
                        if (qa.isCorreta()) {
                            alternativaCorreta = alt;
                        }
                    } else {
                        System.out.println("DEBUG (QuestaoService):        Alternativa ID " + qa.getIdAlternativa() + " não encontrada no AlternativaDAO!");
                    }
                }
            }
            
            questao.setAlternativas(alternativasDaQuestao);
            questao.setAlternativaCorreta(alternativaCorreta);
            System.out.println("DEBUG (QuestaoService):    Total de alternativas populadas para Questão ID " + idQuestao + ": " + alternativasDaQuestao.size());
        } else {
            System.out.println("DEBUG (QuestaoService): Questão ID " + idQuestao + " NÃO encontrada no QuestaoDAO.");
        }
        return questao;
    }

    /**
     * Busca uma lista de questões completas (com alternativas) por matéria.
     * @param idMateria O ID da matéria.
     * @return Uma lista de objetos Questao completos.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public List<Questao> buscarQuestoesCompletasPorMateria(int idMateria) throws SQLException {
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
     * @return Uma Questao aleatória e completa, ou null se não houver questões.
     * @throws SQLException Se ocorrer um erro no acesso ao banco de dados.
     */
    public Questao buscarQuestaoAleatoria() throws SQLException { // Este método pode ser removido se não for mais usado diretamente.
        List<Questao> todasQuestoesBasicas = questaoDAO.listarTodasQuestoes();
        if (todasQuestoesBasicas.isEmpty()) {
            return null;
        }
        Collections.shuffle(todasQuestoesBasicas);
        int idQuestaoAleatoria = todasQuestoesBasicas.get(0).getIdQuestao();
        
        return buscarQuestaoCompletaPorId(idQuestaoAleatoria);
    }

    /**
     * Adiciona uma nova questão ao banco de dados, incluindo suas alternativas.
     * @param questao A Questao completa a ser adicionada.
     * @throws Exception Se ocorrer um erro durante o processo de persistência (propagado dos DAOs).
     */
    public void adicionarQuestao(Questao questao) throws Exception {
        // 1. Salva a Questao (enunciado, dificuldade, matéria, etc.) e obtém o ID gerado.
        int idQuestaoGerado = questaoDAO.adicionarQuestao(questao);
        questao.setIdQuestao(idQuestaoGerado);

        // 2. Salva cada Alternativa e cria a associação QuestaoAlternativa
        if (questao.getAlternativas() != null) {
            for (Alternativa alt : questao.getAlternativas()) {
                int idAlternativaGerado = alternativaDAO.adicionarAlternativa(alt);
                alt.setIdAlternativa(idAlternativaGerado);

                QuestaoAlternativa qa = new QuestaoAlternativa(
                    0, 
                    questao.getIdQuestao(),
                    alt.getIdAlternativa(),
                    alt.isCorreta()
                );
                qaDAO.adicionarQuestaoAlternativa(qa);
            }
        } else {
            throw new IllegalArgumentException("A questão não possui alternativas para serem salvas.");
        }
    }
}
