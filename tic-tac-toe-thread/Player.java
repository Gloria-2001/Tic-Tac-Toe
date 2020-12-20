import java.lang.Thread.*;

public class Player extends Thread{

    private Game g;
    private boolean conti = false;

    public Player(String msg, Game g){
        super(msg); // Nombre
        this.g = g;
    }

    @Override
    public void run(){
        while(!conti){
            try {
                conti = g.runGame(getName());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}