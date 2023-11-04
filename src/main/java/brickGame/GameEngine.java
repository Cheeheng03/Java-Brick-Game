package brickGame;

public class GameEngine {

    private OnAction onAction;
    private int fps = 15;
    private Thread updateThread;
    private Thread physicsThread;
    public boolean isStopped = true;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and we convert it to milliseconds
     */
    public void setFps(int fps) {
        this.fps = (int) 1000 / fps;
    }

    private synchronized void Update() {
        updateThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    onAction.onUpdate();
                    Thread.sleep(fps);
                }
            } catch (InterruptedException e) {
                // Handle the interruption or exit gracefully.
                Thread.currentThread().interrupt(); // Restore the interrupted status.
            }
        });
        updateThread.start();
    }

    private void Initialize() {
        onAction.onInit();
    }

    private synchronized void PhysicsCalculation() {
        physicsThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    onAction.onPhysicsUpdate();
                    Thread.sleep(fps);
                }
            } catch (InterruptedException e) {
                // Handle the interruption or exit gracefully.
                Thread.currentThread().interrupt(); // Restore the interrupted status.
            }
        });
        physicsThread.start();
    }

    public void start() {
        time = 0;
        Initialize();
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }

    public void stop() {
        if (!isStopped) {
            isStopped = true;

            // Interrupt the threads to signal them to stop.
            updateThread.interrupt();
            physicsThread.interrupt();
            timeThread.interrupt();

            try {
                // Wait for each thread to finish gracefully.
                updateThread.join();
                physicsThread.join();
                timeThread.join();
            } catch (InterruptedException e) {
                // Handle the interruption or exit gracefully.
                Thread.currentThread().interrupt(); // Restore the interrupted status.
            }
        }
    }

    private long time = 0;
    private Thread timeThread;

    private void TimeStart() {
        timeThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                // Handle the interruption or exit gracefully.
                Thread.currentThread().interrupt(); // Restore the interrupted status.
            }
        });
        timeThread.start();
    }

    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }
}
