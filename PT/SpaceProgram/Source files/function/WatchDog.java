package function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import function.TimeHandler.Time;
import objects.Factory;
import objects.HasName;
import objects.Planet;
import objects.Route;
import objects.Ship;

/**
 * Class represent action manager in which actions can be registered and every hour WatchDog checks for 
 * actions to be made and performs according actions
 * 
 * @author Martin Hamet
 *
 */
public class WatchDog {
	/**
	 * Heap of planed actions by theirs times
	 */
	private final Heap heap;
	
	/**
	 * Map for every ship of it's all actions
	 */
	public final Map<Ship, ShipTasks> shipTasks;
	
	/**
	 * Report for console
	 */
	private String comReport = null;
	
	/**
	 * Returns true if there is something in report
	 * @return	true if there is something in report
	 */
	public boolean isReport(){
		return comReport != null;
	}
	
	/**
	 * Returns report from last call of this method
	 * @return	report from last call of this method
	 */
	public String getReport(){
		String result = comReport;
		comReport = null;
		return result;
	}
	
	/**
	 * Adds given WatchItem to Map for associated ship to it's list of tasks
	 * @param wi	item to be added to it's ship task list
	 */
	private void addToTasks(WatchItem wi){
		if(shipTasks.containsKey(wi.ship)){
			shipTasks.get(wi.ship).add(wi);
		}else{
			ShipTasks st = new ShipTasks(wi.ship);
			st.add(wi);
			shipTasks.put(wi.ship, st);
		}
	}
	
	/**
	 * Removes given WatchItem from it's ships task list
	 * @param wi	item to remove
	 */
	private void removeTask(WatchItem wi){
		ShipTasks st = shipTasks.get(wi.ship);
		st.remove(wi.id);
	}
	
	/**
	 * Registers new Unload action for given ship at given time, planet by given amount
	 * @param ship		unloading ship
	 * @param time		time when material is completely unloaded
	 * @param planet	planet at which is material unloaded
	 * @param amount	amount of unloaded material
	 */
	public void add(Ship ship, Time time, Planet planet, int amount){
		ItemUnload iu = new ItemUnload(ship, time, planet, amount);
		heap.addToHeap(iu, time.getValue());
		addToTasks(iu);
	}
	
	/**
	 * Registers new Load action for given ship at given time, factory by given amount
	 * @param ship		loading ship
	 * @param time		time when material is completely loaded
	 * @param factory	factory at which is material loaded
	 * @param amount	amount to load
	 */
	public void add(Ship ship, Time time, Factory factory, int amount){
		ItemLoad il = new ItemLoad(time, ship, factory, amount);
		heap.addToHeap(il, time.getValue());
		addToTasks(il);
	}
	
	/**
	 * Registers given Route for given ship at given time
	 * @param ship		ship for given route
	 * @param time		time for assigning route
	 * @param route		route for assign
	 */
	public void add(Ship ship, Time time, Route route){
		ItemShipAvailable isa = new ItemShipAvailable(ship, time, route);
		heap.addToHeap(isa, isa.getTime().getValue());
		addToTasks(isa);
	}
	
	/**
	 * Registers pirate encounter for given ship at given time
	 * @param ship	ship in encounter
	 * @param time	time of encounter
	 */
	public void add(Ship ship, Time time){
		ItemPirate ip = new ItemPirate(time, ship);
		heap.addToHeap(ip, time.getValue());
		addToTasks(ip);
	}
	
	/**
	 * Perform tasks for actual time of simulation
	 */
	public void performTasks(){
		if(isEmpty()){
			return;
		}
		
		double actualTime = TimeHandler.getActualTime().getValue();
		WatchItem first = (WatchItem)heap.readTop();
		
		while(	!heap.isEmpty() &&
				first.getTime().getValue() <= actualTime
				){
			first = (WatchItem)heap.getTop();
			first.performAction();
			addToReport(first);
			removeTask(first);
			
			first = (WatchItem)heap.readTop();
		}
		
	}
	
