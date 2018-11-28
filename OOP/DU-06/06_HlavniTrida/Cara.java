/*******************************************************************************
 * Třída pro prací s čárou komunikující s aktivním plátnem.
 * Čára je určena svými krajními body, přičemž souřadnice počátečního bodu
 * je současně považována za pozici celé instance.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public class Cara implements IPosuvny, IKopirovatelny
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Počáteční barva nakreslené instance v případě,
     *  kdy uživatel žádnou požadovanou barvu nezadá -
     *  pro čáru Barva.ČERNÁ. */
    public static final Barva IMPLICITNI_BARVA = Barva.CERNA;

    /** Plátno, na které se bude instance kreslit. */
    private static final SpravcePlatna SP = SpravcePlatna.getInstance(false);



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Počet vytvořených instancí */
    private static int pocet = 0;



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Pořadí vytvoření dané instance v rámci třídy. */
    private final int poradi = ++pocet;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    /** Název instance sestávající implicitně
     *  z jednoduchého názvu třídy následovaného potržítkem a
     *  pořadím vytvoření instance v rámci instancí této třídy. */
    private String nazev = "Cara_" + poradi;

    /** Bodová x-ová souřadnice instance, tj. souřadnice počátku. */
    private int xPos;

    /** Bodová y-ová souřadnice instance, tj. souřadnice počátku. */
    private int yPos;

    /** Bodová x-ová souřadnice konce. */
    protected int kx;

    /** Bodová y-ová souřadnice konce. */
    protected int ky;

    /** Barva instance. */
    private Barva barva;



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Připraví novou instanci s implicitním umístěním, rozměry a barvou.
     * Instance bude umístěna v levém horním rohu plátna
     * a bude mít implicitní barvu,
     * Končit bude ve středu plátna.
     */
    public Cara()
    {
        this( 0, 0, SP.getSloupcu() * SP.getKrok() / 2,
                    SP.getRadku()   * SP.getKrok() / 2);
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí a rozměry
     * a implicitní barvou.
     *
     * @param x   Vodorovná (x-ová) souřadnice instance (jejího počátku),
     *            x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y   Svislá (y-ová) souřadnice instance (jejího počátku),
     *            y=0 má horní okraj plátna, souřadnice roste dolů
     * @param kx  x-ová souřadnice koncového bodu instance
     * @param ky  y-ová souřadnice koncového bodu instance
     */
    public Cara(int x, int y, int kx, int ky)
    {
        this( x, y, kx, ky, IMPLICITNI_BARVA );
    }


    /***************************************************************************
     * Připraví instanci se zadanou pozicí a velikostí a implicitní barvou.
     * Pozice instance je definována pozicí jejího počátečního bodu.
     *
     * @param start Pozice počátečního bodu
     * @param stop  Pozice koncového bodu
     */
    public Cara(Pozice start, Pozice stop)
    {
        this(start.x, start.y, stop.x, stop.y, IMPLICITNI_BARVA);
    }


    /***************************************************************************
     * Připraví instanci se zadanou pozicí, velikostí a barvou.
     * Pozice instance je definována pozicí jejího počátečního bodu.
     *
     * @param start Pozice počátečního bodu
     * @param stop  Pozice koncového bodu
     * @param barva Barva instance
     */
    public Cara(Pozice start, Pozice stop, Barva barva)
    {
        this(start.x, start.y, stop.x, stop.y, barva);
    }



    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí, rozměry a barvou.
     *
     * @param x   Vodorovná (x-ová) souřadnice instance (jejího počátku),
     *            x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y   Svislá (y-ová) souřadnice instance (jejího počátku),
     *            y=0 má horní okraj plátna, souřadnice roste dolů
     * @param kx      x-ová souřadnice koncového bodu instance
     * @param ky      y-ová souřadnice koncového bodu instance
     * @param barva   Barva vytvářené instance
     */
    public Cara(int x, int y, int kx, int ky, Barva barva)
    {
//        //Test platnosti parametru
//        if( (x<0) || (y<0) || (kx<0) || (ky<0) ) {
//            throw new IllegalArgumentException(
//                "\nParametry nemají povolené hodnoty: x="
//                + x + ", y=" + y + ", kx=" + kx + ", ky=" + ky );
//        }

        //Parametry akceptovány --> můžeme tvořit
        this.xPos  = x;
        this.yPos  = y;
        this.kx    = kx;
        this.ky    = ky;
        this.barva = barva;
    }


    /***************************************************************************
     * Vrátí kopii daného tvaru,
     * tj. stejný tvar, stejně velký, stejně umístěný a se stejnou barvou.
     *
     * @return Požadovaná kopie
     */
    @Override
    public Cara kopie()
    {
        return new Cara(xPos, yPos, kx, ky, barva);
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Vrátí x-ovou (vodorovnou) souřadnici pozice instance (jejího počátku).
     *
     * @return  Aktuální vodorovná (x-ová) souřadnice instance,
     *          x=0 má levý okraj plátna, souřadnice roste doprava
     */
    public int getX()
    {
        return xPos;
    }


    /***************************************************************************
     * Vrátí y-ovou (svislou) souřadnici pozice instance (jejího počátku).
     *
     * @return  Aktuální svislá (y-ová) souřadnice instance,
     *          y=0 má horní okraj plátna, souřadnice roste dolů
     */
    public int getY()
    {
        return yPos;
    }


    /***************************************************************************
     * Vrátí instanci třídy {@code Pozice} s aktuální pozicí instance.
     *
     * @return  Instance třídy {@code Pozice} s aktuální pozicí instance
     */
    @Override
    public Pozice getPozice()
    {
        return new Pozice( getX(), getY() );
    }


    /***************************************************************************
     * Nastaví novou pozici instance.
     *
     * @param pozice   Nastavovaná pozice instance
     */
    @Override
    public void setPozice(Pozice pozice)
    {
        setPozice( pozice.x, pozice.y );
    }


    /***************************************************************************
     * Nastaví novou pozici instance.
     *
     * @param x  Nově nastavovaná vodorovná (x-ová) souřadnice instance,
     *           x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y  Nově nastavovaná svislá (y-ová) souřadnice instance,
     *           y=0 má horní okraj plátna, souřadnice roste dolů
     */
    @Override
    public void setPozice(int x, int y)
    {
        SP.nekresli(); {
            kx   = x  +  (kx - xPos);
            ky   = y  +  (ky - yPos);
            xPos = x;
            yPos = y;
            SP.prekresli();
        } SP.vratKresli();
    }


    /***************************************************************************
     * Vrátí x-ovou souřadnici koncového bodu instance.
     *
     * @return  Aktuální vodorovná (x-ová) souřadnice koncového bodu instance,
     *          x=0 má levý okraj plátna, souřadnice roste doprava
     */
    public int getKX()
    {
        return kx;
    }


    /***************************************************************************
     * Vrátí y-ovou souřadnici koncového bodu instance.
     *
     * @return  Aktuální svislá (y-ová) souřadnice koncového bodu instance,
     *          y=0 má horní okraj plátna, souřadnice roste dolů
     */
     public int getKY()
     {
         return ky;
     }


    /***************************************************************************
     * Vrátí pozici koncového bodu instance.
     *
     * @return  Instance třídy {@code Pozice} s aktuální pozicí koncového bodu
     */
     public Pozice getKPozice()
     {
         return new Pozice( getKX(), getKY() );
     }


    /***************************************************************************
     * Nastaví pozici koncového bodu instance.
     *
     * @param kpozice  Pozice koncového bodu.
     */
     public void setKPozice( Pozice kpozice )
     {
         setPozice( kpozice.x, kpozice.y );
     }


    /***************************************************************************
     * Nastaví pozici koncového bodu instance.
     *
     * @param kx  Vodorovná souřadnice koncového bodu.
     * @param ky  Svislá souřadnice koncového bodu.
     */
     public void setKPozice( int kx, int ky )
     {
         this.kx = kx;
         this.ky = ky;
         SP.prekresli();
     }


    /***************************************************************************
     * Vrátí barvu instance.
     *
     * @return Instance třídy {@code Barva} definující aktuálně nastavenou barvu
     */
    public Barva getBarva()
    {
        return barva;
    }


    /***************************************************************************
     * Nastaví novou barvu instance.
     *
     * @param nova   Požadovaná nová barva
     */
    public void setBarva(Barva nova)
    {
        barva = nova;
        SP.prekresli();
    }


    /***************************************************************************
     * Vrátí název instance, implicitně název její třídy následovaný
     * pořadím vytvoření instance v rámci instancí této třídy.
     *
     * @return  Řetězec s názvem instance
     */
    public String getNazev()
    {
        return nazev;
    }


    /***************************************************************************
     * Nastaví nový název instance.
     *
     * @param nazev  Řetězec s novým názvem instance
     */
    public void setNazev(String nazev)
    {
        this.nazev = nazev;
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vrátí podpis instance, tj. její řetězcovou reprezentaci.
     * Používá se především při ladění.
     *
     * @return Řetězcová reprezentace (podpis) dané instance
     */
    @Override
    public String toString()
    {
        return nazev + "_(x=" + xPos + ",y=" + yPos  +
                      ", kx=" + kx + ", ky=" + ky +
                      ",barva=" + barva + ")";
    }


    /***************************************************************************
     * Prostřednictvím dodaného kreslítka vykreslí obraz své instance.
     *
     * @param kreslitko Kreslítko, které nakreslí instanci
     */
    @Override
    public void nakresli( Kreslitko kreslitko )
    {
        kreslitko.kresliCaru( getX(), getY(), kx, ky, getBarva() );
    }


    /***************************************************************************
     * Přesune se tak, aby spojila zadané body.
     *
     * @param x1      x-ová souřadnice počátku, x&gt;=0, x=0 má levý okraj plátna
     * @param y1      y-ová souřadnice počátku, y&gt;=0, y=0 má horní okraj plátna
     * @param kx      x-ová souřadnice koncového bodu instance
     * @param ky      y-ová souřadnice koncového bodu instance
     */
    public void spoj( int x1, int y1, int kx, int ky )
    {
        setPozice( x1, y1 );
        this.kx   = kx;
        this.ky   = ky;
        SP.prekresli();
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTOVACÍ METODY A TŘÍDY ==================================================
}
