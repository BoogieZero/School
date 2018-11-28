package ppa1;

/**
 * Represents movable person.
 * @version 181124
 * @author Martin Hamet
 */
public class Postava {

    private final Svet svet;
    private final int startX;
    private final int startY;
    private final int domovX;
    private final int domovY;

    private Coordinates possition;

    /**
     * Moves person in given direction if possible.
     * @param smer  movement direction
     * @return      true if movement was successful, false otherwise
     */
    public boolean jdi(Smer smer){
        Coordinates newCoor = possition.getNewCoordinates(smer);
        char ch = svet.uzemi(newCoor.x, newCoor.y);

        switch(ch){
            case '@':
            case '^':
            case ' ':
                possition = newCoor;
                return true;
            case '#':
            case 0:
            default:
                return false;
        }
    }

    /**
     * Returns true if person is at final position.
     * @return  true if if person is at final position
     */
    public boolean jeDoma(){
        if(possition.x == domovX && possition.y == domovY){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Draws into given gui persons position and it's final destination
     * @param gui   gui reference
     */
    public void vykresli(GUI gui){
        gui.zapis(possition.x, possition.y, '@');
        gui.zapis(domovX, domovY, '^');
    }

    /**
     * Instantiates new person from given attributes
     * @param svet      world reference
     * @param startX    x-coordinate of starting position
     * @param startY    y-coordinate of starting position
     * @param domovX    x-coordinate of final destination
     * @param domovY    y-coordinate of final destination
     */
    public Postava(Svet svet, int startX, int startY, int domovX, int domovY){
        this.svet = svet;
        this.startX = startX;
        this.startY = startY;
        this.domovX = domovX;
        this.domovY = domovY;

        this.possition = new Coordinates(startX, startY);
    }
}
