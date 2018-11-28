import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/****************************************************************
 * Testovací třída {@code TestOsoby} slouží ke komplexnímu otestování
 * třídy {@link Osoby}.
 *
 * @author    Martin Hamet A14B0254P
 * @version   6.00.000
 */
public class TestOsoby {
  private Osoba osStihlaVysoka;
  private Osoba osDiteChlapec;
  private Osoba osDiteDivka;
  private Osoba osBeznyMuz;
  private Osoba osBeznaZena;
  private Osoba osNoName;
  private Osoba osUrostlyGeneral;
  
//== KONSTANTNI ATRIBUTY TRIDY ================================
//== PROMENNE ATRIBUTY TRIDY ==================================
//== STATICKY INICIALIZACNI BLOK - STATICKY KONSTRUKTOR =======
//== KONSTANTNI ATRIBUTY INSTANCI =============================
//== PROMENNE ATRIBUTY INSTANCI ===============================
//== PRISTUPOVE METODY VLASTNOSTI TRIDY =======================
//== OSTATNI NESOUKROME METODY TRIDY ==========================

//#############################################################
//== KONSTRUKTORY A TOVARNI METODY ============================
//== PRIPRAVA A UKLID PRIPRAVKU ===============================

  /************************************************************
   * Inicializace předcházející spuštění každého testu a připravující tzv.
   * přípravek (fixture), což je sada objektů, s nimiž budou testy pracovat.
   */
  @Before
  public void setUp() {
      // nepouzito
  }


  /************************************************************
   * Úklid po testu - tato metoda se spuatí po vykonání každého testu.
   */
  @After
  public void tearDown() {
      // nepouzito
  }



//== ABSTRAKTNI METODY ========================================
//== PRISTUPOVE METODY VLASTNOSTI INSTANCI ====================
//== OSTATNI NESOUKROME METODY INSTANCI =======================
//== SOUKROME A POMOCNE METODY TRIDY ==========================
    /**
     * Provede základní test jedné osoby 
     * testuje skutečné hodnoty dané osoby a to: souřadnice, výšku, šířku a barvu těla 
     * proti očekávaným hodnotám 
     * po skončení testu zobrazí dotaz pro uživatele na správnost testu
     * 
     * @param osoba testovaná osoba
     * @param x očekávaná x-souřadnice
     * @param y očekávaná y-souřadnice
     * @param sirka očekávaná šířka
     * @param vyska očekávaná výška
     * @param barva očekávaná barva
     */
    private void testJedneOsoby(Osoba osoba, int x, int  y, int sirka, int vyska, Barva barva) {
        assertEquals("Chybná souřadnice X: ",x,osoba.getX());
        assertEquals("Chybná souřadnice Y: ",y,osoba.getY());
        assertEquals("Chybná šířka: ",sirka,osoba.getSirka());
        assertEquals("Chybná výška: ",vyska,osoba.getVyska());
        assertEquals("Chybná barva: ",barva,osoba.getBarvaTela());
       // assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je tato osoba správně?"));
    }

//== SOUKROME A POMOCNE METODY INSTANCI =======================
//== INTERNI DATOVE TYPY ======================================
//== VLASTNI TESTY ============================================


//==NOVÉ TESTY Z DÚ 3 =========================================

  /**
     * Testuje odloženou inicializaci
   */
  @Test
  public void testStihlaVysokaNazev()
    {
    osStihlaVysoka = new Osoba(0, 0, 60, 1.0/2, 4.0/1, Barva.HNEDA);
      assertEquals("Chybný název: ", "Osoba_1", osStihlaVysoka.getNazev());
    }

  /**
     * Testuje překrytí toString()
   */
  @Test
    public void testStihlaVysokaToString()
    {
    osStihlaVysoka = new Osoba(0, 0, 60, 1.0/2, 4.0/1, Barva.HNEDA);
      String spravnyVysledek = "Osoba_1: x=0, y=0, výška=180, šířka=60, barva těla=hneda";
      assertEquals("Chybný toSting(): ", spravnyVysledek, osStihlaVysoka.toString());
    }
    
  @Test
    public void testRuznychOsob()
    {
        osDiteChlapec = new Osoba(250, 0, 30, Barva.MODRA);
        testJedneOsoby(osDiteChlapec,250,0,35,70,Barva.MODRA);
        
        osDiteDivka = new Osoba(300, 0, 30, Barva.SEDA);
        osDiteDivka.setBarvaTela(Barva.CERVENA);
        testJedneOsoby(osDiteDivka,300,0,35,70,Barva.CERVENA);
        
        osBeznyMuz = new Osoba(120, 150, Barva.MODRA);
        testJedneOsoby(osBeznyMuz,120,150,70,140,Barva.MODRA);
        
        osBeznaZena = new Osoba(200, 150, Barva.CERVENA);
        testJedneOsoby(osBeznaZena,200,150,70,140,Barva.CERVENA);
        
        osNoName = new Osoba();
        testJedneOsoby(osNoName,0,0,70,140,Barva.SEDA);
        
        osUrostlyGeneral = new Osoba(350, 0, 30, 1.0/5, 8.0/6, Barva.KHAKI);
        testJedneOsoby(osUrostlyGeneral,350,0,112,180,Barva.KHAKI);
    }
        /*********************************************************
   * test existence metody testJedneOsoby()
   */
  @Test
  public void testExistenceTestuJedneOsoby() {
      osBeznyMuz = new Osoba(120, 150, Barva.MODRA);
      testJedneOsoby(osBeznyMuz, 120, 150, 70, 140, Barva.MODRA);
}
  
