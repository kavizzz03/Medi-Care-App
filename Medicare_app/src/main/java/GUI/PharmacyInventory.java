package GUI;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import Db.*;

public class PharmacyInventory extends JFrame {
    private JTextField txtItemName, txtQuantity, txtPrice;
    private JButton btnAdd, btnUpdate, btnDelete, btnView, btnBack;
    private JTable tblInventory;
    private DefaultTableModel tableModel;

    public PharmacyInventory() {
        setTitle("Pharmacy Inventory Management");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Title Label
        JLabel lblTitle = new JLabel("Pharmacy Inventory Management", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));
        add(lblTitle, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        inputPanel.add(new JLabel("Item Name:"));
        txtItemName = new JTextField();
        inputPanel.add(txtItemName);

        inputPanel.add(new JLabel("Quantity:"));
        txtQuantity = new JTextField();
        inputPanel.add(txtQuantity);

        inputPanel.add(new JLabel("Price:"));
        txtPrice = new JTextField();
        inputPanel.add(txtPrice);

        add(inputPanel, BorderLayout.WEST);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        btnAdd = createStyledButton("Add", new Color(52, 152, 219));
        btnUpdate = createStyledButton("Update", new Color(46, 204, 113));
        btnDelete = createStyledButton("Delete", new Color(231, 76, 60));
        btnView = createStyledButton("View Inventory", new Color(241, 196, 15));
        btnBack = createStyledButton("Back to Home", new Color(127, 140, 141));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.SOUTH);

        // Table Panel
        String[] columnNames = {"Item ID", "Item Name", "Quantity", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblInventory = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblInventory);
        add(scrollPane, BorderLayout.CENTER);

        // Button Actions
        btnAdd.addActionListener(e -> addItem());
        btnUpdate.addActionListener(e -> updateItem());
        btnDelete.addActionListener(e -> deleteItem());
        btnView.addActionListener(e -> loadInventory());
        btnBack.addActionListener(e -> {
            dispose();
            new HomePageGUI();  // Redirects to Home Page (Replace with your actual home class)
        });

        setVisible(true);
    }

    // Styled Button Creation
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    // Add Item to Inventory
    private void addItem() {
        String itemName = txtItemName.getText();
        String quantityText = txtQuantity.getText();
        String priceText = txtPrice.getText();

        // Validate quantity input
        if (quantityText.isEmpty() || !quantityText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity (numeric value).");
            return;
        }

        // Validate price input
        if (priceText.isEmpty() || !priceText.matches("\\d+(\\.\\d{1,2})?")) { // Allows decimals with up to 2 digits
            JOptionPane.showMessageDialog(this, "Please enter a valid price (numeric value).");
            return;
        }

        int quantity = Integer.parseInt(quantityText);
        double price = Double.parseDouble(priceText);

        String query = "INSERT INTO pharmacy_inventory (item_name, quantity, price) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item added successfully!");
            clearFields();
            loadInventory();  // Refresh inventory
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding item.");
        }
    }

    // Update Item in Inventory
    private void updateItem() {
        int selectedRow = tblInventory.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to update.");
            return;
        }

        int itemId = (int) tableModel.getValueAt(selectedRow, 0); // Item ID
        String itemName = txtItemName.getText();
        String quantityText = txtQuantity.getText();
        String priceText = txtPrice.getText();

        // Validate quantity input
        if (quantityText.isEmpty() || !quantityText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity (numeric value).");
            return;
        }

        // Validate price input
        if (priceText.isEmpty() || !priceText.matches("\\d+(\\.\\d{1,2})?")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price (numeric value).");
            return;
        }

        int quantity = Integer.parseInt(quantityText);
        double price = Double.parseDouble(priceText);

        String query = "UPDATE pharmacy_inventory SET item_name = ?, quantity = ?, price = ? WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.setInt(4, itemId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item updated successfully!");
            clearFields();
            loadInventory();  // Refresh inventory
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating item.");
        }
    }

    // Delete Item from Inventory
    private void deleteItem() {
        int selectedRow = tblInventory.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.");
            return;
        }

        int itemId = (int) tableModel.getValueAt(selectedRow, 0); // Item ID
        String query = "DELETE FROM pharmacy_inventory WHERE item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item deleted successfully!");
            loadInventory();  // Refresh inventory
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting item.");
        }
    }

    // Load Inventory
    private void loadInventory() {
        String query = "SELECT * FROM pharmacy_inventory";
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");

                tableModel.addRow(new Object[]{
                        itemId,
                        itemName,
                        quantity,
                        price
                });

                // Check if quantity is below 5 and send email notification
                if (quantity < 5) {
                    sendLowStockEmail(itemName, quantity);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading inventory.");
        }
    }

    // Send Low Stock Email
    private void sendLowStockEmail(String itemName, int quantity) {
        String to = "kavindumalshan2003@gmail.com"; // Replace with recipient's email
        String subject = "Low Stock Alert: " + itemName;
        String messageBody = "The stock for " + itemName + " is running low. Only " + quantity + " items are left.";

        sendEmail(to, subject, messageBody);
    }

    // Send Email Method
    private void sendEmail(String to, String subject, String messageBody) {
        String from = "medicareplus2025@gmail.com";  // Replace with your email
        String password = "lnie mmlv osmd xvrk";  // Replace with your App Password (if using 2-Step Verification)

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(messageBody);
            System.out.println("Welcome email sent successfully!");

            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error sending email notification.");
        }
    }

    // Clear Input Fields
    private void clearFields() {
        txtItemName.setText("");
        txtQuantity.setText("");
        txtPrice.setText("");
    }
}
