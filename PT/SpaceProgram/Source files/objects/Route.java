package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import function.Dispatcher;
import function.TimeHandler;
import function.TimeHandler.Time;

/**
 * Class represents set of TimeStamps which are going to be visited on the way to target and to home 
 * by ship with this Route.
 * Provides tools for creating route for ship and setting actions to individual TimeStamps on this route
 * 
 * @author Martin Hamet
 *
 */
public class Route {
	/**
	 * Represents route to primary target
	 */
	private final List<TimeStamp> routeToTarget;
	
	/**
	 * Represents route for returning to home
	 */
	private final List<TimeStamp> routeToHome;
	
	/**
	 * Length of whole route in ightyears
	 */
	private double routeLength;
	
	/**
	 * Time of arrival at home destination
	 */
	private final Time returnTime;
	
	/**
	 * Map of all actions on this route
	 */
	private final Map<SpaceObject, TimeStamp> actions;
	
	/**
	 * Time of start
	 */
	private final Time startTime;
	
	/**
	 * Last added TimeStamp to this route
	 * It's meant for creation
	 */
	private TimeStamp actual;
	
	/**
	 * Material lost on this route due to pirates
	 */
	private int lost;
	
	/**
	 * True indicates successfully attacked route by pirates
	 */
	private boolean looted;
	
	/**
	 * Returns true if this route was looted
	 * 
	 * @return	true looted Route
	 */
	public boolean isLooted(){
		return looted;
	}
	
	/**
	 * Returns amount of material that was loaded on StartPoint
	 * @return	material loaded at start
	 */
	public int getLoaded(){
		return getStartPoint().getAmount();
	}
	
	/**
	 * Returns amount of successfully distributed material on this Route 
	 * @return	amount of successfully distributed material on this Route 
	 */
	public int getDistributed(){
		TimeStamp ts = getStartPoint();
		int val = ts.getAmount();
		return val - lost;
	}
	
	/**
	 * Returns route to target represented by list of TimeStamps
	 * @return	route to target represented by list of TimeStamps
	 */
	public List<TimeStamp> getRouteToTarget(){
		return new ArrayList<TimeStamp>(routeToTarget);
	}
	
	/**
	 * Returns route to home represented by list of TimeStamps
	 * @return	route to home represented by list of TimeStamps
	 */
	public List<TimeStamp> getRouteToHome(){
		return new ArrayList<TimeStamp>(routeToHome);
	}
	
	/**
	 * Returns last added TimeStamp to this Route
	 * @return	last added TimeStamp to this Route
	 */
	public TimeStamp getLast(){
		return actual;
	}
	
	/**
	 * Returns first TimeStamp added to this Route
	 * @return	first TimeStamp added to this Route
	 */
	public TimeStamp getStartPoint(){
		return routeToTarget.get(0);
	}
	
	/**
	 * Returns map of all actions set on this Route
	 * @return	map of all actions set on this Route
	 */
	public Map<SpaceObject, TimeStamp> getActions(){
		return actions;
	}
	
	/**
	 * Returns primary target point of this Route
	 * @return	primary target point of this Route
	 */
	public TimeStamp getTargetPoint(){
		return routeToTarget.get(routeToTarget.size()-1);
	}
	
	/**
	 * Returns length of whole Route in lightyears
	 * @return	length of whole Route
	 */
	public double getRouteLength(){
		return routeLength;
	}
	
	/**
	 * Returns time how much time is required to complete this route
	 * @return	required time for completing this Route
	 */
	public Time getRouteTime(){
		Time route = new Time(returnTime);
		route.sub(startTime);
		return route;
	}
	
	/**
	 * Returns time of arrival at home destination
	 * @return	Time of arrival at home destination
	 */
	public Time getReturnTime(){
		return new Time(returnTime);
	}
	
	/**
	 * Returns time of start this Route
	 * @return	Time of start this Route
	 */
	public Time getStartTime(){
		return new Time(startTime);
	}
	
	/**
	 * Adds given SpaceObject to route to target as TimeStamp
	 * Calculates times of arrival to this TimeStamp by previous added points and StartTime of this Route
	 * and sets actual (last added point) to this one
	 * @param so	SpaceObject which s to be added to route to target
	 */
	public void addRTTPoint(SpaceObject so){
		Time timeToNext = new Time(0);
		if(actual != null){
			addTimeOfAction(timeToNext); 		//add time of acton on actual point to travel time to next
			addTimeOfTravel(timeToNext, so);	//add travel time to next RTT point
			
			returnTime.setValue(timeToNext); 			//add timeToNext to overal time of route
			
			TimeStamp ts = new TimeStamp(so, timeToNext, Ship.Status.INFLIGHT);
			routeToTarget.add(ts);
			actual = ts;
		}else{
			//there wasn't previous point in route (it's starting point)
			timeToNext.add(startTime);
			TimeStamp ts = new TimeStamp(so, timeToNext, Ship.Status.INFLIGHT);
			routeToTarget.add(ts);
			actual = ts;
		}
	}
	
