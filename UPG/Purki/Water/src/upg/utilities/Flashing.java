/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg.utilities;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import upg.WaterNetwork.Reservoir;
import upg.controllers.MessageAlertController;

/**
 *
 * @author purka
 */
public class Flashing extends Thread{
    private boolean isEmpty;
    private boolean isFull;
    private boolean isActive;
    private final Rectangle reservoir;
    private final Reservoir logicReservoir;
    private boolean isVisible = true;
    
    public Flashing(Rectangle rec, Reservoir r){
        this.reservoir = rec;
        this.logicReservoir = r;
        if(r.content == 0)
        {
            isEmpty = true;
        }
        else
        {
            isEmpty = false;
        }
        if(r.content == r.capacity)
        {
            isFull = true;
        }
        else
        {
            isFull = false;
        }
    }
    
    public void Shutdown(){
        this.isActive = false;
    }
            
    
    @Override
    public void run(){
        this.isActive = true;
        try
        {
            while(this.isActive)
            {
                getReservoirState();
                flashingAnimation();
                Thread.sleep(500);
            }
        }
        catch(Exception ex)
        {
            Scene messageScene = new Scene((Parent) guiLoader.loadFXML("MessageAlert.fxml"));
            Stage messageStage = new Stage();
            messageStage.setScene(messageScene);
            MessageAlertController.setMessageText("Chyba v Flashing "+ex);
            MessageAlertController.MESSAGE_ALERT_STAGE = messageStage;
            messageStage.initModality(Modality.APPLICATION_MODAL);
            messageStage.showAndWait();
        }
    }
    
    private void getReservoirState(){
        if(this.logicReservoir.content < 0.01)
        {
            this.isEmpty = true;
        }
        else
        {
            this.isEmpty = false;
        }
        if(this.logicReservoir.content >= this.logicReservoir.capacity)
        {
            this.isFull = true;
        }
        else
        {
            this.isFull = false;
        }
    }
    
    private void flashingAnimation(){
        if(this.isEmpty || this.isFull)
        {
            if(this.isVisible)
            {
                this.reservoir.setVisible(false);
                this.isVisible = false;
            }
            else
            {
                this.reservoir.setVisible(true);
                this.isVisible = true;
            }
        }
        else
        {
            this.reservoir.setVisible(true);
            this.isVisible = true;
        }
    }
    
}
