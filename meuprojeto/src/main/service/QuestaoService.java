package service;

import dao.QuestaoDAO;
import dao.AlternativaDAO;
import dao.QuestaoAlternativaDAO;
import model.Questao;
import model.Alternativa;
import model.QuestaoAlternativa;

import java.sql.SQLException;
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

    public List<Questao> buscarQuestoesPorTemas(List<String> nomesDosTemas, int quantidadeDesejada) throws SQLException {
        List<Questao> questoesCandidatas = new ArrayList<>();
        System.out.println("DEBUG (QuestaoService): Buscando questões para os temas: " + nomesDosTemas);

        if (nomesDosTemas == null || nomesDosTemas.isEmpty()) {
            return questoesCandidatas; // Retorna lista vazia se nenhum tema for fornecido
        }

        // Mapeia o nome do tema para o ID da matéria no banco de dados.
        // ATENÇÃO: Verifique se estes IDs (1, 2, 3...) correspondem aos IDs na sua tabela de Matérias!
        for (String nomeTema : nomesDosTemas) {
            int idMateria = -1;
            switch (nomeTema) {
                case "Português":           idMateria = 1; break;
                case "Matemática":          idMateria = 2; break;
                case "Inglês":              idMateria = 3; break;
                case "Ciências da Natureza":idMateria = 4; break;
                case "Ciências Humanas":    idMateria = 5; break;
            }

            if (idMateria != -1) {
                // Busca todas as questões daquela matéria e adiciona à lista de candidatas
                List<Questao> questoesDaMateria = questaoDAO.listarQuestoesPorMateria(idMateria);
                questoesCandidatas.addAll(questoesDaMateria);
            }
        }

        // Embaralha todas as questões encontradas para garantir aleatoriedade
        Collections.shuffle(questoesCandidatas);

        // Seleciona o número desejado de questões e popula com suas alternativas
        List<Questao> questoesCompletasParaPartida = new ArrayList<>();
        int numeroDeQuestoesParaPegar = Math.min(quantidadeDesejada, questoesCandidatas.size());

        for (int i = 0; i < numeroDeQuestoesParaPegar; i++) {
            Questao questaoBasica = questoesCandidatas.get(i);
            // Reutiliza seu método para buscar os detalhes (alternativas) de cada questão
            Questao questaoCompleta = buscarQuestaoCompletaPorId(questaoBasica.getIdQuestao());
            if (questaoCompleta != null) {
                questoesCompletasParaPartida.add(questaoCompleta);
            }
        }

        System.out.println("DEBUG (QuestaoService): Total de questões COMPLETAS para a partida: " + questoesCompletasParaPartida.size());
        return questoesCompletasParaPartida;
    }


    public List<Questao> buscarQuestoesAleatorias(int quantidadePartida) throws SQLException {
        List<Questao> questoesBasicasColetadas = new ArrayList<>();
        System.out.println("DEBUG (QuestaoService): Iniciando busca de questões aleatórias para partida.");

        // 1. Busca e adiciona 5 questões de NÍVEL 1
        List<Questao> questoesNivel1 = questaoDAO.listarQuestoesPorNivel(1);
        Collections.shuffle(questoesNivel1);
        int qtdNivel1 = Math.min(5, questoesNivel1.size());
        questoesBasicasColetadas.addAll(questoesNivel1.subList(0, qtdNivel1));

        // 2. Busca e adiciona 4 questões de NÍVEL 2
        List<Questao> questoesNivel2 = questaoDAO.listarQuestoesPorNivel(2);
        Collections.shuffle(questoesNivel2);
        int qtdNivel2 = Math.min(4, questoesNivel2.size());
        questoesBasicasColetadas.addAll(questoesNivel2.subList(0, qtdNivel2));

        // 3. Busca e adiciona 4 questões de NÍVEL 3
        List<Questao> questoesNivel3 = questaoDAO.listarQuestoesPorNivel(3);
        Collections.shuffle(questoesNivel3);
        int qtdNivel3 = Math.min(4, questoesNivel3.size());
        questoesBasicasColetadas.addAll(questoesNivel3.subList(0, qtdNivel3));

        List<Questao> questoesCompletasParaPartida = new ArrayList<>();
        if (questoesBasicasColetadas.isEmpty()) {
            return questoesCompletasParaPartida;
        }

        for (Questao qBasica : questoesBasicasColetadas) {
            Questao questaoCompleta = buscarQuestaoCompletaPorId(qBasica.getIdQuestao());
            if (questaoCompleta != null) {
                questoesCompletasParaPartida.add(questaoCompleta);
            }
        }
        return questoesCompletasParaPartida;
    }

   
    public Questao buscarQuestaoCompletaPorId(int idQuestao) throws SQLException {
        Questao questao = questaoDAO.buscarQuestaoPorId(idQuestao);

        if (questao != null) {
            List<QuestaoAlternativa> qas = qaDAO.buscarAlternativasPorQuestao(idQuestao);
            List<Alternativa> alternativasDaQuestao = new ArrayList<>();
            Alternativa alternativaCorreta = null;

            if (qas != null) {
                for (QuestaoAlternativa qa : qas) {
                    Alternativa alt = alternativaDAO.buscarAlternativaPorId(qa.getIdAlternativa());
                    if (alt != null) {
                        alt.setCorreta(qa.isCorreta());
                        alternativasDaQuestao.add(alt);
                        if (qa.isCorreta()) {
                            alternativaCorreta = alt;
                        }
                    }
                }
            }
            questao.setAlternativas(alternativasDaQuestao);
            questao.setAlternativaCorreta(alternativaCorreta);
        }
        return questao;
    }

   
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

   
    public Questao buscarQuestaoAleatoria() throws SQLException {
        List<Questao> todasQuestoesBasicas = questaoDAO.listarTodasQuestoes();
        if (todasQuestoesBasicas.isEmpty()) {
            return null;
        }
        Collections.shuffle(todasQuestoesBasicas);
        int idQuestaoAleatoria = todasQuestoesBasicas.get(0).getIdQuestao();
        return buscarQuestaoCompletaPorId(idQuestaoAleatoria);
    }

   
    public void adicionarQuestao(Questao questao) throws Exception {
        int idQuestaoGerado = questaoDAO.adicionarQuestao(questao);
        questao.setIdQuestao(idQuestaoGerado);

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