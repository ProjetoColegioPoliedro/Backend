package service;

import dao.AlunoDAO;
import model.Aluno;

import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64; // Para encoder/decoder de Base64 (hash)

public class AlunoService {

    private AlunoDAO alunoDAO;

    public AlunoService() {
        this.alunoDAO = new AlunoDAO();
    }

    /**
     * Cadastra um novo aluno no sistema.
     * Realiza validações e faz o hash da senha antes de persistir.
     *
     * @param nome O nome completo do aluno.
     * @param loginAluno O login/nome de usuário do aluno.
     * @param senha Texto puro da senha do aluno.
     * @param anoLetivo O ano letivo do aluno.
     * @return O objeto Aluno cadastrado com o ID gerado, ou null em caso de falha.
     * @throws IllegalArgumentException Se houver erros de validação dos dados.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     * @throws NoSuchAlgorithmException Se o algoritmo de hash não for encontrado (erro interno).
     */
    public Aluno cadastrarAluno(String nome, String loginAluno, String senha, int anoLetivo) throws IllegalArgumentException, SQLException, NoSuchAlgorithmException {
        // 1. Validação de campos obrigatórios
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do aluno é obrigatório.");
        }
        if (loginAluno == null || loginAluno.trim().isEmpty()) {
            throw new IllegalArgumentException("O login do aluno é obrigatório.");
        }
        if (senha == null || senha.isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }
        // anoLetivo pode ser validado como > 0 ou dentro de um range, se necessário.

        // 2. Validação de unicidade do login (nome de usuário)
        if (alunoDAO.buscarAlunoPorLogin(loginAluno) != null) {
            throw new IllegalArgumentException("O login '" + loginAluno + "' já está em uso. Por favor, escolha outro.");
        }

        // 3. Hash da senha (CRUCIAL para segurança!)
        String senhaHash = hashSenha(senha);

        // 4. Criar objeto Aluno com o hash da senha
        Aluno novoAluno = new Aluno(nome, loginAluno, senhaHash, anoLetivo);

        // 5. Adicionar ao banco de dados via DAO
        int idGerado = alunoDAO.inserirAluno(novoAluno); // O DAO agora lança SQLException
        if (idGerado > 0) {
            novoAluno.setIdAluno(idGerado); // Atualiza o ID do objeto
            return novoAluno;
        } else {
            // Isso pode indicar que o DAO não retornou o ID ou que houve falha sem exceção explícita
            throw new SQLException("Falha ao cadastrar aluno: não foi possível obter o ID gerado.");
        }
    }

    /**
     * Realiza o hash de uma senha usando SHA-256 e Base64.
     * !!! ATENÇÃO: Esta é uma implementação SIMPLES para fins de exemplo.
     * !!! Para produção, use bibliotecas robustas como BCrypt (Spring Security) ou Argon2.
     *
     * @param senhaTextoPuro A senha em texto puro.
     * @return A senha hash em formato Base64.
     * @throws NoSuchAlgorithmException Se o algoritmo SHA-256 não for encontrado (erro interno).
     */
    private String hashSenha(String senhaTextoPuro) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(senhaTextoPuro.getBytes());
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}
