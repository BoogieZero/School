public class Ppa1u01a {
    /**
     * Calculates surface and volume of a fixed block.
     * @param args  not used
     */
    public static void main(String []args){
        int a = 56;     //width
        int b = 32;     //height
        int c = 89;     //depth

        int volume = a * b * c;
        int surface = (a * b * 2) + (c * b * 2) + (a * c *2);

        System.out.println("a = "+a);
        System.out.println("b = "+b);
        System.out.println("c = "+c);
        System.out.println("-----");
        System.out.println("s = "+surface);
        System.out.println("v = "+volume);
    }
}
