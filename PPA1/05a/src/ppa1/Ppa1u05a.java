package ppa1;


import java.util.Locale;
import java.util.Scanner;

import static java.lang.System.out;

public class Ppa1u05a {
    private static Scanner sc = new Scanner(System.in).useLocale(Locale.US);

    public static void main(String[] args){
        out.print("x1: ");
        double x1 = sc.nextDouble();
        out.print("x2: ");
        double x2 = sc.nextDouble();
        out.print("xs: ");
        int xs = sc.nextInt();

        out.print("y1: ");
        double y1 = sc.nextDouble();
        out.print("y2: ");
        double y2 = sc.nextDouble();
        out.print("ys: ");
        int ys = sc.nextInt();

        out.print("ts: ");
        int ts = sc.nextInt();


        double[] xValues = new double [xs];
        double[] yValues = new double [ys];
        double[] tValues = new double [ts];

        double xStep = (x2-x1)/xs;
        double yStep = (y2-y1)/ys;
        double tStep = 1/xs;





        out.println("x, y, z, t");



    }
}
