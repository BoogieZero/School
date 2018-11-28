/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg.utilities;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import upg.WaterNetwork.Pipe;
import upg.WaterNetwork.Reservoir;
import upg.controllers.MessageAlertController;

/**
 *
 * @author purkart
 */
public class WaternetData {
    private Object o;
    private XYChart.Series volumeInPercent;
    private XYChart.Series volumeInMeters;
    private XYChart.Series pipeOpen;
    private XYChart.Series pipeFlow;
    public WaternetData(Object o){
        this.o = o;
        if(o.getClass().equals(Rectangle.class))
        {
            volumeInPercent = new XYChart.Series<>();
            volumeInMeters = new XYChart.Series<>();
            volumeInPercent.setName("Obsah (%)");
            volumeInMeters.setName("Obsah (m3)");
        }
        else if(o.getClass().equals(Line.class))
        {
            pipeOpen = new XYChart.Series<>();
            pipeFlow = new XYChart.Series<>();
        }
    }
    
    public XYChart.Series getVolumeInPercent(){
        return this.volumeInPercent;
    }
    
    public XYChart.Series getVolumeInMeters(){
        return this.volumeInMeters;
    }
    
    public XYChart.Series getPipeOpen(){
        return this.pipeOpen;
    }
    
    public XYChart.Series getPipeFlow(){
        return this.pipeFlow;
    }
    
    public void addPipeFlowData(String xAxis, double yAxis) throws ObjectClassException{
        if(!o.getClass().equals(Line.class))
        {
            throw new ObjectClassException("Method implemented only for class Pipe");
        }
        this.pipeFlow.getData().add(new XYChart.Data<>(xAxis, yAxis));
    }
    
    public void addPipeOpenData(String xAxis, double yAxis) throws ObjectClassException{
        if(!o.getClass().equals(Line.class))
        {
            throw new ObjectClassException("Method implemented only for class Pipe");
        }
        this.pipeOpen.getData().add(new XYChart.Data<>(xAxis, yAxis));
    }
    
    public void addVolumeInPercentData(String xAxis, double yAxis) throws ObjectClassException{
        //System.out.println(o.getClass());
        if(!o.getClass().equals(Rectangle.class))
        {
            throw new ObjectClassException("Method implemented only for class Reservoir");
        }
        this.volumeInPercent.getData().add(new XYChart.Data<>(xAxis, yAxis));
    }
    
    public void addVolumeInMetersData(String xAxis, double yAxis) throws ObjectClassException{
        //System.out.println(o.getClass());
        if(!o.getClass().equals(Rectangle.class))
        {
            throw new ObjectClassException("Method implemented only for class Reservoir");
        }
        this.volumeInMeters.getData().add(new XYChart.Data<>(xAxis, yAxis));
    }
    
    public XYChart.Series getFlowBasedOnTime(long start, long stop, int skip){
        int i = 0;
        XYChart.Series returnData = new XYChart.Series<>();
        Line l = (Line) o;
        returnData.setName(l.getId());
        while(i<this.pipeFlow.getData().size())
        {
            if(isBetween(start, stop, (XYChart.Data)pipeFlow.getData().get(i)))
            {
                returnData.getData().add(pipeFlow.getData().get(i));
            }
            i += skip;
        }
        if(returnData.getData().size()<1)
        {
            Scene messageScene = new Scene((Parent) guiLoader.loadFXML("MessageAlert.fxml"));
            Stage messageStage = new Stage();
            messageStage.setScene(messageScene);
            MessageAlertController.setMessageText("Zadna data k zobrazeni. Poupravte casove nastaveni grafu v levem menu...");
            MessageAlertController.MESSAGE_ALERT_STAGE = messageStage;
            messageStage.initModality(Modality.APPLICATION_MODAL);
            messageStage.showAndWait();
        }
        return returnData;
    }
    
    public XYChart.Series getMetersBasedOnTime(long start, long stop, int skip){
        int i = 0;
        XYChart.Series returnData = new XYChart.Series<>();
        Rectangle r = (Rectangle) o;
        returnData.setName(r.getId());
        while(i<this.volumeInMeters.getData().size())
        {
            if(isBetween(start, stop, (XYChart.Data)volumeInMeters.getData().get(i)))
            {
                returnData.getData().add(volumeInMeters.getData().get(i));
            }
            i += skip;
        }
        if(returnData.getData().size()<1)
        {
            Scene messageScene = new Scene((Parent) guiLoader.loadFXML("MessageAlert.fxml"));
            Stage messageStage = new Stage();
            messageStage.setScene(messageScene);
            MessageAlertController.setMessageText("Zadna data k zobrazeni. Poupravte casove nastaveni grafu v levem menu...");
            MessageAlertController.MESSAGE_ALERT_STAGE = messageStage;
            messageStage.initModality(Modality.APPLICATION_MODAL);
            messageStage.showAndWait();
        }
        return returnData;
    }
    
    public XYChart.Series getOpenessBasedOnTime(long start, long stop, int skip){
        int i = 0;
        XYChart.Series returnData = new XYChart.Series<>();
        Line l = (Line) o;
        returnData.setName(l.getId());
        while(i<this.pipeOpen.getData().size())
        {
            if(isBetween(start, stop, (XYChart.Data)pipeOpen.getData().get(i)))
            {
                returnData.getData().add(pipeOpen.getData().get(i));
            }
            i += skip;
        }
        if(returnData.getData().size()<1)
        {
            Scene messageScene = new Scene((Parent) guiLoader.loadFXML("MessageAlert.fxml"));
            Stage messageStage = new Stage();
            messageStage.setScene(messageScene);
            MessageAlertController.setMessageText("Zadna data k zobrazeni. Poupravte casove nastaveni grafu v levem menu...");
            MessageAlertController.MESSAGE_ALERT_STAGE = messageStage;
            messageStage.initModality(Modality.APPLICATION_MODAL);
            messageStage.showAndWait();
        }
        return returnData;
    }
    
    public XYChart.Series getPercentBasedOnTime(long start, long stop, int skip){
        int i = 0;
        XYChart.Series returnData = new XYChart.Series<>();
        Rectangle r = (Rectangle) o;
        returnData.setName(r.getId());
        while(i<this.volumeInPercent.getData().size())
        {
            if(isBetween(start, stop, (XYChart.Data)volumeInPercent.getData().get(i)))
            {
                returnData.getData().add(volumeInPercent.getData().get(i));
            }
            i += skip;
        }
        if(returnData.getData().size()<1)
        {
            Scene messageScene = new Scene((Parent) guiLoader.loadFXML("MessageAlert.fxml"));
            Stage messageStage = new Stage();
            messageStage.setScene(messageScene);
            MessageAlertController.setMessageText("Zadna data k zobrazeni. Poupravte casove nastaveni grafu v levem menu...");
            MessageAlertController.MESSAGE_ALERT_STAGE = messageStage;
            messageStage.initModality(Modality.APPLICATION_MODAL);
            messageStage.showAndWait();
        }
        return returnData;
    }
    
    private boolean isBetween(long start, long stop, XYChart.Data data){
        double simTime = Double.parseDouble((String) data.XValueProperty().getValue());
        if(simTime>=start && simTime<=stop)
        {
            return true;
        }
        return false;
    }
}
