/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.FacadeController;
import Controller.MusicPlayerController;
import Model.FacadeModel;
import Model.Playlist;
import Model.PlaylistInterface;
import Model.SongInterface;
import Mp3agic.InvalidDataException;
import Mp3agic.NotSupportedException;
import Mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;

/**
 *
 * @author Stanley Sie
 */
public class DashboardView extends View {

    @FXML
    private Button play, close, next, prev, repeat, shuffle, minim, search, profile, list, speaker, login, signup, upload;
    @FXML
    private Button tracks, albums, artists, genres, years, favMusic, mostPlayed, playlists, createPlaylist, playPlaylist;
    @FXML
    private Slider musicSlider, speakerSlider;
    @FXML
    private VBox playlistVBox;
    @FXML
    private AnchorPane profilePane, playlistPane, playlistViewPane;
    @FXML
    private ImageView cover, lookPlaylist;
    @FXML
    private Label greetings, or, start, end, songName, artistName, listLabel;
    @FXML
    private TableView table, table1;
    @FXML
    private TextField searchField;

    private Stage stage;
    private Scene scene;
    private FXMLLoader loader;
    private Parent root;

    private FacadeModel model;
    private FacadeController controller;
    private MusicPlayerController musicPlayer;
    private boolean showProfile;

    private ObservableList<Label> listOfPlaylist;
    private FilteredList<SongInterface> filteredData;

    public DashboardView(FacadeModel model, Stage stage) {
        this.stage = stage;
        this.model = model;
        this.controller = new FacadeController(this.model, this);
        this.model.attach(this);
        listOfPlaylist = FXCollections.observableArrayList();
        musicPlayer = new MusicPlayerController(model, this);

//        try {
//            if (model.getUser() == null && model.getAllSong() != null) {
//                filteredData = new FilteredList<>(songView.getSongList(), p -> true);
//            } else if (model.getUser() != null && model.getUserSongs() != null) {
//                filteredData = new FilteredList<>(songView.getSongList(), p -> true);
//            }
//        } catch (SQLException ex) {
//        }
        try {
            loader = new FXMLLoader(getClass().getResource("/View/Dashboard.fxml"));
            loader.setController(this);
            root = (Parent) loader.load();
            scene = new Scene(root);

            this.stage.setTitle("Dashboard");
            this.stage.setScene(scene);
            this.stage.centerOnScreen();
            this.stage.setResizable(false);
            this.stage.initStyle(StageStyle.UNDECORATED);
            this.stage.show();
        } catch (IOException ie) {
        }

        init();
    }

