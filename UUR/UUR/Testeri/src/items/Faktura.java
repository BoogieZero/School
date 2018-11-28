package items;

import java.time.LocalDate;

/**
 * Třída představuje položku reprezentující fakturu a její parametry.
 * 
 * @author Martin Hamet
 *
 */
public class Faktura extends SeznamZbozi {
	/**
	 * Zakázka spojená s fakturou
	 */
	private Zakazka project;
	
	/**
	 * Datum splatnosti faktury
	 */
	private LocalDate dueDate;
	
	/**
	 * Cena faktury
	 */
	private float price;
	
	/**
	 * Popis položky ve faktuře
	 */
	private String Description;
	
	/**
	 * Způsob platby
	 */
	private PaymentType payment;
	
	/**
	 * Číslo účtu
	 */
	private String accNumber;
	
	
	/**
	 * Jméno zákazníka
	 */
	private String cName;
	
	/**
	 * Příjmení zákazníka
	 */
	private String cSurname;
	
	/**
	 * Adresa zákazníka
	 */
	private String cAddress;
	
	/**
	 * PSČ zákazníka
	 */
	private String cPsc;
	
	/**
	 * IČO zákazníka
	 */
	private String cICO;
	
	/**
	 * DIČ zákazníka
	 */
	private String cDIC;
	
	/**
	 * Stav faktury
	 */
	private boolean closed;
	
	
	
	/**
	 * Vrátí číslo účtu
	 * 
	 * @return	číslo účtu
	 */
	public String getAccNumber() {
		return accNumber;
	}
	
	/**
	 * Nastaví číslo účtu
	 * 
	 * @param accNumber	číslo účtu
	 */
	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	/**
	 * Vrátí způsob platby
	 * 
	 * @return	způsob platby
	 */
	public PaymentType getPayment() {
		return payment;
	}

	/**
	 * Nastaví způsob platby
	 * 
	 * @param payment	způsob platby
	 */
	public void setPayment(PaymentType payment) {
		this.payment = payment;
	}

	/**
	 * Vrátí popis předmětu faktury
	 * 
	 * @return	popis předmětu faktury
	 */
	public String getDescription() {
		return Description;
	}
	
	/**
	 * Nastaví popis předmětu faktury
	 * 
	 * @param description	popis předmětu faktury
	 */
	public void setDescription(String description) {
		Description = description;
	}

	/**
	 * Vrátí hodnotu true pokud je faktura uzavřená
	 * 
	 * @return	stav faktury
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * Nastaví stav faktury
	 * 
	 * @param closed	stav faktury
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	/**
	 * Vrátí datum splatnosti
	 * 
	 * @return	datum splatnosti
	 */
	public LocalDate getDueDate() {
		return dueDate;
	}

	/**
	 * Nastaví datum splatnosti
	 * 
	 * @param dueDate	datum splatnosti
	 */
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * Vrátí jméno
	 * 
	 * @return	jméno
	 */
	public String getcName() {
		return cName;
	}
	
	/**
	 * Nastaví jméno
	 * 
	 * @param cName	jméno
	 */
	public void setcName(String cName) {
		this.cName = cName;
	}

	/**
	 * Vrátí příjmení
	 * 
	 * @return	příjmení
	 */
	public String getcSurname() {
		return cSurname;
	}

	/**
	 * Nastaví příjmení
	 * 
	 * @param cSurname	příjmení
	 */
	public void setcSurname(String cSurname) {
		this.cSurname = cSurname;
	}

	/**
	 * Vrátí adresu
	 * 
	 * @return	adresa
	 */
	public String getcAddress() {
		return cAddress;
	}

	/**
	 * Nastaví adresu
	 * 
	 * @param cAddress	adresa
	 */
	public void setcAddress(String cAddress) {
		this.cAddress = cAddress;
	}

	/**
	 * Vrátí PSČ
	 * 
	 * @return	PSČ
	 */
	public String getcPsc() {
		return cPsc;
	}

	/**
	 * Nastaví PSČ
	 * 
	 * @param cPsc	PSČ
	 */
	public void setcPsc(String cPsc) {
		this.cPsc = cPsc;
	}

	/**
	 * Vrátí IČO
	 * @return IČO
	 */
	public String getcICO() {
		return cICO;
	}

	/**
	 * Nastaví IČO
	 * @param cICO	IČO
	 */
	public void setcICO(String cICO) {
		this.cICO = cICO;
	}

	/**
	 * Vrátí DIČ
	 * @return	DIČ
	 */
	public String getcDIC() {
		return cDIC;
	}

	/**
	 * Nastaví DIČ
	 * 
	 * @param cDIC DIČ
	 */
	public void setcDIC(String cDIC) {
		this.cDIC = cDIC;
	}

	/**
	 * Vrátí zakázku spojenou s fakturou
	 * 
	 * @return	zakázka spojená s fakturou
	 */
	public Zakazka getProject() {
		return project;
	}

	/**
	 * Nastaví zakázku pro fakturu
	 * 
	 * @param project	zakázka pro fakturu
	 */
	public void setProject(Zakazka project) {
		this.project = project;
	}
	
	/**
	 * Vrátí zákazníka
	 * 
	 * @return	zákazník
	 */
	public String getCustomer(){
		return (this.project.getCustomer());
	}
	
	/**
	 * Vrátí cenu
	 * 
	 * @return	cena
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * Nastaví cenu
	 * 
	 * @param price	cena
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * Vytvoří novou instanci faktury podle zadaných parametrů.
	 * 
	 * @param project	zakázka
	 * @param price		cena
	 */
	public Faktura(	Zakazka project,
					float price){
		super();
		this.project = project;
		this.price = price;
		this.closed = false;
	}
	
	/**
	 * Vrátí String stručně popisující fakturu
	 */
	public String toString(){
		return 	"\n"+
				"Kód:  		"+code+"\n"+
				"Zakázka: 	"+project.getName()+"\n"+
				"Hodnota:	"+price+"\n";
	}
	
	/**
	 * Výčtová třída představující způsob platby
	 * 
	 * @author Martin Hamet
	 *
	 */
	public enum PaymentType{
		CASH, ACCOUNT;
	}
}
