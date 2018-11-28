package function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import data.DijkstraController;
import function.Orders.OrderItem;
import function.TimeHandler.Time;
import objects.Factory;
import objects.HasName;
import objects.Path;
import objects.Planet;
import objects.Route;
import objects.Ship;
import objects.SpaceObject;
import objects.TimeStamp;

/**
 * Class which dispatch given orders, that means finding best factories creating routes and assigning 
 * those routes to factories by shortest route to target and primary so that less ships is needed for fulfilling 
 * orders. Routes are designed in such way so ship is using maximum it's cargo capacity if it's possible
 * 
 * @author Martin Hamet
 *
 */
public class Dispatcher {
	/**
	 * Actual given orders
	 */
	private static Orders actualOrders;
	
	/**
	 * Tries to unload at last added point on the way home in given Route
	 * @param rt			route which is worked on
	 * @param loadLeftT		load left
	 * @param wantedAmount	wanted amount to be unloaded at the last point in given Route
	 * @return				returns load left after possible unloading
	 */
	private int tryUnloadAtRTH(Route rt, int loadLeftT, int wantedAmount){
		int loadLeft = loadLeftT;
		if(wantedAmount <= 0){
			return loadLeft;
		}
		
		List<TimeStamp> actRTH = rt.getRouteToHome();
		Planet actPlanet = (Planet)actRTH.get(actRTH.size()-1).getObject();
		
		if(loadLeft >= wantedAmount){
			if(rt.setAction(Ship.Status.UNLOADING, wantedAmount)){
				actualOrders.fillOrder(actPlanet, wantedAmount);
				loadLeft -= wantedAmount;
			}
		}else{
			if(rt.setAction(Ship.Status.UNLOADING, loadLeft)){
				actualOrders.fillOrder(actPlanet, loadLeft);
				loadLeft = 0;
			}
		}
		return loadLeft;
	}
	
	/**
	 * Returns true if given TimeStamp is (by it's time attribute) in actual month
	 * @param ts	examined TimeStamp
	 * @return		true if if given TimeStamp is (by it's time attribute) in actual month
	 */
	private boolean isTSWithinMonth(TimeStamp ts){
		return TimeHandler.isInMonth(ts.getTime());
	}
	
	/**
	 * Returns true if given TimeStamp is (by it's time attribute) in actual month
	 * with reserve by given addedTime
	 * @param ts			examined TimeStamp
	 * @param addedTime		needed reserve in hours
	 * @return				true if if given TimeStamp is (by it's time attribute) in actual month
	 * 						with reserve by given addedTime
	 */
	private boolean isTSWithinMonth(TimeStamp ts, double addedTime){
		Time t = new Time(ts.getTime());
		t.add(addedTime);
		return TimeHandler.isInMonth(t);
	}
	
	/**
	 * Sets for given route amount to load at start point
	 * @param rt		route for setting loaded material
	 * @param loadLeft	amount to load
	 */
	private void setAmountToLoad(Route rt, int loadLeft){
		int load = Ship.DEFAULT_LOAD_CAPACITY - loadLeft;
		TimeStamp first = rt.getStartPoint();
		first.setAction(first.getAction(), load);
	}
	
	/**
	 * Returns true if subjected factory have or will have available ship in time
	 * for delivery to be made until end of actual month.
	 * 
	 * @param factory		subjected factory
	 * @param deliveryTime	time of completed delivery
	 * @return				true - available ship exist in chosen factory to make delivery
	 */
	private boolean isAvailable(Factory factory, double deliveryTime){
		Time timeOfShip = factory.getTimeToNextShip();
		//System.out.println("TimeOfNextShp: "+timeOfShip);
		timeOfShip.add(deliveryTime);
		return timeOfShip.getValue()
				<= TimeHandler.getTimeLeft(TimeHandler.getActualTime());
	
	}
	
	/**
	 * Returns true if given list isn't null
	 * @param list	examined list
	 * @return		true if list isn't null
	 */
	private boolean routeExist(List<SpaceObject> list){
		return !(list == null);
	}
	
