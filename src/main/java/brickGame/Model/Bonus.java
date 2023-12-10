package brickGame.Model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents a bonus item in the brick game.
 * This class manages the properties of a bonus item, including its position, type, appearance, and state (taken or not).
 */
public class Bonus implements Serializable {
    private Rectangle bonusRectangle;
    private double x;
    private double y;
    long timeCreated;
    private boolean taken = false;
    private final int type;
    Paint imagePattern;

    /**
     * Constructs a new Bonus object at a specific position based on the row and column, with a specified type.
     *
     * @param row The row position of the bonus in the grid.
     * @param column The column position of the bonus in the grid.
     * @param type The type of the bonus, determining its appearance and behavior.
     */
    public Bonus(int row, int column, int type) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + ((double) Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + ((double) Block.getHeight() / 2) - 15;

        this.type = type;

        draw();
    }

    /**
     * Initializes and draws the bonus item on the screen.
     * Sets up the graphical representation of the bonus based on its type.
     */
    private void draw() {
        bonusRectangle = new Rectangle();
        bonusRectangle.setWidth(30);
        bonusRectangle.setHeight(30);
        bonusRectangle.setX(x);
        bonusRectangle.setY(y);

        if (type == Block.BLOCK_CHOCO) {
            fillChocoPattern();
        } else if (type == Block.BLOCK_MYSTERY) {
            fillMysteryPattern();
        }

        bonusRectangle.setFill(imagePattern);
    }
    /**
     * Sets the pattern for a choco bonus type.
     * Chooses and applies an image pattern for the choco bonus using random.
     */
    private void fillChocoPattern() {
        String url = (new Random().nextInt(20) % 2 == 0) ? "bonus1.png" : "bonus2.png";
        imagePattern = new ImagePattern(new Image(url));
    }

    /**
     * Sets the pattern for a mystery bonus type.
     * Applies a specific image pattern for the mystery bonus.
     */
    private void fillMysteryPattern() {
        String url = "mysteryBonus.jpg";
        imagePattern = new ImagePattern(new Image(url));
    }

    /**
     * Retrieves the rectangle shape representing the bonus item.
     *
     * @return The Rectangle object representing the bonus.
     */
    public Rectangle getBonus() {
        return bonusRectangle;
    }

    /**
     * Gets the current x-coordinate of the bonus item.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the current y-coordinate of the bonus item.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Retrieves the creation time of the bonus item.
     *
     * @return The time at which the bonus was created.
     */
    public long getTimeCreated() {
        return timeCreated;
    }

    /**
     * Checks if the bonus item has been taken.
     *
     * @return True if the bonus has been taken, false otherwise.
     */
    public boolean isTaken() {
        return taken;
    }

    /**
     * Sets the state of the bonus to taken or not.
     *
     * @param taken True to mark the bonus as taken, false otherwise.
     */
    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    /**
     * Updates the y-coordinate of the bonus item based on the current time.
     * This is typically used to animate the bonus falling down the screen.
     *
     * @param currentTime The current time in the game loop.
     */
    public void updateY(long currentTime) {
        y = y + ((currentTime - timeCreated) / 1000.000) + 1.000;
    }

    /**
     * Retrieves the type of the bonus item.
     *
     * @return The integer representing the type of the bonus.
     */
    public int getType(){
        return type;
    }

}
