/*******************************************************************************
 * Instance třídy {@code Obdelnik} představují obdélníky
 * určené pro práci na virtuálním plátně
 * při prvním seznámení s třídami a objekty
 * a definované svojí pozicí, rozměrem a barvou.
 * Pozicí instance se přitom rozumí
 * pozice jejího levého horního rohu.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public class Obdelnik
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Počáteční barva nakreslené instance v případě,
     *  kdy uživatel žádnou požadovanou barvu nezadá -
     *  pro obdélník {@code Barva.ČERVENÁ}. */
    public static final Barva IMPLICITNI_BARVA = Barva.CERVENA;

    /** Maximální povolená velikost kroku. */
    public static final int MAX_KROK = 100;

    /** Plátno, na které se bude instance kreslit. */
    private static final Platno PLATNO = Platno.getPlatno();



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Počet pixelů, o něž se instance posune
     *  po bezparametrickém posunovém povelu */
    private static int krok = 50;

    /** Počet vytvořených instancí */
    private static int pocet = 0;



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Pořadí vytvoření dané instance v rámci třídy. */
    private final int poradi = ++pocet;

    /** Název sestávající z názvu třídy a pořadí instance */
    private final String nazev = "Obdelnik_" + poradi;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    /** Bodová x-ová souřadnice instance. */
    private int xPos;

    /** Bodová y-ová souřadnice instance. */
    private int yPos;

    /** Šířka v bodech. */
    protected int sirka;

    /** Výška v bodech. */
    protected int vyska;

    /** Barva instance. */
    private Barva barva;



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================

    /***************************************************************************
     * Vrátí velikost implicitního kroku, o který se instance přesune
     * při volaní bezparametrickych metod přesunu.
     *
     * @return Velikost implicitního kroku v bodech
     */
     public static int getKrok()
     {
         return krok;
     }


    /***************************************************************************
     * Nastaví velikost implicitního kroku, o který se instance přesune
     * při volaní bezparametrickych metod přesunu.
     *
     * @param velikost  Velikost implicitního kroku v bodech;<br>
     *                  musí platit:  0 &lt;= velikost &lt;= {@link #MAX_KROK}
     */
    public static void setKrok( int velikost )
    {
        if( (velikost < 0)  || (velikost > MAX_KROK) ) {
            throw new IllegalArgumentException(
                "\nKrok musi byt z intervalu <0;" + MAX_KROK + ">." );
        }
        krok = velikost;
    }



