package items;

/**
 * Třída představuje položku reprezentující zboží a jeho parametry.
 * 
 * @author Martin Hamet
 *
 */
public class Zbozi {
	
	/**
	 * Identifikační kód zboží v programu
	 */
	private int code;
	
	/**
	 * Název
	 */
	private String name;
	
	/**
	 * Množství
	 */
	private int quantity;
	
	/**
	 * Jednotky spojené s množstvím a cenou zboží
	 */
	private Units unit;
	
	/**
	 * Cena za uvedenou jednotku zboží
	 */
	private float price;
	
	/**
	 * Výrobce
	 */
	private String manufacturer;
	
	/**
	 * Doplňující informace
	 */
	private String info;
	
	/*
	private List<Prijemka> prijemkyUsed;
	public void addPrijemkaUsed(Prijemka p){
		prijemkyUsed.add(p);
	}
	
	public void removePrijemkaUsed(Prijemka p){
		prijemkyUsed.remove(p);
	}
	
	public ObservableList<Prijemka> getPrijemkyUsed() {
		ObservableList<Prijemka> ol = 
				FXCollections.observableArrayList(prijemkyUsed);
		return ol;
	}
	*/

	/**
	 * Vrátí kód zboží
	 * 
	 * @return	kód zboží
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Nastaví kó zboží
	 * 
	 * @param code	kód zboží
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Vrátí název zboží
	 * 
	 * @return	název zboží
	 */
	public String getName() {
		return name;
	}

	/**
	 * Nastaví název zboží
	 * 
	 * @param name	název zboží
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Vrátí množství zboží
	 * 
	 * @return	množství zboží
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Nastaví množství zboží
	 * 
	 * @param quantity	množství zboží
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * Přidá zadané množství k aktuálnímu množství zboží
	 * 
	 * @param addedQuantity	přidávané množství
	 */
	public void addQuantity(int addedQuantity){
		this.quantity += addedQuantity;
	}
	
	/**
	 * Vrátí jednotky daného zboží
	 * 
	 * @return	jednotky zboží
	 */
	public Units getUnit() {
		return unit;
	}

	/**
	 * Nastaví jednotky zboží
	 * 
	 * @param unit	požadované jednotky
	 */
	public void setUnit(Units unit) {
		this.unit = unit;
	}

	/**
	 * Vrátí cenu zboží
	 * 
	 * @return	cena zboží
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * nastaví cenu zboží
	 * 
	 * @param price	cena zboží
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * Vrátí výrobce zboží
	 * 
	 * @return	výrobce
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * Nastaví výrobce zboží
	 * 
	 * @param producer	výrobce
	 */
	public void setManufacturer(String producer) {
		this.manufacturer = producer;
	}

	/**
	 * Vrátí doplňující informace ke zboží
	 * 
	 * @return	doplňující informace
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Nastaví doplňující informace ke zboží
	 * 
	 * @param info	doplňující informace
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	
	/**
	 * Nastaví parametry aktuálního zboží podle zadaného zboží.
	 * 
	 * @param zbozi	zadané zboží
	 */
	public void setAll(Zbozi zbozi){
		setName(zbozi.getName());
		setQuantity(zbozi.getQuantity());
		setUnit(zbozi.getUnit());
		setPrice(zbozi.getPrice());
		setManufacturer(zbozi.getManufacturer());
		setInfo(zbozi.getInfo());
	}
	
	/**
	 * Vrátí String stručně popisující aktuální zboží.
	 */
	public String toString(){
		return 	"\n"+
				"Kód:  			"+code+"\n"+
				"Jméno: 		"+name+"\n"+
				"Množství:		"+quantity+" "+unit.toString();
	}
	
	/**
	 * Vytvoří novou instanci zboží podle zadaných parametrů.
	 * 
	 * @param name	název zboží
	 * @param unit	jednotky zboží
	 * @param price	cena zboží
	 * @param manufacturer	výrobce zboží
	 * @param info	doplňující informace
	 */
	public Zbozi(	String name, 
					Units unit,
					float price, 
					String manufacturer,
					String info){
		
		this.name = name;
		this.unit = unit;
		this.quantity = 0;
		this.price = price;
		this.manufacturer = manufacturer;
		this.info = info;
		//this.prijemkyUsed = new ArrayList<Prijemka>();
	}
	
	/**
	 * Vytvoří novou instanci zboží s parametry požadovaného zboží.
	 * 
	 * @param zbozi	požadované zboží
	 */
	public Zbozi(Zbozi zbozi){
		this.code = zbozi.getCode();
		this.name = zbozi.getName();
		this.unit = zbozi.getUnit();
		this.quantity = 0;
		this.price = 0;
		this.manufacturer = zbozi.getManufacturer();
		this.info = zbozi.getInfo();
	}
	
	/**
	 * Výčtová třída představující měrné jednotky zboží.
	 * 
	 * @author Martin Hamet
	 *
	 */
	public enum Units {
		ks, bal, m;
	}
}
