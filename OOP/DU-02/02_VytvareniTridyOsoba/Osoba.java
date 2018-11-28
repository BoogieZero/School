
/**
 * Write a description of class Osoba here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Osoba
{
    // instance variables - replace the example below with your own
    private static final Barva BARVA_HLAVY = Barva.RUZOVA;
    private static final Barva IMPL_BARVA_TELA = Barva.SEDA;
    private static final int IMPL_VELIKOST_HLAVY = 60;
    private static final double POMER_HLAVA_TELO = 6.0/8.0;
    private static final double POMER_TELO_VYS_SIR = 8.0/7.0;
    private final Elipsa hlava;
    private final Obdelnik telo;
    private static int pocet;
    private final int PORADI=0;
    private final String NAZEV;
    
    public Elipsa getHlava(){
        return hlava;
    }
    
    public Obdelnik getTelo(){
        return telo;
    }
    
    public int getX(){return Math.min(telo.getX(),hlava.getX());}
    
    public int getY(){return hlava.getY();
    }
    
    public String getNazev(){return NAZEV;}
    
    public int getSirka(){return Math.max(telo.getSirka(),hlava.getSirka());}
    
    public int getVyska(){return (telo.getVyska()+hlava.getVyska());}
    
    public Barva getBarvaTela(){return telo.getBarva();}
    
    public void setBarvaTela(Barva barvaTela){
        telo.setBarva(barvaTela);
    }
    
    /**
     * Constructor for objects of class Osoba
     */
    public Osoba(int x, int y, int velikostHlavy, double pomerHlavaTelo, double pomerTelo, Barva barvaTela){
        int vyska = (int)(velikostHlavy/pomerHlavaTelo);
        int sirka = (int)(vyska/pomerTelo);
        pocet++;
        NAZEV = "Osoba_"+pocet;
        if(sirka<velikostHlavy){
            hlava = new Elipsa(x,y,velikostHlavy,velikostHlavy,BARVA_HLAVY);
            telo = new Obdelnik(x+(velikostHlavy/2)-(sirka/2),y+velikostHlavy,sirka,vyska,barvaTela);
        }else{
            hlava = new Elipsa(x+(sirka/2)-(velikostHlavy/2),y,velikostHlavy,velikostHlavy,BARVA_HLAVY);
            telo = new Obdelnik(x,y+velikostHlavy,sirka,vyska,barvaTela);
        }
    }
    
    public Osoba(int x, int y, int velikostHlavy, Barva barvaTela){
        this(x,y,velikostHlavy,POMER_HLAVA_TELO,POMER_TELO_VYS_SIR,barvaTela);
    }
    
    public Osoba(int x, int y, Barva barvaTela){
        this(x,y,IMPL_VELIKOST_HLAVY,POMER_HLAVA_TELO,POMER_TELO_VYS_SIR,barvaTela);
    }
    
    public Osoba(){
        this(0,0,IMPL_VELIKOST_HLAVY,POMER_HLAVA_TELO,POMER_TELO_VYS_SIR,IMPL_BARVA_TELA);
    }
}
