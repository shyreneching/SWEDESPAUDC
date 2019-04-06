/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.FacadeModel;
import Model.Playlist;
import Model.PlaylistInterface;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Stanley Sie
 */
public class AddPlaylistView extends View {
    
    private Stage stage;
    private Scene scene;
    private FXMLLoader loader;
    private Parent root;
    private FacadeModel model;
    
    @FXML
    private Button close, create;
    @FXML
    private TextField name;
    
    private DashboardView view;
    
    public AddPlaylistView(FacadeModel model, DashboardView view) {
        this.model = model;
        this.view = view;
        try {
            loader = new FXMLLoader(getClass().getResource("/View/AddPlaylist.fxml"));
            loader.setController(this);
            root = (Parent) loader.load();
            scene = new Scene(root);
            
            stage = new Stage();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException io) { }
    }
    
    public void initialize() {
        create.setDefaultButton(true);
        close.setOnAction(event -> {
            stage.close();
        });
        create.setOnAction(event -> {
            PlaylistInterface p = new Playlist();
            p.setName(name.getText().trim());
            
            if(model.getUser() == null) {
                model.addPlaylistLocally(p);
            } else {
                try {
                    model.addPlaylist(p);
                } catch (SQLException ex) { }
            }
            model.update();
            stage.close();
        });
    }
    
    @Override
    public void update() {
        
    }
    
}
