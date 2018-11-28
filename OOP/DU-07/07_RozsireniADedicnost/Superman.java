
/**
 * Třída rozšiřuje třídu {@code Osoba} o znak na telě. Dále upravuje metody potřebné
 * pro přesuny a další práci s osobami.
 * 
 * @author Hamet Martin A14B0254P
 * @version 11.11.2014
 */
public class Superman extends Osoba
{
    /**
     * Udává poměr šířek znaku a těla.
     */
    private static final double POMER_ZNAK_TELO = 3.0/5.0;
    
    /**
     * Odkaz na instanci třídy {@code Trojuhelnik} rprezentující znak supermana.
     */
    private final Trojuhelnik znak;
    
    /**
     * Udává posun znaku vůči tělu. (posun X =posun Y).
     */
    private final int posunZnaku;
    
    /**
     * Vrátí odkaz na instanci třídy {@code Trojuhelnik} reprezentující znak supermana.
     * 
     *  @return znak    odkaz na instanci třídy {@code Trojuhelnik}
     */
    public Trojuhelnik getZnak(){
        return znak;
    }
    
    /**
     * Nastaví novou pozici osoby (opsaného obdelníku osoby). Dále nastaví i pozici znaku
     * supermana vzhledem k jeho tělu.
     * 
     *  @param x    nová souřadnice X opsaného obdelníku osoby
     *  @param y    nová souřadnice Y opsanho obdelníku osoby
     */
    @Override
    public void setPozice(int x, int y){
        super.setPozice(x, y);
        
        Obdelnik pomObd = this.getTelo();
        int znakX = pomObd.getX() + posunZnaku;
        int znakY = pomObd.getY() + posunZnaku;
        
        znak.setPozice(new Pozice(znakX, znakY));
    }
    
    /**
     * Nastaví novou pozici osoby (opsaného obdelníku osoby). Dále nastaví i pozici znaku
     * supermana vzhledem k jeho tělu.
     * 
     *  @param p    nová pozice osoby reprezentovaná instancí třídy {@code Pozice}
     */
    public void setPozice(Pozice p){
        this.setPozice(p.getX(), p.getY());
    }
    
    /**
     * Vykreslí na plátno osobu i se znakem.
     * 
     *  @param kreslitko     nástroj pro kreslení
     */
    @Override
    public void nakresli(Kreslitko kreslitko){
        super.nakresli(kreslitko);
        znak.nakresli(kreslitko);
    }
    
    /**
     * vytvoří instanci libovolného rozměru na zadané pozici
     * tělo je vždy modré, standardních rozměrů Osoby
     * znak je vždy červený orientovaný vrcholem dolů
     * znak je v poměru k tělu a je odsazen o {@code posunZnaku}
     * vůči X i Y souřadnici těla
     * 
     *  @param pozice           pozice zobrazení
     *  @param velikostHlavy    velikost hlavy, podle které se vytvoří poměrově osoba
     */
    public Superman(Pozice pozice, int velikostHlavy)
    {
        super(pozice, velikostHlavy, Barva.MODRA);
        
        Obdelnik pomObd = this.getTelo();
        int pomSirka = pomObd.getSirka();
        
        Rozmer rozmerZnaku = new Rozmer((int)(pomSirka * POMER_ZNAK_TELO), 
                                        (int)(pomSirka * POMER_ZNAK_TELO)
                                        );
                                        
        this.posunZnaku = (pomSirka/2) - (rozmerZnaku.getSirka()/2);
        
        int znakX = pomObd.getX() + posunZnaku;
        int znakY = pomObd.getY() + posunZnaku;
        
        Pozice pomPoz = new Pozice(znakX, znakY);
        
        this.znak = new Trojuhelnik(pomPoz, rozmerZnaku, Barva.CERVENA, Smer8.JIH);
    }
    
    /**
     * Vytvoří instanci standardního rozměru na zadané pozici.
     * 
     *  @param pozice   pozice zobrazení
     */
    public Superman(Pozice pozice){
        this(pozice, IMPL_VELIKOST_HLAVY);
    }
    
    /**
     * Vytvoří instanci standardního rozměru v levém horním rohu plátna.
     */
    public Superman(){
        this(new Pozice(0, 0));
    }

}
