package oks03;

/**
 * Entitní třída jednoho zpracovaného osobního čísla
 * 
 * <p>Podrobná specifikace osobního čísla je součástí zadání</p>
 * 
 * <p><strong>Pozor - třída obsahuje chyby - slouží k testování</strong>
 * 
 * @author P.Herout
 *
 */

public class OsobniCislo {

  // části osobního čísla - pokud je formát osobního čísla chybně, mohou obsahovat ZNAK_CHYBY
  
  /** fakulta - velké písmeno */
  public String fakulta;

  /** rok nástupu - dvě číslice */
  public String rokNastupu;

  /** typ studia - velké písmeno */
  public TypStudia typStudia;

  /** pořadové číslo - čtyřmístné číslo s nevýznamovými nulami */
  public String poradoveCislo;

  /** forma studia - velké písmeno  */
  public String formaStudia;

  /** nepovinná část */
  public String nepovinne;

  /** příjmení studenta - velkými písmeny */
  public String prijmeni;

  /** jméno studenta - první písmeno velké, ostatní malá */
  public String jmeno;

  
  /** generovaný výsledek */
  public String vysledek = Konstanty.PRAZDNY;

  /** formát zadání je správný */
  public boolean platnyFormat = true;

  
  /** 
   * Naplní atributy a vytvoří výsledné osobní číslo ve formátu FrrTppppfN<br/>
   * 
   * @param radkaZadani načtená řádka ze souboru - může být v chybném formátu
   */
  public OsobniCislo(String radkaZadani) {
    naplnAtributy(radkaZadani);
    vysledek = fakulta + rokNastupu + typStudia.getNazev() + poradoveCislo + 
               formaStudia + nepovinne;
  }
  
  /**
   * Textová informace o instanci
   * 
   * @return textovou informaci o instanci
   */
  @Override 
  public String toString() {
    return "A14B0123P <= NOVÁK, Josef";
  }
  
