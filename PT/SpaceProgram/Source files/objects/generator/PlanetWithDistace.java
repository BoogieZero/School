package objects.generator;

import objects.Planet;

/**
 * Class PlanetWithDistance
 * This class is use for creating galaxy. If I need generate planets on one rode and I have ordered minimal
 * distance between planet and center then I create this class for all object on this rode.
 * 
 * @author Jiří Lukáš
 *
 */
public class PlanetWithDistace implements Comparable<PlanetWithDistace>  {
	
	/**
	 * Distance between planet and center
	 */
	private double distance;
	
	/**
	 * Planet
	 */
	private Planet planet;
	
	/**
	 * Constructor
	 * @param planet Object planet
	 * @param distance distance between planet and center
	 */
	public PlanetWithDistace(Planet planet, double distance) {
		this.planet = planet;
		this.distance = distance;
	}
	
	
	/**
	 * Method getDistance
	 * @return distance Distance between planet and center
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Method setDistance
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Method getPlanet
	 * @return planet Return planet 
	 */
	public Planet getPlanet() {
		return planet;
	}


	/**
	 * Method setPlanet
	 * @param planet the planet to set
	 */
	public void setPlanet(Planet planet) {
		this.planet = planet;
	}

	@Override
	public int compareTo(PlanetWithDistace o) {
		double result = this.getDistance() -  o.getDistance();
		if(result < 0){
			return -1;
		}
		if(result > 0){
			return 1;
		}
		return 0;
	}
}
