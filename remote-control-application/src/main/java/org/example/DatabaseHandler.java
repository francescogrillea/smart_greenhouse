package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private final String ip = "localhost";
    private final String port = "3306";
    private final String name = "smart_greenhouse";
    private final String user = "root";
    private final String pass = "123";

    public List<String> findActuatorsIPs (){
        List<String> ips = new ArrayList<>();
        String url = "jdbc:mysql://"+ip+":"+port+"/"+name;
        String sql = "SELECT ip FROM actuators";
        try (Connection co = DriverManager.getConnection(url, user, pass);
        PreparedStatement pr = co.prepareStatement(sql)){
            ResultSet r = pr.executeQuery();
            while(r.next()){
                ips.add(r.getString(1));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ips;
    }

    public List<Double> findLastTemperatures(int numRows){
        List<Double> temps = new ArrayList<>();
        String url = "jdbc:mysql://"+ip+":"+port+"/"+name;
        String sql = "SELECT temperature FROM SensorData ORDER BY timestamp DESC LIMIT "+numRows;
        try (Connection co = DriverManager.getConnection(url, user, pass);
             PreparedStatement pr = co.prepareStatement(sql)){
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
