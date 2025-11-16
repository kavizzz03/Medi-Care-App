package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Db.*;
import Code.WelcomeEmailSender;
import javax.swing.table.DefaultTableCellRenderer;


public class PatientManagement extends JFrame {
    private JTextField txtName, txtAge, txtEmail;
    private JButton btnAdd, btnUpdate, btnDelete, btnView, btnBackToHome;
    private JTable tblPatients;
    private DefaultTableModel tableModel;

    public PatientManagement() {
        setTitle("Patient Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main Panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Input Panel with styling
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(new Color(230, 230, 255));

        // Input Fields with Labels
        inputPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        inputPanel.add(txtName);

        inputPanel.add(new JLabel("Age:"));
        txtAge = new JTextField();
        inputPanel.add(txtAge);

        inputPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        inputPanel.add(txtEmail);

        // Buttons with custom styling
        btnAdd = createStyledButton("Add");
        btnUpdate = createStyledButton("Update");
        btnDelete = createStyledButton("Delete");
        btnBackToHome = createStyledButton("Back to Home");

        inputPanel.add(btnAdd);
        inputPanel.add(btnUpdate);
        inputPanel.add(btnDelete);
        inputPanel.add(btnBackToHome);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Table Panel with customized look
        String[] columnNames = {"Email", "Name", "Age"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblPatients = new JTable(tableModel);
        tblPatients.setFillsViewportHeight(true);
        tblPatients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPatients.setRowHeight(30);
        tblPatients.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblPatients.getTableHeader().setBackground(new Color(100, 100, 250));
        tblPatients.getTableHeader().setForeground(Color.WHITE);

        tblPatients.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        JScrollPane scrollPane = new JScrollPane(tblPatients);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        btnView = createStyledButton("View Patients");
        buttonPanel.add(btnView);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnAdd.addActionListener(e -> addPatient());
        btnUpdate.addActionListener(e -> updatePatient());
        btnDelete.addActionListener(e -> deletePatient());
        btnView.addActionListener(e -> loadPatients());
        btnBackToHome.addActionListener(e -> backToHome());

        // Finalizing the frame
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Create a styled button with hover effect
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
        return button;
    }

    // Custom Table Cell Renderer for alternating row colors
    class CustomTableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row % 2 == 0) {
                cell.setBackground(new Color(245, 245, 245)); // Light gray for even rows
            } else {
                cell.setBackground(Color.WHITE); // White for odd rows
            }
            return cell;
        }
    }

    // Add patient method
    private void addPatient() {
        String name = txtName.getText();
        int age = Integer.parseInt(txtAge.getText());
        String email = txtEmail.getText();

        String query = "INSERT INTO patients (email, name, age) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, name);
            stmt.setInt(3, age);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient added successfully!");

            // Send welcome email to the patient
            WelcomeEmailSender.sendWelcomeEmail(email, name);

            // Clear the text fields after successful addition
            txtName.setText("");
            txtAge.setText("");
            txtEmail.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding patient. Email might already exist.");
        }
    }

    // Update patient method
    private void updatePatient() {
        int selectedRow = tblPatients.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to update.");
            return;
        }

        String email = (String) tableModel.getValueAt(selectedRow, 0);
        String name = txtName.getText();
        int age = Integer.parseInt(txtAge.getText());

        String query = "UPDATE patients SET name = ?, age = ? WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient updated successfully!");

            // Clear the text fields after successful update
            txtName.setText("");
            txtAge.setText("");
            txtEmail.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating patient.");
        }
    }

    // Delete patient method
    private void deletePatient() {
        int selectedRow = tblPatients.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete.");
            return;
        }

        String email = (String) tableModel.getValueAt(selectedRow, 0);

        String query = "DELETE FROM patients WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting patient.");
        }
    }

    // Load patients method
    private void loadPatients() {
        String query = "SELECT * FROM patients";
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getInt("age")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patients.");
        }
    }

    // Back to Home functionality
    private void backToHome() {
        this.dispose(); // Close the current frame
        new HomePageGUI(); // Open the home screen
    }
}