	/**
	 * Calculates time needed for delivery to given target planet from nearest factory
	 * @param dj		provides shortest routes
	 * @param p			target planet
	 * @param danger	true - searches dangerous routes too
	 * @return			returns time needed for delivery to given target planet in hours
	 */
	private double resolveDeliveryTime(DijkstraController dj, Planet p, boolean danger){
		return resolveDeliveryTime(dj, p, 0, danger);
	}
	
	 /**
	  * Calculates time needed for delivery to given target planet
	  * @param dj		provides shortest routes
	  * @param p		target planet
	  * @param index	index in sorted list of factories by distance to target planet
	  * @param danger	true - searches dangerous routes too
	  * @return			returns time needed for delivery to given target planet in hours
	  */
	private double resolveDeliveryTime(DijkstraController dj, Planet p, int index, boolean danger){
		double length = dj.getValueOfRouteTo(p.getName(), index, danger);
		double deliveryTime = 24*(length / Ship.DEFAULT_SPEED) + Ship.DEFAULT_TIME_OF_ACTION*2; 
		//*2 loading at home and unloading at target
		return deliveryTime;
	}
	
	/**
	 * Checks radius from given TimeStamp other planets with not fully dispatched orders for dispersing
	 * left load
	 * Radius is defined by constant and time left from given actual point (tSo) and load left
	 * 
	 * @param tSo		actual point (start point for search)
	 * @param loadLeft	load left to be dispersed
	 * @return			most valuable route (represented by list of TimeStamps) in the radius
	 * 					for dispersing maximum of left load 
	 */
	private List<SpaceObject> checkRadiusForOrders(TimeStamp tSo, int loadLeft){
		SpaceObject so = tSo.getObject();
		
		Time t = new Time(tSo.getTime());
		Time tLeft = new Time(TimeHandler.getTimeLeft(t));
		double distanceAtDisposal = tLeft.getValue() * (Ship.DEFAULT_SPEED/24);
		
		Heap heap = new Heap(Heap.TypeOfHeap.MAX);
		SearchBug firstBug = new SearchBug(so, 0, 0);
		SearchBug.makeBugs(heap, firstBug, so, loadLeft, distanceAtDisposal);
		
		List<SpaceObject> list = new ArrayList<SpaceObject>();
		
		if(heap.isEmpty()){
			return list;
		}
		
		SearchBug winner = (SearchBug)heap.getTop();
		if(winner.value == 0){
			return list;
		}
		
		//move to last valid (bug where it's value changed)
		while(winner.spaceObject == winner.parrent.spaceObject){
			winner = winner.parrent;
		}
		
		while(true){	//go to parrents until we come to firstBug
			if(winner.parrent == null){
				break;
			}
			list.add(winner.spaceObject);
			winner = winner.parrent;
		}
		Collections.reverse(list);
		
		return list;
	}

	/**
	 * Fills given dispersing route by most valuable found jump to planets without fully dispatched orders
	 * @param tSo		actual point in route
	 * @param loadLeftT	load left
	 * @param rt		route to fill
	 * @return			load left after unloading (dispersing) left load on the new route
	 */
	private int nextDisperseJumpRTHPoints(TimeStamp tSo, int loadLeftT, Route rt){
		int loadLeft = loadLeftT;
		List<SpaceObject> list = checkRadiusForOrders(tSo, loadLeft);
		
		for(SpaceObject soL : list){
			rt.addRTHPoint(soL);
			if(!isTSWithinMonth(rt.getLast(), Ship.DEFAULT_TIME_OF_ACTION)){
				break;
			}
			
			if(soL instanceof Planet){
				Planet plOnWay = (Planet)soL;
				int needed = actualOrders.getOrderProgressLeft(plOnWay);
				loadLeft = tryUnloadAtRTH(rt, loadLeft, needed);
			}
		}
		return loadLeft;
	}

	/**
	 * Provides given route while dispersing by next given point on general way home from actual position
	 * @param so		next point on the way home
	 * @param rt		route to fill
	 * @param loadLeftT	load left
	 * @return			load left after possible unloading (dispersing) on this point
	 */
	private int nextDisperseMainRTHPoint(SpaceObject so, Route rt, int loadLeftT){
		int loadLeft = loadLeftT;
		rt.addRTHPoint(so);
		
		if(!isTSWithinMonth(rt.getLast(), Ship.DEFAULT_TIME_OF_ACTION)){
			return loadLeft;	//out of time limit
		}
		
		if(so instanceof Planet){
			Planet plOnWay = (Planet)so;
			int needed = actualOrders.getOrderProgressLeft(plOnWay);
			loadLeft = tryUnloadAtRTH(rt, loadLeft, needed);
		}
		//main road point solved
		return loadLeft;
	}

