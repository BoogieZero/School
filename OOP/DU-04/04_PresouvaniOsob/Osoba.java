
/**
 * Instance třídy Osoba představují celé osoby složené z instancí tříd Elipsa a Obdelník.
 * Osoby jsou definované barvou hlavy, barvou těla, jejich rozměry a umístěním celé osoby
 * na plátně. Umístěním osoby se rozumí pozice levého horního rohu nejměnšího možného opsaného 
 * obdelníku osoby.efwef
 * 
 * @author Hamet Martin A14B0254P
 * @version 10.9.2014
 */
public class Osoba
{
    //-KONSTANTNÍ ATRIBUTY-
    
    /**
     * Počáteční barva hlavy osoby.
     * {@code Barva.RUZOVA}
     */
    private static final Barva BARVA_HLAVY = Barva.RUZOVA;
    
    /**
     * Počáteční barva těla osoby.
     * {@code Barva.SEDA}
     */
    private static final Barva IMPL_BARVA_TELA = Barva.SEDA;
    
    /**
     * Počátení velikost hlavy osoby.
     */
    private static final int IMPL_VELIKOST_HLAVY = 60;
    
    /**
     * Počáteční poměr hlava/tělo osoby.
     */
    private static final double POMER_HLAVA_TELO = 6.0/8.0;
    
    /**
     * Počáteční poměr výška/šírka těla osoby.
     */
    private static final double POMER_TELO_VYS_SIR = 8.0/7.0;
    
    //-PROMĚNNÉ ATRIBUTY-
    
    /**
     * Instance třídy Elipsa reprezentující hlavu osoby.
     */
    private final Elipsa hlava;
    
    /**
     * Instance třídy Obdelník reprezentující tělo osoby.
     */
    private final Obdelnik telo;
    
    /**
     * Proměnná pro čítání počtu vytvořených osob.
     */
    private static int pocet=0;
    
    /**
     * Proměnná pro čítání počtu vytvořených osob.
     */
    private final int PORADI=++pocet;
    
    /**
     * Název osoby ve tvaru:    "Osoba_X"
     *  kde X je číslo dané osoby.
     */
    private String nazev = null;
    
    //-PŘÍSTUPOVÉ METODY-
    
    /**
     * Vrátí odkaz na instanci třídy Elipsa reprezentující hlavu osoby.
     * 
     * @return odkaz na instanci třídy Elipsa
     */
    public Elipsa getHlava(){
        return hlava;
    }
    
    /**
     * Vrátí odkaz na instanci třídy Obdelník reprezentující tělo osoby.
     * 
     * @return odkaz na instanci třídy Obdelník
     */
    public Obdelnik getTelo(){
        return telo;
    }
    
    /**
     * Vrátí (vodorovnou) souřadnici X levého horního rohu opsanho obdelníku osoby.
     *  V levém horním rohu plátna je X=0,
     *  souřadnice X roste směrem doprava.
     * 
     * @return aktuální hodnota souřadnice X osoby
     */
    public int getX(){return Math.min(telo.getX(),hlava.getX());}
    
    /**
     * Vrátí (svislou) souřadnici Y levého horního rohu opsanho obdelníku osoby.
     *  V levém horním rohu plátna je Y=0,
     *  souřadnice Y roste směrem dolů.
     *  
     * @return aktuální hodnota souřadnice Y osoby
     */
    public int getY(){return hlava.getY();
    }
    
    /**
     * Vrátí název osoby jako řetězec String ve tvaru: "NÁZEV_TŘÍDY_X" kde:
     *  NÁZEV_TŘÍDY je název instance {@code this.getClass().getSimpleName()}
     *  X           je číslo osoby.
     * 
     * @return vrátí úplný název osoby
     */
    public String getNazev(){
        nazev = this.getClass().getSimpleName()+"_"+PORADI;
        return nazev;
    }
    
    /**
     * Vrátí šířku opsaného obdelníku osoby.
     * 
     * @return šířka osoby
     */
    public int getSirka(){return Math.max(telo.getSirka(),hlava.getSirka());}
    
    /**
     * Vrátí výšku opsaného obdelníku osoby.
     * 
     * @return výška osoby
     */
    public int getVyska(){return (telo.getVyska()+hlava.getVyska());}
    
