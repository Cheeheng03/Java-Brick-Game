package brickGame.Model;

public class CustomLevel18 implements CustomLevel {

    private GameModel gameModel;

    public CustomLevel18 (GameModel gameModel){
        this.gameModel = gameModel;
    }
    @Override
    public void initLevel() {
        System.out.println("Custom");
        gameModel.getBlocks().clear();

        Integer[][] layout = {
                {Block.BLOCK_NORMAL, Block.BLOCK_FREEZE, Block.Block_GHOST, Block.Block_GHOST, Block.BLOCK_FREEZE, null, Block.BLOCK_FREEZE, Block.BLOCK_MYSTERY, Block.BLOCK_STAR, Block.BLOCK_WALL, Block.Block_GHOST, Block.BLOCK_MYSTERY, Block.Block_GHOST },
                {Block.BLOCK_WALL, Block.Block_GHOST, Block.BLOCK_CHOCO, Block.BLOCK_CHOCO, Block.Block_GHOST, Block.BLOCK_STAR, Block.BLOCK_FREEZE, Block.BLOCK_STAR, Block.BLOCK_FREEZE, null, Block.BLOCK_HEART},
                {Block.BLOCK_WALL, Block.Block_GHOST, Block.BLOCK_CHOCO, Block.BLOCK_CHOCO, Block.Block_GHOST, Block.BLOCK_STAR, Block.BLOCK_FREEZE, Block.BLOCK_STAR, null, Block.BLOCK_FREEZE, Block.BLOCK_HEART},
                {Block.BLOCK_NORMAL, Block.BLOCK_FREEZE, Block.Block_GHOST, Block.Block_GHOST, Block.BLOCK_FREEZE, null, Block.BLOCK_FREEZE, Block.BLOCK_MYSTERY, Block.BLOCK_STAR, Block.Block_GHOST, Block.BLOCK_WALL, Block.BLOCK_MYSTERY, Block.Block_GHOST }
        };

        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                if (layout[i][j] != null) {
                    gameModel.getBlocks().add(new Block(j, i, layout[i][j], 0));
                }
            }
        }
    }
}