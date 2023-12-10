package brickGame.View;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Manages the sound effects and background music for the brick game.
 * This class is responsible for initializing and controlling playback of various audio elements in the game.
 */
public class GameSound {
    private MediaPlayer bgMediaPlayer;
    private Media hitSound;
    private Media loseSound;
    private Media winSound;

    /**
     * Constructs a new GameSound object.
     * Initializes the background music and sound effects for different game events.
     */
    public GameSound() {
        initializeBackgroundMusic();
        initializeHitSound();
        initializeLoseSound();
        initializeWinSound();
    }

    /**
     * Initializes the background music for the game.
     * Loads the background music file and sets it to loop indefinitely.
     */
    private void initializeBackgroundMusic() {
        URL bgMusicURL = getClass().getResource("/background_music_1.mp3");
        if (bgMusicURL != null) {
            Media bgMedia = new Media(bgMusicURL.toExternalForm());
            bgMediaPlayer = new MediaPlayer(bgMedia);
            bgMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        } else {
            System.err.println("Background music file not found");
        }
    }

    /**
     * Initializes the hit sound effect for the game.
     * Loads the hit sound file to be played when the ball hits a block or paddle.
     */
    private void initializeHitSound() {
        URL hitSoundURL = getClass().getResource("/hit_sound.mp3");
        if (hitSoundURL != null) {
            hitSound = new Media(hitSoundURL.toExternalForm());
        } else {
            System.err.println("Hit sound file not found");
        }
    }

    /**
     * Initializes the lose sound effect for the game.
     * Loads the lose sound file to be played when the player loses the game.
     */
    private void initializeLoseSound() {
        URL loseSoundURL = getClass().getResource("/you_lose.mp3");
        if (loseSoundURL != null) {
            loseSound = new Media(loseSoundURL.toExternalForm());
        } else {
            System.err.println("Lose sound file not found");
        }
    }

    /**
     * Initializes the win sound effect for the game.
     * Loads the win sound file to be played when the player wins the game.
     */
    private void initializeWinSound() {
        URL winSoundURL = getClass().getResource("/you_win.mp3");
        if (winSoundURL != null) {
            winSound = new Media(winSoundURL.toExternalForm());
        } else {
            System.err.println("Win sound file not found");
        }
    }

    /**
     * Plays the background music for the game.
     * Starts the playback of the background music loop.
     */
    public void playBackgroundMusic() {
        if (bgMediaPlayer != null) {
            bgMediaPlayer.play();
        }
    }

    /**
     * Stops the background music for the game.
     * Halts the playback of the background music.
     */
    public void stopBackgroundMusic() {
        if (bgMediaPlayer != null) {
            bgMediaPlayer.stop();
        }
    }

    /**
     * Plays the hit sound effect.
     * Triggers the playback of the sound effect when the ball hits a block or paddle.
     */
    public void playHitSound() {
        if (hitSound != null) {
            MediaPlayer hitMediaPlayer = new MediaPlayer(hitSound);
            hitMediaPlayer.play();
        }
    }

    /**
     * Plays the lose sound effect.
     * Triggers the playback of the sound effect when the player loses the game.
     */
    public void playLoseSound() {
        if (loseSound != null) {
            MediaPlayer loseMediaPlayer = new MediaPlayer(loseSound);
            loseMediaPlayer.play();
        }
    }

    /**
     * Plays the win sound effect.
     * Triggers the playback of the sound effect when the player wins the game.
     */
    public void playWinSound() {
        if (winSound != null) {
            MediaPlayer winMediaPlayer = new MediaPlayer(winSound);
            winMediaPlayer.play();
        }
    }
}
