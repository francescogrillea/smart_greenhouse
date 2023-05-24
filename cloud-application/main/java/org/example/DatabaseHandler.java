package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private final String port = "3306";
    private final String name = "smart_greenhouse";
    private final String user = "root";
    private final String pass = "123";
    private final String ip_db = "localhost";

    public boolean addActuatorIP(String ip){
        System.out.println("[ACTUATOR IS BEING REGISTERED]");
        String url = "jdbc:mysql://"+ip_db+":"+port+"/"+name;
        String sql = "INSERT INTO actuators(ip) values (?)";
        try (Connection co = DriverManager.getConnection(url, user, pass);
             PreparedStatement pr = co.prepareStatement(sql)){
            pr.setString(1, ip);
            int rowsInserted = pr.executeUpdate();
            if(rowsInserted <=0){
                System.out.println("[ACTUATORS]: No rows inserted.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("[ACTUATOR INSERTED]: "+ip);
        return true;
    }

    public boolean addTemperature(double temp, String ip){
        String url = "jdbc:mysql://"+ip_db+":"+port+"/"+name;
        String sql = "INSERT INTO SensorData(ip, temperature) values (?,?)";
        try (Connection co = DriverManager.getConnection(url, user, pass);
             PreparedStatement pr = co.prepareStatement(sql)){
            pr.setString(1, ip);
            pr.setDouble(2, temp);
            int rowsInserted = pr.executeUpdate();
            if(rowsInserted <=0){
                System.out.println("[SENSORDATA]: No rows inserted.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("[RELEVATION INSERTED]: "+ip+" "+temp);
        return true;
    }
}