    /**
     * Vrátí odkaz na instanci třídy Barva instance Obdelník.
     *  Instance Obdelník představuje tělo osoby.
     *  Instance Barva představuje barvu těla osoby.
     * 
     * @return odkaz na instanci třídy Barva reprezentující barvu těla osoby
     */
    public Barva getBarvaTela(){return telo.getBarva();}
    
    /**
     * Vytvoří novou instanci třídy pozice s parametry x,y ,kterou vrátí.
     * 
     * @return  odkaz na instanci třídy Pozice reprezentující pozici osoby
     */
    public Pozice getPozice(){
        return new Pozice(this.getX(),this.getY());
    }
    
    /**
     * Vrátí rozměr osoby.
     * 
     * @return odkaz na instanci třídy Rozměr reprezentující rozměry osoby
     */
    public Rozmer getRozmer(){
        return new Rozmer(this.getSirka(), this.getVyska());
    }
    
    /**
     * Vrátí osobu běžného muže vytvořeného na zadané pozici s modrou barvou těla.
     * 
     * @param pozice    požadovaná pozice běžného muže určená instancí třídy Pozice
     * 
     * @return  běžný muž reprezentovaný instancí třídy Osoba
     */
    public static Osoba getBeznyMuz(Pozice pozice){
        return new Osoba(pozice.getX(),pozice.getY(),Barva.MODRA);
    }
    
    /**
     * Vrátí osobu běžné ženy vytvořenéh na zadané pozici s červenou barvou těla.
     * 
     * @param pozice    požadovaná pozice běžné ženy určená instancí třídy Pozice
     * 
     * @return  běžná žena reprezentovaná instancí třídy Osoba
     */
    public static Osoba getBeznaZena(Pozice pozice){
        return new Osoba(pozice.getX(),pozice.getY(),Barva.CERVENA);
    }
    
    /**
     * Nastaví barvu těla osoby.
     * 
     * @param barvaTela požadovaná barva těla
     */
    public void setBarvaTela(Barva barvaTela){
        telo.setBarva(barvaTela);
    }
    
    /**
     * Nastaví novou pozici osoby (opsaného obdelníku osoby).
     * 
     * @param x     nová souřadnice X opsaného obdelníku osoby
     * @param y     nová souřadnice Y opsanho obdelníku osoby
     */
    public void setPozice(int x, int y){
        int rozdilX;
        int pozHlavaX;
        int pozHlavaY;
        int pozTeloX;
        int pozTeloY;
        
        if(hlava.getSirka()<telo.getSirka()){
            //hlava mensi nez telo
            rozdilX = hlava.getX()-telo.getX();
            pozHlavaX = x+rozdilX;
            pozTeloX = x;
            pozHlavaY = y;
            pozTeloY = y+hlava.getVyska();
        }else{
            //hlava vetsi nez telo
            rozdilX = telo.getX()-hlava.getX();
            pozHlavaX = x;
            pozTeloX = x+rozdilX;
            pozHlavaY = y;
            pozTeloY = y+hlava.getVyska();
        }
        hlava.setPozice(pozHlavaX, pozHlavaY);
        telo.setPozice(pozTeloX, pozTeloY);
    }
    
    /**
     * Nastaví novou pozici osoby (opsaného obdelníku osoby).
     * 
     * @param pozice    nová pozice osoby reprezentovaná instancí třídy Pozice
     */
    public void setPozice(Pozice pozice){
        this.setPozice(pozice.getX(), pozice.getY());
    }
    
    /**
     * Vrátí řetězec String ve tvaru: 
     *  "NÁZEV_OSOBY_ČÍSLO_OSOBY: x=X, y=Y, výška=VÝŠKA, šířka=ŠÍŘKA, barva těla=BARVA" kde:
     *      NÁZEV_OSOBY     Je název osoby.
     *      ČÍSLO_OSOBY     Je pořadové číslo osoby.
     *      X               Je vodorovná (x-ová) souřadnice osoby.
     *      Y               Je svislá (y-ová) souřadnice osoby.
     *      VÝŠKA           Je výška osoby.
     *      ŠÍŘKA           Je šířka osoby.
     *      BARVA           Je barva těla osoby.
     *      
     * @return popis osoby
     */
    public String toString(){
        return this.getNazev()
                +": x="+this.getX()
                +", y="+this.getY()
                +", výška="+this.getVyska()
                +", šířka="+this.getSirka()
                +", barva těla="+this.getBarvaTela().toString();
    }
    
