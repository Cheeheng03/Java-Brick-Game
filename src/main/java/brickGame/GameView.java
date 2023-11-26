package brickGame;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameView {
    private final int sceneWidth = 500;
    private final int sceneHeight = 700;
    private Pane root;
    private Circle ball;
    private Rectangle paddleRect;
    private Label scoreLabel;
    private Label heartLabel;
    private Label levelLabel;
    private Button loadButton;
    private Button newGameButton;
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

        loadButton = new Button("Load Game");
        newGameButton = new Button("Start New Game");
        loadButton.setTranslateX(220);
        loadButton.setTranslateY(300);
        newGameButton.setTranslateX(220);
        newGameButton.setTranslateY(340);

    }

    public void addToRoot(GameModel gameModel, boolean loadFromSave){
        root.getChildren().clear();
        if (!loadFromSave) {
            root.getChildren().addAll(paddleRect, ball, scoreLabel, heartLabel, levelLabel, newGameButton, loadButton);
        } else {
            root.getChildren().addAll(paddleRect, ball, scoreLabel, heartLabel, levelLabel);
        }

        for (Block block : gameModel.getBlocks()) {
            root.getChildren().add(block.rect);
        }
    }

    public void updateLabels(GameModel gameModel) {
        scoreLabel.setText("Score : " + gameModel.getScore());
        heartLabel.setText("Heart : " + gameModel.getHeart());
        levelLabel.setText("Level : " + gameModel.getLevel());
    }

    public void setSceneToStage(Stage stage) {
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
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

    public void setLevelButtonsVisibility(boolean visible) {
        loadButton.setVisible(visible);
        newGameButton.setVisible(visible);
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
        block.rect.setVisible(false);
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

    public void resetGoldStatusUI() {
        ball.setFill(new ImagePattern(new Image("ball.png")));
        root.getStyleClass().remove("goldRoot");
        root.getStyleClass().add("bgImageRoot");
    }

    public void resetFreezeUI(){
        ball.setFill(new ImagePattern(new Image("ball.png")));
        paddleRect.setFill(new ImagePattern(new Image("block.jpg")));
    }

    public void updatePaddleUI(GameModel gameModel){
        Paddle paddle = gameModel.getPaddle();
        paddleRect.setX(paddle.getX());
        paddleRect.setY(paddle.getY());
        paddleRect.setWidth(paddle.getWidth());
    }

    public void showWin() {
        new Score().showWin(root);
    }

    // Method to display a message
    public void showMessage(String message) {
        new Score().showMessage(message, root);
    }
    public void show(final double x, final double y, int scoreAmount) {
        new Score().show(x, y, scoreAmount, root);
    }
    // Method to show game over message
    public void showGameOver(Score.GameRestartAction restartAction) {
        new Score().showGameOver(root, restartAction);
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

    public Pane getRoot(){
        return root;
    }
}
