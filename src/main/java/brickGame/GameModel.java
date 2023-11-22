package brickGame;

import javafx.application.Platform;

import java.util.*;

public class GameModel {
    private Ball gameball;
    private Paddle paddle;
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Bonus> chocos = new ArrayList<Bonus>();
    private Queue<Block> blocksToRemove;
    private int level = 0;
    private int score = 0;
    private int heart = 1;
    private long time;
    private long goldTime;
    private boolean isGoldStatus = false;
    private int destroyedBlockCount;
    private int initialBlockCount;
    private final int sceneWidth = 500;
    private final int sceneHeight = 700;
    private double xBallPrevious;
    private double yBallPrevious;
    private boolean isExistHeartBlock = false;
    private boolean readyForNextLevel = false;
    private boolean goDownBall                  = true;
    private boolean goRightBall                 = true;
    private boolean colideToBreak               = false;
    private boolean colideToBreakAndMoveToRight = true;
    private boolean colideToRightWall           = false;
    private boolean colideToLeftWall            = false;
    private boolean colideToRightBlock          = false;
    private boolean colideToBottomBlock         = false;
    private boolean colideToLeftBlock           = false;
    private boolean colideToTopBlock            = false;
    private boolean colideToTopLeftBlock        = false;
    private boolean colideToTopRightBlock       = false;
    private boolean colideToBottomLeftBlock     = false;
    private boolean colideToBottomRightBlock    = false;

    public GameModel() {

        this.paddle = new Paddle();
        this.gameball = initBall();
        this.blocks = new ArrayList<>();
        this.chocos = new ArrayList<>();
        this.blocksToRemove = new LinkedList<>();
    }

    private Ball initBall() {
        Random random = new Random();

        int xBall = random.nextInt(sceneWidth) + 1;
        int blocksBottomY = (level + 1) * Block.getHeight() + Block.getPaddingTop();
        int paddleTopY = (int) paddle.getY();
        int ballRadius = 10;
        int ballMinY = blocksBottomY + ballRadius;
        int ballMaxY = paddleTopY - ballRadius;

        int yBall = (ballMinY < ballMaxY) ? random.nextInt(ballMaxY - ballMinY) + ballMinY : ballMinY;

        return new Ball(xBall, yBall);
    }

