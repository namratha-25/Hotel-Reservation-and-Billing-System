package ui;
import com.toedter.calendar.JDateChooser;
import dao.GuestDAO;
import dao.RoomDAO;
import dao.ReservationDAO;
import model.Guest;
import model.Room;
import model.Reservation;
import util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class ManageReservations extends JFrame {

    private JComboBox<String> guestCombo;
    private JComboBox<String> roomCombo;
    private JDateChooser checkInDate;
    private JDateChooser checkOutDate;

    private JTable table;
    private DefaultTableModel model;

    public ManageReservations() {

        setTitle("Royal Grand Hotel - Reservations");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 18, 18));
        add(mainPanel);

        JLabel title = new JLabel("RESERVATION MANAGEMENT", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(new Color(212, 175, 55));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 40, 40));
        centerPanel.setBackground(new Color(18, 18, 18));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // ================= LEFT PANEL =================
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(new Color(30, 30, 30));
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 3));
        centerPanel.add(formPanel);

        JLabel guestLabel = createLabel("Select Guest:", 60);
        formPanel.add(guestLabel);

        guestCombo = new JComboBox<>();
        guestCombo.setBounds(220, 60, 250, 30);
        formPanel.add(guestCombo);

        JLabel roomLabel = createLabel("Select Room:", 130);
        formPanel.add(roomLabel);

        roomCombo = new JComboBox<>();
        roomCombo.setBounds(220, 130, 250, 30);
        formPanel.add(roomCombo);

        JLabel checkInLabel = createLabel("Check-In (YYYY-MM-DD):", 200);
        formPanel.add(checkInLabel);

        checkInDate = new JDateChooser();
        checkInDate.setBounds(220, 200, 250, 30);
        checkInDate.setDateFormatString("yyyy-MM-dd");
        formPanel.add(checkInDate);

        JLabel checkOutLabel = createLabel("Check-Out (YYYY-MM-DD):", 270);
        formPanel.add(checkOutLabel);

        checkOutDate = new JDateChooser();
        checkOutDate.setBounds(220, 270, 250, 30);
        checkOutDate.setDateFormatString("yyyy-MM-dd");
        formPanel.add(checkOutDate);

        RoundedButton bookBtn = createButton("Book Room", 340);
        RoundedButton checkInBtn = createButton("Check-In", 400);
        RoundedButton checkOutBtn = createButton("Check-Out", 460);
        RoundedButton backButton = new RoundedButton("Back to Dashboard");
        backButton.setBackground(new Color(150, 150, 150));
        backButton.setBounds(180, 520, 200, 45); 
        formPanel.add(backButton);
        backButton.addActionListener(e -> {
            new Dashboard(true);
            dispose();
        });


        formPanel.add(bookBtn);
        formPanel.add(checkInBtn);
        formPanel.add(checkOutBtn);

        // ================= RIGHT PANEL =================
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(30, 30, 30));
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 3));
        centerPanel.add(tablePanel);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "Reservation ID", "Guest Name", "Room Number",
                "Check-In", "Check-Out", "Status"
        });

        table = new JTable(model);
        table.setBackground(new Color(40, 40, 40));
        table.setForeground(Color.WHITE);
        table.setRowHeight(25);

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        loadGuests();
        loadAvailableRooms();
        loadReservations();

        bookBtn.addActionListener(e -> bookRoom());
        checkInBtn.addActionListener(e -> checkIn());
        checkOutBtn.addActionListener(e -> checkOut());

        setVisible(true);
    }

    private JLabel createLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setBounds(60, y, 200, 25);
        return label;
    }

    private RoundedButton createButton(String text, int y) {
        RoundedButton btn = new RoundedButton(text);
        btn.setBackground(new Color(212, 175, 55));
        btn.setBounds(180, y, 200, 45);
        return btn;
    }

    private void loadGuests() {
        guestCombo.removeAllItems();
        GuestDAO dao = new GuestDAO();
        List<Guest> list = dao.getAllGuests();

        for (Guest g : list) {
            guestCombo.addItem(g.getGuestId() + " - " + g.getFirstName());
        }
    }

    private void loadAvailableRooms() {
        roomCombo.removeAllItems();
        RoomDAO dao = new RoomDAO();
        List<Room> list = dao.getAvailableRooms();

        for (Room r : list) {
            roomCombo.addItem(r.getRoomId() + " - Room " + r.getRoomNumber());
        }
    }

    private void loadReservations() {
        model.setRowCount(0);

        String sql = "SELECT r.reservation_id, g.first_name, rm.room_number, "
                + "r.check_in, r.check_out, r.status "
                + "FROM reservations r "
                + "JOIN guests g ON r.guest_id = g.guest_id "
                + "JOIN rooms rm ON r.room_id = rm.room_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("reservation_id"),
                        rs.getString("first_name"),
                        rs.getInt("room_number"),
                        rs.getDate("check_in"),
                        rs.getDate("check_out"),
                        rs.getString("status")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bookRoom() {
        try {

            if (guestCombo.getSelectedItem() == null || roomCombo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Select Guest and Room!");
                return;
            }

            int guestId = Integer.parseInt(
                    guestCombo.getSelectedItem().toString().split(" - ")[0]);

            int roomId = Integer.parseInt(
                    roomCombo.getSelectedItem().toString().split(" - ")[0]);

        java.util.Date checkInUtil = checkInDate.getDate();
        java.util.Date checkOutUtil = checkOutDate.getDate();

            Date checkIn = new Date(checkInUtil.getTime());
            Date checkOut = new Date(checkOutUtil.getTime());

            if (checkOut.before(checkIn)) {
                JOptionPane.showMessageDialog(this, "Check-Out must be after Check-In!");
                return;
            }

            if (checkIn.toLocalDate().isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Check-In cannot be in the past!");
                return;
            }

            ReservationDAO dao = new ReservationDAO();

            if (!dao.isRoomAvailable(roomId, checkIn, checkOut)) {
                JOptionPane.showMessageDialog(this, "Room Not Available!");
                return;
            }

            Reservation res = new Reservation();
            res.setGuestId(guestId);
            res.setRoomId(roomId);
            res.setCheckIn(checkIn);
            res.setCheckOut(checkOut);
            res.setStatus("Booked");

            if (dao.createReservation(res)) {
                JOptionPane.showMessageDialog(this, "Reservation Created!");
                loadReservations();
                loadAvailableRooms();
                clearFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Date Format! Use YYYY-MM-DD");
        }
    }

    private void checkIn() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a reservation first!");
            return;
        }

        int reservationId = (int) model.getValueAt(row, 0);

        if (new ReservationDAO().checkIn(reservationId)) {
            JOptionPane.showMessageDialog(this, "Checked-In Successfully!");
            loadReservations();
            loadAvailableRooms();
        }
    }

    private void checkOut() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a reservation first!");
            return;
        }

        int reservationId = (int) model.getValueAt(row, 0);

        if (new ReservationDAO().checkOut(reservationId)) {
            JOptionPane.showMessageDialog(this, "Checked-Out Successfully!");
            loadReservations();
            loadAvailableRooms();
        }
    }

private void clearFields() {
    checkInDate.setDate(null);
    checkOutDate.setDate(null);
}
}