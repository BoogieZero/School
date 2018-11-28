package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("client.fxml"));
        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(root, 335, 275));
        primaryStage.show();
    }

    /**
     * Main entry point.
     * Starts GUI.
     * @param args no arguments are expected
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop(){
        ConnectionWorker.killWorkers();
        System.out.println("Client closed. Shutting down threads.");
    }
}
