package Controller;

import Model.AlbumInterface;
import Model.FacadeModel;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class MusicPlayerController {

    private FacadeModel model;

    private boolean shuffle, play, mute, played;
    private int repeat;
    private MediaPlayer media;
    private int index;
    private Slider timeSlider, volumeSlider;
    private Label start, end, title, artist;
    private ImageView playBtn, songPicture;

    public MusicPlayerController(FacadeModel model) {
        this.model = model;
        shuffle = false;
        play = false;
        played = false;
        mute = false;
        repeat = 0;
        index = 0;
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

    public void play() {
        try {
            if (!played) {
                setSong(model.playSong(model.getCurrentPlaylist().getSongs().get(index)));
                played = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!play) {
            play = true;
            playBtn.setImage(new Image(getClass().getResourceAsStream("/Media/pause_button.png")));
            media.play();
        } else {
            play = false;
            media.pause();
        }

        media.currentTimeProperty().addListener((Observable observable) -> {
            updateTimeSlider();
        });

        media.setOnEndOfMedia(() -> {
            System.out.println("music end");
            playBtn.setImage(new Image(getClass().getResourceAsStream("/Media/play_button.png")));
            if(repeat == 0) {
                play = false;
                played = false;
                media.stop();
                if(index < model.getCurrentPlaylist().getSongs().size() - 1) {
                    next();
                }
            } else if (repeat == 1) {
                play = false;
                media.stop();
                next();
            } else if (repeat == 2){
                media.seek(Duration.ZERO);
            }
        });
    }

    public void next() {
        if (shuffle) {
            getIndex();
            media.stop();
            play = false;
            played = false;
            play();
        } else {
            if ((repeat > 0 || index < model.getCurrentPlaylist().getSongs().size()) && model.getCurrentPlaylist().getSongs().size() > 1) {
                media.stop();
                if (index == model.getCurrentPlaylist().getSongs().size() - 1) {
                    index = 0;
                } else {
                    index++;
                }
                play = false;
                played = false;
                play();
            }
        }
    }

    public void prev() {
        if (shuffle) {
            getIndex();
            media.stop();
            play = false;
            played = false;
            play();
        } else {
            if (media.getCurrentTime().toSeconds() >= 5) {
                media.seek(Duration.ZERO);
            } else {
                if ((repeat > 0 || index > 0) && model.getCurrentPlaylist().getSongs().size() > 1) {
                    media.stop();
                    if (index == 0) {
                        index = model.getCurrentPlaylist().getSongs().size() - 1;
                    } else {
                        index--;
                    }
                    play = false;
                    played = false;
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

    public void stop() {
        media.stop();
        play = false;
        played = false;
        playBtn.setImage(new Image(getClass().getResourceAsStream("/Media/play_button.png")));
    }

    public void setButton(ImageView playBtn) {
        this.playBtn = playBtn;
    }

    public void setPicture(ImageView songPicture) {
        this.songPicture = songPicture;
    }

    public void setSong(File location) {
        media = new MediaPlayer(new Media(location.toURI().toString()));
        model.setCurrentSong(model.getCurrentPlaylist().getSongs().get(index));
        title.setText(model.getCurrentSong().getName());
        artist.setText(model.getCurrentSong().getArtist());
        end.setText(getDuration(model.getCurrentSong().getLength()));
        try {
            AlbumInterface aa = model.getAlbumFromUser(model.getCurrentSong().getAlbum().trim(), model.getCurrentSong().getArtist().trim());
            File file = model.getAlbumArt(aa.getAlbumID());
            if(file != null) {
                songPicture.setImage(new Image(file.toURI().toString().replaceAll("%20", " ").replaceAll("\'","\\'")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void setIndex(int index) {
        this.index = index;
    }

    public int returnIndex() {
        return index;
    }

    public MediaPlayer getMedia() {
        return media;
    }
}