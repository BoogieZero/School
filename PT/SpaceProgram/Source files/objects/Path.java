package objects;

/**
 * Class Path
 * Is class representation  route between factory and planet or planet and planet.
 * 
 * @author Jiří Lukáš
 *
 */
public class Path {
	
	/**
	 * Distance between this and target
	 */
	private double length;
	
	/**
	 * target planet or factory where road ends
	 */
	private SpaceObject target;
	
	/**
	 * is path with pirates or without
	 */
	private boolean danger;

	
	/**
	 * Constructor
	 * @param length  Distance between this and target
	 * @param target  Target planet or factory where road ends
	 * @param danger  Is path with pirates or without
	 */
	public Path(double length, SpaceObject target, boolean danger) {
		this.length = length;
		this.target = target;
		this.danger = danger;
	}

	/**
	 * Method getLength
	 * @return length Distance between this and target
	 */
	public double getLength() {
		return length;
	}

	/**
	 * Method setLength
	 * @param length the length to set
	 */
	public void setLength(double length) {
		this.length = length;
	}

	/**
	 * Method getTarget
	 * @return target Target planet or factory where road ends
	 */
	public SpaceObject getTarget() {
		return target;
	}

	/**
	 * Method setTarget
	 * @param target the target to set
	 */
	public void setTarget(Planet target) {
		this.target = target;
	}
	
	/**
	 * Method isDanger
	 * @return the danger If this path is pirates or not
	 */
	public boolean isDanger() {
		return danger;
	}

	/**
	 * Method setDanger
	 * @param danger the danger to set
	 */
	public void setDanger(boolean danger) {
		this.danger = danger;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Path [length=" + length + ", target=" + target + ", danger=" + danger + "]";
	}
	
	
}
