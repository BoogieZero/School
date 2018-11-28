package upg.WaterNetwork;

import java.awt.geom.Point2D;

/**
 * The {@code Reservoir} class represents a network node with a water tank (reservoir).
 * 
 * @see NetworkNode
 */
public class Reservoir extends NetworkNode {
	/** The capacity of this reservoir in cubic meters */
	public double capacity;	
	/** The current content of this reservoir in cubic meters */
	public double content;
	private double maxLevel;
	private double minLevel;
	
	/** This method returns the current water level in this reservoir in meters.
	 * @return the current water level
	 */
	public double level() {
		return minLevel + (content/capacity)*(maxLevel-minLevel);
	}
	
	/**
	 * Creates a reservoir of the specified capacity at the specified position.
	 * @param capacity The capacity of the reservoir in cubic meters.
	 * @param content The current content of the reservoir in cubic meters.
	 * @param minLevel The minimal water level (in meters)
	 * @param maxLevel The maximal water level (in meters)
	 * @param position The position of the reservoir in 2D
	 */
	public Reservoir(double capacity, float content, float minLevel, float maxLevel, Point2D position) {
		super(position);
		this.capacity = capacity;
		this.content = content;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;		
	}
}
