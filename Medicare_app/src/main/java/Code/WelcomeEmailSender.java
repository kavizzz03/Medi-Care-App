package Code;
import GUI.PatientManagement;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class WelcomeEmailSender {
    private static final String fromEmail = "medicareplus2025@gmail.com";  // Replace with your email
    private static final String password = "lnie mmlv osmd xvrk";     // Replace with your email password

    public static void sendWelcomeEmail(String toEmail, String name) {
        String subject = "Welcome to Medicare Plus!";
        String messageText = "Dear " + name + ",\n\nWelcome to Medicare Plus! We're glad to have you with us.\n\nBest Regards,\nMedicare Plus Team";

        sendEmail(toEmail, subject, messageText);
    }

    private static void sendEmail(String toEmail, String subject, String messageText) {
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
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);
            System.out.println("Welcome email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error sending welcome email: " + e.getMessage());
        }
    }
}