    /**
     * Vykreslí tělo a hlavu osoby na platno.
     */
    public void nakresli(){
        hlava.nakresli();
        telo.nakresli();
    }
    
    
    
    /**
     * Připraví novou instanci se všemi zadanými parametry.
     *  @param x                Vodorovná (x-ová) souřadnice osoby
     *  @param y                Svislá (y-ová) souřadnice osoby
     *  @param velikostHlavy    Šířka hlavy osoby
     *  @param pomerHlavaTelo   Poměr hlava/tělo osoby
     *  @param pomerTelo        Poměr výška/šířka těla osoby
     *  @param barvaTela        Barva těla osoby
     *  
     * Dále nastaví zbylé parametry osoby:
     *  vyska   výška opsaného obdelníku osoby
     *  sirka   šířka opsaného obdelníku osoby
     *  PORADI  pořadí vytvořené osoby
     *  NAZEV   název osoby podle jejího pořadí
     */
    public Osoba(int x, int y, int velikostHlavy, double pomerHlavaTelo, double pomerTelo, Barva barvaTela){
        int vyska = (int)(velikostHlavy/pomerHlavaTelo);
        int sirka = (int)(vyska/pomerTelo);
        
        if(sirka<velikostHlavy){
            hlava = new Elipsa(x,y,velikostHlavy,velikostHlavy,BARVA_HLAVY);
            telo = new Obdelnik(x+(velikostHlavy/2)-(sirka/2),y+velikostHlavy,sirka,vyska,barvaTela);
        }else{
            hlava = new Elipsa(x+(sirka/2)-(velikostHlavy/2),y,velikostHlavy,velikostHlavy,BARVA_HLAVY);
            telo = new Obdelnik(x,y+velikostHlavy,sirka,vyska,barvaTela);
        }
    }
    
    /**
     * Připraví novou instanci se zadanými parametry. Zbylé parametry doplní na počáteční hodnoty.
     *  @param x                Vodorovná (x-ová) souřadnice osoby
     *  @param y                Svislá (y-ova) souřadnice osoby
     *  @param veliksotHlavy    Šířka hlavy osoby
     *  @param barvaTela        Barva těla osoby
     *  
     *  Dále nastaví zbylé parametry osoby:
     *  vyska   výška opsaného obdelníku osoby
     *  sirka   šířka opsaného obdelníku osoby
     *  PORADI  pořadí vytvořené osoby
     *  NAZEV   název osoby podle jejího pořadí
     */
    public Osoba(int x, int y, int velikostHlavy, Barva barvaTela){
        this(x,y,velikostHlavy,POMER_HLAVA_TELO,POMER_TELO_VYS_SIR,barvaTela);
    }
    
    /**
     * Připraví novou instanci se zadanými parametry. Zbylé parametry doplní na počáteční hodnoty.
     *  @param x                Vodorovná (x-ová) souřadnice osoby
     *  @param y                Svislá (y-ova) souřadnice osoby
     *  @param barvaTela        Barva těla osoby
     *  
     *  Dále nastaví zbylé parametry osoby:
     *  vyska   výška opsaného obdelníku osoby
     *  sirka   šířka opsaného obdelníku osoby
     *  PORADI  pořadí vytvořené osoby
     *  NAZEV   název osoby podle jejího pořadí
     */
    public Osoba(int x, int y, Barva barvaTela){
        this(x,y,IMPL_VELIKOST_HLAVY,POMER_HLAVA_TELO,POMER_TELO_VYS_SIR,barvaTela);
    }
    
    /**
     * Připraví novou instanci. Všechny parametry osoby nastaví na počáteční hodnoty.
     * 
     *  Dále nastaví zbylé parametry osoby:
     *  vyska   výška opsaného obdelníku osoby
     *  sirka   šířka opsaného obdelníku osoby
     *  PORADI  pořadí vytvořené osoby
     *  NAZEV   název osoby podle jejího pořadí
     */
    public Osoba(){
        this(0,0,IMPL_VELIKOST_HLAVY,POMER_HLAVA_TELO,POMER_TELO_VYS_SIR,IMPL_BARVA_TELA);
    }
    //nove konstruktory
    
