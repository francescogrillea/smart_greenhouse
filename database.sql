CREATE DATABASE smart_greenhouse;
USE smart_greenhouse;

CREATE TABLE SensorData (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ID_Greenhouse INT,
    IP_Sensor VARCHAR(255),
    Temperature FLOAT,
    Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE Actuators (
    IP_Actuator VARCHAR(255) PRIMARY KEY,
    ID_Greenhouse INT,
    Role VARCHAR(255)
);
