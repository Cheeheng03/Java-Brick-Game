package brickGame.Model;

import java.io.Serializable;

/**
 * Serializable representation of a block in the brick game.
 * This class is used to save and load the state of a block, including its position, type, and specific properties.
 */
public class BlockSerializable implements Serializable {
    public final int row;
    public final int j;
    public final int type;
    public int countBreakerCount;

    /**
     * Constructs a new BlockSerializable object with specified parameters.
     *
     * @param row The row position of the block.
     * @param j The column position of the block.
     * @param type The type of the block, determining its behavior.
     * @param countBreakerCount The count for blocks that require multiple hits to be destroyed.
     */
    public BlockSerializable(int row , int j , int type, int countBreakerCount) {
        this.row = row;
        this.j = j;
        this.type = type;
        this.countBreakerCount = countBreakerCount;
    }
}
