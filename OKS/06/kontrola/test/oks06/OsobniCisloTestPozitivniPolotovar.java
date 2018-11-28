package oks06;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OsobniCisloTestPozitivniPolotovar {
	OsobniCislo oc;
	
	@Before
	public void setUp() throws Exception {
		oc = new OsobniCislo("Novák, Josef, fav, 2014, b, p");
	}

	@Test
	public void testCompareTo1() {
		assertEquals("Je první", 1, oc.compareTo(new OsobniCislo("Aovák, Josef, fav, 2014, b, p")));
	}
	
	@Test
	public void testCompareTo2() {
		assertEquals("Je druhý", -1, oc.compareTo(new OsobniCislo("Zovák, Josef, fav, 2014, b, p")));
	}
	
	@Test
	public void testCompareTo3() {
		assertEquals("Je první", 1, oc.compareTo(new OsobniCislo("Novák, Aosef, fav, 2014, b, p")));
	}
	
	@Test
	public void testCompareTo4() {
		assertEquals("Je druhý", -1, oc.compareTo(new OsobniCislo("Novák, Zosef, fav, 2014, b, p")));
	}
	
	@Test
	public void testCompareTo5() {
		assertEquals("Jsou stejní", 0, oc.compareTo(new OsobniCislo("Novák, Josef, fav, 2014, b, p")));
	}

	@Test
	public void testToString1() {
		oc.vysledek = "123";
		oc.prijmeni = "456";
		oc.jmeno = "789";
		assertEquals("Chybný formát výpisu", "123 <= 456 789" , oc.toString());
	}

	@Test
	public void testGetOsobniCislo() {
		oc.vysledek = "789";
		assertEquals("Chybné osobní číslo ", "789", oc.getOsobniCislo());
	}
	
	@Test
	public void testIsPlatneOsobniCislo() {
		assertEquals("Číslo by nemělo být platné", false, oc.isPlatneOsobniCislo());
	}
	
	@Test
	public void testIsPlatnyFormat() {
		assertEquals("Formát by měl být platný", true, oc.isPlatnyFormat());
	}

	@Test
	public void testGetTypStudia() {
		assertEquals("Očekávaný typ studia bakalářský", TypStudia.BAKALARSKY, oc.getTypStudia());
	}

	@Test
	public void testGetFakulta() {
		oc.fakulta = "789";
		assertEquals("Očekávaná fakulta 789", "789", oc.getFakulta());
	}

}
