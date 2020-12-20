import java.io.*;
import java.net.*;

public class HiloJugador{

    public Socket sc;
    private BufferedReader entrada;
    private PrintWriter salida;
    private char signo;

    public HiloJugador(Socket s, char sig){
        try {
            sc = s;
            // Establece canal de entrada
            entrada = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            // Establece canal de salida
            salida = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(
                sc.getOutputStream())),true);
            
            signo = sig;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void enviarMarca() throws IOException{
        salida.println("Tu marca es "+ signo);
    }

    public void enviarMsg(String msg) throws IOException{
        salida.println(msg);
    }
}