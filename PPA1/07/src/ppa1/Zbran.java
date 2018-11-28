package ppa1;

/**
 *  Represent weapon with it's attributes.
 *  @author Martin Hamet
 *  @version 181109
 */
public class Zbran {
    private String nazev;
    private int obrana;
    private int utok;

    /**
     * Instantiates new weapon from given attributes.
     * @param nazev     name
     * @param obrana    attack
     * @param utok      defense
     */
    public Zbran(String nazev, int obrana, int utok){
        this.nazev = nazev;
        this.obrana = obrana;
        this.utok = utok;
    }

    /**
     * Returns weapon's name
     * @return  weapon's name
     */
    public String getNazev() {
        return nazev;
    }

    /**
     * Returns weapon's defenses
     * @return weapon's defenses
     */
    public int getObrana() {
        return obrana;
    }

    /**
     * Returns weapon's attack
     * @return weapon's attack
     */
    public int getUtok() {
        return utok;
    }

    @Override
    public String toString() {
        return nazev+" ("+utok+"/"+obrana+")";
    }
}
