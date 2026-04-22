package com.example.smartcampus.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getDiscovery() {
        Map<String, Object> discovery = new HashMap<>();

        discovery.put("apiName", "Smart Campus Sensor & Room Management API");
        discovery.put("version", "v1");
        discovery.put("adminContact", "smartcampus@westminster.ac.uk");

        Map<String, String> resources = new HashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");

        discovery.put("resources", resources);

        return discovery;
    }
}