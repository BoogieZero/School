package oks06;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class OsobniCisloTestPozitivniFinalni {
	OsobniCislo oc;
	
	@Before
	public void setUp() throws Exception {
		oc = new OsobniCislo("Novák, Josef, fav, 2014, b, p");
		oc.generujOsobniCislo("0123");
	}
	
	@Test
	public void testIsPlatneOsobniCislo() {
		assertEquals("Číslo by mělo být platné", true, oc.isPlatneOsobniCislo());
	}
}
