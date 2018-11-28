package generator;

import java.util.Random;

import function.TimeHandler;

/**
 * This class provides acces to randomly generated data under normal Gauss distribution of 
 * probability. Default values and methods are prepared for specific generation of population
 * in range between 100k - 10M people, but can be set for different scenario using 
 * non-simplified constructor.
 * 	Class also provides generation of name for planet(population). Those names are generated
 * for "line". Names on the same line are diferent by 3 last digit of the name. Lines are
 * labeled by 2 characters before numbers.
 * 
 * @author Martin Hamet
 * 7.10.2015
 *
 */
public class GaussDistribution {
	/**
	 * Default derivation of Gauss curve.
	 */
	private static final int DEFAULT_DERIVATION = 2100000;
	
	/**
	 * Default lowest limit for samples.
	 */
	private static final int DEFAULT_BOUNDS_LOW = 100000;
	
	/**
	 * Default highest limit for samples.
	 */
	private static final int DEFAULT_BOUNDS_HIGH = 10000000;
	
	/**
	 * Value which is on peak of Gauss curve.
	 */
	private final double CENTER_POINT;
	
	/**
	 * Derivation of Gauss curve.
	 */
	private final double DERIVATION;
	
	/**
	 * Lowest limit for samples.
	 */
	private final int BOUNDS_LOW;
	
	/**
	 * Highest limit for samples.
	 */
	private final int BOUNDS_HIGH;
	
	/**
	 * Field with result samples.
	 */
	private int [] result;
	
	/**
	 * Pointer for returning just one sample at the time.
	 */
	private int pointer;
	
	/**
	 * Counts how many planets were created for one line.
	 */
	private int planetCount;
	
	/**
	 * Fist and second letters in planet name.
	 */
	private char fChar,sChar;
	
	/**
	 * Generates name of planet by it's queue number on one line and
	 * line code (fChar + sChar).
	 * @return	generated planet name
	 */
	public String getPlanetName(){
		planetCount++;
		String sNum = String.format("%03d", planetCount);
		return "P-"+fChar+""+sChar+"-"+sNum;
	}
	
	/**
	 * Shifts line code accordingly to another value.
	 */
	public void nextPlanetLine(){
		planetCount = 0;
		sChar++;
		if(sChar=='Z'){
			sChar = 'A';
			fChar++;
		}
	}
	
	/**
	 * Returns field with result samples.
	 * 
	 * @return	result samples
	 */
	public int[] getResult(){
		return result;
	}
	
	/**
	 * Return one sample from results.
	 * 
	 * @return	result sample
	 */
	public int getNextPopulation(){
		if(pointer>result.length-1){
			System.out.println("<GaussDistribution>"+ "\n"+ 
					"ERROR - Not enough samples stored! \n"+
					"			Returned value (-1)");
			return -1;
		}
		return result[pointer++];
	}
	
	/**
	 * Generates results for result field.
	 */
	private void run(){
		Random r = TimeHandler.getRandom();
		//System.out.println(r.nextInt());
		int number;
		int j = 0;
		while(j<result.length){
			number = (int)Math.round(
					r.nextGaussian()*DERIVATION + CENTER_POINT
					);
			if(number>BOUNDS_HIGH){
				number = BOUNDS_HIGH;
			}
			if(number>=BOUNDS_LOW){
				result[j] = number;
				j++;
			}
			
		}
		if(j!=result.length){
			System.out.println("ERROR - Not enough saples for result!");
			System.exit(0);
		}
		
	}
	
	/**
	 * Instantiates GaussDistribution class by given parameters.
	 * 
	 * @param samples		number of required samples
	 * @param centerPoint	value with highest probability
	 * @param derivation	derivation of Gauss curve
	 * @param boundsLow		lowest value limit for samples
	 * @param boundsHigh	highest value limit for samples
	 */
	public GaussDistribution(	int samples, 
								double centerPoint, 
								double derivation,
								int boundsLow,
								int boundsHigh
							) {
		CENTER_POINT = centerPoint;
		DERIVATION = derivation;
		BOUNDS_LOW = boundsLow;
		BOUNDS_HIGH = boundsHigh;
		result = new int[samples];
		pointer = 0;
		planetCount = 0;
		fChar = 'A';
		sChar = 'A';
		
		run();
	}
	
	/**
	 * Simplified instantiation of GaussDistribution which uses default values and
	 * given parameters.
	 * This constructor is set for one specific case for aproximately 
	 * 5000 samples,
	 * highest probability value 3M,
	 * derivation 2M,
	 * with range between 100k - 10M
	 * 
	 * @param samples		number of required samples
	 * @param centerPoint	value with highest probability
	 */
	public GaussDistribution(int samples, double centerPoint){
		this(	samples,
				centerPoint,
				DEFAULT_DERIVATION,
				DEFAULT_BOUNDS_LOW,
				DEFAULT_BOUNDS_HIGH
				);
	}
	
	/**
	 * Get average of sample values in result field.
	 * 
	 * @return	average value of samples
	 */
	private double getAverage(){
		double sum = 0;
		for(int i=0;i<result.length;i++){
			sum += result[i];
		}
		return (sum/result.length);
	}
	
	/**
	 * Generates one index of 10 for given value. Buckets are meant for 1M steps
	 * from 0M to 10M.
	 * 
	 * @param value		value which is index of bucket searched for
	 * @return			index of bucket
	 */
	private int bucketRange(int value){
		int val = value;
		int bucketSize = 1000000;
		int i = -1;
		while(val>0){
			val -= bucketSize;
			i++;
		}
		return i;
	}
	
	
	/**
	 * Generates String with detailed description of generated result field.
	 * @return	String with detailed description of result
	 */
	public String getReport(){
		int [] graph = new int[10];
		int bucket;
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		
		for(int i=0;i<result.length;i++){
			if(result[i]<min){
				min = result[i];
			}
			if(result[i]>max){
				max = result[i];
			}
			bucket = bucketRange(result[i]);
			graph[bucket] = ++graph[bucket];
		}
		return //Arrays.toString(result)+"\n"+
				"Population range : Planets in range "+"#"+"\n"+
			     "  0M-1M : "+ graph[0]+"#"+"\n"+
			     "  1M-2M : "+graph[1]+"#"+"\n"+
			     "  2M-3M : "+graph[2]+"#"+"\n"+
			     "  3M-4M : "+graph[3]+"#"+"\n"+
			     "  4M-5M : "+graph[4]+"#"+"\n"+
			     "  5M-6M : "+graph[5]+"#"+"\n"+
			     "  6M-7M : "+graph[6]+"#"+"\n"+
			     "  7M-8M : "+graph[7]+"#"+"\n"+
			     "  8M-9M : "+graph[8]+"#"+"\n"+
			     "  9M-10M : "+graph[9]+"#"+"\n"+
			      "\n"+
			     "Average population : "+getAverage()+"#"+"\n"+
			     "Minimal population : "+min+"#"+"\n"+
			     "Maximum population : "+max+"#";
	}
	
}
