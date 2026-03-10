
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Staff;
import util.DBConnection;

public class StaffDAO {

    // ===== ADD STAFF =====
    public boolean addStaff(Staff staff) {

        String sql = "INSERT INTO staff(name, role, username, password) VALUES(?,?,?,?)";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staff.getName());
            ps.setString(2, staff.getRole());
            ps.setString(3, staff.getUsername());
            ps.setString(4, staff.getPassword());

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== LOGIN VALIDATION =====
    public boolean validateLogin(String username, String password) {

        String sql = "SELECT * FROM staff WHERE username=? AND password=?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== GET ALL STAFF =====
    public List<Staff> getAllStaff() {

        List<Staff> staffList = new ArrayList<>();

        String sql = "SELECT * FROM staff";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {

                Staff staff = new Staff();

                staff.setStaffId(rs.getInt("staff_id"));
                staff.setName(rs.getString("name"));
                staff.setRole(rs.getString("role"));
                staff.setUsername(rs.getString("username"));
                staff.setPassword(rs.getString("password"));

                staffList.add(staff);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return staffList;
    }

    // ===== UPDATE STAFF =====
    public boolean updateStaff(Staff staff) {

        String sql = "UPDATE staff SET name=?, role=?, username=?, password=? WHERE staff_id=?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, staff.getName());
            ps.setString(2, staff.getRole());
            ps.setString(3, staff.getUsername());
            ps.setString(4, staff.getPassword());
            ps.setInt(5, staff.getStaffId());

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===== DELETE STAFF =====
    public boolean deleteStaff(int staffId) {

        String sql = "DELETE FROM staff WHERE staff_id=?";

        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, staffId);

            return ps.executeUpdate() > 0;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

