/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.FacadeModel;
import Model.PlaylistInterface;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Stanley Sie
 */
public class PlaylistView extends View {

    private FacadeModel model;
    private Label listLabel;
    private TableView table;
    private TableColumn title;

    private ObservableList<PlaylistInterface> playlists;

    public PlaylistView(FacadeModel model, TableView table, Label listLabel) {
        this.model = model;
        this.table = table;
        this.listLabel = listLabel;

        playlists = FXCollections.observableArrayList();
        this.model.attach(this);

        update();
    }

    public void groupedByAlbum() {
        listLabel.setText("Albums");
        try {
            setPlaylists(model.getAlbumPlaylist());
        } catch (SQLException ex) { }
    }
    
    public void groupedByArtist() {
        listLabel.setText("Artists");
        try {
            setPlaylists(model.getArtistPlaylist());
        } catch (SQLException ex) { }
    }
    
    public void groupedByYear() {
        listLabel.setText("Years");
        try {
            setPlaylists(model.getYearPlaylist());
        } catch (SQLException ex) { }
    }
    
    public void groupedByGenre() {
        listLabel.setText("Genres");
        try {
            setPlaylists(model.getGenrePlaylist());
        } catch (SQLException ex) { }
    }

    public ObservableList<PlaylistInterface> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ObservableList<PlaylistInterface> playlists) {
        this.playlists = playlists;
        update();
    }

    @Override
    public void update() {
        title = new TableColumn("Title");
        title.setCellValueFactory(new PropertyValueFactory("name"));
        title.setPrefWidth(900);
        title.setStyle("-fx-alignment: CENTER_LEFT;-fx-text-fill: white;");
        title.setResizable(false);
        table.getColumns().setAll(title);
        table.setItems(playlists);
    }
}
