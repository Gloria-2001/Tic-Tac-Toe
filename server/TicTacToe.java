import java.util.concurrent.Semaphore;
import java.util.Scanner;

public class TicTacToe {
    public char [][]board = {{'-','-','-'},{'-','-','-'},{'-','-','-'}};
    private boolean turn = true, endgame = false;
    private Scanner scan = new Scanner(System.in);
    private Semaphore canPlay = new Semaphore(1, true);
    private String winner = "";

    public void printBoard(){
        System.out.print("  ");
        for(int i=0; i<3; i++){
            System.out.print(i+" ");
        }
        System.out.println();
        for(int i=0; i<3; i++){
            System.out.print(i+" ");
            for(int j=0; j<3; j++){
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
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

    public void verificarTirada(int x, int y, String mark){
        if(board[x][y] != '-'){
            System.out.println("Lugar ocupado, intente de nuevo.");
            play(mark);
        }else{
            board[x][y] = mark.charAt(0);
            if(isWinner(mark.charAt(0))){
                printBoard();
                System.out.println("Haz ganado marca "+mark+".");
                winner = mark;
                endgame = true;
            }
        }
    }

    public void play(String mark){
        System.out.println("\nTurno del jugador "+mark);
        printBoard();
        System.out.print("Ponga una coordenada para tirar: ");
        String xy = scan.nextLine();
        if(xy.equals("exit")) System.exit(-1);
        String[] c = xy.split(",");
        verificarTirada(Integer.parseInt(c[0]),Integer.parseInt(c[1]),mark);
    }

    public boolean runGame(PlayerThread p) throws InterruptedException{
        canPlay.acquire();
        try {
            System.out.println("Entro al juego "+p.getName());
            p.sendMsg("exit");
        } catch (Exception e) {
            System.out.println(e);
        }
        // if(!endgame){
        //     play(mark);
        // }else{
        //     System.out.println("El jugador "+winner+" ha ganado.");
        //     printBoard();
        // }
        canPlay.release();
        return true;
    }

}