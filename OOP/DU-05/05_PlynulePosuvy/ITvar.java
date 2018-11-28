/*******************************************************************************
 * Instance rozhraní {@code ITvar} představují obecné geometrické tvary
 * určené pro práci na virtuálním plátně při seznamování s třídami a objekty.
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public interface ITvar extends IHybaci, IKopirovatelny
{
//== VEŘEJNÉ KONSTANTY =========================================================
//== DEKLAROVANÉ METODY ========================================================

    /***************************************************************************
     * Vrátí svoji kopii, tj. instanci s naprosto shodnými vlastnostmi
     * s výjimkou těch, které podle kontraktu shodné být nesmějí - v tomto
     * případě stejný tvar, stejně velký, stejně umístěný a se stejnou barvou.
     * <p>
     * Oproti verzi zděděné z předka {@code IKopirovatelny} omezuje typ
     * vrácené hodnoty na sebe sama, tj. na {@code ITvar}.
     *
     * @return Požadovaná kopie
     */
    @Override
    public ITvar kopie();



//== ZDĚDĚNÉ METODY ============================================================
//== INTERNÍ DATOVÉ TYPY =======================================================
}
