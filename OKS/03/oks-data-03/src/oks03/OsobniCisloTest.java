package oks03;

import static org.junit.Assert.*;

/**
 * Created by Boogie Zero on 16/03/2016.
 */
public class OsobniCisloTest {
    private OsobniCislo oc;

    @org.junit.Before
    public void setUp() throws Exception {
        oc = new OsobniCislo("Novák, Josef, fav, 2014, b, 0123, p, i");
    }

    @org.junit.Test
    public void testOsobniCislo() throws Exception{
        String fakulta = Konstanty.PLATNE_FAKULTY[0][1];
        String rokNastupu = "15";
        TypStudia typStudia = TypStudia.values()[0];
        String poradoveCislo = "0001";
        String formaStudia = Konstanty.PLATNE_FORMY_STUDIA[0];
        String nepovinne = "I";
        String prijmeni = "NOVÁK";
        String jmeno = "Josef";
        String vysledek = "A14B0001PI";
        boolean platnyFormat = true;
        OsobniCislo os = new OsobniCislo("Novák, Josef, fav, 2014, b, 0123, p, i");
        assertEquals(fakulta, os.fakulta);
        assertEquals(rokNastupu, os.rokNastupu);
        assertEquals(typStudia, os.typStudia);
        assertEquals(poradoveCislo, os.poradoveCislo);
        assertEquals(formaStudia, os.formaStudia);
        assertEquals(nepovinne, os.nepovinne);
        assertEquals(prijmeni, os.prijmeni);
        assertEquals(jmeno, os.jmeno);
        assertEquals(platnyFormat, os.platnyFormat);

        assertEquals(vysledek, os.vysledek);
    }

    @org.junit.Test
    public void testGetOsobniCislo() throws Exception {
        oc.vysledek = "ASDA651665119SFAF";
        assertEquals("ASDA651665119SFAF", oc.getOsobniCislo());
    }

    @org.junit.Test
    public void testIsPlatneOsobniCislo1() throws Exception {
        oc.vysledek = Konstanty.PRAZDNY;
        assertEquals(false, oc.isPlatneOsobniCislo());
    }

    @org.junit.Test
    public void testIsPlatneOsobniCislo2() throws Exception {
        oc.vysledek = "616561651156516";
        oc.platnyFormat = false;
        assertEquals(false, oc.isPlatneOsobniCislo());
    }

    @org.junit.Test
    public void testIsPlatnyFormat1() throws Exception {
        oc.platnyFormat = true;
        assertEquals(true, oc.isPlatnyFormat());
    }

    @org.junit.Test
    public void testIsPlatnyFormat2() throws Exception {
        oc.platnyFormat = false;
        assertEquals(false, oc.isPlatnyFormat());
    }

    @org.junit.Test
    public void testGetTypStudia1() throws Exception {
        oc.typStudia = TypStudia.values()[0];
        assertEquals(TypStudia.values()[0], oc.getTypStudia());
    }

    @org.junit.Test
    public void testGetTypStudia2() throws Exception {
        oc.typStudia = TypStudia.values()[1];
        assertEquals(TypStudia.values()[1], oc.getTypStudia());
    }

    @org.junit.Test
    public void testGetTypStudia3() throws Exception {
        oc.typStudia = TypStudia.values()[2];
        assertEquals(TypStudia.values()[2], oc.getTypStudia());
    }

    @org.junit.Test
    public void testGetTypStudia4() throws Exception {
        oc.typStudia = TypStudia.values()[3];
        assertEquals(TypStudia.values()[3], oc.getTypStudia());
    }

    @org.junit.Test
    public void testGetTypStudia5() throws Exception {
        oc.typStudia = TypStudia.values()[4];
        assertEquals(TypStudia.values()[4], oc.getTypStudia());
    }

