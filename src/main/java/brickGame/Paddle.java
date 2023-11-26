    package brickGame;

    public class Paddle {
        private double x;
        private double y;
        private double centerBreakX;
        private int width;
        private final int height;

        public Paddle() {
            this.x = 0.0f;
            this.y = 640.0f;
            this.width = 130;
            this.height = 30;
        }

        public void increaseWidth() {
            this.width += 50;
        }

        public void decreaseWidth() {
            this.width -= 50;
        }

        public void setWidth(int width){
            this.width = width;
        }
        public void resetWidth() {
            this.width = 130;
        }

        public void moveRight(int sceneWidth) {
            this.x = Math.min(this.x + 1, sceneWidth - this.width);
        }

        public void moveLeft() {
            this.x = Math.max(this.x - 1, 0);
        }

        // Getters and Setters
        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }
        public void setCenterBreakX(double centerBreakX) {
            this.centerBreakX = centerBreakX;
        }

        public double getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
        public double getCenterBreakX() {
            return centerBreakX = x + (double) width / 2;
        }

    }
