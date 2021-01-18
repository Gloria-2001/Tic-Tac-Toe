import java.util.concurrent.Semaphore;
import java.io.*;

public class TicTacToe {
    public char [][]board = {{'-','-','-'},{'-','-','-'},{'-','-','-'}};
    private boolean endgame = false;    // Indica si el juego acabo
    private Semaphore canPlay = new Semaphore(1, true); // Da paso a los jugadores (turno)
    private PlayerThread winner = null; // Guarda al ganador
    private String lastMark = "", coord = "";   // Guardan marca y coordenada del ultimo tiro
    private boolean firstTime = true, tie = false;  // Indica que si se tira por primera vez y si hay un empate

    public String getWinner(){  // Devuelve el nombre del ganador
        if(winner == null) return "none";
        return winner.getName();
    }

    public String printBoard(){ // Devuelve un String del tablero
        String strBoard = "  ";
        
        for(int i=0; i<3; i++){
           strBoard += String.valueOf(i)+" ";
        }
        strBoard += "\n";
        for(int i=0; i<3; i++){
            strBoard += String.valueOf(i)+" ";
            for(int j=0; j<3; j++){
                strBoard += String.valueOf(board[i][j])+" ";
            }
            strBoard += "\n";
        }

        return strBoard;
    }

    private boolean isWinner(char mark){    // Ve si la marca tiene un tres en linea
        // Devuelve un booleano
        if(board[0][0] == mark && board[1][1] == mark && board[2][2] == mark){
            return true;
        }else if(board[2][0] == mark && board[1][1] == mark && board[0][2] == mark){
            return true;
        }else if(board[0][0] == mark && board[0][1] == mark && board[0][2] == mark){
            return true;
        }else if(board[1][0] == mark && board[1][1] == mark && board[1][2] == mark){
            return true;
        }else if(board[2][0] == mark && board[2][1] == mark && board[2][2] == mark){
            return true;
        }else if(board[0][0] == mark && board[1][0] == mark && board[2][0] == mark){
            return true;
        }else if(board[0][1] == mark && board[1][1] == mark && board[2][1] == mark){
            return true;
        }else if(board[0][2] == mark && board[1][2] == mark && board[2][2] == mark){
            return true;
        }
        else{
            return false;
        }
    }
    
    private boolean isTie(){    // Verifica si hay un empate
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++)
                if(board[i][j]=='-') return false;
        return true;
    }

    public void verificarTirada(int x, int y, PlayerThread p) throws IOException{
        // Verifica que el lugar no tenga una marca de juego (campo vacio)
        if(board[x][y] != '-'){
            p.sendMsg("Lugar ocupado, intente de nuevo.");
            play(p);
        }else{
            board[x][y] = p.getSymbol();    // Pone la marca en el tablero
            lastMark = String.valueOf(p.getSymbol());   // Guarda el simbolo de la ultima tirada
            if(isWinner(p.getSymbol())){    // Si hay un 3 en linea
                System.out.println("Ha ganado "+p.getName());
                p.sendMsg("win");   // Manda al jugador que gano
                winner = p;         // Guarda al ganador
                endgame = true;     // Indica que acabo el juego
                p.sendMsg("exit");  // Le dice al jugador que salga del juego
            }else if(isTie()){      // Si es un empate
                p.sendMsg("tie");   // Manda al jugador que es un empate
                p.sendMsg("exit");  // Le dice al jugador que salga del juego
                endgame = true;     // Indica que acabo el juego
                tie = true;         // Indica que acabo el juego por empate
            }
        }
        System.out.println(printBoard());
    }

    public void play(PlayerThread p) throws IOException{
        System.out.println("\nTurno de "+p.getName());
        p.sendMsg("doMark");    // Manda al jugador que es su turno
        String xy = p.reciveMsg();  // Recibe las coordenadas del jugador
        System.out.println("Se ingreso "+xy);
        if(xy.equals("exit")) endgame=true; // Si es un exit, significa que acabo el juego
        coord = xy; // Guarda las coordenadas
        String[] c = xy.split(","); // Hace un split de los numeros (x,y) de coordenadas
        verificarTirada(Integer.parseInt(c[0]),Integer.parseInt(c[1]),p);   // Verifica la tirada
    }

    public void sendLast(PlayerThread p) throws IOException{
        p.sendMsg("last");      // Manda al jugador la ultima tirada
        p.sendMsg(lastMark);    // Manda la marca
        p.sendMsg(coord);       // Manda las coordenadas
    }

    public boolean runGame(PlayerThread p) throws InterruptedException{
        try {
            p.sendMsg("wait");  // Pide al jugador que espere
            canPlay.acquire();  // El semaforo dara paso cuando se desocupe el turno
            if(!endgame){       // Si el juego no ha acabado entonces
                if(!firstTime){ // Si es la primera vez
                    sendLast(p);// no mandes la ultima tirada
                }
                firstTime = false;  // Marca que ya fue la primera vez
                play(p);        // Juega el jugador
            }else{
                if(!tie){   // Si termino por no empate
                    sendLast(p);    // Manda el tiro
                    p.sendMsg("lose");  // Le dice al jugador que perdio
                    p.sendMsg(winner.getName());    // Manda el nombre del ganador
                    p.sendMsg("exit");  // Manda que salga el usuario
                }else{
                    sendLast(p);    // Manda el ultirmo tiro
                    p.sendMsg("tie");   // Manda que fue un empate
                    p.sendMsg("exit");  // Manda que salga el usuario
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        canPlay.release();  // Libera el recurso
        return endgame;     // Dice que el juego acabo o no
    }

}