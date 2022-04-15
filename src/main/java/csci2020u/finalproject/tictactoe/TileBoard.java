/**
 * @author Japnit Ahuja
 * @author Aanisha Newaz
 * @author Chioma Okechukwu
 * @author Jessica Patel
 *
 * @version 1.0
 */

package csci2020u.finalproject.tictactoe;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Arrays;

public class TileBoard {
    // use StackPane for the tile board
    private StackPane pane;

    // array of Tiles
    public Tile[][] tiles = new Tile[3][3];

    // the player this tile board belongs to
    private Player thisPlayer;

    /**
     * Class constructor for TileBoard class to add tiles onto game board
     *
     * @param player            Current player for new Client Window
     */
    public TileBoard(Player player) {
        thisPlayer = player;

        pane = new StackPane();
        // set height and width using UI constants, and place in the middle
        pane.setMinSize(UIConstants.APP_WIDTH, UIConstants.TILE_BOARD_HEIGHT);
        pane.setTranslateX((float) UIConstants.APP_WIDTH / 2);
        pane.setTranslateY((float) UIConstants.APP_HEIGHT / 2 + UIConstants.INFO_CENTER_HEIGHT);

        addAllTiles(); // initialize an empty tile board
    }

    /**
     * Method to add empty tiles to the tile board
     */
    private void addAllTiles() {
        int pos = 0; // update the position of each tile
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Tile tile = new Tile(pos, row, col);

                pos++;
                // position each tile according to its index in the array
                tile.getTilePane().setTranslateX((col * 100) - 100);
                tile.getTilePane().setTranslateY((row * 100) - 100);

                // add the stack pane of the tile to the tile board
                pane.getChildren().add(tile.getTilePane());

                // add this new tile to the tile array
                tiles[row][col] = tile;
            }
        }
    }

    /**
     * Getter method to access stackPane
     * @return pane
     */
    public StackPane getStackPane() {
        return pane;
    }

    /**
     * Method to update tile board after each turn with X and O symbols
     * @param row           row of new mark on the board
     * @param col           column of new mark on the board
     * @param mark          mark to be placed on the board
     */
    public void updateTileBoard(int row, int col, String mark) {
        tiles[row][col].setTileValue(mark);
    }

    /**
     *  Inner-class for each tile on the tile board to update on both player window after each turn
     */
    public class Tile {
        // pane layout for each tile
        private StackPane tilePane;
        // the label on each tile is the "X" or "O"
        private Label tileLabel;
        // the position of this tile on the board
        private int position;
        private int row;
        private int col;

        public Tile(int position, int row, int col) {
            tilePane = new StackPane();
            this.position = position;
            this.row = row;
            this.col = col;

            // each tile will be 100x100
            pane.setMinSize(100, 100);

            // add a border
            Rectangle border = new Rectangle();
            border.setHeight(100);
            border.setWidth(100);
            border.setStroke(Color.BLACK);

            // give the tile a background
            border.setFill(Color.WHITESMOKE);

            // initialize label
            tileLabel = new Label("");
            tileLabel.setAlignment(Pos.CENTER);
            tileLabel.setFont(Font.font(24));

            // add border and label to the pane layout
            tilePane.getChildren().addAll(border, tileLabel);

            // update the value of this tile when it is clicked
            tilePane.setOnMouseClicked(event -> {
                System.out.println("Change to X or O if allowed");

                if (thisPlayer.myTurn && thisPlayer.gameRunning) {
                    //place an X or 0 based on the player's turn to keep track of mark
                    if (thisPlayer.markedSpaces[position] == null) {
                        thisPlayer.markedSpaces[position] = thisPlayer.myMark;
                    }
                    setTileValue(thisPlayer.myMark);
                    System.out.println(Arrays.toString(thisPlayer.markedSpaces));

                    // after making a move, it is no longer my turn
                    thisPlayer.myTurn = false;

                    // send to the server
                    thisPlayer.sendMyMove(position, row, col,thisPlayer.checkForWin(), thisPlayer.checkTie());
                }


                // right after you play, wait for your turn
                // run this in a new thread so that the UI loads
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        thisPlayer.updatePlayerTurn();
                    }
                });
                t.start();
            });
        }

        /**
         * Getter method for tile pane
         * @return tilePane         get specific tile pane
         */
        public StackPane getTilePane() {
            return tilePane;
        }

        /**
         * Getter method to access position of move on tile
         * @return position         Position of mark
         */
        public int getPosition() {
            return position;
        }

        /**
         * Getter method of tile value
         * @return tileLabel.getText()
         */
        public String getTileValue() {
            return tileLabel.getText();
        }

        /** setter method to set tile value
         * 
         * @param value         
         */
        public void setTileValue(String value) {
            tileLabel.setText(value);
        }
    }
}
