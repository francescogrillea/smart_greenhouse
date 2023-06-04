package org.example;

import java.util.List;

public class RemoteControlApplicationThread extends Thread{
    private boolean isRunning;
    private final Object lock = new Object();
    private int numMillis;
    private int temperatureThreshold=28;
    private final DatabaseHandler databaseHandler;
    private final CoAPHandler coapHandler;
    private String tentState="up";
    private final int greenhouseId;

    public RemoteControlApplicationThread(int numMillis, DatabaseHandler databaseHandler, CoAPHandler coapHandler, int greenhouseId) {
        this.numMillis = numMillis;
        isRunning=true;
        this.databaseHandler= databaseHandler;
        this.coapHandler = coapHandler;
        this.greenhouseId=greenhouseId;
    }

    public void run(){
        while(isRunning){
            // compute the average temperature for the last 5 secs by accessing to the DB
            List<Double> lastTemps = databaseHandler.findLastTemperatures(10, greenhouseId);
            double averageTemp= 0;
            for(Double f: lastTemps){
                averageTemp+=f;
            }
            averageTemp = averageTemp/lastTemps.size();
            // if temperature too high/too low and tents are down/up
            // send COAP message to actuators
            synchronized((Integer)temperatureThreshold) {
                if (averageTemp > temperatureThreshold && tentState.equals("down")) {
                    coapHandler.sendMessage("tent", "up", databaseHandler.findTentsIPs(greenhouseId));
                    tentState = "up";
                    System.out.println("[TENTS UP]");
                }
                if (averageTemp < temperatureThreshold && tentState.equals("up")) {
                    coapHandler.sendMessage("tent", "down", databaseHandler.findTentsIPs(greenhouseId));
                    System.out.println("[TENTS DOWN]");
                    tentState = "down";
                }
            }
            // go to sleep for the next numMillis milliseconds
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
    public void changeTemperatureThreshold(int tt){
        synchronized ((Integer)temperatureThreshold){
            temperatureThreshold = tt;
        }
    }

    public void changeTiming(int numMillis){
        synchronized ((Integer)numMillis) {
            this.numMillis = numMillis;
        }
    }
}
