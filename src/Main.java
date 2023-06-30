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
        char[][] table = createEmptyTable(xSide, ySide);        //the gameboard
        drawTable(table);
        System.out.println();
        int[] firstChoices = firstClick();                      // [Y és X]
        //nehezitest belerakni!!!!!!!!!!!!!!!!!
        System.out.println();
        int[][] hiddenResult = hiddenTable(xSide, ySide, firstChoices[1], firstChoices[0]); //******** X és Y felcserélve!(0 és 1)
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
            if (((((Math.abs(randX - chosenCoordinateX)) > 1)
                    || ((Math.abs(randY - chosenCoordinateY)) > 1)) && hiddenTable[randY][randX] != 9)) {    //******** X és Y felcserélve!
                //az első lépés szomszédos mezőire, és arra a mezőre, ahol már van akna nem rak aknát
                //jobb átlátás/ellenőrzés miatt a "9"-es a "*"
                hiddenTable[randY][randX] = 9;              //******** X és Y felcserélve!
                mineCreated++;
            }
        }
        //Eddig legenerálódnak a csillagok

        // számértékek megjelenítése
        for (int i = 1; i < hiddenTable.length - 1; i++) {                   //1-gyel csökkentett határok
            for (int j = 1; j < hiddenTable[i].length - 1; j++) {
                if (hiddenTable[i][j] == 9) {                                //jobb átlátás/ellenőrzés miatt a "9"-es a "*"
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
        hiddenTable[chosenCoordinateY][chosenCoordinateX] = 8;  //******** X és Y felcserélve!
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

    /**
     * @param chosenCoordinateX - a kiválasztott koordináta x értéke
     * @param chosenCoordinateY - a kiválasztott koordináta y értéke
     * @param sideX             - a játéktér szélessége (látható tartmomány)
     * @param sideY             - a játéktér magassága (látható tartmomány)
     * @param hiddenResult      - a rejtett tábla
     * @param table             - gameboard amit a játékos lát
     * @return table - a bemenetét adja vissza további mezőket felfedve
     */
    public static char[][] emptyField(int chosenCoordinateX, int chosenCoordinateY, int sideX, int sideY, int[][] hiddenResult, char[][] table) {
        table[chosenCoordinateY][chosenCoordinateX] = '0';                      //******** X és Y felcserélve!
//        int actualX = 0;      //a nullpont aktuális koordinátája - X          //Ez mire kellett? - REDUNDÁNS
//        int actualY = 0;      //a nullpont aktuális koordinátája - Y          //Ez mire kellett? - REDUNDÁNS

        ArrayList<Integer> listEarlierPointsX = new ArrayList<>();              //Lista létrehozása X-re
        ArrayList<Integer> listEarlierPointsY = new ArrayList<>();
        listEarlierPointsX.add(chosenCoordinateX);                              //Első megadott X koordináta hozzáadása
        listEarlierPointsY.add(chosenCoordinateY);


        for (int i = chosenCoordinateY - 1; i <= chosenCoordinateY + 1; i++) {      //******** X és Y felcserélve!
            for (int j = chosenCoordinateX - 1; j <= chosenCoordinateX + 1; j++) {
                System.out.println();                                               //csak fejlesztésre jelenítjük meg!
                System.out.println(i + " " + j);

                //  ***eredeti tömbös elképzelés***
//                int[] earlierPointsX = new int[1];      //x koordináták
//                int[] earlierPointsY = new int[1];      //y koordináták
//                earlierPointsX[0] = chosenCoordinateX;    //a kezdő Xértéket beírja eleve
//                earlierPointsY[0] = chosenCoordinateY;    //a kezdő Xértéket beírja eleve

                if (hiddenResult[i][j] == 0 && table[i][j] != '0' && i > 0 && j > 0     //ha a megtalált mező 0...
                        && i < hiddenResult.length - 1 && j < hiddenResult[i].length - 1) {

                    table[i][j] = '0';
                    chosenCoordinateY =  i;      //******** X és Y felcserélve!
                    chosenCoordinateX =  j;      //koordináta-változtatás
                    /* Itt a chosenCoordinateY +=i helyett simán csak i értékét veszi fel, ezért már nem ugrik ki a limitből!!*/


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


                } else if (hiddenResult[i][j] != 0 && hiddenResult[i][j] != 9 && table[i][j] == '_') {  //utolsót negáltam, hogy =='_'
                    table[i][j] = Character.forDigit(hiddenResult[i][j], 10); //radix?
                    table[chosenCoordinateY][chosenCoordinateX] = '0';                       //******** X és Y felcserélve!
                    //valamiért elveszti az értékét az előző sor nélkül KEZDŐPONT
                } else {
                    if (listEarlierPointsX.size() <= 1) {
                        continue;
                    }

                    listEarlierPointsX.remove(listEarlierPointsX.size() - 1);         //kivenni a legutolsó X listelemet
                    listEarlierPointsY.remove(listEarlierPointsY.size() - 1);         //kivenni a legutolsó X listelemet

                    chosenCoordinateX = listEarlierPointsX.get(listEarlierPointsX.size() - 1);  //koordináta-változtatás
                    chosenCoordinateY = listEarlierPointsY.get(listEarlierPointsY.size() - 1);  //koordináta-változtatás
                }           //Itt volt egy elírás: a 2. kifejezésben is X volt


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
        char[][] table = new char[ySide][xSide];                            //******** X és Y felcserélve!
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
        int[][] nullTable = new int[ySide][xSide];                          //******** X és Y felcserélve!
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
        System.out.println("Válassz sort!(add meg az Y értékét)");
        firstClick[0] = sc.nextInt();
        System.out.println("Válassz oszlopot!(add meg az X értékét)");
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
