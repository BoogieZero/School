/*******************************************************************************
 * Instance třídy {@code Trojuhelnik} představují trojúhelníky
 * určené pro práci na virtuálním plátně
 * při prvním seznámení s třídami a objekty
 * a definované svojí pozicí, rozměrem, barvou a směrem,
 * do nějž je otočen hlavní vrchol.
 * Pozicí instance se přitom rozumí
 * pozice levého horního rohu opsaného obdélníku
 * a rozměrem rozměr tohoto obdélníku.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public class Trojuhelnik
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Počáteční barva nakreslené instance v případě,
     *  kdy uživatel žádnou požadovanou barvu nezadá -
     *  pro trojúhelník {@code Barva.ZELENÁ}. */
    public static final Barva IMPLICITNI_BARVA = Barva.ZELENA;

    /** Směr, kam bude ukazovat vrcholt trojúhelníku v případě,
     *  kdy uživatel žádný preferovný směr nezadá.    */
    public static final Smer8 IMPLICITNI_SMER = Smer8.SEVER;

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

    /** ID instance = pořadí vytvoření dané instance v rámci třídy. */
    private final int ID = ++pocet;

    /** Název sestávající z názvu třídy a ID instance. */
    private final String nazev = "Trojuhelnik_" + ID;



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

    /** Směr, do nějž je otočen vrchol trojúhelníku. */
    private Smer8   smer;



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================

    /***************************************************************************
     * Vrátí implicitní vzdálenost (krok), o kterou se instance přesune
     * při volaní bezparametrickych metod přesunu.
     *
     * @return Velikost implicitního kroku v bodech
     */
     public static int getKrok()
     {
         return krok;
     }


    /***************************************************************************
     * Nastaví implicitní vzdálenost (krok), o kterou se instance přesune
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
     * Připraví novou instanci s implicitním umístěním, rozměry, barvou
     * a natočením.
     * Instance bude umístěna v levém horním rohu plátna
     * a bude mít implicitní barvu,
     * výšku rovnu kroku a šířku dvojnásobku kroku (tj. implicitně 50x100 bodů)
     * a bude natočena vrcholem na sever.
     */
    public Trojuhelnik()
    {
        this( 0, 0, 2*krok, krok );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí a rozměry
     * a implicitní barvou a směrem natočení.
     * Pozice instance jsou přitom definovány jako pozice
     * levého horního rohu opsaného obdélníku,
     * rozměr instance jako rozměr tohoto obdélníku.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param sirka   Šířka vytvářené instance,  šířka &gt; 0
     * @param vyska   Výška vytvářené instance,  výška &gt; 0
     */
    public Trojuhelnik( int x, int y, int sirka, int vyska )
    {
        this( x, y, sirka, vyska, IMPLICITNI_BARVA, IMPLICITNI_SMER );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí, rozměry a směrem natočení
     * a s implicitní barvou.
     * Pozice instance jsou přitom definovány jako pozice
     * levého horního rohu opsaného obdélníku,
     * rozměr instance jako rozměr tohoto obdélníku.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param sirka   Šířka instance,   šířka &gt; 0
     * @param vyska   Výška instance,   výška &gt; 0
     * @param smer    Směr, do nějž bude natočen vrchol trojúhelníku -
     *                je třeba zadat některou z instancí třídy Směr8
     */
    public Trojuhelnik( int x, int y, int sirka, int vyska, Smer8 smer )
    {
        this( x, y, sirka, vyska, IMPLICITNI_BARVA, smer );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí, rozměry a barvou.
     * Směr natočení bude implicitní, tj. na sever.
     * Pozice instance jsou přitom definovány jako pozice
     * levého horního rohu opsaného obdélníku,
     * rozměr instance jako rozměr tohoto obdélníku.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param sirka   Šířka vytvářené instance,  šířka &gt; 0
     * @param vyska   Výška vytvářené instance,  výška &gt; 0
     * @param barva   Barva vytvářené instance
     */
    public Trojuhelnik( int x, int y, int sirka, int vyska, Barva barva )
    {
        this( x, y, sirka, vyska, barva, IMPLICITNI_SMER );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí, rozměry, barvou,
     * i směrem natočení.
     * Pozice instance jsou přitom definovány jako pozice
     * levého horního rohu opsaného obdélníku,
     * rozměr instance jako rozměr tohoto obdélníku.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param sirka   Šířka vytvářené instance,  šířka &gt; 0
     * @param vyska   Výška vytvářené instance,  výška &gt; 0
     * @param barva   Barva vytvářené instance
     * @param smer    Směr, do nějž bude natočen vrchol trojúhelníku -
     *                je třeba zadat některou z instancí třídy Směr8
     */
    public Trojuhelnik( int x, int y, int sirka, int vyska, Barva barva,
                        Smer8 smer )
    {
        //Test platnosti parametru
        if ((sirka<=0) || (vyska<=0)) {
            throw new IllegalArgumentException(
                "\nnew Trojuhelnik - Parametry nemaji povolene hodnoty: x="
                + x + ", y=" + y + ", sirka=" + sirka + ", vyska=" + vyska );
        }

        //Parametry akceptovány --> můžeme tvořit
        this.xPos  = x;
        this.yPos  = y;
        this.sirka = sirka;
        this.vyska = vyska;
        this.barva = barva;
        this.smer  = smer;
        nakresliPrivate();
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Vrátí x-ovou (vodorovnou) souřadnici pozice instance,
     * tj. vodorovnou souřadnici levého horního rohu opsaného obdélníku.
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
     * tj. svislou souřadnici levého horního rohu opsaného obdélníku.
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
     * levého horního rohu opsaného obdélníku.
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
     * Šířka instance jsou přitom definována jako šířka
     * opsaného obdélníku.
     *
     * @return  Aktuální šířka instance v bodech
     */
     public int getSirka()
     {
         return sirka;
     }


    /***************************************************************************
     * Vrátí výšku instance v bodech.
     * Výška instance jsou přitom definována jako výška
     * opsaného obdélníku.
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
     * Rozměry instance jsou přitom definovány jako rozměry
     * opsaného obdélníku.
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
     * Rozměry instance jsou přitom definovány jako rozměry
     * opsaného obdélníku.
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


    /***************************************************************************
     * Vrátí aktuální směr instance. tj. směr, do nějž je natočen vrchol.
     *
     * @return  Instance třídy {@code Směr8} definující aktuálně nastavený směr
     */
    public Smer8 getSmer()
    {
        return smer;
    }


    /***************************************************************************
     * Nastaví nový směr instance.
     *
     * @param novy  Požadovaný nový směr
     */
    public void setSmer(Smer8 novy)
    {
        if (novy != Smer8.ZADNY) {
            smaz();
            smer = novy;
            nakresli();
        }
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vrátí podpis instance, tj. její řetězcovou reprezentaci.
     * Používá se především při ladění.
     *
     * @return Název instance následovaný jejími souřadnicemi,
     *         rozměry, barvou a směrem
     */
    @Override
    public String toString()
    {
        return nazev + "[x=" + xPos + ", y=" + yPos  +
               ", sirka=" + sirka + ", vyska=" + vyska +
               ", barva=" + barva + ", smer="  + smer  + "]";
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
        int[][] points = getVrcholy();
        PLATNO.zapln(new java.awt.Polygon(points[0], points[1], 3));
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
        int[][] points = getVrcholy();
        PLATNO.zapln(new java.awt.Polygon(points[0], points[1], 3));
    }


    /***************************************************************************
     * Vrátí matici se souřadnicemi vrcholů daného trojúhelníku.
     *
     * @return Požadovaná matice
     */
    private int[][] getVrcholy()
    {
        int[] xpoints = null;
        int[] ypoints = null;

        switch( smer )
        {
            case VYCHOD:
                xpoints = new int[]{ xPos,  xPos + (sirka),    xPos };
                ypoints = new int[]{ yPos,  yPos + (vyska/2),  yPos + vyska };
                break;

            case SEVEROVYCHOD:
                xpoints = new int[]{ xPos,  xPos + sirka,  xPos + sirka };
                ypoints = new int[]{ yPos,  yPos,          yPos + vyska };
                break;

            case SEVER:
                xpoints = new int[]{ xPos,         xPos + (sirka/2), xPos + sirka };
                ypoints = new int[]{ yPos + vyska, yPos,             yPos + vyska };
                break;

            case SEVEROZAPAD:
                xpoints = new int[]{ xPos,          xPos,  xPos + sirka };
                ypoints = new int[]{ yPos + vyska,  yPos,  yPos         };
                break;

            case ZAPAD:
                xpoints = new int[]{ xPos,             xPos + sirka, xPos + sirka };
                ypoints = new int[]{ yPos + (vyska/2), yPos,         yPos + vyska };
                break;

            case JIHOZAPAD:
                xpoints = new int[]{ xPos,  xPos,          xPos + sirka };
                ypoints = new int[]{ yPos,  yPos + vyska,  yPos + vyska };
                break;

            case JIH:
                xpoints = new int[]{ xPos,  xPos + (sirka/2),  xPos + sirka };
                ypoints = new int[]{ yPos,  yPos + vyska,      yPos,        };
                break;

            case JIHOVYCHOD:
                xpoints = new int[]{ xPos,          xPos +sirka,   xPos + sirka };
                ypoints = new int[]{ yPos + vyska,  yPos + vyska,  yPos         };
                break;

            default:
                throw new IllegalStateException(
                    "Instance ukazuje do nedefinovaneho smeru" );
        }
        return new int[][] { xpoints, ypoints };
    }



//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTOVACÍ METODY A TŘÍDY ==================================================
}
