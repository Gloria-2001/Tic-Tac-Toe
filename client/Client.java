import java.io.*;
import java.net.*;

public class Client{

    private String host;
    private int port;
    private Socket sc;
    private BufferedReader msgIn;
    private PrintWriter msgOut;

    public Client(){
        port = 1234;
        host = "localhost";
    }

    public Client(int p){
        port = p;
        host = "localhost";
    }

    public Client(String h, int p){
        host = h;
        port = p;
    }

    public void init(){
        try {
            sc = new Socket(host,port); //Socket para el cliente
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public void runClient() throws IOException{
        try {
            // Obtenemos el canal de msgIn
            msgIn = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            
            // Obtenemos el canal de msgOut
            msgOut = new PrintWriter(new BufferedWriter(new 
            OutputStreamWriter(sc.getOutputStream())),true);
        }catch (IOException e){
            System.err.println("No puede establer canales de E/S para la conexión");
            System.exit(0);
        }

        String linea;

        try {
            while(true){
                // Envía a la msgOut estándar la respuesta del servidor
                linea = msgIn.readLine();
                if(linea.equals("exit")) break;
                System.out.println("Respuesta del servidor: " + linea);
            }
        }catch (IOException e){
            System.out.println("IOException: " + e.getMessage());
        }

        // Libera recursos
        msgOut.close();
        msgIn.close();
    }

    public void close() throws IOException{
        sc.close();
    }

    public static void main(String[] args)  throws IOException{
        Client c = new Client();
        c.init();
        c.runClient();
        c.close();
    }
}