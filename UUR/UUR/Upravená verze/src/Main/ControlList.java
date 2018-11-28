package Main;

/**
 * Třída představující položky výběrového stromu
 * 
 * @author Martin Hamet
 *
 */
public class ControlList {
	
	/**
	 * Název
	 */
	private String name;
	
	/**
	 * Typ
	 */
	private ControlListType type;
	
	/**
	 * Vrátí název
	 * 
	 * @return	název
	 */
	public String getName() {
		return name;
	}

	/**
	 * Vrátí typ
	 * @return	typ
	 */
	public ControlListType getType() {
		return type;
	}

	/**
	 * Vytvoří novou instanci položky stromu podle zadaných parametrů
	 * 
	 * @param name	název
	 * @param type	typ
	 */
	public ControlList(String name, ControlListType type){
		this.name = name;
		this.type = type;
	}
	
	/**
	 * Vytvoří položku stromu pouze s názvem
	 * 
	 * @param name	náezv
	 */
	public ControlList(String name){
		this.name = name;
		this.type = ControlListType.NONE;
	}
	
	/**
	 * Vrátí String se jménem položky
	 */
	public String toString(){
		return name;
	}
	
	/**
	 * Výčtová třída představující typ položky ve stromovém seznamu
	 * 
	 * @author Martin Hamet
	 *
	 */
	public static enum ControlListType {
		NONE, ZBOZI, FAKTURY, VYDEJKY, PRIJEMKY, ZAKAZKY;
	}
	
	
	
}
