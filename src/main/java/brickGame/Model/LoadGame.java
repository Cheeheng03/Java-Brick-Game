package brickGame.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LoadGame {
    private GameModel gameModel;

    public LoadGame(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void applyStateToGameModel(LoadSave loadSave) {
        gameModel.setLevel(loadSave.level);
        gameModel.setScore(loadSave.score);
        gameModel.setHeart(loadSave.heart);
        gameModel.setDestroyedBlockCount(loadSave.destroyedBlockCount);

        gameModel.setTime(loadSave.time);
        gameModel.setGoldTime(loadSave.goldTime);
        gameModel.setFreezeTime(loadSave.freezeTime);
        gameModel.setGhostTime(loadSave.ghostTime);

        gameModel.setGoldStatus(loadSave.isGoldStauts);
        gameModel.setIsFreezeStatus(loadSave.isFreezeStatus);
        gameModel.setGhostStatus(loadSave.isGhostStatus);
        gameModel.setPaddleWidthChanged(loadSave.isPaddleChanged);
        gameModel.setPaddleTimeRemaining(loadSave.paddleTimeRemaining);
        gameModel.setIsExistHeartBlock(loadSave.isExistHeartBlock);
        if(loadSave.level == 17){
            gameModel.setInitialBlockCount(loadSave.blocks.size() + loadSave.destroyedBlockCount - 4);
        } else{
            gameModel.setInitialBlockCount(loadSave.blocks.size() + loadSave.destroyedBlockCount);
        }

        // Setting ball state
        Ball gameball = gameModel.getGameball();
        gameball.setX(loadSave.xBall);
        gameball.setY(loadSave.yBall);
        gameball.setVelocityX(loadSave.vX);
        gameball.setVelocityY(loadSave.vY);

        // Setting previous ball position
        gameModel.setXBallPrevious(loadSave.xBallPrevious);
        gameModel.setYBallPrevious(loadSave.yBallPrevious);

        // Setting paddle state
        Paddle paddle = gameModel.getPaddle();
        paddle.setWidth(loadSave.paddleWidth);
        paddle.setX(loadSave.xBreak);
        paddle.setY(loadSave.yBreak);
        paddle.setCenterBreakX(loadSave.centerBreakX);

        // Restoring collision flags
        gameModel.setColideToBreak(loadSave.colideToBreak);
        gameModel.setColideToBreakAndMoveToRight(loadSave.colideToBreakAndMoveToRight);
        gameModel.setColideToRightWall(loadSave.colideToRightWall);
        gameModel.setColideToLeftWall(loadSave.colideToLeftWall);
        gameModel.setColideToRightBlock(loadSave.colideToRightBlock);
        gameModel.setColideToBottomBlock(loadSave.colideToBottomBlock);
        gameModel.setColideToLeftBlock(loadSave.colideToLeftBlock);
        gameModel.setColideToTopBlock(loadSave.colideToTopBlock);
        gameModel.setColideToTopLeftBlock(loadSave.colideToTopLeftBlock);
        gameModel.setColideToTopRightBlock(loadSave.colideToTopRightBlock);
        gameModel.setColideToBottomLeftBlock(loadSave.colideToBottomLeftBlock);
        gameModel.setColideToBottomRightBlock(loadSave.colideToBottomRightBlock);

        // Restoring directional flags
        gameModel.setGoDownBall(loadSave.goDownBall);
        gameModel.setGoRightBall(loadSave.goRightBall);

        restoreBlocksFromSerializable(loadSave.blocks);
    }

    private void restoreBlocksFromSerializable(ArrayList<BlockSerializable> blockSerializables) {
        gameModel.getBlocks().clear();
        for (BlockSerializable ser : blockSerializables) {
            gameModel.addBlock(new Block(ser.row, ser.j, ser.type, ser.countBreakerCount));
        }
    }
}
