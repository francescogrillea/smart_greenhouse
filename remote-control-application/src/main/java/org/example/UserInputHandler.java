package org.example;

import javax.xml.crypto.Data;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class UserInputHandler
{
    static DatabaseHandler databaseHandler;
    static int numMillis=10000;
    public static void main( String[] args )
    {
        System.out.println("Welcome in the remote control application prompt.");
        System.out.println("[SYSTEM STARTING...]");
        databaseHandler = new DatabaseHandler();
        RemoteControlApplicationThread remoteControlApplicationThread = new RemoteControlApplicationThread(numMillis, databaseHandler);
        remoteControlApplicationThread.start();
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        System.out.println("[SYSTEM STARTED");
        while(input != 4) {
            System.out.println("[PROMPT]");
            System.out.println("- If you want to run the application by yourself enter 1");
            System.out.println("- If you want to change the temperature threshold enter 2");
            System.out.println("- If you want to change the timing of database's checks enter 3");
            System.out.println("- If you want to leave enter 4");
            // .....
            input = scanner.nextInt();
            switch (input) {
                case 1:
                    System.out.println("[TURNING OFF THE REMOTE CONTROL APPLICATION]");
                    remoteControlApplicationThread.stopCheckingDB();
                    do {
                        System.out.println("[REMOTE CONTROL APPLICATION SHUT DOWN");
                        System.out.println("- If you want to turn up tents press 1");
                        System.out.println("- If you want to turn down tents press 2");
                        System.out.println("- If you want to activate again the remote control application press 3");
                        input = scanner.nextInt();
                        switch (input){
                            case 1:
                                // turn up tents
                                break;
                            case 2:
                                // turn down tents
                                break;
                        }
                    } while (input != 3);
                    break;
                case 2:
                    System.out.println("- Enter a new temperature threshold");
                    input = scanner.nextInt();
                    remoteControlApplicationThread.changeTemperatureThreshold(input);
                    break;
                case 3:
                    System.out.println("- Enter a new timing");
                    input=scanner.nextInt();
                    remoteControlApplicationThread.changeTiming(input);
                    break;
            }
        }
        System.out.println("Good bye...");
    }
    public void turnUpTents(){
        // to implement
    }

    public void turnDownTents(){
        // to implement
    }

}
