/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import upg.controllers.MainWindowController;
import upg.controllers.RightInfoBarController;
import upg.controllers.StatusBarController;

/**
 *
 * @author Ondrej Purkart - A17B0050K
 * @version 0.6.3.22
 */
public class UPG extends Application {
    public static double GLYPH_SIZE = 0;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("gui/MainWindow.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Semestralni prace KIV/UPG pro akademicky rok 2017/2018. Vypracoval Ondrej Purkart, A17B0050K");
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop(){
        MainWindowController.dwn.wnTimer.cancel();
        RightInfoBarController.displaySimTime.cancel();
        MainWindowController.dwn.displayTimer.cancel();
        StatusBarController.dateAndTimeTimer.cancel();
        MainWindowController.dwn.colectData.cancel();
        //MainWindowController.dwn.stopThreads();
    }

    /**
     * @param args Argument prikazove radky
     */
    public static void main(String[] args) {
        if(args.length>0)
        {
            String glyphSizeValue = args[0].substring(args[0].lastIndexOf("=")+1, args[0].length());
            GLYPH_SIZE = Double.parseDouble(glyphSizeValue.replace(',', '.'));
        }
        else
        {
            GLYPH_SIZE = 100;
        }
        //System.out.println(GLYPH_SIZE);
        launch(args);
    }
    
}
