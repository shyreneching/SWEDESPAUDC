package driver;

import javafx.application.Application;
import javafx.stage.Stage;
import model.FacadeModel;
import view.LoginView;

public class main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        LoginView view = new LoginView(primaryStage, new FacadeModel());
    }

    public static void main(String[] args) {
        launch(args);
    }
}