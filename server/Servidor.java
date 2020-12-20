import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Servidor{
    private int port;
    private ServerSocket ss;
    private Socket sc;
    private BufferedReader entrada;
    private PrintWriter salida;
    private int numJuga = 0;
    private ArrayList<HiloJugador> jugadores;
    private char []signo = {'X','O'};

    public Servidor(){
        port = 1234;
        jugadores = new ArrayList<HiloJugador>();
    }

    public Servidor(int p){
        port = p;
        jugadores = new ArrayList<HiloJugador>();
    }

    public void init(){
        try {
            ss = new ServerSocket(port);//Se crea el socket para el servidor en puerto 1234
            sc = new Socket(); //Socket para el cliente            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public void escuchar() throws IOException{
        System.out.println("Esperando jugadores");
        try {
            while(numJuga < 2){
                // Se bloquea hasta que recibe alguna petición de un cliente
                // abriendo un socket para el cliente
                sc = ss.accept();
                System.out.println("Conexion aceptada: "+ sc);
                jugadores.add(new HiloJugador(sc,signo[numJuga]));
                // Establece canal de salida
                salida = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(
                    sc.getOutputStream())),true);
                if(numJuga < 1) salida.println("Esperando a otro jugador");
                numJuga++;
            }

            for(HiloJugador hj : jugadores){
                hj.enviarMarca();
                hj.enviarMsg("exit");
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
        salida.close();
    }

    public void cerrar() throws IOException{
        sc.close();
        ss.close();
    }

    public static void main(String[] args) throws IOException{
        Servidor s = new Servidor();
        s.init();
        s.escuchar();
        s.cerrar();
    }
}