	/**
	 * Adds given SpaceObject to route to home as TimeStamp
	 * Calculates times of arrival to this TimeStamp by previous added points and StartTime of this Route
	 * and sets actual (last added point) to this one
	 * @param so	SpaceObject which s to be added to route to target
	 */
	public void addRTHPoint(SpaceObject so){
		Time timeToNext = new Time(0);
		
		if( !(routeToHome.size() == 0) ){
			addTimeOfAction(timeToNext); 		//add time of acton on actual point to travel time to next
			//time of leaving target
			addTimeOfTravel(timeToNext, so);	//add travel time to next RTH point
		}else{
			TimeStamp last = routeToTarget.get(routeToTarget.size()-1);
			timeToNext.add(last.getTime());
			addTimeOfAction(timeToNext);
		}
		
		returnTime.setValue(timeToNext); 			//add timeToNext to overal time of route
		
		TimeStamp ts = new TimeStamp(so, timeToNext, Ship.Status.INFLIGHT);
		routeToHome.add(ts);
		actual = ts;
	}
	
	/**
	 * Returns true for dangerous paths between TimeStamp first and TimeStamp second
	 * 
	 * @param first		first TimeStamp
	 * @param second	second TimeStamp
	 * @return			true	dangerous path between first and second
	 */
	private boolean isDanger(TimeStamp first, TimeStamp second){
		Path pth = first.getObject().getNeighbours().get(second.getObject().getName());
		return pth.isDanger();
	}
	
	/**
	 * Iterates trough given part of Route (list of TimeStamps) and registers PirateEncouters to Watchdog 
	 * if path is dangerous and given ship have load which could be stolen
	 * @param ship		ship moving on given part of Route
	 * @param list		given part of Route
	 * @param loadLeftT	actual load on given ship
	 * @return			returns load left on ship after proceeding through this part of Route
	 */
	private int regRT(Ship ship, List<TimeStamp> list, int loadLeftT){
		int loadLeft = loadLeftT;
		Iterator<TimeStamp> ite = list.iterator();
		TimeStamp prev = ite.next();
		if(prev.getAction() == Ship.Status.UNLOADING){
			loadLeft -= prev.getAmount();
		}
		
		TimeStamp act;
		double checkTime, firstT, secondT;
		while(ite.hasNext()){
			if(loadLeft <= 0){
				break;
			}
			act = ite.next();
			
			if(act.getAction() == Ship.Status.UNLOADING){
				loadLeft -= act.getAmount();
			}
				
			if(isDanger(prev, act)){
				//path is dangerous
				firstT = prev.getTime().getValue();
				secondT = act.getTime().getValue();
				if(Ship.Status.actionToPerform(prev.getAction())){
					//action is taking place
					firstT += Ship.DEFAULT_TIME_OF_ACTION;
				}
				checkTime = (firstT + secondT) / 2;
				TimeHandler.getWatchDog().add(ship, new Time(checkTime));
			}
			prev = act;
		}
		return loadLeft;
	}

	/**
	 * Registers pirate encounters to WatchDog from this Route
	 * @param ship	ship which is this Route assigned to
	 */
	public void registerDangersToWatch(Ship ship){
		int loadLeft = getStartPoint().getAmount();
		loadLeft = regRT(ship, routeToTarget, loadLeft);
		loadLeft = regRT(ship, routeToHome.subList(1, routeToHome.size()), loadLeft);
		
		if(loadLeft != 0){
			System.out.println("ERROR - loadLeft in ROUTE: "+loadLeft);
			System.exit(0);
		}
	}
	
	/**
	 * Adds time of action to given time if on actual(last added TimeStamp to this Route) is action rquired
	 * from ship
	 * @param timeToNext	Time potentially increased by Time of action
	 */
	private void addTimeOfAction(Time timeToNext){
		//if actual have non-default action + time of that action
		if(Ship.Status.actionToPerform(actual.getAction())){
			timeToNext.add(Ship.DEFAULT_TIME_OF_ACTION);
		}
	}
	
	/**
	 * Adds to given Time time for ship to travel from actual(last added TimeStamp to this Route) to
	 * given SpaceObject
	 * @param timeToNext	Time potentially increased by Time of travel between actual and given SpaceObject
	 * @param so			next object in this Route
	 */
	private void addTimeOfTravel(Time timeToNext, SpaceObject so){
		double length = getLengthTo(so);
		routeLength += length;
		
		timeToNext.add( (length / Ship.DEFAULT_SPEED) * 24);	//*24h -> one day
		timeToNext.add(actual.getTime());
	}
	
