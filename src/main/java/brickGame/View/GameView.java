package brickGame.View;

import brickGame.Model.Block;
import brickGame.Model.Bonus;
import brickGame.Model.GameModel;
import brickGame.Model.Paddle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameView {
    private final int sceneWidth = 500;
    private final int sceneHeight = 700;
    private Pane root;
    private Pane mainMenuPane;
    private ImageView backgroundImageView;
    private Circle ball;
    private Rectangle paddleRect;
    private Label scoreLabel;
    private Label heartLabel;
    private Label levelLabel;
    private Button loadButton;
    private Button newGameButton;
    private  Button pauseButton;
    Stage stage;

    public GameView() {
        root = new Pane();
    }

    public void initializeUI(GameModel gameModel, boolean loadFromSave) {

        root.getStyleClass().add("bgImageRoot");

        scoreLabel = new Label("Score : ");
        heartLabel = new Label("Heart : ");
        levelLabel = new Label("Level : ");

        levelLabel.setTranslateY(20);
        heartLabel.setTranslateX(sceneWidth - 75);

        pauseButton = new Button("\u23F8");
        pauseButton.setTranslateX(240);
        pauseButton.setTranslateY(0);

        double buttonWidth = 30;
        double buttonHeight = 30;
        pauseButton.setMinSize(buttonWidth, buttonHeight);
        pauseButton.setMaxSize(buttonWidth, buttonHeight);
        pauseButton.setPrefSize(buttonWidth, buttonHeight);

        pauseButton.setVisible(false);

    }

    public void initializeMainMenu() {
        mainMenuPane = new Pane();

        Image backgroundImage = new Image("mainBG.png");
        backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(sceneWidth);
        backgroundImageView.setFitHeight(sceneHeight);

        loadButton = new Button("Load Game");
        newGameButton = new Button("Start New Game");

        loadButton.setStyle("-fx-background-color: #0e3958; -fx-text-fill: #ffeb97;");
        newGameButton.setStyle("-fx-background-color: #0e3958; -fx-text-fill: #ffeb97;");

        loadButton.setMinSize(150, 40);
        newGameButton.setMinSize(150, 40);

        loadButton.setTextFill(Color.WHITE);
        newGameButton.setTextFill(Color.WHITE);

        loadButton.setTranslateX(175);
        loadButton.setTranslateY(340);
        newGameButton.setTranslateX(175);
        newGameButton.setTranslateY(400);

        mainMenuPane.getChildren().addAll(backgroundImageView, loadButton, newGameButton);
    }

    public void addToRoot(GameModel gameModel, boolean loadFromSave){
        root.getChildren().clear();
        if (!loadFromSave) {
            root.getChildren().addAll(paddleRect, ball, scoreLabel, heartLabel, levelLabel, pauseButton);
        } else {
            root.getChildren().addAll(paddleRect, ball, scoreLabel, heartLabel, levelLabel, pauseButton);
        }

        for (Block block : gameModel.getBlocks()) {
            root.getChildren().add(block.getBlockView().getRect());
            if (block.type == Block.BLOCK_COUNT_BREAKER && block.blockText != null) {
                root.getChildren().add(block.getBlockView().getBlockText());
            }
        }
    }

    public void updateLabels(GameModel gameModel) {
        scoreLabel.setText("Score : " + gameModel.getScore());
        heartLabel.setText("Heart : " + gameModel.getHeart());
        levelLabel.setText("Level : " + gameModel.getLevel());
    }

    public void changeSceneToGame(Stage stage) {
        Scene currentScene = stage.getScene();
        if (currentScene != null) {
            currentScene.setRoot(root);
        } else {
            currentScene = new Scene(root, sceneWidth, sceneHeight);
            currentScene.getStylesheets().add("style.css");
            stage.setScene(currentScene);
        }
    }

    public void setSceneToStage(Stage stage) {
        Scene scene = new Scene(mainMenuPane, sceneWidth, sceneHeight);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void initBallAndPaddle(GameModel gameModel) {
        ball = new Circle(gameModel.getGameball().getX(), gameModel.getGameball().getY(), gameModel.getGameball().getRadius(),new ImagePattern(new Image("ball.png")));
        paddleRect = new Rectangle(gameModel.getPaddle().getX(), gameModel.getPaddle().getY(), gameModel.getPaddle().getWidth(), gameModel.getPaddle().getHeight());
        paddleRect.setFill(new ImagePattern(new Image("block.jpg")));
        root.getChildren().addAll(ball, paddleRect);
    }

    public void updateLabelsAndBall(GameModel gameModel) {
        scoreLabel.setText("Score : " + gameModel.getScore());
        heartLabel.setText("Heart : " + gameModel.getHeart());
        levelLabel.setText("Level : " + gameModel.getLevel());

        paddleRect.setX(gameModel.getPaddle().getX());
        paddleRect.setY(gameModel.getPaddle().getY());
        ball.setCenterX(gameModel.getGameball().getX());
        ball.setCenterY(gameModel.getGameball().getY());
    }

    public void setNotVisibleAfterBlockRemoval(Block block) {
        block.getBlockView().getRect().setVisible(false);
        if (block.type == Block.BLOCK_COUNT_BREAKER && block.blockText != null) {
            block.getBlockView().getBlockText().setVisible(false);
        }
    }

    public void addBonusUI(Bonus bonus){
        root.getChildren().add(bonus.getBonus());
    }
    public void addGoldRoot(){
        ball.setFill(new ImagePattern(new Image("goldball.jpeg")));
        root.getStyleClass().remove("goldRoot");
        root.getStyleClass().remove("bgImageRoot");
        root.getStyleClass().add("goldRoot");
        System.out.println("Gold Ball");
    }

    public void addFreezeRoot(){
        ball.setFill(new ImagePattern(new Image("iceBall.jpeg")));
        paddleRect.setFill(new ImagePattern(new Image("lockPaddle.jpg")));
        System.out.println("Oh No! Paddle frozen for 3 seconds");
    }

    public void addGhostUI(){
        ball.setVisible(false);
        System.out.println("Oh No! Ball became invisible for 1.5 seconds");;
    }
    public void resetGoldStatusUI() {
        ball.setFill(new ImagePattern(new Image("ball.png")));
        root.getStyleClass().remove("goldRoot");
        root.getStyleClass().add("bgImageRoot");
    }

    public void resetFreezeUI(){
        ball.setFill(new ImagePattern(new Image("ball.png")));
        paddleRect.setFill(new ImagePattern(new Image("block.jpg")));
    }

    public void resetGhostUI(){
        ball.setVisible(true);
    }

    public void updatePaddleUI(GameModel gameModel){
        Paddle paddle = gameModel.getPaddle();
        paddleRect.setX(paddle.getX());
        paddleRect.setY(paddle.getY());
        paddleRect.setWidth(paddle.getWidth());
    }

    public void resumeUI(){
        pauseButton.setText("\u25B6");
    }

    public void pauseUI(){
        pauseButton.setText("\u23F8");
    }
    public void showWin(Score.GameRestartAction restartAction, GameModel gameModel) {
        new Score().showWin(root, restartAction, gameModel);
    }

    // Method to display a message
    public void showMessage(String message) {
        new Score().showMessage(message, root);
    }
    public void show(final double x, final double y, int scoreAmount) {
        new Score().show(x, y, scoreAmount, root);
    }
    // Method to show game over message
    public void showGameOver(Score.GameRestartAction restartAction, GameModel gameModel) {
        new Score().showGameOver(root, restartAction, gameModel);
    }

    public void showGameOverAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("The game is over and cannot be saved.");
        alert.showAndWait();
    }
    // Getters for UI components
    public Button getLoadButton() {
        return loadButton;
    }

    public Button getNewGameButton() {
        return newGameButton;
    }
    public Button getPauseButton() {
        return pauseButton;
    }

    public Pane getRoot(){
        return root;
    }

}