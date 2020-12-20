public class Main{
    public static void main(String[] args) {
        Game g = new Game();
        Player us00 = new Player("X", g);
        Player us01 = new Player("O", g);

        us00.start();
        us01.start();
    }
}