package ui; // Garanta que este seja o pacote correto (diretório 'ui')

@FunctionalInterface
public interface LoginAuthCallback {
    /**
     * Método para ser executado quando a autenticação é solicitada na tela de login.
     * Recebe o login e a senha fornecidos pelo usuário.
     * @param login O login (nome de usuário ou e-mail) fornecido pelo usuário.
     * @param senha A senha fornecida pelo usuário.
     */
    void authenticate(String login, String senha);
}