	/**
	 * Fills given route on the way home in such way that ship can unload rest of it's left load on near
	 * planets if possible
	 * This method primary search for dispersing left load on non-dangerous paths if it's possible
	 * If every given load left was dispersed given route is filled by generic fastest way home
	 * @param rt		Route to fill
	 * @param loadLeftT	load left 
	 * @return			load left after dispersing
	 */
	private int fillDisperseRTH(Route rt, int loadLeftT){
		int loadLeft = loadLeftT;
		SpaceObject target = rt.getTargetPoint().getObject();
		rt.addRTHPoint(target);
		
		SpaceObject start = rt.getStartPoint().getObject();
		List<SpaceObject> djHome;
		
		while(true){
			SpaceObject actPosition = rt.getRouteToHome().get(rt.getRouteToHome().size()-1).getObject();
			
			if(rt.isEnded()){
				//ship is home
				break;
			}
			
			if(loadLeft <= 0 || !isTSWithinMonth(rt.getLast(), Ship.DEFAULT_TIME_OF_ACTION)){
				//ship is empty or is next month
				fillGenericRTH(rt, actPosition);
				break;
			}
			
			djHome = TimeHandler.getDijkstra().getRouteTo(
											actPosition.getName(), 
											start.getName(), 
											false);
			
			if(!routeExist(djHome)){
				djHome = TimeHandler.getDijkstra().getRouteTo(
						actPosition.getName(), 
						start.getName(), 
						true);
			}
			
			Collections.reverse(djHome);
			SpaceObject so = djHome.get(1);	//Next point after this actual point
			
			loadLeft = nextDisperseMainRTHPoint(so, rt, loadLeft);
			if(rt.isEnded()){
				break;
			}
			
			if(loadLeft > 0 && isTSWithinMonth(rt.getLast())){	//some load left -> search for possible unload
				loadLeft = nextDisperseJumpRTHPoints(rt.getLast(), loadLeft, rt);
			}
		}
		return loadLeft;
	}

	/**
	 * Fills given route by fastest possible route to start point
	 * @param rt			route for filling
	 * @param actPosition	actual position in given route
	 */
	public static void fillGenericRTH(Route rt, SpaceObject actPosition){
		List<SpaceObject> fastRTH;
		TimeStamp tsStart = rt.getStartPoint();
		TimeStamp tsTarget;
		
		if(rt.getRouteToHome().size() > 0){
			//already running home
			tsTarget = rt.getRouteToHome().get(rt.getRouteToHome().size()-1);	
			//target = last added waypoint
		}else{
			//RTH just started
			tsTarget = rt.getTargetPoint();
			rt.addRTHPoint(tsTarget.getObject());
		}
		
		fastRTH = TimeHandler.getDijkstra().getRouteTo(
				tsTarget.getObject().getName(), 
				tsStart.getObject().getName(), 
				true);
		
		//RTH
		
		if(fastRTH == null){
			//ship is already home
			return;
		}
		Collections.reverse(fastRTH); 	//route from target to home
		Iterator<SpaceObject> it = fastRTH.iterator();
		
		if( actPosition == fastRTH.get(0)){
			it.next();
		}
		
		while(it.hasNext()){
			SpaceObject so = it.next();
			rt.addRTHPoint(so);
		}

	}

	/**
	 * Fills given route by given way to target without danger
	 * @param djRoute	route to target
	 * @param rt		route which is being worked on
	 * @param oi		actual order
	 */
	private void fillGenericRTT(List<SpaceObject> djRoute, Route rt, OrderItem oi){
		//RTT
		Iterator<SpaceObject> it = djRoute.iterator();
		rt.addRTTPoint(it.next());
		rt.setAction(Ship.Status.LOADING, 0);
	    while (it.hasNext()){
	    	SpaceObject so = it.next();
	    	rt.addRTTPoint(so);
	    }
	    rt.setAction(Ship.Status.UNLOADING, oi.order);
	    Planet pt = (Planet)rt.getTargetPoint().getObject();
	    actualOrders.fillOrder(pt, oi.order);
	}

