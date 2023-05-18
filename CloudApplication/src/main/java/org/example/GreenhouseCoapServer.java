package org.example;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;


/**
 * Hello world!
 *
 */
public class GreenhouseCoapServer extends CoapServer
{
    static class CoapRegistrationResource extends CoapResource {
        public CoapRegistrationResource(String name) {
            super(name);
            setObservable(true);
        }
        public void handleGET(CoapExchange exchange) {
            exchange.respond("hello world");
        }
        public void handlePOST(CoapExchange exchange) {
            // read a number from the request payload
            // and reply back with the square of the
            // input number
            Response response = new Response(CoAP.ResponseCode.CONTENT);
            System.out.println("Request is arriving");
            if(exchange.getRequestOptions().getContentFormat() == MediaTypeRegistry.APPLICATION_JSON) {
                String payload = exchange.getRequestText();
                System.out.println(payload);
                try {
                    // request unpacking
                    JSONObject requestJson = (JSONObject) JSONValue.parseWithException(payload);
                    long value = (long) requestJson.get("value");
                    System.out.println("Value: " + value);
                    value = value * value;

                    // response creation
                    requestJson.put("value", value);
                    response.setPayload(requestJson.toJSONString());
                    response.getOptions().setContentFormat(MediaTypeRegistry.APPLICATION_JSON);

                    System.out.println("Response sent");
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                exchange.respond(response);
            }
        }
    }
}
