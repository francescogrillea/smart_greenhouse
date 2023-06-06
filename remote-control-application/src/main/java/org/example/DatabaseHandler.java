package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
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


    public List<String> findTentsIPs (int greenhouseId){
        List<String> ips = new ArrayList<>();
        String url = "jdbc:mysql://"+db_IP+":"+port+"/"+db_name;
        String sql = "SELECT IP_Actuator FROM Actuators WHERE ID_Greenhouse=? AND Role=\"tent\"";
        try (Connection co = DriverManager.getConnection(url, user, password);
        PreparedStatement pr = co.prepareStatement(sql)){
            pr.setInt(1,greenhouseId);
            ResultSet r = pr.executeQuery();
            while(r.next()){
                ips.add(r.getString(1));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ips;
    }

    public List<Double> findLastTemperatures(int numRows, int greenhouseId){
        List<Double> temps = new ArrayList<>();
        String url = "jdbc:mysql://"+db_IP+":"+port+"/"+db_name;
        String sql = "SELECT Temperature FROM SensorData WHERE ID_Greenhouse=? ORDER BY Timestamp DESC LIMIT "+numRows;
        try (Connection co = DriverManager.getConnection(url, user, password);
             PreparedStatement pr = co.prepareStatement(sql)){
            pr.setInt(1,greenhouseId);
            ResultSet r = pr.executeQuery();
            while(r.next()){
                temps.add( r.getDouble(1));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return temps;
    }
}
