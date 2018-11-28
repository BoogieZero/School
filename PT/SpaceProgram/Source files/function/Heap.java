package function;

import java.util.HashMap;
import java.util.Map;
import objects.HasName;

/**
 * Class Heap
 * This class is heap with all properties of heap.
 * This class contains all method for work with heap.
 * @author Jiří Lukáš
 * 
 */
public class Heap {
	
	/**
	 * Type of heap
	 * Max - the biggest value is on top of heap.
	 * Min - the lowest value is on top of heap.
	 */
	private TypeOfHeap heapType;
	/**
	 * ElementCount
	 * How many elements is in heap
	 */
	private int elementCount = 0;
	/**
	 * TopIndex
	 * Peak of the heap
	 */
	private final int  TOPINDEX = 1;
	/**
	 * DEFAULT
	 * default size of heap
	 */
	private static final int DEFAULT = 7000;
	
	/**
	 * HeapArray
	 * Array for heap.
	 */
	private HeapElement [] heapArray;
	
	/**
	 * Heap map
	 * is Map with values in heap. If we need search value in heap.
	 */
	private final Map<String, Integer> heapMap = new HashMap<String, Integer>();
	
	/**
	 * Constructor 
	 * @param heapType Heap type - MAX or MIN
	 */
	public Heap(TypeOfHeap heapType) {
		this(heapType, DEFAULT);
	}
	/**
	 * Constructor (more complex)
	 * @param heapType Heap type - MAX or MIN
	 * @param size Size of heap
	 */
	public Heap(TypeOfHeap heapType, int size){
		this.heapType = heapType;
		this.heapArray = new HeapElement [size];
	}
	
