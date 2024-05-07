package services;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static void sendEmail(String senderName, String recipientEmail, String subject, String body) {
        // Sender's email and password
        final String senderEmail = "yikara707@gmail.com";
        final String senderPassword = "jppv qzuj vqfq iojw";

        // Email properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);

            // Set sender and recipient email addresses
            try {
                message.setFrom(new InternetAddress(senderEmail, senderName));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            // Set email subject and body
            message.setSubject(subject);
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(body, "text/html"); // Set content type to HTML

            // Create a Multipart object to hold the MimeBodyPart objects
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);

            // Set the content of the message to the Multipart object
            message.setContent(multipart);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
