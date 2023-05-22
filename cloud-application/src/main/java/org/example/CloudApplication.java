package org.example;


import org.eclipse.californium.core.CoapServer;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CloudApplication{
    public static void main( String[] args ){

        // MySQL Connection
        final String mysql_url = "jdbc:mysql://localhost:3307/smart_greenhouse";
        final String mysql_username = "root";
        final String mysql_password = "root";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(mysql_url, mysql_username, mysql_password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        // CoAP Server that stores actuators info in a MySQL DB
        CoapServer server = new CoapServer();
        server.add(new CoapRegistrationResource("registration", connection));
        server.start();

        // MQTT subscriber launch
        try {
            MqttSubscriber myMQTTSubscriber = new MqttSubscriber(connection);
        } catch (MqttException me ){
            me.printStackTrace();
        }

    }
}
