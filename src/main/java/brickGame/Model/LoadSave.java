package brickGame.Model;

import java.io.*;
import java.util.ArrayList;

/**
 * Manages the saving and loading of the game state to and from a file.
 * This class serializes and deserializes various game attributes including level, score, game object positions, and statuses.
 */
public class LoadSave {
    public boolean          isExistHeartBlock;
    public boolean          isGoldStauts;
    public boolean          goDownBall;
    public boolean          goRightBall;
    public boolean          colideToBreak;
    public boolean          colideToBreakAndMoveToRight;
    public boolean          colideToRightWall;
    public boolean          colideToLeftWall;
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
    public long             freezeTime;
    public long             ghostTime;
    public boolean          isPaddleChanged;
    public boolean          isFreezeStatus;
    public boolean          isGhostStatus;
    public int              paddleTimeRemaining;
    public int              paddleWidth;
    public double           vX;
    public double           xBallPrevious;
    public double           yBallPrevious;
    public double           vY;
    public static String savePath    = "C:/save/save.mdds";
    public static String savePathDir = "C:/save/";
    public ArrayList<BlockSerializable> blocks = new ArrayList<BlockSerializable>();

    /**
     * Saves the current game state to a file.
     * Serializes various aspects of the game model such as levels, scores, positions of game objects, and more.
     *
     * @param gameModel The GameModel instance whose state is to be saved.
     */
    public void saveGameState(GameModel gameModel) {
        new File(savePathDir).mkdirs();
        File file = new File(savePath);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeInt(gameModel.getLevel());
            outputStream.writeInt(gameModel.getScore());
            outputStream.writeInt(gameModel.getHeart());
            outputStream.writeInt(gameModel.getDestroyedBlockCount());
            outputStream.writeDouble(gameModel.getGameball().getX());
            outputStream.writeDouble(gameModel.getGameball().getY());
            outputStream.writeDouble(gameModel.getXBallPrevious());
            outputStream.writeDouble(gameModel.getYBallPrevious());
            outputStream.writeDouble(gameModel.getPaddle().getX());
            outputStream.writeDouble(gameModel.getPaddle().getY());
            outputStream.writeDouble(gameModel.getPaddle().getCenterBreakX());
            outputStream.writeLong(gameModel.getTime());
            outputStream.writeLong(gameModel.getGoldTime());
            outputStream.writeLong(gameModel.getFreezeTime());
            outputStream.writeLong(gameModel.getGhostTime());
            outputStream.writeBoolean(gameModel.isPaddleWidthChanged());
            outputStream.writeInt(gameModel.getPaddle().getWidth());
            outputStream.writeInt(gameModel.getPaddleTimeRemaining());
            outputStream.writeDouble(gameModel.getGameball().getVelocityX());
            outputStream.writeDouble(gameModel.getGameball().getVelocityY());
            outputStream.writeBoolean(gameModel.getIsExistHeartBlock());
            outputStream.writeBoolean(gameModel.getIsGoldStatus());
            outputStream.writeBoolean(gameModel.getIsFreezeStatus());
            outputStream.writeBoolean(gameModel.getIsGhostStatus());
            outputStream.writeBoolean(gameModel.getGameball().isGoingDown());
            outputStream.writeBoolean(gameModel.getGameball().isGoingRight());
            // Save all collision flags
            saveCollisionFlags(outputStream, gameModel);

            saveBlocks(outputStream, gameModel);

        } catch (IOException e) {
            //handleException(e);
            e.printStackTrace();
        }
    }

    /**
     * Saves the collision flags of the game to the output stream.
     *
     * @param outputStream The ObjectOutputStream to write the collision flags to.
     * @param gameModel The GameModel instance containing the collision flags.
     */
    private void saveCollisionFlags(ObjectOutputStream outputStream, GameModel gameModel) throws IOException {
        outputStream.writeBoolean(gameModel.isColideToBreak());
        outputStream.writeBoolean(gameModel.isColideToBreakAndMoveToRight());
        outputStream.writeBoolean(gameModel.isColideToRightWall());
        outputStream.writeBoolean(gameModel.isColideToLeftWall());
    }

    /**
     * Serializes and saves the blocks' state to the output stream.
     *
     * @param outputStream The ObjectOutputStream to write the blocks' state to.
     * @param gameModel The GameModel instance containing the blocks' information.
     */
    private void saveBlocks(ObjectOutputStream outputStream, GameModel gameModel) throws IOException {
        ArrayList<BlockSerializable> blockSerializables = new ArrayList<>();
        for (Block block : gameModel.getBlocks()) {
            if (block.isDestroyed) {
                continue;
            }
            int countBreakerCount = 0;
            if (block.type == Block.BLOCK_COUNT_BREAKER) {
                countBreakerCount = block.getHitsToDestroy();
            }

            blockSerializables.add(new BlockSerializable(block.row, block.column, block.type, countBreakerCount));

        }
        outputStream.writeObject(blockSerializables);
    }

    /**
     * Reads and loads the game state from a file.
     * Deserializes various attributes of the game such as level, score, positions of game objects, and more.
     */
    public void read() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(savePath)))) {

            this.level = inputStream.readInt();
            this.score = inputStream.readInt();
            this.heart = inputStream.readInt();
            this.destroyedBlockCount = inputStream.readInt();
            this.xBall = inputStream.readDouble();
            this.yBall = inputStream.readDouble();
            this.xBallPrevious = inputStream.readDouble();
            this.yBallPrevious = inputStream.readDouble();
            this.xBreak = inputStream.readDouble();
            this.yBreak = inputStream.readDouble();
            this.centerBreakX = inputStream.readDouble();
            this.time = inputStream.readLong();
            this.goldTime = inputStream.readLong();
            this.freezeTime = inputStream.readLong();
            this.ghostTime = inputStream.readLong();
            this.isPaddleChanged = inputStream.readBoolean();
            this.paddleWidth = inputStream.readInt();
            this.paddleTimeRemaining = inputStream.readInt();
            this.vX = inputStream.readDouble();
            this.vY = inputStream.readDouble();
            this.isExistHeartBlock = inputStream.readBoolean();
            this.isGoldStauts = inputStream.readBoolean();
            this.isFreezeStatus = inputStream.readBoolean();
            this.isGhostStatus = inputStream.readBoolean();
            this.goDownBall = inputStream.readBoolean();
            this.goRightBall = inputStream.readBoolean();
            this.colideToBreak = inputStream.readBoolean();
            this.colideToBreakAndMoveToRight = inputStream.readBoolean();
            this.colideToRightWall = inputStream.readBoolean();
            this.colideToLeftWall = inputStream.readBoolean();

            try {
                this.blocks = (ArrayList<BlockSerializable>) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
