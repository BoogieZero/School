/**
 * Instance třídy {@code Elipsa} představují elipsy
 * určené pro práci na virtuálním plátně
 * při prvním seznámení s třídami a objekty
 * a definované svojí pozicí, rozměrem a barvou.
 * Pozicí instance se přitom rozumí
 * pozice levého horního rohu opsaného obdélníku
 * a rozměrem rozměr tohoto obdélníku.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public class Elipsa implements ITvar
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Počáteční barva nakreslené instance v případě,
     *  kdy uživatel žádnou požadovanou barvu nezadá -
     *  pro elipsu {@code Barva.MODRÁ}. */
    public static final Barva IMPLICITNI_BARVA = Barva.MODRA;

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
    private String nazev = "Elipsa_" + poradi;

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
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Připraví novou instanci s implicitním umístěním, rozměry a barvou.
     * Instance bude umístěna v levém horním rohu plátna
     * a bude mít implicitní barvu,
     * výšku rovnu kroku a šířku dvojnásobku kroku plátna.
     */
    public Elipsa()
    {
        this( 0, 0, 2*SP.getKrok(), SP.getKrok() );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí a rozměry
     * a implicitní barvou.
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
    public Elipsa( int x, int y, int sirka, int vyska )
    {
        this( x, y, sirka, vyska, IMPLICITNI_BARVA );
    }


    /***************************************************************************
     * Připraví novou instanci se zadanou pozicí, rozměry a barvou.
     *
     * @param pozice    Pozice vytvářené instance
     * @param rozmer    Rozměr vytvářené instance
     * @param barva     Barva vytvářené instance
     */
    public Elipsa(Pozice pozice, Rozmer rozmer, Barva barva)
    {
        this( pozice.x, pozice.y, rozmer.sirka, rozmer.vyska, barva );
    }


    /***************************************************************************
     * Vytvoří novou instanci vyplňující zadanou oblast
     * a mající zadanou barvu.
     *
     * @param oblast   Oblast definující pozici a rozměr vytvářené instance
     * @param barva    Barva vytvářené instance
     */
    public Elipsa(Oblast oblast, Barva barva)
    {
        this( oblast.x, oblast.y, oblast.sirka, oblast.vyska, barva );
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
    public Elipsa( int x, int y, int sirka, int vyska, Barva barva )
    {
        //Test platnosti parametru
        if ((sirka<=0) || (vyska<=0)) {
            throw new IllegalArgumentException(
                "\nnew Elipsa - Parametry nemaji povolene hodnoty: x="
                + x + ", y=" + y + ", sirka=" + sirka + ", vyska=" + vyska );
        }

        //Parametry akceptovány --> můžeme tvořit
        this.xPos  = x;
        this.yPos  = y;
        this.sirka = sirka;
        this.vyska = vyska;
        this.barva = barva;
    }


    /***************************************************************************
     * Vrátí kopii daného tvaru,
     * tj. stejný tvar, stejně velký, stejně umístěný a se stejnou barvou.
     *
     * @return Požadovaná kopie
     */
    @Override
    public Elipsa kopie()
    {
        return new Elipsa(xPos, yPos, sirka, vyska, barva);
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
     * @return   Instance třídy {@code Rozmer} s aktuálními rozměry instance
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
     * Nastaví nové rozměry instance. Nastavované rozměry musí být nezáporné,
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
     * @param nova Požadovaná nová barva
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
               ",sirka=" + sirka + ",vyska=" + vyska +
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
        kreslitko.vyplnOval( xPos, yPos, sirka, vyska, barva );
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
     * resp. {@code SpravcePlatna.setKrokRozmer(int,int,int)}.
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
     * resp. {@code SpravcePlatna.setKrokRozmer(int,int,int)}.
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
     * resp. {@code SpravcePlatna.setKrokRozmer(int,int,int)}.
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
     * resp. {@code SpravcePlatna.setKrokRozmer(int,int,int)}.
     */
    public void posunVzhuru()
    {
        posunDolu( -SP.getKrok() );
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTOVACÍ METODY A TŘÍDY ==================================================
}
