package ppa1;

import java.util.Scanner;

import static java.lang.System.out;

/**
 * Calculates time to get from A to C. Outputs travel time if it's below maxTime threshold.
 */
public class Ppa1u03b {
    private static Scanner sc = new Scanner(System.in);
    private static final int toMin = 60;
    private static final double toHour = 1.0 / 60.0;
    private static final int minInDay = 60 * 24;
    private static final int maxTime = 60 * 12;

    public static void main(String[] args) {
        out.println("Zadej cas odjezdu z mista A do mista B:");
        out.print("hodina: ");
        int ABleaveH = sc.nextInt();
        out.print("minuta: ");
        int ABleaveM = sc.nextInt();

        out.println("Zadej cas prejezdu z mista A do mista B:");
        out.print("hodina: ");
        int ABtimeH = sc.nextInt();
        out.print("minuta: ");
        int ABtimeM = sc.nextInt();

        out.println("Zadej cas odjezdu z mista B do mista C:");
        out.print("hodina: ");
        int BCleaveH = sc.nextInt();
        out.print("minuta: ");
        int BCleaveM = sc.nextInt();

        out.println("Zadej cas prejezdu z mista B do mista C:");
        out.print("hodina: ");
        int BCtimeH = sc.nextInt();
        out.print("minuta: ");
        int BCtimeM = sc.nextInt();

//        int ABleaveH = 5;
//        int ABleaveM = 20;
//        int ABtimeH = 1;
//        int ABtimeM = 0;
//
//        int BCleaveH = 6;
//        int BCleaveM = 20;
//        int BCtimeH = 1;
//        int BCtimeM = 0;

        //time in min
        int ABLeave = ABleaveH * toMin + ABleaveM;
        int ABTime = ABtimeH * toMin + ABtimeM;
        int BCLeave = BCleaveH * toMin + BCleaveM;
        int BCTime = BCtimeH * toMin + BCtimeM;

        int result = ABTime;                            //first route
        result += getTimeTo((ABLeave + ABTime) % minInDay, BCLeave);   //waiting for second route
        result += BCTime;                               //second route

        out.print("Cesta: ");
        if (result > maxTime) {
            out.print("spoje nelze pouzit\n");
            return;
        }

        //convert to hour:minute time
        int hours = (int) (result * toHour);
        int minutes = result - hours * toMin;

        out.print(hours + ":" + minutes+"\n");
    }

    /**
     * Calculates wait time in minutes between from and to.
     *
     * @param from start time in day (minutes)
     * @param to   target time in day (minutes)
     * @return return difference between given times beginning at from
     */
    private static int getTimeTo(int from, int to) {
        if (from <= to) {
            //same day
            return to - from;
        }
        //over night
        return minInDay - from + to;
    }
}
