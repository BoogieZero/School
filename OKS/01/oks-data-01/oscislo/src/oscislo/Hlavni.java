package oscislo;

/**
 * Hlavní třída generátoru osobních čísel
 * 
 * @author P.Herout
 *
 */
public class Hlavni {

  /** jméno souboru načtené z příkazové řádky */
  private static String jmenoVstupnihoSouboru;

  /** zkratka fakulty načtená z příkazové řádky */
  private static String fakulta;
  
  /**
   * privátní konstruktor, aby se zamezilo vytvoření instance
   */
  private Hlavni() {
    // skutečně žádný kód
  }
  
  /**
   * Nápověda, není-li zadán žádný parametr příkazové řádky
   */
  private static void help() {
    System.out.println("Generátor osobních čísel");
    System.out.println(" Použití:");
    System.out.println("   java Hlavni jmeno_vstupniho_souboru.txt fakulta");
  }
  
  /**
   * Zpracuje dva parametry příkazové řádky<br/>
   * pokud neexistují, vypíše návod a skončí
   * 
   * @param args parametry příkazové řádky
   */
  private static void parametryPrikazoveRadky(String[] args) {
    if (args.length > 1) {
      jmenoVstupnihoSouboru = args[0];
      fakulta = args[1];
    }
    else {
      System.err.println("Nezadáno jméno vstupního souboru nebo fakulta");
      help();
      System.exit(1);
    }
  }
  
  /**
   * Metoda <code>main()</code> aplikace
   * 
   * @param args parametry příkazové řádky
   */
  public static void main(String[] args) {
    parametryPrikazoveRadky(args);
    new Generator().zpracovani(jmenoVstupnihoSouboru, fakulta);
  }
  
  /**
   * Getr
   * 
   * @return jméno vstupního textového souboru
   */
  public static String getJmenoVstupnihoSouboru() {
    return jmenoVstupnihoSouboru;
  }
  
  /**
   * Getr
   * 
   * @return zkratka fakulty
   */
  public static String getFakulta() {
    return fakulta;
  }
}
