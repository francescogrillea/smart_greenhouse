package org.example;


import org.eclipse.californium.core.CoapServer;
import org.eclipse.paho.client.mqttv3.MqttException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CloudApplication{
    public static void main( String[] args ){

        // MySQL Connection
        DatabaseHandler databaseHandler = new DatabaseHandler("config.properties");


        // CoAP Server that stores actuators info in a MySQL DB
        CoapServer server = new CoapServer();
        server.add(new CoapRegistrationResource("registration", databaseHandler));
        server.start();

        // MQTT subscriber launch
        MqttSubscriber myMQTTSubscriber = new MqttSubscriber(databaseHandler);
    }
}
