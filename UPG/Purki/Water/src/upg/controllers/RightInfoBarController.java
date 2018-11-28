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
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import upg.utilities.WaternetData;

/**
 * FXML Controller class
 *
 * @author purka
 */
public class RightInfoBarController implements Initializable {
    private static Label simulationTime;
    private static Label simulationSpeed;
    private static final NumberFormat formatter = new DecimalFormat("#0.00");
    private static AnchorPane mainWindow;
    private static Label[] reservoirLabels;
    private static LineChart objectGraph;
    public static Timer displaySimTime;
    private static int platformI;
    private static int labelNumber = 0;
    @FXML
    private Label SIMULATION_TIME;
    
    @FXML
    private Label SIMULATION_SPEED;
    
    @FXML
    private AnchorPane MAIN_WINDOW;
    
    @FXML
    private LineChart OBJECT_GRAPH;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        objectGraph = OBJECT_GRAPH;
        objectGraph.setTitle("Graf obsahu vodniho rezervoaru");
        simulationTime = SIMULATION_TIME;
        simulationSpeed = SIMULATION_SPEED;
        mainWindow = MAIN_WINDOW;
        displaySimTime = new Timer();
        displaySimTime.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        SIMULATION_TIME.setText("Aktualni cas simulace: "+formatter.format(MainWindowController.dwn.getSimulationTime())+"s");
                    }
                });
            }
        }, 0, 100);
    }   
    
    public static void removeLabels(){
        int i = 0;
        platformI = 0;
        while(i<reservoirLabels.length)
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    reservoirLabels[platformI].toBack();
                    reservoirLabels[platformI].setVisible(false);
                    mainWindow.getChildren().remove(reservoirLabels[platformI]);
                    mainWindow.setVisible(false);
                    mainWindow.setVisible(true);
                    platformI++;
                }
            });
            i++;
        }
        reservoirLabels = null;
    }
    
    public static void restartTimer(){
        displaySimTime = new Timer();
        displaySimTime.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        simulationTime.setText("Aktualni cas simulace: "+formatter.format(MainWindowController.dwn.getSimulationTime())+"s");
                    }
                });
            }
        }, 0, 100);
    }
    
    public static void setSimulationTime(String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                simulationTime.setText(text);
            }
        });
    }
    
    public static void setSimulationSpeed(String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                simulationSpeed.setText(text);
            }
        });
    }
    
    @FXML
    private void slowButtonAction(){
        MainWindowController.dwn.slowSimulation();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                SIMULATION_SPEED.setText("Aktualni rychlost simulace: 50 steps/s");
            }
        });
    }
    
    @FXML
    private void fastButtonAction(){
        MainWindowController.dwn.fastenUpSimulation();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                SIMULATION_SPEED.setText("Aktualni rychlost simulace: 200 steps/s");
            }
        });
    }

    /**    
    *@param text    Text to be displayed
    *@param x       x coordinate in layout
    *@param y       y coordinate in layout
    *@param width   label width
    *@param heigth  labe heigth
    **/
    public static void addLabel(String text, double x, double y, double width, double heigth){
        Label newLabel = new Label(text);
        newLabel.setLayoutX(x);
        newLabel.setLayoutY(y);
        newLabel.setMinHeight(heigth);
        newLabel.setPrefHeight(heigth);
        newLabel.setMaxHeight(heigth);
        newLabel.setMinWidth(width);
        newLabel.setPrefWidth(width);
        newLabel.setMaxWidth(width);
        reservoirLabels[labelNumber] = newLabel;
        labelNumber++;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainWindow.getChildren().add(newLabel);
            }
        });
    }
    
    public static void prepareLabelField(int length){
        reservoirLabels = new Label[length];
    }
    
    public static void updateLabel(int id, String text){
        //System.out.println(id);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                reservoirLabels[id].setText(text);
            }
        });
    }
    
    public static void resetLabelNumber(){
        labelNumber = 0;
    }
    
    public static void setGraphData(WaternetData data, String reservoirNumber, String objectType){
        if(!objectGraph.getTitle().contains(reservoirNumber))
        {
            objectGraph.getXAxis().setLabel("Cas simulace (s)");
            objectGraph.getYAxis().setLabel("Obsah rezervoaru (%)");
            objectGraph.getData().clear();
            //System.out.println(objectType);
            if(objectType.equals("upg.WaterNetwork.Reservoir"))
            {
                objectGraph.setTitle("Graf obsahu vodniho rezervoaru "+reservoirNumber);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //objectGraph.getData().add(data.getVolumeInMeters());
                        objectGraph.getData().add(data.getPercentBasedOnTime((int) LeftInfoBarController.timeMin.getValue(),
                                                                                (int) LeftInfoBarController.timeMax.getValue(),
                                                                                (int) LeftInfoBarController.skippedSteps.getValue()));
                    }
                });
            }
        }
    }
    
}
