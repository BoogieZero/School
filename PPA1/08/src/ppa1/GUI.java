package ppa1;

import java.util.Scanner;

/**
 * Works as interface with console.
 * Draws and gets inputs from console.
 * @author Martin Hamet
 * @version 181124
 */
public class GUI {
    private final int sirka;
    private final int vyska;
    private final Scanner sc;

    private char[][] plane;

    /**
     * Writes a character to given position in plane.
     * @param x     x-coordinate
     * @param y     y-coordinate
     * @param znak  character to write
     */
    public void zapis(int x, int y, char znak) {
        plane[y][x] = znak;
    }

    /**
     * Fills whole plane with space characters.
     */
    public void smaz() {
        for (int i = 0; i < plane.length; i++) {
            for (int j = 0; j < plane[i].length; j++) {
                plane[i][j] = ' ';
            }
        }
    }

    /**
     * Gets new direction from console in numeric form and returns it's logical value of Smer.
     * @return new direction from console
     */
    public Smer nactiAkci() {
        int input = sc.nextInt();
        switch (input) {
            case 2:
                return Smer.JIH;
            case 4:
                return Smer.ZAPAD;
            case 6:
                return Smer.VYCHOD;
            case 8:
                return Smer.SEVER;
            default:
                return null;
        }
    }

    /**
     * Outputs plane to console.
     */
    public void vykresli() {
        for (int i = 0; i < plane.length; i++) {
            for (int j = 0; j < plane[i].length; j++) {
                System.out.print(plane[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Instantiates new GUI
     * @param sirka plane width
     * @param vyska plane height
     * @param sc    console link
     */
    public GUI(int sirka, int vyska, Scanner sc) {
        this.sirka = sirka;
        this.vyska = vyska;
        this.sc = sc;

        plane = new char[vyska][sirka];
    }
}
