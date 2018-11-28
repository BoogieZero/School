import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*******************************************************************************
 * Třída <b><code>SprávcePlátna</code></b> slouží k jednoduchému kreslení
 * na virtuální plátno a připadné následné animaci nakreslených obrázků.
 * </p><p>
 * Třída neposkytuje veřejný konstruktor, protože chce, aby její instance
 * byla jedináček, tj. aby se všechno kreslilo na jedno a to samé plátno.
 * Jediným způsobem, jak získat odkaz na instanci třídy
 * <code>SprávcePlátna</code>,
 * je volaní její statické metody <code>getInstance()</code>.
 * </p><p>
 * Třída <code>SprávcePlátna</code> funguje jako manažer, který dohlíží
 * na to, aby se po změně zobrazení některého z tvarů všechny ostatní tvary
 * řádně překreslily, aby byly správně zachyceny všechny překryvy
 * a aby se při pohybu jednotlivé obrazce vzájemně neodmazavaly.
 * Aby vše správně fungovalo, je možno použít jeden ze dvou přístupů:</p>
 * <ul>
 *   <li>Manažer bude obsah plátna překreslovat
 *       <b>v pravidelných intervalech</b>
 *       bez ohledu na to, jestli se na něm udála nějaká změna či ne.
 *       <ul><li>
 *       <b>Výhodou</b> tohoto přístupu je, že se zobrazované objekty
 *       nemusí starat o to, aby se manažer dozvěděl, že se jejich stav změnil.
 *       </li><li>
 *       <b>Neýhodou</b> tohoto přístupu je naopak to, že manažer
 *       spotřebovává na neustálé překreslování jistou část výkonu
 *       procesoru, což může u pomalejších počítačů působit problémy.
 *       <br>&nbsp;</li></ul></li>
 *   <li>Manažer překresluje plátno <b>pouze na výslovné požádání</b>.
 *       <ul><li>
 *       <b>Výhodou</b> tohoto přístupu je úspora spotřebovaného výkonu
 *       počítače v období, kdy se na plátně nic neděje.
 *       </li><li>
 *       <b>Nevýhodou</b> tohoto přístupu je naopak to, že kreslené
 *       objekty musí na každou změnu svého stavu upozornit manažera,
 *       aby věděl, žed má plátno překreslit.
 *   </li>
 * </ul><p>
 * Třída <code>SprávcePlátna</code> požívá druhou z uvedených strategií,
 * tj. <b>překresluje plátno pouze na požádání</b>.
 * </p><p>
 * Obrazec, který chce být zobrazován na plátně, se musí nejprve přihlásit
 * u instance třídy <code>SprávcePlátna</code>, aby jej tato zařadila
 * mezi spravované obrazce (sada metod <code>přidej&hellip;</code>).
 * Přihlásit se však mohou pouze instance tříd, které implementují
 * rozhraní <code>IKreslený</code>.
 * </p><p>
 * Nepřihlášený obrazec nemá šanci býti zobrazen, protože na plátno
 * se může zobrazit pouze za pomoci kreslítka, jež může získat jedině od
 * instance třídy <code>SprávcePlátna</code>, ale ta je poskytuje pouze
 * instancím, které se přihlásily do její správy.
 * </p><p>
 * Obrazec, který již dále nemá byt kreslen, se muže odhlásit zavoláním
 * metody <code>odstraň(IKreslený)</code>.Zavoláním metody
 * <code>odstraňVše()</code> se ze seznamu spravovaných (a tím i z plátna)
 * odstraní všechny vykreslované obrazce.
 * </p><p>
 * Efektivitu vykreslování je možné ovlivnit voláním metody
 * <code>nekresli()</code>, která pozastaví překreslování plátna po nahlášených
 * změnách. Její volání je výhodné např. v situaci, kdy je třeba vykreslit
 * obrazec složený z řady menších obrazců a bylo by nevhodné překreslovat
 * plátno po vykreslení každého z nich.
 * </p><p>
 * Do původního stavu převedeme plátno voláním metody <code>vraťKresli()</code>,
 * která vrátí vykreslování do stavu před posledním voláním metody
 * <code>nekresli()</code>. Nemůžeč se tedy stát, že by se při zavolání metody
 * <code>nekresli()</code> v situaci, kdy je již vykreslování pozastaveno,
 * začalo po následém zavolání <code>vraťKresli()</code> hned vykreslovat.
 * Po dvou voláních <code>vraťKresli()</code> se začne vykreslovat až po
 * dvou zavoláních <code>vraťKresli()</code>.
 * </p><p>
 * Proto plátno pouze žádáme, aby se vrátilo do toho kreslícího stavu,
 * ve kterém bylo v okamžiku, kdy jsme je naposledy žádali o to,
 * aby se přestalo překreslovat. Nemůže se tedy stát, že by se při zavolání
 * metody <code>nekresli()</code> v situaci, kdy je již vykreslování
 * pozastaveno, začalo po následném zavolání <code>vraťKresli()</code> hned
 * vykreslovat.
 * </p><p>
 * Každé zavolání metody <code>nekresli()</code> musí být doplněno
 * odpovídajícím voláním <code>vraťKresli()</code>. Teprve když poslední
 * <code>vraťKresli()</code> odvolá první <code>nekresli()</code>, bude
 * překreslování opět obnoveno.
 * </p>
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public final class SpravcePlatna
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Titulek okna aktivního plátna. */
    private static final String TITULEK_0  = "Platno ovladane spravcem";

    /** Počáteční políčková šířka aktivní plochy plátna. */
    private static final int SIRKA_0 = 10;

    /** Počáteční políčková výška aktivní plochy plátna. */
    private static final int VYSKA_0 = 6;

    /** Počáteční barva pozadí plátna. */
    private static final Barva POZADI_0 = Barva.KREMOVA;

    /** Počáteční barva čar mřížky. */
    private static final Barva BARVA_CAR_0 = Barva.CERNA;

    /** Implicitní rozteč čtvercové sítě. */
    private static final int KROK_0 = 50;

