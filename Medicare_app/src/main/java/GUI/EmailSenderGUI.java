package GUI;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

public class EmailSenderGUI {

    private static final String fromEmail = "medicareplus2025@gmail.com";
    private static final String password = "lnie mmlv osmd xvrk";

    private JFrame frame;
    private JTextField toField;
    private JTextArea messageArea;

    public EmailSenderGUI() {
        frame = new JFrame("Medicare Plus - Email Sender");
        frame.setSize(500, 450);  // Adjusted size to accommodate the new button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panel for form components
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Send Email");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        // Recipient Email Label
        JLabel toLabel = new JLabel("Recipient Email:");
        toLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(toLabel, gbc);

        // Recipient Email Field
        toField = new JTextField();
        toField.setFont(new Font("Arial", Font.PLAIN, 14));
        toField.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 1;
        panel.add(toField, gbc);

        // Message Label
        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(messageLabel, gbc);

        // Message Area
        messageArea = new JTextArea(5, 30);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 14));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setPreferredSize(new Dimension(300, 100));
        gbc.gridx = 1;
        panel.add(messageScroll, gbc);

        // Send Button
        JButton sendButton = new JButton("Send Email");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBackground(new Color(0, 102, 204));
        sendButton.setPreferredSize(new Dimension(150, 40));
        sendButton.setBorder(BorderFactory.createBevelBorder(0));
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String toEmail = toField.getText();
                String messageText = messageArea.getText();
                if (!toEmail.isEmpty() && !messageText.isEmpty()) {
                    sendEmail(toEmail, messageText);
                    toField.setText("");
                    messageArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter recipient email and message.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(sendButton, gbc);

        // Back to Home Button
        JButton homeButton = new JButton("Back to Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 14));
        homeButton.setForeground(Color.WHITE);
        homeButton.setBackground(new Color(255, 99, 71));  // Tomato red color for the "Back to Home" button
        homeButton.setPreferredSize(new Dimension(150, 40));
        homeButton.setBorder(BorderFactory.createBevelBorder(0));
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic for "Back to Home" button, here it will close the current frame
                frame.dispose();
                // Optionally, create and show the home screen window here
                JOptionPane.showMessageDialog(null, "Going back to Home Screen...");
                // Example to launch a new HomeScreen window (You can create this as another JFrame)
                // new HomeScreen().setVisible(true);  // Uncomment this line after creating HomeScreen class
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(homeButton, gbc);

        frame.add(panel, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }

    private void sendEmail(String toEmail, String messageText) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Medicare Plus");
            message.setText(messageText);

            Transport.send(message);
            JOptionPane.showMessageDialog(frame, "Email sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Welcome email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error sending email: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
  /*  public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmailSenderGUI());
    }*/
}
