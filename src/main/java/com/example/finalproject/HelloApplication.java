package com.example.finalproject;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HelloApplication implements Runnable {

    private String ip = "localhost";
    private int port = 22222;
    private Scanner in = new Scanner(System.in);


    private boolean userTurn = false;
    private boolean circle = true;
    private boolean authorized = false;
    private boolean cannotConnect = false;
    private boolean winner = false;
    private boolean enemyIsWinner = false;
    private boolean isTie = false;


    private String waitingString = "Waiting for second player";
    private String cannotConnectString = "Cannot connect with opponent.";
    private String wonString = "Winner!";
    private String enemyIsWinnerString = "You Lose!";
    private String tieString = "Its a tie.";

    private int spaceLength = 160;
    private int errors = 0;
    private int firstSpot = -1;
    private int secondSpot = -1;
    private String[] spaces = new String[9];

    //Winner Positions
    private int[][] wins = new int[][] { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, { 0, 4, 8 }, { 2, 4, 6 } };


    private JFrame frame;
    //IF NEEDED CHANGE THE FRAME
    private int WIDTH = 506;
    private int HEIGHT = 527;
    private Thread thread;

    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private Painter pntr;
    private Socket socket;
    private ServerSocket serverSocket;

    private BufferedImage board;
    private BufferedImage redX;
    private BufferedImage blueX;
    private BufferedImage redCircle;
    private BufferedImage blueCircle;

    //CHIOMA CHANGE THIS
    private Font font = new Font("Verdana", Font.BOLD, 32);
    private Font smallerFont = new Font("Verdana", Font.BOLD, 20);
    private Font largerFont = new Font("Verdana", Font.BOLD, 50);




    public HelloApplication() {
        System.out.println("Please input the IP: ");
        ip = in.nextLine();
        System.out.println("Please input the port: ");
        port = in.nextInt();
        while (port < 1 || port > 65535) {
            System.out.println("Invalid! Input port: ");
            port = in.nextInt();
        }

        loadImages(); //load the images first (loads board)

        pntr = new Painter();
        pntr.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        if (!connect()) readyServer();

        frame = new JFrame();
        frame.setContentPane(pntr);
        frame.setSize(WIDTH, HEIGHT);
        frame.setTitle("TIC TAC TOE");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        thread = new Thread(this, "TIC-TAC-TOE");
        thread.start();
    }

    //CHIOMA CHANGE THE IMAGES
    private void loadImages() {
        try {
            board = ImageIO.read(getClass().getResourceAsStream("/board.png"));
            redX = ImageIO.read(getClass().getResourceAsStream("/redX.png"));
            redCircle = ImageIO.read(getClass().getResourceAsStream("/redCircle.png"));
            blueX = ImageIO.read(getClass().getResourceAsStream("/blueX.png"));
            blueCircle = ImageIO.read(getClass().getResourceAsStream("/blueCircle.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //CHIOMA CHANGE THE GRAPHICS HERE (TRY NOT TO USE THE SAME COLORS LISTED ALREADY)
    private void makeMatch(Graphics g) {
        g.drawImage(board, 0, 0, null);
        if (cannotConnect) {
            g.setColor(Color.RED);
            g.setFont(smallerFont);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int stringWidth = g2.getFontMetrics().stringWidth(cannotConnectString);
            g.drawString(cannotConnectString, WIDTH / 2 - stringWidth / 2, HEIGHT / 2);
            return;
        }

        if (authorized) {
            for (int i = 0; i < spaces.length; i++) {
                if (spaces[i] != null) {
                    if (spaces[i].equals("X")) {
                        if (!circle) {
                            g.drawImage(blueX, (i % 3) * spaceLength + 10 * (i % 3),
                                    (int) (i / 3) * spaceLength + 10 * (int) (i / 3), null);
                        } else {
                            g.drawImage(redX, (i % 3) * spaceLength + 10 * (i % 3),
                                    (int) (i / 3) * spaceLength + 10 * (int) (i / 3), null);
                        }
                    } else if (spaces[i].equals("O")) {
                        if (!circle) {
                            g.drawImage(redCircle, (i % 3) * spaceLength + 10 * (i % 3),
                                    (int) (i / 3) * spaceLength + 10 * (int) (i / 3), null);
                        } else {
                            g.drawImage(blueCircle, (i % 3) * spaceLength + 10 * (i % 3),
                                    (int) (i / 3) * spaceLength + 10 * (int) (i / 3), null);
                        }
                    }
                }
            }
            if (winner || enemyIsWinner) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(10));
                g.setColor(Color.BLACK);
                g.drawLine(firstSpot % 3 * spaceLength + 10 * firstSpot % 3 + spaceLength / 2,
                        (int) (firstSpot / 3) * spaceLength + 10 * (int) (firstSpot / 3) + spaceLength / 2,
                        secondSpot % 3 * spaceLength + 10 * secondSpot % 3 + spaceLength / 2,
                        (int) (secondSpot / 3) * spaceLength + 10 * (int) (secondSpot / 3) + spaceLength / 2);

                g.setColor(Color.RED);
                g.setFont(largerFont);
                if (winner) {
                    int stringWidth = g2.getFontMetrics().stringWidth(wonString);
                    g.drawString(wonString, WIDTH / 2 - stringWidth / 2, HEIGHT / 2);
                } else if (enemyIsWinner) {
                    int stringWidth = g2.getFontMetrics().stringWidth(enemyIsWinnerString);
                    g.drawString(enemyIsWinnerString, WIDTH / 2 - stringWidth / 2, HEIGHT / 2);
                }
            }
            if (isTie) {
                Graphics2D g2 = (Graphics2D) g;
                g.setColor(Color.BLACK);
                g.setFont(largerFont);
                int stringWidth = g2.getFontMetrics().stringWidth(tieString);
                g.drawString(tieString, WIDTH / 2 - stringWidth / 2, HEIGHT / 2);
            }
        } else {
            g.setColor(Color.RED);
            g.setFont(font);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int stringWidth = g2.getFontMetrics().stringWidth(waitingString);
            g.drawString(waitingString, WIDTH / 2 - stringWidth / 2, HEIGHT / 2);
        }

    }

    private void mark() {
        if (errors >= 10) cannotConnect = true;

        if (!userTurn && !cannotConnect) {
            try {
                int space = inputStream.readInt();
                if (circle) spaces[space] = "X";
                else spaces[space] = "O";
                checkForEnemyWin();
                checkTie();
                userTurn = true;
            } catch (IOException e) {
                e.printStackTrace();
                errors+= 1;
            }
        }
    }


    private void checkForEnemyWin() {
        for (int i = 0; i < wins.length; i++) {
            if (!circle) {
                if (spaces[wins[i][0]] == "O" && spaces[wins[i][1]] == "O" && spaces[wins[i][2]] == "O") {
                    firstSpot = wins[i][0];
                    secondSpot = wins[i][2];
                    enemyIsWinner = true;
                }

            } else {
                if (spaces[wins[i][0]] == "X" && spaces[wins[i][1]] == "X" && spaces[wins[i][2]] == "X") {
                    firstSpot = wins[i][0];
                    secondSpot = wins[i][2];
                    enemyIsWinner = true;
                }
            }
        }
    }

    private void checkForWin() {
        for (int i = 0; i < wins.length; i++) {
            if (!circle) {
                if (spaces[wins[i][0]] == "X" && spaces[wins[i][1]] == "X" && spaces[wins[i][2]] == "X") {
                    firstSpot = wins[i][0];
                    secondSpot = wins[i][2];
                    winner = true;
                }

            } else {
                if (spaces[wins[i][0]] == "O" && spaces[wins[i][1]] == "O" && spaces[wins[i][2]] == "O") {
                    firstSpot = wins[i][0];
                    secondSpot = wins[i][2];
                    winner = true;
                }
            }
        }
    }

    private void checkTie() {
        for (int i = 0; i < spaces.length; i++) {
            if (spaces[i] == null) {
                return;
            }
        }
        isTie = true;
    }

    private void auditServerRequest() {
        Socket socket = null;
        try {
            socket = serverSocket.accept();
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            authorized = true;
            System.out.println("Client has joined!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean connect() {
        try {
            socket = new Socket(ip, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            authorized = true;
        } catch (IOException e) {
            System.out.println("Waiting to connect to: " + ip + ":" + port);
            return false;
        }
        System.out.println("Connected to Server!");
        return true;
    }

    public void run() {
        while (true) {
            mark();
            pntr.repaint();

            if (!circle && !authorized) {
                auditServerRequest();
            }

        }
    }

    private void readyServer() {
        try {
            serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
        } catch (Exception e) {
            e.printStackTrace();
        }
        userTurn = true;
        circle = false;
    }


    @SuppressWarnings("unused")




    public static void main(String[] args)
    {
        HelloApplication ticTacToe = new HelloApplication();
    }

    private class Painter extends JPanel implements MouseListener {

        public Painter() {
            setFocusable(true);
            requestFocus();
            setBackground(Color.LIGHT_GRAY);
            addMouseListener(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            makeMatch(g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (authorized == true) {
                if (userTurn && !cannotConnect && !winner && !enemyIsWinner) {
                    int x = e.getX() / spaceLength;
                    int y = e.getY() / spaceLength;
                    y *= 3;
                    int position = x + y;

                    if (spaces[position] == null) {
                        if (!circle) spaces[position] = "X";
                        else spaces[position] = "O";
                        userTurn = false;
                        repaint();
                        //Toolkit.getDefaultToolkit().sync();

                        try {
                            outputStream.writeInt(position);
                            outputStream.flush();
                        } catch (IOException e1) {
                            errors+= 1;
                            e1.printStackTrace();
                        }
                        checkForWin();
                        checkTie();

                    }
                }
            }
        }

        /**
         * Invoked when a mouse button has been pressed on a component.
         *
         * @param e the event to be processed
         */
        @Override
        public void mousePressed(MouseEvent e) {

        }

        /**
         * Invoked when a mouse button has been released on a component.
         *
         * @param e the event to be processed
         */
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        /**
         * Invoked when the mouse enters a component.
         *
         * @param e the event to be processed
         */
        @Override
        public void mouseEntered(MouseEvent e) {

        }

        /**
         * Invoked when the mouse exits a component.
         *
         * @param e the event to be processed
         */
        @Override
        public void mouseExited(MouseEvent e) {

        }


    }

}