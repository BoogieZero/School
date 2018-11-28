package logoscislo;

import java.text.Collator;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Entitní třída jednoho zpracovaného osobního čísla
 * 
 * @author P.Herout
 *
 */

public class OsobniCislo implements Comparable<OsobniCislo> {

	static{
		System.setProperty(	"java.util.logging.config.class",
							"logoscislo.InterniKonfigurace");
	}
	
	public static Logger OsobniCisloLogger = 
			Logger.getLogger(OsobniCislo.class.getName());
	
	
  /**
   * Definice kolátoru pro české řazení řetězců<br/>
   * Bude využit při řazení osobních čísel podle příjmení a jména
   */
  private static final Collator COLLATOR = Collator.getInstance(new Locale("cs", "CZ"));
  
  // části osobního čísla - pokud je formát osobního čísla chybně, mohou obsahovat ZNAK_CHYBY
  
  /** fakulta */
  private String fakulta;

  /** rok nástupu */
  private String rokNastupu;

  /** typ studia */
  private TypStudia typStudia;

  /** forma studia */
  private String formaStudia;

  /** nepovinná část */
  private String nepovinne;

  /** příjmení studenta - velkými písmeny */
  private String prijmeni;

  /** jméno studenta - první písmeno velké, ostatní malá */
  private String jmeno;

  
  /** generovaný výsledek */
  private String vysledek = Konstanty.PRAZDNY;

  /** formát zadání je správný */
  private boolean platnyFormat = true;

  
  /** 
   * Naplní atributy<br/>
   * v případě chybného formátu je nahradí <code>ZNAK_CHYBY</code><br/>
   * Toto osobní číslo je předpřipravené - pořadové číslo je xxxx.<br/>
   * K dokončení mu musí být vygenerováno pořadové číslo.
   * 
   * @param radkaZadani načtená řádka ze souboru - může být v chybném formátu
   */
  public OsobniCislo(String radkaZadani) {
    naplnAtributy(radkaZadani);
    vysledek = fakulta + rokNastupu + typStudia.getZkratka() + "xxxx" + formaStudia + nepovinne;
    OsobniCisloLogger.finer(vysledek);
  }
  
  /**
   * Porovná příjmení proti sobě. V případě rovnosti porovnává jména.<br/>
   * Zajišťuje přirozené řazení podle české normy.
   * 
   * @param oc osobní číslo porovnávaného studenta
   * @return hodnoty přirozeného řazení
   */
  @Override
  public int compareTo(OsobniCislo oc) {
    int vysledek = COLLATOR.compare(this.prijmeni, oc.prijmeni);
    if (vysledek == 0) {
      return COLLATOR.compare(this.jmeno, oc.jmeno);
    }
    else {
      return vysledek;
    }
  }

  /**
   * Textová informace o instanci
   * 
   * @return textovou informaci o instanci
   */
  @Override
  public String toString() {
    String pom = vysledek + " <= " + prijmeni + " " + jmeno;
    if (platnyFormat == false) {
      pom += " (" + Konstanty.TEXT_CHYBNY_FORMAT + ")";
    }
    return pom;
  }
  
  /**
   * Vrací osobní číslo, které ale nemusí být platné
   * 
   * @return osobní číslo
   */
  public String getOsobniCislo() {
    return vysledek;
  }
  
  /**
   * Vrací informaci o tom, zda je osobní číslo platné
   * 
   * @return <code>true</code>, je-li osobní číslo platné, <br/>
   * nebo <code>false</code>, pokud ještě nebylo osobní číslo vygenerováno nebo je některý z formátů neplatný
   */
  public boolean isPlatneOsobniCislo() {
    if (platnyFormat == false ||
        vysledek.equals(Konstanty.PRAZDNY)) {
      return false;
    }
    else {
      return true;
    }
  }
  
