package objects;

import function.TimeHandler;
import function.TimeHandler.Time;

/**
 * Wrapper class TimeStamp provides time attribute for SpaceObject. Specificly for class
 * Route where is required time of arrival to given destination.
 * 
 * @author Martin Hamet
 * 8.10.2015
 *
 */
public class TimeStamp implements Comparable<TimeStamp>{
	
	/**
	 * SpaceObject
	 */
	private SpaceObject object;
	
	/**
	 * Time of arrival at SpaceObject
	 */
	private TimeHandler.Time time;
	
	/**
	 * Action which should be done at given SpaceOject
	 */
	private Ship.Status action;
	
	/**
	 * Amount of goods for given action
	 */
	private int amount;
	
	/**
	 * Returns SpaceObject
	 * @return	SpaceObject
	 */
	public SpaceObject getObject(){
		return object;
	}
	
	/**
	 * Returns time for SpaceObject
	 * 
	 * @return	time for SpaceObject
	 */
	public TimeHandler.Time getTime(){
		return time;
	}
	
	/**
	 * Returns amount of goods for this TimeStamp
	 * 
	 * @return	amount of goods for this TimeStamp
	 */
	public int getAmount(){
		return amount;
	}
	
	/**
	 * Sets type of action and amount of goods for this timestamp
	 * 
	 * @param action	type of action Ship.Status
	 * @param amount	amount of goods for given action
	 */
	public void setAction(Ship.Status action, int amount){
		this.action = action;
		this.amount = amount;
	}
	
	/**
	 * Returns action set for SpaceObject
	 * 
	 * @return	action for SpaceObject
	 */
	public Ship.Status getAction(){
		return action;
	}

	/**
	 * Instantiates class TimeStamp where time and action are assigned to SpaceObject
	 * 
	 * @param object	SpaceObject for action and time
	 * @param time		time of arrival to SpaceObject
	 * @param action	type of action at SpaceObject
	 * @param amount	amount associated with action on this TimeStamp
	 */
	public TimeStamp(SpaceObject object, TimeHandler.Time time, Ship.Status action, int amount) {
		this.object = object;
		this.time = time;
		this.action = action;
		this.amount = amount;
	}
	
	/**
	 * Instantiates class TimeStamp where time and action are assigned to SpaceObject
	 * 
	 * @param object	SpaceObject for action and time
	 * @param time		time of arrival to SpaceObject
	 * @param action	type of action at SpaceObject
	 */
	public TimeStamp(SpaceObject object, TimeHandler.Time time, Ship.Status action) {
		this(object, time, action, 0);
	}
	
	/**
	 * Instantiates class TimeStamp where is time assigned to SpaceObject with default
	 * type of action.
	 * 
	 * 
	 * @param object	SpaceObject for time
	 * @param time		time of arrival to SpaceObject
	 */
	public TimeStamp(SpaceObject object, TimeHandler.Time time) {
		this(object, time, Ship.Status.NONE, 0);
	}
	
	/**
	 * Creates copy of given TimeStamp
	 * 
	 * @param ts desired TimeStamp
	 */
	public TimeStamp(TimeStamp ts){
		this.object = ts.object;
		this.time = new Time(ts.time);
		this.action = ts.action;
		this.amount = ts.amount;
	}
	
	@Override
	public int compareTo(TimeStamp o) {
		double val = this.time.getValue() - o.getTime().getValue();
		if(val == 0){
			return 0;
		}
		
		return (val > 0)? 1 : -1;
	}
	
	@Override
	public String toString(){
		String s = String.format("%-20s %-15s %-15s %-15s", 
					object.getName(), time.toString(), action.toString(), amount);
				
		return s;//object.getName()+"		"+time.toString()+"		"+action.toString()+"		"+amount;
	}

}
