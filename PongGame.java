import jServe.Core.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

/**
 * Creates a Breakout like game
 *
 * @author Ammar Malik, Amal Thomas, Daniel Graham, Bailey Cross, Aimen HARIZI
 * @version Spring 2021
 */
public class PongGame implements Runnable, MouseMotionListener, MouseListener, ActionListener{

    //The main JPanel
    private JPanel panel;

    //The buttons to control the game logic
    private JButton startGame;
    private JButton restart;

    //The current active game ball
    private Ball newBall;

    //The planks top left point
    private Point plankLocation = new Point(70, 700);

    //Track if the game is currently running
    private boolean start = false;
    private boolean firstStart = false;
    
    //Used to time the game for scoring
    private Stopwatch timer;
    private int tempTimer;
    private Score score;

    //The current powerup (a Circle obj), and track if one needs to be drawn
    private Circle curPower;
    private boolean makePow = true;
    private int powSize = 60;
    
    @Override
    public void run(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Pong Game");
        frame.setPreferredSize(new Dimension(600, 800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        timer = new Stopwatch();

        try {
            score = new Score();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        panel = new JPanel(new BorderLayout()){
            @Override
            public void paint(Graphics g){
                super.paintComponent(g);

                //If a ball is made, make sure to check if its out of bounds
                if(newBall != null) {
                    //Need to determine when the ball is out of bounds to display this restart button
                    if (newBall.done){
                        newBall.resetBall();
                        start = false;
                        restart.setVisible(true);

                        //Add the score to the list if the ball is out of bounds
                        try {
                            score.addToList(tempTimer);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                    }

                    newBall.paint(g);
                }

                //Draw the leaderboard when the game is not active
                if (!start){
                    g.setColor(Color.BLACK);
                    if (!(firstStart)){
                        g.drawString("Press \"Start Game\" to begin!", getWidth()/2 - g.getFontMetrics().stringWidth("Press Start Game to begin!")/2,getHeight()/2 - g.getFontMetrics().getAscent()/2);
                    }else{
                        g.drawString("Press \"Restart Game\" to begin!", getWidth()/2 - g.getFontMetrics().stringWidth("Press Restart Game to begin!")/2,getHeight()/2 - g.getFontMetrics().getAscent()/2);
                    }
                    g.drawString("Leaderboard: ", getWidth()/2 - g.getFontMetrics().stringWidth("Leaderboard: ")/2,getHeight()/2 - g.getFontMetrics().getAscent()/2 + 25);
                    g.drawString(score.leaderBoard(), getWidth()/2 - g.getFontMetrics().stringWidth(score.leaderBoard())/2,getHeight()/2 - g.getFontMetrics().getAscent()/2 + 50);
                    g.drawString("You are: " + score.setName(), panel.getWidth()/2 - g.getFontMetrics().stringWidth("You are: " + score.setName())/2, g.getFontMetrics().getAscent()/2 + 75);
                }

                //Main game loop
                if(start) {
                    if (!newBall.done) {
                        //Draw the timer
                        g.setColor(Color.BLACK);
                        tempTimer = (int) (timer.elapsed() / 1000000000);
                        g.drawString(tempTimer + " Seconds", 500, 30);

                        //Check if we hit a powerup and need to make a new one
                        if (makePow){
                            //Generate random stuffs
                            Random rand = new Random();
                            int powX = rand.nextInt(500) + 50;
                            int powY = rand.nextInt(500) + 50;
                            int curCol = rand.nextInt(3);
                            Color col;
                            if (curCol == 0){
                                col = Color.RED;
                            }else if (curCol == 1){
                                col = Color.GREEN;
                            }else{
                                col = Color.BLUE;
                            }

                            //Draw the circles aka the powerups
                            curPower = new Circle(powSize, new Point (powX, powY), col);

                            makePow = false;
                        }

                        //Draw the current powerup
                        g.setColor(curPower.getColor());
                        curPower.paint(g);

                        //Check if our current ball hit a powerup
                        //Green = Fast
                        //Red = Slow
                        //Blue = Change ball color randomly
                        if (curPower.contains(new Point (newBall.circleX + newBall.BALL_SIZE, newBall.circleY + newBall.BALL_SIZE))){
                            //Perform current powerup
                            if (curPower.getColor() == Color.GREEN){
                                if (newBall.speedY > 0) newBall.speedY = 3;
                                else newBall.speedY = -3;

                                if (newBall.speedX > 0) newBall.speedX = 3;
                                else newBall.speedX = -3;
                            }else if(curPower.getColor() == Color.RED){
                                if (newBall.speedY > 0) newBall.speedY += 10;
                                else newBall.speedY -= 10;

                                if (newBall.speedX > 0) newBall.speedX += 4;
                                else newBall.speedX -= 4;
                            }else if (curPower.getColor() == Color.BLUE){
                                Random rand = new Random();
                                int t1 = rand.nextInt(256);
                                int t2 = rand.nextInt(256);
                                int t3 = rand.nextInt(256);
                                newBall.curColor = new Color(t1, t2, t3);
                            }
                            //If we hit a power up, we need to make a new one
                            makePow = true;
                        }

                    } else if(newBall.done){
                        //If the ball is done, draw the leaderboards and stop the timer
                        timer.stop();
                        g.drawString(score.printList(), panel.getWidth() / 2, panel.getHeight() / 2);
                    }
                }

                //Draw the plank
                g.setColor(Color.GRAY);
                g.fillRect(plankLocation.x - 40, 700, 80, 20);
            }
        };
        frame.add(panel);

        panel.setBackground(new Color(255, 239, 99));

        //Initialize the start and restart buttons
        startGame = new JButton("Start Game");
        startGame.addActionListener(this);
        startGame.setSize(100, 100);

        restart = new JButton("Restart Game");
        restart.addActionListener(this);
        restart.setSize(100, 100);
        restart.setVisible(false);

        //Make a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setSize(new Dimension(100, 100));
        buttonPanel.add(startGame);
        buttonPanel.add(restart);
        buttonPanel.setBackground(new Color(255, 239, 99));
        //Add the button panel to the main panel
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);

        frame.pack();
        frame.setVisible(true);
    }

    /**
     *
     * @param MouseEvent e
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     *
     * @param MouseEvent e
     */
    @Override
    public void mousePressed(MouseEvent e) {

        makePow = true;
    }

    /**
     * Check if the mouse has been moved on the screen
     *
     * @param MouseEvent e
     * mouse moved moves the plank location for playing
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        //If the game is started, asjust the plank location point
        if(start){
            plankLocation = e.getPoint();
            if (newBall != null) {
                newBall.setPlankLocation(plankLocation);
            }
            panel.repaint();
        }
    }

    /**
     * Check if a button has been pressed on the screen
     *
     * @param e the current button press
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Check which button has been pressed and perform actions accordingly
        if(e.getSource().equals(startGame)){

            //Start a ball and timer
            Point startingPosition = new Point(panel.getWidth() / 2, panel.getHeight() / 2);
            newBall = new Ball(startingPosition, 0, 0, panel);
            timer.start();
            newBall.start();

            //remove the startGame button
            startGame.setVisible(false);
            start = true;
            firstStart = true;
        }else if (e.getSource().equals(restart)){
            //Start a ball and timer
            Point startingPosition = new Point(panel.getWidth() / 2, panel.getHeight() / 2);
            newBall = new Ball(startingPosition, 0, 0, panel);
            timer.start();
            newBall.start();

            makePow = true;

            //remove the restart button
            restart.setVisible(false);
            start = true;
        }
    }

    /**
     *
     * @param MouseEvent e
     * mouse released functionality
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     *
     * @param MouseEvent e
     * mouse entered functionality
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     *
     * @param MouseEvent e
     * mouse dragged functionality
     */
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Main method, start the PongGame class
     *
     * @param command line params
     * 
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new PongGame());
    }
}

class Circle{

    //The size of the current circle 
    private int size;

    //The upper left point of the circle
    private Point upperLeft;

    //The color of the circle
    private Color color;

    public Circle(int size, Point upperLeft, Color color) {
        this.size = size;
        this.upperLeft = upperLeft;
        this.color = color;
    }

    public boolean contains(Point p){
        Point circleCenter =
            new Point(upperLeft.x + size/2, upperLeft.y + size/2);
        return circleCenter.distance(p) <= size/2;
    }

    public void paint(Graphics g){
        g.fillOval(upperLeft.x, upperLeft.y, size, size);
        g.setColor(color);
    }

    public Color getColor(){
        return color;
    }
}
