package ppa1;

import java.util.Scanner;

import static java.lang.System.out;

/**
 * Calculates number of solutions for a quadratic equation on a given interval.
 */
public class Ppa1u03a {
    /**
     * Input from console
     */
    public static Scanner sc = new Scanner(System.in);

    /**
     * Calculates number of roots for a quadratic equation in a given interval.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        out.print("Zadej koeficient a: ");
        double a = sc.nextDouble();
        out.print("Zadej koeficient b: ");
        double b = sc.nextDouble();
        out.print("Zadej koeficient c: ");
        double c = sc.nextDouble();

        out.print("Zadej pocatek intervalu x1: ");
        double x1 = sc.nextDouble();
        out.print("Zadej konec intervalu x2: ");
        double x2 = sc.nextDouble();

        //Sort interval points
//        if (x1 > x2) {
//            double pom = x1;
//            x1 = x2;
//            x2 = pom;
//        }

        //diskriminant
        double dis = b * b - 4 * a * c;

        //roots
        double res1 = Double.MIN_VALUE;
        double res2 = Double.MIN_VALUE;

        //number of roots in a given interval
        int resNum = 0;

        if (a < 0.00001) {
            //divide by zero
            resNum = 0;
        } else if (Math.abs(dis) < 0.00001) {
            //one solution
            res1 = -b / 2 * a;

            if (isIn(x1, x2, res1)) {
                resNum = 1;
            }
        } else if (dis > 0) {
            //two solutions
            res1 = (-b + Math.sqrt(dis)) / (2 * a);
            res2 = (-b - Math.sqrt(dis)) / (2 * a);

            if (isIn(x1, x2, res1)) resNum++;
            if (isIn(x1, x2, res2)) resNum++;
        }

        out.print("reseni: ");
        switch (resNum) {
            case 0:
                out.println("neexistuje");
                break;
            case 1:
                out.println("existuje jedno");
                break;
            case 2:
                out.println("existuji dve");
        }

    }

    /**
     * Returns true if x1 <= res <= x2. Returns false otherwise.
     *
     * @param x1  leftmost border of the interval
     * @param x2  rightmost border of the interval
     * @param res examined value
     * @return true for x1 <= res <= x2
     */
    public static boolean isIn(double x1, double x2, double res) {
        if (res >= x1 && res <= x2) {
            return true;
        }
        return false;

    }
}
