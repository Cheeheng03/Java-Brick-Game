package brickGame;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Score {

    public void show(final double x, final double y, int scoreAmount, final Main main) {
        String sign = scoreAmount >= 0 ? "+" : "";
        final Label label = new Label(sign + scoreAmount);
        label.setTranslateX(x);
        label.setTranslateY(y);

        main.root.getChildren().add(label); // This is already running on JavaFX thread due to the calling method context

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(315), new KeyValue(label.scaleXProperty(), 2), new KeyValue(label.scaleYProperty(), 2)),
                new KeyFrame(Duration.seconds(1.5), new KeyValue(label.opacityProperty(), 0))
        );
        timeline.setOnFinished(event -> main.root.getChildren().remove(label));
        timeline.play();
    }

    public void showMessage(String message, final Main main) {
        final Label label = new Label(message);
        label.setTranslateX(220);
        label.setTranslateY(340);

        Platform.runLater(() -> main.root.getChildren().add(label));

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

        timeline.setOnFinished(e -> main.root.getChildren().remove(label));
        timeline.play();
    }

    public void showGameOver(final Main main) {
        Label label = new Label("Game Over :(");
        label.setTranslateX(200);
        label.setTranslateY(250);
        label.setScaleX(2);
        label.setScaleY(2);

        Button restart = new Button("Restart");
        restart.setTranslateX(220);
        restart.setTranslateY(300);
        restart.setOnAction(event -> main.restartGame());

        main.root.getChildren().addAll(label, restart);
    }

    public void showWin(final Main main) {
        Label label = new Label("You Win :)");
        label.setTranslateX(200);
        label.setTranslateY(250);
        label.setScaleX(2);
        label.setScaleY(2);

        Platform.runLater(() -> main.root.getChildren().add(label));
    }
}
