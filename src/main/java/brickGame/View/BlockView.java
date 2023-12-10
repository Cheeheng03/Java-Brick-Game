package brickGame.View;

import brickGame.Model.Block;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Random;

/**
 * Provides the graphical representation of a block in the brick game.
 * This class is responsible for drawing the block and applying the appropriate visual styles based on its type.
 */
public class BlockView {
    private final Block block;
    private final Rectangle rect;
    private Text blockText;
    private final int width = 100;
    private final int height = 30;

    /**
     * Constructs a new BlockView object for a given block.
     *
     * @param block The Block object to be visually represented.
     */
    public BlockView(Block block) {
        this.block = block;
        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);

        draw();
    }

    /**
     * Draws the block and applies the appropriate image pattern based on the block type.
     * Sets the block's position and size, and chooses the image pattern corresponding to its type.
     */
    private void draw() {
        int paddingH = 50;
        int x = (block.column * width) + paddingH;
        int paddingTop = height * 2;
        int y = (block.row * height) + paddingTop;

        rect.setX(x);
        rect.setY(y);

        if (block.type == Block.BLOCK_CHOCO) {
            setImagePattern("choco.jpg");
        } else if (block.type == Block.BLOCK_HEART) {
            setImagePattern("heart.jpg");
        } else if (block.type == Block.BLOCK_STAR) {
            setImagePattern("star.jpg");
        } else if (block.type == Block.BLOCK_FREEZE) {
            setImagePattern("freeze.jpg");
        } else if (block.type == Block.BLOCK_MYSTERY) {
            setImagePattern("mystery.jpg");
        } else if (block.type == Block.BLOCK_WALL) {
            setImagePattern("bedrock.jpg");
        } else if (block.type == Block.Block_GHOST) {
            setImagePattern("ghost.jpg");
        } else if (block.type == Block.BLOCK_COUNT_BREAKER) {
            block.blockText = createBlockText("" + block.getHitsToDestroy());
            setImagePattern("countBreaker.jpeg");
        } else {
            setImagePattern(getRandomBrickImage());
        }
    }

    /**
     * Applies an image pattern to the block.
     *
     * @param imageName The name of the image file to be used as the pattern for the block.
     */
    private void setImagePattern(String imageName) {
        Image image = new Image(imageName);
        ImagePattern pattern = new ImagePattern(image);
        rect.setFill(pattern);
    }

    /**
     * Creates and positions a text label on the block.
     *
     * @param text The string to be displayed on the block.
     * @return The Text object representing the label.
     */
    private Text createBlockText(String text) {
        blockText = new Text(text);
        blockText.setX(rect.getX() + (double) width / 2 - blockText.getLayoutBounds().getWidth() / 2);
        blockText.setY(rect.getY() + (double) height / 2 + blockText.getLayoutBounds().getHeight() / 4);
        blockText.setFill(Color.WHITE);
        return blockText;
    }

    /**
     * Selects a random image for a normal brick block.
     *
     * @return The file name of the randomly selected brick image.
     */
    private String getRandomBrickImage() {
        int imageIndex = new Random().nextInt(3);
        return "brick" + (imageIndex + 1) + ".jpg";
    }

    /**
     * Retrieves the rectangle shape representing the block.
     *
     * @return The Rectangle object representing the block.
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * Retrieves the text label of the block, if any.
     *
     * @return The Text object representing the block's label, or null if there is no label.
     */
    public Text getBlockText() {
        return blockText;
    }
}

