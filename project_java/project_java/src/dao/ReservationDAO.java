package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

import model.Reservation;
import util.DBConnection;

public class ReservationDAO {

    // ===============================
    // 🔹 CHECK ROOM AVAILABILITY
    // ===============================
    public boolean isRoomAvailable(int roomId, Date checkIn, Date checkOut) {

        String sql = "SELECT 1 FROM reservations "
                + "WHERE room_id = ? "
                + "AND status != 'Checked-Out' "
                + "AND (check_in < ? AND check_out > ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ps.setDate(2, checkOut);
            ps.setDate(3, checkIn);

            try (ResultSet rs = ps.executeQuery()) {
                return !rs.next();   // true = available
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===============================
    // 🔹 CREATE RESERVATION
    // ===============================
    public boolean createReservation(Reservation reservation) {

        String sql = "INSERT INTO reservations "
                + "(guest_id, room_id, check_in, check_out, status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservation.getGuestId());
            ps.setInt(2, reservation.getRoomId());
            ps.setDate(3, reservation.getCheckIn());
            ps.setDate(4, reservation.getCheckOut());
            ps.setString(5, reservation.getStatus());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===============================
    // 🔹 CHECK-IN
    // ===============================
    public boolean checkIn(int reservationId) {
        return updateReservationStatus(reservationId, "Checked-In", "Occupied");
    }

    // ===============================
    // 🔹 CHECK-OUT
    // ===============================
    public boolean checkOut(int reservationId) {
        return updateReservationStatus(reservationId, "Checked-Out", "Available");
    }

    // ===============================
    // 🔹 COMMON TRANSACTION METHOD
    // ===============================
    private boolean updateReservationStatus(int reservationId,
                                            String reservationStatus,
                                            String roomStatus) {

        String updateReservation =
                "UPDATE reservations SET status=? WHERE reservation_id=?";

        String updateRoom =
                "UPDATE rooms SET status=? "
                        + "WHERE room_id = (SELECT room_id FROM reservations WHERE reservation_id=?)";

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(updateReservation);
                 PreparedStatement ps2 = con.prepareStatement(updateRoom)) {

                // Update reservation
                ps1.setString(1, reservationStatus);
                ps1.setInt(2, reservationId);
                int rows1 = ps1.executeUpdate();

                // Update room
                ps2.setString(1, roomStatus);
                ps2.setInt(2, reservationId);
                int rows2 = ps2.executeUpdate();

                if (rows1 == 0 || rows2 == 0) {
                    con.rollback();
                    return false;
                }

                con.commit();
                return true;
            }

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}