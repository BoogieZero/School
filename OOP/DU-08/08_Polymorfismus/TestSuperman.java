import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/****************************************************************
 * Testovací třída {@code TestSuperman} slouží ke komplexnímu otestování
 * třídy {@link TestSuperman}.
 *
 * @author    Pavel Herout
 * @version   3.00.000
 */
public class TestSuperman {

//== KONSTANTNÍ ATRIBUTY TŘÍDY ================================
  private static final SpravcePlatna SP = SpravcePlatna.getInstance();

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
      // nepouzita
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
  /**
   * @param po očekávaná pozice
   * @param ps skutečná pozice
   * @param nazevInstance název testované instance
   */
  private void testPozice(Pozice po, Pozice ps, String nazevInstance) {
	  assertEquals("Chybná x-souřadnice " + nazevInstance + ": ", po.x, ps.x);
	  assertEquals("Chybná y-souřadnice " + nazevInstance + ": ", po.y, ps.y);
	 }

//== INTERNÍ DATOVÉ TYPY ======================================
//== VLASTNÍ TESTY ============================================

  /*********************************************************
   *
   */
  @Test
  public void testKompletniKonstruktor() {
    Superman spm = new Superman(new Pozice(200, 100), 60);
    spm.zobraz();
    Trojuhelnik znak = spm.getZnak();
    Pozice pZnak = new Pozice(214, 174);
    testPozice(pZnak, znak.getPozice(), "znak");
	assertEquals("Chybná šířka znaku: ", 42, znak.getSirka());
	assertEquals("Chybná výška znaku: ", 42, znak.getVyska());
	assertEquals("Chybná orientace znaku: ", Smer8.JIH, znak.getSmer());
	assertEquals("Chybná barva znaku: ", Barva.CERVENA, znak.getBarva());
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superman vykreslený správně?"));
	SP.odstranVse();
  }

  @Test
  public void testVelkySuperman() {
    Superman spm = new Superman(new Pozice(200, 10), 120);
    spm.zobraz();
    Trojuhelnik znak = spm.getZnak();
    Pozice pZnak = new Pozice(228, 158);
    testPozice(pZnak, znak.getPozice(), "znak");
	  assertEquals("Chybná šířka znak: ", 84, znak.getSirka());
	  assertEquals("Chybná výška znak: ", 84, znak.getVyska());
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je velký superman vykreslený správně?"));
	  SP.odstranVse();
  }
  
  @Test
  public void testKonstruktorPozice() {
    Superman spm = new Superman(new Pozice(100, 10));
    spm.zobraz();
    Trojuhelnik znak = spm.getZnak();
    Pozice pZnak = new Pozice(114, 84);
    testPozice(pZnak, znak.getPozice(), "znak");
	  assertEquals("Chybná šířka znak: ", 42, znak.getSirka());
	  assertEquals("Chybná výška znak: ", 42, znak.getVyska());
	  assertEquals("Chybná orientace znak: ", Smer8.JIH, znak.getSmer());
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superman vykreslený správně?"));
	  SP.odstranVse();
  }
  
  @Test
  public void testBezparKonstruktor() {
    Superman spm = new Superman();
    spm.zobraz();
    Trojuhelnik znak = spm.getZnak();
    Pozice pZnak = new Pozice(14, 74);
    testPozice(pZnak, znak.getPozice(), "znak");
	  assertEquals("Chybná šířka znak: ", 42, znak.getSirka());
	  assertEquals("Chybná výška znak: ", 42, znak.getVyska());
	  assertEquals("Chybná orientace znak: ", Smer8.JIH, znak.getSmer());
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superman vykreslený správně?"));
	  SP.odstranVse();
  }

  @Test
  public void testPresun() {
    Superman spm = new Superman();
    spm.zobraz();
    Presouvac presun = new Presouvac(5);
    Pozice pP = new Pozice(150, 150);
    presun.presunNa(pP, spm);
    Trojuhelnik znak = spm.getZnak();
    Pozice pZnak = new Pozice(150 + 14, 150 + 74);
    testPozice(pZnak, znak.getPozice(), "znak");
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Přesunul se superman správně?"));
	  SP.odstranVse();
  }

  @Test
  public void testZvyrazneni() {
    Osoba spm = new Superman(new Pozice(250, 50));
    spm.zobraz();
    Zvyraznovac zvyr = new Zvyraznovac(30);
    zvyr.zvyrazniPozadiElipsou(spm, Barva.STRIBRNA);
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superman zvýrazněný správně?"));
	  SP.odstranVse();
  }

  @Test
  public void testSetPoziceXY() {
    Superman spm = new Superman();
    spm.zobraz();
    Pozice pP = new Pozice(150, 150);
    spm.setPozice(pP.x, pP.y);
    testPozice(pP, spm.getPozice(), "Superman");
    Trojuhelnik znak = spm.getZnak();
    Pozice pZnak = new Pozice(150 + 14, 150 + 74);
    testPozice(pZnak, znak.getPozice(), "znak");
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superman vykreslený správně?"));
	SP.odstranVse();
  }
  
  @Test
  public void testSetPozicePoz() {
    Superman spm = new Superman();
    spm.zobraz();
    Pozice pP = new Pozice(150, 150);
    spm.setPozice(pP);
    testPozice(pP, spm.getPozice(), "Superman");
    Trojuhelnik znak = spm.getZnak();
    Pozice pZnak = new Pozice(150 + 14, 150 + 74);
    testPozice(pZnak, znak.getPozice(), "znak");
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superman vykreslený správně?"));
	SP.odstranVse();
  }
}
