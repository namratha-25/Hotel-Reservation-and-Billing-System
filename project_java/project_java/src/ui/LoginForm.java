package ui;
import dao.StaffDAO;
import javax.swing.*;
import java.awt.*;


public class LoginForm extends JFrame {

    public LoginForm() {

        setTitle("Royal Grand Hotel - Login");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(18, 18, 18));
        add(panel);

        JLabel title = new JLabel("ROYAL GRAND HOTEL");
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(new Color(212, 175, 55));
        title.setBounds(110, 40, 350, 40);
        panel.add(title);

        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(100, 120, 100, 25);
        panel.add(userLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(200, 120, 180, 30);
        panel.add(usernameField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(100, 170, 100, 25);
        panel.add(passLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(200, 170, 180, 30);
        panel.add(passwordField);

        RoundedButton loginButton = new RoundedButton("Login");
        loginButton.setBackground(new Color(212, 175, 55));
        loginButton.setBounds(180, 230, 140, 45);
        panel.add(loginButton);

       loginButton.addActionListener(e -> {

    String username = usernameField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();

    // Admin login
    if(username.equals("admin") && password.equals("admin123")) {

        dispose();
        new Dashboard(true);   // Admin dashboard

    } else {

        StaffDAO dao = new StaffDAO();

        if(dao.validateLogin(username,password)) {

            dispose();
            new Dashboard(false);   // Staff dashboard

        } else {

            JOptionPane.showMessageDialog(this,"Invalid Username or Password");
        }
    }
});

        setVisible(true);
        
    }
}
