package view;

import controller.FacadeController;
import controller.MusicPlayerController;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.FacadeModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MusicPlayerView implements View {

    private Stage stage;
    private Scene scene;
    private FacadeModel model;
    private FacadeController controller;
    private MusicPlayerController musicPlayerController;

    @FXML
    private Rectangle titleBar;
    @FXML
    private Button closeBtn, minimizeBtn, playlistbtn;
    @FXML
    private Slider slider, volSlider;
    @FXML
    private ProgressBar progress, volProgress;
    @FXML
    private ImageView transitionGif, playBtn, nextBtn, prevBtn, shuffleBtn, repeatBtn, muteBtn, searchBtn;
    @FXML
    private Path pathingStart;
    @FXML
    private AnchorPane transitionPane;
    @FXML
    private Label titleLbl, artistLbl, titleBtn, artistBtn, albumBtn, genreBtn, yearBtn, timeBtn, currentTime, endTime;
    @FXML
    private Line titleLine, artistLine, albumLine, genreLine, yearLine, timeLine;

    private double xOffset = 0;
    private double yOffset = 0;

    private boolean isTitleSorted;
    private boolean isArtistSorted;
    private boolean isAlbumSorted;
    private boolean isGenreSorted;
    private boolean isYearSorted;
    private boolean isTimeSorted;

    private boolean init;

    public MusicPlayerView(Stage stage, FacadeModel model) {
        this.stage = stage;
        this.model = model;
        this.model.attach(this);
        this.controller = new FacadeController(this.model);
        this.musicPlayerController = new MusicPlayerController(this.model);
        this.init = false;
        FXMLLoader pane = new FXMLLoader(getClass().getResource("/view/MusicPlayer.fxml"));
        pane.setController(this);
        try {
            this.scene = new Scene(pane.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stage.setScene(this.scene);
        this.stage.show();
        musicPlayerController.setTimeSlider(slider);
        musicPlayerController.setVolumeSlider(volSlider);
        musicPlayerController.setTimerLabel(currentTime, endTime);
    }

    public void initialize() {
        /*isPlaying = false;
        isShuffled = false;
        isRepeated = false;
        isMuted = false;
        isSongRepeated = false;*/

        isTitleSorted = false;
        isArtistSorted = false;
        isAlbumSorted = false;
        isGenreSorted = false;
        isYearSorted = false;
        isTimeSorted = false;

        transitionGif.setImage(new Image(getClass().getResourceAsStream("/media/waveGif.gif")));

        Path tempPath = new Path();
        tempPath.getElements().addAll(new MoveTo(0.0f, 50.0f));
        tempPath.getElements().add(new LineTo(100.0f, 100.0f));
        Rectangle tempRect = new Rectangle();
        tempRect.setHeight(10);
        tempRect.setWidth(10);
        tempRect.setOpacity(0);

        PathTransition move = new PathTransition();
        move.setDuration(Duration.seconds(4.4));
        move.setPath(tempPath);
        move.setNode(tempRect);
        move.setAutoReverse(false);
        move.play();
        move.setOnFinished(event -> {
            transitionGif.setImage(null);
            transitionGif.setDisable(true);
            transitionGif.setOpacity(0);
            FadeTransition fade = new FadeTransition();
            fade.setDuration(Duration.seconds(2));
            fade.setFromValue(10);
            fade.setToValue(0);
            fade.setAutoReverse(false);
            fade.setNode(transitionPane);
            fade.play();
            fade.setOnFinished(event1 -> {

                transitionPane.setOpacity(0);
                transitionPane.setDisable(true);
            });
        });

        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            Stage primaryStage = (Stage) titleBar.getScene().getWindow();
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        minimizeBtn.setOnAction(event -> {
            Stage stage = (Stage) minimizeBtn.getScene().getWindow();
            stage.setIconified(true);
        });

        closeBtn.setOnAction(event -> {
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        });

        titleBtn.setOnMouseClicked(event -> {
            if (!isTitleSorted) {
                isTitleSorted = true;
                isArtistSorted = false;
                isAlbumSorted = false;
                isGenreSorted = false;
                isYearSorted = false;
                isTimeSorted = false;
                titleBtn.setTextFill(Color.web("#00ead0"));
                titleLine.setStroke(Color.web("#00ead0"));
                artistBtn.setTextFill(Color.web("#FFFFFF"));
                artistLine.setStroke(Color.web("#FFFFFF"));
                albumBtn.setTextFill(Color.web("#FFFFFF"));
                albumLine.setStroke(Color.web("#FFFFFF"));
                genreBtn.setTextFill(Color.web("#FFFFFF"));
                genreLine.setStroke(Color.web("#FFFFFF"));
                yearBtn.setTextFill(Color.web("#FFFFFF"));
                yearLine.setStroke(Color.web("#FFFFFF"));
                timeBtn.setTextFill(Color.web("#FFFFFF"));
                timeLine.setStroke(Color.web("#FFFFFF"));
            } else if (isTitleSorted) {
                isTitleSorted = false;
                isArtistSorted = false;
                isAlbumSorted = false;
                isGenreSorted = false;
                isYearSorted = false;
                isTimeSorted = false;
                titleBtn.setTextFill(Color.web("#FFFFFF"));
                titleLine.setStroke(Color.web("#FFFFFF"));
            }
        });

        artistBtn.setOnMouseClicked(event -> {
            if (!isArtistSorted) {
                isTitleSorted = false;
                isArtistSorted = true;
                isAlbumSorted = false;
                isGenreSorted = false;
                isYearSorted = false;
                isTimeSorted = false;
                artistBtn.setTextFill(Color.web("#00ead0"));
                artistLine.setStroke(Color.web("#00ead0"));
                titleBtn.setTextFill(Color.web("#FFFFFF"));
                titleLine.setStroke(Color.web("#FFFFFF"));
                albumBtn.setTextFill(Color.web("#FFFFFF"));
                albumLine.setStroke(Color.web("#FFFFFF"));
                genreBtn.setTextFill(Color.web("#FFFFFF"));
                genreLine.setStroke(Color.web("#FFFFFF"));
                yearBtn.setTextFill(Color.web("#FFFFFF"));
                yearLine.setStroke(Color.web("#FFFFFF"));
                timeBtn.setTextFill(Color.web("#FFFFFF"));
                timeLine.setStroke(Color.web("#FFFFFF"));
            } else if (isArtistSorted) {
                isTitleSorted = false;
                isArtistSorted = false;
                isAlbumSorted = false;
                isGenreSorted = false;
                isYearSorted = false;
                isTimeSorted = false;
                artistBtn.setTextFill(Color.web("#FFFFFF"));
                artistLine.setStroke(Color.web("#FFFFFF"));
            }
        });

        albumBtn.setOnMouseClicked(event -> {
            if (!isAlbumSorted) {
                isTitleSorted = false;
                isArtistSorted = false;
                isAlbumSorted = true;
                isGenreSorted = false;
                isYearSorted = false;
                isTimeSorted = false;
                albumBtn.setTextFill(Color.web("#00ead0"));
                albumLine.setStroke(Color.web("#00ead0"));
                titleBtn.setTextFill(Color.web("#FFFFFF"));
                titleLine.setStroke(Color.web("#FFFFFF"));
                artistBtn.setTextFill(Color.web("#FFFFFF"));
                artistLine.setStroke(Color.web("#FFFFFF"));
                genreBtn.setTextFill(Color.web("#FFFFFF"));
                genreLine.setStroke(Color.web("#FFFFFF"));
                yearBtn.setTextFill(Color.web("#FFFFFF"));
                yearLine.setStroke(Color.web("#FFFFFF"));
                timeBtn.setTextFill(Color.web("#FFFFFF"));
                timeLine.setStroke(Color.web("#FFFFFF"));
            } else if (isAlbumSorted) {
                isTitleSorted = false;
                isArtistSorted = false;
                isAlbumSorted = false;
                isGenreSorted = false;
                isYearSorted = false;
                isTimeSorted = false;
                albumBtn.setTextFill(Color.web("#FFFFFF"));
                albumLine.setStroke(Color.web("#FFFFFF"));
            }
        });

        genreBtn.setOnMouseClicked(event -> {
            if (!isGenreSorted) {
                isTitleSorted = false;
                isArtistSorted = false;
                isAlbumSorted = false;
                isGenreSorted = true;
                isYearSorted = false;
                isTimeSorted = false;
                genreBtn.setTextFill(Color.web("#00ead0"));
                genreLine.setStroke(Color.web("#00ead0"));
                titleBtn.setTextFill(Color.web("#FFFFFF"));
                titleLine.setStroke(Color.web("#FFFFFF"));
                artistBtn.setTextFill(Color.web("#FFFFFF"));
                artistLine.setStroke(Color.web("#FFFFFF"));
                albumBtn.setTextFill(Color.web("#FFFFFF"));
                albumLine.setStroke(Color.web("#FFFFFF"));
                yearBtn.setTextFill(Color.web("#FFFFFF"));
                yearLine.setStroke(Color.web("#FFFFFF"));
                timeBtn.setTextFill(Color.web("#FFFFFF"));
                timeLine.setStroke(Color.web("#FFFFFF"));
            } else if (isGenreSorted) {
                isTitleSorted = false;
                isArtistSorted = false;
                isAlbumSorted = false;
                isGenreSorted = false;
                isYearSorted = false;
                isTimeSorted = false;
                genreBtn.setTextFill(Color.web("#FFFFFF"));
                genreLine.setStroke(Color.web("#FFFFFF"));
            }
        });

        yearBtn.setOnMouseClicked(event -> {
            if (!isYearSorted) {
                isTitleSorted = false;
                isArtistSorted = false;
                isAlbumSorted = false;
                isGenreSorted = false;
                isYearSorted = true;
                isTimeSorted = false;
                yearBtn.setTextFill(Color.web("#00ead0"));
                yearLine.setStroke(Color.web("#00ead0"));
                titleBtn.setTextFill(Color.web("#FFFFFF"));
                titleLine.setStroke(Color.web("#FFFFFF"));
                artistBtn.setTextFill(Color.web("#FFFFFF"));
                artistLine.setStroke(Color.web("#FFFFFF"));
                albumBtn.setTextFill(Color.web("#FFFFFF"));
                albumLine.setStroke(Color.web("#FFFFFF"));
                genreBtn.setTextFill(Color.web("#FFFFFF"));
                genreLine.setStroke(Color.web("#FFFFFF"));
                timeBtn.setTextFill(Color.web("#FFFFFF"));
                timeLine.setStroke(Color.web("#FFFFFF"));
            } else if (isYearSorted) {
                isTitleSorted = false;
                isArtistSorted = false;
                isAlbumSorted = false;
                isGenreSorted = false;
                isYearSorted = false;
                isTimeSorted = false;
                yearBtn.setTextFill(Color.web("#FFFFFF"));
                yearLine.setStroke(Color.web("#FFFFFF"));
            }
        });

        timeBtn.setOnMouseClicked(event -> {
            if (!isTimeSorted) {
                isTitleSorted = false;
                isArtistSorted = false;
                isAlbumSorted = false;
                isGenreSorted = false;
                isYearSorted = false;
                isTimeSorted = true;
                timeBtn.setTextFill(Color.web("#00ead0"));
                timeLine.setStroke(Color.web("#00ead0"));
                titleBtn.setTextFill(Color.web("#FFFFFF"));
                titleLine.setStroke(Color.web("#FFFFFF"));
                artistBtn.setTextFill(Color.web("#FFFFFF"));
                artistLine.setStroke(Color.web("#FFFFFF"));
                albumBtn.setTextFill(Color.web("#FFFFFF"));
                albumLine.setStroke(Color.web("#FFFFFF"));
                genreBtn.setTextFill(Color.web("#FFFFFF"));
                genreLine.setStroke(Color.web("#FFFFFF"));
                yearBtn.setTextFill(Color.web("#FFFFFF"));
                yearLine.setStroke(Color.web("#FFFFFF"));
            } else if (isTimeSorted) {
                isTitleSorted = false;
                isArtistSorted = false;
                isAlbumSorted = false;
                isGenreSorted = false;
                isYearSorted = false;
                isTimeSorted = false;
                timeBtn.setTextFill(Color.web("#FFFFFF"));
                timeLine.setStroke(Color.web("#FFFFFF"));
            }
        });

        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            progress.setProgress(new_val.doubleValue() / 100);
        });

        volSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            volProgress.setProgress(new_val.doubleValue() / 100);
        });

        searchBtn.setOnMouseEntered(event -> {
            searchBtn.setImage(new Image(getClass().getResourceAsStream("/media/search_hover.png")));
        });

        searchBtn.setOnMouseExited(event -> {
            searchBtn.setImage(new Image(getClass().getResourceAsStream("/media/search.png")));
        });

        playBtn.setOnMouseEntered(event -> {
            if (!musicPlayerController.isPlay()) {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/media/play_button_hover.png")));
            } else {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/media/pause_button_hover.png")));
            }
        });

        playBtn.setOnMouseExited(event -> {
            if (!musicPlayerController.isPlay()) {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/media/play_button.png")));
            } else {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/media/pause_button.png")));
            }
        });

        playBtn.setOnMouseClicked(event -> {
            //TESTING
            model.getCurrentPlaylist().setSongs(model.getSongs());
            if(!init) {
                init = true;
                musicPlayerController.setSong(model.getCurrentPlaylist().getSongs().get(musicPlayerController.returnIndex()).getSongfile());
            }
            //TESTING

            if (!musicPlayerController.isPlay()) {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/media/pause_button_hover.png")));
            } else {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/media/play_button_hover.png")));
            }
            musicPlayerController.play();
        });

        nextBtn.setOnMouseEntered(event -> {
            nextBtn.setImage(new Image(getClass().getResourceAsStream("/media/next_button_hover.png")));
        });

        nextBtn.setOnMouseExited(event -> {
            nextBtn.setImage(new Image(getClass().getResourceAsStream("/media/next_button.png")));
        });

        nextBtn.setOnMouseClicked(event -> {
            musicPlayerController.next();
        });

        prevBtn.setOnMouseEntered(event -> {
            prevBtn.setImage(new Image(getClass().getResourceAsStream("/media/previous_button_hover.png")));
        });

        prevBtn.setOnMouseExited(event -> {
            prevBtn.setImage(new Image(getClass().getResourceAsStream("/media/previous_button.png")));
        });

        prevBtn.setOnMouseClicked(event -> {
            musicPlayerController.prev();
        });

        shuffleBtn.setOnMouseEntered(event -> {
            if (!musicPlayerController.isShuffle()) {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/media/shuffle_hover.png")));
            } else {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/media/shuffle_clicked.png")));
            }
        });

        shuffleBtn.setOnMouseExited(event -> {
            if (!musicPlayerController.isShuffle()) {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/media/shuffle.png")));
            } else {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/media/shuffle_clicked.png")));
            }
        });

        shuffleBtn.setOnMouseClicked(event -> {
            if (!musicPlayerController.isShuffle()) {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/media/shuffle_clicked.png")));
            } else {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/media/shuffle.png")));
            }
            musicPlayerController.shuffle();
        });

        repeatBtn.setOnMouseEntered(event -> {
            if (musicPlayerController.getRepeat() == 0) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_hover.png")));
            } else if (musicPlayerController.getRepeat() == 1) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_clicked.png")));
            } else if (musicPlayerController.getRepeat() == 2) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_song_clicked.png")));
            }
        });

        repeatBtn.setOnMouseExited(event -> {
            if (musicPlayerController.getRepeat() == 0) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat.png")));
            } else if (musicPlayerController.getRepeat() == 1) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_clicked.png")));
            } else if (musicPlayerController.getRepeat() == 2) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_song_clicked.png")));
            }
        });

        repeatBtn.setOnMouseClicked(event -> {
            if (musicPlayerController.getRepeat() == 0) {
                repeatBtn.setFitHeight(20);
                repeatBtn.setFitWidth(20);
                repeatBtn.setLayoutY(27);
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_clicked.png")));
            } else if (musicPlayerController.getRepeat() == 1) {
                repeatBtn.setFitHeight(25);
                repeatBtn.setFitWidth(35);
                repeatBtn.setLayoutY(22);
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_song_clicked.png")));
            } else if (musicPlayerController.getRepeat() == 2) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat.png")));
            }
            musicPlayerController.repeat();
        });

        muteBtn.setOnMouseEntered(event -> {
            if (!musicPlayerController.isMute()) {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/media/volume_hover.png")));
            } else {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/media/mute_clicked.png")));
            }
        });

        muteBtn.setOnMouseExited(event -> {
            if (!musicPlayerController.isMute()) {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/media/volume.png")));
            } else {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/media/mute_clicked.png")));
            }
        });

        muteBtn.setOnMouseClicked(event -> {
            if (!musicPlayerController.isMute()) {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/media/mute_clicked.png")));
            } else if (musicPlayerController.isMute()) {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/media/volume_hover.png")));
            }
            musicPlayerController.mute();
        });

        playlistbtn.setOnAction(event -> {
            FileChooser file = new FileChooser();
            List<File> list = file.showOpenMultipleDialog(stage);
            if (list != null) {
                for (File f : list) {
                    System.out.println(f.toURI().toString().substring(6, f.toURI().toString().length()).replaceAll("%20", " ").replaceAll("%5", " "));
                    model.addSongLocally(f.toURI().toString().substring(6, f.toURI().toString().length()).replaceAll("%20", " ").replaceAll("%5", " "));
                }
            }
        });
    }

    @Override
    public void update() {

    }
}
