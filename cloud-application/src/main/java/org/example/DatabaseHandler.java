package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The DatabaseHandler class handles database operations for actuator and sensor data.
 */
public class DatabaseHandler {
    private String db_IP;
    private String port;
    private String db_name;
    private String user;
    private String password;

    /**
     * Constructs a new DatabaseHandler object by loading the database configuration from a file.
     *
     * @param config_path  the path to the configuration file
     */
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

    /**
     * Adds an actuator to the database.
     *
     * @param ip            the IP address of the actuator
     * @param greenhouseId  the ID of the associated greenhouse
     * @param role          the role of the actuator
     * @return true if the actuator is successfully added, false otherwise
     */
    public boolean addActuator(String ip, int greenhouseId, String role){
        String url = "jdbc:mysql://"+db_IP+":"+port+"/"+db_name;
        String sql = "INSERT INTO Actuators(IP_Actuator, ID_Greenhouse, Role) values (?,?,?)";
        try (Connection co = DriverManager.getConnection(url, user, password);
             PreparedStatement pr = co.prepareStatement(sql)){
            pr.setString(1, ip);
            pr.setInt(2,greenhouseId);
            pr.setString(3,role);
            int rowsInserted = pr.executeUpdate();
            if(rowsInserted <=0){
                System.out.println("[ACTUATOR ALREADY REGISTERED]");
                return false;
            }
        } catch(SQLIntegrityConstraintViolationException e){
            System.out.println("[ACTUATOR ALREADY REGISTERED]");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("[ACTUATOR REGISTERED]: GreenHouseID: " + greenhouseId + " Role: " + role);

        return true;
    }

    /**
     * Adds a temperature reading to the database.
     *
     * @param temp          the temperature value
     * @param ip            the IP address of the sensor
     * @param greenhouseId  the ID of the associated greenhouse
     * @return true if the temperature reading is successfully added, false otherwise
     */
    public boolean addTemperature(double temp, String ip, int greenhouseId){
        String url = "jdbc:mysql://"+db_IP+":"+port+"/"+db_name;
        String sql = "INSERT INTO SensorData(IP_Sensor, ID_Greenhouse, Temperature) values (?,?,?)";
        try (Connection co = DriverManager.getConnection(url, user, password);
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
            System.out.println("[RELEVATION NOT INSERTED]");
            e.printStackTrace();
            return false;
        }
        System.out.println("[RELEVATION INSERTED]:\t From " + ip + " Value: " + temp);
        return true;
    }
    
}