  /*********************************************************
   * test úplnosti metody testRuznychOsob()
   */
  @Test
  public void testUplnostiTestuRuznychOsob() {
    osDiteChlapec = null;
    osDiteDivka = null;
    osBeznyMuz = null;
    osBeznaZena = null;
    osNoName = null;
    osUrostlyGeneral = null;
    // vytvoří nové instance
   testRuznychOsob();
   // testy těchto instancí
   assertNotNull("Netestován osDiteChlapec", osDiteChlapec);
   assertNotNull("Netestován osDiteDivka", osDiteDivka);
   assertNotNull("Netestován osBeznyMuz", osBeznyMuz);
   assertNotNull("Netestován osBeznaZena", osBeznaZena);
   assertNotNull("Netestován osNoName", osNoName);
   assertNotNull("Netestován osUrostlyGeneral", osUrostlyGeneral);
  }

//   @Test
//  public void testDiteChlapec()
//  {
//    osDiteChlapec = new Osoba(250, 0, 30, Barva.MODRA);
//    assertEquals("Chybná x-souřadnice: ", 250, osDiteChlapec.getX());
//    assertEquals("Chybná y-souřadnice: ", 0, osDiteChlapec.getY());
//    assertEquals("Chybná šířka: ", 35, osDiteChlapec.getSirka());
//    assertEquals("Chybná výška: ", 70, osDiteChlapec.getVyska());
//    assertEquals("Chybná barva: ", Barva.MODRA, osDiteChlapec.getBarvaTela());
//    assertEquals("Chybná barva: ", Barva.MODRA, osDiteChlapec.getTelo().getBarva());
//  }
// 
//   @Test
//  public void testDiteDivka()
//  {
//    osDiteDivka = new Osoba(300, 0, 30, Barva.SEDA);
//    assertEquals("Chybná x-souřadnice: ", 300, osDiteDivka.getX());
//    assertEquals("Chybná y-souřadnice: ", 0, osDiteDivka.getY());
//    assertEquals("Chybná šířka: ", 35, osDiteDivka.getSirka());
//    assertEquals("Chybná výška: ", 70, osDiteDivka.getVyska());
//    IO.cekej(500);
//    osDiteDivka.setBarvaTela(Barva.CERVENA);
//    assertEquals("Chybná barva: ", Barva.CERVENA, osDiteDivka.getBarvaTela());
//    assertEquals("Chybná barva: ", Barva.CERVENA, osDiteDivka.getTelo().getBarva());
//  }
// 
//   @Test
//  public void testBeznyMuz()
//  {
//    osBeznyMuz = new Osoba(120, 150, Barva.MODRA);
//    assertEquals("Chybná x-souřadnice: ", 120, osBeznyMuz.getX());
//    assertEquals("Chybná y-souřadnice: ", 150, osBeznyMuz.getY());
//    assertEquals("Chybná šířka: ", 70, osBeznyMuz.getSirka());
//    assertEquals("Chybná výška: ", 140, osBeznyMuz.getVyska());
//    assertEquals("Chybná barva: ", Barva.MODRA, osBeznyMuz.getBarvaTela());
//  }
// 
//   @Test
//  public void testBeznaZena()
//  {
//    osBeznaZena = new Osoba(200, 150, Barva.CERVENA);
//    assertEquals("Chybná x-souřadnice: ", 200, osBeznaZena.getX());
//    assertEquals("Chybná y-souřadnice: ", 150, osBeznaZena.getY());
//    assertEquals("Chybná šířka: ", 70, osBeznaZena.getSirka());
//    assertEquals("Chybná výška: ", 140, osBeznaZena.getVyska());
//    assertEquals("Chybná barva: ", Barva.CERVENA, osBeznaZena.getBarvaTela());
//  }
// 
//   @Test
//  public void testNoName()
//  {
//    osNoName = new Osoba();
//    assertEquals("Chybná x-souřadnice: ", 0, osNoName.getX());
//    assertEquals("Chybná y-souřadnice: ", 0, osNoName.getY());
//    assertEquals("Chybná šířka: ", 70, osNoName.getSirka());
//    assertEquals("Chybná výška: ", 140, osNoName.getVyska());
//    assertEquals("Chybná barva: ", Barva.SEDA, osNoName.getBarvaTela());
//  }
// 
//   @Test
//  public void testUrostlyGeneral()
//  {
//    osUrostlyGeneral = new Osoba(350, 0, 30, 1.0/5, 8.0/6, Barva.KHAKI);
//    assertEquals("Chybná x-souřadnice: ", 350, osUrostlyGeneral.getX());
//    assertEquals("Chybná y-souřadnice: ", 0, osUrostlyGeneral.getY());
//    assertEquals("Chybná šířka: ", 112, osUrostlyGeneral.getSirka());
//    assertEquals("Chybná výška: ", 180, osUrostlyGeneral.getVyska());
//    assertEquals("Chybná barva: ", Barva.KHAKI, osUrostlyGeneral.getBarvaTela());
//  }
  
}
