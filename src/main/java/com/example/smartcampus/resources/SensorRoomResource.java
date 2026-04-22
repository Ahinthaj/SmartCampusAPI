package com.example.smartcampus.resources;

import com.example.smartcampus.dao.RoomDAO;
import com.example.smartcampus.model.Room;
import com.example.smartcampus.exception.RoomNotEmptyException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import java.net.URI;
import java.util.List;

@Path("/rooms")
public class SensorRoomResource {

    private RoomDAO roomDAO = new RoomDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(Room room, @Context UriInfo uriInfo) {
        roomDAO.addRoom(room);

        URI newRoomUri = uriInfo.getAbsolutePathBuilder()
                .path(room.getId())
                .build();

        return Response.created(newRoomUri)
                .entity(room)
                .build();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getRoomById(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Room with ID " + roomId + " not found.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getRoomById(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Room with ID " + roomId + " not found.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room " + roomId + " cannot be deleted because it still has assigned sensors.");
        }

        roomDAO.deleteRoom(roomId);

        return Response.noContent().build();
    }
}
