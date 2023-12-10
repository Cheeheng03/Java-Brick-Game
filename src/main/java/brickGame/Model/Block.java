package brickGame.Model;

import brickGame.View.BlockView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents a block in the brick game, including its position, size, type, and collision properties.
 * Blocks can have different types, influencing their behavior and interaction with the ball.
 */
public class Block implements Serializable {
    private static Block block = new Block(-1, -1, 99, 0);
    public int row;
    public int column;
    public boolean isDestroyed = false;
    public int type;
    public int x;
    public int y;
    private int width = 100;
    private int height = 30;
    private int paddingTop = height * 2;
    private int paddingH = 50;
    public static int NO_HIT = -1;
    public static int HIT_RIGHT = 0;
    public static int HIT_BOTTOM = 1;
    public static int HIT_LEFT = 2;
    public static int HIT_TOP = 3;
    public static int HIT_TOP_LEFT = 4;
    public static int HIT_TOP_RIGHT = 5;
    public static int HIT_BOTTOM_LEFT = 6;
    public static int HIT_BOTTOM_RIGHT = 7;
    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_CHOCO = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;
    public static int BLOCK_FREEZE = 103;
    public static int BLOCK_MYSTERY = 104;
    public static int BLOCK_WALL = 105;
    public static int Block_GHOST= 106;
    public static int BLOCK_COUNT_BREAKER =107;
    private int hitsToDestroy;
    private int currentHits;
    private long lastHitTime = -1;
    private static final long COOLDOWN_TIME = 25;
    public boolean isAlreadyHit = false;
    private boolean blockHitFlagReset = false;
    public Text blockText;
    private BlockView blockView;

    /**
     * Constructs a new Block object with specified parameters.
     *
     * @param row The row position of the block.
     * @param column The column position of the block.
     * @param type The type of the block, determining its behavior.
     * @param hitsToDestroy The number of hits required to destroy the count breaker block.
     */
    public Block(int row, int column, int type, int hitsToDestroy) {
        this.row = row;
        this.column = column;
        this.type = type;

        if (this.type == BLOCK_COUNT_BREAKER) {
            this.hitsToDestroy = hitsToDestroy;
        }

        this.blockView = new BlockView(this);
        this.setPosition();
    }

