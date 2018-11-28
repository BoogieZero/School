/**
 * Třída {@code Přesouvač} slouží k plynulému přesouvání instancí tříd
 * implementujicich rozhraní {@link IPosuvný}.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public class Presouvac
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Počet milisekund mezi dvěma překresleními objektu. */
    private static final int PERIODA = 50;



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Počet vytvořených instancí */
    private static int pocet = 0;



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

//     /** Identifikační kód (rodné číslo) instance. */
//     private final int ID = ++pocet;

    /** Název sestávající z názvu třídy a pořadí instance */
    private final String nazev;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    /** Specifikuje rychlost posunu objektu daným posunovačem. */
    private int rychlost;



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Vytvoří přesouvače, který bude přesouvat objekty rychlosti 1.
     */
    public Presouvac()
    {
        this( 1 );
    }


    /***************************************************************************
     * Vytvoří přesouvače, který bude přesouvat objekty zadanou rychlostí.
     *
     * @param rychlost Rychlost, kterou bude přesouvač pohybovat
     *                 se svěřenými objekty.
     */
    public Presouvac( int rychlost )
    {
        if( rychlost <= 0 ) {
            throw new IllegalArgumentException(
                "Zadana rychlost musi byt kladna!" );
        }
        this.rychlost = rychlost;
        this.nazev    = getClass().getName() + "(ID=" + pocet +
                        ",rychlost=" + rychlost + ")";
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================
//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Metoda převádí instanci na řetězec - používá se pro účely ladění.
     * Vrácený řetězec obsahuje název třídy následovaný identifikačním číslem
     * dané instance, které určuje pořadí dané instance mezi instancemi této
     * třídy, a nastavenou rychlostí přesunu.
     *
     * @return  Řetězec charakterizující instanci
     */
    @Override
    public String toString()
    {
        return nazev;
    }


    /***************************************************************************
     * Plynule přesune zadaný objekt o zadaný počet obrazových bodů.
     *
     * @param doprava   Počet bodů, o než se objekt přesune doprava
     * @param dolu      Počet bodů, o než se objekt přesune dolů
     * @param objekt    Přesouvaný objekt
     */
    public void presunO(int doprava, int dolu, IPosuvny objekt)
    {
        double vzdalenost = Math.sqrt(doprava*doprava + dolu*dolu);
        int    kroku      = (int)(vzdalenost / rychlost);
      // doplnil P.Herout
        if (kroku == 0) {
          kroku = 1;
        }
      // konec doplneni
        double dx = (doprava+.4) / kroku;
        double dy = (dolu   +.4) / kroku;
        Pozice pozice = objekt.getPozice();
        double x  = pozice.x + .4;
        double y  = pozice.y + .4;

        for (int i=kroku;   i > 0;   i--)  {
            x = x + dx;
            y = y + dy;
            objekt.setPozice( (int)x, (int)y );
            IO.cekej(PERIODA);
        }
    }


    /***************************************************************************
     * Plynule přesune zadaný objekt o zadaný počet obrazových bodů.
     *
     * @param posun  Velikost posunu v jednotlivých směrech
     * @param objekt Přesouvaný objekt
     */
    public void presunO(Pozice posun, IPosuvny objekt)
    {
        presunO(posun.x, posun.y, objekt);
    }


    /***************************************************************************
     * Plynule přesune zadaný objekt do požadované pozice.
     *
     * @param x       x-ova souřadnice požadované cílové pozice
     * @param y       y-ova souřadnice požadované cílové pozice
     * @param objekt  Přesouvaný objekt
     */
    public void presunNa(int x, int y, IPosuvny objekt)
    {
        Pozice pozice = objekt.getPozice();
        presunO(x-pozice.x, y-pozice.y, objekt);
    }


    /***************************************************************************
     * Plynule přesune zadaný objekt do požadované pozice.
     *
     * @param pozice  Cílová pozice
     * @param objekt  Přesouvaný objekt
     */
    public void presunNa(Pozice pozice, IPosuvny objekt)
    {
        presunNa(pozice.x, pozice.y, objekt);
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTY A METODA MAIN =======================================================
}
