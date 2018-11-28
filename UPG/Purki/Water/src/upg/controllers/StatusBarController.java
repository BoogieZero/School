/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import upg.utilities.updateDisplay;

/**
 * FXML Controller class
 *
 * @author purkart
 */
public class StatusBarController implements Initializable {
    private static Label author;
    private static Label objectId;
    private static Label objectX;
    private static Label objectY;
    private static Label dateAndTime;
    public static Timer dateAndTimeTimer;
    DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    @FXML
    private Label DATE_AND_TIME;
    
    @FXML
    private Label AUTHOR;
    
    @FXML
    private Label OBJECT_ID;
    
    @FXML
    private Label OBJECT_X;
    
    @FXML
    private Label OBJECT_Y;
    
    @FXML
    private AnchorPane STATUS_BAR;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        STATUS_BAR.setStyle("-fx-background-color: Silver");
        author = AUTHOR;
        author.setText("Purkart - A17B0050K");
        objectId = OBJECT_ID;
        objectX = OBJECT_X;
        objectY = OBJECT_Y;
        dateAndTime = DATE_AND_TIME;
        objectId.setText("");
        objectX.setText("");
        objectY.setText("");
        dateAndTime.setText("");
        dateAndTimeTimer = new Timer();
        dateAndTimeTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                LocalDateTime ld = LocalDateTime.now();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        dateAndTime.setText(ld.format(sdf));
                    }
                });
            }
        }, 0, 1000);
    }    
    
    
    public static void setInfo(String id, String x, String y){
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                objectId.setText(id);
                objectX.setText(x);
                objectY.setText(y);
            }
        });
    }
}
