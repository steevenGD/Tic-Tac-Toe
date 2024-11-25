package espol.tic_tac_toe.models;

import espol.tic_tac_toe.enums.Mark;
import java.io.Serializable;

public class Board implements Serializable {

    private Mark[][] matrix;

    public Board() {
        this.matrix = new Mark[3][3];
    }

    public Board(Mark[][] matrix) {
        this.matrix = matrix;
    }

    public Mark[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Mark[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(matrix[i][j]);
                if (j < 2) {
                    sb.append(" "); // Add space between elements in the same row
                }
            }
            if (i < 2) {
                sb.append("\n"); // Add newline between rows
            }
        }
        return "\n" + sb.toString() + "\n";
    }
}
