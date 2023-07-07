import java.util.*;


/*
Szabalyok
- Tetszoleges teglalap alapu palya - done
- Minden mezo vagy akna vagy nem akna - done
- Mezoket meg lehet jelolni vagy ra lehet lepni - done
- Jelolest azt jelenti, hogy aknat velunk azon a mezon - done
Ralepes:
- ha akna volt a mezon, akkor vesztettunk - done
- ha van szomszedos akna, akkor a szomszedos aknak szama kerul a mezore - done
-ha nincs szomszedos akna, akkor az egybefuggo aknamentes resz kerul felfedesre - done
utolso mezo eseten, ha az nem akna, akkor nyert - done
- Jatek vegen a palyat fel kell fedni - done

Pontszamitas:
- nehezsegi pont a palya merete es aknak szama alapjan
- nehezseg alapjan minden lepesert pontlevonas
- eltelt ido

Nehezitesek:
- Egyedi palyameret - done
- Szabalyozhato akna szam -done
- Elore meghatarozott nehezsegek (konnyu, kozepes, nehez) ami megadja a palya meretet es akna szamot -done
- Smiley a kozepen
- Minel szebb megjelenes
 */

/* Extra features
- nehézségi szint szám megadásánál hiba javítás, és bombák számának korlátozása.
- megfelelő bemenetek korlátozása - done***************************
- mentés felajánlása
- cheat: ezt beírva kirajzolja a hidden table-t - done
- szabályok kiiratása a játék megkezdése előtt, pl. Ctrl+C kilépés, stb.

 */


