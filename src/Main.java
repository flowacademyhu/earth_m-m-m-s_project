import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        int xSide = 8;
        int ySide = 8;
        char[][] table = createEmptyTable(xSide, ySide);
        drawTable(table);
    }

    public static void drawTable(char[][] table){                   //Számok a sorok és az oszlopok mellé!
        for (int i = 0; i < table.length; i++) {
            System.out.println();
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(table[i][j]);
            }
        }
    }
    public static char[][] createEmptyTable(int xSide, int ySide) {
        char[][] table = new char[xSide][ySide];
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = '_';
            }
        }
        return table;
    }

    public static String leftClick(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Válassz sort!");
        
        int xChoice = sc.nextInt();
        System.out.println("Válassz oszlopot!");
        int yChoice = sc.nextInt();

    }
    
    public static char[][] mines(int xSide, int ySide) {
        char[][] randomArray = new char[xSide][ySide];
        int bomb = 0;
        for (int i = 0; i < randomArray.length; i++) {
        ThreadLocalRandom.current().nextInt();

        }
    }

}