    /**
     * Sets the position of the block based on its row and column.
     */
    private void setPosition() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;
    }

    /**
     * Resets the hit flag for the block once, if it has been hit.
     * Used to manage the state of the block in consecutive hits.
     */
    public void resetHitFlagOnce() {
        if (isAlreadyHit) {
            isAlreadyHit = false;
            blockHitFlagReset = true;
        } else {
            blockHitFlagReset = false;
        }
    }

    /**
     * Checks if the ball has hit the block and determines the type of collision
     *
     * @param xBall The current x-coordinate of the ball.
     * @param yBall The current y-coordinate of the ball.
     * @param xBallPrevious The previous x-coordinate of the ball.
     * @param yBallPrevious The previous y-coordinate of the ball.
     * @param ballRadius The radius of the ball.
     * @return An integer representing the side of the block hit or NO_HIT if no collision occurred.
     */
    public int checkHitToBlock(double xBall, double yBall, double xBallPrevious, double yBallPrevious, double ballRadius) {
        final double Epsilon = 0.00001;

        if (isDestroyed || isAlreadyHit) {
            System.out.println("NO hit");
            return NO_HIT;
        }

        double left = Math.min(xBall - ballRadius, xBallPrevious - ballRadius);
        double right = Math.max(xBall + ballRadius, xBallPrevious + ballRadius);
        double top = Math.min(yBall - ballRadius, yBallPrevious - ballRadius);
        double bottom = Math.max(yBall + ballRadius, yBallPrevious + ballRadius);

        if (right + Epsilon < x || left - Epsilon > x + width || bottom + Epsilon < y || top - Epsilon > y + height) {
            return NO_HIT;
        }

        if (Math.abs(xBall - x) <= ballRadius + Epsilon && Math.abs(yBall - y) <= ballRadius + Epsilon) {
            return HIT_TOP_LEFT;
        } else if (Math.abs(xBall - (x + width)) <= ballRadius + Epsilon && Math.abs(yBall - y) <= ballRadius + Epsilon) {
            return HIT_TOP_RIGHT;
        } else if (Math.abs(xBall - x) <= ballRadius + Epsilon && Math.abs(yBall - (y + height)) <= ballRadius + Epsilon) {
            return HIT_BOTTOM_LEFT;
        } else if (Math.abs(xBall - (x + width)) <= ballRadius + Epsilon && Math.abs(yBall - (y + height)) <= ballRadius + Epsilon) {
            return HIT_BOTTOM_RIGHT;
        }

        if (xBall + ballRadius + Epsilon >= x && xBall - ballRadius - Epsilon <= x + width &&
                yBall + ballRadius + Epsilon >= y && yBall - ballRadius - Epsilon <= y + height) {
            double dx = Math.min(Math.abs(xBall - x), Math.abs(xBall - (x + width)));
            double dy = Math.min(Math.abs(yBall - y), Math.abs(yBall - (y + height)));

            if (dx + Epsilon < dy) {
                if (xBall < x + (double) width / 2) {
                    return HIT_LEFT;
                } else {
                    return HIT_RIGHT;
                }
            } else if (dx > dy + Epsilon) {
                if (yBall < y + (double) height / 2) {
                    return HIT_TOP;
                } else {
                    return HIT_BOTTOM;
                }
            }
        } else {
            if (xBallPrevious + ballRadius + Epsilon < x && xBall >= x) {
                return HIT_LEFT;
            } else if (xBallPrevious - ballRadius - Epsilon > x + width && xBall <= x + width) {
                return HIT_RIGHT;
            } else if (yBallPrevious + ballRadius + Epsilon < y && yBall >= y) {
                return HIT_TOP;
            } else if (yBallPrevious - ballRadius - Epsilon > y + height && yBall <= y + height) {
                return HIT_BOTTOM;
            }
        }

        return NO_HIT;
    }

    /**
     * Decrements the hit count required to destroy the block.
     * Applicable for blocks that require multiple hits to be destroyed.
     */
    public void decrementCount() {
        if (type == BLOCK_COUNT_BREAKER && hitsToDestroy > 0) {
            hitsToDestroy--;
            blockText.setText("" + hitsToDestroy);
        }
    }

    /**
     * Checks if a hit to the block can be processed based on the current time and cooldown period.
     *
     * @param currentTime The current time in the game loop.
     * @return True if the hit can be processed, false otherwise.
     */
    public boolean checkAndProcessHit(long currentTime) {
        if (this.lastHitTime >= currentTime - COOLDOWN_TIME) {
            return false;
        }
        this.lastHitTime = currentTime;
        return true;
    }

    /**
     * Gets the number of hits required to destroy the block.
     *
     * @return The number of hits to destroy the block.
     */
    public int getHitsToDestroy() {
        return hitsToDestroy;
    }

    /**
     * Returns the top padding of the block.
     *
     * @return The top padding value.
     */
    public static int getPaddingTop() {
        return block.paddingTop;
    }

    /**
     * Returns the horizontal padding of the block.
     *
     * @return The horizontal padding value.
     */
    public static int getPaddingH() {
        return block.paddingH;
    }

    /**
     * Returns the height of the block.
     *
     * @return The height of the block.
     */
    public static int getHeight() {
        return block.height;
    }

    /**
     * Returns the width of the block.
     *
     * @return The width of the block.
     */
    public static int getWidth() {
        return block.width;
    }

    /**
     * Retrieves the view representation of the block.
     *
     * @return The BlockView associated with this block.
     */
    public BlockView getBlockView() {
        return blockView;
    }

}