import java.util.HashMap;
import java.util.Map;

/**
 * Třída {@code Smer8} slouží jako výčtový typ pro 8 hlavních a vedlejších
 * světových stran spolu se směrem ZADNY.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public enum Smer8
{
//== HODNOTY VÝČTOVÉHO TYPU ====================================================

    /** Východ       = doprava.        */    VYCHOD      ( "V" ,  1,  0 ),
    /** Severovýchod = doprava nahoru. */    SEVEROVYCHOD( "SV",  1, -1 ),
    /** Sever        = nahoru.         */    SEVER       ( "S" ,  0, -1 ),
    /** Severozápad  = doleva nahoru.  */    SEVEROZAPAD ( "SZ", -1, -1 ),
    /** Západ        = doleva.         */    ZAPAD       ( "Z" , -1,  0 ),
    /** Jihozápad    = doleva nahoru.  */    JIHOZAPAD   ( "JZ", -1,  1 ),
    /** Jih          = dolu.           */    JIH         ( "J" ,  0,  1 ),
    /** Jihovýchod   = doprava dolu.   */    JIHOVYCHOD  ( "JV",  1,  1 ),
    /** Žádný        = nikam.          */    ZADNY       ( "@" ,  0,  0 ),
    ;



//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Celkový počet definovaných směrů. */
    public  static final int SMERU = values().length;

    /** Maska pro dělení modulo
     *  Výpočet masky předpokládá, že počet směrů je mocninou dvou
     *  plus jedna pro směr ZADNY). */
    private static final int MASKA = SMERU-2;

    /** Odmocnina z jedné poloviny pro výpočet šikmých vzdáleností. */
    private static final double SQR = Math.sqrt( 0.5 );

    /** Mapa konveruijící názvy směrů (s diakritikou i bez ní) a jejich zkratky
     *  na příslušné směry. */
    private static final Map<String,Smer8> nazev2smer =
                                        new HashMap<String,Smer8>( SMERU*3 );



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Příznak povolení operací se směrem {@code ZADNY}. */
    private static boolean zadnyZakazan = false;



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================

    //Inicializace statických atributů je nerealizovatelná před
    //definicí jednotlivých hodnot ==> je ji proto potřeba realizovat dodatečně
    static
    {
        if (SMERU != 9) {
            throw new RuntimeException(
                "\nNabourany zdrojovy kod - spatny pocet smeru");
        }
        for (Smer8 s : values())  {
            nazev2smer.put( s.name(),  s );
            nazev2smer.put( s.zkratka, s );
        }
    }



//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Velikost změny příslušné složky souřadnic po přesunu
     *  na sousední políčko v daném směru. */
    private final int dx, dy;

    /** Jedno- či dvojpísmenná zkratka úplného názvu daného směru. */
    private final String zkratka;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    /**
     * Vrátí vektor se čtyřmi hlavními světovými stranami.
     *
     * @return  Požadovaný vektor.
     */
    public static Smer8[] values4()
    {
        return new Smer8[] { VYCHOD, SEVER, ZAPAD, JIH };
    }


    /**
     * Nastaví, zda budou povoleny operace se směrem {@code ZADNY}.
     * Nejsou-li operace povoleny, vyhazují metody při použití tohoto směru
     * výjimku {@code java.lang.IllegalStateException}.
     * Jsou-li operace povoleny, pak objekt natočený do směru {@code ZADNY}
     * zůstává v romto "směru" po jakémkoliv otočení
     * a při jakékmkoliv přesunu zůstává na místě.
     *
     * @param zakazat {@code true} mají-li se operace zakázat,
     *                {@code false} mají-li se povolit
     * @return Původní nastavení tohoto příznaku
     */
    public static boolean zakazatZadny(boolean zakazat) {
        boolean puvodni = zadnyZakazan;
        zadnyZakazan = zakazat;
        return puvodni;
    }



