package upg.WaterNetwork;

import java.awt.geom.Point2D;
import java.io.Console;

import Jama.Matrix;
/**
 * The {@code WaterNetwork} class simulates the flow of water in a water network system formed by 
 * nodes ({@link NetworkNode}, {@link Reservoir}) interconnected by pipes ({@link Pipe}) with valves.
 * The current state of the water network system is accessible via the methods {@link #currentSimulationTime()},
 * {@link #getAllNetworkNodes()}, and {@link #getAllPipes()}. To update the state of the system, i.e., to perform the
 * simulation, it is necessary to call the method {@link #updateState()}.      
 */
public class WaterNetwork {
	NetworkNode[] nodes;
	Pipe[] pipes;
	double time;
	
	final int SPS_NORMAL_SPEED = 50;
	final int SPS_FAST_SPEED = 200;
	
	int stepsPerSec = SPS_NORMAL_SPEED;	//number of simulation steps per second
	long updateTime;					//last time the simulation was updated
	
	static double FLOW_CONSTANT = 1; // flow = this*(level difference)*crossSection*open
	static double INTEGRATION_TIME = 0.1; // seconds? What seems real?
	static double ZERO_CONTENT = 0.0001; // below this is deemed empty, more than capacity-this is deemed full
	
	/** Constructs the default water network and starts the simulation of water flow.*/
	public WaterNetwork() {
		setDefaultConfig1();
		
		startSimulation();
	}
	
	/** Constructs the default water network and starts the simulation of water flow.*/
	public WaterNetwork(int scenario) {
		switch (scenario) {
		case 0: 
			setDefaultConfig1();
			break;
		case 1:
			setDefaultConfig2();
			break;
		case 2:
			setDefaultConfig3();
			break;
		case 3:
			setDefaultConfig4();
			break;
		case 4:
			setDefaultConfig5();
			break;
		}
		
		startSimulation();
	}
	
	private void startSimulation() {		
		updateTime = System.currentTimeMillis();		
	}

	/** This method returns all the simple nodes {@link NetworkNode} and reservoirs {@link Reservoir} in the system.
	 * N.B. to update the state of the nodes, run the method {@link #updateState()} first.
	 *  
	 * To determine, if the reference is an instance of {@link NetworkNode} class or of {@link Reservoir} class,
	 * it is recommended to use this code:
	 * <pre>
	 * if (nodes[i] instanceof Reservoir) {
	 *   Reservoir r = (Reservoir)nodes[i];
	 * 		
	 * 	//manipulation with reservoir r
	 * }
	 * </pre> 
	 * @return an array of all the nodes
	 */
	public NetworkNode[] getAllNetworkNodes() {
		return nodes;
	}
	
	/** This method returns all the pipes in the system.
	 *
 	 * N.B. to update the state of the pipes, run the method {@link #updateState()} first
	 * @return an array of all the pipes
	 */
	public Pipe[] getAllPipes() {
		return pipes;
	}
	
	/** This method returns the simulation time elapsed since the start of the simulation.
	 * 
	 * N.B. to update the simulation state, call the method {@link #updateState()}
	 * @return the current simulation time in seconds
	 */
	public double currentSimulationTime() {
		return time;
	}
	
	/** This method makes the simulation to run in the normal speed. */
	public void runNormal() {
		this.stepsPerSec = SPS_NORMAL_SPEED;
	}
	
	/** This method makes the simulation to run in the fast speed */
	public void runFast() {
		this.stepsPerSec = SPS_FAST_SPEED;
	}
		
	/** This method updates the current state of simulation, i.e., 
	 * it updates the current simulation time, states of pipes and nodes. */
	public void updateState() {
		long tm = System.currentTimeMillis();
		
		int n = (int)(((tm - updateTime) * stepsPerSec) / 1000);
		for (int i = 0; i < n; i++) {
			simulationStep();
		}
		
		updateTime = tm;
	}
	
