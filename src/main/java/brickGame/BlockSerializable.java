package brickGame;

import java.io.Serializable;

public class BlockSerializable implements Serializable {
    public final int row;
    public final int j;
    public final int type;
    public int countBreakerCount;

    public BlockSerializable(int row , int j , int type, int countBreakerCount) {
        this.row = row;
        this.j = j;
        this.type = type;
        this.countBreakerCount = countBreakerCount;
    }
}
