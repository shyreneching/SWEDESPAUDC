package controller;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import model.FacadeModel;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import java.io.File;

public class MusicPlayerController {

    private FacadeModel model;

    private boolean shuffle, play, mute;
    private int repeat;
    private MediaPlayer media;
    private int index;
    private Slider timeSlider, volumeSlider;
    private Label start, end, title, artist;

    public MusicPlayerController(FacadeModel model) {
        this.model = model;
        shuffle = false;
        play = false;
        mute = false;
        repeat = 0;
        index = 0;
    }

    public void play() {
        title.setText(model.getCurrentPlaylist().getSongs().get(index).getName());
        artist.setText(model.getCurrentPlaylist().getSongs().get(index).getArtist());

        if (!play) {
            play = true;
            media.play();
        } else {
            play = false;
            media.pause();
        }

        media.currentTimeProperty().addListener((Observable observable) -> {
            updateTimeSlider();
        });

        media.setOnEndOfMedia(() -> {
            if (repeat < 2) {
                play = false;
                media.stop();
                next();
            } else {
                media.seek(Duration.ZERO);
            }
        });
    }

    public void next() {
        if (shuffle) {
            getIndex();
            media.stop();
            setSong(model.getCurrentPlaylist().getSongs().get(index).getSongfile());
            play = false;
            play();
        } else {
            if (repeat > 0 || index < model.getCurrentPlaylist().getSongs().size() - 1) {
                media.stop();
                if (index == model.getCurrentPlaylist().getSongs().size() - 1) {
                    index = 0;
                } else {
                    index++;
                }
                setSong(model.getCurrentPlaylist().getSongs().get(index).getSongfile());
                play = false;
                play();
            }
        }
    }

    public void prev() {
        if (shuffle) {
            getIndex();
            media.stop();
            setSong(model.getCurrentPlaylist().getSongs().get(index).getSongfile());
            play = false;
            play();
        } else {
            if (media.getCurrentTime().toSeconds() >= 5) {
                media.seek(Duration.ZERO);
            } else {
                if (repeat > 0 || index > 0) {
                    media.stop();
                    if (index == 0) {
                        index = model.getCurrentPlaylist().getSongs().size() - 1;
                    } else {
                        index--;
                    }
                    setSong(model.getCurrentPlaylist().getSongs().get(index).getSongfile());
                    play = false;
                    play();
                }
            }
        }
    }

    public void repeat() {
        if (repeat == 2) {
            repeat = 0;
        } else {
            repeat++;
        }

        if (repeat == 2) {
            media.setCycleCount(MediaPlayer.INDEFINITE);
        } else {
            media.setCycleCount(1);
        }
    }

    public void shuffle() {
        if (shuffle) {
            shuffle = false;
        } else {
            shuffle = true;
        }
    }

    public void mute() {
        if (mute) {
            mute = false;
            media.setMute(false);
        } else {
            mute = true;
            media.setMute(true);
        }
    }

    public void setSong(File location) {
        media = new MediaPlayer(new Media(location.toURI().toString()));
        end.setText(getDuration((int) model.getCurrentPlaylist().getSongs().get(index).getLength()));
    }

    public void getIndex() {
        int x;
        do {
            x = (int) (Math.random() * model.getCurrentPlaylist().getSongs().size() - 1);
        } while (x == index);
        index = x;
    }

    public void setTimeSlider(Slider timeSlider) {
        this.timeSlider = timeSlider;

        this.timeSlider.valueProperty().addListener(observable -> {
            if (timeSlider.isPressed() || timeSlider.isValueChanging()) {
                Duration duration = media.getMedia().getDuration();
                media.seek(duration.multiply(timeSlider.getValue() / 100));
            }
        });
    }

    public void setVolumeSlider(Slider volumeSlider) {
        this.volumeSlider = volumeSlider;
        this.volumeSlider.setValue(20);
        this.volumeSlider.valueProperty().addListener((Observable observable) -> {
            if (this.volumeSlider.isPressed() || this.volumeSlider.isValueChanging()) {
                setVolume((float) this.volumeSlider.getValue() / 100);
            }
        });
    }

    public void setTimerLabel(Label start, Label end) {
        this.start = start;
        this.end = end;
    }

    public void updateTimeSlider() {
        Platform.runLater(() -> {
            start.setText(getDuration((int) media.getCurrentTime().toSeconds()));
            timeSlider.setValue(media.getCurrentTime().divide(media.getMedia().getDuration()).toMillis() * 100);
        });
    }

    public static void setVolume(Float f) {
        javax.sound.sampled.Port.Info source = Port.Info.SPEAKER;
        if (AudioSystem.isLineSupported(source)) {
            try {
                Port outline = (Port) AudioSystem.getLine(source);
                outline.open();
                FloatControl volumeControl = (FloatControl) outline.getControl(FloatControl.Type.VOLUME);
                volumeControl.setValue(f);
            } catch (LineUnavailableException ex) {
            }
        }
    }

    public void setTitle(Label title) {
        this.title = title;
    }

    public void setArtist(Label artist) {
        this.artist = artist;
    }

    public String getDuration(int time) {
        return String.format("%02d", time / 60) + ":" + String.format("%02d", time % 60);
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public int getRepeat() {
        return repeat;
    }

    public boolean isMute() {
        return mute;
    }

    public int returnIndex() {
        return index;
    }

    public MediaPlayer getMedia() { return media; }
}