	/**
	 * Adds given WatchItem to report
	 * @param wi	item to report
	 */
	private void addToReport(WatchItem wi){
		if(TimeHandler.isFullReport() || 
				(wi instanceof ItemPirate && ((ItemPirate)wi).looted) ){
			if(comReport == null){
				comReport = wi+"";
			}else{
				comReport += "\n";
				comReport += wi+"";
			}
		}
	}
	
	/**
	 * Returns true if WatchDog haven't any actions registered
	 * @return	true if WatchDog haven't any actions registered
	 */
	public boolean isEmpty(){
		return heap.isEmpty();
	}
	
	/**
	 * Instantiates new WatchDog
	 */
	public WatchDog() {
		this.heap = new Heap(Heap.TypeOfHeap.MIN, 100000);
		this.shipTasks = new HashMap<Ship, ShipTasks>();
	}
	
	/**
	 * Class represents Map of all actions registered for specific ship
	 * 
	 * @author Martin Hamet
	 *
	 */
	public class ShipTasks{
		/**
		 * Ship for actions are registered in WatchDog
		 */
		Ship ship;
		
		/**
		 * Map of all actions registered for given ship
		 */
		private final Map<Integer, WatchItem> ids;
		
		/**
		 * Adds new WatchItem to Map for this ship
		 * @param wi	Item to add
		 */
		public void add(WatchItem wi){
			ids.put(wi.id, wi);
		}
		
		/**
		 * Remove from map WatchItem by given id
		 * @param id	id of WatchItem
		 */
		public void remove(int id){
			ids.remove(id);
		}
		
		/**
		 * Returns list of all actions for this ship
		 * @return	list of all actions for this ship
		 */
		public List<WatchItem> getTasks(){
			List<WatchItem> list = new ArrayList<WatchItem>(ids.values());
			return list;
		}
		
		/**
		 * Instantiates new tasks for given ship
		 * @param ship	ship for register
		 */
		public ShipTasks(Ship ship) {
			this.ship = ship;
			this.ids = new HashMap<Integer, WatchItem>();
		}
		
		@Override
		public String toString(){
			String s ="";
			for(Map.Entry<Integer, WatchItem> wi : ids.entrySet()){
				s += wi.toString();
			}
			return s;
		}
	}
	
	/**
	 * Interface provides id, time and action to perform
	 * 
	 * @author Martin Hamet
	 *
	 */
	public interface Watchable extends HasName{
		public Time getTime();
		public int getId();
		public void performAction();
	}
	
	/**
	 * Class represent one registered action in WatchDog
	 * 
	 * @author Martin Hamet
	 *
	 */
	public static abstract class WatchItem implements Watchable {
		/**
		 * Counter of ids
		 */
		private static int countId;
		
		/**
		 * Id of this WatchItem
		 */
		private final int id;
		
		/**
		 * Time for this action
		 */
		private final Time time;
		
		/**
		 * Ship which is associated with this action
		 */
		private final Ship ship;

		@Override
		public Time getTime() {
			
			return time;
		}

		@Override
		public int getId() {
			
			return id;
		}

		@Override
		public String getName() {
			
			return ""+getId();
		}
		
		/**
		 * Returns ship associated with this action
		 * @return	ship associated with this action
		 */
		public Ship getShip(){
			return ship;
		}
		
		/**
		 * Instantiates new WatchItem by given ship and time
		 * @param time	time of action
		 * @param ship	ship associated with this action
		 */
		public WatchItem(Time time, Ship ship) {
			this.id = countId++;
			this.time = time;
			this.ship = ship;
		}
		
	}
	
	/**
	 * Class represents Action of unloading at given planet by given amount
	 * 
	 * @author Martin Hamet
	 *
	 */
	public class ItemUnload extends WatchItem{
		Planet planet;
		int amount;
		
		@Override
		public void performAction() {
			super.ship.unload(planet, amount);
		}
		
		/**
		 * Creates new WatchItem by given attributes
		 * @param ship		unloading ship
		 * @param time		time of unload
		 * @param planet	planet at which is ship unloading
		 * @param amount	amount of unloaded material
		 */
		public ItemUnload(Ship ship, Time time, Planet planet, int amount) {
			super(time, ship);
			this.planet = planet;
			this.amount = amount;
		}
		