public class Main {
    public static void main(String[] args) {                        //XY oldalak meg vannak fordítva!

        functions.clearScreen();
        board.gameRules();
        int[] gameLevelParams = functions.gameLevel();
        int numberOfMines = gameLevelParams[2];
        int xSide = gameLevelParams[0];                                     //x dimension of the board (megnövelt érték az első és az utolsó sorral!)
        int ySide = gameLevelParams[1];                                       //y dimension of the board (megnövelt érték az első és az utolsó oszloppal!)
        char[][] playerBoard = board.createEmptyBoard(xSide, ySide);        //the gameboard
        functions.clearScreen();
        board.drawBoard(playerBoard);                                       //playerBoard kiiratás az első input előtt
        System.out.println();
        int[] firstChoices = functions.firstClick(ySide, xSide);                      // [Y és X]
        System.out.println();
        functions.setTimer();
        int[][] hiddenResult = board.hiddenBoard(numberOfMines, xSide, ySide, firstChoices[0], firstChoices[1]); //******** X és Y felcserélve!(0 és 1)
        board.drawBoard(hiddenResult);                                // amíg készül a kód, kiíratjuk a hiddenBoard-ot is
        board.emptyField(firstChoices[0], firstChoices[1], hiddenResult, playerBoard);
        System.out.println();
        functions.clearScreen();                                                  // amíg készül a kód, kiíratjuk a hiddenBoard-ot is
        board.drawBoard(playerBoard);                                         //playerBoard kiiratás inputok előtt
        System.out.println("Felhasználható zászlók száma: " + numberOfMines);

        if (!functions.isFinished(playerBoard, numberOfMines)) {      //Ha elsőre nyerünk, akkor nem fut le a mögötte lévő do-while ciklus
            int i = 0;
            String[] choices = new String[0];
            boolean isWon = true;
            int flagCounter = 0;

            do {
                String[] choicesClick = functions.click(ySide, xSide, playerBoard);
                choices = Arrays.copyOf(choices, choices.length + 3);
//                System.out.println(Arrays.toString(choicesClick));
//                System.out.println(Arrays.toString(choices));
                choices[i] = choicesClick[0];
                choices[i + 1] = choicesClick[1];
                choices[i + 2] = choicesClick[2];
//                System.out.println(choices[i]);
//                System.out.println(choices[i + 1]);

                int choiceY = Integer.parseInt(choices[i]);
                int choiceX = Integer.parseInt(choices[i + 1]);

                int choiceValue = hiddenResult[choiceY][choiceX];

                if (choices[i + 2].equals("F")) {                            //flag funkció hozzáadása
                    if (flagCounter < numberOfMines) {
                        if (playerBoard[choiceY][choiceX] == '_') {
                            functions.createFlag(choiceY, choiceX, playerBoard);      //beírja az F-et!
                            flagCounter++;
                        } else if (playerBoard[choiceY][choiceX] == 'F') {
                            functions.removeFlag(choiceY, choiceX, playerBoard);      //kitörli az F-et!
                            flagCounter--;
                        } else {
                            System.out.println("Ez a mező már fel van fedve, ide nem léphetsz.");
                        }
                    } else {
                        if (playerBoard[choiceY][choiceX] == '_') {
                            System.out.println("Elfogytak a zászlóid!");
                        } else if (playerBoard[choiceY][choiceX] == 'F') {
                            functions.removeFlag(choiceY, choiceX, playerBoard);      //kitörli az F-et!
                            flagCounter--;
                        } else {
                            System.out.println("Ez a mező már fel van fedve, ide nem léphetsz.");
                        }
                    }
                    functions.clearScreen();
                    board.drawBoard(playerBoard);
                    System.out.println("Felhasználható zászlók száma: " + (numberOfMines - flagCounter));
//                    System.out.println("flagCounter: " + flagCounter);
                    System.out.println();

                } else if (playerBoard[choiceY][choiceX] == 'F' && !choices[i + 2].equals("F")) {
                    System.out.println("Ezen a mezőn már zászló van, a felfedéséhez vedd vissza a zászlót!");
                } else if (choices[i + 2].equals("CHEAT")) {
                    functions.cheat(playerBoard, hiddenResult);
                    functions.clearScreen();
                    board.drawBoard(playerBoard);
                } else if (choiceValue == 9) {        //ha gameover
                    functions.gameOver(choiceY, choiceX, hiddenResult, playerBoard);
                    functions.clearScreen();
                    board.drawBoard(hiddenResult);
                    System.out.println();
                    isWon = false;
                    break;
                } else if (choiceValue == 0) {         //ha nullát talál
                    board.emptyField(choiceY, choiceX, hiddenResult, playerBoard);
                    //choiceX és choiceY sorrendje felcserélve a függvénybemenetben
                    board.drawBoard(hiddenResult);
                    System.out.println();
                    functions.clearScreen();
                    board.drawBoard(playerBoard);
                    System.out.println("Felhasználható zászlók száma: " + (numberOfMines - flagCounter));
                } else if (choiceValue > 0 && choiceValue < 9) {         //ha 0-tól eltérő számot talál
                    functions.showNumber(choiceY, choiceX, hiddenResult, playerBoard);
                    functions.clearScreen();
                    board.drawBoard(hiddenResult);
                    System.out.println();
                    functions.clearScreen();
                    board.drawBoard(playerBoard);
                    System.out.println("Felhasználható zászlók száma: " + (numberOfMines - flagCounter));
                }
                i += 3;
            }
            while (!functions.isFinished(playerBoard, numberOfMines));

            if (isWon) {
                functions.clearScreen();
                board.drawBoard(functions.flagsAfterWin(playerBoard));
                System.out.println("Felhasználható zászlók száma: " + (numberOfMines - flagCounter));
                System.out.println("Nyertél, gratulálok!");
                functions.stopTimer();
                System.out.println("Eltelt idő: " + functions.timeElapsed + " másodperc");
            } else {
                functions.clearScreen();
                board.drawBoard(playerBoard);
                System.out.println("Felhasználható zászlók száma: " + (numberOfMines - flagCounter));
                System.out.println("Vesztettél, majd legközelebb! Ügyesen játszottál!");
                functions.stopTimer();
                System.out.println("Eltelt idő: " + functions.timeElapsed + " másodperc");
            }
        } else {
            System.out.println("Nyertél, gratulálok!");
            functions.stopTimer();
            System.out.println("Eltelt idő: " + functions.timeElapsed + " másodperc");
        }

    }

















}
