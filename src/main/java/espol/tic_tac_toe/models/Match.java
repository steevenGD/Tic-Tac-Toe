package espol.tic_tac_toe.models;

import espol.tic_tac_toe.adts.Tree;
import java.time.LocalDateTime;

public class Match {

    public static String id;
    public static int winsX;
    public static int winsO;
    public static int ties;
    public static Board board;
    public static Player playerX;
    public static Player playerO;
    public static Player currentTurn;
    public static Player firstTurn;
    public static Tree<Board> predictionsTree;
    public static boolean isPlayer1X;
    public static LocalDateTime saveDateTime;
    
}
