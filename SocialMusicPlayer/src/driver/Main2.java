package Driver;

import Model.FacadeModel;
import View.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main2 extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        LoginView view = new LoginView(primaryStage, new FacadeModel());
    }

    public static void main(String[] args) {
        launch(args);
    }
}