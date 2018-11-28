/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upg.utilities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.LinearGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import upg.WaterNetwork.NetworkNode;
import upg.WaterNetwork.Pipe;
import upg.WaterNetwork.Reservoir;
import upg.WaterNetwork.WaterNetwork;
import upg.controllers.InfoWindowController;
import upg.controllers.InfoWindowPipeController;
import upg.controllers.MainWindowController;
import upg.controllers.MessageAlertController;
import upg.controllers.RightInfoBarController;
import upg.controllers.StatusBarController;

/**
 *
 * @author purkart
 */
public class DisplayWaterNet {
    private final NumberFormat formatter = new DecimalFormat("#0.00");
    private final AnchorPane waterScene;
    private Rectangle reservoirs[];
    private Circle nodes[];
    private Line linePipes[];
    private final WaterNetwork wn;
    private final int displayMultiplier = 1;
    private final double xMove;
    private final double yMove;
    private double glyphSize = upg.UPG.GLYPH_SIZE;
    private Reservoir biggestActiveReservoir;
    private final List<WaternetData> reservoirGraphData = Collections.synchronizedList(new ArrayList<>());
    private Pipe biggestActivePipe;
    private int id = 1;
    public Timer wnTimer;
    public Timer displayTimer;
    public Timer colectData;
    public Timer colectDataPipe;
    private final NetworkNode[] allNodes;
    private final Pipe[] allPipes;
    private ArrayList<Flashing> flashingThreads = new ArrayList(); 
    
