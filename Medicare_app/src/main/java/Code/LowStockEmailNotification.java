package Code;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class LowStockEmailNotification {

    // Email setup and sending method
    public static void sendEmail(String to, String subject, String messageBody) {
        String from = "kavindunethvitha96@gmail.com"; // Your email address
        String host = "smtp.gmail.com"; // SMTP server for Gmail
        String password = "cklf yvnz hvwu ldxl"; // Your email password or app password for Gmail

        // Set properties for Gmail SMTP server
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a session with the properties and authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create a message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(messageBody);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // Method to check stock and send notification if low
    public static void checkAndNotifyLowStock(int stockLevel, String itemName, String userEmail) {
        if (stockLevel < 10) {  // If stock is below threshold (e.g., 10)
            sendLowStockEmail(itemName, stockLevel, userEmail);  // Send low stock email
        }
    }

    // Method to send a custom low stock email
    public static void sendLowStockEmail(String itemName, int stockLevel, String userEmail) {
        String subject = "Low Stock Notification: " + itemName;
        String messageBody = "Dear User,\n\nThe stock for item '" + itemName + "' is low. Only " + stockLevel + " units remain.\nPlease consider restocking.\n\nBest regards,\nYour Inventory System";

        // Send email with the above subject and message
        sendEmail(userEmail, subject, messageBody);
    }
}
