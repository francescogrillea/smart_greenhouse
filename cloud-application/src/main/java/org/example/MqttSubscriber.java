package org.example;

import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.paho.client.mqttv3.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;

public class MqttSubscriber implements MqttCallback {
    private final String topic ="temperature";
    private final String content = "Message from contiki publishers";
    private final String broker = "tcp://127.0.0.1:1883";
    private final String clientId = "SmartGreenhouse";
    private Connection mysql_connection;
    public MqttSubscriber(Connection connection) throws MqttException {

        this.mysql_connection = connection;

        MqttClient mqttClient = new MqttClient(broker,clientId);
        mqttClient.setCallback( this );
        mqttClient.connect();
        mqttClient.subscribe(topic);
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection with broker lost..." + throwable.getMessage());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String message = new String( mqttMessage.getPayload());

        String sql = "INSERT INTO SensorData (timestamp, temperature, IP_address) VALUES (?, ?, ?)";
        double temperature = Double.parseDouble(message);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ip = null;

        try {
            PreparedStatement statement = this.mysql_connection.prepareStatement(sql);
            statement.setTimestamp(1, timestamp);
            statement.setDouble(2, temperature);
            statement.setString(3, ip);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted <= 0) {
                System.out.println("No new rows inserted!");
            }

            statement.close();
            // TODO - capire se va chiusa anche la connessione col DB e quando

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("Delivery completed!");
    }
}
