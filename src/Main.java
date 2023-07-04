import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

/* Szintek, korlátozások, értékvizsgálat, ne lehessen oda rakni ahol már van számérték!

 */
public class Main {
    public static void main(String[] args) {
        // mines
        int mines = 8;                                      //szinteket belerakni
        int xSide = 8;                                       //x dimension of the table (megnövelt érték az első és az utolsó sorral!)
        int ySide = 8;                                       //y dimension of the table (megnövelt érték az első és az utolsó oszloppal!)
        char[][] table = createEmptyTable(xSide, ySide);        //the gameboard
        drawTable(table);
        System.out.println();
        int[] firstChoices = firstClick();                      // [Y és X]

        System.out.println();
        int[][] hiddenResult = hiddenTable(mines, xSide, ySide, firstChoices[0], firstChoices[1]); //******** X és Y felcserélve!(0 és 1)
        drawTable(hiddenResult);                                 // amíg készül a kód, kiíratjuk a hiddenTablet-t is
        table = emptyField(firstChoices[0], firstChoices[1], hiddenResult, table);
        System.out.println();
        drawTable(table);           //Ha elsőre nyerünk, akkor ne fusson le a mögötte lévő ciklus
        int i = 0;
        String[] choices = new String[0];
        boolean isWon = true;
        boolean addFlag = true;     //true hozzáadásnál...etc
        int flags = 0;

        do {
            String[] choicesClick = click();
            choices = Arrays.copyOf(choices, choices.length + 3);
            System.out.println(Arrays.toString(choicesClick));
            System.out.println(Arrays.toString(choices));
            choices[i] = choicesClick[0];
            choices[i + 1] = choicesClick[1];
            choices[i + 2] = choicesClick[2];
            System.out.println(choices[i]);
            System.out.println(choices[i + 1]);

            int choiceY = Integer.parseInt(choices[i]);
            int choiceX = Integer.parseInt(choices[i + 1]);
            int choiceValue = hiddenResult[choiceY][choiceX];

            if (choices[i + 2].equals("F")) {
                createFlag(choiceY, choiceX, table);        //beírja vagy kitörli az F-et!
                drawTable(table);
                System.out.println();

            } else if (choiceValue == 9) {        //gamover
                gameOver(choiceY, choiceX, hiddenResult, table);
                drawTable(hiddenResult);
                System.out.println();
                isWon = false;
                break;
            } else if (choiceValue == 0) {         //null
                emptyField(choiceY, choiceX, hiddenResult, table);
                //choiceX és choiceY sorrendje felcserélve a függvénybemenetben
                drawTable(hiddenResult);
                System.out.println();
                drawTable(table);
            } else if (choiceValue > 0 && choiceValue < 9) {         //number
                showNumber(choiceY, choiceX, hiddenResult, table);
                drawTable(hiddenResult);
                System.out.println();
                drawTable(table);

            }

            i += 3;
        }
        while (!isFinished(table, mines));
        if (isWon == true) {
            drawTable(finalFlag(table));
            System.out.println("Nyertél, gratulálok!");
        } else {
            drawTable(table);
            System.out.println("Vesztettél, majd legközelebb, ügyesen játszottál!");
        }
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
    public static int[][] hiddenTable(int mines, int xSide, int ySide, int chosenCoordinateX, int chosenCoordinateY) {
        int[][] hiddenTable = createNullTable(xSide, ySide);
        int mineCreated = 0;
        while (mineCreated < mines) {
            int randX = ThreadLocalRandom.current().nextInt(1, xSide - 1);      //creating random coordinates
            int randY = ThreadLocalRandom.current().nextInt(1, ySide - 1);      //creating random coordinates
            if (((((Math.abs(randX - chosenCoordinateX)) > 1)
                    || ((Math.abs(randY - chosenCoordinateY)) > 1)) && hiddenTable[randX][randY] != 9)) {    //******** X és Y felcserélve!
                //az első lépés szomszédos mezőire, és arra a mezőre, ahol már van akna nem rak aknát
                //jobb átlátás/ellenőrzés miatt a "9"-es a "*"
                hiddenTable[randX][randY] = 9;              //******** X és Y felcserélve!
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
        hiddenTable[chosenCoordinateX][chosenCoordinateY] = 8;  //******** X és Y felcserélve!
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
        System.out.println();
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
     * @param hiddenResult      - a rejtett tábla
     * @param table             - gameboard amit a játékos lát
     * @return table - a bemenetét adja vissza további mezőket felfedve
     */
    public static char[][] emptyField(int chosenCoordinateX, int chosenCoordinateY, int[][] hiddenResult, char[][] table) {
        table[chosenCoordinateX][chosenCoordinateY] = '0';                      //******** X és Y felcserélve!

        List<Integer> listEarlierPointsX = new ArrayList<>();              //Lista létrehozása X-re
        List<Integer> listEarlierPointsY = new ArrayList<>();
        listEarlierPointsX.add(chosenCoordinateX);                              //Első megadott X koordináta hozzáadása
        listEarlierPointsY.add(chosenCoordinateY);                              //Első megadott Y koordináta hozzáadása

        for (int i = chosenCoordinateX - 1; i <= chosenCoordinateX + 1; i++) {      //******** X és Y felcserélve!
            for (int j = chosenCoordinateY - 1; j <= chosenCoordinateY + 1; j++) {

                boolean withinBorders = i > 0 && j > 0 && i < hiddenResult.length - 1 && j < hiddenResult[i].length - 1;
                boolean lastNeighbourCheck = i == chosenCoordinateX + 1 && j == chosenCoordinateY + 1;
                boolean yesBombNeighbour = hiddenResult[i][j] != 0 && hiddenResult[i][j] != 9 && table[i][j] == '_';
                boolean noBombNeighbour = hiddenResult[i][j] == 0 && table[i][j] == '_';
                boolean alreadyVisible = table[i][j] != '_';
                boolean onlyOneZeroCoordinate = listEarlierPointsX.size() <= 1;


                //  ***eredeti tömbös elképzelés***
//                int[] earlierPointsX = new int[1];      //x koordináták
//                int[] earlierPointsY = new int[1];      //y koordináták
//                earlierPointsX[0] = chosenCoordinateX;    //a kezdő Xértéket beírja eleve
//                earlierPointsY[0] = chosenCoordinateY;    //a kezdő Xértéket beírja eleve


                if (noBombNeighbour && withinBorders) {                             //ha 0-t talál
                    table[i][j] = '0';
                    chosenCoordinateX = i;      //******** X és Y felcserélve!
                    chosenCoordinateY = j;      //koordináta-változtatás
                    listEarlierPointsX.add(chosenCoordinateX);                              //új X koordináta hozzáadása
                    listEarlierPointsY.add(chosenCoordinateY);                              //új Y koordináta hozzáadása
                    i = chosenCoordinateX - 1;                                              //az i, j-t itt kell meghatározni, különben nem jó!!!!!
                    j = chosenCoordinateY - 2;                                              // A j azért -2 mert a j++ alapból hozzáad

                } else if (yesBombNeighbour && withinBorders) {                      //ha számot talál, ami nem 0
                    table[i][j] = Character.forDigit(hiddenResult[i][j], 10);
                    table[chosenCoordinateX][chosenCoordinateY] = '0';                       //******** X és Y felcserélve!
                    if (lastNeighbourCheck) {
                        if (onlyOneZeroCoordinate) {
                            continue;
                        }
                        listEarlierPointsX.remove(listEarlierPointsX.size() - 1);         //kivenni a legutolsó X listelemet
                        listEarlierPointsY.remove(listEarlierPointsY.size() - 1);         //kivenni a legutolsó Y listelemet

                        chosenCoordinateX = listEarlierPointsX.get(listEarlierPointsX.size() - 1);  //koordináta-változtatás
                        chosenCoordinateY = listEarlierPointsY.get(listEarlierPointsY.size() - 1);  //koordináta-változtatás
                        i = chosenCoordinateX - 1;
                        j = chosenCoordinateY - 2;
                    }

                } else if (alreadyVisible && lastNeighbourCheck) {            //ha már felfedett mezőt talál
                    if (onlyOneZeroCoordinate) {
                        continue;
                    }
                    listEarlierPointsX.remove(listEarlierPointsX.size() - 1);         //kivenni a legutolsó X listelemet
                    listEarlierPointsY.remove(listEarlierPointsY.size() - 1);         //kivenni a legutolsó Y listelemet

                    chosenCoordinateX = listEarlierPointsX.get(listEarlierPointsX.size() - 1);  //koordináta-változtatás
                    chosenCoordinateY = listEarlierPointsY.get(listEarlierPointsY.size() - 1);  //koordináta-változtatás
                    i = chosenCoordinateX - 1;
                    j = chosenCoordinateY - 2;

                } else if (!withinBorders && lastNeighbourCheck) {      //ha a látható táblán kívülre esik az szomszéd-vizsgálat utolsó lépése
                    if (onlyOneZeroCoordinate) {
                        continue;
                    }
                    listEarlierPointsX.remove(listEarlierPointsX.size() - 1);         //kivenni a legutolsó X listelemet
                    listEarlierPointsY.remove(listEarlierPointsY.size() - 1);         //kivenni a legutolsó Y listelemet

                    chosenCoordinateX = listEarlierPointsX.get(listEarlierPointsX.size() - 1);  //koordináta-változtatás
                    chosenCoordinateY = listEarlierPointsY.get(listEarlierPointsY.size() - 1);  //koordináta-változtatás
                    i = chosenCoordinateX - 1;
                    j = chosenCoordinateY - 2;
                }
            }
        }
        return table;
    }
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


//A 0 melletti "0" mezők fefedése


    /**
     * Létrehozza a táblát
     *
     * @param xSide
     * @param ySide
     * @return
     */
    public static char[][] createEmptyTable(int xSide, int ySide) {
        char[][] table = new char[xSide][ySide];                            //******** X és Y felcserélve!
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
        int[][] nullTable = new int[xSide][ySide];                          //******** X és Y felcserélve!
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
    public static void leftClick() {

    }

    /**
     * Lépés bekérés, ismétlődő
     */
    public static String[] click() {
        String[] click = new String[3];
        Scanner sc = new Scanner(System.in);
        System.out.println("Válassz sort!(add meg az Y értékét)");
        click[0] = sc.nextLine();
        System.out.println("Válassz oszlopot!(add meg az X értékét)");
        click[1] = sc.nextLine();
        System.out.println("Ha meg akarod jelölni, nyomj egy F-et?");       //hülyebiztos legyen!!!!!!!!!!!!!!!!!!!!!! - try catch
        click[2] = sc.nextLine();
        return click;
    }

    public static char[][] gameOver(int yChoice, int xChoice, int[][] hiddenResult, char[][] gameOverBoard) {
        if (hiddenResult[yChoice][xChoice] == 9) {
            for (int i = 1; i < hiddenResult.length - 1; i++) {
                for (int j = 1; j < hiddenResult[i].length - 1; j++) {
                    if (hiddenResult[i][j] == 9) {
                        gameOverBoard[i][j] = '*';
                    }
                }
            }
        }
        return gameOverBoard;
    }


    public static char[][] showNumber(int yChoice, int xChoice, int[][] hiddenResult, char[][] board) {
        if (hiddenResult[yChoice][xChoice] != 9 && hiddenResult[yChoice][xChoice] != 0) {
            board[yChoice][xChoice] = Character.forDigit(hiddenResult[yChoice][xChoice], 10);
        }
        return board;
    }


    public static boolean isFinished(char[][] board, int mines) {
        int count = 0;
        for (int i = 1; i < board.length - 1; i++) {
            for (int j = 1; j < board[i].length - 1; j++) {
                if (board[i][j] == '_' || board[i][j] == 'F') {
                    count++;
                }
            }
        }
        if (count == mines) {
            return true;
        }
        return false;

    }

    public static char[][] finalFlag(char[][] board) {
        for (int i = 1; i < board.length - 1; i++) {
            for (int j = 1; j < board[i].length - 1; j++) {
                if (board[i][j] == '_') {
                    board[i][j] = 'F';
                }
            }
        }
        return board;
    }


    public static char[][] createFlag(int xCoord, int yCoord, char[][] board) {
        if (board[xCoord][yCoord] == 'F') {
            board[xCoord][yCoord] = '_';
        } else if (board[xCoord][yCoord] == '_') {
            board[xCoord][yCoord] = 'F';
        }
        return board;
    }

//    public static int flagCounter(int flagcounter, boolean addFlag) {
//        return flagcounter = addFlag ? flagcounter++ : flagcounter--;
//    }

}
