import java.io.*;
import java.net.*;

public class PlayerThread{

    public Socket sc;
    private BufferedReader msgIn;
    private PrintWriter msgOut;
    private char symbol;

    public PlayerThread(Socket s, char sig){
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

    public void sendSymbol() throws IOException{
        msgOut.println("Tu marca es "+ symbol);
    }

    public void sendMsg(String msg) throws IOException{
        msgOut.println(msg);
    }
}