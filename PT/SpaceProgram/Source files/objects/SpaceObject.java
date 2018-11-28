package objects;

import java.util.HashMap;
import java.util.Map;

/**
 * Class SpaceObject
 * This class is parent for all object in galaxy like planets, factories etc.
 * 
 * @author Jiří Lukáš
 *
 */
public class SpaceObject {
	/**
	 * Name of space object
	 */
	private String name;
	/**
	 * Coordinate x
	 */
	private double x;
	/**
	 * Coordinate y
	 */
	private double y;
	/**
	 * Map of space object neighbors
	 */
	private final Map<String,Path> neighbours = new HashMap<String,Path>();
	

	/**
	 * Constructor
	 * @param name  Name of space object
	 * @param x Coordinate x
	 * @param y Coordinate y
	 */
	public SpaceObject(String name, double x, double y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Method addNeghbour
	 * Adding neighbor to the map of all neighbors
	 * @param neighbour Added neighbor
	 */
	public void addNeghbour(Path neighbour){
		neighbours.put(neighbour.getTarget().getName(),neighbour);
	}
	
	/**
	 * Method getNeighbours
	 * @return Map of all neighbors
	 */
	public Map<String,Path> getNeighbours(){
		return neighbours;
	}

	/**
	 * Method getName
	 * Get for name of Space object
	 * @return name Name of Space object
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method setName
	 * Set for name of Space object
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Method getX
	 * Get for coordinate X 
	 * @return x Coordinate X 
	 */
	public double getX() {
		return x;
	}

	/**
	 * Method setX
	 * @param x Coordinate x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Method getY
	 * Get for coordinate Y 
	 * @return y Coordinate Y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Method setY
	 * @param y Coordinate y to set
	 */
	public void setY(double y) {
		this.y = y;
	}
}
