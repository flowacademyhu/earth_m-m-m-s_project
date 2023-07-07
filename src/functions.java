import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class functions {

    public static int timeElapsed = 0;
    private static Timer timer;
    private static TimerTask runningGame;


    public static void cheat(char[][] board, int[][] hiddenResult) {
        for (int i = 1; i < board.length - 1; i++) {
            for (int j = 1; j < board[i].length - 1; j++) {
                if ((board[i][j] == '_' && hiddenResult[i][j] == 9) || (board[i][j] == 'F' && hiddenResult[i][j] == 9)) {
                    board[i][j] = '*';
                }
            }
        }
    }

    public static void setTimer() {                                                                              //TIMER ITT VAN.
        timer = new Timer();
        runningGame = new TimerTask() {
            @Override
            public void run() {
                timeElapsed++;
            }
        };
        timer.schedule(runningGame, 0, 1000);
    }

    public static void stopTimer() {
        timer.cancel();
        runningGame.cancel();

    }


    /**
     * Első koordináta bekérés
     *
     * @return
     */
    public static int[] firstClick(int ySide, int xSide) {
        int[] firstClick = new int[2];
        Scanner sc = new Scanner(System.in);
        boolean isWrongInputX = true;
        boolean isWrongInputY = true;

        while (isWrongInputX) {             //X koordináta vizsgálata
            try {
                System.out.println("Válassz oszlopot!(add meg az X értékét)");
                firstClick[1] = sc.nextInt();
            } catch (InputMismatchException f) {
                System.out.println("Nem számot adtál meg koordinátának!");
                sc.next();                                          // ez átírja a bemenetet, enélkül végtelen loop
                continue;
            }

            if (firstClick[1] < 1 || firstClick[1] > ySide - 2) {
                System.out.println("A táblán kívül van a megadott oszlop!");
            } else {
                isWrongInputX = false;
            }
        }

        while (isWrongInputY) {             //Y koordináta vizsgálata
            try {
                System.out.println("Válassz sort!(add meg az Y értékét)");
                firstClick[0] = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Nem számot adtál meg koordinátának!");
                sc.next();                  // ez átírja a bemenetet, enélkül végtelen loop
                continue;
            }

            if (firstClick[0] < 1 || firstClick[0] > xSide - 2) {
                System.out.println("A táblán kívül van a megadott sor!");
            } else {
                isWrongInputY = false;
            }
        }
        return firstClick;
    }

    /**
     * Lépés bekérés, ismétlődő
     */
    public static String[] click(int ySide, int xSide, char[][] playerBoard) {            //ha már vmi be van írva, azt ne lehessen felülírni, kivéve removeFlag!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String[] click = new String[3];
        Scanner sc = new Scanner(System.in);
        boolean isWrongInputX = true;
        boolean isWrongInputY = true;
        boolean isAlreadyVisible = true;

        do {
            while (isWrongInputX) {             //X koordináta vizsgálata
                try {
                    System.out.println("Válassz oszlopot!(add meg az X értékét)");
                    click[1] = sc.nextLine();
                    Integer.parseInt(click[1]);       //csak akkor működik, ha számot ír be
                } catch (NumberFormatException f) {
                    System.out.println("Nem számot adtál meg koordinátának!");
                    System.out.println();
                    continue;
                }
                int choiceX = Integer.parseInt(click[1]);

                if (choiceX < 1 || choiceX > ySide - 2) {
                    System.out.println("A táblán kívül van a megadott oszlop!");
                    System.out.println();
                } else {
                    isWrongInputX = false;
                }
            }

            while (isWrongInputY) {             //Y koordináta vizsgálata
                try {
                    System.out.println("Válassz sort!(add meg az Y értékét)");
                    click[0] = sc.nextLine();
                    Integer.parseInt(click[0]);       //csak akkor működik, ha számot ír be
                } catch (NumberFormatException e) {
                    System.out.println("Nem számot adtál meg koordinátának!");
                    continue;
                }

                int choiceY = Integer.parseInt(click[0]);

                if (choiceY < 1 || choiceY > xSide - 2) {
                    System.out.println("A táblán kívül van a megadott sor!");
                } else {
                    isWrongInputY = false;
                }
            }

            /* XY pont fel van-e fejtve?  - A 3 inputot lehetne 1 inputba sűríteni mindenféle String művelettel.
           Elkezdtem kialakítani, de még nem "hülyebiztos" de egyelőre ez jobbnak tűnik!
            */
            int choiceY = Integer.parseInt(click[0]);
            int choiceX = Integer.parseInt(click[1]);
            if (playerBoard[choiceY][choiceX] == 'F' || playerBoard[choiceY][choiceX] == '_') {
                isAlreadyVisible = false;
            } else {
                isWrongInputX = true;
                isWrongInputY = true;
                System.out.println("Ez a mező már látható, válassz egy új még nem láthatót!");
                System.out.println();
            }
        } while (isAlreadyVisible);


        System.out.println("Ha meg akarod jelölni, nyomj egy F-et?");
        click[2] = sc.nextLine().toUpperCase();
        return click;
    }

    public static void gameOver(int yChoice, int xChoice, int[][] hiddenResult, char[][] gameOverBoard) {
        if (hiddenResult[yChoice][xChoice] == 9) {
            for (int i = 1; i < hiddenResult.length - 1; i++) {
                for (int j = 1; j < hiddenResult[i].length - 1; j++) {
                    if (hiddenResult[i][j] == 9) {
                        gameOverBoard[i][j] = '*';
                    }
                }
            }
        }
    }

    public static void showNumber(int yChoice, int xChoice, int[][] hiddenResult, char[][] board) {
        if (hiddenResult[yChoice][xChoice] != 9 && hiddenResult[yChoice][xChoice] != 0) {
            board[yChoice][xChoice] = Character.forDigit(hiddenResult[yChoice][xChoice], 10);
        }
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


    public static void createFlag(int xCoord, int yCoord, char[][] board) {
        if (board[xCoord][yCoord] == '_') {
            board[xCoord][yCoord] = 'F';
        }
    }

    public static void removeFlag(int xCoord, int yCoord, char[][] board) {
        if (board[xCoord][yCoord] == 'F') {
            board[xCoord][yCoord] = '_';
        }
    }

    public static void clearScreen() {          //Windows CMD-n és IDE terminálon nem működik! What else???????????????????????
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static int[] gameLevel() {         //hülyebiztosítás!
        System.out.println("Válassz nehézségi szintet!");
        System.out.println("1 - easy, 2 - medium, 3 - Die hard, 4 - egyéni");
        Scanner dc = new Scanner(System.in);
        int level = dc.nextInt();
        //ySide, xSide, mines
        int[] levelEasy = new int[]{10, 10, 10};       //easy + 2-2 a méreteknél
        int[] levelMedium = new int[]{18, 18, 40};
        int[] levelHard = new int[]{34, 34, 100};
        int[] levelCustom = new int[3];
        if (level == 1) {
            return levelEasy;
        } else if (level == 2) {
            return levelMedium;
        } else if (level == 3) {
            return levelHard;
        } else if (level == 4) {
            boolean isPlayable = true;
            while (isPlayable) {
                try {
                    System.out.println("Add meg a pálya magasságát!");
                    Scanner sc = new Scanner(System.in);
                    levelCustom[0] = Math.abs(sc.nextByte() + 2);
                } catch (Exception e) {
                    System.out.println("Nem jó paraméter!");
                    continue;
                }
                isPlayable = false;
            }
            isPlayable = true;
            while (isPlayable) {
                try {
                    System.out.println("Add meg a pálya szélességét!");
                    Scanner sc = new Scanner(System.in);
                    levelCustom[1] = Math.abs(sc.nextByte() + 2);
                } catch (Exception e) {
                    System.out.println("Nem jó paraméter!");
                    continue;
                }
                isPlayable = false;
            }
            isPlayable = true;
            while (isPlayable) {
                try {
                    System.out.println("Add meg az aknák számát");
                    Scanner sc = new Scanner(System.in);
                    levelCustom[2] = Math.abs(sc.nextByte());
                } catch (Exception e) {
                    System.out.println("Nem jó paraméter!");
                    continue;
                }
                isPlayable = false;
            }
            return levelCustom;
        }
        return levelEasy;
    }
}
