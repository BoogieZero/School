/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg.utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.LinearGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import upg.WaterNetwork.Reservoir;
import upg.controllers.MainWindowController;
import upg.controllers.MessageAlertController;
import upg.controllers.RightInfoBarController;

/**
 *
 * @author purka
 */
public class updateDisplay extends Thread{
    private int platformI;
    private final NumberFormat formatter = new DecimalFormat("#0.00");
    public updateDisplay(){
        
    }
    
    @Override
    public void run(){
        try
        {
            Rectangle[] gui = MainWindowController.dwn.getReservoirs();
            int i = 0;
            //platformI = 0;
            while(i<gui.length)
            {
                Reservoir r = (Reservoir) MainWindowController.dwn.getLogicalreservoir(gui[i]);
                double whiteOffset = 1;
                float blueOffset =((float) r.content/(float) r.capacity);
                whiteOffset = blueOffset;
                if(((float) r.content/(float) r.capacity)<0.01f)
                {
                    whiteOffset = 0;
                    blueOffset = 0;
                }
                if(((float) r.content/(float) r.capacity)>=99.99f)
                {
                    whiteOffset = 1;
                    blueOffset = 1;
                }
                Stop[] stops = new Stop[] { new Stop(blueOffset, rgb(116,204,244)), new Stop(whiteOffset, Color.WHITE)};
                LinearGradient linearGrad = LinearGradientBuilder.create()
                .startX(gui[i].getX())
                .startY(gui[i].getY()+gui[i].getHeight())
                .endX(gui[i].getX())
                .endY(gui[i].getY())
                .proportional(false)
                .cycleMethod(CycleMethod.NO_CYCLE)
                .stops(stops)
                .build();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        RightInfoBarController.updateLabel(r.myID-1, "Obsah rezervoaru cislo "+r.myID+" : "+formatter.format(((float) r.content/(float) r.capacity)*100)+"%");
                        gui[platformI].setFill(linearGrad);
                        //System.out.println(gui[platformI].toString());
                        platformI++;
                    }
                });
                i++;
            }
        }
        catch(Exception ex)
        {
            Scene messageScene = new Scene((Parent) guiLoader.loadFXML("MessageAlert.fxml"));
            Stage messageStage = new Stage();
            messageStage.setScene(messageScene);
            MessageAlertController.setMessageText("Chyba v updateDisplay() "+ex);
            MessageAlertController.MESSAGE_ALERT_STAGE = messageStage;
            messageStage.initModality(Modality.APPLICATION_MODAL);
            messageStage.showAndWait();
        }
    }
}
