package upg.WaterNetwork;

/**
 * The {@code Pipe} class represents a pipe connecting two nodes in the water network.
 * 
 * @see NetworkNode
 * @see Reservoir
 */
public class Pipe {
	/** The reference to the node where the pipe starts.
	 * 
	 * N.B. It could be a simple node {@link NetworkNode} 
	 * or a reservoir {@link Reservoir}. To distinguish 
	 * between these two cases, it is recommended to use this code:
	 * <pre>
	 * if (pipe.start instanceof Reservoir) {
	 *   Reservoir r = (Reservoir)pipe.start;
	 * 		
	 * 	//manipulation with reservoir r
	 * }
	 * </pre> 
	 */
	public NetworkNode start;
	
	/** The reference to the node where the pipe ends.
	 * 
	 * N.B. It could be a simple node {@link NetworkNode} 
	 * or a reservoir {@link Reservoir}. To distinguish 
	 * between these two cases, it is recommended to use this code:
	 * <pre>
	 * if (pipe.end instanceof Reservoir) {
	 *   Reservoir r = (Reservoir)pipe.end;
	 * 		
	 * 	//manipulation with reservoir r
	 * }
	 * </pre> 
	 */
	public NetworkNode end;
	
	/** The cross section of the pipe in square meters.*/
	public double crossSection;
	
	/** The pipe valve openness on the scale 0 (closed) - 1 (fully opened).*/
	public double open;
	
	/** The current flow in cubic meters per second whereas the sign of the flow denotes the direction of the flow.
	 * Positive values mean that the water flows from start to end, negative values from end to start. */
	public double flow;	
	
	/** Creates a pipe of the specified cross section connecting two nodes in the water network. 
	 * @param start The node at the one end of the pipe  
	 * @param end The node at the other end of the pipe
	 * @param crossSection The cross section of the pipe in square meters
	 * @param open The openness of the valve in the pipe (0-1)
	 */
	public Pipe(NetworkNode start, NetworkNode end, double crossSection, double open) {
		this.start = start;
		this.end = end;
		this.crossSection = crossSection;
		this.open = open;
	}
}
