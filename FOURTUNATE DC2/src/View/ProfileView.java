package View;

import Model.Account;
import Model.AccountInterface;
import Model.FacadeModel;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class ProfileView extends View {

    @FXML
    private Button editeverything, save, cancel, closeprofile, see;
    @FXML
    private Label displayfullname, displayusername, displaypassword;
    @FXML
    private TextField newusername, newfullname;
    @FXML
    private PasswordField newpassword;

    private FXMLLoader loader;
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    private FacadeModel model;

    public ProfileView (FacadeModel model) {
        this.model = model;
        try {
            loader = new FXMLLoader(getClass().getResource("/View/Profile.fxml"));
            loader.setController(this);
            root = (Parent) loader.load();
            scene = new Scene(root);

            stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();

        } catch (IOException ex) {
        }
        
        init();
    }
    
    public void initialize () {
        closeprofile.setOnAction(event -> {
            stage.close();
        });

        editeverything.setOnAction(event -> {
            newfullname.setVisible(true);
            newfullname.setDisable(false);
            newusername.setVisible(true);
            newusername.setDisable(false);
            newpassword.setVisible(true);
            newpassword.setDisable(false);
            displayfullname.setVisible(false);
            save.setVisible(true);
            save.setDisable(false);
            cancel.setVisible(true);
            cancel.setDisable(false);
            editeverything.setDisable(true);
            editeverything.setVisible(false);
        });

        save.setOnAction(event -> {
            try {
                model.updateNameofUser(newfullname.getText().trim(), (AccountInterface) model.getUser());
                model.updateUserPassword(newpassword.getText().trim(), (AccountInterface) model.getUser());
            } catch (SQLException ex) { }
            newfullname.setVisible(false);
            newfullname.setDisable(true);
            newusername.setVisible(false);
            newusername.setDisable(true);
            newpassword.setVisible(false);
            newpassword.setDisable(true);
            displayfullname.setVisible(true);
            save.setVisible(false);
            save.setDisable(true);
            cancel.setVisible(false);
            cancel.setDisable(true);
            editeverything.setDisable(false);
            editeverything.setVisible(true);
            update();
        });

        cancel.setOnAction(event -> {
            newfullname.setVisible(false);
            newfullname.setDisable(true);
            newusername.setVisible(false);
            newusername.setDisable(true);
            newpassword.setVisible(false);
            newpassword.setDisable(true);
            displayfullname.setVisible(true);
            save.setVisible(false);
            save.setDisable(true);
            cancel.setVisible(false);
            cancel.setDisable(true);
            editeverything.setDisable(false);
            editeverything.setVisible(true);
        });
    }
    
    private void init () {
        newfullname.setVisible(false);
        newusername.setVisible(false);
        newpassword.setVisible(false);
        newfullname.setDisable(true);
        newusername.setDisable(true);
        newpassword.setDisable(true);
        newfullname.setText(model.getUser().getName());
        newusername.setText(model.getUser().getUsername());
        newpassword.setText(model.getUser().getPassword());
        save.setVisible(false);
        cancel.setVisible(false);
        save.setDisable(true);
        cancel.setDisable(true);
        update();
    }
    
    public String stars(String pw) {
        String x = "";
        for(int i = 0; i < pw.length(); i++) {
            x += "*";
        }
        return x;
    }
    
    @Override
    public void update() {
        displayfullname.setText(model.getUser().getName());
        displayusername.setText(model.getUser().getUsername());
        displaypassword.setText(stars(model.getUser().getPassword()));
    }
}
