/*******************************************************************************
 * Instance třídy {@code Oblast} představují přepravky uchovávající
 * informace o pozici a rozměru objektu.
 * Proto jsou jejich atributy deklarovány jako veřejné konstanty.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public class Oblast
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Vodorovná souřadnice dané oblasti, tj. jejího levého horního rohu. */
    public final int x;

    /** Svislá souřadnice dané oblasti, tj. jejího levého horního rohu. */
    public final int y;

    /** Šířka oblasti. */
    public final int sirka;

    /** Výška oblasti. */
    public final int vyska;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Vytvoří oblast se zadaným umístěním a rozměry.
     *
     * @param x     Vodorovná souřadnice oblasti, tj. jejího levého horního rohu
     * @param y     Svislá souřadnice oblasti, tj. jejího levého horního rohu
     * @param sirka Šířka oblasti
     * @param vyska Výška oblasti
     */
    public Oblast( int x, int y, int sirka, int vyska )
    {
        this.x     = x;
        this.y     = y;
        this.sirka = sirka;
        this.vyska = vyska;
    }


    /***************************************************************************
     * Vytvoří oblast se zadaným umístěním a rozměry.
     *
     * @param pozice  Pozice oblasti, tj. pozice jejího levého horního rohu
     * @param rozmer  Rozměr vytvářené oblasti
     */
    public Oblast( Pozice pozice, Rozmer rozmer )
    {
        this( pozice.x, pozice.y, rozmer.sirka, rozmer.vyska );
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Vrátí hodnotu vodorovné souřadnice oblasti.
     *
     * @return  Vodorovná souřadnice
     */
    public int getX()
    {
        return x;
    }


    /***************************************************************************
     * Vrátí hodnotu svislé souřadnice oblasti.
     *
     * @return  Svislá souřadnice
     */
    public int getY()
    {
        return y;
    }


    /***************************************************************************
     * Vrátí pozici oblasti.
     *
     * @return  Pozice oblasti
     */
    public Pozice getPozice()
    {
        return new Pozice ( x, y );
    }


    /***************************************************************************
     * Vrátí velikost šířky oblasti.
     *
     * @return  Šířka oblasti
     */
    public int getSirka()
    {
        return sirka;
    }


    /***************************************************************************
     * Vrátí velikost výšky oblasti.
     *
     * @return  Výška oblasti
     */
    public int getVyska()
    {
        return vyska;
    }


    /***************************************************************************
     * Vrátí rozměr oblasti.
     *
     * @return  Rozměr oblasti
     */
    public Rozmer getRozmer()
    {
        return new Rozmer (sirka, vyska);
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vrátí informaci o tom, představuje-li instance zadaná v parametru
     * stejnou oblast.
     *
     * @param  o   Testovaná instance
     * @return Je-li zadaná instance oblastí se stejnými hodnotami atributů,
     *         vrátí {@code true}, jinak vrátí {@code false}.
     */
    @Override
    public boolean equals( Object o )
    {
        if( ! (o instanceof Oblast) ) {
            return false;               //==========>
        }
        Oblast druha = (Oblast)o;
        return (druha.x     == x)     && (druha.y     == y)    &&
               (druha.sirka == sirka) && (druha.vyska == vyska);
    }


    /***************************************************************************
     * Vrací textovou reprezentaci (podpis) dané instance.
     * Používá se především k ladicím účelům.
     *
     * @return Požadovaná textová reprezentace.
     */
    @Override
    public String toString()
    {
        return "Oblast:[x=" + x + ", y=" + y +
               ", sirka=" + sirka + ", vyska=" + vyska + "]";
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTY A METODA MAIN =======================================================
}
