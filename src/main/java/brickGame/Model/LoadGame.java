package brickGame.Model;

import java.util.ArrayList;

/**
 * Handles the loading of a saved game state into the GameModel.
 * This class is responsible for restoring the state of various game elements
 * such as the ball, paddle, blocks, and various game statuses from a saved state.
 */
public class LoadGame {
    private GameModel gameModel;

    /**
     * Constructs a new LoadGame instance.
     *
     * @param gameModel The GameModel instance into which the saved state will be loaded.
     */
    public LoadGame(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    /**
     * Applies the saved game state to the GameModel.
     * Restores the state of the game including levels, scores, game time,
     * special statuses, and the states of the ball and paddle.
     *
     * @param loadSave The LoadSave object containing the saved game state.
     */
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

        // Restoring directional flags
        gameModel.getGameball().setGoingDown(loadSave.goDownBall);
        gameModel.getGameball().setGoingRight(loadSave.goRightBall);

        restoreBlocksFromSerializable(loadSave.blocks);
    }

    /**
     * Restores the block states from their serializable form.
     * Converts BlockSerializable objects back into Block objects and adds them to the game model.
     *
     * @param blockSerializables The list of BlockSerializable objects representing the saved block states.
     */
    private void restoreBlocksFromSerializable(ArrayList<BlockSerializable> blockSerializables) {
        gameModel.getBlocks().clear();
        for (BlockSerializable ser : blockSerializables) {
            gameModel.addBlock(new Block(ser.row, ser.j, ser.type, ser.countBreakerCount));
        }
    }
}
