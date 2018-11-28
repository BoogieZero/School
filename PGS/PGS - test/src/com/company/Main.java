package com.company;

import java.util.Random;

public class Main {


    public static void main(String[] args) {
	// write your code here

        Vlakno sude = new Vlakno("sude");
        Vlakno liche = new Vlakno("liche");

        Random rnd = new Random();

        sude.start();
        liche.start();

        int ranInt;
        for (int i = 0; i < 10; i++) {
            System.out.println("Cyklus: "+i);
            ranInt = rnd.nextInt(10);

            if(ranInt % 2 == 0){
                sude.temporaryStop();
            }else{
                liche.temporaryStop();
            }

        }


        sude.end();
        liche.end();

        try {
            sude.join();
            liche.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
