public class unicodeChar {
//narrow border
    public static final String VV_N_TABLE = "\u2502";   // Vertical line
    public static final String HH_N_TABLE = "\u2500";   // Horizontal line
    public static final String LU_N_TABLE = "\u250c";   // Left Upper
    public static final String LM_N_TABLE = "\u251c";   // Left Middle
    public static final String LL_N_TABLE = "\u2514";   // Left Lower
    public static final String MU_N_TABLE = "\u252c";   // Middle Upper
    public static final String MM_N_TABLE = "\u253c";   // Middle Middle
    public static final String ML_N_TABLE = "\u2534";   // Middle Lower
    public static final String RU_N_TABLE = "\u2510";   // Right Upper
    public static final String RM_N_TABLE = "\u2524";   // Right Middle
    public static final String RL_N_TABLE = "\u2518";   // Right Lower

    //Table border elements/

    /**
     *
     * @param playerBoardY - a plyerBoard tömb valós magassága (a látható + 2), utolsó elem indexe 1-el kisebb!
     * @param playerBoardX - a plyerBoard tömb valós szélessége(a látható + 2), utolsó elem indexe 1-el kisebb!
     * @param tableLine Az aktuális sor indexe
     * @return  Adott vonalkombináció visszaadása String-ként
     */
    public static String tableRows(int playerBoardY, int playerBoardX, int tableLine) {

        String firstLine = LU_N_TABLE + HH_N_TABLE + HH_N_TABLE + HH_N_TABLE;       //első sor kezdete - first
        String firstTag = MU_N_TABLE + HH_N_TABLE + HH_N_TABLE + HH_N_TABLE;        //első sor ismétlődő eleme
        String generalLine = LM_N_TABLE + HH_N_TABLE + HH_N_TABLE + HH_N_TABLE;     //általános sor kezdete
        String generalTag = MM_N_TABLE + HH_N_TABLE + HH_N_TABLE + HH_N_TABLE;     //általános sor ismétlődő eleme
        String lastLine = LL_N_TABLE + HH_N_TABLE + HH_N_TABLE + HH_N_TABLE;        //utolsó sor kezdete -last
        String lastTag = ML_N_TABLE + HH_N_TABLE + HH_N_TABLE + HH_N_TABLE;        //utolsó sor ismétlődő eleme
        String actualLine;      //az aktuális sor szerinti kezdőelem
        String actualTag;      //az aktuális sor szerinti közbenső elem
        String actualEnd;       //az aktuális sor vége

        if (tableLine == 1) {           //az eredeti tömb indexei alapján!
            actualLine = firstLine;
            actualTag = firstTag;
            actualEnd = RU_N_TABLE;
        } else if (tableLine == playerBoardY-1) {
            //i = a tömb indexe, utolsó látható sorban 1-gyel kisebb, azt kell 1-el növelni
            actualLine = lastLine;
            actualTag = lastTag;
            actualEnd = RL_N_TABLE;
        } else {
            actualLine = generalLine;
            actualTag = generalTag;
            actualEnd = RM_N_TABLE;
        }

        for (int i = 1; i < playerBoardX; i++) {
            actualLine += actualTag;
        }
        actualLine += actualEnd;
        return actualLine;

    }

}


