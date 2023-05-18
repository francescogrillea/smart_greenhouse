package org.example;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class CoapRegistrationResource extends CoapResource {
    public CoapRegistrationResource(String name) {
        super(name);
        setObservable(true);
    }

    public void handleGET(CoapExchange exchange) {
        exchange.respond("hello world");
    }
    public void handlePOST(CoapExchange exchange) {

        Response response = new Response(CoAP.ResponseCode.CONTENT);
        System.out.println("Request is arriving");
        if(exchange.getRequestOptions().getContentFormat() == MediaTypeRegistry.APPLICATION_JSON) {
            String payload = exchange.getRequestText();
            System.out.println(payload);

            try {
                // TODO - settare per bene il nome dei campi
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

            // TODO - add to MySQL Database

            exchange.respond(response);
        }
    }
}
