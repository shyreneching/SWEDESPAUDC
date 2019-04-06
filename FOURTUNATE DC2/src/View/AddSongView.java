/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.FacadeModel;
import Model.PlaylistInterface;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Stanley Sie
 */
public class AddSongView extends View {

    private FacadeModel model;
    private Stage stage;
    private Scene scene;
    private FXMLLoader loader;
    private Parent root;

    @FXML
    private Button close;
    @FXML
    private VBox vbox;

    public AddSongView(FacadeModel model) {
        this.model = model;

        try {
            loader = new FXMLLoader(getClass().getResource("/View/AddSong.fxml"));
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
        close.setOnAction(event -> {
            stage.close();
        });
    }

    private void init() {
        vbox.setMaxHeight(Double.MAX_VALUE);
        vbox.setSpacing(20);

        Label label = new Label();
        label.setText("     " + "Add song to queue");
        label.setAlignment(Pos.CENTER_LEFT);
        label.setFont(new Font("Segoe UI", 14));
        label.setPrefWidth(200);

        label.setOnMouseClicked(event -> {
            model.getCurrentPlaylist().getSongs().add(model.getSelectedSong());
            model.update();
            stage.close();
        });

        vbox.getChildren().add(label);

        if (model.getUser() == null) {
            for (PlaylistInterface p : model.getGroups()) {
                Label newp = new Label();
                newp.setText("     " + "Add song to " + p.getName());
                newp.setAlignment(Pos.CENTER_LEFT);
                newp.setFont(new Font("Segoe UI", 14));
                newp.setPrefWidth(200);

                newp.setOnMouseClicked(event -> {
                    if(!p.getSongs().contains(model.getSelectedSong())) {
                        p.getSongs().add(model.getSelectedSong());
                    }
                    stage.close();
                });

                vbox.getChildren().add(newp);
            }
        } else {
            try {
                if (model.getUserPlaylist() != null) {
                    for (PlaylistInterface p : model.getUserPlaylist()) {
                        Label newp = new Label();
                        newp.setText("     " + "Add song to " + p.getName());
                        newp.setAlignment(Pos.CENTER_LEFT);
                        newp.setFont(new Font("Segoe UI", 14));
                        newp.setPrefWidth(200);
                        
                        newp.setOnMouseClicked(event -> {
                            try {
                                if (model.addSongToPlaylist(model.getSelectedSong(), p)) {
                                    model.update();
                                }
                            } catch (SQLException ex) {
                            }
                            stage.close();
                        });
                        
                        vbox.getChildren().add(newp);
                    }
                }
            } catch (SQLException ex) { }
        }
    }

    @Override
    public void update() {

    }

}
