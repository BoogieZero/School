package objects.draw;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import objects.Factory;

/**
 * Class represents set of functions added to given Factory which allows to draw it in ImgPlane.
 * This class is practically drawable shape of the given factory wth it's paths.
 * 
 * @author Martin Hamet
 *
 */
public class FactoryShape {
	/**
	 * Default shape size
	 */
	public static final double	DEFAULT_SIZE = 4;
	
	/**
	 * Default shape color
	 */
	private static final Paint 	DEFAULT_COLOR = Color.YELLOW;
	
	/**
	 * Default color of higlighted factory
	 */
	private static final Paint 	DEFAULT_HIGHLIGHTED_COLOR = Color.YELLOW;
	
	/**
	 * Scale for highlighted factory
	 */
	private static final Double DEFAULT_HIGHLIGHTED_SCALE = 2.0;
	
	/**
	 * Shadow effect droped by highlighted factory
	 */
	private final DropShadow shadow;
	
	/**
	 * Given Factory
	 */
	private final Factory factory;
	
	/**
	 * Actual Shape of factory
	 */
	private Rectangle shape;
	
	/**
	 * Returns given factory
	 * @return	factory given for creation of FactoryShape
	 */
	public Factory getFactory(){
		return factory;
	}
	
	/**
	 * Actual shape of factory
	 * 
	 * @return	actual Shape of factory
	 */
	public Rectangle getShape(){
		return shape;
	}
	
	/**
	 * Sets highlighted mode of this FactoryShape
	 * 
	 * @param highlighted	true	factory becomes highlighted
	 * 						false	factory becomes non-highlighted
	 */
	public void setHighlighted(boolean highlighted){
		if(highlighted){
			shape.setFill(DEFAULT_HIGHLIGHTED_COLOR);
			shape.setScaleX(DEFAULT_HIGHLIGHTED_SCALE);
			shape.setScaleY(DEFAULT_HIGHLIGHTED_SCALE);
			shape.setEffect(shadow);
			
		}else{
			shape.setEffect(null);
			shape.setScaleX(1);
			shape.setScaleY(1);
			shape.setFill(DEFAULT_COLOR);
		}
	}
	
	/**
	 * Instantiates FactoryShape by given Factory
	 * 
	 * @param f	factory for creation of FactoryShape
	 */
	public FactoryShape(Factory f) {
		this.factory = f;
		shadow = new DropShadow();
		shadow.setRadius(1.75);
		shadow.setBlurType(BlurType.GAUSSIAN);
		shadow.setSpread(0.6);
		shadow.setColor(Color.FIREBRICK);
		createShape();
	}
	
	/**
	 * Creates actual Shape for factory
	 */
	private void createShape(){
		shape = new Rectangle(factory.getX() - DEFAULT_SIZE/2, factory.getY() - DEFAULT_SIZE/2, 
				DEFAULT_SIZE, DEFAULT_SIZE);
		shape.setFill(DEFAULT_COLOR);
	}
}
