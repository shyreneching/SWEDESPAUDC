package View;

import Model.FacadeModel;
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
import javafx.scene.control.Label;

public class LoginView extends View {

    private FacadeModel model;

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login, signup, closelogin;
    @FXML
    private Label errormessage;

    private FXMLLoader loader;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private SongListView view;

    public LoginView(FacadeModel model, SongListView view) {
        this.model = model;
        this.view = view;
        try {
            loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
            loader.setController(this);
            root = (Parent) loader.load();
            scene = new Scene(root);

            stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ie) {
        }
        
        init();
    }

    public void initialize() {
        login.setDefaultButton(true);
        closelogin.setOnAction(event -> {
            stage.close();
        });

        login.setOnAction(event -> {
            if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) {
                errormessage.setText("**Please input a valid username and password**");
                errormessage.setVisible(true);
            } else {
                try {
                    if (model.login(username.getText().trim(), password.getText().trim())) {
                        view.loadSong();
                        stage.close();
                    } else {
                        errormessage.setText("**Incorrect username or password**");
                        errormessage.setVisible(true);
                    }
                } catch (SQLException ex) {

                }
            }
        });

        signup.setOnAction(event -> {
            SignupView view = new SignupView(model);
            stage.close();
        });
    }

    private void init() {
        errormessage.setVisible(false);
    }

    @Override
    public void update() {

    }
}
