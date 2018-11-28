package ppa1;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static java.lang.System.*;

public class Ppa1u04b {
    private static final int WINDOW_SIZE = 4;

    private static int denominator;
    private static Scanner sc = new Scanner(System.in).useLocale(Locale.US);

    /**
     * Weighted Moving Average
     * @param list  source data
     * @return  WMA
     */
    private static double getWMA(List<Double> list) {
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i) * (i + 1) / denominator;
        }
        return sum;
    }

    public static void main(String[] args) {
//        double[] inputs = {24.0, 23.7, 24.5, 25.0, 24.7, 25.5, 25.6, 25.0, 25.8, 26.3, 25.9, 26.8, 27.2, 26.5, 28.0, 0};

        denominator = 0;
        for (int d = 0; d < WINDOW_SIZE; d++) {
            denominator += d + 1;
        }

        List<Double> list = new ArrayList<Double>();
        int i = 0;
        double input;


        //window is not full
        for (int j = 0; j < WINDOW_SIZE - 1; j++) {
//            input = inputs[i];
            input = sc.nextDouble();
            if (input == 0) break;

            list.add(input);
            out.printf("%d;%.2f;\n", i + 1, input);
            i++;
        }

        //full window
        while (true){
//            input = inputs[i];
            input = sc.nextDouble();
            if (input == 0) break;

            list.add(input);
            out.printf("%d;%.2f;%.2f\n", i + 1, input, getWMA(list));
            i++;
            list.remove(0);
        }
    }
}
