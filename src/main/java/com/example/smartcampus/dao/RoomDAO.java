package com.example.smartcampus.dao;

import com.example.smartcampus.model.Room;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public List<Room> getAllRooms() {
        return new ArrayList<>(MockDatabase.ROOMS.values());
    }

    public Room getRoomById(String id) {
        return MockDatabase.ROOMS.get(id);
    }

    public void addRoom(Room room) {
        MockDatabase.ROOMS.put(room.getId(), room);
    }
    
    public void deleteRoom(String id) {
    MockDatabase.ROOMS.remove(id);
    }
}