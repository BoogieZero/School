import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/****************************************************************
 * Testovací třída {@code TestRande} slouží ke komplexnímu otestování
 * třídy {@link TestRande}.
 *
 * @author    Pavel Herout
 * @version   3.00.000
 */
public class TestRande {

//== KONSTANTNÍ ATRIBUTY TŘÍDY ================================
  private static final SpravcePlatna SP = SpravcePlatna.getInstance();
  private Rande rande;

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
	 
	 /**
   * Test vykreslení obrazce na plátnu
   * @param chyboveHlaseni chybové hlášení Assertu
   * @param obrazec obrazec, terý má být vykreslen
   */
  private void testVykresleni(String chyboveHlaseni, IKresleny obrazec) {
    if (SP.poradi(obrazec) == -1) {
      assertEquals(chyboveHlaseni, 0, SP.poradi(obrazec));
    }
  }

//== VLASTNÍ TESTY ============================================
//
// z DU-06
  /*********************************************************
   *
   */
  @Test
  public void testVzniku() {
 	  SP.odstranVse();
    Pozice pM = new Pozice(10, 10);
    Pozice pZ = new Pozice(400, 150);
    rande = new Rande(pM, pZ);
    
    Osoba muz = rande.getMuz();
    testPozice(pM, muz.getPozice(), "muž");
    
    Osoba zena = rande.getZena();
    testPozice(pZ, zena.getPozice(), "žena");
    
    testPozice(pM, rande.getDomaMuz(), "domaMuž");
    testPozice(pZ, rande.getDomaZena(), "domaŽena");

    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Jsou obě osoby vykresleny správně?"));
 	  SP.odstranVse();
  }

  @Test
  public void testSetkaniUprostred() {
 	  SP.odstranVse();
    Pozice pM = new Pozice(10, 10);
    Pozice pZ = new Pozice(400, 150);
    rande = new Rande(pM, pZ);

    Osoba muz = rande.getMuz();
    Osoba zena = rande.getZena();
    
    testVykresleni("Muž není vykreslen: ", muz);
    testVykresleni("Žena není vykreslena: ", zena);

    Presouvac chuzeNaRande = new Presouvac(5);
    Pozice pS = new Pozice(150, 80);
    rande.jdouNaRande(pS, chuzeNaRande);
    
    testPozice(new Pozice(220, 80), muz.getPozice(), "muž");
    testPozice(pS, zena.getPozice(), "žena");
    
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Jsou obě osoby vykresleny správně?"));
 	  SP.odstranVse();
  }

  @Test
  public void testSetkaniVlevo() {
 	  SP.odstranVse();
    Pozice pM = new Pozice(200, 150);
    Pozice pZ = new Pozice(100, 150);
    rande = new Rande(pM, pZ);
    
    Osoba muz = rande.getMuz();
    Osoba zena = rande.getZena();
    
    testVykresleni("Muz neni vykreslen: ", muz);
    testVykresleni("Zena neni vykreslena: ", zena);

    Presouvac chuzeNaRande = new Presouvac(10);
    Pozice pS = new Pozice(10, 10);
    rande.jdouNaRande(pS, chuzeNaRande);
    
    testPozice(new Pozice(80, 10), muz.getPozice(), "muž");
    testPozice(pS, zena.getPozice(), "žena");
    
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Jsou obě osoby vykresleny správně?"));
 	  SP.odstranVse();
  }

  @Test
  public void testSetkaniVpravo() {
 	  SP.odstranVse();
    Pozice pM = new Pozice(200, 10);
    Pozice pZ = new Pozice(100, 110);
    rande = new Rande(pM, pZ);
    
    Osoba muz = rande.getMuz();
    Osoba zena = rande.getZena();
    
    testVykresleni("Muž není vykreslen: ", muz);
    testVykresleni("Žena není vykreslena: ", zena);

    Presouvac chuzeNaRande = new Presouvac(10);
    Pozice pS = new Pozice(300, 150);
    rande.jdouNaRande(pS, chuzeNaRande);
    
    testPozice(new Pozice(370, 150), muz.getPozice(), "muž");
    testPozice(pS, zena.getPozice(), "žena");
    
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Jsou obě osoby vykresleny správně?"));
 	  SP.odstranVse();
  }
  
