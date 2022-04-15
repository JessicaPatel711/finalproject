package csci2020u.finalproject.tictactoe;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * The info center is the area above the tile board
 */
public class InfoCenter {
    private StackPane pane;
    private Label message;

    public InfoCenter() {
        pane = new StackPane();
        pane.setMinSize(UIConstants.APP_WIDTH, UIConstants.INFO_CENTER_HEIGHT);
        pane.setTranslateX((float) UIConstants.APP_WIDTH / 2); // place in the middle on the pane
        pane.setTranslateY((float) UIConstants.INFO_CENTER_HEIGHT / 2);

        // the message to be displayed in the info center
        message = new Label(" Tic-Tac-Toe ");
        message.setMinSize(UIConstants.APP_WIDTH, UIConstants.INFO_CENTER_HEIGHT);
        message.setFont(Font.font(24));
        message.setAlignment(Pos.CENTER);
        message.setStyle("-fx-font-size:20; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: radial-gradient(center 50% 50%, radius 100%, #de62ba, #f568ff, #613b87)");


        // add the message to the pane
        pane.getChildren().add(message);
    }

    // getters
    public StackPane getStackPane() {
        return pane;
    }

    public void setInfoCenterMessage(String message) {
        this.message.setText(message);
    }
}
