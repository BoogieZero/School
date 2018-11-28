
/**
 * Třída vytvoří instance třídy {@code Osoba} reprezentující muže a ženu. Dále poskytuje nástroje pro jejich
 * přesouvání a vykreslování na plátně jednotlivě nebo jako páru.
 * 
 * @author Hamet Martin A14B0254P
 * @version 5.11.2014
 */
public class Rande
{
   /**
    * Odkaz na instanci SpravcePlatna pro kreslení na plátno.
    */
   private static final SpravcePlatna SP = SpravcePlatna.getInstance();
   
   /**
    * Odkaz na instanci třídy Osoba reprezentující muže.
    */
   private Osoba muz;
   
   /**
    * Odkaz na instanci třídy Osoba reprezentující ženu.
    */
   private Osoba zena;
   
   /**
    * Domovská pozice muže.
    */
   private Pozice domaMuz;
   
   /**
    * Domovská pozice ženy.
    */
   private Pozice domaZena;
   
   /**
    * Vrátí odkaz na instanci třídy Osoba reprezentující muže.
    * 
    * @return muz   odkaz na instanci třídy Osoba reprezentující muže
    */
   public Osoba getMuz(){
       return muz;
   }
   
   /**
    * Vrátí odkaz na instanci třídy Osoba reprezentující ženu.
    * 
    * @return   odkaz na instanci třídy Osoba reprezentující ženy
    */
   public Osoba getZena(){
       return zena;
   }
   
   /**
    * Vrátí odkaz na instanci třídy Pozice reprezentující domovkskou pozici muže.
    * 
    * @return domaMuz   odkaz na instanci třídy Pozice reprezentující domovskou pozici muže
    */
   public Pozice getDomaMuz(){
       return domaMuz;
   }
   
   /**
    * Vrátí odkaz na instanci třídy Pozice reprezentující domovkskou pozici ženy.
    * 
    * @return domaZena  odkaz na instanci třídy Pozice reprezentující domovskou pozici ženy
    */
   public Pozice getDomaZena(){
       return domaZena;
   }
   
   /**
    * Přesune nejprve muže na pozici určenou pomocí {@code mistoSchuzky}. Dále přesune ženu na pozici vlevo od muže.
    * 
    *   @param mistoSchuzky     odkaz na instanci třídy Pozice reprezentující pozici schůzky
    *   @param chuzeNaRande     odkaz na instanci třídy Presouvac pro přesouvání osob
    */
   public void jdouNaRande(Pozice mistoSchuzky, Presouvac chuzeNaRande){
       Pozice pomMuz = new Pozice(mistoSchuzky.getX()+zena.getSirka()-muz.getX(),mistoSchuzky.getY()-muz.getY());
       chuzeNaRande.presunO(pomMuz, muz);
       chuzeNaRande.presunNa(mistoSchuzky,zena);
   }
   
   /**
    * Přesune pár na pozici určenou pomocí {@code mistoSchuzky}.
    * 
    *   @param mistoVyletu      odkaz na instanci třídy Pozice reprezentující cílovou pozici výletu
    *   @param chuzeSpolu       odkaz na instanci třídy Presouvac pro přesouvání páru
    *   
    *   @return par             odkaz na instanci třídy Par reprezentující pár
    */
   public Par parJdeSpolecne(Pozice mistoVyletu, Presouvac chuzeSpolu){
       Par par = new Par(this);
       SP.pridej(par);
       chuzeSpolu.presunNa(mistoVyletu,par);
       return par;
   }
   
   /**
    * Přesune pár na další pozici určenou pomocí {@code dalsiMistoVletu}.
    * 
    *   @param par                  odkaz na instanci třídy Par reprezentující přesouvaný pár
    *   @param dalsiMistoVyletu     odkaz na instanci třídy Pozice reprezentující další pozici výletu
    *   @param chuzeSpolu          odkaz na instanci třídy Presouvac pro přesouvání páru
    */
   public void parPokracujeSpolecne(Par par, Pozice dalsiMistoVyletu, Presouvac chuzeSpolu){
       chuzeSpolu.presunNa(dalsiMistoVyletu, par);
   }
   
   /**
    * Přesune pár na domácí pozici ženy. Dále přesune muže na jeho domácí pozici.
    * 
    *   @param par          odkaz na instanci třídy Par reprezentující přesouvaný pár
    *   @param chuzeDomu    odkaz na instanci třídy Presouvac pro přesouvání páru
    */
   public void jdouDomu(Par par, Presouvac chuzeDomu){
       chuzeDomu.presunNa(getDomaZena(), par);
       chuzeDomu.presunNa(getDomaMuz(), muz);
   }
   
   /**
    * Připraví novou instanci třídy podle zadané domovské pozice muže a ženy.
    *   
    *   @param domaMuz      domovská pozice muže
    *   @param domaZena     domovská pozice ženy
    */
   public Rande(Pozice domaMuz, Pozice domaZena){
       muz = new Osoba(domaMuz, Pohlavi.MUZ);
       zena = new Osoba(domaZena, Pohlavi.ZENA);
       this.domaMuz = domaMuz;
       this.domaZena = domaZena;
       muz.zobraz();
       zena.zobraz();
   }
}
