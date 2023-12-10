package brickGame.View;

import brickGame.Model.GameModel;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Handles the display of scoring, messages, and end-game screens in the brick game.
 * Provides functionalities for showing score effects, messages, game over, and win screens.
 */
public class Score {

    /**
     * Displays a score effect at the specified position.
     *
     * @param x The x-coordinate where the score effect is to be displayed.
     * @param y The y-coordinate where the score effect is to be displayed.
     * @param scoreAmount The amount of score to display.
     * @param root The pane where the score label is to be added.
     */
    public void show(final double x, final double y, int scoreAmount, Pane root) {
        String sign = scoreAmount >= 0 ? "+" : "";
        final Label label = new Label(sign + scoreAmount);
        label.setTranslateX(x);
        label.setTranslateY(y);

        root.getChildren().add(label);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(315), new KeyValue(label.scaleXProperty(), 2), new KeyValue(label.scaleYProperty(), 2)),
                new KeyFrame(Duration.seconds(1.5), new KeyValue(label.opacityProperty(), 0))
        );
        timeline.setOnFinished(event -> root.getChildren().remove(label));
        timeline.play();
    }

    /**
     * Displays a message on the screen.
     *
     * @param message The message to be displayed.
     * @param root The pane where the message label is to be added.
     */
    public void showMessage(String message, Pane root) {
        final Label label = new Label(message);
        label.setTranslateX(220);
        label.setTranslateY(340);

        Platform.runLater(() -> root.getChildren().add(label));

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(label.scaleXProperty(), 1),
                        new KeyValue(label.scaleYProperty(), 1),
                        new KeyValue(label.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(750),
                        new KeyValue(label.scaleXProperty(), 2),
                        new KeyValue(label.scaleYProperty(), 2)),
                new KeyFrame(Duration.millis(1500),
                        new KeyValue(label.scaleXProperty(), 1),
                        new KeyValue(label.scaleYProperty(), 1)),
                new KeyFrame(Duration.seconds(3.5),
                        new KeyValue(label.opacityProperty(), 0))
        );

        timeline.setOnFinished(e -> root.getChildren().remove(label));
        timeline.play();
    }

    /**
     * Functional interface for defining the game restart action.
     */
    @FunctionalInterface
    public interface GameRestartAction {
        void restart();
    }

    /**
     * Displays the game over screen with the final score and a restart option.
     *
     * @param root The pane where the game over screen is to be displayed.
     * @param restartAction The action to be performed when restarting the game.
     * @param gameModel The game model containing the final score data.
     */
    public void showGameOver(Pane root, GameRestartAction restartAction, GameModel gameModel) {
        Rectangle background = new Rectangle();
        background.setWidth(500);
        background.setHeight(700);
        Image backgroundImage = new Image("gameOver.png");
        background.setFill(new ImagePattern(backgroundImage));
        background.setTranslateX(0);
        background.setTranslateY(0);

        Label scoreLabel = new Label("Final Score: " + gameModel.getScore());
        scoreLabel.setTranslateX(200);
        scoreLabel.setTranslateY(200);
        scoreLabel.setScaleX(2);
        scoreLabel.setScaleY(2);
        scoreLabel.setStyle("-fx-text-fill: #fdfef5;");

        Button restart = new Button("Restart");
        restart.setTranslateX(220);
        restart.setTranslateY(300);
        restart.setOnAction(event -> restartAction.restart());

        root.getChildren().addAll(background, scoreLabel, restart);
    }

    /**
     * Displays the win screen with the final score and a restart option.
     *
     * @param root The pane where the win screen is to be displayed.
     * @param restartAction The action to be performed when restarting the game.
     * @param gameModel The game model containing the final score data.
     */
    public void showWin(Pane root, GameRestartAction restartAction, GameModel gameModel) {
        Rectangle background = new Rectangle();
        background.setWidth(500);
        background.setHeight(700);
        Image backgroundImage = new Image("win.png");
        background.setFill(new ImagePattern(backgroundImage));
        background.setTranslateX(0);
        background.setTranslateY(0);

        Label scoreLabel = new Label("Final Score: " + gameModel.getScore());
        scoreLabel.setTranslateX(200);
        scoreLabel.setTranslateY(370);
        scoreLabel.setScaleX(2);
        scoreLabel.setScaleY(2);
        scoreLabel.setStyle("-fx-text-fill: black;");

        Button restart = new Button("Restart");
        restart.setTranslateX(220);
        restart.setTranslateY(420);
        restart.setOnAction(event -> restartAction.restart());


        Platform.runLater(() -> {
            root.getChildren().addAll(background, scoreLabel, restart);
        });
    }
}