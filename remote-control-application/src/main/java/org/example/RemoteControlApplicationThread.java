package org.example;

import javax.xml.crypto.Data;

public class RemoteControlApplicationThread extends Thread{
    private boolean isRunning;
    private final Object lock = new Object();
    private int numMillis;
    private int temperatureThreshold;
    private final DatabaseHandler databaseHandler;

    public RemoteControlApplicationThread(int numMillis, DatabaseHandler databaseHandler) {
        this.numMillis = numMillis;
        isRunning=true;
        this.databaseHandler= databaseHandler;
    }

    public void run(){
        while(isRunning){
            // compute the average temperature for the last 5 secs by accessing to the DB
            // if temperature too high/too low and tents are down/up
            // retrieve the IPs of the actuators
            // send COAP message to actuators
            synchronized(lock){
                try{
                    lock.wait(numMillis);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void stopCheckingDB(){
        isRunning=false;
        synchronized(lock){
            lock.notify();
        }
    }
    public synchronized void changeTemperatureThreshold(int tt){
        temperatureThreshold = tt;
    }

    public synchronized void changeTiming(int numMillis){
        this.numMillis=numMillis;
    }
}
