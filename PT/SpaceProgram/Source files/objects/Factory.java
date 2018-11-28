package objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import function.Heap;
import function.TimeHandler;
import function.TimeHandler.Time;

/**
 * Class represents Factory and allows creating and assigning tasks to it's ships
 * 
 * @author Martin Hamet
 *
 */
public class Factory extends SpaceObject {
	public static int cnt = 0;
	/**
	 * Heap of owned ships (by ships available times)
	 */
	private final Heap ships;
	
	/**
	 * Material loaded to ships this month
	 */
	private long materialMadeMonth;
	
	/**
	 * Material loaded to ships from creation
	 */
	private long materialMadeAll;
	
	/**
	 * List of owned ships
	 */
	private final List<Ship> shipList;
	
	/**
	 * Returns copy of list of owned ships
	 * @return copy of list of owned ships
	 */
	public List<Ship> getShipList(){
		return new ArrayList<Ship>(shipList);
	}
	
	/**
	 * Returns number of created ships
	 * @return	number of created ships
	 */
	public int getShipsCreated(){
		//System.out.println("Factory: "+this.getName()+" avTime: "+getTimeOfNextShip());
		return shipList.size();
	}
	
	/**
	 * Returns material loaded to ships from creation
	 * @return	material loaded to ships from creation
	 */
	public long getMaterialMadeAll(){
		return materialMadeAll;
	}
	
	/**
	 * Returns material loaded to ships this month
	 * @return material loaded to ships this month
	 */
	public long getMaterialMadeMonth(){
		return materialMadeMonth;
	}
	
	/**
	 * Returns true if Factory doesn't own any ships
	 * @return	true if Factory doesn't own any ships
	 */
	public boolean isEmpty(){
		return ships.isEmpty();
	}
	
	/**
	 * Adds given amount to material loaded on ships this month
	 * @param amount	material added to loaded material for this month
	 */
	public void addMaterial(int amount){
		materialMadeMonth += amount;
	}
	
	/**
	 * Assign given route to first available owned ship
	 * if ship isn't available now Watchdog is provided by action to set given Route to this ship when
	 * it's possible and available time of this ship is extended by Time needed for given Route
	 * @param route	route for assigning
	 */
	public void assignTask(Route route){
		Ship sh = (Ship)ships.getTop();
		
		if(sh.getAvailableTime().getValue() == TimeHandler.getActualTime().getValue()){
			//ship is available now
			sh.setRoute(route);
			sh.addToAvailableTime(route.getRouteTime());
			ships.addToHeap(sh, sh.getAvailableTime().getValue());
			fillShipLists(route, sh);
		}else{										//ship is in flight
			Time routeTime = route.getRouteTime();
			Time ready = sh.getAvailableTime();
			TimeHandler.getWatchDog().add(sh, ready, route);
			sh.addToAvailableTime(routeTime);
			ships.addToHeap(sh, sh.getAvailableTime().getValue());
		}
	}
	
	/**
	 * Iterates through Route list of action and registering given ship on these SpaceObject
	 * @param rt	route for register
	 * @param sh	ship assigned for given route
	 */
	public void fillShipLists(Route rt, Ship sh){
		Map<SpaceObject, TimeStamp> list = rt.getActions();
		SpaceObject so;
		for(Map.Entry<SpaceObject, TimeStamp> ts : list.entrySet()){
			so = ts.getValue().getObject();
			if(so instanceof Planet){
				Planet pl = (Planet)so;
				pl.addToShipList(new Ship(sh));
				Time unloaded = new Time(ts.getValue().getTime());
				unloaded.add(Ship.DEFAULT_TIME_OF_ACTION);
				TimeHandler.getWatchDog().add(sh, unloaded, pl, ts.getValue().getAmount());
			}
		}
	}
	
	/**
	 * Returns Time of next available Ship owned by this factory
	 * @return	Time of next available Ship owned by this factory
	 */
	public Time getTimeOfNextShip(){
		if(ships.isEmpty()){
			return new Time(Integer.MAX_VALUE);	//empty factory
		}else{
			Ship ship = (Ship)ships.readTop();
			if(ship.getAvailableTime().getValue() < TimeHandler.getActualTime().getValue()){
				ship.setAvailableTime(TimeHandler.getActualTime());
			}
			return ship.getAvailableTime();
		}
	}
	
	/**
	 * Returns Time left to first available Ship owned by this factory
	 * @return	Time left to first available Ship owned by this factory
	 */
	public Time getTimeToNextShip(){
		Time t = getTimeOfNextShip();
		t.sub(TimeHandler.getActualTime());
		return t;
	}
	
	/**
	 * Instantiates Factory by given name and coordinates
	 * @param name	factory name
	 * @param x		coordinate X
	 * @param y		coordinate Y
	 */
	public Factory(String name, double x, double y) {
		super(name, x, y);
		ships = new Heap(Heap.TypeOfHeap.MIN);
		this.materialMadeMonth = 0;
		this.materialMadeAll = 0;
		this.shipList = new ArrayList<Ship>();
	}
	
	/**
	 * Clear counter for material loaded on ship this month
	 */
	public void monthClearMaterial(){
		materialMadeAll += materialMadeMonth;
		materialMadeMonth = 0;
	}
	
	/**
	 * Creates new ship
	 */
	public void createShip(){
		Ship sh = new Ship(this);
		ships.addToHeap(sh, sh.getAvailableTime().getValue());
		shipList.add(sh);
	}
	
}
