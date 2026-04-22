package com.example.smartcampus.dao;

import com.example.smartcampus.model.Room;
import com.example.smartcampus.model.Sensor;
import com.example.smartcampus.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockDatabase {

    public static final Map<String, Room> ROOMS = new HashMap<>();
    public static final Map<String, Sensor> SENSORS = new HashMap<>();
    public static final Map<String, List<SensorReading>> READINGS = new HashMap<>();

    static {
        ROOMS.put("LIB-301", new Room("LIB-301", "Library Quiet Study", 60, new ArrayList<>()));
        ROOMS.put("ENG-201", new Room("ENG-201", "Engineering Lab", 40, new ArrayList<>()));
        ROOMS.put("SCI-101", new Room("SCI-101", "Science Seminar Room", 80, new ArrayList<>()));

        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 24.5, "LIB-301");
        Sensor s2 = new Sensor("CO2-001", "CO2", "MAINTENANCE", 600.0, "ENG-201");

        SENSORS.put(s1.getId(), s1);
        SENSORS.put(s2.getId(), s2);

        ROOMS.get("LIB-301").getSensorIds().add("TEMP-001");
        ROOMS.get("ENG-201").getSensorIds().add("CO2-001");

        READINGS.put("TEMP-001", new ArrayList<>());
        READINGS.put("CO2-001", new ArrayList<>());
    }
}