	/**
	 * Method addToHeap
	 * is for adding value to the heap
	 * @param object object inserted to the heap
	 * @param value Value for sort object in heap.
	 */
	public void addToHeap(Object object, double value){
		HeapElement element = new HeapElement(object, value);
		heapArray[++elementCount] = element;
		if(object instanceof HasName){
			HasName hs = (HasName) object;
			heapMap.put(hs.getName(), new Integer(elementCount));
		}
		nahoru(elementCount);
	}
	/**
	 * Method getTop
	 * Return top object from heap and remove it.
	 * @return Top object from heap - If heap is empty return null;
	 */
	public Object getTop(){
		if(isEmpty()){
			return null;
		}
		Object object = heapArray[TOPINDEX].getObject();
		removeTop();
		return object;
	}
	/**
	 * Method readTop
	 * @return Top object from heap - If heap is empty return null;
	 */
	public Object readTop(){
		if(isEmpty()){
			return null;
		}
		return heapArray[TOPINDEX].getObject();
	}
	/**
	 * Method changeValue
	 * On specific index change value 
	 * @param index Index of object in heap which value we want to change.
	 * @param value New value
	 */
	public void changeValue(int index, double value){
		heapArray[index].setValue(value);
		nahoru(index);
		down(index);
	}
	/**
	 * Method readElement
	 * returning object by object name.
	 * If heap not contains this object return null.
	 * @param name Object name in heap.
	 * @return Return this object.
	 */
	public Object readElement (String name){
		if(heapMap.containsKey(name)){
			int index = heapMap.get(name);
			return heapArray[index].getObject();
		}else{
			return null;
		}
		
	}
	/**
	 * Metod getElement
	 * returning object by object name and remove it.
	 * If heap not contains this object return null.
	 * @param name Object name in heap.
	 * @return Return this object.
	 */
	public Object getElement (String name){
		if(heapMap.containsKey(name)){
			int index = heapMap.get(name);
			HeapElement element = heapArray[index];
			if(elementCount > 1){
				change(index, elementCount);
			}
			elementCount--;
			nahoru(index);
			down(index);
			heapMap.remove(name);
			return element.getObject();
			
		}else{
			return null;
		}
		
	}
	/**
	 * Method contains
	 * @param name Object name in heap.
	 * @return If heap contains this object return true. If not return false.
	 */
	public boolean contains(String name){
		return heapMap.containsKey(name);
	}
	/**
	 * Method isEmpty
	 * @return Return true if heap is empty. If not return false.
	 */
	public boolean isEmpty(){
		return elementCount == 0;
	}
	/**
	 * Method removeTop
	 * removing top from heap and rebuilding heap.
	 */
	private void removeTop(){
		change(TOPINDEX, elementCount);
		
		if(heapArray[elementCount].getObject() instanceof HasName){
			HasName name = (HasName) heapArray[elementCount].getObject();
			heapMap.remove(name.getName());
		}
		
		elementCount--;
		down(TOPINDEX);
		
	}
	/**
	 * Method change
	 * changing two object in heap. This method is used for rebuilding heap.
	 * @param index1 Index of first object in heap.
	 * @param index2 Index of second object in heap.
	 */
	private void change(int index1, int index2){
		
		if(heapArray[index1].getObject() instanceof HasName){
			HasName name1 = (HasName) heapArray[index1].getObject();
			HasName name2 = (HasName) heapArray[index2].getObject();
		
			heapMap.put(name1.getName(), index2);
			heapMap.put(name2.getName(), index1);
		}
		
			HeapElement element = heapArray[index1];
			heapArray[index1] = heapArray[index2];
			heapArray[index2] = element;	
	}
	/**
	 * Method up
	 * If heap is changed this method rebuild it.
	 * @param arrayIndex where is heap changed.
	 */
	private void nahoru(int arrayIndex){
		int index = arrayIndex;
		if(heapType == TypeOfHeap.MAX){
			
			while(index > TOPINDEX && heapArray[index].getValue() > heapArray[index/2].getValue()){
				change(index,index/2);
				index = index/2;
			}
			
		}else{
			
			while(index > TOPINDEX && heapArray[index].getValue() < heapArray[index/2].getValue()){
				change(index,index/2);
				index = index/2;
			}
		}
	}
	/**
	 * Method down
	 * If heap is changed this method rebuild it. 
	 * @param arrayIndex arrayIndex where is heap changed.
	 */
	private void down(int arrayIndex){
		int index = arrayIndex;
		if(heapType == TypeOfHeap.MAX){
			downHeapMAX(index);
		}else{
			downHeapMIN(index);
		}
	}
	/**
	 * Method downHeapMAX
	 * If heap is changed and MAX this method rebuild it.
	 * @param arrayIndex where is heap changed.
	 */
	private void downHeapMAX(int arrayIndex){
		int index = arrayIndex;
		while(index*2 <= elementCount){
			int indexOfChild = 2*index;
			
			if(indexOfChild < elementCount && heapArray[indexOfChild].getValue() < heapArray[indexOfChild+1].getValue()){
				indexOfChild++;
			}
			
			if(heapArray[indexOfChild].getValue() <= heapArray[index].getValue()){
				break;
			}
			change(index,indexOfChild);
			index = indexOfChild;
		}
	}
	/**
	 * Method downHeapMIN
	 * If heap is changed and MIN this method rebuild it.
	 * @param arrayIndex where is heap changed.
	 */
	private void downHeapMIN(int arrayIndex){
		int index = arrayIndex;
		while(index*2 <= elementCount){
			int indexOfChild = 2*index;
			
			if(indexOfChild < elementCount && heapArray[indexOfChild].getValue() > heapArray[indexOfChild+1].getValue()){
				indexOfChild++;
			}
			
			if(heapArray[indexOfChild].getValue() >= heapArray[index].getValue()){
				break;
			}
			change(index,indexOfChild);
			index = indexOfChild;
		}
	}
	
	public class HeapElement {
		
		/**
		 * Object for heap like planet ship ...
		 */
		private final Object object;
		/**
		 * Value for sorted heap
		 */
		private double value;
		

		/**
		 * Constructor
		 * creating new heap element
		 * @param object object like planet ship.
		 * @param value Value for sorting in heap
		 */
		public HeapElement(Object object, double value) {
			this.object = object;
			this.value = value;
		}
		
		/**
		 * Method getObject
		 * Getter for object in heap.
		 * @return object Object in heap.
		 */
		public Object getObject() {
			return object;
		}

		/**
		 * Method getValue
		 * Getter for value of object.
		 * @return value Value of object in heap.
		 */
		public double getValue() {
			return value;
		}

		/**
		 * Method setValue
		 * Setter for Value of object in heap.
		 * @param value the value to set
		 */
		public void setValue(double value) {
			this.value = value;
		}
	}
	
	/**
	 * Enum class for type of heap
	 * @author Jiří Lukáš
	 *
	 */
	public enum TypeOfHeap {
		
		MAX,MIN

	}
}


