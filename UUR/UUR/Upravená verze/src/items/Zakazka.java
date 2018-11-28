package items;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import storage.Closed;

/**
 * Třída představuje položku reprezentující zakázku a její parametry.
 * @author Martin Hamet
 *
 */
public class Zakazka extends SeznamZbozi {
	/**
	 * Název
	 */
	private String name;
	
	/**
	 * Zákazník
	 */
	private String customer;
	
	/**
	 * Mezní termín vyhotovení
	 */
	private LocalDate deadline;
	
	/**
	 * Navržená cena
	 */
	private float priceTag;
	
	/**
	 * Cena za materiál
	 */
	private float materialPrice;
	
	/**
	 * Cena za práci
	 */
	private float workPrice;
	
	/**
	 * Zisk ze zakázky
	 */
	private float gainPrice;
	
	/**
	 * Hodnota zakázky (Veškeré náklady na vyhotovení zakázky)
	 */
	private float value;
	
	/**
	 * Určuje zda zakázka ještě probíhá nebo zda je již uzavřená (Hotová a zaplacená)
	 */
	private Closed closed;
	
	/**
	 * Odkaz na fakturu spojenou s touto zakázkou
	 */
	private Faktura bill;
	
	/**
	 * Doplňující informace
	 */
	private String info;
	
	/**
	 * Seznam výdejek vytvořených na tuto zakázku
	 */
	private Map<Integer,Vydejka> materialUsed;
	
	
	
	/**
	 * Vrátí zisk ze zakázky
	 * 
	 * @return	zisk na zakázce
	 */
	public float getGainPrice() {
		return gainPrice;
	}
	
	/**
	 * Nastaví zisk ze zakázky
	 * 
	 * @param gainPrice	zisk na zakázce
	 */
	public void setGainPrice(float gainPrice) {
		this.gainPrice = gainPrice;
	}
	
	/**
	 * Vrátí doplňující informace
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
	 * Vrátí hodnotu zakázky
	 */
	public float getValue() {
		recountPrices();
		return value;
	}

	/**
	 * Nastaví hodnotu zakázky
	 * 
	 * @param value	hodnota zakázky
	 */
	public void setValue(float value) {
		this.value = value;
	}
	
	/**
	 * Nastaví stav zakázky
	 * 
	 * @param closed	stav zakázky	
	 */
	public void setClosed(Closed closed) {
		this.closed = closed;
	}
	
	/**
	 * Vrátí název
	 * 
	 * @return	název
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Nastaví název
	 * 
	 * @param name	název
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Vrátí zákazníka
	 * 
	 * @return	zákazník
	 */
	public String getCustomer() {
		return customer;
	}
	
	/**
	 * Vrátí zákazníka
	 * 
	 * @param customer	zakázník
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	/**
	 * Vrátí mezní termín zakázky
	 * 
	 * @return	mezní termín zakázky
	 */
	public LocalDate getDeadline() {
		return deadline;
	}

	/**
	 * Nastaví mezní termín zakázky
	 * 
	 * @param deadline	mezní termín zakázky
	 */
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	
	/**
	 * Vrátí navrhnutou cenu zakázky
	 * 
	 * @return	navržená cena zakázky
	 */
	public float getPriceTag() {
		return priceTag;
	}
	
	/**
	 * Nastaví navrnutou cenu zakázky
	 * 
	 * @param priceTag	navržená cena zakázky
	 */
	public void setPriceTag(float priceTag) {
		this.priceTag = priceTag;
	}
	
	/**
	 * Vrátí cenu materiálu na zakázku
	 * 
	 * @return	cena materiálu použitého na zakázku
	 */
	public float getMaterialPrice() {
		recountPrices();
		return materialPrice;
	}
	
	/**
	 * Přepočítá hodnoty zakázky
	 */
	public void recountPrices(){
		materialPrice = 0;
		List<items.Vydejka> list = getMaterialUsedList();
		for(items.Vydejka sVy : list){
			materialPrice += sVy.getValue() + sVy.getExpencses();
		}
		this.value = materialPrice + workPrice;
		this.gainPrice = priceTag - value;
	}
	
	/**
	 * Nastaví cenu materiálu
	 * 
	 * @param materialPrice	cena materiálu
	 */
	public void setMaterialPrice(float materialPrice) {
		this.materialPrice = materialPrice;
	}

	/**
	 * Vrátí cenu za práci na zakázce
	 * 
	 * @return	cena za práci
	 */
	public float getWorkPrice() {
		recountPrices();
		return workPrice;
	}
	
