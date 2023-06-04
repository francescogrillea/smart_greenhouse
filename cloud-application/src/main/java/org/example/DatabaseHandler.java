package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseHandler {
    private String db_IP;
    private String port;
    private String db_name;
    private String user;
    private String password;

    public DatabaseHandler(String config_path) {
        Properties config = new Properties();
        try (FileInputStream input = new FileInputStream(config_path)) {
            config.load(input);
        } catch (IOException e) {
            System.err.println("Failed to load configuration file!");
            e.printStackTrace();
        }

        this.db_IP = config.getProperty("db.IP");
        this.db_name = config.getProperty("db.name");
        this.port = config.getProperty("db.port");
        this.user = config.getProperty("db.username");
        this.password = config.getProperty("db.password");
    }
    public boolean addActuator(String ip, int greenhouseId, String role){
        System.out.println("[ACTUATOR IS BEING REGISTERED]");
        String url = "jdbc:mysql://"+ip_db+":"+port+"/"+name;
        String sql = "INSERT INTO actuators(ip, greenhouseid, role) values (?,?,?)";
        try (Connection co = DriverManager.getConnection(url, user, pass);
             PreparedStatement pr = co.prepareStatement(sql)){
            pr.setString(1, ip);
            pr.setInt(2,greenhouseId);
            pr.setString(3,role);
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

    public boolean addTemperature(double temp, String ip, int greenhouseId){
        String url = "jdbc:mysql://"+ip_db+":"+port+"/"+name;
        String sql = "INSERT INTO SensorData(ip,greenhouseid, temperature) values (?,?,?)";
        try (Connection co = DriverManager.getConnection(url, user, pass);
             PreparedStatement pr = co.prepareStatement(sql)){
            pr.setString(1, ip);
            pr.setInt(2,greenhouseId);
            pr.setDouble(3, temp);
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
