package ppa1;

import java.util.Locale;
import java.util.Scanner;

import static java.lang.System.*;

public class Ppa1u04a {
    private static final double STEP = 1.0 / 1024;
    private static Scanner sc = new Scanner(in).useLocale(Locale.US);

    private static double a;
    private static double b;
    private static double c;
    private static double d;

    public static double getFunctionValue(double x) {
        return a * x * x * x + b * x * x + c * x + d;
    }

    public static void main(String[] args) {
//        String input = sc.nextLine();
//        String input = "a=0.1, b=-1, c=-10, d=50, X1=-4, X2=11 ";
//        input = input.replaceAll("\\s", "");    //white spaces
//        String[] inptSplit = input.split(",");

//        double[] inptValues = new double[inptSplit.length];
//        String s;
//        int i = 0;
//
//        //a, b, c, d
//        for (; i < 4; i++) {
//            s = inptSplit[i].substring(2);
//            inptValues[i] = Double.parseDouble(s);
//        }
//
//        //x1, x2
//        for (; i < inptSplit.length; i++) {
//            s = inptSplit[i].substring(3);
//            inptValues[i] = Double.parseDouble(s);
//        }


        double[] inptValues = new double[6];
        out.print("a=");
        inptValues[0] = sc.nextDouble();
        out.print("b=");
        inptValues[1] = sc.nextDouble();
        out.print("c=");
        inptValues[2] = sc.nextDouble();
        out.print("d=");
        inptValues[3] = sc.nextDouble();
        out.print("x1=");
        inptValues[4] = sc.nextDouble();
        out.print("x2=");
        inptValues[5] = sc.nextDouble();

        //parameters
        a = inptValues[0];
        b = inptValues[1];
        c = inptValues[2];
        d = inptValues[3];

        //extremes
        double x = inptValues[4];
        double xf = getFunctionValue(x);
        double df1 = getFunctionValue(x - STEP);
        double df2 = getFunctionValue(x + STEP);
        double diff1, diff2;


        while (x <= inptValues[5]) {
            diff1 = df1 - xf;
            diff2 = xf - df2;

            if (diff1 * diff2 < 0) {
                //extreme
                if(diff1 < 0){      //max
                    out.printf("max: %.2f\n", x);
                }else{              //min
                    out.printf("min: %.2f\n", x);
                }
            }

            //shift by step
            x += STEP;
            df1 = xf;
            xf = df2;
            df2 = getFunctionValue(x+STEP);
        }
    }
}
