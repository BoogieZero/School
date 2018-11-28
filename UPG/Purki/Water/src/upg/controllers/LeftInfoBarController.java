/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.event.ChangeListener;
import upg.utilities.guiLoader;

/**
 * FXML Controller class
 *
 * @author purkart
 */
public class LeftInfoBarController implements Initializable {
    public static ChoiceBox timeMin;
    public static ChoiceBox timeMax;
    public static Slider skippedSteps;
    public static ChoiceBox glyphSizeValue;
    
    @FXML
    private ChoiceBox GLYPH_SIZE_VALUE;
    
    @FXML
    private ChoiceBox TIME_MIN;
    
    @FXML
    private ChoiceBox TIME_MAX;
    
    @FXML
    private Slider SKIPPED_STEPS;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        glyphSizeValue = GLYPH_SIZE_VALUE;
        skippedSteps = SKIPPED_STEPS;
        timeMin = TIME_MIN;
        timeMax = TIME_MAX;
        SKIPPED_STEPS.setSnapToTicks(true);
        SKIPPED_STEPS.valueProperty().addListener((obs, oldval, newVal) ->
        SKIPPED_STEPS.setValue(Math.round(newVal.doubleValue())));
        setChoiceBoxes();
        TIME_MIN.valueProperty().addListener(new javafx.beans.value.ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if((int) newValue>=(int) TIME_MAX.getValue())
                {
                    Scene messageScene = new Scene((Parent) guiLoader.loadFXML("MessageAlert.fxml"));
                    Stage messageStage = new Stage();
                    messageStage.setScene(messageScene);
                    MessageAlertController.setMessageText("Cas grafu od nemuze byt vetsi nez Cas grafu do!");
                    MessageAlertController.MESSAGE_ALERT_STAGE = messageStage;
                    messageStage.initModality(Modality.APPLICATION_MODAL);
                    messageStage.showAndWait();
                    TIME_MIN.setValue(oldValue);
                }
                else
                {
                    TIME_MIN.setValue(newValue);
                }
            }
        });
        TIME_MAX.valueProperty().addListener(new javafx.beans.value.ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if((int) newValue<=(int) TIME_MIN.getValue())
                {
                    Scene messageScene = new Scene((Parent) guiLoader.loadFXML("MessageAlert.fxml"));
                    Stage messageStage = new Stage();
                    messageStage.setScene(messageScene);
                    MessageAlertController.setMessageText("Cas grafu do nemuze byt mensi nez Cas grafu od!");
                    MessageAlertController.MESSAGE_ALERT_STAGE = messageStage;
                    messageStage.initModality(Modality.APPLICATION_MODAL);
                    messageStage.showAndWait();
                    TIME_MAX.setValue(oldValue);
                }
                else
                {
                    TIME_MAX.setValue(newValue);
                }
            }
        });
        setGlyphSizeValues();
        glyphSizeValue.valueProperty().addListener(new javafx.beans.value.ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                upg.UPG.GLYPH_SIZE = (double) glyphSizeValue.getValue();
                MainWindowController.dwn.newGlyphSizeValue();
            }
        });
    }    
    
    private void setGlyphSizeValues(){
        double i = 70.0;
        while(i<140.0)
        {
            glyphSizeValue.getItems().add(i);
            i += 10.0;
        }
        glyphSizeValue.setValue(upg.UPG.GLYPH_SIZE);
    }
    
    private void setChoiceBoxes(){
        ObservableList<Integer> values = TIME_MIN.getItems();
        int i = 0;
        while(i<3000)
        {
            values.add(i);
            i += 100;
        }
        TIME_MIN.setItems(values);
        TIME_MAX.setItems(values);
        TIME_MIN.setValue(0);
        TIME_MAX.setValue(100);
    }
    
}
