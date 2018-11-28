/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import upg.utilities.DisplayWaterNet;
import upg.utilities.guiLoader;

/**
 *
 * @author purkart
 */
public class MainWindowController implements Initializable {
    public static String PATH_IN_STRING = "";
    public static long LAST_PROPERTY_CHANGE_TIME = 0;
    public static BorderPane mainWindow;
    public static DisplayWaterNet dwn;
    private double stageWidth = 0;
    private double stageHeight = 0;
    private long timeSinceWidthChange = 0;
    @FXML
    private BorderPane MAIN_WINDOW;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PATH_IN_STRING = url.toString();
        mainWindow = MAIN_WINDOW;
        //Kod pro RT resize - je na nem videt zpomaleni FX vlakna
        //Jednodussi bude pozice prvku aktualizovat vzdy pri prekresleni sceny pomoci timer
        //tim eliminujeme enormni pozadavek na cpu vypocty a aplikace bude plynulejsi
        ChangeListener<Number> widthChange = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try
                {
                    LAST_PROPERTY_CHANGE_TIME = System.currentTimeMillis();
                    if(oldValue.intValue()>0)
                    {
                        long difference = System.currentTimeMillis()-LAST_PROPERTY_CHANGE_TIME;
                        if(difference>1000)
                        {
                            int widthChange = newValue.intValue() - oldValue.intValue();
                            AnchorPane bottom = (AnchorPane) mainWindow.getBottom();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    int  i = 0;
                                    while(i<bottom.getChildren().size())
                                    {
                                        AnchorPane.setLeftAnchor(bottom.getChildren().get(i),
                                                bottom.getChildren().get(i).getLayoutX()+widthChange);
                                        i++;
                                    }}
                            });
                        }
                    }
                }
                catch(Exception ex)
                {
                    System.out.println(ex);
                }
            }
        };
        mainWindow.widthProperty().addListener(widthChange);
        setPath();
        loadStatusBar();
        loadMenuBar();
        //testMessageAlert();
        dwn = new DisplayWaterNet();
        loadRightInfoBar();
        loadLeftInfoBar();
    }    
    
    private void testMessageAlert(){
        Scene messageScene = new Scene((Parent) guiLoader.loadFXML("MessageAlert.fxml"));
        Stage messageStage = new Stage();
        messageStage.setScene(messageScene);
        MessageAlertController.setMessageText("Chyba v displayWaterNet() ");
        MessageAlertController.MESSAGE_ALERT_STAGE = messageStage;
        messageStage.initModality(Modality.APPLICATION_MODAL);
        messageStage.showAndWait();
    }
    
    private void loadStatusBar(){
        Node statusBar = guiLoader.loadFXML("StatusBar.fxml");
        MAIN_WINDOW.setBottom(statusBar);
    }
    
    private void loadRightInfoBar(){
        Node rightInfoBar = guiLoader.loadFXML("rightInfoBar.fxml");
        MAIN_WINDOW.setRight(rightInfoBar);
    }
    
    private void loadLeftInfoBar(){
        Node rightInfoBar = guiLoader.loadFXML("leftInfoBar.fxml");
        MAIN_WINDOW.setLeft(rightInfoBar);
    }
    
    private void loadMenuBar(){
        Node menuBar = guiLoader.loadFXML("Menu.fxml");
        MAIN_WINDOW.setTop(menuBar);
    }
    
    private void setPath(){
        int position = PATH_IN_STRING.lastIndexOf("/");
        StringBuilder sb = new StringBuilder(PATH_IN_STRING);
        while(position<sb.length())
        {
            sb.setCharAt(position, ' ');
            position++;
        }
        PATH_IN_STRING = sb.toString().trim();
    }
    
}
