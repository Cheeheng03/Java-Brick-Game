package brickGame;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameModel gameModel = new GameModel();
        GameView gameView = new GameView();

        GameController gameController = new GameController(gameModel, gameView);
        gameController.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}