package upg.WaterNetwork;

import java.awt.geom.Point2D;

/**
 * The {@code NetworkNode} class represents a single node in which the pipes of the water network meet. 
 *
 * @see Pipe
 */
public class NetworkNode {
	
	/** position of this node in 2D */
	public Point2D position;
	public int ID;
        public int myID;
	
	/**
	* Creates a node at the specified position
	*  
	* @param position in 2D
	*/
	public NetworkNode(Point2D position) {
		this.position = position;
	}
}
