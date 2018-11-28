
/**
 *Třída rozšiřuje třídu {@code Osoba} o znak na telě a písmeno "S". Dále upravuje 
 * metody potřebné pro přesuny a další práci s osobami.
 * 
 * @author Hamet Martin A14B0254P
 * @version 11.24.2014
 */
public class Superwoman extends Osoba
{
    
    /**
     * Udává poměr šířek znaku a těla.
     */
    private static final double POMER_ZNAK_TELO = 2.5/5.0;
    
    /**
     * Odkaz na instanci třídy {@code Trojuhelnik} reprezentující tělo superwoman.
     */
    private final Trojuhelnik superTelo;
    
    /**
     * Odkaz na instanci třídy {@code Trojuhelnik} reprezentující znak na tele
     * superwoman.
     */
    private final Trojuhelnik znak;
    
    /**
     * Udává posun znaku vůči tělu. (posun X =posun Y).
     */
    private final int posunZnaku;
    
    /**
     * Odkaz na instanci třídy {@code Text} reprezentující písmeno "S" ve znaku
     * superwoman.
     */
    private final Text pismenoS;
    
    /**
     * Hodnota posunu x-ové souřadnice znaku oproti tělu supoerwoman.
     */
    private final int xPosunSkTelu;
    
    /**
     * Hodnota posunu y-ové souřadnice znaku oproti tělu supoerwoman.
     */
    private final int yPosunSkTelu;
    
    /**
     * Vrátí odkaz na instanci třídy {@code Trojuhelnik} reprezentující tělo
     * superwoman.
     * 
     *  @return superTelo   odkaz na instanci třídy {@code Trojuhelnik}
     */
    public Trojuhelnik getSuperTelo(){
        return superTelo;
    }
    
    /**
     * Vrátí odkaz na instanci třídy {@code Trojuhelnik} reprezentující znak
     * superwoman.
     * 
     *  @return znak    odkaz na instanci třídy {@code Trojuhelnik} reprezentující
     *                  znak superwoman
     */
    public Trojuhelnik getZnak(){
        return znak;
    }
    
    /**
     * Vrátí odkaz na instanci třídy {@code Text} reprezentující písmeno "S" ve
     * znaku superwoman.
     * 
     *  @return pismenoS    odkaz na instanci třídy {@code Text} reprezentující písmeno
     *                      "S" ve znaku superwoman
     */
    public Text getPismenoS(){
        return pismenoS;
    }
    
    public void nakresli(Kreslitko kreslitko){
        super.nakresli(kreslitko);
        superTelo.nakresli(kreslitko);
        znak.nakresli(kreslitko);
        pismenoS.nakresli(kreslitko);
    }
    
    /**
     * Nastaví novou pozici osoby (opsaného obdelníku osoby). Dále nastaví pozici znaku i
     * písmena "S" superwoman vzhledem k jejímu tělu.
     */
    public void setPozice(int x, int y){
        Trojuhelnik pomTro = this.getSuperTelo();
        Trojuhelnik pomZnak = this.getZnak();
        
        //rodil souřadnic těla od hlavy
        int teloX = Math.abs(this.getX() - pomTro.getX());
        int teloY = Math.abs(this.getY() - pomTro.getY());
        
        //rodzíl souřadnic písmene od těla
        int pismenoX = pismenoS.getX() - pomTro.getX();
        int pismenoY = pismenoS.getY() - pomTro.getY();
        
        //rozdíl souřadnice znaku od těla
        int znakX = pomZnak.getX() - pomTro.getX();
        int znakY = pomZnak.getY() - pomTro.getY();
        
        super.setPozice(x, y);
        
        teloX += this.getX();
        teloY += this.getY();
        pomTro.setPozice(teloX, teloY);
        
        znakX += pomTro.getX();
        znakY += pomTro.getY();
        znak.setPozice(new Pozice(znakX, znakY));
        
        pismenoX += pomTro.getX();
        pismenoY += pomTro.getY();
        pismenoS.setPozice(pismenoX, pismenoY);
    }
    
    /**
     * Nastaví novou pozici osoby (opsaného obdelníku osoby). Dále nastaví i pozici znaku a písmene
     * "S" superwoman vzhledem k jeho tělu.
     * 
     *  @param pozice   nová pozice osoby reprezentovaná instancí třídy {@code Pozice}
     */
    public void setPozice(Pozice pozice){
        this.setPozice(pozice.getX(), pozice.getY());
    }
    
    /**
     * vytvoří instanci libovolného rozměru na zadané pozici
     * trojúhelníkové tělo {@code superTelo} je vždy černé,
     * šířky a výšky původního těla Osoby
     * na těle je červený trojúhelníkový znak {@code znak}
     * a na něm tučné žluté písmeno S {@code pismenoS}
     * 
     * @param   pozice pozice zobrazení
     * @param   velikostHlavy velikost hlavy, podle které se vytvoří poměrově
     *          osoba
     */
    public Superwoman(Pozice pozice, int velikostHlavy){
        super(pozice, velikostHlavy, Barva.ZADNA);
        
        //telo
        Obdelnik pomObd = super.getTelo();
        Rozmer rozmer = pomObd.getRozmer();
        this.superTelo = new Trojuhelnik(pomObd.getPozice(), rozmer, Barva.CERNA, Smer8.JIH);
        
        //znak
        Trojuhelnik pomTro = this.getSuperTelo();
        int pomSirka = pomTro.getSirka();
        Rozmer rozmerZnaku = new Rozmer((int)(pomSirka * POMER_ZNAK_TELO),
                                        (int)(pomSirka * POMER_ZNAK_TELO)
                                        );  
        this.posunZnaku = rozmerZnaku.getSirka()/2;
        //(pomSirka/2) - (rozmerZnaku.getSirka()/2);
        int znakX = pomTro.getX() + posunZnaku;
        int znakY = pomTro.getY() + posunZnaku;
        Pozice pomPoz = new Pozice(znakX, znakY);
        this.znak = new Trojuhelnik(pomPoz, rozmerZnaku, Barva.CERVENA, Smer8.JIH);
        
        //pismeno S na znaku
        int velikost = znak.getVyska();
        xPosunSkTelu = (int) (velikost / 1.4);
        yPosunSkTelu = velikost / 5;
        pismenoS = new Text(telo.getX() + xPosunSkTelu, telo.getY() + yPosunSkTelu, Barva.ZLUTA, "S");
        pismenoS.setFont("Serif", Text.TUCNY, velikost);
    }
    
    /**
     * Vytvoří instanci standardního rozměru na zadané pozici.
     * 
     *  @param pozice   pozice zobrazení
     */
    public Superwoman(Pozice pozice){
        this(pozice, IMPL_VELIKOST_HLAVY);
    }
    
    /**
     * Vytvoří instanci standardního rozměru v levém horním rohu plátna.
     */
    public Superwoman(){
        this(new Pozice(0,0));
    }
}
