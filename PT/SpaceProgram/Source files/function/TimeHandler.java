package function;

import generator.Loader;
import generator.MapGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import objects.Factory;
import objects.Planet;
import data.DijkstraController;

/**
 * Class TimeHandler provides control of whole application, time of simulation and 
 * tasks associated with time.
 * 
 * @author Martin Hamet
 *
 */
public class TimeHandler {
	/**
	 * Hours in month
	 */
	private static final int MONTH = 720;
	
	/**
	 * Default input file for planets
	 */
	private static final String FILE_NONDEF_PlANETS 	= "SpaceProgramPlanets.txt";
	
	/**
	 * Default file for paths
	 */
	private static final String FILE_NONDEF_NEIGHBOURS 	= "SpaceProgramNeighbours.txt";
	
	/**
	 * Actual time of simulation
	 */
	private static Time simulationTime = new Time(0);
	
	/**
	 * Loader for input data
	 */
	private static Loader load = new Loader();
	
	/**
	 * Orders manager
	 */
	private static Orders orders = new Orders(load);
	
	/**
	 * Orders handler
	 */
	private static Dispatcher dispatcher = new Dispatcher();
	
	/**
	 * Shortest routes manager
	 */
	private static DijkstraController dijkstra = new DijkstraController(load);
	
	/**
	 * Report of actions
	 */
	private static String comReport = null;
	
	/**
	 * Actions manager
	 */
	private static WatchDog watchDog = new WatchDog();
	
	/**
	 * Random generator
	 */
	private static Random random = new Random(System.currentTimeMillis());
	
	/**
	 * Switch for full report
	 */
	private static BooleanProperty fullReport = new SimpleBooleanProperty();
	
	/**
	 * List of planets doomed for population decay
	 */
	private static List<Planet> listFastDecay = new ArrayList<Planet>();
	
	/**
	 * List of planets with high probability of population decay
	 */
	private static List<Planet> listSlowDecay = new ArrayList<Planet>();
	
	/**
	 * Report of endangered planets
	 */
	private static String decayReport;
	
	/**
	 * Data writer
	 */
	private static DataWriter writer = new DataWriter(load);
	
	/**
	 * Returns new random generator
	 * @return	new random generator
	 */
	public static Random getRandom(){
		return new Random(System.currentTimeMillis());
	}
	
	/**
	 * Returns report of endangered planets
	 * @return	report of endangered planets
	 */
	public static String getDecayReport(){
		return decayReport;
	}
	
	/**
	 * Binds given property with full report switch
	 * 
	 * @param value	property to be binded to full report switch
	 */
	public static void BindToFullReport(BooleanProperty value){
		value.bindBidirectional(fullReport);
	}
	
	/**
	 * Return true if full report is turned on
	 * @return	true if full report is turned on
	 */
	public static boolean isFullReport(){
		return fullReport.getValue();
	}
	
	/**
	 * Returns random integer number between min and max
	 * @param min	lower bound
	 * @param max	higher bound
	 * @return		random integer number between min and max
	 */
	public static int getRandomInt(int min, int max){
		int randomNumber = random.nextInt(max - min) + min;
		return randomNumber;
	}
	
	/**
	 * Returns Orders manager
	 * @return	Orders manager
	 */
	public static Orders getOrders(){
		return orders;
	}
	
	/**
	 * Returns actions manager
	 * @return	actions manager
	 */
	public static WatchDog getWatchDog(){
		return watchDog;
	}
	
	/**
	 * Returns loader for input data
	 * @return	loader for input data
	 */
	public static Loader getLoader(){
		return load;
	}
	
	/**
	 * Return shortest routes manager
	 * @return	shortest routes manager
	 */
	public static DijkstraController getDijkstra(){
		return dijkstra;
	}
	
	/**
	 * Returns report from last call of this method
	 * if there isn't anything to be reported returns null
	 * @return	report from last call of this method
	 */
	public static String getReport(){
		if(comReport == null){
			return null;
		}
		String result = comReport;
		comReport = null;
		return result;
	}
	
	/**
	 * Shifts time of simulation by one hour forward
	 */
	public static void nextHour(){
		if(simulationTime.getValue() % (MONTH*12) == 0 && simulationTime.value != 0){
			return;
		}
		simulationTime.add(1);
		checkMonth();
		
		if(watchDog.isReport()){
			if(comReport == null){
				comReport = watchDog.getReport();
			}else{
				comReport += "\n";
				comReport += watchDog.getReport();
			}
		}
	}
	
