/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import database.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class adminDashboard extends JFrame {
    private DefaultTableModel tableModel;
    private final Connection conn = new DBconnection().getConnection();

    public adminDashboard() {
        // Pengaturan dasar JFrame
        setTitle("Admin Dashboard");
        setSize(225, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inisialisasi tabel model dan JTable
        tableModel = new DefaultTableModel(new Object[] {"ID", "Username", "Role"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Mengambil data pengguna dari database
        loadUserData();

        // Menampilkan JFrame
        setVisible(true);
    }

    // Mengambil data pengguna dari database
    private void loadUserData() {
        String query = "SELECT id, username, role FROM users";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            // Mengisi tabel dengan data pengguna
            tableModel.setRowCount(0);
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String role = rs.getString("role");
                tableModel.addRow(new Object[] {id, username, role});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengambil data dari database.", "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }
}
