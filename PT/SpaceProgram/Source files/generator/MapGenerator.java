package generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import function.TimeHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import objects.Path;
import objects.Planet;
import objects.generator.PlanetWithDistace;

/**
 * Class MapGenerator
 * In this class we generate planets in galaxy. This planets are saved in listOfPlanets (ArrayList).
 * These planets are generated on rods. We have 360 rods and 5040/360 planets on one rod.
 * We generate planets on rods randomly and then we sorting these planet. If some planets has distance is less than 2 then we changed
 * this distance and rebuild this list of planet on one rod.
 * After that we adding 5 nearest neighbors to these planets.
 * 5 planets are factories.
 * All information are in text file "SpaceProgramPlanets.txt" and "SpaceProgramNeighbours.txt"
 * @author Jiří Lukáš
 *
 */
public class MapGenerator {
	
	/**
	 * Minimal distance from center of galaxy
	 */
	private final double MINDISTANCE = 114.6;
	/**
	 * RandomSET for generate planets on rods
	 */
	private final int RANDOMSET = 400;
	/**
	 * Final count of planets
	 */
	private final int ALLPLANETS = 5040;
	/**
	 * Maximum steps for generate rods
	 */
	private final double MAXIMUMSTEPS = 360;
	/**
	 * 
	 */
	private final double MINDIFFERENCE = 2.1;
	/**
	 * Stepping
	 */
	private final int STEP = 1;
	/**
	 * Pirates for calculate all pirates in galaxy
	 */
	private final int PIRATES = 5;
	/**
	 * Population peak 
	 */
	private final int POPULATIONPEAK = 3000000;
	/**
	 * Count of steps
	 */
	private int start = 0;
	/**
	 * Random generator for adding planets to rode
	 */
	private final Random random = TimeHandler.getRandom();
	/**
	 * Gauss distribution for name planets and population on planets
	 */
	private final GaussDistribution GD = new GaussDistribution(ALLPLANETS, POPULATIONPEAK);;
	/**
	 * Array list of planets on one rode
	 */
	private final List<Planet> listOfPlanets = new ArrayList<Planet>();
	/**
	 * Array list of all spaceOjects in galaxy
	 */
	private final List<Planet> galaxy = new ArrayList<Planet>();
	/**
	 * Planets with distance 
	 */
	private final List<PlanetWithDistace> planetsWithDistance = new ArrayList<PlanetWithDistace>();
	/**
	 * Planets with distance and 5 neighbors
	 */
	private final List<PlanetWithDistace> planetsNeighbours = new ArrayList<PlanetWithDistace>();
	

	/**
	 * Method generate
	 * creating planets, assigns neighbors to planets.
	 * creating output data.
	 */
	public void generate(){
		
		double numberOfStepsDouble = MAXIMUMSTEPS/STEP;
		int numberOfSteps = (int) Math.round(numberOfStepsDouble);
		double planetsOnStepDouble = ALLPLANETS/(numberOfStepsDouble);
		int planetsOnStep = (int)Math.round(planetsOnStepDouble);
	
		for(int i = 0; i < numberOfSteps; i++ ){
				createPlanets(planetsOnStep);
				createCoordinates(GD);
				start += STEP;
		}
		
		txt1Writer();
		createNeighbours(createPirateArray());
		txt2Writer();
		txt3Writer();
	}
	
//----------------------------------------Create planets-------------------------------------------------------------
	/**
	 * Method create planets
	 * Creating planets on the rod with method createPlanet.
	 * All planets are sorted by distance (x is used for save distance).
	 * Last "for" is for respecting minimum distance between planets. 
	 * @param planetsOnStep - How much planets are on one rod.
	 */
	private void createPlanets(int planetsOnStep){
		for(int i = 0; i < planetsOnStep; i++){
			listOfPlanets.add(createPlanet());
		}
	
		Collections.sort(listOfPlanets);
		
		Planet old = null;
		for(Planet p: listOfPlanets){
			if(old == null){
				old = p;
			}else{
				double difference = MINDIFFERENCE - (p.getX() - old.getX());
				if(difference < 0){
					old = p;
				}else{
					p.setX(p.getX()+difference);
					old = p;
				}
			}
		}
	}
	
