package brickGame.View;

import brickGame.Model.Block;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Random;

public class BlockView {
    private Block block;
    private Rectangle rect;
    private Text blockText;
    private int width = 100;
    private int height = 30;
    private int paddingTop = height * 2;
    private int paddingH = 50;

    public BlockView(Block block) {
        this.block = block;
        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);

        draw();
    }

    private void draw() {
        int x = (block.column * width) + paddingH;
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

    private void setImagePattern(String imageName) {
        Image image = new Image(imageName);
        ImagePattern pattern = new ImagePattern(image);
        rect.setFill(pattern);
    }

    private Text createBlockText(String text) {
        blockText = new Text(text);
        blockText.setX(rect.getX() + width / 2 - blockText.getLayoutBounds().getWidth() / 2);
        blockText.setY(rect.getY() + height / 2 + blockText.getLayoutBounds().getHeight() / 4);
        blockText.setFill(Color.WHITE);
        return blockText;
    }

    private String getRandomBrickImage() {
        int imageIndex = new Random().nextInt(3);
        return "brick" + (imageIndex + 1) + ".jpg";
    }

    public Rectangle getRect() {
        return rect;
    }

    public Text getBlockText() {
        return blockText;
    }
}

