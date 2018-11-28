package ppa1;

import java.util.Arrays;

/**
 * Represents world map.
 *
 * @author Martin Hamet
 * @version 181124
 */
public class Svet {
    private final int sirka;
    private final int vyska;
    private final char[] data;
    private char[][] data2;

    /**
     * Converts coordinates to single dimension index.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return single dimensional index
     */
    private int toArrayIdx(int x, int y) {
        int result = y * sirka + x;
        return result;
    }

    /**
     * Returns character on given coordinates.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return character at given coordinates
     */
    public char uzemi(int x, int y) {
        if (x < 0 || y < 0) return 0;
        if (x >= sirka || y >= vyska) return 0;
        int idx = toArrayIdx(x, y);
        char ret = data[idx];
        return ret;
    }

    /**
     * Draws world to given gui
     *
     * @param gui gui reference
     */
    public void vykresli(GUI gui) {
        int idx = 0;
        for (int i = 0; i < vyska; i++) {
            for (int j = 0; j < sirka; j++) {
                gui.zapis(j, i, data[idx]);
                idx++;
            }
        }
    }

    /**
     * Instantiates new world map by given attributes and data.
     *
     * @param sirka world width
     * @param vyska world height
     * @param data  data to populate world by rows
     */
    public Svet(int sirka, int vyska, char[] data) {
        this.sirka = sirka;
        this.vyska = vyska;
        this.data = data;
    }
}
