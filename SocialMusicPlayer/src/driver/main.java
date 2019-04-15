package Driver;

import javafx.application.Application;
import javafx.stage.Stage;
import Model.FacadeModel;
import View.LoginView;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        LoginView view = new LoginView(primaryStage, new FacadeModel());
    }

    public static void main(String[] args) {
        launch(args);
    }
}