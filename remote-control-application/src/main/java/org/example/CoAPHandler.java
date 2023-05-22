package org.example;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.util.List;

public class CoAPHandler {
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
