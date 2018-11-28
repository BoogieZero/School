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
public class Trojuhelnik implements ITvar
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Počáteční barva nakreslené instance v případě,
     *  kdy uživatel žádnou požadovanou barvu nezadá -
     *  pro trojúhelník Barva.ZELENÁ. */
    public static final Barva IMPLICITNI_BARVA = Barva.ZELENA;

    /** Směr, kam bude ukazovat vrcholt trojúhelníku v případě,
     *  kdy uživatel žádný preferovný směr nezadá.    */
    public static final Smer8 IMPLICITNI_SMER = Smer8.SEVER;

    /** Plátno, na které se bude instance kreslit. */
    private static final SpravcePlatna SP = SpravcePlatna.getInstance(false);



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Počet vytvořených instancí. */
    private static int pocet = 0;



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Pořadí vytvoření dané instance v rámci třídy. */
    private final int poradi = ++pocet;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    /** Název instance sestávající implicitně
     *  z jednoduchého názvu třídy následovaného potržítkem a
     *  pořadím vytvoření instance v rámci instancí této třídy. */
    private String nazev = "Trojuhelnik_" + poradi;

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
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Připraví novou instanci s implicitním umístěním, rozměry, barvou
     * a natočením.
     * Instance bude umístěna v levém horním rohu plátna
     * a bude mít implicitní barvu,
     * výšku rovnu kroku a šířku dvojnásobku kroku plátna
     * a bude natočena vrcholem na sever.
     */
    public Trojuhelnik()
    {
        this( 0, 0, 2*SP.getKrok(), SP.getKrok() );
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
     * Připraví novou instanci vyplňující zadanou oblast
     * a mající zadnou barvu a směr natočení.
     *
     * @param pozice  Pozice vytvářené instance
     * @param rozmer  Rozměr vytvářené instance
     * @param barva   Barva vytvářené instance
     * @param smer    Směr, do nějž bude natočen vrchol trojúhelníku -
     *                je třeba zadat některou z instancí třídy Směr8
     */
    public Trojuhelnik( Pozice pozice, Rozmer rozmer, Barva barva, Smer8 smer )
    {
        this(pozice.x, pozice.y, rozmer.sirka, rozmer.vyska, barva, smer);
    }


    /***************************************************************************
     * Připraví novou instanci vyplňující zadanou oblast
     * a mající zadnou barvu a směr natočení.
     *
     * @param oblast  Oblast, kterou má vytvářená instance zaujmout
     * @param barva   Barva vytvářené instance
     * @param smer    Směr, do nějž bude natočen vrchol trojúhelníku -
     *                je třeba zadat některou z instancí třídy Směr8
     */
    public Trojuhelnik( Oblast oblast, Barva barva, Smer8 smer )
    {
        this(oblast.x, oblast.y, oblast.sirka, oblast.vyska, barva, smer);
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
    }


    /***************************************************************************
     * Vrátí kopii daného tvaru,
     * tj. stejný tvar, stejně velký, stejně umístěný a se stejnou barvou.
     *
     * @return Požadovaná kopie
     */
    @Override
    public Trojuhelnik kopie()
    {
        return new Trojuhelnik(xPos, yPos, sirka, vyska, barva,smer);
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
        xPos = x;
        yPos = y;
        SP.prekresli();
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
     * Vrátí instanci třídy {@code Rozměr} s aktuálními rozměry instance.
     *
     * @return   Instance třídy {@code Rozměr} s aktuálními rozměry instance
     */
    @Override
    public Rozmer getRozmer()
    {
        return new Rozmer( getSirka(), getVyska() );
    }


    /***************************************************************************
     * Nastaví nové rozměry instance.
     *
     * @param rozmer    Nově nastavovaný rozměr
     */
    @Override
    public void setRozmer(Rozmer rozmer)
    {
        setRozmer( rozmer.sirka, rozmer.vyska );
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
    @Override
    public void setRozmer(int sirka, int vyska)
    {
        if( (sirka < 0) || (vyska < 0) ) {
            throw new IllegalArgumentException(
                            "Rozmery musi byt nezaporne: sirka=" +
                            sirka + ", vyska=" + vyska);
        }
        this.sirka = Math.max(1, sirka);
        this.vyska = Math.max(1, vyska);
        SP.prekresli();
    }


    /***************************************************************************
     * Vrátí instanci třídy {@code Oblast} s informacemi
     * o aktuální pozici a rozměrech instance.
     *
     * @return   Instance třídy {@code Oblast} s informacemi
     *           o aktuální pozici a rozměrech instance
     */
    public Oblast getOblast()
    {
        return new Oblast( getX(), getY(), getSirka(), getVyska() );
    }


    /***************************************************************************
     * Nastaví novou polohu a rozměry instance prostřednictvím
     * instance třídy {@code Oblast}.
     *
     * @param oblast Nově nastavovaná oblast zaujímaná instancí
     */
    public void setOblast(Oblast oblast)
    {
        setOblast( oblast.x, oblast.y, oblast.sirka, oblast.vyska );
    }


    /***************************************************************************
     * Nastaví pozici a rozměr objektu.
     *
     * @param pozice  Nově nastavovaná pozice objektu
     * @param rozmer  Nově nastavovaný rozměr objektu
     */
    public void setOblast( Pozice pozice, Rozmer rozmer )
    {
        setOblast( pozice.x, pozice.y, rozmer.sirka, rozmer.vyska );
    }


    /***************************************************************************
     * Nastaví novou pozici a rozměr objektu.
     *
     * @param x       Vodorovná (x-ová) souřadnice instance,
     *                x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y       Svislá (y-ová) souřadnice instance,
     *                y=0 má horní okraj plátna, souřadnice roste dolů
     * @param sirka   Šířka vytvářeného objektu v bodech
     * @param vyska   Výška vytvářeného objektu v bodech
     */
    public void setOblast( int x, int y, int sirka, int vyska )
    {
        SP.nekresli(); {
            setPozice( x,     y     );
            setRozmer( sirka, vyska );
        } SP.vratKresli();
    }


    /***************************************************************************
     * Vrátí aktuální barvu instance.
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
     * @param nova  Požadovaná nová barva
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
            smer = novy;
            SP.prekresli();
        }
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
               ",sirka=" + sirka + ",vyska=" + vyska +
               ",barva=" + barva + ",smer="  + smer  + ")";
    }


    /***************************************************************************
     * Prostřednictvím dodaného kreslítka vykreslí obraz své instance.
     *
     * @param kreslitko Kreslítko, které nakreslí instanci
     */
    @Override
    public void nakresli( Kreslitko kreslitko )
    {
        int[][] points = getVrcholy();
        kreslitko.vyplnPolygon( points[0], points[1], barva );
    }


    /***************************************************************************
     * Přihlásí se u správce plátna.
     */
    public void nakresli()
    {
        SP.pridej(this);
    }


    /***************************************************************************
     * Odhlásí se u správce plátna.
     */
    public void smaz()
    {
        SP.odstran(this);
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
     * Tento počet definuje správce plátna a je možno jej zjistit
     * zavoláním jeho metody {@code SpravcePlatna.getKrok()}
     * a nastavit zavoláním jeho metody {@code SpravcePlatna.setKrok(int)},
     * resp. {@code SpravcePlatna.setKrokRozměr(int,int,int)}.
     */
    public void posunVpravo()
    {
        posunVpravo( SP.getKrok() );
    }


    /***************************************************************************
     * Přesune instanci o implicitní počet bodů vlevo.
     * Tento počet definuje správce plátna a je možno jej zjistit
     * zavoláním jeho metody {@code SpravcePlatna.getKrok()}
     * a nastavit zavoláním jeho metody {@code SpravcePlatna.setKrok(int)},
     * resp. {@code SpravcePlatna.setKrokRozměr(int,int,int)}.
     */
    public void posunVlevo()
    {
        posunVpravo( -SP.getKrok() );
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
     * Tento počet definuje správce plátna a je možno jej zjistit
     * zavoláním jeho metody {@code SpravcePlatna.getKrok()}
     * a nastavit zavoláním jeho metody {@code SpravcePlatna.setKrok(int)},
     * resp. {@code SpravcePlatna.setKrokRozměr(int,int,int)}.
     */
    public void posunDolu()
    {
        posunDolu( SP.getKrok() );
    }


    /***************************************************************************
     * Přesune instanci o implicitní počet bodů nahoru.
     * Tento počet definuje správce plátna a je možno jej zjistit
     * zavoláním jeho metody {@code SpravcePlatna.getKrok()}
     * a nastavit zavoláním jeho metody {@code SpravcePlatna.setKrok(int)},
     * resp. {@code SpravcePlatna.setKrokRozměr(int,int,int)}.
     */
    public void posunVzhuru()
    {
        posunDolu( -SP.getKrok() );
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

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
