import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Creates a ball for the pong game
 *
 * @author Ammar Malik, Amal Thomas, Daniel Graham, Bailey Cross, Aimen HARIZI
 * @version Spring 2021
 */
public class Ball extends Thread{

    //Constants for ball objects
    public final int BALL_SIZE = 30;
    public final int DELAY_TIME = 33;
    public static final double ALMOST_STOPPED = 0.4;
    public static final double GRAVITY = 0.35;
    public static final double DAMPING = 1.02;

    //Speeds of the current ball
    public double speedX, speedY;

    //Top left points of the circle
    public int circleX, circleY;

    //The max x and y value for the current container
    private int xMax, yMax;

    //See if the current ball no longer needs to be drawn
    public boolean done;

    //The current container
    private JComponent container;

    //Check if the ball hit the plank
    private boolean bouncedPlank;

    //The top left point of the plank
    private Point plankLoc;

    //Check if the game is currently playing
    private boolean gameStarted = false;

    //The max speed for the ball
    private double maxSpeedY = 35;
    private double maxSpeedX = 25;

    //Used to get the current color of the game ball
    public Color curColor = Color.BLACK;

    /**
     * public constructor for the Ball class
     * 
     * @param drawLocation, double speedX, double speedY, JComponent container
     * @param speedX the x speed of the ball
     * @param speedY the y speed of the ball
     * @param container the panel of which the ball is being drawn
     */
    public Ball(Point drawLocation, double speedX, double speedY, JComponent container) {
        this.speedX = speedX;
        this.speedY = speedY;
        circleX = drawLocation.x - BALL_SIZE / 2;
        circleY = drawLocation.y - BALL_SIZE / 2;
        this.yMax = container.getHeight() - BALL_SIZE;
        this.xMax = container.getWidth() - BALL_SIZE;
        this.container = container;
    }

    /**
     * Paint method for the Ball class, draw the ball
     * 
     * @param Graphics g
     */
    public void paint(Graphics g) {
        //Draw the current game ball
        g.setColor(curColor);
        g.fillOval((int) circleX, (int) circleY, BALL_SIZE, BALL_SIZE);
    }

    /**
     * Run method for the Ball class, the main logic
     */
    @Override
    public void run() {

        //Check if the ball is done running
        if(!done){
            while (!done) {

                try {
                    sleep(DELAY_TIME);
                } catch (InterruptedException e) {
                }

                //Check if the speeds are too fast
                if (speedX > maxSpeedX){
                    speedX = maxSpeedX;
                }
                if (speedY > maxSpeedY){
                    speedY = maxSpeedY;
                }

                //Update the current position of the ball
                circleX += speedX;
                circleY += speedY;

                boolean bounced = false;

                //Used to give the ball a random xSpeed
                Random xSpeed = new Random();
                double test;
                
                //Check how the ball bounced, update its velocity accordingly
                if (circleX < 0) {
                    circleX = 0;
                    bounced = true;
                    speedX = -speedX;
                }

                if (circleX > xMax) {
                    circleX = xMax;
                    bounced = true;
                    speedX = -speedX;
                }

                if (circleY < 0) {
                    circleY = 0;
                    bounced = true;
                    speedY = -speedY;
                }

                if (circleY > yMax) {
                    circleY = yMax;
                    bounced = true;
                    speedY = -speedY;
                }

                //Check if the ball hit the plank.
                //If it did, make it somewhat randomly change velocity
                if (gameStarted) {
                    if (Collision.circleOverlapsRectangle(circleX + BALL_SIZE / 2, circleY + BALL_SIZE / 2, BALL_SIZE / 2, plankLoc.x - 40, 690, 80, 20)) {
                        bounced = true;
                        speedY = -Math.abs(speedY);
                        test = (double) (xSpeed.nextInt(10) - 5);
                        speedX = test;
                    }

                }
                
                //If we bounced, update the speed aswell
                if (bounced) {
                    speedY *= DAMPING;
                    speedX *= DAMPING;
                }

                speedY += GRAVITY;

                container.repaint();
                done();
            }
        }
    }

    /**
     * Check if the ball should stop running
     * 
     * @return boolean true or false, based on whether or not the Ball's y
     *  plus the Ball's size divided by 2 is greater than or equal to 750
     */
    public boolean done() {
        if((circleY + BALL_SIZE / 2) >= 750) {
            done = true;
        } else {
            done = false;
        }
        return done;
    }

    /**
     * Sets the current plank location for the Ball class to use
     * 
     * @param Point plankLocation
     */
    public void setPlankLocation(Point plankLocation){
        gameStarted = true;
        plankLoc = new Point(plankLocation.x, 700);
    }

    /**
     * Resets the Ball's done variable to false
     */
    public void resetBall(){
        done = false;
    }

}
