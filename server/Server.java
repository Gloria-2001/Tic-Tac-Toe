import java.io.*;
import java.net.*;
import java.util.*;
import java.time.*;

public class Server extends Document{
    private int port;
    private ServerSocket ss;
    private Socket sc;
    private BufferedReader msgIn;
    private PrintWriter msgOut;
    private int numPlayers = 0;
    private ArrayList<PlayerThread> players;
    private Stack<PlayerThread> playRemove;
    private char []symbol = {'X','O'};
    private String winner;

    public Server(){
        super("registro.txt");
        port = 1234;
        players = new ArrayList<PlayerThread>();
        playRemove = new Stack<PlayerThread>();
    }

    public Server(int p){
        super("registro.txt");
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
        TicTacToe g = new TicTacToe();
        LocalDate day = LocalDate.now(); // Create a date object
        LocalTime startTime = LocalTime.now();
        LocalTime endTime;

        for(PlayerThread hj : players){
            hj.initPlayer(g);
            hj.sendSymbol();
            hj.sendMsg("play");
            hj.start();
        }

        while(true){
            for(PlayerThread hj : players){
                if(!hj.isAlive()){
                    this.winner = hj.getWinner();
                    playRemove.push(hj);
                }
            }
            
            while(!playRemove.empty()){
                players.remove(playRemove.pop());
            }

            if(players.isEmpty()){
                numPlayers = 0;
                System.out.println("Pueden ingresar nuevos jugadores");
                endTime = LocalTime.now();
                break;
            }
        }

        writeFile(day+" - "+startTime+" - "+endTime+" - "+this.winner);
    }

    public void listen() throws IOException{
        System.out.println("Esperando jugadores en el puerto "+port);
        try {
            while(true){
                // Wait a response from the client
                sc = ss.accept();
                System.out.println("Conexion aceptada: "+ sc);
                // Create an out buffer
                msgOut = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(
                    sc.getOutputStream())),true);
                msgIn = new BufferedReader(new InputStreamReader(sc.getInputStream()));
                if(numPlayers >= 2){
                    msgOut.println("Suficientes jugadores en el juego");
                    msgOut.println("exit");
                }else{
                    msgOut.println("name");
                    String name = msgIn.readLine();
                    players.add(new PlayerThread(sc,symbol[numPlayers],name));
                    numPlayers++;
                    if(numPlayers == 1){
                        msgOut.println("w00");
                    }
                    if(numPlayers == 2) initGame();
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
        msgOut.close();
        msgIn.close();
    }

    public void close() throws IOException{
        sc.close();
        ss.close();
    }

    public static void main(String[] args) throws IOException{
        Server s;
        if(args.length == 1){
            s = new Server(Integer.parseInt(args[0]));
        }else{
            s = new Server();
        }
        s.init();
        s.listen();
        s.close();
    }
}