//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Připraví novou instanci s implicitním umístěním, rozměry a barvou.
     * Instance bude umístěna v levém horním rohu plátna
     * a bude mít implicitní barvu,
     * výšku rovnu kroku a šířku dvojnásobku kroku (tj. implicitně 50x100 bodů).
     */
    public Obdelnik()
    {
        this( 0, 0, 2*krok, krok );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí a rozměry
     * a implicitní barvou.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param sirka   Šířka vytvářené instance,  šířka &gt; 0
     * @param vyska   Výška vytvářené instance,  výška &gt; 0
     */
    public Obdelnik( int x, int y, int sirka, int vyska )
    {
        this( x, y, sirka, vyska, IMPLICITNI_BARVA );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí, rozměry a barvou.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param sirka   Šířka vytvářené instance,  šířka &gt; 0
     * @param vyska   Výška vytvářené instance,  výška &gt; 0
     * @param barva   Barva vytvářené instance
     */
    public Obdelnik( int x, int y, int sirka, int vyska, Barva barva )
    {
        //Test platnosti parametru
        if ((sirka<=0) || (vyska<=0)) {
            throw new IllegalArgumentException(
                "\nnew Obdelnik - Parametry nemaji povolene hodnoty: x="
                + x + ", y=" + y + ", sirka=" + sirka + ", vyska=" + vyska );
        }

        //Parametry akceptovány --> můžeme tvořit
        this.xPos  = x;
        this.yPos  = y;
        this.sirka = sirka;
        this.vyska = vyska;
        this.barva = barva;
        nakresliPrivate();
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Vrátí x-ovou (vodorovnou) souřadnici pozice instance,
     * tj. vodorovnou souřadnici jejího levého horního rohu.
     *
     * @return  Aktuální vodorovná (x-ová) souřadnice instance,
     *          x=0 má levý okraj plátna, souřadnice roste doprava
     */
    public int getX()
    {
        return xPos;
    }


    /***************************************************************************
     * Vrátí y-ovou (svislou) souřadnici pozice instance,
     * tj. svislou souřadnici jejího levého horního rohu.
     *
     * @return  Aktuální svislá (y-ová) souřadnice instance,
     *          y=0 má horní okraj plátna, souřadnice roste dolů
     */
    public int getY()
    {
        return yPos;
    }


    /***************************************************************************
     * Nastaví novou pozici instance.
     * Pozice instance jsou přitom definovány jako pozice
     * jeho levého horního rohu.
     *
     * @param x  Nově nastavovaná vodorovná (x-ová) souřadnice instance,
     *           x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y  Nově nastavovaná svislá (y-ová) souřadnice instance,
     *           y=0 má horní okraj plátna, souřadnice roste dolů
     */
    public void setPozice(int x, int y)
    {
        smaz();
        xPos = x;
        yPos = y;
        nakresli();
    }


    /***************************************************************************
     * Vrátí šířku instance v bodech.
     *
     * @return  Aktuální šířka instance v bodech
     */
     public int getSirka()
     {
         return sirka;
     }


    /***************************************************************************
     * Vrátí výšku instance v bodech.
     *
     * @return  Aktuální výška instance v bodech
     */
     public int getVyska()
     {
         return vyska;
     }


    /***************************************************************************
     * Nastaví nový "čtvercový" rozměr instance -
     * na zadaný rozměr se nastaví výška i šířka.
     * Nastavované rozměry musí být nezáporné,
     * místo nulového rozměru se nastaví rozměr rovný jedné.
     *
     * @param rozmer  Nově nastavovaný rozměr v obou směrech; rozměr &gt; 0
     */
    public void setRozmer(int rozmer)
    {
        setRozmer( rozmer, rozmer );
    }


    /***************************************************************************
     * Nastaví nové rozměry instance.
     * Nastavované rozměry musí být nezáporné,
     * místo nulového rozměru se nastaví rozměr rovný jedné.
     *
     * @param sirka    Nově nastavovaná šířka; šířka &gt;= 0
     * @param vyska    Nově nastavovaná výška; výška &gt;= 0
     */
    public void setRozmer(int sirka, int vyska)
    {
        if ((sirka < 0) || (vyska < 0)) {
            throw new IllegalArgumentException(
                                    "Rozmery musi byt nezaporne: sirka=" +
                                    sirka + ", vyska=" + vyska);
        }
        smaz();
        this.sirka = Math.max(1, sirka);
        this.vyska = Math.max(1, vyska);
        nakresli();
    }


    /***************************************************************************
     * Vrátí aktuální barvu instance.
     *
     * @return Instance třídy {@link Barva} definující aktuálně nastavenou barvu
     */
    public Barva getBarva()
    {
        return barva;
    }


    /***************************************************************************
     * Nastaví novou barvu instance.
     *
     * @param nova  Požadovaná nová barva
     */
    public void setBarva(Barva nova)
    {
        if ( nova != Barva.ZADNA) {
            barva = nova;
            nakresli();
        }
    }


    /***************************************************************************
     * Vrátí název instance, tj. název její třídy následovaný
     * pořadím vytvoření instance v rámci instancí této třídy.
     *
     * @return  Řetězec s názvem instance
     */
    public String getNazev()
    {
        return nazev;
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vrátí podpis instance, tj. její řetězcovou reprezentaci.
     * Používá se především při ladění.
     *
     * @return Název instance následovaný jejími souřadnicemi,
     *         rozměry a barvou
     */
    @Override
    public String toString()
    {
        return nazev + "[x=" + xPos + ", y=" + yPos  +
               ", sirka=" + sirka + ", vyska=" + vyska +
               ", barva=" + barva + "]";
    }


    /***************************************************************************
     * Vykreslí obraz své instance na plátno.
     */
    public void nakresli()
    {
        nakresliPrivate();
    }


    /***************************************************************************
     * Smaže obraz své instance z plátna (nakreslí ji barvou pozadí plátna).
     */
    public void smaz()
    {
        PLATNO.setBarvaPopredi( PLATNO.getBarvaPozadi() );
        PLATNO.zapln(new java.awt.geom.Rectangle2D.Double
                         (xPos, yPos, sirka, vyska));
    }


    /***************************************************************************
     * Přesune instanci o zadaný počet bodů vpravo,
     * při záporné hodnotě parametru vlevo.
     *
     * @param vzdalenost Vzdálenost, o kterou se instance přesune
     */
    public void posunVpravo(int vzdalenost)
    {
        setPozice( xPos+vzdalenost, yPos );
    }


    /***************************************************************************
     * Přesune instanci o implicitní počet bodů vpravo.
     * Tento počet lze zjistit zavoláním statické metody {@link #getKrok()}
     * a nastavit zavoláním statické metody {@link #setKrok(int)}.
     */
    public void posunVpravo()
    {
        posunVpravo( krok );
    }


    /***************************************************************************
     * Přesune instanci o implicitní počet bodů vlevo.
     * Tento počet lze zjistit zavoláním statické metody {@link #getKrok()}
     * a nastavit zavoláním statické metody {@link #setKrok(int)}.
     */
    public void posunVlevo()
    {
        posunVpravo( -krok );
    }


    /***************************************************************************
     * Přesune instanci o zadaný počet bodů dolů,
     * při záporné hodnotě parametru nahoru.
     *
     * @param vzdalenost   Počet bodů, o které se instance přesune
     */
    public void posunDolu(int vzdalenost)
    {
        setPozice( xPos, yPos+vzdalenost );
    }


    /***************************************************************************
     * Přesune instanci o implicitní počet bodů dolů.
     * Tento počet lze zjistit zavoláním statické metody {@link #getKrok()}
     * a nastavit zavoláním statické metody {@link #setKrok(int)}.
     */
    public void posunDolu()
    {
        posunDolu( krok );
    }


    /***************************************************************************
     * Přesune instanci o implicitní počet bodů nahoru.
     * Tento počet lze zjistit zavoláním statické metody {@link #getKrok()}
     * a nastavit zavoláním statické metody {@link #setKrok(int)}.
     */
    public void posunVzhuru()
    {
        posunDolu( -krok );
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vykreslí obraz své instance na plátno.
     */
    private void nakresliPrivate()
    {
        PLATNO.setBarvaPopredi( barva );
        PLATNO.zapln(new java.awt.geom.Rectangle2D.Double
                         (xPos, yPos, sirka, vyska));
    }



//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTOVACÍ METODY A TŘÍDY ==================================================
}
