import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client{

    private String host;
    private int port;
    private Socket sc;
    private BufferedReader msgIn;
    private PrintWriter msgOut;
    public Scanner scan = new Scanner(System.in);

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
        boolean go = true;
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
            while(go){
                linea = msgIn.readLine();
                switch (linea) {
                    case "exit":
                        go = false;
                    break;
                    case "name":
                        String n = scan.nextLine();
                        msgOut.println(n);
                    break;
                    default:
                        // Respuesta del servidor
                        System.out.println(linea);
                    break;
                }                
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