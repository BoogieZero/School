package gui;

import java.net.URL;
import java.util.ResourceBundle;
import function.TimeHandler;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import objects.Factory;
import objects.Planet;
import objects.Route;
import objects.Ship;
import objects.TimeStamp;
import objects.draw.RouteShape;

/**
 * Class controller
 * This class set up all actions in our window application.
 * 
 * @author Jiří Lukáš
 *
 */
public class Controller implements Initializable{
	
	/**
	 * actual ship from table Ship on Planet card
	 */
	private Ship selectedShip = null;
	
	/**
	 * actual ship from table Ship on Factory card
	 */
	private Ship selectedFactoryShip = null;
	
	/**
	 * Million for shortest values
	 */
	private final int MILION = 1000000;
	
	/**
	 * Console String for visualization card
	 */
	private String console = "";
	
	/**
	 * Image plane for First and visualization card
	 */
	private ImgPlane imagePlane = new ImgPlane();
		
//--------------------------------For all cards -----------------------------------------
	
	@FXML
	TabPane tabPanelTP;
	
	@FXML
	Tab mainPageTB;
	
	@FXML
	Tab visualizationPageTB;
	
	@FXML
	Tab planetPageTB;
	
	@FXML
	Tab factoryPageTB;
	
	@FXML
	Tab statisticPageTB;
	
	@FXML
	Text populationAndOrdersVcardT;
	
	@FXML
	Text populationAndOrdersPcardT;
	
	@FXML
	Text populationAndOrdersFcardT;
	
	@FXML
	Text timeOfSimulationVcardT;
	
	@FXML
	Text timeOfSimulationPcardT;
	
	@FXML
	Text timeOfSimulationFcardT;
//---------------------------------------------------------------------------------------
//------------------------------- First Card --------------------------------------------
	@FXML
	BorderPane firstPageBP;
	
	@FXML
	TextArea firstCardInfo;
//---------------------------------------------------------------------------------------		
//------------------------------- Image plane card---------------------------------------

	@FXML
	BorderPane mapa;
	
	@FXML
	Slider zoomSlider;
	
	@FXML
	Button nextHourBT;
	
	@FXML
	Button nextDayBT;
	
	@FXML
	Button nextMonthBT;
	
	@FXML
	CheckBox fullReportCB;
	
//----------------------------------------------------------------------------------------	
//------------------------------ Information card-----------------------------------------
	
	@FXML
	TextArea infoPlanetTA;
	
	@FXML
	TextArea consoleTA;
	
	@FXML
	TextField setOrdersTF;
//------------------------------ Table for ships------------------------------------------
	/**
	 * Model for table ships in Information card
	 */
	private final ObservableList<Ship> tableShipModel = FXCollections.observableArrayList();
		
	@FXML
	TableView<Ship> tableShips;
		
	@FXML
	TableColumn<Ship, String> shipName;
		
	@FXML
	TableColumn<Ship, String> shipStartTime;
		
	@FXML
	TableColumn<Ship, String> shipArrivalTime;
		
	@FXML
	TableColumn<Ship, String> shipReturnTime;
		
	@FXML
	TableColumn<Ship, String> shipProgress;
		
	@FXML
	TableColumn<Ship, Number> shipLoad;
		
//---------------------------------Table View for route--------------------------------------
	/**
	 * Model for table route in Information card
	 */
	private final ObservableList<TimeStamp> tableRouteModel = FXCollections.observableArrayList();
		
	@FXML
	TableView<TimeStamp> tableRoute;
		
	@FXML
	TableColumn<TimeStamp, String> routeNameTC;
		
	@FXML
	TableColumn<TimeStamp, String> routeTimeTC;

	@FXML
	TableColumn<TimeStamp, String> routeActionTC;
		
	@FXML
	TableColumn<TimeStamp, String> routeAmountTC;
		
	@FXML
	RadioButton routeToTargetCB;
		
	@FXML
	RadioButton routeToHomeCB;
		
	@FXML
	RadioButton routeActionCB;
		
//-----------------------------------------------------------------------------------------
//--------------------------------- Factory Card ------------------------------------------
	
	@FXML
	TextArea infoFactoryTA;
		
//------------------------------ Table for ships------------------------------------------
	/**
	 * Model for table ships in Factory card
	 */
	private final ObservableList<Ship> factoryTableShipModel = FXCollections.observableArrayList();
		
