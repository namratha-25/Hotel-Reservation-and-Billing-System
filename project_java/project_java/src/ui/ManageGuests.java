package ui;

import dao.GuestDAO;
import model.Guest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class ManageGuests extends JFrame {

    private JTextField firstNameField, lastNameField, phoneField, emailField, addressField;
    private JTable table;
    private DefaultTableModel model;

    public ManageGuests() {

        setTitle("Guest Management - Royal Grand Hotel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 18, 18));
        add(mainPanel);

        // ===== TITLE =====
        JLabel title = new JLabel("GUEST MANAGEMENT", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(new Color(212, 175, 55));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 40, 40));
        centerPanel.setBackground(new Color(18, 18, 18));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // ================= LEFT PANEL (FORM) =================
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(30, 30, 30));
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 3));
        centerPanel.add(formPanel);

        JLabel firstLabel = createLabel("First Name:", 50);
        formPanel.add(firstLabel);
        firstNameField = createTextField(50);
        formPanel.add(firstNameField);

        JLabel lastLabel = createLabel("Last Name:", 110);
        formPanel.add(lastLabel);
        lastNameField = createTextField(110);
        formPanel.add(lastNameField);

        JLabel phoneLabel = createLabel("Phone:", 170);
        formPanel.add(phoneLabel);
        phoneField = createTextField(170);
        formPanel.add(phoneField);

        JLabel emailLabel = createLabel("Email:", 230);
        formPanel.add(emailLabel);
        emailField = createTextField(230);
        formPanel.add(emailField);

        JLabel addressLabel = createLabel("Address:", 290);
        formPanel.add(addressLabel);
        addressField = createTextField(290);
        formPanel.add(addressField);

        RoundedButton addBtn = createButton("Add Guest", 360);
        formPanel.add(addBtn);

        RoundedButton updateBtn = createButton("Update Guest", 430);
        formPanel.add(updateBtn);

        RoundedButton deleteBtn = createButton("Delete Guest", 500);
        formPanel.add(deleteBtn);

        RoundedButton backButton = new RoundedButton("Back to Dashboard");
        backButton.setBackground(new Color(120, 120, 120));
        backButton.setBounds(150, 570, 200, 45);
        formPanel.add(backButton);

        // ================= RIGHT PANEL (TABLE) =================
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(30, 30, 30));
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 3));
        centerPanel.add(tablePanel);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "ID", "First Name", "Last Name", "Phone", "Email", "Address"
        });

        table = new JTable(model);
        table.setBackground(new Color(40, 40, 40));
        table.setForeground(Color.WHITE);
        table.setRowHeight(25);
        table.setGridColor(new Color(212, 175, 55));

        JScrollPane scroll = new JScrollPane(table);
        tablePanel.add(scroll, BorderLayout.CENTER);

        loadGuestData();

        // Button Actions
        addBtn.addActionListener(e -> addGuest());
        updateBtn.addActionListener(e -> updateGuest());
        deleteBtn.addActionListener(e -> deleteGuest());

        backButton.addActionListener(e -> {
            new Dashboard(true);
            dispose();
        });

        table.getSelectionModel().addListSelectionListener(e -> fillFields());

        setVisible(true);
    }

    // ================= VALIDATION METHOD =================

    private boolean validateFields() {

        String firstName = firstNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (firstName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name is required!");
            return false;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone must be 10 digits!");
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailRegex, email)) {
            JOptionPane.showMessageDialog(this, "Invalid Email format!");
            return false;
        }

        return true;
    }

    // ================= CRUD METHODS =================

    private void addGuest() {

        if (!validateFields()) return;

        Guest guest = new Guest();
        guest.setFirstName(firstNameField.getText().trim());
        guest.setLastName(lastNameField.getText().trim());
        guest.setPhone(phoneField.getText().trim());
        guest.setEmail(emailField.getText().trim());
        guest.setAddress(addressField.getText().trim());
        guest.setIdProof("N/A");

        GuestDAO dao = new GuestDAO();

        if (dao.addGuest(guest)) {
            JOptionPane.showMessageDialog(this, "Guest Added Successfully!");
            loadGuestData();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to Add Guest!");
        }
    }

    private void updateGuest() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a guest to update!");
            return;
        }

        if (!validateFields()) return;

        int id = (int) model.getValueAt(row, 0);

        Guest guest = new Guest();
        guest.setGuestId(id);
        guest.setFirstName(firstNameField.getText().trim());
        guest.setLastName(lastNameField.getText().trim());
        guest.setPhone(phoneField.getText().trim());
        guest.setEmail(emailField.getText().trim());
        guest.setAddress(addressField.getText().trim());
        guest.setIdProof("N/A");

        GuestDAO dao = new GuestDAO();

        if (dao.updateGuest(guest)) {
            JOptionPane.showMessageDialog(this, "Guest Updated Successfully!");
            loadGuestData();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to Update Guest!");
        }
    }

    private void deleteGuest() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a guest to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this guest?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) model.getValueAt(row, 0);

        GuestDAO dao = new GuestDAO();

        if (dao.deleteGuest(id)) {
            JOptionPane.showMessageDialog(this, "Guest Deleted Successfully!");
            loadGuestData();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to Delete Guest!");
        }
    }

    // ================= HELPER METHODS =================

    private void loadGuestData() {
      
    model.setRowCount(0);

    GuestDAO dao = new GuestDAO();
    List<Guest> list = dao.getAllGuests();

    System.out.println("Guests fetched: " + list.size());

    for (Guest g : list) {

        System.out.println("Guest: " + g.getFirstName());

        model.addRow(new Object[]{
                g.getGuestId(),
                g.getFirstName(),
                g.getLastName(),
                g.getPhone(),
                g.getEmail(),
                g.getAddress()
        });
    }
}
    

    private void fillFields() {
        int row = table.getSelectedRow();
        if (row != -1) {
            firstNameField.setText(model.getValueAt(row, 1).toString());
            lastNameField.setText(model.getValueAt(row, 2).toString());
            phoneField.setText(model.getValueAt(row, 3).toString());
            emailField.setText(model.getValueAt(row, 4).toString());
            addressField.setText(model.getValueAt(row, 5).toString());
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
    }

    private JLabel createLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setBounds(60, y, 120, 25);
        return label;
    }

    private JTextField createTextField(int y) {
        JTextField field = new JTextField();
        field.setBounds(200, y, 220, 30);
        return field;
    }

    private RoundedButton createButton(String text, int y) {
        RoundedButton btn = new RoundedButton(text);
        btn.setBackground(new Color(212, 175, 55));
        btn.setBounds(150, y, 200, 45);
        return btn;
    }
}