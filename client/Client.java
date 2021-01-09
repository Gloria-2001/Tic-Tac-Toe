import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.JOptionPane;

public class Client extends JFrame implements ActionListener{

    private String host,name;
    private int port;
    private Socket sc;
    private BufferedReader msgIn;
    private PrintWriter msgOut;
    private Container cp;
    private GridLayout gl;
    private JLabel l00;
    private String mark,tiro;
    private boolean turno = false;
    private HashMap<String, JButton> table;

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
            table = new HashMap<String,JButton>();
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
        panel.add(new JLabel(name));
        l00 = new JLabel("Espera tu turno");
        panel.add(l00);
        cp.add(panel,BorderLayout.NORTH);
    }

    public void crearBotones(){
        JPanel panel = new JPanel();
        panel.setLayout(this.gl);
        // this.no.setActionCommand("Cancelar");
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                JButton b = new JButton();
                b.setActionCommand(i+","+j);
                b.setFont(new Font("Serif", Font.BOLD, 50));
                b.addActionListener(this);
                table.put(i+","+j,b);
                panel.add(b);
            }
        }
        cp.add(panel,BorderLayout.CENTER);
    }

    public void runClient() throws IOException{
        boolean go = true;
        mark = "";
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
        // this.setVisible(true);

        try {
            while(go){
                linea = msgIn.readLine();
                switch (linea) {
                    case "exit":
                        System.out.println("Adios");
                        go = false;
                    break;
                    case "name":
                        // Ingresa un nombre
                        msgOut.println(name);
                    break;
                    case "doMark":
                        // Da coordeandas para tirar
                        l00.setText("Es tu turno");
                        turno = true;
                    break;
                    case "w00":
                        JOptionPane.showMessageDialog(null, "Esperando a otro jugador, de aceptar y espere", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
                    break;
                    case "wait":
                        l00.setText("Espera tu turno");
                    break;
                    case "play":
                        setVisible(true);
                    break;
                    case "sym":
                        this.mark = msgIn.readLine();
                    break;
                    case "last":
                        lastMark();
                    break;
                    case "tie":
                        JOptionPane.showMessageDialog(null, "Es un empate", "Tic Tac Toe", JOptionPane.WARNING_MESSAGE);
                    break;
                    case "win":
                        JOptionPane.showMessageDialog(null, "Felicidades "+name+".\n¡Haz ganado!", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
                    break;
                    case "lose":
                        linea = msgIn.readLine();
                        JOptionPane.showMessageDialog(null, "Lo sentimos\n"+linea+" ha ganado", "Tic Tac Toe", JOptionPane.WARNING_MESSAGE);
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
        close();
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        // e.getSource() se compara con la instancia de la clase
        if(turno){
            tiro = e.getActionCommand();
            System.out.println(tiro);
            JButton b = table.get(tiro);
            b.setText(mark);
            b.setForeground(Color.red);
            msgOut.println(tiro);
        }else{
            JOptionPane.showMessageDialog(null, "No es su turno, por favor espere", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
        }
        turno = false;
    }

    public void lastMark(){
        try {
            String m = msgIn.readLine();    // Marca
            String c = msgIn.readLine();    // Coordeanda
            JButton b = table.get(c);
            b.setText(m);   
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void close() throws IOException{
        sc.close();
    }

    public static void main(String[] args)  throws IOException{
        Client c = new Client();
        c.init();
        c.runClient();
    }
}