	@FXML
	TableView<Ship> factoryTableShips;
		
	@FXML
	TableColumn<Ship, String> factoryShipName;
		
	@FXML
	TableColumn<Ship, String> factoryShipStartTime;
			
	@FXML
	TableColumn<Ship, String> factoryShipReturnTime;
		
	@FXML
	TableColumn<Ship, String> factoryShipProgress;
		
	@FXML
	TableColumn<Ship, Number> factoryShipLoad;
		
//---------------------------------Table View for route--------------------------------------
	/**
	 * Model for table route in Factory card
	 */
	private final ObservableList<TimeStamp> factoryTableRouteModel = FXCollections.observableArrayList();
		
	@FXML
	TableView<TimeStamp> factoryTableRoute;
		
	@FXML
	TableColumn<TimeStamp, String> factoryRouteNameTC;
		
	@FXML
	TableColumn<TimeStamp, String> factoryRouteTimeTC;

	@FXML
	TableColumn<TimeStamp, String> factoryRouteActionTC;
		
	@FXML
	TableColumn<TimeStamp, String> factoryRouteAmountTC;
		
	@FXML
	RadioButton factoryRouteToTargetCB;
		
	@FXML
	RadioButton factoryRouteToHomeCB;
		
	@FXML
	RadioButton factoryRouteActionCB;

//---------------------------------Table View for All routes-------------------------------
	/**
	 * Model for table of all routes in Factory card
	 */
	private final ObservableList<Route> factoryTableAllRoutesModel = FXCollections.observableArrayList();
	
	@FXML
	TableView<Route> factoryTableAllRoutes;
	
	@FXML
	TableColumn<Route, String> factoryRoutesPrimaryTargetTC;
	
	@FXML
	TableColumn<Route, String> factoryRoutesStartTimeTC;
	
	@FXML
	TableColumn<Route, String> factoryRoutesReturnTimeTC;
	
//-----------------------------------------------------------------------------------------
//--------------------------------- Statistics card ---------------------------------------
	@FXML
	TextArea staticticsTA;
//-----------------------------------------------------------------------------------------
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		actionTimeOfSimulation();
		actionSetPopulationAndOrders();
		consoleTA.setText(console);
		
		setZoomSlider();
		
		firstPageBP.setCenter(imagePlane);
		
		setShipTable();
		setRouteTableView();
		setRadioButtons();
		
		setShipTableFactory();
		setRouteTableViewFactory();
		setAllRoutesTableViewFactory();
		setRadioButtonsFactory();
		
	}
	
//-------------------------------------- Set and Action for first card---------------------
	@FXML
	private void actionFireStartProgramBT(){
		 mainPageTB.setDisable(true);
		 visualizationPageTB.setDisable(false);
		 planetPageTB.setDisable(false);
		 factoryPageTB.setDisable(false);
		 statisticPageTB.setDisable(false);
		 mapa.setCenter(imagePlane);
		 TimeHandler.startSimulation();
		 tabPanelTP.getSelectionModel().selectNext();
	}
	
	@FXML
	private void actionFireGenerateNewMapBT(){
		TimeHandler.generateNewMapActions();
		ImgPlane imagePlaneNew = new ImgPlane();
		imagePlane = imagePlaneNew;
		firstPageBP.setCenter(imagePlaneNew);
		firstCardInfo.clear();
		firstCardInfo.setText(TimeHandler.getLoader().getPopulationInfo());
	}
