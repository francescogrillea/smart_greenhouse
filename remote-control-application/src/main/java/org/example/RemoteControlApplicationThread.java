package org.example;

import java.util.List;

/**
 * The RemoteControlApplicationThread class represents a thread that monitors temperature conditions
 * and controls the state of tents in a greenhouse based on temperature thresholds.
 */
public class RemoteControlApplicationThread extends Thread{
    private boolean isRunning;
    private final Object lock = new Object();
    private int numMillis;
    private int temperatureThreshold=28;
    private final DatabaseHandler databaseHandler;
    private final CoAPHandler coapHandler;
    private String tentState;
    private final int greenhouseId;


    /**
     * Constructs a new RemoteControlApplicationThread object.
     *
     * @param numMillis         the duration of the sleep interval in milliseconds
     * @param databaseHandler   the instance of DatabaseHandler to interact with the database
     * @param coapHandler       the instance of CoAPHandler to send CoAP messages to actuators
     * @param greenhouseId      the ID of the greenhouse to monitor and control
     */
    public RemoteControlApplicationThread(int numMillis, DatabaseHandler databaseHandler, CoAPHandler coapHandler, int greenhouseId, String tentState) {
        this.numMillis = numMillis;
        isRunning=true;
        this.databaseHandler= databaseHandler;
        this.coapHandler = coapHandler;
        this.greenhouseId=greenhouseId;
        this.tentState=tentState;
    }

    /**
     * The main execution logic of the thread.
     */
    public void run(){
        while(isRunning){
            // compute the average temperature for the last 10 rows by accessing to the DB
            // we check the last 10 rows of the database to be sure
            // new records are 4: 2 new data each 5 seconds. We check a sliding overlapping window of size 10
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


    /**
     * Stops the monitoring and control process.
     */
    public void stopCheckingDB(){
        isRunning=false;
        synchronized(lock){
            lock.notify();
        }
    }

    /**
     * Changes the temperature threshold value.
     *
     * @param tt the new temperature threshold
     */    public void changeTemperatureThreshold(int tt){
        synchronized ((Integer)temperatureThreshold){
            temperatureThreshold = tt;
        }
    }

    /**
     * Changes the sleep interval duration.
     *
     * @param numMillis the new sleep interval duration in milliseconds
     */
    public void changeTiming(int numMillis){
        synchronized ((Integer)numMillis) {
            this.numMillis = numMillis;
        }
    }

    public String getTentState() {
        return tentState;
    }
}
