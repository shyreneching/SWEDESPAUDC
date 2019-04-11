package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader root = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        primaryStage.setTitle("wave.");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/media/waveIcon.png")));
        Scene scene = new Scene(root.load(),1200,790);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}