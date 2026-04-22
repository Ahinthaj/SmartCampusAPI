/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.smartcampus.resources;

import com.example.smartcampus.dao.RoomDAO;
import com.example.smartcampus.dao.SensorDAO;
import com.example.smartcampus.exception.LinkedResourceNotFoundException;
import com.example.smartcampus.model.Room;
import com.example.smartcampus.model.Sensor;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/sensors")
public class SensorResource {

    private SensorDAO sensorDAO = new SensorDAO();
    private RoomDAO roomDAO = new RoomDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        if (type != null && !type.trim().isEmpty()) {
            return sensorDAO.getSensorsByType(type);
        }
        return sensorDAO.getAllSensors();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSensor(Sensor sensor, @Context UriInfo uriInfo) {
        Room room = roomDAO.getRoomById(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException(
                    "Cannot create sensor. Room with ID " + sensor.getRoomId() + " does not exist."
            );
        }

        sensorDAO.addSensor(sensor);

        if (room.getSensorIds() != null) {
            room.getSensorIds().add(sensor.getId());
        }

        URI newSensorUri = uriInfo.getAbsolutePathBuilder()
                .path(sensor.getId())
                .build();

        return Response.created(newSensorUri)
                .entity(sensor)
                .build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}