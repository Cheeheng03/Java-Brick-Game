package brickGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class LoadSave {
    public boolean          isExistHeartBlock;
    public boolean          isGoldStauts;
    public boolean          goDownBall;
    public boolean          goRightBall;
    public boolean          colideToBreak;
    public boolean          colideToBreakAndMoveToRight;
    public boolean          colideToRightWall;
    public boolean          colideToLeftWall;
    public boolean          colideToRightBlock;
    public boolean          colideToBottomBlock;
    public boolean          colideToLeftBlock;
    public boolean          colideToTopBlock;
    public boolean          colideToTopLeftBlock;
    public boolean          colideToTopRightBlock;
    public boolean          colideToBottomLeftBlock;
    public boolean          colideToBottomRightBlock;
    public int              level;
    public int              score;
    public int              heart;
    public int              destroyedBlockCount;
    public double           xBall;
    public double           yBall;
    public double           xBreak;
    public double           yBreak;
    public double           centerBreakX;
    public long             time;
    public long             goldTime;
    public double           vX;
    public double           xBallPrevious;
    public double           yBallPrevious;
    public double           vY;
    public ArrayList<BlockSerializable> blocks = new ArrayList<BlockSerializable>();


    public void read() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(Main.savePath)))) {

            level = inputStream.readInt();
            score = inputStream.readInt();
            heart = inputStream.readInt();
            destroyedBlockCount = inputStream.readInt();

            xBall = inputStream.readDouble();
            yBall = inputStream.readDouble();
            xBallPrevious = inputStream.readDouble();
            yBallPrevious = inputStream.readDouble();
            xBreak = inputStream.readDouble();
            yBreak = inputStream.readDouble();
            centerBreakX = inputStream.readDouble();
            time = inputStream.readLong();
            goldTime = inputStream.readLong();
            vX = inputStream.readDouble();
            vY = inputStream.readDouble();

            isExistHeartBlock = inputStream.readBoolean();
            isGoldStauts = inputStream.readBoolean();
            goDownBall = inputStream.readBoolean();
            goRightBall = inputStream.readBoolean();
            colideToBreak = inputStream.readBoolean();
            colideToBreakAndMoveToRight = inputStream.readBoolean();
            colideToRightWall = inputStream.readBoolean();
            colideToLeftWall = inputStream.readBoolean();
            colideToRightBlock = inputStream.readBoolean();
            colideToBottomBlock = inputStream.readBoolean();
            colideToLeftBlock = inputStream.readBoolean();
            colideToTopBlock = inputStream.readBoolean();
            colideToTopLeftBlock = inputStream.readBoolean();
            colideToTopRightBlock = inputStream.readBoolean();
            colideToBottomLeftBlock = inputStream.readBoolean();
            colideToBottomRightBlock = inputStream.readBoolean();

            try {
                blocks = (ArrayList<BlockSerializable>) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
