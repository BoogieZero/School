package ppa1;

/**
 * Substitutes pair of coordinates.
 * @author Martin Hamet
 * @version 181124
 */
public class Coordinates {
    /**
     * x-coordinate
     */
    public int x;
    /**
     * y-coordinate
     */
    public int y;

    /**
     * Instantiates new coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Shifts coordinate in given direction.
     * @param direction shift direction
     */
    public void shiftCoordinates(Smer direction) {
        switch (direction) {
            case JIH:
                y += 1;
                break;
            case SEVER:
                y -= 1;
                break;
            case ZAPAD:
                x -= 1;
                break;
            case VYCHOD:
                x += 1;
                break;
        }
    }

    /**
     * Returns new coordinates from new direction
     * @param direction shift direction
     * @return          new instance of coordinates in given direction
     */
    public Coordinates getNewCoordinates(Smer direction){
        Coordinates result = new Coordinates(x, y);
        result.shiftCoordinates(direction);
        return result;
    }
}
