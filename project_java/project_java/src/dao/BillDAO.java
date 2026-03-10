package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

import model.Bill;
import util.DBConnection;

public class BillDAO {

    // 🔹 Generate Bill
    public boolean generateBill(int reservationId, double serviceCharges) {

        try {
            Connection con = DBConnection.getConnection();

            // 1️⃣ Get reservation details + room price
            String sql = "SELECT r.check_in, r.check_out, rm.price "
                    + "FROM reservations r "
                    + "JOIN rooms rm ON r.room_id = rm.room_id "
                    + "WHERE r.reservation_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, reservationId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Date checkIn = rs.getDate("check_in");
                Date checkOut = rs.getDate("check_out");
                double roomPrice = rs.getDouble("price");

                // 2️⃣ Calculate number of days
                long diff = checkOut.getTime() - checkIn.getTime();
                long days = diff / (1000 * 60 * 60 * 24);

                double totalAmount = roomPrice * days;

                // 3️⃣ Tax 10%
                double taxAmount = totalAmount * 0.10;

                double finalAmount = totalAmount + taxAmount + serviceCharges;

                // 4️⃣ Insert bill
                String insertSql = "INSERT INTO bills "
                        + "(reservation_id, total_amount, tax_amount, service_charges, final_amount, bill_date) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement insertPs = con.prepareStatement(insertSql);

                insertPs.setInt(1, reservationId);
                insertPs.setDouble(2, totalAmount);
                insertPs.setDouble(3, taxAmount);
                insertPs.setDouble(4, serviceCharges);
                insertPs.setDouble(5, finalAmount);
                insertPs.setDate(6, new Date(System.currentTimeMillis()));

                int rows = insertPs.executeUpdate();

                return rows > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
