package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import generator.Loader;
import objects.Factory;
import objects.SpaceObject;
import objects.generator.FactoryForDijkstra;
import objects.generator.PlanetWithFactories;
import objects.generator.Vertex;

/**
 * Class Dijkstra Controller 
 * creating planet with factories map. This is Map with all planets 
 * and to each planet is added 5 factories (pirates, without pirates).
 * @author Jiří Lukáš
 *
 */
public class DijkstraController {
	/**
	 * Map with all planets and to each planet is added 5 factories (pirates, without pirates).
	 */
	private final Map<String, PlanetWithFactories> listPlanetWithFactories = new HashMap<String, PlanetWithFactories>();
	/**
	 * Instance of dijkstra for shortest routes
	 */
	private Dijkstra dijkstra;
	/**
	 * Map of all space objects in galaxy
	 */
	private final Map<String, SpaceObject> galaxy;
	/**
	 * List of vertex (vertex is a space object with distance and name of ancestor)
	 */
	private final List<Vertex> listOfVertex = new ArrayList<Vertex>();
	

	/**
	 * Constructor
	 * 1) fill galaxy from Space object list from loader
	 * 2) fill factories Factory list from loader
	 * 3) fill list planets with factories by method fill list planet
	 * 4) Start Dijkstra for all factories one "for" for pirates route and another for route without pirates
	 * 
	 * @param loader Loader with factories map.
	 */
	public DijkstraController(Loader loader) {
		this.galaxy = loader.getSpaceObjects();
		
		Map<String, Factory> factories;
		factories = loader.getFactoryMap();
		
		fillListPlanet();
		
		for(Factory factory : factories.values()){
			startDijkstra(factory, false);
		}
		
		for(Factory factory : factories.values()){
			startDijkstra(factory, true);
		}
	}
	
	/**
	 * Method getRouteTo 
	 * Returning route to planet like list of space objects.  
	 * @param name Name of final planet on route.
	 * @param index Index of factory which is used.
	 * @param isPirates If true return list planet with pirates. If not return list planet without pirates.
	 * @return Return shortest route to planet
	 */
	public List<SpaceObject> getRouteTo(String name, int index, boolean isPirates){
		try{
			if(isPirates){
				return new ArrayList<SpaceObject>(listPlanetWithFactories.get(name).getPirates().get(index).getShortestRoute());
			}else{
				return new ArrayList<SpaceObject>(listPlanetWithFactories.get(name).getNormal().get(index).getShortestRoute());
			}
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * Method getValueOfRouteTo
	 * Returning value of the shortest route to planet.
	 * @param name Name of planet
	 * @param index Index of factory which is used.
	 * @param isPirates If true return list planet with pirates. If not return list planet without pirates.
	 * @return Return value of shortest route to planet
	 */
	public double getValueOfRouteTo (String name, int index, boolean isPirates){
		try{
			if(isPirates){
				return listPlanetWithFactories.get(name).getPirates().get(index).getDistance();
			}else{
				return listPlanetWithFactories.get(name).getNormal().get(index).getDistance();
			}
		}catch(Exception e){
			return Double.MAX_VALUE;
		}
	}
	
	/**
	 * Method getRouteTo
	 * Returning route to planet like list of space objects.
	 * @param name Name of planet
	 * @param factoryName Name of factory which is used.
	 * @param isPirates If true return list planet with pirates. If not return list planet without pirates.
	 * @return Return shortest route to planet
	 */
	public  List<SpaceObject> getRouteTo (String name, String factoryName, boolean isPirates){
		try{
			List<SpaceObject> factory = null;
			if(isPirates){
				for(FactoryForDijkstra ffd : listPlanetWithFactories.get(name).getPirates()){
					if(ffd.getName().equals(factoryName)){
						factory = ffd.getShortestRoute();
						return new ArrayList<SpaceObject>(factory);
					}
				}
			}else{
				for(FactoryForDijkstra ffd : listPlanetWithFactories.get(name).getNormal()){
					if(ffd.getName().equals(factoryName)){
						factory =  ffd.getShortestRoute();
						return new ArrayList<SpaceObject>(factory);
					}
				}
			}
		}catch(Exception e){
			System.out.println("Factory doesn't exist getRouteTo");
			System.exit(0);
		}
		return null;
	}
	
	/**
	 * Method getValueRouteTo
	 * Returning value of the shortest route to planet.
	 * @param name Name of planet
	 * @param factoryName Name of factory which is used.
	 * @param isPirates If true return list planet with pirates. If not return list planet without pirates.
	 * @return Return value of shortest route to planet
	 */
	public  double  getValueOfRouteTo (String name, String factoryName, boolean isPirates){
		try{
			if(isPirates){
				for(FactoryForDijkstra ffd : listPlanetWithFactories.get(name).getPirates()){
					if(ffd.getName().equals(factoryName)){
						return ffd.getDistance();
					}
				}
			}else{
				for(FactoryForDijkstra ffd : listPlanetWithFactories.get(name).getNormal()){
					if(ffd.getName().equals(factoryName)){
						return ffd.getDistance();
					}
				}
			}
		}catch(Exception e){
			System.out.println("Factory doesn't exist getValueOfRouteTo");
			System.exit(0);
		}
		return Double.MAX_VALUE;
	}
	
	
	/**
	 * Method startDijkstra
	 * starting Dijkstra for selected factory
	 * @param factory selected factory
	 * @param pirates if true start Dijkstra for pirates route. If not start Dijkstra for not pirates route
	 */
	private void startDijkstra(SpaceObject factory, boolean pirates){
		dijkstra = new Dijkstra(pirates);
		dijkstra.doDijkstra(factory);
		addFactoryToPlanet(factory.getName(), pirates);
	}
	
	
	
	/**
	 * Method fillListPlanet
	 * Create galaxy from Planet with factories and save this to list planet with factories.
	 */
	private void fillListPlanet(){
		for(SpaceObject sp : galaxy.values()){
			PlanetWithFactories pl = new PlanetWithFactories(sp.getName());
			listPlanetWithFactories.put(sp.getName(), pl);
		}
	}
	
	/**
	 * Method addFactoryToPlanet
	 * @param factoryName Name of factory
	 * @param pirates if this factory is adding to list pirates or normal
	 */
	private void addFactoryToPlanet(String factoryName, boolean pirates){
		for(Vertex vertex : dijkstra.getVertexMap().values()){
			if(!vertex.getName().equals(factoryName)){
				Vertex actual = vertex;
				double distance = vertex.getDistace();
				
	
				while(actual != null){ 
					listOfVertex.add(actual);
					actual = dijkstra.getVertexMap().get(actual.getNameOfAncestor());	
				}
				Collections.reverse(listOfVertex);
				
				if(pirates){
					listPlanetWithFactories.get(vertex.getName()).addToListPirates(createFactory(factoryName, distance)); 
					listOfVertex.clear();
				}else{
					listPlanetWithFactories.get(vertex.getName()).addToListNormal(createFactory(factoryName, distance)); 
					listOfVertex.clear();
					
				}
			}
		}
	}
	/**
	 * Method createFactory
	 * creating factory and adding shortest route to planet
	 * @param name  Factory name
	 * @param distance Distance of this shortest route
	 * @return Factory with shortest route
	 */
	private FactoryForDijkstra createFactory(String name, double distance){
		FactoryForDijkstra factory = new FactoryForDijkstra(name, distance);
		for(Vertex vertex : listOfVertex){
			factory.addToshortestRoute(galaxy.get(vertex.getName()));
		}
		return factory;	
	}
}
