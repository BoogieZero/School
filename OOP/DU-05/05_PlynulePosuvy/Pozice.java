
/*******************************************************************************
 * Instance třídy {@code Pozice} představují přepravky uchovávající informace
 * o pozici objektu.
 * Proto jsou jejich atributy deklarovány jako veřejné konstanty.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public class Pozice
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Vodorovná souřadnice. */
    public final int x;

    /** Svislá souřadnice. */
    public final int y;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Vytvoří přepravku uchovávající zadané souřadnice.
     *
     * @param x  Vodorovná souřadnice
     * @param y  Svislá souřadnice
     */
    public Pozice( int x, int y )
    {
        this.x = x;
        this.y = y;
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Vrátí uloženou velikost vodorovné souřadnice.
     *
     * @return  Požadovaná hodnota
     */
    public int getX()
    {
        return x;
    }


    /***************************************************************************
     * Vrátí uloženou velikost svislé souřadnice.
     *
     * @return  Požadovaná hodnota
     */
    public int getY()
    {
        return y;
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vrátí informaci o tom, představuje-li instance zadaná v parametru
     * stejnou pozici.
     *
     * @param  o   Testovaná instance
     * @return Je-li zadaná instance pozicí se stejnými hodnotami atributů,
     *         vrátí {@code true}, jinak vrátí {@code false}.
     */
    @Override
    public boolean equals( Object o )
    {
        if( ! (o instanceof Pozice) ) {
            return false;               //==========>
        }
        Pozice p = (Pozice)o;
        return (p.x == x) && (p.y == y);
    }


    /***************************************************************************
     * Vrací textovou reprezentaci (podpis) dané instance
     * používanou především k ladicím účelům.
     *
     * @return Podpis dané instance
     */
    @Override
    public String toString()
    {
        return "Pozice[x=" + x + ", y=" + y + "]";
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTY A METODA MAIN =======================================================
//
//    /***************************************************************************
//     * Testovací metoda.
//     */
//    public static void test()
//    {
//        Pozice inst = new Pozice();
//    }
//    /** @param args Parametry příkazového řádku - nepoužívané. */
//    public static void main( String[] args )  {  test();  }
}
