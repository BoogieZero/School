package objects.draw;



import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import objects.Planet;

/**
 * Class represents set of functions added to given Planet which allows to draw it in ImgPlane.
 * This class is practically drawable shape of the given planet with it's paths.
 * 
 * @author Martin Hamet
 *
 */
public class PlanetShape {
	//Planet
	
	/**
	 * Default shape size
	 */
	public static final double	DEFAULT_SIZE = 1.5;
	
	/**
	 * Default shape color
	 */
	private static final Paint 	DEFAULT_COLOR = Color.MEDIUMBLUE;
	
	/**
	 * Default highlighted color
	 */
	private static final Paint 	DEFAULT_HIGHLIGHTED_COLOR = Color.FIREBRICK;
	
	/**
	 * Default highlighted scale
	 */
	private static final Double DEFAULT_HIGHLIGHTED_SCALE = 2.0;
	
	
	
	//Paths
	
	/**
	 * Default width of path
	 */
	public static final double 	DEFAULT_PATH_WIDTH = 0.2;
	
	/**
	 * Default color of path
	 */
	public static final Paint 	DEFAULT_PATH_COLOR = Color.DARKBLUE;
	
	/**
	 * Default color of dangerous path
	 */
	public static final Paint	DEFAULT_PATH_PIRATE_COLOR = Color.BLACK;//Color.DIMGREY;
	
	/**
	 * Default scale of dangerous path
	 */
	public static final double 	DEFAULT_PATH_PIRATE_MUL = 1.5;
	
	/**
	 * Shadow effect droped by highlighted planet
	 */
	private final DropShadow shadow;
	
	/**
	 * Given Planet
	 */
	private final Planet planet;
	
	/**
	 * Actual Shape of PlanetShape
	 */
	private Circle shape;
	
	
	/**
	 * Returns Planet given for creation of PlanetShape
	 * 
	 * @return	Planet given for creation of PlanetShape
	 */
	public Planet getPlanet(){
		return planet;
	}
	
	/**
	 * Returns actual Shape of PlanetShape
	 * 
	 * @return	actual Shape of PlanetShape
	 */
	public Circle getShape(){
		return shape;
	}
	
	/**
	 * Sets highlighted mode for this PlanetShape
	 * 
	 * @param highlighted	true	factory becomes highlighted
	 * 						false	factory becomes non-highlighted
	 */
	public void setHighlighted(boolean highlighted){
		if(highlighted){
			shape.setFill(DEFAULT_HIGHLIGHTED_COLOR);
			shape.setScaleX(DEFAULT_HIGHLIGHTED_SCALE*0.3 + shape.getScaleX()*0.7);
			shape.setScaleY(DEFAULT_HIGHLIGHTED_SCALE*0.3 + shape.getScaleY()*0.7);
			shape.setEffect(shadow);
			
		}else{
			shape.setEffect(null);
			shape.setFill(createColor());
			double scl = createScale();
			shape.setScaleX(scl);
			shape.setScaleY(scl);
		}
	}
	
	/**
	 * Instantiates PlanetShape by given Planet
	 * 
	 * @param p	Planet for creation of PlanetShape
	 */
	public PlanetShape(Planet p) {
		this.planet = p;
		shadow = new DropShadow();
		shadow.setRadius(1.75);
		shadow.setBlurType(BlurType.GAUSSIAN);
		shadow.setSpread(0.6);
		shadow.setColor(Color.FIREBRICK);
		createShape();
	}
	
	/**
	 * Returns scale multiplier coresponding to population of given Planet
	 * 
	 * @param p	scale is calculated for this Planet
	 * 
	 * @return	coresponding scale to given Planet
	 */
	public static double createScaleFor(Planet p){
		double scl = 2/DEFAULT_SIZE;
		double factor = 0.4 + 0.6*(double)p.getPopulation()/10000000;
		scl *= factor;
		return scl;
	}
	
	/**
	 * Returns 	scale multiplier coresponding to population of this Planet
	 * 
	 * @return	coresponding to population of this Planet
	 */
	private double createScale(){
		double scl = 2/DEFAULT_SIZE;
		double factor = 0.4 + 0.6*(double)planet.getPopulation()/10000000;
		scl *= factor;
		return scl;
	}
	
	/**
	 * Creates 	coresponding color for this Planet by changing saturation of it's default color
	 * 
	 * @return	saturated color by population of this Planet
	 */
	private Color createColor(){
		Color col = (Color)DEFAULT_COLOR;
		double factor = (double)planet.getPopulation()/10000000;
		
		double hue = col.getHue();
		double sat = (col.getSaturation()*0.3) + (0.7*factor);
		double brt = col.getBrightness();
		
		col = Color.hsb(hue, sat, brt);
		
		return col;
	}
	
	/**
	 * Creates actual shape for this Planet
	 */
	private void createShape(){
		shape = new Circle(planet.getX(),planet.getY(),DEFAULT_SIZE);
		shape.setFill(createColor());
		double scl = createScale();
		shape.setScaleX(scl);
		shape.setScaleY(scl);
	}
}
