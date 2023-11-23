package brickGame;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Score {

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

    @FunctionalInterface
    public interface GameRestartAction {
        void restart();
    }

    public void showGameOver(Pane root,GameRestartAction restartAction) {
        Label label = new Label("Game Over :(");
        label.setTranslateX(200);
        label.setTranslateY(250);
        label.setScaleX(2);
        label.setScaleY(2);

        Button restart = new Button("Restart");
        restart.setTranslateX(220);
        restart.setTranslateY(300);
        restart.setOnAction(event -> restartAction.restart());

        root.getChildren().addAll(label, restart);
    }

    public void showWin(Pane root) {
        Label label = new Label("You Win :)");
        label.setTranslateX(200);
        label.setTranslateY(250);
        label.setScaleX(2);
        label.setScaleY(2);

        Platform.runLater(() -> root.getChildren().add(label));
    }
}
