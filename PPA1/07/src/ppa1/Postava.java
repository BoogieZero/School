package ppa1;

/**
 * Represents person with it's attributes.
 *
 * @author Martin Hamet
 * @version 181109
 */
public class Postava {
    private String jmeno;
    private int sila;
    private int hbitost;
    private int vitalita;
    private int health;

    private Zbran left = null;
    private Zbran right = null;

    /**
     * Arms the person with given weapons.
     *
     * @param ruka  designated hand
     * @param zbran weapon for designated hand
     * @return true for success, false if person already has weapon in the hand
     */
    public boolean vezmiZbran(Ruka ruka, Zbran zbran) {
        if (ruka == Ruka.LEVA) {
            if (left != null) return false;
            left = zbran;
        } else {
            if (right != null) return false;
            right = zbran;
        }
        return true;
    }

    /**
     * Calculates health loss from the person based on given attack minus defenses.
     * Health loss is sibtracted from it's health.
     *
     * @param utok attack
     * @return health loss
     */
    public int branSe(int utok) {
        int result = utok - getDefense();
        if (result < 0) result = 0;
        doDamage(result);
        return result;
    }

    /**
     * Calculates attack based on persons power and weapons used.
     *
     * @return attack power
     */
    public int zautoc() {
        return getAttack();
    }

    /**
     * Returns true if the person is alive.
     *
     * @return true for alive person.
     */
    public boolean jeZiva() {
        if (health > 1) return true;

        return false;
    }

    @Override
    public String toString() {
        return jmeno + "[" + health + "/" + vitalita + "] (" + getAttack() + "/" + getDefense() + ")";
    }

    private void doDamage(int damage) {
        health -= damage;
    }

    private int getDefense() {
        int def = 0;
        if (left != null) def += left.getObrana();
        if (right != null) def += right.getObrana();
        def += hbitost;
        return def;
    }

    private int getAttack() {
        int atk = 0;
        if (left != null) atk += left.getUtok();
        if (right != null) atk += right.getUtok();
        atk += sila;
        return atk;
    }

    /**
     * Instantiates new person from given attributes.
     *
     * @param jmeno    name
     * @param sila     power
     * @param hbitost  agility
     * @param vitalita vitality
     */
    public Postava(String jmeno, int sila, int hbitost, int vitalita) {
        this.jmeno = jmeno;
        this.sila = sila;
        this.hbitost = hbitost;
        this.vitalita = vitalita;
        this.health = vitalita;
    }

}
