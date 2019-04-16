package View;

import Controller.FacadeController;
import Controller.MusicPlayerController;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import Model.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MusicPlayerView implements View {

    private Stage stage;
    private Scene scene;
    private FacadeModel model;
    private FacadeController controller;
    private MusicPlayerController musicPlayerController;
    private AccountInterface viewUser;

    private ObservableList<SongInterface> songSearchList = FXCollections.observableArrayList();
    private ObservableList<AccountInterface> artistSearchList = FXCollections.observableArrayList();
    private ObservableList<PlaylistInterface> playlistSearchList = FXCollections.observableArrayList();
    private ObservableList<AlbumInterface> albumSearchList = FXCollections.observableArrayList();
    /*private ObservableList<SongInterface> songSearchList = FXCollections.observableArrayList(); for profile*/

    @FXML
    private Rectangle titleBar;
    @FXML
    private Button closeBtn, minimizeBtn, playListBtn, followBtn, songsBtn, artistsBtn, albumsBtn, genresBtn, yearsBtn, recentsBtn, mostBtn, artistfollowBtn, listenerfollowBtn, profile_icon_btn, notif_closeBtn, testBtn, logoutBtn, albumEnterBtn, albumCancelBtn, uploadEnterBtn, uploadCancelBtn, listenerunfollowBtn, artistunfollowBtn, addplaylistenterBtn, addplaylistcancelBtn;
    @FXML
    private Slider slider, volSlider;
    @FXML
    private ProgressBar progress, volProgress;
    @FXML
    private ImageView transitionGif, playBtn, nextBtn, prevBtn, shuffleBtn, repeatBtn, muteBtn, searchBtn, playListMore, createPlaylistBtn, queueBtn, createBtn, homeBtn, songPicture;
    @FXML
    private Path pathingStart;
    @FXML
    private AnchorPane transitionPane, song_etc_anchorpane, profile_pane, artist_profile_pane, notif_pane, songsPane, playListKebab, searchPane, welcomePane, createPane, albumCreate, songUpload, mainPane, cancelPane, sortPlaylistPane, addtoPlaylistPane, leftTab;
    @FXML
    private Label titleLbl, artistLbl, titleBtn, artistBtn, albumBtn, genreBtn, yearBtn, timeBtn, songsLbl, recentsLbl, mostLbl, artistsLbl, albumsLbl, genresLbl, yearsLbl, public_playlists_btn, prof_following_btn, prof_followers_btn, listenerusername, artist_prof_playlists_btn, artist_prof_albums_btn, artist_prof_following_btn, artist_prof_followers_btn, artist_username, notif_title, notif_lbl, add_to_queue_btn, remove_from_playlist_btn, add_to_playlist_btn, songViewTitle, songViewCreator, deletePlaylistLbl, makePublicLbl, createAlbumBtn, uploadSongBtn, sortPlaylistLbl, currentTime, endTime, choice_list, user_label;
    @FXML
    private Line titleLine, artistLine, albumLine, genreLine, yearLine, timeLine;
    @FXML
    private VBox songList, playList, songSearchPane, albumSearchPane, artistSearchPane, playlistSearchPane, profileSearchPane, sortPlaylistScroll, listener_vbox, artist_vbox;
    @FXML
    private TextField searchBar, albumNameInput;
    @FXML
    private ChoiceBox albumChoice, playlistChoice;
    @FXML
    private CheckBox singleChoice;

    private double xOffset = 0;
    private double yOffset = 0;

    private boolean isSongsSelected;
    private boolean isArtistsSelected;
    private boolean isAlbumsSelected;
    private boolean isGenresSelected;
    private boolean isYearsSelected;
    private boolean isRecentsSelected;
    private boolean isMostSelected;

    private boolean isTitleSorted;
    private boolean isArtistSorted;
    private boolean isAlbumSorted;
    private boolean isGenreSorted;
    private boolean isYearSorted;
    private boolean isTimeSorted;

    private boolean isKebabOpened;
    private boolean isProfileOpened;
    private boolean isNotifOpened;
    private boolean isPlayListKebabOpened;
    private boolean isQueueOpened;
    private boolean isCreateOpened;
    private boolean isCreateAlbumOpened;
    private boolean isUploadSongOpened;
    private boolean isaddtoplaylistOpened;

    private int previousSong;
    private int nextSong;
    private int previousPlaylist;
    private int nextPlaylist;

    public MusicPlayerView(Stage stage, FacadeModel model) {
        this.stage = stage;
        this.model = model;
        this.model.attach(this);
        this.controller = new FacadeController(this.model);
        this.musicPlayerController = new MusicPlayerController(this.model);
        FXMLLoader pane = new FXMLLoader(getClass().getResource("/View/MusicPlayer.fxml"));
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
        musicPlayerController.setTitle(titleLbl);
        musicPlayerController.setArtist(artistLbl);
        musicPlayerController.setButton(playBtn);
        musicPlayerController.setPicture(songPicture);
    }

    public void initialize() {
        isSongsSelected = false;
        isArtistsSelected = false;
        isAlbumsSelected = false;
        isGenresSelected = false;
        isYearsSelected = false;
        isRecentsSelected = false;
        isMostSelected = false;

        isTitleSorted = false;
        isArtistSorted = false;
        isAlbumSorted = false;
        isGenreSorted = false;
        isYearSorted = false;
        isTimeSorted = false;

        isKebabOpened = false;
        isProfileOpened = false;
        isNotifOpened = false;
        isPlayListKebabOpened = false;
        isQueueOpened = false;
        isCreateOpened = false;
        isCreateAlbumOpened = false;
        isUploadSongOpened = false;

        nextSong = 0;
        previousSong = 0;
        nextPlaylist = 0;
        previousPlaylist = 0;

        transitionGif.setImage(new Image(getClass().getResourceAsStream("/Media/waveGif.gif")));

        /*albumChoice.getItems().addAll("album1", "album2", "album3", "album4", "album5");*/

        /*setPlaylistView();*/
        try {
            setPlaylistView(model.getUserPlaylist());
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

        leftTab.setOnMouseClicked(event -> {
            createPane.setVisible(false);
            albumCreate.setVisible(false);
            songUpload.setVisible(false);
            addtoPlaylistPane.setVisible(false);
            song_etc_anchorpane.setVisible(false);
        });

        welcomePane.setOnMouseClicked(event -> {
            createPane.setVisible(false);
            albumCreate.setVisible(false);
            songUpload.setVisible(false);
            addtoPlaylistPane.setVisible(false);
            song_etc_anchorpane.setVisible(false);
        });

        artist_profile_pane.setOnMouseClicked(event -> {
            createPane.setVisible(false);
            albumCreate.setVisible(false);
            songUpload.setVisible(false);
            addtoPlaylistPane.setVisible(false);
            song_etc_anchorpane.setVisible(false);
        });

        profile_pane.setOnMouseClicked(event -> {
            createPane.setVisible(false);
            albumCreate.setVisible(false);
            songUpload.setVisible(false);
            addtoPlaylistPane.setVisible(false);
            song_etc_anchorpane.setVisible(false);
        });

        mainPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        mainPane.setOnMouseDragged(event -> {
            Stage primaryStage = (Stage) mainPane.getScene().getWindow();
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
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
            searchBtn.setImage(new Image(getClass().getResourceAsStream("/Media/search_hover.png")));
        });

        searchBtn.setOnMouseExited(event -> {
            searchBtn.setImage(new Image(getClass().getResourceAsStream("/Media/search.png")));
        });

        searchBtn.setOnMouseClicked(event -> {
            if (!searchBar.getText().isEmpty()) {
                searchPane.setVisible(true);

                artist_profile_pane.setVisible(false);
                profile_pane.setVisible(false);
                songsPane.setVisible(false);
                isQueueOpened = false;

                setSearchView("song", songSearchPane);
                setSearchView("artist", artistSearchPane);
                setSearchView("album", albumSearchPane);
                setSearchView("playlist", playlistSearchPane);
                setSearchView("profile", profileSearchPane);

                mostLbl.setTextFill(Color.web("#AAAAAA"));
                mostBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                songsLbl.setTextFill(Color.web("#AAAAAA"));
                songsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                artistsLbl.setTextFill(Color.web("#AAAAAA"));
                artistsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                albumsLbl.setTextFill(Color.web("#AAAAAA"));
                albumsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                genresLbl.setTextFill(Color.web("#AAAAAA"));
                genresBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                yearsLbl.setTextFill(Color.web("#AAAAAA"));
                yearsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                recentsLbl.setTextFill(Color.web("#AAAAAA"));
                recentsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
            }
        });

        playListBtn.setOnMouseEntered(event -> {
            playListBtn.setStyle("-fx-background-color: #00ffe3;");
            playListBtn.setPrefWidth(140);
            playListBtn.setPrefHeight(58);
            playListBtn.setLayoutX(610);
            playListBtn.setLayoutY(25);
        });

        playListBtn.setOnMouseExited(event -> {
            playListBtn.setStyle("-fx-background-color: #00ead0;");
            playListBtn.setPrefWidth(130);
            playListBtn.setPrefHeight(48);
            playListBtn.setLayoutX(615);
            playListBtn.setLayoutY(30);
        });

        followBtn.setOnMouseEntered(event -> {
            followBtn.setPrefWidth(160);
            followBtn.setPrefHeight(55);
            followBtn.setLayoutX(765);
            followBtn.setLayoutY(28);
        });

        followBtn.setOnMouseExited(event -> {
            followBtn.setPrefWidth(150);
            followBtn.setPrefHeight(40);
            followBtn.setLayoutX(770);
            followBtn.setLayoutY(30);
        });

        playListMore.setOnMouseEntered(event -> {
            playListMore.setImage(new Image(getClass().getResourceAsStream("/Media/kebab_hover.png")));
        });

        playListMore.setOnMouseExited(event -> {
            playListMore.setImage(new Image(getClass().getResourceAsStream("/Media/kebab.png")));
        });

        playListMore.setOnMouseClicked(event -> {
            if (!isPlayListKebabOpened) {
                isPlayListKebabOpened = true;
                playListKebab.setVisible(true);
            } else if (isPlayListKebabOpened) {
                isPlayListKebabOpened = false;
                playListKebab.setVisible(false);
            }
        });

        makePublicLbl.setOnMouseEntered(event -> {
            makePublicLbl.setTextFill(Color.web("#FFFFFF"));
        });
        makePublicLbl.setOnMouseExited(event -> {
            makePublicLbl.setTextFill(Color.web("#C6C6C6"));
        });

        deletePlaylistLbl.setOnMouseEntered(event -> {
            deletePlaylistLbl.setTextFill(Color.web("#FFFFFF"));
        });
        deletePlaylistLbl.setOnMouseExited(event -> {
            deletePlaylistLbl.setTextFill(Color.web("#C6C6C6"));
        });

        songsBtn.setOnAction(event -> {
            if (!isSongsSelected) {
                isSongsSelected = true;
                isArtistsSelected = false;
                isAlbumsSelected = false;
                isGenresSelected = false;
                isYearsSelected = false;
                isRecentsSelected = false;
                isMostSelected = false;
                songsLbl.setTextFill(Color.web("#FFFFFF"));
                songsBtn.setStyle("-fx-border-width: 0 0 0 4; -fx-border-color: #00ead0; -fx-background-color: transparent");

                artistsLbl.setTextFill(Color.web("#AAAAAA"));
                artistsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                albumsLbl.setTextFill(Color.web("#AAAAAA"));
                albumsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                genresLbl.setTextFill(Color.web("#AAAAAA"));
                genresBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                yearsLbl.setTextFill(Color.web("#AAAAAA"));
                yearsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                recentsLbl.setTextFill(Color.web("#AAAAAA"));
                recentsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                mostLbl.setTextFill(Color.web("#AAAAAA"));
                mostBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");

                welcomePane.setVisible(false);
                isProfileOpened = false;
                artist_profile_pane.setVisible(false);
                profile_pane.setVisible(false);
                sortPlaylistPane.setVisible(false);
                songsPane.setVisible(true);
                try {
                    setSongView(model.getUserSongs());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                songViewTitle.setText("Songs");
                songViewCreator.setText("");
                followBtn.setVisible(false);
                playListMore.setVisible(false);
                playListBtn.setVisible(true);
            } else {
                isProfileOpened = false;
                welcomePane.setVisible(false);
                profile_pane.setVisible(false);
                searchPane.setVisible(false);
            }
        });

        artistsBtn.setOnAction(event -> {
            if (!isArtistsSelected) {
                isSongsSelected = false;
                isArtistsSelected = true;
                isAlbumsSelected = false;
                isGenresSelected = false;
                isYearsSelected = false;
                isRecentsSelected = false;
                isMostSelected = false;
                artistsLbl.setTextFill(Color.web("#FFFFFF"));
                artistsBtn.setStyle("-fx-border-width: 0 0 0 4; -fx-border-color: #00ead0; -fx-background-color: transparent");

                songsLbl.setTextFill(Color.web("#AAAAAA"));
                songsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                albumsLbl.setTextFill(Color.web("#AAAAAA"));
                albumsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                genresLbl.setTextFill(Color.web("#AAAAAA"));
                genresBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                yearsLbl.setTextFill(Color.web("#AAAAAA"));
                yearsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                recentsLbl.setTextFill(Color.web("#AAAAAA"));
                recentsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                mostLbl.setTextFill(Color.web("#AAAAAA"));
                mostBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");

                welcomePane.setVisible(false);
                artist_profile_pane.setVisible(false);
                isProfileOpened = false;
                profile_pane.setVisible(false);
                songsPane.setVisible(false);
                sortPlaylistPane.setVisible(true);
                setSortPlaylistView("Artists");
            } else {
                isProfileOpened = false;
                welcomePane.setVisible(false);
                profile_pane.setVisible(false);
                searchPane.setVisible(false);
            }
        });

        albumsBtn.setOnAction(event -> {
            if (!isAlbumsSelected) {
                isSongsSelected = false;
                isArtistsSelected = false;
                isAlbumsSelected = true;
                isGenresSelected = false;
                isYearsSelected = false;
                isRecentsSelected = false;
                isMostSelected = false;
                albumsLbl.setTextFill(Color.web("#FFFFFF"));
                albumsBtn.setStyle("-fx-border-width: 0 0 0 4; -fx-border-color: #00ead0; -fx-background-color: transparent");

                songsLbl.setTextFill(Color.web("#AAAAAA"));
                songsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                artistsLbl.setTextFill(Color.web("#AAAAAA"));
                artistsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                genresLbl.setTextFill(Color.web("#AAAAAA"));
                genresBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                yearsLbl.setTextFill(Color.web("#AAAAAA"));
                yearsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                recentsLbl.setTextFill(Color.web("#AAAAAA"));
                recentsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                mostLbl.setTextFill(Color.web("#AAAAAA"));
                mostBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");

                welcomePane.setVisible(false);
                artist_profile_pane.setVisible(false);
                isProfileOpened = false;
                profile_pane.setVisible(false);
                songsPane.setVisible(false);
                sortPlaylistPane.setVisible(true);
                setSortPlaylistView("Albums");
            } else {
                isProfileOpened = false;
                welcomePane.setVisible(false);
                profile_pane.setVisible(false);
                searchPane.setVisible(false);
            }
        });

        genresBtn.setOnAction(event -> {
            if (!isGenresSelected) {
                isSongsSelected = false;
                isArtistsSelected = false;
                isAlbumsSelected = false;
                isGenresSelected = true;
                isYearsSelected = false;
                isRecentsSelected = false;
                isMostSelected = false;
                genresLbl.setTextFill(Color.web("#FFFFFF"));
                genresBtn.setStyle("-fx-border-width: 0 0 0 4; -fx-border-color: #00ead0; -fx-background-color: transparent");

                songsLbl.setTextFill(Color.web("#AAAAAA"));
                songsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                artistsLbl.setTextFill(Color.web("#AAAAAA"));
                artistsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                albumsLbl.setTextFill(Color.web("#AAAAAA"));
                albumsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                yearsLbl.setTextFill(Color.web("#AAAAAA"));
                yearsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                recentsLbl.setTextFill(Color.web("#AAAAAA"));
                recentsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                mostLbl.setTextFill(Color.web("#AAAAAA"));
                mostBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");

                welcomePane.setVisible(false);
                artist_profile_pane.setVisible(false);
                isProfileOpened = false;
                profile_pane.setVisible(false);
                songsPane.setVisible(false);
                sortPlaylistPane.setVisible(true);
                setSortPlaylistView("Genres");
            } else {
                isProfileOpened = false;
                welcomePane.setVisible(false);
                profile_pane.setVisible(false);
                searchPane.setVisible(false);
            }
        });

        yearsBtn.setOnAction(event -> {
            if (!isYearsSelected) {
                isSongsSelected = false;
                isArtistsSelected = false;
                isAlbumsSelected = false;
                isGenresSelected = false;
                isYearsSelected = true;
                isRecentsSelected = false;
                isMostSelected = false;
                yearsLbl.setTextFill(Color.web("#FFFFFF"));
                yearsBtn.setStyle("-fx-border-width: 0 0 0 4; -fx-border-color: #00ead0; -fx-background-color: transparent");

                songsLbl.setTextFill(Color.web("#AAAAAA"));
                songsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                artistsLbl.setTextFill(Color.web("#AAAAAA"));
                artistsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                albumsLbl.setTextFill(Color.web("#AAAAAA"));
                albumsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                genresLbl.setTextFill(Color.web("#AAAAAA"));
                genresBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                recentsLbl.setTextFill(Color.web("#AAAAAA"));
                recentsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                mostLbl.setTextFill(Color.web("#AAAAAA"));
                mostBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");

                welcomePane.setVisible(false);
                artist_profile_pane.setVisible(false);
                isProfileOpened = false;
                profile_pane.setVisible(false);
                songsPane.setVisible(false);
                sortPlaylistPane.setVisible(true);
                setSortPlaylistView("Years");
            } else {
                isProfileOpened = false;
                welcomePane.setVisible(false);
                profile_pane.setVisible(false);
                searchPane.setVisible(false);
            }
        });

        recentsBtn.setOnAction(event -> {
            if (!isRecentsSelected) {
                isSongsSelected = false;
                isArtistsSelected = false;
                isAlbumsSelected = false;
                isGenresSelected = false;
                isYearsSelected = false;
                isRecentsSelected = true;
                isMostSelected = false;
                recentsLbl.setTextFill(Color.web("#FFFFFF"));
                recentsBtn.setStyle("-fx-border-width: 0 0 0 4; -fx-border-color: #00ead0; -fx-background-color: transparent");

                songsLbl.setTextFill(Color.web("#AAAAAA"));
                songsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                artistsLbl.setTextFill(Color.web("#AAAAAA"));
                artistsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                albumsLbl.setTextFill(Color.web("#AAAAAA"));
                albumsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                genresLbl.setTextFill(Color.web("#AAAAAA"));
                genresBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                yearsLbl.setTextFill(Color.web("#AAAAAA"));
                yearsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                mostLbl.setTextFill(Color.web("#AAAAAA"));
                mostBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");

                welcomePane.setVisible(false);
                artist_profile_pane.setVisible(false);
                isProfileOpened = false;
                profile_pane.setVisible(false);
                sortPlaylistPane.setVisible(false);
                songsPane.setVisible(true);
//                setSongView();
                songViewTitle.setText("Recently Played");
                songViewCreator.setText("");
                followBtn.setVisible(false);
                playListMore.setVisible(false);
                playListBtn.setVisible(true);
            } else {
                isProfileOpened = false;
                welcomePane.setVisible(false);
                profile_pane.setVisible(false);
                searchPane.setVisible(false);
            }
        });

        mostBtn.setOnAction(event -> {
            if (!isMostSelected) {
                isSongsSelected = false;
                isArtistsSelected = false;
                isAlbumsSelected = false;
                isGenresSelected = false;
                isYearsSelected = false;
                isRecentsSelected = false;
                isMostSelected = true;
                mostLbl.setTextFill(Color.web("#FFFFFF"));
                mostBtn.setStyle("-fx-border-width: 0 0 0 4; -fx-border-color: #00ead0; -fx-background-color: transparent");

                songsLbl.setTextFill(Color.web("#AAAAAA"));
                songsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                artistsLbl.setTextFill(Color.web("#AAAAAA"));
                artistsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                albumsLbl.setTextFill(Color.web("#AAAAAA"));
                albumsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                genresLbl.setTextFill(Color.web("#AAAAAA"));
                genresBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                yearsLbl.setTextFill(Color.web("#AAAAAA"));
                yearsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                recentsLbl.setTextFill(Color.web("#AAAAAA"));
                recentsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");

                isProfileOpened = false;
                welcomePane.setVisible(false);
                artist_profile_pane.setVisible(false);
                profile_pane.setVisible(false);
                sortPlaylistPane.setVisible(false);
                songsPane.setVisible(true);
//                setSongView();
                songViewTitle.setText("Most Played");
                songViewCreator.setText("");
                followBtn.setVisible(false);
                playListMore.setVisible(false);
                playListBtn.setVisible(true);
            } else {
                isProfileOpened = false;
                welcomePane.setVisible(false);
                profile_pane.setVisible(false);
                searchPane.setVisible(false);
            }
        });

        playBtn.setOnMouseEntered(event -> {
            if (!musicPlayerController.isPlay()) {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/Media/play_button_hover.png")));
            } else {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/Media/pause_button_hover.png")));
            }
        });

        playBtn.setOnMouseExited(event -> {
            if (!musicPlayerController.isPlay()) {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/Media/play_button.png")));
            } else {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/Media/pause_button.png")));
            }
        });

        playBtn.setOnMouseClicked(event -> {
            if (!musicPlayerController.isPlay()) {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/Media/pause_button_hover.png")));
            } else {
                playBtn.setImage(new Image(getClass().getResourceAsStream("/Media/play_button_hover.png")));
            }
            if (musicPlayerController.getMedia() != null) {
                musicPlayerController.play();
            }
        });

        nextBtn.setOnMouseEntered(event -> {
            nextBtn.setImage(new Image(getClass().getResourceAsStream("/Media/next_button_hover.png")));
        });

        nextBtn.setOnMouseExited(event -> {
            nextBtn.setImage(new Image(getClass().getResourceAsStream("/Media/next_button.png")));
        });

        nextBtn.setOnMouseClicked(event -> {
            if (musicPlayerController.getMedia() != null) {
                musicPlayerController.next();
            }
        });

        prevBtn.setOnMouseEntered(event -> {
            prevBtn.setImage(new Image(getClass().getResourceAsStream("/Media/previous_button_hover.png")));
        });

        prevBtn.setOnMouseExited(event -> {
            prevBtn.setImage(new Image(getClass().getResourceAsStream("/Media/previous_button.png")));
        });

        prevBtn.setOnMouseClicked(event -> {
            if (musicPlayerController.getMedia() != null) {
                musicPlayerController.prev();
            }
        });

        shuffleBtn.setOnMouseEntered(event -> {
            if (!musicPlayerController.isShuffle()) {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/Media/shuffle_hover.png")));
            } else {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/Media/shuffle_clicked.png")));
            }
        });

        shuffleBtn.setOnMouseExited(event -> {
            if (!musicPlayerController.isShuffle()) {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/Media/shuffle.png")));
            } else {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/Media/shuffle_clicked.png")));
            }
        });

        shuffleBtn.setOnMouseClicked(event -> {
            if (!musicPlayerController.isShuffle()) {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/Media/shuffle_clicked.png")));
            } else {
                shuffleBtn.setImage(new Image(getClass().getResourceAsStream("/Media/shuffle.png")));
            }
            if (musicPlayerController.getMedia() != null) {
                musicPlayerController.shuffle();
            }
        });

        repeatBtn.setOnMouseEntered(event -> {
            if (musicPlayerController.getRepeat() == 0) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/Media/repeat_hover.png")));
            } else if (musicPlayerController.getRepeat() == 1) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/Media/repeat_clicked.png")));
            } else if (musicPlayerController.getRepeat() == 2) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/Media/repeat_song_clicked.png")));
            }
        });

        repeatBtn.setOnMouseExited(event -> {
            if (musicPlayerController.getRepeat() == 0) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/Media/repeat.png")));
            } else if (musicPlayerController.getRepeat() == 1) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/Media/repeat_clicked.png")));
            } else if (musicPlayerController.getRepeat() == 2) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/Media/repeat_song_clicked.png")));
            }
        });

        repeatBtn.setOnMouseClicked(event -> {
            if (musicPlayerController.getRepeat() == 0) {
                repeatBtn.setFitHeight(20);
                repeatBtn.setFitWidth(20);
                repeatBtn.setLayoutY(27);
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/Media/repeat_clicked.png")));
            } else if (musicPlayerController.getRepeat() == 1) {
                repeatBtn.setFitHeight(25);
                repeatBtn.setFitWidth(35);
                repeatBtn.setLayoutY(22);
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/Media/repeat_song_clicked.png")));
            } else if (musicPlayerController.getRepeat() == 2) {
                repeatBtn.setImage(new Image(getClass().getResourceAsStream("/Media/repeat.png")));
            }
            if (musicPlayerController.getMedia() != null) {
                musicPlayerController.repeat();
            }
        });

        muteBtn.setOnMouseEntered(event -> {
            if (!musicPlayerController.isMute()) {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/Media/volume_hover.png")));
            } else {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/Media/mute_clicked.png")));
            }
        });

        muteBtn.setOnMouseExited(event -> {
            if (!musicPlayerController.isMute()) {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/Media/volume.png")));
            } else {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/Media/mute_clicked.png")));
            }
        });

        muteBtn.setOnMouseClicked(event -> {
            if (!musicPlayerController.isMute()) {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/Media/mute_clicked.png")));
            } else {
                muteBtn.setImage(new Image(getClass().getResourceAsStream("/Media/volume_hover.png")));
            }
            musicPlayerController.mute();
        });

        public_playlists_btn.setOnMouseClicked(event -> {
            public_playlists_btn.setTextFill(Color.web("#00ead0"));
            prof_followers_btn.setTextFill(Color.web("#FFFFFF"));
            prof_following_btn.setTextFill(Color.web("#FFFFFF"));

            listener_vbox.getChildren().clear();

            try {
                for (int i = 0; i < model.getUserPlaylist(viewUser.getUsername()).size(); i++) {
                    PlaylistInterface p = model.getUserPlaylist(viewUser.getUsername()).get(i);
                    Label pubplaylist = new Label(p.getName()); //insert musicplayerController.getpublicplaylistname or something
                    listener_vbox.getChildren().add(pubplaylist);
                    listener_vbox.setSpacing(24);
                    pubplaylist.setTextFill(Color.web("#FFFFFF"));
                    pubplaylist.setAlignment(Pos.CENTER);
                    pubplaylist.setFont(Font.font(24));
                    pubplaylist.setTextAlignment(TextAlignment.CENTER);

                    pubplaylist.setOnMouseEntered(event1 -> {
                        pubplaylist.setTextFill(Color.web("#00ead0"));
                    });

                    pubplaylist.setOnMouseExited(event1 -> {
                        pubplaylist.setTextFill(Color.web("#FFFFFF"));
                    });

                    pubplaylist.setOnMouseClicked(event1 -> {
                        isProfileOpened = false;
                        profile_pane.setVisible(false);
                        songsPane.setVisible(true);
                        setSongView(p.getSongs());
                        songViewTitle.setText(pubplaylist.getText());
                        songViewCreator.setText("created by: " + viewUser.getName());
                        followBtn.setVisible(true);
                        playListMore.setVisible(true);
                        playListBtn.setVisible(true);
                    });
                }
            } catch (SQLException sq) {
            }
        });

        prof_following_btn.setOnMouseClicked(event -> {
            prof_following_btn.setTextFill(Color.web("#00ead0"));
            prof_followers_btn.setTextFill(Color.web("#FFFFFF"));
            public_playlists_btn.setTextFill(Color.web("#FFFFFF"));

            listener_vbox.getChildren().clear();

            try {
                for (int i = 0; i < model.getFollowed(viewUser.getName()).size(); i++) {
                    AccountInterface acc = model.getFollowed(viewUser.getName()).get(i);
                    Label followings = new Label(acc.getUsername()); //insert musicplayerController.getpublicplaylistname or something
                    listener_vbox.getChildren().add(followings);
                    listener_vbox.setSpacing(24);
                    followings.setTextFill(Color.web("#FFFFFF"));
                    followings.setAlignment(Pos.CENTER);
                    followings.setFont(Font.font(24));
                    followings.setTextAlignment(TextAlignment.CENTER);

                    followings.setOnMouseEntered(event1 -> {
                        followings.setTextFill(Color.web("#00ead0"));
                    });

                    followings.setOnMouseExited(event1 -> {
                        followings.setTextFill(Color.web("#FFFFFF"));
                    });

                    followings.setOnMouseClicked(event1 -> {
                        viewUser = model.getUser(followings.getText());
                        if(viewUser instanceof Artist) {
                            showArtistProfilePane(followings.getText());
                        } else if (viewUser instanceof Listener) {
                            showListenerProfilePane(followings.getText());
                        }
                        public_playlists_btn.fireEvent(event1);
                        prof_followers_btn.fireEvent(event1);
                        prof_following_btn.fireEvent(event1);
                        logoutBtn.setVisible(false);
                    });
                }
            } catch (SQLException sq) {
            }
        });

        prof_followers_btn.setOnMouseClicked(event -> {
            prof_followers_btn.setTextFill(Color.web("#00ead0"));
            public_playlists_btn.setTextFill(Color.web("#FFFFFF"));
            prof_following_btn.setTextFill(Color.web("#FFFFFF"));

            listener_vbox.getChildren().clear();

            try {
                for (int i = 0; i < model.getFollowers(viewUser.getUsername()).size(); i++) {
                    AccountInterface acc = model.getFollowers(viewUser.getUsername()).get(i);
                    Label followers = new Label(acc.getUsername()); //insert musicplayerController.getpublicplaylistname or something
                    listener_vbox.getChildren().add(followers);
                    listener_vbox.setSpacing(24);
                    followers.setTextFill(Color.web("#FFFFFF"));
                    followers.setAlignment(Pos.CENTER);
                    followers.setFont(Font.font(24));
                    followers.setTextAlignment(TextAlignment.CENTER);

                    followers.setOnMouseEntered(event1 -> {
                        followers.setTextFill(Color.web("#00ead0"));
                    });

                    followers.setOnMouseExited(event1 -> {
                        followers.setTextFill(Color.web("#FFFFFF"));
                    });

                    followers.setOnMouseClicked(event1 -> {
                        viewUser = model.getUser(followers.getText());
                        if(viewUser instanceof Artist) {
                            showArtistProfilePane(followers.getText());
                        } else if (viewUser instanceof Listener) {
                            showListenerProfilePane(followers.getText());
                        }
                        public_playlists_btn.fireEvent(event1);
                        prof_followers_btn.fireEvent(event1);
                        prof_following_btn.fireEvent(event1);
                        //if following then display unfollow btn otherwise
                        //listenerfollowBtn.setVisible(true);
                    });
                }
            } catch (SQLException sq) {
            }
        });

        artist_prof_albums_btn.setOnMouseClicked(event -> {
            artist_prof_albums_btn.setTextFill(Color.web("#00ead0"));
            artist_prof_followers_btn.setTextFill(Color.web("#FFFFFF"));
            artist_prof_following_btn.setTextFill(Color.web("#FFFFFF"));
            artist_prof_playlists_btn.setTextFill(Color.web("#FFFFFF"));

            artist_vbox.getChildren().clear();

            for (int i = 0; i <= 24; i++) {
                Label albums = new Label("Album " + i); //insert musicplayerController.getpublicplaylistname or something
                artist_vbox.getChildren().add(albums);
                artist_vbox.setSpacing(24);
                albums.setTextFill(Color.web("#FFFFFF"));
                albums.setAlignment(Pos.CENTER);
                albums.setFont(Font.font(24));
                albums.setTextAlignment(TextAlignment.CENTER);

                albums.setOnMouseEntered(event1 -> {
                    albums.setTextFill(Color.web("#00ead0"));
                });

                albums.setOnMouseExited(event1 -> {
                    albums.setTextFill(Color.web("#FFFFFF"));
                });

                albums.setOnMouseClicked(event1 -> {
                    artist_profile_pane.setVisible(false);
                    songsPane.setVisible(true);
//                    setSongView();
                });
            }
        });

        artist_prof_playlists_btn.setOnMouseClicked(event -> {
            artist_prof_playlists_btn.setTextFill(Color.web("#00ead0"));
            artist_prof_followers_btn.setTextFill(Color.web("#FFFFFF"));
            artist_prof_following_btn.setTextFill(Color.web("#FFFFFF"));
            artist_prof_albums_btn.setTextFill(Color.web("#FFFFFF"));

            artist_vbox.getChildren().clear();

            for (int i = 0; i <= 24; i++) {
                Label playlists = new Label("Playlist " + i); //insert musicplayerController.getpublicplaylistname or something
                artist_vbox.getChildren().add(playlists);
                artist_vbox.setSpacing(24);
                playlists.setTextFill(Color.web("#FFFFFF"));
                playlists.setAlignment(Pos.CENTER);
                playlists.setFont(Font.font(24));
                playlists.setTextAlignment(TextAlignment.CENTER);

                playlists.setOnMouseEntered(event1 -> {
                    playlists.setTextFill(Color.web("#00ead0"));
                });

                playlists.setOnMouseExited(event1 -> {
                    playlists.setTextFill(Color.web("#FFFFFF"));
                });

                playlists.setOnMouseClicked(event1 -> {
                    artist_profile_pane.setVisible(false);
                    songsPane.setVisible(true);
//                    setSongView();
                });
            }
        });

        artist_prof_following_btn.setOnMouseClicked(event -> {
            artist_prof_following_btn.setTextFill(Color.web("#00ead0"));
            artist_prof_followers_btn.setTextFill(Color.web("#FFFFFF"));
            artist_prof_albums_btn.setTextFill(Color.web("#FFFFFF"));
            artist_prof_playlists_btn.setTextFill(Color.web("#FFFFFF"));

            artist_vbox.getChildren().clear();

            for (int i = 0; i <= 24; i++) {
                Label afollowing = new Label("MAMAMOO " + i); //insert musicplayerController.getpublicplaylistname or something
                artist_vbox.getChildren().add(afollowing);
                artist_vbox.setSpacing(24);
                afollowing.setTextFill(Color.web("#FFFFFF"));
                afollowing.setAlignment(Pos.CENTER);
                afollowing.setFont(Font.font(24));
                afollowing.setTextAlignment(TextAlignment.CENTER);

                afollowing.setOnMouseEntered(event1 -> {
                    afollowing.setTextFill(Color.web("#00ead0"));
                });

                afollowing.setOnMouseExited(event1 -> {
                    afollowing.setTextFill(Color.web("#FFFFFF"));
                });

                afollowing.setOnMouseClicked(event1 -> {
                    showArtistProfilePane(afollowing.getText());
                    artistunfollowBtn.setVisible(true);
                    artistfollowBtn.setVisible(false);
                });
            }
        });

        artist_prof_followers_btn.setOnMouseClicked(event -> {
            artist_prof_followers_btn.setTextFill(Color.web("#00ead0"));
            artist_prof_albums_btn.setTextFill(Color.web("#FFFFFF"));
            artist_prof_following_btn.setTextFill(Color.web("#FFFFFF"));
            artist_prof_playlists_btn.setTextFill(Color.web("#FFFFFF"));

            artist_vbox.getChildren().clear();

            for (int i = 0; i <= 24; i++) {
                Label afollowers = new Label("Kae " + i); //insert musicplayerController.getplaylistname or something
                artist_vbox.getChildren().add(afollowers);
                artist_vbox.setSpacing(24);
                afollowers.setTextFill(Color.web("#FFFFFF"));
                afollowers.setAlignment(Pos.CENTER);
                afollowers.setFont(Font.font(24));
                afollowers.setTextAlignment(TextAlignment.CENTER);

                afollowers.setOnMouseEntered(event1 -> {
                    afollowers.setTextFill(Color.web("#00ead0"));
                });

                afollowers.setOnMouseExited(event1 -> {
                    afollowers.setTextFill(Color.web("#FFFFFF"));
                });

                afollowers.setOnMouseClicked(event1 -> {
                    showListenerProfilePane(afollowers.getText());
                    artistunfollowBtn.setVisible(false);
                    artistfollowBtn.setVisible(true);
                });
            }
        });

        artistfollowBtn.setOnMouseEntered(event -> {
            artistfollowBtn.setPrefWidth(160);
            artistfollowBtn.setPrefHeight(55);
            artistfollowBtn.setLayoutX(817);
            artistfollowBtn.setLayoutY(86);
        });

        artistfollowBtn.setOnMouseExited(event -> {
            artistfollowBtn.setPrefWidth(150);
            artistfollowBtn.setPrefHeight(40);
            artistfollowBtn.setLayoutX(822);
            artistfollowBtn.setLayoutY(88);
        });

        artistunfollowBtn.setOnMouseEntered(event -> {
            artistunfollowBtn.setPrefWidth(160);
            artistunfollowBtn.setPrefHeight(55);
            artistunfollowBtn.setLayoutX(817);
            artistunfollowBtn.setLayoutY(86);
        });

        artistunfollowBtn.setOnMouseExited(event -> {
            artistunfollowBtn.setPrefWidth(150);
            artistunfollowBtn.setPrefHeight(40);
            artistunfollowBtn.setLayoutX(822);
            artistunfollowBtn.setLayoutY(88);
        });

        listenerfollowBtn.setOnMouseEntered(event -> {
            listenerfollowBtn.setPrefWidth(160);
            listenerfollowBtn.setPrefHeight(55);
            listenerfollowBtn.setLayoutX(827);
            listenerfollowBtn.setLayoutY(93);
        });

        listenerfollowBtn.setOnMouseExited(event -> {
            listenerfollowBtn.setPrefWidth(150);
            listenerfollowBtn.setPrefHeight(40);
            listenerfollowBtn.setLayoutX(832);
            listenerfollowBtn.setLayoutY(95);
        });

        listenerunfollowBtn.setOnMouseEntered(event -> {
            listenerunfollowBtn.setPrefWidth(160);
            listenerunfollowBtn.setPrefHeight(55);
            listenerunfollowBtn.setLayoutX(827);
            listenerunfollowBtn.setLayoutY(93);
        });

        listenerunfollowBtn.setOnMouseExited(event -> {
            listenerunfollowBtn.setPrefWidth(150);
            listenerunfollowBtn.setPrefHeight(40);
            listenerunfollowBtn.setLayoutX(832);
            listenerunfollowBtn.setLayoutY(95);
        });

        add_to_queue_btn.setOnMouseEntered(event -> {
            add_to_queue_btn.setTextFill(Color.web("#FFFFFF"));
        });
        add_to_queue_btn.setOnMouseExited(event -> {
            add_to_queue_btn.setTextFill(Color.web("#C6C6C6"));
        });

        remove_from_playlist_btn.setOnMouseEntered(event -> {
            remove_from_playlist_btn.setTextFill(Color.web("#FFFFFF"));
        });
        remove_from_playlist_btn.setOnMouseExited(event -> {
            remove_from_playlist_btn.setTextFill(Color.web("#C6C6C6"));
        });

        remove_from_playlist_btn.setOnMouseClicked(event -> {
            if(controller.removeFromPlaylist(model.getUser().getSongs().get(nextSong), songViewTitle.getText().trim())) {
                try {
                    setSongView(model.getUserPlaylist().get(nextPlaylist).getSongs());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("CLOSE");
            song_etc_anchorpane.setVisible(false);
            isKebabOpened = false;
        });

        add_to_playlist_btn.setOnMouseEntered(event -> {
            add_to_playlist_btn.setTextFill(Color.web("#FFFFFF"));
        });
        add_to_playlist_btn.setOnMouseExited(event -> {
            add_to_playlist_btn.setTextFill(Color.web("#C6C6C6"));
        });

        add_to_playlist_btn.setOnMouseClicked(mouseEvent -> {
            if (!isaddtoplaylistOpened) {
                System.out.println("OPEN");
                addtoPlaylistPane.setVisible(true);
                addtoPlaylistPane.setLayoutX(mouseEvent.getSceneX() - 150);
                addtoPlaylistPane.setLayoutY((mouseEvent.getSceneY()));
                addtoPlaylistPane.setDisable(false);
                addtoPlaylistPane.setOpacity(1);
                playlistChoice.getItems().clear();
                try {
                    for (int i = 0; i < model.getUserPlaylist().size(); i++) {
                        playlistChoice.getItems().add(model.getUserPlaylist().get(i).getName());
                    }
                } catch (SQLException sq) { }
                isaddtoplaylistOpened = true;
            } else if (isaddtoplaylistOpened) {
                System.out.println("CLOSE");
                addtoPlaylistPane.setVisible(false);
                isaddtoplaylistOpened = false;
            }
        });

        addplaylistenterBtn.setOnMouseEntered(event -> {
            addplaylistenterBtn.setStyle("-fx-background-color: #00ffe3;");
        });

        addplaylistenterBtn.setOnMouseExited(event -> {
            addplaylistenterBtn.setStyle("-fx-background-color: #00ead0;");
        });

        addplaylistenterBtn.setOnAction(event -> {
            if (playlistChoice.getValue()!=null){
                isaddtoplaylistOpened=false;
                isKebabOpened=false;
                if(controller.addSongToPlaylist(model.getUser().getSongs().get(nextSong), playlistChoice.getValue().toString())) {
                    update();
                }
                addtoPlaylistPane.setVisible(false);
                song_etc_anchorpane.setVisible(false);
            }
        });

        addplaylistcancelBtn.setOnMouseEntered(event -> {
            addplaylistcancelBtn.setStyle("-fx-background-color: #00ffe3;");
        });

        addplaylistcancelBtn.setOnMouseExited(event -> {
            addplaylistcancelBtn.setStyle("-fx-background-color: #00ead0;");
        });

        addplaylistcancelBtn.setOnAction(event -> {
            isaddtoplaylistOpened=false;
            isKebabOpened=true;
            addtoPlaylistPane.setVisible(false);
            song_etc_anchorpane.setVisible(true);
        });

        profile_icon_btn.setOnAction(event -> {
            if (!isProfileOpened) {
                viewUser = model.getUser();
                showProfilePane(viewUser.getName());
            } else if (isProfileOpened) {
                isProfileOpened = false;
                profile_pane.setVisible(false);
                welcomePane.setVisible(true);
            }
        });

        notif_closeBtn.setOnAction(event -> {
            notif_pane.setVisible(false);
            isNotifOpened = false;
        });

        testBtn.setOnAction(event -> {
            notif_pane.setVisible(true);
            FadeTransition fadeIn = new FadeTransition();
            fadeIn.setDuration(Duration.seconds(0.1));
            fadeIn.setFromValue(0);
            fadeIn.setToValue(10);
            fadeIn.setAutoReverse(false);
            fadeIn.setNode(notif_pane);
            fadeIn.play();
            fadeIn.setOnFinished(event1 -> {
                isNotifOpened = true;
                PathTransition moveNotif = new PathTransition();
                moveNotif.setDuration(Duration.seconds(10));
                moveNotif.setPath(tempPath);
                moveNotif.setNode(tempRect);
                moveNotif.setAutoReverse(false);
                moveNotif.play();
                moveNotif.setOnFinished(event2 -> {
                    if (isNotifOpened) {
                        FadeTransition fadeOut = new FadeTransition();
                        fadeOut.setDuration(Duration.seconds(0.5));
                        fadeOut.setFromValue(10);
                        fadeOut.setToValue(0);
                        fadeOut.setAutoReverse(false);
                        fadeOut.setNode(notif_pane);
                        fadeOut.play();
                        fadeOut.setOnFinished(event3 -> {
                            isNotifOpened = false;
                            notif_pane.setVisible(false);
                        });
                    }
                });
            });
        });

        createPlaylistBtn.setOnMouseClicked(event -> {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("");
            dialog.setHeaderText("Create a Playlist");
            dialog.setContentText("Playlist Name:");
            dialog.setGraphic(null);

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                if(controller.createPlaylist(result.get())) {
                    try {
                        setPlaylistView(model.getUserPlaylist());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        logoutBtn.setOnMouseEntered(event -> {
            logoutBtn.setStyle("-fx-background-color: #00ffe3;");
            logoutBtn.setPrefWidth(140);
            logoutBtn.setPrefHeight(60);
            logoutBtn.setLayoutX(835);
            logoutBtn.setLayoutY(90);
        });

        logoutBtn.setOnMouseExited(event -> {
            logoutBtn.setStyle("-fx-background-color: #00ead0;");
            logoutBtn.setPrefWidth(130);
            logoutBtn.setPrefHeight(50);
            logoutBtn.setLayoutX(840);
            logoutBtn.setLayoutY(95);
        });

        logoutBtn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("");
            alert.setHeaderText("Logout");
            alert.setContentText("Are you sure you want to logout?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) logoutBtn.getScene().getWindow();
                LoginView loginView = new LoginView(stage, this.model);
            }
        });

        queueBtn.setOnMouseEntered(event -> {
            queueBtn.setImage(new Image(getClass().getResourceAsStream("/Media/queue_hover.png")));
        });

        queueBtn.setOnMouseExited(event -> {
            queueBtn.setImage(new Image(getClass().getResourceAsStream("/Media/queue.png")));
        });

        queueBtn.setOnMouseClicked(event -> {
            if (!isQueueOpened) {
                isQueueOpened = true;
                songsPane.setVisible(true);
                isSongsSelected = false;
                setSongView(model.getCurrentPlaylist().getSongs());
                songViewTitle.setText("Queue");
                songViewCreator.setText("");
                followBtn.setVisible(false);
                playListMore.setVisible(false);
                playListBtn.setVisible(false);
                artist_profile_pane.setVisible(false);
                profile_pane.setVisible(false);

                mostLbl.setTextFill(Color.web("#AAAAAA"));
                mostBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                songsLbl.setTextFill(Color.web("#AAAAAA"));
                songsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                artistsLbl.setTextFill(Color.web("#AAAAAA"));
                artistsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                albumsLbl.setTextFill(Color.web("#AAAAAA"));
                albumsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                genresLbl.setTextFill(Color.web("#AAAAAA"));
                genresBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                yearsLbl.setTextFill(Color.web("#AAAAAA"));
                yearsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
                recentsLbl.setTextFill(Color.web("#AAAAAA"));
                recentsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
            } else if (isQueueOpened) {
                isQueueOpened = false;
                songsPane.setVisible(false);
                welcomePane.setVisible(true);
            }
        });

        createBtn.setOnMouseClicked(event -> {
            if (!isCreateOpened) {
                isCreateOpened = true;
                createPane.setVisible(true);
            } else if (isCreateOpened) {
                isCreateOpened = false;
                createPane.setVisible(false);
            }
        });

        createAlbumBtn.setOnMouseEntered(event -> {
            createAlbumBtn.setTextFill(Color.web("#FFFFFF"));
        });
        createAlbumBtn.setOnMouseExited(event -> {
            createAlbumBtn.setTextFill(Color.web("#C6C6C6"));
        });

        createAlbumBtn.setOnMouseClicked(event -> {
            if (!isCreateAlbumOpened) {
                isCreateAlbumOpened = true;
                albumCreate.setVisible(true);
                createPane.setVisible(false);
            } else if (isCreateAlbumOpened) {
                isCreateAlbumOpened = false;
                albumCreate.setVisible(false);
            }
        });

        albumEnterBtn.setOnMouseEntered(event -> {
            albumEnterBtn.setStyle("-fx-background-color: #00ffe3;");
        });

        albumEnterBtn.setOnMouseExited(event -> {
            albumEnterBtn.setStyle("-fx-background-color: #00ead0;");
        });

        albumEnterBtn.setOnAction(event -> {
            if (!albumNameInput.getText().isEmpty()) {
                isCreateAlbumOpened = false;
                isCreateOpened = false;
                albumCreate.setVisible(false);
                createPane.setVisible(false);
                if(singleChoice.isSelected()) {
                    if(controller.createAlbum(albumNameInput.getText().trim() + " - Single")) {
                        albumNameInput.clear();
                        updateAlbumList();
                    }
                } else {
                    if(controller.createAlbum(albumNameInput.getText().trim())) {
                        albumNameInput.clear();
                        updateAlbumList();
                    }
                }
            }
        });

        albumCancelBtn.setOnMouseEntered(event -> {
            albumCancelBtn.setStyle("-fx-background-color: #00ffe3;");
        });

        albumCancelBtn.setOnMouseExited(event -> {
            albumCancelBtn.setStyle("-fx-background-color: #00ead0;");
        });

        albumCancelBtn.setOnAction(event -> {
            isCreateAlbumOpened = false;
            isCreateOpened = true;
            albumCreate.setVisible(false);
            createPane.setVisible(true);
        });

        uploadEnterBtn.setOnMouseEntered(event -> {
            uploadEnterBtn.setStyle("-fx-background-color: #00ffe3;");
        });

        uploadEnterBtn.setOnMouseExited(event -> {
            uploadEnterBtn.setStyle("-fx-background-color: #00ead0;");
        });

        uploadEnterBtn.setOnAction(event -> {
            if (albumChoice.getValue() != null) {
                isUploadSongOpened = false;
                isCreateOpened = false;
                songUpload.setVisible(false);
                createPane.setVisible(false);
                FileChooser file = new FileChooser();
                List<File> list = file.showOpenMultipleDialog(stage);
                if (list != null) {
                    int i = 0;
                    while (i < list.size()) {
                        String location = list.get(i).toURI().toString().substring(6, list.get(i).toURI().toString().length()).replaceAll("%20", " ").replaceAll("%5", " ");
                        System.out.println(location);
                        System.out.println(albumChoice.getValue().toString());
                        if (controller.addSong(location, albumChoice.getValue().toString())) {
                            i++;
                        }
                    }
                }
                songsBtn.fire();
            }
        });

        uploadCancelBtn.setOnMouseEntered(event -> {
            uploadCancelBtn.setStyle("-fx-background-color: #00ffe3;");
        });

        uploadCancelBtn.setOnMouseExited(event -> {
            uploadCancelBtn.setStyle("-fx-background-color: #00ead0;");
        });

        uploadCancelBtn.setOnAction(event -> {
            isUploadSongOpened = false;
            isCreateOpened = true;
            songUpload.setVisible(false);
            createPane.setVisible(true);
        });

        uploadSongBtn.setOnMouseEntered(event -> {
            uploadSongBtn.setTextFill(Color.web("#FFFFFF"));
        });
        uploadSongBtn.setOnMouseExited(event -> {
            uploadSongBtn.setTextFill(Color.web("#C6C6C6"));
        });

        uploadSongBtn.setOnMouseClicked(event -> {
            if (!isUploadSongOpened) {
                isUploadSongOpened = true;
                songUpload.setVisible(true);
            } else if (isUploadSongOpened) {
                isUploadSongOpened = false;
                songUpload.setVisible(false);
            }
        });

        homeBtn.setOnMouseClicked(event -> {
            isProfileOpened = false;
            isCreateOpened = false;
            isSongsSelected = false;
            isArtistsSelected = false;
            isAlbumsSelected = false;
            isGenresSelected = false;
            isYearsSelected = false;
            isRecentsSelected = false;
            isMostSelected = false;
            profile_pane.setVisible(false);
            songsPane.setVisible(false);
            createPane.setVisible(false);
            searchPane.setVisible(false);
            artist_profile_pane.setVisible(false);

            welcomePane.setVisible(true);
            sortPlaylistPane.setVisible(false);
            mostLbl.setTextFill(Color.web("#AAAAAA"));
            mostBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
            songsLbl.setTextFill(Color.web("#AAAAAA"));
            songsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
            artistsLbl.setTextFill(Color.web("#AAAAAA"));
            artistsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
            albumsLbl.setTextFill(Color.web("#AAAAAA"));
            albumsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
            genresLbl.setTextFill(Color.web("#AAAAAA"));
            genresBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
            yearsLbl.setTextFill(Color.web("#AAAAAA"));
            yearsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
            recentsLbl.setTextFill(Color.web("#AAAAAA"));
            recentsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        });
    }

    public void setSongView(ObservableList<SongInterface> songs) {
        ArrayList<StackPane> songStack = new ArrayList<>();
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        ArrayList<AnchorPane> anchors = new ArrayList<>();

        int numSongs = songs.size();

        profile_pane.setVisible(false);
        searchPane.setVisible(false);
        welcomePane.setVisible(false);

        songList.getChildren().clear();
        songStack.clear();
        rectangles.clear();
        anchors.clear();


        for (int i = 0; i < numSongs; i++) {
            SongInterface ss = songs.get(i);
            songStack.add(new StackPane());
            rectangles.add(new Rectangle());
            rectangles.get(i).setWidth(1000);
            rectangles.get(i).setHeight(50);
            rectangles.get(i).setFill(Color.web("#202020"));
            songStack.get(i).getChildren().add(rectangles.get(i));
            anchors.add(new AnchorPane());
            ImageView kebab = new ImageView(new Image(getClass().getResourceAsStream("/Media/kebab.png")));
            kebab.setFitWidth(25);
            kebab.setFitHeight(7);
            kebab.setLayoutX(940);
            kebab.setLayoutY(25);
            kebab.setOpacity(0.0);
            kebab.setDisable(true);
            kebab.setOnMouseEntered(event -> {
                kebab.setImage(new Image(getClass().getResourceAsStream("/Media/kebab_hover.png")));
            });
            kebab.setOnMouseExited(new EventHandler<>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    kebab.setImage(new Image(getClass().getResourceAsStream("/Media/kebab.png")));
                }
            });
            int finalI1 = i;
            kebab.setOnMouseClicked(mouseEvent -> {
                if (!isKebabOpened) {
                    System.out.println("OPEN");
                    song_etc_anchorpane.setVisible(true);
                    song_etc_anchorpane.setLayoutX(kebab.getLayoutX());
                    song_etc_anchorpane.setLayoutY((mouseEvent.getSceneY()));
                    remove_from_playlist_btn.setTextFill(Color.web("#C6C6C6"));
                    if(isSongsSelected) {
                        remove_from_playlist_btn.setDisable(true);
                    } else {
                        remove_from_playlist_btn.setDisable(false);
                    }
                    song_etc_anchorpane.setDisable(false);
                    song_etc_anchorpane.setOpacity(1);
                    isKebabOpened = true;
                } else if (isKebabOpened) {
                    System.out.println("CLOSE");
                    song_etc_anchorpane.setVisible(false);
                    song_etc_anchorpane.setDisable(true);
                    song_etc_anchorpane.setOpacity(0);
                    isKebabOpened = false;
                }
            });

            Label title = new Label(ss.getName());
            title.setMaxWidth(255);
            title.setTextFill(Color.web("#FFFFFF"));
            title.setFont(new Font("Brown", 20));
            title.setLayoutX(25);
            title.setLayoutY(10);
            Label artist = new Label(ss.getArtist());
            artist.setMaxWidth(165);
            artist.setTextFill(Color.web("#FFFFFF"));
            artist.setFont(new Font("Brown", 20));
            artist.setLayoutX(300);
            artist.setLayoutY(10);
            Label album = new Label(ss.getAlbum());
            album.setMaxWidth(165);
            album.setTextFill(Color.web("#FFFFFF"));
            album.setFont(new Font("Brown", 20));
            album.setLayoutX(485);
            album.setLayoutY(10);
            Label genre = new Label(ss.getGenre());
            genre.setMaxWidth(110);
            genre.setTextFill(Color.web("#FFFFFF"));
            genre.setFont(new Font("Brown", 20));
            genre.setLayoutX(670);
            genre.setLayoutY(10);
            Label date = new Label("" + ss.getYear());
            date.setMaxWidth(55);
            date.setTextFill(Color.web("#FFFFFF"));
            date.setFont(new Font("Brown", 20));
            date.setLayoutX(800);
            date.setLayoutY(10);
            Label time = new Label(musicPlayerController.getDuration(ss.getLength()));
            time.setMaxWidth(55);
            time.setTextFill(Color.web("#FFFFFF"));
            time.setFont(new Font("Brown", 20));
            time.setLayoutX(875);
            time.setLayoutY(10);
            anchors.get(i).getChildren().addAll(kebab, title, artist, album, genre, date, time);
            int finalI = i;
            anchors.get(i).setOnMouseClicked(event -> {
                if(event.getClickCount() == 1) {
                    previousSong = nextSong;
                    nextSong = finalI;
                    for (Node node : anchors.get(previousSong).getChildren()) {
                        if (node instanceof Label) {
                            ((Label) node).setTextFill(Color.web("#FFFFFF"));
                        }
                    }
                    for (Node node : anchors.get(nextSong).getChildren()) {
                        if (node instanceof Label) {
                            ((Label) node).setTextFill(Color.web("#00ead0"));
                        }
                    }
                } else if(event.getClickCount() == 2) {
                    if(isSongsSelected) {
                        if(musicPlayerController.getMedia() != null) {
                            if(musicPlayerController.getMedia().getStatus().equals(MediaPlayer.Status.PLAYING) ||
                                musicPlayerController.getMedia().getStatus().equals(MediaPlayer.Status.PAUSED)) {
                                musicPlayerController.stop();
                            }
                        }
                        try {
                            model.getCurrentPlaylist().setSongs(model.getAllSong());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        musicPlayerController.setIndex(finalI);
                        musicPlayerController.play();
                    }
                }

                /*int z = 0, c = 0;
                previousSong = nextSong;
                nextSong = finalI;

                for (Node node : anchors.get(previousSong).getChildren()) {
                    if (node instanceof Label) {
                        ((Label) node).setTextFill(Color.web("#FFFFFF"));
                    }
                }
                for (Node node : anchors.get(nextSong).getChildren()) {
                    if (node instanceof Label) {
                        ((Label) node).setTextFill(Color.web("#00ead0"));
                        z = 1;
                    }
                }*/
//                if(z==1){
//                    selectedSongID=finalI;
//                    //currentSong = initializeSong(DB);
//                    if (musicController.getMP().getMediaPlayerStatus() == MediaPlayer.Status.PAUSED&&currentSongID!=selectedSongID) {
//                        currentSongID=selectedSongID;
//                        currentSong = initializeSong(DB);
//                    }
//                    else if(musicController.getMP().getMediaPlayerStatus() == MediaPlayer.Status.PLAYING&&currentSongID!=selectedSongID){
//                        musicController.stop();
//                        currentSong = initializeSong(DB);
//                        currentSongID=selectedSongID;
//                        musicController.play();
//                        initializeSongInfo();
//
//                    }
//
//                    UserWithSongService userWithSongService = new UserWithSongService(DB);
//
//                    for(int h=0;h<userWithSongService.getAll(user.getId()).size();h++) {
//                        System.out.println(userWithSongService.getAll(user.getId()).get(h).getSongID());
//                        System.out.println(selectedSongID);
//                        if (userWithSongService.getAll(user.getId()).get(h).getSongID()==selectedSongID) {
//                            c=1;
//                        }
//                    }
//                    if(c==0){
//                        UserWithSong userWithSong = new UserWithSong();
//                        userWithSong.setUserwithsongID(userWithSongService.getComplete().size()+1);
//                        userWithSong.setSongID(selectedSongID+1);
//                        userWithSong.setUserID(user.getId());
//                        userWithSong.setPlaycount(1);
//                        userWithSongService.add(userWithSong);
//                    }
//                }
//                editSong(songData,finalI,songStack, rectangles, anchors);
            });

            anchors.get(i).setOnMouseEntered(event -> {
                rectangles.get(finalI).setFill(Color.web("#323232"));
                kebab.setDisable(false);
                kebab.setOpacity(1.0);
            });
            anchors.get(i).setOnMouseExited(event -> {
                rectangles.get(finalI).setFill(Color.web("#202020"));
                kebab.setDisable(true);
                kebab.setOpacity(0.0);
            });
            songStack.get(i).getChildren().addAll(anchors.get(i));
            songList.getChildren().add(songStack.get(i));
        }
    }

    public void setPlaylistView(ObservableList<PlaylistInterface> playlists) {
        ArrayList<StackPane> playlistStack = new ArrayList<>();
        ArrayList<Rectangle> boxes = new ArrayList<>();
        ArrayList<AnchorPane> anchorPane = new ArrayList<>();

        int numPlaylist = playlists.size();

        playList.getChildren().clear();
        playlistStack.clear();
        boxes.clear();
        anchorPane.clear();

        for (int i = 0; i < numPlaylist; i++) {
            PlaylistInterface pp = playlists.get(i);
            playlistStack.add(new StackPane());
            boxes.add(new Rectangle());
            boxes.get(i).setWidth(200);
            boxes.get(i).setHeight(50);
            boxes.get(i).setLayoutX(0);
            boxes.get(i).setLayoutY(0);
            boxes.get(i).setFill(Color.web("#202020"));
            playlistStack.get(i).getChildren().add(boxes.get(i));
            anchorPane.add(new AnchorPane());
            Label title = new Label(pp.getName());
            title.setMaxWidth(175);
            title.setTextFill(Color.web("#FFFFFF"));
            title.setFont(new Font("Brown", 20));
            title.setLayoutX(15);
            title.setLayoutY(10);
            anchorPane.get(i).getChildren().addAll(title);
            int finalX = i;
            anchorPane.get(i).setOnMouseClicked(event -> {
                previousPlaylist = nextPlaylist;
                nextPlaylist = finalX;
                for (Node node : anchorPane.get(previousPlaylist).getChildren()) {
                    if (node instanceof Label) {
                        ((Label) node).setTextFill(Color.web("#FFFFFF"));
                        //songList.getChildren().clear();
                    }
                }
                for (Node node : anchorPane.get(nextPlaylist).getChildren()) {
                    if (node instanceof Label) {
                        ((Label) node).setTextFill(Color.web("#00ead0"));
                        isProfileOpened = false;
                        songsPane.setVisible(true);
                        sortPlaylistPane.setVisible(false);
                        profile_pane.setVisible(false);
                        isSongsSelected = false;
                        setSongView(pp.getSongs());
                        songViewTitle.setText(pp.getName());
                        songViewCreator.setText("created by: " + model.getUser().getUsername());
                        followBtn.setVisible(true);
                        playListMore.setVisible(true);
                        playListBtn.setVisible(true);

//                        if(((Label) node).getText().compareToIgnoreCase("Songs")==0) {
//                            setSongsView(songService.getAll(), songStack, rectangles, anchors);
//                            selectedPlaylistID=finalX;
//                            queuePlaylist=songService.getAll();
//                            musicController.initializePreviousBTN(previousBtn, isShuffling, queuePlaylist, currentSongID, this);
//                            musicController.initializeForwardBTN(forwardBtn, isShuffling, queuePlaylist, currentSongID, this);
//                        }else {
//                            setSongsView(songInPlaylistService.getSongsInPlaylist(playlistData.get(nextPlaylist).getPlaylistID()), songStack, rectangles, anchors);
//                            selectedPlaylistID=finalX;
//                            System.out.println("QUEUE PLAYLIST");
//                            queuePlaylist=songInPlaylistService.getSongsInPlaylist(playlistData.get(nextPlaylist).getPlaylistID());
//                            musicController.initializePreviousBTN(previousBtn, isShuffling, queuePlaylist, currentSongID, this);
//                            musicController.initializeForwardBTN(forwardBtn, isShuffling, queuePlaylist, currentSongID, this);
//                        }
                    }
                }
            });

            anchorPane.get(i).setOnMouseEntered(event -> {
                boxes.get(finalX).setFill(Color.web("#323232"));
            });
            anchorPane.get(i).setOnMouseExited(event -> {
                boxes.get(finalX).setFill(Color.web("#202020"));
            });
            playlistStack.get(i).getChildren().addAll(anchorPane.get(i));
            playList.getChildren().add(playlistStack.get(i));
        }
    }

    public void setSortPlaylistView(String label) {
        ArrayList<StackPane> playlistStack = new ArrayList<>();
        ArrayList<Rectangle> boxes = new ArrayList<>();
        ArrayList<AnchorPane> anchorPane = new ArrayList<>();

        sortPlaylistLbl.setText(label);
        ObservableList<PlaylistInterface> sortedPlaylist = FXCollections.observableArrayList();

        try {
            if(label.equalsIgnoreCase("artists")) {
                sortedPlaylist = model.getArtistPlaylist();
            } else if(label.equalsIgnoreCase("albums")) {
                sortedPlaylist = model.getAlbumPlaylist();
            } else if(label.equalsIgnoreCase("genres")) {
                sortedPlaylist = model.getGenrePlaylist();
            } else if(label.equalsIgnoreCase("years")) {
                sortedPlaylist = model.getYearPlaylist();
            }
        } catch (SQLException sq) {}

        int numPlaylist = sortedPlaylist.size();

        sortPlaylistScroll.getChildren().clear();
        playlistStack.clear();
        boxes.clear();
        anchorPane.clear();

        for (int i = 0; i < numPlaylist; i++) {
            PlaylistInterface pp = sortedPlaylist.get(i);
            playlistStack.add(new StackPane());
            boxes.add(new Rectangle());
            boxes.get(i).setWidth(1000);
            boxes.get(i).setHeight(50);
            boxes.get(i).setLayoutX(0);
            boxes.get(i).setLayoutY(0);
            boxes.get(i).setFill(Color.web("#202020"));
            playlistStack.get(i).getChildren().add(boxes.get(i));
            anchorPane.add(new AnchorPane());
            Label title = new Label(pp.getName());
            title.setMaxWidth(1000);
            title.setTextFill(Color.web("#FFFFFF"));
            title.setFont(new Font("Brown", 20));
            title.setLayoutX(15);
            title.setLayoutY(10);
            anchorPane.get(i).getChildren().addAll(title);
            int finalX = i;
            anchorPane.get(i).setOnMouseClicked(event -> {
                previousPlaylist = nextPlaylist;
                nextPlaylist = finalX;
                for (Node node : anchorPane.get(previousPlaylist).getChildren()) {
                    if (node instanceof Label) {
                        ((Label) node).setTextFill(Color.web("#FFFFFF"));
                        //songList.getChildren().clear();
                    }
                }
                for (Node node : anchorPane.get(nextPlaylist).getChildren()) {
                    if (node instanceof Label) {
                        ((Label) node).setTextFill(Color.web("#00ead0"));
                        if(label.equalsIgnoreCase("artists")) {
                            isArtistsSelected = false;
                        } else if(label.equalsIgnoreCase("albums")) {
                            isAlbumsSelected = false;
                        } else if(label.equalsIgnoreCase("genres")) {
                            isGenresSelected = false;
                        } else if(label.equalsIgnoreCase("years")) {
                            isYearsSelected = false;
                        }
                        songsPane.setVisible(true);
                        sortPlaylistPane.setVisible(false);
                        setSongView(pp.getSongs());
                        songViewTitle.setText(pp.getName());
                        songViewCreator.setText("created by: " + pp.getUser());
                        followBtn.setVisible(true);
                        playListMore.setVisible(true);
                        playListBtn.setVisible(true);
                    }
                }
            });

            anchorPane.get(i).setOnMouseEntered(event -> {
                boxes.get(finalX).setFill(Color.web("#323232"));
            });
            anchorPane.get(i).setOnMouseExited(event -> {
                boxes.get(finalX).setFill(Color.web("#202020"));
            });
            playlistStack.get(i).getChildren().addAll(anchorPane.get(i));
            sortPlaylistScroll.getChildren().add(playlistStack.get(i));
        }
    }

    public void setSearchView(String label, VBox playList) {
        ArrayList<StackPane> playlistStack = new ArrayList<>();
        ArrayList<Rectangle> boxes = new ArrayList<>();
        ArrayList<AnchorPane> anchorPane = new ArrayList<>();

        songsPane.setVisible(false);
        profile_pane.setVisible(false);
        welcomePane.setVisible(false);
        isProfileOpened = false;
        isSongsSelected = false;
        isArtistsSelected = false;
        isAlbumsSelected = false;
        isGenresSelected = false;
        isYearsSelected = false;
        isRecentsSelected = false;
        isMostSelected = false;

        int numPlaylist = 0;
        SongInterface ss = null;
        AccountInterface ac = null;
        AlbumInterface aa = null;
        PlaylistInterface pp = null;

        if(label.equalsIgnoreCase("song")) {
            songSearchList = model.searchSong(searchBar.getText().trim());
            numPlaylist = songSearchList.size();
        } else if(label.equalsIgnoreCase("artist")) {
            artistSearchList = model.searchArtist(searchBar.getText().trim());
            numPlaylist = artistSearchList.size();
        } else if(label.equalsIgnoreCase("album")) {
            albumSearchList = model.searchAlbum(searchBar.getText().trim());
            numPlaylist = albumSearchList.size();
        } else if(label.equalsIgnoreCase("playlist")) {
            playlistSearchList = model.searchPlaylist(searchBar.getText().trim());
            numPlaylist = playlistSearchList.size();
        } else if(label.equalsIgnoreCase("profile")) {
            //NOT YET IMPLEMENTED
        }

        playList.getChildren().clear();
        playlistStack.clear();
        boxes.clear();
        anchorPane.clear();

        for (int i = 0; i < numPlaylist; i++) {
            if(label.equalsIgnoreCase("song")) {
                ss = songSearchList.get(i);
            } else if(label.equalsIgnoreCase("artist")) {
                ac = artistSearchList.get(i);
            } else if(label.equalsIgnoreCase("album")) {
                aa = albumSearchList.get(i);
            } else if(label.equalsIgnoreCase("playlist")) {
                pp = playlistSearchList.get(i);
            } else if(label.equalsIgnoreCase("profile")) {
                //NOT YET IMPLEMENTED
            }

            playlistStack.add(new StackPane());
            boxes.add(new Rectangle());
            boxes.get(i).setWidth(195);
            boxes.get(i).setHeight(50);
            boxes.get(i).setLayoutX(0);
            boxes.get(i).setLayoutY(0);
            boxes.get(i).setFill(Color.web("#202020"));
            playlistStack.get(i).getChildren().add(boxes.get(i));
            anchorPane.add(new AnchorPane());
            Label title = new Label();
            if(label.equalsIgnoreCase("song")) {
                title.setText(ss.getName().trim());
            } else if(label.equalsIgnoreCase("artist")) {
                title.setText(ac.getName().trim());
            } else if(label.equalsIgnoreCase("album")) {
                title.setText(aa.getAlbumname().trim());
            } else if(label.equalsIgnoreCase("playlist")) {
                title.setText(pp.getName().trim());
            } else if(label.equalsIgnoreCase("profile")) {
                //NOT YET IMPLEMENTED
                title.setText("Profile");
            }
            title.setMaxWidth(195);
            title.setTextFill(Color.web("#FFFFFF"));
            title.setFont(new Font("Brown", 20));
            title.setLayoutX(15);
            title.setLayoutY(10);
            anchorPane.get(i).getChildren().addAll(title);
            int finalX = i;

            if (label.equalsIgnoreCase("song")) {
                System.out.println("KEBAB");
                ImageView kebab = new ImageView(new Image(getClass().getResourceAsStream("/Media/kebab.png")));
                kebab.setFitWidth(25);
                kebab.setFitHeight(7);
                kebab.setLayoutX(150);
                kebab.setLayoutY(25);
                kebab.setVisible(false);
                kebab.setOnMouseEntered(event -> {
                    kebab.setImage(new Image(getClass().getResourceAsStream("/Media/kebab_hover.png")));
                });
                kebab.setOnMouseExited(new EventHandler<>() {

                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        kebab.setImage(new Image(getClass().getResourceAsStream("/Media/kebab.png")));
                    }
                });
                int finalI1 = i;
                kebab.setOnMouseClicked(mouseEvent -> {
                    if (!isKebabOpened) {
                        System.out.println("OPEN");
                        song_etc_anchorpane.setVisible(true);
                        song_etc_anchorpane.setLayoutX(kebab.getLayoutX());
                        song_etc_anchorpane.setLayoutY((mouseEvent.getSceneY()));
                        song_etc_anchorpane.setDisable(false);
                        song_etc_anchorpane.setOpacity(1);
                        remove_from_playlist_btn.setTextFill(Color.web("#7b7b7b"));
                        remove_from_playlist_btn.setDisable(true);
                        isaddtoplaylistOpened = false;
                        isKebabOpened = true;
                    } else if (isKebabOpened) {
                        System.out.println("CLOSE");
                        song_etc_anchorpane.setVisible(false);
                        isKebabOpened = false;
                    }
                });
                anchorPane.get(i).getChildren().add(kebab);

                anchorPane.get(i).setOnMouseEntered(event -> {
                    boxes.get(finalX).setFill(Color.web("#323232"));
                    kebab.setVisible(true);
                });
                anchorPane.get(i).setOnMouseExited(event -> {
                    boxes.get(finalX).setFill(Color.web("#202020"));
                    kebab.setVisible(false);
                });
            }
            anchorPane.get(i).setOnMouseClicked(event -> {
                previousPlaylist = nextPlaylist;
                nextPlaylist = finalX;
                for (Node node : anchorPane.get(previousPlaylist).getChildren()) {
                    if (node instanceof Label) {
                        ((Label) node).setTextFill(Color.web("#FFFFFF"));
                        //songList.getChildren().clear();
                    }
                }
                for (Node node : anchorPane.get(nextPlaylist).getChildren()) {
                    if (node instanceof Label) {
                        ((Label) node).setTextFill(Color.web("#00ead0"));
                        //searchPane.setVisible(true);
                        if (((Label) node).getText().equalsIgnoreCase("artist")) {
                            showArtistProfilePane(((Label) node).getText());
                        } else if (((Label) node).getText().equalsIgnoreCase("profile")) {
                            showListenerProfilePane(((Label) node).getText());
                        } else if (((Label) node).getText().equalsIgnoreCase("album")) {
//                            setSongView();
                            songsPane.setVisible(true);
                        } else if (((Label) node).getText().equalsIgnoreCase("playlist")) {
//                            setSongView();
                            songsPane.setVisible(true);
                        }
                    }
                }
            });

            if (!label.equalsIgnoreCase("song")) {
                anchorPane.get(i).setOnMouseEntered(event -> {
                    boxes.get(finalX).setFill(Color.web("#323232"));
                });
                anchorPane.get(i).setOnMouseExited(event -> {
                    boxes.get(finalX).setFill(Color.web("#202020"));
                });
            }
            playlistStack.get(i).getChildren().addAll(anchorPane.get(i));
            playList.getChildren().add(playlistStack.get(i));

        }
    }

    public void showProfilePane(String username) {
        isProfileOpened = true;
        isSongsSelected = false;
        isArtistsSelected = false;
        isAlbumsSelected = false;
        isGenresSelected = false;
        isYearsSelected = false;
        isRecentsSelected = false;
        isMostSelected = true;
        profile_pane.setVisible(true);
        if(model.getUser() instanceof Artist) {
            user_label.setText("artist");
        } else if (model.getUser() instanceof Listener) {
            user_label.setText("listener");
        }
        listenerusername.setText(username);
        listenerunfollowBtn.setVisible(false);
        listenerfollowBtn.setVisible(false);
        logoutBtn.setVisible(true);
        isCreateOpened = false;

        songsPane.setVisible(false);
        createPane.setVisible(false);
        searchPane.setVisible(false);
        welcomePane.setVisible(false);
        sortPlaylistPane.setVisible(false);
        artist_profile_pane.setVisible(false);

        mostLbl.setTextFill(Color.web("#AAAAAA"));
        mostBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        songsLbl.setTextFill(Color.web("#AAAAAA"));
        songsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        artistsLbl.setTextFill(Color.web("#AAAAAA"));
        artistsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        albumsLbl.setTextFill(Color.web("#AAAAAA"));
        albumsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        genresLbl.setTextFill(Color.web("#AAAAAA"));
        genresBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        yearsLbl.setTextFill(Color.web("#AAAAAA"));
        yearsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        recentsLbl.setTextFill(Color.web("#AAAAAA"));
        recentsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
    }

    public void showListenerProfilePane(String username /*, follower or not*/) {
        isProfileOpened = false;
        isSongsSelected = false;
        isArtistsSelected = false;
        isAlbumsSelected = false;
        isGenresSelected = false;
        isYearsSelected = false;
        isRecentsSelected = false;
        isMostSelected = false;
        profile_pane.setVisible(true);
        listenerusername.setText(username);
        logoutBtn.setVisible(false);

        //if (following, show unfollow button) else
        if(controller.isInFollowing(username)) {
            listenerunfollowBtn.setVisible(true);
            listenerfollowBtn.setVisible(false);
        } else {
            listenerunfollowBtn.setVisible(false);
            listenerfollowBtn.setVisible(true);
        }
        user_label.setText("listener");
        isCreateOpened = false;

        songsPane.setVisible(false);
        createPane.setVisible(false);
        searchPane.setVisible(false);
        welcomePane.setVisible(false);
        sortPlaylistPane.setVisible(false);
        artist_profile_pane.setVisible(false);

        mostLbl.setTextFill(Color.web("#AAAAAA"));
        mostBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        songsLbl.setTextFill(Color.web("#AAAAAA"));
        songsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        artistsLbl.setTextFill(Color.web("#AAAAAA"));
        artistsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        albumsLbl.setTextFill(Color.web("#AAAAAA"));
        albumsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        genresLbl.setTextFill(Color.web("#AAAAAA"));
        genresBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        yearsLbl.setTextFill(Color.web("#AAAAAA"));
        yearsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        recentsLbl.setTextFill(Color.web("#AAAAAA"));
        recentsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");

    }

    public void showArtistProfilePane(String artistusername) {
        artist_profile_pane.setVisible(true);
        user_label.setText("artist");
        //if follow= true, show artistunfollowBtn; else
        if(controller.isInFollowing(artistusername)) {
            artistunfollowBtn.setVisible(true);
            artistfollowBtn.setVisible(false);
        } else {
            artistunfollowBtn.setVisible(false);
            artistfollowBtn.setVisible(true);
        }
        artist_username.setText(artistusername);

        isProfileOpened = true;
        isSongsSelected = false;
        isArtistsSelected = false;
        isAlbumsSelected = false;
        isGenresSelected = false;
        isYearsSelected = false;
        isRecentsSelected = false;
        isMostSelected = true;
        searchPane.setVisible(false);
        isCreateOpened = false;

        songsPane.setVisible(false);
        createPane.setVisible(false);
        searchPane.setVisible(false);
        welcomePane.setVisible(false);
        sortPlaylistPane.setVisible(false);
        profile_pane.setVisible(false);

        mostLbl.setTextFill(Color.web("#AAAAAA"));
        mostBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        songsLbl.setTextFill(Color.web("#AAAAAA"));
        songsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        artistsLbl.setTextFill(Color.web("#AAAAAA"));
        artistsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        albumsLbl.setTextFill(Color.web("#AAAAAA"));
        albumsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        genresLbl.setTextFill(Color.web("#AAAAAA"));
        genresBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        yearsLbl.setTextFill(Color.web("#AAAAAA"));
        yearsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");
        recentsLbl.setTextFill(Color.web("#AAAAAA"));
        recentsBtn.setStyle("-fx-border-width: 0 0 0 0; -fx-border-color: #00ead0; -fx-background-color: transparent");

    }

    public void updateAlbumList() {
        albumChoice.getItems().clear();
        if(model.getUserAlbum(model.getUser().getUsername()) != null) {
            ObservableList<String> names = FXCollections.observableArrayList();
            for(AlbumInterface al : model.getUserAlbum(model.getUser().getUsername())) {
                names.add(al.getAlbumname());
            }
            albumChoice.setItems(names);
        }
    }

    @Override
    public void update() {
        Platform.runLater(() -> {
            updateAlbumList();
            try {
                setPlaylistView(model.getUserPlaylist());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
