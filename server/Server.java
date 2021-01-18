import java.io.*;
import java.net.*;
import java.util.*;
import java.time.*;
import java.lang.*;

public class Server extends Document{
    private int port;   // Puerto
    private ServerSocket ss;    // Socket servidor
    private Socket sc;          // Socket para clientes
    private BufferedReader msgIn;   // Canal de entrada
    private PrintWriter msgOut;     // Canal de salida
    private int numPlayers = 0;     // Numero de jugadores
    private ArrayList<PlayerThread> players;    // Jugadores conectados
    private Stack<PlayerThread> playRemove;     // Jugadores a borrar
    private char []symbol = {'X','O'};          // Marcas disponibles
    private String winner;          // Nombre del ganador

    public Server(){
        super("registro.txt");
        port = 1234;
        players = new ArrayList<PlayerThread>();
        playRemove = new Stack<PlayerThread>();
    }

    public Server(int p, String nameFile){
        super(nameFile);
        port = p;
        players = new ArrayList<PlayerThread>();
        playRemove = new Stack<PlayerThread>();
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
        TicTacToe g = new TicTacToe();  // Crea un objeto TicTacToe
        LocalDate day = LocalDate.now(); // Crea objeto date
        LocalTime startTime = LocalTime.now();  // Crea objeto de hora de inicio
        LocalTime endTime;  // Crea objeto de hora de fin

        for(PlayerThread hj : players){ // Para todos los jugadores conectados
            hj.initPlayer(g);           // Inicia un jugador
            hj.sendSymbol();            // Manda su marca
            hj.sendMsg("play");         // Indica que puede jugar
            hj.start();                 // Inicia el juego para ambos jugadores
        }

        while(true){
            for(PlayerThread hj : players){ // Para cada jugador
                if(!hj.isAlive()){          // Esta vivo?
                    this.winner = hj.getWinner();   // Obtener ganador
                    playRemove.push(hj);            // Agregar a la pila para removerlos de la lista
                }
            }
            
            while(!playRemove.empty()){
                players.remove(playRemove.pop());   // Remover de la pila y la lista
            }

            if(players.isEmpty()){  // Si ambos usuario se desconectaron
                numPlayers = 0;
                System.out.println("Pueden ingresar nuevos jugadores");
                endTime = LocalTime.now();  // Se pone el valor de la hora
                break;
            }
        }

        writeFile(day+" - "+startTime+" - "+endTime+" - "+this.winner); // Se escribe en el archivo
    }

    public void listen() throws IOException{
        System.out.println("Esperando jugadores en el puerto "+port);
        try {
            while(true){
                // espera una peticion del cliente
                sc = ss.accept();
                System.out.println("Conexion aceptada: "+ sc);
                // Crea un buffer de entrada y salida para el cliente aceptado
                msgOut = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(
                    sc.getOutputStream())),true);
                msgIn = new BufferedReader(new InputStreamReader(sc.getInputStream()));
                if(numPlayers >= 2){    // Si el numero de conexiones es mayor a dos
                    msgOut.println("Suficientes jugadores en el juego");    // Rechaza al cliente entrante
                    msgOut.println("exit");
                }else{
                    msgOut.println("name"); // Pide nombre
                    String name = msgIn.readLine(); // Espera la lectura del nombre
                    players.add(new PlayerThread(sc,symbol[numPlayers],name));  // Crea un jugador
                    numPlayers++;
                    if(numPlayers == 1){    // Si es un jugador, debe esperar al otro
                        msgOut.println("w00");
                    }
                    if(numPlayers == 2) initGame(); // Si son dos, que inicie el juego
                }
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
        msgOut.close();
        msgIn.close();
    }

    public void close() throws IOException{ // Cierra la conexion
        sc.close();
        ss.close();
    }

    public static void main(String[] args) throws IOException{
        Server s;
        int portMain = 1234, data = args.length;
        String outFile = "registro.txt";

        if(args.length > 0){
            for(int i=0; i<data; i++){
                switch (args[i]){
                    case "-p":  // port
                        i++;
                        portMain = Integer.parseInt(args[i]);
                    break;
                    case "-o":  // output
                        i++;
                        outFile = args[i];
                    break;
                }
            }
            s = new Server(portMain,outFile);
        }else{
            s = new Server();
        }
        
        s.init();
        s.listen();
        s.close();
    }
}