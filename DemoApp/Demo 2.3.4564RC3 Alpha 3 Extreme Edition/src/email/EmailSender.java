/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package email;

import com.sun.mail.smtp.SMTPSenderFailedException;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javax.mail.internet.AddressException;


/**
 * @author Jose_Balle
 */
public class EmailSender {
    //Added EmailView to update email status label.
    EmailView emailView = new EmailView();

    // Constructors
    EmailSender() {

    }

    EmailSender(String fromEmail, String username, String password, String toEmail, String subject, String textMessage, String attachment, Label output) {

    }

    public static void EmailSender(String email, String password, String toEmail, String subject, String message, String attachment, Label output) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.office365.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.starttls.enable", "true");

            Session mailSession = Session.getInstance(props, null);
            mailSession.setDebug(true);

            Message emailMessage = new MimeMessage(mailSession);

            emailMessage.setFrom(new InternetAddress(email));
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // Mail multiple recipients using a comma ( , ) as parse
            emailMessage.setSubject(subject);

            // Create body part for the text message and attatchment
            BodyPart messageBodyPart = new MimeBodyPart();
            BodyPart attatchmentBodyPart = new MimeBodyPart();
            
            // set contnets for text message and attatchment bodyparts
            messageBodyPart.setText(message); 

            //Create bodypart for attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            attatchmentBodyPart.setDataHandler(new DataHandler(source));
            attatchmentBodyPart.setFileName(attachment);

            //Add bodyparts
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attatchmentBodyPart);

            emailMessage.setContent(multipart);

            //Helps Sends Message through smtp protocol
            Transport transport = mailSession.getTransport("smtp");
            transport.connect("smtp.office365.com", email, password);

            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());

            transport.send(emailMessage);
            
            //Email Status
            output.setText("Email Sent!");            
            
        } catch (AddressException e) {
            e.printStackTrace();
            output.setText("Username or Password Mispelled or Missing!");
        } catch (MessagingException e) {
            e.printStackTrace();
            output.setText("Email Not Sent!");
        }
    }
}
