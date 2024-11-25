module espol.tic_tac_toe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens espol.tic_tac_toe to javafx.fxml;
    exports espol.tic_tac_toe;
    opens espol.tic_tac_toe.controllers to javafx.fxml;
    exports espol.tic_tac_toe.controllers;
}
