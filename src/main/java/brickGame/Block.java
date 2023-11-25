package brickGame;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.Random;

public class Block implements Serializable {
    private static Block block = new Block(-1, -1, 99);

    public int row;
    public int column;


    public boolean isDestroyed = false;

    private Color color;
    public int type;

    public int x;
    public int y;

    private int width = 100;
    private int height = 30;
    private int paddingTop = height * 2;
    private int paddingH = 50;
    public Rectangle rect;


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


    public Block(int row, int column, int type) {
        this.row = row;
        this.column = column;
        this.type = type;

        draw();
    }

    private void draw() {
        //int paddingBetweenBlocks = 4; // This value can be changed to whatever suits your needs


        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width); // Width of the block remains the same
        rect.setHeight(height); // Height of the block remains the same
        rect.setX(x); // Set the X position including the horizontal padding
        rect.setY(y);

        if (type == BLOCK_CHOCO) {
            Image image = new Image("choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_FREEZE) {
            Image image = new Image("freeze.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            int imageIndex = new Random().nextInt(3);
            String imageName = "brick" + (imageIndex + 1) + ".jpg";
            Image image = new Image(imageName);
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        }

    }


    public int checkHitToBlock(double xBall, double yBall, double xBallPrevious, double yBallPrevious, double ballRadius) {
        final double Epsilon = 0.00001; // Define an epsilon value for floating-point comparison

        if (isDestroyed) {
            return NO_HIT;
        }

        // Calculate the ball's path as a rectangle for AABB collision detection using epsilon for precision
        double left = Math.min(xBall - ballRadius, xBallPrevious - ballRadius);
        double right = Math.max(xBall + ballRadius, xBallPrevious + ballRadius);
        double top = Math.min(yBall - ballRadius, yBallPrevious - ballRadius);
        double bottom = Math.max(yBall + ballRadius, yBallPrevious + ballRadius);

        // Adjust the comparison to include epsilon
        if (right + Epsilon < x || left - Epsilon > x + width || bottom + Epsilon < y || top - Epsilon > y + height) {
            // No intersection with the block, considering epsilon
            return NO_HIT;
        }

        if (Math.abs(xBall - x) <= ballRadius + Epsilon && Math.abs(yBall - y) <= ballRadius + Epsilon) {
            //System.out.println("Hit Top Left Corner");
            return HIT_TOP_LEFT;
        } else if (Math.abs(xBall - (x + width)) <= ballRadius + Epsilon && Math.abs(yBall - y) <= ballRadius + Epsilon) {
            //System.out.println("Hit Top Right Corner");
            return HIT_TOP_RIGHT;
        } else if (Math.abs(xBall - x) <= ballRadius + Epsilon && Math.abs(yBall - (y + height)) <= ballRadius + Epsilon) {
            //System.out.println("Hit Bottom Left Corner");
            return HIT_BOTTOM_LEFT;
        } else if (Math.abs(xBall - (x + width)) <= ballRadius + Epsilon && Math.abs(yBall - (y + height)) <= ballRadius + Epsilon) {
            //System.out.println("Hit Bottom Right Corner");
            return HIT_BOTTOM_RIGHT;
        }

        // Further checks with epsilon comparison
        // Check if the ball's current position intersects with the block's margins considering epsilon
        if (xBall + ballRadius + Epsilon >= x && xBall - ballRadius - Epsilon <= x + width &&
                yBall + ballRadius + Epsilon >= y && yBall - ballRadius - Epsilon <= y + height) {
            // Compute displacements with epsilon in mind to handle edge cases
            double dx = Math.min(Math.abs(xBall - x), Math.abs(xBall - (x + width)));
            double dy = Math.min(Math.abs(yBall - y), Math.abs(yBall - (y + height)));

            if (dx + Epsilon < dy) {
                if (xBall < x + (double) width / 2) {
                    //System.out.println("Hit Left");
                    return HIT_LEFT;
                } else {
                    //System.out.println("Hit Right");
                    return HIT_RIGHT;
                }
            } else if (dx > dy + Epsilon) {
                if (yBall < y + (double) height / 2) {
                   // System.out.println("Hit Top");
                    return HIT_TOP;
                } else {
                    //System.out.println("Hit Bottom");
                    return HIT_BOTTOM;
                }
            }
        } else {
            // Previous position comparisons considering epsilon to detect edge crossing
            if (xBallPrevious + ballRadius + Epsilon < x && xBall >= x) {
                //System.out.println("HIT LEFT SIDE VIA PREVIOUS");
                return HIT_LEFT;
            } else if (xBallPrevious - ballRadius - Epsilon > x + width && xBall <= x + width) {
                //System.out.println("HIT RIGHT SIDE VIA PREVIOUS");
                return HIT_RIGHT;
            } else if (yBallPrevious + ballRadius + Epsilon < y && yBall >= y) {
                //System.out.println("HIT TOP SIDE VIA PREVIOUS");
                return HIT_TOP;
            } else if (yBallPrevious - ballRadius - Epsilon > y + height && yBall <= y + height) {
               // System.out.println("HIT BOTTOM SIDE VIA PREVIOUS");
                return HIT_BOTTOM;
            }
        }

        return NO_HIT; // If none of the conditions met, there is no hit
    }

    public static int getPaddingTop() {
        return block.paddingTop;
    }

    public static int getPaddingH() {
        return block.paddingH;
    }

    public static int getHeight() {
        return block.height;
    }

    public static int getWidth() {
        return block.width;
    }

}
