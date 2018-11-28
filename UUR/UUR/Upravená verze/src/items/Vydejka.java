package items;

import java.util.List;

/**
 * Třída představuje položku reprezentující výdejku a její parametry.
 * 
 * @author Martin Hamet
 *
 */
public class Vydejka extends SeznamZbozi {
	/**
	 * Zakázka pro kterou je daná výdejka vytvořená
	 */
	private Zakazka project;
	
	/**
	 * Hodnota výdejky
	 */
	private float value;
	
	/**
	 * Další výdaje spojené s materiálem
	 */
	private float expencses;
	
	/**
	 * Vrátí stav výdejky
	 * 
	 * @return	stav výdejky
	 */
	public storage.Closed getClosed() {
		return project.getClosed();
	}
	
	/**
	 * Vrátí hodnotu výdejky
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Nastaví hodnotu výdejky
	 * 
	 * @param value	hodnota výdejky
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * Vrátí další výdaje spojené s výdejkou
	 * 
	 * @return	další výdaje spojené s výdejkou
	 */
	public float getExpencses() {
		return expencses;
	}
	
	/**
	 * Nastaví další výdaje spojené s výdejkou
	 * 
	 * @param expencses	další výdaje spojené s výdejkou
	 */
	public void setExpencses(float expencses) {
		this.expencses = expencses;
	}

	/**
	 * Vrátí zakázku spojenou s aktuální výdejkou
	 * 
	 * @return	zakázka spojená s aktuální výdejkou
	 */
	public Zakazka getProject() {
		return project;
	}
	
	/**
	 * Vrátí jméno zakázky
	 * 
	 * @return	jméno zakázky
	 */
	public String getProjectName(){
		return project.getName();
	}
	
	/**
	 * Nastaví zakázku pro aktuální výdejku
	 * 
	 * @param project	nastavovaná zakázka
	 */
	public void setProject(Zakazka project) {
		//System.out.println("setUpaPROJECT");
		if(this.project!=null&&this.project.getMaterialUsed()!=null){
			if(this.project.getMaterialUsed().containsKey(this.getCode())){
				this.project.removeVydejka(this);
				//System.out.println("removing link from "+this.code);
			}
		}
		
		this.project = project;
		//System.out.println("SETUP P: "+this.project);
		linkThisToProject();
	}
	
	/**
	 * Vytvoří novou innstanci výdejky podle zadaných parametrů
	 * 
	 * @param project	zakázka spojená s výdejkou
	 * @param items		seznam zboží ve výdejce
	 */
	public Vydejka(	Zakazka project,
			List<Zbozi> items){
		super(items);
		this.project = project;
		//linkThisToProject();
		this.expencses = 0;
		this.value = 0;
	}
	
	/**
	 * Vytvoří novou instanci výdejky podle zadaných parametrů
	 * 
	 * @param items	seznam zboží ve výdejce
	 */
	public Vydejka(List<Zbozi> items){
		super(items);
		this.expencses = 0;
		this.value = 0;
	}
	
	/**
	 * Připojí aktuální výdejku k její zakázce
	 */
	private void linkThisToProject(){
		this.project.addVydejka(this);
	}
	
	/**
	 * Vrátí String stručně popisující výdejku
	 */
	public String toString(){
		return 	"\n"+
				"Kód:  		"+code+"\n"+
				"Hodnota:	"+value+"\n"+
				" 		Zakázka: "+"\n"+project.toString()+"\n";
	}
}
