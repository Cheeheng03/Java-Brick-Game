package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private final int sceneWidth = 500;
    private final int sceneHeigt = 700;
    private static final int LEFT  = 1;
    private static final int RIGHT = 2;
    private long time     = 0;
    private GameEngine engine;
    private boolean loadFromSave = false;
    private boolean isLevelTransitionInProgress = false;
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    Stage  primaryStage;
    private GameModel gameModel;
    private GameView gameView;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Called");
        this.primaryStage = primaryStage;

        gameView = new GameView();
        if (gameModel == null) {
            gameModel = new GameModel();
        }
        gameView.initializeUI(gameModel, loadFromSave);
        initializeGameElements();
        gameView.addToRoot(gameModel, loadFromSave);
        gameView.setSceneToStage(primaryStage);
        primaryStage.getScene().setOnKeyPressed(this);
        if (!loadFromSave) {
            initializeGameButtons();
            setupButtonActions();
        } else {
            startGameEngine();
            loadFromSave = false;
        }
    }

    private void initializeGameElements() {
        if (!loadFromSave) {
            gameModel.addToLevel(1);
            gameView.updateLabels(gameModel);
            if (gameModel.getLevel() == 18) {
                gameView.showWin();
                gameView.setLevelButtonsVisibility(false);
                return;
            }
            if (gameModel.getLevel() > 1 && gameModel.getLevel() <18) {
                gameView.showMessage("Level Up :)");
            }
            gameView.setLevelButtonsVisibility(gameModel.getLevel() == 1);

            gameView.initBallAndPaddle(gameModel);
            gameModel.initBoard();
            gameModel.setInitialBlockCount(gameModel.getBlocks().size());
        } else{
            gameView.updateLabels(gameModel);
        }
    }

    private void initializeGameButtons() {
        gameView.getLoadButton().setOnAction(event -> {
            loadGame();
            gameView.setLevelButtonsVisibility(false);
        });

        gameView.getNewGameButton().setOnAction(event -> {
            engine = new GameEngine();
            engine.setOnAction(Main.this);
            engine.setFps(120);
            engine.start();

            gameView.setLevelButtonsVisibility(false);
        });
    }

    private void setupButtonActions() {
        if (gameModel.getLevel() > 1 && gameModel.getLevel() < 18) {
            gameView.setLevelButtonsVisibility(false);
            startGameEngine();
        }
    }

    private void startGameEngine() {
        engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        engine.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(KeyEvent event) {
        if(event.getCode() == KeyCode.LEFT) {
            move(LEFT);
        } else if(event.getCode() == KeyCode.RIGHT) {
            move(RIGHT);
//        } else if(event.getCode() == KeyCode.DOWN) {
//            //setPhysicsToBall(); // Assuming you have physics method for ball
//        }
        } else if(event.getCode() == KeyCode.S) {
            saveGame();
        }
    }

    private void move(final int direction) {
        new Thread(() -> {
            int sleepTime = 4;
            int maxIterations = 30;

            for (int i = 0; i < maxIterations; i++) {
                if ((gameModel.getPaddle().getX() <= 0 && direction == LEFT) || (gameModel.getPaddle().getX() >= (sceneWidth - gameModel.getPaddle().getWidth()) && direction == RIGHT)) {
                    return;
                }
                if (direction == RIGHT) {
                    gameModel.getPaddle().moveRight(sceneWidth);
                } else if (direction == LEFT) {
                    gameModel.getPaddle().moveLeft();
                }

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    handleException(e);
                }

                if (i >= 20) {
                    sleepTime = i;
                }
            }
        }).start();
    }

    private void setPhysicsToBall() {
        gameModel.setPhysicsToBall();
        if (gameModel.checkHeartDecrement()) {
            handleGameEndScenario();
        }
    }

    private void handleGameEndScenario() {
        gameView.show((double) sceneWidth / 2, (double) sceneHeigt / 2, -1);

        if (gameModel.getHeart() == 0) {
            //new Score().showGameOver(this);
            gameView.showGameOver(restartAction);
            engine.stop();
        }
    }

    private void saveGame() {
        if (gameModel.getLevel() < 18 && gameModel.getHeart() > 0) {
            LoadSave loadSave = new LoadSave();
            loadSave.saveGameState(gameModel);
            gameView.showMessage("Game Saved");
        } else {
            gameView.showGameOverAlert();
        }
    }

    private void loadGame() {
        LoadSave loadSave = new LoadSave();
        loadSave.read();
        try {
            gameModel.applyStateToGameModel(loadSave);
            loadFromSave = true;
            start(primaryStage);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void updateGameState() {
        if (gameModel.checkLevelCompletion() && !isLevelTransitionInProgress) {
            isLevelTransitionInProgress = true;
            Platform.runLater(() -> {
                try {
                    gameModel.initializeNextLevel();
                    engine.stop();
                    gameModel.resetGameElements();
                    start(primaryStage);
                } catch (Exception e) {
                    handleException(e);
                } finally {
                    isLevelTransitionInProgress = false;
                }
            });
        }
    }

    Score.GameRestartAction restartAction = () -> {
        Platform.runLater(() -> {
            try {
                gameModel.resetGameForRestart();
                start(primaryStage);
            } catch (Exception e) {
                handleException(e);
            }
        });
    };

    @Override
    public void onUpdate() {
        updateLabelsAndPosition();

        gameModel.setXBallPrevious(gameModel.getGameball().getX());
        gameModel.setYBallPrevious(gameModel.getGameball().getY());
        gameModel.setTime(time);

        if (gameModel.getGameball().getY() >= Block.getPaddingTop() && gameModel.getGameball().getY() <= (Block.getHeight() * (gameModel.getLevel() + 1)) + Block.getPaddingTop()) {
            gameModel.updateBlockCollisions();
            updateBlockCollisionsUI();
        }

        gameModel.removeDestroyedBlocks();
        updateUIAfterBlockRemoval();
    }

    private void updateLabelsAndPosition() {
        Platform.runLater(() -> {
          gameView.updateLabelsAndBall(gameModel);

            for (Bonus choco : gameModel.getChocos()) {
                choco.getChoco().setY(choco.getY());
            }
        });
    }

    private void updateUIAfterBlockRemoval() {
        for (Block block : gameModel.getBlocksToRemove()) {
            Platform.runLater(() -> gameView.setNotVisibleAfterBlockRemoval(block));
        }
    }

    private void updateBlockCollisionsUI() {
        for (Block block : gameModel.getBlocks()) {
            if (block.isDestroyed) {
                //new Score().show(block.x, block.y, 1, this);
                gameView.show(block.x, block.y, 1);

                if (block.type == Block.BLOCK_CHOCO) {
                    final Bonus choco = new Bonus(block.row, block.column);
                    gameModel.addChoco(choco);
                    Platform.runLater(() -> gameView.addChocoUI(choco));
                } else if (block.type == Block.BLOCK_STAR) {
                    Platform.runLater(() -> {
                       gameView.addGoldRoot();
                    });
                } else if (block.type == Block.BLOCK_HEART) {
                  //new Score().showMessage("Heart +1", this);
                    gameView.showMessage("Heart +1");
                }
            }
        }
    }

    @Override
    public void onInit() {

    }

    public void onPhysicsUpdate() {
        updateGameState();
        setPhysicsToBall();
        gameModel.updateGoldStatus(time);
        if (!gameModel.getIsGoldStatus()) {
            gameView.resetGoldStatusUI();
        }

        gameModel.updateChocos();
        updateChocoUI();
    }

    private void updateChocoUI() {
        Iterator<Bonus> iterator = gameModel.getChocos().iterator();
        while (iterator.hasNext()) {
            Bonus choco = iterator.next();
            if (choco.isTaken()) {
                choco.getChoco().setVisible(false);
                gameView.show(choco.getX(), choco.getY(), 3);
                System.out.println("You Got it and +3 score for you");

                iterator.remove();
            }
        }
    }

    @Override
    public void onTime(long time) {
        this.time = time;
    }

    private void handleException(Exception e) {
        logger.log(Level.SEVERE, "An error occurred: " + e.getMessage(), e);
    }
}