//-----------------------------------------------------------------------------------------
//--------------------------------------set and Action for information card----------------	
	/**
	 * Settings for zoomSlider
	 */
	private void setZoomSlider(){
		zoomSlider.setMin(ImgPlane.DEFALUT_SCALE_MIN);
		zoomSlider.setMax(ImgPlane.DEFALUT_SCALE_MAX);
		
		zoomSlider.setSnapToTicks(true);
		zoomSlider.setMajorTickUnit(1);
		zoomSlider.setBlockIncrement(0.1);
		zoomSlider.showTickMarksProperty().set(true);
		zoomSlider.showTickLabelsProperty().set(true);
		imagePlane.BindToImgScale(zoomSlider.valueProperty());
		TimeHandler.BindToFullReport(fullReportCB.selectedProperty());
	}
	/**
	 * Settings for Ship table on planet card
	 */
	private void setShipTable(){
		
		setColumnShipTablePlanet();
		
		tableShips.setItems(tableShipModel);
		tableShips.setEditable(false);
		tableShips.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		tableShips.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ship>() {

			@Override
			public void changed(ObservableValue<? extends Ship> observable, Ship oldValue, Ship newValue) {
				if(newValue != null){
					actionShipRowSelected();
				}
			}
		});	
	}
	
	/**
	 * Settings for ship table in planet card (Columns)
	 */
	private void setColumnShipTablePlanet(){
		
		shipName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		shipStartTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ship,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Ship, String> param) {
				return new SimpleStringProperty(param.getValue().getRoute().getStartTime().toString()) ;
			}
		});
		
		shipArrivalTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ship,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Ship, String> param) {
				if(param.getValue().getRoute().getActions().containsKey(ImgPlane.getSelectedPlanet())){
					return new SimpleStringProperty(param.getValue().getRoute().getActions().get(ImgPlane.getSelectedPlanet()).getTime().toString());
				}
				return null;
			}
		});
		
		shipReturnTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ship,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Ship, String> param) {
				return new SimpleStringProperty(param.getValue().getRoute().getReturnTime().toString());
			}
		});
		
		shipProgress.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ship,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Ship, String> param) {
				
				double startTime = param.getValue().getRoute().getStartTime().getValue();
				double returnTime = param.getValue().getRoute().getReturnTime().getValue();
				double actualTime = TimeHandler.getActualTime().getValue();
				
				int result =(int) Math.round(((actualTime-startTime)/(returnTime-startTime))*100);
				
				String resultToString = "";
				
				if(result > 100){
					resultToString = "100%";
				}else{
					resultToString = Integer.toString(result) +"%";
				}
			
				return new SimpleStringProperty(resultToString);
			}
		});
			
		shipLoad.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ship,Number>, ObservableValue<Number>>() {
			@Override
			public ObservableValue<Number> call(CellDataFeatures<Ship, Number> param) {
				Planet selectedPlanet = ImgPlane.getSelectedPlanet();
				int amount = param.getValue().getRoute().getActions().get(selectedPlanet).getAmount();
				return new SimpleIntegerProperty(amount);
			}
		});																	
	
	}
	
	/**
	 * Settings for route table on planet card
	 */
	private void setRouteTableView(){
		
		routeNameTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TimeStamp,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<TimeStamp, String> param) {
				return new SimpleStringProperty (param.getValue().getObject().getName());
			}
		});
		
		routeTimeTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TimeStamp,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<TimeStamp, String> param) {
				return new SimpleStringProperty(param.getValue().getTime().toString());
			}
		});
		
		routeActionTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TimeStamp,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<TimeStamp, String> param) {
				return new SimpleStringProperty(param.getValue().getAction().toString());
			}
		});
		
		routeAmountTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TimeStamp,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<TimeStamp, String> param) {
				return new SimpleStringProperty(Integer.toString(param.getValue().getAmount()));
			}
		});
		
		tableRoute.setItems(tableRouteModel);
		tableRoute.setEditable(false);
		tableRoute.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	/**
	 * Settings for Radio buttons
	 */
	private void setRadioButtons(){
		ToggleGroup group = new ToggleGroup();
		routeToTargetCB.setToggleGroup(group);
		routeToHomeCB.setToggleGroup(group);
		routeActionCB.setToggleGroup(group);
	}
	
	@FXML
	private void actionCardWasSelected(){
		Planet selected = ImgPlane.getSelectedPlanet();
		String planetInfo = "";
		String planetNeigh = "";
		
		if(selected != null){
			
			for(String pn: selected.getNeighbours().keySet()){
				boolean isPirates = selected.getNeighbours().get(pn).isDanger();
				if(isPirates){
					planetNeigh += pn +"       DANGER" + "\n";
				}else{
					planetNeigh += pn + "\n";
				}
				
			}
		
			planetInfo = selected.getName()+"\n"
						+"X = "+(int)selected.getX()+" Y ="+ (int)selected.getY()+"\n"
						+"Population: "+selected.getPopulation()+"\n"
						+"Orders: "+selected.getOrder()+"\n"
						+"Neighbours:"+"\n"+planetNeigh;
			
			tableShipModel.clear();
			tableShipModel.addAll(selected.getShipList());
			infoPlanetTA.setText(planetInfo);
			
			tableShips.setDisable(false);
			tableRoute.setDisable(false);
			
			routeToTargetCB.setDisable(true);
			routeToHomeCB.setDisable(true);
			routeActionCB.setDisable(true);
		}
		
		if(selectedShip != null){
			if(tableShipModel.contains(selectedShip)){
				
				tableShips.getSelectionModel().select(selectedShip);
				actionShipRowSelected();
				
				routeToTargetCB.setDisable(false);
				routeToHomeCB.setDisable(false);
				routeActionCB.setDisable(false);
			}else{
				tableRouteModel.clear();
			}
			
		}
	}
	
	@FXML
	private void actionShipRowSelected(){
			ImgPlane.highlightRoute(tableShips.getSelectionModel().getSelectedItem().getRoute(), RouteShape.HighlightType.BOTH_ROUTES);
	
		if(routeToTargetCB.isSelected()){
			actionArmRouteToTarget();
		}
		if(routeToHomeCB.isSelected()){
			actionArmRouteToHome();
		}
		if(routeActionCB.isSelected()){
			actionArmRouteAction();
		}
		
		routeToTargetCB.setDisable(false);
		routeToHomeCB.setDisable(false);
		routeActionCB.setDisable(false);
		
		selectedShip = tableShips.getSelectionModel().getSelectedItem();
	}
	
	@FXML
	private void actionArmRouteToTarget(){
		try{
			tableRouteModel.clear();
			tableRouteModel.addAll(tableShips.getSelectionModel().getSelectedItem().getRoute().getRouteToTarget());
			ImgPlane.setHighlightRouteType(RouteShape.HighlightType.ROUTE_TO_TARGET);
		}catch(Exception e){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Selection WARNING");
			alert.setContentText("Choose ship");
			alert.showAndWait();
		}
		
	}
	
	@FXML
	private void actionArmRouteToHome(){
		try{
			tableRouteModel.clear();
			tableRouteModel.addAll(tableShips.getSelectionModel().getSelectedItem().getRoute().getRouteToHome());
			ImgPlane.setHighlightRouteType(RouteShape.HighlightType.ROUTE_TO_HOME);
		}catch(Exception e){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Selection WARNING");
			alert.setContentText("Choose ship");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void actionArmRouteAction(){
		try{
			tableRouteModel.clear();
			
			ObservableList<TimeStamp> newModel = FXCollections.observableArrayList();
			newModel.addAll(tableShips.getSelectionModel().getSelectedItem().getRoute().getActions().values());
			FXCollections.sort(newModel);

			tableRouteModel.addAll(newModel);
			ImgPlane.setHighlightRouteType(RouteShape.HighlightType.BOTH_ROUTES);
		}catch(Exception e){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Selection WARNING");
			alert.setContentText("Choose ship");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void actionFireGoToSelectedPlanetBT(){
		try{
			ImgPlane.setSelected(tableRoute.getSelectionModel().getSelectedItem().getObject());
			
			if(tableRoute.getSelectionModel().getSelectedItem().getObject() instanceof Factory){
				tabPanelTP.getSelectionModel().select(factoryPageTB);
				actionCardWasSelectedFactory();
			}else{
				actionCardWasSelected();
			}
			
		}catch(Exception e){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Selection WARNING");
			alert.setContentText("Choose route");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void actionFireSetOrderBT(){
		try{
			int order = Integer.parseInt(setOrdersTF.getText());
			ImgPlane.getSelectedPlanet().setManualOrder(order);
			setOrdersTF.clear();
			
		}catch(Exception e){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Setting WARNING");
			alert.setContentText("Orther is not a number");
			alert.showAndWait();
		}
	}
		
//--------------------------------------set and Action for visualization card---------------------	
	@FXML
	private void actionFireNextHourBT(){
		TimeHandler.nextHour();
		String addToConsole = TimeHandler.getReport();
		if(addToConsole != null){
			console += addToConsole+"\n";
			consoleTA.setText(console);
			consoleTA.selectPositionCaret(consoleTA.getLength());
			consoleTA.deselect();
		}
		actionSetPopulationAndOrders();
		actionTimeOfSimulation();
	}
	
	@FXML
	private void actionFireNextDayBT(){
		TimeHandler.nextDay();
		String addToConsole = TimeHandler.getReport();
		if(addToConsole != null){
			console += addToConsole+"\n";
			consoleTA.setText(console);
			consoleTA.selectPositionCaret(consoleTA.getLength());
			consoleTA.deselect();
		}
		actionSetPopulationAndOrders();
		actionTimeOfSimulation();
	}
	
	@FXML
	private void actionFireNextMonthBT(){
		TimeHandler.nextMonth();
		String addToConsole = TimeHandler.getReport();
		if(addToConsole != null){
			console += addToConsole+"\n";
			consoleTA.setText(console);
			consoleTA.selectPositionCaret(consoleTA.getLength());
			consoleTA.deselect();
		}
		actionSetPopulationAndOrders();
		actionTimeOfSimulation();
	}
		
//------------------------------------------------------------------------------------------------
	
//--------------------------------------set and Action for factory card---------------------------
		
	/**
	 * Settings for Ship table on factory card
	 */
	private void setShipTableFactory(){
			
		setColumnsShipTableFactory();
			
		factoryTableShips.setItems(factoryTableShipModel);
		factoryTableShips.setEditable(false);
		factoryTableShips.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			
		factoryTableShips.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ship>() {
			@Override
			public void changed(ObservableValue<? extends Ship> observable, Ship oldValue, Ship newValue) {
				if(newValue != null){
					actionShipRowSelectedFactory();
				}
			}
		});
			
			
		}
	
	/**
	 * Setting for Columns on card Factory for table ships
	 */
	private void setColumnsShipTableFactory(){
		factoryShipName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		factoryShipStartTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ship,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Ship, String> param) {
				if(param.getValue().getRoute() == null){
					return new SimpleStringProperty("Docked");	
				}
				return new SimpleStringProperty(param.getValue().getRoute().getStartTime().toString()) ;
			}
		});
						
		factoryShipReturnTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ship,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Ship, String> param) {
				if(param.getValue().getRoute() == null){
					return new SimpleStringProperty("Docked");
				}
				return new SimpleStringProperty(param.getValue().getRoute().getReturnTime().toString());
			}
		});
			
		factoryShipProgress.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ship,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Ship, String> param) {	
				if(param.getValue().getRoute() == null){
					return new SimpleStringProperty("Docked");
				}
				double startTime = param.getValue().getRoute().getStartTime().getValue();
				double returnTime = param.getValue().getRoute().getReturnTime().getValue();
				double actualTime = TimeHandler.getActualTime().getValue();
	
				int result =(int) Math.round(((actualTime-startTime)/(returnTime-startTime))*100);
					
				String resultToString = "";
				
				if(result > 100){
					resultToString = "100%";
				}else{
					resultToString = Integer.toString(result) +"%";
				}
				
				return new SimpleStringProperty(resultToString);
			}
		});
				
		factoryShipLoad.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ship,Number>, ObservableValue<Number>>() {
			@Override
			public ObservableValue<Number> call(CellDataFeatures<Ship, Number> param) {
				int actualLoad = param.getValue().getLoad();
				return new SimpleIntegerProperty(actualLoad);
			}
		});																		
	}
	
	/**
	 * Settings for Route table on factory card
	 */	
	private void setRouteTableViewFactory(){
			
		factoryRouteNameTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TimeStamp,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<TimeStamp, String> param) {
				return new SimpleStringProperty (param.getValue().getObject().getName());
			}
		});
			
		factoryRouteTimeTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TimeStamp,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<TimeStamp, String> param) {
				return new SimpleStringProperty(param.getValue().getTime().toString());
			}
		});
			
		factoryRouteActionTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TimeStamp,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<TimeStamp, String> param) {
				return new SimpleStringProperty(param.getValue().getAction().toString());
			}
		});
			
		factoryRouteAmountTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TimeStamp,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<TimeStamp, String> param) {
				return new SimpleStringProperty(Integer.toString(param.getValue().getAmount()));
			}
		});
			
		factoryTableRoute.setItems(factoryTableRouteModel);
		factoryTableRoute.setEditable(false);
		factoryTableRoute.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	/**
	 * Settings for All route table on factory card
	 */
	private void setAllRoutesTableViewFactory(){
		
		factoryRoutesPrimaryTargetTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Route,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Route, String> param) {
				return new SimpleStringProperty (param.getValue().getTargetPoint().getObject().getName());
			}
		});
		
		factoryRoutesStartTimeTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Route,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Route, String> param) {
				return new SimpleStringProperty(param.getValue().getStartPoint().getTime().toString());
			}
			
		});
		
		factoryRoutesReturnTimeTC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Route,String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Route, String> param) {
				return new SimpleStringProperty(param.getValue().getReturnTime().toString());
			}
			
		});
		
		factoryTableAllRoutes.setItems(factoryTableAllRoutesModel);
		factoryTableAllRoutes.setEditable(false);
		factoryTableAllRoutes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		factoryTableAllRoutes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Route>() {

			@Override
			public void changed(ObservableValue<? extends Route> observable, Route oldValue, Route newValue) {
				if(newValue != null){
					actionRouteWasSelected();
				}
			}
			
		});
		
	}
		
	/**
	 * Settings for radio buttons on factory card
	 */
	private void setRadioButtonsFactory(){
		ToggleGroup group = new ToggleGroup();
		factoryRouteToTargetCB.setToggleGroup(group);
		factoryRouteToHomeCB.setToggleGroup(group);
		factoryRouteActionCB.setToggleGroup(group);
	}
		
	@FXML
	private void actionCardWasSelectedFactory(){
		Factory selected = ImgPlane.getSelectedFactory();
		String factoryInfo = "";
		String factoryNeigh = "";
			
		if(selected != null){
				
			for(String pn: selected.getNeighbours().keySet()){
				boolean isPirates = selected.getNeighbours().get(pn).isDanger();
				if(isPirates){
					factoryNeigh += pn +"       DANGER" + "\n";
				}else{
					factoryNeigh += pn + "\n";
				}
					
			}
			
				factoryInfo = selected.getName()+"\n"
							+"X = "+(int)selected.getX()+" Y ="+ (int)selected.getY()+"\n"
							+"Material sent this month: " + selected.getMaterialMadeMonth() +"\n"
							+"Neighbours:"+"\n"+factoryNeigh;
				
			factoryTableShipModel.clear();
			factoryTableShipModel.addAll(selected.getShipList());
			infoFactoryTA.setText(factoryInfo);
				
			factoryTableShips.setDisable(false);
			factoryTableRoute.setDisable(false);
				
			factoryRouteToTargetCB.setDisable(true);
			factoryRouteToHomeCB.setDisable(true);
			factoryRouteActionCB.setDisable(true);
		}
			
		if(selectedFactoryShip != null){
			if(factoryTableShipModel.contains(selectedFactoryShip)){
					
				factoryTableShips.getSelectionModel().select(selectedFactoryShip);
				actionShipRowSelectedFactory();
					
				factoryRouteToTargetCB.setDisable(false);
				factoryRouteToHomeCB.setDisable(false);
				factoryRouteActionCB.setDisable(false);
			}else{
				factoryTableRouteModel.clear();
			}
				
		}
	}
		
	@FXML
	private void actionShipRowSelectedFactory(){
		
		Ship selectedShip = factoryTableShips.getSelectionModel().getSelectedItem();
		
		factoryTableAllRoutes.getItems().clear();
		factoryTableAllRoutes.getItems().addAll(selectedShip.getRoutes());
		factoryTableRoute.getItems().clear();
		
		factoryTableAllRoutes.setDisable(false);

	}
	
	/**
	 * Settings action if route was selected on factory card
	 */
	private void actionRouteWasSelected(){
		ImgPlane.highlightRoute(factoryTableAllRoutes.getSelectionModel().getSelectedItem(), RouteShape.HighlightType.BOTH_ROUTES);
		
	if(factoryRouteToTargetCB.isSelected()){
		actionArmRouteToTargetFactory();
	}
	if(factoryRouteToHomeCB.isSelected()){
		actionArmRouteToHomeFactory();
	}
	if(factoryRouteActionCB.isSelected()){
		actionArmRouteActionFactory();
	}
		
	factoryRouteToTargetCB.setDisable(false);
	factoryRouteToHomeCB.setDisable(false);
	factoryRouteActionCB.setDisable(false);
		
	selectedFactoryShip = factoryTableShips.getSelectionModel().getSelectedItem();
	}
		
	@FXML
	private void actionArmRouteToTargetFactory(){
		try{
			Route routeToTarget = factoryTableAllRoutes.getSelectionModel().getSelectedItem();
			factoryTableRouteModel.clear();
			factoryTableRouteModel.addAll(routeToTarget.getRouteToTarget());
			ImgPlane.setHighlightRouteType(RouteShape.HighlightType.ROUTE_TO_TARGET);
		}catch(Exception e){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Selection WARNING");
			alert.setContentText("Choose route");
			alert.showAndWait();
		}
			
	}
		
	@FXML
	private void actionArmRouteToHomeFactory(){
		try{
			Route routeToHome = factoryTableAllRoutes.getSelectionModel().getSelectedItem();
			factoryTableRouteModel.clear();
			factoryTableRouteModel.addAll(routeToHome.getRouteToHome());
			ImgPlane.setHighlightRouteType(RouteShape.HighlightType.ROUTE_TO_HOME);
		}catch(Exception e){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Selection WARNING");
			alert.setContentText("Choose ship");
			alert.showAndWait();
		}
	}
		
	@FXML
	private void actionArmRouteActionFactory(){
		try{
			Route routeAction = factoryTableAllRoutes.getSelectionModel().getSelectedItem();
			factoryTableRouteModel.clear();
				
			ObservableList<TimeStamp> newModel = FXCollections.observableArrayList();
			newModel.addAll(routeAction.getActions().values());
			FXCollections.sort(newModel);

			factoryTableRouteModel.addAll(newModel);
			ImgPlane.setHighlightRouteType(RouteShape.HighlightType.BOTH_ROUTES);
		}catch(Exception e){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Selection WARNING");
			alert.setContentText("Choose ship");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void actionFireGoToSelectedFactoryBT(){
		try{
			ImgPlane.setSelected(factoryTableRoute.getSelectionModel().getSelectedItem().getObject());
			
			if(factoryTableRoute.getSelectionModel().getSelectedItem().getObject() instanceof Planet){
				tabPanelTP.getSelectionModel().select(planetPageTB);
				actionCardWasSelected();
			}else{
				actionCardWasSelectedFactory();
			}
			
		}catch(Exception e){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Selection WARNING");
			alert.setContentText("Choose route");
			alert.showAndWait();
		}
	}
	
//-------------------------------------------------------------------------------------------------
//------------------------------------ Set and Action Statistics Card------------------------------
	@FXML
	private void statisticCardWasSelected(){
		
		long shipsCreated = 0;
		for(Factory factory : TimeHandler.getLoader().getFactoryMap().values()){
			shipsCreated += factory.getShipsCreated();
		}
		
		String print = "Time of simulation: " + TimeHandler.getActualTime().toString()+"\n"
					   + "Planets"+"\n"
					   + "Ordered: "
					   + (TimeHandler.getOrders().getOrderedAll()/1000000) + "M " +"\n"
					   + "Population died: "
					   + TimeHandler.getOrders().getPopulationDiedMonth() + "\n"
					   + "Ships"+ "\n"
					   + "Ships created: "+shipsCreated +"\n"
					   + TimeHandler.getDecayReport();
		staticticsTA.setText(print);
		
	}
//-------------------------------------------------------------------------------------------------
	
	/**
	 * Settings for text with time of simulation 
	 */
	private void actionTimeOfSimulation(){
		String actualTime = "Time of simulation: "
				+ (int)TimeHandler.getActualTime().getHour()+ " Hour "
				+ TimeHandler.getActualTime().getDay()+" Day "
				+ TimeHandler.getActualTime().getMonth()+" Month";
		timeOfSimulationVcardT.setText(actualTime);
		timeOfSimulationPcardT.setText(actualTime);
		timeOfSimulationFcardT.setText(actualTime);
	}
	
	/**
	 * Settings for text area in statistics card
	 */
	private void actionSetPopulationAndOrders(){
		long  orders = TimeHandler.getOrders().getOrderedMonth()/MILION;
		long  population = TimeHandler.getOrders().getPopulationMonth()/MILION;
		
		String actualTime = "Population for this month: "
				+ (int) population +" M"
				+ " Orders for this month: "
				+ (int) orders + " M";
		populationAndOrdersVcardT.setText(actualTime);
		populationAndOrdersPcardT.setText(actualTime);
		populationAndOrdersFcardT.setText(actualTime);
	}
	
	@FXML
	private void actionExitProgram(){
		Platform.exit();
	}
}