	/**
	 * This method create planet
	 * This planet has x = distance on rod, y = angle of rod
	 * @return	created planet
	 */
	private Planet createPlanet(){
		double distance = MINDISTANCE + random.nextInt(RANDOMSET);
		Planet planet = new Planet("jupiter", distance, start, 0);
		return planet;
	}
//-------------------------------------------------x y Coordinates---------------------------------------------------------------------
	
	/**
	 * Method createCoordinates
	 * Now I have list of planets with planet (name, distance, step, 0)
	 * This method calculate x a y and set those to the planet.
	 * Name and population are from class GauseDistribution. 
	 * @param gd GaussDistribution for name and population.
	 */
	private void createCoordinates(GaussDistribution gd){
		int factoryStep = 72;
		
		double x = 0;
		double y = 0;
		String planetName = "";
		
		for(Planet p: listOfPlanets){
			x = p.getX() * Math.cos(Math.toRadians(start)); 
			y = p.getX() * Math.sin(Math.toRadians(start));
			planetName = gd.getPlanetName();
			
			if(planetName.substring(5, 8).equals("001") && start%factoryStep == 0){
				String factoryName = "F"+ planetName.substring(1);
				planetName = factoryName;
			}
			p.setX(x);
			p.setY(y);
			p.setName(planetName);
			p.setPopulation(gd.getNextPopulation());
		}
		
		galaxy.addAll(listOfPlanets);
		listOfPlanets.clear();
		gd.nextPlanetLine();
	}
//------------------------------------------------Neighbours --------------------------------------------------------------------------	
	
	/**
	 * Method createNeighbors
	 * creating neighbors for all planets in galaxy.
	 * 1) Create Planet with distance list
	 * 2) Choosing planet with distance one by one and adding neighbors to them.
	 * 3) Set up boolean if route to neighbor is pirate
	 * @param piratesArray array of pirates routes
	 */
	private void createNeighbours(int [] piratesArray){
		
		int pirateCount = 0;
		int counter = 0;
		
		createPlanetWithDistance();

		for(PlanetWithDistace pwdA : planetsWithDistance ){
			if(pwdA.getPlanet().getNeighbours().size() >= 5){
				continue;
			}

			addNeighbors(pwdA);

			Collections.sort(planetsNeighbours);
			
			for(PlanetWithDistace pwd : planetsNeighbours){
				if(pwd.getDistance() != 0){
					if(counter == piratesArray[pirateCount]){
						pwdA.getPlanet().addNeghbour(new Path(pwd.getDistance(), pwd.getPlanet(), true));
						pwd.getPlanet().addNeghbour(new Path(pwd.getDistance(), pwdA.getPlanet(), true));
						counter++;
		
						if(pirateCount < piratesArray.length-1){
							pirateCount++;
						}	
						
					}else{
						pwdA.getPlanet().addNeghbour(new Path(pwd.getDistance(), pwd.getPlanet(),false));
						pwd.getPlanet().addNeghbour(new Path(pwd.getDistance(), pwdA.getPlanet(),false));
						counter++;
					}
				}
				if(pwdA.getPlanet().getNeighbours().size() >= 5 || pwd == planetsNeighbours.get(planetsNeighbours.size()-1)){
					planetsNeighbours.clear();
					break;
				}
			}
		}
	}
	
	/**
	 * Method createPlanetWithDistance
	 * creating planetWithDistance from list of all planets in galaxy
	 */
	private void createPlanetWithDistance(){
		for(Planet p : galaxy){
			planetsWithDistance.add(new PlanetWithDistace(p, 0));
		}
	}
	
