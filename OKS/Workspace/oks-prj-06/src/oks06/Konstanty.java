package oks06;

/**
 * Knihovní třída pro nastavení všech konstant projektu
 * 
 * @author P.Herout
 *
 */
public class Konstanty {
  
  /**
   * privátní konstruktor, aby se zamezilo vytvoření instance
   */
  private Konstanty() {
    // skutečně žádný kód
  }
  
  /** kódování všech použitých souborů */
  public static final String KODOVANI = "UTF-8";
  
  /** jméno výsledkového souboru */
  public static final String VYSLEDKOVY_SOUBOR = "vysledky.txt";

  /** jméno logovacího souboru */
  public static final String LOGOVACI_SOUBOR = "oks-01-log.txt";

  /** znak uvozující komentář */
  public static final String ZNAK_KOMENTARE = "#";

  /** znak oddělovače hodnot v řádce zadání */
  public static final String ZNAK_ODDELOVACE = ",";

  /** znak nahrazující chybný nebo chybějící údaj */
  public static final String ZNAK_CHYBY = "?";

  /** dosud nevygenerované osobní číslo */
  public static final String PRAZDNY = "";
  
  /** počet částí, ze kterých se skládá jedno zadání "Novák, Josef, fav, 2014, b, p, neco" */
  public static final int POCET_CASTI = 7;

  /** fakulty a jejich znaky */
  public static final String[][] PLATNE_FAKULTY = { {"FAV", "A"},
                                                    {"FEL", "E"},
                                                    {"FST", "S"},
                                                    {"FPE", "P"},                                        
                                                    {"FEK", "K"},
                                                    {"FF",  "F"},
                                                    {"FPR", "R"},
                                                    {"FZS", "Z"},
                                                    {"FDU", "D"},
                                                  };

  /**
   * Nalezne znak platný pro fakultu
   * 
   * @param fakulta zkratka fakulty
   * @return znak, pokud je hledaná zkratka fakulty platná, nebo <code>ZNAK_CHYBY</code> v opačných případech
   */
  public static String najdiZnakFakulty(String fakulta) {
    fakulta = fakulta.toUpperCase();
    for (String[] fakulty : PLATNE_FAKULTY) {
      if (fakulty[0].equals(fakulta) == true) {
        return fakulty[1];
      }     
    }
    return ZNAK_CHYBY;
  }
  
  /** znaky platných forem studia */
  public static final String[] PLATNE_FORMYY_STUDIA = {"P", "K", "D"};

  
  /** text ve výsledkovém souboru - správně */
  public static final String TEXT_SPRAVNY_FORMAT = "správně zadáno";

  /** text ve výsledkovém souboru - chybně zadáno */
  public static final String TEXT_CHYBNY_FORMAT = "chybně zadáno";
}
