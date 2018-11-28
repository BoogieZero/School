package objects;

import java.util.ArrayList;
import java.util.List;

import function.TimeHandler;
import function.TimeHandler.Time;

/**
 * Class represents ship created by it's parent factory and provide necessary informations for it's function
 * 
 * @author Martin Hamet
 *
 */
public class Ship{
	/**
	 * Default speed of ship in lightyears per day
	 */
	public static final double 	DEFAULT_SPEED = 25.0;
	
	/**
	 * Default time spend by Loading or Unloading material in hours
	 */
	public static final double 	DEFAULT_TIME_OF_ACTION = 24.0;
	
	/**
	 * Maximum capacity of units per one Ship
	 */
	public static final int 	DEFAULT_LOAD_CAPACITY = 5000000;
	
	/**
	 * Parent factory for this Ship
	 */
	private final Factory home;
	
	/**
	 * Name of this Ship
	 */
	private String name;
	
	/**
	 * Route actually assigned to this ship
	 */
	private Route route;
	
	/**
	 * Time in which this ship become available for next route (time in which is ship docked)
	 */
	private Time availableTime;
	
	/**
	 * Actual amount of loaded material
	 */
	private int load;
	
	/**
	 * List of all previously assigned routes for this ship including actual Route
	 */
	private final List<Route> routes;
	
	/**
	 * Number of successful attacks by pirates
	 */
	private int numberOfIncidents;
	
	/**
	 * Material which has been successfully delivered in this month
	 */
	private int distributedMaterialMonth;
	
	/**
	 * Material which has been successfully delivered in all months
	 */
	private String distributedMaterialMonthS = "";
	
	/**
	 * Material which has been successfully delivered in this quarter
	 */
	private int distributedMaterialQuarter;
	
	/**
	 * Material which has been successfully delivered in all quarter
	 */
	private String distributedMaterialQuarterS = "";
	
	/**
	 * Material which has been successfully delivered from creation of this ship
	 */
	private int distributedMaterialAll;
	
	
	/**
	 * Returns material which has been successfully delivered in all months
	 * @return	Material which has been successfully delivered in all months
	 */
	public String getDistributedMaterialMonthS(){
		return distributedMaterialMonthS;
	}
	
	/**
	 * Returns material which has been successfully delivered in all quarters
	 * @return	Material which has been successfully delivered in all quarters
	 */
	public String getDistributedMaterialQuarterS(){
		return distributedMaterialQuarterS;
	}
	
	/**
	 * Returns material which has been successfully delivered in all months
	 * @return	Material which has been successfully delivered in all months
	 */
	public String getDistributedMaterialAllS(){
		return "All distributed material: "+distributedMaterialAll;
	}
	
	/**
	 * Returns material which has been successfully delivered from creation of this ship
	 * @return	Material which has been successfully delivered from creation of this ship
	 */
	public int getDistributedMaterialAll(){
		return distributedMaterialAll;
	}
	
	public int getDistributedMaterialQuarter(){
		int val = distributedMaterialQuarter;
		Time act = TimeHandler.getActualTime();
		distributedMaterialQuarterS += (act.getMonth()-4)+" - "+act.getMonth()
											+" months distrbuted: "+distributedMaterialQuarter+"\n";
		
		distributedMaterialQuarter = 0;
		return val;
	}
	
	/**
	 * Material which has been successfully delivered in this month
	 * @return	amount of distributed material in actual month
	 */
	public int getDistributedMaterialMonth(){
		int val = distributedMaterialMonth;
		distributedMaterialMonthS += TimeHandler.getActualTime().getMonth()+". month"
										+" distributed: "+distributedMaterialMonth+"\n";
		
		distributedMaterialMonth = 0;
		return val;
	}
	
	/**
	 * Number of successful attacks by pirates
	 * @return	number of successful attacks by pirates
	 */
	public int getNumberOfIncidents(){
		return numberOfIncidents;
	}
	
	/**
	 * Adds one to number of successful attacks by pirates and clears it's load
	 */
	public void addIncident(){
		numberOfIncidents++;
		setLoad(0);
	}
	
	/**
	 * Adds given Route to assigned routes
	 * 
	 * @param route	Route which will be added to assigned routes
	 */
	private void addToRoutes(Route route){
		routes.add(route);
	}
	
	/**
	 * Returns list of all previously assigned routes for this ship including actual Route
	 * @return	list of all previously assigned routes for this ship including actual Route
	 */
	public List<Route> getRoutes(){
		return routes;
	}
	
