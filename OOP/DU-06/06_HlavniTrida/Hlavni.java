
/**
 * Třída vytvoří dvě osoby muže a ženu. Nechá je přesunout doprostřed plátna z jejich domovských
 * pozic. Dále je provede v několika směrech po plátně. Muž odvede ženu na její domovskou pozici a
 * sám odejde na vlastní domovskou pozici. PVstupními parametry lze nastavit rychlost jejich pohybu
 * po plátně.
 *  
 *  @param args         První parametr udává rychlost před schůzkou.
 *                      Druhý parametr udává rychlost na schůzce.
 *                      Třetí parametr udává rychlost po schůzce.
 * 
 * @author Hamet Martin A14B0254P
 * @version 5.11.2014
 */
public class Hlavni
{
    
   /**
   * Odkaz na instanci SpravcePlatna pro kreslení na plátno.
   */
   private static final SpravcePlatna SP = SpravcePlatna.getInstance();
   
   
   public static void main(String [] args){
       SP.odstranVse();
       Pozice pM = new Pozice(10, 10);
       Pozice pZ = new Pozice(400, 150);
       Rande rande = new Rande(pM, pZ);
       
       Presouvac chuzeNaRande = new Presouvac(Integer.parseInt(args[0]));
       Pozice pS = new Pozice(150, 80);
       rande.jdouNaRande(pS, chuzeNaRande);
       
       Pozice pV = new Pozice(10, 10);
       Presouvac chuzeSpolu = new Presouvac(Integer.parseInt(args[1]));
       Par par = rande.parJdeSpolecne(pV, chuzeSpolu);    
    
       Pozice pD = new Pozice(350, 10);
       rande.parPokracujeSpolecne(par, pD, chuzeSpolu);
    
       Pozice pK = new Pozice(10, 150);
       rande.parPokracujeSpolecne(par, pK, chuzeSpolu);
       
       Presouvac chuzeDomu = new Presouvac(Integer.parseInt(args[2]));
       rande.jdouDomu(par, chuzeDomu);
    }
}
