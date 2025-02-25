/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import database.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.sql.*;

public class financeApp extends JFrame {
    private JPanel panel;
    private JLabel amountLabel, descriptionLabel, incomeSum, expenseSum;
    private JTextField amountField, descriptionField;
    private JButton addIncomeButton, addExpenseButton, viewHistoryButton;
    private JTable historyTable;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private int id;
    private Connection conn;
    private final DBconnection dbConn = new DBconnection();

    public financeApp(int userID) {
        conn = dbConn.getConnection();
        
        this.id = userID;
        setTitle("Finance Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        amountField = new JTextField(15);
        descriptionField = new JTextField(15);
        addIncomeButton = new JButton("Add Income");
        addExpenseButton = new JButton("Add Expense");
        amountLabel = new JLabel("Amount :");
        descriptionLabel = new JLabel("Description :");
        incomeSum = new JLabel("Incomes   : " + incomesTotal());
        expenseSum = new JLabel("Expenses : " + expensesTotal());

        // Styling buttons
        amountLabel.setBorder(BorderFactory.createEmptyBorder(0,25,0,0));
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        incomeSum.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        expenseSum.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        addIncomeButton.setBackground(new Color(0, 123, 255));
        addIncomeButton.setForeground(Color.WHITE);
        addExpenseButton.setBackground(Color.red);
        addExpenseButton.setForeground(Color.WHITE);

        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(descriptionLabel);
        panel.add(descriptionField);
        panel.add(addIncomeButton);
        panel.add(addExpenseButton);
        panel.add(incomeSum);
        panel.add(expenseSum);

        // JTable untuk menampilkan history transaksi
        tableModel = new DefaultTableModel(new Object[] {"ID", "Amount", "Description", "Type", "Date"}, 0);
        historyTable = new JTable(tableModel);
        scrollPane = new JScrollPane(historyTable);
        
        // Layout utama
        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        loadTransactionHistory();

        // Menambahkan aksi untuk menambah penghasilan
        addIncomeButton.addActionListener((ActionEvent e) -> {
            String amount = amountField.getText();
            String description = descriptionField.getText();
            addIncome(amount, description);
        });
        
        addExpenseButton.addActionListener((ActionEvent e) -> {
            String amount = amountField.getText();
            String description = descriptionField.getText();
            addExpense(amount, description);
        });
    }

    // Fungsi untuk menambah penghasilan ke database
    private void addIncome(String amount, String description) {
        try {
            String query = "INSERT INTO transactions (amount, description, type, user_id) VALUES (?, ?, 'income', ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, amount);
            stmt.setString(2, description);
            stmt.setInt(3, id);  // Menggunakan ID user 1 (admin) sebagai contoh
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Income Added!");
            loadTransactionHistory();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to add income.");
        }
    }
    
    private void addExpense(String amount, String description) {
        try {
            String query = "INSERT INTO transactions (amount, description, type, user_id) VALUES (?, ?, 'expense', ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, amount);
            stmt.setString(2, description);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Expense Added");
            loadTransactionHistory();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to add expense");
        }
    }

    // Fungsi untuk memuat riwayat transaksi
    private void loadTransactionHistory() {
        try {
            String query = "SELECT * FROM transactions WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id); // Menggunakan ID user 1 (admin) sebagai contoh
            ResultSet rs = stmt.executeQuery();

            // Menghapus data sebelumnya di tabel
            tableModel.setRowCount(0);

            // Menambahkan data transaksi ke tabel
            while (rs.next()) {
                int id = rs.getInt("id");
                String amount = rs.getString("amount");
                String description = rs.getString("description");
                String type = rs.getString("type");
                String date = rs.getString("date");
                tableModel.addRow(new Object[]{id, amount, description, type, date});
            }
            updateTotals();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to load transaction history.");
        }
    }
    
    private int incomesTotal() {
        int incomesTotal = 0;
        try {
            String query = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND type = 'income'";
            PreparedStatement incomeStmt = conn.prepareStatement(query);
            incomeStmt.setInt(1, id);
            ResultSet incomeRs = incomeStmt.executeQuery();
            if (incomeRs.next()) {
                incomesTotal = incomeRs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error Occured");
        }
        return incomesTotal;
    }
    
    private int expensesTotal() {
        int expensesTotal = 0;
        try {
            String query = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND type = 'expense'";
            PreparedStatement expenseStmt = conn.prepareStatement(query);
            expenseStmt.setInt(1, id);
            ResultSet expenseRs = expenseStmt.executeQuery();
            if (expenseRs.next()) {
                expensesTotal = expenseRs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error : " + e);
        }
        return expensesTotal;
    }
    
    private void updateTotals(){
        int totalIncome = incomesTotal();
        int totalExpense = expensesTotal();
        
        incomeSum.setText("Incomes :" + totalIncome);
        expenseSum.setText("Expenses :" + totalExpense);
    }
    
}
