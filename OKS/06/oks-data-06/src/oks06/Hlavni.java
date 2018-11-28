package oks06;

public class Hlavni {
  public static void main(String[] args) {
    OsobniCislo oc = new OsobniCislo("Novák, Josef, fav, 2014, b, p");
    oc.generujOsobniCislo("0123");
    System.out.println(oc);
  }
}
