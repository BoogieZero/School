import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*******************************************************************************
 * Instnace třídy {@code Platno} (jedináček) slouží jako virtuální plátno,
 * na něž mohou být kresleny jednotlivé obrazce.
 * <p>
 * Třída neposkytuje veřejný konstruktor,
 * protože chce, aby její instance byla jedináček,
 * tj. aby se všechno kreslilo na jedno a to samé plátno.
 * Jediným způsobem, jak získat odkaz na instanci třídy Plátno,
 * je volaní statické metody {@link #getPlatno()}.</p>.
 * <p>
 * Aby bylo možno na plátno obyčejné kreslit
 * a nebylo nutno kreslené objekty přihlašovat,
 * odmazané časti obrazců se automaticky neobnovují.
 * Je-li proto při smazání některého obrazce odmazána část jiného obrazce,
 * je třeba příslušný obrazec explicitně překreslit.</p>
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public final class Platno
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Titulek v záhlaví okna plátna. */
    private static final String TITULEK  = "Jednoduche platno";

    /** Počáteční šířka plátna v bodech. */
    private static final int SIRKA_0 = 500;

    /** Počáteční výška plátna v bodech. */
    private static final int VYSKA_0 = 300;

    /** Počáteční barva pozadí plátna. */
    private static final Barva POZADI_0 = Barva.KREMOVA;



//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================

    /** Jediná instance třídy Plátno. */
    private static volatile Platno jedinacek;



//== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //Z venku neovlivnitelné Atributy pro zobrazeni plátna v aplikačním okně

        /** Aplikační okno animacniho plátna. */
        private JFrame okno;

        /** Instance lokální třídy, která je zřízena proto, aby odstínila
         *  metody svého rodiče JPanel. */
        private JPanel vlastniPlatno;

        /** Vše se kreslí na obraz -
         *  ten se snadněji překreslí. */
        private Image obrazPlatna;

        /** Kreslítko získané od obrazu plátna, na nějž se vlastně kreslí. */
        private Graphics2D kreslitko;


    //Atributy přímo ovlivnitelné uživatelem

        /** Barva pozadí při kreslení. */
        private Barva barvaPozadi;

        /** Šířka plátna v bodech. */
        private int sirka;

        /** Výška plátna v bodech. */
        private int vyska;

        /** Pozice plátna na obrazovace - při používání více obrazovek
         *  je občas třeba ji po zviditelnění obnovit. */
        Point pozicePlatna;



//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
//== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    /***************************************************************************
     * Smaže plátno, přesněji smaže všechny obrazce na plátně.
     * Tato metoda by měla býr definována jako metoda instance,
     * avšak protože je instance jedináček,
     * byla metoda pro snazší dostupnost definovaná jako metoda třídy,
     * aby nebylo potřeba před žádostí o smazání plátna vytvářet jeho instanci.
     */
    public static void smazPlatno()
    {
        jedinacek.smaz();
    }



//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Jediná metoda umožnující získat odkaz na instanci plátna.
     * Protože je však tato instance definována jako jedináček
     * Vrací metoda pokaždé odkaz na stejnou instanci.
     *
     * @return Odkaz na instanci třídy Plátno.
     */
    public static Platno getPlatno()
    {
        if (jedinacek == null) {
            synchronized(Platno.class) {
                if (jedinacek == null) {
                    inicializuj();
                }
            }
        }
        jedinacek.setViditelne(true);
        return jedinacek;
    }


    /***************************************************************************
     * Implicitní (a jediný) konstruktor.
     * Je volán pouze jednou, a to v deklaraci jedináčka.
     *
     * @param pozice Počáteční pozice aplikačního okna
     */
    @SuppressWarnings("serial")     //Kvůli anonymní třídě
    private Platno(Point pozice)
    {
        pozicePlatna = pozice;
        okno         = new JFrame();
        okno.setLocation(pozice);
        okno.setTitle(TITULEK);

        //Zavřením okna se zavře celá aplikace
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        vlastniPlatno = new JPanel()
        {   /** Povinně překrývaná abstraktní metoda třídy JPanel. */
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(obrazPlatna, 0, 0, null);
            }
        };
        okno.setContentPane(vlastniPlatno);
        barvaPozadi = POZADI_0;

        setRozmerSoukr(SIRKA_0, VYSKA_0);  //Připraví a vykreslí prázdné plátno
//        IO.Oprava.poziceOkna(okno);
        pripravObrazek();
        smaz();

        IO.oknaNa(pozicePlatna.x,
                  pozicePlatna.y + okno.getSize().height);
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Poskytuje informaci o aktuální viditelnosti okna.
     * Nicméně i viditelná okna mohou být zakryta jinými okny.
     *
     * @return Je-li okno viditelné, vrací <b>true</b>,
     *         jinak vrací <b>false</b>
     */
    public boolean isViditelne()
    {
        return okno.isVisible();
    }


    /***************************************************************************
     * Nastaví viditelnost plátna.
     *
     * @param viditelne {@code true} má-li být plátno viditelné,
     *                  {@code false} má-li naopak přestat být viditelné
     */
    public void setViditelne(boolean viditelne)
    {
        boolean zmena = (isViditelne() != viditelne);
        if (zmena) {
            if (! viditelne) {
                okno.setVisible(false);
            }
            else {
                if (java.awt.EventQueue.isDispatchThread()) {
                    setViditelneInterni();
                    return;
                }
                Runnable run = new Runnable() {
                    @Override
                    public void run()
                    {
                        setViditelneInterni();
                    }
                };
                try {
                    java.awt.EventQueue.invokeAndWait(run);
                }
                catch (Exception ex) {
                    throw new RuntimeException(
                            "\nChyba pri nastavovani viditelnosti", ex);
                }
            }
        }
    }


    /***************************************************************************
     * Vrátí aktuální barvu pozadí.
     *
     * @return   Nastavena barva pozadí
     */
    public Barva getBarvaPozadi()
    {
        return barvaPozadi;
    }


    /***************************************************************************
     * Nastaví pro plátno barvu pozadí.
     *
     * @param barva  Nastavovaná barva pozadí
     */
    public void setBarvaPozadi(Barva barva)
    {
        barvaPozadi = barva;
        kreslitko.setBackground( barvaPozadi.getColor() );
        smaz();
    }


    /***************************************************************************
     * Nastaví pro plátno barvu popředí.
     *
     * @param  barva  Nastavovaná barva popředí
     */
    public void setBarvaPopredi(Barva barva)
    {
        kreslitko.setColor( barva.getColor() );
    }


    /***************************************************************************
     * Vrátí šířku plátna.
     *
     * @return  Aktuální šířka plátna v bodech
     */
    public int getSirka()
    {
        return sirka;
    }


    /***************************************************************************
     * Vrátí výšku plátna.
     *
     * @return  Aktuální výška plátna v bodech
     */
    public int getVyska()
    {
        return vyska;
    }


    /***************************************************************************
     * Nastaví nový rozměr plátna zadáním jeho výsky a šířky.
     *
     * @param sirka  Nová šířka plátna v bodech
     * @param vyska  Nová výška plátna v bodech
     */
    public void setRozmer(int sirka, int vyska)
    {
        setRozmerSoukr(sirka, vyska);
        setViditelne(true);
        pripravObrazek();
        smaz();
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Aktuální barvou popředí nakresli na plátno úsečku
     * se zadanými krajními body a barvou.
     *
     * @param  x1    x-ová souřadnice počátku
     * @param  y1    y-ová souřadnice počátku
     * @param  x2    x-ová souřadnice konce
     * @param  y2    x-ová souřadnice konce
     * @param  barva Barva úsečky
     */
    public void kresliCaru(int x1, int y1, int x2, int y2, Barva barva)
    {
        setBarvaPopredi(barva);
        kreslitko.drawLine(x1, y1, x2, y2);
        vlastniPlatno.repaint();
    }


    /***************************************************************************
     * Vypíše na plátno text aktuálním písmem a aktuální barvou popředí.
     *
     * @param text   Zobrazovaný text
     * @param x      x-ová souřadnice textu
     * @param y      y-ová souřadnice textu
     * @param barva  Barva, kterou se zadaný text vypíše
     */
    public void kresliString(String text, int x, int y, Barva barva)
    {
        setBarvaPopredi(barva);
        kreslitko.drawString(text, x, y);
        vlastniPlatno.repaint();
    }


    /***************************************************************************
     * Smaže plátno, přesněji smaže všechny obrazce na plátně.
     */
    public void smaz()
    {
        smaz(new Rectangle2D.Double(0, 0, sirka, vyska));
    }


    /***************************************************************************
     * Smaže zadaný obrazec na plátně; obrazec vsak stalé existuje,
     * jenom není vidět. Smaže se totiž tak, že se nakreslí barvou pozadí.
     *
     * @param  obrazec   Obrazec, který má byt smazán
     */
    public void smaz(Shape obrazec)
    {
        Color original = kreslitko.getColor();
        kreslitko.setColor(barvaPozadi.getColor());
        kreslitko.fill(obrazec);       //Smaže jej vyplněním barvou pozadí
        kreslitko.setColor(original);
        vlastniPlatno.repaint();
    }


    /***************************************************************************
     * Vrátí string reprezentující danou instanci (podpis instance).
     * Používá se především při ladění.
     *
     * @return Řetězcová reprezentace dané instance.
     */
    @Override
    public String toString()
    {
        return this.getClass().getName() +
            "(" + sirka + "\u00d7" + vyska +
            " bodu, barvaPozadi=" + barvaPozadi + ")";
    }


    /***************************************************************************
     * Nakreslí zadaný obrazec a vybarví jej barvou popředí plátna.
     *
     * @param  obrazec  Kreslený obrazec
     */
    public void zapln(Shape obrazec)
    {
        kreslitko.fill(obrazec);
        vlastniPlatno.repaint();
    }



//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================

    /***************************************************************************
     * Inicializuje některé parametry z konfiguračního souboru.
     * Tento soubor je umístěn v domovském adresáři uživatele
     * ve složce {@code .rup} v souboru {@code bluej.properties}.
     * Je určen především pro učitele, aby jim usnadnil umisťování oken
     * při práci s několika monitolr, z nichž pouze jeden vidí studenti.
     *
     * @return bod
     */
    private static Point konfiguraceZeSouboru()
    {
        Point pozice;

        Properties sysProp = System.getProperties();
        String     userDir = sysProp.getProperty("user.home");
        File       rupFile = new File( userDir, ".rup/bluej.properties");
        Properties rupProp = new Properties();
        try {
            Reader reader = new FileReader(rupFile);
            rupProp.load(reader);
            reader.close();
//            String sx = rupProp.getProperty("canvas.x");
//            String sy = rupProp.getProperty("canvas.y");
            int x = Integer.parseInt(rupProp.getProperty("canvas.x"));
            int y = Integer.parseInt(rupProp.getProperty("canvas.y"));
            pozice = new Point( x, y );
        }catch( Exception e )  {
            pozice = new Point( 0, 0 );
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

        Runnable pripravSP = new Runnable() {
            @Override public void run()
            {
                pripravPlatno(pozice, kutloch);
            }
        };
        try {
            java.awt.EventQueue.invokeAndWait(pripravSP);
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
        Platno platno = kutloch.platno;
        int x = platno.okno.getX();
        int y = platno.okno.getY() + platno.okno.getHeight();
        IO.oknaNa(x, y);

        //Vše je hotovo, můžeme atribut inicializovat
        jedinacek = platno;

    }


    private static void pripravPlatno(Point pozice, Kutloch kutloch)
    {
        kutloch.platno = new Platno(pozice);
    }



//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Připraví obrázek, do nějž se budou všechny tvary kreslit.
     */
    private void pripravObrazek()
    {
        obrazPlatna = vlastniPlatno.createImage(sirka, vyska);
        kreslitko = (Graphics2D)obrazPlatna.getGraphics();
        kreslitko.setColor(barvaPozadi.getColor());
        kreslitko.fillRect(0, 0, sirka, vyska);
        kreslitko.setColor(Color.black);
    }


    /***************************************************************************
     * Nastaví zadaný rozměr plátna, ale pouze ten.
     * Soukromá verze určená pro konstruktor.
     * Veřejná verze přidává ještě zviditelnění plátna a přípravu obrázku.
     *
     * @param sirka  Nastavovaná bodová šířka plátna
     * @param vyska  Nastavovaná bodová výška plátna
     */
    private void setRozmerSoukr(int sirka, int vyska)
    {
//        boolean upravit;
//        Dimension dim;
//        Insets    ins;

        this.sirka = sirka;
        this.vyska = vyska;
        okno.setResizable(true);
        vlastniPlatno.setPreferredSize(new Dimension(sirka, vyska));
        okno.pack();

//        do {
//            this.šířka = šířka;
//            this.výška = výška;
//            okno.setResizable(true);
//            vlastníPlátno.setPreferredSize(new Dimension(šířka, výška));
//            okno.pack();
//            dim = okno.getSize();
//            ins = okno.getInsets();
////            IO.zpráva(
////                   "Nastavuju: šířka=" + šířka + ", výška=" + výška +
////                 "\nMám: width=" + dim.width + ", height=" + dim.height +
////                 "\nleft=" + ins.left + ", right=" + ins.right +
////                 "\n top=" + ins.top + ", bottom=" + ins.bottom );
//            upravit = false;
//            if (šířka < (dim.width - ins.left - ins.right)) {
//                šířka  = dim.width - ins.left - ins.right + 2;
//                upravit= true;
//            }
//            if (výška < (dim.height - ins.top - ins.bottom)) {
//                výška  = dim.height - ins.top - ins.bottom;
//                upravit= true;
//            }
//        } while (upravit);

        okno.setResizable(false);    //Není možné měnit rozměr pomocí myši
//        IO.Oprava.rozměrOkna(okno);
    }


    /***************************************************************************
     * Metoda volána z vlákna událostí.
     */
    private void setViditelneInterni()
    {
        pozicePlatna = okno.getLocation();
        okno.setVisible(true);

        //Při více obrazovkách po zviditelnění blbne =>
        okno.setLocation(pozicePlatna); //je třeba znovu nastavit pozici
        okno.setAlwaysOnTop(true);
        okno.toFront();
        okno.setAlwaysOnTop(false);

        okno.pack();
    }



//== VNOŘENÉ A VNITŘNÍ TŘÍDY ===================================================

    ////////////////////////////////////////////////////////////////////////////
    /***************************************************************************
     * Přepravka, v níž uzávěr předává vytvořené plátno.
     */
    private static class Kutloch
    {
        volatile Platno platno;
    }
    ////////////////////////////////////////////////////////////////////////////



//== TESTOVACÍ TŘÍDY A METODY ==================================================
}
