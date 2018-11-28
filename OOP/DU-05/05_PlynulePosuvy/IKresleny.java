/*******************************************************************************
 * Rozhraní {@code IKresleny} musí implementovat všechny třídy, které chtějí,
 * aby jejich instance byly přijaty do správy instance {@code SpravcePlatna},
 * která vyžaduje, aby se na požádání uměly nakreslit prostřednictvím
 * dodaného kreslítka.
 * <p>
 * Implementací tohoto rozhraní třída současně slibuje, že bude správce plátna
 * okamžitě informovat o jakýchkoliv změnách ve svém umístění a vzhledu,
 * které chce promítnout do své podoby na plántě. O tom, že se něco změnilo,
 * informuje správce plátna zavoláním jeho metody
 * {@code SpravcePlatna.prekresli()}
 *
 * @author  Rudolf PECINOVSKÝ
 * @version 1.09.2629 — 2011-01-03
 */
public interface IKresleny
{
//== VEŘEJNÉ KONSTANTY =========================================================
//== DEKLAROVANÉ METODY ========================================================

    /***************************************************************************
     * Prostřednictvím dodaného kreslítka vykreslí obraz své instance.
     *
     * @param kreslitko Kreslítko, které nakreslí instanci
     */
//     @Override
    public void nakresli(Kreslitko kreslitko);


//== ZDĚDĚNÉ METODY ============================================================
//== VNOŘENÉ TŘÍDY =============================================================
}
