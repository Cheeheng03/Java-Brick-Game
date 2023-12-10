package brickGame.Model;

import java.util.*;

/**
 * Represents the central model for the brick game.
 * This class manages the state and logic of the game, including the ball, paddle, blocks, bonuses, and game physics.
 * It handles game level initialization, scoring, collisions, and special effects.
 */
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
    private boolean colideToBreak               = false;
    private boolean colideToBreakAndMoveToRight = true;
    private boolean colideToRightWall           = false;
    private boolean colideToLeftWall            = false;
    private boolean isFreezeStatus = false;
    private boolean paddleWidthChanged = false;

    /**
     * Constructs a new GameModel instance.
     * Initializes the paddle, ball, and collections for blocks and bonuses.
     */
    public GameModel() {
        this.paddle = new Paddle();
        this.gameball = initBall();
        this.blocks = new ArrayList<>();
        this.chocos = new ArrayList<>();
        this.mysteryBlocks = new ArrayList<>();
        this.blocksToRemove = new LinkedList<>();
    }

    /**
     * Initializes the ball with a random position within the game scene.
     * Ensures the ball's starting position is not within the blocks or paddle area.
     *
     * @return A new Ball instance with randomized position.
     */
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

    /**
     * Initializes the game board for the current level.
     * Sets up blocks based on the level number or custom level logic.
     * Randomly determines block types for standard levels.
     */
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

    /**
     * Determines the type of block based on a random number.
     *
     * @param randomNumber A randomly generated number used to determine the block type.
     * @return The type of block to be created.
     */
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

    /**
     * Determines whether to create a heart block or a normal block.
     * Ensures that only one heart block exists at a time.
     *
     * @return The block type, either BLOCK_HEART or BLOCK_NORMAL.
     */
    private int determineHeartBlockType() {
        if (!isExistHeartBlock) {
            isExistHeartBlock = true;
            return Block.BLOCK_HEART;
        } else {
            return Block.BLOCK_NORMAL;
        }
    }

    /**
     * Resets the collision flags related to the ball and paddle.
     * Used to manage the state of collision in the game loop.
     */
    public void resetColideFlags() {
        colideToBreak = false;
        colideToBreakAndMoveToRight = false;
        colideToRightWall = false;
        colideToLeftWall = false;
    }

    /**
     * Initializes and applies physics to the ball.
     * Sets up the Physics object and updates the ball's movement and collision logic.
     */
    public void setPhysicsToBall() {
        this.physics = new Physics(this);
        physics.setPhysicsToBall();
    }

    /**
     * Checks for collisions between the ball and the paddle.
     *
     * @return True if the ball collides with the paddle, false otherwise.
     */
    public boolean checkPaddleCollisions(){
        return gameball.getY() >= paddle.getY() - gameball.getRadius() && gameball.getX() >= paddle.getX() && gameball.getX() <= paddle.getX() + paddle.getWidth();
    }

    /**
     * Checks if the player's heart count should be decremented.
     * This occurs when the ball falls below the bottom of the scene and the player is not in 'gold status'.
     *
     * @return True if heart count should be decremented, false otherwise.
     */
    public boolean checkHeartDecrement(){
        return !isGoldStatus && gameball.getY() >= sceneHeight - gameball.getRadius();
    }

    /**
     * Updates the game state based on collisions between the ball and blocks.
     * Manages block destruction, scoring, and special block destruction handling.
     */
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

    /**
     * Handles the effects of hitting a special block in the game.
     * This includes updating game states like score, gold time, and activating bonuses.
     *
     * @param block The block that has been hit by the ball.
     */
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

    /**
     * Removes blocks from the game that have been marked as destroyed.
     * Updates the count of destroyed blocks.
     */
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

    /**
     * Updates the status of special blocks based on the current time.
     * Handles the duration of effects like gold, freeze, and ghost status.
     *
     * @param currentTime The current time in the game loop.
     */
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

    /**
     * Updates the state of bonus blocks in the game.
     * Manages the movement and collisions of choco and mystery bonuses.
     */
    public void updateBonusBlocks(){
        handleBonusUpdates(chocos);
        handleBonusUpdates(mysteryBlocks);
    }
    /**
     * Handles updates for a list of bonus items.
     *
     * @param bonuses The list of bonus items to update.
     */
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

    /**
     * Determines whether a bonus item should be skipped during updates.
     *
     * @param bonus The bonus item to check.
     * @return True if the bonus should be skipped, false otherwise.
     */
    public boolean shouldSkipBonus(Bonus bonus) {
        return bonus.getY() > sceneHeight || bonus.isTaken();
    }

    /**
     * Checks for collisions between a bonus item and the paddle.
     *
     * @param bonus The bonus item to check for collision.
     * @return True if there is a collision, false otherwise.
     */
    public boolean handleBonusCollision(Bonus bonus) {
        return bonus.getY() >= paddle.getY() && bonus.getY() <= paddle.getY() + paddle.getHeight()
                && bonus.getX() >= paddle.getX() && bonus.getX() <= paddle.getX() + paddle.getWidth();
    }

    /**
     * Processes the effects of a bonus item colliding with the paddle.
     * Handles scoring and special effects depending on the bonus type.
     *
     * @param bonus The bonus item that collided with the paddle.
     */
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

    /**
     * Processes the effect of a paddle-related bonus.
     * Manages the duration and reversal of paddle size changes.
     */
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

    /**
     * Updates the position of a bonus item based on the elapsed time.
     * Moves the bonus item downwards to simulate falling.
     *
     * @param bonus The bonus item to update.
     */
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

    /**
     * Checks if the current level is completed.
     * Level completion is based on whether all non-special blocks have been destroyed.
     *
     * @return True if the level is completed, false otherwise.
     */
    public boolean checkLevelCompletion() {
        return destroyedBlockCount == initialBlockCount;
    }

    /**
     * Prepares the game model for the next level.
     * Resets game elements, timing, and flags for the new level.
     */
    public void initializeNextLevel() {
        resetBallForNewLevel();
        gameball.setVelocityX(1.000);
        resetColideFlags();
        isGoldStatus = false;
        isFreezeStatus = false;
        isExistHeartBlock = false;
        time = 0;
        goldTime = 0;
        freezeTime = 0;
        paddleTimeRemaining = 0;
        lastHitTime = 0;
    }

    /**
     * Resets the main game elements for a new game or level.
     * Clears blocks, bonuses, and resets the block count.
     */
    public void resetGameElements() {
        blocks.clear();
        chocos.clear();
        mysteryBlocks.clear();;
        destroyedBlockCount = 0;
    }

    /**
     * Resets the ball for a new level.
     * Re-initializes the ball with a random position.
     */
    public void resetBallForNewLevel() {
        this.gameball = initBall();
    }

    /**
     * Resets the game for a restart.
     * Resets score, heart, level, and all game elements to their initial state.
     */
    public void resetGameForRestart(){
        level = 0;
        heart = 3;
        score = 0;
        gameball.setVelocityX(1.000);
        resetColideFlags();
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

    /**
     * Saves the current game state.
     * Utilizes the LoadSave class to serialize and store the game model state.
     */
    public void saveGame(){
        LoadSave loadSave = new LoadSave();
        loadSave.saveGameState(this);
    }

    /**
     * Loads a saved game state.
     * Reads the saved game state and applies it to the current game model.
     */
    public void loadSavedGame(){
        LoadSave loadSave = new LoadSave();
        loadSave.read();
        applyStateToGameModel(loadSave);
    }

    /**
     * Applies a saved game state to the current game model.
     *
     * @param loadSave The saved game state to be applied.
     */
    public void applyStateToGameModel(LoadSave loadSave) {
        new LoadGame(this).applyStateToGameModel(loadSave);
        processPaddleBonus();
    }

    /**
     * Creates a chocolate bonus at the position of the specified block.
     *
     * @param block The block where the chocolate bonus is to be created.
     * @return The newly created chocolate bonus.
     */
    public Bonus createChoco(Block block){
        final Bonus choco = new Bonus(block.row, block.column, Block.BLOCK_CHOCO);
        addChoco(choco);
        return choco;
    }

    /**
     * Creates a mystery bonus at the position of the specified block.
     *
     * @param block The block where the mystery bonus is to be created.
     * @return The newly created mystery bonus.
     */
    public Bonus createMystery(Block block){
        final Bonus mystery  = new Bonus(block.row, block.column, Block.BLOCK_MYSTERY);
        addMystery(mystery);
        return mystery;
    }

    // Getter and setter methods
    /**
     * Retrieves the game's ball instance.
     *
     * @return The current Ball object in the game.
     */
    public Ball getGameball() { return gameball; }

    /**
     * Retrieves the game's paddle instance.
     *
     * @return The current Paddle object in the game.
     */
    public Paddle getPaddle() { return paddle; }

    /**
     * Retrieves the list of all blocks currently in the game.
     *
     * @return An ArrayList containing all Block objects.
     */
    public ArrayList<Block> getBlocks() { return blocks; }

    /**
     * Adds a new block to the game.
     *
     * @param block The Block object to be added to the game.
     */
    public void addBlock(Block block) { blocks.add(block); }

    /**
     * Adds a chocolate bonus to the game.
     *
     * @param choco The Bonus object representing a chocolate bonus.
     */
    public void addChoco(Bonus choco) { chocos.add(choco); }

    /**
     * Adds a mystery bonus to the game.
     *
     * @param mysteryBlock The Bonus object representing a mystery bonus.
     */
    public void addMystery(Bonus mysteryBlock){
        mysteryBlocks.add(mysteryBlock);
    }

    /**
     * Retrieves the current level of the game.
     *
     * @return The current level number.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Retrieves the current score of the game.
     *
     * @return The current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Retrieves the current number of hearts (lives) in the game.
     *
     * @return The current number of hearts.
     */
    public int getHeart() {
        return heart;
    }

    /**
     * Retrieves the current game time.
     *
     * @return The current time in the game.
     */
    public long getTime() {
        return time;
    }

    /**
     * Retrieves the time when the gold status was activated.
     *
     * @return The time of gold status activation.
     */
    public long getGoldTime() {
        return goldTime;
    }

    /**
     * Retrieves the time when the freeze status was activated.
     *
     * @return The time of freeze status activation.
     */
    public long getFreezeTime(){
        return freezeTime;
    }

    /**
     * Retrieves the time when the ghost status was activated.
     *
     * @return The time of ghost status activation.
     */
    public long getGhostTime(){
        return ghostTime;
    }

    /**
     * Checks if the gold status is currently active in the game.
     *
     * @return True if gold status is active, false otherwise.
     */
    public boolean getIsGoldStatus() {
        return isGoldStatus;
    }

    /**
     * Checks if the freeze status is currently active in the game.
     *
     * @return True if freeze status is active, false otherwise.
     */
    public boolean getIsFreezeStatus(){
        return isFreezeStatus;
    }

    /**
     * Checks if the ghost status is currently active in the game.
     *
     * @return True if ghost status is active, false otherwise.
     */
    public boolean getIsGhostStatus(){
        return isGhostStatus;
    }

    /**
     * Retrieves the count of blocks that have been destroyed in the current level.
     *
     * @return The number of destroyed blocks.
     */
    public int getDestroyedBlockCount() {
        return destroyedBlockCount;
    }

    /**
     * Checks if a heart block exists in the current level.
     *
     * @return True if a heart block exists, false otherwise.
     */
    public boolean getIsExistHeartBlock() {
        return isExistHeartBlock;
    }

    /**
     * Retrieves the previous x-coordinate of the ball.
     *
     * @return The previous x-coordinate.
     */
    public double getXBallPrevious(){
        return xBallPrevious;
    }

    /**
     * Retrieves the previous y-coordinate of the ball.
     *
     * @return The previous y-coordinate.
     */
    public double getYBallPrevious(){
        return yBallPrevious;
    }

    /**
     * Retrieves the list of chocolate bonuses in the game.
     *
     * @return A list of Bonus objects representing chocolate bonuses.
     */
    public List<Bonus> getChocos() {
        return chocos;
    }

    /**
     * Retrieves the list of mystery bonuses in the game.
     *
     * @return A list of Bonus objects representing mystery bonuses.
     */
    public List<Bonus> getMysteries(){
        return mysteryBlocks;
    }

    /**
     * Retrieves the queue of blocks that are marked for removal from the game.
     *
     * @return A queue of Block objects to be removed.
     */
    public Queue<Block> getBlocksToRemove() {
        return blocksToRemove;
    }

    /**
     * Retrieves the remaining time for any active paddle bonus.
     *
     * @return The remaining time for the paddle bonus.
     */
    public int getPaddleTimeRemaining() {
        return paddleTimeRemaining;
    }

    /**
     * Sets the remaining time for the active paddle bonus.
     *
     * @param paddleTimeRemaining The time remaining for the paddle bonus.
     */
    public void setPaddleTimeRemaining(int paddleTimeRemaining){
        this.paddleTimeRemaining = paddleTimeRemaining;
    }

    /**
     * Checks if the paddle width has been changed due to a bonus.
     *
     * @return True if the paddle width has been changed, false otherwise.
     */
    public boolean isPaddleWidthChanged() {
        return paddleWidthChanged;
    }

    /**
     * Sets the state of paddle width change.
     *
     * @param changed True if the paddle width has been changed, false otherwise.
     */
    public void setPaddleWidthChanged(boolean changed) {
        this.paddleWidthChanged = changed;
    }

    /**
     * Checks if there is a collision that causes a break.
     *
     * @return True if there is a collision causing a break, false otherwise.
     */
    public boolean isColideToBreak() {
        return colideToBreak;
    }

    /**
     * Checks if there is a collision causing the ball to move to the right.
     *
     * @return True if there is a collision causing the ball to move to the right, false otherwise.
     */
    public boolean isColideToBreakAndMoveToRight() {
        return colideToBreakAndMoveToRight;
    }

    /**
     * Checks if there is a collision with the right wall.
     *
     * @return True if there is a collision with the right wall, false otherwise.
     */
    public boolean isColideToRightWall() {
        return colideToRightWall;
    }

    /**
     * Checks if there is a collision with the left wall.
     *
     * @return True if there is a collision with the left wall, false otherwise.
     */
    public boolean isColideToLeftWall() {
        return colideToLeftWall;
    }

    /**
     * Retrieves the height of the game scene.
     *
     * @return The height of the scene.
     */
    public int getSceneHeight() {
        return sceneHeight;
    }

    /**
     * Retrieves the width of the game scene.
     *
     * @return The width of the scene.
     */
    public int getSceneWidth(){
        return sceneWidth;
    }

    /**
     * Sets the previous x-coordinate of the ball.
     *
     * @param x The previous x-coordinate.
     */
    public void setXBallPrevious(double x) {
        this.xBallPrevious = x;
    }

    /**
     * Sets the previous y-coordinate of the ball.
     *
     * @param y The previous y-coordinate.
     */
    public void setYBallPrevious(double y) {
        this.yBallPrevious = y;
    }

    /**
     * Increments the game level by a specified amount.
     *
     * @param increment The amount to increment the level by.
     */
    public void addToLevel(int increment) {
        this.level += increment;
    }

    /**
     * Sets the game's score.
     *
     * @param score The new score.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Increments the game's score by a specified amount.
     *
     * @param increment The amount to add to the score.
     */
    public void addToScore(int increment) {
        this.score += increment;
    }

    /**
     * Sets the number of hearts (lives) in the game.
     *
     * @param heart The new number of hearts.
     */
    public void setHeart(int heart) {
        this.heart = heart;
    }

    /**
     * Sets the game time.
     *
     * @param time The new game time.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Sets the time when the gold status was activated.
     *
     * @param goldTime The time of gold status activation.
     */
    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }

    /**
     * Sets the time at which the ghost bonus was activated.
     *
     * @param ghostTime The time in milliseconds when the ghost bonus started.
     */
    public  void setGhostTime(long ghostTime){
        this.ghostTime= ghostTime;
    }

    /**
     * Sets the time at which the freeze bonus was activated.
     *
     * @param freezeTime The time in milliseconds when the freeze bonus started.
     */
    public  void setFreezeTime(long freezeTime){
        this.freezeTime = freezeTime;
    }

    /**
     * Sets the status of the gold bonus in the game.
     *
     * @param isGoldStatus True if the gold bonus is active, false otherwise.
     */
    public void setGoldStatus(boolean isGoldStatus) {
        this.isGoldStatus = isGoldStatus;
    }

    /**
     * Sets the status of the ghost bonus in the game.
     *
     * @param isGhostStatus True if the ghost bonus is active, false otherwise.
     */
    public void setGhostStatus(boolean isGhostStatus) {
        this.isGhostStatus = isGhostStatus;
    }

    /**
     * Sets the count of blocks destroyed in the current level.
     *
     * @param count The number of blocks destroyed.
     */
    public void setDestroyedBlockCount(int count) {
        this.destroyedBlockCount = count;
    }

    /**
     * Sets the initial count of blocks at the start of the level.
     *
     * @param count The total number of blocks present at the beginning of the level.
     */
    public void setInitialBlockCount(int count) {
        this.initialBlockCount = count;
    }

    /**
     * Sets the status of the freeze bonus in the game.
     *
     * @param isFreezeStatus True if the freeze bonus is active, false otherwise.
     */
    public void setIsFreezeStatus(boolean isFreezeStatus){
        this.isFreezeStatus = isFreezeStatus;
    }

    /**
     * Sets whether the ball is colliding with a breakable block.
     *
     * @param colideToBreak True if the ball is colliding with a breakable block, false otherwise.
     */
    public void setColideToBreak(boolean colideToBreak) {
        this.colideToBreak = colideToBreak;
    }

    /**
     * Sets whether the ball is colliding with a breakable block and moving to the right.
     *
     * @param colideToBreakAndMoveToRight True if the ball is colliding with a breakable block and moving to the right, false otherwise.
     */
    public void setColideToBreakAndMoveToRight(boolean colideToBreakAndMoveToRight) {
        this.colideToBreakAndMoveToRight = colideToBreakAndMoveToRight;
    }

    /**
     * Sets whether the ball is colliding with the right wall of the game area.
     *
     * @param colideToRightWall True if the ball is colliding with the right wall, false otherwise.
     */
    public void setColideToRightWall(boolean colideToRightWall) {
        this.colideToRightWall = colideToRightWall;
    }

    /**
     * Sets whether the ball is colliding with the left wall of the game area.
     *
     * @param colideToLeftWall True if the ball is colliding with the left wall, false otherwise.
     */
    public void setColideToLeftWall(boolean colideToLeftWall) {
        this.colideToLeftWall = colideToLeftWall;
    }

    /**
     * Sets whether a heart block exists in the current level.
     *
     * @param isExistHeartBlock True if a heart block is present, false otherwise.
     */
    public void setIsExistHeartBlock(boolean isExistHeartBlock) {
        this.isExistHeartBlock = isExistHeartBlock;
    }

    /**
     * Sets the current level of the game.
     *
     * @param level The level number.
     */
    public void setLevel(int level) {
        this.level = level;
    }
}
