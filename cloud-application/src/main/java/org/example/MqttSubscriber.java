package org.example;

import org.eclipse.paho.client.mqttv3.*;

public class MqttSubscriber implements MqttCallback {
    private final String topic ="temperature";
    private final String content = "Message from contiki publishers";
    private final String broker = "tcp://127.0.0.1:1883";
    private final String clientId = "SmartGreenhouse";
    public MqttSubscriber() throws MqttException {
        MqttClient mqttClient = new MqttClient(broker,clientId);
        mqttClient.setCallback( this );
        mqttClient.connect();
        mqttClient.subscribe(topic);
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection with broker lost...");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println(String.format("[%s] %s" , topic, new String( mqttMessage.getPayload())));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("Delivery completed!")
    }
}