    /**
     * Připraví novou instanci se zadanými parametry. Zbylé parametry doplní na počáteční hodnoty.
     *  @param pozice           pozice osoby určená instancí třídy Pozice
     *  @param barvaTela        Barva těla osoby
     *  
     *  Dále nastaví zbylé parametry osoby:
     *  vyska   výška opsaného obdelníku osoby
     *  sirka   šířka opsaného obdelníku osoby
     *  PORADI  pořadí vytvořené osoby
     *  NAZEV   název osoby podle jejího pořadí
     */
    public Osoba(Pozice pozice, Barva barvaTela){
        this(pozice.getX(),pozice.getY(),IMPL_VELIKOST_HLAVY,POMER_HLAVA_TELO,POMER_TELO_VYS_SIR,barvaTela);
    }
    
    /**
     * Připraví novou instanci se zadanými parametry. Zbylé parametry doplní na počáteční hodnoty.
     *  @param pozice           pozice osoby určená instancí třídy Pozice
     *  @param veliksotHlavy    Šířka hlavy osoby
     *  @param barvaTela        Barva těla osoby
     *  
     *  Dále nastaví zbylé parametry osoby:
     *  vyska   výška opsaného obdelníku osoby
     *  sirka   šířka opsaného obdelníku osoby
     *  PORADI  pořadí vytvořené osoby
     *  NAZEV   název osoby podle jejího pořadí
     */
    public Osoba(Pozice pozice, int velikostHlavy, Barva barvaTela){
        this(pozice.getX(),pozice.getY(),velikostHlavy,POMER_HLAVA_TELO,POMER_TELO_VYS_SIR,barvaTela);
    }
    
        /**
     * Připraví novou instanci se všemi zadanými parametry.
     *  @param pozice           pozice osoby určená instancí třídy Pozice
     *  @param velikostHlavy    Šířka hlavy osoby
     *  @param pomerHlavaTelo   Poměr hlava/tělo osoby
     *  @param pomerTelo        Poměr výška/šířka těla osoby
     *  @param barvaTela        Barva těla osoby
     *  
     * Dále nastaví zbylé parametry osoby:
     *  vyska   výška opsaného obdelníku osoby
     *  sirka   šířka opsaného obdelníku osoby
     *  PORADI  pořadí vytvořené osoby
     *  NAZEV   název osoby podle jejího pořadí
     */
    public Osoba(Pozice pozice, int velikostHlavy, double pomerHlavaTelo, double pomerTelo, Barva barvaTela){
        this(pozice.getX(),pozice.getY(),velikostHlavy,pomerHlavaTelo,pomerTelo,barvaTela);
    }
    
    /**
     * Připraví novou instanci osoby podle zadané pozice a pohlaví. Barva těla se určí podle pohlaví:
     *  MUŽ     modrá barva těla
     *  ŽENA    červená barv
     * Zbylé parametry doplní na počáteční hodnoty.
     * 
     *  @param pozice   pozice osoby určená instancí třídy Pozice
     *  @param pohlaví  pohlaví osoby určené výčtovou třídou Pohlaví
     * 
     *  Dále nastaví zbylé parametry osoby:
     *  vyska   výška opsaného obdelníku osoby
     *  sirka   šířka opsaného obdelníku osoby
     *  PORADI  pořadí vytvořené osoby
     *  NAZEV   název osoby podle jejího pořadí
     */
    public Osoba(Pozice pozice, Pohlavi pohlavi){
        this();
        this.setPozice(pozice);
        this.setBarvaTela(zjistiBarvu(pohlavi));
    }
    
    /**
     * Zjistí barvu těla podle pohlaví.
     * 
     * @param pohlavi   instance výčtové třídy Pohlaví reprezentující pohlaví osoby
     * 
     * @return odkaz instance Barva reprezentující barvu těla podle pohlaví
     */
    private Barva zjistiBarvu(Pohlavi pohlavi){
        if(pohlavi == pohlavi.MUZ){
            return Barva.MODRA;
        }else{
            return Barva.CERVENA;
        }
    }
}
