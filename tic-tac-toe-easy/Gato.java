import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class Gato{
    public char [][]tablero = {{'-','-','-'},{'-','-','-'},{'-','-','-'}};
    private char []signo = {'X','O'};
    private int jugador = 1;
    private boolean turno = true, juego = true;
    private Scanner teclado = new Scanner(System.in);

    public void impTablero(){
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

    public void tirar(String xy){
        String[] c = xy.split(",");
        verificarTirada(Integer.parseInt(c[0]),Integer.parseInt(c[1]));
    }

    public boolean hayGanador(){
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
            if(hayGanador()){
                System.out.println("El jugador "+jugador+" ha ganado.");
                juego = false;
            }else{
                if(turno) jugador = 2;
                else jugador = 1;
                turno = !turno;
            }
        }
    }

    public void jugar(){
        while(juego){
            System.out.println("Turno del jugador "+jugador);
            this.impTablero();
            System.out.print("Ponga una coordenada para tirar: ");
            String xy = teclado.nextLine();
            if(xy.equals("exit")) System.exit(0);
            this.tirar(xy);
        }
        impTablero();
        System.out.println("Fin del juego");
    }

    public static void main(String[] args) {
        Gato g = new Gato();
        g.jugar();
    }
}