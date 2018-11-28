import java.awt.Color;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*******************************************************************************
 * Třída {@code Barva} definuje skupinu základních barev
 * pro použití při kreslení tvarů.
 * Není definována jako výčtový typ,
 * aby uživatel mohl libovolně přidávat vlastní barvy.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public class Barva
{
    /** Počet pojmenovaných barev se při konstrukci následujících instancí
     *  načítá, a proto musí být deklarován před nimi. */
    private static int pocet = 0;



//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Atribut alfa pro průsvitné barvy. */
    private static final int PRUSVIT = 96;

    /** Minimální velikost složky při změnách světlosti a průhlednosti. */
    private static final int MINC = 32;

    /** Maximální velikost složky při změnách světlosti a průhlednosti. */
    private static final int MAXC = 255;

    /** Koeficient změny velikost složky při změnách světlosti a průhlednosti.*/
    private static final double KC = 0.7;

    /** Mapa všech doposud vytvořených barev klíčovaná jejich názvy. */
    private static final Map<String,Barva> nazev2barva =
                                             new LinkedHashMap<String,Barva>();

    /** Mapa všech doposud vytvořených barev klíčovaná jejich názvy
     *  s odstraněnou diakritikou. */
    private static final Map<String,Barva> nazevBhc2barva =
                                             new LinkedHashMap<String,Barva>();

    /** Mapa všech doposud vytvořených barev klíčovaná jejich barevností. */
    private static final Map<Color,Barva>  color2barva =
                                             new LinkedHashMap<Color,Barva>();

    /** Seznam všech dosud vytvořených barev. */
    private static final List<Barva> seznamBarev =
                                     new ArrayList<Barva>( 32 );



//########## BARVY #############################################################

    //########## Barvy s ekvivalentními konstantami v java.awt.Color

    /** Černá = RGBA( 0, 0, 0, 255); */         public static final Barva
    CERNA   = new Barva( Color.BLACK,           "cerna"   );

    /** Modrá = RGBA( 0, 0, 255, 255); */       public static final Barva
    MODRA   = new Barva( Color.BLUE,            "modra"   );

    /** Červená = RGBA( 255, 0, 0, 255); */     public static final Barva
    CERVENA = new Barva( Color.RED,             "cervena" );

    /** Fialová = RGBA( 255, 0, 255, 255); */   public static final Barva
    FIALOVA = new Barva( Color.MAGENTA,         "fialova" );

    /** Zelená = RGBA( 0, 255, 0, 255); */      public static final Barva
    ZELENA  = new Barva( Color.GREEN,           "zelena"  );

    /** Azurová = RGBA( 0, 255, 255, 255); */   public static final Barva
    AZUROVA = new Barva( Color.CYAN,            "azurova" );

    /** Žlutá = RGBA( 255, 255, 0, 255); */     public static final Barva
    ZLUTA   = new Barva( Color.YELLOW,          "zluta"   );

    /** Bílá = RGBA( 255, 255, 255, 255); */    public static final Barva
    BILA    = new Barva( Color.WHITE,           "bila"    );

    /** Světlešedá = RGBA( 192,192,192,255 ); */public static final Barva
    SVETLESEDA = new Barva( Color.LIGHT_GRAY,   "svetleseda");  //128 = 0x80

    /** Šedá = RGBA( 128, 128, 128, 255); */    public static final Barva
    SEDA    = new Barva( Color.GRAY,            "seda"    );

    /** Tmavošedá = RGBA(  64,  64,  64, 255);*/public static final Barva
    TMAVOSEDA = new Barva( Color.DARK_GRAY,     "tmavoseda" );  //64 = 0x40

    /** Černá = RGBA( 255, 175, 175, 255); */   public static final Barva
    RUZOVA  = new Barva( Color.PINK,            "ruzova"  );    //175 = 0xAF

    /** Oranžová = RGBA( 255, 200, 0, 255); */  public static final Barva
    ORANZOVA= new Barva( Color.ORANGE,          "oranzova");


    //########## Barvy bez ekvivalentních konstant v java.awt.Color

    /** Stříbrná = RGBA( 216, 216, 216, 255); */public static final Barva
    STRIBRNA = new Barva( 0xD8, 0xD8, 0xD8, 0xFF, "stribrna" );

    /** Zlatá = RGBA( 255, 224,  0, 255); */    public static final Barva
    ZLATA = new Barva( 0xFF, 0xE0, 0x00, 0xFF,  "zlata" );

    /** Cihlová = RGBA( 255, 102, 0, 255); */   public static final Barva
    CIHLOVA = new Barva( 0xFF, 0x66, 0, 0xFF,   "cihlova" );

    /** Hnědá = RGBA( 153, 51, 0, 255); */      public static final Barva
    HNEDA = new Barva( 0x99, 0x33, 0x00, 0xFF,  "hneda"   );

    /** Krémová = RGBA( 255, 255, 204, 255); */ public static final Barva
    KREMOVA = new Barva( 0xFF, 0xFF, 0xCC, 0xFF,"kremova" );

    /** Khaki = RGBA( 153, 153, 0, 255); */     public static final Barva
    KHAKI = new Barva( 0x99, 0x99, 0x00, 0xFF,  "khaki"   );

    /** Ocelová = RGBA( 0, 153, 204, 255); */   public static final Barva
    OCELOVA = new Barva( 0x00, 0x99, 0xCC, 0xFF,"ocelova" );

    /** Okrová = RGBA( 255, 153, 0, 255); */    public static final Barva
    OKROVA = new Barva( 0xFF, 0x99, 0x00, 0xFF, "okrova"   );


    //########## Průsvitné barvy

    /** Mléčná=RGBA( 255, 255, 255, 128 ) - polovičně průsvitná bílá. */
    public static final Barva
    MLECNA  = new Barva( 0xFF, 0xFF, 0xFF, 0x80, "mlecna");

    /** Kouřová = RGBA( 128, 128, 128, 128 ) - polovičně průsvitná šedá. */
    public static final Barva
    KOUROVA = new Barva( 0x80, 0x80, 0x80, 0x80, "kourova");

    /** Žádná = RGBA( 0, 0, 0, 0) - průhledná, neviditelná barva */
    public static final Barva
    ZADNA  = new Barva( 0, 0, 0, 0, "zadna");



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Příznak velikosti písmen, jimiž se vypisují názvy barev. */
    private static boolean velkymi = false;



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Název barvy zadávaný konstruktoru. */
    private final String nazev;

    /** Instance třídy {@link java.awt.Color} představující stejnou barvu. */
    private final Color  color;

    /** Index barvy v seznamu dosud vytvořených barev. */
    private final int index = pocet++;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================

    /***************************************************************************
     * Vrátí kolekci doposud definovaných barev.
     *
     * @return  Kolekce doposud definovaných barev
     */
    public
    static List<Barva> getSeznamBarev()
    {
        return Collections.unmodifiableList( seznamBarev );
    }


    /***************************************************************************
     * Vrátí seznam názvů doposud definovaných barev.
     *
     * @return  Seznam názvů doposud definovaných barev
     */
    public
    static List<String> getSeznamNazvuBarev()
    {
        return Arrays.asList( getPoleNazvuBarev() );
    }


    /***************************************************************************
     * Vrátí pole doposud definovaných barev.
     *
     * @return  Pole doposud definovaných barev
     */
    public static Barva[] getPoleBarev()
    {
        return seznamBarev.toArray( new Barva[seznamBarev.size()] );
    }


    /***************************************************************************
     * Vrátí vektor řetězců s dopsud definovanými názvy barev.
     *
     * @return  Vektor řetězců s dopsud definovanými názvy barev
     */
    public static String[] getPoleNazvuBarev()
    {
        String[] nazvy = nazev2barva.keySet()
                         .toArray(new String[nazev2barva.size()]);
        if( velkymi ) {
            for (int i = 0;   i < nazvy.length;   i++) {
                nazvy[i] = nazvy[i].toUpperCase();
            }
        }
        Arrays.sort(nazvy, Collator.getInstance());
        return nazvy;
    }


    /***************************************************************************
     * Nastaví, zda se budou názvy barev vypisovat velkými písmeny.
     *
     * @param velkymi {@code true} mají-li se názvy vypisovat velkými písmeny,
     *                jinak {@code false}
     * @return Původní nastavení
     */
    public static boolean setVelkymi( boolean velkymi )
    {
        boolean puvodni = Barva.velkymi;
        Barva.velkymi = velkymi;
        return puvodni;
    }



//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    /***************************************************************************
     * Otevře dialogové okno, v němž vypíše všechny doposud definované
     * názvy barev. Jména jsou lexikograficky seřazena.
     */
    public static void vypisZnameNazvy()
    {
        final int MAX_V_RADKU = 64;
        String[] nazvy = getPoleNazvuBarev();
        StringBuilder sb = new StringBuilder();
        for( int i=0, vRadku=0;   i < nazvy.length;   i++ ) {
            String text = nazvy[i];
            int textLength = text.length();
            if( (vRadku + textLength)  >=  MAX_V_RADKU ) {
                sb.append('\n');
                vRadku = 0;
            }
            sb.append(text);
            vRadku += textLength + 2;
            if( i < nazvy.length ) {
                sb.append(", ");
            }
        }
//        System.out.println("Barvy:\n" + sb);
        IO.zprava(sb);
    }



//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Vytvoří instanci barvy se zadanou velikostí barevných složek
     * a hladinou průhlednosti nastavovanou v kanále alfa
     * a se zadaným názvem.
     *
     * @param red       Velikost červené složky
     * @param green     Velikost zelené složky
     * @param blue      Velikost modré složky
     * @param alpha     Velikost složky alfa = hladina průhlednosti:
     *                  0=průhledná, 255=neprůhledná
     * @param nazev     Název vytvořené barvy
     */
    private Barva( int red, int green, int blue, int alpha, String nazev )
    {
        this( new Color(red, green, blue, alpha),  nazev );
    }


    /***************************************************************************
     * Vytvoří barvu ekvivalentní zadané instanci třídy
     * {@link java.awt.Color} se zadaným názvem.
     * Účelem tohoto kontruktoru je co nejjednodušší předání komponent barev,
     * které již mají definované ekvivalentní názvy
     * ve třídě {@link java.awt.Color}.
     *
     * @param c      Instance třídy {@link java.awt.Color} požadované barvy
     * @param nazev  Název vytvářené barvy; {@code null} oznaučje,
     *               že se má pro barvu vytvořit generický název
     */
    private Barva( Color c, String nazev )
    {
        this.color = c;
        this.nazev = nazev.toLowerCase();

        if( nazev2barva.containsKey( nazev )   ||
            color2barva.containsKey( c )  )
        {
            throw new IllegalArgumentException(
                "\nInterni chyba - barva " + getNazev() + " a/nebo " +
                getCharakteristikaHex() + " jiz existuji" );
        }

        Barva ja = this;
        nazev2barva.put(nazev, ja);
        color2barva.put(color, ja);
        seznamBarev.add(ja);

        pridejNazevBezDiakritiky();
    }


    /***************************************************************************
     * Převede název barvy na příslušný objekt typu Barva.
     *
     * @param nazevBarvy  Název požadované barvy -- seznam známých názvů
     *                    je možno získat zavoláním metody getZnáméNázvy()
     * @return Instance třídy Barva reprezentující zadanou barvu
     * @throws IllegalArgumentException
     *         Není-li barva zadného názvu mezi známými barvami
     */
    public static Barva getBarva( String nazevBarvy )
    {
        Barva barva = nazev2barva.get(nazevBarvy.toLowerCase());
        if (barva != null) {
            return barva;
        }
        else {
            throw new IllegalArgumentException(
                    "\nTakto pojmenovanou barvu neznam: " + nazevBarvy);
        }
    }


    /***************************************************************************
     * Vrátí neprůhlednou instanci barvy se zadanými velikostmi složek.
     * Není-li barva ještě definována, vytvoří ji
     * a přiřadí jí název odvozený z velikostí jejích barevných složek.
     *
     * @param red   Velikost červené složky
     * @param green Velikost zelené složky
     * @param blue  Velikost modré složky
     * @return Barva se zadanými velikostmi jednotlivých složek
     */
    public
    static Barva getBarva( int red, int green, int blue )
    {
        return getBarva( red, green, blue, 0xFF );
    }


    /***************************************************************************
     * Vrátí instanci barvy se zadanými velikostmi složek a průhledností.
     * Není-li barva ještě definována, vytvoří ji
     * a přiřadí jí název odvozený z velikostí jejích složek.
     *
     * @param red    Velikost červené složky
     * @param green  Velikost zelené složky
     * @param blue   Velikost modré složky
     * @param alpha  Koeficient alfa = hladina průhlednosti;
     *               0=průhledná, 255=neprůhledná
     * @return Barva se zadanými velikostmi jednotlivých složek
     */
    public
    static Barva getBarva( int red, int green, int blue, int alpha )
    {
        Color color = new Color( red, green, blue, alpha );
        Barva barva = color2barva.get( color );
        if( barva != null ) {
            return barva;
        }
        String nazev = "Barva(r=" + red + ",g=" + green +
                       ",b=" + blue + ",a=" + alpha + ")" ;
        return getBarva( red, green, blue, alpha, nazev );
    }


    /***************************************************************************
     * Vrátí instanci neprůhledné barvy se zadanými barevnými složkami
     * a zadným názvem. Pokud takováto barva neexistuje, vytvoří ji.
     * Existuje-li barva se zadaným názvem ale jinými složkami, anebo
     * existuje-li barva se zadanými složkami, ale jiným názvem,
     * vyhodí výjimku {@link IllegalArgumentException}.
     *
     * @param red       Velikost červené složky
     * @param green     Velikost zelené složky
     * @param blue      Velikost modré složky
     * @param nazev     Název vytvořené barvy
     *
     * @return Barva se zadaným názvem a velikostmi jednotlivých složek
     * @throws IllegalArgumentException má-li některé ze známých barev některý
     *         ze zadaných názvů a přitom má jiné nastavení barevných složek
     *         nebo má jiný druhý název.
     */
    public static Barva getBarva( int red, int green, int blue,
                                 String nazev )
    {
        return getBarva( red, green, blue, 0xFF, nazev );
    }


    /***************************************************************************
     * Vrátí instanci barvy se zadanými barevnými složkami, průhledností
     * a názvem. Pokud takováto barva neexistuje, vytvoří ji.
     * Existuje-li barva se zadaným názvem ale jinými složkami, anebo
     * existuje-li barva se zadanými složkami, ale jiným názvem,
     * vyhodí výjimku {@link IllegalArgumentException}.
     *
     * @param red       Velikost červené složky
     * @param green     Velikost zelené složky
     * @param blue      Velikost modré složky
     * @param alpha     Hladina průhlednosti (kanál alfa):
     *                  0=průhledná, 255=neprůhledná
     * @param nazev     Název vytvořené barvy
     * @return Instance třídy Barva reprezentující zadanou barvu.
     * @throws IllegalArgumentException Má-li některá ze definovaných barev
     *         zadaný název, a přitom má jiné nastavení barevných složek, anebo
     *         má shodnou velikost složek, ale jiný druhý název,
     *         anebo je jako název zadán prázdný řetězec.
     * @throws NullPointerException  Je-li {@code nazev} {@code null}.
     */
    public static Barva getBarva( int red, int green, int blue, int alpha,
                                  String nazev )
    {
        nazev = nazev.trim().toLowerCase();
        if( (nazev == null)  ||  nazev.equals("") )  {
            throw new IllegalArgumentException(
                "\nBarva musi mit zadan neprazdny nazev" );
        }
        Color color = new Color( red, green, blue, alpha );
        Barva barvaN = barvaSNazvem   ( nazev );
        Barva barvaC = color2barva.get( color );

        if( (barvaN != null)  &&  (barvaN == barvaC) ) {
            //Je požadována již existující barva
            return barvaN;
        }
        if( (barvaN == null)  &&  (barvaC == null) ) {
            //Je požadována dosud neexistující barva
            return new Barva(red, green, blue, alpha, nazev);
        }
        //Zjistíme, s kterou existující barvou požadavky kolidují
        Barva b = (barvaC != null)  ?  barvaC  :  barvaN;
        Color c = b.color;
        throw new IllegalArgumentException(
            "\nZadane parametry barvy koliduji s parametry existujici barvy "+
            "[existujici \u00d7 zadana]:" +
            "\nNazev:          " + b.getNazev()  + " \u00d7 " + nazev +
            "\nCervena slozka: " + c.getRed()    + " \u00d7 " + red   +
            "\nZelena  slozka: " + c.getGreen()  + " \u00d7 " + green +
            "\nModra   slozka: " + c.getBlue()   + " \u00d7 " + blue  +
            "\nPruhlednost:    " + c.getAlpha()  + " \u00d7 " + alpha
            );
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Převede námi používanou barvu na typ používaný kreslítkem.
     * Metoda je používaná ve třídě {@code SpravcePlatna}.
     *
     * @return Instance třídy {@code Color} reprezentující zadanou barvu
     */
    public Color getColor()
    {
        return color;
    }


    /***************************************************************************
     * Vrátí řetězec s charakteritikou dané barvy obsahující
     * název a hodnoty barevných složek uvedené v desítkové soustavě
     *
     * @return Řetězec s dekadickou charakteristikou barvy
     */
    public
    String getCharakteristikaDec()
    {
        return String.format("%s(Dec:R=%d,G=%d,B=%d,A=%d)", nazev,
                             color.getRed(),  color.getGreen(),
                             color.getBlue(), color.getAlpha()
                             );
    }


    /***************************************************************************
     * Vrátí řetězec s charakteritikou dané barvy obsahující název a
     * hodnoty barevných složek uvedené v šestnáctkové soustavě
     *
     * @return Řetězec s hexadecimální charakteristikou barvy
     */
    public
    String getCharakteristikaHex()
    {
        return getCharakteristikaHexPrivate();
    }


    /***************************************************************************
     * Vrátí index barvy v seznamu dosud vytvořených barev.
     *
     * @return Index dané barvy
     */
    public int getIndex()
    {
        return index;
    }


    /***************************************************************************
     * Vrátí název barvy.
     *
     * @return Název barvy
     */
    public String getNazev()
    {
        return getNazevPrivate();
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vrátí průsvitnou verzi dané barvy,
     * tj. barvu se stejnými barvenými složkami a průsvitností 0,5.
     *
     * @return Průsvitná verze dané barvy
     */
    public Barva prusvitna()
    {
        Color newColor = new Color (color.getRed(),  color.getGreen(),
                                    color.getBlue(), PRUSVIT);
        Barva barva = color2barva.get(newColor);
        if (barva == null) {
            String nazev2 = "prusvitna_" + this.nazev;
            barva = nazev2barva.get(nazev2);
            if (barva == null) {
                barva = getBarva(
                        color.getRed(),  color.getGreen(),
                        color.getBlue(), PRUSVIT,  nazev2);
            }
        }
        return barva;
    }


    /***************************************************************************
     * Vrátí barvu inverzní k zadané barvě, tj. barvu s inverzními
     * hodnotami jednotlivých barevných složek, ale se stejnou průhledností.
     *
     * @return Inverzní barva
     */
    public
    Barva inverzni()
    {
        return getBarva( MAXC - color.getRed(),
                         MAXC - color.getGreen(),
                         MAXC - color.getBlue(), color.getAlpha() );
    }


    /***************************************************************************
     * Vrátí méně průhlednou verzi dané barvy.
     * Pozor, vzhledem k zaokrouhlovacím chybám není oparace plně reverzibilní.
     *
     * @return Méně průhledná verze barvy
     */
    public
    Barva nepruhlednejsi()
    {
        int a = Math.max( Math.min((int)(color.getAlpha()/KC), MAXC), MINC);
        return getBarva(
            new Color(color.getRed(), color.getGreen(),
                      color.getBlue(), a ));
    }


    /***************************************************************************
     * Vrátí průhlednější verzi dané barvy.
     * Pozor, vzhledem k zaokrouhlovacím chybám není oparace plně reverzibilní.
     *
     * @return Průhlednější verze barvy
     */
    public
    Barva pruhlednejsi()
    {
        int a = (int)(color.getAlpha() * KC);
        return getBarva(
            new Color(color.getRed(), color.getGreen(),
                      color.getBlue(), a ));
    }


    /***************************************************************************
     * Vrátí světlejší verzi dané barvy. Pozor, vzhledem k zaokrouhlovacím
     * chybám nejsou operace světlejší a tmavší zcela reverzní.
     *
     * @return Světlejší verze barvy
     */
    public
    Barva svetlejsi()
    {
        Color c = color.brighter();
        if( c.equals(color) ) {
            c = new Color( Math.max( c.getRed(),   MINC ),
                           Math.max( c.getGreen(), MINC ),
                           Math.max( c.getBlue(),  MINC ),  c.getAlpha() );
        }
        return getBarva( c );
    }


    /***************************************************************************
     * Vrátí tmavší verzi dané barvy. Pozor, vzhledem k zaokrouhlovacím
     * chybám nejsou operace světlejší a tmavší zcela reverzní.
     *
     * @return Tmavší verze barvy
     */
    public
    Barva tmavsi()
    {
        return getBarva( color.darker() );
    }


    /***************************************************************************
     * Vrátí název barvy.
     *
     * @return  Název barvy
     */
    @Override
    public String toString()
    {
        return getNazev();
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================

    /***************************************************************************
     * Vrátí řetězec s charakteritikou dané barvy obsahující název a
     * hodnoty barevných složek uvedené v šestnáctkové soustavě
     *
     * @return Řetězec s hexadecimální charakteristikou barvy
     */
    private String getCharakteristikaHexPrivate()
    {
        return String.format("%s(Hex:R=%02X,G=%02X,B=%02X,A=%02X)", nazev,
                             color.getRed(),  color.getGreen(),
                             color.getBlue(), color.getAlpha()
                             );
    }


    /***************************************************************************
     * Vrátí název barvy.
     *
     * @return Název barvy
     */
    private String getNazevPrivate()
    {
        return (velkymi  ?  nazev.toUpperCase()  :  nazev);
    }


    /***************************************************************************
     * Vrátí barvu s daným názvem přičem je schopen ignorovat diakritiku.
     *
     * @param nazev Název hledané barvy
     * @return Barva se zadaným názvem nebo {@code null}.
     */
    private static Barva barvaSNazvem( String nazev ) {
        nazev = nazev.toLowerCase();
        Barva barva = nazev2barva.get( nazev );
        if( barva == null ) {
            barva = nazevBhc2barva.get(nazev);
        }
        return barva;
    }


    /***************************************************************************
     * Vrátí barvu se stejnýmio hodnotami komponent jako má parametr.
     * Není-li taková barva ještě definována, vytvoří jí
     * a pojmenuje ji s využitím hodnota jejích složek.
     *
     * @param c  Barva, která je instancí třídy {@link java.awt.Color}
     * @return Odpovídající barva třídy {@code Barva}
     */
    private static Barva getBarva( Color c )
    {
        Barva b = color2barva.get( c );
        if( b != null ) {
            return b;
        }
        else {
            return getBarva( c.getRed(),  c.getGreen(),
                             c.getBlue(), c.getAlpha() );
        }
    }



//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Obsahuje-li název diakritiku,
     * uloží do příslušné mapy jeho verzi bez diakritiky.
     */
    private void pridejNazevBezDiakritiky() {
        String bhc = IO.odhackuj(nazev);
        if (! nazev.equals(bhc)) {
            nazevBhc2barva.put(bhc, this);
        }
    }



//== VNOŘENÉ A VNITŘNÍ TŘÍDY ===================================================
//== TESTY A METODA MAIN =======================================================
}
