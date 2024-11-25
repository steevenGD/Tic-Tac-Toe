package espol.tic_tac_toe.utils;

import espol.tic_tac_toe.adts.Tree;
import espol.tic_tac_toe.models.Board;
import espol.tic_tac_toe.models.Human;
import espol.tic_tac_toe.models.Player;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author argen
 */
public class MatchWrapper implements Serializable {

    private final String id;
    private final int winsX;
    private final int winsO;
    private final int ties;
    private final Board board;
    private final Player playerX;
    private final Player playerO;
    private final Player currentTurn;
    private final Player firstTurn;
    private final Tree<Board> predictionsTree;
    private final boolean isPlayer1X;
    private LocalDateTime saveDateTime;

    public MatchWrapper(String id, int winsX, int winsO, int ties, Board board, Player playerX, Player playerO, Player currentTurn, Player firstTurn, Tree<Board> predictionsTree, boolean isPlayer1X, LocalDateTime saveDateTime) {
        this.id = id;
        this.winsX = winsX;
        this.winsO = winsO;
        this.ties = ties;
        this.board = board;
        this.playerX = playerX;
        this.playerO = playerO;
        this.currentTurn = currentTurn;
        this.firstTurn = firstTurn;
        this.predictionsTree = predictionsTree;
        this.isPlayer1X = isPlayer1X;
        this.saveDateTime = saveDateTime;
    }

    public String getId() {
        return id;
    }

    public int getWinsX() {
        return winsX;
    }

    public int getWinsO() {
        return winsO;
    }

    public int getTies() {
        return ties;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayerX() {
        return playerX;
    }

    public Player getPlayerO() {
        return playerO;
    }

    public Player getCurrentTurn() {
        return currentTurn;
    }

    public Player getFirstTurn() {
        return firstTurn;
    }

    public Tree<Board> getPredictionsTree() {
        return predictionsTree;
    }

    public boolean isPlayer1X() {
        return isPlayer1X;
    }

    public LocalDateTime getSaveDateTime() {
        return saveDateTime;
    }

    public void setSaveDateTime(LocalDateTime saveDateTime) {
        this.saveDateTime = saveDateTime;
    }
    
    
    
    public String getSaveDateTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return saveDateTime.format(formatter);
    }

    public String getPlayers() {

        if (playerX instanceof Human && playerO instanceof Human) {
            return "(P1 vs P2)";
        } else if(playerX instanceof Human || playerO instanceof Human){
            return "(P1 vs CPU)";
        } else {
            return "(CPU1 vs CPU2)";
        }
    }
}
