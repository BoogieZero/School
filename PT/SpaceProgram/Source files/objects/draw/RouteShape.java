package objects.draw;

import java.util.Iterator;
import java.util.List;


import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import objects.Factory;
import objects.Planet;
import objects.Route;
import objects.Ship;
import objects.SpaceObject;
import objects.TimeStamp;

/**
 * Class represents set of functions added to given Route which allows to draw it in ImgPlane.
 * his class is practically drawable shape of the given Route.
 * 
 * @author Martin Hamet
 *
 */
public class RouteShape {
	/**
	 * Default scale for highlighted route width (width of individual Paths)
	 */
	private static final double DEFAULT_HIGHLIGTED_WIDTH_MUL = 5;
	
	/**
	 * Default color for highlighted route (it's individual Paths)
	 */
	private static final Color DEFAULT_HIGHLIGHTED_COL = Color.WHITESMOKE;
	
	/**
	 * Given Route
	 */
	private final Route route;
	
	/**
	 * Group of Paths which represent section leading to target
	 */
	private final Group shapeRTT;
	
	/**
	 * Group of Paths which represents section leading to home factory
	 */
	private final Group shapeRTH;
	
	/**
	 * Shadow dropped by route to target
	 */
	private DropShadow shadowRTT;
	
	/**
	 * Shadow dropped by route to home
	 */
	private DropShadow shadowRTH;
	
	/**
	 * Shadow dropped by TimeStamp where should be unloaded some material
	 */
	private DropShadow shadowAction;
	
	/**
	 * Returns Group representing route to target
	 * 
	 * @return	Group representing route to target
	 */
	public Group getShapeRTT(){
		return shapeRTT;
	}
	
	/**
	 * Returns Group representing route to home
	 * @return	Group representing route to home
	 */
	public Group getShapeRTH(){
		return shapeRTH;
	}
	
	/**
	 * Instantiates RouteShape by given Route
	 * 
	 * @param route	Route for creation of RouteShape
	 */
	public RouteShape(Route route) {
		this.route = route;
		shapeRTT = new Group();
		shapeRTH = new Group();
		
		createShadows();
		createShape();
	}
	
	/**
	 * Creates default shadows for route to home, route to target and TimeStamp with unloading action
	 */
	private void createShadows(){
		shadowRTT = new DropShadow();
		shadowRTT.setRadius(1.5);
		shadowRTT.setBlurType(BlurType.ONE_PASS_BOX);
		shadowRTT.setSpread(0.5);
		shadowRTT.setColor(Color.RED);
		
		shadowRTH = new DropShadow();
		shadowRTH.setRadius(1.5);
		shadowRTH.setBlurType(BlurType.ONE_PASS_BOX);
		shadowRTH.setSpread(0.5);
		shadowRTH.setColor(Color.GREENYELLOW);
		
		shadowAction = new DropShadow();
		shadowAction.setRadius(1.5);
		shadowAction.setBlurType(BlurType.TWO_PASS_BOX);
		shadowAction.setSpread(0.6);
		shadowAction.setColor(Color.WHITESMOKE);
	}
	
	/**
	 * Creates shape for given Route
	 */
	private void createShape(){
		createShapeRTT();
		createShapeRTH();
	}
	
	/**
	 * Creates shape for route to target
	 */
	private void createShapeRTT(){
		List<TimeStamp> RTT = route.getRouteToTarget();
		//List<TimeStamp> RTTs = RTT.subList(0, RTT.size());
		Iterator<TimeStamp> it = RTT.iterator();
		TimeStamp prev, current;
		current = it.next();
		
		while(it.hasNext()){
			prev = current;
			current = it.next();
			addLineToShape(prev, current, shapeRTT);
			//System.out.println("P: "+prev.getObject().getName()+" N: "+current.getObject().getName());
		}
	}
	
	/**
	 * Creates shape for route to home
	 */
	private void createShapeRTH(){
		List<TimeStamp> RTH = route.getRouteToHome();
		//List<TimeStamp> RTHs = RTH.subList(0, RTH.size()-1);
		Iterator<TimeStamp> it = RTH.iterator();
		TimeStamp prev, current;
		current = it.next();
		
		while(it.hasNext()){
			prev = current;
			current = it.next();
			addLineToShape(prev, current, shapeRTH);
			//System.out.println("P: "+prev.getObject().getName()+" N: "+current.getObject().getName());
		}
	}
	
	/**
	 * Adds line (path) from TimeStamp ts1 to TimeStamp ts2 on position coresponding to coordinates of
	 * those two given TimeStamps. 
	 * Line is colored and scaled for route to home or route to target accordingly. Those colors
	 * add up when overlapped
	 * Created line and possible shadow for TimeStamp with loading action is added to given Group.
	 * 
	 * @param ts1		start of line
	 * @param ts2		end of line
	 * @param shape		created shape is added to this Group
	 */
	private void addLineToShape(TimeStamp ts1, TimeStamp ts2, Group shape){
		SpaceObject pl1 = ts1.getObject();
		SpaceObject pl2 = ts2.getObject();
		
		if(Ship.Status.actionToPerform(ts1.getAction())){
			//if advanced action should be made here -> highlight
			double x = ts1.getObject().getX();
			double y = ts1.getObject().getY();
			
			Circle cA = new Circle(x, y, PlanetShape.DEFAULT_SIZE, Color.WHITESMOKE);
			if( !(   (ts1.getObject()) instanceof Factory)   ){
				double scl = PlanetShape.createScaleFor((Planet)(ts1.getObject()));
				cA.setScaleX(scl);
				cA.setScaleY(scl);
			}
			cA.setEffect(shadowAction);
			shape.getChildren().add(cA);
		}
		
		Line l1 = new Line(pl1.getX(), pl1.getY(), pl2.getX(), pl2.getY());
		
		if(shape == shapeRTH){
			l1.setStrokeWidth(PlanetShape.DEFAULT_PATH_WIDTH * DEFAULT_HIGHLIGTED_WIDTH_MUL*0.75);
			l1.setStroke(DEFAULT_HIGHLIGHTED_COL);
			l1.setEffect(shadowRTH);
		}else{
			l1.setStrokeWidth(PlanetShape.DEFAULT_PATH_WIDTH * DEFAULT_HIGHLIGTED_WIDTH_MUL);
			l1.setStroke(DEFAULT_HIGHLIGHTED_COL);
			l1.setEffect(shadowRTT);
		}
		
		shape.getChildren().add(l1);
	}
	
	/**
	 * Class represents states in which can be route highlighted
	 * 
	 * @author Martin Hamet
	 *
	 */
	public static enum HighlightType{
		ROUTE_TO_TARGET, ROUTE_TO_HOME, BOTH_ROUTES;
	}
}
