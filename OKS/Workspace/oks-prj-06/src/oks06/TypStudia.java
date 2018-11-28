package oks06;

/**
 * Platné typy studia a jejich zkratky
 * 
 * @author P.Herout
 *
 */

public enum TypStudia {
  BAKALARSKY("B", "Bakalářský"), NAVAZUJICI("N", "Navazující"), 
  DOKTORSKY("P", "Doktorský"), MAGISTERSKY("M", "Magisterský"),
  NEPLATNY(Konstanty.ZNAK_CHYBY, "neplatný");
  
  /** zkratka typu studia */
  private final String zkratka;
  
  /** název typu studia */
  private final String nazev;
  
  /**
   * Naplní hodnotu výčtového typu dalšími informacemi
   * 
   * @param zkratka zkratka typu studia
   * @param nazev název typu studia
   */
  private TypStudia(String zkratka, String nazev) {
    this.zkratka = zkratka;
    this.nazev = nazev;
  }

  /**
   * Vrací zkratku typu studia
   * 
   * @return zkratka typu studia
   */
  public String getZkratka() {
    return zkratka;
  }
  
  /**
   * Vrací název typu studia
   * 
   * @return název typu studia
   */
  public String getNazev() {
    return nazev;
  }
}
