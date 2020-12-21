import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server{
    private int port;
    private ServerSocket ss;
    private Socket sc;
    private BufferedReader msgIn;
    private PrintWriter msgOut;
    private int numPlayers = 0;
    private ArrayList<PlayerThread> players;
    private char []symbol = {'X','O'};

    public Server(){
        port = 1234;
        players = new ArrayList<PlayerThread>();
    }

    public Server(int p){
        port = p;
        players = new ArrayList<PlayerThread>();
    }

    public void init(){
        try {
            ss = new ServerSocket(port); // Create a server socket in port 1234
            sc = new Socket(); // Create a cliente socket to request
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public void initGame() throws IOException{
        for(PlayerThread hj : players){
            hj.sendSymbol();
            hj.sendMsg("exit");
        }
    }

    public void listen() throws IOException{
        System.out.println("Esperando jugadores");
        try {
            while(true){
                // Wait a response from the client
                sc = ss.accept();
                System.out.println("Conexion aceptada: "+ sc);
                // Create an out buffer
                msgOut = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(
                    sc.getOutputStream())),true);
                if(numPlayers >= 2){
                    msgOut.println("Suficientes jugadores en el juego");
                    msgOut.println("exit");
                }else{
                    players.add(new PlayerThread(sc,symbol[numPlayers]));
                    numPlayers++;
                    if(numPlayers == 1) msgOut.println("Esperando a otro jugador");
                    if(numPlayers == 2) initGame();
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
        msgOut.close();
    }

    public void close() throws IOException{
        sc.close();
        ss.close();
    }

    public static void main(String[] args) throws IOException{
        Server s = new Server();
        s.init();
        s.listen();
        s.close();
    }
}