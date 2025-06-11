package ui; 

@FunctionalInterface
public interface LoginAuthCallback {
    void authenticate(String login, String senha);
}