	/**
	 * Shifts time of simulation by one day forward
	 */
	public static void nextDay(){
		int h = (int)simulationTime.getHour();
		for(int i = h; i<24;i++){
			nextHour();
		}
	}
	
	/**
	 * Shifts time of simulation by one month forward
	 */
	public static void nextMonth(){
		int m = simulationTime.getDay();
		for(int i = m; i<30;i++){
			nextDay();
		}
	}
	
	/**
	 * Checks if time of simulation reached month mark and if so call to monthRoutines
	 * otherwise calls to checkList
	 */
	private static void checkMonth(){
		if(simulationTime.getValue() % MONTH == 0 && simulationTime.value != 0){
			monthRoutines();
		}else{
			checkList();
		}
	}
	
	/**
	 * Returns time left from given time to end month
	 * @param from	given time
	 * @return		time left to end of month
	 */
	public static double getTimeLeft(Time from){
		int timesF = (int)(from.getValue() / 720);
		timesF -= simulationTime.getMonth();
		
		double val = from.getValue() % 720;
		val = 720 - val - (timesF * 720);
		return val;
	}
	
	/**
	 * Returns true if given time lies in actual month
	 * @param from	examined time
	 * @return		true if given time lies in actual month
	 */
	public static boolean isInMonth(Time from){
		return (getTimeLeft(from) > 0);
	}
	
	/**
	 * Returns copy of actual time of simulation
	 * @return	copy of actual time of simulation
	 */
	public static Time getActualTime(){
		return simulationTime;
	}
	
	/**
	 * Check every hour tasks
	 */
	private static void checkList(){
		watchDog.performTasks();
	}
	
	/**
	 * Check if time of simulation hit mark of quarter
	 */
	private static void checkQuarter(){
		if(simulationTime.getValue() % (MONTH*4) == 0 && simulationTime.value != 0){
			dataQuarterProcedure();
		}
	}
	
	/**
	 * Calls data procedures for month
	 */
	private static void dataMonthProcedure(){
		writer.monthProcedure();
	}
	
	/**
	 * Calls data procedures for quarter
	 */
	private static void dataQuarterProcedure(){
		writer.quarterProcedure();
	}
	
	/**
	 * Calls data procedures for quarter
	 */
	private static void dataYearProcedure(){
		writer.write();
	}
	
	/**
	 * Check if time of simulation hit mark of year
	 * @return	true if time of simulation hit mark of year
	 */
	private static boolean checkYear(){
		if(simulationTime.getValue() % (MONTH*12) == 0 && simulationTime.value != 0){
			dataYearProcedure();
			return true;
		}
		return false;
	}
	
	/**
	 * Does routines for new month
	 */
	private static void monthRoutines(){
		checkList();
		dataMonthProcedure();
		checkQuarter();
		if(checkYear()){
			return;
		}
		factoriesMonthClear();
		
		if(comReport == null){
			comReport = "New Month";
		}else{
			comReport += "\n";
			comReport += "New Month";
		}
		
		if(!watchDog.isEmpty()){
			System.out.println("WatchDog is NOT empty");
		}
		
		long startTime = System.currentTimeMillis();
		
		orders.createOrders();
		long endTime = System.currentTimeMillis();
		
		comReport += "\n"+getActualTime()+" Orders created ("+(endTime - startTime)+" ms)";
		
		startTime = System.currentTimeMillis();
		dispatcher.dispatchOrders(orders);
		endTime = System.currentTimeMillis();
		
		comReport += "\n"+getActualTime()+" Orders dispatched ("+(endTime - startTime)+"ms)";
	}
	
	/**
	 * Clears material distributed this month in factories
	 */
	private static void factoriesMonthClear(){
		List<Factory> listF = new ArrayList<Factory>(load.getFactoryMap().values());
		for(Factory fct : listF){
			fct.monthClearMaterial();
		}
	}
	
	/**
	 * Resolve created orders from planets
	 */
	public static void dispatchOrders(){
		dispatcher.dispatchOrders(orders);
	}
	