	/**
	 * Nastaví cenu za práci na zakázce
	 * 
	 * @param workPrice	cena za práci
	 */
	public void setWorkPrice(float workPrice) {
		this.workPrice = workPrice;
	}

	/**
	 * Vrátí stav zakázky
	 * 
	 * @return	stav zakázky
	 */
	public Closed getClosed() {
		if(bill==null){
			closed = Closed.OPEN;
		}else{
			closed = Closed.CLOSED;
		}
		return closed;
	}
	
	/**
	 * Vrátí fakturu spojenou se zakázkou. Pokud zakázka není spojena s žádnou fakturou vrátí null
	 * 
	 * @return	faktura spojená se zakázkou
	 */
	public Faktura getBill() {
		return bill;
	}
	
	/**
	 * Nastaví fakturu spojenou se zakázkou
	 * 
	 * @param bill	připojovaná faktura
	 */
	public void setBill(Faktura bill) {
		this.bill = bill;
	}

	/**
	 * Vrátí mapu výdejek použitého materiálu
	 * 
	 * @return	mapa výdejek použitého materiálu
	 */
	public Map<Integer,Vydejka> getMaterialUsed() {
		return materialUsed;
	}
	
	/**
	 * Nastaví mapu výdejek použitého materiálu
	 * 
	 * @param materialUsed	mapa výdejek použitého materiálu
	 */
	public void setMaterialUsed(Map<Integer,Vydejka> materialUsed) {
		this.materialUsed = materialUsed;
	}
	
	/**
	 * Vrátí ObservableList výdejek použitého materiálu
	 * 
	 * @return	ObservableList výdejek použitého materiálu
	 */
	public ObservableList<Vydejka> getMaterialUsedList(){
		List<Vydejka> list = new ArrayList<Vydejka>(materialUsed.values());
		ObservableList<Vydejka> ol = FXCollections.observableArrayList(list);
		return ol;
	}
	
	/**
	 * Přidá výdejku do seznamu výdejek použitého materiálu
	 * 
	 * @param v	přidávaná výdejka
	 */
	public void addVydejka(Vydejka v){
		materialUsed.put(v.getCode(), v);
	}
	
	/**
	 * Odebere výdejku ze seznamu výdejek použitého materiálu
	 * 
	 * @param v	odebíraná výdejka
	 */
	public void removeVydejka(Vydejka v){
		materialUsed.remove(v.getCode());
	}
	
	/**
	 * Nastaví některé parametry zakázky podle požadované zakázky
	 * 
	 * @param z	požadovaná zakázka
	 */
	public void setAll(Zakazka z){
		this.name = z.getName();
		this.customer = z.getCustomer();
		this.deadline = z.getDeadline();
		this.priceTag = z.getPriceTag();
		this.workPrice = z.getWorkPrice();
	}
	
	/**
	 * Vytvoří novou instanci zakázky podle zadaných parametrů
	 * 
	 * @param name		název
	 * @param customer	zákazník
	 * @param deadline	mezní termín
	 * @param priceTag	navrhovaná cena
	 * @param workPrice	cena za práci
	 */
	public Zakazka(	String name, 
					String customer, 
					LocalDate deadline, 
					float priceTag, 
					float workPrice){
		super();
		this.name = name;
		this.customer = customer;
		this.deadline = deadline;
		this.priceTag = priceTag;		//
		this.workPrice = workPrice;			//
		this.bill = null;
		this.materialUsed = new HashMap<Integer,Vydejka>();
	}
	
	/**
	 * Vytvoří novou instanci zakázky bez parametrů
	 */
	public Zakazka(){
		super();
		this.bill = null;
		this.materialUsed = new HashMap<Integer,Vydejka>();
	}
	
	/*
	public Zakazka(Zakazka z){
		this.code = z.getCode();
		this.creationDate = z.getCreationDate();
		this.customer = z.getCustomer();
		this.deadline = z.getDeadline();
		this.gainPrice = z.getGainPrice();
		this.info = z.getInfo();
		this.items = z.getItems();
		this.materialPrice = z.getMaterialPrice();
		this.name = z.getName();
		this.priceTag = z.getPriceTag();
		this.workPrice = z.getWorkPrice();
		this.bill = z.getBill();
	}
	*/
	
	/**
	 * Vrátí String stručně popisující zakázku
	 */
	public String toString(){
		return 	"\n"+
				" Kód:  			"+code+"\n"+
				" Název: 			"+name+"\n"+
				" Zákazník: 		"+customer+"\n"+
				" Navrhnutá cena: 	"+priceTag+"\n";
	}
}
