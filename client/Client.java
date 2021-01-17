import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 0,0 | 0,1 | 0,2
-------------------
 1,0 | 1,1 | 1,2
------------------- 
 2,0 | 2,1 | 2,2
 */

public class Client extends JFrame implements ActionListener, Player{

    // ip, nombre_jugador, (X,O), coordenadas
    private String host, name, mark, tiro;
    private int port; // puerto
    private Socket sc;  // Socket cliente/jugador
    private BufferedReader msgIn;   // Canal de entrada
    private PrintWriter msgOut;     // Canal de salida
    private Container cp;   // Contenedor para los elementos de la interfaz
    private GridLayout gl;  // Hacer malla en la interfaz
    private JLabel l00;     // Informar si es turno del jugador
    private boolean turno = false;  // Si es el turno del jugador
    /**
     * Estructura del tablero
     * {
     *  '0,0': JBoton0("0,0"),
     *  '0,1': JBoton1("0,1"),
     * }
     */
    private HashMap<String, JButton> table; // Tablero


    public Client(){
        super("Tic Tac Toe");   // Inicializar herencia JFrame
        this.setSize(450,450);  // Tamanio de la ventana
        port = 1234;
        host = "localhost";     // ip local = 127.0.0.1
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

    public void init(){     // Inicialización del cliente
        try {
            sc = new Socket(host,port); //Socket para el cliente
            this.cp = getContentPane();
            this.gl = new GridLayout(3,3);  // Divisiones de la interfaz
            gl.setHgap(5);  gl.setVgap(5); // Separacion
            table = new HashMap<String,JButton>();
            // Pido el nombre que quiere usar el cliente
            name = JOptionPane.showInputDialog(null, "Ingrese su nombre", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
            initGUI();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public void initGUI(){  // Inicializamos los componentes de la interfaz
        crearBarra();       // Inicializar los Labels que contienen el nombre y el aviso del turno "Nombre es tu turno/ espera tu turno"
        crearBotones();     // Creación  del tablero en la interfaz
    }

    public void crearBarra(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel(name));    // Label nombre
        l00 = new JLabel("espera tu turno");    // Label del turno
        panel.add(l00);
        cp.add(panel,BorderLayout.NORTH);
    }

    public void crearBotones(){
        JPanel panel = new JPanel();
        panel.setLayout(this.gl);   // setLayout() es un metodo para decir como va a estar distribuido la interfaz, panel o contendor.
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                JButton b = new JButton();
                b.setActionCommand(i+","+j); // Guarda la coordenada en el boton
                b.setFont(new Font("Serif", Font.BOLD, 70));    // Modica/edita el tipo de fuente de la letra
                b.addActionListener(this);
                table.put(i+","+j,b);   // Agregamos el boton configurado a nuestro tablero
                panel.add(b);   // Loagregamos al panel
            }
        }
        cp.add(panel,BorderLayout.CENTER);
    }

    @Override
    public void resetGame(){
        try{
            sc = new Socket(host,port); //Socket para el cliente
            // Limpiar el tablero
            for(String key : table.keySet()){
                JButton b = table.get(key); // Obtener el boton el tablero
                b.setText("");  // Voy a limpiarlod
                b.setForeground(Color.black);   // Volverlo a poner de negro
            }
            l00.setText("espera tu turno"); // Espere el turno
            playGame();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void playGame() throws IOException{
        boolean go = true;
        int reset = -1;
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

        String linea;   // Almacena el mensaje recibido del servidor
        // this.setVisible(true);

        try {
            while(go){
                // Lee mensaje recibido del servidor
                linea = msgIn.readLine();
                switch (linea) {
                    case "exit":    // Salida del juego
                        System.out.println("Fin del juego");
                        go = false;
                    break;
                    case "name":    // Nombre del jugador
                        msgOut.println(name); // Mandar nombre
                    break;
                    case "doMark":  // Obtener corrdenadas de la tirada
                        // Jugar mi turno
                        l00.setText("es tu turno"); // Mostrar mi turno
                        turno = true;
                    break;
                    case "w00":     // Espera del otro jugador
                        JOptionPane.showMessageDialog(null, "Esperando a otro jugador, de clic en aceptar y espere", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
                    break;
                    case "wait":    // Espera su turno para tirar
                        l00.setText("espera tu turno"); // Mostrar que tengo esperar mi turno
                    break;
                    case "play":    // Inicio del juego y visualización del tablero
                        setVisible(true);
                        JOptionPane.showMessageDialog(null, "A jugar, "+name+"; te diremos cuando sea tu turno", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
                    break;
                    case "sym":     // Marca del jugador (X / O)
                        this.mark = msgIn.readLine(); // Guardo la marca
                    break;
                    case "last":    // Ultima tirada del jugador contrario
                        lastMark(); // Obtener la tirada del turno anterior 
                    break;
                    case "tie":     // Menciona que es un empate
                        JOptionPane.showMessageDialog(null, "Es un empate", "Tic Tac Toe", JOptionPane.WARNING_MESSAGE);
                    break;
                    case "win":     // Menciona que el jugador ha ganado
                        JOptionPane.showMessageDialog(null, "Felicidades "+name+".\n¡Haz ganado!", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
                    break;
                    case "lose":    // Menciona que el jugador ha perdido
                        linea = msgIn.readLine(); // Nombre del ganador
                        JOptionPane.showMessageDialog(null, "Lo sentimos.\n"+linea+" ha ganado.", "Tic Tac Toe", JOptionPane.ERROR_MESSAGE);
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
        msgOut.close(); // Cerrar canales
        msgIn.close();
        close();
        reset = JOptionPane.showConfirmDialog(null, "¿Deseas jugar de nuevo, "+name+"?", "Tic Tac Toe", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        /**
         * yes = 0
         * no = 1
         */
        if(reset == 0){
            resetGame();
        }
        JOptionPane.showMessageDialog(null, "Fin del juego.\nHasta pronto.", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public boolean ocupado(String btext){
        if(btext.equals("X") || btext.equals("O"))
            return true;
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        // e.getSource() se compara con la instancia de la clase
        if(turno){
            tiro = e.getActionCommand();    // Recibo una coordenada
            JButton b = table.get(tiro);    // Obtener el boton de dicha coordenada
            if(!ocupado(b.getText())){
                b.setText(mark);    // Darle el simbolo del jugador (X,O)
                b.setForeground(Color.red); // Cambiar color
                msgOut.println(tiro);   // Mandar al servidor coordenadas
                turno = false;  // Desocupar el turno
            }else{
                JOptionPane.showMessageDialog(null, "Lugar ocupado, seleccione otro", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "No es su turno, por favor espere", "Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void lastMark(){
        // De inmediato recibo dos mensajes mas
        try {
            String m = msgIn.readLine();    // Marca
            String c = msgIn.readLine();    // Coordeanda
            JButton b = table.get(c);   // Obtengo el boton del a coordena 'c'
            b.setText(m);   // Edito el texto del boton con la marca del contrincante
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void close() throws IOException{ // Cerrar la conexion
        sc.close();
    }

    public static void main(String[] args)  throws IOException{
        Client c;
        c = new Client();
        c.init();
        c.playGame();
    }
}