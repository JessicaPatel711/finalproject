package com.example.finalproject;

// TODO: STILL IMPLEMENTING GAME WINNER LOGIC
// TODO: IMPLEMENT THE LOGIC FOR TWO PLAYERS
// TODO: 1:00:07 timestamp for drawing the winning line

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

// TODO: may or may not implement the InfoCenter as that would involve passing data from client to server and updating the message, etc.
public class Player extends Application {
    // NETWORKING ELEMENTS
    private ClientSideConnection csc;

    // GAMEPLAY ELEMENTS
    private boolean playerIsCircle;
    public String myMark; // will be either "X" or "O"
    public boolean myTurn;
    public boolean gameRunning = true;
    public String[] markedSpaces = new String[9]; // array to keep track of where this player has placed a mark
    //numbered positions that declare 'win' if a player covers all three positions from any of the eight sets
    private int[][] winLocations = new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6},
            {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    private int firstWinLoc; // location of the first symbol of the winning row/col/diagonal
    private int lastWinLoc; // location of the last symbol of the winning row/col/diagonal


    // UI ELEMENTS
    private TileBoard tileBoard;
    private InfoCenter infoCenter;

    @Override
    public void start(Stage stage) throws IOException {
        // connect to server at the start of the program
        connectToServer();
        // once this player has successfully connected, start the game
        setMyMark();

        // layout for the main menu
        BorderPane mainRoot = new BorderPane();
        // mainRoot.setBackground(new Background(new BackgroundFill(Color.CHOCOLATE)));

        // start game button
        Button startBtn = new Button("Start New Game!!");
        startBtn.setEffect(new DropShadow());
        startBtn.setStyle("-fx-font-size:20");
        mainRoot.setCenter(startBtn);

        // layout for the game
        BorderPane gameRoot = new BorderPane();
        initLayout(gameRoot);

        // SCENE THAT SHOWS THE MAIN MENU
        Scene main = new Scene(mainRoot, UIConstants.APP_WIDTH, UIConstants.APP_HEIGHT);

        // NEW SCENE THAT SHOWS THE GAME
        Scene game = new Scene(gameRoot, UIConstants.APP_WIDTH, UIConstants.APP_HEIGHT);

        // set the window title based on the player
        String windowTitle;
        if (!playerIsCircle) {
            windowTitle = "Player #1: Play as X";
        } else {
            windowTitle = "Player #2: Play as O";
        }

        stage.setTitle("Hello! " + windowTitle);
        stage.setScene(main);
        stage.show();

        // show the game scene when button clicked
        startBtn.setOnAction(event -> {
            stage.setScene(game);
        });
//        startGame(); // TODO: may or may not need this
    }

//    // start the game logic
//    private void startGame() {
//        setTileOnAction();
//    }

    /**
     * add the tile board and info center to the root
     */
    private void initLayout(BorderPane root) {
        initTileBoard(root);
        initInfoCenter(root);
    }

    /**
     * initialize the info center
     */
    private void initInfoCenter(BorderPane root) {
        infoCenter = new InfoCenter();

        root.getChildren().add(infoCenter.getStackPane());
    }

    // create a new tile board and add to the root layout
    private void initTileBoard(BorderPane root) {
        tileBoard = new TileBoard(this);

        // add to the root layout
        root.getChildren().add(tileBoard.getStackPane());
    }

    // LOGIC FOR THE TILE BOARD GAMEPLAY


    /**
     * Method to check if player is the winner based on their X position
     * if the positions where the enemy is placed is valid for win, the winner variable becomes "true"
     */
    public boolean checkForWin() {
        for (int i = 0; i < this.winLocations.length; ++i) {
            if (!this.playerIsCircle) {
                if (this.markedSpaces[this.winLocations[i][0]] == "X" && this.markedSpaces[this.winLocations[i][1]] == "X" && this.markedSpaces[this.winLocations[i][2]] == "X") {
                    infoCenter.setInfoCenterMessage("Player X Won.");
                    gameRunning = false;
                    return true;

                }
            } else if (this.markedSpaces[this.winLocations[i][0]] == "O" && this.markedSpaces[this.winLocations[i][1]] == "O" && this.markedSpaces[this.winLocations[i][2]] == "O") {
                infoCenter.setInfoCenterMessage("Player O Won.");
                gameRunning = false;
                return true;

            }


        }
        return false;
    }


    /**
     * Method to check for a tie
     */
    public boolean checkTie() {
        for (int i = 0; i < this.markedSpaces.length; ++i) {
            if (this.markedSpaces[i] == null) {

                return false;
            }
        }
        infoCenter.setInfoCenterMessage("Tie!");
        gameRunning = false;
        return true;

    }

    // TODO: review this

    /**
     * update the player turn: wait for the opponent to play, then it is your turn
     */
    public void updatePlayerTurn() {
        System.out.println("UPDAATING");

        receiveEnemyMove();
        myTurn = true; //after opponent has played, return to first player's turn
    }


    // NETWORKING

    /**
     * send the move this player made to the server
     */
    public void sendMyMove(int position, int row, int col, boolean won, boolean tie) {
        try {
            csc.dos.writeInt(position);
            csc.dos.writeInt(row);
            csc.dos.writeInt(col);
            csc.dos.writeBoolean(won);
            csc.dos.writeBoolean(tie);
            csc.dos.flush();
        } catch (IOException e) {
            System.out.println("From sendMyMove");
            e.printStackTrace();
        }
    }

    /**
     * receive the move the opponent made from the server
     */
    public void receiveEnemyMove() {
        System.out.println("Receiving enemy move...");

        try {
            int pos = csc.dis.readInt();
            int row = csc.dis.readInt();
            int col = csc.dis.readInt();
            boolean won = csc.dis.readBoolean();
            boolean tie = csc.dis.readBoolean();

//            Platform.setImplicitExit(false);

            if (!playerIsCircle) {
                // System.out.println("This method was reached!!");
                    // run this in a new thread so the UI loads
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(won) {
                                System.out.println("Player O won.");
                                infoCenter.setInfoCenterMessage("Player O won.");
                                gameRunning = false;
                            }
                            if (tie){
                                System.out.println("Tie!");
                                infoCenter.setInfoCenterMessage("Tie!");
                                gameRunning = false;
                            }
                            tileBoard.tiles[row][col].setTileValue("O");
                        }
                    });

            } else {
                // System.out.println("This method was reached!!");

                    // run this in a new thread so the UI loads
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(won) {
                                System.out.println("Player X won.");
                                infoCenter.setInfoCenterMessage("Player X won.");
                                gameRunning = false;
                            }
                            if (tie){
                                System.out.println("Tie!");
                                infoCenter.setInfoCenterMessage("Tie!");
                                gameRunning = false;
                            }
                            tileBoard.tiles[row][col].setTileValue("X");
                        }
                    });
            }

            // keep a record of the spaces the enemy has filled
            if (!playerIsCircle) {
                if(!won){
                    markedSpaces[pos] = "O";
                }
            } else {
                if(!won){
                    markedSpaces[pos] = "X";
                }
            }

            System.out.println("Other player just placed mark on tile #" + pos);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * helper method to run this client on a new thread than the main thread, so the UI loads concurrently
     */
    private void connectToServer() {
        csc = new ClientSideConnection();

        // startReceivingEnemyMoves();
    }

    /**
     * helper method to set the mark of this player: either "X" or "O"
     */
    private void setMyMark() { // TODO: could make this a start game method

        if (!playerIsCircle) {
            myMark = "X";
            myTurn = true;
        } else {
            myMark = "O";
            myTurn = false;

            // run this in a new thread so that the UI loads
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    updatePlayerTurn();
                }
            });
            t.start();
        }
    }

    /**
     * inner class to encapsulate instructions to allow player communicate with the GameServer
     */
    private class ClientSideConnection extends Thread {
        private Socket socket = null;
        private DataInputStream dis = null;
        private DataOutputStream dos = null;

        // constructor
        public ClientSideConnection() {
            System.out.println("----- Client -----");
            try {
                socket = new Socket("localhost", 22222);
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());

                // read the playerID of this player; sent from the GameServer
                playerIsCircle = dis.readBoolean(); // TODO: send this from GameServer
                if (!playerIsCircle) {
                    System.out.println("Connected to server as player #1, You play as \"X\"");
                } else {
                    System.out.println("Connected to server as player #2, You play as \"O\"");
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("From CSC constructor");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}