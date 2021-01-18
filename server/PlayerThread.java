import java.io.*;
import java.net.*;

public class PlayerThread extends Thread{

    public Socket sc;               // Socket del jugador
    private BufferedReader msgIn;   // Canal de entrada 
    private PrintWriter msgOut;     // Canal de salida
    private char symbol;            // Marca del jugador
    private TicTacToe game;         // Juego
    private boolean conti = false;  // Continua jugando
    private String winner;          // Ganador

    public PlayerThread(Socket s, char sig, String n){
        super(n);
        try {
            sc = s;
            // Crea buffer de entrada
            msgIn = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            // Crea buffer de salida
            msgOut = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(
                sc.getOutputStream())),true);
            
            symbol = sig;   // Se le da un simbolo
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void initPlayer(TicTacToe g){    // Inicia el juego
        game = g;
    }

    public void sendSymbol() throws IOException{
        msgOut.println("sym");                  // Manda un msg 'sym' indicando que el cliente 
        msgOut.println(String.valueOf(symbol)); // recibe su marca
    }

    public void sendMsg(String msg) throws IOException{
        msgOut.println(msg);    // Manda un msg string
    }

    public String reciveMsg() throws IOException{
        String msg = msgIn.readLine();  // Recibe un msg String del cliente
        return msg;
    }

    public char getSymbol(){    // Devuelve la marca del jugador
        return symbol;
    }

    public String getWinner(){  // Devuelve el ganador
        return winner;
    }

    @Override
    public void run(){
        while(!conti){  // Hasta que no podamos continuar
            try {
                conti = game.runGame(this); // Corre el juego
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        if(!game.getWinner().equals("none")){   // Si es diferente de 'none'
            winner = game.getWinner();  // El ganador tiene un nombre
        }else{
            winner = "Empate";  // Es un empate
        }
    }
}