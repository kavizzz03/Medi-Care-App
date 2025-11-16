package GUI;

import Db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportGenerationGUI extends JFrame {
    private JButton btnPatientDetails, btnDoctorDetails, btnAppointmentDetails, btnInventoryDetails, btnBack;
    private JPanel tablePanel;

    public ReportGenerationGUI() {
        setTitle("Report Generation");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Title Label
        JLabel lblTitle = new JLabel("Report Generation", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPatientDetails = createStyledButton("View Patient Details", new Color(52, 152, 219));
        btnDoctorDetails = createStyledButton("View Doctor Details", new Color(46, 204, 113));
        btnAppointmentDetails = createStyledButton("View Appointment Details", new Color(241, 196, 15));
        btnInventoryDetails = createStyledButton("View Inventory Details", new Color(231, 76, 60));
        btnBack = createStyledButton("Back to Home", new Color(127, 140, 141));

        buttonPanel.add(btnPatientDetails);
        buttonPanel.add(btnDoctorDetails);
        buttonPanel.add(btnAppointmentDetails);
        buttonPanel.add(btnInventoryDetails);
        buttonPanel.add(btnBack);

        // Table Panel
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Button Actions
        btnPatientDetails.addActionListener(e -> loadPatientDetails());
        btnDoctorDetails.addActionListener(e -> loadDoctorDetails());
        btnAppointmentDetails.addActionListener(e -> loadAppointmentDetails());
        btnInventoryDetails.addActionListener(e -> loadInventoryDetails());
        btnBack.addActionListener(e -> {
            dispose();
            new HomePageGUI();  // Redirects to Home GUI (Replace with your home page class)
        });

        // Layout
        add(lblTitle, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Creates a styled button
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setPreferredSize(new Dimension(200, 40));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(color.darker());
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    // Clears the table before loading a new one
    private void clearTable() {
        tablePanel.removeAll();
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    // Loads Patient Details
    private void loadPatientDetails() {
        String query = "SELECT * FROM patients";
        clearTable();
        displayTable(query, new String[]{"Email", "Name", "Age"});
    }

    // Loads Doctor Details
    private void loadDoctorDetails() {
        String query = "SELECT * FROM doctor_schedules";
        clearTable();
        displayTable(query, new String[]{"Doctor ID", "Doctor Name", "Specialization", "Day", "Time"});
    }

    // Loads Appointment Details
    private void loadAppointmentDetails() {
        String query = "SELECT * FROM appointments";
        clearTable();
        displayTable(query, new String[]{"Appointment ID", "Patient Email", "Doctor ID", "Appointment Date", "Appointment Time"});
    }

    // Loads Inventory Details
    private void loadInventoryDetails() {
        String query = "SELECT * FROM pharmacy_inventory";
        clearTable();
        displayTable(query, new String[]{"Item ID", "Item Name", "Quantity", "Price"});
    }

    // Displays the table based on query
    private void displayTable(String query, String[] columnNames) {
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable tblReport = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblReport);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(44, 62, 80), 2));
        customizeTable(tblReport);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Object[] rowData = new Object[columnNames.length];
                for (int i = 0; i < columnNames.length; i++) {
                    rowData[i] = rs.getObject(i + 1);
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data.");
        }

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    // Customizes the table look
    private void customizeTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(46, 204, 113));
        table.setSelectionForeground(Color.BLACK);
    }
}
