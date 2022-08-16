import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
    static Properties prop = new Properties();
    static Session session;

    EmailSender() throws MessagingException {
        setProperties();
        createSession(FrmMain.username, FrmMain.password);
        sendMessage(FrmMain.username, FrmComposeMail.to, FrmComposeMail.message, FrmComposeMail.subject);
    }

//    public static void main(String[] args) throws MessagingException {
//        createSession("account1@bilal.com", "123");
//        sendMessage("account1@bilal.com", "account2@bilal.com");
//    }

    public static void setProperties() {
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "localhost");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "localhost");
    }

    public static void createSession(String username, String password) {
        session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public static void sendMessage(String sender, String receiver, String msg, String subject) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(receiver));
        message.setSubject(subject);

//        String msg = "This is my fourth email using JavaMailer";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}
