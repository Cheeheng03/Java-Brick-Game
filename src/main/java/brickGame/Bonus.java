package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.Random;

public class Bonus implements Serializable {
    private Rectangle bonusRectangle;
    private double x;
    private double y;
    long timeCreated;
    private boolean taken = false;
    private final int type;
    Paint imagePattern;

    public Bonus(int row, int column, int type) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + ((double) Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + ((double) Block.getHeight() / 2) - 15;

        this.type = type;

        draw();
    }

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

    private void fillChocoPattern() {
        String url = (new Random().nextInt(20) % 2 == 0) ? "bonus1.png" : "bonus2.png";
        imagePattern = new ImagePattern(new Image(url));
    }

    private void fillMysteryPattern() {
        String url = "mysteryBonus.jpg";
        imagePattern = new ImagePattern(new Image(url));
    }

    public Rectangle getBonus() {
        return bonusRectangle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public void updateY(long currentTime) {
        y = y + ((currentTime - timeCreated) / 1000.000) + 1.000;
    }

    public int getType(){
        return type;
    }

}