    public void initBoard() {
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level + 1; j++) {
                int randomNumber = random.nextInt(500);

                if (randomNumber % 5 == 0) {
                    continue;
                }

                int type = determineBlockType(randomNumber);
                blocks.add(new Block(j, i, type));
            }
        }
    }

    private int determineBlockType(int randomNumber) {
        if (randomNumber % 10 == 1) {
            return Block.BLOCK_CHOCO;
        } else if (randomNumber % 10 == 2) {
            return determineHeartBlockType();
        } else if (randomNumber % 10 == 3) {
            return Block.BLOCK_STAR;
        } else {
            return Block.BLOCK_NORMAL;
        }
    }

    private int determineHeartBlockType() {
        if (!isExistHeartBlock) {
            isExistHeartBlock = true;
            return Block.BLOCK_HEART;
        } else {
            return Block.BLOCK_NORMAL;
        }
    }

    private void resetColideFlags() {
        colideToBreak = false;
        colideToBreakAndMoveToRight = false;
        colideToRightWall = false;
        colideToLeftWall = false;
        colideToRightBlock = false;
        colideToBottomBlock = false;
        colideToLeftBlock = false;
        colideToTopBlock = false;
        colideToTopLeftBlock = false;
        colideToTopRightBlock = false;
        colideToBottomLeftBlock = false;
        colideToBottomRightBlock = false;
    }

    public void setPhysicsToBall() {
        gameball.updatePosition();
        handleGameOverConditions();
        handleBreakCollisions();
        handleWallCollisions();
        handleBlockCollisions();
    }

    private void handleBreakCollisions() {
        if (gameball.getY() >= paddle.getY() - gameball.getRadius() && gameball.getX() >= paddle.getX() && gameball.getX() <= paddle.getX() + paddle.getWidth()) {
            calculateBallDirectionAfterBreakCollision();
        }
    }

    private void calculateBallDirectionAfterBreakCollision() {
        resetColideFlags();
        colideToBreak = true;
        gameball.bounceUp();

        double relation = (gameball.getX() - paddle.getCenterBreakX()) / ((double) paddle.getWidth() / 2);
        double newVelocityX = calculateVelocityX(relation, level);
        gameball.setVelocityX(newVelocityX);

        if (gameball.getX() - paddle.getCenterBreakX() > 0) {
            gameball.bounceRight();
        } else {
            gameball.bounceLeft();
        }

    }

    private double calculateVelocityX(double relation, int level) {
        if (Math.abs(relation) <= 0.3) {
            return Math.abs(relation);
        } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
            return (Math.abs(relation) * 1.5) + (level / 3.500);
        } else {
            return (Math.abs(relation) * 2) + (level / 3.500);
        }
    }

    private void handleGameOverConditions() {
        if (gameball.getY() <= 0 || gameball.getY() >= sceneHeight - gameball.getRadius()) {
            resetColideFlags();
            if (gameball.getY() >= sceneHeight - gameball.getRadius()) {
                gameball.bounceUp();
            }
            if (gameball.getY() <= 0) {
                gameball.bounceDown();
            }
            if (checkHeartDecrement()) {
                heart--;
            }
        }
    }

    public boolean checkHeartDecrement(){
        return !isGoldStatus && gameball.getY() >= sceneHeight - gameball.getRadius();
    }



    private void handleWallCollisions() {
        if (gameball.getX() >= sceneWidth || gameball.getX() <= 0) {
            resetColideFlags();
            colideToRightWall = gameball.getX() >= sceneWidth;
            gameball.bounceHorizontally();
        }
    }

    private void handleBlockCollisions() {
        if (colideToTopBlock) {
            handleTopBlockCollision();
        }
        if (colideToBottomBlock) {
            handleBottomBlockCollision();
        }
        if (colideToLeftBlock) {
            handleLeftBlockCollision();
        }
        if (colideToRightBlock) {
            handleRightBlockCollision();
        }
        if (colideToTopLeftBlock) {
            handleTopLeftBlockCollision();
        }
        if (colideToTopRightBlock) {
            handleTopRightBlockCollision();
        }
        if (colideToBottomLeftBlock) {
            handleBottomLeftBlockCollision();
        }
        if (colideToBottomRightBlock) {
            handleBottomRightBlockCollision();
        }
    }

    private void handleTopBlockCollision() {
        gameball.bounceUp();
    }

    private void handleBottomBlockCollision() {
        gameball.bounceDown();
    }

    private void handleLeftBlockCollision() {
        gameball.bounceLeft();
    }

    private void handleRightBlockCollision() {
        gameball.bounceRight();
    }

    private void handleTopLeftBlockCollision() {
        if (gameball.isGoingUp()) {
            gameball.bounceLeft();
        } else if (gameball.isGoingLeft()) {
            gameball.bounceUp();
        } else {
            gameball.bounceUp();
        }
    }

    private void handleTopRightBlockCollision() {
        if (gameball.isGoingUp()) {
            gameball.bounceRight();
        } else if (gameball.isGoingRight()) {
            gameball.bounceUp();
        } else {
            gameball.bounceUp();
        }
    }

    private void handleBottomLeftBlockCollision() {
        if (gameball.isGoingDown()) {
            gameball.bounceLeft();
        } else if (gameball.isGoingLeft()) {
            gameball.bounceDown();
        } else {
            gameball.bounceDown();
        }
    }

    private void handleBottomRightBlockCollision() {
        if (gameball.isGoingDown()) {
            gameball.bounceRight();
        } else if (gameball.isGoingRight()) {
            gameball.bounceDown();
        } else {
            gameball.bounceDown();
        }
    }

    public void updateBlockCollisions() {
        for (final Block block : blocks) {
            int hitCode = block.checkHitToBlock(gameball.getX(), gameball.getY(), xBallPrevious, yBallPrevious, gameball.getRadius());
            if (hitCode != Block.NO_HIT) {
                addToScore(1);
                block.isDestroyed = true;
                blocksToRemove.add(block);

                handleSpecialBlock(block);
                setCollisionFlags(hitCode);
                setPhysicsToBall();
            }
        }
    }

    private void handleSpecialBlock(final Block block) {
        if (block.type == Block.BLOCK_CHOCO) {
            final Bonus choco = new Bonus(block.row, block.column);
            choco.timeCreated = time;
        } else if (block.type == Block.BLOCK_STAR) {
            goldTime = time;
            isGoldStatus = true;
        } else if (block.type == Block.BLOCK_HEART) {
            heart++;
        }
    }

    private void setCollisionFlags(int hitCode) {
        if (hitCode == Block.HIT_RIGHT) {
            System.out.println("Hit right");
            colideToRightBlock = true;
        } else if (hitCode == Block.HIT_BOTTOM) {
            System.out.println("Hit bottom");
            colideToBottomBlock = true;
        } else if (hitCode == Block.HIT_LEFT) {
            System.out.println("Hit Left");
            colideToLeftBlock = true;
        } else if (hitCode == Block.HIT_TOP) {
            System.out.println("Hit top");
            colideToTopBlock = true;
        } else if (hitCode == Block.HIT_TOP_LEFT) {
            System.out.println("Hit top left");
            colideToTopLeftBlock = true;
        } else if (hitCode == Block.HIT_TOP_RIGHT) {
            System.out.println("Hit top right");
            colideToTopRightBlock = true;
        } else if (hitCode == Block.HIT_BOTTOM_LEFT) {
            System.out.println("Hit bottom left");
            colideToBottomLeftBlock = true;
        } else if (hitCode == Block.HIT_BOTTOM_RIGHT) {
            System.out.println("Hit bottom right");
            colideToBottomRightBlock = true;
        }
    }

    public void removeDestroyedBlocks() {
        Iterator<Block> iterator = blocks.iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (blocksToRemove.contains(block)) {
                iterator.remove();
                destroyedBlockCount++;
            }
        }
    }

    public void updateGoldStatus(long currentTime) {
        if (currentTime - goldTime > 5000) {
            resetGoldStatus();
        }
    }

    private void resetGoldStatus() {
        isGoldStatus = false;
    }

    public void updateChocos() {
        for (Bonus choco : chocos) {
            if (shouldSkipChoco(choco)) {
                continue;
            }
            if (handleChocoCollision(choco)) {
                processChocoCollision(choco);
            }
            updateChocoPosition(choco);
        }
    }

    public boolean shouldSkipChoco(Bonus choco) {
        return choco.getY() > sceneHeight || choco.isTaken();
    }

    public boolean handleChocoCollision(Bonus choco) {
        return choco.getY() >= paddle.getY() && choco.getY() <= paddle.getY() + paddle.getHeight()
                && choco.getX() >= paddle.getX() && choco.getX() <= paddle.getX() + paddle.getWidth();
    }

    public void processChocoCollision(Bonus choco) {
        choco.setTaken(true);
        addToScore(3);
    }

    public void updateChocoPosition(Bonus choco) {
        long elapsedTime = time - choco.getTimeCreated();
        double fallingSpeed = 0.05;

        // Calculate the new Y position based on the elapsed time and falling speed
        long newY = (long) (choco.getY() + fallingSpeed * elapsedTime);

        if (newY > sceneHeight) {
            newY = sceneHeight;
        }

        choco.updateY(newY);
    }

    public boolean checkLevelCompletion() {
        if (destroyedBlockCount == initialBlockCount) {
            return true;
        }
        return false;
    }

    public void initializeNextLevel() {
        resetBallForNewLevel();
        gameball.setVelocityX(1.000);
        resetColideFlags();
        goDownBall = true;
        isGoldStatus = false;
        isExistHeartBlock = false;
        time = 0;
        goldTime = 0;
    }

    public void resetGameElements() {
        blocks.clear();
        chocos.clear();
        destroyedBlockCount = 0;
    }

    public void resetBallForNewLevel() {
        this.gameball = initBall();
    }

    public void resetGameForRestart(){
        level = 0;
        heart = 3;
        score = 0;
        gameball.setVelocityX(1.000);
        resetColideFlags();
        goDownBall = true;
        isGoldStatus = false;
        isExistHeartBlock = false;
        time = 0;
        goldTime = 0;
        resetGameElements();
        resetBallForNewLevel();
    }

    public void applyStateToGameModel(LoadSave loadSave) {
        this.setLevel(loadSave.level);
        this.setScore(loadSave.score);
        this.setHeart(loadSave.heart);
        this.setDestroyedBlockCount(loadSave.destroyedBlockCount);
        this.setTime(loadSave.time);
        this.setGoldTime(loadSave.goldTime);
        this.setGoldStatus(loadSave.isGoldStauts);
        this.setIsExistHeartBlock(loadSave.isExistHeartBlock);
        this.setInitialBlockCount(loadSave.blocks.size() + loadSave.destroyedBlockCount);

        // Setting ball state
        Ball gameball = this.getGameball();
        gameball.setX(loadSave.xBall);
        gameball.setY(loadSave.yBall);
        gameball.setVelocityX(loadSave.vX);
        gameball.setVelocityY(loadSave.vY);

        // Setting previous ball position
        this.setXBallPrevious(loadSave.xBallPrevious);
        this.setYBallPrevious(loadSave.yBallPrevious);

        // Setting paddle state
        Paddle paddle = this.getPaddle();
        paddle.setX(loadSave.xBreak);
        paddle.setY(loadSave.yBreak);
        paddle.setCenterBreakX(loadSave.centerBreakX);

        // Restoring collision flags
        this.setColideToBreak(loadSave.colideToBreak);
        this.setColideToBreakAndMoveToRight(loadSave.colideToBreakAndMoveToRight);
        this.setColideToRightWall(loadSave.colideToRightWall);
        this.setColideToLeftWall(loadSave.colideToLeftWall);
        this.setColideToRightBlock(loadSave.colideToRightBlock);
        this.setColideToBottomBlock(loadSave.colideToBottomBlock);
        this.setColideToLeftBlock(loadSave.colideToLeftBlock);
        this.setColideToTopBlock(loadSave.colideToTopBlock);
        this.setColideToTopLeftBlock(loadSave.colideToTopLeftBlock);
        this.setColideToTopRightBlock(loadSave.colideToTopRightBlock);
        this.setColideToBottomLeftBlock(loadSave.colideToBottomLeftBlock);
        this.setColideToBottomRightBlock(loadSave.colideToBottomRightBlock);

        // Restoring directional flags
        this.setGoDownBall(loadSave.goDownBall);
        this.setGoRightBall(loadSave.goRightBall);

        restoreBlocksFromSerializable(loadSave.blocks);
    }

    private void restoreBlocksFromSerializable(ArrayList<BlockSerializable> blockSerializables) {
        this.blocks.clear(); // Clear existing blocks
        for (BlockSerializable ser : blockSerializables) {
            this.blocks.add(new Block(ser.row, ser.j, ser.type));
        }
    }


    // Getter and setter methods
    public Ball getGameball() { return gameball; }
    public Paddle getPaddle() { return paddle; }
    public ArrayList<Block> getBlocks() { return blocks; }
    public void addBlock(Block block) { blocks.add(block); }
    public void addChoco(Bonus choco) { chocos.add(choco); }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public int getHeart() {
        return heart;
    }

    public long getTime() {
        return time;
    }

    public long getGoldTime() {
        return goldTime;
    }

    public boolean getIsGoldStatus() {
        return isGoldStatus;
    }

    public int getDestroyedBlockCount() {
        return destroyedBlockCount;
    }

    public int getInitialBlockCount() {
        return initialBlockCount;
    }

    public boolean getIsExistHeartBlock() {
        return isExistHeartBlock;
    }

    public double getXBallPrevious(){
        return xBallPrevious;
    }

    public double getYBallPrevious(){
        return yBallPrevious;
    }
    public List<Bonus> getChocos() {
        return chocos;
    }

    public Queue<Block> getBlocksToRemove() {
        return blocksToRemove;
    }
    public boolean isColideToBreak() {
        return colideToBreak;
    }

    public boolean isColideToBreakAndMoveToRight() {
        return colideToBreakAndMoveToRight;
    }

    public boolean isColideToRightWall() {
        return colideToRightWall;
    }

    public boolean isColideToLeftWall() {
        return colideToLeftWall;
    }

    public boolean isColideToRightBlock() {
        return colideToRightBlock;
    }

    public boolean isColideToBottomBlock() {
        return colideToBottomBlock;
    }

    public boolean isColideToLeftBlock() {
        return colideToLeftBlock;
    }

    public boolean isColideToTopBlock() {
        return colideToTopBlock;
    }

    public boolean isColideToTopLeftBlock() {
        return colideToTopLeftBlock;
    }

    public boolean isColideToTopRightBlock() {
        return colideToTopRightBlock;
    }

    public boolean isColideToBottomLeftBlock() {
        return colideToBottomLeftBlock;
    }

    public boolean isColideToBottomRightBlock() {
        return colideToBottomRightBlock;
    }


    // Setters
    public void setXBallPrevious(double x) {
        this.xBallPrevious = x;
    }

    public void setYBallPrevious(double y) {
        this.yBallPrevious = y;
    }

    public void addToLevel(int increment) {
        this.level += increment;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addToScore(int increment) {
        this.score += increment;
    }


    public void setHeart(int heart) {
        this.heart = heart;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }

    public void setGoldStatus(boolean isGoldStatus) {
        this.isGoldStatus = isGoldStatus;
    }

    public void setDestroyedBlockCount(int count) {
        this.destroyedBlockCount = count;
    }

    public void setInitialBlockCount(int count) {
        this.initialBlockCount = count;
    }

    public void setReadyForNextLevel(boolean ready) {
        this.readyForNextLevel = ready;
    }

    public void setColideToBreak(boolean colideToBreak) {
        this.colideToBreak = colideToBreak;
    }

    public void setColideToBreakAndMoveToRight(boolean colideToBreakAndMoveToRight) {
        this.colideToBreakAndMoveToRight = colideToBreakAndMoveToRight;
    }

    public void setColideToRightWall(boolean colideToRightWall) {
        this.colideToRightWall = colideToRightWall;
    }

    public void setColideToLeftWall(boolean colideToLeftWall) {
        this.colideToLeftWall = colideToLeftWall;
    }

    public void setColideToRightBlock(boolean colideToRightBlock) {
        this.colideToRightBlock = colideToRightBlock;
    }

    public void setColideToBottomBlock(boolean colideToBottomBlock) {
        this.colideToBottomBlock = colideToBottomBlock;
    }

    public void setColideToLeftBlock(boolean colideToLeftBlock) {
        this.colideToLeftBlock = colideToLeftBlock;
    }

    public void setColideToTopBlock(boolean colideToTopBlock) {
        this.colideToTopBlock = colideToTopBlock;
    }

    public void setColideToTopLeftBlock(boolean colideToTopLeftBlock) {
        this.colideToTopLeftBlock = colideToTopLeftBlock;
    }

    public void setColideToTopRightBlock(boolean colideToTopRightBlock) {
        this.colideToTopRightBlock = colideToTopRightBlock;
    }

    public void setColideToBottomLeftBlock(boolean colideToBottomLeftBlock) {
        this.colideToBottomLeftBlock = colideToBottomLeftBlock;
    }

    public void setColideToBottomRightBlock(boolean colideToBottomRightBlock) {
        this.colideToBottomRightBlock = colideToBottomRightBlock;
    }

    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }

    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }

    public void setIsExistHeartBlock(boolean isExistHeartBlock) {
        this.isExistHeartBlock = isExistHeartBlock;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
