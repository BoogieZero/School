package function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import generator.Loader;
import objects.HasName;
import objects.Planet;
import objects.Ship;

/**
 * Class provides creation and observation on created orders and theirs fulfillment
 * 
 * @author Martin Hamet
 *
 */
public class Orders {
	/**
	 * Minimal order for one planet
	 */
	private static final int MINIMAL_OPENING_ORDER = 40000;
	
	/**
	 * Heap of created orders
	 */
	private final Heap heap;
	
	/**
	 * list of all planets
	 */
	private final List<Planet> planets;
	
	/**
	 * Amount left from previous order
	 */
	private OrderItem left = null;
	
	/**
	 * Ordered material this month
	 */
	private long orderedMonth;
	
	/**
	 * All ordered material
	 */
	private long orderedAll;
	
	/**
	 * Population on all planets to this month
	 */
	private long populationMonth;
	
	/**
	 * Dead population for this month
	 */
	private int populationDiedMonth;
	
	/**
	 * All dead population
	 */
	private long populationDiedAll;
	
	/**
	 * Creates new order for each planet
	 */
	public void createOrders(){
		
		if(!heap.isEmpty()){
			System.out.println("ERROR - HEAP was NOT empty!");
			clearHeap(heap);
		}
				
		monthClear();
		for(Planet pl : planets){
			planetClearRoutines(pl);
			OrderItem oi = new OrderItem(pl, pl.createOrder());
			if(oi.order < MINIMAL_OPENING_ORDER){
				continue;
			}
			
			heap.addToHeap(oi, oi.order);
			orderedMonth += oi.order;
			populationMonth += pl.getPopulation();
		}
	}
	
	/**
	 * Clears counter for statistics each month
	 */
	private void monthClear(){
		orderedAll += orderedMonth;
		populationDiedAll += populationMonth;
		
		populationDiedMonth = 0;
		orderedMonth = 0;
		populationMonth = 0;
	}
	
	/**
	 * Clears heap of orders
	 * @param heap	heap of orders
	 */
	private void clearHeap(Heap heap){
		while(!heap.isEmpty()){
			heap.getTop();
		}
	}
	
	/**
	 * Clears left ordered material from planets which affect theirs population in case 
	 * there is not enough material delivered
	 * @param planet	planet for clear
	 */
	private void planetClearRoutines(Planet planet){
		populationDiedMonth += planet.clearOrther();
		planet.clearShipList();
	}
	
	/**
	 * Returns all dead population
	 * @return	all dead population
	 */
	public long getPopulationDiedAll(){
		return populationDiedAll;
	}
	
	/**
	 * Returns population which died this month
	 * @return	population which died this month
	 */
	public int getPopulationDiedMonth(){
		return populationDiedMonth;
	}
	
	/**
	 * Returns population on all planets for this month
	 * @return	population on all planets for this month
	 */
	public long getPopulationMonth(){
		return populationMonth;
	}
	
	/**
	 * Returns all ordered material
	 * @return	all ordered material
	 */
	public long getOrderedAll(){
		return orderedAll;
	}
	
	/**
	 * Returns material ordered this month
	 * @return	material ordered this month
	 */
	public long getOrderedMonth(){
		return orderedMonth;
	}
	
	/**
	 * Returns true if all orders have been dispatched
	 * @return	true if all orders have been dispatched
	 */
	public boolean isComplete(){
		return heap.isEmpty() && left == null;
	}
	
	/**
	 * Return next biggest order for dispatching or it's part
	 * @return	next order to dispatch
	 */
	public OrderItem getNextOrder(){
		OrderItem oi;
		
		if(left!=null){
			oi = left;
		}else{
			oi = (OrderItem)heap.getTop();
		}
		
		if(oi.order>Ship.DEFAULT_LOAD_CAPACITY){
			left = oi;
			left.order -= Ship.DEFAULT_LOAD_CAPACITY;
			return new OrderItem(oi.planet, Ship.DEFAULT_LOAD_CAPACITY);
		}else{
			left = null;
			return oi;
		}
	}
	
	/**
	 * Fills fullfilment of order assigned to given planet in given value
	 * @param p		dispatched planet
	 * @param value	dispatched value
	 */
	public void fillOrder(Planet p, int value){
		if(!heap.contains(p.getName())){
			return;
		}
		
		OrderItem oi = (OrderItem)heap.readElement(p.getName());
		
		if(value == oi.order){	//order is completed
			heap.getElement(p.getName());
		}else{
			heap.getElement(p.getName());	//order is reduced
			oi.order -= value;
			heap.addToHeap(oi, oi.order);
		}
	}
	
	/**
	 * Returns how much material is yet to be dispatched for given planet
	 * @param p	examined planet
	 * @return	material yet to be dispatched for given planet
	 */
	public int getOrderProgressLeft(Planet p){
		OrderItem oi;
		if(heap.contains(p.getName())){
			oi = (OrderItem)heap.readElement(p.getName());
			return oi.order;
		}else{
			return 0;		//order is complete
		}
	}
	
	/**
	 * Reverse dispatchment of order assigned to given planet by given amount
	 * @param planet			planet for reversing order
	 * @param reversedAmount	amount of reversed material
	 */
	public void reverseOrder(Planet planet, int reversedAmount){
		OrderItem oi = new OrderItem(planet, reversedAmount);
		heap.addToHeap(oi, oi.order);
	}
	
	/**
	 * Instantiates new Orders manager by given loader which provides list of all planets
	 * @param loader	Loader which provides list of all planets
	 */
	public Orders(Loader loader) {
		heap = new Heap(Heap.TypeOfHeap.MAX);
		this.orderedMonth = 0;
		this.populationMonth = 0;
		this.populationDiedMonth = 0;
		this.orderedAll = 0;
		this.populationDiedAll = 0;
		Map<String, Planet> gal = loader.getPlanets();
		this.planets = new ArrayList<Planet>(gal.values());
	}
	
	/**
	 * Class represents one order which can be dispatched by one ship
	 * @author Martin Hamet
	 *
	 */
	public static class OrderItem implements HasName{
		/**
		 * Planet which is order assigned to
		 */
		Planet planet;
		
		/**
		 * Amount of ordered material
		 */
		int order;
		
		/**
		 * Instantiates new OrderItem by given planet and ordered amount
		 * @param planet	Planet which is order assigned to	
		 * @param order		Amount of ordered material
		 */
		public OrderItem(Planet planet, int order) {
			this.planet = planet;
			this.order = order;
		}

		@Override
		public String getName() {
			return planet.getName();
		}
	}
	
}
