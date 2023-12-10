package brickGame.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * GameEngine is responsible for managing the game loop and timing in the brick game.
 * It handles the updating of the game state, physics computations, and tracking game time.
 */
public class GameEngine {

    private OnAction onAction;
    private Timeline updateTimeline;
    private Timeline physicsTimeline;
    private Timeline timeTimeline;
    private boolean isStopped = true;
    private long time = 0;
    private Duration frameTimeDuration;

    /**
     * Sets the action callbacks for various game events.
     *
     * @param onAction An implementation of the OnAction interface containing methods to be called during game updates.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Sets the frames per second for the game's update and physics timelines.
     *
     * @param fps The desired frames per second.
     */
    public void setFps(int fps) {
        this.frameTimeDuration = Duration.millis((double) 1000 / fps);
    }

    /**
     * Initializes the game engine, typically called at the start of the game.
     */
    private void initialize() {
        onAction.onInit();
    }

    /**
     * Sets the initial time for the game, usually used when loading a game from a saved state.
     *
     * @param initialTime The initial game time in milliseconds.
     */
    public void setInitialTime(long initialTime) {
        this.time = initialTime;
    }


    /**
     * Starts the game engine. Initializes the timelines for game updates, physics, and time tracking.
     * The game begins its loop with these timelines after this method is called.
     */
    public void start() {
        isStopped = false;
        initialize();
        startUpdateTimeline();
        startPhysicsTimeline();
        startTimeTimeline();
    }

    /**
     * Stops the game engine. Halts all timelines and marks the engine as stopped.
     * Typically called when the game is paused or ended.
     */
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

    /**
     * Pauses the game. Temporarily halts all timelines without resetting the game state.
     * Can be resumed from the same state by calling the resume method.
     */
    public void pause() {
        if (!isStopped) {
            if (updateTimeline != null) {
                updateTimeline.pause();
            }
            if (physicsTimeline != null) {
                physicsTimeline.pause();
            }
            if (timeTimeline != null) {
                timeTimeline.pause();
            }
        }
    }

    /**
     * Resumes the game from a paused state. Restarts the timelines that were paused.
     */
    public void resume() {
        if (!isStopped) {
            if (updateTimeline != null) {
                updateTimeline.play();
            }
            if (physicsTimeline != null) {
                physicsTimeline.play();
            }
            if (timeTimeline != null) {
                timeTimeline.play();
            }
        }
    }

    /**
     * Starts the update timeline. This timeline is responsible for triggering onUpdate events at a set interval.
     */
    private void startUpdateTimeline() {
        updateTimeline = new Timeline(new KeyFrame(frameTimeDuration, e -> onAction.onUpdate()));
        updateTimeline.setCycleCount(Timeline.INDEFINITE);
        updateTimeline.play();
    }

    /**
     * Starts the physics timeline. This timeline handles the physics updates at a set interval.
     */
    private void startPhysicsTimeline() {
        physicsTimeline = new Timeline(new KeyFrame(frameTimeDuration, e -> onAction.onPhysicsUpdate()));
        physicsTimeline.setCycleCount(Timeline.INDEFINITE);
        physicsTimeline.play();
    }

    /**
     * Starts the time tracking timeline. This timeline updates the game time and triggers onTime events.
     */
    private void startTimeTimeline() {
        timeTimeline = new Timeline(new KeyFrame(Duration.millis(1), e -> onAction.onTime(time++)));
        timeTimeline.setCycleCount(Timeline.INDEFINITE);
        timeTimeline.play();
    }

    /**
     * The OnAction interface implemented by various game events like updates, initialization, physics updates, and time tracking.
     */
    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }
}