	/**
	 * Creates route by given route to target represented by list of SpaceObjects
	 * and assign this route to corresponding factory
	 * @param list	route to target represented by list of SpaceObejcts
	 * @param oi	demanded order
	 */
	private void assignRoute(List<SpaceObject> list, OrderItem oi){
		Factory fct = (Factory)list.get(0);
		
		Time t = fct.getTimeOfNextShip();
		Route rt  = new Route(t);
		int loadLeft;
		fillGenericRTT(list, rt, oi);
		
		if(oi.order == Ship.DEFAULT_LOAD_CAPACITY){
			SpaceObject target = rt.getTargetPoint().getObject();//rt.getRouteToTarget().get(rt.getRouteToTarget().size()-1).getObject();
			fillGenericRTH(rt, target);
			setAmountToLoad(rt, 0);
			fct.assignTask(rt);
			return;
		}
		
		loadLeft = Ship.DEFAULT_LOAD_CAPACITY - oi.order;
		loadLeft = fillDisperseRTH(rt, loadLeft);
		setAmountToLoad(rt, loadLeft);
		
		fct.assignTask(rt);
	}

	/**
	 * Prepares nearest factory if it's able to deliver order in time and creates new ship at this factory
	 * @param dj		provides shortest routes
	 * @param p			target planet
	 * @param danger	true - searches dangerous routes too
	 * @return			returns prepared nearest factory
	 */
	private Factory prepareNearestFactory(DijkstraController dj, Planet p, boolean danger){
		Factory chosenFct;
		List<SpaceObject> chR = dj.getRouteTo(p.getName(), 0, danger);
		if(!routeExist(chR)){
			return null;
		}
		chosenFct = (Factory)chR.get(0);
		
		double deliveryTime = resolveDeliveryTime(dj, p, danger);
		Time t = new Time(deliveryTime);
		
		if(	 !(t.getValue() <= TimeHandler.getTimeLeft(TimeHandler.getActualTime()) )  ){
			return null; 	//is NOT possible
		}
		
		chosenFct.createShip();
		return chosenFct;
	}

	/**
	 * Checks if given factory is able to provide ship for this order
	 * @param dj		provides shortest routes
	 * @param actFct	examined factory
	 * @param indexF	index in sorted list of factories by distance to target planet
	 * @param p			target planet
	 * @param danger	true - searches dangerous routes too
	 * @return			true if given factory is able to provide ship for this order
	 */
	private boolean checkFactory(DijkstraController dj, Factory actFct, int indexF, Planet p, boolean danger){
		double deliveryTime = resolveDeliveryTime(dj, p, indexF, danger);
		return isAvailable(actFct, deliveryTime);
	}

	/**
	 * Return false if delivery is too far away so it is NOT possible to get there in one
	 * month travel time.
	 * @param deliveryTime	time of completed delivery
	 * @return				true - if delivery is possible to deliver in one month
	 */
	private boolean isDeliveryInRange(double deliveryTime){
		return deliveryTime <= TimeHandler.getTimeLeft(TimeHandler.getActualTime());
	}

	/**
	 * Returns true if delivery is even possible in one Month travel time to target planet
	 * @param dj		provides shortest routes
	 * @param p			target planet
	 * @param danger	true - searches dangerous routes too
	 * @return			true true if delivery is possible in one Month travel time to target planet
	 */
	private boolean isDeliveryPossibleinMonth(DijkstraController dj, Planet p, boolean danger){
		double deliveryTime = resolveDeliveryTime(dj, p, danger);
		return (isDeliveryInRange(deliveryTime));
	}
	
