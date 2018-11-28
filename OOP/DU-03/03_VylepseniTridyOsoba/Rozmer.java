
/**
 * Write a description of class Rozmer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Rozmer
{
    private int sirka;
    private int vyska;

    public int getSirka(){
        return sirka;
    }
    
    public int getVyska(){
        return vyska;
    }
    
    public String toString(){
        return "Rozmer[sirka="+sirka+", vyska="+vyska+"]";
    }
    
    /**
     * Constructor for objects of class Rozmer
     */
    public Rozmer(int sirka, int vyska)
    {
        
    }
    
    public Rozmer(){
        this.sirka=0;
        this.vyska=0;
    }
}
