module csci2020u.finalproject.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;


    opens csci2020u.finalproject.tictactoe to javafx.fxml;
    exports csci2020u.finalproject.tictactoe;
}