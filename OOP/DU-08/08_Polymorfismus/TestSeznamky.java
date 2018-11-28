import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/****************************************************************
 * Testovací třída {@code TestSeznamky} slouží ke komplexnímu otestování
 * třídy {@link TestSeznamky}.
 *
 * @author    Pavel Herout
 * @version   2.00.000
 */
public class TestSeznamky {

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
//
  /*********************************************************
   *
   */
  @Test
  public void testSeznamky() {
    Seznamka seznamka = Seznamka.getSeznamka();
    Pozice vznik = new Pozice(0, 0);
    Osoba m1 = Osoba.getBeznyMuz(vznik);
    Osoba m2 = new Superman();
    Osoba z1 = Osoba.getBeznaZena(vznik);
    Osoba z2 = new Superwoman();
    Pozice novySrazMuzu = new Pozice(10, 100);
    seznamka.setSrazMuzu(novySrazMuzu);
    seznamka.pridejMuze(m1);
    testPozice(novySrazMuzu, m1.getPozice(), "nový sraz Mužů");
    seznamka.setSrazMuzu(vznik);
    seznamka.pridejMuze(m2);
	  Osoba m3 = new Osoba(vznik, 60, 1.0/1, 1.0/2, Barva.MODRA);
    seznamka.pridejMuze(m3);
   testPozice(vznik, m1.getPozice(), "m1 - původní sraz mužů");
   testPozice(vznik, m2.getPozice(), "m2 - původní sraz mužů");
   testPozice(vznik, m3.getPozice(), "m3 - původní sraz mužů");
    
    Pozice srazZen = new Pozice(350, 100);
    seznamka.setSrazZen(srazZen);
    seznamka.pridejZenu(z1);
   testPozice(srazZen, z1.getPozice(), "srazŽen");
    Pozice novySrazZen = new Pozice(350, 100);
    seznamka.setSrazZen(novySrazZen);
    seznamka.pridejZenu(z2);
	  Osoba z3 = new Osoba(vznik, 60, 1.0/2, 4.0/1, Barva.CERVENA);
    seznamka.pridejZenu(z3);
   testPozice(novySrazZen, z1.getPozice(), "z1 - nový sraz žen");
   testPozice(novySrazZen, z2.getPozice(), "z2 - nový sraz žen");
   testPozice(novySrazZen, z3.getPozice(), "z3 - nový sraz žen");
    
    seznamka.setPresouvac(10);
    seznamka.setMistoSchuzky(new Pozice(150, 50));
    
    assertEquals("Chybný počet uskutečněných setkání:", 7, 
             seznamka.realizujSchuzky(new Pozice(10, 150), 0));
  }
}
