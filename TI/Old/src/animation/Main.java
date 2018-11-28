package animation;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{
	private static final int MIN_HEIGHT 		= 600;
	private static final int MIN_WIDTH 			= 700;
	
	private static final int FLOOR_HEIGHT 		= 75;
	private static final int ELEVATOR_HEIGHT 	= 50;
	private static final int ELEVATOR_WIDTH		= 40;
	private static final int FLOOR_COUNT		= 6;
	
	private static final int FLOOR_TRAVEL_TIME 	= 1000;	//ms
	
	private static double TIME_SPEED_MULTIPLYER = 1.5;
	
	private static boolean animationInProgress	= false;
	
	private static Elevator elevator;
	private static Line ropes;
	
	public static IntegerProperty[] waitingPassengers = new  IntegerProperty[FLOOR_COUNT];
	
	public static void main(String[] args) {
		for(int i=0;i<waitingPassengers.length;i++){
			waitingPassengers[i] = new SimpleIntegerProperty(0);
		}
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Elevator");
		stage.setScene(getScene());
		stage.setMinHeight(MIN_HEIGHT);
		stage.setMinWidth(MIN_WIDTH);
		stage.show();
	}
	
	private Scene getScene(){
		return new Scene(getRoot());
	}
	
	private Parent getRoot() {
		BorderPane rootPane = new BorderPane();
		rootPane.setBackground(Background.EMPTY);
		rootPane.setCenter(getImagePanel());
		rootPane.setRight(getControllPanel());
		return rootPane;
	}

	
	private Node getCallElevator(){
		Group controll = new Group();
		
		VBox vbControllCall = new VBox();
		vbControllCall.setSpacing(7);
		
		Button btCall0 = new Button("Ground floor");
		Button btCall1 = new Button("First floor");
		Button btCall2 = new Button("Second floor");
		Button btCall3 = new Button("Third floor");
		Button btCall4 = new Button("Fouth floor");
		Button btCall5 = new Button("Fifth floor");
		
		btCall0.setMaxWidth(Double.MAX_VALUE);
		btCall1.setMaxWidth(Double.MAX_VALUE);
		btCall2.setMaxWidth(Double.MAX_VALUE);
		btCall3.setMaxWidth(Double.MAX_VALUE);
		btCall4.setMaxWidth(Double.MAX_VALUE);
		btCall5.setMaxWidth(Double.MAX_VALUE);
		
		
		
		// <TEST BUTTONS>
		Button bTestUp = new Button("TEST UP");
		Button bTestDown = new Button("TEST DOWN");
		
		bTestUp.setOnAction(new EventHandler<ActionEvent>() {
		
			@Override
			public void handle(ActionEvent arg0) {
				elevatorMoveUp();
			}
		});
		
		bTestDown.setOnAction(new EventHandler<ActionEvent>() {
		
			@Override
			public void handle(ActionEvent arg0) {
				elevatorMoveDown();
			}
		});
		
		Button bTestAddPass = new Button("TEST add pass");
		Button bTestRemPass = new Button("TEST Rem pass");
		
		bTestAddPass.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				boardElevator();
			}
		});
		
		bTestRemPass.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				elevator.removePasangers(1);
			}
		});
		
		// <\TEST BUTTONS>
		
		
		vbControllCall.getChildren().addAll(btCall5,
											btCall4,
											btCall3,
											btCall2,
											btCall1,
											btCall0);
		
		vbControllCall.getChildren().addAll(bTestUp, 
											bTestDown, 
											bTestAddPass, 
											bTestRemPass);
		
		GroupBox gb = new GroupBox("Call Elevator", vbControllCall);
		
		controll.getChildren().add(gb);
		
		btCall0.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int val = waitingPassengers[0].getValue();
				waitingPassengers[0].set(val+1);
				sendQueueRequest(0);
			}
		});
		
		btCall1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int val = waitingPassengers[1].getValue();
				waitingPassengers[1].set(val+1);
				sendQueueRequest(1);
			}
		});

		btCall2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int val = waitingPassengers[2].getValue();
				waitingPassengers[2].set(val+1);
				sendQueueRequest(2);
			}
		});
		
		btCall3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int val = waitingPassengers[3].getValue();
				waitingPassengers[3].set(val+1);
				sendQueueRequest(3);
			}
		});
		
		btCall4.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int val = waitingPassengers[4].getValue();
				waitingPassengers[4].set(val+1);
				sendQueueRequest(4);
			}
		});
		
		btCall5.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int val = waitingPassengers[5].getValue();
				waitingPassengers[5].set(val+1);
				sendQueueRequest(5);
			}
		});
		
		return controll;
	}
	
	private Node getInterior(){
		
		Button btCall0 = new Button("Ground floor");
		Button btCall1 = new Button("First floor");
		Button btCall2 = new Button("Second floor");
		Button btCall3 = new Button("Third floor");
		Button btCall4 = new Button("Fouth floor");
		Button btCall5 = new Button("Fifth floor");
		
		Button btEmergency = new Button("Emergency");
		
		btCall0.setMaxWidth(Double.MAX_VALUE);
		btCall1.setMaxWidth(Double.MAX_VALUE);
		btCall2.setMaxWidth(Double.MAX_VALUE);
		btCall3.setMaxWidth(Double.MAX_VALUE);
		btCall4.setMaxWidth(Double.MAX_VALUE);
		btCall5.setMaxWidth(Double.MAX_VALUE);
		btEmergency.setMaxWidth(Double.MAX_VALUE);
		
		VBox btInterior = new VBox();
		btInterior.setAlignment(Pos.TOP_CENTER);
		btInterior.setSpacing(7);
		
		HBox floorBox = new HBox();
		Text lbFloor = new Text("Floor: ");
		Text txFloor = new Text();
		
		txFloor.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		lbFloor.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		
		elevator.bindToFloor(txFloor.textProperty());
		
		floorBox.setAlignment(Pos.CENTER);
		floorBox.getChildren().addAll(	lbFloor,
										txFloor);	
		
		
		btInterior.getChildren().addAll(floorBox,
										btCall5,
										btCall4,
										btCall3,
										btCall2,
										btCall1,
										btCall0,
										btEmergency);
		
		StackPane sp = new StackPane();
		sp.getChildren().addAll(btInterior,
								btEmergency);
		
		StackPane.setAlignment(btCall0, Pos.TOP_CENTER);
		StackPane.setAlignment(btEmergency, Pos.BOTTOM_CENTER);
		
		GroupBox gb = new GroupBox("Interior", sp);
		
		btCall0.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				sendQueueRequest(0);
			}
		});
		
		btCall1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				sendQueueRequest(1);
			}
		});
		
		btCall2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				sendQueueRequest(2);
			}
		});
		
		btCall3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				sendQueueRequest(3);
			}
		});
		
		btCall4.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				sendQueueRequest(4);
			}
		});
		
		btCall5.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				sendQueueRequest(5);
			}
		});
		
		return gb;
	}
	
	private Node getControllPanel(){
		Group controll = new Group();
		HBox hbContr = new HBox();
		controll.getChildren().add(hbContr);
		
		hbContr.getChildren().addAll(	getInterior(),
										getCallElevator());
		
		
		
		return controll;
	}
	
	public static void elevatorMoveUp(){
		if(elevator.getFloor() >= FLOOR_COUNT-1 || animationInProgress == true){
			System.out.println("Consumed <Animation UP>");
			return;
		}
		elevator.up();
		animationInProgress = true;
		
		double actTransY = elevator.translateYProperty().getValue();
		KeyValue kvE = new KeyValue(elevator.translateYProperty(), actTransY - FLOOR_HEIGHT);
		KeyValue kvR = new KeyValue(ropes.endYProperty(), ropes.getEndY() - FLOOR_HEIGHT);
		elevatorAnimation(kvE, kvR);
	}
	
	private void sendQueueRequest(int floor){
		//queue add floor
	}
	
	public static void elevatorMoveDown(){
		if(elevator.getFloor() <= 0 || animationInProgress == true){
			System.out.println("Consumed <Animation DOWN>");
			return;
		}
		elevator.down();
		animationInProgress = true;
		
		double actTransY = elevator.translateYProperty().getValue();
		KeyValue kvE = new KeyValue(elevator.translateYProperty(), actTransY + FLOOR_HEIGHT);
		KeyValue kvR = new KeyValue(ropes.endYProperty(), ropes.getEndY() + FLOOR_HEIGHT);
		elevatorAnimation(kvE, kvR);
	}
	
	private static void elevatorAnimation(KeyValue kvElevator, KeyValue kvRopes){		
		final Timeline timeline = new Timeline();
		timeline.setRate(TIME_SPEED_MULTIPLYER);
		timeline.setCycleCount(1);
		timeline.setAutoReverse(false);
		KeyFrame kfElevator = new KeyFrame(Duration.millis(FLOOR_TRAVEL_TIME), kvElevator);
		KeyFrame kfRopes = new KeyFrame(Duration.millis(FLOOR_TRAVEL_TIME), kvRopes);
		timeline.getKeyFrames().addAll(kfElevator, kfRopes);
		timeline.play();
		timeline.onFinishedProperty().set(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				animationInProgress = false;
			}
		});
	}
	
	public static void boardElevator(){
		int amountOnFloor = waitingPassengers[elevator.getFloor()].getValue();
		elevator.addPasangers(amountOnFloor);
		waitingPassengers[elevator.getFloor()].set(0);
	}
	
	private void setUpElevator(){
		elevator = new Elevator(ELEVATOR_WIDTH, ELEVATOR_HEIGHT, Color.LIGHTGRAY);
		int ground = -ELEVATOR_HEIGHT + ((FLOOR_COUNT-1)*FLOOR_HEIGHT);
		elevator.translateYProperty().set(ground);
		elevator.translateXProperty().set(5);
	}
	
	private Node getImagePanel(){
		
		Group imgPanel = new Group();
		
		setUpElevator();
		
		Group floorLines = new Group();
		Line floor5 = new Line(0, 0, 150, 0);
		Line floor4 = new Line(0, 	FLOOR_HEIGHT, 	150, 	FLOOR_HEIGHT);
		Line floor3 = new Line(0, 2*FLOOR_HEIGHT, 	150, 2*	FLOOR_HEIGHT);
		Line floor2 = new Line(0, 3*FLOOR_HEIGHT, 	150, 3*	FLOOR_HEIGHT);
		Line floor1 = new Line(0, 4*FLOOR_HEIGHT, 	150, 4*	FLOOR_HEIGHT);
		Line floor0 = new Line(0, 5*FLOOR_HEIGHT,	150, 5*	FLOOR_HEIGHT);
		
		floor0.setStrokeWidth(3);
		floor1.setStrokeWidth(2);
		floor2.setStrokeWidth(2);
		floor3.setStrokeWidth(2);
		floor4.setStrokeWidth(2);
		floor5.setStrokeWidth(2);
		
		floorLines.getChildren().addAll(floor0,
										floor1,
										floor2,
										floor3,
										floor4,
										floor5);
		
		
		Group floorLabels = new Group();
		Text txFloor5 = new Text(110, 0 			 -3, "Floor 5");
		Text txFloor4 = new Text(110, 1*FLOOR_HEIGHT -3, "Floor 4");
		Text txFloor3 = new Text(110, 2*FLOOR_HEIGHT -3, "Floor 3");
		Text txFloor2 = new Text(110, 3*FLOOR_HEIGHT -3, "Floor 2");
		Text txFloor1 = new Text(110, 4*FLOOR_HEIGHT -3, "Floor 1");
		Text txFloor0 = new Text(110, 5*FLOOR_HEIGHT -3, "Floor 0");
		
		floorLabels.getChildren().addAll(	txFloor0,
											txFloor1,
											txFloor2,
											txFloor3,
											txFloor4,
											txFloor5);
		
		Group floorPassengers = new Group();
		Text passengersLabel = new Text(55, 0 			 -FLOOR_HEIGHT - 30, "Waiting"+"\n"+"passengers");
		
		Text txFloorCount5 = new Text(70, 0 			 -30,"");
		Text txFloorCount4 = new Text(70, 1*FLOOR_HEIGHT -30,"");
		Text txFloorCount3 = new Text(70, 2*FLOOR_HEIGHT -30,"");
		Text txFloorCount2 = new Text(70, 3*FLOOR_HEIGHT -30,"");
		Text txFloorCount1 = new Text(70, 4*FLOOR_HEIGHT -30,"");
		Text txFloorCount0 = new Text(70, 5*FLOOR_HEIGHT -30,"");
		
		Font fnt = Font.font("Verdana", FontWeight.BOLD, 12);
		txFloorCount5.setFont(fnt);
		txFloorCount4.setFont(fnt);
		txFloorCount3.setFont(fnt);
		txFloorCount2.setFont(fnt);
		txFloorCount1.setFont(fnt);
		txFloorCount0.setFont(fnt);
		
		txFloorCount5.textProperty().bind(waitingPassengers[5].asString());
		txFloorCount4.textProperty().bind(waitingPassengers[4].asString());
		txFloorCount3.textProperty().bind(waitingPassengers[3].asString());
		txFloorCount2.textProperty().bind(waitingPassengers[2].asString());
		txFloorCount1.textProperty().bind(waitingPassengers[1].asString());
		txFloorCount0.textProperty().bind(waitingPassengers[0].asString());
		
		
		floorPassengers.getChildren().addAll(	passengersLabel,
												txFloorCount5,
												txFloorCount4,
												txFloorCount3,
												txFloorCount2,
												txFloorCount1,
												txFloorCount0);
		
		Group shaft = new Group();
		Line shftR = new Line(ELEVATOR_WIDTH + 10	, -FLOOR_HEIGHT, ELEVATOR_WIDTH + 10, 5*FLOOR_HEIGHT);
		Line shftL = new Line(0						, -FLOOR_HEIGHT, 0					, 5*FLOOR_HEIGHT);
		Line roof = new Line(0						, -FLOOR_HEIGHT, ELEVATOR_WIDTH + 10, -FLOOR_HEIGHT);
		
		shftR.setStrokeWidth(4);
		shftL.setStrokeWidth(4);
		roof.setStrokeWidth(4);
		
		shaft.getChildren().addAll(	shftR,
									shftL,
									roof);
		

		int midShaft = (ELEVATOR_WIDTH + 10)/2;
		int maxRopeLength = ( (FLOOR_COUNT-2) * FLOOR_HEIGHT ) + FLOOR_HEIGHT - ELEVATOR_HEIGHT;
		ropes = new Line(midShaft, -FLOOR_HEIGHT, midShaft, maxRopeLength);
		
		ropes.setStrokeWidth(2);

		
		imgPanel.getChildren().addAll(	elevator, 
										floorLines,
										floorLabels,
										floorPassengers,
										shaft,
										ropes);
	
		
		return imgPanel;
	}
	
}
