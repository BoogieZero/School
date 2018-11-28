/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg.controllers;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import upg.WaterNetwork.Pipe;
import upg.utilities.WaternetData;

/**
 * FXML Controller class
 *
 * @author purka
 */
public class InfoWindowPipeController implements Initializable {
    private final static NumberFormat formatter = new DecimalFormat("#0.00");
    public static Stage stage;
    public static Label crossSection;
    public static ChoiceBox valveOpeness;
    public static Label waterFlow;
    public static LineChart waterFlowGraph;
    public static LineChart valveOpenessGraph;
    private static Pipe thisPipe;
    
    @FXML
    private Label CROSS_SECTION;
    
    @FXML
    private ChoiceBox VALVE_OPENESS;
    
    @FXML
    private Label WATER_FLOW;
    
    @FXML
    private LineChart WATER_FLOW_GRAPH;
    
    @FXML
    private LineChart VALVE_OPENESS_GRAPH;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        crossSection = CROSS_SECTION;
        valveOpeness = VALVE_OPENESS;
        waterFlow = WATER_FLOW;
        waterFlowGraph = WATER_FLOW_GRAPH;
        valveOpenessGraph = VALVE_OPENESS_GRAPH;
        setOpenessValues();
    }    
    
    public static void setThisPipe(Pipe p){
        thisPipe = p;
    }
    
    @FXML
    private void okButton(){
        stage.close();
    }
    
    private void setOpenessValues(){
        int i = 0;
        while(i<=100)
        {
            //System.out.println(i);
            valveOpeness.getItems().add(Integer.toString(i)+"%");
            i += 5;
        }
    }
    
    public static void setCrossSection(double val){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                crossSection.setText("Prumer trubky (m2): "+formatter.format(val)+" m2");
            }
        });
    }
    
    public static void setVaterFlow(double val){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
               waterFlow.setText("Proud vody trubkou (m3): "+formatter.format(val)+" m3");
            }
        });
    }
    
    public static void setValveOpeness(double val){
        double percent = val * 100;
        int workingVal = (int) percent;
        String percentInString = Integer.toString(workingVal)+"%";
        valveOpeness.setValue(percentInString);
        valveOpeness.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                String newValveOpeness = (String) newValue;
                StringBuilder sb = new StringBuilder(newValveOpeness);
                sb.setCharAt(sb.length()-1, ' ');
                double newValuePercent = Double.parseDouble(sb.toString().trim());
                double usableValveOpeness = newValuePercent / 100;
                thisPipe.open = usableValveOpeness;
            }
        });
    }
    
    public static void setWaterFlowGraphData(WaternetData data, String pipeNumber, String objectType){
        if(!waterFlowGraph.getTitle().contains(pipeNumber))
        {
            waterFlowGraph.getXAxis().setLabel("Cas simulace (s)");
            waterFlowGraph.getYAxis().setLabel("Proud vody (m3)");
            waterFlowGraph.getData().clear();
            //System.out.println(objectType);
            if(objectType.equals("upg.WaterNetwork.Pipe"))
            {
                waterFlowGraph.setTitle("Graf proudu vody "+pipeNumber);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //objectGraph.getData().add(data.getVolumeInMeters());
                        waterFlowGraph.getData().add(data.getFlowBasedOnTime((int) LeftInfoBarController.timeMin.getValue(),
                                                                                (int) LeftInfoBarController.timeMax.getValue(),
                                                                                (int) LeftInfoBarController.skippedSteps.getValue()));
                    }
                });
            }
        }
    }
     
     public static void setValveOpenessGraphData(WaternetData data, String pipeNumber, String objectType){
        if(!valveOpenessGraph.getTitle().contains(pipeNumber))
        {
            valveOpenessGraph.getXAxis().setLabel("Cas simulace (s)");
            valveOpenessGraph.getYAxis().setLabel("Otevreni ventilu (m2)");
            valveOpenessGraph.getData().clear();
            //System.out.println(objectType);
            if(objectType.equals("upg.WaterNetwork.Pipe"))
            {
                valveOpenessGraph.setTitle("Graf otevreni ventilu "+pipeNumber);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //objectGraph.getData().add(data.getVolumeInMeters());
                        valveOpenessGraph.getData().add(data.getOpenessBasedOnTime((int) LeftInfoBarController.timeMin.getValue(),
                                                                                (int) LeftInfoBarController.timeMax.getValue(),
                                                                                (int) LeftInfoBarController.skippedSteps.getValue()));
                    }
                });
            }
        }
    }
    
}