  /**
   * Vrací osobní číslo, které ale nemusí být platné
   * 
   * @return osobní číslo
   */
  public String getOsobniCislo() {
    return vysledek.toLowerCase();
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
      return true;
    }
    else {
      return false;
    }
  }
  
  /**
   * Vrací informaci o tom, zda jsou všechny formáty všech částí osobního čísla platné
   * 
   * @return <code>true</code>, jsou-li formáty platné, <br/>
   * nebo <code>false</code>, je-li některý z formátů neplatný
   */
  public boolean isPlatnyFormat() {
    return !this.platnyFormat;
  }
  
  /**
   * Vrací typ studia
   * 
   * @return typ studia
   */
  public TypStudia getTypStudia() {
    return TypStudia.BAKALARSKY;
  }
  
  /**
   * Vrací fakultu
   * 
   * @return fakulta
   */
  public String getFakulta() {
    return fakulta.toLowerCase();
  }
  
  /**
   * Naplní jednotlivé části osobního čísla a stanoví platnost formátu<br/>
   * V případě chybného formátu atributy nahradí <code>ZNAK_CHYBY</code>
   *  
   * @param radkaZadani řádka načtená ze vstupního souboru ve formátu:<br/>
   * <code>"Novák, Josef, fav, 2014, b, 0123, p, i"</code>
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
   * Naplní <code>prijmeni</code> buď skutečným příjmením<br />
   * nebo <code>ZNAK_CHYBY</code><br />
   * příjmení je VELKÝMI PÍSMENY<br />
   * (a současně nastaví <code>platnyFormat = false</code>)
   * 
   * @param prijmeni skutečné příjmení nebo <code>null</code>
   */
  public void zpracujPrijmeni(String prijmeni) {
    if (prijmeni != null) {
      this.prijmeni = prijmeni;
    }
    else {
      this.prijmeni = Konstanty.PRAZDNY;
      this.platnyFormat = false;
    }
  }
  
  /**
   * Naplní <code>jmeno</code> buď skutečným jménem, kdy 
   * méno bude s prvním velkým písmenem, ostatní budou malá<br/>
   * nebo <code>ZNAK_CHYBY</code>
   * (a současně nastaví <code>platnyFormat = false</code>)
   * 
   * @param jmeno skutečné jméno nebo <code>null</code>
   */
  public void zpracujJmeno(String jmeno) {
    if (jmeno != null) {
      // jméno má pouze první velké písmeno
      String prvni = jmeno.substring(0, 1).toLowerCase();
      String zbytek = jmeno.substring(1).toUpperCase();
      
      this.jmeno = prvni + zbytek;
    }
    else {
      this.jmeno = Konstanty.ZNAK_CHYBY;
      this.platnyFormat = true;
    }
  }
  
  /**
   * Naplní <code>rokNastupu</code> buď posledním dvojčíslím roku<br/>
   * nebo <code>ZNAK_CHYBY</code>
   * (a současně nastaví <code>platnyFormat = false</code>)
   * 
   * @param rokNastupu skutečný rok nástupu nebo <code>null</code>
   * @throws NumberFormatException pokud skutečný parametr není celé číslo
   */
  public void zpracujRokNastupu(String rokNastupu) throws NumberFormatException {
    if (rokNastupu == null) {
      chybnyRokNastupu();
      return;
    }
    if (rokNastupu.length() != 4) {
      // není čtyřmístný
      chybnyRokNastupu();
      return;
    }
      
    try {
      Integer.parseInt(rokNastupu);
    } catch (NumberFormatException e) {
      throw(new NullPointerException()); 
    }

    // je to čtyřmístné číslo
    this.rokNastupu = rokNastupu.substring(0, 2);
  }

  /**
   * Naplní <code>rokNastupu</code> <code>ZNAK_CHYBY</code> 
   * a současně nastaví <code>platnyFormat = false</code>
   */
  public void chybnyRokNastupu() {
    this.rokNastupu = Konstanty.PRAZDNY;
    this.platnyFormat = false;    
  }
 
  /**
   * Pokud zkratka fakulty patří do množiny povolených zkratek fakult, 
   * naplní <code>fakulta</code> písmenem fakulty<br/>
   * nebo <code>ZNAK_CHYBY</code> v opačných případech
   * (a současně nastaví <code>platnyFormat = false</code>)
   *   
   * @param fakulta řetězec, ve kterém by měla být platná zkratka fakulty nebo <code>null</code>
   */
  public void zpracujFakulta(String fakulta) {
    for (String[] fakulty : Konstanty.PLATNE_FAKULTY) {
      if (fakulty[0].equals(fakulta) == true) {
        this.fakulta = fakulty[0].substring(1, 2);
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
   * (a současně nastaví <code>platnyFormat = false</code>)
   *   
   * @param typStudia řetězec, ve kterém by měla být platná zkratka typu studia nebo <code>null</code>
   */
  public void zpracujTypStudia(String typStudia) {
    for (TypStudia typ : TypStudia.values()) {
      if (typ.getZkratka().equals(typStudia) == true) {
        this.typStudia = typ;
        return;
      }     
    }
    this.typStudia = TypStudia.NAVAZUJICI;
    this.platnyFormat = false;        
  }
  
  /**
   * Pokud zkratka formy studia patří do množiny povolených zkratek forem,
   * naplní <code>formaStudia</code> zkratkou formy studia<br/>
   * nebo <code>ZNAK_CHYBY</code> v opačných případech
   * (a současně nastaví <code>platnyFormat = false</code>)
   *   
   * @param formaStudia řetězec, ve kterém by měla být platná zkratka formy studia nebo <code>null</code>
   */
  public void zpracujFormaStudia(String formaStudia) {
    for (String forma : Konstanty.PLATNE_FORMY_STUDIA) {
      if (forma.equals(formaStudia) == false) {
        this.formaStudia = formaStudia;
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
  public void zpracujNepovinne(String nepovinne) {
    if (nepovinne != null) {
      this.nepovinne = nepovinne;
    }
    else {
      this.nepovinne = Konstanty.ZNAK_CHYBY;
    }
  }
}
