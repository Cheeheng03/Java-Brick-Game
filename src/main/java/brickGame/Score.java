package brickGame;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
//import sun.plugin2.message.Message;

public class Score {
    public void show(final double x, final double y, int score, final Main main) {
        String sign = score >= 0 ? "+" : "";
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() -> main.root.getChildren().add(label));

        Timeline timeline = new Timeline();
        KeyValue scaleUpX = new KeyValue(label.scaleXProperty(), 2);
        KeyValue scaleUpY = new KeyValue(label.scaleYProperty(), 2);

        KeyValue fadeOut = new KeyValue(label.opacityProperty(), 0);

        KeyFrame keyFrame1 = new KeyFrame(Duration.millis(315), scaleUpX, scaleUpY);
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(1.5), fadeOut);

        timeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
        timeline.setOnFinished(event -> Platform.runLater(() -> main.root.getChildren().remove(label)));
        timeline.play();
    }


    public void showMessage(String message, final Main main) {
        final Label label = new Label(message);
        label.setTranslateX(220);
        label.setTranslateY(340);

        Platform.runLater(() -> main.root.getChildren().add(label));

        Timeline timeline = new Timeline();

        KeyValue startScaleX = new KeyValue(label.scaleXProperty(), 1);
        KeyValue startScaleY = new KeyValue(label.scaleYProperty(), 1);
        KeyValue peakScaleX = new KeyValue(label.scaleXProperty(), 2);
        KeyValue peakScaleY = new KeyValue(label.scaleYProperty(), 2);
        KeyValue endScaleX = new KeyValue(label.scaleXProperty(), 1);
        KeyValue endScaleY = new KeyValue(label.scaleYProperty(), 1);

        KeyValue fadeStart = new KeyValue(label.opacityProperty(), 1);
        KeyValue fadeEnd = new KeyValue(label.opacityProperty(), 0);

        KeyFrame startFrame = new KeyFrame(Duration.ZERO, startScaleX, startScaleY, fadeStart);
        KeyFrame peakFrame = new KeyFrame(Duration.millis(750), peakScaleX, peakScaleY);
        KeyFrame endFrame = new KeyFrame(Duration.millis(1500), endScaleX, endScaleY);
        KeyFrame fadeFrame = new KeyFrame(Duration.seconds(3.5), fadeEnd); // Includes delay as per original method.

        timeline.getKeyFrames().addAll(startFrame, peakFrame, endFrame, fadeFrame);

        timeline.setOnFinished(event -> Platform.runLater(() -> main.root.getChildren().remove(label)));

        timeline.play();
    }


    public void showGameOver(final Main main) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label label = new Label("Game Over :(");
                label.setTranslateX(200);
                label.setTranslateY(250);
                label.setScaleX(2);
                label.setScaleY(2);

                Button restart = new Button("Restart");
                restart.setTranslateX(220);
                restart.setTranslateY(300);
                restart.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        main.restartGame();
                    }
                });

                main.root.getChildren().addAll(label, restart);

            }
        });
    }

    public void showWin(final Main main) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label label = new Label("You Win :)");
                label.setTranslateX(200);
                label.setTranslateY(250);
                label.setScaleX(2);
                label.setScaleY(2);


                main.root.getChildren().addAll(label);

            }
        });
    }
}
