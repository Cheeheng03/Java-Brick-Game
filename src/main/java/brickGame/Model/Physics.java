package brickGame.Model;

public class Physics {
    private Ball gameball;
    private GameModel gameModel;
    private Paddle paddle;

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
        handleBlockCollisions();
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

    public void handleBlockCollisions() {
        if (gameModel.isColideToTopBlock()) {
            handleTopBlockCollision();
        }
        if (gameModel.isColideToBottomBlock()) {
            handleBottomBlockCollision();
        }
        if (gameModel.isColideToLeftBlock()) {
            handleLeftBlockCollision();
        }
        if (gameModel.isColideToRightBlock()) {
            handleRightBlockCollision();
        }
        if (gameModel.isColideToTopLeftBlock()) {
            handleTopLeftBlockCollision();
        }
        if (gameModel.isColideToTopRightBlock()) {
            handleTopRightBlockCollision();
        }
        if (gameModel.isColideToBottomLeftBlock()) {
            handleBottomLeftBlockCollision();
        }
        if (gameModel.isColideToBottomRightBlock()) {
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
}
