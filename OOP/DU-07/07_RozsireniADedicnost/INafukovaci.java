/*******************************************************************************
 * Instance rozhraní {@code INafukovaci} představují geometrické tvary,
 * které umějí prozradit a nastavit svoje rozměry.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public interface INafukovaci extends IKresleny
{
//== VEŘEJNÉ KONSTANTY =========================================================
//== DEKLAROVANÉ METODY ========================================================

    /***************************************************************************
     * Vrátí instanci třídy {@code Rozmer} s aktuálními rozměry instance.
     *
     * @return   Instance třídy {@code Rozmer} s aktuálními rozměry instance
     */
//     @Override
     public Rozmer getRozmer();


    /***************************************************************************
     * Nastaví nové rozměry instance.
     *
     * @param rozmer  Nově nastavovaný rozměr
     */
//     @Override
    public void setRozmer(Rozmer rozmer);


    /***************************************************************************
     * Nastaví nové rozměry instance. Nastavované rozměry musí být nezáporné,
     * místo nulového rozměru se nastaví rozměr rovný jedné.
     *
     * @param sirka    Nově nastavovaná šířka; šířka &gt;= 0
     * @param vyska    Nově nastavovaná výška; výška &gt;= 0
     */
//     @Override
    public void setRozmer(int sirka, int vyska);



//== ZDĚDĚNÉ METODY ============================================================
//== INTERNÍ DATOVÉ TYPY =======================================================
}
