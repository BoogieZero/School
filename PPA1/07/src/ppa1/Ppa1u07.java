package ppa1;

import java.util.Scanner;

/**
 * Main class which will engage two created person in combat. Winner is displayed in console.
 *
 * @author Martin Hamet
 * @version 181109
 */
public class Ppa1u07 {

    /**
     * Creates new person from console input.
     *
     * @param sc console input
     * @return created person
     */
    public static Postava nactiPostavu(Scanner sc) {
        //System.out.println("Person:");
        String name = sc.nextLine();
        int strength = Integer.parseInt(sc.nextLine());
        int agility = Integer.parseInt(sc.nextLine());
        int vitality = Integer.parseInt(sc.nextLine());
        return new Postava(name, strength, agility, vitality);
    }

    /**
     * Creates new weapon from console input.
     *
     * @param sc console input
     * @return created weapon
     */
    public static Zbran nactiZbran(Scanner sc) {
        //System.out.println("Weapon:");
        String name = sc.nextLine();
        if (name.isEmpty()) return null;
        int attack = Integer.parseInt(sc.nextLine());
        int defense = Integer.parseInt(sc.nextLine());
        return new Zbran(name, defense, attack);
    }

    /**
     * Arms a person with given weapons.
     *
     * @param postava the person to be armed
     * @param leva    weapon for left hand
     * @param prava   weapon for right hand
     */
    public static void vyzbrojPostavu(Postava postava, Zbran leva, Zbran prava) {
        postava.vezmiZbran(Ruka.LEVA, leva);
        postava.vezmiZbran(Ruka.PRAVA, prava);
    }

    /**
     * Starts combat between two given persons. Person1 starts.
     *
     * @param postava1 first combatant
     * @param postava2 second combatant
     * @return winner
     */
    public static Postava souboj(Postava postava1, Postava postava2) {
        while (true) {
            postava2.branSe(postava1.zautoc());
            if (!postava2.jeZiva()) return postava1;
            postava1.branSe(postava2.zautoc());
            if (!postava1.jeZiva()) return postava2;
        }
    }

    /**
     * Loads two combatants arms them and will engage combat between them.
     * Winner will be written in console.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Scanner sc = null;
        sc = new Scanner("Thorygg\n" +
                "1\n" +
                "2\n" +
                "3\n" +
                "Tezky mec\n" +
                "4\n" +
                "5\n" +
                "Tezky mec 2\n" +
                "6\n" +
                "7\n" +
                "vlk\n" +
                "8\n" +
                "9\n" +
                "10\n" +
                "Tezky mec 3\n" +
                "11\n" +
                "12\n" +
                "Tezky mec 4\n" +
                "13\n" +
                "14\n" +
                "\n");
        sc = new Scanner(System.in);
        Postava one = nactiPostavu(sc);
        Zbran oneL = nactiZbran(sc);
        Zbran oneR = nactiZbran(sc);

        Postava two = nactiPostavu(sc);
        Zbran twoL = nactiZbran(sc);
        Zbran twoR = nactiZbran(sc);

        vyzbrojPostavu(one, oneL, oneR);
        vyzbrojPostavu(two, twoL, twoR);

//        System.out.println(one);
//        System.out.println(two);

        Postava winner = souboj(one, two);
        System.out.println("Vitez: " + winner);
    }
}
