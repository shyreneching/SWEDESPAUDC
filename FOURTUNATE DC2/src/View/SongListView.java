/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.FacadeModel;
import Model.PlaylistInterface;
import Model.SongInterface;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Stanley Sie
 */
public class SongListView extends View {

    private FacadeModel model;
    private ObservableList<SongInterface> songList;

    private Label listLabel;
    private TableView table;
    private Stage stage;

    private TableColumn title, album, artist, genre, year, add, del, favorite, edit, duration;

    public SongListView(FacadeModel model, TableView table, Label listLabel, Stage stage) {
        this.model = model;
        this.table = table;
        this.listLabel = listLabel;
        this.stage = stage;
        songList = FXCollections.observableArrayList();
        this.model.attach(this);

        update();
    }

    public ObservableList<SongInterface> getSongList() {
        return songList;
    }

    public void setSongList(ObservableList<SongInterface> songList) {
        this.songList = songList;
        update();
    }

    public void showSong() {
        listLabel.setText("Songs");
        try {
            loadSong();
        } catch (SQLException ex) {
        }
    }

    public void showPlaylist() {
        listLabel.setText(model.getSelectedPlaylist().getName());
        setSongList(model.getSelectedPlaylist().getSongs());
    }

    public void showQueue() {
        listLabel.setText("Queue");
        setSongList(model.getCurrentPlaylist().getSongs());
    }

    public void groupedByMostPlayed() {
        listLabel.setText("Most Played");
        setSongList(model.getMostPlayed());
    }

    public void groupedByFavorite() {
        listLabel.setText("Favorites");
        try {
            if (model.getStarredSongs() != null) {
                setSongList(model.getStarredSongs().getSongs());
            }
        } catch (SQLException ex) {
            Logger.getLogger(SongListView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update() {
        title = new TableColumn("Title");
        title.setCellValueFactory(new PropertyValueFactory("name"));
        title.setPrefWidth(220);
        title.setStyle("-fx-alignment: CENTER_LEFT;-fx-text-fill: white;");
        title.setResizable(false);
        artist = new TableColumn("Artist");
        artist.setCellValueFactory(new PropertyValueFactory("artist"));
        artist.setPrefWidth(160);
        artist.setStyle("-fx-alignment: CENTER_LEFT;-fx-text-fill: white;");
        artist.setResizable(false);
        album = new TableColumn("Album");
        album.setCellValueFactory(new PropertyValueFactory("album"));
        album.setPrefWidth(150);
        album.setStyle("-fx-alignment: CENTER_LEFT;-fx-text-fill: white;");
        album.setResizable(false);
        year = new TableColumn("Year");
        year.setCellValueFactory(new PropertyValueFactory("year"));
        year.setStyle("-fx-alignment: CENTER_LEFT;-fx-text-fill: white;");
        year.setPrefWidth(80);
        year.setResizable(false);
        duration = new TableColumn("Duration");
        duration.setCellValueFactory(new PropertyValueFactory("duration"));
        duration.setPrefWidth(90);
        duration.setStyle("-fx-alignment: CENTER_LEFT;-fx-text-fill: white;");
        duration.setResizable(false);
        favorite = new TableColumn();
        favorite.setCellValueFactory(new PropertyValueFactory("favorite"));
        favorite.setPrefWidth(65);
        favorite.setResizable(false);
        add = new TableColumn();
        add.setCellValueFactory(new PropertyValueFactory("add"));
        add.setPrefWidth(45);
        add.setResizable(false);
        edit = new TableColumn();
        edit.setCellValueFactory(new PropertyValueFactory("edit"));
        edit.setPrefWidth(45);
        edit.setResizable(false);
        del = new TableColumn();
        del.setCellValueFactory(new PropertyValueFactory("del"));
        del.setPrefWidth(45);
        del.setResizable(false);
        table.getColumns().setAll(favorite, title, artist, album, year, duration, add, edit, del);
        table.setItems(songList);
    }

    public void loadSong() throws SQLException {
        if (model.getUser() == null) {
            setSongList(model.getSongs());
        } else {
            setSongList(model.getUserSongs());
        }
    }

    public void updateTable(FilteredList<SongInterface> filteredData) {
        SortedList<SongInterface> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    public void uploadSong() {
        FileChooser file = new FileChooser();
        List<File> list = file.showOpenMultipleDialog(stage);
        if (list != null) {
            if (model.getUser() == null) {
                for (File f : list) {
                    System.out.println(f.toURI().toString().substring(6, f.toURI().toString().length()).replaceAll("%20", " "));
                    model.addSongLocally(f.toURI().toString().substring(6, f.toURI().toString().length()).replaceAll("%20", " "));
                }
            } else {
                for (File f : list) {
                    System.out.println(f.toURI().toString().substring(6, f.toURI().toString().length()).replaceAll("%20", " "));
                    try {
                        if (model.addSong(f.toURI().toString().substring(6, f.toURI().toString().length()).replaceAll("%20", " "))) {
                            loadSong();
                        }
                    } catch (SQLException ex) {

                    }
                }
            }
        }
    }
}