//     /** Maximální povolená velikost rozteče čtvercové sítě. */
//     private static final int MAX_KROK = 200;



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Jediná instance třídy. */
    private static volatile SpravcePlatna SP;



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Aplikační okno animačního plátna. */
    private final JFrame okno;

    /** Instance lokální třídy, která je zřízena proto, aby odstínila
     *  metody svého rodiče JPanel. */
    private final JPanel platno;

    /** Seznam zobrazovaných předmětů. */
    private final List<IKresleny> predmety = new ArrayList<IKresleny>();



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //Z venku neovlivnitelné Atributy pro zobrazeni plátna v aplikačním okně

        /** Vše se kreslí na obraz - ten se snadněji překreslí. */
        private Image obrazPlatna;

        /** Kreslítko získané od obrazu plátna, na nějž se vlastně kreslí. */
        private Kreslitko kreslitko;

        /** Semafor bránící příliš častému překreslování. Překresluje se pouze
         *  je-li ==0. Nesmí být &lt; 0. */
        private int nekreslit = 0;

        /** Příznak toho, že kreslení právě probíhá,
         *  takže vypínání nefunguje. */
        private boolean kreslim = false;

        /** Čáry zobrazující na plátně mřížku. */
        private Cara[] vodorovna,   svisla;


    //Přímo ovlivnitelné atributy

        /** Rozteč čtvercové sítě. */
        private int krok = KROK_0;

        /** 1 = mřížka se zobrazuje nad obrazem,
         *  0 = mřížka se nezobrazuje,
         * -1 = mřížka se zobrazuje pod obrazem. */
        private int mrizka = -1;

        /** Barva pozadí plátna. */
        private Barva barvaPozadi = POZADI_0;

        /** Barva čar mřížky. */
        private Barva barvaCar = BARVA_CAR_0;

        /** Šířka aktivní plochy plátna v udávaná v polích. */
        private int sloupcu = SIRKA_0;

        /** Výška aktivní plochy plátna v udávaná v polích. */
        private int radku = VYSKA_0;

        /** Šířka aktivní plochy plátna v bodech. */
        private int sirkaBodu = SIRKA_0 * krok;

        /** Výška aktivní plochy plátna v bodech. */
        private int vyskaBodu = VYSKA_0 * krok;