	/**
	 * Checks all factories for one which can fulfill given order for planet or creates new ship at
	 * nearest factory if it's possible to reach target in time if not returns null
	 * @param dj		provides shortest routes
	 * @param p			target planet
	 * @param danger	true - searches dangerous routes too
	 * @return			chosen factory
	 * 					null if not found
	 */
	private Factory checkFactories(DijkstraController dj, Planet p, boolean danger){
		Factory chosenFct;
		int indexF = 0;		//index in sorted field of nearest factories
		
		List<SpaceObject> chR = dj.getRouteTo(p.getName(), indexF, danger);
		
		if(!routeExist(chR)){
			//Route doesn't exist
			return null;
		}
		
		chosenFct = (Factory)chR.get(0);
		
		if(!isDeliveryPossibleinMonth(dj, p, danger)){	//if delivery is NOT in range
			//This if is not necessary for default size of map
			return null;
		}
		//Delivery is theoretically possible at this point
		
		Factory actFct = chosenFct;
		
		if(checkFactory(dj, actFct, indexF, p, danger)){	//first factory
			chosenFct = actFct;
			return chosenFct;				//available factory is chosen
		}
		
		//searches all other factories
		while(true){
			indexF++;
			
			if(indexF >= 5){	//there isn't available factory
				//chose nearest and create new ship
				chosenFct = prepareNearestFactory(dj, p, danger);
				break;			
			}
			
			chR = dj.getRouteTo(p.getName(), indexF, danger);
			if(!routeExist(chR)){
				//Route doesn't exist
				continue;
			}
			actFct = (Factory)chR.get(0);
			
			if(checkFactory(dj, actFct, indexF, p, danger)){
				chosenFct = actFct;
				break;
			}
		}
		return chosenFct;
	}

	/**
	 * Finds suitable factory for given planet by first searching for safe route and then for dangerous one
	 * if there is not possible to reach given planet in time returns null
	 * @param p	planet which demanded order
	 * @return	created route for order
	 * 			null	if there ist't way to reach target planet in time
	 */
	private List<SpaceObject> findFactory(Planet p){
		Factory chosenFct;
		DijkstraController dj = TimeHandler.getDijkstra();
		
		chosenFct = checkFactories(dj, p, false);	//without danger
		boolean danger = false;
		
		if(chosenFct == null){
			chosenFct = checkFactories(dj, p, true);
			danger = true;
		}
		
		if(chosenFct == null){
			return null;
		}
		
		return dj.getRouteTo(p.getName(), chosenFct.getName(), danger);
	}

	/**
	 * Dispatch order tries to dispatch given order by finding suitable factory and assigning
	 * created route to the factory
	 * @param oi	order to dispatch
	 */
	private void dispatchOrder(OrderItem oi){
		List<SpaceObject> list = findFactory(oi.planet);
		if(list == null){
			return;
		}
		assignRoute(list, oi);
	}
	
	/**
	 * Iterates through given orders and tries to dispatch them
	 * @param orders	orders to dispatch
	 */
	public void dispatchOrders(Orders orders){
		actualOrders = orders;
		while( ! orders.isComplete() ){
			dispatchOrder(orders.getNextOrder());
		}
	}
	
	/**
	 * Class provides recursive searching mechanism from starting point for most valuable route in set radius
	 * 
	 * @author Martin Hamet
	 *
	 */
	public static class SearchBug implements HasName{
		/**
		 * Radius limit
		 */
		public static final double 	MAX_DISTANCE = 
				(Ship.DEFAULT_TIME_OF_ACTION*2 * (Ship.DEFAULT_SPEED/24)) +
				10;
		
		/**
		 * Point on route
		 */
		SpaceObject spaceObject;
		
		/**
		 * Distance traveled to this SpaceObject
		 */
		double distance;
		
		/**
		 * Value of this route by it's ordered material
		 */
		int value;
		
		/**
		 * Parent which created this bug
		 */
		SearchBug parrent;
		
		/**
		 * Check if distance is within set radius
		 * @param bug	examined bug
		 * @param DAD	distance at disposal
		 * @return		true - if examined bug have distance in radius
		 */
		private static boolean checkDistance(SearchBug bug, double DAD){
			return !(	bug.distance > SearchBug.MAX_DISTANCE || 
						bug.distance >= DAD);
		}
		
