import static java.lang.System.out;
public class Ppa1u01b {
    /**
     * Conversion value from mm to inches
     */
    private static final double toInch = 1.0/25.4;

    /**
     * Computes attributes of a fixed LCD monitor. Gives size in pixels for 4x3cm rectangle (WYSIWYG).
     * @param args  not used
     */
    public static void main(String []args){
        int hSize = 346;
        int vSize = 195;
        int hResolution = 1920;
        int vResolution = 1080;
        out.println("hSize [mm] = "+hSize);
        out.println("vSize [mm] = "+vSize);
        out.println("hResolution [pixels] = "+hResolution);
        out.println("vResolution [pixels] = "+vResolution);
        out.println("---------------------");

        //Points per inch
        double hPPI = hResolution / (hSize * toInch);
        double vPPI = vResolution / (vSize * toInch);
        double rPPI = hPPI / vPPI;

        //Distance between pixels
        double pitch = (double)hSize / hResolution;

        //4x3cm rectangle WYSISWG in pixels
        double width = 40.0 * hResolution / hSize;
        double height = 30.0 * vResolution / vSize;

        out.println("hPPI = "+hPPI);
        out.println("vPPI = "+vPPI);
        out.println("rPPI = "+rPPI);
        out.println("pitch [mm] = "+pitch);
        out.println("width [pixels]= "+width);
        out.println("height [pixels]= "+height);
        out.println("url: https://www.asus.com/us/Laptops/F555UA/");
    }
}
