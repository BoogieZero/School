
/**
 * Učuje rozměry daného objektu.
 * 
 * @author Martin Hamet A14B0254P 
 * @version 21.10.2014
 */
public class Rozmer
{
    /**
     * Výška objektu.
     */
    public final int vyska;
    
    /**
     * Šířka objektu.
     */
    public final int sirka;
    
    /**
     * Vrátí výšku.
     * 
     * @return výška
     */
    public int getVyska(){
        return vyska;
    }
    
    /**
     * Vrátí šířku.
     * 
     * @return šířka
     */
    public int getSirka(){
        return sirka;
    }
    
     /**
     * Vrátí řetězec String ve tvaru: 
     *  "Rozměr[sirka=SIRKA, vyska="VYSKA]" kde:
     *      SIRKA   je šířka objektu
     *      VYSKA   je výška objektu
     *      
     * @return popis rozměrů objektu
     */
    @Override
    public String toString(){
        return "Rozmer[sirka="+sirka+", vyska="+vyska+"]";
    }
    
    /**
     * Připraví novou instanci rozměru se zadanými parametry.
     * 
     * @param vyska     vyska objektu
     * @param sirka     sirka objektu
     */
    public Rozmer(int sirka, int vyska){
        this.vyska = vyska;
        this.sirka = sirka;
    }
    
    /**
     * Připraví novou instanci rozměru s počátečními parametry.
     * 
     *  výška = šířka = 0
     */
    public Rozmer()
    {
        this.vyska = 0;
        this.sirka = 0;
    }
    
}
