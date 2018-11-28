package ppa1;

import java.util.Scanner;

import static java.lang.System.out;

public class Ppa1u02a {
    public static void main(String []args){
        Scanner sc = new Scanner(System.in);

        //inputs
        out.print("Zadej plne jizdne [Kc]: ");
        int full = sc.nextInt();
        out.print("Zadej slevu [%]: ");
        int discount = sc.nextInt();
        out.print("Zadej cenu slevove karty [Kc]: ");
        int card = sc.nextInt();
        double discounted = full * (1.0 - (discount/100.0));

        out.printf("bezne jizdne: %.2f\n", (double)full);
        out.printf("zlevnene jizdne: %.2f\n", discounted);
        out.printf("1 jizda: %.2f\n", card + discounted);
        out.printf("2 jizdy: %.2f\n", (card + 2 * discounted)/2);
        out.printf("3 jizdy: %.2f\n", (card + 3 * discounted)/3);

    }
}