	/** This method performs one step of the water flow simulation.*/
	protected void simulationStep() {
		int unknowns = pipes.length; // number of unknowns		
		int nodeIndex = 0;		
		for (int i = 0;i<nodes.length;i++)
			if (!(nodes[i] instanceof Reservoir)) {				
				// simple node, its level is unknown
				nodes[i].ID = nodeIndex;
				unknowns++;
				nodeIndex++;
			}
		Matrix m = new Matrix(unknowns, unknowns);
		Matrix rhs = new Matrix(unknowns, 1);
		for (int i = 0;i<pipes.length;i++) {
			m.set(i, i, 1);
			Pipe p = pipes[i];
			
			// level at pipe start
			if (p.start instanceof Reservoir) {
				rhs.set(i,0, p.crossSection*p.open*FLOW_CONSTANT*((Reservoir)p.start).level());
			}
			else
			{
				m.set(i, pipes.length+p.start.ID, - p.crossSection*p.open*FLOW_CONSTANT);
				m.set(pipes.length+p.start.ID, i, -1); // node equilibrium
			}
			
			// level at pipe end
			if (p.end instanceof Reservoir) {
				rhs.set(i, 0, rhs.get(i,0) - p.crossSection*p.open*FLOW_CONSTANT*((Reservoir)p.end).level());
			}
			else
			{
				m.set(i, pipes.length+p.end.ID, p.crossSection*p.open*FLOW_CONSTANT);
				m.set(pipes.length+p.end.ID, i, 1); // node equilibrium
			}						
		}
		
		// first solve, may contain flows from empty reservoirs and flows into full reservoirs
		// these will be resolved later
		Matrix s = m.solve(rhs);
		
		for(int i = 0;i<pipes.length;i++)
		{
			pipes[i].flow = (float)s.get(i,0);
			if (pipes[i].start instanceof Reservoir)
			{
				if (pipes[i].flow>0) {
					Reservoir r = (Reservoir)pipes[i].start;
					if (r.content<ZERO_CONTENT) {
						// flow out, but empty
						for (int j = 0;j<unknowns;j++)
							m.set(i, j, 0);
						m.set(i,i, 1);
						rhs.set(i, 0, 0);
					}
				}
			}
			if (pipes[i].end instanceof Reservoir)
			{
				if (pipes[i].flow<0) {
					Reservoir r = (Reservoir)pipes[i].end;
					if (r.content<ZERO_CONTENT) {
						// flow out, but empty
						for (int j = 0;j<unknowns;j++)
							m.set(i, j, 0);
						m.set(i,i, 1);
						rhs.set(i, 0, 0);
					}
				}
			}
		}
		
		// second solve with constrained flows
		s = m.solve(rhs);
		
		for(int i = 0;i<pipes.length;i++)
		{
			pipes[i].flow = (float)s.get(i,0);
			if (pipes[i].start instanceof Reservoir)
			{
				if (pipes[i].flow>0) {
					Reservoir r = (Reservoir)pipes[i].start;
					if (r.content<ZERO_CONTENT) {
						// flow out, but empty
						for (int j = 0;j<unknowns;j++)
							m.set(i, j, 0);
						m.set(i,i, 1);
						rhs.set(i, 0, 0);
					}
				}
				else {
					Reservoir r = (Reservoir)pipes[i].start;
					if (r.content>(r.capacity-ZERO_CONTENT)) {
						// flow in, but full
						for (int j = 0;j<unknowns;j++)
							m.set(i, j, 0);
						m.set(i,i, 1);
						rhs.set(i, 0, 0);
					}
				}
			}
			if (pipes[i].end instanceof Reservoir)
			{
				if (pipes[i].flow<0) {
					Reservoir r = (Reservoir)pipes[i].end;
					if (r.content<ZERO_CONTENT) {
						// flow out, but empty
						for (int j = 0;j<unknowns;j++)
							m.set(i, j, 0);
						m.set(i,i, 1);
						rhs.set(i, 0, 0);
					}
				}
				else {
					Reservoir r = (Reservoir)pipes[i].end;
					if (r.content>(r.capacity-ZERO_CONTENT)) {
						// flow in, but full
						for (int j = 0;j<unknowns;j++)
							m.set(i, j, 0);
						m.set(i,i, 1);
						rhs.set(i, 0, 0);
					}
				}
			}
		}
		
		// final solve
		s = m.solve(rhs);
		
		// determine integration time
		double clampedIntegrationTime = INTEGRATION_TIME;
		for(int i = 0;i<pipes.length;i++)
		{
			pipes[i].flow = (float)s.get(i,0);
			if (pipes[i].start instanceof Reservoir)
			{
				Reservoir r = (Reservoir)pipes[i].start;
				double newContent = r.content - INTEGRATION_TIME * pipes[i].flow;
				if (newContent<-ZERO_CONTENT)				
					clampedIntegrationTime = Math.min(clampedIntegrationTime, r.content/pipes[i].flow);				
				if (newContent > (r.capacity+ZERO_CONTENT))					
					clampedIntegrationTime = Math.min(clampedIntegrationTime, (r.content-r.capacity)/pipes[i].flow);
			}
			if (pipes[i].end instanceof Reservoir)
			{
				Reservoir r = (Reservoir)pipes[i].end;
				double newContent = r.content + INTEGRATION_TIME * pipes[i].flow;
				if (newContent<-ZERO_CONTENT)				
					clampedIntegrationTime = Math.min(clampedIntegrationTime, -r.content/pipes[i].flow);				
				if (newContent > (r.capacity+ZERO_CONTENT))					
					clampedIntegrationTime = Math.min(clampedIntegrationTime, (r.capacity-r.content)/pipes[i].flow);
			}
		}
		
		if (clampedIntegrationTime<0)
			System.out.println("Error");
		
		// integration
		for(int i = 0;i<pipes.length;i++)
		{
			pipes[i].flow = (float)s.get(i,0);
			if (pipes[i].start instanceof Reservoir)
			{
				Reservoir r = (Reservoir)pipes[i].start;
				r.content -= clampedIntegrationTime * pipes[i].flow;				
			}
			if (pipes[i].end instanceof Reservoir)
			{
				Reservoir r = (Reservoir)pipes[i].end;
				r.content += clampedIntegrationTime * pipes[i].flow;				
			}
		}
		time += clampedIntegrationTime;

	}
	
