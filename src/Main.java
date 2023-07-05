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
        int numberOfMines = 10;                                      //szinteket belerakni
        int xSide = 10;                                       //x dimension of the board (megnövelt érték az első és az utolsó sorral!)
        int ySide = 10;                                       //y dimension of the board (megnövelt érték az első és az utolsó oszloppal!)
        char[][] playerBoard = createEmptyBoard(xSide, ySide);        //the gameboard
        drawBoard(playerBoard);
        System.out.println();
        int[] firstChoices = firstClick();                      // [Y és X]

        System.out.println();
        int[][] hiddenResult = hiddenBoard(numberOfMines, xSide, ySide, firstChoices[0], firstChoices[1]); //******** X és Y felcserélve!(0 és 1)
        drawBoard(hiddenResult);                                 // amíg készül a kód, kiíratjuk a hiddenBoard-t is
        emptyField(firstChoices[0], firstChoices[1], hiddenResult, playerBoard);

        System.out.println();
        drawBoard(playerBoard);
        System.out.println("Felhasználható zászlók száma: " + numberOfMines);
        if (!isFinished(playerBoard, numberOfMines)) {      //Ha elsőre nyerünk, akkor nem fut le a mögötte lévő do-while ciklus
            int i = 0;
            String[] choices = new String[0];
            boolean isWon = true;
            int flagCounter = 0;

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
                int choiceX = Integer.parseInt(choices[i + 1]); //+ 1 a koordinátákhoz, mert a számozás miatt el van tolva

                int choiceValue = hiddenResult[choiceY][choiceX];

                if (choices[i + 2].equals("F")) {                            //flag funkció hozzáadása
                    if (flagCounter < numberOfMines) {
                        if (playerBoard[choiceY][choiceX] == '_') {
                            createFlag(choiceY, choiceX, playerBoard);      //beírja az F-et!
                            flagCounter++;
                        } else if (playerBoard[choiceY][choiceX] == 'F') {
                            removeFlag(choiceY, choiceX, playerBoard);      //kitörli az F-et!
                            flagCounter--;
                        }
                    } else {
                        if (playerBoard[choiceY][choiceX] == '_') {
                            System.out.println("Elfogytak a zászlóid!");
                        } else if (playerBoard[choiceY][choiceX] == 'F') {
                            removeFlag(choiceY, choiceX, playerBoard);      //kitörli az F-et!
                            flagCounter--;
                        }
                    }
                    drawBoard(playerBoard);
                    System.out.println("Felhasználható zászlók száma: " + (numberOfMines - flagCounter));
                    System.out.println("flagCounter: " + flagCounter);
                    System.out.println();

                } else if (choiceValue == 9) {        //ha gameover
                    gameOver(choiceY, choiceX, hiddenResult, playerBoard);
                    drawBoard(hiddenResult);
                    System.out.println();
                    isWon = false;
                    break;
                } else if (choiceValue == 0) {         //ha nullát talál
                    emptyField(choiceY, choiceX, hiddenResult, playerBoard);
                    //choiceX és choiceY sorrendje felcserélve a függvénybemenetben
                    drawBoard(hiddenResult);
                    System.out.println();
                    drawBoard(playerBoard);
                    System.out.println("Felhasználható zászlók száma: " + (numberOfMines - flagCounter));
                } else if (choiceValue > 0 && choiceValue < 9) {         //ha 0-tól eltérő számot talál
                    showNumber(choiceY, choiceX, hiddenResult, playerBoard);
                    drawBoard(hiddenResult);
                    System.out.println();
                    drawBoard(playerBoard);
                    System.out.println("Felhasználható zászlók száma: " + (numberOfMines - flagCounter));
                }
                i += 3;
            }
            while (!isFinished(playerBoard, numberOfMines));

            if (isWon) {
                drawBoard(flagsAfterWin(playerBoard));
                System.out.println("Felhasználható zászlók száma: " + (numberOfMines - flagCounter));
                System.out.println("Nyertél, gratulálok!");
            } else {
                drawBoard(playerBoard);
                System.out.println("Felhasználható zászlók száma: " + (numberOfMines - flagCounter));
                System.out.println("Vesztettél, majd legközelebb! Ügyesen játszottál!");
            }
        } else {
            System.out.println("Nyertél, gratulálok!");

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
    public static int[][] hiddenBoard(int numberOfMines, int xSide, int ySide, int chosenCoordinateX, int chosenCoordinateY) {
        int[][] hiddenBoard = createNullBoard(xSide, ySide);
        int mineCreated = 0;
        while (mineCreated < numberOfMines) {
            int randX = ThreadLocalRandom.current().nextInt(1, xSide - 1);      //creating random coordinates
            int randY = ThreadLocalRandom.current().nextInt(1, ySide - 1);      //creating random coordinates
            if (((((Math.abs(randX - chosenCoordinateX)) > 1)
                    || ((Math.abs(randY - chosenCoordinateY)) > 1)) && hiddenBoard[randX][randY] != 9)) {    //******** X és Y felcserélve!
                //az első lépés szomszédos mezőire, és arra a mezőre, ahol már van akna nem rak aknát
                hiddenBoard[randX][randY] = 9;              //******** X és Y felcserélve!
                mineCreated++;
            }
        }
        //Eddig legenerálódnak a csillagok

        // számértékek megjelenítése
        for (int i = 1; i < hiddenBoard.length - 1; i++) {                   //2-vel csökkentett határok
            for (int j = 1; j < hiddenBoard[i].length - 1; j++) {
                if (hiddenBoard[i][j] == 9) {                                //jobb átlátás/ellenőrzés miatt a "9"-es a "*"
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            if (hiddenBoard[k][l] != 9) {
                                hiddenBoard[k][l]++;
                            }
                        }
                    }
                }
            }
        }
        hiddenBoard[chosenCoordinateX][chosenCoordinateY] = 8;  //******** X és Y felcserélve!
        return hiddenBoard;
    }

    /**
     * Megrajzolja a látható táblát
     *
     * @param board
     */
    public static void drawBoard(char[][] board) {
        int rowCounter = 0;
        int columnCounter = 0;

        for (int i = 0; i < board[0].length - 2; i++) {            //Számok kiírása az oszlopok fölé
            columnCounter++;
            System.out.print(columnCounter + (columnCounter > 9 ? "  " : "   "));  //10 oszlop felett mínusz egy szóköz, mert különben elcsúszna a számozás az oszlopoktól
        }
        System.out.println();
        System.out.println();

        for (int i = 1; i < board.length - 1; i++) {
            for (int j = 1; j < board[0].length - 1; j++) {
                System.out.print(board[i][j]);                   //a játékterület kiíratása
                if (j == board[0].length - 2) {                  //sor végén a számok kiírása az sorok mellé
                    rowCounter++;
                    System.out.print("     " + rowCounter);
                    System.out.println();
                    break;
                }
                System.out.print("   ");                        //a mezők elválasztása egymástól a sorokban
            }
        }
        System.out.println();
    }

    /**
     * Megrajzolja a rejtett táblát
     *
     * @param board
     */
    public static void drawBoard(int[][] board) {                   //Számok a sorok és az oszlopok mellé!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        for (int i = 1; i < board.length - 1; i++) {                //a keret (első és utolsó sor, ill. oszlop) nem kerül megjelenítésre
            System.out.println();
            for (int j = 1; j < board[i].length - 1; j++) {
                System.out.print(board[i][j]);
                System.out.print("  ");
            }
        }
        System.out.println();
    }

    /**
     * @param chosenCoordinateX - a kiválasztott koordináta x értéke
     * @param chosenCoordinateY - a kiválasztott koordináta y értéke
     * @param hiddenResult      - a rejtett tábla
     * @param board             - gameboard amit a játékos lát
     * @return board - a bemenetét adja vissza további mezőket felfedve
     */
    public static char[][] emptyField(int chosenCoordinateX, int chosenCoordinateY, int[][] hiddenResult, char[][] board) {
        board[chosenCoordinateX][chosenCoordinateY] = '0';                      //******** X és Y felcserélve!

        List<Integer> listEarlierPointsX = new ArrayList<>();              //Lista létrehozása X-re
        List<Integer> listEarlierPointsY = new ArrayList<>();
        listEarlierPointsX.add(chosenCoordinateX);                              //Első megadott X koordináta hozzáadása
        listEarlierPointsY.add(chosenCoordinateY);                              //Első megadott Y koordináta hozzáadása

        for (int i = chosenCoordinateX - 1; i <= chosenCoordinateX + 1; i++) {      //******** X és Y felcserélve!
            for (int j = chosenCoordinateY - 1; j <= chosenCoordinateY + 1; j++) {

                boolean withinBorders = i > 0 && j > 0 && i < hiddenResult.length - 1 && j < hiddenResult[i].length - 1;
                boolean lastNeighbourCheck = i == chosenCoordinateX + 1 && j == chosenCoordinateY + 1;
                boolean yesBombNeighbour = hiddenResult[i][j] != 0 && hiddenResult[i][j] != 9 && board[i][j] == '_';
                boolean noBombNeighbour = hiddenResult[i][j] == 0 && board[i][j] == '_';
                boolean alreadyVisible = board[i][j] != '_';
                boolean onlyOneZeroCoordinate = listEarlierPointsX.size() <= 1;


                //  ***eredeti tömbös elképzelés***
//                int[] earlierPointsX = new int[1];      //x koordináták
//                int[] earlierPointsY = new int[1];      //y koordináták
//                earlierPointsX[0] = chosenCoordinateX;    //a kezdő Xértéket beírja eleve
//                earlierPointsY[0] = chosenCoordinateY;    //a kezdő Xértéket beírja eleve


                if (noBombNeighbour && withinBorders) {                             //ha 0-t talál
                    board[i][j] = '0';
                    chosenCoordinateX = i;      //******** X és Y felcserélve!
                    chosenCoordinateY = j;      //koordináta-változtatás
                    listEarlierPointsX.add(chosenCoordinateX);                              //új X koordináta hozzáadása
                    listEarlierPointsY.add(chosenCoordinateY);                              //új Y koordináta hozzáadása
                    i = chosenCoordinateX - 1;                                              //az i, j-t itt kell meghatározni, különben nem jó!!!!!
                    j = chosenCoordinateY - 2;                                              // A j azért -2 mert a j++ alapból hozzáad

                } else if (yesBombNeighbour && withinBorders) {                      //ha számot talál, ami nem 0
                    board[i][j] = Character.forDigit(hiddenResult[i][j], 10);
                    board[chosenCoordinateX][chosenCoordinateY] = '0';                       //******** X és Y felcserélve!
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
        return board;
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
    public static char[][] createEmptyBoard(int xSide, int ySide) {
        char[][] board = new char[xSide][ySide];                            //******** X és Y felcserélve!
        for (char[] chars : board) {                                        //eredetileg két for ciklus volt itt
            Arrays.fill(chars, '_');
        }
        return board;
    }

    /**
     * Nullákkal feltölti a táblázatot
     *
     * @param xSide
     * @param ySide
     * @return
     */
    public static int[][] createNullBoard(int xSide, int ySide) {
        int[][] nullBoard = new int[xSide][ySide];                          //******** X és Y felcserélve!
        for (int[] ints : nullBoard) {                                      //eredetileg két for ciklus volt itt
            Arrays.fill(ints, 0);
        }
        return nullBoard;
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

    /**
     * Lépés bekérés, ismétlődő
     */
    public static String[] click() {            //ha már vmi be van írva, azt ne lehessen felülírni, kivéve removeFlag!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String[] click = new String[3];
        Scanner sc = new Scanner(System.in);
        System.out.println("Válassz sort!(add meg az Y értékét)");
        click[0] = sc.nextLine();
        System.out.println("Válassz oszlopot!(add meg az X értékét)");
        click[1] = sc.nextLine();
        System.out.println("Ha meg akarod jelölni, nyomj egy F-et?");       //hülyebiztos legyen!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! - try catch
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


    public static boolean isFinished(char[][] board, int numberOfMines) {
        int count = 0;
        for (int i = 1; i < board.length - 1; i++) {
            for (int j = 1; j < board[i].length - 1; j++) {
                if (board[i][j] == '_' || board[i][j] == 'F') {
                    count++;
                }
            }
        }
        return count == numberOfMines;

    }

    public static char[][] flagsAfterWin(char[][] board) {
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
        if (board[xCoord][yCoord] == '_') {
            board[xCoord][yCoord] = 'F';
        }
        return board;
    }

    public static char[][] removeFlag(int xCoord, int yCoord, char[][] board) {
        if (board[xCoord][yCoord] == 'F') {
            board[xCoord][yCoord] = '_';
        }
        return board;
    }
}
