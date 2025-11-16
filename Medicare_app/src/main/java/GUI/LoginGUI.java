package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginGUI() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel lblTitle = new JLabel("Welcome to the Medicare Plus", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(44, 62, 80));
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        // Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        loginPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        loginPanel.add(txtUsername);

        loginPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        loginPanel.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        loginPanel.add(new JLabel());
        loginPanel.add(btnLogin);

        add(loginPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        JLabel lblFooter = new JLabel("© 2025 Medicare Plus. All Rights Reserved.", JLabel.CENTER);
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(lblFooter);
        add(footerPanel, BorderLayout.SOUTH);

        // Button Action
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });

        setVisible(true);
    }

    private void authenticate() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.equals("admin") && password.equals("admin123")) {
            JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new HomePageGUI();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   /* public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }*/
}

class HomePageGUI extends JFrame {
    public HomePageGUI() {
        setTitle("Home Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnReportGeneration = createStyledButton("Report Generation");
        JButton btnDoctorSchedule = createStyledButton("Doctor Schedule Management");
        JButton btnAppointmentManagement = createStyledButton("Appointment Management");
        JButton btnPatientDetails = createStyledButton("Patient Management");
        JButton btnInventoryManagement = createStyledButton("Pharmacy Inventory Management");
        JButton btnPrescriptionManagement = createStyledButton("Email Sending");

        buttonPanel.add(btnReportGeneration);
        buttonPanel.add(btnDoctorSchedule);
        buttonPanel.add(btnAppointmentManagement);
        buttonPanel.add(btnPatientDetails);
        buttonPanel.add(btnInventoryManagement);
        buttonPanel.add(btnPrescriptionManagement);

        add(buttonPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        JLabel lblFooter = new JLabel("© 2025 Medicare Plus. All Rights Reserved.", JLabel.CENTER);
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(lblFooter);
        add(footerPanel, BorderLayout.SOUTH);

        // Button Actions
        btnReportGeneration.addActionListener(e -> openReportGenerationGUI());
        btnDoctorSchedule.addActionListener(e -> openDoctorScheduleManagement());
        btnAppointmentManagement.addActionListener(e -> openAppointmentManagement());
        btnPatientDetails.addActionListener(e -> openPatientDetails());
        btnInventoryManagement.addActionListener(e -> openInventoryManagement());
        btnPrescriptionManagement.addActionListener(e -> openPrescriptionManagement());

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
        });
        return button;
    }

    private void openReportGenerationGUI() {
        JOptionPane.showMessageDialog(this, "Opening Report Generation GUI...");
        new ReportGenerationGUI();
    }

    private void openDoctorScheduleManagement() {
        JOptionPane.showMessageDialog(this, "Opening Doctor Schedule Management...");
        new DoctorScheduleManagement();
    }

    private void openAppointmentManagement() {
        JOptionPane.showMessageDialog(this, "Opening Appointment Management...");
        new AppointmentBooking();
    }

    private void openPatientDetails() {
        JOptionPane.showMessageDialog(this, "Opening Patient Management...");
        new PatientManagement();
    }

    private void openInventoryManagement() {
        JOptionPane.showMessageDialog(this, "Opening Pharmacy Inventory Management...");
        new PharmacyInventory();
    }

    private void openPrescriptionManagement() {
        JOptionPane.showMessageDialog(this, "Opening Email Sending...");
        new EmailSenderGUI();
    }
}
