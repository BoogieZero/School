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
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import upg.utilities.WaternetData;

/**
 * FXML Controller class
 *
 * @author purkart
 */
public class InfoWindowController implements Initializable {
    private static Label objectX;
    private static Label objectY;
    private static Label objectId;
    private static Label levelInPercent;
    private static Label levelInMeters;
    private static Label simulationTime;
    private static Label objectProperty;
    public static Stage thisStage;
    public static LineChart percentGraph;
    public static LineChart metersGraph;
    
    @FXML
    private LineChart PERCENT_GRAPH;
    
    @FXML
    private LineChart METERS_GRAPH;
    
    @FXML
    private Label LEVEL_IN_PERCENT;
    
    @FXML
    private Label LEVEL_IN_METERS;
    
    @FXML
    private Label OBJECT_X;
    
    @FXML
    private Label OBJECT_Y;
    
    @FXML
    private Label OBJECT_ID;
    
    @FXML
    private Label OBJECT_PROPERTY;
    
    @FXML
    private Label SIMULATION_TIME;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        levelInPercent = LEVEL_IN_PERCENT;
        levelInMeters = LEVEL_IN_METERS;
        metersGraph = METERS_GRAPH;
        percentGraph = PERCENT_GRAPH;
        objectX = OBJECT_X;
        objectY = OBJECT_Y;
        objectId = OBJECT_ID;
        objectProperty = OBJECT_PROPERTY;
        simulationTime = SIMULATION_TIME;
    }    
    
    @FXML
    private void okButton(){
        thisStage.close();
    }
    
    public static void setValues(String x, String y, String id, String time, String property, String percent, String meters){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                objectX.setText("Souradnice X: "+x);
                objectY.setText("Souradnice Y: "+y);
                objectId.setText("ID Objektu: "+id);
                levelInPercent.setText("Hladina (%): "  +percent+" %");
                levelInMeters.setText("Hladina (m3): "+meters+" m3");
                simulationTime.setText("Simulacni cas: "+time);
                objectProperty.setText("Kapacita: "+property+" m3");
            }
        });
    }
    
     public static void setPercentGraphData(WaternetData data, String reservoirNumber, String objectType){
        if(!percentGraph.getTitle().contains(reservoirNumber))
        {
            percentGraph.getXAxis().setLabel("Cas simulace (s)");
            percentGraph.getYAxis().setLabel("Obsah rezervoaru (%)");
            percentGraph.getData().clear();
            //System.out.println(objectType);
            if(objectType.equals("upg.WaterNetwork.Reservoir"))
            {
                percentGraph.setTitle("Graf obsahu vodniho rezervoaru "+reservoirNumber);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //objectGraph.getData().add(data.getVolumeInMeters());
                        percentGraph.getData().add(data.getPercentBasedOnTime((int) LeftInfoBarController.timeMin.getValue(),
                                                                                (int) LeftInfoBarController.timeMax.getValue(),
                                                                                (int) LeftInfoBarController.skippedSteps.getValue()));
                    }
                });
            }
        }
    }
     
     public static void setMetersGraphData(WaternetData data, String reservoirNumber, String objectType){
        if(!metersGraph.getTitle().contains(reservoirNumber))
        {
            metersGraph.getXAxis().setLabel("Cas simulace (s)");
            metersGraph.getYAxis().setLabel("Obsah rezervoaru (m3)");
            metersGraph.getData().clear();
            //System.out.println(objectType);
            if(objectType.equals("upg.WaterNetwork.Reservoir"))
            {
                metersGraph.setTitle("Graf obsahu vodniho rezervoaru "+reservoirNumber);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //objectGraph.getData().add(data.getVolumeInMeters());
                        metersGraph.getData().add(data.getMetersBasedOnTime((int) LeftInfoBarController.timeMin.getValue(),
                                                                                (int) LeftInfoBarController.timeMax.getValue(),
                                                                                (int) LeftInfoBarController.skippedSteps.getValue()));
                    }
                });
            }
        }
    }
}
