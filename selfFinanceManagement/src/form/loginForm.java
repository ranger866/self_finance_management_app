/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;
import database.*;
import main.*;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class loginForm extends JFrame {
    private JPanel panel;
    private JLabel usernameLabel, passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signUpButton;
    private Connection conn;
    private final DBconnection dbConn = new DBconnection();

    public loginForm() {
        conn = dbConn.getConnection();
        
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel untuk tampilan
        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 20, 20));
        
        // Instansiasi komponen
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");
        usernameLabel = new JLabel("Username :");
        passwordLabel = new JLabel("Password :");

        // Styling  
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        signUpButton.setBackground(new Color(0, 225, 123));
        signUpButton.setForeground(Color.WHITE);
        panel.setBackground(Color.WHITE);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(0,25,0,0));
        passwordLabel.setBorder(BorderFactory.createEmptyBorder(0,25,0,0));

        // Susunan tampilan komponen
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(signUpButton);
        panel.add(loginButton);

        // Menambahkan aksi login
        loginButton.addActionListener((ActionEvent e) -> {
            loginUser();
        });
        
        signUpButton.addActionListener((ActionEvent e) -> {
            new signUpForm().setVisible(true);
            dispose();
        });

        add(panel);
    }
    
    private void loginUser(){
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
                
        try {
            String query = "SELECT id, role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String role = rs.getString("role");
                
                if ("admin".equals(role)) {
                    new adminDashboard().setVisible(true);
                    dispose();
                } else {
                new financeApp(userId).setVisible(true); // Pass userId to FinanceApp
                dispose(); // Close login form
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
            }
        } catch (SQLException e) {
            System.err.println("Error Occured");
        }
    }

    // Fungsi untuk autentikasi user dari database
    private boolean authenticate(String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}