//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /**
     * Vrátí směr se zadaným názvem nebo zkratkou.
     * Bohužel není možno pojmenovat tuto metodu {@code valueOf()},
     * protože takot nazvanou metodu definuje překladač v této třídě
     * takže ji není možno překrýt vlastní verzí.
     *
     * @param nazev Název požadovaného směru nebo jeho zkratka;
     *              při zadávání nezáleží na velikosti písmen
     * @return Požadovaný směr
     * @throws IllegalArgumentException  Neexistuje-li směr se zadaným
     *                                   názvem nebo zkratkou
     */
    public static Smer8 get( String nazev )
    {
        Smer8 ret = nazev2smer.get(nazev.toUpperCase() );
        if( ret == null ) {
            throw new IllegalArgumentException("\nNeznamy nazev smeru: "
                                               + nazev);
        }
        return ret;
    }


    /**
     * Vytvoří nový směr a zapamatuje si zkratku jeho názvu
     * spolu se změnami souřadnic při pohybu v daném směru.
     *
     * @param zkratka   Jedno- či dvoj-písmenná zkratka označující daný směr
     * @param dx        Změna vodorovné souřadnice
     *                  při přesunu na sousední políčko v daném směru
     * @param dy        Změna svislé souřadnice
     *                  při přesunu na sousední políčko v daném směru
     */
    private Smer8( String zkratka, int dx, int dy )
    {
        this.zkratka = zkratka;
        this.dx = dx;
        this.dy = dy;
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /**
     * Vrátí zkratku názvu daného směru.
     *
     * @return  Požadovaná zkratka
     */
    public String getZkratka()
    {
        return zkratka;
    }


    /**
     * Vrátí informaci o tom, je-li daný směr jedním ze 4 hlavních směrů,
     * tj. jedná-li se o jeden ze směrů {@code VYCHOD}, {@code SEVER},
     * {@code ZAPAD}, {@code JIH}.
     *
     * @return Jedná-li se o hlavní směr, vrátí {@code true},
     *         jinak vrátí {@code false}
     */
    public boolean isHlavni()
    {
        return ((this.ordinal() & 1) == 0)  &&  (this != ZADNY);
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /**
     * Vrátí pozici sousedního políčka v daném směru.
     *
     * @param pozice      Pozice stávajícího políčka
     * @return Pozice políčka vzdáleného v daném směru o zadanou vzdálenost
     */
    public Pozice dalsiPozice(Pozice pozice)
    {
        overPlatny();
        return new Pozice(pozice.x + dx,  pozice.y + dy);
    }


    /**
     * Vrátí pozici políčka vzdáleného v daném směru o zadanou vzdálenost.
     *
     * @param pozice      Pozice stávajícího políčka
     * @param vzdalenost  Vzdálenost hladnané pozice
     * @return Pozice políčka vzdáleného v daném směru o zadanou vzdálenost
     */
    public Pozice dalsiPozice(Pozice pozice, int vzdalenost)
    {
        overPlatny();
        if( (dx != 0)  &&  (dy != 0) ) {
            int prirustek = (int)(SQR*vzdalenost + 0.5);
            return new Pozice( pozice.x + prirustek,
                               pozice.y + prirustek );
        } else {
            return new Pozice( pozice.x + dx*vzdalenost,
                               pozice.y + dy*vzdalenost );
        }
    }


    /**
     * Obdrží x-vou souřadnici políčka a vrátí x-vou souřadnici
     * sousedního políčka v daném směru.
     *
     * @param x Obdržená x-ová souřadnice
     * @return x-ová souřadnice políčka po přesunu o jedno pole v daném směru
     */
    public int dalsiX( int x )
    {
        overPlatny();
        return x + dx;
    }


    /**
     * Obdrží x-vou souřadnici políčka a vrátí x-vou souřadnici políčka
     * vzdáleného v daném směru o zadanou vzdálenost.
     *
     * @param x            Obdržená x-ová souřadnice
     * @param vzdalenost   Vzdálenost políčka v daném směru
     * @return x-ová souřadnice vzdáleného políčka
     */
    public double dalsiX( int x, int vzdalenost )
    {
        overPlatny();
        if( (dx != 0)  &&  (dy != 0) ) {
            return x + SQR*dx*vzdalenost;
        } else {
            return x + dx*vzdalenost;
        }
    }


    /**
     * Obdrží y-vou souřadnici políčka a vrátí y-vou souřadnici
     * sousedního políčka v daném směru.
     *
     * @param y Obdržená y-ová souřadnice
     * @return y-ová souřadnice sousedního políčka v daném směru
     */
    public int dalsiY( int y )
    {
        overPlatny();
        return y + dy;
    }


    /**
     * Obdrží x-vou souřadnici políčka a vrátí x-vou souřadnici políčka
     * vzdáleného v daném směru o zadanou vzdálenost.
     *
     * @param y            Obdržená y-ová souřadnice
     * @param vzdalenost   Vzdálenost políčka v daném směru
     * @return y-ová souřadnice vzdáleného políčka
     */
    public double dalsiY( int y, int vzdalenost )
    {
        overPlatny();
        if( (dx != 0)  &&  (dy != 0) ) {
            return y + SQR*dy*vzdalenost;
        } else {
            return y + dy*vzdalenost;
        }
    }


    /**
     * Vrátí změnu vodorovné souřadnice při přesunu
     * na sousední pole v daném směru.
     *
     * @return Změna x-ové souřadnice při přesunu o jedno pole v daném směru
     */
    public int dx()
    {
        overPlatny();
        return dx;
    }


    /**
     * Vrátí změnu svislé souřadnice při přesunu
     * na sousední pole v daném směru.
     *
     * @return Změna y-ové souřadnice při přesunu o jedno pole v daném směru
     */
    public int dy()
    {
        overPlatny();
        return dy;
    }


    /**
     * Vráti směr otočený o 90° vlevo.
     *
     * @return Směr objektu po vyplněni příkazu vlevo v bok.
     */
    public Smer8 vlevoVbok()
    {
        overPlatny();
        return (this == ZADNY)
               ?  ZADNY
               :  values()[MASKA & (2+ordinal())];
    }


    /**
     * Vráti směr otočený o 90° vpravo.
     *
     * @return Směr objektu po vyplněni příkazu vpravo v bok
     */
    public Smer8 vpravoVbok()
    {
        overPlatny();
        return (this == ZADNY)
               ?  ZADNY
               :  values()[MASKA & (-2+ordinal())];
    }


    /**
     * Vráti směr otočený o 180°.
     *
     * @return Směr objektu po vyplněni příkazu čelem vzad.
     */
    public Smer8 celemVzad()
    {
        overPlatny();
        return (this == ZADNY)
               ?  ZADNY
               :  values()[MASKA & (4+ordinal())];
    }


    /**
     * Vráti směr otočený o 45° vlevo.
     *
     * @return Směr objektu po vyplnění příkazu nalevo vpříč.
     */
    public Smer8 nalevoVpric()
    {
        overPlatny();
        return (this == ZADNY)
               ?  ZADNY
               : values()[MASKA & (1 + ordinal())];
    }


    /**
     * Vráti směr otočený o 45° vpravo.
     *
     * @return Směr objektu po vyplnění příkazu napravo vpříč.
     */
    public Smer8 napravoVpric()
    {
        overPlatny();
        return (this == ZADNY)
               ?  ZADNY
               :  values()[MASKA & (-1 + ordinal())];
    }


    /**
     * Přepočítá rozměry zadané oblasti otrientované na východ
     * a umístěné v referenční oblasti
     * na nové rozměry po otočení do tohoto směru.
     *
     * @param obl   Oblast, jejíž reozměry přepočítáváme a jejíž souřadnice
     *              jsou relativní vůče referenční oblasti
     * @param ref   Referenční oblast, v níž je otáčená oblast umístěna
     *              a vůči níž jsou udávány výchozí souřadnice.
     *              V této otočené oblasti má být oblast umístěna po otočení.
     *              Přitom se předpokládá, že souřadnice referenční oblasti
     *              se otočením nezmění.
     *              V tomto ohledu je ideální referenční oblast čtvercová.
     * @return Oblast s novými parametry reprezentujícími původní oblast
     *         po otočení do zadaného směru;
     *         její souřadnice jsou udávány jako absolutní
     */
    public Oblast otocVOblast(Oblast obl, Oblast ref)
    {
        int x, y, s, v;
        switch(this) //Přepočet závisí na cílovém směru
        {
            case VYCHOD:
                x = ref.x  +  obl.x;
                y = ref.y  +  obl.y;
                s = obl.sirka;
                v = obl.vyska;
                return new Oblast(x, y, s, v);

            case SEVER:
                x = ref.x  +  obl.y;
                y = ref.y  -  obl.x  +  ref.sirka  -  obl.sirka;
                s = obl.vyska;
                v = obl.sirka;
                return new Oblast(x, y, s, v);

            case ZAPAD:
                x = ref.x  -  obl.x  +  ref.sirka  -  obl.sirka;
                y = ref.y  -  obl.y  +  ref.vyska  -  obl.vyska;
                s = obl.sirka;
                v = obl.vyska;
                return new Oblast(x, y, s, v);

            case JIH:
                x = ref.x  -  obl.y  +  ref.vyska  -  obl.vyska;
                y = ref.y  +  obl.x;
                s = obl.vyska;
                v = obl.sirka;
                return new Oblast(x, y, s, v);

            default:
                throw new RuntimeException(
                        "\nOblast neze otocit do smeru " + this);
        }
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    /**
     * Ověří, že se nejedná o operaci zakázanou pro směr {@code ZADNY}.
     *
     * @throws IllegalStateException
     *         Pro směr {@code ZADNY} je tato operace zakázána
     */
    private void overPlatny()
    {
        if( zadnyZakazan  &&  (this == ZADNY) ) {
            Throwable t = new Throwable();
            StackTraceElement[] aste = t.getStackTrace();
            StackTraceElement   ste  = aste[1];
            String metoda = ste.getMethodName();

            throw new IllegalStateException(
                "\nPro smer ZADNY je tato operace zakazana: " + metoda );
        }
    }



//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTY A METODA MAIN =======================================================
}