	void printState() {
		System.out.print("T\t" + time + "\t");
		for (int i = 0;i<nodes.length;i++)
		{
			if (nodes[i] instanceof Reservoir)
			{	
				Reservoir r = (Reservoir)nodes[i];
				System.out.print("R"+i + "\t" + r.level() + "\t" + r.content + "\t");			
			}
		}
		for(int i = 0;i<pipes.length;i++) {
			System.out.print("P"+i+"\t" + pipes[i].flow + "\t");
		}		
		System.out.println();
	}	

	private void setDefaultConfig1() {
		nodes = new NetworkNode[4];
		nodes[0] = new Reservoir(100, 100, 20, 25, new Point2D.Double(0,0));
		nodes[1] = new Reservoir(100, 0, 18, 20, new Point2D.Double(100,0));
		nodes[2] = new Reservoir(200, 0, 17, 19, new Point2D.Double(0,100));
		nodes[3] = new NetworkNode(new Point2D.Double(50, 50));
		pipes = new Pipe[3];
		pipes[0] = new Pipe(nodes[0],nodes[3], 10.0, 0.1);
		pipes[1] = new Pipe(nodes[1],nodes[3], 10.0, 0.2);
		pipes[2] = new Pipe(nodes[2],nodes[3], 10.0, 0.3);
	}
	