    public DisplayWaterNet(){
        xMove = glyphSize;
        yMove = glyphSize;
        wn = new WaterNetwork(4);
        //casovac ktery updatuje stav waterNetwork v poskytnute implementaci
        wnTimer = new Timer();
        wnTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                wn.updateState();
            }
        }, 0,100);
        allNodes = wn.getAllNetworkNodes();
        allPipes = wn.getAllPipes();
        findBiggestReservoir();
        findBiggestPipe();
        waterScene = new AnchorPane();
        waterScene.setStyle("-fx-background-color: #696969");
        addWaterNodes();
        addWaterPipes();
        //repositionToCenter();
        MainWindowController.mainWindow.setCenter(waterScene);
        //timer, ktery obnovuje stav implementace v graficke reprezentaci
        displayTimer = new Timer();
        displayTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                updateDisplay ud = new updateDisplay();
                ud.start();
            }
        }, 0, 50);
        //graf se nyni aktualizuje v teto metode kazdou vterinu diky implementaci pres arayList
        //ma to negativni vliv na beh aplikace - neni osetreno kdyz se stane ze graf chce data
        //ale cyklus v collectData jeste nedobehl - vznikne chyba a graf se prestane aktualizovat
        colectData = new Timer();
        colectData.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                int i = 0;
                while(i<reservoirs.length)
                {
                    Reservoir r = (Reservoir) allNodes[Integer.parseInt(reservoirs[i].getId())-1];
                    String simTime = formatter.format(wn.currentSimulationTime());
                    simTime = simTime.replace(',', '.');
                    //double st = Double.parseDouble(simTime);
                    try
                    {
                        reservoirGraphData.get(Integer.parseInt(reservoirs[i].getId())-1).addVolumeInMetersData(simTime, r.content);
                        reservoirGraphData.get(Integer.parseInt(reservoirs[i].getId())-1).addVolumeInPercentData(simTime, contentInPercent(r));
                    }
                    catch(ObjectClassException ex)
                    {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Scene messageScene = new Scene((Parent) guiLoader.loadFXML("MessageAlert.fxml"));
                                Stage messageStage = new Stage();
                                messageStage.setScene(messageScene);
                                MessageAlertController.setMessageText(ex.toString());
                                MessageAlertController.MESSAGE_ALERT_STAGE = messageStage;
                                messageStage.initModality(Modality.APPLICATION_MODAL);
                                messageStage.showAndWait();
                            }
                        });
                        this.cancel();
                    }
                    i++;
                }
                i = 0;
                while(i<allPipes.length)
                {
                    Pipe p = (Pipe) allPipes[i];
                    String simTime = formatter.format(wn.currentSimulationTime());
                    simTime = simTime.replace(',', '.');
                    //double st = Double.parseDouble(simTime);
                    try
                    {
                        reservoirGraphData.get(i+reservoirs.length).addPipeFlowData(simTime, p.flow);
                        reservoirGraphData.get(i+reservoirs.length).addPipeOpenData(simTime, p.open);
                    }
                    catch(ObjectClassException ex)
                    {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Scene messageScene = new Scene((Parent) guiLoader.loadFXML("MessageAlert.fxml"));
                                Stage messageStage = new Stage();
                                messageStage.setScene(messageScene);
                                MessageAlertController.setMessageText(ex.toString());
                                MessageAlertController.MESSAGE_ALERT_STAGE = messageStage;
                                messageStage.initModality(Modality.APPLICATION_MODAL);
                                messageStage.showAndWait();
                            }
                        });
                        this.cancel();
                    }
                    i++;
                }
            }
        }, 0, 1000);
        //startThreads();
        reservoirsToFront();
    }
    
    public void stopThreads(){
        int i = 0;
        while(i<flashingThreads.size())
        {
            flashingThreads.get(i).Shutdown();
            i++;
        }
    }
    
    private void startThreads(){
        int i = 0;
        while(i<flashingThreads.size())
        {
            flashingThreads.get(i).start();
            //System.out.println(i);
            i++;
        }
    }
    /**
     * Presune vsechny rezervoary na platne do popredi, aby je neprekryvala
     * jina komponenta
     */
    private void reservoirsToFront(){
        int i = 0;
        while(i<reservoirs.length)
        {
            reservoirs[i].toFront();
            i++;
        }
    }
    
    private double contentInPercent(Reservoir r){
        String percent = formatter.format((r.content/r.capacity)*100);
        //System.out.println(Double.parseDouble(percent.replace(',', '.')));
        return Double.parseDouble(percent.replace(',', '.'));
    }
    
    /**
     * Presune graficke znazorneni doprostred platna
     */
    private void repositionToCenter(){
        double maxWidth = waterScene.getBoundsInParent().getWidth();
        double maxHeight = waterScene.getBoundsInParent().getHeight();
        System.out.println("maxWidth "+maxWidth);
        System.out.println("maxHeight "+maxHeight);
        double minWidthLeft = findMinWidthLeft();
        double minWidthRight = findMinWidthRight(maxWidth);
        double minHeightTop = findMinHeightTop();
        double minHeightBottom = findMinHeightBottom(maxHeight);
        System.out.println("mwl "+minWidthLeft);
        System.out.println("mwr "+minWidthRight);
        System.out.println("mht "+minHeightTop);
        System.out.println("mhb "+minHeightBottom);
        double translateX = 0;
        double translateY = 0;
        if(minWidthLeft<minWidthRight)
        {
            translateX = (minWidthRight-minWidthLeft)/2;
        }
        else
        {
            translateX = ((minWidthLeft-minWidthRight)*-1)/2;
        }
        if(minHeightTop<minHeightBottom)
        {
            translateY = (minHeightBottom-minHeightTop)/2;
        }
        else
        {
            translateY = ((minHeightTop - minHeightBottom)*-1)/2;
        }
        int i = 0;
        System.out.println("tx "+translateX);
        System.out.println("ty "+translateY);
        while(i<waterScene.getChildren().size())
        {
            waterScene.getChildren().get(i).translateXProperty().set(translateX);
            waterScene.getChildren().get(i).translateYProperty().set(translateY);
            i++;
        }
    }
    
    /**
     * 
     * @param maxHeigth Aktualni maximlani vyska okna
     * @return metoda vrati nejmensi vzdalenost kterekoli komponenty a 
     * spodni hrany okna
     */
    private double findMinHeightBottom(double maxHeigth){
        int i = 0;
        double minHeightBottom = 999;
        while(i<waterScene.getChildren().size())
        {
            if((maxHeigth-waterScene.getChildren().get(i).getBoundsInParent().getMinY())+waterScene.getChildren().
                    get(i).boundsInParentProperty().getValue().getHeight()<minHeightBottom)
            {
                minHeightBottom = (maxHeigth-waterScene.getChildren().get(i).getBoundsInParent().getMinY())+waterScene.getChildren().
                    get(i).boundsInParentProperty().getValue().getHeight();
            }
            i++;
        }
        return minHeightBottom;
    }
        
    /**
     * 
     * @return Metoda vrati nejmensi vzdalenost kterekoli komponenty a horni
     * hrany okna
     */
    private double findMinHeightTop(){
        int i = 0;
        double minHeightTop = 999;
        while(i<waterScene.getChildren().size())
        {
            if(waterScene.getChildren().get(i).getBoundsInParent().getMinY()<minHeightTop)
            {
                minHeightTop = waterScene.getChildren().get(i).getBoundsInParent().getMinY();
            }
            i++;
        }
        return minHeightTop;
    }
    
    /**
     * 
     * @param maxWidth Aktualni maximalni sirka okna
     * @return Vrati nejmensi vzdalenost kterekoli komponenty a prave
     * hrany okna
     */
    private double findMinWidthRight(double maxWidth){
        int i = 0;
        double minWidthRight = 999;
        while(i<waterScene.getChildren().size())
        {
            if((maxWidth-waterScene.getChildren().get(i).getBoundsInParent().getMinX())+waterScene.getChildren().
                    get(i).boundsInParentProperty().getValue().getWidth()<minWidthRight)
            {
                minWidthRight = (maxWidth-waterScene.getChildren().get(i).getBoundsInParent().getMinX())+waterScene.getChildren().
                    get(i).boundsInParentProperty().getValue().getWidth();
            }
            i++;
        }
        return minWidthRight;
    }
    
    /**
     * @return Vrati nejmensi vzdalenost kterekoli komponenty a leve
     * hrany okna
    */
    private double findMinWidthLeft(){
        int i = 0;
        double minWidthLeft = 999;
        while(i<waterScene.getChildren().size())
        {
            if(waterScene.getChildren().get(i).getBoundsInParent().getMinX()<minWidthLeft)
            {
                minWidthLeft = waterScene.getChildren().get(i).getBoundsInParent().getMinX();
            }
            i++;
        }
        return minWidthLeft;
    }
    
    public void newGlyphSizeValue(){
        glyphSize = upg.UPG.GLYPH_SIZE;
        int i = 0;
        while(i<reservoirs.length)
        {
            Reservoir r = (Reservoir)wn.getAllNetworkNodes()[i];
            double resize = r.capacity/biggestActiveReservoir.capacity;
            reservoirs[i].setWidth(resize*upg.UPG.GLYPH_SIZE);
            reservoirs[i].setHeight(resize*upg.UPG.GLYPH_SIZE);
            i++;
        }
        i = 0;
        double maxWidth = 0.2*upg.UPG.GLYPH_SIZE;
        while(i<linePipes.length)
        {
            Pipe p = allPipes[i];
            double resize = p.crossSection/biggestActivePipe.crossSection;
            linePipes[i].setStrokeWidth(maxWidth*resize);
            i++;
        }
    }
    
    /**
     * Metoda pro vykresleni trubek z jednotlivych Node v implementaci
     */
    private void addWaterPipes(){
        try
        {
            Pipe[] networkPipes = wn.getAllPipes();
            linePipes = new Line[networkPipes.length];
            int  i = 0;
            while(i<networkPipes.length)
            {
                Line newLine = new Line();
                Line coverLine = new Line();
                coverLine.setOpacity(0);
                newLine.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try
                        {
                            Line sceneClicked = (Line) event.getSource();
                            Pipe clicked = (Pipe) allPipes[(Integer.parseInt(sceneClicked.getId())-1)-(allNodes.length)];
                            StatusBarController.setInfo("myID Objektu: "+sceneClicked.getId(), "Vychozi node myID: "+clicked.start.myID, "Koncovy node myID: "+clicked.end.myID);
                            if(event.getClickCount()>1)
                            {
                                Node newNode = guiLoader.loadFXML("InfoWindowPipe.fxml");
                                Parent root = (Parent) newNode;
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setResizable(false);
                                InfoWindowPipeController.stage = stage;
                                InfoWindowPipeController.setCrossSection(clicked.crossSection);
                                InfoWindowPipeController.setVaterFlow(clicked.flow);
                                InfoWindowPipeController.setValveOpeness(clicked.open);
                                InfoWindowPipeController.setThisPipe(clicked);
                                stage.showAndWait();
                            }
                        }
                        catch(Exception ex)
                        {
                            System.out.println(ex);
                        }
                    }
                });
                coverLine.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try
                        {
                            Line sceneClicked = (Line) event.getSource();
                            Pipe clicked = (Pipe) allPipes[(Integer.parseInt(sceneClicked.getId())-1)-(allNodes.length)];
                            StatusBarController.setInfo("myID Objektu: "+sceneClicked.getId(), "Vychozi node myID: "+clicked.start.myID, "Koncovy node myID: "+clicked.end.myID);
                            if(event.getClickCount()>1)
                            {
                                Node newNode = guiLoader.loadFXML("InfoWindowPipe.fxml");
                                Parent root = (Parent) newNode;
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setResizable(false);
                                InfoWindowPipeController.stage = stage;
                                InfoWindowPipeController.setCrossSection(clicked.crossSection);
                                InfoWindowPipeController.setVaterFlow(clicked.flow);
                                InfoWindowPipeController.setValveOpeness(clicked.open);
                                InfoWindowPipeController.setThisPipe(clicked);
                                int sceneClickedId = Integer.parseInt(sceneClicked.getId());
                                InfoWindowPipeController.valveOpenessGraph.setTitle("Potrubi ");
                                InfoWindowPipeController.setValveOpenessGraphData(reservoirGraphData.get(sceneClickedId-allNodes.length+reservoirs.length-1), sceneClicked.getId(), Pipe.class.getName());
                                InfoWindowPipeController.waterFlowGraph.setTitle("Potrubi ");
                                InfoWindowPipeController.setWaterFlowGraphData(reservoirGraphData.get(sceneClickedId-allNodes.length+reservoirs.length-1), sceneClicked.getId(), Pipe.class.getName());
                                stage.showAndWait();
                            }
                        }
                        catch(Exception ex)
                        {
                            System.out.println(ex);
                        }
                    }
                });
                double maxWidth = 0.2*glyphSize;
                double resize = networkPipes[i].crossSection/biggestActivePipe.crossSection;
                newLine.setId(Integer.toString(id));
                coverLine.setId(Integer.toString(id));
                //System.out.println(id);
                id++;
                newLine.setStrokeWidth(maxWidth*resize);
                newLine.setStroke(Color.DARKSLATEGRAY);
                newLine.setStartX((networkPipes[i].start.position.getX()*displayMultiplier)+xMove);
                newLine.setStartY((networkPipes[i].start.position.getY()*displayMultiplier)+yMove);
                newLine.setEndX((networkPipes[i].end.position.getX()*displayMultiplier)+xMove);
                newLine.setEndY((networkPipes[i].end.position.getY()*displayMultiplier)+yMove);
                coverLine.setStrokeWidth(20);
                //coverLine.setStroke(Color.DARKSLATEGRAY);
                coverLine.setStartX((networkPipes[i].start.position.getX()*displayMultiplier)+xMove);
                coverLine.setStartY((networkPipes[i].start.position.getY()*displayMultiplier)+yMove);
                coverLine.setEndX((networkPipes[i].end.position.getX()*displayMultiplier)+xMove);
                coverLine.setEndY((networkPipes[i].end.position.getY()*displayMultiplier)+yMove);
                /*Arrow newArrow = new Arrow();
                newArrow.drawArrowHead(newArrow.drawBody(waterScene, newLine.getStartX(),
                                        newLine.getStartY(), newLine.getEndX(), newLine.getEndY())
                                        , waterScene);*/
                //System.out.println(id-wn.getAllNetworkNodes().length-2);
                WaternetData wnd = new WaternetData(newLine);
                reservoirGraphData.add(wnd);
                linePipes[id-wn.getAllNetworkNodes().length-2] = newLine;
                waterScene.getChildren().add(newLine);
                waterScene.getChildren().add(coverLine);
                newLine.toBack();
                i++;
            }
        }
        catch(Exception ex)
        {
            //
            System.out.println("136 @ DisplayWaterNet "+ex);
        }
    }
    
    /**
     * metoda pro vykresleni vsech Node v implementaci
     */
    private void addWaterNodes(){
        try
        {
            int i = 0;
            int numberOfReservoirs = 0;
            int numberOfNodes = 0;
            while(i<allNodes.length)
            {
                if(allNodes[i].getClass().equals(Reservoir.class))
                {
                    numberOfReservoirs++;
                }
                else
                {
                    numberOfNodes++;
                }
                i++;
            }
            if((numberOfNodes-numberOfReservoirs)>0)
            {
               nodes = new Circle[numberOfNodes-numberOfReservoirs]; 
            }
            reservoirs = new Rectangle[numberOfReservoirs];
            nodes = new Circle[numberOfNodes];
            i = 0;
            int reservoirNumber = 0;
            RightInfoBarController.prepareLabelField(reservoirs.length);
            while(i<allNodes.length)
            {
                if(allNodes[i].getClass().equals(Reservoir.class))
                {
                    //kod pro rectangle
                    Rectangle newReservoir = new Rectangle();
                    reservoirs[reservoirNumber] = newReservoir;
                    reservoirNumber++;
                    Reservoir drawed = (Reservoir) allNodes[i];
                    double resize = drawed.capacity/biggestActiveReservoir.capacity;
                    //Do repositionToCenter() musim pridat korekci pro resize*glyphSize/2
                    newReservoir.setLayoutX((allNodes[i].position.getX()*displayMultiplier)+xMove-((resize*glyphSize))/2);
                    newReservoir.setLayoutY((allNodes[i].position.getY()*displayMultiplier)+yMove-((resize*glyphSize))/2);
                    newReservoir.setWidth((resize)*glyphSize);
                    newReservoir.setHeight((resize)*glyphSize);
                    newReservoir.setStroke(Color.BLACK);
                    newReservoir.setId(Integer.toString(id));
                    WaternetData wnd = new WaternetData(newReservoir);
                    reservoirGraphData.add(wnd);
                    newReservoir.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            Rectangle sceneClicked = (Rectangle) event.getSource();
                            NetworkNode[] allNodes = wn.getAllNetworkNodes();
                            Reservoir clicked = (Reservoir) allNodes[Integer.parseInt(sceneClicked.getId())-1];
                            StatusBarController.setInfo("myID Objektu: "+Integer.toString(clicked.myID), "X objektu: "+Double.toString(clicked.position.getX()),
                                                        "Y objektu: "+Double.toString(clicked.position.getY()));
                            RightInfoBarController.setGraphData(reservoirGraphData.get(clicked.myID-1), Integer.toString(clicked.myID), Reservoir.class.getName());
                            if(event.getClickCount()>1)
                            {
                                Node newNode = guiLoader.loadFXML("InfoWindow.fxml");
                                Parent root = (Parent) newNode;
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.initModality(Modality.APPLICATION_MODAL);
                                InfoWindowController.thisStage = stage;
                                InfoWindowController.setValues(Double.toString(clicked.position.getX()), 
                                                                Double.toString(clicked.position.getY()), Integer.toString(clicked.myID),
                                                                formatter.format(wn.currentSimulationTime())+"s", Double.toString(clicked.capacity),
                                                                Double.toString(contentInPercent(clicked)), formatter.format(clicked.content));
                                stage.setResizable(false);
                                InfoWindowController.percentGraph.setTitle("Rezervoar ");
                                InfoWindowController.setPercentGraphData(reservoirGraphData.get(clicked.myID-1), Integer.toString(clicked.myID), Reservoir.class.getName());
                                InfoWindowController.metersGraph.setTitle("Rezervoar ");
                                InfoWindowController.setMetersGraphData(reservoirGraphData.get(clicked.myID-1), Integer.toString(clicked.myID), Reservoir.class.getName());
                                stage.showAndWait();
                            }
                        }
                    });
                    newReservoir.setFill(Color.WHITE);
                    waterScene.getChildren().add(newReservoir);
                    newReservoir.toFront();
                    allNodes[i].myID = id;
                    RightInfoBarController.addLabel("Obsah rezervoaru cislo "+id+" : "+formatter.format((drawed.content/drawed.capacity)*100)+"%", 10, 180+(30*reservoirNumber), 250, 17);
                    flashingThreads.add(new Flashing(newReservoir, drawed));
                    id++;
                }
                else
                {
                    Circle newNode = new Circle();
                    newNode.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            Circle sceneClicked = (Circle) event.getSource();
                            NetworkNode clicked = (NetworkNode) allNodes[Integer.parseInt(sceneClicked.getId())-1];
                            StatusBarController.setInfo("myID Objektu: "+Integer.toString(clicked.myID), "X objektu: "+Double.toString(clicked.position.getX()),
                                                        "Y objektu: "+Double.toString(clicked.position.getY()));
                            if(event.getClickCount()>1)
                            {
                                Node newNode = guiLoader.loadFXML("InfoWindow.fxml");
                                Parent root = (Parent) newNode;
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.initModality(Modality.APPLICATION_MODAL);
                                InfoWindowController.thisStage = stage;
                                /*InfoWindowController.setValues(Double.toString(clicked.position.getX()), 
                                                                Double.toString(clicked.position.getY()), Integer.toString(clicked.myID),
                                                                Double.toString(wn.currentSimulationTime()), "Nejaky parametr NetworkNode"
                                );*/
                                stage.setResizable(false);
                                stage.showAndWait();
                            }
                        }
                    });
                    newNode.setLayoutX((allNodes[i].position.getX()*displayMultiplier)+xMove);
                    newNode.setLayoutY((allNodes[i].position.getY()*displayMultiplier)+yMove);
                    newNode.setRadius(35);
                    newNode.setStroke(Color.DARKSLATEGRAY);
                    Stop[] stops = new Stop[] { new Stop(0.5, Color.WHITE), new Stop(0.5, rgb(116,204,244))};
                    LinearGradient linearGrad = LinearGradientBuilder.create()
                    .startX(newNode.getCenterX())
                    .startY(newNode.getCenterY()-newNode.getRadius())
                    .endX(newNode.getCenterX())
                    .endY(newNode.getCenterY()+newNode.getRadius())
                    .proportional(false)
                    .cycleMethod(CycleMethod.NO_CYCLE)
                    .stops(stops)
                    .build();
                    newNode.setFill(linearGrad);
                    newNode.setId(Integer.toString(id));
                    nodes[(id-numberOfReservoirs)-1] = newNode;
                    waterScene.getChildren().add(newNode);
                    allNodes[i].myID = id;
                    id++;
                }
                i++;
            }
        }
        catch(Exception ex)
        {
            System.out.println("255 @ DisplayWaterNet" + ex);
            ex.printStackTrace();
            Scene messageScene = new Scene((Parent) guiLoader.loadFXML("MessageAlert.fxml"));
            Stage messageStage = new Stage();
            messageStage.setScene(messageScene);
            MessageAlertController.setMessageText("Chyba v displayWaterNet() "+ex);
            MessageAlertController.MESSAGE_ALERT_STAGE = messageStage;
            messageStage.initModality(Modality.APPLICATION_MODAL);
            messageStage.showAndWait();
            //nastavit stage pro kliknuti tlacitka
        }
    }
    
    /**
     * 
     * @return Vrati array reprezentaci vsech objektu Rectangle, ktere
     * predstavuji rezervoary v graficke reprezentai WaterNetwork
     */
    public Rectangle[] getReservoirs(){
        return this.reservoirs;
    }
    
    /**
     * 
     * @param rec Objekt Rectangle, ktery reprezentuje rezervoat v 
     * graficke reprezentaci
     * @return Vrati NetworkNode sparovany s odpovidajicim objektem Rectangle
     * ktery reprezentuje NetworkNode v graficke reprezentaci
     */
    public NetworkNode getLogicalreservoir(Rectangle rec){
        return allNodes[Integer.parseInt(rec.getId())-1];
    }
    
    /**
     * Metoda najde a nastavi nejvetsi rezervoar
     */
    private void findBiggestReservoir(){
        int i = 0;
        Reservoir biggest = null;
        while(i<allNodes.length)
        {
            if(allNodes[i].getClass().equals(Reservoir.class))
            {
                if(biggest == null)
                {
                    biggest = (Reservoir) allNodes[i];
                }
                else
                {
                    Reservoir tested = (Reservoir) allNodes[i];
                    if(biggest.capacity<tested.capacity)
                    {
                        biggest = tested;
                    }
                }
            }
            i++;
        }
        biggestActiveReservoir = biggest;
    }
    
    /**
     * Metoda najde a nstavi nejvetsi trubku
     */
    private void findBiggestPipe(){
        Pipe biggest = null;
        int i = 0;
        while(i<allPipes.length)
        {
            if(biggest==null)
            {
                biggest = allPipes[i];
            }
            else
            {
                if(biggest.crossSection<allPipes[i].crossSection)
                {
                    biggest = allPipes[i];
                }
            }
            i++;
        }
        biggestActivePipe = biggest;
    }
    
    /**
     * Zrychleni simulace
     */
    public void fastenUpSimulation(){
        wn.runFast();
    }
    
    /**
     * zpomaleni simulace
     */
    public void slowSimulation(){
        wn.runNormal();
    }
    
    /**
     * 
     * @return Vrati aktualni simulacni cas
     */
    public double getSimulationTime(){
        return wn.currentSimulationTime();
    }
}
