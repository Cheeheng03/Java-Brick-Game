package brickGame;

public class Ball {
    private double x;
    private double y;
    private final int radius;
    private double velocityX;
    private double velocityY;
    private boolean goingDown;
    private boolean goingRight;

    public Ball(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        this.radius = 10;
        this.velocityX = 1.0;
        this.velocityY = 1.0;
        this.goingDown = true;
        this.goingRight = true;
    }

    public void updatePosition() {
        this.x += goingRight ? velocityX : -velocityX;
        this.y += goingDown ? velocityY : -velocityY;
    }

    public void bounceUp() {
        this.goingDown = false;
    }

    public void bounceDown() {
        this.goingDown = true;
    }

    public void bounceLeft() {
        this.goingRight = false;
    }

    public void bounceRight() {
        this.goingRight = true;
    }

    public void bounceVertically() {
        this.goingDown = !this.goingDown;
    }

    public void bounceHorizontally() {
        this.goingRight = !this.goingRight;
    }

    // Getters and setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isGoingDown() {
        return goingDown;
    }

    public boolean isGoingRight() {
        return goingRight;
    }

    public boolean isGoingLeft() {
        return !goingRight;
    }

    public boolean isGoingUp() {
        return !goingDown;
    }

    public void setGoingDown(boolean goingDown) {
        this.goingDown = goingDown;
    }

    public void setGoingRight(boolean goingRight) {
        this.goingRight = goingRight;
    }
}