	/**
	 * Prepares data for shortest routes and loader when new map is generated
	 */
	public static void generateNewMapActions(){
		new MapGenerator().generate();
		load = new Loader(FILE_NONDEF_PlANETS, FILE_NONDEF_NEIGHBOURS);
		orders = new Orders(load);
		dijkstra = new DijkstraController(load);
		dispatcher = new Dispatcher();
		watchDog = new WatchDog();
		writer = new DataWriter(load);
	}
	
	/**
	 * Instantiates new TimeHandler
	 */
	public TimeHandler() {
		TimeHandler.fullReport.setValue(false);
	}
	
	/**
	 * Finds planets with difficult or non-existent delivery routes
	 */
	private static void findPlanetsForDecay(){
		List<Planet> list = new ArrayList<Planet>(load.getPlanets().values());
		for(Planet pl : list){
			if(dijkstra.getRouteTo(pl.getName(), 0, false) != null){
				//planet is reachable
				continue;
			}
			if(dijkstra.getRouteTo(pl.getName(), 0, true) != null){
				//planet can be reached only through pirates
				listSlowDecay.add(pl);
				continue;
			}
			//planet cannot be reached by any means
			listFastDecay.add(pl);
		}
		//Planets found
		String str = 	"Planets to which leads only way through pirates, \n"
				+		"therefore planets to decay over time. : \n";
		for(Planet pl : listSlowDecay){
			str += "\t"+pl.getName()+" Population: "+pl.getPopulation()+"\n";
		}
		str += "\n";
		if(listSlowDecay.isEmpty()){
			str += "\t------------------------------";
		}
		str += 			"Planets which cannot be reached by any means, \n"
					+ 	"therefore planets to quickly decay over time: \n";
		for(Planet pl : listFastDecay){
			str += "\t"+pl.getName()+" Population: "+pl.getPopulation()+"\n";
		}
		if(listFastDecay.isEmpty()){
			str += "\t------------------------------";
		}
		decayReport = str;
	}
	
	/**
	 * Starts new simulation
	 */
	public static void startSimulation(){
		findPlanetsForDecay();
		monthRoutines();
	}
	
	/**
	 * Class represents time in simulation and provides easy String output for UI
	 * 
	 * @author Martin Hamet
	 *
	 */
	public static class Time{
		/**
		 * Value of time in hours
		 */
		private double value;
		
		/**
		 * Returns time in hours
		 * @return	time in hours
		 */
		public double getValue(){
			return value;
		}
		
		/**
		 * Sets time to given value in hours
		 * @param value	time in hours
		 */
		public void setValue(double value){
			this.value = value;
		}
		
		/**
		 * Sets time to given Time
		 * @param time	sets time to Time
		 */
		public void setValue(Time time){
			this.value = time.getValue();
		}
		
		/**
		 * Returns only hours from this time
		 * @return	only hours
		 */
		public double getHour(){
			int m = (int)(value/(30*24));
			int d = (int)(value-m*30*24) / (24);
			int h = (int)(value-d*24 - m*30*24);
			return h;
		}
		
		/**
		 * Returns only days from this time
		 * @return	only days
		 */
		public int getDay(){
			int m = (int)(value/(30*24));
			int d = (int)(value-m*30*24) / (24);
			return d;
		}
		
		/**
		 * Returns only month fromt this time
		 * @return	only month
		 */
		public int getMonth(){
			int m = (int)(value/(30*24));
			return m;
		}
		
		/**
		 * Add to this time given Time t
		 * @param t	Time to add
		 */
		public void add(Time t){
			value += t.getValue();
		}
		
		/**
		 * Add to this time given time in hours
		 * @param value	time in hours
		 */
		public void add(double value){
			this.value += value;
		}
		
		/**
		 * Subtract given Time from this time
		 * @param t	time to subtract
		 */
		public void sub(Time t){
			this.value -= t.getValue();
		}
		
		/**
		 * Subtract given time in hours from this time
		 * @param value	time in hours
		 */
		public void sub(double value){
			this.value -= value;
		}
		
		/**
		 * Instantiates new time by given time in hours
		 * @param value	time in hours
		 */
		public Time(double value) {
			this.value = value;
		}
		
		/**
		 * Instantiates copy of given Time
		 * @param time	time to copy
		 */
		public Time(Time time) {
			this.value = time.getValue();
		}
		
		@Override
		public String toString(){
			String s = "";// = String.format("(%02.2f) ", value);
			s += String.format("%02d/%02d/%02d", (int)getHour(), getDay(), getMonth());
			return s;
					
		}
	}
}
