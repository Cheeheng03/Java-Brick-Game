package brickGame.Model;

import java.util.Random;

/**
 * Implementation of the CustomLevel interface to define the unique layout and characteristics of level 17 in the brick game.
 * This class specifies the arrangement and types of blocks for level 17, including their behavior and interactions.
 */
public class CustomLevel17 implements CustomLevel {

    private GameModel gameModel;

    /**
     * Constructs a new instance of CustomLevel17.
     *
     * @param gameModel The GameModel instance associated with this level, used for managing game state and elements.
     */
    public CustomLevel17 (GameModel gameModel){
        this.gameModel = gameModel;
    }

    /**
     * Initializes the layout and properties of level 17.
     * Sets up the blocks in the game model according to a predefined layout specific to this level.
     * New block - 'BLOCK_COUNT_BREAKER' added requiring a certain number of hits to be destroyed.
     */
    @Override
    public void initLevel() {
        gameModel.getBlocks().clear();
        Random random = new Random();

        Integer[][] layout = {
                {Block.BLOCK_COUNT_BREAKER, Block.BLOCK_COUNT_BREAKER, null, Block.BLOCK_WALL},
                {Block.BLOCK_COUNT_BREAKER},
                {Block.BLOCK_COUNT_BREAKER},
                {Block.BLOCK_COUNT_BREAKER, Block.BLOCK_COUNT_BREAKER, null, null, null, Block.BLOCK_WALL}
        };

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                if (layout[i][j] != null) {
                    int hitsToDestroy = 0;
                    if (layout[i][j] == Block.BLOCK_COUNT_BREAKER) {
                        hitsToDestroy = 10 + random.nextInt(11);
                    }
                    gameModel.getBlocks().add(new Block(j, i, layout[i][j], hitsToDestroy));
                }
            }
        }
    }
}