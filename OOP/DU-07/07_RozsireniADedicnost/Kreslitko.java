import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

/*******************************************************************************
 * Třída {@code Kreslitko} slouží k zprostředkování kreslících schopností
 * objektům přihlášeným u {@code SprávcePlátna}.
 * Je konstruován jako adaptér objektu {@code java.awt.Graphics2D}.
 *
 * Třída {@code Kreslitko} slouží ke zprostředkování komunikace kreslených tvarů
 * s příslušným grafickým kontextem. Jejím hlavím úkolem je zjednodušit
 * tuto komunikaci pro začínající uživatele. Je to vlastně adaptér
 * grafického kontextu na české prostředí tvarů.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public class Kreslitko
{
//== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================

    /** Pozorovatel obrázků, který ví, že pozorované obrázky
     * se již nebudou měnit, protože jsou celé načtené hned od počátku. */
    public static final ImageObserver OBRAZOR = new ImageObserver();


//== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
//== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================

    /** Adaptovaný grafický kontext. */
    private Graphics2D g;



//== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
//== NESOUKROMÉ METODY TŘÍDY ===================================================

//##############################################################################
//== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    /***************************************************************************
     * Inicializuje atribut adaptovaným objektem.
     * Předpokládá pouze spolupráci se správcem plátna,
     * který bude ve stejném balíčku - proto je package private.
     *<p>
     * Vytvoří nové kreslítko jako obalový typ
     * kolem zadané instance třídy <code>java.awt.Graphics2D</code>.
     *
     * @param g Obalovaná instance
     */
    Kreslitko( Graphics2D g )
    {
        this.g = g;
    }



//== ABSTRAKTNÍ METODY =========================================================
//== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================

    /***************************************************************************
     * Vrátí font, kterým se sází vypisované texty.
     *
     * @return Aktuálně používaný font
     */
    public Font getFont()
    {
        return g.getFont();
    }


    /***************************************************************************
     * Nastaví font, kterým se budou sázet vypisované texty.
     * Všechny následující operace používají tento font.
     * Parametr {@code null} je tiše ignorován.
     *
     * @param font  Nastavované písmo.
     */
    public void setFont( Font font )
    {
        g.setFont( font );
    }


    /***************************************************************************
     * Vrátí aktuálně používanou barvu pozadí.
     *
     * @return Aktuálně používaná barva pozadí
     */
    public Barva getPozadi()
    {
        Color color = g.getBackground();
        Barva barva = Barva.getBarva(
                                color.getRed(),  color.getGreen(),
                                color.getBlue(), color.getAlpha());
        return barva;
    }


    /***************************************************************************
     * Nastaví barvu pozadí.
     *
     * @param barva Nastavovaná barva pozadí
     */
    public void setPozadi( Barva barva )
    {
        g.setBackground( barva.getColor() );
    }


    /***************************************************************************
     * Vrátí zabalený grafický kontext.
     *
     * @return Zabalený grafický kontext
     */
    public Graphics2D getGraphics()
    {
        return g;
    }



//== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================

    /***************************************************************************
     * Vykreslí zadanou barvou na zadaných souřadnicích nevyplněný ovál
     * zadaného rozměru. Souřadnicí elipsyse přitom rozumí souřadnice
     * levého horního rohu opsaného obdélníku.
     *
     * @param x       x-ová souřadnice instance, x=0 má levý okraj plátna
     * @param y       y-ová souřadnice instance, y=0 má horní okraj plátna
     * @param sirka   Šířka kresleného oválu
     * @param vyska   Výška kresleného oválu
     * @param barva   Barva kresleného oválu
     */
    public void kresliOval( int x, int y, int sirka, int vyska, Barva barva )
    {
        g.setColor( barva.getColor() );
        g.drawOval( x, y, sirka, vyska );
    }


    /***************************************************************************
     * Vykreslí zadanou barvou na zadaných souřadnicích vyplněný ovál
     * zadaného rozměru. Souřadnicí elipsyse přitom rozumí souřadnice
     * levého horního rohu opsaného obdélníku.
     *
     * @param x       x-ová souřadnice instance, x=0 má levý okraj plátna
     * @param y       y-ová souřadnice instance, y=0 má horní okraj plátna
     * @param sirka   Šířka kresleného oválu
     * @param vyska   Výška kresleného oválu
     * @param barva   Barva kresleného oválu
     */
    public void vyplnOval( int x, int y, int sirka, int vyska, Barva barva )
    {
        g.setColor( barva.getColor() );
        g.fillOval( x, y, sirka, vyska );
    }


    /***************************************************************************
     * Vykreslí zadanou barvou na zadaných souřadnicích nevyplněný obdélník
     * zadaného rozměru. Souřadnicí obdélníku se přitom rozumí souřadnice
     * jeho levého horního rohu.
     *
     * @param x       x-ová souřadnice instance, x=0 má levý okraj plátna
     * @param y       y-ová souřadnice instance, y=0 má horní okraj plátna
     * @param sirka   Šířka kresleného obdélníku
     * @param vyska   Výška kresleného obdélníku
     * @param barva   Barva kresleného obdélníku
     */
    public void kresliRam( int x, int y, int sirka, int vyska, Barva barva )
    {
        g.setColor( barva.getColor() );
        g.drawRect( x, y, sirka, vyska );
    }


    /***************************************************************************
     * Vykreslí zadanou barvou na zadaných souřadnicích vyplněný obdélník
     * zadaného rozměru. Souřadnicí obdélníku se přitom rozumí souřadnice
     * jeho levého horního rohu.
     *
     * @param x       x-ová souřadnice instance, x=0 má levý okraj plátna
     * @param y       y-ová souřadnice instance, y=0 má horní okraj plátna
     * @param sirka   Šířka kresleného obdélníku
     * @param vyska   Výška kresleného obdélníku
     * @param barva   Barva kresleného obdélníku
     */
    public void vyplnRam( int x, int y, int sirka, int vyska, Barva barva )
    {
        g.setColor( barva.getColor() );
        g.fillRect( x, y, sirka, vyska );
    }


    /***************************************************************************
     * Vykreslí zadanou barvou nevyplněný mnohoúhelník se zadnými vrcholy.
     *
     * @param x     Pole x-ových souřadnic vrcholů, x=0 má levý okraj plátna
     * @param y     Pole y-ových souřadnic vrcholů, y=0 má horní okraj plátna
     * @param barva Barva kresleného polygonu
     */
    public void kresliPolygon( int[] x, int[] y, Barva barva )
    {
        g.setColor( barva.getColor() );
        g.drawPolygon( x, y, Math.min(x.length, y.length) );
    }


    /***************************************************************************
     * Vykreslí zadanou barvou vyplněný mnohoúhelník se zadnými vrcholy.
     *
     * @param x     Pole x-ových souřadnic vrcholů, x=0 má levý okraj plátna
     * @param y     Pole y-ových souřadnic vrcholů, y=0 má horní okraj plátna
     * @param barva Barva kresleného polygonu
     */
    public void vyplnPolygon( int[] x, int[] y, Barva barva )
    {
        g.setColor( barva.getColor() );
        g.fillPolygon( x, y, Math.min(x.length, y.length) );
    }


    /***************************************************************************
     * Zadanou barvou nakreslí úsečku se zadanými krajními body.
     *
     * @param x1      x-ová souřadnice počátku, x=0 má levý okraj plátna
     * @param y1      y-ová souřadnice počátku, y=0 má horní okraj plátna
     * @param x2      x-ová souřadnice konce
     * @param y2      y-ová souřadnice konce
     * @param barva Barva kreslené úsečky
     */
    public void kresliCaru( int x1, int y1, int x2, int y2, Barva barva )
    {
        g.setColor( barva.getColor() );
        g.drawLine( x1, y1, x2, y2 );
    }


    /***************************************************************************
     * Vykreslí přednastaveným písmem zadaný trext na zadaných souřadnicích
     * a zadanou barvou.
     *
     * @param text  Zobrazovaný text
     * @param x     Vodorovná souřadnice levého horního rohu opsaného obdélníku
     * @param y     Svislá souřadnice levého horního rohu opsaného obdélníku
     * @param barva Barva vykreslovaného textu
     */
    public void kresliText( String text, int x, int y, Barva barva )
    {
        g.setColor( barva.getColor() );
        g.drawString( text, x, y );
    }


    /***************************************************************************
     * Nakreslí zadaný obrázek na zadané souřadnice.
     * 
     * @param x       Vodorovná souřadnice levého horního rohu
     * @param y       Svislá souřadnice levého horního rohu
     * @param obrazek Kreslený obrázek
     * @param at      Afinní transformace použitá při změně rozměru obrázku
     */
    public void kresliObrazek( int x, int y, Image obrazek,
                               AffineTransform at )
    {
        if( at == null ) {
            g.drawImage(obrazek, x, y, OBRAZOR);
        }
        else {
            g.drawImage(obrazek, at, OBRAZOR);
        }
    }


//== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
//== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
//== VNOŘENÉ A VNITŘNÍ TŘÍDY ===================================================

    /*******************************************************************************
     * Instance třídy ImageObserver slouží k předání informací o tom,
     * že požadovaný obrázek je již načten.
     */
    public static class ImageObserver implements java.awt.image.ImageObserver
    {
        /***************************************************************************
         * Metoda je volaná v okamžiku, kdy jsou k dispozici infomrace o před
         * tím požadovaném obrázku.
         * {@inheritDoc}
         * @param img       Pozorovaný obrázek
         * @param infoflags Bitový OR několika příznaků
         * @param x         Vodorovná souřadnice
         * @param y         Svislá souřadnice
         * @param width     Šířka
         * @param height    Výška
         * @return <code>true</code> bude-li se obrázek ještě v budnoucnu měnit
         *         (není ještě celý načten),
         *         <code>false</code> je-li definitivní
         */
        @Override
        public boolean imageUpdate( Image img, int infoflags,
                       int x, int y, int width, int height )
        {
            //Obrázek se již nebude měnit tím, že by se donačetl
            return false;
        }
    }

//== TESTY A METODA MAIN =======================================================
}
