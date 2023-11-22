package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private final int sceneWidth = 500;
    private final int sceneHeigt = 700;
    private static final int LEFT  = 1;
    private static final int RIGHT = 2;
    private Circle ball;
    private Rectangle rect;
    private long time     = 0;
    private GameEngine engine;
    public static String savePath    = "C:/save/save.mdds";
    public static String savePathDir = "C:/save/";
    public  Pane             root;
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;
    private boolean loadFromSave = false;
    private boolean isLevelTransitionInProgress = false;
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    Stage  primaryStage;
    Button load    = null;
    Button newGame = null;
    private GameModel gameModel;

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Called");
        this.primaryStage = primaryStage;

        if (gameModel == null) {
            gameModel = new GameModel();
        }
        createLabelsAndButtons();

        initializeGameElements();
        root = createRootPane();

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
            updateLabels();
            if (gameModel.getLevel() == 18) {
                new Score().showWin(this);
                load.setVisible(false);
                newGame.setVisible(false);
                return;
            }
            if (gameModel.getLevel() > 1 && gameModel.getLevel() <18) {
                new Score().showMessage("Level Up :)", this);
            }
            if (gameModel.getLevel() == 1) {
                load.setVisible(true);
                newGame.setVisible(true);
            } else {
                load.setVisible(false);
                newGame.setVisible(false);
            }


            initBreak();
            initBall();
            gameModel.initBoard();

            gameModel.setInitialBlockCount(gameModel.getBlocks().size());
            System.out.println(gameModel.getInitialBlockCount());
            System.out.println(gameModel.getDestroyedBlockCount());
        } else{
            updateLabels();
        }
    }

    private void updateLabels() {
        scoreLabel.setText("Score : " + gameModel.getScore());
        heartLabel.setText("Heart : " + gameModel.getHeart());
        levelLabel.setText("Level : " + gameModel.getLevel());
    }

    private Pane createRootPane() {
        Pane root = new Pane();
        root.getStyleClass().add("bgImageRoot");

        if (!loadFromSave) {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel, newGame, load);
        } else {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
        }

        for (Block block : gameModel.getBlocks()) {
            root.getChildren().add(block.rect);
        }

        Scene scene = new Scene(root, sceneWidth, sceneHeigt);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        return root;
    }

    private void createLabelsAndButtons() {
        scoreLabel = new Label("Score : ");
        heartLabel = new Label("Heart : ");
        levelLabel = new Label("Level : ");

        levelLabel.setTranslateY(20);
        heartLabel.setTranslateX(sceneWidth - 75);

        load = new Button("Load Game");
        newGame = new Button("Start New Game");
        load.setTranslateX(220);
        load.setTranslateY(300);
        newGame.setTranslateX(220);
        newGame.setTranslateY(340);
    }
    private void initializeGameButtons() {
        load.setOnAction(event -> {
            loadGame();
            load.setVisible(false);
            newGame.setVisible(false);
        });

        newGame.setOnAction(event -> {
            engine = new GameEngine();
            engine.setOnAction(Main.this);
            engine.setFps(120);
            engine.start();

            load.setVisible(false);
            newGame.setVisible(false);
        });
    }

    private void setupButtonActions() {
        if (gameModel.getLevel() > 1 && gameModel.getLevel() < 18) {
            load.setVisible(false);
            newGame.setVisible(false);
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

    private void initBall() {
        createBall();
    }

    private void createBall() {
        ball = new Circle(gameModel.getGameball().getX(), gameModel.getGameball().getY(), gameModel.getGameball().getRadius(),new ImagePattern(new Image("ball.png")));
    }

    private void initBreak() {
        rect = createBreakRectangle();
        rect.setFill(createBreakFill());
    }

    private Rectangle createBreakRectangle() {
        return new Rectangle(gameModel.getPaddle().getX(), gameModel.getPaddle().getY(), gameModel.getPaddle().getWidth(), gameModel.getPaddle().getHeight());
    }

    private Paint createBreakFill() {
        return new ImagePattern(new Image("block.jpg"));
    }

    private void setPhysicsToBall() {
        gameModel.setPhysicsToBall();
        if (gameModel.checkHeartDecrement()) {
            handleGameEndScenario();
        }
    }

    private void handleGameEndScenario() {
        new Score().show((double) sceneWidth / 2, (double) sceneHeigt / 2, -1, this);
        if (gameModel.getHeart() == 0) {
            new Score().showGameOver(this);
            engine.stop();
        }
    }

    private void saveGame() {
        if (gameModel.getLevel() < 18 && gameModel.getHeart() > 0) {
            LoadSave loadSave = new LoadSave();
            loadSave.saveGameState(gameModel);
            new Score().showMessage("Game Saved", Main.this);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("The game is over and cannot be saved.");
            alert.showAndWait();
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

    public void restartGame() {
        Platform.runLater(() -> {
            try {
                gameModel.resetGameForRestart();
                start(primaryStage);
            } catch (Exception e) {
                handleException(e);
            }
        });
    }

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
            scoreLabel.setText("Score : " + gameModel.getScore());
            heartLabel.setText("Heart : " + gameModel.getHeart());

            rect.setX(gameModel.getPaddle().getX());
            rect.setY(gameModel.getPaddle().getY());
            ball.setCenterX(gameModel.getGameball().getX());
            ball.setCenterY(gameModel.getGameball().getY());

            for (Bonus choco : gameModel.getChocos()) {
                choco.getChoco().setY(choco.getY());
            }
        });
    }

    private void updateUIAfterBlockRemoval() {
        for (Block block : gameModel.getBlocksToRemove()) {
            Platform.runLater(() -> block.rect.setVisible(false ));
        }
    }

    private void updateBlockCollisionsUI() {
        for (Block block : gameModel.getBlocks()) {
            if (block.isDestroyed) {
                new Score().show(block.x, block.y, 1, this);

                if (block.type == Block.BLOCK_CHOCO) {
                    final Bonus choco = new Bonus(block.row, block.column);
                    gameModel.addChoco(choco);
                    Platform.runLater(() -> root.getChildren().add(choco.getChoco()));
                } else if (block.type == Block.BLOCK_STAR) {
                    Platform.runLater(() -> {
                        ball.setFill(new ImagePattern(new Image("goldball.jpeg")));
                        root.getStyleClass().remove("bgImageRoot");
                        root.getStyleClass().add("goldRoot");
                    });
                } else if (block.type == Block.BLOCK_HEART) {
                    new Score().showMessage("Heart +1", this);
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
            resetGoldStatusUI();
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
                new Score().show(choco.getX(), choco.getY(), 3, this);
                System.out.println("You Got it and +3 score for you");

                iterator.remove();
            }
        }
    }

    private void resetGoldStatusUI() {
        ball.setFill(new ImagePattern(new Image("ball.png")));
        root.getStyleClass().remove("goldRoot");
        root.getStyleClass().add("bgImageRoot");
    }


    @Override
    public void onTime(long time) {
        this.time = time;
    }

    private void handleException(Exception e) {
        logger.log(Level.SEVERE, "An error occurred: " + e.getMessage(), e);
    }
}
