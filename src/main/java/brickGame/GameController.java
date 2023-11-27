package brickGame;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameController implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private GameModel gameModel;
    private GameView gameView;
    private GameEngine engine;
    private boolean loadFromSave = false;
    private boolean isLevelTransitionInProgress = false;
    private final int sceneWidth = 500;
    private final int sceneHeight = 700;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private long time = 0;
    private boolean previousGoldStatus = false;
    private boolean previousFreezeStatus = false;
    private boolean isPaused = false;
    Stage  primaryStage;
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    public GameController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.engine = new GameEngine();

        engine.setOnAction(this);
        engine.setFps(120);
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        if (gameModel == null) {
            gameModel = new GameModel();
        }

        gameView.initializeUI(gameModel, loadFromSave);
        initializeGameElements();
        gameView.addToRoot(gameModel, loadFromSave);
        if(gameModel.getLevel()<18){
            gameView.setSceneToStage(primaryStage);
        }
        primaryStage.getScene().setOnKeyPressed(this);
        if (!loadFromSave) {
            initializeGameButtons();
            initializePauseButton();
            setupButtonActions();
        } else {
            initializePauseButton();
            startGameEngine();
            gameView.getPauseButton().setVisible(true);
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
            gameModel. setInitialBlockCount(gameModel.getBlocks().size());
        } else{
            gameView.initBallAndPaddle(gameModel);
            gameView.updateLabels(gameModel);
            updateGoldStatusUIOnLoad();
            updateFreezeStatusUIOnLoad();
        }
    }

    private void updateGoldStatusUIOnLoad() {
        if (gameModel.getIsGoldStatus()) {
            gameView.addGoldRoot();
        } else {
            gameView.resetGoldStatusUI();
        }
    }

    private void updateFreezeStatusUIOnLoad() {
        if (gameModel.getIsFreezeStatus()) {
            gameView.addFreezeRoot();
        } else {
            gameView.resetFreezeUI();
        }
    }

    private void initializeGameButtons() {
        gameView.getLoadButton().setOnAction(event -> {
            loadGame();
            gameView.setLevelButtonsVisibility(false);
        });

        gameView.getNewGameButton().setOnAction(event -> {
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
            gameView.getPauseButton().setVisible(true);
            gameView.setLevelButtonsVisibility(false);
        });
    }

    private void initializePauseButton(){
        gameView.getPauseButton().setFocusTraversable(false);
        gameView.getPauseButton().setOnAction(e -> {
            isPaused = !isPaused;
            if (isPaused) {
                engine.pause();
                gameView.resumeUI();
            } else {
                engine.resume();
                gameView.pauseUI();
            }
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
        engine.setInitialTime(gameModel.getTime());
        engine.start();
    }

    @Override
    public void handle(KeyEvent event) {
        if(!gameModel.getIsFreezeStatus()) {
            if (event.getCode() == KeyCode.LEFT) {
                move(LEFT);
            } else if (event.getCode() == KeyCode.RIGHT) {
                move(RIGHT);
//        } else if(event.getCode() == KeyCode.DOWN) {
//            //setPhysicsToBall(); // Assuming you have physics method for ball
//        }
            }
        }
        if (event.getCode() == KeyCode.S) {
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
        gameView.show((double) sceneWidth / 2, (double) sceneHeight / 2, -1);

        if (gameModel.getHeart() == 0) {
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
            gameView = null;
            gameView = new GameView();
            onTime(gameModel.getTime());
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
                    if(gameModel.getLevel() == 17){
                        engine.stop();
                        start(primaryStage);
                    } else{
                        gameModel.initializeNextLevel();
                        engine.stop();
                        gameModel.resetGameElements();
                        gameView = null;
                        gameView = new GameView();
                        start(primaryStage);
                    }
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
                gameView = null;
                gameView = new GameView();
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
                choco.getBonus().setY(choco.getY());
            }
            for (Bonus mystery : gameModel.getMysteries()) {
                mystery.getBonus().setY(mystery.getY());
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
                gameView.show(block.x, block.y, 1);

                if (block.type == Block.BLOCK_CHOCO) {
                    final Bonus choco = new Bonus(block.row, block.column, Block.BLOCK_CHOCO);
                    gameModel.addChoco(choco);
                    Platform.runLater(() -> gameView.addBonusUI(choco));
                } else if (block.type == Block.BLOCK_STAR) {
                    Platform.runLater(() -> {
                        gameView.addGoldRoot();
                    });
                } else if (block.type == Block.BLOCK_HEART) {
                    gameView.showMessage("Heart +1");
                } else if (block.type == Block.BLOCK_FREEZE) {
                    Platform.runLater(() -> {
                        gameView.addFreezeRoot();
                    });
                } else if (block.type == Block.BLOCK_MYSTERY) {
                    final Bonus mystery = new Bonus(block.row, block.column, Block.BLOCK_MYSTERY);
                    gameModel.addMystery(mystery);
                    Platform.runLater(() -> gameView.addBonusUI(mystery));
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

        gameModel.updateSpecialBlockStatus(time);

        boolean currentGoldStatus = gameModel.getIsGoldStatus();
        if (!currentGoldStatus && previousGoldStatus) {
            gameView.resetGoldStatusUI();
        }
        previousGoldStatus = currentGoldStatus;

        boolean currentFreezeStatus = gameModel.getIsFreezeStatus();
        if (!currentFreezeStatus && previousFreezeStatus) {
            gameView.resetFreezeUI();
        }
        previousFreezeStatus = currentFreezeStatus;

        if (gameModel.isPaddleWidthChanged()) {
            gameView.updatePaddleUI(gameModel);
            gameModel.setPaddleWidthChanged(false);
        }

        gameModel.updateBonusBlocks();
        updateChocoUI();
        updateMysteryUI();
    }

    private void updateChocoUI() {
        Iterator<Bonus> iterator = gameModel.getChocos().iterator();
        while (iterator.hasNext()) {
            Bonus choco = iterator.next();
            if (choco.isTaken()) {
                choco.getBonus().setVisible(false);
                gameView.show(choco.getX(), choco.getY(), 3);
                System.out.println("You Got it and +3 score for you");
                iterator.remove();
            }
        }
    }

    private void updateMysteryUI() {
        Iterator<Bonus> iterator = gameModel.getMysteries().iterator();
        while (iterator.hasNext()) {
            Bonus mystery = iterator.next();
            if (mystery.isTaken()) {
                mystery.getBonus().setVisible(false);
                System.out.println("You Got the mystery gift");
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