//         /** Zda se mají přizpůsobiví upozorňovat na změny rozměru pole. */
//         private boolean hlasitZmenyRozmeru = true;

        /** Zda je možno měnit velikost kroku. */
        private Object vlastnikPovoleniZmenyKroku = null;

        /** Pozice plátna na obrazovace - při používání více obrazovek
         *  je občas třeba ji po zviditelnění obnovit. */
        Point pozicePlatna;



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Metoda umožnující získat odkaz na instanci správce plátna
     * a případně zviditelnit jeho aplikační okno.
     * Vrací vsak pokaždé odkaz na stejnou instanci,
     * protože instance plátna je jedináček.
     * <p>
     * Pokud instance při volaní metody ještě neexistuje,
     * metoda instanci vytvoří.</p>
     *
     * @return Instance třídy {@code SprávcePlátna}
     */
    public static SpravcePlatna getInstance()
    {
        return getInstance(true);
    }


    /***************************************************************************
     * Metoda umožnující získat odkaz na instanci správce plátna
     * a současně nastavit, zda má být jeho aplikační okno viditelné.
     * Vrací vsak pokaždé odkaz na stejnou instanci,
     * protože instance plátna je jedináček.
     * <p>
     * Pokud instance při volaní metody ještě neexistuje,
     * metoda instanci vytvoří.</p>
     *
     * @param viditelny Má-li se zajistit viditelnost instance;
     *                  {@code false} aktuálně nastavenou viditelnost nemění
     * @return Instance třídy {@code SprávcePlátna}
     */
    public static SpravcePlatna getInstance(boolean viditelny)
    {
        if (SP == null) {
            synchronized(SpravcePlatna.class) {
                if (SP == null) {
                    inicializuj();
                }
            }
        }
        if (viditelny) {
            SP.setViditelne(true);
        }
        return SP;
    }


    /***************************************************************************
     * Vytvoří instanci třídy - jedináčka => je volán pouze jednou.
     */
    @SuppressWarnings("serial")
    private SpravcePlatna()
    {
        okno = new JFrame();
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        platno = new JPanel()
        {   /** Overrides parent's abstract method. */
            @Override
            public void paintComponent(Graphics g)
            {
                g.drawImage(obrazPlatna, 0, 0, null);
            }
        };
        okno.setContentPane(platno);
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Nastaví rozměr plátna zadáním bodové velikosti políčka a
     * počtu políček ve vodorovném a svislém směru.
     * Při velikosti políčka = 1 se vypíná zobrazování mřížky.
     *
     * @param  krok    Nová bodová velikost políčka
     * @param  pSirka  Nový počet políček vodorovně
     * @param  pVyska  Nový počet políček svisle
     */
    public void setKrokRozmer(int krok, int pSirka, int pVyska)
    {
        setKrokRozmer(krok, pSirka, pVyska, null);
    }


    /***************************************************************************
     * Nastaví rozměr plátna zadáním bodové velikosti políčka a
     * počtu políček ve vodorovném a svislém směru.
     * Při velikosti políčka = 1 se vypíná zobrazování mřížky.
     *
     * @param  krok    Nová bodová velikost políčka
     * @param  pSirka  Nový počet políček vodorovně
     * @param  pVyska  Nový počet políček svisle
     * @param  menic   Objekt, který žádá o změnu rozměru. Jakmile je jednou
     *                 tento objekt nastaven, nesmí již rozměr plátna
     *                 měnit nikdo jiný.
     */
    public
    synchronized void setKrokRozmer(final int krok,
                                    final int pSirka, final int pVyska,
                                    Object menic)
    {
        setKrokRozmer_OverParametry(krok, pSirka, pVyska, menic);

        nekresli(); {
//             int stary = this.krok;
            invokeAndWait(new Runnable() {
                @Override
                public void run()
                {
                    setKrokRozmerInterni(krok, pSirka, pVyska);
                }
            }, "setKrokRozmerInterni from setKrokRozmer");
            pripravCary();
//             obvolejPrizpusobive(stary, krok);
            setViditelne_Pockam();
        } vratKresli();
////%A+ =999%
//        msgF("setKrokRozměr");
////%A-
    }


    /***************************************************************************
     * Vrátí vzdálenost čar mřížky = bodovou velikost políčka.
     *
     * @return Bodová velikost políčka
     */
     public int getKrok()
     {
         return krok;
     }


    /***************************************************************************
     * Nastaví vzdálenost čar mřížky = bodovou velikost políčka.
     * Při velikosti políčka = 1 se vypíná zobrazování mřížky.
     *
     * @param velikost  Nová bodová velikost políčka
     */
    public
    void setKrok(int velikost)
    {
        setKrokRozmer(velikost, sloupcu, radku);
    }


    /***************************************************************************
     * Vrátí počet sloupců plátna, tj. jeho políčkovou šířku.
     *
     * @return  Aktuální políčková šířka plátna (počet políček vodorovně)
     */
    public int getSloupcu()
    {
        return sloupcu;
    }


    /***************************************************************************
     * Vrátí bodovou šířku plátna.
     *
     * @return  Aktuální bodová šířka plátna (počet bodů vodorovně)
     */
    public
    int getBsirka()
    {
        return sirkaBodu;
    }


    /***************************************************************************
     * Vrátí počet řádků plátna, tj. jeho políčkovou výšku.
     *
     * @return  Aktuální políčková výška plátna (počet políček svisle)
     */
    public int getRadku()
    {
        return radku;
    }


    /***************************************************************************
     * Vrátí bodovou výšku plátna.
     *
     * @return  Aktuální bodová výška plátna (počet bodů svisle)
     */
    public
    int getBVyska()
    {
        return vyskaBodu;
    }


    /***************************************************************************
     * Vrátí políčkový rozměr plátna, tj. šířku a výšku v polích.
     *
     * @return  Aktuální políčkový rozměr plátna
     */
    public Rozmer getRozmer()
    {
        return new Rozmer(sloupcu, radku);
    }


    /***************************************************************************
     * Nastaví rozměr plátna zadáním jeho políčkové výsky a šířky.
     *
     * @param  sloupcu  Nový počet políček vodorovně
     * @param  radku    Nový počet políček svisle
     */
    public
    void setRozmer(int sloupcu, int radku)
    {
        setKrokRozmer(krok, sloupcu, radku);
    }


    /***************************************************************************
     * Nastaví rozměr plátna zadáním jeho políčkové výsky a šířky.
     *
     * @param  rozmer  Zadávaný rozměr v počtu políček
     */
    public void setRozmer(Rozmer rozmer)
    {
        setRozmer(rozmer.sirka, rozmer.vyska);
    }


    /***************************************************************************
     * Vrátí informaci o tom, je-li zobrazována mřížka.
     *
     * @return Mřížka je zobrazována = true, není zobrazována = false.
     */
    public boolean isMrizka()
    {
    	return (mrizka != 0);
    }


    /***************************************************************************
     * V závislosti na hodntě parametru nastaví nebo potlačí
     * zobrazování čar mřížky.
     *
     * @param zobrazit  Jestli mřížku zobrazovat.
     */
    public synchronized void setMrizka(boolean zobrazit)
    {
        setMrizka(zobrazit, false);
    }


    /***************************************************************************
     * Nastaví má-li se na plátně nastavovat mřížka a pokud ano, tak
     * zda se čáry mžížky budou zobrazovat nad nebo pod zobrazovnými tvary.
     *
     * @param zobrazit  Jestli mřížku zobrazovat.
     * @param nad {@code true} má-li se mřížka zobrazovat nad zobrazenými tvary,
     *            {@code false} má-li se zobrazovat pod nimi.
     */
    public synchronized void setMrizka(boolean zobrazit, boolean nad)
    {
        if (zobrazit) {
            if (nad) {
                mrizka = 1;
            }
            else {
                mrizka = -1;
            }
        }
        else {
            mrizka = 0;
        }
        pripravCary();
        prekresli();
    }


    /***************************************************************************
     * Poskytuje informaci o aktuální viditelnosti okna.
     *
     * @return Je-li okno viditelné, vrací <b>true</b>, jinak vrací <b>false</b>
     */
    public
    boolean isViditelne()
    {
        return okno.isVisible();
    }


    /***************************************************************************
     * V závislosti na hodnotě svého parametru
     * nastaví nebo potlačí viditelnost plátna na displeji.
     *
     * @param viditelne logická hodnota požadované viditelnost (true=viditelné)
     */
    public
    synchronized void setViditelne(final boolean viditelne)
    {
//         boolean prekresleno = false;
        boolean zmena = (isViditelne() != viditelne);
        if (! zmena) {
            return;                 //==========>
        }
        if (! viditelne) {
            okno.setVisible(false);
            return;                 //==========>
        }

        //Máme dosud neviditelné okno zobrazit
        pozicePlatna = okno.getLocation();
        if (EventQueue.isDispatchThread()) {
            setViditelneInterni(viditelne);
        }
        else {
            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    setViditelneInterni(viditelne);
                }
            };
            EventQueue.invokeLater(runnable);
        }
    }


    /***************************************************************************
     * Vrátí aktuální barvu pozadí.
     *
     * @return  Nastavená barva pozadí
     */
    public Barva getBarvaPozadi()
    {
        return barvaPozadi;
    }


    /***************************************************************************
     * Nastaví pro plátno barvu pozadí.
     *
     * @param  barva  Nastavovaná barva pozadí
     */
    public synchronized void setBarvaPozadi(Barva barva)
    {
        barvaPozadi = barva;
        kreslitko.setPozadi(barvaPozadi);
        prekresli();
    }


    /***************************************************************************
     * Pomocná metoda pro účely ladění aby bylo možno zkontrolovat,
     * ze na konci metody má semafor stejnou hodnotu, jako měl na počátku.
     *
     * @return  Stav vnitřního semaforu: &gt;0  - nebude se kreslit,<br>
     *                                   ==0 - kreslí se,<br>
     *                                   &lt;0  - chyba
     */
    public
    int getNekresli()
    {
        return nekreslit;
    }


    /***************************************************************************
     * Vrátí aktuální název v titulkové liště okna plátna.
     *
     * @return  Aktuální název okna
     */
    public String getNazev()
    {
        return okno.getTitle();
    }


    /***************************************************************************
     * Nastaví název v titulkové liště okna plátna.
     *
     * @param nazev  Nastavovaný název
     */
    public void setNazev(String nazev)
    {
        okno.setTitle(nazev);
    }


    /***************************************************************************
     * Vrátí vodorovnou souřadnici aplikačního okna plátna.
     *
     * @return Pozice levého horního rohu aplikačního okna plátna.
     */
    public Pozice getPozice()
    {
        return new Pozice(okno.getX(), okno.getY());
    }


    /***************************************************************************
     * Nastaví pozici aplikačního okna aktivního plátna na obrazovce.
     *
     * @param x  Vodorovná souřadnice aplikačního okna plátna.
     * @param y  Svislá souřadnice aplikačního okna plátna.
     */
    public synchronized void setPozice(int x, int y)
    {
        okno.setLocation(x, y);
        pozicePlatna = new Point(x, y);
    }


    /***************************************************************************
     * Nastaví pozici aplikačního okna aktivního plátna na obrazovce.
     *
     * @param pozice  Požadovaná pozice aplikačního okna plátna na obrazovce.
     */
    public void setPozice(Pozice pozice)
    {
        okno.setLocation(pozice.getX(), pozice.getY());
    }


    /***************************************************************************
     * Vrátí instanci třídy <code>Obrázek</code> zobrazující zadaný výřez
     * AktivníhoPlátna.
     * @param x     Vodorovná pozice požadovaného výřezu
     * @param y     Svislá pozice požadovaného výřezu
     * @param sirka Šířka požadovaného výřezu v bodech
     * @param vyska Výška požadovaného výřezu v bodech
     * @return Instance třídy <code>Obrázek</code> zobrazující zadaný výřez
     */
    public Obrazek getObrazek(int x, int y, int sirka, int vyska)
    {
        BufferedImage bim = getBufferedImage(x, y, sirka, vyska);
        return new Obrazek(0, 0, bim);
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Převede instanci na řetězec. Používá se především při ladění.
     *
     * @return Řetězcová reprezentace dané instance.
     */
    @Override
    public String toString()
    {
        return getClass().getName() + "(krok=" + krok +
                ", sirka=" + sloupcu + ", vyska=" + radku +
                ", pozadi=" + barvaPozadi + ")";
    }


    /***************************************************************************
     * Vykreslí všechny elementy.
     */
    public void prekresli()
    {
        if (kreslim) {    //Právě překresluji - volám nepřímo sám sebe
            return;
        }
        if ((nekreslit == 0)  &&  isViditelne())   //Mám kreslit a je proč
        {
            kreslim = true;
            synchronized(platno) {
                kreslitko.vyplnRam(0, 0, sirkaBodu, vyskaBodu,
                                   barvaPozadi);
                if ((mrizka < 0)  &&    //Má-li se kreslit pod obrazci
                    (barvaCar != barvaPozadi))
                {
                    kresliMrizku();
                }
                for (IKresleny predmet : predmety) {
                    predmet.nakresli(kreslitko);
                }
                if (mrizka > 0) {       //Má-li se kreslit nad obrazci
                    kresliMrizku();
                }
            }

            //Calls to repaint() don’t need to be done
            //from the event-dispatch thread
            platno.repaint();

            kreslim = false;        //Už nekreslím
        }
    }


    /***************************************************************************
     * Potlačí překreslování plátna, přesněji zvýší hladinu potlačení
     * překreslování o jedničku. Návratu do stavu před voláním této metody
     * se dosáhne zavoláním metody <code>vraťKresli()</code>.
     * <p>
     * Metody <code>nekresli()</code> a <code>vraťKresli()</code>
     * se tak chovají obdobně jako závorky, mezi nimiž je vykreslování
     * potlačeno.</p>
     */
    public synchronized void nekresli()
    {
        nekreslit++;
    }


    /***************************************************************************
     * Vrátí překreslování do stavu před posledním voláním metody
     * <code>nekresli()</code>. Předcházelo-li proto více volání metody
     * <code>nekresli()</code>, začne se překreslovat až po odpovídajím počtu
     * zavolání metody <code>vraťKresli()</code>.
     *
     * @throws IllegalStateException
     *         Je-li metoda volána aniž by předcházelo odpovídající volání
     *         <code>nekresli()</code>.
     */
    public synchronized void vratKresli()
    {
        if (nekreslit == 0) {
            throw new IllegalStateException(
                "Vraceni do stavu kresleni musi prechazet zakaz!");
        }
        nekreslit--;
        if (nekreslit == 0)  {
            prekresli();
        }
    }


    /***************************************************************************
     * Odstraní zadaný obrazec ze seznamu malovaných.
     * Byl-li obrazec v seznamu, překreslí plátno.
     *
     * @param obrazec  Odstraňovaný obrazec
     *
     * @return  true v případě, když obrazec v seznamu byl,
     *          false v případě, když nebylo co odstraňovat
     */
    public synchronized boolean odstran(IKresleny obrazec)
    {
        boolean ret = predmety.remove(obrazec);
        if (ret) {
            prekresli();
        }
        return ret;
    }


    /***************************************************************************
     * Vyčisti plátno, tj. vyprázdní seznam malovaných
     * (odstraní z něj všechny obrazce).
     */
    public void odstranVse()
    {
        nekresli(); {
            ListIterator<IKresleny> it = predmety.listIterator();
            while (it.hasNext()) {
                it.next();
                it.remove();
            }
        } vratKresli();
    }


    /***************************************************************************
     * Není-li zadaný obrazec v seznamu malovaných, přidá jej na konec
     * (bude se kreslit jako poslední, tj. na vrchu.
     * Byl-li obrazec opravdu přidán, překreslí plátno.
     * Objekty budou vždy kresleny v pořadí, v němž byly přidány do správy,
     * tj. v seznamu parametrů zleva doprava
     * a dříve zaregistrované objekty před objekty zaregistrovanými později.
     *
     * @param  obrazec  Přidávané obrazce
     * @return  Počet skutečně přidaných obrazců
     */
    public synchronized int pridej(IKresleny... obrazec)
    {
        int pocet = 0;
        nekresli(); {
            for (IKresleny ik : obrazec)
            {
                if (! predmety.contains(ik)) {
                    predmety.add(ik);
                    pocet++;
                }
            }
        } vratKresli();
        return pocet;
    }


    /***************************************************************************
     * Přidá obrazec do seznamu malovaných tak, aby byl kreslen
     * nad zadaným obrazcem.
     * Pokud již v seznamu byl, jenom jej přesune do zadané pozice.
     *
     * @param soucasny  Obrazec, který má byt při kreslení pod
     *                   přidávaným obrazcem
     * @param pridany   Přidávaný obrazec
     *
     * @return  {@code true}  v případě, když byl obrazec opravdu přidán,
     *          {@code false} v případě, když již mezi zobrazovanými byl
     *          a pouze se přesunul do jiné urovné
     */
    public synchronized boolean pridejNad(IKresleny soucasny,
                                          IKresleny pridany)
    {
        boolean nebyl = ! predmety.remove(pridany);
        int kam = predmety.indexOf(soucasny);
        if (kam < 0)
        {
            throw new IllegalArgumentException(
                "Referencni objekt neni na platne zobrazovan!");
        }
        predmety.add(kam+1, pridany);
        prekresli();
        return nebyl;
    }


    /***************************************************************************
     * Přidá obrazec do seznamu malovaných tak, aby byl kreslen
     * pod zadaným obrazcem.
     * Pokud již v seznamu byl, jenom jej přesune do zadané pozice.
     *
     * @param  soucasny  Obrazec, který má byt při kreslení nad
     *                   přidávaným obrazcem
     * @param  pridany   Přidávaný obrazec
     *
     * @return  true  v případě, když byl obrazec opravdu přidán,
     *          false v případě, když již mezi zobrazovanými byl
     *                a pouze se přesunul do jiné urovné
     */
    public synchronized boolean pridejPod(IKresleny soucasny,
                                          IKresleny pridany)
    {
        boolean nebyl = ! predmety.remove(pridany);
        int kam = predmety.indexOf(soucasny);
        if (kam < 0)
        {
            throw new IllegalArgumentException(
                "Referencni objekt neni na platne zobrazovan!");
        }
        predmety.add(kam, pridany);
        prekresli();
        return nebyl;
    }


    /***************************************************************************
     * Přidá obrazec do seznamu malovaných tak, aby byl kreslen
     * nad všemi obrazci.
     * Pokud již v seznamu byl, jenom jej přesune do požadované pozice.
     *
     * @param  pridany   Přidávaný obrazec
     *
     * @return  true  v případě, když byl obrazec opravdu přidán,
     *          false v případě, když již mezi zobrazovanými byl
     *                a pouze se přesunul do jiné urovné
     */
    public
    synchronized boolean pridejNavrch(IKresleny pridany)
    {
        boolean nebyl = ! predmety.remove(pridany);
        predmety.add(pridany);
        prekresli();
        return nebyl;
    }


    /***************************************************************************
     * Přidá obrazec do seznamu malovaných tak, aby byl kreslen
     * pod zadaným obrazcem.
     * Pokud již v seznamu byl, jenom jej přesune do zadané pozice.
     *
     * @param  pridany   Přidávaný obrazec
     *
     * @return  true  v případě, když byl obrazec opravdu přidán,
     *          false v případě, když již mezi zobrazovanými byl
     *                a pouze se přesunul do jiné urovné
     */
    public
    synchronized boolean pridejDospod(IKresleny pridany)
    {
        boolean nebyl = ! predmety.remove(pridany);
        predmety.add(0, pridany);
        prekresli();
        return nebyl;
    }


    /***************************************************************************
     * Vrátí pořadí zadaného prvku v seznamu kreslených prvků.
     * Prvky se přitom kreslí v rostoucím pořadí, takže obrazec
     * s větším poradím je kreslen nad obrazcem s menším poradím.
     * Neni-li zadaný obrazec mezi kreslenými, vrátí -1.
     *
     * @param  obrazec  Objekt, na jehož kreslicí pořadí se dotazujeme
     *
     * @return  Pořadí obrazce; prvý kresleny obrazec má pořadí 0.
     *          Neni-li zadaný obrazec mezi kreslenými, vrátí -1.
     */
    public
    int poradi(IKresleny obrazec)
    {
        return predmety.indexOf(obrazec);
    }


    /***************************************************************************
     * Vrátí nemodifikovatelný seznam všech spravovaných obrázků.
     *
     * @return  Požadovaný seznam
     */
    public
    List<IKresleny> seznamKreslenych()
    {
        return Collections.unmodifiableList(predmety);
    }


//     /***************************************************************************
//      * Nastaví, zda se mají přihlášeným posluchačům hlásit změny
//      * velikosti kroku a vrátí původní nastavení.
//      *
//      * @param hlásit  Požadované nastavení (true=hlásit, false=nehlasit).
//      * @return Původní nastavení
//      */
//     public boolean hlasitZmenyRozmeru(boolean hlasit)
//     {
//         boolean ret = hlasitZmenyRozmeru;
//         hlasitZmenyRozmeru = hlasit;
//         return ret;
//     }
// 

    /***************************************************************************
     * Přihlásí posluchače událostí klávesnice.
     *
     * @param posluchac  Přihlašovaný posluchač
     */
    public
    void prihlasKlavesnici(KeyListener posluchac)
    {
        okno.addKeyListener(posluchac);
    }


    /***************************************************************************
     * Odhlásí posluchače klávesnice.
     *
     * @param posluchac  Odhlašovaný posluchač
     */
    public
    void odhlasKlavesnici(KeyListener posluchac)
    {
        okno.removeKeyListener(posluchac);
    }


    /***************************************************************************
     * Přihlásí posluchače událostí myši.
     *
     * @param posluchac  Přihlašovaný posluchač
     */
    public
    void prihlasMys(MouseListener posluchac)
    {
        okno.addMouseListener(posluchac);
    }


    /***************************************************************************
     * Odhlásí posluchače myši.
     *
     * @param posluchac  Odhlašovaný posluchač
     */
    public
    void odhlasMys(MouseListener posluchac)
    {
        okno.removeMouseListener(posluchac);
    }


    /***************************************************************************
     * Uloží obraz aktivního plátna do zadaného souboru.
     *
     * @param soubor Soubor, do nějž se má obraz plátna uložit
     */
    public
    void ulozJakoObrazek(File soubor)
    {
        BufferedImage bim = getBufferedImage();
        try {
            ImageIO.write(bim, "PNG", soubor);
        } catch(IOException exc)  {
            throw new RuntimeException(
            	"\nObraz aktivniho platna se nepodarilo ulozit do souboru " +
                soubor,  exc);
        }
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================

    /***************************************************************************
     * Běží-li ve vlákně událostí, vykoná zadanou akci,
     * v opačném případě ji zařadí do vlákna a počká na její dokončení.
     *
     * @param akce Spuštěná akce
     * @param nazev Název akce pro kontrolní tisky
     */
    private static void invokeAndWait(Runnable akce, String nazev)
    {
        if (EventQueue.isDispatchThread()) {
            akce.run();
            return;
        }
        try {
            EventQueue.invokeAndWait(akce);
        }
        catch (Exception ex) {
            throw new RuntimeException( "\nSpusteni akce <<" + nazev +
                      ">> ve fronte udalosti se nezdarilo", ex);
        }
    }


    /***************************************************************************
     * Přečte parametry z konfiguračního souboru.
     * Tento soubor je umístěn v domovském adresáři uživatele
     * ve složce {@code .rup} v souboru {@code bluej.properties}.
     *
     * @return Pozice, na kterou se má umístit aplikační okno
     */
    private static Point konfiguraceZeSouboru()
    {
        Point pozice;

        Properties sysProp = System.getProperties();
        String     userDir = sysProp.getProperty("user.home");
        File       rupFile = new File(userDir, ".rup/bluej.properties");
        Properties rupProp = new Properties();
        try {
            Reader reader = new FileReader(rupFile);
            rupProp.load(reader);
            reader.close();
//             String sx = rupProp.getProperty("canvas.x");
//             String sy = rupProp.getProperty("canvas.y");
            int x = Integer.parseInt(rupProp.getProperty("canvas.x"));
            int y = Integer.parseInt(rupProp.getProperty("canvas.y"));
            pozice = new Point(x, y);
        }catch(Exception e)  {
            pozice = new Point(0, 0);
        }
        return pozice;
    }


    /***************************************************************************
     * Initialize a canvas manager by putting the initializing code
     * into the AWT Event Queue.
     */
    private static void inicializuj()
    {
        final Point   pozice  = konfiguraceZeSouboru();
        final Kutloch kutloch = new Kutloch();

        Runnable pripravSpravce = new Runnable() {
            @Override public void run()
            {
                pripravSpravce(pozice, kutloch);
            }
        };
        try {
            EventQueue.invokeAndWait(pripravSpravce);
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter  pw = new PrintWriter(sw);

            sw.write("\nCreation of CanvasManager doesn't succeed\n");
            ex.printStackTrace(pw);

            String msg = sw.toString();
            System.err.println(msg);
            JOptionPane.showMessageDialog(null, msg);

            System.exit(1);
        }

        //Správce je vytvořen, budeme umísťovat dialogové okna
        SpravcePlatna spravce = kutloch.spravce;
        int x = spravce.okno.getX();
        int y = spravce.okno.getY() + spravce.okno.getHeight();
        IO.oknaNa(x, y);

        //Vše je hotovo, můžeme atribut inicializovat
        SP = spravce;
        //Čáry se mohou vytvořit až po inicializaci správce,
        //protože si o něj třída čar řekne
        SP.pripravCary();

    }


    /***************************************************************************
     * Prepares a canvas manager and its application window
     * while running in AWT Event Queue.
     *
     * @param pozice  Position of the created application window
     * @param kutloch Přepravka pro umístění vraceného odkazu na správce
     */
    private static void pripravSpravce(Point pozice, Kutloch kutloch)
    {
        SpravcePlatna spravce = new SpravcePlatna();
        spravce.setNazev(TITULEK_0);
        spravce.setPozice(pozice.x, pozice.y);
        spravce.setKrokRozmerInterni(KROK_0, SIRKA_0, VYSKA_0);

        kutloch.spravce = spravce;
    }



//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vrátí obrázek na aktivním plátně.
     *
     * @return Obsah plátna jako obrázek
     */
    private BufferedImage getBufferedImage()
    {
        if (obrazPlatna instanceof BufferedImage) {
            return (BufferedImage) obrazPlatna;         //==========>
        }
        else {
            return getBufferedImage(0, 0, sirkaBodu, vyskaBodu);
        }
    }


    /***************************************************************************
     * Vrátí obrázek výřezu na aktivním plátně.
     *
     * @param x     Vodorovná pozice požadovaného výřezu
     * @param y     Svislá pozice požadovaného výřezu
     * @param sirka Šířka požadovaného výřezu v bodech
     * @param vyska Výška požadovaného výřezu v bodech
     * @return Výřez obsahu plátna jako obrázek
     */
    private BufferedImage getBufferedImage(int x, int y, int sirka, int vyska)
    {
        BufferedImage ret = new BufferedImage(sirka, vyska,
                                              BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D)ret.getGraphics();
        g2d.drawImage(obrazPlatna, -x, -y, Kreslitko.OBRAZOR);
        return ret;
    }


    /***************************************************************************
     * Byly-li by čáry mřížky vidět, nakreslí je.
     * Čáry mřížky mají šanci být viděny pokud se mřížka kreslí nad obrazci,
     * anebo pokud se kreslí pod nimi
     * a barva jejích čar se liší od barvy pozadí.
     */
    private void kresliMrizku()
    {
        if (krok > 1) {
            for (int i=0;   i < sloupcu;  ) {
                svisla[i++].nakresli(kreslitko);
            }
            for (int i=0;   i < radku;  ) {
                vodorovna[i++].nakresli(kreslitko);
            }
        }
    }


//     /***************************************************************************
//      * Obvolá všechny spravované přizpůsobivé objekty
//      * a oznámí jim změnu kroku.
//      *
//      * @param starýKrok Původní velikost kroku
//      *                  (aby si ji přizpůsobiví nemuseli pamatovat)
//      * @param novýKrok  Nová velikost kroku
//      */
//     private void obvolejPrizpusobive(int staryKrok, int novyKrok)
//     {
//         if (hlasitZmenyRozmeru  &&  (staryKrok != novyKrok)) {
//             nekresli(); {
//                 for (IKresleny ik : seznamKreslenych()) {
//                     if (ik instanceof IPrizpusobivy) {
//                         ((IPrizpusobivy)ik).krokZmenen(staryKrok, novyKrok);
//                     }
//                 }
//             } vratKresli();
//         }
//     }


    /***************************************************************************
     * Připraví čáry vyznačující jednotlivá pole aktivního plátna.
     * Pokud se čáry kreslit nemají, vyprázdní odkazy na ně.
     */
    private void pripravCary()
    {
        if ((mrizka != 0)  &&  (krok > 1))
        {
            if ((svisla == null)  ||  (svisla.length != sloupcu)) {
                svisla = new Cara[sloupcu];
            }
            if ((vodorovna == null)  ||
                (vodorovna.length != radku))
            {
                vodorovna = new Cara[radku];
            }
            for (int i=0, x=krok;   i < sloupcu;      i++, x+=krok) {
                svisla[i] = new Cara(x, 0, x, vyskaBodu, barvaCar);
            }
            for (int i=0, y=krok;   i < radku;   i++, y+=krok) {
                vodorovna[i] = new Cara(0, y, sirkaBodu, y, barvaCar);
            }
        }
        else
        {
            //Uvolnění doposud používaných instancí
            svisla    = null;
            vodorovna = null;
        }
    }


    /***************************************************************************
     *
     * @param krok krok
     * @param pSirka šířka
     * @param pVyska výška
     * @param menic měnič
     * @throws IllegalArgumentException výjimka
     * @throws IllegalStateException výjimka
     * @throws HeadlessException výjimka
     */
    private void setKrokRozmer_OverParametry(int krok,
                                             int pSirka, int pVyska,
                                             Object menic)
            throws IllegalArgumentException,
                   IllegalStateException,
                   HeadlessException
    {
        //Kontrola, jestli rozměry mění ten, kdo je měnit smí
        if ((menic != null)  &&
            (menic != vlastnikPovoleniZmenyKroku))
        {
            if (vlastnikPovoleniZmenyKroku == null) {
                vlastnikPovoleniZmenyKroku = menic;
            } else {
                throw new IllegalStateException(
                    "Zmena kroku a rozmeru neni danemu objektu povolena");
            }
        }
        //Ověření korektnosti zadaných parametrů
        Dimension obrazovka = Toolkit.getDefaultToolkit().getScreenSize();
        if ((krok   < 1)  ||
            (pSirka < 2)  ||  (obrazovka.width  < sirkaBodu) ||
            (pVyska < 2)  ||  (obrazovka.height < vyskaBodu))
        {
            throw new IllegalArgumentException(
                "\nSpatne zadane rozmery: " +
                "\n  krok =" + krok  + " bodu," +
                "\n  sirka=" + pSirka +
                   " poli = " + pSirka*krok + " bodu," +
                "\n  vyska=" + pVyska +
                   " poli = " + pVyska*krok + " bodu," +
                "\n  obrazovka= " + obrazovka.width  + "\u00d7" +
                                    obrazovka.height + " bodu\n");
        }
    }


    /***************************************************************************
     *
     * @param krok krok
     * @param pSirka šířka
     * @param pVyska výška
     */
    private void setKrokRozmerInterni(int krok, int pSirka, int pVyska)
    {
        sirkaBodu = pSirka * krok;
        vyskaBodu = pVyska * krok;

        okno.setResizable(true);
        platno.setPreferredSize(new Dimension(sirkaBodu, vyskaBodu));
        okno.pack();
        okno.setResizable(false);

        obrazPlatna = platno.createImage(sirkaBodu, vyskaBodu);
        kreslitko   = new Kreslitko((Graphics2D)obrazPlatna.getGraphics());
        kreslitko.setPozadi(barvaPozadi);

        this.krok    = krok;
        this.sloupcu = pSirka;
        this.radku   = pVyska;
        IO.Oprava.rozmerOkna(okno);
        IO.Oprava.poziceOkna(okno);
    }


    /***************************************************************************
     *
     * @param viditelne viditelnost okna
     */
    private void setViditelneInterni(final boolean viditelne)
    {
        okno.setVisible(viditelne);
        if (viditelne)
        {
            //Na WinXP při více obrazovkách po zviditelnění blblo
            //=> bylo třeba znovu nastavit pozici
            okno.setLocation(pozicePlatna);

            okno.setAlwaysOnTop(true);
            okno.toFront();
            prekresli();
            okno.pack();
            okno.setAlwaysOnTop(false);
        }
    }


    /***************************************************************************
     *
     */
    private void setViditelne_Pockam()
    {
        if (EventQueue.isDispatchThread()) {
            okno.setVisible(true);
            okno.pack();
            return;
        }
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                okno.setVisible(true);
                prekresli();
                okno.pack();
            }
        };
        try {
            EventQueue.invokeAndWait(runnable);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return;
        }
        catch (InvocationTargetException ex) {
            throw new RuntimeException(
                "\nVyjimka vyhozena behem nastavovani kroku a rozmeru", ex );
        }
////%A+ =999%
//        msgF("setViditelné_Počkám");
////%A-
    }



