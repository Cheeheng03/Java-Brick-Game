package brickGame.Model;

/**
 * Manages the physics of the ball in the game, including ball movement and collision handling.
 * This class is responsible for updating the ball's position and handling interactions with the paddle and blocks.
 */
public class Physics {
    private final Ball gameball;
    private final GameModel gameModel;
    private final Paddle paddle;
    private int previousHitCode = Block.NO_HIT;
    private long lastHitTime = 0;

    /**
     * Constructs a new Physics object.
     *
     * @param gameModel The GameModel instance containing the game objects whose physics will be managed.
     */
    public Physics(GameModel gameModel) {
        this.gameModel = gameModel;
        this.gameball = gameModel.getGameball();
        this.paddle = gameModel.getPaddle();
    }

    /**
     * Updates the ball's position and handles its interactions with other game objects.
     * Includes checking for collisions with the paddle, walls, and blocks.
     */
    public void setPhysicsToBall() {
        gameball.updatePosition();
        handleGameOverConditions();
        handleBreakCollisions();
        handleWallCollisions();
    }

    /**
     * Handles collisions between the ball and the paddle.
     * Calculates the new direction of the ball based on the collision point.
     */
    private void handleBreakCollisions() {
        if (checkPaddleCollisions()) {
            calculateBallDirectionAfterBreakCollision();
        }
    }

    /**
     * Checks if there is a collision between the ball and the paddle.
     *
     * @return True if the ball is colliding with the paddle, false otherwise.
     */
    private boolean checkPaddleCollisions() {
        return gameball.getY() >= paddle.getY() - gameball.getRadius() &&
                gameball.getX() >= paddle.getX() && gameball.getX() <= paddle.getX() + paddle.getWidth();
    }

    /**
     * Calculates and updates the ball's direction after a collision with the paddle.
     * Adjusts the ball's horizontal velocity based on the collision point relative to the paddle's center.
     */
    private void calculateBallDirectionAfterBreakCollision() {
        gameModel.resetColideFlags();
        gameModel.setColideToBreak(true);
        gameball.bounceUp();

        double relation = (gameball.getX() - paddle.getCenterBreakX()) / ((double) paddle.getWidth() / 2);
        double newVelocityX = calculateVelocityX(relation, gameModel.getLevel());
        gameball.setVelocityX(newVelocityX);

        if (gameball.getX() - paddle.getCenterBreakX() > 0) {
            gameball.bounceRight();
        } else {
            gameball.bounceLeft();
        }

    }