    @org.junit.Test
    public void testGetFakulta1() throws Exception {
        oc.fakulta = Konstanty.PLATNE_FAKULTY[0][1];
        assertEquals(Konstanty.PLATNE_FAKULTY[0][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testGetFakulta2() throws Exception {
        oc.fakulta = Konstanty.PLATNE_FAKULTY[1][1];
        assertEquals(Konstanty.PLATNE_FAKULTY[1][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testGetFakulta3() throws Exception {
        oc.fakulta = Konstanty.PLATNE_FAKULTY[2][1];
        assertEquals(Konstanty.PLATNE_FAKULTY[2][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testGetFakulta4() throws Exception {
        oc.fakulta = Konstanty.PLATNE_FAKULTY[3][1];
        assertEquals(Konstanty.PLATNE_FAKULTY[3][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testGetFakulta5() throws Exception {
        oc.fakulta = Konstanty.PLATNE_FAKULTY[4][1];
        assertEquals(Konstanty.PLATNE_FAKULTY[4][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testGetFakulta6() throws Exception {
        oc.fakulta = Konstanty.PLATNE_FAKULTY[5][1];
        assertEquals(Konstanty.PLATNE_FAKULTY[5][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testGetFakulta7() throws Exception {
        oc.fakulta = Konstanty.PLATNE_FAKULTY[6][1];
        assertEquals(Konstanty.PLATNE_FAKULTY[6][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testGetFakulta8() throws Exception {
        oc.fakulta = Konstanty.PLATNE_FAKULTY[7][1];
        assertEquals(Konstanty.PLATNE_FAKULTY[7][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testGetFakulta9() throws Exception {
        oc.fakulta = Konstanty.PLATNE_FAKULTY[8][1];
        assertEquals(Konstanty.PLATNE_FAKULTY[8][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testZpracujPrijmeni1() throws Exception {
        oc.zpracujPrijmeni("PRIJMENI");
        assertEquals("PRIJMENI", oc.prijmeni);
    }

    @org.junit.Test
    public void testZpracujPrijmeni2() throws Exception {
        oc.zpracujPrijmeni("pri65156jmeni");
        assertEquals(Konstanty.ZNAK_CHYBY, oc.prijmeni);
        assertEquals(false, oc.platnyFormat);
    }

    @org.junit.Test
    public void testZpracujPrijmeni3() throws Exception {
        oc.zpracujPrijmeni("prijmeni");
        assertEquals(Konstanty.ZNAK_CHYBY, oc.prijmeni);
        assertEquals("PRIJMENI", oc.platnyFormat);
    }

    @org.junit.Test
    public void testZpracujJmeno1() throws Exception {
        oc.zpracujJmeno("Jmeno");
        assertEquals("Jmeno", oc.jmeno);
    }

    @org.junit.Test
    public void testZpracujJmeno2() throws Exception {
        oc.zpracujJmeno("jmeno");
        assertEquals(Konstanty.ZNAK_CHYBY, oc.jmeno);
        assertEquals(false, oc.platnyFormat);
    }

    @org.junit.Test
    public void testZpracujRokNastupu1() throws Exception {
        oc.zpracujRokNastupu("2015");
        assertEquals("15", oc.rokNastupu);
    }

    @org.junit.Test
    public void testZpracujRokNastupu2() throws Exception {
        oc.zpracujRokNastupu("-2015");
        assertEquals(Konstanty.ZNAK_CHYBY, oc.rokNastupu);
        assertEquals(false, oc.platnyFormat);
    }

    @org.junit.Test(expected = NumberFormatException.class)
    public void testZpracujRokNastupu3() throws Exception {
        oc.zpracujRokNastupu("FASjnsa");
    }

    @org.junit.Test
    public void testChybnyRokNastupu() throws Exception {
        oc.chybnyRokNastupu();
        assertEquals(Konstanty.ZNAK_CHYBY, oc.rokNastupu);
        assertEquals(false, oc.platnyFormat);
    }

    @org.junit.Test
    public void testZpracujFakulta2() throws Exception {
        oc.zpracujFakulta(Konstanty.PLATNE_FAKULTY[0][0]);
        assertEquals(Konstanty.PLATNE_FAKULTY[0][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testZpracujFakulta3() throws Exception {
        oc.zpracujFakulta(Konstanty.PLATNE_FAKULTY[1][0]);
        assertEquals(Konstanty.PLATNE_FAKULTY[1][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testZpracujFakulta4() throws Exception {
        oc.zpracujFakulta(Konstanty.PLATNE_FAKULTY[2][0]);
        assertEquals(Konstanty.PLATNE_FAKULTY[2][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testZpracujFakulta5() throws Exception {
        oc.zpracujFakulta(Konstanty.PLATNE_FAKULTY[3][0]);
        assertEquals(Konstanty.PLATNE_FAKULTY[3][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testZpracujFakulta6() throws Exception {
        oc.zpracujFakulta(Konstanty.PLATNE_FAKULTY[4][0]);
        assertEquals(Konstanty.PLATNE_FAKULTY[4][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testZpracujFakulta7() throws Exception {
        oc.zpracujFakulta(Konstanty.PLATNE_FAKULTY[5][0]);
        assertEquals(Konstanty.PLATNE_FAKULTY[5][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testZpracujFakulta8() throws Exception {
        oc.zpracujFakulta(Konstanty.PLATNE_FAKULTY[6][0]);
        assertEquals(Konstanty.PLATNE_FAKULTY[6][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testZpracujFakulta9() throws Exception {
        oc.zpracujFakulta(Konstanty.PLATNE_FAKULTY[7][0]);
        assertEquals(Konstanty.PLATNE_FAKULTY[7][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testZpracujFakulta10() throws Exception {
        oc.zpracujFakulta(Konstanty.PLATNE_FAKULTY[8][0]);
        assertEquals(Konstanty.PLATNE_FAKULTY[8][1], oc.getFakulta());
    }

    @org.junit.Test
    public void testZpracujFakulta1() throws Exception {
        oc.zpracujFakulta("");
        assertEquals(Konstanty.ZNAK_CHYBY, oc.getFakulta());
        assertEquals(false, oc.platnyFormat);
    }

    @org.junit.Test
    public void testZpracujTypStudia2() throws Exception {
        oc.zpracujTypStudia(TypStudia.values()[0].getZkratka());
        assertEquals(TypStudia.values()[0], oc.typStudia);
    }

    @org.junit.Test
    public void testZpracujTypStudia3() throws Exception {
        oc.zpracujTypStudia(TypStudia.values()[1].getZkratka());
        assertEquals(TypStudia.values()[1], oc.typStudia);
    }

    @org.junit.Test
    public void testZpracujTypStudia4() throws Exception {
        oc.zpracujTypStudia(TypStudia.values()[2].getZkratka());
        assertEquals(TypStudia.values()[2], oc.typStudia);
    }

    @org.junit.Test
    public void testZpracujTypStudia5() throws Exception {
        oc.zpracujTypStudia(TypStudia.values()[3].getZkratka());
        assertEquals(TypStudia.values()[3], oc.typStudia);
    }

    @org.junit.Test
    public void testZpracujTypStudia1() throws Exception {
        oc.zpracujTypStudia(TypStudia.values()[4].getZkratka());
        assertEquals(TypStudia.values()[4], oc.typStudia);
        assertEquals(false, oc.platnyFormat);
    }



    @org.junit.Test
    public void testZpracujFormaStudia1() throws Exception {
        oc.zpracujFormaStudia(Konstanty.PLATNE_FORMY_STUDIA[0]);
        assertEquals(Konstanty.PLATNE_FORMY_STUDIA[0], oc.formaStudia);
    }
    @org.junit.Test
    public void testZpracujFormaStudia2() throws Exception {
        oc.zpracujFormaStudia(Konstanty.PLATNE_FORMY_STUDIA[1]);
        assertEquals(Konstanty.PLATNE_FORMY_STUDIA[1], oc.formaStudia);
    }
    @org.junit.Test
    public void testZpracujFormaStudia3() throws Exception {
        oc.zpracujFormaStudia(Konstanty.PLATNE_FORMY_STUDIA[2]);
        assertEquals(Konstanty.PLATNE_FORMY_STUDIA[2], oc.formaStudia);
    }

    @org.junit.Test
    public void testZpracujFormaStudia4() throws Exception {
        oc.zpracujFormaStudia("fsdafagag");
        assertEquals(Konstanty.ZNAK_CHYBY, oc.formaStudia);
        assertEquals(false, oc.platnyFormat);
    }

    @org.junit.Test
    public void testZpracujNepovinne1() throws Exception {
        oc.zpracujNepovinne("fasdfs");
        assertEquals("fasdfs",oc.nepovinne);
    }

    @org.junit.Test
    public void testZpracujNepovinne2() throws Exception {
        oc.zpracujNepovinne(null);
        assertEquals("",oc.nepovinne);
    }
}