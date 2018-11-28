package data;
import java.util.HashMap;
import java.util.Map;
import function.Heap;
import objects.Path;
import objects.SpaceObject;
import objects.generator.Vertex;

/**
 * Class Dijkstra
 * This Class calculate shortest route between factory and all planets. 
 * Vertex Map is for save this information. Every vertex contains name of ancestor
 * because if I am searching for shortest route I need go over this map.
 * I get shortest route if I select planet from vertex map and by name of ancestor go to the factory.
 *
 * @author Jiří Lukáš
 *
 */
public class Dijkstra {
	/**
	 * Map of shortest route between factory and all plants
	 */
	private final Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
	/**
	 * Dijkstra is implements by min heap
	 */
	private final Heap minHeap = new Heap(Heap.TypeOfHeap.MIN);
	/**
	 * if Dijkstra can calculate with dangers route.
	 */
	private final boolean pirates;


	/**
	 * Constructor
	 * @param pirates if Dijkstra can calculate with dangers route.
	 */
	public Dijkstra(boolean pirates) {
		this.pirates = pirates;
	}
	

	/**
	 * Method doDijkstra
	 * starting Dijkstra process (Searching for shortest route between factory and all planets)
	 * @param factory The factory 
	 */
	public void doDijkstra(SpaceObject factory){
		Vertex vertex = new Vertex(factory);
		vertex.setDistace(0);
		minHeap.addToHeap(vertex, 0);
	
		while(!minHeap.isEmpty()){
			Vertex actual = (Vertex) minHeap.getTop();
			vertexMap.put(actual.getName(), actual);
			calculateDistanceForNeighbours(actual);
		
		}
	}
	

	/**
	 * Method calculateDistanceForNeighbours
	 * @param vertex Actual vertex where we need calculate distance for his neighbors.
	 * I pick one neighbor then I calculate distance between vertex and this neighbor.
	 * If this neighbor is not in heap and vertex map I create new vertex for him and this neighbor
	 * is added to heap.
	 */
	private void calculateDistanceForNeighbours(Vertex vertex){
		for(Path path : vertex.getSpaceObject().getNeighbours().values()){
			if(path.isDanger()&& !pirates){
				continue;
			}
			
			double distance = vertex.getDistace() + path.getLength();
			
			if(minHeap.contains(path.getTarget().getName())){
				Vertex vertexInHeap = (Vertex) minHeap.readElement(path.getTarget().getName());
				if(distance < vertexInHeap.getDistace()){
					minHeap.getElement(vertexInHeap.getName());
					vertexInHeap.setDistace(distance);
					vertexInHeap.setNameOfAncestor(vertex.getName());
					minHeap.addToHeap(vertexInHeap, distance);
				}
				continue;
			}
			if(!vertexMap.containsKey(path.getTarget().getName())){
				Vertex newVertex = new Vertex(path.getTarget());
				newVertex.setDistace(distance);
				newVertex.setNameOfAncestor(vertex.getName());
				minHeap.addToHeap(newVertex, distance);
			}
		}
	}
	/**
	 * Method getVertexMap
	 * @return Map of vertex (shortest route between factory and planet)
	 */
	public Map<String, Vertex> getVertexMap(){
		return vertexMap;
	}
}
