package org.example;

/**
 * Hello world!
 *
 */
public class CloudApplication
{
    public static void main( String[] args )
    {
        GreenhouseCoapServer server = new GreenhouseCoapServer();
        server.add(new GreenhouseCoapServer.CoapRegistrationResource("registration"));
        server.start();
    }
}
