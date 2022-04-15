/**
 * @author Japnit Ahuja
 * @author Aanisha Newaz
 * @author Chioma Okechukwu
 * @author Jessica Patel
 *
 * @version 1.0
 */

package csci2020u.finalproject.tictactoe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
