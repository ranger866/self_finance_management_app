/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;
import java.sql.*;


public class DBconnection {
    private Connection conn;
    private String url = "jdbc:mysql://localhost:3306/selffinancemanagement";
    private String user = "root";
    private String password = "";
    
    public DBconnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Koneksi ke DataBase berhasil!!");
        } catch(ClassNotFoundException e) {
            System.err.println("Driver JDBC tidak ditemukan");
        } catch(SQLException e) {
            System.err.println("Gagal terhubung ke DataBase!!");
        }
    }
    
    public Connection getConnection() {
        return conn;
    }
    
    public void openConnection(){
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Koneksi berhasil dibuka");
            } catch(SQLException e) {
                System.err.println("Gagal membuka koneksi ke database");
            } 
        }
    }
    
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close(); // Menutup koneksi
                System.out.println("Koneksi ditutup.");
            } catch (SQLException e) {
                System.err.println("Gagal menutup koneksi ke database!");
            }
        }
    }
}