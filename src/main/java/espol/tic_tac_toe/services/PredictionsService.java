package espol.tic_tac_toe.services;

import espol.tic_tac_toe.adts.Tree;
import espol.tic_tac_toe.enums.Mark;
import espol.tic_tac_toe.models.Board;
import espol.tic_tac_toe.models.Player;
import espol.tic_tac_toe.utils.UtilityCalculator;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PredictionsService {

    public static Tree<Board> generatePredictionsTree(Board matchsBoard, Player playerX, Player playerO, Player curentTurn) {
        Tree<Board> predictionsTree = new Tree<>(matchsBoard);

        List<Tree<Board>> firstTurnPossibleStates = generatePossibleStates(predictionsTree.getRoot(), curentTurn);

//        System.out.println("firstTurnPossibleStates:" + firstTurnPossibleStates);
//        System.out.println("");

        predictionsTree.addChildren(firstTurnPossibleStates);

        Player oponent = curentTurn.getMark() == Mark.X ? playerO : playerX;

        for (Tree<Board> firstTurnPossibleState : predictionsTree.getChildren()) {
            List<Tree<Board>> secondTurnPossibleStates = generatePossibleStates(firstTurnPossibleState.getRoot(), oponent);

//            System.out.println("secondTurnPossibleStates:" + secondTurnPossibleStates);
//            System.out.println("");

            firstTurnPossibleState.addChildren(secondTurnPossibleStates);
        }

        return predictionsTree;
    }

    private static List<Tree<Board>> generatePossibleStates(Board board, Player currentTurn) {
        List<Tree<Board>> possibleStates = new LinkedList<>();

        Mark[][] matrix = board.getMatrix();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == null) {
                    Mark[][] possibleStateMatrix = new Mark[matrix.length][matrix[i].length];
                    for (int row = 0; row < matrix.length; row++) {
                        System.arraycopy(matrix[row], 0, possibleStateMatrix[row], 0, matrix[i].length);
                    }

                    possibleStateMatrix[i][j] = currentTurn.getMark();

                    Board possibleStateBoard = new Board(possibleStateMatrix);
                    possibleStates.add(new Tree(possibleStateBoard));
                }
            }
        }

        return possibleStates;
    }

    public static Board getBestMovement(Tree<Board> predictionsTree, Player player, Player oponent) {
        if (predictionsTree.getChildren().size() == 1) {
            return predictionsTree.getChildren().get(0).getRoot();
        }

        List<Integer> minUtilitySecondLevel = new LinkedList<>();

        for (Tree<Board> firstLevelBoard : predictionsTree.getChildren()) {
            List<Integer> secondLevelUtilitiesPerFirstLevelBoard = new LinkedList<>();

            for (Tree<Board> secondLevelBoard : firstLevelBoard.getChildren()) {
                Integer secondLevelBoardUtility = UtilityCalculator.calculate(secondLevelBoard.getRoot(), player, oponent);
                secondLevelUtilitiesPerFirstLevelBoard.add(secondLevelBoardUtility);
            }

            Integer minUtilityPerFirstLevelBoard = Collections.min(secondLevelUtilitiesPerFirstLevelBoard);
            minUtilitySecondLevel.add(minUtilityPerFirstLevelBoard);
        }

        int maxMinUtilitySecondLevel = Collections.max(minUtilitySecondLevel);
        int maxMinUtilitySecondLevelIndex = minUtilitySecondLevel.indexOf(maxMinUtilitySecondLevel);

        Board bestMovement = predictionsTree.getChildren().get(maxMinUtilitySecondLevelIndex).getRoot();

        return bestMovement;
    }
}
