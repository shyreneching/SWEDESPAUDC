package controller;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MusicPlayerController {

    @FXML
    private Rectangle titleBar;
    @FXML
    private Button closeBtn, minimizeBtn;
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
    private Label titleLbl, artistLbl, titleBtn, artistBtn, albumBtn, genreBtn, yearBtn, timeBtn;
    @FXML
    private Line titleLine, artistLine, albumLine, genreLine, yearLine, timeLine;

    private double xOffset = 0;
    private double yOffset = 0;

    private boolean isPlaying;
    private boolean isShuffled;
    private boolean isRepeated;
    private boolean isMuted;
    private boolean isSongRepeated;

    private boolean isTitleSorted;
    private boolean isArtistSorted;
    private boolean isAlbumSorted;
    private boolean isGenreSorted;
    private boolean isYearSorted;
    private boolean isTimeSorted;

    public void initialize() {
        isPlaying = false;
        isShuffled = false;
        isRepeated = false;
        isMuted = false;
        isSongRepeated = false;

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
            if (!isPlaying) {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/media/play_button_hover.png")));
            } else if (isPlaying) {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/media/pause_button_hover.png")));
            }
        });

        playBtn.setOnMouseExited(event -> {
            if (!isPlaying) {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/media/play_button.png")));
            } else if (isPlaying) {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/media/pause_button.png")));
            }
        });

        playBtn.setOnMouseClicked(event -> {
            if (!isPlaying) {
                isPlaying = true;
                playBtn.setImage(new Image(getClass().getResourceAsStream("/media/pause_button_hover.png")));
            } else if (isPlaying) {
                isPlaying = false;
                playBtn.setImage(new Image(getClass().getResourceAsStream("/media/play_button_hover.png")));
            }
        });


        nextBtn.setOnMouseEntered(event -> {
            nextBtn.setImage(new Image(getClass().getResourceAsStream("/media/next_button_hover.png")));
        });

        nextBtn.setOnMouseExited(event -> {
            nextBtn.setImage(new Image(getClass().getResourceAsStream("/media/next_button.png")));
        });

        prevBtn.setOnMouseEntered(event -> {
            prevBtn.setImage(new Image(getClass().getResourceAsStream("/media/previous_button_hover.png")));
        });

        prevBtn.setOnMouseExited(event -> {
            prevBtn.setImage(new Image(getClass().getResourceAsStream("/media/previous_button.png")));
        });

        shuffleBtn.setOnMouseEntered(event -> {
            if (!isShuffled) {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/media/shuffle_hover.png")));
            } else if (isShuffled) {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/media/shuffle_clicked.png")));
            }
        });

        shuffleBtn.setOnMouseExited(event -> {
            if (!isShuffled) {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/media/shuffle.png")));
            } else if (isShuffled) {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/media/shuffle_clicked.png")));
            }
        });

        shuffleBtn.setOnMouseClicked(event -> {
            if (!isShuffled) {
                isShuffled = true;
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/media/shuffle_clicked.png")));
            } else if (isShuffled) {
                isShuffled = false;
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/media/shuffle.png")));
            }
        });

        repeatBtn.setOnMouseEntered(event -> {
            if (!isRepeated) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_hover.png")));
            } else if (isRepeated) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_clicked.png")));
            }
            if (isSongRepeated) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_song_clicked.png")));
            }
        });

        repeatBtn.setOnMouseExited(event -> {
            if (!isRepeated) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat.png")));
            } else if (isRepeated) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_clicked.png")));
            }
            if (isSongRepeated) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_song_clicked.png")));
            }
        });

        repeatBtn.setOnMouseClicked(event -> {
            if (!isRepeated || isSongRepeated) {
                isRepeated = true;
                isSongRepeated = false;
                repeatBtn.setFitHeight(20);
                repeatBtn.setFitWidth(20);
                repeatBtn.setLayoutY(27);
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_clicked.png")));
            } else if (isRepeated) {
                isRepeated = false;
                isSongRepeated = false;
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat.png")));
            }
            if (event.getButton().equals(MouseButton.PRIMARY) && !isSongRepeated) {
                if (event.getClickCount() == 2) {
                    isSongRepeated = true;
                    repeatBtn.setFitHeight(25);
                    repeatBtn.setFitWidth(35);
                    repeatBtn.setLayoutY(22);
                    repeatBtn.setImage(new Image(getClass().getResourceAsStream("/media/repeat_song_clicked.png")));
                }
            }
        });

        muteBtn.setOnMouseEntered(event -> {
            if (!isMuted) {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/media/volume_hover.png")));
            } else if (isMuted) {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/media/mute_clicked.png")));
            }
        });

        muteBtn.setOnMouseExited(event -> {
            if (!isMuted) {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/media/volume.png")));
            } else if (isMuted) {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/media/mute_clicked.png")));
            }
        });

        muteBtn.setOnMouseClicked(event -> {
            if (!isMuted) {
                isMuted = true;
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/media/mute_clicked.png")));
            } else if (isMuted) {
                isMuted = false;
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/media/volume_hover.png")));
            }
        });
    }
}