import java.util.*;
/**
 * Třída {@code Seznamka} umožňuje schůzky a následný výběr vhodných párů. Dále provede
 * celou seznamovací ukázkou pomocí metody {@code realizujSchuzky(Pozice, int)}
 * Třída může mít pouze jedinou svojí instanci.
 * 
 * 
 * @author Hamet Martin A14B0254P
 * @version 24.11.2014
 */
public class Seznamka
{
    
    /**
     * Jediná instance seznamky.
     */
    private static final Seznamka INSTANCE = new Seznamka();
    
    /**
     * Odkaz na instanci SpravcePlatna pro kreslení na plátno.
     */
    private static final SpravcePlatna SP = SpravcePlatna.getInstance();
    
    /**
     * Defaultní rychlost pohybu osob.
     */
    private static final int DEF_RYCHLOST = 5;
    
    /**
     * Defaultní pozice mužů na plátně.
     */
    private static final Pozice DEF_SRAZ_MUZI = new Pozice(10, 10);
    
    /**
     * Defaultní pozice žen na plátně.
     */
    private static final Pozice DEF_SRAZ_ZENY;
    
    /**
     * Defaultní pozice setkání pátu.
     */
    private static final Pozice DEF_SETKANI;
    
    /**
     * Seznam žen v seznamce.
     */
    private List<Osoba> seznamZen;
    
    /**
     * Seznam mužů v seznamce.
     */
    private List<Osoba> seznamMuzu;
    
    /**
     * Pozice srazu mužů.
     */
    private Pozice srazMuzu;
    
    /**
     * Pozice srazu žen.
     */
    private Pozice srazZen;
    
    /**
     * Pozice místa schůzky.
     */
    private Pozice mistoSchuzky;
    
    /**
     * Odkaz na instanci třídy {@code Presouvac} pro přesouvání osob.
     */
    private Presouvac presouvac;

    static {
        int sirkaPlatna = SP.getBsirka();
        int vyskaPlatna = SP.getBVyska();
        DEF_SRAZ_ZENY = new Pozice(sirkaPlatna - 100, vyskaPlatna - 100);
        DEF_SETKANI = new Pozice(sirkaPlatna / 3, vyskaPlatna / 3);
    }
    
    /**
     * Připraví jedinou instanci třídy {@code Seznamka}.
     * 
     *  @return INSTANCE    vrátí odkaz na jedinou možnou instanci třídy {@code Seznamka}
     */
    public static Seznamka getSeznamka(){
        return INSTANCE;
    }
    
    /**
     * Změní pozici srazu mužů
     * pokud již v seznamu mužů nějací jsou, změní všem souřadnice
     * 
     *  @param pozice   nová pozice srazu
     */
    public void setSrazMuzu(Pozice pozice){
        this.srazMuzu = pozice;
        if(!seznamMuzu.isEmpty()){
            Iterator<Osoba> i = seznamMuzu.iterator();
            while(i.hasNext()){
                i.next().setPozice(srazMuzu);
            }
        }
        
    }
    
     /**
     * Změní pozici srazu žen
     * pokud již v seznamu žen nějaké jsou, změní všem souřadnice
     * 
     *  @param pozice   nová pozice srazu
     */
    public void setSrazZen(Pozice pozice){
        this.srazZen = pozice;
         if(!seznamZen.isEmpty()){
            Iterator<Osoba> i = seznamZen.iterator();
            while(i.hasNext()){
                i.next().setPozice(srazZen);
            }
        }
        
    }
    
    /**
     * Změní místo schůzky
     * 
     *  @param pozice   nová pozice schůzky
     */
    public void setMistoSchuzky(Pozice pozice){
        this.mistoSchuzky = pozice;
    }
    
    /**
     * Vytvoří nový přesouvač podle zadané rychlosti posunu.
     * 
     *  @param rychlost     požadovaná rychlsot posuvu.
     */
    public void setPresouvac(int rychlost){
        this.presouvac = new Presouvac(rychlost);
    }
    
    /**
     * Přidá muže do seznamu
     * přidávanému muži nastaví pozici srazu mužů
     * 
     *  @param muz  přidávaný muž
     */
    public void pridejMuze(Osoba muz){
        muz.setPozice(srazMuzu);
        seznamMuzu.add(muz);
    }
    
    /**
     * Přidá ženu do seznamu
     * přidávané ženě nastaví pozici srazu žen
     * 
     *  @param zena     přidávaná žena
     */
    public void pridejZenu(Osoba zena){
        zena.setPozice(srazZen);
        seznamZen.add(zena);
    }
    
    /**
     * postupně zařizuje schůzky všem kombinacím párů z obou seznamů
     * pokud se sejde instance Superman a Superwoman, jsou považovány za 
     * ideální pár, který společně odejde na určené místo
     *
     * @param spolecnyOdchod    cílová pozice společného odchodu vybraného páru
     * @param dobaCekani        v milisec mezi jednotlivými schůzkami
     * 
     * @return pocetSetkani     počet uskutečněných setkání
     */
    public int realizujSchuzky(Pozice spolecnyOdchod, int dobaCekani){
        int pocetSetkani = 0;
         
        Osoba muz, zena;
        Outer:
        for(int i=0; i<seznamZen.size(); i++){
           zena = seznamZen.get(i);
            for(int j=0; j<seznamMuzu.size(); j++){
                muz = seznamMuzu.get(j);
                Rande rande = new Rande(muz, zena);
                rande.jdouNaRande(mistoSchuzky, presouvac);
                pocetSetkani++;
                IO.cekej(dobaCekani);

                if(muz instanceof Superman && zena instanceof Superwoman){
                    Zvyraznovac zvyr = new Zvyraznovac();
                    rande.parJdeSpolecne(mistoSchuzky ,presouvac);
                    Obdelnik zvyrM = zvyr.zvyrazniPozadi(muz, Barva.SEDA);
                    Obdelnik zvyrZ = zvyr.zvyrazniPozadi(zena, Barva.SEDA);
                    //IO.zprava("Ideální pár");
                    SP.odstran(zvyrM);
                    SP.odstran(zvyrZ);
                    rande.parJdeSpolecne(spolecnyOdchod, presouvac);
                    seznamZen.remove(zena);
                    seznamMuzu.remove(muz);
                    
                    IO.cekej(dobaCekani);
                    SP.odstranVse();
                    i--;
                    continue Outer;
                }else{
                    presouvac.presunNa(srazZen, zena);
                    presouvac.presunNa(srazMuzu, muz);
                }
                
                IO.cekej(dobaCekani);
                SP.odstranVse();              
            }
        }
        return pocetSetkani;
    }
    
    /**
     * Vytvoří jedinou instanci třídy {@Code Seznamka}
     */
    public Seznamka(){
        seznamMuzu = new ArrayList<Osoba>();
        seznamZen = new ArrayList<Osoba>();
        setPresouvac(DEF_RYCHLOST);
        setSrazMuzu(DEF_SRAZ_MUZI);
        setSrazZen(DEF_SRAZ_ZENY);
        setMistoSchuzky(DEF_SETKANI);
    }

}
