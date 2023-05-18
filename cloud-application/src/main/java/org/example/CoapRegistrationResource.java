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

    Connection mysql_connection;

    public CoapRegistrationResource(String name, Connection connection) {
        super(name);
        this.mysql_connection = connection;
        setObservable(true);
    }

    public void handleGET(CoapExchange exchange) {
        exchange.respond("hello world");
    }
    public void handlePOST(CoapExchange exchange) {

        Response response = new Response(CoAP.ResponseCode.CONTENT);
        String sql = "INSERT INTO SensorData (timestamp, temperature, IP_address) VALUES (?, ?, ?)";

        double temperature = 0.0;
        Timestamp timestamp;
        String ip = null;

        if(exchange.getRequestOptions().getContentFormat() == MediaTypeRegistry.APPLICATION_JSON) {
            String payload = exchange.getRequestText();
            System.out.println(payload);
            JSONObject requestJson = new JSONObject();

            try {
                requestJson = (JSONObject) JSONValue.parseWithException(payload);
                temperature = (double)((long) requestJson.get("temperature")) / 10;
                timestamp = new Timestamp(System.currentTimeMillis());
                ip = (String)requestJson.get("IP");

            } catch (ParseException e) {
                requestJson.put("value", 500);  // Server error!
                throw new RuntimeException(e);
            }

            try {
                PreparedStatement statement = this.mysql_connection.prepareStatement(sql);
                statement.setTimestamp(1, timestamp);
                statement.setDouble(2, temperature);
                statement.setString(3, ip);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted <= 0) {
                    System.out.println("No new rows inserted!");
                    response.setPayload("500");
                }

                statement.close();
                // TODO - capire se va chiusa anche la connessione col DB e quando

            } catch (SQLException e) {
                response.setPayload("500");
                throw new RuntimeException(e);
            }
            // response creation
            response.setPayload("200");

        }
        else{
            response.setPayload("Response but not in JSON format!");
        }
        exchange.respond(response);
        System.out.println("Response sent!");
    }
}
