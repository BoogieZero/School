package gui;


import java.util.HashMap;
import java.util.Map;

import function.TimeHandler;
import generator.Loader;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import objects.Factory;
import objects.Path;
import objects.Planet;
import objects.Route;
import objects.SpaceObject;
import objects.draw.FactoryShape;
import objects.draw.PlanetShape;
import objects.draw.RouteShape;


public class ImgPlane extends StackPane{
	private static final double 	DEFAULT_WIDTH = 1300;
	private static final double 	DEFAULT_HEIGHT = 1300;
	private static final double 	DEFAULT_SCALE = 0.7;
	private static final double 	DEFAULT_ZOOM_FACTOR = 1.2;
	private static final Color		DEFAULT_BACKGROUND_COLOR = 
										new Color(0, 0, 0.1, 1);
	public static final double 		DEFALUT_SCALE_MIN = 0.5;
	public static final double 		DEFALUT_SCALE_MAX = 20.0;
	
	private final Group imgRoot;
	private Group img;
	private final ScrollPane scrp;
	
	private final DoubleProperty imgScale;
	
	private static PlanetShape selectedP = null;
	private static RouteShape selectedR = null;
	private static FactoryShape selectedF = null;
	
	private static Map<Planet, PlanetShape> planetShapes = new HashMap<Planet, PlanetShape>();
	private static Map<Factory, FactoryShape> factoryShapes = new HashMap<Factory, FactoryShape>();
	
	public static Route test;
	private static Group highLightedRoute = new Group();
	
	public static Planet getSelectedPlanet(){
		if(selectedP == null){
			return null;
		}
		return selectedP.getPlanet();
	}
	
	public static Factory getSelectedFactory(){
		if(selectedF == null){
			return null;
		}
		return selectedF.getFactory();
	}
	
	public static void setHighlightRouteType(RouteShape.HighlightType hightLitghType){
		switch(hightLitghType){
			case ROUTE_TO_TARGET:	setHighlightRouteGroup(selectedR.getShapeRTT());
									break;
			case ROUTE_TO_HOME:		setHighlightRouteGroup(selectedR.getShapeRTH());
									break;
			case BOTH_ROUTES:		Group shape = new Group();
									shape.getChildren().addAll(	selectedR.getShapeRTT(),
																selectedR.getShapeRTH());
									setHighlightRouteGroup(shape);
									break;
									
				default:	System.out.println("ERROR - Wrong type of highlight route!");
				 			System.exit(0);
				 			break;
		}
	}
	
	private static void setHighlightRouteGroup(Group shape){
		highLightedRoute.getChildren().setAll(shape);
	}
	
	public static void setSelected(SpaceObject selected){
		if(selected instanceof Planet){
			PlanetShape ps = planetShapes.get((Planet)selected);
			selectPlanet(ps);
		}else{
			FactoryShape fs = factoryShapes.get((Factory)selected);
			selectFactory(fs);
		}
	}
	
	public static void highlightRoute(Route route, RouteShape.HighlightType highligthType){
		selectedR = new RouteShape(route);
		setHighlightRouteType(highligthType);
	}
	
	private static void selectPlanet(PlanetShape ps){
		if(selectedP != null){
			selectedP.setHighlighted(false);
		}
		selectedP = ps;
		selectedP.setHighlighted(true);
		
		//comWriteSelected(ps);		//testing
	}
	
	private static void selectFactory(FactoryShape fs){
		if(selectedF != null){
			selectedF.setHighlighted(false);
		}
		selectedF = fs;
		selectedF.setHighlighted(true);
		
		//comWriteSelected(fs); 		//testing
	}
	
	
	public static void comWriteSelected(FactoryShape fs){
		System.out.println("SELECTED_F: "+fs.getFactory().getName());
		//System.out.println(fs.getFactory().getShipList());
	}
	
