package brickGame.Model;

import java.util.*;

public class GameModel {
    private Ball gameball;
    private Paddle paddle;
    private Physics physics;
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Bonus> chocos = new ArrayList<Bonus>();
    private ArrayList<Bonus> mysteryBlocks = new ArrayList<Bonus>();
    private Queue<Block> blocksToRemove;
    private int level = 0;
    private int score = 0;
    private int heart = 3;
    private long time = 0;
    private long goldTime;
    private long freezeTime;
    private long ghostTime;
    private long lastHitTime = 0;
    private int paddleTimeRemaining;
    private boolean isGoldStatus = false;
    private boolean isGhostStatus = false;
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
    private boolean isFreezeStatus = false;

    public GameModel() {
        this.paddle = new Paddle();
        this.gameball = initBall();
        this.blocks = new ArrayList<>();
        this.chocos = new ArrayList<>();
        this.mysteryBlocks = new ArrayList<>();
        this.blocksToRemove = new LinkedList<>();
    }

    private Ball initBall() {
        Random random = new Random();

        int xBall = random.nextInt(sceneWidth) + 1;
        int blocksBottomY = (level + 2) * Block.getHeight() + Block.getPaddingTop();
        int paddleTopY = (int) paddle.getY();
        int ballRadius = 10;
        int ballMinY = blocksBottomY + ballRadius;
        int ballMaxY = paddleTopY - ballRadius;

        int yBall = (ballMinY < ballMaxY) ? random.nextInt(ballMaxY - ballMinY) + ballMinY : ballMinY;

        return new Ball(xBall, yBall);
    }

