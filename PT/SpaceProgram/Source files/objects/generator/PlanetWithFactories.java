package objects.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class Planet with factories
 * Planet with two lists one for 5 factories without pirates and one for 5 factories with pirates.
 * @author Jiří Lukáš
 */
public class PlanetWithFactories {
	/**
	 * Planet name
	 */
	private final String name;
	/**
	 * List of 5 factories without pirates
	 */
	private final List <FactoryForDijkstra>  normal = new ArrayList<FactoryForDijkstra>();
	/**
	 * List of 5 factories with pirates
	 */
	private final List <FactoryForDijkstra>  pirates = new ArrayList<FactoryForDijkstra>();
 
	/**
	 * Constructor
	 * @param name Name of Planet
	 */
	public PlanetWithFactories(String name) {
		this.name = name;
	}
	
	/**
	 * Method addToListNormal
	 * Adding factory to list of factories without pirates.
	 * Sorting list of factories.
	 * @param factory Added factory.
	 */
	public void addToListNormal(FactoryForDijkstra factory){
		normal.add(factory);
		Collections.sort(normal);
	}
	
	/**
	 * Method addToListPirates
	 * Adding factory to list of factories with pirates.
	 * Sorting list of factories. 
	 * @param factory Added factory.
	 */
	public void addToListPirates(FactoryForDijkstra factory){
		pirates.add(factory);
		Collections.sort(pirates);
	}

	/**
	 * Method getName
	 * Getter name of planet with factories.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method getNormal
	 * Returning list of factories without pirates.
	 * @return normal  List of factories without pirates
	 */
	public List<FactoryForDijkstra> getNormal() {
		return normal;
	}

	/**
	 * Method getPirates
	 * Returning list of factories with pirates.
	 * @return pirates List of factories with pirates.
	 */
	public List<FactoryForDijkstra> getPirates() {
		return pirates;
	}
}
