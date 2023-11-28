package brickGame;

import java.util.Random;

public class CustomLevel17 implements CustomLevel {

    private GameModel gameModel;

    public CustomLevel17 (GameModel gameModel){
        this.gameModel = gameModel;
    }

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