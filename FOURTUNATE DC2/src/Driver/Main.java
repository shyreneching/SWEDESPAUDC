/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Driver;

import Model.FacadeModel;
import View.DashboardView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Stanley Sie
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DashboardView view = new DashboardView(new FacadeModel(), primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
