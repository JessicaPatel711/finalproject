/**
 * @author Japnit Ahuja
 * @author Aanisha Newaz
 * @author Chioma Okechukwu
 * @author Jessica Patel
 *
 * @version 1.0
 */

package com.example.finalproject;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * The info center is the area above the tile board
 *
 * InfoCentre is the part of the program UI above the X and O tile board where the players play. 
 * It shows up to date information and updates on the gameplay, for example, announcing the winner.
 */
public class InfoCenter {
    private StackPane pane;
    private Label message;
    /**
    * Class constructor 
    */
    public InfoCenter() {
        pane = new StackPane();
        pane.setMinSize(UIConstants.APP_WIDTH, UIConstants.INFO_CENTER_HEIGHT);
        pane.setTranslateX((float) UIConstants.APP_WIDTH / 2); // place in the middle on the pane
        pane.setTranslateY((float) UIConstants.INFO_CENTER_HEIGHT / 2);

        // the message to be displayed in the info center
        message = new Label("Tic-Tac-Toe");
        message.setMinSize(UIConstants.APP_WIDTH, UIConstants.INFO_CENTER_HEIGHT);
        message.setFont(Font.font(24));
        message.setAlignment(Pos.CENTER);


        // add the message to the pane
        pane.getChildren().add(message);
    }

    /**
    * Getter method to access stack pane
    */
    public StackPane getStackPane() {
        return pane;
    }

    /**
    * setter method to set message to announce game winner or tie decision
    */
    public void setInfoCenterMessage(String message) {
        this.message.setText(message);
    }
}
