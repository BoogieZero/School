/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author purka
 */
public class MessageAlertController implements Initializable {
    private static Label messageText;
    public static Stage MESSAGE_ALERT_STAGE;
    @FXML
    private Label MESSAGE_TEXT;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        messageText = MESSAGE_TEXT;
    }  
    
    @FXML
    private void okButton(){
        MESSAGE_ALERT_STAGE.close();
    }
    
    public static void setMessageText(String message){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messageText.setText(message);
            }
        });
    }
    
}
