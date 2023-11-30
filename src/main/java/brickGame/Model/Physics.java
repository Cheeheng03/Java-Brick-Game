package brickGame.Model;

public class Physics {
    private final Ball gameball;
    private final GameModel gameModel;
    private final Paddle paddle;
    private int previousHitCode = Block.NO_HIT;
    private long lastHitTime = 0;

    public Physics(GameModel gameModel) {
        this.gameModel = gameModel;
        this.gameball = gameModel.getGameball();
        this.paddle = gameModel.getPaddle();
    }

    public void setPhysicsToBall() {
        gameball.updatePosition();
        handleGameOverConditions();
        handleBreakCollisions();
        handleWallCollisions();
    }

    private void handleBreakCollisions() {
        if (checkPaddleCollisions()) {
            calculateBallDirectionAfterBreakCollision();
        }
    }

    private boolean checkPaddleCollisions() {
        return gameball.getY() >= paddle.getY() - gameball.getRadius() &&
                gameball.getX() >= paddle.getX() && gameball.getX() <= paddle.getX() + paddle.getWidth();
    }

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

    private double calculateVelocityX(double relation, int level) {
        if (Math.abs(relation) <= 0.3) {
            return Math.abs(relation);
        } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
            return (Math.abs(relation) * 1.5) + (level / 3.500);
        } else {
            return (Math.abs(relation) * 2) + (level / 3.500);
        }
    }

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

    private boolean checkHeartDecrement() {
        return !gameModel.getIsGoldStatus() && gameball.getY() >= gameModel.getSceneHeight() - gameball.getRadius();
    }

    public void handleWallCollisions() {
        int sceneWidth = gameModel.getSceneWidth();

        if (gameball.getX() >= sceneWidth || gameball.getX() <= 0) {
            gameModel.resetColideFlags();
            gameModel.setColideToRightWall(gameball.getX() >= sceneWidth);
            gameball.bounceHorizontally();
        }
    }

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
            gameball.adjustVerticalAngle();
            gameball.bounceUp();
        } else {
            gameball.adjustVerticalAngle();
            gameball.bounceUp();
        }
    }

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

    private void invertVerticalDirection(){
        gameball.bounceVertically();
    }

    private void invertHorizontalDirection(){
        gameball.bounceHorizontally();
    }
}
