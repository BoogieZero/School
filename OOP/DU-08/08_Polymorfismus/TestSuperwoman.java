import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.awt.Font;

import static org.junit.Assert.*;

/****************************************************************
 * Testovací třída {@code TestSuperwoman} slouží ke komplexnímu otestování
 * třídy {@link TestSuperwoman}.
 *
 * @author    Pavel Herout
 * @version   3.00.000
 */
public class TestSuperwoman {

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
  @Test
  public void testKompletniKonstruktor() {
    Superwoman spw = new Superwoman(new Pozice(200, 100), 60);
    spw.zobraz();
    Trojuhelnik superTelo = spw.getSuperTelo();
    Pozice pTelo = new Pozice(200, 100 + 60);
    testPozice(pTelo, superTelo.getPozice(), "superTělo");
	assertEquals("Chybná šířka superTělo: ", 70, superTelo.getSirka());
	assertEquals("Chybná výška superTělo: ", 80, superTelo.getVyska());
	assertEquals("Chybná orientace superTělo: ", Smer8.JIH, superTelo.getSmer());
	assertEquals("Chybná barva těla: ", Barva.ZADNA, spw.getBarvaTela());
	assertEquals("Chybná barva supertěla: ", Barva.CERNA, superTelo.getBarva());

    Trojuhelnik znak = spw.getZnak();
    Pozice pZnak = new Pozice(217, 177);
    testPozice(pZnak, znak.getPozice(), "znak");
    assertEquals("Chybná šířka znaku: ", 35, znak.getSirka());
	assertEquals("Chybná výška znaku: ", 35, znak.getVyska());
	assertEquals("Chybná orientace znaku: ", Smer8.JIH, znak.getSmer());
	assertEquals("Chybná barva znaku: ", Barva.CERVENA, znak.getBarva());
	
	Text pismenoS = spw.getPismenoS();
    Pozice pPS = new Pozice(225, 167);
    testPozice(pPS, pismenoS.getPozice(), "písmenoS");
	assertEquals("Chybná barva písmene S: ", Barva.ZLUTA, pismenoS.getBarva());
    assertEquals("Chybný text písmene S: ", "S", pismenoS.getNazev());
    Font font = pismenoS.getFont();
	assertEquals("Chybné jméno fontu písmene S: ", "Serif", font.getName());
	assertEquals("Chybná velikost písmene S: ", 35, font.getSize());
	assertEquals("Chybná tloušťka fontu písmene S: ", Text.TUCNY, font.getStyle());
	
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superwoman vykreslena správně?"));
	SP.odstranVse();
  }

  @Test
  public void testVelkaSuperwoman() {
    Superwoman spw = new Superwoman(new Pozice(200, 10), 120);
    spw.zobraz();
    Trojuhelnik superTelo = spw.getSuperTelo();
    Pozice pTelo = new Pozice(200, 10 + 120);
    testPozice(pTelo, superTelo.getPozice(), "superTělo");
	assertEquals("Chybná šířka superTělo: ", 140, superTelo.getSirka());
	assertEquals("Chybná výška superTělo: ", 160, superTelo.getVyska());
	
    Trojuhelnik znak = spw.getZnak();
    Pozice pZnak = new Pozice(235, 165);
    testPozice(pZnak, znak.getPozice(), "znak");
    assertEquals("Chybná šířka znaku: ", 70, znak.getSirka());
	assertEquals("Chybná výška znaku: ", 70, znak.getVyska());
	
	Text pismenoS = spw.getPismenoS();
    Pozice pPS = new Pozice(250, 144);
    testPozice(pPS, pismenoS.getPozice(), "písmenoS");
    Font font = pismenoS.getFont();
	assertEquals("Chybná velikost písmene S: ", 70, font.getSize());
	
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je velká superwoman vykreslena správně?"));
	SP.odstranVse();
  }

  @Test
  public void testBezparKonstruktor() {
    Superwoman spw = new Superwoman();
    spw.zobraz();
    Trojuhelnik superTelo = spw.getSuperTelo();
    Pozice pTelo = new Pozice(0, 60);
    testPozice(pTelo, superTelo.getPozice(), "superTělo");
	assertEquals("Chybná šířka superTělo: ", 70, superTelo.getSirka());
	assertEquals("Chybná výška superTělo: ", 80, superTelo.getVyska());
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superwoman vykreslená správně?"));
	SP.odstranVse();
  }

  @Test
  public void testKonstruktorPozice() {
    Superwoman spw = new Superwoman(new Pozice(100, 10));
    spw.zobraz();
    Trojuhelnik superTelo = spw.getSuperTelo();
    Pozice pTelo = new Pozice(100, 10 + 60);
    testPozice(pTelo, superTelo.getPozice(), "superTělo");
	assertEquals("Chybná šířka superTělo: ", 70, superTelo.getSirka());
	assertEquals("Chybná výška superTělo: ", 80, superTelo.getVyska());
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superwoman vykreslená správně?"));
	SP.odstranVse();
  }
  

  @Test
  public void testPresun() {
    Superwoman spw = new Superwoman();
    spw.zobraz();
    Presouvac presun = new Presouvac(5);
    Pozice pP = new Pozice(150, 150);
    presun.presunNa(pP, spw);
    Trojuhelnik superTelo = spw.getSuperTelo();
    Pozice pTelo = new Pozice(150, 150 + 60);
    testPozice(pTelo, superTelo.getPozice(), "superTělo");
    
    Trojuhelnik znak = spw.getZnak();
    Pozice pZnak = new Pozice(167, 227);
    testPozice(pZnak, znak.getPozice(), "znak");
	
	Text pismenoS = spw.getPismenoS();
    Pozice pPS = new Pozice(175, 217);
    testPozice(pPS, pismenoS.getPozice(), "písmenoS");
    
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superwoman přesunuta správně?"));
	SP.odstranVse();
  }
  

  @Test
  public void testZvyrazneni() {
    Osoba spw = new Superwoman(new Pozice(250, 50));
    spw.zobraz();
    Zvyraznovac zvyr = new Zvyraznovac();
    zvyr.zvyrazniPozadi(spw, Barva.ZLATA);
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superwoman zvýrazněna správně?"));
	  SP.odstranVse();
  }

  @Test
  public void testSetPoziceXY() {
    Superwoman spw = new Superwoman();
    spw.zobraz();
    Pozice pP = new Pozice(150, 150);
    spw.setPozice(pP.x, pP.y);
    testPozice(pP, spw.getPozice(), "Superwoman");

    Trojuhelnik znak = spw.getZnak();
    Pozice pZnak = new Pozice(167, 227);
    testPozice(pZnak, znak.getPozice(), "znak");
	
	Text pismenoS = spw.getPismenoS();
    Pozice pPS = new Pozice(175, 217);
    testPozice(pPS, pismenoS.getPozice(), "písmenoS");

    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superwoman vykreslena správně?"));
	SP.odstranVse();
  }
  
  @Test
  public void testSetPozicePoz() {
    Superwoman spw = new Superwoman();
    spw.zobraz();
    Pozice pP = new Pozice(150, 150);
    spw.setPozice(pP);
    testPozice(pP, spw.getPozice(), "Superwoman");

    Trojuhelnik znak = spw.getZnak();
    Pozice pZnak = new Pozice(167, 227);
    testPozice(pZnak, znak.getPozice(), "znak");
	
	Text pismenoS = spw.getPismenoS();
    Pozice pPS = new Pozice(175, 217);
    testPozice(pPS, pismenoS.getPozice(), "písmenoS");

    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je superwoman vykreslena správně?"));
	SP.odstranVse();
  }
}
