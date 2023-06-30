import java.util.ArrayList;
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
        System.out.println();
        int[][] hiddenResult = hiddenTable(xSide, ySide, firstChoices[0], firstChoices[1]);
        drawTable(hiddenResult);                                 // amíg készül a kód, kiíratjuk a hiddenTablet-t is
        table = emptyField(firstChoices[0], firstChoices[1], xSide, ySide, hiddenResult, table);
        System.out.println();
        drawTable(table);
    }


    /**
     * Rejtett tábla, amit a játékos nem lát
     *
     * @param xSide
     * @param ySide
     * @param chosenCoordinateX
     * @param chosenCoordinateY
     * @return
     */
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

    /**
     * Megrajzolja a látható táblát
     *
     * @param table
     */
    public static void drawTable(char[][] table) {                   //Számok a sorok és az oszlopok mellé!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        for (int i = 1; i < table.length - 1; i++) {                  //a keret (első és utolsó sor, ill. oszlop) nem kerül megjelenítésre
            System.out.println();
            for (int j = 1; j < table[i].length - 1; j++) {
                System.out.print(table[i][j]);
                System.out.print("  ");
            }
        }
    }

    /**
     * Megrajzolja a rejtett táblát
     *
     * @param table
     */
    public static void drawTable(int[][] table) {                   //Számok a sorok és az oszlopok mellé!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        for (int i = 1; i < table.length - 1; i++) {                //a keret (első és utolsó sor, ill. oszlop) nem kerül megjelenítésre
            System.out.println();
            for (int j = 1; j < table[i].length - 1; j++) {
                System.out.print(table[i][j]);
                System.out.print("  ");
            }
        }
    }

    public static char[][] emptyField(int chosenCoordinateX, int chosenCoordinateY, int sideX, int sideY, int[][] hiddenResult, char[][] table) {
        table[chosenCoordinateX][chosenCoordinateY] = '0';
        int actualX = 0;      //a nullpont aktuális koordinátája - X
        int actualY = 0;      //a nullpont aktuális koordinátája - Y

        ArrayList<Integer> listEarlierPointsX = new ArrayList<>();
        ArrayList<Integer> listEarlierPointsY = new ArrayList<>();
        listEarlierPointsX.add(chosenCoordinateX);
        listEarlierPointsY.add(chosenCoordinateY);


        for (int i = chosenCoordinateX - 1; i <= chosenCoordinateX + 1; i++) {
            for (int j = chosenCoordinateY - 1; j <= chosenCoordinateY + 1; j++) {


                //  ***eredeti tömbös elképzelés***
//                int[] earlierPointsX = new int[1];      //x koordináták
//                int[] earlierPointsY = new int[1];      //y koordináták
//                earlierPointsX[0] = chosenCoordinateX;    //a kezdő Xértéket beírja eleve
//                earlierPointsY[0] = chosenCoordinateY;    //a kezdő Xértéket beírja eleve

                if (hiddenResult[i][j] == 0 && table[i][j] != '0' && i > 0 && j > 0
                        && i < hiddenResult.length - 1 && j < hiddenResult[i].length - 1) {

                    table[i][j] = '0';
                    chosenCoordinateX = chosenCoordinateX + i;      //koordináta-változtatás
                    chosenCoordinateY = chosenCoordinateY + j;      //koordináta-változtatás


                    //  ***eredeti tömbös elképzelés***
//                    earlierPointsX = Arrays.copyOf(earlierPointsX, earlierPointsX.length+1);
//                    chosenCoordinateX = chosenCoordinateX + i;      //koordináta-változtatás
//                    earlierPointsX[earlierPointsX.length-1] = chosenCoordinateX;        //beírtuk a tömbbe
//
//                    earlierPointsY = Arrays.copyOf(earlierPointsY, earlierPointsY.length+1);
//                    chosenCoordinateY = chosenCoordinateY + i;      //koordináta-változtatás
//                    earlierPointsY[earlierPointsY.length-1] = chosenCoordinateY;        //beírtuk a tömbbe

                    //elmentettük a korábbi kezdőpontot (cikluson kívül) és átírtuk a kezdőpontot és azt is kiírtuk


//                    listferenc.remove(Integer.valueOf(2));


                } else if (hiddenResult[i][j] != 0 && hiddenResult[i][j] != 9 && table[i][j] != '_') {
                    table[i][j] = Character.forDigit(hiddenResult[i][j], 10); //radix?
                    table[chosenCoordinateX][chosenCoordinateY] = '0';      //valamirt elveszti az értékét enélkül KEZDŐPONT
                    System.out.print("lista x: " + listEarlierPointsX);
                    System.out.println("lista y: " + listEarlierPointsY);
                    if (listEarlierPointsX.size()<1) {
                        continue;
                    }

                    listEarlierPointsX.remove(listEarlierPointsX.size() - 1);         //kivenni a legutolsó X listelemet
                    listEarlierPointsY.remove(listEarlierPointsY.size() - 1);         //kivenni a legutolsó X listelemet


                    chosenCoordinateX = listEarlierPointsX.get(listEarlierPointsX.size() - 1);  //koordináta-változtatás
                    chosenCoordinateY = listEarlierPointsY.get(listEarlierPointsX.size() - 1);  //koordináta-változtatás
                }

            }


        }
        return table;
    }


    //A 0 melletti "0" mezők fefedése


    /**
     * Létrehozza a táblát
     *
     * @param xSide
     * @param ySide
     * @return
     */
    public static char[][] createEmptyTable(int xSide, int ySide) {
        char[][] table = new char[xSide][ySide];
        for (char[] chars : table) {                                        //eredetileg két for ciklus volt itt
            Arrays.fill(chars, '_');
        }
        return table;
    }

    /**
     * Nullákkal feltölti a táblázatot
     *
     * @param xSide
     * @param ySide
     * @return
     */
    public static int[][] createNullTable(int xSide, int ySide) {
        int[][] nullTable = new int[xSide][ySide];
        for (int[] ints : nullTable) {                                      //eredetileg két for ciklus volt itt
            Arrays.fill(ints, 0);
        }
        return nullTable;
    }

    /**
     * Első koordináta bekérés
     *
     * @return
     */
    public static int[] firstClick() {
        int[] firstClick = new int[2];
        Scanner sc = new Scanner(System.in);
        System.out.println("Válassz sort!");
        firstClick[0] = sc.nextInt();
        System.out.println("Válassz oszlopot!");
        firstClick[1] = sc.nextInt();
        return firstClick;
    }

//    public static int rightClick(){
//        String flag = "F";
//
//    }
//    public static void leftClick(){
//
//    }

    /**
     * Lépés bekérés, ismétlődő
     */
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
