package espol.tic_tac_toe.controllers;

import espol.tic_tac_toe.App;
import espol.tic_tac_toe.enums.Mark;
import espol.tic_tac_toe.models.Board;
import espol.tic_tac_toe.models.CPU;
import espol.tic_tac_toe.models.Human;
import espol.tic_tac_toe.models.Match;
import espol.tic_tac_toe.models.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.SVGPath;

public class HomeController implements Initializable {

    @FXML
    private ToggleGroup markSelector;

    @FXML
    private ToggleButton xSelectionBtn;

    @FXML
    private ToggleButton oSelectionBtn;

    @FXML
    private SVGPath xSVG;

    @FXML
    private SVGPath oSVG;

    @FXML
    private ToggleGroup firstSelector;

    @FXML
    private ToggleButton xFirstBtn;

    @FXML
    private ToggleButton oFirstBtn;

    @FXML
    private SVGPath xFirstSVG;

    @FXML
    private SVGPath oFirstSVG;

    private Mark player1Mark;

    private Mark firstTurnMark;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void onMarkSelected(ActionEvent event) {

        if (event.getSource() == xSelectionBtn) {
            switchButtonAppearance("#a8bfc9", "#1a2a33");
            player1Mark = Mark.X;

        } else if (event.getSource() == oSelectionBtn) {
            switchButtonAppearance("#1a2a33", "#a8bfc9");
            player1Mark = Mark.O;
        }

    }

    private void switchButtonAppearance(String color1, String color2) {
        xSelectionBtn.setStyle("-fx-background-color: " + color1);
        xSVG.setStyle("-fx-fill: " + color2);
        oSelectionBtn.setStyle("-fx-background-color: " + color2);
        oSVG.setStyle("-fx-fill: " + color1);
    }

    @FXML
    void onFirstSelected(ActionEvent event) {

        if (event.getSource() == xFirstBtn) {
            switchFirstButtonAppearance("#a8bfc9", "#1a2a33");
            firstTurnMark = Mark.X;

        } else if (event.getSource() == oFirstBtn) {
            switchFirstButtonAppearance("#1a2a33", "#a8bfc9");
            firstTurnMark = Mark.O;
        }
    }

    private void switchFirstButtonAppearance(String color1, String color2) {
        xFirstBtn.setStyle("-fx-background-color: " + color1);
        xFirstSVG.setStyle("-fx-fill: " + color2);
        oFirstBtn.setStyle("-fx-background-color: " + color2);
        oFirstSVG.setStyle("-fx-fill: " + color1);
    }

    @FXML
    void onNewGameVersusCPU(ActionEvent event) throws IOException {
        if (markSelector.getSelectedToggle() != null
                && firstSelector.getSelectedToggle() != null) {
            Player playerX;
            Player playerO;

            if (player1Mark == Mark.X) {
                Match.isPlayer1X = true;
                playerX = new Human(Mark.X);
                playerO = new CPU(Mark.O);
            } else {
                Match.isPlayer1X = false;
                playerO = new Human(Mark.O);
                playerX = new CPU(Mark.X);
            }

            startGame(playerX, playerO);

            App.setRoot("game");
        }
    }

    @FXML
    void onNewGameVersusP2(ActionEvent event) throws IOException {
        if (markSelector.getSelectedToggle() != null
                && firstSelector.getSelectedToggle() != null) {
            Player playerX = new Human(Mark.X);
            Player playerO = new Human(Mark.O);

            Match.isPlayer1X = player1Mark == Mark.X;

            startGame(playerX, playerO);

            App.setRoot("game");

        }
    }

    @FXML
    void onLoadGame(ActionEvent event) throws IOException {
        App.setRoot("loadGame");
    }

    @FXML
    void onNewGameCPU1vsCPU2(ActionEvent event) throws IOException {
        if (markSelector.getSelectedToggle() != null
                && firstSelector.getSelectedToggle() != null) {
            Player playerX = new CPU(Mark.X);
            Player playerO = new CPU(Mark.O);

            Match.isPlayer1X = player1Mark == Mark.X;

            startGame(playerX, playerO);

            App.setRoot("game");

        }
    }

    private void startGame(Player playerX, Player playerO) {
        Match.id = UUID.randomUUID().toString();
        Match.board = new Board();
        Match.playerX = playerX;
        Match.playerO = playerO;
        Match.firstTurn = firstTurnMark == Mark.X ? playerX : playerO;
        Match.currentTurn = Match.firstTurn;
        Match.ties = 0;
        Match.winsO = 0;
        Match.winsX = 0;
    }
    
}
