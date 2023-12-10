package brickGame.Model;

/**
 * Represents the ball in the brick game.
 * This class manages the ball's position, velocity, direction, and behavior when bouncing off surfaces.
 */
public class Ball {
    private double x;
    private double y;
    private final int radius;
    private double velocityX;
    private double velocityY;
    private boolean goingDown;
    private boolean goingRight;
    private static final float angleAdjustment = 0.3f;

    /**
     * Constructs a new Ball object with a specified start position.
     *
     * @param startX The initial x-coordinate of the ball.
     * @param startY The initial y-coordinate of the ball.
     */
    public Ball(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        this.radius = 10;
        this.velocityX = 1.0;
        this.velocityY = 1.0;
        this.goingDown = true;
        this.goingRight = true;
    }

    /**
     * Updates the position of the ball based on its current velocity and direction.
     */
    public void updatePosition() {
        this.x += goingRight ? velocityX : -velocityX;
        this.y += goingDown ? velocityY : -velocityY;
    }

    /**
     * Adjusts the vertical angle of the ball to change its trajectory.
     */
    public void adjustVerticalAngle() {
        this.velocityY += angleAdjustment;
    }

    /**
     * Changes the ball's vertical direction upwards.
     */
    public void bounceUp() {
        this.goingDown = false;
    }

    /**
     * Changes the ball's vertical direction downwards.
     */
    public void bounceDown() {
        this.goingDown = true;
    }

    /**
     * Changes the ball's horizontal direction to the left.
     */
    public void bounceLeft() {
        this.goingRight = false;
    }

    /**
     * Changes the ball's horizontal direction to the left.
     */
    public void bounceRight() {
        this.goingRight = true;
    }

    /**
     * Reverses the ball's vertical direction.
     */
    public void bounceVertically() {
        this.goingDown = !this.goingDown;
    }

    /**
     * Reverses the ball's horizontal direction.
     */
    public void bounceHorizontally() {
        this.goingRight = !this.goingRight;
    }

    /**
     * Sets the vertical direction of the ball.
     *
     * @param goingDown True if the ball should move downwards, false otherwise.
     */
    public void setGoingDown(boolean goingDown){
        this.goingDown = goingDown;
    }

    /**
     * Sets the horizontal direction of the ball.
     *
     * @param goingRight True if the ball should move to the right, false otherwise.
     */
    public void setGoingRight(boolean goingRight){
        this.goingRight = goingRight;
    }

    // Getters and setters
    /**
     * Gets the x-coordinate of the ball.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the ball.
     *
     * @param x The new x-coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the ball.
     *
     * @return The y    -coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the ball.
     *
     * @param y The new x-coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Retrieves the radius of the ball.
     *
     * @return The radius of the ball.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Gets the current horizontal velocity of the ball.
     *
     * @return The horizontal velocity.
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * Sets the horizontal velocity of the ball.
     *
     * @param velocityX The new horizontal velocity.
     */
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    /**
     * Gets the current vertical velocity of the ball.
     *
     * @return The vertical velocity.
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Sets the vertical velocity of the ball.
     *
     * @param velocityY The new vertical velocity.
     */
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    /**
     * Checks if the ball is moving downwards.
     *
     * @return True if the ball is moving down, false otherwise.
     */
    public boolean isGoingDown() {
        return goingDown;
    }

    /**
     * Checks if the ball is moving to the right.
     *
     * @return True if the ball is moving right, false otherwise.
     */
    public boolean isGoingRight() {
        return goingRight;
    }

    /**
     * Checks if the ball is moving to the left.
     *
     * @return True if the ball is moving left, false otherwise.
     */
    public boolean isGoingLeft() {
        return !goingRight;
    }

    /**
     * Checks if the ball is moving upwards.
     *
     * @return True if the ball is moving up, false otherwise.
     */
    public boolean isGoingUp() {
        return !goingDown;
    }

}