	public static void comWriteSelected(PlanetShape ps){
		System.out.println("SELECTED_P: "+ps.getPlanet().getName()+" Pop: "+ps.getPlanet().getPopulation()+" Ord: "+ps.getPlanet().getOrder());
		
		
		for(Map.Entry<String, Path> pth : ps.getPlanet().getNeighbours().entrySet()){
			System.out.println("   "+pth.getValue().getTarget().getName()+"  "
								+pth.getValue().getLength()+ " Danger: "+pth.getValue().isDanger());
		}
		
		System.out.println(ps.getPlanet().getShipList());
		//ImgPlane.highlightRoute(ps.getPlanet().getShipList().values().iterator().next().getRoute(), RouteShape.HighlightType.ROUTE_TO_TARGET);
		
	}
	
	private Line drawPath(SpaceObject p1, SpaceObject p2){
		Line l1 = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		
		l1.setStrokeWidth(PlanetShape.DEFAULT_PATH_WIDTH);
		
		if(p1.getNeighbours().get(p2.getName()).isDanger()){
			l1.setStroke(PlanetShape.DEFAULT_PATH_PIRATE_COLOR);	
			l1.setStrokeWidth(PlanetShape.DEFAULT_PATH_WIDTH * PlanetShape.DEFAULT_PATH_PIRATE_MUL);
		}else{
			l1.setStroke(PlanetShape.DEFAULT_PATH_COLOR);
		}
		
		return l1;
	}
	
	private void drawPaths(Group pathGroup, PlanetShape ps){
		Planet p = ps.getPlanet();
		
		Map<String, Path> neigh = p.getNeighbours();
		for(Map.Entry<String, Path>pth:neigh.entrySet()){
			pathGroup.getChildren().add(
									drawPath(p,pth.getValue().getTarget())
									);
		}
	}
	
	private void drawPaths(Group pathGroup, FactoryShape fs){
		Factory f = fs.getFactory();
		
		Map<String, Path> neigh = f.getNeighbours();
		for(Map.Entry<String, Path>pth:neigh.entrySet()){
			pathGroup.getChildren().add(
									drawPath(f,pth.getValue().getTarget())
									);
		}
	}
	
	private void drawPlanet(Group planetGroup, Group pathGroup, Planet p){
		PlanetShape ps = new PlanetShape(p);
		planetShapes.put(ps.getPlanet(), ps);
		Circle c = ps.getShape();
		c.setOnMouseClicked(evt -> selectPlanet(ps));
		
		drawPaths(pathGroup, ps);
		planetGroup.getChildren().add(c);
	}
	
	private void drawPlanets(Group img){
		Group planets = new Group();
		Group paths = new Group();
		//Loader load = new Loader();
		Loader load = TimeHandler.getLoader();
		
		Map<String, Planet> gal = load.getPlanets();
		
		for(Map.Entry<String, Planet>pl : gal.entrySet()){
			drawPlanet(planets, paths, pl.getValue());
		}
		
		img.getChildren().addAll(paths,planets);
	}
	
	private void drawFactory(Group factoryGroup, Group pathGroup, Factory f){
		FactoryShape fs = new FactoryShape(f);
		factoryShapes.put(fs.getFactory(), fs);
		
		Rectangle c = fs.getShape();
		c.setOnMouseClicked(evt -> selectFactory(fs));
		
		drawPaths(pathGroup, fs);
		factoryGroup.getChildren().add(c);
	}
	
	private void drawFactories(Group img){
		Group factories = new Group();
		Group pathsF = new Group();
		Loader load = TimeHandler.getLoader();
		
		Map<String, Factory> factoriesM = load.getFactoryMap();
		for(Map.Entry<String, Factory> fct: factoriesM.entrySet()){
			drawFactory(factories, pathsF, fct.getValue());
		}
		
		img.getChildren().addAll(pathsF, factories);
	}
	
	private void paint(){
		img = new Group();
		clrScene(img);
		imgRoot.getChildren().add(img);
		imgRoot.setOnScroll(evt -> zoom(evt));
		resetScale();

		img.getChildren().add(highLightedRoute);
		
		drawPlanets(img);
		drawFactories(img);
		drawCenter(img);
	}
	
