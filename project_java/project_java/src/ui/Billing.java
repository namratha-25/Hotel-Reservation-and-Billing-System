package ui;


import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Billing extends JFrame {

    private JComboBox<String> reservationCombo;
    private JLabel daysLabel, totalLabel, taxLabel, serviceLabel, finalLabel;

    private double roomPrice = 0;
    private long days = 0;

    public Billing() {

        setTitle("Royal Grand Hotel - Billing");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(18,18,18));
        add(mainPanel);

        JLabel title = new JLabel("BILLING MANAGEMENT");
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(new Color(212,175,55));
        title.setBounds(500,50,500,40);
        mainPanel.add(title);

        JLabel resLabel = createLabel("Select Reservation:",150);
        mainPanel.add(resLabel);

        reservationCombo = new JComboBox<>();
        reservationCombo.setBounds(600,150,300,35);
        mainPanel.add(reservationCombo);

        RoundedButton calculateBtn = createButton("Calculate Bill",220);
        mainPanel.add(calculateBtn);

        daysLabel = createResultLabel("Days: ",300);
        totalLabel = createResultLabel("Room Total: ",350);
        taxLabel = createResultLabel("Tax (18%): ",400);
        serviceLabel = createResultLabel("Service Charge (5%): ",450);
        finalLabel = createResultLabel("Final Amount: ",500);
         RoundedButton backButton = new RoundedButton("Back to Dashboard");
        backButton.setBackground(new Color(120,120,120));
        backButton.setBounds(550,650,250,45);  
        backButton.addActionListener(e -> {
            new Dashboard(true);
            dispose();
        });
        mainPanel.add(backButton);

        

        mainPanel.add(daysLabel);
        mainPanel.add(totalLabel);
        mainPanel.add(taxLabel);
        mainPanel.add(serviceLabel);
        mainPanel.add(finalLabel);

        RoundedButton saveBtn = createButton("Save Bill",580);
        mainPanel.add(saveBtn);

        loadReservations();

        calculateBtn.addActionListener(e -> calculateBill());
        saveBtn.addActionListener(e -> saveBill());

        setVisible(true);
    }

    private JLabel createLabel(String text,int y){
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setBounds(400,y,200,30);
        return label;
    }

    private RoundedButton createButton(String text,int y){
        RoundedButton btn = new RoundedButton(text);
        btn.setBackground(new Color(212,175,55));
        btn.setBounds(550,y,250,45);
        return btn;
    }

    private JLabel createResultLabel(String text,int y){
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setBounds(500,y,500,30);
        return label;
    }

    private void loadReservations(){

        reservationCombo.removeAllItems();

        try{
            Connection con = DBConnection.getConnection();
            String sql = "SELECT reservation_id FROM reservations WHERE status='Checked-Out'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                reservationCombo.addItem(String.valueOf(rs.getInt("reservation_id")));
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void calculateBill(){

        try{
            int reservationId = Integer.parseInt(
                    reservationCombo.getSelectedItem().toString());

            Connection con = DBConnection.getConnection();

            String sql = "SELECT r.check_in, r.check_out, rm.price " +
                    "FROM reservations r " +
                    "JOIN rooms rm ON r.room_id = rm.room_id " +
                    "WHERE r.reservation_id=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,reservationId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                java.sql.Date checkIn = rs.getDate("check_in");
                java.sql.Date checkOut = rs.getDate("check_out");
                roomPrice = rs.getDouble("price");

                days = (checkOut.getTime() - checkIn.getTime()) / (1000*60*60*24);

                double total = days * roomPrice;
                double tax = total * 0.18;
                double service = total * 0.05;
                double finalAmount = total + tax + service;

                daysLabel.setText("Days: " + days);
                totalLabel.setText("Room Total: ₹ " + total);
                taxLabel.setText("Tax (18%): ₹ " + tax);
                serviceLabel.setText("Service Charge (5%): ₹ " + service);
                finalLabel.setText("Final Amount: ₹ " + finalAmount);
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Select Reservation!");
        }
    }

    private void saveBill(){

        try{
            int reservationId = Integer.parseInt(
                    reservationCombo.getSelectedItem().toString());

            double total = days * roomPrice;
            double tax = total * 0.18;
            double service = total * 0.05;
            double finalAmount = total + tax + service;

            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO billing " +
                    "(reservation_id,total_amount,tax_amount,service_charges,final_amount) " +
                    "VALUES (?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1,reservationId);
            ps.setDouble(2,total);
            ps.setDouble(3,tax);
            ps.setDouble(4,service);
            ps.setDouble(5,finalAmount);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Bill Saved Successfully!");

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Calculate First!");
        }
    }
}
