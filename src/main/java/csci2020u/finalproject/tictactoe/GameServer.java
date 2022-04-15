/**
 * @author Japnit Ahuja
 * @author Aanisha Newaz
 * @author Chioma Okechukwu
 * @author Jessica Patel
 *
 * @version 1.0
 */

package csci2020u.finalproject.tictactoe;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

    /**
     * Constructor to make new serverSocket at the specified port
     */
public class GameServer {
    private ServerSocket serverSocket = null; //server to connect clients to the game
    private int numPlayers; //number of players (used to track max players, which is two)

    private ClientHandler player1; //player 1 client
    private ClientHandler player2; //player 2 client

    // constructor to make new serverSocket at the specified port
    public GameServer() {
        System.out.println("----- Game Server ------");
        numPlayers = 0; // initialize num players to zero

        // make an empty tile
        try {
            serverSocket = new ServerSocket(55444);
        } catch (IOException ex) {
            System.out.println("Exception from GameServer constructor");
            ex.printStackTrace();
        }
    }

    /**
     *  Method to encapsulate the instructions for the server waiting for connections.
     *
     *  This method updates the user in the terminal when the first and second player joins.
     *      After the second player joins, the game starts when one clicks the button
     */
    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");

            // first player is X (not circle)
            boolean thisPlayerIsCircle = false;

            // ONLY ACCEPT TWO PLAYERS FOR NOW
            while (numPlayers < 2) {
                Socket socket = serverSocket.accept();
                numPlayers++;
                System.out.println("Player #" + numPlayers + " connected");

                // use a thread to handle each connection
                ClientHandler clientHandler = new ClientHandler(socket, thisPlayerIsCircle);
                if (numPlayers == 1) {
                    player1 = clientHandler;
                } else {
                    player2 = clientHandler;
                }

                thisPlayerIsCircle = true;

                Thread t = new Thread(clientHandler);
                t.start();
            }
            System.out.println("Max number of players connected. No longer accepting requests");
        } catch (IOException e) {
            System.out.println("GameServer; acceptConnections()");
            e.printStackTrace();
        }
    }

    /**
     *  This class uses threads to concurrently let two players play.
     *
     *  The class is used to add instructions to run on each thread.
     *
     */
    private class ClientHandler implements Runnable {
        // the client socket
        private Socket clientSocket = null;

        // input and output streams
        private DataInputStream dis = null;
        private DataOutputStream dos = null;
        private boolean playerIsCircle;

        // CONSTRUCTOR: receive the client socket, get input and output stream, assign the playerID
        public ClientHandler(Socket clientSocket, boolean playerIsCircle) {
            this.clientSocket = clientSocket;
            this.playerIsCircle = playerIsCircle;

            try {
                dis = new DataInputStream(clientSocket.getInputStream());
                dos = new DataOutputStream(clientSocket.getOutputStream());
            } catch (IOException e) {
                System.out.println("Exception from ClientHandler constructor");
                e.printStackTrace();
            }
        }

         /**
         * Method to run the game by letting players take turns
         *
         * This method keeps track on players' moves, who won or if the game is a tie.
         */

        @Override
        public void run() {
            // send to the player: whether they are circle or not
            try {
                dos.writeBoolean(playerIsCircle);

                // keep reading the player's move
                while (true) {
                    int move = dis.readInt();
                    int row = dis.readInt();
                    int col = dis.readInt();
                    boolean winner = dis.readBoolean();
                    boolean tie = dis.readBoolean();
                    System.out.println(winner);

                    //if it is the first player's turn(X)
                    if (!playerIsCircle) {
                        if(winner){ //if they win
                            System.out.println("Player 1 won");
                        }
                        else if (tie){ //if the game is a tie
                            System.out.println("Tie");
                        }
                        else{
                            System.out.println("Player #1 placed a mark on tile " + move);
                        }
                        //send the player's move and update into the method
                        player2.sendPlayerMove(move, row, col,winner,tie);


                    } else {//if it is the second player's turn
                        if(winner){
                            System.out.println("Player 2 won");
                        }
                        else if (tie){
                            System.out.println("Tie");
                        }
                        else{
                            //if the game doesn't end, update their move
                            System.out.println("Player #2 placed a mark on tile " + move);
                        }
                        //send player 2's move and updates to the method for player 1
                        player1.sendPlayerMove(move, row, col,winner,tie);

                    }
                }
            } catch (IOException e) {
                System.out.println("Exception from ClientHandler run()");
                e.printStackTrace();
            }
        }

        /**
         * Method to send any player's move to the players
         * @param pos           Player's tile position
         * @param row           Row placement of tile placement on board
         * @param col           Column placement of the tile on the board
         * @param won           Update if current player won
         * @param tie           Update if game is a tie
         */
        public void sendPlayerMove(int pos, int row, int col, boolean won, boolean tie) {
            try {
                dos.writeInt(pos);
                dos.writeInt(row);
                dos.writeInt(col);
                dos.writeBoolean(won);
                dos.writeBoolean(tie);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     *  Main method to run the server to start the game
     * @param args
     */
    public static void main(String[] args) {
        GameServer gg = new GameServer();
        gg.acceptConnections();
    }
}