	private void setDefaultConfig2() {
		nodes = new NetworkNode[2];
		nodes[0] = new Reservoir(100, 100, 20, 25, new Point2D.Double(0,0));
		nodes[1] = new Reservoir(100, 0, 20, 25, new Point2D.Double(100,0));
		
		pipes = new Pipe[1];
		pipes[0] = new Pipe(nodes[0],nodes[1], 1.0, 1.0);		
	}
	
	private void setDefaultConfig3() {
		nodes = new NetworkNode[3];
		nodes[0] = new Reservoir(150, 150, 20, 25, new Point2D.Double(0,0));
		nodes[1] = new Reservoir(150, 0, 18, 20, new Point2D.Double(100,0));
		nodes[2] = new Reservoir(10, 0, 17, 30, new Point2D.Double(0,100));
		//nodes[3] = new NetworkNode(new Point2D.Double(50, 50));
		pipes = new Pipe[2];
		pipes[0] = new Pipe(nodes[0],nodes[1], 1.0, 0.3);
		pipes[1] = new Pipe(nodes[0],nodes[2], 1.0, 1.0);
		//pipes[2] = new Pipe(nodes[2],nodes[3], 10.0, 1.0);
	}
	
	private void setDefaultConfig5() {
		nodes = new NetworkNode[8];
		nodes[0] = new Reservoir(150, 150, 20, 25, new Point2D.Double(0,0));
		nodes[1] = new Reservoir(50, 0, 10, 12, new Point2D.Double(100,300));
		nodes[2] = new Reservoir(50, 0, 10, 12, new Point2D.Double(200,300));
		nodes[3] = new Reservoir(50, 0, 10, 12, new Point2D.Double(300,300));
		nodes[4] = new Reservoir(50, 0, 19, 21, new Point2D.Double(400,100));
		nodes[5] = new NetworkNode(new Point2D.Double(100,200));
		nodes[6] = new NetworkNode(new Point2D.Double(200,200));
		nodes[7] = new NetworkNode(new Point2D.Double(300,200));
		pipes = new Pipe[7];
		pipes[0] = new Pipe(nodes[0],nodes[5], 1.0, 0.1);
		pipes[1] = new Pipe(nodes[5],nodes[6], 1.0, 1.0);
		pipes[2] = new Pipe(nodes[6],nodes[7], 1.0, 1.0);
		pipes[3] = new Pipe(nodes[7],nodes[4], 1.0, 1.0);
		pipes[4] = new Pipe(nodes[5],nodes[1], 0.1, 0.1);
		pipes[5] = new Pipe(nodes[6],nodes[2], 0.1, 0.1);
		pipes[6] = new Pipe(nodes[7],nodes[3], 0.1, 0.1);
	}
	
	private void setDefaultConfig4() {
		nodes = new NetworkNode[6];
		nodes[0] = new Reservoir(150, 120, 20, 25, new Point2D.Double(0,0));
		nodes[1] = new Reservoir(50, 0, 10, 12, new Point2D.Double(100,200));
		nodes[2] = new Reservoir(50, 0, 10, 12, new Point2D.Double(200,200));
		nodes[3] = new Reservoir(50, 0, 19, 20, new Point2D.Double(400,50));
		nodes[4] = new NetworkNode(new Point2D.Double(100,100));
		nodes[5] = new NetworkNode(new Point2D.Double(200,100));
		pipes = new Pipe[5];
		pipes[0] = new Pipe(nodes[0],nodes[4], 1.0, 0.1);
		pipes[1] = new Pipe(nodes[4],nodes[5], 1.0, 1.0);
		pipes[2] = new Pipe(nodes[5],nodes[3], 1.0, 1.0);
		pipes[3] = new Pipe(nodes[4],nodes[1], 0.1, 0.1);
		pipes[4] = new Pipe(nodes[5],nodes[2], 0.1, 0.1);

	}
	
}
