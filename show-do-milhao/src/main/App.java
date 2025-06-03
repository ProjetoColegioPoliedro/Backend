// ponto de entrada. quando executar o projeto, é esse arquivo que o Java procura para começar a rodar o código. é onde instancia as classes e controla o fluxo do jogo.
import controller.Navegador;

public class App{
    public static void main(String[] args) {
        var nav = new Navegador();
        nav.start();
    }
}