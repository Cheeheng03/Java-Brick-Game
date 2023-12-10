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

/**
 * Represents the graphical user interface of the brick game.
 * Manages the rendering of game elements like blocks, ball, paddle, and UI components such as buttons and labels.
 */
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

    /**
     * Constructs a new GameView object.
     * Initializes the root pane for rendering game elements.
     */
    public GameView() {
        root = new Pane();
    }

    /**
     * Initializes the user interface elements of the game.
     *
     * @param gameModel The game model containing the game's data.
     * @param loadFromSave Indicates whether the UI is being initialized from a saved game state.
     */
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

    /**
     * Initializes the main menu UI, including buttons and background.
     */
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

    /**
     * Adds game elements to the root pane for rendering.
     *
     * @param gameModel The game model containing the game's data.
     * @param loadFromSave Indicates whether the elements are being added from a saved game state.
     */
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

    /**
     * Updates the game's labels such as score, heart count, and level.
     *
     * @param gameModel The game model containing the data to be displayed.
     */
    public void updateLabels(GameModel gameModel) {
        scoreLabel.setText("Score : " + gameModel.getScore());
        heartLabel.setText("Heart : " + gameModel.getHeart());
        levelLabel.setText("Level : " + gameModel.getLevel());
    }

    /**
     * Changes the scene to the game view.
     *
     * @param stage The primary stage of the application.
     */
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

    /**
     * Sets the main menu scene to the primary stage.
     *
     * @param stage The primary stage of the application where the scene is set.
     */
    public void setSceneToStage(Stage stage) {
        Scene scene = new Scene(mainMenuPane, sceneWidth, sceneHeight);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the graphical representations of the ball and paddle.
     *
     * @param gameModel The game model containing the ball and paddle data.
     */
    public void initBallAndPaddle(GameModel gameModel) {
        ball = new Circle(gameModel.getGameball().getX(), gameModel.getGameball().getY(), gameModel.getGameball().getRadius(),new ImagePattern(new Image("ball.png")));
        paddleRect = new Rectangle(gameModel.getPaddle().getX(), gameModel.getPaddle().getY(), gameModel.getPaddle().getWidth(), gameModel.getPaddle().getHeight());
        paddleRect.setFill(new ImagePattern(new Image("block.jpg")));
        root.getChildren().addAll(ball, paddleRect);
    }

    /**
     * Updates the labels and ball position based on the game model.
     *
     * @param gameModel The game model containing the updated data.
     */
    public void updateLabelsAndBall(GameModel gameModel) {
        scoreLabel.setText("Score : " + gameModel.getScore());
        heartLabel.setText("Heart : " + gameModel.getHeart());
        levelLabel.setText("Level : " + gameModel.getLevel());

        paddleRect.setX(gameModel.getPaddle().getX());
        paddleRect.setY(gameModel.getPaddle().getY());
        ball.setCenterX(gameModel.getGameball().getX());
        ball.setCenterY(gameModel.getGameball().getY());
    }

    /**
     * Sets the visibility of blocks to 'not visible' after they are removed from the game.
     *
     * @param block The block that has been removed and should no longer be visible.
     */
    public void setNotVisibleAfterBlockRemoval(Block block) {
        block.getBlockView().getRect().setVisible(false);
        if (block.type == Block.BLOCK_COUNT_BREAKER && block.blockText != null) {
            block.getBlockView().getBlockText().setVisible(false);
        }
    }

    /**
     * Adds a bonus UI element to the game view.
     *
     * @param bonus The bonus object to be added to the UI.
     */
    public void addBonusUI(Bonus bonus){
        root.getChildren().add(bonus.getBonus());
    }

    /**
     * Changes the UI to reflect the 'Gold' status of the game.
     * Alters the appearance of the ball and the game background.
     */
    public void addGoldRoot(){
        ball.setFill(new ImagePattern(new Image("goldball.jpeg")));
        root.getStyleClass().remove("goldRoot");
        root.getStyleClass().remove("bgImageRoot");
        root.getStyleClass().add("goldRoot");
        System.out.println("Gold Ball");
    }

    /**
     * Changes the UI to reflect the 'Freeze' status of the game.
     * Alters the appearance of the ball and the paddle.
     */
    public void addFreezeRoot(){
        ball.setFill(new ImagePattern(new Image("iceBall.jpeg")));
        paddleRect.setFill(new ImagePattern(new Image("lockPaddle.jpg")));
        System.out.println("Oh No! Paddle frozen for 3 seconds");
    }

    /**
     * Changes the UI to reflect the 'Ghost' status of the game.
     * Makes the ball invisible.
     */
    public void addGhostUI(){
        ball.setVisible(false);
        System.out.println("Oh No! Ball became invisible for 1.5 seconds");;
    }

    /**
     * Resets the UI to its normal state from the 'Gold' status.
     */
    public void resetGoldStatusUI() {
        ball.setFill(new ImagePattern(new Image("ball.png")));
        root.getStyleClass().remove("goldRoot");
        root.getStyleClass().add("bgImageRoot");
    }

    /**
     * Resets the UI to its normal state from the 'Freeze' status.
     */
    public void resetFreezeUI(){
        ball.setFill(new ImagePattern(new Image("ball.png")));
        paddleRect.setFill(new ImagePattern(new Image("block.jpg")));
    }

    /**
     * Resets the UI to its normal state from the 'Ghost' status.
     */
    public void resetGhostUI(){
        ball.setVisible(true);
    }

    /**
     * Updates the UI of the paddle based on its current state in the game model.
     *
     * @param gameModel The game model containing the updated state of the paddle.
     */
    public void updatePaddleUI(GameModel gameModel){
        Paddle paddle = gameModel.getPaddle();
        paddleRect.setX(paddle.getX());
        paddleRect.setY(paddle.getY());
        paddleRect.setWidth(paddle.getWidth());
    }

    /**
     * Changes the pause button's UI to indicate that the game can be resumed.
     */
    public void resumeUI(){
        pauseButton.setText("\u25B6");
    }

    /**
     * Changes the pause button's UI to indicate that the game can be paused.
     */
    public void pauseUI(){
        pauseButton.setText("\u23F8");
    }

    /**
     * Displays a win message and the option to restart the game.
     *
     * @param restartAction The action to perform when the game is restarted.
     * @param gameModel The game model containing the current game state.
     */
    public void showWin(Score.GameRestartAction restartAction, GameModel gameModel) {
        new Score().showWin(root, restartAction, gameModel);
    }

    /**
     * Displays a message within the game view.
     *
     * @param message The message to be displayed.
     */
    public void showMessage(String message) {
        new Score().showMessage(message, root);
    }

    /**
     * Displays a score effect at a specific position in the game view.
     *
     * @param x The x-coordinate for the score effect.
     * @param y The y-coordinate for the score effect.
     * @param scoreAmount The score amount to display.
     */
    public void show(final double x, final double y, int scoreAmount) {
        new Score().show(x, y, scoreAmount, root);
    }

    /**
     * Displays a game over message and the option to restart the game.
     *
     * @param restartAction The action to perform when the game is restarted.
     * @param gameModel The game model containing the current game state.
     */
    public void showGameOver(Score.GameRestartAction restartAction, GameModel gameModel) {
        new Score().showGameOver(root, restartAction, gameModel);
    }

    /**
     * Shows an alert indicating that the game is over and cannot be saved.
     */
    public void showGameOverAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("The game is over and cannot be saved.");
        alert.showAndWait();
    }

    // Getters
    /**
     * Retrieves the button used for loading a saved game.
     *
     * @return The button for loading a game.
     */
    public Button getLoadButton() {
        return loadButton;
    }

    /**
     * Retrieves the button used for starting a new game.
     *
     * @return The button for starting a new game.
     */
    public Button getNewGameButton() {
        return newGameButton;
    }

    /**
     * Retrieves the button used for pausing and resuming the game.
     *
     * @return The button for pausing and resuming the game.
     */
    public Button getPauseButton() {
        return pauseButton;
    }


}
