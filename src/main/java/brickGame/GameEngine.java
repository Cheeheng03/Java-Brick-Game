package brickGame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameEngine {

    private OnAction onAction;
    private Timeline updateTimeline;
    private Timeline physicsTimeline;
    private Timeline timeTimeline;
    private boolean isStopped = true;
    private long time = 0;
    private Duration frameTimeDuration;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    public void setFps(int fps) {
        this.frameTimeDuration = Duration.millis(1000 / fps);
    }

    private void initialize() {
        onAction.onInit();
    }

    public void setInitialTime(long initialTime) {
        this.time = initialTime;
    }

    public void start() {
        isStopped = false;
        initialize();
        startUpdateTimeline();
        startPhysicsTimeline();
        startTimeTimeline();
    }

    public void stop() {
        if (!isStopped) {
            isStopped = true;
            if (updateTimeline != null) {
                updateTimeline.stop();
            }
            if (physicsTimeline != null) {
                physicsTimeline.stop();
            }
            if (timeTimeline != null) {
                timeTimeline.stop();
            }
        }
    }

    private void startUpdateTimeline() {
        updateTimeline = new Timeline(new KeyFrame(frameTimeDuration, e -> onAction.onUpdate()));
        updateTimeline.setCycleCount(Timeline.INDEFINITE);
        updateTimeline.play();
    }

    private void startPhysicsTimeline() {
        physicsTimeline = new Timeline(new KeyFrame(frameTimeDuration, e -> onAction.onPhysicsUpdate()));
        physicsTimeline.setCycleCount(Timeline.INDEFINITE);
        physicsTimeline.play();
    }

    private void startTimeTimeline() {
        timeTimeline = new Timeline(new KeyFrame(Duration.millis(1), e -> onAction.onTime(time++)));
        timeTimeline.setCycleCount(Timeline.INDEFINITE);
        timeTimeline.play();
    }

    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }
}