	/**
	 * Returns length between actual and given SpaceObjet
	 * 
	 * @param so	next object in Route
	 * @return		length from actual to given object
	 */
	private double getLengthTo(SpaceObject so){
		SpaceObject pa = actual.getObject();
		return (pa.getNeighbours().get(so.getName()) ).getLength();
	}
	
	/**
	 * Sets action for last added point in Route
	 * @param action	type of action
	 * @param amount	amount corresponding to given action or zero
	 * @return			true - successfully set actions
	 */
	public boolean setAction(Ship.Status action, int amount){
		if( TimeHandler.isInMonth(actual.getTime()) ){
			actual.setAction(action, amount);
			actions.put(actual.getObject(), actual);
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * Adds up lost material on given TimeStamp and removes given TimeStamp from map of actions on this Route
	 * @param ts	point which is going to be removed from route
	 */
	private void reverseAction(TimeStamp ts){
		if(ts.getAction() == Ship.Status.UNLOADING){
			TimeHandler.getOrders().reverseOrder((Planet)ts.getObject(), ts.getAmount());	/////
			lost += ts.getAmount();
			//actions.remove(ts.getObject());
		}
	}
	
	/**
	 * Clears rest of part of Route given by list from given time forward
	 * @param list			part of Route
	 * @param actualTime	time from which rest of part will be removed
	 */
	private void clearRestOfRoute(List<TimeStamp> list, double actualTime){
		double actTime = actualTime;
		Iterator<TimeStamp> it = list.iterator();
		TimeStamp ts = null;
		while(it.hasNext()){
			ts = it.next();
			if(ts.getTime().getValue() > actTime){
				break;
			}
		}
		reverseAction(ts);
		it.remove();
		while(it.hasNext()){
			ts = it.next();
			reverseAction(ts);
			it.remove();
		}
	}
	
	/**
	 * Alters this Route after successful pirate attack to fast return to home
	 * @param ship	ship which have assigned this route
	 */
	public void emergencyRoute(Ship ship){
		this.looted = true;
		double actTime = TimeHandler.getActualTime().getValue();
		if(actTime < getTargetPoint().getTime().getValue()+Ship.DEFAULT_TIME_OF_ACTION){
			//before reaching target
			clearRestOfRoute(routeToTarget, actTime);
			//Route to target was cleared
			routeToHome.clear();
			//Route to home was cleared
			addRTHPoint(getTargetPoint().getObject());
		}else{
			//on the way home
			clearRestOfRoute(routeToHome, actTime);
		}
		actual = routeToHome.get(routeToHome.size()-1);
		Dispatcher.fillGenericRTH(this, actual.getObject());
		TimeHandler.dispatchOrders();	/////
	}
	
	/**
	 * Instantiates new Route by given start time
	 * @param startTime	start time of this Route
	 */
	public Route(Time startTime) {
		this.routeToTarget = new ArrayList<TimeStamp>();
		this.routeToHome = new ArrayList<TimeStamp>();
		this.startTime = startTime;
		this.routeLength = 0;
		this.returnTime = new Time(0);
		this.actions = new HashMap<SpaceObject, TimeStamp>();
		this.lost = 0;
		this.looted = false;
	}
	
	/**
	 * Creates copy of given route
	 * @param rt	route for copy
	 */
	public Route(Route rt){
		this.routeToTarget = rt.getRouteToTarget();
		this.routeToHome = new ArrayList<TimeStamp>();
		this.startTime = new Time(rt.startTime);
		this.routeLength = 0;
		this.returnTime = new Time(0);
		this.actions = new HashMap<SpaceObject, TimeStamp>();
		this.lost = rt.lost;
		this.looted = rt.looted;
	}
	
	/**
	 * Return true if route is ended which means it starts and end on the same SpaceObject
	 * @return	true if Route is ended
	 */
	public boolean isEnded(){
		return ( 	routeToTarget.get(0).getObject() == 
					routeToHome.get(routeToHome.size()-1).getObject() 
					);
	}
	
	@Override
	public String toString(){
		String out = "Route to target: \n";
		for(TimeStamp ts : routeToTarget){
			out += "O: "+ts.getObject().getName()+" T:"+ts.getTime()+"\n";
		}
		out += "\n Route to home: \n";
		for(TimeStamp ts : routeToHome){
			out += "O: "+ts.getObject().getName()+" T:"+ts.getTime()+"\n";
		}
		out += "\n Actions: \n";
		TimeStamp ts;
		for(Map.Entry<SpaceObject, TimeStamp> tsM : actions.entrySet()){
			ts = tsM.getValue();
			out += 	"O: "+ts.getObject().getName()+" T:"+ts.getTime()+"\n"+
					"	Action: "+ts.getAction()+" : "+ts.getAmount()+"\n";
		}
		return out;
	}
}