  /**
   * Vrací informaci o tom, zda jsou všechny formáty všech částí osobního čísla platné
   * 
   * @return <code>true</code>, jsou-li formáty platné, <br/>
   * nebo <code>false</code>, je-li některý z formátů neplatný
   */
  public boolean isPlatnyFormat() {
    return this.platnyFormat;
  }
  
  /**
   * Vrací typ studia
   * 
   * @return typ studia
   */
  public TypStudia getTypStudia() {
    return this.typStudia;
  }
  
  /**
   * Vrací fakultu
   * 
   * @return fakulta
   */
  public String getFakulta() {
    return fakulta;
  }
  
  /**
   * Složení výsledku z jednotlivých částí
   *  
   * @param poradoveCislo čtyřmístné číslo s nevýznamovými nulami
   */
  public void generujOsobniCislo(String poradoveCislo) {    
    vysledek = fakulta + rokNastupu + typStudia.getZkratka() + poradoveCislo + formaStudia + nepovinne;
    OsobniCisloLogger.fine(vysledek);
  }
  
  /**
   * Naplní jednotlivé části osobního čísla a stanoví platnost formátu
   *  
   * @param radkaZadani řádka načtená ze vstupního souboru 
   */
  private void naplnAtributy(String radkaZadani) {
    String[] ocekavano = new String[Konstanty.POCET_CASTI];
    for (int i = 0; i < ocekavano.length; i++) {
      ocekavano[i] = null;
    }
    
    // částí může být méně, než je očekáváno, pak se bude předávat null
    String[] casti = radkaZadani.split(Konstanty.ZNAK_ODDELOVACE);
    
    for (int i = 0; i < casti.length; i++) {
      casti[i] = casti[i].trim();
      // vše velkými písmeny, kromě jména
      if (i != 1) {
        casti[i] = casti[i].toUpperCase();
      }
      if (i < Konstanty.POCET_CASTI) {
        // více údajů na vstupní řádce - zahazují se
        ocekavano[i] = casti[i];
      }
    }
    
    zpracujPrijmeni(ocekavano[0]);
    zpracujJmeno(ocekavano[1]);
    zpracujFakulta(ocekavano[2]);
    zpracujRokNastupu(ocekavano[3]);
    zpracujTypStudia(ocekavano[4]);
    zpracujFormaStudia(ocekavano[5]);
    zpracujNepovinne(ocekavano[6]);
  }
    
  /**
   * Naplní <code>prijmeni</code> buď skutečným příjmením nebo <code>ZNAK_CHYBY</code><br />
   * příjmení je VELKÝMI PÍSMENY
   * 
   * @param prijmeni skutečné příjmení nebo <code>null</code>
   */
  private void zpracujPrijmeni(String prijmeni) {
    if (prijmeni != null) {
      this.prijmeni = prijmeni;
      OsobniCisloLogger.finest("prijmeni: "+this.prijmeni);
    }
    else {
      this.prijmeni = Konstanty.ZNAK_CHYBY;
      this.platnyFormat = false;
    }
  }
  
  /**
   * Naplní <code>jmeno</code> buď skutečným jménem nebo <code>ZNAK_CHYBY</code><br/>
   * Jméno bude s prvním velkým písmenem, ostatní budou malá
   * 
   * @param jmeno skutečné jméno nebo <code>null</code>
   */
  private void zpracujJmeno(String jmeno) {
    if (jmeno != null) {
      // jméno má pouze první velké písmeno
      String prvni = jmeno.substring(0, 1).toUpperCase();
      String zbytek = jmeno.substring(1).toLowerCase();
      this.jmeno = prvni + zbytek;
      OsobniCisloLogger.finest("jmeno: "+this.jmeno);
    }
    else {
      this.jmeno = Konstanty.ZNAK_CHYBY;
      this.platnyFormat = false;
    }
  }
  