  @Test
  public void testJdouSpolu() {
 	  SP.odstranVse();
    Pozice pM = new Pozice(10, 10);
    Pozice pZ = new Pozice(400, 150);
    rande = new Rande(pM, pZ);
    
    Osoba muz = rande.getMuz();
    Osoba zena = rande.getZena();
    
    testVykresleni("Muž není vykreslen: ", muz);
    testVykresleni("Žena není vykreslena: ", zena);

    IO.zprava("Jdou na rande");
    Presouvac chuzeNaRande = new Presouvac(5);
    Pozice pS = new Pozice(150, 80);
    rande.jdouNaRande(pS, chuzeNaRande);
    
    IO.zprava("Jdou společně");
    Pozice pV = new Pozice(10, 10);
    Presouvac chuzeSpolu = new Presouvac(3);
    rande.parJdeSpolecne(pV, chuzeSpolu);
    
    testPozice(pV, zena.getPozice(), "žena");
    
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Jsou obě osoby vykresleny správně?"));
 	  SP.odstranVse();
  }

  @Test
  public void testPokracujiSpolu() {
 	  SP.odstranVse();
    Pozice pM = new Pozice(10, 10);
    Pozice pZ = new Pozice(400, 150);
    rande = new Rande(pM, pZ);

    Presouvac chuzeNaRande = new Presouvac(5);
    Pozice pS = new Pozice(150, 80);
    rande.jdouNaRande(pS, chuzeNaRande);
    
    Pozice pV = new Pozice(10, 10);
    Presouvac chuzeSpolu = new Presouvac(3);
    Par par = rande.parJdeSpolecne(pV, chuzeSpolu);
    IO.zprava("Jdou dál společně");
    
    Pozice pD = new Pozice(350, 10);
    rande.parPokracujeSpolecne(par, pD, chuzeSpolu);
    Pozice pK = new Pozice(10, 150);
    rande.parPokracujeSpolecne(par, pK, chuzeSpolu);
    
    Osoba zena = rande.getZena();
    testPozice(pK, zena.getPozice(), "žena");
    
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Jsou obě osoby vykresleny správně?"));
 	  SP.odstranVse();
  }

  @Test
  public void testCeleRande() {
 	  SP.odstranVse();
    Pozice pM = new Pozice(10, 10);
    Pozice pZ = new Pozice(400, 150);
    rande = new Rande(pM, pZ);
    
    Osoba muz = rande.getMuz();
    Osoba zena = rande.getZena();
    
    Presouvac chuzeNaRande = new Presouvac(15);
    Pozice pS = new Pozice(150, 80);
    rande.jdouNaRande(pS, chuzeNaRande);
    
    Pozice pV = new Pozice(10, 10);
    Presouvac chuzeSpolu = new Presouvac(13);
    Par par = rande.parJdeSpolecne(pV, chuzeSpolu);    
    
    // prvni spolecna zastavka
    //    IO.zprava("První společná zastávka");
    testPozice(new Pozice(10 + zena.getSirka(), 10), muz.getPozice(), "První společná zastávka muž");
    testPozice(pV, zena.getPozice(), "První společná zastávka žena");
    
    Pozice pD = new Pozice(350, 10);
    rande.parPokracujeSpolecne(par, pD, chuzeSpolu);

    // druha spolecna zastavka
    //   IO.zprava("Druhá společná zastávka");
    testPozice(new Pozice(350 + zena.getSirka(), 10), muz.getPozice(), "Druhá společná zastávka muž");
    testPozice(pD, zena.getPozice(), "Druhá společná zastávka žena");
    
    Pozice pK = new Pozice(10, 150);
    rande.parPokracujeSpolecne(par, pK, chuzeSpolu);
    testPozice(new Pozice(10 + zena.getSirka(), 150), muz.getPozice(), "Rande končí muž");
    testPozice(pK, zena.getPozice(), "Rande končí žena");
    
    IO.zprava("Rande končí");
    
    rande.jdouDomu(par, chuzeSpolu);
    testPozice(pM, muz.getPozice(), "domaMuž");
    testPozice(pZ, zena.getPozice(), "domaŽena");
    
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Jsou obě osoby vykresleny správně?"));
 	  SP.odstranVse();
  }

////////////////////////////////////////////////////////////////////////////////  
// z DU-07 

  @Test
  public void testSetkaniMalaGeneral() {
    Pozice pM = new Pozice(10, 10);
    Pozice pZ = new Pozice(400, 150);
	  Osoba general = new Osoba(pM, 30, 1.0/5, 8.0/6, Barva.KHAKI);
	  Osoba zena = Osoba.getBeznaZena(pZ);
    rande = new Rande(general, zena);
    
    Presouvac chuzeNaRande = new Presouvac(10);
    Pozice pS = new Pozice(150, 80);
    rande.jdouNaRande(pS, chuzeNaRande);
    
    testPozice(new Pozice(220, 80), general.getPozice(), "generál");
    testPozice(new Pozice(150, 120), zena.getPozice(), "běžná žena");
    
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Jsou obě osoby vykresleny správně?"));
	  SP.odstranVse();
  }

