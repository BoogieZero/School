package ppa1;

import java.util.ArrayList;
import java.util.Scanner;

public class Ppa1u06 {

    private static ArrayList<Integer> primes = new ArrayList<Integer>();

    public static boolean jePrvocislo(int cislo) {
        if (primes.contains(cislo)) return true;
        if(cislo == 1) return false;

        for (int i = cislo - 1; i > 1; i--) {
            if (cislo % i == 0) return false;
        }

        primes.add(cislo);
        return true;
    }

    public static int jeSoucetPrvocisel(int cislo) {
        if (cislo <= 0) return 0;

        int num = 2;
        while (num <= cislo / 2) {
            if (jePrvocislo(num) && jePrvocislo(cislo - num)) {
                return num;
            }
            num++;
        }

        return 0;
    }

    public static int nactiPrirozeneCislo(Scanner sc) {
        int num = 0;
        while(true){
            System.out.print("Zadej prirozene cislo: ");
            num = sc.nextInt();
            if(num > 0) return num;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = nactiPrirozeneCislo(sc);
        int result = jeSoucetPrvocisel(num);
        if (result == 0) {
            System.out.println("Nelze rozlozit.");
        } else {
            System.out.println("Lze rozlozit: " + result + " + " + (num - result));
        }

    }
}
