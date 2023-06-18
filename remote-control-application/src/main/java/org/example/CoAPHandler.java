package org.example;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.util.List;

/**
 * The CoAPHandler class provides methods to send CoAP messages to actuators.
 */
public class CoAPHandler {

    /**
     * Sends a CoAP message to a list of actuators.
     *
     * @param resource     the resource to which the CoAP message should be sent
     * @param command      the command to be included in the CoAP message
     * @param actuatorsIPs the list of IP addresses of the actuators
     */
    public void sendMessage(String resource, String command, List<String> actuatorsIPs){
        for(String s: actuatorsIPs){
            String url = "coap://["+s+"]/"+resource;
            CoapClient client = new CoapClient(url);
            CoapResponse request = client.put(command, MediaTypeRegistry.TEXT_PLAIN);
            CoapResponse response = client.get();
            System.out.println("[RESPONSE: "+response.getCode()+" ]");
        }
    }
}