  @Test
  public void testSetkaniVysokaTlusty() {
    Pozice pM = new Pozice(10, 10);
    Pozice pZ = new Pozice(400, 100);
	  Osoba tlusty = new Osoba(pM, 60, 1.0/1, 1.0/2, Barva.MODRA);
	  Osoba vysoka = new Osoba(pZ, 60, 1.0/2, 4.0/1, Barva.CERVENA);
    testPozice(pM, tlusty.getPozice(), "Vznik tlustý");
    testPozice(pZ, vysoka.getPozice(), "Vznik vysoká");
    rande = new Rande(tlusty, vysoka);
    
    Presouvac chuzeNaRande = new Presouvac(10);
    Pozice pS = new Pozice(150, 80);
    rande.jdouNaRande(pS, chuzeNaRande);
    
    testPozice(new Pozice(210, 140), tlusty.getPozice(), "tlustý");
    testPozice(new Pozice(150, 80), vysoka.getPozice(), "vysoká");
    
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Jsou obě osoby vykresleny správně?"));
	  SP.odstranVse();
  }

  @Test
  public void testCeleRandeGeneral() {
    Pozice pM = new Pozice(10, 10);
    Pozice pZ = new Pozice(400, 150);
	  Osoba general = new Osoba(pM, 30, 1.0/5, 8.0/6, Barva.KHAKI);
	  Osoba zena = Osoba.getBeznaZena(pZ);
    testPozice(pM, general.getPozice(), "Vznik generál");
    testPozice(pZ, zena.getPozice(), "Vznik žena");
    rande = new Rande(general, zena);
    
    Presouvac chuzeNaRande = new Presouvac(5);
    Pozice pS = new Pozice(150, 80);
    rande.jdouNaRande(pS, chuzeNaRande);
    
    Pozice pV = new Pozice(10, 10);
    Presouvac chuzeSpolu = new Presouvac(3);
    Par par = rande.parJdeSpolecne(pV, chuzeSpolu);
    testPozice(pV, par.getPozice(), "pár generál-žena");

    // prvni spolecna zastavka
    //     IO.zprava("První společná zastávka");
    testPozice(new Pozice(10 + zena.getSirka(), 10), general.getPozice(), "První společná zastávka generál");
    int rozdilVysek = general.getVyska() - zena.getVyska();
    testPozice(new Pozice(10, 10 + rozdilVysek), zena.getPozice(), "První společná zastávka žena");

    int podlahaGeneral = general.getPozice().y + general.getVyska();
    int podlahaZena = zena.getPozice().y + zena.getVyska();
    assertEquals("Těla jsou různě vysoko", 0, podlahaGeneral - podlahaZena);
    
    Pozice pD = new Pozice(350, 10);
    rande.parPokracujeSpolecne(par, pD, chuzeSpolu);
    Pozice pK = new Pozice(10, 150);
    rande.parPokracujeSpolecne(par, pK, chuzeSpolu);
    
    rande.jdouDomu(par, chuzeSpolu);
    testPozice(pM, general.getPozice(), "domaMuž");
    testPozice(pZ, zena.getPozice(), "domaŽena");
    
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Jsou obě osoby vykresleny správně?"));
	  SP.odstranVse();
  }
  
  @Test
  public void testCeleRandeSuperman() {
    Pozice pM = new Pozice(10, 10);
    Pozice pZ = new Pozice(400, 150);
	  Osoba muz = new Superman(pM);
	  Osoba zena = Osoba.getBeznaZena(pZ);
    rande = new Rande(muz, zena);
    
    Presouvac chuzeNaRande = new Presouvac(5);
    Pozice pS = new Pozice(150, 80);
    rande.jdouNaRande(pS, chuzeNaRande);
    
    Pozice pV = new Pozice(10, 10);
    Presouvac chuzeSpolu = new Presouvac(3);
    Par par = rande.parJdeSpolecne(pV, chuzeSpolu);
    testPozice(pV, par.getPozice(), "pár muž-žena");
    
    Pozice pD = new Pozice(350, 10);
    rande.parPokracujeSpolecne(par, pD, chuzeSpolu);
    Pozice pK = new Pozice(10, 150);
    rande.parPokracujeSpolecne(par, pK, chuzeSpolu);
    
    rande.jdouDomu(par, chuzeSpolu);
    testPozice(pM, muz.getPozice(), "domaMuž");
    testPozice(pZ, zena.getPozice(), "domaŽena");
    
    assertEquals("Nepotvrzená správnost ", true, IO.souhlas("Jsou obě osoby vykresleny správně?"));
	  SP.odstranVse();
  }

}
