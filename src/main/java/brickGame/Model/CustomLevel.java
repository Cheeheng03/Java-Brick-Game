package brickGame.Model;
/**
 * Interface for creating custom levels in the brick game.
 * This interface allows the implementation of specific level initialization logic for different level designs.
 */
public interface CustomLevel {
    /**
     * Initializes the level (17 and 18) with custom settings and layouts.
     * Implementations of this method should set up the new custom levels.
     */
    void initLevel();
}
