
/**
 * Třída vytvoří pár muže a ženy podle třídy {@code Rande} pro snadnější manipulaci s dvojicí.
 * 
 * @author Hamet Martin A14B0254P
 * @version 5.11.2014
 */
public class Par implements IKresleny, IPosuvny
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
    * Určuje posun muže v pixelech pokud je muž menší než žena.
    *   jinak 0
    */
   public int yPosunMuze;
   
   /**
    * Určuje posun ženy v pixelech pokud je žena menší než muž.
    *   jinak 0
    */
   public int yPosunZeny;
   
   /**
    * Vrátí odkaz na instanci třídy Pozice reprezentující pozici páru.
    * 
    * @return pozice    odkaz na instanci třídy Pozice reprezentující pozici páru
    */
   public Pozice getPozice(){
       Pozice pom = new Pozice(zena.getPozice().getX(), zena.getPozice().getY()-yPosunZeny);
       return pom;
   }
   
   /**
    * Nastaví pozici páru na novou pozici.
    * 
    *   @param pozice   nová pozice páru
    */
   public void setPozice(Pozice pozice){
       Pozice pomMuz = new Pozice (pozice.getX()+zena.getSirka(), pozice.getY()+yPosunMuze);
       Pozice pomZena = new Pozice(pozice.getX(), pozice.getY()+yPosunZeny);
       zena.setPozice(pomZena);
       muz.setPozice(pomMuz);
   }
   
   /**
    * Nastaví pozici páru na novou pozici.
    * 
    *   @param x    x-ová souřadnice nové pozice páru
    *   @param y    y-ová souřadnice nov pozice páru
    */
   public void setPozice(int x, int y){
       setPozice(new Pozice(x,y));
   }
   
   /**
    * Vykreslí pár na plátno.
    * 
    * @param kreslitko  nástroj pro kreslení
    */
   public void nakresli(Kreslitko kreslitko){
       SP.nekresli();
       muz.zobraz();
       zena.zobraz();
       SP.vratKresli();
   }
   
   /**
    * Připraví novou instanci třídy podle třídy Rande.
    *   
    *   @param rande    odkaz na instanci třídy Rande ze které se vytvoří pár
    */
   public Par(Rande rande){
       this.muz = rande.getMuz();
       this.zena = rande.getZena();
       this.yPosunMuze = rande.yPosunMuze();
       this.yPosunZeny = rande.yPosunZeny();
   }
}