    public void initialize() {
        albums.setOnAction(event -> {
            controller.getPlaylistView().groupedByAlbum();
            playlistViewPane.setVisible(true);
            playlistViewPane.setDisable(false);
        });
        artists.setOnAction(event -> {
            controller.getPlaylistView().groupedByArtist();
            playlistViewPane.setVisible(true);
            playlistViewPane.setDisable(false);
        });
        genres.setOnAction(event -> {
            controller.getPlaylistView().groupedByGenre();
            playlistViewPane.setVisible(true);
            playlistViewPane.setDisable(false);
        });
        years.setOnAction(event -> {
            controller.getPlaylistView().groupedByYear();
            playlistViewPane.setVisible(true);
            playlistViewPane.setDisable(false);
        });
        favMusic.setOnAction(event -> {
            controller.getSongView().groupedByFavorite();
            playlistViewPane.setVisible(false);
            playlistViewPane.setDisable(true);
        });
        mostPlayed.setOnAction(event -> {
            controller.getSongView().groupedByMostPlayed();
            playlistViewPane.setVisible(false);
            playlistViewPane.setDisable(true);
        });
        upload.setOnAction(event -> {
            controller.getSongView().uploadSong();
        });
        playlists.setOnMouseEntered(event -> {
            lookPlaylist.setVisible(true);
        });
        playlists.setOnMouseExited(event -> {
            lookPlaylist.setVisible(false);
        });
        playlists.setOnAction(event -> {
            if (playlistPane.isVisible()) {
                playlistPane.setVisible(false);
                playlistPane.setDisable(true);
            } else {
                playlistPane.setVisible(true);
                playlistPane.setDisable(false);
            }
        });
        createPlaylist.setOnAction(event -> {
            controller.createPlaylist();
        });
        playPlaylist.setOnAction(event -> {
            if (model.getUser() == null) {
                if (musicPlayer.getCurrentSong() != null) {
                    musicPlayer.stopMusic();
                }
                PlaylistInterface p = new Playlist();
                if (listLabel.getText().equalsIgnoreCase("songs")) {
                    for (SongInterface s : model.getSongs()) {
                        p.getSongs().add(s);
                    }
                } else if (listLabel.getText().equalsIgnoreCase("queue")) {
                    for (SongInterface s : model.getCurrentPlaylist().getSongs()) {
                        p.getSongs().add(s);
                    }
                } else {
                    for (PlaylistInterface pp : model.getGroups()) {
                        if (pp.getName().equalsIgnoreCase(listLabel.getText())) {
                            for (SongInterface ss : pp.getSongs()) {
                                p.getSongs().add(ss);
                            }
                            break;
                        }
                    }
                }
                enablePlayer();
                startList();
                endList();
                model.setCurrentPlaylist(p);
                model.setCurrentSong(model.getCurrentPlaylist().getSongs().get(0));
                musicPlayer.setCurrentIndex(0);
                musicPlayer.setCurrentSong(musicPlayer.getSong());
                try {
                    cover.setImage(new Image(model.getsongImage(model.getCurrentSong()).toURI().toURL().toString()));
                } catch (InvalidDataException ex) {
                    Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedTagException ex) {
                    Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotSupportedException ex) {
                    Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
                }
                play.setStyle("-fx-background-image: url('/Files/pause.png');");
                musicPlayer.playMusic();
            }
        });
        play.setOnMouseEntered(event -> {
            play.setPrefSize(42, 42);
            play.setLayoutX(574);
        });
        play.setOnMouseExited(event -> {
            play.setPrefSize(40, 40);
            play.setLayoutX(575);
        });
        play.setOnAction(event -> {
            if (musicPlayer.isPause()) {
                play.setStyle("-fx-background-image: url('/Files/pause.png');");
            } else {
                play.setStyle("-fx-background-image: url('/Files/play.png');");
            }
            musicPlayer.playMusic();
        });
        next.setOnAction(event -> {
            musicPlayer.nextMusic();
            try {
                cover.setImage(new Image(model.getsongImage(model.getCurrentSong()).toURI().toURL().toString()));
            } catch (InvalidDataException ex) {
                Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedTagException ex) {
                Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotSupportedException ex) {
                Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        prev.setOnAction(event -> {
            musicPlayer.prevMusic();
            try {
                cover.setImage(new Image(model.getsongImage(model.getCurrentSong()).toURI().toURL().toString()));
            } catch (InvalidDataException ex) {
                Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedTagException ex) {
                Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotSupportedException ex) {
                Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        repeat.setOnMouseEntered(event -> {
            if (musicPlayer.getRepeat() == 0) {
                repeat.setStyle("-fx-background-image: url('/Files/repeat_hover.png');");
            } else if (musicPlayer.getRepeat() == 1) {
                repeat.setStyle("-fx-background-image: url('/Files/repeat_hover.png');");
            } else if (musicPlayer.getRepeat() == 2) {
                repeat.setStyle("-fx-background-image: url('/Files/repeat_one_clicked_hover.png');");
            }

        });
        repeat.setOnMouseExited(event -> {
            if (musicPlayer.getRepeat() == 0) {
                repeat.setStyle("-fx-background-image: url('/Files/repeat.png');");
            } else if (musicPlayer.getRepeat() == 1) {
                repeat.setStyle("-fx-background-image: url('/Files/repeat_clicked.png');");
            } else if (musicPlayer.getRepeat() == 2) {
                repeat.setStyle("-fx-background-image: url('/Files/repeat_one_clicked.png');");
            }
        });
        repeat.setOnAction(event -> {
            if (musicPlayer.getRepeat() == 0) {
                musicPlayer.setRepeat(1);
                next.setDisable(false);
                prev.setDisable(false);
                repeat.setStyle("-fx-background-image: url('/Files/repeat_clicked.png');");
            } else if (musicPlayer.getRepeat() == 1) {
                musicPlayer.setRepeat(2);
                next.setDisable(false);
                prev.setDisable(false);
                repeat.setStyle("-fx-background-image: url('/Files/repeat_one_clicked.png');");
            } else if (musicPlayer.getRepeat() == 2) {
                musicPlayer.setRepeat(0);
                next.setDisable(true);
                prev.setDisable(true);
                repeat.setStyle("-fx-background-image: url('/Files/repeat.png');");
            }
        });
        shuffle.setOnMouseEntered(event -> {
            shuffle.setStyle("-fx-background-image: url('/Files/shuffle_hover.png');");
        });
        shuffle.setOnMouseExited(event -> {
            if (musicPlayer.isShuffle()) {
                shuffle.setStyle("-fx-background-image: url('/Files/shuffle_clicked.png');");
            } else {
                shuffle.setStyle("-fx-background-image: url('/Files/shuffle.png');");
            }
        });
        shuffle.setOnAction(event -> {
            if (musicPlayer.isShuffle()) {
                musicPlayer.setShuffle(false);
                shuffle.setStyle("-fx-background-image: url('/Files/shuffle.png');");
            } else {
                musicPlayer.setShuffle(true);
                shuffle.setStyle("-fx-background-image: url('/Files/shuffle_clicked.png');");
            }
        });
        speaker.setOnMouseEntered(event -> {
            if (musicPlayer.isMute()) {
                speaker.setStyle("-fx-background-image: url('/Files/mute_hover.png');");
            } else {
                speaker.setStyle("-fx-background-image: url('/Files/volume_hover.png');");
            }
        });
        speaker.setOnMouseExited(event -> {
            if (musicPlayer.isMute()) {
                speaker.setStyle("-fx-background-image: url('/Files/mute.png');");
            } else {
                speaker.setStyle("-fx-background-image: url('/Files/volume.png');");
            }
        });
        speaker.setOnAction(event -> {
            if (musicPlayer.isMute()) {
                musicPlayer.setMute(false);
                speaker.setStyle("-fx-background-image: url('/Files/volume.png');");
            } else {
                musicPlayer.setMute(true);
                speaker.setStyle("-fx-background-image: url('/Files/mute.png');");
            }
        });
        list.setOnAction(event -> {
            controller.getSongView().showQueue();
            playlistViewPane.setVisible(false);
            playlistViewPane.setDisable(true);
            upload.setDisable(true);
            upload.setVisible(false);
        });
        login.setOnMouseEntered(event -> {
            if (model.getUser() != null) {
                login.setStyle("-fx-background-image: url('/Files/viewprofile_button_clicked.png')");
            } else {
                login.setStyle("-fx-background-image: url('/Files/login_button_clicked.png');");
            }
        });
        login.setOnMouseExited(event -> {
            if (model.getUser() != null) {
                login.setStyle("-fx-background-image: url('/Files/viewprofile_button.png')");
            } else {
                login.setStyle("-fx-background-image: url('/Files/login_button.png');");
            }
        });
        login.setOnAction(event -> {
            if (model.getUser() == null) {
                controller.login();
            } else {
                controller.viewProfile();
            }
        });
        signup.setOnMouseEntered(event -> {
            if (model.getUser() != null) {
                signup.setStyle("-fx-background-image: url('/Files/logout_button_clicked.png')");
            } else {
                signup.setStyle("-fx-background-image: url('/Files/signup_button_red-black_clicked.png');");
            }
        });
        signup.setOnMouseExited(event -> {
            if (model.getUser() != null) {
                signup.setStyle("-fx-background-image: url('/Files/logout_button.png')");
            } else {
                signup.setStyle("-fx-background-image: url('/Files/signup_button_red-black.png');");
            }
        });
        signup.setOnAction(event -> {
            if (model.getUser() != null) {
                model.logout();
            } else {
                controller.signin();
            }
        });
        close.setOnMouseEntered(event -> {
            close.setPrefSize(25, 25);
        });
        close.setOnMouseExited(event -> {
            close.setPrefSize(23, 23);
        });
        search.setOnMouseEntered(event -> {
            search.setPrefSize(32, 32);
        });
        search.setOnMouseExited(event -> {
            search.setPrefSize(30, 30);
        });
        minim.setOnMouseEntered(event -> {
            minim.setPrefSize(25, 25);
        });
        minim.setOnMouseExited(event -> {
            minim.setPrefSize(23, 23);
        });
        profile.setOnMouseEntered(event -> {
            profile.setPrefSize(42, 42);
            profile.setLayoutX(1091);
        });
        profile.setOnMouseExited(event -> {
            profile.setPrefSize(40, 40);
            profile.setLayoutX(1092);
        });
        profile.setOnAction(event -> {
            if (showProfile) {
                profilePane.setVisible(false);
                profilePane.setDisable(true);
                showProfile = false;
            } else {
                profilePane.setVisible(true);
                profilePane.setDisable(false);
                showProfile = true;
            }
        });
        minim.setOnAction(event -> {
            stage.setIconified(true);
        });
        close.setOnAction(event -> {
            System.exit(0);
            stage.close();
        });
        table1.setOnMouseClicked(event -> {
            model.setSelectedPlaylist((PlaylistInterface) table1.getSelectionModel().getSelectedItem());
            controller.getSongView().showPlaylist();
            playlistViewPane.setVisible(false);
            playlistViewPane.setDisable(true);
        });
        table.setRowFactory(table -> {
            TableRow<SongInterface> row = new TableRow<>();
            row.hoverProperty().addListener((Observable observable) -> {
                SongInterface song = row.getItem();
                if (song != null) {
                    if (row.isHover()) {
                        song.getFavorite().setVisible(true);
                        song.getFavorite().setDisable(false);
                        song.getAdd().setVisible(true);
                        song.getAdd().setDisable(false);
                        song.getDel().setVisible(true);
                        song.getDel().setDisable(false);
                        song.getEdit().setVisible(true);
                        song.getEdit().setDisable(false);
                    } else {
                        song.getFavorite().setVisible(false);
                        song.getFavorite().setDisable(true);
                        song.getAdd().setVisible(false);
                        song.getAdd().setDisable(true);
                        song.getDel().setVisible(false);
                        song.getDel().setDisable(true);
                        song.getEdit().setVisible(false);
                        song.getEdit().setDisable(true);
                    }

                    song.getFavorite().setOnAction(event -> {
                        PlaylistInterface starred = new Playlist();
                        try {
                            starred = model.getStarredSongs();
                        } catch (SQLException ex) { }
                        if(starred.getSongs() == null || !starred.getSongs().contains(song)) {
                            song.getFavorite().setStyle("-fx-background-image: url('/Files/starred_clicked.PNG');");
                            try {
                                model.starSong(song);
                            } catch (SQLException ex) { }
                        } else {
                            song.getFavorite().setStyle("-fx-background-image: url('/Files/starred.PNG');");
                            try {
                                model.deleteStarred(song);
                            } catch (SQLException ex) { }
                        }
                    });

                    song.getAdd().setOnAction(event -> {
                        model.setSelectedSong(song);
                        controller.addSong();
                    });
                    song.getAdd().setOnMouseEntered(event -> {
                        song.getAdd().setPrefSize(32, 32);
                    });
                    song.getAdd().setOnMouseExited(event -> {
                        song.getAdd().setPrefSize(30, 30);
                    });

                    song.getEdit().setOnAction(event -> {
                        model.setSelectedSong(song);
                        controller.editSong();
                    });
                    song.getEdit().setOnMouseEntered(event -> {
                        song.getEdit().setPrefSize(32, 32);
                    });
                    song.getEdit().setOnMouseExited(event -> {
                        song.getEdit().setPrefSize(30, 30);
                    });

                    song.getDel().setOnAction(event -> {
                        model.setSelectedSong(song);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Song");
                        alert.setHeaderText("Are you sure you want to delete " + model.getSelectedSong().getName());
                        Optional<ButtonType> option = alert.showAndWait();
                        if (option.get() == ButtonType.OK) {
                            if (model.getUser() == null) {
                                if (listLabel.getText().equalsIgnoreCase("songs")) {
                                    if (model.getSelectedSong().getName().equalsIgnoreCase(model.getCurrentSong().getName())) {
                                        model.setCurrentSong(null);
                                        musicPlayer.stopMusic();
                                        play.setStyle("-fx-background-image: url('/Files/play.png');");
                                        cover.setImage(new Image(new File("/Files/album_art.png").toURI().toString()));
                                    }
                                    model.removeSongLocally(model.getSelectedSong());
                                } else if (listLabel.getText().equalsIgnoreCase("queue")) {
                                    model.getCurrentPlaylist().getSongs().remove(song);
                                    model.setCurrentSong(null);
                                    musicPlayer.stopMusic();
                                    controller.getSongView().showQueue();
                                    playlistViewPane.setVisible(false);
                                    playlistViewPane.setDisable(true);
                                    update();
                                } else {
                                    if (model.getSelectedSong().getName().equalsIgnoreCase(model.getCurrentSong().getName())) {
                                        model.setCurrentSong(null);
                                        musicPlayer.stopMusic();
                                        play.setStyle("-fx-background-image: url('/Files/play.png');");
                                        cover.setImage(new Image(new File("/Files/album_art.png").toURI().toString()));
                                    }
                                    model.removeSongFromPlaylist(listLabel.getText(), song);
                                }
                            } else {
                                try {
                                    model.deleteSong(model.getSelectedSong().getSongid());
                                } catch (SQLException ex) {
                                }
                            }
                        } else if (option.get() == ButtonType.CANCEL) {
                            event.consume();
                        }
                    });
                    song.getDel().setOnMouseEntered(event -> {
                        song.getDel().setPrefSize(32, 32);
                    });
                    song.getDel().setOnMouseExited(event -> {
                        song.getDel().setPrefSize(30, 30);
                    });
                }
            });
            return row;
        });
        table.setOnMouseClicked(event -> {
            model.setSelectedSong((SongInterface) table.getSelectionModel().getSelectedItem());

            if (event.getClickCount() == 2) {
                model.setCurrentSong((SongInterface) table.getSelectionModel().getSelectedItem());
                if (musicPlayer.getCurrentSong() != null) {
                    musicPlayer.stopMusic();
                }
                PlaylistInterface p = new Playlist();
                if (model.getUser() == null) {
                    for (SongInterface s : model.getSongs()) {
                        p.getSongs().add(s);
                    }
                } else {
                    for (SongInterface s : model.getUser().getSongs()) {
                        p.getSongs().add(s);
                    }
                }

                model.setCurrentPlaylist(p);
                for (SongInterface ss : model.getCurrentPlaylist().getSongs()) {
                    if (ss.getName().equalsIgnoreCase(model.getCurrentSong().getName())) {
                        musicPlayer.setCurrentIndex(model.getCurrentPlaylist().getSongs().indexOf(ss));
                        break;
                    }
                }
                musicPlayer.setCurrentSong(musicPlayer.getSong());
                play.setStyle("-fx-background-image: url('/Files/pause.png');");
                try {
                    cover.setImage(new Image(model.getsongImage(model.getCurrentSong()).toURI().toURL().toString()));
                } catch (InvalidDataException ex) {
                    Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedTagException ex) {
                    Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotSupportedException ex) {
                    Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
                }
                enablePlayer();
                startList();
                endList();
                musicPlayer.playMusic();
            }

            if (playlistPane.isVisible()) {
                playlistPane.setVisible(false);
                playlistPane.setDisable(true);
            }
        });
        tracks.setOnAction(event -> {
            controller.getSongView().showSong();
            playlistViewPane.setVisible(false);
            playlistViewPane.setDisable(true);
            upload.setDisable(false);
            upload.setVisible(true);
        });
//        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (filteredData != null) {
//                filteredData.setPredicate(song -> {
//                    if (newValue == null || newValue.isEmpty()) {
//                        return true;
//                    }
//
//                    String filter = newValue.toLowerCase();
//                    if (song.getName().toLowerCase().contains(filter)) {
//                        return true;
//                    }
//
//                    return false;
//                });
//
//                songView.updateTable(filteredData);
//            }
//        });
        musicSlider.valueProperty().addListener((Observable observable) -> {
            if (musicSlider.isPressed() || musicSlider.isValueChanging()) {
                Duration duration = musicPlayer.getCurrentSong().getMedia().getDuration();
                musicPlayer.getCurrentSong().seek(duration.multiply(musicSlider.getValue() / 100));
            }
        });
        speakerSlider.valueProperty().addListener((Observable observable) -> {
            if (speakerSlider.isPressed() || speakerSlider.isValueChanging()) {
                setVolume((float) speakerSlider.getValue() / 100);
            }
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

    private void init() {
        playPlaylist.setDisable(true);
        playPlaylist.setVisible(false);
        play.setDisable(true);
        next.setDisable(true);
        prev.setDisable(true);
        playlistViewPane.setVisible(false);
        playlistViewPane.setDisable(true);

        speakerSlider.setValue(20);
        setVolume((float) speakerSlider.getValue() / 100);

        controller.init(model, table, listLabel, stage, table1);
        controller.getSongView().showSong();
        playlistViewPane.setVisible(false);
        playlistViewPane.setDisable(true);

        if (LocalDateTime.now().getHour() >= 0 && LocalDateTime.now().getHour() < 12) {
            if (model.getUser() != null) {
                greetings.setText("Good morning, " + model.getUser().getName() + "!");
                or.setVisible(false);
            } else {
                greetings.setText("Good morning, Guest!");
                or.setVisible(true);
            }
        } else if (LocalDateTime.now().getHour() == 12) {
            if (model.getUser() != null) {
                greetings.setText("Good day, " + model.getUser().getName() + "!");
                or.setVisible(false);
            } else {
                greetings.setText("Good day, Guest!");
                or.setVisible(true);
            }
        } else if (LocalDateTime.now().getHour() > 12 && LocalDateTime.now().getHour() < 18) {
            if (model.getUser() != null) {
                greetings.setText("Good afternoon, " + model.getUser().getName() + "!");
                or.setVisible(false);
            } else {
                greetings.setText("Good afternoon, Guest!");
                or.setVisible(true);
            }
        } else if (LocalDateTime.now().getHour() >= 18 && LocalDateTime.now().getHour() <= 23) {
            if (model.getUser() != null) {
                greetings.setText("Good evening, " + model.getUser().getName() + "!");
                or.setVisible(false);
            } else {
                greetings.setText("Good evening, Guest!");
                or.setVisible(true);
            }
        }

        profilePane.setVisible(false);
        profilePane.setDisable(true);
        playlistPane.setVisible(false);
        playlistPane.setDisable(true);
        lookPlaylist.setVisible(false);
        showProfile = false;

        playlistVBox.setMaxHeight(Double.MAX_VALUE);
        playlistVBox.setSpacing(20);

        updatePlaylist();
    }

    public void addPlaylist(PlaylistInterface p) {
        p.setName(p.getName());

        Label label = new Label();
        label.setText("     " + p.getName());
        label.setAlignment(Pos.CENTER_LEFT);
        label.setFont(new Font("Segoe UI", 14));
        label.setStyle("-fx-text-fill: gray;");
        label.setPrefWidth(200);

        MenuItem item = new MenuItem("DeletePlaylist");
        item.setOnAction(event -> {
            listOfPlaylist.remove(label);
            updatePlaylist();
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(item);
        menu.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Song");
            alert.setHeaderText("Are you sure you want to delete " + p.getName());
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                if (model.getUser() == null) {
                    model.deletePlaylistLocally(p);
                } else {
                    try {
                        if (model.deletePlaylist(p.getPlaylistid())) {
                            updatePlaylist();
                        }
                    } catch (SQLException ex) {
                    }
                }
            } else if (option.get() == ButtonType.CANCEL) {
                event.consume();
            }
        });
        label.setContextMenu(menu);

        label.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                model.setSelectedPlaylist(p);
                controller.getSongView().showPlaylist();
                playlistViewPane.setVisible(false);
                playlistViewPane.setDisable(true);
                upload.setDisable(true);
                upload.setVisible(false);
                playlistPane.setVisible(false);
                playlistPane.setDisable(true);
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                menu.fireEvent(event);
            }
        });
        label.setOnMouseEntered(event -> {
            label.setStyle("-fx-text-fill: white;");
        });
        label.setOnMouseExited(event -> {
            label.setStyle("-fx-text-fill: gray;");
        });

        listOfPlaylist.add(label);
    }

    public void updatePlaylist() {
        listOfPlaylist.clear();
        playlistVBox.getChildren().clear();
        if (model.getUser() == null) {
            for (PlaylistInterface p : model.getGroups()) {
                addPlaylist(p);
            }
        } else {
            if (model.getUser().getPlaylists() != null) {
                try {
                    for (PlaylistInterface p : model.getUserPlaylist()) {
                        addPlaylist(p);
                    }
                } catch (SQLException ex) {
                }
            }
        }
        if (listOfPlaylist.isEmpty()) {
            Label label = new Label();
            label.setText("     No playlist created");
            label.setAlignment(Pos.CENTER_LEFT);
            label.setFont(new Font("Segoe UI", 14));
            label.setStyle("-fx-text-fill: gray;");
            label.setPrefWidth(200);
            listOfPlaylist.add(label);
        }
        playlistVBox.getChildren().setAll(listOfPlaylist);
    }

    public void updateSlider() {
        Platform.runLater(() -> {
            start.setText(getDuration((int) musicPlayer.getCurrentSong().getCurrentTime().toSeconds()));
            if (start.getText().equalsIgnoreCase(end.getText())) {
                play.setStyle("-fx-background-image: url('/Pictures/pause.png');");
            }
            musicSlider.setValue(musicPlayer.getCurrentSong().getCurrentTime().divide(musicPlayer.getCurrentSong().getMedia().getDuration()).toMillis() * 100);
        });
    }

    public void resetSlider() {
        start.setText("00:00");
        musicSlider.setValue(0);
    }

    public void startList() {
        if (musicPlayer.getRepeat() > 0) {
            prev.setDisable(false);
        } else {
            if (musicPlayer.getCurrentIndex() == 0) {
                prev.setDisable(true);
            } else {
                prev.setDisable(false);
            }
        }
    }

    public void endList() {
        if (musicPlayer.getRepeat() > 0) {
            next.setDisable(false);
        } else {
            if (musicPlayer.getCurrentIndex() == model.getCurrentPlaylist().getSongs().size() - 1) {
                next.setDisable(true);
            } else {
                next.setDisable(false);
            }
        }
    }

    public void enablePlayer() {
        play.setDisable(false);
    }

    @Override
    public void update() {
        if (model.getSongs().size() > 0) {
            playPlaylist.setDisable(false);
            playPlaylist.setVisible(true);
        }

        startList();
        endList();

        if (model.getUser() != null) {
            musicPlayer.stopMusic();
            play.setStyle("-fx-background-image: url('/Files/play.png');");
            cover.setImage(new Image(new File("/Files/album_art.png").toURI().toString()));
        }

        if (model.getCurrentSong() != null) {
            songName.setText(model.getCurrentSong().getName());
            artistName.setText(model.getCurrentSong().getArtist());
            int x = model.getCurrentSong().getLength();
            end.setText(String.format("%02d", x / 60) + ":" + String.format("%02d", x % 60));
        } else {
            songName.setText("Song Name");
            artistName.setText("Artist Name");
            end.setText("00:00");
        }

        updatePlaylist();

        if (listLabel.getText().equalsIgnoreCase("songs")) {
            controller.getSongView().showSong();
            playlistViewPane.setVisible(false);
            playlistViewPane.setDisable(true);
        } else if (listLabel.getText().equalsIgnoreCase("queue")) {
            controller.getSongView().showQueue();
            playlistViewPane.setVisible(false);
            playlistViewPane.setDisable(true);
        } else {
            controller.getSongView().showPlaylist();
            playlistViewPane.setVisible(false);
            playlistViewPane.setDisable(true);
        }

        if (model.getUser() != null) {
            login.setStyle("-fx-background-image: url('/Files/viewprofile_button.png')");
            signup.setStyle("-fx-background-image: url('/Files/logout_button.png')");
        } else {
            login.setStyle("-fx-background-image: url('/Files/login_button.png');");
            signup.setStyle("-fx-background-image: url('/Files/signup_button_red-black.png');");
            controller.getSongView().showSong();
        }

        if (LocalDateTime.now().getHour() >= 0 && LocalDateTime.now().getHour() < 12) {
            if (model.getUser() != null) {
                greetings.setText("Good morning, " + model.getUser().getName().substring(0, model.getUser().getName().indexOf(" ")) + "!");
                or.setVisible(false);
            } else {
                greetings.setText("Good morning, Guest!");
                or.setVisible(true);
            }
        } else if (LocalDateTime.now().getHour() == 12) {
            if (model.getUser() != null) {
                greetings.setText("Good day, " + model.getUser().getName().substring(0, model.getUser().getName().indexOf(" ")) + "!");
                or.setVisible(false);
            } else {
                greetings.setText("Good day, Guest!");
                or.setVisible(true);
            }
        } else if (LocalDateTime.now().getHour() > 12 && LocalDateTime.now().getHour() < 18) {
            if (model.getUser() != null) {
                greetings.setText("Good afternoon, " + model.getUser().getName().substring(0, model.getUser().getName().indexOf(" ")) + "!");
                or.setVisible(false);
            } else {
                greetings.setText("Good afternoon, Guest!");
                or.setVisible(true);
            }
        } else if (LocalDateTime.now().getHour() >= 18 && LocalDateTime.now().getHour() <= 23) {
            if (model.getUser() != null) {
                greetings.setText("Good evening, " + model.getUser().getName().substring(0, model.getUser().getName().indexOf(" ")) + "!");
                or.setVisible(false);
            } else {
                greetings.setText("Good evening, Guest!");
                or.setVisible(true);
            }
        }
    }

    public String getDuration(int time) {
        return String.format("%02d", time / 60) + ":" + String.format("%02d", time % 60);
    }

}
