/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.FacadeModel;
import Mp3agic.InvalidDataException;
import Mp3agic.NotSupportedException;
import Mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Stanley Sie
 */
public class EditSongView extends View {

    private Stage stage;
    private Scene scene;
    private FXMLLoader loader;
    private Parent root;
    private FacadeModel model;
    private File image;

    @FXML
    private Button close, save, edit, cancel, upload, remove;
    @FXML
    private TextField number, title, artist, album, genre, year;
    @FXML
    private ImageView cover;
    @FXML
    private Label numberLabel, titleLabel, artistLabel, albumLabel, genreLabel, yearLabel;

    public EditSongView(FacadeModel model) {
        this.model = model;
        
        try {
            loader = new FXMLLoader(getClass().getResource("/View/EditSong.fxml"));
            loader.setController(this);
            root = (Parent) loader.load();
            scene = new Scene(root);

            stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException io) {
        }
        init();
    }

    public void initialize() {
        edit.setOnAction(event -> {
            number.setText("" + model.getSelectedSong().getTrackNumber());
            title.setText(model.getSelectedSong().getName());
            artist.setText(model.getSelectedSong().getArtist());
            album.setText(model.getSelectedSong().getAlbum());
            genre.setText(model.getSelectedSong().getGenre());
            year.setText("" + model.getSelectedSong().getYear());
            number.setVisible(true);
            number.setDisable(false);
            title.setVisible(true);
            title.setDisable(false);
            artist.setVisible(true);
            artist.setDisable(false);
            album.setVisible(true);
            album.setDisable(false);
            genre.setVisible(true);
            genre.setDisable(false);
            year.setVisible(true);
            year.setDisable(false);
            edit.setVisible(false);
            edit.setDisable(true);
            save.setVisible(true);
            save.setDisable(false);
            cancel.setVisible(true);
            cancel.setDisable(false);
            remove.setVisible(true);
            remove.setDisable(false);
            upload.setVisible(true);
            upload.setDisable(false);
        });
        save.setOnAction(event -> {
            model.getSelectedSong().setTrackNumber(Integer.parseInt(number.getText()));
            model.getSelectedSong().setName(title.getText());
            model.getSelectedSong().setArtist(artist.getText());
            model.getSelectedSong().setAlbum(album.getText());
            model.getSelectedSong().setGenre(genre.getText());
            model.getSelectedSong().setYear(Integer.parseInt(year.getText()));
            numberLabel.setText(number.getText());
            titleLabel.setText(title.getText());
            artistLabel.setText(artist.getText());
            albumLabel.setText(album.getText());
            genreLabel.setText(genre.getText());
            yearLabel.setText(year.getText());
            number.setVisible(false);
            number.setDisable(true);
            title.setVisible(false);
            title.setDisable(true);
            artist.setVisible(false);
            artist.setDisable(true);
            album.setVisible(false);
            album.setDisable(true);
            genre.setVisible(false);
            genre.setDisable(true);
            year.setVisible(false);
            year.setDisable(true);
            edit.setVisible(true);
            edit.setDisable(false);
            save.setVisible(false);
            save.setDisable(true);
            cancel.setVisible(false);
            cancel.setDisable(true);
            remove.setVisible(false);
            remove.setDisable(true);
            upload.setVisible(false);
            upload.setDisable(true);

            if (model.getUser() != null) {
                try {
                    model.updateSongName(title.getText(), model.getSelectedSong());
                    model.updateSongTrackNumber(Integer.parseInt(number.getText()), model.getSelectedSong());
                    model.updateSongArtist(artist.getText(), model.getSelectedSong());
                    model.updateSongAlbum(album.getText(), model.getSelectedSong());
                    model.updateSongGenre(genre.getText(), model.getSelectedSong());
                    model.updateSongYear(Integer.parseInt(year.getText()), model.getSelectedSong());
                    model.updateSongCover(image, model.getSelectedSong());
                } catch (UnsupportedTagException ex) {
                    Logger.getLogger(EditSongView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotSupportedException ex) {
                    Logger.getLogger(EditSongView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidDataException ex) {
                    Logger.getLogger(EditSongView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(EditSongView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(EditSongView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        cancel.setOnAction(event -> {
            number.setVisible(false);
            number.setDisable(true);
            title.setVisible(false);
            title.setDisable(true);
            artist.setVisible(false);
            artist.setDisable(true);
            album.setVisible(false);
            album.setDisable(true);
            genre.setVisible(false);
            genre.setDisable(true);
            year.setVisible(false);
            year.setDisable(true);
            edit.setVisible(true);
            edit.setDisable(false);
            save.setVisible(false);
            save.setDisable(true);
            cancel.setVisible(false);
            cancel.setDisable(true);
            remove.setVisible(false);
            remove.setDisable(true);
            upload.setVisible(false);
            upload.setDisable(true);
        });
        upload.setOnAction(event -> {
            FileChooser file = new FileChooser();
            image = file.showOpenDialog(stage);
            cover.setImage(new Image(image.toURI().toString()));
        });
        remove.setOnAction(event -> {
            cover.setImage(new Image(new File("/Files/album_art.png").toURI().toString()));
        });
        close.setOnAction(event -> {
            stage.close();
        });
    }

    private void init() {
        numberLabel.setText("" + model.getSelectedSong().getTrackNumber());
        titleLabel.setText(model.getSelectedSong().getName());
        artistLabel.setText(model.getSelectedSong().getArtist());
        albumLabel.setText(model.getSelectedSong().getAlbum());
        genreLabel.setText(model.getSelectedSong().getGenre());
        yearLabel.setText("" + model.getSelectedSong().getYear());
        try {
            cover.setImage(new Image(model.getsongImage(model.getSelectedSong()).toURI().toURL().toString()));
        } catch (InvalidDataException ex) {
            Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedTagException ex) {
            Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(DashboardView.class.getName()).log(Level.SEVERE, null, ex);
        }
        number.setVisible(false);
        number.setDisable(true);
        title.setVisible(false);
        title.setDisable(true);
        artist.setVisible(false);
        artist.setDisable(true);
        album.setVisible(false);
        album.setDisable(true);
        genre.setVisible(false);
        genre.setDisable(true);
        year.setVisible(false);
        year.setDisable(true);
        save.setVisible(false);
        save.setDisable(true);
        cancel.setVisible(false);
        cancel.setDisable(true);
        remove.setVisible(false);
        remove.setDisable(true);
        upload.setVisible(false);
        upload.setDisable(true);
    }

    @Override
    public void update() {

    }

}