    /**
     * Calculates the new horizontal velocity of the ball based on its position relative to the paddle's center and the game level.
     *
     * @param relation The relative position of the ball to the paddle's center.
     * @param level The current game level, which may affect the ball's velocity.
     * @return The calculated horizontal velocity of the ball.
     */
    private double calculateVelocityX(double relation, int level) {
        if (Math.abs(relation) <= 0.3) {
            return Math.abs(relation);
        } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
            return (Math.abs(relation) * 1.5) + (level / 3.500);
        } else {
            return (Math.abs(relation) * 2) + (level / 3.500);
        }
    }

    /**
     * Handles game over conditions based on the ball's position.
     * Checks if the ball has reached the boundaries of the play area and updates the game state accordingly.
     */
    public void handleGameOverConditions() {
        int sceneHeight = gameModel.getSceneHeight();

        if (gameball.getY() <= 0 || gameball.getY() >= sceneHeight - gameball.getRadius()) {
            gameModel.resetColideFlags();
            if (gameball.getY() >= sceneHeight - gameball.getRadius()) {
                gameball.bounceUp();
            }
            if (gameball.getY() <= 0) {
                gameball.bounceDown();
            }
            if (checkHeartDecrement()) {
                gameModel.setHeart(gameModel.getHeart() - 1);
            }
        }
    }

    /**
     * Checks if the conditions are met to decrement the player's heart count.
     * A heart is decremented when the ball falls below the bottom of the scene without gold status.
     *
     * @return True if a heart should be decremented, false otherwise.
     */
    private boolean checkHeartDecrement() {
        return !gameModel.getIsGoldStatus() && gameball.getY() >= gameModel.getSceneHeight() - gameball.getRadius();
    }

    /**
     * Handles collisions between the ball and the walls of the game area.
     * Updates the ball's direction upon hitting a wall.
     */
    public void handleWallCollisions() {
        int sceneWidth = gameModel.getSceneWidth();

        if (gameball.getX() >= sceneWidth || gameball.getX() <= 0) {
            gameModel.resetColideFlags();
            gameModel.setColideToRightWall(gameball.getX() >= sceneWidth);
            gameball.bounceHorizontally();
        }
    }

    /**
     * Handles collisions between the ball and blocks.
     * Adjusts the ball's direction and velocity based on the type and position of the collision.
     *
     * @param hitCode The code indicating the type of collision.
     * @param time The current time in the game loop to keep track on last hit time and check for double block destruction.
     */
    public void handleBlockCollisions(int hitCode, long time) {
        gameball.setVelocityY(1.0);
        if (time - lastHitTime > 5) {
            previousHitCode = Block.NO_HIT;
        }

        if ((hitCode == Block.HIT_TOP_RIGHT && previousHitCode == Block.HIT_BOTTOM_RIGHT) ||
                (hitCode == Block.HIT_BOTTOM_RIGHT && previousHitCode == Block.HIT_TOP_RIGHT)) {
            handleRightBlockCollision();
        } else if ((hitCode == Block.HIT_TOP_LEFT && previousHitCode == Block.HIT_BOTTOM_LEFT) ||
                (hitCode == Block.HIT_BOTTOM_LEFT && previousHitCode == Block.HIT_TOP_LEFT)) {
            handleLeftBlockCollision();
        } else if ((hitCode == Block.HIT_TOP_LEFT && previousHitCode == Block.HIT_TOP_RIGHT) ||
                (hitCode == Block.HIT_TOP_RIGHT && previousHitCode == Block.HIT_TOP_LEFT)) {
            handleTopLeftBlockCollision();
        } else if ((hitCode == Block.HIT_BOTTOM_RIGHT && previousHitCode == Block.HIT_BOTTOM_LEFT) ||
                (hitCode == Block.HIT_BOTTOM_LEFT && previousHitCode == Block.HIT_BOTTOM_RIGHT)) {
            handleBottomLeftBlockCollision();
        } else if ((hitCode == Block.HIT_TOP_RIGHT && previousHitCode == Block.HIT_BOTTOM_LEFT) ||
                (hitCode == Block.HIT_BOTTOM_LEFT && previousHitCode == Block.HIT_TOP_RIGHT)) {
            invertVerticalDirection();
            invertHorizontalDirection();
        } else if ((hitCode == Block.HIT_TOP_LEFT && previousHitCode == Block.HIT_BOTTOM_RIGHT) ||
                (hitCode == Block.HIT_BOTTOM_RIGHT && previousHitCode == Block.HIT_TOP_LEFT)) {
            invertVerticalDirection();
            invertHorizontalDirection();
        }

        if (hitCode == Block.HIT_TOP) {
            handleTopBlockCollision();
        } else if (hitCode == Block.HIT_BOTTOM) {
            handleBottomBlockCollision();
        } else if (hitCode == Block.HIT_LEFT) {
            handleLeftBlockCollision();
        } else if (hitCode == Block.HIT_RIGHT) {
            handleRightBlockCollision();
        } else if (hitCode == Block.HIT_TOP_LEFT) {
            handleTopLeftBlockCollision();
        } else if (hitCode == Block.HIT_TOP_RIGHT) {
            handleTopRightBlockCollision();
        } else if (hitCode == Block.HIT_BOTTOM_LEFT) {
            handleBottomLeftBlockCollision();
        } else if (hitCode == Block.HIT_BOTTOM_RIGHT) {
            handleBottomRightBlockCollision();
        }

        previousHitCode = hitCode;
        lastHitTime = time;
    }

    /**
     * Handles the collision of the ball with the top side of a block.
     * Causes the ball to bounce upward.
     */
    private void handleTopBlockCollision() {
        gameball.bounceUp();
    }

    /**
     * Handles the collision of the ball with the bottom side of a block.
     * Causes the ball to bounce downward.
     */
    private void handleBottomBlockCollision() {
        gameball.bounceDown();
    }

    /**
     * Handles the collision of the ball with the left side of a block.
     * Causes the ball to bounce to the left.
     */
    private void handleLeftBlockCollision() {
        gameball.bounceLeft();
    }

    /**
     * Handles the collision of the ball with the right side of a block.
     * Causes the ball to bounce to the right.
     */
    private void handleRightBlockCollision() {
        gameball.bounceRight();
    }

    /**
     * Handles the collision of the ball with the top-left corner of a block.
     * Adjusts the ball's vertical angle and direction based on its current trajectory.
     */
    private void handleTopLeftBlockCollision() {
        if (gameball.isGoingUp()) {
            gameball.bounceLeft();
        } else if (gameball.isGoingLeft()) {
            gameball.adjustVerticalAngle();
            gameball.bounceUp();
        } else {
            gameball.adjustVerticalAngle();
            gameball.bounceUp();
        }
    }

    /**
     * Handles the collision of the ball with the top-right corner of a block.
     * Adjusts the ball's vertical angle and direction based on its current trajectory.
     */
    private void handleTopRightBlockCollision() {
        if (gameball.isGoingUp()) {
            gameball.bounceRight();
        } else if (gameball.isGoingRight()) {
            gameball.adjustVerticalAngle();
            gameball.bounceUp();
        } else {
            gameball.adjustVerticalAngle();
            gameball.bounceUp();
        }
    }

    /**
     * Handles the collision of the ball with the bottom-left corner of a block.
     * Adjusts the ball's vertical angle and direction based on its current trajectory.
     */
    private void handleBottomLeftBlockCollision() {
        if (gameball.isGoingDown()) {
            gameball.bounceLeft();
        } else if (gameball.isGoingLeft()) {
            gameball.adjustVerticalAngle();
            gameball.bounceDown();
        } else {
            gameball.adjustVerticalAngle();
            gameball.bounceDown();
        }
    }

    /**
     * Handles the collision of the ball with the bottom-right corner of a block.
     * Adjusts the ball's vertical angle and direction based on its current trajectory.
     */
    private void handleBottomRightBlockCollision() {
        if (gameball.isGoingDown()) {
            gameball.bounceRight();
        } else if (gameball.isGoingRight()) {
            gameball.adjustVerticalAngle();
            gameball.bounceDown();
        } else {
            gameball.adjustVerticalAngle();
            gameball.bounceDown();
        }
    }

    /**
     * Inverts the vertical direction of the ball.
     * Used when the ball collides with objects in a manner that requires a vertical direction change.
     */
    private void invertVerticalDirection(){
        gameball.bounceVertically();
    }

    /**
     * Inverts the horizontal direction of the ball.
     * Used when the ball collides with objects in a manner that requires a horizontal direction change.
     */
    private void invertHorizontalDirection(){
        gameball.bounceHorizontally();
    }
}
