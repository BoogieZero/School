/***************************************************************
 * Třída {@code Hlavni} je hlavní třídou projektu,
 * který zrealizuje seznamku
 *
 * @author    Pavel Herout
 * @version   2.00.000
 */ 
public class Hlavni {
  /***********************************************************
   * Metoda, prostřednictvím níž se spouští celá aplikace.
   *
   * @param args rychlost přesunů
   */
  public static void main(String[] args) {
    Seznamka seznamka = Seznamka.getSeznamka();
    Pozice vznik = new Pozice(0, 0);
    Osoba m1 = Osoba.getBeznyMuz(vznik);
    Osoba m2 = new Superman();
	  Osoba m3 = new Osoba(vznik, 60, 1.0/1, 1.0/2, Barva.MODRA);
    Osoba z1 = Osoba.getBeznaZena(vznik);
    Osoba z2 = new Superwoman();
	  Osoba z3 = new Osoba(vznik, 60, 1.0/2, 4.0/1, Barva.CERVENA);

    seznamka.setSrazMuzu(vznik);
    seznamka.pridejMuze(m1);
    seznamka.pridejMuze(m2);
    seznamka.pridejMuze(m3);
    
    Pozice srazZen = new Pozice(350, 100);
    seznamka.setSrazZen(srazZen);
    seznamka.pridejZenu(z1);
    seznamka.pridejZenu(z2);
    seznamka.pridejZenu(z3);
    
    seznamka.setPresouvac(10);
    seznamka.setMistoSchuzky(new Pozice(150, 50));
    
    seznamka.realizujSchuzky(new Pozice(10, 150), 300);
  }
}
