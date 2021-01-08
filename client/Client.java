import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Client extends JFrame implements ActionListener{

    private String host,name;
    private int port;
    private Socket sc;
    private BufferedReader msgIn;
    private PrintWriter msgOut;
    public Scanner scan = new Scanner(System.in);
    private Container cp;
    private GridLayout gl;
    private Label l00;

    public Client(){
        super("Tic Tac Toe");
        this.setSize(450,450);
        port = 1234;
        host = "localhost";
    }

    public Client(int p){
        super("Tic Tac Toe");
        this.setSize(450,450);
        port = p;
        host = "localhost";
    }

    public Client(String h, int p){
        super("Tic Tac Toe");
        this.setSize(450,450);
        port = p;
        host = h;
    }

    public void init(){
        try {
            sc = new Socket(host,port); //Socket para el cliente
            this.cp = getContentPane();
            this.gl = new GridLayout(3,3);
            gl.setHgap(5);  gl.setVgap(5);
            name = JOptionPane.showInputDialog(null, "Ingrese su nombre", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
            this.initGUI();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public void initGUI(){
        crearBarra();
        crearBotones();
    }

    public void crearBarra(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new Label(name));
        l00 = new Label("Espera tu turno");
        panel.add(l00);
        cp.add(panel,BorderLayout.NORTH);
    }

    public void crearBotones(){
        JPanel panel = new JPanel();
        panel.setLayout(this.gl);
        // this.no.setActionCommand("Cancelar");
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                JButton b = new JButton("");
                b.setActionCommand(i+","+j);
                b.addActionListener(this);
                panel.add(b);
            }
        }
        // for(int i=1; i<=9; i++){
        //     panel.add(new JButton(String.valueOf(i)));
        // }
        cp.add(panel,BorderLayout.CENTER);
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
        this.setVisible(true);

        try {
            while(go){
                linea = msgIn.readLine();
                System.out.println(linea);
                switch (linea) {
                    case "exit":
                        go = false;
                    break;
                    case "name":
                        // Ingresa un nombre
                        msgOut.println(name);
                    break;
                    case "doMark":
                        // Da coordeandas para tirar
                        linea = scan.nextLine();
                        msgOut.println(linea);
                    break;
                    case "w00":
                        JOptionPane.showMessageDialog(null, "Experando a otro jugador", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
                    break;
                    case "play":
                        // this.setVisible(true);
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

    @Override
    public void actionPerformed(ActionEvent e){
        // e.getSource() se compara con la instancia de la clase
        System.out.println(e.getActionCommand());
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