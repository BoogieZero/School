import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/****************************************************************
 * Testovací třída {@code TestOsoby} slouží ke komplexnímu otestování
 * třídy {@link TestOsoby}.
 *
 * @author    Martin Hamet A14B0254P
 * @version   5.00.000
 */
public class TestOsoby {
    private Elipsa vlasy;
    private Obdelnik prekryvVlasu;
    private Elipsa hlava;
    private Elipsa praveOko;
    private Elipsa leveOko;
    private Elipsa usmev;
    private Elipsa prekryvUsmevu;
    private Obdelnik krk;
    private Obdelnik telo;
    private Obdelnik levaRuka;
    private Obdelnik pravaRuka;
    private Obdelnik levaNoha;
    private Obdelnik pravaNoha;

    
    
    
    
    
    
    

    
    
    
    
    

    
    
    

    
    

//  private Elipsa vlasy;

//== KONSTANTNÍ ATRIBUTY TŘÍDY ================================
//== PROMĚNNÉ ATRIBUTY TŘÍDY ==================================
//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR =======
//== KONSTANTNÍ ATRIBUTY INSTANCÍ =============================
//== PROMĚNNÉ ATRIBUTY INSTANCÍ ===============================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY =======================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ==========================

//#############################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY ============================
//== PŘÍPRAVA A ÚKLID PŘÍPRAVKU ===============================

  /************************************************************
   * Inicializace předcházející spuštění každého testu a připravující tzv.
   * přípravek (fixture), což je sada objektů, s nimiž budou testy pracovat.
   */
  @Before
  public void setUp() {
        vlasy = new Elipsa(15, 0, 70, 70, Barva.ZLUTA);
        prekryvVlasu = new Obdelnik(0, 35, 200, 200, Barva.KREMOVA);
        hlava = new Elipsa(20, 5, 60, 60, Barva.RUZOVA);
        praveOko = new Elipsa(35, 25, 10, 10, Barva.MODRA);
        leveOko = new Elipsa(55, 25, 10, 10, Barva.MODRA);
        usmev = new Elipsa(36, 40, 27, 15, Barva.CERVENA);
        prekryvUsmevu = new Elipsa(36, 35, 27, 12, Barva.RUZOVA);
        krk = new Obdelnik(40, 63, 20, 15, Barva.RUZOVA);
        telo = new Obdelnik(15, 78, 70, 80, Barva.SEDA);
        levaRuka = new Obdelnik(85, 83, 15, 80, Barva.RUZOVA);
        pravaRuka = new Obdelnik(0, 83, 15, 80, Barva.RUZOVA);
        levaNoha = new Obdelnik(55, 158, 20, 50, Barva.RUZOVA);
        pravaNoha = new Obdelnik(25, 158, 20, 50, Barva.RUZOVA);
    }


  /************************************************************
   * Úklid po testu - tato metoda se spustí po vykonání každého testu.
   */
  @After
  public void tearDown() {
      // nepouzita
  }



//== ABSTRAKTNÍ METODY ========================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ ====================
//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ =======================
//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ==========================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ =======================

