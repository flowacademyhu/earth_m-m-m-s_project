import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;



/* Arrays.fill() method??
Alternatíva a kezdeti feltöltésre?

 */
public class Main {
    public static void main(String[] args) {
        int xSide = 10;                      //x dimension of the table (megnövelt érték!)
        int ySide = 10;                      //y dimension of the table
        char[][] table = createEmptyTable(xSide, ySide);
        drawTable(table);
        System.out.println();
        drawTable(hiddenTable(xSide, ySide));        //amíg készül a kód, megjelenik a hiddenTable
        System.out.println();
//        leftClick();


    }

    public static int[][] hiddenTable(int xSide, int ySide) {
        int[][] hiddenTable = createNullTable(xSide, ySide);
        int mineNumber = 10;                                       //predefined
        int mineCreated = 0;
        while (mineCreated < mineNumber) {
            int randX = ThreadLocalRandom.current().nextInt(1, xSide - 1);      //creating random coordinates
            int randY = ThreadLocalRandom.current().nextInt(1, ySide - 1);      //creating random coordinates
            if (hiddenTable[randX][randY] == 9) {
                continue;
            } else {
                hiddenTable[randX][randY] = 9;         //-1 = *
                mineCreated++;                         //jobb átlátás/ellenőrzés miatt a "9"-es a "*"
            }
        }
        //Eddig legenerálódnak a csillagok

        // számértékek megjelenítése
        for (int i = 1; i < hiddenTable.length - 1; i++) {      //1-es csökkentett határok
            for (int j = 1; j < hiddenTable[i].length - 1; j++) {

                if (hiddenTable[i][j] == 9) {                   //jobb átlátás/ellenőrzés miatt a "9"-es a "*"
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            if (hiddenTable[k][l] == 9) {
                                continue;
                            } else {
                                hiddenTable[k][l]++;
                            }
                        }

                    }
                }


//                if (hiddenTable[i][j] == -1) {
//                    hiddenTable[i + 1][j + 1] = (hiddenTable[i + 1][j + 1] == -1 ? -1 : hiddenTable[i + 1][j + 1]++);    //jobbra alatta
//                    hiddenTable[i + 1][j] = (hiddenTable[i + 1][j] == -1 ? -1 : hiddenTable[i + 1][j]++);                // alatta
//                    hiddenTable[i + 1][j - 1] = (hiddenTable[i + 1][j - 1] == -1 ? -1 : hiddenTable[i + 1][j - 1]++);    // balra alatta
//                    hiddenTable[i][j - 1] = (hiddenTable[i][j - 1] == -1 ? -1 : hiddenTable[i][j - 1]++);                // balra
//                    hiddenTable[i - 1][j - 1] = (hiddenTable[i - 1][j - 1] == -1 ? -1 : hiddenTable[i - 1][j - 1]++);    // balra felette
//                    hiddenTable[i - 1][j] = (hiddenTable[i - 1][j] == -1 ? -1 : hiddenTable[i - 1][j]++);                //jobbra felette
//                    hiddenTable[i - 1][j + 1] = (hiddenTable[i - 1][j + 1] == -1 ? -1 : hiddenTable[i - 1][j + 1]++);    //jobbra felette
//                }
            }
        }

        return hiddenTable;
    }

    public static void drawTable(char[][] table) {                   //Számok a sorok és az oszlopok mellé!
        for (int i = 0; i < table.length; i++) {
            System.out.println();
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(table[i][j]);
                System.out.print("  ");
            }
        }
    }

    public static void drawTable(int[][] table) {                   //Számok a sorok és az oszlopok mellé!
        for (int i = 1; i < table.length - 1; i++) {                    // a kerület nem kerül megjelenítésre
            System.out.println();
            for (int j = 1; j < table[i].length - 1; j++) {
                System.out.print(table[i][j]);
                System.out.print("  ");
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

    public static int[][] createNullTable(int xSide, int ySide) {
        int[][] nullTable = new int[xSide][ySide];
        for (int i = 0; i < nullTable.length; i++) {
            for (int j = 0; j < nullTable[i].length; j++) {
                nullTable[i][j] = 0;
            }
        }
        return nullTable;
    }

    public static void leftClick() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Válassz sort!");
        int yChoice = sc.nextInt();
        System.out.println("Válassz oszlopot!");
        int xChoice = sc.nextInt();
    }

}
