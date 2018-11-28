import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/****************************************************************
 * Testovací třída {@code TestOsoby} slouží ke komplexnímu otestování
 * třídy {@link Osoby}.
 * Oproti verzi 1.00 přibyly další testy
 *
 * @author    Pavel Herout
 * @version   3.00.000
 */
public class TestOsoby {
  private Osoba osStihlaVysoka;
  private Osoba osDiteChlapec;
  private Osoba osDiteDivka;
  private Osoba osBeznyMuz;
  private Osoba osBeznaZena;
  private Osoba osNoName;
  private Osoba osUrostlyGeneral;
  private Rozmer rozmer;
  private Osoba osMalaTlusta;
  
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
   * přípravek (fixture), což je sada objektu, s nimiž budou testy pracovat.
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
	private void testJedneOsoby(Osoba osoba, int x, int y, int sirka, int vyska, Barva barva) {
	  assertEquals("Chybná x-souřadnice: ", x, osoba.getX());
	  assertEquals("Chybná y-souřadnice: ", y, osoba.getY());
	  assertEquals("Chybná šířka: ", sirka, osoba.getSirka());
	  assertEquals("Chybná výška: ", vyska, osoba.getVyska());
	  assertEquals("Chybná barva: ", barva, osoba.getBarvaTela());
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Je tato osoba správně?"));
	}
	
	//Z DU-04
	/**
	 * Provede základní test pozice jedné osoby 
	 * testuje skutečné hodnoty dané osoby a to: souřadnice pozice 
	 * proti očekávaným hodnotám
	 * 
	 * @param osoba testovaná osoba
	 * @param pozice očekávaná pozice
	 */
	private void testPoziceJedneOsoby(Osoba osoba, Pozice pozice) {
	  assertEquals("Chybná x-souřadnice: ", pozice.x, osoba.getX());
	  assertEquals("Chybná y-souřadnice: ", pozice.y, osoba.getY());
	}
	
	 /***************************************************************************
   * Prohodí pozice zadaných osob a nechá zkontrolovat, 
   * zda si osoby své pozice doopravdy vyměnily.
   * První osobu je nutné nechta na konci překreslit,
   * protože druhá osoba ji při přesunu vymaže.
   *
   * @param o1 první osoba
   * @param o2 druhá osoba
   */
  private void pomProhodPozice(Osoba o1, Osoba o2) {
   Pozice p1 = o1.getPozice();
   Pozice p2 = o2.getPozice();
   o1.setPozice(p2.x, p2.y);
   o2.setPozice(p1);
   // zámerná chyba - ukazuje na nedokonalost řešení - neopravovat
  /*  o1.nakresli(); */
	  assertEquals("Chybná x-souřadnice: ", p1.x, o2.getX());
	  assertEquals("Chybná y-souřadnice: ", p1.y, o2.getY());
	  assertEquals("Chybná x-souřadnice: ", p2.x, o1.getX());
	  assertEquals("Chybná y-souřadnice: ", p2.y, o1.getY());
  }
	
//== SOUKROME A POMOCNE METODY INSTANCI =======================
//== INTERNI DATOVE TYPY ======================================
//== VLASTNI TESTY ============================================


//Testy z DÚ 3
	
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
	  testJedneOsoby(osDiteChlapec, 250, 0, 35, 70, Barva.MODRA);
	  osDiteDivka = new Osoba(300, 0, 30, Barva.SEDA);
	  osDiteDivka.setBarvaTela(Barva.CERVENA);
	  testJedneOsoby(osDiteDivka, 300, 0, 35, 70, Barva.CERVENA);
	  osBeznyMuz = new Osoba(120, 150, Barva.MODRA);
	  testJedneOsoby(osBeznyMuz, 120, 150, 70, 140, Barva.MODRA);
	  osBeznaZena = new Osoba(200, 150, Barva.CERVENA);
	  testJedneOsoby(osBeznaZena, 200, 150, 70, 140, Barva.CERVENA);
	  osNoName = new Osoba();
	  testJedneOsoby(osNoName, 0, 0, 70, 140, Barva.SEDA);
	  osUrostlyGeneral = new Osoba(350, 0, 30, 1.0/5, 8.0/6, Barva.KHAKI);
	  testJedneOsoby(osUrostlyGeneral, 350, 0, 112, 180, Barva.KHAKI);
	}