	/**
	 * Returns actual load of this ship
	 * 
	 * @return	actual load of this ship
	 */
	public int getLoad(){
		return load;
	}
	
	/**
	 * Sets time in which is this ship going to be available
	 * 
	 * @param time	time of availability
	 */
	public void setAvailableTime(Time time){
		availableTime = new Time(time);
	}
	
	/**
	 * Adds given Time to available time
	 * 
	 * @param time	adds this time to available time
	 */
	public void addToAvailableTime(Time time){
		availableTime.add(time);
	}
	
	/**
	 * Returns copy of available time for this ship
	 * @return	copy of available time for this ship
	 */
	public Time getAvailableTime(){
		Time at = new Time(availableTime);
		return at;
	}
	
	/**
	 * Unloads this ship at given planet with given amount
	 * 
	 * @param p			Planet for unloading
	 * @param amount	amount of unloading material
	 */
	public void unload(Planet p, int amount){
		load -= amount;
		distributedMaterialMonth += amount;
		distributedMaterialQuarter += amount;
		distributedMaterialAll += amount;
		p.unload(amount);
	}
	
	/**
	 * Loads this ship at parent factory by given amount
	 * @param amount	amount to load
	 */
	public void load(int amount){
		load = amount;
		home.addMaterial(amount);
	}
	
	/**
	 * Sets actual load for this ship
	 * @param load	actual load for this ship
	 */
	public void setLoad(int load){
		this.load = load;
	}
	
	/**
	 * Returns actually assigned Route
	 * @return	actually assigned Route
	 */
	public Route getRoute(){
		return route;
	}
	
	/**
	 * Sets actual Route for this ship and adds action of Loading and possible pirate encounters to Watchdog
	 * 
	 * @param route	Route for this ship
	 */
	public void setRoute(Route route){
		addToRoutes(route);
		this.route = route;
		Time strt = new Time(route.getStartTime());
		strt.add(Ship.DEFAULT_TIME_OF_ACTION);
		TimeHandler.getWatchDog().add(this, strt, home, route.getStartPoint().getAmount());
		route.registerDangersToWatch(this);
		//this.availableTime = route.getReturnTime();
	}
	
	/**
	 * Returns name of this Ship
	 * @return	name of this Ship
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns parent factory of this ship
	 * @return	parent factory of this ship
	 */
	public Factory getHome(){
		return home;
	}
	
	/**
	 * Instantiates Ship by given factory
	 * @param factory	parent factory for this ship
	 */
	public Ship(Factory factory) {
		this.home = factory;
		this.route = null;
		this.availableTime = new Time(TimeHandler.getActualTime());
		this.routes = new ArrayList<Route>();
		this.numberOfIncidents = 0;
		this.distributedMaterialMonth = 0;
		this.distributedMaterialQuarter = 0;
		this.distributedMaterialAll = 0;
		createName();
	}
	
	/**
	 * Creates copy of given ship
	 * @param ship	ship for copy
	 */
	public Ship(Ship ship){
		this.home = ship.getHome();
		this.route = ship.getRoute();
		this.availableTime = ship.getAvailableTime();
		this.routes = ship.routes;
		this.name = ship.getName();
		this.load = ship.load;
		this.numberOfIncidents = ship.getNumberOfIncidents();
		this.distributedMaterialMonth = ship.distributedMaterialMonth;
		this.distributedMaterialQuarter = ship.distributedMaterialQuarter;
		this.distributedMaterialAll = ship.distributedMaterialAll;
	}
	
	/**
	 * Creates name for this ship in pattern:
	 * 		S-'2 characters from parrent factory name'-'serial number'
	 */
	private void createName(){
		String sNum = String.format("%05d", home.getShipsCreated());
		this.name = "S-"+home.getName().charAt(2)+""+home.getName().charAt(3)+"-"+sNum;
		distributedMaterialMonthS = name+" \n";
	}
	
	/**
	 * Class represents states in which ship can be found
	 * 
	 * @author Martin Hamet
	 *
	 */
	public static enum Status{
		NONE, LOADING, UNLOADING, INFLIGHT;
		
		/**
		 * Returns true if given status falls into category in which is required for ship to take action
		 * @param s	examined state
		 * @return	true	action which takes time
		 * 			false	trivial action
		 */
		public static boolean actionToPerform(Status s){
			switch(s){
				case NONE:
				case INFLIGHT:	return false;
				case LOADING:
				case UNLOADING:	return true;
				
				default:	System.out.println("ERROR - wrong type of action(status)!");
							System.exit(0);
							return false;
			}
			
		}
	}
}
