package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Room;
import util.DBConnection;

public class RoomDAO {

    // 🔹 CHECK IF ROOM NUMBER EXISTS
    public boolean roomNumberExists(int roomNumber) {

        String sql = "SELECT room_id FROM rooms WHERE room_number = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomNumber);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 CHECK DUPLICATE FOR UPDATE
    public boolean roomNumberExistsForUpdate(int roomNumber, int roomId) {

        String sql = "SELECT room_id FROM rooms WHERE room_number = ? AND room_id != ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomNumber);
            ps.setInt(2, roomId);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 ADD ROOM
    public boolean addRoom(Room room) {

        String sql = "INSERT INTO rooms (room_number, room_type, price, status) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getPrice());
            ps.setString(4, room.getStatus());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 GET ALL ROOMS
    public List<Room> getAllRooms() {

        List<Room> roomList = new ArrayList<>();
        String sql = "SELECT * FROM rooms";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomNumber(rs.getInt("room_number"));
                room.setRoomType(rs.getString("room_type"));
                room.setPrice(rs.getDouble("price"));
                room.setStatus(rs.getString("status"));

                roomList.add(room);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return roomList;
    }

    // 🔹 DELETE ROOM
    public boolean deleteRoom(int roomId) {

        String sql = "DELETE FROM rooms WHERE room_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 UPDATE ROOM
    public boolean updateRoom(Room room) {

        String sql = "UPDATE rooms SET room_number=?, room_type=?, price=?, status=? WHERE room_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType());
            ps.setDouble(3, room.getPrice());
            ps.setString(4, room.getStatus());
            ps.setInt(5, room.getRoomId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // 🔹 GET AVAILABLE ROOMS
public List<Room> getAvailableRooms() {

    List<Room> roomList = new ArrayList<>();

    try {
        Connection con = DBConnection.getConnection();
        String sql = "SELECT * FROM rooms WHERE status='Available'";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Room room = new Room();
            room.setRoomId(rs.getInt("room_id"));
            room.setRoomNumber(rs.getInt("room_number"));
            room.setRoomType(rs.getString("room_type"));
            room.setPrice(rs.getDouble("price"));
            room.setStatus(rs.getString("status"));

            roomList.add(room);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return roomList;
}
}
