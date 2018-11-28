package generator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import objects.Factory;
import objects.Path;
import objects.Planet;
import objects.SpaceObject;

public class Loader {
	/**
	 * Map of all planets in galaxy
	 */
	private final Map<String, Planet> galaxy = new HashMap<String, Planet>();
	/**
	 * Map of all factories in galaxy
	 */
	private final Map<String, Factory> factoryMap = new HashMap<String, Factory>();
	/**
	 * Name of default file for loading planets
	 */
	private static final String PLANETS =  "DefaultSpaceProgramPlanets.txt";
	/**
	 * Name of default file for loading neighbors.
	 */
	private static final String NEIGHBOURS = "DefaultSpaceProgramNeighbours.txt";
	/**
	 * String - information about population on planets.
	 */
	private String populationInfo = "";

	/**
	 * Constructor Loader
	 * Call constructor with parameters.
	 */
	public Loader(){
		this(PLANETS, NEIGHBOURS);
	}
	
	/**
	 * Constructor Loader
	 * Fill map galaxy and Map factory
	 * @param spaceObjects Name of input planet file
	 * @param neighbours Name of input factory name
	 */
	public Loader(String spaceObjects, String neighbours) {
		loadSpaceObjects(spaceObjects);
		loadNeighbours(neighbours);
		loadInformation();
	}
	
	/**
	 * Method getSpaceOjects
	 * @return Map with space object (factory + planets)
	 */
	public Map<String, SpaceObject> getSpaceObjects(){
		Map<String, SpaceObject> spaceObjectMap = new HashMap<String, SpaceObject>();
		spaceObjectMap.putAll(galaxy);
		spaceObjectMap.putAll(factoryMap);
		return spaceObjectMap;
	}
	
	/**
	 * Method getPlanets
	 * Get for Map with all planets
	 * @return Map with planets
	 */
	public Map<String, Planet> getPlanets(){
		return galaxy;
	}
	
	/**
	 * Method getFactoryMap
	 * Get Map with all factory in galaxy
	 * @return Map with factories
	 */
	public Map<String, Factory> getFactoryMap(){
		return factoryMap;
	}
	
	/**
	 * Method getPopulationInfo
	 * Get information about gaussian distribution.
	 * @return String with information about population.
	 */
	public String getPopulationInfo(){
		return populationInfo;
	}
	
	/**
	 * Method loadSpaceObjects
	 * Loading all space objects from text file and fill maps.where is planet safe
	 * @param file	name of the file 
	 */
	private void loadSpaceObjects(String file){
		try{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);
			
			loadFactory(sc);
			loadPlanets(sc);

			sc.close();
			br.close();
			fr.close();
				
		}catch(Exception e){
			System.out.println("File not found: SpaceProgramPlanets.txt");
		}
	}
	
	/**
	 * Method load factory - Load factory from text.
	 * This method fill Map with factories.
	 * @param sc  Scanner for loading
	 */
	private void loadFactory(Scanner sc){
		String factoryLine = sc.nextLine();
		
		while(!(factoryLine.equals("#"))){
			String [] split = factoryLine.split(",");
			
			try{
				double x = Double.parseDouble(split[1]);
				double y = Double.parseDouble(split[2]);
				Factory factory = new Factory(split[0], x, y);
				factoryMap.put(split[0], factory);
				
			}catch(Exception e){
				System.out.println("Data can't be loaded");
			}
			factoryLine = sc.nextLine();
		}
	}
	
	/**
	 * Method load planets - Load planets from text.
	 * This method fill Map with planets.
	 * @param sc  Scanner for loading
	 */
	private void loadPlanets(Scanner sc){
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			String [] split = line.split(",");
			
			try{
				double x = Double.parseDouble(split[1]);
				double y = Double.parseDouble(split[2]);
				int population = Integer.parseInt(split[3]);
				galaxy.put(split[0], new Planet(split[0], x, y, population));
			}catch(Exception e){
				System.out.println("Data can't be loaded");
			}
		}
	}
	
	/**
	 * Method load neighbors
	 * Load neighbors from text file and assign neighbors to the planets or factory.
	 * @param file Name of input file.
	 */
	private void loadNeighbours(String file){
		String neighbour = null;
		try{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);
			
			while(sc.hasNextLine()){
				String planetName = sc.nextLine();
				neighbour = sc.nextLine();
	
				while(!(neighbour.equals("#"))){
					try{
						String split [] = neighbour.split(",");
						asignNeighbours(split, planetName);
						
					}catch(Exception e){
						System.out.println("Data can't be loaded");
						System.exit(0);
					}
					neighbour = sc.nextLine();
				}
			}
			sc.close();
			br.close();
			fr.close();
			
		}catch(Exception e){
			System.out.println("File not found: SpaceProgramNeghbours.txt");
		}
	}
	
	/**
	 * Method asignNeighbours
	 * Assign neighbor to the factory or planet.
	 * @param split Array with split data from text about neighbor.
	 * @param planetName Planet name
	 */
	private void asignNeighbours(String split [], String planetName){
		double length = Double.parseDouble(split[1]);
		boolean danger = Boolean.parseBoolean(split[2]);
		
		if(factoryMap.containsKey(planetName)&&factoryMap.containsKey(split[0])){
			factoryMap.get(planetName).addNeghbour(new Path(length, factoryMap.get(split[0]), danger));	
		}
		if(factoryMap.containsKey(planetName) && galaxy.containsKey(split[0])){
			factoryMap.get(planetName).addNeghbour(new Path(length, galaxy.get(split[0]), danger));	
		}
		if(galaxy.containsKey(planetName) && factoryMap.containsKey(split[0])){
			galaxy.get(planetName).addNeghbour(new Path(length, factoryMap.get(split[0]), danger));	
		}
		if(galaxy.containsKey(planetName) && galaxy.containsKey(split[0])){
			galaxy.get(planetName).addNeghbour(new Path(length, galaxy.get(split[0]), danger));
		}
	}

	/**
	 * Method loadInformation
	 * Loading data about gaussian distribution from text file
	 * Result is in populationInfo.
	 */
	private void loadInformation(){
		try{
			FileReader fr = new FileReader("SpaceProgramGaussDistribution.txt");
			BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);
			
			String info = "";
			
			while(sc.hasNextLine()){
				info += sc.nextLine();
			}
			
			String split[] = info.split("#");
			
			for(int i = 0; i < split.length; i++){
				populationInfo += split[i]+"\n";
			}
			sc.close();
			br.close();
			fr.close();
			
		}catch(Exception e){
			System.out.println("File not found: SpaceProgramGaussDistribution.txt");
		}
	}
}