  private String getNazevTvaru(String celyNazev) {
    int indexVNazvu = celyNazev.indexOf("_");
    return celyNazev.substring(0, indexVNazvu);
  }

//== INTERNÍ DATOVÉ TYPY ======================================
//== VLASTNÍ TESTY ============================================
//
  @Test
  public void testVytvareniOsoby()
  {
    assertEquals("Vlasy: chybný tvar", "Elipsa", getNazevTvaru(vlasy.getNazev()));
    assertEquals("Vlasy: chybná x-souřadnice", 15, vlasy.getX());
    assertEquals("Vlasy: chybná y-souřadnice", 0, vlasy.getY());
    assertEquals("Vlasy: chybná šířka", 70, vlasy.getSirka());
    assertEquals("Vlasy: chybná výška", 70, vlasy.getVyska());
    assertEquals("Vlasy: chybná barva", Barva.ZLUTA, vlasy.getBarva());

    assertEquals("PřekryvVlasů: chybný tvar", "Obdelnik", getNazevTvaru(prekryvVlasu.getNazev()));
    assertEquals("PřekryvVlasů: chybná y-souřadnice", 35, prekryvVlasu.getY());
   Barva barvaPrekryvu = Platno.getPlatno().getBarvaPozadi();
    assertEquals("PřekryvVlasů: chybná barva", barvaPrekryvu, prekryvVlasu.getBarva());
// 
    assertEquals("Hlava: chybný tvar", "Elipsa", getNazevTvaru(hlava.getNazev()));
    assertEquals("Hlava: chybná x-souřadnice", 20, hlava.getX());
    assertEquals("Hlava: chybná y-souřadnice", 5, hlava.getY());
    assertEquals("Hlava: chybná šířka", 60, hlava.getSirka());
    assertEquals("Hlava: chybná výška", 60, hlava.getVyska());
    assertEquals("Hlava: chybná barva", Barva.RUZOVA, hlava.getBarva());
// 
    assertEquals("PravéOko: chybný tvar", "Elipsa", getNazevTvaru(praveOko.getNazev()));
    assertEquals("PravéOko: chybná x-souřadnice", 35, praveOko.getX());
    assertEquals("PravéOko: chybná y-souřadnice", 25, praveOko.getY());
    assertEquals("PravéOko: chybná šířka", 10, praveOko.getSirka());
    assertEquals("PravéOko: chybná výška", 10, praveOko.getVyska());
    assertEquals("PravéOko: chybná barva", Barva.MODRA, praveOko.getBarva());

    assertEquals("LevéOko: chybný tvar", "Elipsa", getNazevTvaru(leveOko.getNazev()));
    assertEquals("LevéOko: chybná x-souřadnice", 55, leveOko.getX());
    assertEquals("LevéOko: chybná y-souřadnice", 25, leveOko.getY());
    assertEquals("LevéOko: chybná šířka", 10, leveOko.getSirka());
    assertEquals("LevéOko: chybná výška", 10, leveOko.getVyska());
    assertEquals("LevéOko: chybná barva", Barva.MODRA, leveOko.getBarva());
// 
    assertEquals("Úsměv: chybný tvar", "Elipsa", getNazevTvaru(usmev.getNazev()));
    assertEquals("Úsměv: chybná x-souřadnice", 36, usmev.getX());
    assertEquals("Úsměv: chybná y-souřadnice", 40, usmev.getY());
    assertEquals("Úsměv: chybná šířka", 27, usmev.getSirka());
    assertEquals("Úsměv: chybná výška", 15, usmev.getVyska());
    assertEquals("Úsměv: chybná barva", Barva.CERVENA, usmev.getBarva());

    assertEquals("PřekryvÚsměvu: chybný tvar", "Elipsa", getNazevTvaru(prekryvUsmevu.getNazev()));
    assertEquals("PřekryvÚsměvu: chybná x-souřadnice", 36, prekryvUsmevu.getX());
    assertEquals("PřekryvÚsměvu: chybná y-souřadnice", 35, prekryvUsmevu.getY());
    assertEquals("PřekryvÚsměvu: chybná šířka", 27, prekryvUsmevu.getSirka());
    assertEquals("PřekryvÚsměvu: chybná výška", 12, prekryvUsmevu.getVyska());
// 
    assertEquals("Krk: chybný tvar", "Obdelnik", getNazevTvaru(krk.getNazev()));
    assertEquals("Krk: chybná x-souřadnice", 40, krk.getX());
    assertEquals("Krk: chybná y-souřadnice", 63, krk.getY());
    assertEquals("Krk: chybná šířka", 20, krk.getSirka());
    assertEquals("Krk: chybná výška", 15, krk.getVyska());
    assertEquals("Krk: chybná barva", Barva.RUZOVA, krk.getBarva());

    assertEquals("Tělo: chybný tvar", "Obdelnik", getNazevTvaru(telo.getNazev()));
    assertEquals("Tělo: chybná x-souřadnice", 15, telo.getX());
    assertEquals("Tělo: chybná y-souřadnice", 78, telo.getY());
    assertEquals("Tělo: chybná šířka", 70, telo.getSirka());
    assertEquals("Tělo: chybná výška", 80, telo.getVyska());
    assertEquals("Tělo: chybná barva", Barva.SEDA, telo.getBarva());
    
    assertEquals("LeváRuka: chybný tvar", "Obdelnik", getNazevTvaru(levaRuka.getNazev()));
    assertEquals("LeváRuka: chybná x-souřadnice", 85, levaRuka.getX());
    assertEquals("LeváRuka: chybná y-souřadnice", 83, levaRuka.getY());
    assertEquals("LeváRuka: chybná šířka", 15, levaRuka.getSirka());
    assertEquals("LeváRuka: chybná výška", 80, levaRuka.getVyska());
    assertEquals("LeváRuka: chybná barva", Barva.RUZOVA, levaRuka.getBarva());

    assertEquals("PraváRuka: chybný tvar", "Obdelnik", getNazevTvaru(pravaRuka.getNazev()));
    assertEquals("PraváRuka: chybná x-souřadnice", 0, pravaRuka.getX());
    assertEquals("PraváRuka: chybná y-souřadnice", 83, pravaRuka.getY());
    assertEquals("PraváRuka: chybná šířka", 15, pravaRuka.getSirka());
    assertEquals("PraváRuka: chybná výška", 80, pravaRuka.getVyska());
    assertEquals("PraváRuka: chybná barva", Barva.RUZOVA, pravaRuka.getBarva());

    assertEquals("LeváNoha: chybný tvar", "Obdelnik", getNazevTvaru(levaNoha.getNazev()));
    assertEquals("LeváNoha: chybná x-souřadnice", 55, levaNoha.getX());
    assertEquals("LeváNoha: chybná y-souřadnice", 158, levaNoha.getY());
    assertEquals("LeváNoha: chybná šířka", 20, levaNoha.getSirka());
    assertEquals("LeváNoha: chybná výška", 50, levaNoha.getVyska());
    assertEquals("LeváNoha: chybná barva", Barva.RUZOVA, levaNoha.getBarva());

    assertEquals("PraváNoha: chybný tvar", "Obdelnik", getNazevTvaru(pravaNoha.getNazev()));
    assertEquals("PraváNoha: chybná x-souřadnice", 25, pravaNoha.getX());
    assertEquals("PraváNoha: chybná y-souřadnice", 158, pravaNoha.getY());
    assertEquals("PraváNoha: chybná šířka", 20, pravaNoha.getSirka());
    assertEquals("PraváNoha: chybná výška", 50, pravaNoha.getVyska());
    assertEquals("PraváNoha: chybná barva", Barva.RUZOVA, pravaNoha.getBarva());
  }

}

