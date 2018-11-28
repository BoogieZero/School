package ppa1;

import java.util.Scanner;

import static java.lang.System.out;

/**
 * Calculates intersection of two lines given by 4 coordinates.
 */
public class Ppa1u02b {
    /**
     * Input from console
     */
    private static Scanner sc = new Scanner(System.in);

    /**
     * Calculates intersection of two lines given by 4 coordinates from console input.
     * @param args  not used
     */
    public static void main(String[] args) {
        double[][] points = new double[4][2];
        out.print("Zadej Ax:");
        points[0][0] = sc.nextDouble();
        out.print("Zadej Ay:");
        points[0][1] = sc.nextDouble();
        out.print("Zadej Bx:");
        points[1][0] = sc.nextDouble();
        out.print("Zadej By:");
        points[1][1] = sc.nextDouble();
        out.print("Zadej Cx:");
        points[2][0] = sc.nextDouble();
        out.print("Zadej Cy:");
        points[2][1] = sc.nextDouble();
        out.print("Zadej Dx:");
        points[3][0] = sc.nextDouble();
        out.print("Zadej Dy:");
        points[3][1] = sc.nextDouble();

//        points[0][0] = 1.5;
//        points[0][1] = 1.5;
//        points[1][0] = -1.5;
//        points[1][1] = -1.5;
//        points[2][0] = -1.5;
//        points[2][1] = 1.5;
//        points[3][0] = 1.5;
//        points[3][1] = -1.5;

//        points[0][0] = -3.0;
//        points[0][1] = 2.5;
//        points[1][0] = -3.0;
//        points[1][1] = -1.0;
//        points[2][0] = -4.5;
//        points[2][1] = 0.0;
//        points[3][0] = 0.0;
//        points[3][1] = 0.0;

        //general equation of line
        double [][] norms = getNorms(points);
        double c1 = norms[0][0] * points[0][0] + norms[0][1] * points[0][1];
        c1 = -c1;
        double c2 = norms[1][0] * points[2][0] + norms[1][1] * points[2][1];
        c2 = -c2;

        //intersection x coordinate
        double x = (c1 * norms[1][1] - c2 * norms[0][1]) /
                (norms[1][0] * norms[0][1] - norms[0][0] * norms[1][1]);

        //intersection y coordinate
//        double y = (-c1 - norms[0][0] * x) /
//                norms[0][1];
        double y = (c1 * norms[1][0] - c2 * norms[0][0])/
                (norms[1][1] * norms[0][0] - norms[0][1] * norms[1][0]);

        out.printf("Ix = %.3f\n", x+0.0);
        out.printf("Iy = %.3f\n", y+0.0);
    }

    /**
     * Returs array of norm vectors of two lines represented by 4 points in array points [x][y].
     * @param points    array of coordinates of points representing two lines
     * @return          array of two norm vectors for given points
     */
    private static double[][] getNorms(double[][]points) {
        double pom, a, b;
        int j = 0;
        double [][]norms = new double[3][2];

        for (int i = 0; i < points.length; i+=2) {
            //vector
            a = points[i][0] - points[i+1][0];
            b = points[i][1] - points[i+1][1];
            //norm
            pom = a;
            a = b;
            b = -pom;
            //result norm
            norms[j][0] = a;
            norms[j][1] = b;
            j++;
        }
        return norms;
    }
}
