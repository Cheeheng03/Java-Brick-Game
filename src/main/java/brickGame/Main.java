package brickGame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {

    private int level = 0;

    private double xBreak = 0.0f;
    private double centerBreakX;
    private double yBreak = 640.0f;

    private int breakWidth     = 130;
    private int breakHeight    = 30;
    private int halfBreakWidth = breakWidth / 2;

    private int sceneWidth = 500;
    private int sceneHeigt = 700;

    private static int LEFT  = 1;
    private static int RIGHT = 2;

    private Circle ball;
    private double xBall;
    private double yBall;
    private double xBallPrevious;
    private double yBallPrevious;

    private boolean isGoldStauts      = false;
    private boolean isExistHeartBlock = false;

    private Rectangle rect;
    private int       ballRadius = 10;

    private double v = 1.000;

    private int  heart    = 3;
    private int  score    = 0;
    private long time     = 0;
    private long hitTime  = 0;
    private long goldTime = 0;
    private int initialBlockCount;
    private int destroyedBlockCount = 0;

    private GameEngine engine;
    public static String savePath    = "C:/save/save.mdds";
    public static String savePathDir = "C:/save/";

    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Bonus> chocos = new ArrayList<Bonus>();

    public enum GameState {
        RUNNING,
        NEXT_LEVEL_TRANSITION
    }
    public  Pane             root;
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;

    private boolean loadFromSave = false;
    private Queue<Block> blocksToRemove = new LinkedList<>();
    private boolean readyForNextLevel = false;

    private Timeline moveTimeline;

    Stage  primaryStage;
    Button load    = null;
    Button newGame = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

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
            level++;
            levelLabel.setText("Level : " + level);
            if (level > 1) {
                new Score().showMessage("Level Up :)", this);
            }
            if (level == 18) {
                new Score().showWin(this);
                return;
            }

            if (level == 1) {
                load.setVisible(true);
                newGame.setVisible(true);
            } else {
                load.setVisible(false);
                newGame.setVisible(false);
            }

            initBall();
            initBreak();
            initBoard();

            initialBlockCount = blocks.size();
        }
    }


    private Pane createRootPane() {
        Pane root = new Pane();
        root.getStyleClass().add("bgImageRoot");

        if (!loadFromSave) {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel, newGame, load);
        } else {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
        }

        for (Block block : blocks) {
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
        scoreLabel = new Label("Score : 0");
        heartLabel = new Label("Heart : 3");
        levelLabel = new Label("Level : 1");

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
        if (level > 1 && level < 18) {
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


    private void initBoard() {
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(KeyEvent event) {
        if(event.getCode() == KeyCode.LEFT) {
            move(LEFT);
        } else if(event.getCode() == KeyCode.RIGHT) {
            move(RIGHT);
        } else if(event.getCode() == KeyCode.DOWN) {
            //setPhysicsToBall(); // Assuming you have physics method for ball
        } else if(event.getCode() == KeyCode.S) {
            saveGame();
        }
    }

    private void move(final int direction) {
        new Thread(() -> {
            int sleepTime = 4;
            int maxIterations = 30;

            for (int i = 0; i < maxIterations; i++) {
                if ((xBreak == 0 && direction == LEFT) || (xBreak == (sceneWidth - breakWidth) && direction == RIGHT)) {
                    return;
                }

                if (direction == RIGHT) {
                    xBreak++;
                } else {
                    xBreak--;
                }

                centerBreakX = xBreak + halfBreakWidth;

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (i >= 20) {
                    sleepTime = i;
                }
            }
        }).start();
    }


    private void initBall() {
        Random random = new Random();

        xBall = random.nextInt(sceneWidth) + 1;

        int blocksBottomY = (int) ((level + 1) * Block.getHeight() + Block.getPaddingTop());
        int paddleTopY = (int) yBreak;

        int ballMinY = blocksBottomY + ballRadius;
        int ballMaxY = paddleTopY - ballRadius;

        yBall = (ballMinY < ballMaxY) ? random.nextInt(ballMaxY - ballMinY) + ballMinY : ballMinY;

        createBall();
    }

    private void createBall() {
        ball = new Circle(xBall, yBall, ballRadius, new ImagePattern(new Image("ball.png")));
    }


    private void initBreak() {
        rect = createBreakRectangle();
        rect.setFill(createBreakFill());
    }

    private Rectangle createBreakRectangle() {
        return new Rectangle(xBreak, yBreak, breakWidth, breakHeight);
    }

    private Paint createBreakFill() {
        return new ImagePattern(new Image("block.jpg"));
    }



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

    private double vX = 1.000;
    private double vY = 1.000;


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

    private void setPhysicsToBall() {
        updateBallPosition();
        handleGameOverConditions();
        handleBreakCollisions();
        handleWallCollisions();
        handleBlockCollisions();
    }

    private void updateBallPosition() {
        yBall += goDownBall ? vY : -vY;
        xBall += goRightBall ? vX : -vX;
    }



    private void handleBreakCollisions() {
        if (yBall >= yBreak - ballRadius && xBall >= xBreak && xBall <= xBreak + breakWidth) {
            calculateBallDirectionAfterBreakCollision();
        }
    }

    private void calculateBallDirectionAfterBreakCollision() {
        hitTime = time;
        resetColideFlags();
        colideToBreak = true;
        goDownBall = false;

        double relation = (xBall - centerBreakX) / ((double) breakWidth / 2);
        vX = calculateVelocityX(relation, level);
        colideToBreakAndMoveToRight = xBall - centerBreakX > 0;
        goRightBall = colideToBreakAndMoveToRight;
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
        if (yBall <= 0 || yBall >= sceneHeigt) {
            resetColideFlags();
            goDownBall = yBall <= 0;

            if (!isGoldStauts && yBall >= sceneHeigt) {
                handleGameEndScenario();
            }
        }
    }

    private void handleGameEndScenario() {
        heart--;
        new Score().show((double) sceneWidth / 2, (double) sceneHeigt / 2, -1, this);

        if (heart == 0) {
            new Score().showGameOver(this);
            engine.stop();
        }
    }

    private void handleWallCollisions() {
        if (xBall >= sceneWidth || xBall <= 0) {
            resetColideFlags();
            colideToRightWall = xBall >= sceneWidth;
            goRightBall = xBall <= 0;
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
        goDownBall = false;
    }

    private void handleBottomBlockCollision() {
        goDownBall = true;
    }

    private void handleLeftBlockCollision() {
        goRightBall = false;
    }

    private void handleRightBlockCollision() {
        goRightBall = true;
    }

    private void handleTopLeftBlockCollision() {
        if (!goDownBall) {
            goRightBall = false;
        } else if (!goRightBall) {
            goDownBall = false;
        } else {
            goDownBall = false;
        }
    }

    private void handleTopRightBlockCollision() {
        if (!goDownBall) {
            goRightBall = true;
        } else if (goRightBall) {
            goDownBall = false;
        } else {
            goDownBall = false;
        }
    }

    private void handleBottomLeftBlockCollision() {
        if (goDownBall) {
            goRightBall = false;
        } else if (!goRightBall) {
            goDownBall = true;
        } else {
            goDownBall = true;
        }
    }

    private void handleBottomRightBlockCollision() {
        if (goDownBall) {
            goRightBall = true;
        } else if (goRightBall) {
            goDownBall = true;
        } else {
            goDownBall = true;
        }
    }


    private GameState currentState = GameState.RUNNING;
    private void checkDestroyedCount() {
        if (destroyedBlockCount == initialBlockCount && currentState == GameState.RUNNING) {
            currentState = GameState.NEXT_LEVEL_TRANSITION;
            nextLevel();
        }
    }

    private void saveGame() {
        if (level < 18 && heart > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new File(savePathDir).mkdirs();
                    File file = new File(savePath);
                    ObjectOutputStream outputStream = null;
                    try {
                        outputStream = new ObjectOutputStream(new FileOutputStream(file));

                        outputStream.writeInt(level);
                        outputStream.writeInt(score);
                        outputStream.writeInt(heart);
                        outputStream.writeInt(destroyedBlockCount);

                        outputStream.writeDouble(xBall);
                        outputStream.writeDouble(yBall);
                        outputStream.writeDouble(xBreak);
                        outputStream.writeDouble(yBreak);
                        outputStream.writeDouble(centerBreakX);
                        outputStream.writeLong(time);
                        outputStream.writeLong(goldTime);
                        outputStream.writeDouble(vX);

                        outputStream.writeBoolean(isExistHeartBlock);
                        outputStream.writeBoolean(isGoldStauts);
                        outputStream.writeBoolean(goDownBall);
                        outputStream.writeBoolean(goRightBall);
                        outputStream.writeBoolean(colideToBreak);
                        outputStream.writeBoolean(colideToBreakAndMoveToRight);
                        outputStream.writeBoolean(colideToRightWall);
                        outputStream.writeBoolean(colideToLeftWall);
                        outputStream.writeBoolean(colideToRightBlock);
                        outputStream.writeBoolean(colideToBottomBlock);
                        outputStream.writeBoolean(colideToLeftBlock);
                        outputStream.writeBoolean(colideToTopBlock);

                        ArrayList<BlockSerializable> blockSerializables = new ArrayList<BlockSerializable>();
                        for (Block block : blocks) {
                            if (block.isDestroyed) {
                                continue;
                            }
                            blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
                        }

                        outputStream.writeObject(blockSerializables);

                        new Score().showMessage("Game Saved", Main.this);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {
            // Display a message indicating that the game is over and cannot be saved
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


        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStauts = loadSave.isGoldStauts;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        colideToBreak = loadSave.colideToBreak;
        colideToBreakAndMoveToRight = loadSave.colideToBreakAndMoveToRight;
        colideToRightWall = loadSave.colideToRightWall;
        colideToLeftWall = loadSave.colideToLeftWall;
        colideToRightBlock = loadSave.colideToRightBlock;
        colideToBottomBlock = loadSave.colideToBottomBlock;
        colideToLeftBlock = loadSave.colideToLeftBlock;
        colideToTopBlock = loadSave.colideToTopBlock;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        destroyedBlockCount = loadSave.destroyedBlockCount;
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xBreak = loadSave.xBreak;
        yBreak = loadSave.yBreak;
        centerBreakX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        blocks.clear();
        chocos.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, ser.type));
        }


        try {
            loadFromSave = true;
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void nextLevel() {
        Platform.runLater(() -> {
            try {
                initializeNextLevel();
                resetGameElements();
                start(primaryStage);

            } catch (Exception e) {
                handleException(e);

            } finally {
                currentState = GameState.RUNNING;
            }
        });

        readyForNextLevel = false;
    }

    private void initializeNextLevel() {
        vX = 1.000;
        resetColideFlags();
        goDownBall = true;
        isGoldStauts = false;
        isExistHeartBlock = false;
        hitTime = 0;
        time = 0;
        goldTime = 0;
    }

    private void resetGameElements() {
        engine.stop();
        blocks.clear();
        chocos.clear();
        destroyedBlockCount = 0;
    }

    private void handleException(Exception e) {
        e.printStackTrace();
    }

    public void restartGame() {
        Platform.runLater(() -> {
            try {
                level = 0;
                heart = 3;
                score = 0;
                vX = 1.000;
                destroyedBlockCount = 0;
                resetColideFlags();
                goDownBall = true;

                isGoldStauts = false;
                isExistHeartBlock = false;
                hitTime = 0;
                time = 0;
                goldTime = 0;

                blocks.clear();
                chocos.clear();

                start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onUpdate() {
        updateLabelsAndPosition();
        checkBallPosition();

        if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
            updateBlockCollisions();
        }

        removeDestroyedBlocks();
    }

    private void updateLabelsAndPosition() {
        xBallPrevious = xBall;
        yBallPrevious = yBall;

        Platform.runLater(() -> {
            scoreLabel.setText("Score : " + score);
            heartLabel.setText("Heart : " + heart);

            rect.setX(xBreak);
            rect.setY(yBreak);
            ball.setCenterX(xBall);
            ball.setCenterY(yBall);

            for (Bonus choco : chocos) {
                choco.choco.setY(choco.y);
            }
        });
    }

    private void checkBallPosition() {
        if (yBall >= sceneHeigt) {
            goDownBall = false;
            setPhysicsToBall();
        }
    }

    private void updateBlockCollisions() {
        for (final Block block : blocks) {
            int hitCode = block.checkHitToBlock(xBall, yBall, xBallPrevious, yBallPrevious, ballRadius);
            if (hitCode != Block.NO_HIT) {
                score += 1;
                new Score().show(block.x, block.y, 1, this);
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
            Platform.runLater(() -> root.getChildren().add(choco.choco));
            chocos.add(choco);
        } else if (block.type == Block.BLOCK_STAR) {
            goldTime = time;
            Platform.runLater(() -> {
                ball.setFill(new ImagePattern(new Image("goldball.jpeg")));
                root.getStyleClass().remove("bgImageRoot");
                root.getStyleClass().add("goldRoot");
            });
            isGoldStauts = true;
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

    private void removeDestroyedBlocks() {
        Iterator<Block> iterator = blocks.iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (blocksToRemove.contains(block)) {
                iterator.remove();  // Safe removal using iterator
                Platform.runLater(() -> block.rect.setVisible(false));
                destroyedBlockCount++;
            }
        }
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();
        updateGoldStatus();
        updateChocos();

        // System.out.println("time is:" + time + " goldTime is " + goldTime);
    }

    private void updateGoldStatus() {
        if (time - goldTime > 5000) {
            resetGoldStatus();
        }
    }

    private void resetGoldStatus() {
        ball.setFill(new ImagePattern(new Image("ball.png")));
        root.getStyleClass().remove("goldRoot");
        root.getStyleClass().add("bgImageRoot");
        isGoldStauts = false;
    }

    private void updateChocos() {
        for (Bonus choco : chocos) {
            if (shouldSkipChoco(choco)) {
                continue;
            }

            handleChocoCollision(choco);
            updateChocoPosition(choco);
        }
    }

    private boolean shouldSkipChoco(Bonus choco) {
        return choco.y > sceneHeigt || choco.taken;
    }

    private void handleChocoCollision(Bonus choco) {
        if (choco.y >= yBreak && choco.y <= yBreak + breakHeight
                && choco.x >= xBreak && choco.x <= xBreak + breakWidth) {
            processChocoCollision(choco);
        }
    }

    private void processChocoCollision(Bonus choco) {
        System.out.println("You Got it and +3 score for you");
        choco.taken = true;
        choco.choco.setVisible(false);
        score += 3;
        new Score().show(choco.x, choco.y, 3, this);
    }

    private void updateChocoPosition(Bonus choco) {
        choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
    }



    @Override
    public void onTime(long time) {
        this.time = time;
    }
}
