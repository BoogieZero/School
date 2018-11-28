/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import upg.utilities.DisplayWaterNet;

/**
 * FXML Controller class
 *
 * @author purka
 */
public class MenuController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void exitApp(){
        System.exit(0);
    }
    
    @FXML
    private void restartSim(){
        MainWindowController.dwn.displayTimer.cancel();
        MainWindowController.dwn.wnTimer.cancel();
        RightInfoBarController.displaySimTime.cancel();
        RightInfoBarController.resetLabelNumber();
        RightInfoBarController.removeLabels();
        MainWindowController.dwn = new DisplayWaterNet();
        RightInfoBarController.restartTimer();
    }
    
}
