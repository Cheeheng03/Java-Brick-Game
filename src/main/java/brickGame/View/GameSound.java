package brickGame.View;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class GameSound {
    private MediaPlayer bgMediaPlayer;
    private Media hitSound;
    private Media loseSound;
    private Media winSound;

    public GameSound() {
        initializeBackgroundMusic();
        initializeHitSound();
        initializeLoseSound();
        initializeWinSound();
    }


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

    private void initializeHitSound() {
        URL hitSoundURL = getClass().getResource("/hit_sound.mp3");
        if (hitSoundURL != null) {
            hitSound = new Media(hitSoundURL.toExternalForm());
        } else {
            System.err.println("Hit sound file not found");
        }
    }

    private void initializeLoseSound() {
        URL loseSoundURL = getClass().getResource("/you_lose.mp3");
        if (loseSoundURL != null) {
            loseSound = new Media(loseSoundURL.toExternalForm());
        } else {
            System.err.println("Lose sound file not found");
        }
    }

    private void initializeWinSound() {
        URL winSoundURL = getClass().getResource("/you_win.mp3");
        if (winSoundURL != null) {
            winSound = new Media(winSoundURL.toExternalForm());
        } else {
            System.err.println("Win sound file not found");
        }
    }

    public void playBackgroundMusic() {
        if (bgMediaPlayer != null) {
            bgMediaPlayer.play();
        }
    }

    public void stopBackgroundMusic() {
        if (bgMediaPlayer != null) {
            bgMediaPlayer.stop();
        }
    }

    public void playHitSound() {
        if (hitSound != null) {
            MediaPlayer hitMediaPlayer = new MediaPlayer(hitSound);
            hitMediaPlayer.play();
        }
    }

    public void playLoseSound() {
        if (loseSound != null) {
            MediaPlayer loseMediaPlayer = new MediaPlayer(loseSound);
            loseMediaPlayer.play();
        }
    }

    public void playWinSound() {
        if (winSound != null) {
            MediaPlayer winMediaPlayer = new MediaPlayer(winSound);
            winMediaPlayer.play();
        }
    }
}
