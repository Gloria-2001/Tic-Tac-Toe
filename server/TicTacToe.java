import java.util.concurrent.Semaphore;
import java.io.*;

public class TicTacToe {
    public char [][]board = {{'-','-','-'},{'-','-','-'},{'-','-','-'}};
    private boolean endgame = false;
    private Semaphore canPlay = new Semaphore(1, true);
    private PlayerThread winner = null;
    private String lastMark = "", coord = "";
    private boolean firstTime = true, tie = false;

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

    private boolean isWinner(char mark){
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
    
    private boolean isTie(){
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++)
                if(board[i][j]=='-') return false;
        return true;
    }

    public void verificarTirada(int x, int y, PlayerThread p) throws IOException{
        if(board[x][y] != '-'){
            p.sendMsg("Lugar ocupado, intente de nuevo.");
            play(p);
        }else{
            board[x][y] = p.getSymbol();
            lastMark = String.valueOf(p.getSymbol());
            if(isWinner(p.getSymbol())){
                System.out.print("Ha ganado"+p.getName());
                p.sendMsg("win");
                winner = p;
                endgame = true;
                p.sendMsg("exit");
            }else if(isTie()){
                p.sendMsg("tie");
                p.sendMsg("exit");
                endgame = true;
                tie = true;
            }
        }
        System.out.println(printBoard());
    }

    public void play(PlayerThread p) throws IOException{
        System.out.println("\nTurno de "+p.getName());
        //p.sendMsg(printBoard());
        p.sendMsg("doMark");
        String xy = p.reciveMsg();
        System.out.println("Se ingreso "+xy);
        if(xy.equals("exit")) endgame=true;
        coord = xy;
        String[] c = xy.split(",");
        verificarTirada(Integer.parseInt(c[0]),Integer.parseInt(c[1]),p);
    }

    public void sendLast(PlayerThread p) throws IOException{
        p.sendMsg("last");
        p.sendMsg(lastMark);
        p.sendMsg(coord);
    }

    public boolean runGame(PlayerThread p) throws InterruptedException{
        try {
            p.sendMsg("wait");
            canPlay.acquire();
            if(!endgame){
                if(!firstTime){
                    sendLast(p);
                }
                firstTime = false;
                play(p);
            }else{
                if(!tie){
                    sendLast(p);
                    p.sendMsg("lose");
                    p.sendMsg(winner.getName());
                    p.sendMsg("exit");
                }else{
                    p.sendMsg("tie");
                    p.sendMsg("exit");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        canPlay.release();
        return endgame;
    }

}