  /**
   * Naplní <code>rokNastupu</code> buď posledním dvojčíslím roku nebo <code>ZNAK_CHYBY</code><br/>
   * 
   * @param rokNastupu skutečný rok nástupu nebo <code>null</code>
   */
  private void zpracujRokNastupu(String rokNastupu) {
    if (rokNastupu == null) {
      chybnyRokNastupu();
      OsobniCisloLogger.finest("neni uveden rok nastupu");
      return;
    }
    if (rokNastupu.length() != 4) {
      // není čtyřmístný
      chybnyRokNastupu();
      OsobniCisloLogger.finest("rok nastupu neni ctyrciferny: "+rokNastupu);
      return;
    }
      
    try {
      Integer.parseInt(rokNastupu);
    } catch (NumberFormatException e) {
      // není to číslo
      chybnyRokNastupu();
      return;
    }

    // je to čtyřmístné číslo
    this.rokNastupu = rokNastupu.substring(2);
    OsobniCisloLogger.finest("rok nastupu: "+this.rokNastupu);
  }

  /**
   * Naplní <code>rokNastupu</code> <code>ZNAK_CHYBY</code> a nastaví chybu formátu
   */
  private void chybnyRokNastupu() {
    this.rokNastupu = Konstanty.ZNAK_CHYBY;
    this.platnyFormat = false;    
  }
 
  /**
   * Pokud zkratka fakulty patří do množiny povolených zkratek fakult, 
   * naplní <code>fakulta</code> písmenem fakulty<br/>
   * nebo <code>ZNAK_CHYBY</code> v opačných případech
   *   
   * @param fakulta řetězec, ve kterém by měla být platná zkratka fakulty nebo <code>null</code>
   */
  public void zpracujFakulta(String fakulta) {
    for (String[] fakulty : Konstanty.PLATNE_FAKULTY) {
      if (fakulty[0].equals(fakulta) == true) {
        this.fakulta = fakulty[1];
        OsobniCisloLogger.finest("fakulta: "+fakulty[1]);
        return;
      }     
    }
    this.fakulta = Konstanty.ZNAK_CHYBY;
    this.platnyFormat = false;        
  }
  
  
  /**
   * Pokud zkratka typu studia patří do výčtového typu povolených zkratek typů,
   * naplní <code>typStudia</code> příslušným enumem<br/>
   * nebo <code>TypStudia.NEPLATNY</code> v opačných případech
   *   
   * @param typStudia řetězec, ve kterém by měla být platná zkratka typu studia nebo <code>null</code>
   */
  private void zpracujTypStudia(String typStudia) {
    for (TypStudia typ : TypStudia.values()) {
      if (typ.getZkratka().equals(typStudia) == true) {
        this.typStudia = typ;
        OsobniCisloLogger.finest("typ studia: "+this.typStudia.getZkratka());
        return;
      }     
    }
    this.typStudia = TypStudia.NEPLATNY;
    this.platnyFormat = false;   
    
  }
  
  /**
   * Pokud zkratka formy studia patří do množiny povolených zkratek forem,
   * naplní <code>formaStudia</code> zkratkou formy studia<br/>
   * nebo <code>ZNAK_CHYBY</code> v opačných případech
   *   
   * @param formaStudia řetězec, ve kterém by měla být platná zkratka formy studia nebo <code>null</code>
   */
  private void zpracujFormaStudia(String formaStudia) {
    for (String forma : Konstanty.PLATNE_FORMYY_STUDIA) {
      if (forma.equals(formaStudia) == true) {
        this.formaStudia = formaStudia;
        OsobniCisloLogger.finest("forma studia: "+this.formaStudia);
        return;
      }     
    }
    this.formaStudia = Konstanty.ZNAK_CHYBY;
    this.platnyFormat = false;   
  }
  
  /**
   * Naplní <code>nepovinne</code> buď existující hodnotou nebo prázdnou hodnotou
   * @param nepovinne nepovinná část osobního čísla
   */
  private void zpracujNepovinne(String nepovinne) {
    if (nepovinne != null) {
      this.nepovinne = nepovinne;
      OsobniCisloLogger.finest("nepovinne: "+this.nepovinne);
    }
    else {
      this.nepovinne = "";
    }
  }
}
