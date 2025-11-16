package GUI;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import Db.*;

public class AppointmentBooking extends JFrame {
    private JComboBox<String> cmbPatient, cmbDoctor;
    private JTextField txtDate, txtTime;
    private JButton btnBookAppointment, btnBackToHome;
    private List<String> patientEmails = new ArrayList<>();
    private List<Integer> doctorIds = new ArrayList<>();

    public AppointmentBooking() {
        setTitle("Book Appointment");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));

        JLabel lblTitle = new JLabel("Appointment Booking", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(44, 62, 80));

        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        mainPanel.add(new JLabel("Select Patient:"));
        cmbPatient = new JComboBox<>();
        loadPatients();
        mainPanel.add(cmbPatient);

        mainPanel.add(new JLabel("Select Doctor:"));
        cmbDoctor = new JComboBox<>();
        loadDoctors();
        mainPanel.add(cmbDoctor);

        mainPanel.add(new JLabel("Appointment Date (YYYY-MM-DD):"));
        txtDate = new JTextField();
        mainPanel.add(txtDate);

        mainPanel.add(new JLabel("Appointment Time (HH:MM:SS):"));
        txtTime = new JTextField();
        mainPanel.add(txtTime);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnBookAppointment = new JButton("Book Appointment");
        btnBackToHome = new JButton("Back to Home");

        buttonPanel.add(btnBookAppointment);
        buttonPanel.add(btnBackToHome);

        btnBookAppointment.addActionListener(e -> bookAppointment());
        btnBackToHome.addActionListener(e -> {
            dispose();
            new HomePageGUI();
        });

        add(lblTitle, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadPatients() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT email FROM patients")) {
            while (rs.next()) {
                String email = rs.getString("email");
                cmbPatient.addItem(email);
                patientEmails.add(email);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading patients.");
        }
    }

    private void loadDoctors() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT doctor_id, doctor_name FROM doctor_schedules")) {
            while (rs.next()) {
                int doctorId = rs.getInt("doctor_id");
                String doctorName = rs.getString("doctor_name");
                cmbDoctor.addItem(doctorName);
                doctorIds.add(doctorId);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading doctors.");
        }
    }

    private void bookAppointment() {
        String selectedPatientEmail = (String) cmbPatient.getSelectedItem();
        int selectedDoctorIndex = cmbDoctor.getSelectedIndex();
        if (selectedDoctorIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.");
            return;
        }

        int selectedDoctorId = doctorIds.get(selectedDoctorIndex);
        String appointmentDate = txtDate.getText();
        String appointmentTime = txtTime.getText();

        if (selectedPatientEmail == null || appointmentDate.isEmpty() || appointmentTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        String query = "INSERT INTO appointments (patient_email, doctor_id, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, selectedPatientEmail);
            stmt.setInt(2, selectedDoctorId);
            stmt.setString(3, appointmentDate);
            stmt.setString(4, appointmentTime);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Appointment booked successfully!");

            sendConfirmationEmail(selectedPatientEmail, appointmentDate, appointmentTime, (String) cmbDoctor.getSelectedItem());

            // Clear fields after successful booking
            txtDate.setText("");
            txtTime.setText("");
            cmbPatient.setSelectedIndex(0);
            cmbDoctor.setSelectedIndex(0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error booking appointment.");
        }
    }

    private void sendConfirmationEmail(String toEmail, String date, String time, String doctor) {
        final String fromEmail = "medicareplus2025@gmail.com";
        final String password = "lnie mmlv osmd xvrk";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Appointment Confirmation");
            message.setText("Dear Patient,\n\nYour appointment is confirmed!\n\nDoctor: " + doctor +
                    "\nDate: " + date +
                    "\nTime: " + time +
                    "\n\nThank you for choosing our service.\n\nBest regards,\nMedicare plus");

            Transport.send(message);
            JOptionPane.showMessageDialog(this, "Confirmation email sent to " + toEmail);
        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(this, "Failed to send email.");
        }
    }
}