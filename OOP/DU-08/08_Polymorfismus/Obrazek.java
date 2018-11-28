import java.awt.Image;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.geom.AffineTransform;

/*******************************************************************************
 * Instance třídy {@code Obrázek} představují obrázky,
 * které je možné načíst ze souborů nebo vytvořit z oblasti plátna.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */

public class Obrazek implements IHybaci//, IKopírovatelný
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Instance třídy {@code SpravcePlatna} spravující plátno,
     *  na které se všechny tvary kreslí. */
    protected static final SpravcePlatna SP = SpravcePlatna.getInstance(false);



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Celkový počet vytvořených instancí. */
    private static int pocet = 0;



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Rodné číslo instance (její identifikátor v rámci třídy) je odvozeno
     *  z pořadí jejího vytvoření. */
    protected final int ID = ++pocet;

    /** Obalený obrázek. */
    private final Image obrazek;

    /** Rozměry originálního obrázku. */
    private final int maSirka, maVyska;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    /** Název instance sestávající implicitně z jednoduchého názvu třídy
     *  následovaným potržítkem a {@code ID} instance */
    private String nazev = "Obrazek_" + ID;

    /** Bodová x-ová souřadnice instance. */
    private int xPos;

    /** Bodová y-ová souřadnice instance. */
    private int yPos;

    /** Šířka v bodech. */
    protected int sirka;

    /** Výška v bodech. */
    protected int vyska;

    /** Afinní transformace zodpovědná za změny velikosti obrázku. */
    AffineTransform aft;



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Přečte ze zadaného souboru obrázek, který bude kreslit
     * na zadaných souřadnicích; pomocí úvodních hvězdiček
     * je možno zadat, zda se daný soubor bude hledat ve složce
     * balíčku třídy volající metody (pak musí jméno předcházet *&#47;),
     * nebo ve složce kořenovéhío balíčku (pak musí předcházet **&#47;);
     *
     * @param x      Vodorovná souřadnice levého horního rohu obrázku
     * @param y      Svislá souřadnice levého horního rohu obrázku
     * @param soubor Název souboru, v němž je obrázek uložen
     */
    public Obrazek(int x, int y, String soubor)
    {
        this(x, y, soubor, null);
    }


    /***************************************************************************
     * Vytvoří nový obrázek ze zadaného obrázku - instance třídy
     * <code>java.awt.Image</code> a umístí jej do počátku.
     *
     * @param x      Vodorovná souřadnice levého horního rohu obrázku
     * @param y      Svislá souřadnice levého horního rohu obrázku
     * @param obrazek Instance třídy <code>java.awt.Image</code>,
     *                která bude základem obrázku.
     */
    public Obrazek(int x, int y, Image obrazek)
    {
        this(x, y, null, obrazek);
    }


    /***************************************************************************
     * Přečte ze zadaného souboru obrázek, který bude kreslit
     * na zadaných souřadnicích; pomocí úvodních hvězdiček
     * je možno zadat, zda se daný soubor bude hledat ve složce
     * balíčku třídy volající metody (pak musí jméno předcházet *&#47;),
     * nebo ve složce kořenovéhío balíčku (pak musí předcházet **&#47;);
     *
     * @param x      Vodorovná souřadnice levého horního rohu obrázku
     * @param y      Svislá souřadnice levého horního rohu obrázku
     * @param soubor Název souboru, v němž je obrázek uložen
     * @param obrazek Instance třídy <code>java.awt.Image</code>,
     *                která bude základem obrázku.
     */
    private Obrazek(int x, int y, String soubor, Image obrazek)
    {
        //Test platnosti parametru
        if ((x<0) || (y<0)) {
            throw new IllegalArgumentException(
                "\nParametry nemaji povolene hodnoty: x=" + x + ", y=" + y);
        }
        //Parametry akceptovány --> můžeme tvořit
        this.xPos  = x;
        this.yPos  = y;
        if (obrazek != null) {
            this.obrazek = obrazek;
        } else {
            URL url;
            if (soubor.charAt(0) == '*') {
                if (soubor.charAt(1) == '*') {
                    String jmeno = soubor.substring(3);
                    url = getClass().getClassLoader().getResource(jmeno);
                } else {
                    Throwable t = new Throwable();
                    StackTraceElement ste = t.getStackTrace()[1];
                    String clsn = ste.getClassName();
                    Class<?> clss;
                    try{ clss = Class.forName(clsn);
                    } catch(ClassNotFoundException exc) {
                        throw new RuntimeException(
                            "\nNeco se podelalo - nenasel existujici tridu " +
                            clsn, exc);
                    }
                    String jmeno = soubor.substring(2);
                    url = clss.getResource(jmeno);
                }
            }else {
                try {
                    url = new URL(soubor);
                } catch(MalformedURLException exc) {
                    throw new RuntimeException(
                            "\nNepodarilo se nacist obrazek v souboru " + soubor, exc);
                }
            }
            this.obrazek = Toolkit.getDefaultToolkit().getImage(url);
        }
        maSirka = obrazek.getWidth (Kreslitko.OBRAZOR);
        maVyska = obrazek.getHeight(Kreslitko.OBRAZOR);
        this.sirka = maSirka;
        this.vyska = maVyska;
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Vrátí x-ovou souřadnici pozice instance.
     *
     * @return  x-ová souřadnice
     */
    public int getX()
    {
        return xPos;
    }


    /***************************************************************************
     * Vrátí y-ovou souřadnici pozice instance.
     *
     * @return  y-ová souřadnice
     */
    public int getY()
    {
        return yPos;
    }


    /***************************************************************************
     * Vrátí instanci třídy Pozice s pozicí instance.
     *
     * @return   Pozice s pozicí instance
     */
    @Override
    public Pozice getPozice()
    {
        return new Pozice(getX(), getY());
    }


    /***************************************************************************
     * Nastaví novou pozici instance.
     *
     * @param x   Nová x-ová souřadnice instance
     * @param y   Nová y-ová souřadnice instance
     */
    @Override
    public void setPozice(int x, int y)
    {
        if (aft != null) {
            aft.translate((x-getX()) / aft.getScaleX(),
                           (y-getY()) / aft.getScaleY());
        }
        xPos = x;
        yPos = y;
        SP.prekresli();
    }


    /***************************************************************************
     * Nastaví novou pozici instance.
     *
     * @param pozice   Nová pozice instance
     */
    @Override
    public void setPozice(Pozice pozice)
    {
        setPozice(pozice.x, pozice.y);
    }


    /***************************************************************************
     * Vrátí šířku instance.
     *
     * @return  šířka instance v bodech
     */
     public int getSirka()
     {
         return sirka;
     }


    /***************************************************************************
     * Vrátí výšku instance.
     *
     * @return  výška instance v bodech
     */
     public int getVyska()
     {
         return vyska;
     }


    /***************************************************************************
     * Vrátí instanci třídy Rozměr s rozměry instance.
     *
     * @return   Rozměr s rozměry instance.
     */
    @Override
    public Rozmer getRozmer()
    {
        return new Rozmer(getSirka(), getVyska());
    }


    /***************************************************************************
     * Nastaví nový "čtvercový" rozměr instance -
     * na zadaný rozměr se nastaví výška i šířka.
     *
     * @param rozmer  Nově nastavovaný rozměr v obou směrech; rozměr&gt;0
     */
    public void setRozmer(int rozmer)
    {
        setRozmer(rozmer, rozmer);
    }


    /***************************************************************************
     * Nastaví nové rozměry instance.
     *
     * @param rozmer    Nově nastavovaný rozměr.
     */
    @Override
    public void setRozmer(Rozmer rozmer)
    {
        setRozmer(rozmer.sirka, rozmer.vyska);
    }


    /***************************************************************************
     * Nastaví nové rozměry instance.
     *
     * @param sirka    Nově nastavovaná šířka; šířka&gt;0
     * @param vyska    Nově nastavovaná výška; výška&gt;0
     */
    @Override
    public void setRozmer(int sirka, int vyska)
    {
        if ((sirka == maSirka)  &&  (vyska == maVyska)) {
            aft = null;
        } else {
            aft = new AffineTransform();
            aft.translate(getX(), getY());
            aft.scale((double)sirka / maSirka,  (double)vyska / maVyska);
        }
        this.sirka = sirka;
        this.vyska = vyska;
        SP.prekresli();
    }


    /***************************************************************************
     * Vrátí instanci třídy Oblast s informacemi o pozici a rozměrech instance.
     *
     * @return   Oblast s informacemi o pozici a rozměre instance.
     */
    public Oblast getOblast()
    {
        return new Oblast(getX(), getY(), getSirka(), getVyska());
    }


    /***************************************************************************
     * Nastaví novou polohu a rozměry instance.
     *
     * @param oblast Nově nastavovaná oblast zaujímaná instancí.
     */
    public void setOblast(Oblast oblast)
    {
        setOblast(oblast.x, oblast.y, oblast.sirka, oblast.vyska);
    }


    /***************************************************************************
     * Nastaví pozici a rozměr objektu.
     *
     * @param pozice  Pozice objektu
     * @param rozmer   Rozměr objektu
     */
    public void setOblast(Pozice pozice, Rozmer rozmer)
    {
        setOblast(pozice.x, pozice.y, rozmer.sirka, rozmer.vyska);
    }


    /***************************************************************************
     * Nastaví novou pozici a rozměr objektu.
     *
     * @param x       x-ová souřadnice počátku, x&gt;=0, x=0 má levý okraj plátna
     * @param y       y-ová souřadnice počátku, y&gt;=0, y=0 má horní okraj plátna
     * @param sirka   Šířka vytvářeného objektu v bodech
     * @param vyska   Výška vytvářeného objektu v bodech
     */
    public void setOblast(int x, int y, int sirka, int vyska)
    {
        SP.nekresli(); {
            setPozice(x,     y    );
            setRozmer(sirka, vyska);
        } SP.vratKresli();
    }


    /***************************************************************************
     * Vrátí název instance - implicitně název třídy následovaný ID instance.
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
     * Převede instanci na řetězec.
     *
     * @return Řetězcová reprezentace dané instance.
     */
    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "#" + (100 + hashCode() % 900);
    }


    /***************************************************************************
     * {@inheritDoc}
     * @param kreslitko Kreslítko, které nakreslí instanci
     */
    @Override
    public void nakresli(Kreslitko kreslitko)
    {
        kreslitko.kresliObrazek(getX(), getY(), obrazek, aft);
    }

//
//    /***************************************************************************
//     * Přijme návštěvníka a nechá jej provést jeho metodu.
//     *
//     * @param návštěvník  Přijímaný návštěvník
//     * @param param       Parametry návštěvníkovy metody
//     * @return Hodnota vrácená metodou návštěvníka
//     */
//    @Override
//    public Object přijmi(INávštěvník návštěvník, Object param)
//    {
//        return návštěvník.aplikujNa(this, param);
//    }
//


//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== INTERNÍ DATOVÉ TYPY =======================================================
//== TESTY A METODA MAIN =======================================================

//    /***************************************************************************
//     * Testovací metoda.
//     * @return obr
//     * /
//     public static Obrázek test()
//     {
//         //Součet řetězců v následujících příkazech má jediný účel:
//         //aby tyto příkazy předčasně neukončovaly blokové zakomentování
////         Obrázek o = new Obrázek(0, 0, "*"+"/POKUS");
//         Obrázek o = new Obrázek(0, 0, "**"+"/XXX.PNG");
////         System.out.println("Obr.: " + o);
//         AktivníPlátno SP = AktivníPlátno.getPlátno();
//         SP.přidej(o);
////         System.out.println("KONEC");
//         return o;
//     }/**/
//     /** @param ppr Parametry příkazového řádku - nepoužité */
//     public static void main(String[]ppr){ TestObrázek.main(null); }//test(); }/*-*/
}
