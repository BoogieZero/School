package objects.generator;
import java.util.ArrayList;
import java.util.List;
import objects.SpaceObject;

/**
 * Class FactoryForDijkstra
 * is factory with shortest route to planet and distance.
 * @author Jiří Lukáš
 */
public class FactoryForDijkstra implements Comparable<FactoryForDijkstra>{
	/**
	 * Name of factory
	 */
	private final String name;
	/**
	 * Distance (factory to planet)
	 */
	private final double distance;
	/**
	 * List of shortest route (factory to planet)
	 */
	private final List<SpaceObject> shortestRoute = new ArrayList<SpaceObject>();
	
	/**
	 * Constructor 
	 * @param name Name of factory
	 * @param distance Distance (factory to planet)
	 */
	public FactoryForDijkstra(String name, double distance) {
		this.distance = distance;
		this.name = name;
	}
	
	/**
	 * Method addToShortestRoute
	 * Adding space object to list of shortest route
	 * @param o Space object which I want to add.
	 */
	public void addToshortestRoute(SpaceObject o){
		shortestRoute.add(o);
	}
	
	@Override
	public int compareTo(FactoryForDijkstra o) {
		double result = this.distance - o.distance;
		return (int) result;
	}

	/**
	 * Method getDistance
	 * Getter for distance (factory - planet)
	 * @return distance Distance (factory - planet)
	 */
	public double getDistance() {
		return distance;
	}
	
	
	/**
	 * Method getName
	 * @return name Name of factory
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method getShortestRoute
	 * @return shortestRoute List of shortest route (factory - planet)
	 */
	public List<SpaceObject> getShortestRoute() {
		return shortestRoute;
	}
}
