import java.util.concurrent.Semaphore;
import java.util.Scanner;

public class Game {
    public char [][]board = {{'-','-','-'},{'-','-','-'},{'-','-','-'}};
    private char []symbols = {'X','O'};
    private boolean turn = true;
    private Scanner scan = new Scanner(System.in);
    private Semaphore canPlay = new Semaphore(1, true);

    public void printBoard(){
        System.out.print("  ");
        for(int i=0; i<3; i++){
            System.out.print(i+" ");
        }
        System.out.println();
        for(int i=0; i<3; i++){
            System.out.print(i+" ");
            for(int j=0; j<3; j++){
                System.out.print(tablero[i][j]+" ");
            }
            System.out.println();
        }
    }

    public boolean isWinner(){
        if(tablero[0][0] == signo[jugador-1] && tablero[1][1] == signo[jugador-1] && tablero[2][2] == signo[jugador-1]){
            return true;
        }else if(tablero[2][0] == signo[jugador-1] && tablero[1][1] == signo[jugador-1] && tablero[0][2] == signo[jugador-1]){
            return true;
        }else if(tablero[0][0] == signo[jugador-1] && tablero[0][1] == signo[jugador-1] && tablero[0][2] == signo[jugador-1]){
            return true;
        }else if(tablero[1][0] == signo[jugador-1] && tablero[1][1] == signo[jugador-1] && tablero[1][2] == signo[jugador-1]){
            return true;
        }else if(tablero[2][0] == signo[jugador-1] && tablero[2][1] == signo[jugador-1] && tablero[2][2] == signo[jugador-1]){
            return true;
        }else if(tablero[0][0] == signo[jugador-1] && tablero[1][0] == signo[jugador-1] && tablero[2][0] == signo[jugador-1]){
            return true;
        }else if(tablero[0][1] == signo[jugador-1] && tablero[1][1] == signo[jugador-1] && tablero[2][1] == signo[jugador-1]){
            return true;
        }else if(tablero[0][2] == signo[jugador-1] && tablero[1][2] == signo[jugador-1] && tablero[2][2] == signo[jugador-1]){
            return true;
        }
        else{
            return false;
        }
    }

    public void verificarTirada(int x, int y){
        if(tablero[x][y] != '-'){
            System.out.println("Lugar ocupado, intente de nuevo.");
        }else{
            tablero[x][y] = signo[jugador-1];
            if(isWinner()){
                System.out.println("El jugador "+jugador+" ha ganado.");
                juego = false;
            }else{
                if(turno) jugador = 2;
                else jugador = 1;
                turno = !turno;
            }
        }
    }

    public void play(String xy){
        String[] c = xy.split(",");
        verificarTirada(Integer.parseInt(c[0]),Integer.parseInt(c[1]));
    }

    public void runGame(){
        while(juego){
            System.out.println("Turno del jugador "+jugador);
            this.printBoard();
            System.out.print("Ponga una coordenada para tirar: ");
            String xy = teclado.nextLine();
            if(xy.equals("exit")) System.exit(0);
            this.tirar(xy);
        }
        printBoard();
        System.out.println("Fin del juego");
    }

}
