/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.smartcampus.dao;

import com.example.smartcampus.model.Sensor;
import java.util.ArrayList;
import java.util.List;

public class SensorDAO {

    public List<Sensor> getAllSensors() {
        return new ArrayList<>(MockDatabase.SENSORS.values());
    }

    public List<Sensor> getSensorsByType(String type) {
        List<Sensor> filteredSensors = new ArrayList<>();

        for (Sensor sensor : MockDatabase.SENSORS.values()) {
            if (sensor.getType() != null && sensor.getType().equalsIgnoreCase(type)) {
                filteredSensors.add(sensor);
            }
        }

        return filteredSensors;
    }

    public Sensor getSensorById(String id) {
        return MockDatabase.SENSORS.get(id);
    }

    public void addSensor(Sensor sensor) {
        MockDatabase.SENSORS.put(sensor.getId(), sensor);
    }
}
