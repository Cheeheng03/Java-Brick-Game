package brickGame.View;

import brickGame.Model.Block;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;

import java.util.Random;

public class BlockView {
    private Block block;
    private int width = 100;
    private int height = 30;
    private int paddingTop = height * 2;
    private int paddingH = 50;

    public BlockView(Block block) {
        this.block = block;
        draw(block);
    }

    private void draw(Block block) {
        int x = (block.column * width) + paddingH;
        int y = (block.row * height) + paddingTop;

        block.rect.setWidth(width);
        block.rect.setHeight(height);
        block.rect.setX(x);
        block.rect.setY(y);

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
        block.rect.setFill(pattern);
    }

    private Text createBlockText(String text) {
        block.blockText = new Text(text);
        block.blockText.setX(block.rect.getX() + width / 2 - block.blockText.getLayoutBounds().getWidth() / 2);
        block.blockText.setY(block.rect.getY() + height / 2 + block.blockText.getLayoutBounds().getHeight() / 4);
        block.blockText.setFill(Color.WHITE);
        return block.blockText;
    }

    private String getRandomBrickImage() {
        int imageIndex = new Random().nextInt(3);
        return "brick" + (imageIndex + 1) + ".jpg";
    }
}
