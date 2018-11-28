
/**
 * Write a description of class Zvyraznovac here.
 * 
 * @author Hamet Martin A14B0254P
 * @version 28.10.2014
 */
public class Zvyraznovac
{
    /**
     * Odkaz na instanci SpravcePlatna pro kreslení na plátno.
     */
    private static final SpravcePlatna SP = SpravcePlatna.getInstance();
    
    /**
     * Udává počet pixelů, které se standardně přidají na každou stranu zvýrazňovaného obrazce.
     */
    private static final int IMPLICITNE_PRIDANO = 10;
    
    /**
     * Udává počet pixelů, které se skutečně přidají na každou stranu zvýrazňovaného obrazce.
     */
    private int pridano;

    /**
     * Připraví novou instanci třídy Zvyraznovac a nastaví počáteční hodnoty zvětšení zvýrazňovaného objektu.
     */
    public Zvyraznovac()
    {
        this.pridano = IMPLICITNE_PRIDANO;
    }
    
    /**
     * Připraí novou intanci třídy Zvyraznovac se zadaným zvětšením zvýrazňovaného objektu.
     *  @param pridano  počet pixelů o které je větší zvýraznění objektu na všechny strany
     */
    public Zvyraznovac(int pridano){
        this.pridano = pridano;
    }
    
    /**
     * Zvýrazní kreslený objekt tím, že danou barvou vykreslí na pozadí objektu
     * ohraničující obdelník, jehož rozměry jsou na všechny strany zvětšeny oproti
     * rozměrům zvýrazňovaného objektu.
     * 
     *  @param objekt   je zvýrazňovaný objekt
     *  @param barva    je barva zvýrazňujícího obdelníka na pozadí
     *  @return         zvýrazňující obdelník
     */
    public Obdelnik zvyrazniPozadi(IZvyrazneny objekt, Barva barva){
        Oblast pom = zjistiOblastZvyrazneni(objekt);
        Obdelnik novyObd = new Obdelnik(pom, barva);
        SP.pridejDospod(novyObd);
        return novyObd;
    }
    
     /**
     * Zvýrazní kreslený objekt tím, že danou barvou vykreslí na pozadí objektu
     * ohraničující elipsu, jejíž rozměry jsou na všechny strany zvětšeny oproti
     * rozměrům zvýrazňovaného objektu.
     * 
     *  @param objekt   je zvýrazňovaný objekt
     *  @param barva    je barva zvýrazňujícího obdelníka na pozadí
     *  @return         zvýrazňující elipsa
     */
    public Elipsa zvyrazniPozadiElipsou(IZvyrazneny objekt, Barva barva){
        Oblast pom = zjistiOblastZvyrazneni(objekt);
        Elipsa novaEl = new Elipsa(pom, barva);
        SP.pridejDospod(novaEl);
        return novaEl;
    }
    
    /**
     * Vrátí oblast zvrýrazňovaného objektu zvětšenou na všechny strany.
     *  
     *  @param objekt   zvyýrazňovaný objekt
     *  @return oblast  odkaz na instanci třídy Oblast, která představuje 
     *                  prostor ve kterém bude vykreslen zvýrazňující tvar
     */
    public Oblast zjistiOblastZvyrazneni(IZvyrazneny objekt){
        int pozX = objekt.getPozice().getX()-pridano;
        int pozY = objekt.getPozice().getY()-pridano;
        int sirka = objekt.getSirka()+(2*pridano);
        int vyska = objekt.getVyska()+(2*pridano);
        
        Oblast obl = new Oblast(pozX, pozY, sirka, vyska);
        return obl;
    }

}
