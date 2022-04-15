package com.example.finalproject;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.Arrays;


public class TileBoard {
    // use StackPane for the tile board
    private StackPane pane;

    // array of Tiles
    public Tile[][] tiles = new Tile[3][3];

    // the player this tile board belongs to
    private Player thisPlayer;

    public TileBoard(Player player) {
        thisPlayer = player;

        pane = new StackPane();
        // set height and width using UI constants, and place in the middle
        pane.setMinSize(UIConstants.APP_WIDTH, UIConstants.TILE_BOARD_HEIGHT);
        pane.setTranslateX((float) UIConstants.APP_WIDTH / 2);
        pane.setTranslateY((float) UIConstants.APP_HEIGHT / 2 + UIConstants.INFO_CENTER_HEIGHT); // TODO: reposition this if using info center

        addAllTiles(); // initialize an empty tile board
    }

    // add empty tiles to the tile board
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

    // getters
    public StackPane getStackPane() {
        return pane;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void updateTileBoard(int row, int col, String mark) {
        tiles[row][col].setTileValue(mark);
    }

    // inner-class for each tile on the tile board
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
            border.setFill(Color.TRANSPARENT);

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

                    // check for win or tie

//                    thisPlayer.checkTie();
                }


                // right after you play, wait for your turn
                // run this in a new thread so that the UI loads
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        thisPlayer.checkForEnemyWin();
                        thisPlayer.updatePlayerTurn();
                    }
                });
                t.start();
            });
        }

        // getters
        public StackPane getTilePane() {
            return tilePane;
        }

        public int getPosition() {
            return position;
        }

        public String getTileValue() {
            return tileLabel.getText();
        }

        // setters
        public void setTileValue(String value) {
            tileLabel.setText(value);
        }
    }
}