	private void resetScale(){
		img.setScaleX(DEFAULT_SCALE);
		img.setScaleY(DEFAULT_SCALE);
	}
	
	private void drawCenter(Group g){
		int length = 100;
		
		Circle mid = new Circle(0, 0, 85);
		mid.setStroke(null);
		mid.setFill(Color.BLACK);
		mid.setStrokeWidth(1.5);
		mid.setOpacity(0.95);
		
		DropShadow ds = new DropShadow();
		ds.setRadius(130);
		ds.setSpread(0.65);
		ds.setColor(Color.ALICEBLUE);
		
		mid.setEffect(ds);
		
		Group cross = new Group();
		Line l1 = new Line(-length, 0, length, 0);
		Line l2 = new Line(0, -length, 0, length);
		
		l1.setStroke(Color.RED);
		l1.setStrokeWidth(4);
		l2.setStroke(Color.RED);
		l2.setStrokeWidth(4);
		
		cross.getChildren().addAll(l1,l2);
		
		
		g.getChildren().addAll(mid);
		
	}
	
	private void setCroppedImage(Group img){
		Image im = new Image(getClass().getResourceAsStream("/img/bckGround.jpeg"));
		double imWidth = im.getWidth();
		
		ImageView bckImage = new ImageView();
		bckImage.setImage(im);
		
		double afterScaleMid = ( imWidth / 2 ) - DEFAULT_WIDTH / 2;
		
		Rectangle2D cut = new Rectangle2D(afterScaleMid, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		bckImage.setFitHeight(DEFAULT_HEIGHT);
		bckImage.setFitWidth(DEFAULT_WIDTH);
		bckImage.setSmooth(true);
		
		bckImage.setViewport(cut);

		bckImage.setOpacity(0.6);
		img.getChildren().add(bckImage);
		
	}
	
	private void clrScene(Group img){
		Group background = new Group();
		Rectangle bckRect = new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_BACKGROUND_COLOR);
		
		RadialGradient gradient1 = new RadialGradient(	0,
														0,
														DEFAULT_WIDTH/2,
														DEFAULT_HEIGHT/2,
														400,
														false,
														CycleMethod.NO_CYCLE,
														new Stop(0.4, Color.DARKBLUE),
														new Stop(2, DEFAULT_BACKGROUND_COLOR
														));
		
		bckRect.setFill(gradient1);
		
		background.getChildren().add(bckRect);
		
		background.setTranslateX(-DEFAULT_WIDTH/2);
		background.setTranslateY(-DEFAULT_HEIGHT/2);
		
		img.getChildren().addAll(background);//, bckImage);
		setCroppedImage(background);
	}
	
	public ImgPlane(){
		scrp = new ScrollPane();
		imgRoot = new Group();
		paint();
		scrp.setContent(imgRoot);
		scrp.setPannable(true);
		//scrp.setOnMouseReleased(evt -> test());
		imgScale = new SimpleDoubleProperty();
		this.getChildren().setAll(scrp);
	}
	
	public void BindToImgScale(DoubleProperty val){
		val.bind(imgScale);
	}
	
	private void zoom(ScrollEvent evt){
		double factor = DEFAULT_ZOOM_FACTOR;
		if(evt.getDeltaY() <= 0){
			factor = 1/factor;
		}
		double scale = img.getScaleX() * factor;
		
		if(scale >= DEFALUT_SCALE_MAX){
			scale = DEFALUT_SCALE_MAX;
		}
		
		if(scale <= DEFALUT_SCALE_MIN){
			scale = DEFALUT_SCALE_MIN;
		}
		
		imgScale.set(scale);
		
		
		double scrValH = scrp.getHvalue();
		double scrValV = scrp.getVvalue();
		
		img.setScaleX(imgScale.getValue());
		img.setScaleY(imgScale.getValue());
		
		scrp.setHvalue(scrValH);
		scrp.setVvalue(scrValV);

		evt.consume();
	}
}