		/**
		 * Decide if already found bug in heap should be replaced by new bug
		 * @param heap	heap of bugs
		 * @param bug	new found bug
		 * @return		true if given new bug should create more bugs
		 * 				false end of it's life
		 */
		private static boolean replaceBug(Heap heap, SearchBug bug){
			if(heap.contains(bug.getName())){
				SearchBug bugInMap = (SearchBug)heap.readElement(bug.getName());
				if(bugInMap.value < bug.value){	
					//new bug is more valuable
					heap.getElement(bug.getName());
					heap.addToHeap(bug, bug.value);
					return false;	//continue searching
				}else{
					//oldbug is better or the same as the new one
					if(bugInMap.value == bug.value){
						if(bugInMap.distance > bug.distance){
							//new bug is nearer than old one
							heap.getElement(bug.getName());
							heap.addToHeap(bug, bug.value);
							bugInMap = bug;
							return false;	//continue searching
						}
						//new bug is bad -> end it!
						return true;//break;
					}else{
						//new bug is less valuable than old one -> end it!
						return true;//break;
						//old bug.value > new bug
					}
				}
			}else{
				//heap does not contain such bug
				heap.addToHeap(bug, bug.value);			//add bug to heap by value
				return false;	//continue searching
			}
		}
		
		/**
		 * Checks given path if it's target is Planet and checks radius if so creates new bug on target
		 * @param pt		path to target
		 * @param heap		heap of bugs
		 * @param parrent	parent of possible new bugs
		 * @param DAD		distance at disposal (affects radius)
		 * @param loadLeft	load left in parent bug
		 * @return			true if parent bug reached radius limit
		 */
		private static boolean checkPath(Path pt, Heap heap, SearchBug parrent, double DAD, int loadLeft){
			if(pt.getTarget() instanceof Planet && !(pt.isDanger())){
				Planet pl = (Planet)pt.getTarget();
				int orderSize = actualOrders.getOrderProgressLeft(pl);
				double distance = pt.getLength() + parrent.distance;
				if(orderSize > 0){
					distance += Ship.DEFAULT_TIME_OF_ACTION * (Ship.DEFAULT_SPEED/24);	
				}
					//adding one day worth distance (substitute for unloading time)
				
				SearchBug bug = new SearchBug(	pl, 
												distance, 
												orderSize + parrent.value,
												parrent);
				
				if(!checkDistance(bug, DAD)){
					return false;
				}
				
				if(replaceBug(heap, bug)){
					return true;
				}
				
				if(bug.value < loadLeft){			//if there is not enough ordered material make more bugs
					makeBugs(heap, bug, bug.spaceObject, loadLeft, DAD);
				}
			}
			return false;
		}
		
		/**
		 * Creates new bugs for each neighbor of given SpaceObject
		 * @param heap		heap of bugs
		 * @param parrent	parent of possible created bugs
		 * @param from		start point for this branch of bugs
		 * @param loadLeft	load left of given parent
		 * @param DAD		distance at disposale (affects radius)
		 */
		public static void makeBugs(	Heap heap, 
										SearchBug parrent,
										SpaceObject from,
										int loadLeft,
										double DAD	//distance at disposal
										){
			
			Map<String, Path> neigh = from.getNeighbours();
			
			for(Map.Entry<String, Path> pth : neigh.entrySet()){	//run through neighbors
				Path pt = pth.getValue();
				if(checkPath(pt, heap, parrent, DAD, loadLeft)){
					break;
				}
			}
		
		}
		
		/**
		 * Instantiates new SeachBug which represents point on the found route with distance
		 * lead to this point and value of this route by orders
		 * @param spaceObject	positions of this bug
		 * @param distance		distance lead from first bug to this point
		 * @param value			value of route to this point (from the first bug)
		 * @param parrent		parent of this bug
		 */
		public SearchBug(SpaceObject spaceObject, double distance, int value, SearchBug parrent) {
			this.spaceObject = spaceObject;
			this.distance = distance;
			this.value = value;
			this.parrent = parrent;
		}
		
		/**
		 * Instantiates first bug at given SpaceObject with given distance and value
		 * @param spaceObject	position of first bug
		 * @param distance		distance of first bug
		 * @param value			value of first bug
		 */
		public SearchBug(SpaceObject spaceObject, double distance, int value){
			this(spaceObject, distance, value, null);
		}

		@Override
		public String getName() {
			return spaceObject.getName();
		}
	}
	
}

