import java.util.concurrent.Semaphore;
import java.util.Scanner;
import java.io.*;

public class TicTacToe {
    public char [][]board = {{'-','-','-'},{'-','-','-'},{'-','-','-'}};
    private boolean turn = true, endgame = false;
    private Scanner scan = new Scanner(System.in);
    private Semaphore canPlay = new Semaphore(1, true);
    private PlayerThread winner = null;

    public String printBoard(){
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

    public boolean isWinner(char mark){
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

    public void verificarTirada(int x, int y, PlayerThread p) throws IOException{
        if(board[x][y] != '-'){
            p.sendMsg("Lugar ocupado, intente de nuevo.");
            play(p);
        }else{
            board[x][y] = p.getSymbol();
            p.sendMsg(printBoard());
            if(isWinner(p.getSymbol())){
                p.sendMsg("Haz ganado "+p.getName());
                winner = p;
                endgame = true;
                p.sendMsg("exit");
            }
        }
    }

    public void play(PlayerThread p) throws IOException{
        System.out.println("\nTurno de "+p.getName());
        p.sendMsg("\nTurno de "+p.getName());
        p.sendMsg(printBoard());
        p.sendMsg("Ponga una coordenada para tirar: ");
        p.sendMsg("doMark");
        String xy = p.reciveMsg();
        System.out.println("Se ingreso "+xy);
        if(xy.equals("exit")) endgame=true;
        String[] c = xy.split(",");
        verificarTirada(Integer.parseInt(c[0]),Integer.parseInt(c[1]),p);
    }

    public boolean runGame(PlayerThread p) throws InterruptedException{
        try {
            p.sendMsg("Espear tu turno");
            canPlay.acquire();
            if(!endgame){
                play(p);
            }else{
                p.sendMsg("El jugador "+winner.getName()+" ha ganado");
                p.sendMsg(printBoard());
                p.sendMsg("exit");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        canPlay.release();
        return endgame;
    }

}