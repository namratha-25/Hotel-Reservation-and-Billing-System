package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Guest;
import util.DBConnection;

public class GuestDAO {

    // 🔹 ADD GUEST
    public boolean addGuest(Guest guest) {

        String sql = "INSERT INTO guests (first_name, last_name, phone, email, address, id_proof) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, guest.getFirstName());
            ps.setString(2, guest.getLastName());
            ps.setString(3, guest.getPhone());
            ps.setString(4, guest.getEmail());
            ps.setString(5, guest.getAddress());
            ps.setString(6, guest.getIdProof());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 GET ALL GUESTS
    public List<Guest> getAllGuests() {

        List<Guest> guestList = new ArrayList<>();
        String sql = "SELECT * FROM guests";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Guest guest = new Guest();
                guest.setGuestId(rs.getInt("guest_id"));
                guest.setFirstName(rs.getString("first_name"));
                guest.setLastName(rs.getString("last_name"));
                guest.setPhone(rs.getString("phone"));
                guest.setEmail(rs.getString("email"));
                guest.setAddress(rs.getString("address"));
                guest.setIdProof(rs.getString("id_proof"));

                guestList.add(guest);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return guestList;
    }

    // 🔹 UPDATE GUEST
    public boolean updateGuest(Guest guest) {

        String sql = "UPDATE guests SET first_name=?, last_name=?, phone=?, email=?, address=?, id_proof=? WHERE guest_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, guest.getFirstName());
            ps.setString(2, guest.getLastName());
            ps.setString(3, guest.getPhone());
            ps.setString(4, guest.getEmail());
            ps.setString(5, guest.getAddress());
            ps.setString(6, guest.getIdProof());
            ps.setInt(7, guest.getGuestId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 DELETE GUEST
    public boolean deleteGuest(int guestId) {

        String sql = "DELETE FROM guests WHERE guest_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, guestId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}