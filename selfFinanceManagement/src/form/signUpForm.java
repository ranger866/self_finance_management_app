/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;
import database.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class signUpForm extends JFrame {
    private JPanel panel;
    private JLabel usernameLabel, passwordLabel, confirmPasswordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton signUpButton, loginButton;
    private Connection conn;
    private final DBconnection dbConn = new DBconnection();
    
    public signUpForm() {
        conn = dbConn.getConnection();
        
        setTitle("Sign Up");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 20, 20));

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        confirmPasswordField = new JPasswordField(1);
        signUpButton = new JButton("Sign Up");
        loginButton = new JButton("Login");
        usernameLabel = new JLabel("Username :");
        passwordLabel = new JLabel("Password :");
        confirmPasswordLabel = new JLabel("Confirm Password :");
        
        // Styling
        panel.setBackground(Color.WHITE);
        signUpButton.setBackground(new Color(0, 225, 123));
        signUpButton.setForeground(Color.WHITE);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        passwordLabel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        confirmPasswordLabel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        
        // Susunan komponen
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);
        panel.add(loginButton);
        panel.add(signUpButton);

        // Menambahkan aksi untuk proses sign up
        signUpButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            
            if (password.equals(confirmPassword)){
               if (signUp(username, password)) {
               JOptionPane.showMessageDialog(null, "Sign Up Successful");
               dispose();
               new loginForm().setVisible(true);
               } else {
               JOptionPane.showMessageDialog(null, "Error signing up.");
               }
            } else {
                JOptionPane.showMessageDialog(null, "Password tidak sesuai", "Error", 1);
            }
            
        });
        
        loginButton.addActionListener((ActionEvent e) -> {
            new loginForm().setVisible(true);
            dispose();
        });

        add(panel);
    }

    // Fungsi untuk menyimpan data pengguna baru ke database
    private boolean signUp(String username, String password) {
        try {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
