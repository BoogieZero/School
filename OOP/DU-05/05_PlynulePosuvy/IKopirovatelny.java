/*******************************************************************************
 * Instance rozhraní {@code IKopirovatelny} jsou schopny vytvořit svoji kopii.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public interface IKopirovatelny
{
//== VEŘEJNÉ KONSTANTY =========================================================
//== DEKLAROVANÉ METODY ========================================================

    /***************************************************************************
     * Vrátí svoji kopii, tj. instanci s naprosto shodnými vlastnostmi
     * s výjimkou těch, které podle kontraktu shodné být nesmějí.
     *
     * @return Požadovaná kopie
     */
//     @Override
    public IKopirovatelny kopie();



//== ZDĚDĚNÉ METODY ============================================================
//== INTERNÍ DATOVÉ TYPY =======================================================
}