	/**
	 * Method addNeighbors
	 * adding 5 neighbors to the actual planet
	 * @param pwdA actual planet without neighbors
	 */
	private void addNeighbors(PlanetWithDistace pwdA){
		for(PlanetWithDistace pwdB : planetsWithDistance){
			int neighboursCountSelected = pwdB.getPlanet().getNeighbours().size();
			boolean isActualContainsNeighbour = pwdA.getPlanet().getNeighbours().containsKey(pwdB.getPlanet().getName());
			
			if(neighboursCountSelected < 5 && !isActualContainsNeighbour){
				double distance = Math.sqrt(Math.pow(pwdB.getPlanet().getX() - pwdA.getPlanet().getX(), 2) + Math.pow(pwdB.getPlanet().getY() - pwdA.getPlanet().getY(), 2));
				pwdB.setDistance(distance);
				planetsNeighbours.add(pwdB);
			}		
		}
	}
//------------------------------------------------------PIRATES--------------------------------------------------------------	
	/**
	 * Method createPirateArray
	 * First calculate all routes to the double pirates
	 * piratesPercent pirates * 0,2 only 20% of those routes are pirates. This is for pirates array length.
	 * PiratesArray contains random sorted numbers of pirates routes. 
	 * @return pirates array
	 */
	private int [] createPirateArray(){
		double pirates = ((double)ALLPLANETS * (double)PIRATES)/2.0;
		int allPirates = (int) Math.round(pirates);
		int piratesPercent = (int) (pirates * 0.2);
	
		
		int [] piratesArray = new int [piratesPercent];
		
		for(int i = 0; i < piratesArray.length; i++){
			piratesArray[i] = random.nextInt(allPirates);
		}

		Arrays.sort(piratesArray);	                        
		
		for(int j = 0; j < piratesArray.length-1; j++ ){
			if(!(piratesArray[j+1]>piratesArray[j])){
				piratesArray[j+1] += (piratesArray[j] - piratesArray[j+1]) +1; 
			}
		}
		return piratesArray;
	}
//--------------------------------------Output-------------------------------------------------------------------------------

	/**
	 * Method txt1Writer
	 * Creating SpaceprogramPlanets.txt on disk.
	 */
	private void txt1Writer(){
		try{
			FileWriter fw = new  FileWriter(new File("SpaceProgramPlanets.txt"));
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			
			for(Planet planet : galaxy){
				if(planet.getName().substring(0, 1).equals("F")){
					pw.println(planet);
				}
			}
			pw.println("#");
			
			for(Planet p : galaxy){
				if(!p.getName().substring(0, 1).equals("F")){
					pw.println(p);
				}
			}
			pw.close();
			bw.close();
			fw.close();

				
		}catch(Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error in print into tho SpaceProgramPlanets.txt");
			alert.showAndWait();
		}	
	}

	/**
	 * Method txt2Writer
	 * Creating SpaceProgramNeighbours on disk.
	 */
	private void txt2Writer(){
		try{
			
			FileWriter fw = new  FileWriter(new File("SpaceProgramNeighbours.txt"));
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			
			for(PlanetWithDistace planet : planetsWithDistance){
				pw.println(planet.getPlanet().getName());
				for(Path neighbour: planet.getPlanet().getNeighbours().values()){
					pw.println(neighbour.getTarget().getName()+","+neighbour.getLength()+","+neighbour.isDanger());
				}
				pw.println("#");
			}
			pw.close();
			bw.close();
			fw.close();
		
		}catch(Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error in print into tho SpaceProgramNeighbours.txt");
			alert.showAndWait();

		}	
	}
	
	/**
	 * Method txt3Writer
	 * Creating SpaceProgramGaussDistribution on disk.
	 * Information about Gaussian distribution (population) on planets.
	 */
	private void txt3Writer(){
		try{
			FileWriter fw = new  FileWriter(new File("SpaceProgramGaussDistribution.txt"));
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.print(GD.getReport());
			
			pw.close();
			bw.close();
			fw.close();
					
		}catch(Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Error in print into tho SpaceProgramGaussDistribution.txt");
			alert.showAndWait();

		}
	}
	
	
//-----------------------------------------------------------------------------------------------------------------------------	
	
}