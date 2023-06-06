package org.example;

import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.paho.client.mqttv3.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;

public class MqttSubscriber implements MqttCallback {
    private final String topic ="temperature";
    private final String broker = "tcp://127.0.0.1:1883";
    private final String clientId = "SmartGreenhouse";
    private DatabaseHandler databaseHandler;
    public MqttSubscriber(DatabaseHandler databaseHandler){
        this.databaseHandler = databaseHandler;
        try {
            MqttClient mqttClient = new MqttClient(broker, clientId);
            mqttClient.setCallback(this);
            mqttClient.connect();
            mqttClient.subscribe(topic);
        } catch (MqttException me){
            me.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection with broker lost..." + throwable.getMessage());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        try {
            // message unpacking
            JSONObject requestJson = (JSONObject) JSONValue.parseWithException(new String(mqttMessage.getPayload()));
            String app = requestJson.get("app").toString();
            if(app.equals("smart_greenhouse")) {
                int greenhouseId = Integer.parseInt(requestJson.get("greenhouse_id").toString());
                int temp = Integer.parseInt(requestJson.get("temperature").toString());
                String ip = requestJson.get("ip").toString();
                double temp_double = (double) temp / 10;
                // adding to the database
                databaseHandler.addTemperature(temp_double, ip, greenhouseId);
            }
        } catch (ParseException e) {
            System.out.println("[RELEVATION NOT RECEIVED CORRECTLY]");
            throw new RuntimeException(e);
        }
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("Delivery completed!");
    }
}
