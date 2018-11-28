package storage;

/**
 * Výčtová třída určující stav dokladu.
 * @author Martin Hamet
 *
 */
public enum Closed{
	CLOSED, OPEN;
	
	/**
	 * Vrátí string popisující stav dokladu
	 * @param value	stav dokladu
	 * @return	popis stavu dokladu
	 */
	public String toString(Closed value){
		switch(value){
			case CLOSED:	return "Uzavřená";
			case OPEN:		return "Otevřená";
			default:		return "Chyba ve výčtovém typu Closed";
		}
	}
}