////////////////////////////////////////////////////////////////////
// Nove testy z DU-04	

  @Test
	public void testRozmer()
	{
    rozmer = new Rozmer(200, 100);
	  assertEquals("Chybná šířka: ", 200, rozmer.getSirka());
	  assertEquals("Chybná výška: ", 100, rozmer.getVyska());
	  // test přístupových práv atributu
	  assertEquals("Chybná šířka: ", 200, rozmer.sirka);
	  assertEquals("Chybná výška: ", 100, rozmer.vyska);
	   //test toString()
	  assertEquals("Chybný toString(): ", "Rozmer[sirka=200, vyska=100]", rozmer.toString());
	}
	
  @Test
	public void testBezparRozmer()
	{
    rozmer = new Rozmer();
	  assertEquals("Chybná šířka: ", 0, rozmer.getSirka());
	  assertEquals("Chybná výška: ", 0, rozmer.getVyska());
	  // test toString()
	  assertEquals("Chybný toString(): ", "Rozmer[sirka=0, vyska=0]", rozmer.toString());
	}
	
  @Test
  public void testKonstruktoruPozicRuznychOsob()
	{
	  Pozice pozice = new Pozice(250, 0);
	  osDiteChlapec = new Osoba(pozice, 30, Barva.MODRA);
	  testPoziceJedneOsoby(osDiteChlapec, pozice);

	  pozice = new Pozice(120, 150);
	  osBeznyMuz = new Osoba(pozice, Barva.MODRA);
	  testPoziceJedneOsoby(osBeznyMuz, pozice);

	  pozice = new Pozice(350, 0);
	  osUrostlyGeneral = new Osoba(pozice, 30, 1.0/5, 8.0/6, Barva.KHAKI);
	  testPoziceJedneOsoby(osUrostlyGeneral, pozice);
	}

  @Test
  public void testProhodPozice() {
	  osBeznaZena = new Osoba(250, 0, Barva.CERVENA);
	  osMalaTlusta = new Osoba(100, 0, 60, 1.0/1, 1.0/2, Barva.CERNA);
	  assertEquals("Chybná šířka: ", 70, osBeznaZena.getSirka());
	  assertEquals("Chybná výška: ", 140, osBeznaZena.getVyska());
	  IO.zprava("Před přesunem");
    pomProhodPozice(osBeznaZena, osMalaTlusta);
	  assertEquals("Chybný rozměr: ", "Rozmer[sirka=70, vyska=140]", 
	               osBeznaZena.getRozmer().toString());
  }

  @Test
  public void testStatickaTovarniMetoda() {
	  osBeznyMuz = Osoba.getBeznyMuz(new Pozice(180, 130));
	  assertEquals("Chybná barva těla: ", Barva.MODRA, 
	               osBeznyMuz.getBarvaTela());
	  testJedneOsoby(osBeznyMuz, 180, 130, 70, 140, Barva.MODRA);
	  osBeznaZena = Osoba.getBeznaZena(new Pozice(260, 130));
	  assertEquals("Chybná barva těla: ", Barva.CERVENA, 
	               osBeznaZena.getBarvaTela());
	  testJedneOsoby(osBeznaZena, 260, 130, 70, 140, Barva.CERVENA);
  }

  @Test
  public void testVytvoreniPodlePohlavi() {
	  osBeznyMuz = new Osoba(new Pozice(120, 150), Pohlavi.MUZ);
	  assertEquals("Chybná barva těla: ", Barva.MODRA, 
	               osBeznyMuz.getBarvaTela());
	  osBeznaZena = new Osoba(new Pozice(200, 150), Pohlavi.ZENA);
	  assertEquals("Chybná barva těla: ", Barva.CERVENA, 
	               osBeznaZena.getBarvaTela());
  }
  
}
