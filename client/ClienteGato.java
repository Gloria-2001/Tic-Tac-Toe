import java.io.*;
import java.net.*;

public class ClienteGato{

    private String host;
    private int port;
    private Socket sc;
    private BufferedReader entrada;
    private PrintWriter salida;

    public ClienteGato(){
        port = 1234;
        host = "localhost";
    }

    public ClienteGato(int p){
        port = p;
        host = "localhost";
    }

    public ClienteGato(String h, int p){
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

    public void correrCliente() throws IOException{
        try {
            // Obtenemos el canal de entrada
            entrada = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            
            // Obtenemos el canal de salida
            salida = new PrintWriter(new BufferedWriter(new 
            OutputStreamWriter(sc.getOutputStream())),true);
        }catch (IOException e){
            System.err.println("No puede establer canales de E/S para la conexión");
            System.exit(0);
        }
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String linea;

        try {
            while(true){
                // Envía a la salida estándar la respuesta del servidor
                linea = entrada.readLine();
                if(linea.equals("exit")) break;
                System.out.println("Respuesta del servidor: " + linea);
            }
        }catch (IOException e){
            System.out.println("IOException: " + e.getMessage());
        }

        // Libera recursos
        salida.close();
        entrada.close();
    }

    public void cerrar() throws IOException{
        sc.close();
    }

    public static void main(String[] args)  throws IOException{
        ClienteGato c = new ClienteGato();
        c.init();
        c.correrCliente();
        c.cerrar();
    }
}