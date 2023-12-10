package brickGame;

import brickGame.Controller.GameController;
import brickGame.Model.GameModel;
import brickGame.View.GameView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main class for the brick game application.
 * Initializes and starts the JavaFX application, setting up the game's model, view, and controller.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application and initializes the game components.
     * Sets up the game model, view, and controller, and starts the game.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        GameModel gameModel = new GameModel();
        GameView gameView = new GameView();

        GameController gameController = new GameController(gameModel, gameView);
        gameController.start(primaryStage);
    }

    /**
     * The main entry point for the application.
     * Launches the JavaFX application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}