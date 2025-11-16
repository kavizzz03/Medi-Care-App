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

public class DoctorScheduleManagement extends JFrame {
    private JTextField txtDoctorName, txtSpecialization, txtDay, txtTime;
    private JButton btnAdd, btnUpdate, btnDelete, btnView, btnBack;
    private JTable tblSchedules;
    private DefaultTableModel tableModel;

    public DoctorScheduleManagement() {
        setTitle("Doctor Schedule Management");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel lblTitle = new JLabel("Doctor Schedule Management", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(44, 62, 80));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        inputPanel.add(new JLabel("Doctor Name:"));
        txtDoctorName = new JTextField();
        inputPanel.add(txtDoctorName);

        inputPanel.add(new JLabel("Specialization:"));
        txtSpecialization = new JTextField();
        inputPanel.add(txtSpecialization);

        inputPanel.add(new JLabel("Day (e.g. Monday):"));
        txtDay = new JTextField();
        inputPanel.add(txtDay);

        inputPanel.add(new JLabel("Time (e.g. 9:00 AM - 5:00 PM):"));
        txtTime = new JTextField();
        inputPanel.add(txtTime);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnAdd = createStyledButton("Add", new Color(52, 152, 219));
        btnUpdate = createStyledButton("Update", new Color(241, 196, 15));
        btnDelete = createStyledButton("Delete", new Color(231, 76, 60));
        btnView = createStyledButton("View Schedules", new Color(46, 204, 113));
        btnBack = createStyledButton("Back to Home", new Color(127, 140, 141));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);
        buttonPanel.add(btnBack);

        // Table Panel
        String[] columnNames = {"Doctor ID", "Doctor Name", "Specialization", "Day", "Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblSchedules = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblSchedules);
        customizeTable();

        // Button Actions
        btnAdd.addActionListener(e -> addSchedule());
        btnUpdate.addActionListener(e -> updateSchedule());
        btnDelete.addActionListener(e -> deleteSchedule());
        btnView.addActionListener(e -> loadSchedules());
        btnBack.addActionListener(e -> {
            dispose();  // Closes the current window
            new HomePageGUI();  // Redirects to the home page (Replace with your home GUI class)
        });

        // Layout
        add(lblTitle, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(140, 40));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

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

    private void customizeTable() {
        tblSchedules.setRowHeight(30);
        tblSchedules.setFont(new Font("Arial", Font.PLAIN, 14));
        tblSchedules.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblSchedules.getTableHeader().setBackground(new Color(52, 73, 94));
        tblSchedules.getTableHeader().setForeground(Color.WHITE);
        tblSchedules.setSelectionBackground(new Color(46, 204, 113));
        tblSchedules.setSelectionForeground(Color.BLACK);
    }

    private void addSchedule() {
        String doctorName = txtDoctorName.getText();
        String specialization = txtSpecialization.getText();
        String day = txtDay.getText();
        String time = txtTime.getText();

        if (doctorName.isEmpty() || specialization.isEmpty() || day.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        String query = "INSERT INTO doctor_schedules (doctor_name, specialization, day, time) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, doctorName);
            stmt.setString(2, specialization);
            stmt.setString(3, day);
            stmt.setString(4, time);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor schedule added successfully!");
            loadSchedules();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding schedule.");
        }
    }

    private void updateSchedule() {
        int selectedRow = tblSchedules.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a schedule to update.");
            return;
        }

        int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
        String doctorName = txtDoctorName.getText();
        String specialization = txtSpecialization.getText();
        String day = txtDay.getText();
        String time = txtTime.getText();

        String query = "UPDATE doctor_schedules SET doctor_name = ?, specialization = ?, day = ?, time = ? WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, doctorName);
            stmt.setString(2, specialization);
            stmt.setString(3, day);
            stmt.setString(4, time);
            stmt.setInt(5, doctorId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Schedule updated successfully!");
            loadSchedules();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating schedule.");
        }
    }

    private void deleteSchedule() {
        int selectedRow = tblSchedules.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a schedule to delete.");
            return;
        }

        int doctorId = (int) tableModel.getValueAt(selectedRow, 0); // Get doctor_id from selected row

        String query = "DELETE FROM doctor_schedules WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, doctorId); // Delete the schedule based on doctor_id
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor schedule deleted successfully!");
            loadSchedules();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting doctor schedule.");
        }
    }

    private void loadSchedules() {
        String query = "SELECT * FROM doctor_schedules";
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("doctor_id"),
                        rs.getString("doctor_name"),
                        rs.getString("specialization"),
                        rs.getString("day"),
                        rs.getString("time")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading schedules.");
        }
    }
}