    public void initBoard() {
        Random random = new Random();

        if(level == 17){
            CustomLevel customLevel = new CustomLevel17(this);
            customLevel.initLevel();
            initialBlockCount = blocks.size()-2;
        } else if(level == 18){
            CustomLevel customLevel = new CustomLevel18(this);
            customLevel.initLevel();
            initialBlockCount = blocks.size()-4;
        } else {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < level + 1; j++) {
                    int randomNumber = random.nextInt(500);

                    if (randomNumber % 6 == 0) {
                        continue;
                    }

                    int type = determineBlockType(randomNumber);
                    blocks.add(new Block(j, i, type, 0));
                }
            }
            initialBlockCount = blocks.size();
        }
    }

    private int determineBlockType(int randomNumber) {
        if (randomNumber % 10 == 1) {
            return Block.BLOCK_CHOCO;
        } else if (randomNumber % 10 == 2) {
            return determineHeartBlockType();
        } else if (randomNumber % 10 == 3) {
            return Block.BLOCK_STAR;
        } else if (randomNumber % 10 == 4) {
            return Block.BLOCK_FREEZE;
        } else if (randomNumber % 10 == 5) {
            return Block.BLOCK_MYSTERY;
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

    public void resetColideFlags() {
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
        this.physics = new Physics(this);
        physics.setPhysicsToBall();
    }

    public boolean checkPaddleCollisions(){
        return gameball.getY() >= paddle.getY() - gameball.getRadius() && gameball.getX() >= paddle.getX() && gameball.getX() <= paddle.getX() + paddle.getWidth();
    }

    public boolean checkHeartDecrement(){
        return !isGoldStatus && gameball.getY() >= sceneHeight - gameball.getRadius();
    }

    public void updateBlockCollisions() {
            for (final Block block : blocks) {
                int hitCode = block.checkHitToBlock(gameball.getX(), gameball.getY(), xBallPrevious, yBallPrevious, gameball.getRadius());
                if (hitCode != Block.NO_HIT && block.checkAndProcessHit(time)) {
                    block.isAlreadyHit = true;
                    if (block.type == Block.BLOCK_COUNT_BREAKER) {
                        block.decrementCount();
                        if (block.getHitsToDestroy() == 0) {
                            addToScore(1);
                            block.isDestroyed = true;
                            blocksToRemove.add(block);
                        }
                    } else if (block.type != Block.BLOCK_WALL) {
                        addToScore(1);
                        block.isDestroyed = true;
                        blocksToRemove.add(block);
                    }

                    handleSpecialBlock(block);
                    physics.handleBlockCollisions(hitCode, time);
                }
            }

        for (final Block block : blocks) {
            if (block.type == Block.BLOCK_WALL || block.type == Block.BLOCK_COUNT_BREAKER) {
                block.resetHitFlagOnce();
            }
        }
    }

    private void handleSpecialBlock(final Block block) {
        if (block.type == Block.BLOCK_CHOCO) {
            final Bonus choco = new Bonus(block.row, block.column, Block.BLOCK_CHOCO);
            choco.timeCreated = time;
        } else if (block.type == Block.BLOCK_STAR) {
            goldTime = time;
            isGoldStatus = true;
        } else if (block.type == Block.BLOCK_HEART) {
            heart++;
        } else if (block.type == Block.BLOCK_FREEZE) {
            freezeTime = time;
            isFreezeStatus = true;
        } else if (block.type == Block.BLOCK_MYSTERY) {
            final Bonus mystery = new Bonus(block.row, block.column, Block.BLOCK_MYSTERY);
            mystery.timeCreated = time;
        } else if (block.type == Block.Block_GHOST) {
            ghostTime = time;
            isGhostStatus = true;
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

    public void updateSpecialBlockStatus(long currentTime) {
        if (currentTime - goldTime > 5000) {
            isGoldStatus = false;
        }
        if(currentTime - freezeTime > 3000){
            isFreezeStatus = false;
        }
        if(currentTime - ghostTime > 1500){
            isGhostStatus = false;
        }
    }

    public void updateBonusBlocks(){
        handleBonusUpdates(chocos);
        handleBonusUpdates(mysteryBlocks);
    }
    public void handleBonusUpdates(List<Bonus> bonuses) {
        for (Bonus bonus : bonuses) {
            if (shouldSkipBonus(bonus)) {
                continue;
            }
            if (handleBonusCollision(bonus)) {
                processBonusCollision(bonus );
            }
            updateBonusPosition(bonus);
        }
    }

    public boolean shouldSkipBonus(Bonus bonus) {
        return bonus.getY() > sceneHeight || bonus.isTaken();
    }

    public boolean handleBonusCollision(Bonus bonus) {
        return bonus.getY() >= paddle.getY() && bonus.getY() <= paddle.getY() + paddle.getHeight()
                && bonus.getX() >= paddle.getX() && bonus.getX() <= paddle.getX() + paddle.getWidth();
    }

    private boolean paddleWidthChanged = false;

    public void processBonusCollision(Bonus bonus) {
        bonus.setTaken(true);
        int type = bonus.getType();
        if (type == Block.BLOCK_CHOCO) {
            addToScore(3);
        } else if (type == Block.BLOCK_MYSTERY) {
            Random random = new Random();
            boolean increaseWidth = random.nextBoolean();

            if (increaseWidth) {
                paddle.increaseWidth();
                System.out.println("Congrats! The paddle width has been expanded for 10 seconds");
            } else {
                paddle.decreaseWidth();
                System.out.println("Oh No! The paddle width has been shrunken for 10 seconds");
            }

            paddleWidthChanged = true;
            paddleTimeRemaining = 10;
            processPaddleBonus();
        }
    }

    private void processPaddleBonus(){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            if (paddleTimeRemaining > 0) {
                paddleTimeRemaining--;
            } else {
                paddle.resetWidth();
                paddleWidthChanged = true;
                this.cancel();
            }
            }
        }, 0, 1000);
    }

    public int getPaddleTimeRemaining() {
        return paddleTimeRemaining;
    }

    public void setPaddleTimeRemaining(int paddleTimeRemaining){
        this.paddleTimeRemaining = paddleTimeRemaining;
    }

    public boolean isPaddleWidthChanged() {
        return paddleWidthChanged;
    }

    public void setPaddleWidthChanged(boolean changed) {
        this.paddleWidthChanged = changed;
    }

    public void updateBonusPosition(Bonus bonus) {
        long elapsedTime = time - bonus.getTimeCreated();
        double fallingSpeed = 0.05;

        // Calculate the new Y position based on the elapsed time and falling speed
        long newY = (long) (bonus.getY() + fallingSpeed * elapsedTime);

        if (newY > sceneHeight) {
            newY = sceneHeight;
        }

        bonus.updateY(newY);
    }

    public boolean checkLevelCompletion() {
        return destroyedBlockCount == initialBlockCount;
    }

    public void initializeNextLevel() {
        resetBallForNewLevel();
        gameball.setVelocityX(1.000);
        resetColideFlags();
        goDownBall = true;
        isGoldStatus = false;
        isFreezeStatus = false;
        isExistHeartBlock = false;
        time = 0;
        goldTime = 0;
        freezeTime = 0;
        paddleTimeRemaining = 0;
        lastHitTime = 0;
    }

    public void resetGameElements() {
        blocks.clear();
        chocos.clear();
        mysteryBlocks.clear();;
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
        isFreezeStatus = false;
        isGhostStatus = false;
        isExistHeartBlock = false;
        time = 0;
        goldTime = 0;
        freezeTime = 0;
        ghostTime = 0;
        paddleTimeRemaining = 0;
        lastHitTime = 0;
        resetGameElements();
        resetBallForNewLevel();
    }

    public void applyStateToGameModel(LoadSave loadSave) {
        new LoadGame(this).applyStateToGameModel(loadSave);
        processPaddleBonus();
    }

    // Getter and setter methods
    public Ball getGameball() { return gameball; }
    public Paddle getPaddle() { return paddle; }
    public ArrayList<Block> getBlocks() { return blocks; }
    public void addBlock(Block block) { blocks.add(block); }
    public void addChoco(Bonus choco) { chocos.add(choco); }

    public void addMystery(Bonus mysteryBlock){
        mysteryBlocks.add(mysteryBlock);
    }

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
    public long getFreezeTime(){
        return freezeTime;
    }

    public long getGhostTime(){
        return ghostTime;
    }

    public boolean getIsGoldStatus() {
        return isGoldStatus;
    }

    public boolean getIsFreezeStatus(){
        return isFreezeStatus;
    }

    public boolean getIsGhostStatus(){
        return isGhostStatus;
    }

    public int getDestroyedBlockCount() {
        return destroyedBlockCount;
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

    public List<Bonus> getMysteries(){
        return mysteryBlocks;
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

    public  void setGhostTime(long ghostTime){
        this.ghostTime= ghostTime;
    }

    public  void setFreezeTime(long freezeTime){
        this.freezeTime = freezeTime;
    }

    public void setGoldStatus(boolean isGoldStatus) {
        this.isGoldStatus = isGoldStatus;
    }

    public void setGhostStatus(boolean isGhostStatus) {
        this.isGhostStatus = isGhostStatus;
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

    public void setIsFreezeStatus(boolean isFreezeStatus){
        this.isFreezeStatus = isFreezeStatus;
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

    public int getSceneHeight() {
        return sceneHeight;
    }

    public int getSceneWidth(){
        return sceneWidth;
    }
}
