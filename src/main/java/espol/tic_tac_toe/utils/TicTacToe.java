package espol.tic_tac_toe.utils;

import espol.tic_tac_toe.enums.Mark;

public class TicTacToe {

    // Method to check if a given mark is a winner on the board
    public static boolean isWinner(Mark[][] board, Mark markToCheck) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (checkLine(markToCheck, board[i][0], board[i][1], board[i][2])
                    || checkLine(markToCheck, board[0][i], board[1][i], board[2][i])) {
                return true;
            }
        }

        // Check diagonals
        return checkLine(markToCheck, board[0][0], board[1][1], board[2][2])
                || checkLine(markToCheck, board[0][2], board[1][1], board[2][0]);
    }

// Helper method to check a line for a winner
    private static boolean checkLine(Mark markToCheck, Mark a, Mark b, Mark c) {
        return a == markToCheck && b == markToCheck && c == markToCheck;
    }

    // Helper method to check if the board is full
    public static boolean isBoardFull(Mark[][] board) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == null) {
                    // If any cell is empty, the board is not full
                    return false;
                }
            }
        }
        // All cells are occupied, the board is full
        return true;
    }

    // Method to find the coordinates of the unique different cell
    public static int[] findDifferentCell(Mark[][] board1, Mark[][] board2) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board1[row][col] != board2[row][col]) {
                    // Return the coordinates of the different cell
                    return new int[]{row, col};
                }
            }
        }
        // No difference found
        return null;
    }
}
