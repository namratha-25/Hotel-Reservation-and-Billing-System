package ui;

import dao.RoomDAO;
import model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageRooms extends JFrame {

    private JTextField roomNumberField;
    private JComboBox<String> roomTypeField;
    private JTextField priceField;
    private JTable table;
    private DefaultTableModel tableModel;

    public ManageRooms() {

        setTitle("Room Management - Royal Grand Hotel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 18, 18));
        add(mainPanel);

        // ===== TITLE =====
        JLabel title = new JLabel("ROOM MANAGEMENT", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(new Color(212, 175, 55));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 40, 40));
        centerPanel.setBackground(new Color(18, 18, 18));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // ================= FORM PANEL =================
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(30, 30, 30));
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 3));
        centerPanel.add(formPanel);

        JLabel roomNumberLabel = new JLabel("Room Number:");
        roomNumberLabel.setForeground(Color.WHITE);
        roomNumberLabel.setFont(new Font("Arial", Font.BOLD, 16));
        roomNumberLabel.setBounds(40, 50, 150, 25);
        formPanel.add(roomNumberLabel);

        roomNumberField = new JTextField();
        roomNumberField.setFont(new Font("Arial", Font.PLAIN, 16));
        roomNumberField.setBounds(180, 50, 200, 30);
        formPanel.add(roomNumberField);

        JLabel roomTypeLabel = new JLabel("Room Type:");
        roomTypeLabel.setForeground(Color.WHITE);
        roomTypeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        roomTypeLabel.setBounds(40, 110, 150, 25);
        formPanel.add(roomTypeLabel);

        roomTypeField = new JComboBox<>(new String[]{
                "Single",
                "Double",
                "Deluxe",
                "Suite",
                "Executive"
        });
        roomTypeField.setFont(new Font("Arial", Font.PLAIN, 16));
        roomTypeField.setBounds(180, 110, 200, 30);
        formPanel.add(roomTypeField);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setBounds(40, 170, 150, 25);
        formPanel.add(priceLabel);

        priceField = new JTextField();
        priceField.setFont(new Font("Arial", Font.PLAIN, 16));
        priceField.setBounds(180, 170, 200, 30);
        formPanel.add(priceField);

        RoundedButton addButton = new RoundedButton("Add Room");
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        addButton.setBackground(new Color(212, 175, 55));
        addButton.setBounds(110, 230, 250, 45);
        formPanel.add(addButton);

        RoundedButton updateButton = new RoundedButton("Update Room");
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateButton.setBackground(new Color(212, 175, 55));
        updateButton.setBounds(110, 290, 250, 45);
        formPanel.add(updateButton);

        RoundedButton deleteButton = new RoundedButton("Delete Room");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.setBackground(new Color(212, 175, 55));
        deleteButton.setBounds(110, 350, 250, 45);
        formPanel.add(deleteButton);

       
        RoundedButton backButton = new RoundedButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(120, 120, 120));
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(110, 410, 250, 45);
        formPanel.add(backButton);

        // ================= TABLE PANEL =================
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(30, 30, 30));
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 3));
        centerPanel.add(tablePanel);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
                "ID", "Room Number", "Room Type", "Price", "Status"
        });

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setBackground(new Color(40, 40, 40));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(212, 175, 55));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // 🔥 AUTO-FILL WHEN ROW SELECTED
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {

                    roomNumberField.setText(
                            tableModel.getValueAt(selectedRow, 1).toString()
                    );

                    roomTypeField.setSelectedItem(
                            tableModel.getValueAt(selectedRow, 2).toString()
                    );

                    priceField.setText(
                            tableModel.getValueAt(selectedRow, 3).toString()
                    );
                }
            }
        });

        // Load Data
        loadRoomData();

        // Button Actions
        addButton.addActionListener(e -> addRoom());
        updateButton.addActionListener(e -> updateRoom());
        deleteButton.addActionListener(e -> deleteRoom());

        // 🔥 BACK BUTTON ACTION
        backButton.addActionListener(e -> {
            new Dashboard(true);
            dispose();
        });

        setVisible(true);
    }

    private void addRoom() {

    try {
        int roomNumber = Integer.parseInt(roomNumberField.getText());
        double price = Double.parseDouble(priceField.getText());

        if (roomNumber <= 0 || price <= 0) {
            JOptionPane.showMessageDialog(this, "Room number and price must be positive!");
            return;
        }

        RoomDAO dao = new RoomDAO();

        // 🔥 Duplicate Check
        if (dao.roomNumberExists(roomNumber)) {
            JOptionPane.showMessageDialog(this, "Room number already exists!");
            return;
        }

        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomTypeField.getSelectedItem().toString());
        room.setPrice(price);
        room.setStatus("Available");

        if (dao.addRoom(room)) {
            JOptionPane.showMessageDialog(this, "Room Added Successfully!");
            loadRoomData();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to Add Room!");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Invalid Input!");
    }
}

    
   private void updateRoom() {

    int selectedRow = table.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Select a room to update!");
        return;
    }

    try {
        int roomId = (int) tableModel.getValueAt(selectedRow, 0);
        int roomNumber = Integer.parseInt(roomNumberField.getText());
        double price = Double.parseDouble(priceField.getText());

        RoomDAO dao = new RoomDAO();

        // 🔥 Duplicate Check for Update
        if (dao.roomNumberExistsForUpdate(roomNumber, roomId)) {
            JOptionPane.showMessageDialog(this, "Room number already exists!");
            return;
        }

        Room room = new Room();
        room.setRoomId(roomId);
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomTypeField.getSelectedItem().toString());
        room.setPrice(price);
        room.setStatus("Available");

        if (dao.updateRoom(room)) {
            JOptionPane.showMessageDialog(this, "Room Updated Successfully!");
            loadRoomData();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to Update Room!");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Invalid Input!");
    }
}
private void deleteRoom() {

    int selectedRow = table.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Select a room to delete!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this room?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) return;

    int roomId = (int) tableModel.getValueAt(selectedRow, 0);

    RoomDAO dao = new RoomDAO();

    if (dao.deleteRoom(roomId)) {
        JOptionPane.showMessageDialog(this, "Room Deleted Successfully!");
        loadRoomData();
        clearFields();
    } else {
        JOptionPane.showMessageDialog(this, "Failed to Delete Room!");
    }
}

    private void loadRoomData() {

        tableModel.setRowCount(0);

        RoomDAO dao = new RoomDAO();
        List<Room> rooms = dao.getAllRooms();

        for (Room room : rooms) {
            tableModel.addRow(new Object[]{
                    room.getRoomId(),
                    room.getRoomNumber(),
                    room.getRoomType(),
                    room.getPrice(),
                    room.getStatus()
            });
        }
    }

    private void clearFields() {
        roomNumberField.setText("");
        roomTypeField.setSelectedIndex(0);
        priceField.setText("");
    }
}
