/*******************************************************************************
 * Instance rozhraní {@code IPosuvny} představují geometrické tvary,
 * které umějí prozradit a nastavit svoji pozici.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public interface IPosuvny extends IKresleny
{
//== VEŘEJNÉ KONSTANTY =========================================================
//== DEKLAROVANÉ METODY ========================================================

    /***************************************************************************
     * Vrátí instanci třídy {@code Pozice} s aktuální pozicí instance.
     *
     * @return  Instance třídy {@code Pozice} s aktuální pozicí instance
     */
//     @Override
    public Pozice getPozice();


    /***************************************************************************
     * Nastaví novou pozici instance.
     *
     * @param pozice   Nastavovaná pozice instance
     */
//     @Override
    public void setPozice(Pozice pozice);


    /***************************************************************************
     * Nastaví novou pozici instance.
     *
     * @param x  Nově nastavovaná vodorovná (x-ová) souřadnice instance,
     *           x=0 má levý okraj plátna, souřadnice roste doprava
     * @param y  Nově nastavovaná svislá (y-ová) souřadnice instance,
     *           y=0 má horní okraj plátna, souřadnice roste dolů
     */
//     @Override
    public void setPozice(int x, int y);



//== ZDĚDĚNÉ METODY ============================================================
//== INTERNÍ DATOVÉ TYPY =======================================================
}
