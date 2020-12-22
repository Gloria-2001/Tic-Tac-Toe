import java.io.*;
import java.net.*;

public class PlayerThread extends Thread{

    public Socket sc;
    private BufferedReader msgIn;
    private PrintWriter msgOut;
    private char symbol;
    private TicTacToe game;
    private boolean conti = false;

    public PlayerThread(Socket s, char sig, String n){
        super(n);
        try {
            sc = s;
            // Create an in buffer
            msgIn = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            // Create an out buffer
            msgOut = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(
                sc.getOutputStream())),true);
            
            symbol = sig;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void initPlayer(TicTacToe g){
        game = g;
    }

    public void sendSymbol() throws IOException{
        msgOut.println("Tu marca es "+ symbol);
    }

    public void sendMsg(String msg) throws IOException{
        msgOut.println(msg);
    }

    public String reciveMsg() throws IOException{
        return msgIn.readLine();
    }

    public char getSymbol(){
        return symbol;
    }

    @Override
    public void run(){
        while(!conti){
            try {
                conti = game.runGame(this);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}