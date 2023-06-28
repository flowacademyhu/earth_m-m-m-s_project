import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/*
Szabalyok
- Tetszoleges teglalap alapu palya - done
- Minden mezo vagy akna vagy nem akna
- Mezoket meg lehet jelolni vagy ra lehet lepni
- Jelolest azt jelenti, hogy aknat velunk azon a mezon
Ralepes:
- ha akna volt a mezon, akkor vesztettunk
- ha van szomszedos akna, akkor a szomszedos aknak szama kerul a mezore
-ha nincs szomszedos akna, akkor az egybefuggo aknamentes resz kerul felfedesre
utolso mezo eseten, ha az nem akna, akkor nyert
- Jatek vegen a palyat fel kell fedni
Pontszamitas:
- nehezsegi pont a palya merete es aknak szama alapjan
- nehezseg alapjan minden lepesert pontlevonas
- eltelt ido

Nehezitesek:
- Egyedi palyameret
- Szabalyozhato akna szam
- Elore meghatarozott nehezsegek (konnyu, kozepes, nehez) ami megadja a palya meretet es akna szamot
- Smiley a kozepen
- Minel szebb megjelenes
 */

/* Arrays.fill() method??
Alternatíva a kezdeti feltöltésre?

 */
public class Main {
    public static void main(String[] args) {
        int xSide = 10;                                       //x dimension of the table (megnövelt érték az első és az utolsó sorral!)
        int ySide = 10;                                       //y dimension of the table (megnövelt érték az első és az utolsó oszloppal!)
        char[][] table = createEmptyTable(xSide, ySide);
        drawTable(table);
        System.out.println();
        int[] firstChoices = firstClick();
        //nehezitest belerakni!!!!!!!!!!!!!!!!!
        drawTable(hiddenTable(xSide, ySide, firstChoices[1], firstChoices[0]));               //a koordináták Y,X sorrendben vannak / amíg készül a kód, kiíratjuk a hiddenTable
        System.out.println();
    }

    public static int[][] hiddenTable(int xSide, int ySide, int chosenCoordinateX, int chosenCoordinateY) {
        int[][] hiddenTable = createNullTable(xSide, ySide);
        int mineNumber = 10;                                       //predefined
        int mineCreated = 0;
        while (mineCreated < mineNumber) {
            int randX = ThreadLocalRandom.current().nextInt(1, xSide - 1);      //creating random coordinates
            int randY = ThreadLocalRandom.current().nextInt(1, ySide - 1);      //creating random coordinates
            if (((((Math.abs(randX - chosenCoordinateX)) > 1) || ((Math.abs(randY - chosenCoordinateY)) > 1)) && hiddenTable[randX][randY] != 9)) { //az első lépés szomszédos mezőire, és arra a mezőre, ahol már van akna nem rak aknát
            //átírtam kicsita kódot, megfordítottam a reláció jelet (így már egy feltételbe lehet rakni a nem egyenlő 9-cel),
            // illetve "vagy"-t raktam közéjük, mert "és"-sel mind2 irányban 3 oszlop szélesen nem rakna aknát
            // de ez nekünk nem szükséges. Illetve odaraktam "és"-sel a nem egyenlő 9-et is, és így akkor nem kell "else if"

                hiddenTable[randX][randY] = 9;                                                    //jobb átlátás/ellenőrzés miatt a "9"-es a "*"
                mineCreated++;
            }
        }
        //Eddig legenerálódnak a csillagok

        // számértékek megjelenítése
        for (int i = 1; i < hiddenTable.length - 1; i++) {                                  //1-gyel csökkentett határok
            for (int j = 1; j < hiddenTable[i].length - 1; j++) {
                if (hiddenTable[i][j] == 9) {                                              //jobb átlátás/ellenőrzés miatt a "9"-es a "*"
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            if (hiddenTable[k][l] != 9) {
                                hiddenTable[k][l]++;
                            }
                        }
                    }
                }
            }
        }
        hiddenTable[chosenCoordinateX][chosenCoordinateY] = 8;
        return hiddenTable;
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


    public static void drawTable(char[][] table) {                   //Számok a sorok és az oszlopok mellé!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        for (int i = 1; i < table.length - 1; i++) {                  //a keret (első és utolsó sor, ill. oszlop) nem kerül megjelenítésre
            System.out.println();
            for (int j = 1; j < table[i].length - 1; j++) {
                System.out.print(table[i][j]);
                System.out.print("  ");
            }
        }
    }

    public static void drawTable(int[][] table) {                   //Számok a sorok és az oszlopok mellé!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        for (int i = 1; i < table.length - 1; i++) {                //a keret (első és utolsó sor, ill. oszlop) nem kerül megjelenítésre
            System.out.println();
            for (int j = 1; j < table[i].length - 1; j++) {
                System.out.print(table[i][j]);
                System.out.print("  ");
            }
        }
    }

    public static char[][] createEmptyTable(int xSide, int ySide) {
        char[][] table = new char[xSide][ySide];
        for (char[] chars : table) {                                        //eredetileg két for ciklus volt itt
            Arrays.fill(chars, '_');
        }
        return table;
    }

    public static int[][] createNullTable(int xSide, int ySide) {
        int[][] nullTable = new int[xSide][ySide];
        for (int[] ints : nullTable) {                                      //eredetileg két for ciklus volt itt
            Arrays.fill(ints, 0);
        }
        return nullTable;
    }

    public static int[] firstClick() {
        int[] firstClick = new int[2];
        Scanner sc = new Scanner(System.in);
        System.out.println("Válassz sort!");
        firstClick[0] = sc.nextInt();
        System.out.println("Válassz oszlopot!");
        firstClick[1] = sc.nextInt();
        return firstClick;
    }


    public static String[] click() {
        String[] click = new String[3];
        Scanner sc = new Scanner(System.in);
        System.out.println("Válassz sort!");
        click[0] = sc.nextLine();
        System.out.println("Válassz oszlopot!");
        click[1] = sc.nextLine();
        System.out.println("Ha meg akarod jelölni, nyomj egy F-et?");       //hülyebiztos legyen!!!!!!!!!!!!!!!!!!!!!!
        click[2] = sc.nextLine();
//        if (flag == "F") {
//            rightClick();
//        } else {
//            leftClick();
//        }
        return click;
    }


//    public static

//    public static boolean isGameOver(int yChoice, int xChoice, String flag) {
//
//    }
}
