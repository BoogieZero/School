package objects.generator;

import objects.HasName;
import objects.SpaceObject;

/**
 * Class Vertex
 * vertex is an object for Dijkstra.
 * We used vertex for searching shortest route with name of ancestor.
 * Vertex has space object because it can be planet or factory.
 * @author Jiří Lukáš
 *
 */
public class Vertex implements HasName {
	/**
	 * Space object 
	 */
	private final SpaceObject spaceObject;
	/**
	 * Name of ancestor
	 */
	private String nameOfAncestor;
	/**
	 * Distance for Dijkstra
	 */
	private double distace = Double.MAX_VALUE;
	


	/**
	 * Constructor
	 * @param spaceObject Space object (planet or factory)
	 */
	public Vertex(SpaceObject spaceObject) {
		this.spaceObject = spaceObject;
	}
	
	
	/**
	 * Method getName
	 * Getter for name of Space object in vertex.
	 * @return name Name of Space object in vertex
	 */
	public String getName() {
		return spaceObject.getName();
	}

	/**
	 * Method nameOfAncestor
	 * Getter for name of ancestor.
	 * @return nameOfAncestor name of ancestor
	 */
	public String getNameOfAncestor() {
		return nameOfAncestor;
	}


	/**
	 * Method getDistance
	 * @return distace Distance
	 */
	public double getDistace() {
		return distace;
	}
	

	/**
	 * Method getSpaceObject
	 * @return spaceObject Space object in vertex (planet, factory)
	 */
	public SpaceObject getSpaceObject() {
		return spaceObject;
	}


	/**
	 * Method setNameOfAncestor
	 * @param nameOfAncestor the nameOfAncestor to set
	 */
	public void setNameOfAncestor(String nameOfAncestor) {
		this.nameOfAncestor = nameOfAncestor;
	}

	/**
	 * Method setDistance
	 * @param distace the distace to set
	 */
	public void setDistace(double distace) {
		this.distace = distace;
	}

}
