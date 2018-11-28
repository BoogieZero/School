/**
 * 
 */
package function;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import generator.Loader;
import objects.Factory;
import objects.Planet;
import objects.Route;
import objects.Ship;
import objects.TimeStamp;

/**
 * Class for final output Statistics.txt
 * 
 * @author Jiří Lukáš
 *
 */
public class DataWriter {
	
	/**
	 * Loader
	 * In this class I need information about all planets and ships.
	 * I get this information from loader.
	 */
	private final Loader LOADER;
	
	/**
	 * Unloaded medicine in the galaxy
	 */
	private long unloadedMedicine = 0;
	
	/**
	 * All orders from planets
	 */
	private long allOrders = 0;
	
	/**
	 * All material from ships delivered in galaxy
	 */
	private long materialDistributed = 0;

	/**
	 * Constructor without parameters
	 * Getting loader.
	 * @param loader	loader for getting information about all SpaceObjects
	 */
	public DataWriter(Loader loader) {
		this.LOADER = loader;
	}
	
	
	/**
	 * Method monthProcedure
	 * creating statistics about planets and ships for one month.
	 */
	public void monthProcedure(){
		monthProcedurePlanets();
		monthProcedureShips();
	}
	
	/**
	 * Method fourthMonthProcedure
	 * creating statistics about planets and ships for four months.
	 */
	public void quarterProcedure(){
		fourthMonthProcedurePlanets();
		fourthMonthProcedureShips();
	}
	
	/**
	 * Method write
	 * Creating output files
	 */
	public void write(){
		writePlanets("ResultReport\\PlanetStatistics.txt");
		writeShips("ResultReport\\ShipsStatistics.txt");
		writeShipsRoute("ResultReport\\ShipsRouteStatistics.txt");
	}
	
	
	/**
	 * Method monthProcedurePlanets
	 * creating statistics about planets for one month.
	 */
	private void monthProcedurePlanets(){
		for(Planet planet : LOADER.getPlanets().values()){
			planet.addToStatisticsMonthRutine();
		}
	}
	
	/**
	 * Method fourthMonthProcedurePlanets
	 * creating statistics about planets for four months.
	 */
	private void fourthMonthProcedurePlanets(){
		for(Planet planet : LOADER.getPlanets().values()){
			planet.addToStatisticsFourthMonthRutine();
		}
	}
	
	/**
	 * Method monthProcedureShips
	 * creating statistics about ships for one month.
	 */
	private void monthProcedureShips(){
		List<Ship> shipList = new ArrayList<Ship>();
		shipList.addAll(getAllShips());

		for(Ship ship : shipList){
			ship.getDistributedMaterialMonth();
		}
	}
	
	/**
	 * Method fourMonthProcedureShips
	 * creating statistics about ships for four months.
	 */
	private void fourthMonthProcedureShips(){
		List<Ship> shipList = new ArrayList<Ship>();
		shipList.addAll(getAllShips());
		
		for(Ship ship : shipList){
			ship.getDistributedMaterialQuarter();
		}
	
	}
	
	/**
	 * Method write planets
	 * Creating text file with statistic about planets
	 * @param name Name of text file
	 */
	private void writePlanets(String name){
		try{
			FileWriter fw = new  FileWriter(new File(name));
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			
			for(Planet planet : LOADER.getPlanets().values()){
				splitAndWrite(planet.getStatisticsOneMonth(), pw);
				splitAndWrite(planet.getStatisticFourthMonth(), pw);
				pw.println(planet.getStatisticAllMonth());
				allOrders += planet.getAllOrders();
				unloadedMedicine += planet.getAllMedicine();
			}
			
			pw.print("ALL ORDERS IN GALAXY: " + (allOrders/1000000) 
				 + "M ALL UNLOADED MEDICINE: "+(unloadedMedicine/1000000)+"M "
				 +" ALL DEAD PEOPLE: "+(TimeHandler.getOrders().getPopulationDiedAll()/1000000)+"M ");
			
			pw.close();
			bw.close();
			fw.close();
			
		}catch(Exception e){
			System.out.println(name +" has been corrupted");
		}
	}
	
	/**
	 * Method write Ships
	 * Creating text file with statistic about ships
	 * @param name Name of text file
	 */
	private void writeShips(String name){
		try{
			FileWriter fw = new  FileWriter(new File(name));
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			
			List<Ship> shipList = new ArrayList<>();
			shipList.addAll(getAllShips());
			
			for(Ship ship : shipList){
				splitAndWrite(ship.getDistributedMaterialMonthS(), pw);
				splitAndWrite(ship.getDistributedMaterialQuarterS(), pw);
				pw.println(ship.getDistributedMaterialAllS());
				materialDistributed += ship.getDistributedMaterialAll();
			}
			
			pw.println("ALL DISTRIBUTED MATERIAL IN GALAXY: " + (materialDistributed/1000000) + "M ");
	
			pw.close();
			bw.close();
			fw.close();
			
		}catch(Exception e){
			System.out.println(name +" has been corrupted");
		}
	}
	
	/**
	 * Method writeShipsRoute
	 * Creating text file with statistic about ship routes
	 * @param name Name of text file
	 */
	private void writeShipsRoute(String name){
		
		try{
			FileWriter fw = new  FileWriter(new File(name));
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			
			List<Ship> shipList = new ArrayList<>();
			shipList.addAll(getAllShips());
			
			for(Ship ship : shipList){
				int routeNumber = 1;
				pw.println(ship.getName()+ " Medicine unloaded: " + ship.getDistributedMaterialAll()
										 + " Looted: " +ship.getNumberOfIncidents());
				for(Route route : ship.getRoutes()){
					pw.println(getRoute(route, routeNumber));
					routeNumber++;
				}
			}
			
			pw.close();
			bw.close();
			fw.close();
			
		}catch(Exception e){
			System.out.println(name +" has been corrupted");
		}
	}
	/**
	 * Method split and write
	 * Splitting text by \n and then write them to the text file.
	 * @param text Text you want to split
	 * @param pw Print writer
	 */
	private void splitAndWrite(String text, PrintWriter pw){
		String [] splittedText = text.split("\n");
		
		for(int i = 0; i < splittedText.length; i++){
			pw.println(splittedText[i]);
		}
	}
	
	/**
	 * Method getAllShips
	 * Creating List of ships from all factories.
	 * @return List with all ships in galaxy.
	 */
	private List <Ship> getAllShips(){
		List<Ship> shipList = new ArrayList<Ship>();
		for(Factory factory : LOADER.getFactoryMap().values()){
			shipList.addAll(factory.getShipList());
		}
		return shipList;
	}
	
	/**
	 * Method getRoute
	 * returning String of route for output.
	 * String contains planets and one factory on route.
	 * @param route Route from ship
	 * @param routeNumber Number of route
	 * @return String of route for output.
	 */
	private String getRoute(Route route, int routeNumber){
		Route actual = route;
		String stringRoute = routeNumber + ". route: ";
		
		List<TimeStamp> routeList = new ArrayList<TimeStamp>();
		routeList.addAll(actual.getRouteToTarget());
		routeList.addAll(actual.getRouteToHome().subList(1, actual.getRouteToHome().size()));
		
		for(TimeStamp ts : routeList ){
			stringRoute += ts.getObject().getName() + "  ";
		}
		
		stringRoute += "Looted: "+ actual.isLooted();
		
		return stringRoute;
	}

}
