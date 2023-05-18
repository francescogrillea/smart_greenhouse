package org.example;


import org.eclipse.californium.core.CoapServer;

public class CloudApplication{
    public static void main( String[] args ){

        // CoAP Server that stores actuators info in a MySQL DB
        CoapServer server = new CoapServer();
        server.add(new CoapRegistrationResource("registration"));
        server.start();

        // TODO - add MQTT Subscriber

    }
}
