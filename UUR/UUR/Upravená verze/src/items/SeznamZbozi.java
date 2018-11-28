package items;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

/**
 * Třída obsahující základný součásti ostatních dokladů.
 * 
 * @author Martin Hamet
 *
 */
public class SeznamZbozi {
	/**
	 * Kód dokladu
	 */
	public int code;
	
	/**
	 * Datum vytvoření dokladu
	 */
	public LocalDate creationDate;
	
	/**
	 * Seznam zboží
	 */
	public List<Zbozi> items;
	
	/**
	 * Doplňující informace
	 */
	public String info;
	
	/**
	 * Vytvoří nový seznam zboží se zadaným zbožím
	 * 
	 * @param items	požadovanézboží
	 */
	public SeznamZbozi(List<Zbozi> items){
		this.items = items;
		this.creationDate = LocalDate.now();
		this.info = "";
	}
	
	/**
	 * Vytvoří novou instanci seznamu zboží pouze s datumem vytvoření
	 */
	public SeznamZbozi(){
		this.items = null;
		this.creationDate = LocalDate.now();
		this.info = "";
	}
	
	/**
	 * Vrátí doplňující informace
	 * 
	 * @return	doplňující informace
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Nastaví doplňující informace
	 * 
	 * @param info	doplňující informace
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * Vrátí kód dokladu
	 * 
	 * @return	kód dokladu
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Nastaví kód dokladu
	 * 
	 * @param code	kód dokladu
	 */
	public void setCode(int code) {
		this.code = code;
	}
	
	/**
	 * Vrátí datum vytvoření 
	 * 
	 * @return	datum vytvoření
	 */
	public LocalDate getCreationDate() {
		return creationDate;
	}

	/**
	 * Nastaví datum vytvoření
	 * 
	 * @param date	datum vytvoření
	 */
	public void setCreationDate(LocalDate date) {
		this.creationDate = date;
	}

	/**
	 * Vrátí seznam zboží dokladu
	 * 
	 * @return	seznam zboží dokladu
	 */
	public List<Zbozi> getItems() {
		return items;
	}

	/**
	 * Nastaví seznam zboží
	 * 
	 * @param items	seznam zboží
	 */
	public void setItems(List<Zbozi> items) {
		this.items = items;
	}
	
	/**
	 * Zjistí hodnotu dokladu dle ceny jednotlivých položek zboží.
	 * 
	 * @return	celková cena příjemky
	 */
	public float getValue() {
		float value = 0; 
		for (Iterator<Zbozi> iterator = items.iterator(); iterator.hasNext();) {
			 Zbozi pom = iterator.next();
			 value += pom.getPrice();
		    }
		return value;
	}
}
