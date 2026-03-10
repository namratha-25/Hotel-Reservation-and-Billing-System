
package ui;

import dao.StaffDAO;
import model.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageStaff extends JFrame {

    private JTextField nameField, roleField, usernameField, passwordField;
    private JTable table;
    private DefaultTableModel model;

    public ManageStaff() {

        setTitle("Staff Management - Royal Grand Hotel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18,18,18));
        add(mainPanel);

        JLabel title = new JLabel("STAFF MANAGEMENT",SwingConstants.CENTER);
        title.setFont(new Font("Serif",Font.BOLD,32));
        title.setForeground(new Color(212,175,55));
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        mainPanel.add(title,BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1,2,40,40));
        centerPanel.setBackground(new Color(18,18,18));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20,50,50,50));
        mainPanel.add(centerPanel,BorderLayout.CENTER);

        // ===== LEFT FORM =====
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(new Color(30,30,30));
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(212,175,55),3));
        centerPanel.add(formPanel);

        JLabel nameLabel = createLabel("Name:",60);
        formPanel.add(nameLabel);

        nameField = createTextField(60);
        formPanel.add(nameField);

        JLabel roleLabel = createLabel("Role:",130);
        formPanel.add(roleLabel);

        roleField = createTextField(130);
        formPanel.add(roleField);

        JLabel userLabel = createLabel("Username:",200);
        formPanel.add(userLabel);

        usernameField = createTextField(200);
        formPanel.add(usernameField);

        JLabel passLabel = createLabel("Password:",270);
        formPanel.add(passLabel);

        passwordField = createTextField(270);
        formPanel.add(passwordField);

        RoundedButton addBtn = createButton("Add Staff",340);
        RoundedButton updateBtn = createButton("Update Staff",410);
        RoundedButton deleteBtn = createButton("Delete Staff",480);

        formPanel.add(addBtn);
        formPanel.add(updateBtn);
        formPanel.add(deleteBtn);

        // ===== RIGHT TABLE =====
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(30,30,30));
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(212,175,55),3));
        centerPanel.add(tablePanel);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "ID","Name","Role","Username","Password"
        });

        table = new JTable(model);
        table.setBackground(new Color(40,40,40));
        table.setForeground(Color.WHITE);
        table.setRowHeight(25);

        tablePanel.add(new JScrollPane(table),BorderLayout.CENTER);

        loadStaffData();

        addBtn.addActionListener(e -> addStaff());
        updateBtn.addActionListener(e -> updateStaff());
        deleteBtn.addActionListener(e -> deleteStaff());

        table.getSelectionModel().addListSelectionListener(e -> fillFields());

        setVisible(true);
    }

    // ===== CRUD METHODS =====

    private void addStaff(){

        Staff staff = new Staff();
        staff.setName(nameField.getText());
        staff.setRole(roleField.getText());
        staff.setUsername(usernameField.getText());
        staff.setPassword(passwordField.getText());

        StaffDAO dao = new StaffDAO();

        if(dao.addStaff(staff)){
            JOptionPane.showMessageDialog(this,"Staff Added Successfully");
            loadStaffData();
            clearFields();
        }else{
            JOptionPane.showMessageDialog(this,"Failed to Add Staff");
        }
    }

    private void updateStaff(){

        int row = table.getSelectedRow();

        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select staff to update");
            return;
        }

        int id = (int)model.getValueAt(row,0);

        Staff staff = new Staff();
        staff.setStaffId(id);
        staff.setName(nameField.getText());
        staff.setRole(roleField.getText());
        staff.setUsername(usernameField.getText());
        staff.setPassword(passwordField.getText());

        StaffDAO dao = new StaffDAO();

        if(dao.updateStaff(staff)){
            JOptionPane.showMessageDialog(this,"Staff Updated Successfully");
            loadStaffData();
            clearFields();
        }else{
            JOptionPane.showMessageDialog(this,"Failed to Update Staff");
        }
    }

    private void deleteStaff(){

        int row = table.getSelectedRow();

        if(row == -1){
            JOptionPane.showMessageDialog(this,"Select staff to delete");
            return;
        }

        int id = (int)model.getValueAt(row,0);

        StaffDAO dao = new StaffDAO();

        if(dao.deleteStaff(id)){
            JOptionPane.showMessageDialog(this,"Staff Deleted Successfully");
            loadStaffData();
            clearFields();
        }else{
            JOptionPane.showMessageDialog(this,"Failed to Delete Staff");
        }
    }

    // ===== LOAD TABLE DATA =====

    private void loadStaffData(){

        model.setRowCount(0);

        StaffDAO dao = new StaffDAO();
        List<Staff> list = dao.getAllStaff();

        for(Staff s : list){

            model.addRow(new Object[]{
                    s.getStaffId(),
                    s.getName(),
                    s.getRole(),
                    s.getUsername(),
                    s.getPassword()
            });
        }
    }

    // ===== HELPER METHODS =====

    private void fillFields(){

        int row = table.getSelectedRow();

        if(row != -1){

            nameField.setText(model.getValueAt(row,1).toString());
            roleField.setText(model.getValueAt(row,2).toString());
            usernameField.setText(model.getValueAt(row,3).toString());
            passwordField.setText(model.getValueAt(row,4).toString());
        }
    }

    private void clearFields(){

        nameField.setText("");
        roleField.setText("");
        usernameField.setText("");
        passwordField.setText("");
    }

    private JLabel createLabel(String text,int y){

        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setBounds(60,y,120,25);
        return label;
    }

    private JTextField createTextField(int y){

        JTextField field = new JTextField();
        field.setBounds(200,y,220,30);
        return field;
    }

    private RoundedButton createButton(String text,int y){

        RoundedButton btn = new RoundedButton(text);
        btn.setBackground(new Color(212,175,55));
        btn.setBounds(150,y,200,45);
        return btn;
    }
}

