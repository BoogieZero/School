package oks06;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class OsobniCisloTestNegativni {
	
	@Test
	public void testToString1() {
		OsobniCislo oc = new OsobniCislo("Novák, Josef, fav, 2014, Z, p");
		assertEquals("Chybný formát výpisu", "A14?xxxxP <= NOVÁK Josef"+" (" + Konstanty.TEXT_CHYBNY_FORMAT + ")" , oc.toString());
	}
	
	@Test
	public void testIsPlatneOsobniCislo() {
		OsobniCislo oc = new OsobniCislo(null);
		assertEquals("Číslo by nemělo být platné", false, oc.isPlatneOsobniCislo());
	}
	
	@Test
	public void testNaplnAtributy1() {
		OsobniCislo oc = new OsobniCislo("");
		assertEquals("Počet atributů je 0", false, oc.isPlatnyFormat());
	}
	
	@Test
	public void testNaplnAtributy2() {
		OsobniCislo oc = new OsobniCislo("Novák, Josef, fav, 2014, b, p, prebytek, prebytek");
		assertEquals("Neořezané přebytečné atributy",true, oc.isPlatnyFormat());
	}

	@Test
	public void testZpracujRokNastupu1() {
		OsobniCislo oc = new OsobniCislo("Novák, Josef, fav, 20145, b, p");
		assertEquals("Rok nástupu není čtyřmístný", false, oc.isPlatnyFormat());
	}
	
	@Test
	public void testZpracujRokNastupu2() {
		OsobniCislo oc = new OsobniCislo("Novák, Josef, fav, dsas, b, p");
		assertEquals("Rok nástupu není číslo", false, oc.isPlatnyFormat());
	}
}
