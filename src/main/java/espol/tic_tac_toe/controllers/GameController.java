package espol.tic_tac_toe.controllers;

import espol.tic_tac_toe.App;
import espol.tic_tac_toe.enums.Mark;
import espol.tic_tac_toe.helpers.Colors;
import espol.tic_tac_toe.helpers.IconsContent;
import espol.tic_tac_toe.models.Board;
import espol.tic_tac_toe.models.CPU;
import espol.tic_tac_toe.models.Human;
import espol.tic_tac_toe.models.Match;
import espol.tic_tac_toe.models.Player;
import espol.tic_tac_toe.services.PredictionsService;
import espol.tic_tac_toe.utils.MatchWrapper;
import espol.tic_tac_toe.utils.TicTacToe;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

public class GameController implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private SVGPath currentTurn;

    @FXML
    private Label xLabel;

    @FXML
    private Label xWinsLabel;

    @FXML
    private Label tiesLabel;

    @FXML
    private Label oLabel;

    @FXML
    private Label oWinsLabel;

    private Board bestMovement;

    private static boolean isSaved;

    private boolean exit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Attach hover event handlers to each VBox in the GridPane
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                VBox vBox = getVBox(gridPane, row, col);
                attachEventHandlers(vBox, row, col);

                // update each vBox if game is saved
                if (isSaved) {
                    markFromSavedState(vBox, row, col);
                }

            }
        }
        if (isSaved) {
            updateCounts();
        }

        updateCurrentTurnView();
        updateBestMovement();

        int[] bestMovementCoords = TicTacToe.findDifferentCell(Match.board.getMatrix(), bestMovement.getMatrix());

        if (Match.currentTurn instanceof CPU) {

            VBox bestMovementGridItem = getVBox(gridPane, bestMovementCoords[0], bestMovementCoords[1]);

            simulateClick(bestMovementGridItem);
        }

        if (isPlayerVsCPU()) {
            xLabel.setText(Match.playerX instanceof Human ? "X (YOU)" : "X (CPU)");
            oLabel.setText(Match.playerO instanceof Human ? "O (YOU)" : "O (CPU)");
        } else if (isPlayerVsPlayer()) {
            xLabel.setText(Match.isPlayer1X ? "X (PLAYER 1)" : "X (PLAYER 2)");
            oLabel.setText(Match.isPlayer1X ? "O (PLAYER 2)" : "O (PLAYER 1)");
        } else if (isCPUVsCPU()) {
            xLabel.setText(Match.isPlayer1X ? "X (CPU 1)" : "X (CPU 2)");
            oLabel.setText(Match.isPlayer1X ? "O (CPU 2)" : "O (CPU 1)");
        }
    }

    private boolean isPlayerVsCPU() {
        return (Match.playerX instanceof Human && Match.playerO instanceof CPU) || (Match.playerX instanceof CPU && Match.playerO instanceof Human);
    }

    private boolean isPlayerVsPlayer() {
        return Match.playerX instanceof Human && Match.playerO instanceof Human;
    }

    private boolean isCPUVsCPU() {
        return Match.playerX instanceof CPU && Match.playerO instanceof CPU;
    }

    private void attachEventHandlers(VBox vBox, int row, int col) {
        vBox.setOnMouseEntered((MouseEvent event) -> {
            // Change content on hover
            SVGPath svg = (SVGPath) vBox.getChildren().get(0);

            if (svg.getContent().isBlank()) {
                svg.setContent(isCurrentMarkX() ? IconsContent.X : IconsContent.O);
                svg.setFill(Paint.valueOf("#ffffff00"));
                svg.setStroke(Paint.valueOf(isCurrentMarkX() ? Colors.X : Colors.O));
                svg.setStrokeWidth(2);

                int[] bestMovementCoords = TicTacToe.findDifferentCell(Match.board.getMatrix(), bestMovement.getMatrix());

                if (bestMovementCoords[0] == row && bestMovementCoords[1] == col) {
                    svg.setStroke(Paint.valueOf("#ffffff"));
                }
            }
        });

        //Add mouse exit event to revert changes
        vBox.setOnMouseExited((MouseEvent event) -> {
            SVGPath svg = (SVGPath) vBox.getChildren().get(0);
            if (svg.getFill().equals(Paint.valueOf("#ffffff00"))) {
                svg.setContent("");
            }
        });

        vBox.setOnMouseClicked((MouseEvent event) -> {
            SVGPath svg = (SVGPath) vBox.getChildren().get(0);

            if (svg.getFill().equals(Paint.valueOf("#ffffff00"))) {
                svg.setContent(isCurrentMarkX() ? IconsContent.X : IconsContent.O);
                svg.setFill(Paint.valueOf(isCurrentMarkX() ? Colors.X : Colors.O));
                svg.setStroke(Paint.valueOf(isCurrentMarkX() ? Colors.X : Colors.O));
                svg.setStrokeWidth(2);

                updateMatchState(row, col);

                int[] bestMovementCoords = TicTacToe.findDifferentCell(Match.board.getMatrix(), bestMovement.getMatrix());

                if (Match.currentTurn instanceof CPU) {

                    VBox bestMovementGridItem = getVBox(gridPane, bestMovementCoords[0], bestMovementCoords[1]);

                    simulateClick(bestMovementGridItem);
                }

            }
        });
    }

    private boolean isCurrentMarkX() {
        return (Match.currentTurn.getMark() == Mark.X);
    }

    private void updateMatchState(int newRow, int newCol) {
        System.out.println(Match.currentTurn.getMark() + " hit click on " + "(" + newRow + "," + newCol + ")");

        updateMatchsBoard(newRow, newCol);

        System.out.println(Match.board);

        boolean isWinner = TicTacToe.isWinner(Match.board.getMatrix(), Match.currentTurn.getMark());

        if (isWinner) {
            showWinnerAlert();
            updateWinnerCount();
            onRestart();
            return;
        }

        boolean isBoardFull = TicTacToe.isBoardFull(Match.board.getMatrix());

        if (isBoardFull) {
            showTieAlert();
            updateTieCount();
            onRestart();
            return;
        }

        updateCurrentTurn();
        updateCurrentTurnView();
        updateBestMovement();
    }

    private void updateWinnerCount() {
        if (isCurrentMarkX()) {
            Match.winsX = Match.winsX + 1;
            xWinsLabel.setText(String.valueOf(Match.winsX));
        } else {
            Match.winsO = Match.winsO + 1;
            oWinsLabel.setText(String.valueOf(Match.winsO));
        }
    }

    private void updateTieCount() {
        Match.ties = Match.ties + 1;
        tiesLabel.setText(String.valueOf(Match.ties));
    }

    private void showWinnerAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Congratulations!");
        alert.setHeaderText(null);

        HBox container = new HBox();

        SVGPath svgPath = new SVGPath();
        svgPath.setContent(isCurrentMarkX() ? IconsContent.X : IconsContent.O);
        svgPath.setFill(Paint.valueOf(isCurrentMarkX() ? Colors.X : Colors.O));
        svgPath.setStroke(Paint.valueOf(isCurrentMarkX() ? Colors.X : Colors.O));
        svgPath.setStrokeWidth(2);

        Text text = new Text("Won!");
        text.setFill(Paint.valueOf("#a8bfc9"));
        text.setStyle("-fx-font-weight: 900; -fx-font-size: 20px");

        container.getChildren().addAll(svgPath, text);
        container.setPadding(new Insets(10));
        container.setSpacing(10);
        container.setAlignment(Pos.CENTER);

        alert.getDialogPane().setBackground(new Background(new BackgroundFill(Color.valueOf("#1a2a33"), null, null)));
        alert.setGraphic(container);

        alert.showAndWait();
    }

    private void showTieAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Well played!");
        alert.setHeaderText(null);

        HBox container = new HBox();

        SVGPath svgPath1 = new SVGPath();
        svgPath1.setContent(IconsContent.X);
        svgPath1.setFill(Paint.valueOf(Colors.X));
        svgPath1.setStroke(Paint.valueOf(Colors.X));
        svgPath1.setStrokeWidth(2);

        SVGPath svgPath2 = new SVGPath();
        svgPath2.setContent(IconsContent.O);
        svgPath2.setFill(Paint.valueOf(Colors.O));
        svgPath2.setStroke(Paint.valueOf(Colors.O));
        svgPath2.setStrokeWidth(2);

        Text text = new Text("It's a tie!");
        text.setFill(Paint.valueOf("#a8bfc9"));
        text.setStyle("-fx-font-weight: 900; -fx-font-size: 20px");

        container.getChildren().addAll(svgPath1, svgPath2, text);
        container.setPadding(new Insets(10));
        container.setSpacing(10);
        container.setAlignment(Pos.CENTER);

        alert.getDialogPane().setBackground(new Background(new BackgroundFill(Color.valueOf("#1a2a33"), null, null)));
        alert.setGraphic(container);

        alert.showAndWait();
    }

    private void updateCurrentTurn() {
        Match.currentTurn = isCurrentMarkX() ? Match.playerO : Match.playerX;
    }

    private void updateMatchsBoard(int newRow, int newCol) {
        Match.board.getMatrix()[newRow][newCol] = Match.currentTurn.getMark();
    }

    private void updateCurrentTurnView() {
        currentTurn.setContent(isCurrentMarkX() ? IconsContent.X_SMALL : IconsContent.O_SMALL);
    }

    private void updateBestMovement() {
        Match.predictionsTree = PredictionsService.generatePredictionsTree(Match.board, Match.playerX, Match.playerO, Match.currentTurn);

        System.out.println("currentBoard: ");
        System.out.println(Match.predictionsTree);

        bestMovement = PredictionsService.getBestMovement(Match.predictionsTree, Match.currentTurn, getOpponent());

        System.out.println("bestMovement: ");
        System.out.println(bestMovement);
    }

    private Player getOpponent() {
        return isCurrentMarkX() ? Match.playerO : Match.playerX;
    }

    private VBox getVBox(GridPane gridPane, int row, int col) {
        // Calculate the index in the children list based on row and column
        int index = row * gridPane.getColumnCount() + col;

        // Retrieve the VBox at the specified index
        return (VBox) gridPane.getChildren().get(index);
    }

    private void simulateClick(VBox vBox) {
        if (exit) {
            return;
        }

        CompletableFuture<Void> asyncTask = CompletableFuture.runAsync(() -> {
            try {
                // Simulate some time-consuming task
                Thread.sleep(isCPUVsCPU() ? 2000 : 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Attach a callback to the CompletableFuture to update the UI when the task completes
        asyncTask.thenRun(() -> {
            Platform.runLater(() -> {
                MouseEvent mouseEvent = new MouseEvent(
                        MouseEvent.MOUSE_CLICKED,
                        0, 0, 0, 0,
                        null, 0, false, false, false, false,
                        false, false, false, false, false, false, null
                );

                vBox.fireEvent(mouseEvent);
            });
        });

    }

    public void onRestart() {
        Match.board = new Board();
        Match.currentTurn = Match.firstTurn;
        updateCurrentTurnView();
        updateBestMovement();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                VBox vBox = getVBox(gridPane, row, col);
                SVGPath svg = (SVGPath) vBox.getChildren().get(0);
                svg.setFill(Paint.valueOf("#ffffff00"));
                svg.setContent("");
            }
        }

        int[] bestMovementCoords = TicTacToe.findDifferentCell(Match.board.getMatrix(), bestMovement.getMatrix());

        if (Match.currentTurn instanceof CPU) {
            VBox bestMovementGridItem = getVBox(gridPane, bestMovementCoords[0], bestMovementCoords[1]);

            simulateClick(bestMovementGridItem);
        }

    }

    public void onSave() {
        Thread thread = new Thread(() -> {

            Match.saveDateTime = LocalDateTime.now();
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.path + "files/" + Match.id + ".bin"))) {
                out.writeObject(new MatchWrapper(Match.id, Match.winsX, Match.winsO,
                        Match.ties, Match.board, Match.playerX, Match.playerO, Match.currentTurn,
                        Match.firstTurn, Match.predictionsTree, Match.isPlayer1X,
                        Match.saveDateTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.run();
        thread.setDaemon(true);
    }

    public void onExit() throws IOException {
        exit = true;
        App.setRoot("home");
    }

    public static void setSaved(boolean isGameSaved) {
        isSaved = isGameSaved;
    }

    private void markFromSavedState(VBox vBox, int row, int col) {
        if (Match.board.getMatrix()[row][col] == Mark.O) {
            drawMark(vBox, Mark.O);
        } else if (Match.board.getMatrix()[row][col] == Mark.X) {
            drawMark(vBox, Mark.X);
        }
    }

    private void drawMark(VBox vBox, Mark mark) {
        SVGPath svg = (SVGPath) vBox.getChildren().get(0);
        svg.setContent(mark == Mark.X ? IconsContent.X : IconsContent.O);
        svg.setFill(Paint.valueOf(mark == Mark.X ? Colors.X : Colors.O));
        svg.setStroke(Paint.valueOf(mark == Mark.X ? Colors.X : Colors.O));
        svg.setStrokeWidth(2);

    }

    private void updateCounts() {
        xWinsLabel.setText(String.valueOf(Match.winsX));
        oWinsLabel.setText(String.valueOf(Match.winsO));
        tiesLabel.setText(String.valueOf(Match.ties));
    }

}
