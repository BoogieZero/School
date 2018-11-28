package ppa1;

import java.util.Scanner;

/**
 * Main class creates and starts game.
 * @author Martin Hamet
 * @version 181124
 */
public class Ppa1u08 {

    /**
     * Program entry point.
     * Creates new gui, world and person.
     * Starts game.
     * @param args  not used
     */
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        GUI gui = new GUI(5,3, sc);
//        Svet world = new Svet(7, 5, new char[] {
//                '#','#','#','#','#','#','#',
//                '#',' ','#',' ',' ',' ','#',
//                '#',' ','#',' ',' ',' ','#',
//                '#',' ',' ',' ','#',' ','#',
//                '#','#','#','#','#','#','#',
//        });

        Svet world = new Svet(5, 3, new char[] {
                '#','#','#','#','#',
                '#',' ','#',' ',' ',
                '#',' ','#',' ',' ',
        });


        Postava person = new Postava(world, 1,1, 3, 2);

        if(spust(gui, world, person)){
            System.out.println("Konecne doma...");
        }else{
            System.out.println("Asi jsem se ztratil...");
        }
    }

    /**
     * Main loop.
     * Clears GUI
     * Draws world with person
     * Gets new direction from console
     * Outputs result to console
     *
     * @param gui       gui reference
     * @param world     world reference
     * @param person    person reference
     * @return          true if person is at final possition
     *                  false if invalid movement or input occurred
     */
    public static boolean spust(GUI gui, Svet world, Postava person) {
        while(!person.jeDoma()){
            gui.smaz();
            world.vykresli(gui);
            person.vykresli(gui);
            gui.vykresli();
            Smer dir = gui.nactiAkci();
            if(dir == null) return false;
            if(!person.jdi(dir)) return false;
        }
        return true;
    }
}
