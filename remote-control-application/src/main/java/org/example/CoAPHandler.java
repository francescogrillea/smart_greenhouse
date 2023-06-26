package org.example;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;

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
            String url = "coap://["+s+"]/greenhouse/"+resource;
            CoapClient client = new CoapClient(url);
            Request req = new Request(CoAP.Code.PUT);
            req.setPayload(command);
            req.getOptions().setAccept(MediaTypeRegistry.TEXT_PLAIN);
            CoapResponse response = client.advanced(req);
            if(response!=null) {
                CoAP.ResponseCode code = response.getCode();
                switch (code) {
                    case CHANGED:
                        // caso 204
                        System.out.println("[RESPONSE: 204 OK]");
                        break;
                    case BAD_REQUEST:
                        System.out.println("[RESPONSE: INTERNAL APPLICATION ERROR]");
                        break;
                    case BAD_OPTION:
                        System.out.println("[RESPONSE: BAD OPTION ERROR]");
                        break;
                    default:
                        System.out.println("[RESPONSE: ACTUATOR ERROR]");
                        break;
                }
            }
        }
    }
}
