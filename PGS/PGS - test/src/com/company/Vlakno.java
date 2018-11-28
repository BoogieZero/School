package com.company;

/**
 * Created by Boogie Zero on 07/06/2016.
 */
public class Vlakno extends Thread {
    private String name;
    private boolean running = true;
    public boolean stop = false;

    public Vlakno(String name){

        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Start "+ name);
        int count = 0;
        while(running){
            System.out.println("Name: "+name + " č.: "+count);
            count++;
            while(stop){
                try {
                    synchronized (this){
                        wait();
                    }
                } catch (InterruptedException e) {
                }
            }
        }
        System.out.println("End "+name);
    }

    public synchronized void end(){
        running = false;
        notify();
    }

    public synchronized void temporaryStop() {
        if(stop){
            stop = false;
            this.notify();
        }else{
            stop = true;
        }
    }
}