//== VNOŘENÉ A VNITŘNÍ TŘÍDY ===================================================

    /***************************************************************************
     * Přepravka, v níž uzávěr předává vytvořeného správce.
     */
    private static class Kutloch
    {
        volatile SpravcePlatna spravce;
    }



//== TESTY A METODA MAIN =======================================================
////%A+ =999%
//
//    private static void msgS(String text) {
////        rup.česky.utility.ThreadMessages.msgS(text);
//    }
//    private static void msg(String text) {
////        rup.česky.utility.ThreadMessages.msg(text);
//    }
//    private static void msgF(String text) {
////        rup.česky.utility.ThreadMessages.msgF(text);
//    }
//
//
//    /***************************************************************************
//     *
//     */
//    private static void w() {
//        try{
//            Thread.sleep(10);
//        }
//        catch(InterruptedException e){}
//    }
//
//
//    /***************************************************************************
//     * Testovaci metoda
//     */
//    public static void test2()
//    {
//        //Abych zaručil inicializovanost SP
//        SprávcePlátna sp = SprávcePlátna.getInstance();
//
//        sp.přidej(new Obdélník());                    w();
//        sp.přidej(new Elipsa(), new Trojúhelník());   w();
//        IO.zpráva("Výchozí obrázek - budu vyjímat výřez");
//        Obrázek obr = SP.getObrázek(50, 0, 75, 75);   w();
//        sp.přidej(obr);                               w();
//        sp.setBarvaPozadí(Barva.ČERNÁ);               w();
//        IO.zpráva("Obrázek přidaný?");
//        obr.setPozice(100, 50);                       w();
//        IO.zpráva("Posunutý?");
//        obr.setRozměr(150, 150);                      w();
//        IO.zpráva("Zvětšený?");
//        sp.setKrokRozměr(50, 5, 2);
////        SP.setKrokRozměr(1, 50, 50);
////        SP.uložJakoObrázek(new File("D:/SMAZAT.PNG"));
//
//        System.exit(0);
//    }
//
////%A-

    /***************************************************************************
     * Testovaci metoda
     */
    public static void test()
    {
        getInstance();  //Abych zaručil nastavenost SP
        IO.zprava("Platno vytvoreno");
        SP.pridej(new Obdelnik   (0, 0, 300, 300));
        SP.pridej(new Elipsa     (0, 0, 300, 300));
        SP.pridej(new Trojuhelnik(0, 0, 300, 300));
        IO.zprava("Prvni triumvirat nakreslen");

        SP.pridej(new Obdelnik   ( 99,  99, 102, 102),
                  new Elipsa     (100, 100, 100, 100),
                  new Trojuhelnik(100, 100, 100, 100));
        SP.setMrizka(true, true);
        IO.zprava("Druhy triumvirat nakreslen");

        System.exit(0);
    }
    /** @param args Paremtry příkazového řádku - nepoužité  */
    public static void main(String[] args) { test(); } /**/
////%A+ =999%
//    {
//        msgF("SprávcePlátna - deklarace atributů instance");
//    }
//    static {
//        msgF("SprávcePlátna - class constructor");
//    }
////%A-
}
