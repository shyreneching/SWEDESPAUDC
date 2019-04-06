/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.FacadeModel;
import View.DashboardView;
import java.sql.SQLException;
import javafx.beans.Observable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 *
 * @author Stanley Sie
 */
public class MusicPlayerController {

    private DashboardView main;
    private FacadeModel model;

    private boolean pause;
    private boolean shuffle;
    private boolean mute;
    private int repeat; //0 = not applicable, 1 = repeat all, 2 = repeat 1 song
    private int currentIndex;
    private MediaPlayer currentSong;
    private Media media;

    public MusicPlayerController(FacadeModel model, DashboardView main) {
        this.main = main;
        this.model = model;
        currentIndex = 0;
        pause = true;
        shuffle = false;
        mute = false;
        repeat = 0;
    }

    public MediaPlayer getSong() {
        return new MediaPlayer(new Media(model.getCurrentPlaylist().getSongs().get(currentIndex).getSongfile().toURI().toString()));
    }

    public MediaPlayer getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(MediaPlayer currentSong) {
        this.currentSong = currentSong;
    }

    public void playMusic() {
        if (pause) {
            currentSong.play();
            pause = false;
        } else {
            currentSong.pause();
            pause = true;
        }
        
        currentSong.currentTimeProperty().addListener((Observable observable) -> {
            main.updateSlider();
        });

        currentSong.setOnEndOfMedia(() -> {
            pause = true;
            if (repeat == 2) {
                currentSong.stop();
                currentSong.setStartTime(Duration.ZERO);
                currentSong.play();
            } else if (currentIndex < model.getCurrentPlaylist().getSongs().size()) {
                nextMusic();
            }
        });
    }

    public void stopMusic() {
        if (currentSong != null) {
            currentSong.stop();
            pause = true;
            main.resetSlider();
        }
    }

    public void nextMusic() {
        currentSong.stop();
        setPause(true);
        if (shuffle) {
            int x;
            do {
                x = (int) (Math.random() * model.getCurrentPlaylist().getSongs().size());
            } while (x == currentIndex);
            currentIndex = x;
        } else {
            if (currentIndex < model.getCurrentPlaylist().getSongs().size() - 1) {
                currentIndex += 1;
            } else {
                currentIndex = 0;
            }
        }
        main.startList();
        main.endList();
        model.setCurrentSong(model.getCurrentPlaylist().getSongs().get(currentIndex));
        setCurrentSong(getSong());
        playMusic();
    }

    public void prevMusic() {
        currentSong.stop();
        currentSong.setStartTime(Duration.ZERO);
        setPause(true);
        if (shuffle) {
            int x;
            do {
                x = (int) (Math.random() * model.getCurrentPlaylist().getSongs().size());
            } while (x == currentIndex);
            currentIndex = x;
        } else {
            if (currentIndex > 0) {
                currentIndex -= 1;
            } else {
                currentIndex = model.getCurrentPlaylist().getSongs().size() - 1;
            }
        }
        main.startList();
        main.endList();
        model.setCurrentSong(model.getCurrentPlaylist().getSongs().get(currentIndex));
        setCurrentSong(getSong());
        playMusic();
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}
