    package brickGame.Model;

    /**
     * Represents the paddle in the brick game.
     * Manages the paddle's position, size, and movement within the game scene.
     */
    public class Paddle {
        private double x;
        private double y;
        private double centerBreakX;
        private int width;
        private final int height;
        private final int originalWidth = 130;

        /**
         * Constructs a new Paddle object with default settings.
         * Initializes the paddle's position and size to default values.
         */
        public Paddle() {
            this.x = 0.0f;
            this.y = 640.0f;
            this.width = 130;
            this.height = 30;
        }

        /**
         * Increases the width of the paddle.
         * This can be used as part of a game mechanic to enhance the player's ability to hit the ball.
         */
        public void increaseWidth() {
            this.width =  180;
        }

        /**
         * Decreases the width of the paddle.
         * This can be used to increase the game's difficulty or as part of a game mechanic.
         */
        public void decreaseWidth() {
            this.width = 80;
        }

        /**
         * Sets the width of the paddle.
         *
         * @param width The new width of the paddle.
         */
        public void setWidth(int width){
            this.width = width;
        }

        /**
         * Resets the width of the paddle to its original value.
         */
        public void resetWidth() {
            this.width = originalWidth;
        }

        /**
         * Moves the paddle to the right within the bounds of the game scene.
         *
         * @param sceneWidth The width of the game scene to ensure the paddle doesn't move out of bounds.
         */

        public void moveRight(int sceneWidth) {
            this.x = Math.min(this.x + 1, sceneWidth - this.width);
        }

        /**
         * Moves the paddle to the left, ensuring it doesn't move out of the game scene's bounds.
         */
        public void moveLeft() {
            this.x = Math.max(this.x - 1, 0);
        }

        // Getters and Setters
        /**
         * Gets the current x-coordinate of the paddle.
         *
         * @return The x-coordinate of the paddle.
         */
        public double getX() {
            return x;
        }

        /**
         * Sets the x-coordinate of the paddle.
         *
         * @param x The new x-coordinate for the paddle.
         */
        public void setX(double x) {
            this.x = x;
        }

        /**
         * Sets the y-coordinate of the paddle.
         *
         * @param y The new y-coordinate for the paddle.
         */
        public void setY(double y) {
            this.y = y;
        }

        /**
         * Sets the center x-coordinate of the paddle used to track the middle point of the paddle to set ball physics after paddle ball collision.
         *
         * @param centerBreakX The new center x-coordinate for the paddle.
         */
        public void setCenterBreakX(double centerBreakX) {
            this.centerBreakX = centerBreakX;
        }

        /**
         * Gets the current y-coordinate of the paddle.
         *
         * @return The y-coordinate of the paddle.
         */
        public double getY() {
            return y;
        }

        /**
         * Gets the current width of the paddle.
         *
         * @return The width of the paddle.
         */
        public int getWidth() {
            return width;
        }

        /**
         * Gets the height of the paddle.
         *
         * @return The height of the paddle.
         */
        public int getHeight() {
            return height;
        }

        /**
         * Calculates and returns the x-coordinate of the paddle's center.
         *
         * @return The x-coordinate of the center of the paddle.
         */
        public double getCenterBreakX() {
            return centerBreakX = x + (double) width / 2;
        }

    }