		@Override
		public String toString(){
			return super.time.toString() + " "+super.ship.getName()+" Unloaded at: "
						+planet.getName()+" goods: "+amount;
		}
	}
	
	/**
	 * Class represents Action of loading at given factory by given amount
	 * @author Martin Hamet
	 *
	 */
	public class ItemLoad extends WatchItem{
		/**
		 * Factory for loading
		 */
		Factory factory;
		
		/**
		 * Amount of loaded material
		 */
		int amount;

		@Override
		public void performAction() {
			super.ship.load(amount);
		}
		
		/**
		 * Creates new WatchItem by given attributes
		 * @param time		time of load
		 * @param ship		loading ship
		 * @param factory	factory at which ship is loading
		 * @param amount	amount of loaded material
		 */
		public ItemLoad(Time time, Ship ship, Factory factory, int amount) {
			super(time, ship);
			this.factory = factory;
			this.amount = amount;
		}
		
		@Override
		public String toString(){
			return super.time.toString() + " "+super.ship.getName()+" Loaded at: "
					+factory.getName()+" goods: "+amount;
		}
	}
	
	/**
	 * Creates new WatchItem by given attributes
	 * 
	 * @author Martin Hamet
	 *
	 */
	public class ItemShipAvailable extends WatchItem{
		/**
		 * Route for assign
		 */
		Route route;

		@Override
		public void performAction() {
			super.ship.setRoute(route);
			super.ship.getHome().fillShipLists(route, super.ship);
		}
		
		/**
		 * Creates new WatchItem by given attributes
		 * @param ship		ship for assign
		 * @param time		time of assigning
		 * @param route		route to assign
		 */
		public ItemShipAvailable(Ship ship, Time time, Route route) {
			super(time, ship);
			this.route = route;
		}
		
		@Override
		public String toString(){
			return super.time.toString() + " "+super.ship.getName()+" Added new route to ("
						+route.getTargetPoint().getObject().getName()+")";
		}
	}
	
	/**
	 * Creates new WatchItem by given attributes
	 * 
	 * @author Martin Hamet
	 *
	 */
	public class ItemPirate extends WatchItem{
		private boolean looted;
		
		@Override
		public void performAction() {
			if(isStolen()){
				looted = true;
				ShipTasks st = shipTasks.get(super.ship);
				List<WatchItem> list = st.getTasks();
				for(WatchItem wi : list){
					if(wi instanceof ItemShipAvailable){
						continue;
					}
					heap.getElement(wi.getName());
					st.remove(wi.id);
				}
				super.ship.addIncident();
				super.ship.getRoute().emergencyRoute(super.ship);
			}
		}
		
		/**
		 * Returns true if pirate encounter was successful for pirates
		 * @return	true if pirate encounter was successful for pirates
		 */
		private boolean isStolen(){
			int i = TimeHandler.getRandomInt(0, 100);
			return (i<10);
		}
		
		/**
		 * Creates new WatchItem by given attributes
		 * @param time	time of encounter
		 * @param ship	ship associated with encounter
		 */
		public ItemPirate(Time time, Ship ship) {
			super(time, ship);
			this.looted = false;
		}
		
		@Override
		public String toString(){
			String s = super.time.toString() + " "+super.ship.getName()+" Pirate encounter \n";
			if(looted){
				s += "\t(the ship was looted)";
			}else{
				s += "\t(the ship was not looted)";
			}
			return s;
		}
	}
	
	/**
	 * Method for extract content of WatchDog to console for testing
	 * @param number	number of actions to extract
	 */
	public void comWrite(int number){
		if(heap.isEmpty()){
			return;
		}
		List<WatchItem> list = new ArrayList<WatchItem>();
		WatchItem w = (WatchItem)heap.readTop();
		for(int i = 0; i < number;i++){
			if(heap.isEmpty()){
				break;
			}
			w = (WatchItem)heap.getTop();
			System.out.println(w);
			list.add(w);
		}
		
		for(WatchItem wi : list){
			heap.addToHeap(wi, wi.getTime().getValue());
		}
	}
	
}
