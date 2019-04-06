package View;

import Model.Account;
import Model.AccountInterface;
import Model.FacadeModel;
import Model.Playlist;
import Model.PlaylistInterface;
import Model.SongInterface;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

public class SignupView extends View {

    @FXML
    private Button mainsignup, closesignup;
    @FXML
    private Label usernamewarn, passwordwarn1, passwordwarn2, required1, required2;
    @FXML
    private TextField firstname, lastname, username;
    @FXML
    private PasswordField password, conpassword;

    private FXMLLoader loader;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private FacadeModel model;

    public SignupView(FacadeModel model) {
        this.model = model;
        try {
            loader = new FXMLLoader(getClass().getResource("/View/SignUp.fxml"));
            loader.setController(this);
            root = (Parent) loader.load();
            scene = new Scene(root);

            stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
        }

        init();
    }

    public void initialize() {
        mainsignup.setDefaultButton(true);
        closesignup.setOnAction(event -> {
            stage.close();
        });

        mainsignup.setOnAction(event -> {
            if (firstname.getText().trim().isEmpty()) {
                required1.setVisible(true);
            } else if (lastname.getText().trim().isEmpty()) {
                required2.setVisible(true);
            } else {
                if (username.getText().trim().isEmpty()) {
                    usernamewarn.setText("**Please input a valid username**");
                    usernamewarn.setVisible(true);
                } else {
                    if (password.getText().trim().isEmpty()) {
                        passwordwarn1.setVisible(true);
                    } else {
                        if (!password.getText().equalsIgnoreCase(conpassword.getText())) {
                            passwordwarn2.setVisible(true);
                        } else {
                            Account a = new Account();
                            a.setName(firstname.getText().trim() + " " + lastname.getText().trim());
                            a.setUsername(username.getText());
                            a.setPassword(password.getText());
                            try {
                                if (model.createUser(a)) {
                                    model.setUser((AccountInterface) a);
//                                    for(SongInterface ss : model.getSongs()) {
//                                        model.addSong(ss.getFilelocation());
//                                    }
//                                    for(PlaylistInterface pp : model.getGroups()) {
//                                        PlaylistInterface p = new Playlist();
//                                        p.setName(pp.getName());
//                                        model.addPlaylist(p);
//                                        for(SongInterface ss : pp.getSongs()) {
//                                            model.addSongToPlaylist(ss, p);
//                                        }
//                                    }
                                    stage.close();
                                } else {
                                    usernamewarn.setText("**Username is already taken**");
                                    usernamewarn.setVisible(true);
                                }
                            } catch (SQLException ex) {

                            }
                        }
                    }
                }
            }
        });
    }

    private void init() {
        usernamewarn.setText("**Warning: You can't change your username in the future**");
        passwordwarn1.setVisible(false);
        passwordwarn2.setVisible(false);
        required1.setVisible(false);
        required2.setVisible(false);
    }

    @Override
    public void update() {

    }
}
