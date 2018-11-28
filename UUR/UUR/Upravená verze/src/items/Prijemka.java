package items;

import java.util.List;

/**
 * Třída představuje položku reprezentující příjemku a její parametry.
 * @author Martin Hamet
 *
 */
public class Prijemka extends SeznamZbozi {
	/**
	 * Dodavatel
	 */
	private String supplier;
	
	/**
	 * Počet položek
	 */
	private int itemsCount;
	
	/**
	 * Hodnota příjemky
	 */
	private float value;
	
	/**
	 * Další výdaje spojené s příjemkou
	 */
	private float expencses;
	
	/**
	 * Vrátí další výdaje spojené s příjemkou
	 * 
	 * @return	další výdaje spojené s příjemkou
	 */
	public float getExpencses() {
		return expencses;
	}

	/**
	 * Nastaví další výdaje spojené s příjemkou
	 * 
	 * @param expencses	další výdaje spojené s příjemkou
	 */
	public void setExpencses(float expencses) {
		this.expencses = expencses;
	}

	/**
	 * Vrátí dodavatele
	 * 
	 * @return	dodavatel
	 */
	public String getSupplier() {
		return supplier;
	}

	/**
	 * Nastaví dodavatele
	 * 
	 * @param supplier	dodavatel
	 */
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	/**
	 * Vrát počet položek v seznamu zboží
	 * 
	 * @return	počet položek v seznamu zboží
	 */
	public int getItemsCount() {
		return itemsCount;
	}

	/**
	 * Nastaví počet položek v seznamu zboží
	 * 
	 * @param itemsCount	počet položek v seznamu zboží
	 */
	public void setItemsCount(int itemsCount) {
		this.itemsCount = itemsCount;
	}

	/**
	 * Vrátí hodnotu příjemky
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Nastaví hodnotu příjemky
	 * 
	 * @param value	hodnota příjemky
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * Nastaví parametry příjemky podle zadané příjemky
	 * 
	 * @param p	zadaná příjemka
	 */
	public void setAll(Prijemka p){
		this.items = p.getItems();
		this.supplier = p.supplier;
		this.value = p.value;
		this.itemsCount = p.getItemsCount();
		this.expencses = p.getExpencses();
	}
	
	/**
	 * Vytvoří novou instanci příjemky podle zadaných parametrů
	 * 
	 * @param supplier	dodavatel
	 * @param items		seznam zboží příjemky
	 */
	public Prijemka(String supplier,
					List<Zbozi> items){
		
			super(items);
			this.supplier = supplier;		
			this.itemsCount = items.size();
			this.expencses = 0;
	}
			
	/**
	 * Vytvoří novou instanci příjemky podle zadaných parametrů
	 * 
	 * @param supplier	dodavatel
	 * @param value		hodnota
	 * @param items		seznam zboží
	 */
	public Prijemka(String supplier,
					float value,
					List<Zbozi> items){
		
			super(items);
			this.supplier = supplier;		
			this.itemsCount = items.size();
			this.value = value;
			this.expencses = 0;
	}
	
	/**
	 * Vytvoří novou instanci příjemky podle zadaných parametrů
	 * 
	 * @param items	seznam zboží
	 */
	public Prijemka(List<Zbozi> items){
		super(items);
		this.expencses = 0;
	}
	
	/**
	 * Vrátí strin stručně popisující příjemku
	 */
	public String toString(){
		return 	"\n"+
				"code:  "+code+"\n"+
				"suplier: "+supplier+"\n"+
				"date:"		+creationDate;
	}

}
