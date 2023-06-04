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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CoapRegistrationResource extends CoapResource {

    DatabaseHandler databaseHandler;

    public CoapRegistrationResource(String name, DatabaseHandler databaseHandler) {
        super(name);
        this.databaseHandler = databaseHandler;
        setObservable(true);
    }

    public void handleGET(CoapExchange exchange) {
        exchange.respond("hello world");
    }
    public void handlePOST(CoapExchange exchange) {
        Response response = new Response(CoAP.ResponseCode.CONTENT);
        if(exchange.getRequestOptions().getContentFormat() == MediaTypeRegistry.APPLICATION_JSON) {
            String payload = exchange.getRequestText();
            try {
                // request unpacking
                JSONObject requestJson = (JSONObject) JSONValue.parseWithException(payload);
                String appName = (String) requestJson.get("app");
                System.out.println(appName);
                String role = (String) requestJson.get("role");
                System.out.println(role);
                int greenhouseId= Integer.parseInt(requestJson.get("greenhouse_id").toString());
                System.out.println(greenhouseId);
                if(appName.equals("smart_greenhouse")) {
                    databaseHandler.addActuator(exchange.getSourceAddress().toString().substring(1), greenhouseId, role);
                    response.setPayload("200");
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            response.setPayload("Actuator registration request is not in JSON format!");
        }
        exchange.respond(response);
    }
}
