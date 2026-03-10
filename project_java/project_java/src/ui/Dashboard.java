
package ui;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard(boolean isAdmin) {

        setTitle("Royal Grand Hotel - Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(18, 18, 18));
        add(mainPanel);

        // ===== Welcome Text =====
        JLabel welcomeLabel;

        if(isAdmin){
            welcomeLabel = new JLabel("WELCOME ADMIN");
        } else {
            welcomeLabel = new JLabel("WELCOME STAFF");
        }

        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(212, 175, 55));
        welcomeLabel.setBounds(50, 30, 400, 40);
        mainPanel.add(welcomeLabel);

        // ===== Logout Button =====
        RoundedButton logoutBtn = new RoundedButton("Logout");
        logoutBtn.setBackground(new Color(212, 175, 55));
        logoutBtn.setBounds(1200, 30, 120, 40);
        mainPanel.add(logoutBtn);

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        // ===== Hotel Name =====
        JLabel hotelName = new JLabel("ROYAL GRAND HOTEL");
        hotelName.setFont(new Font("Serif", Font.BOLD, 32));
        hotelName.setForeground(Color.WHITE);
        hotelName.setBounds(550, 100, 500, 50);
        mainPanel.add(hotelName);

        // ===== Center Panel =====
        JPanel centerBox = new JPanel();
        centerBox.setBackground(new Color(30, 30, 30));
        centerBox.setBounds(550, 200, 400, 420);
        mainPanel.add(centerBox);

        RoundedButton roomsBtn = createLuxuryButton("Manage Rooms");
        RoundedButton guestsBtn = createLuxuryButton("Manage Guests");
        RoundedButton reservationBtn = createLuxuryButton("Reservations");
        RoundedButton billingBtn = createLuxuryButton("Billing");
        RoundedButton staffBtn = createLuxuryButton("Manage Staff");

        // ===== Layout depending on role =====
        if(isAdmin){

            centerBox.setLayout(new GridLayout(5,1,20,20));

            centerBox.add(roomsBtn);
            centerBox.add(guestsBtn);
            centerBox.add(reservationBtn);
            centerBox.add(billingBtn);
            centerBox.add(staffBtn);

        } else {

            centerBox.setLayout(new GridLayout(4,1,20,20));

            centerBox.add(roomsBtn);
            centerBox.add(guestsBtn);
            centerBox.add(reservationBtn);
            centerBox.add(billingBtn);
        }

        // ===== Button Actions =====

        staffBtn.addActionListener(e -> {
            try{
                new ManageStaff().setLocationRelativeTo(null);
            }catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error opening Staff Page");
            }
        });

        roomsBtn.addActionListener(e -> {
            try{
                new ManageRooms().setLocationRelativeTo(null);
            }catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error opening Rooms Page");
            }
        });

        guestsBtn.addActionListener(e -> {
            try{
                new ManageGuests().setLocationRelativeTo(null);
            }catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error opening Guests Page");
            }
        });

        reservationBtn.addActionListener(e -> {
            try{
                new ManageReservations().setLocationRelativeTo(null);
            }catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,"Error opening Reservations Page");
            }
        });

        billingBtn.addActionListener(e -> {
            try{
                new Billing().setLocationRelativeTo(null);
            }catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,"Billing Page Not Found!");
            }
        });

        setVisible(true);
    }

    // ===== Button Style =====
    private RoundedButton createLuxuryButton(String text){

        RoundedButton button = new RoundedButton(text);
        button.setBackground(new Color(212,175,55));

        button.addMouseListener(new java.awt.event.MouseAdapter(){

            public void mouseEntered(java.awt.event.MouseEvent evt){
                button.setBackground(new Color(255,215,0));
            }

            public void mouseExited(java.awt.event.MouseEvent evt){
                button.setBackground(new Color(212,175,55));
            }
        });

        return button;
    }
}

