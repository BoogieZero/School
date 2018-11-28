package objects;
import java.util.ArrayList;
import java.util.List;

import function.TimeHandler;

/**
 * Class Planet
 * Is an object which contains basic information about planet.
 * Basic information like (name, coordinates, population, ship list - Which ships will landing on this planet)
 * @author Jiří Lukáš
 *
 */
public class Planet extends SpaceObject implements Comparable<Planet> {
	/**
	 * Population on this planet
	 */
	private int population;
	

	/**
	 * Medicine which planet need to get.
	 */
	private int order = 0;	
	/**
	 * If order is set by user
	 */
	private int manualOrder = 0;
	
	/**
	 * Order for this month
	 */
	private int monthOrder = 0;
	
	/**
	 * Order for fourth month
	 */
	private int fourthMonthOrder = 0;
	
	/**
	 * All order for all simulation
	 */
	private int allOrder = 0;
	
	/**
	 * Unloaded medicine for fourth month
	 */
	private int fourthMonthMedicine = 0;
	
	/**
	 * Unloaded medicine for all simulation
	 */
	private int allMedicine = 0;
	
	/**
	 * List of ships which travel to this planet.
	 */
	List<Ship> shipList = new ArrayList<Ship>();	
	
	/**
	 * Statistics for final output file
	 */
	private String statisticsOneMonth = "Planet "+ getName()+"\n";
	
	/**
	 * Statistics for fourth month of simulation
	 */
	private String statisticsFourthMonth = "";
	

	/**
	 * Constructor
	 * @param name Name of planet
	 * @param x X coordinate of planet
	 * @param y Y coordinate of planet
	 * @param population Population on this planet
	 */
	public Planet(String name, double x, double y, int population) {
		super(name,x,y);
		this.population = population;
	}
	
	
	/**
	 * Creating order for this planet from population
	 * @return Ordered goods
	 */
	public int createOrder(){
		if(manualOrder != 0){
			setMonthOrder(manualOrder);
			order += manualOrder;
			int returnedOrder = manualOrder;
			manualOrder = 0;
			return returnedOrder;
		}
		double base = TimeHandler.getRandom().nextDouble();
		double randomNumber = (base * 0.6) + 0.2;
		double planetOrder = Math.round(population * randomNumber);
		int result = (int) planetOrder;
		order += result;
		setMonthOrder(result);
		return result;
	}
	
	/**
	 * Method unload
	 * if some ship unload goods on this planet 
	 * ordered goods = ordered goods - unloading ship goods
	 * @param amount Unloading ships goods
	 */
	public void unload (int amount){
		order = order - amount;
	}
	
	/**
	 * Method addToStatisticsMonthRutine
	 * This method is for month statistic.
	 * This string will be use on final text file output.
	 */
	public void addToStatisticsMonthRutine(){
		statisticsOneMonth += TimeHandler.getActualTime().getMonth() +". month " 
				      		  +"order: " + this.monthOrder 
				      		  +" medicine: " + (this.monthOrder - order)
				      		  +" population: " + this.population +"\n";
		
		fourthMonthMedicine += (this.monthOrder - order);
		fourthMonthOrder += this.monthOrder;
		
		allOrder += this.monthOrder;
		allMedicine += (this.monthOrder - order);
	}
	
	/**
	 * Method addToStatisticsMonthRutine
	 * This method is for fourth month statistic.
	 * This string will be use on final text file output.
	 */
	public void addToStatisticsFourthMonthRutine(){
		int actualMonth = TimeHandler.getActualTime().getMonth();
		statisticsFourthMonth += (actualMonth - 4) + " - " + actualMonth + " month"  +" orders: " + this.fourthMonthOrder 
																			         +" medicine: " + this.fourthMonthMedicine +"\n";
		this.fourthMonthOrder = 0;
		this.fourthMonthMedicine = 0;
	}
	
	/**
	 * Method addToShipList
	 * Adding ship on planet ship list
	 * @param ship This ship will be added on ship list
	 */
	public void addToShipList(Ship ship){
		shipList.add(ship);
	}
	
	/**
	 * Method removeShip
	 * Removing ship from planet ship list
	 * @param ship This ship will be removed from ship list
	 */
	public void removeShip(Ship ship){
		shipList.remove(ship);
	}

	/**
	 * Method getShipList
	 * @return shipList Returning ship list of loading or unloading ships
	 */
	public List<Ship> getShipList() {
		return shipList;
	}

	/**
	 * Method getStatisticsOneMonth
	 * Get statistics about planet for one month
	 * @return statistics about this planet
	 */
	public String getStatisticsOneMonth(){
		return statisticsOneMonth;
	}
	
	/**
	 * Method getStatisticsFourthMonth
	 * Get statistics about planet for four months
	 * @return statistics about this planet
	 */
	public String getStatisticFourthMonth(){
		return statisticsFourthMonth;
	}
	
	/**
	 * Method getStatisticsAllMonth
	 * Get statistics about planet for all months
	 * @return statistics about this planet
	 */
	public String getStatisticAllMonth(){
		return "All orders: " + allOrder + " All medicine: " +allMedicine;
	}
	
	/**
	 * Method getPopulation
	 * Get population on this planet
	 * @return population Population on this planet
	 */
	public int getPopulation() {
		return population;
	}
	
	/**
	 * Method getOrder
	 * @return order Planet order
	 */
	public int getOrder() {
		return order;
	}
	
	/**
	 * Method getAllMedicine
	 * @return all unloaded medicine
	 */
	public int getAllMedicine(){
		return allMedicine;
	}
	
	/**
	 * Method getAllOrder
	 * @return All orders
	 */
	public int getAllOrders(){
		return allOrder;
	}

	/**
	 * Method setMonthOrder
	 * @param order Order to set
	 */
	public void setMonthOrder(int order){
		monthOrder = order;
	}
	
	/**
	 * Method setManualOrder
	 * @param newOrder New order to set
	 */
	public void setManualOrder(int newOrder){
		manualOrder = newOrder;
	}
	
	/**
	 * Method setPopulation
	 * Set for population on planet
	 * @param population the population to set
	 */
	public void setPopulation(int population) {
		this.population = population;
	}
	
	/**
	 * Method clearOrder
	 * On the end of month if planet have some order then some people die 
	 * (population - order)
	 * @return Old order How much order planet have on the end of month. 
	 */
	public int clearOrther(){
		if(order < 0){
			order = 0;
		}
		if(order > population){
			System.out.println("Order: "+order+" pop: "+population+" name: "+getName());
		}
		population = population - order;
		
		int oldOrder = order;
		order = 0;
		return oldOrder;
	}
	
	/**
	 * Method clearShipList
	 * removing all ships from ship list.
	 */
	public void clearShipList(){
		shipList.clear();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return  getName() + "," + getX() + "," + getY() +","+getPopulation();
	}
	
	@Override
	public int compareTo(Planet o) {
		int result = (int) (this.getX() - o.getX());
		return result;
	}
}
