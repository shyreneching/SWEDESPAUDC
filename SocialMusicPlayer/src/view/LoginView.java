package view;

import controller.LoginController;
import controller.MusicPlayerController;
import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.FacadeModel;

import java.io.IOException;
import java.util.regex.Pattern;

public class LoginView {
    
    private Stage stage;
    private Scene scene;
    private FacadeModel model;
    private LoginController controller;

    @FXML
    private MediaView loginVideo;
    @FXML private Button signUpBtn,closeBtn,minimizeBtn,loginBtn,backBtn,registerBtn;
    @FXML private TextField usernameInput,regUsernameInput;
    @FXML private PasswordField passwordInput,regPasswordInput;
    @FXML private Rectangle titleBar,coverScreen;
    @FXML private AnchorPane anchorMove;
    @FXML private Path pathing,pathingback,pathingEnd;
    @FXML private ChoiceBox userTypeChoice;
    @FXML private Label missingLbl,missingLogLbl;

    private static final Pattern passwordPattern = Pattern.compile("^(?=.*\\d)(?=.*\\p{Punct})(?=.*[a-zA-z]).{6,20}$");

    private double xOffset = 0;
    private double yOffset = 0;

    public LoginView(Stage stage, FacadeModel model) {
        this.stage = stage;
        this.model = model;
        this.controller = new LoginController(this.model);
        FXMLLoader root = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        root.setController(this);
        this.stage.setTitle("wave.");
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.setResizable(false);
        this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/media/waveIcon.png")));
        try {
            scene = new Scene(root.load(),1200,790);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    public void initialize(){

        PathTransition move = new PathTransition();
        move.setDuration(Duration.seconds(0.5));
        move.setPath(pathing);
        move.setNode(anchorMove);
        move.setAutoReverse(false);

        PathTransition moveBack = new PathTransition();
        moveBack.setDuration(Duration.seconds(0.5));
        moveBack.setPath(pathingback);
        moveBack.setNode(anchorMove);
        moveBack.setAutoReverse(false);

        PathTransition moveEnd = new PathTransition();
        moveEnd.setDuration(Duration.seconds(0.5));
        moveEnd.setPath(pathingEnd);
        moveEnd.setNode(coverScreen);
        moveEnd.setAutoReverse(false);

        userTypeChoice.getItems().addAll("artist","listener");

        MediaPlayer mediaPlayer = new MediaPlayer(new Media(getClass().getResource("/media/loginVideo.mp4").toExternalForm()));
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setMute(true);
        mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
        loginVideo.setMediaPlayer(mediaPlayer);
        mediaPlayer.setOnReady(()->{

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

        usernameInput.setOnKeyPressed(event -> {
            missingLogLbl.setOpacity(0);
        });

        passwordInput.setOnKeyPressed(event -> {
            missingLogLbl.setOpacity(0);
        });

        loginBtn.setOnMouseEntered(event -> {
            loginBtn.setStyle("-fx-background-color: #00ffe3;");
        });

        loginBtn.setOnMouseExited(event -> {
            loginBtn.setStyle("-fx-background-color: #00ead0");
        });

        loginBtn.setOnAction(event -> {
            if (!usernameInput.getText().isEmpty()&&!passwordInput.getText().isEmpty()){
                coverScreen.setDisable(false);
                coverScreen.setOpacity(1);
                coverScreen.setVisible(true);
                moveEnd.setOnFinished(event1 -> {
                    mediaPlayer.stop();
                    Stage stage = (Stage) loginBtn.getScene().getWindow();
                    MusicPlayerView musicPlayerView = new MusicPlayerView(stage, model);
                });
                moveEnd.play();
            } else {
                missingLogLbl.setOpacity(1);
            }
        });

        signUpBtn.setOnMouseEntered(event -> {
            signUpBtn.setStyle("-fx-background-color: #00ffe3;");
        });

        signUpBtn.setOnMouseExited(event -> {
            signUpBtn.setStyle("-fx-background-color: #00ead0");
        });

        signUpBtn.setOnAction(event -> {
            anchorMove.setDisable(false);
            anchorMove.setOpacity(1);
            move.play();
        });

        regUsernameInput.setOnKeyPressed(event -> {
            missingLbl.setOpacity(0);
        });

        regPasswordInput.setOnKeyPressed(event -> {
            missingLbl.setOpacity(0);
        });

        userTypeChoice.setOnMouseClicked(event -> {
            missingLbl.setOpacity(0);
        });

        registerBtn.setOnMouseEntered(event -> {
            registerBtn.setStyle("-fx-background-color: #353b3e;");
        });

        registerBtn.setOnMouseExited(event -> {
            registerBtn.setStyle("-fx-background-color: #252a2c");
        });

        registerBtn.setOnAction(event -> {
            if (!regUsernameInput.getText().isEmpty()&&!regPasswordInput.getText().isEmpty()&&userTypeChoice.getValue()!=null
                    &&passwordPattern.matcher(regPasswordInput.getText()).matches()){
                regUsernameInput.clear();
                regPasswordInput.clear();
                moveBack.play();
            } else if (!passwordPattern.matcher(regPasswordInput.getText()).matches()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Invalid Password");
                alert.setContentText("Password Requires at least 1 number, 1 special character, 6-20 characters, 1 alphabetical character");
                alert.showAndWait();
            } else {
                missingLbl.setOpacity(1);
            }
        });

        backBtn.setOnMouseEntered(event -> {
            backBtn.setStyle("-fx-background-color: #353b3e;");
        });

        backBtn.setOnMouseExited(event -> {
            backBtn.setStyle("-fx-background-color: #252a2c");
        });

        backBtn.setOnAction(event -> {
            moveBack.play();
        });
    }
}
