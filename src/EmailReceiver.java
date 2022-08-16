import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.*;

class MyInbox {
    String from, to, subject, message;
    MyInbox(String from, String to, String subject, String message) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
    }
}


public class EmailReceiver {

    public static ArrayList<MyInbox> inbox = new ArrayList<>();

    private static Properties getServerProperties(String protocol, String host,
                                                  String port) {
        Properties properties = new Properties();

        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);

        /*
 SSL setting
        properties.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");
*/
        properties.setProperty(
                String.format("mail.%s.socketFactory.fallback", protocol),
                "false");
        properties.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(port));

        return properties;
    }

    public static void downloadEmails(String protocol, String host, String port,
                                      String userName, String password) {
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);

        try {
            // connects to the message store
            Store store = session.getStore(protocol);
            store.connect(userName, password);

            FrmMain.isValid = true;

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);

            // fetches new messages from server
            Message[] messages = folderInbox.getMessages();
            inbox.clear();
            for (Message msg : messages) {
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                String toList = parseAddresses(msg
                        .getRecipients(RecipientType.TO));
                String ccList = parseAddresses(msg
                        .getRecipients(RecipientType.CC));
                String sentDate = msg.getSentDate().toString();

                String contentType = msg.getContentType();
                String messageContent = "";

//                if (contentType.contains("text/plain")
//                        || contentType.contains("text/html")) {
                try {
                    Object content = msg.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                        inbox.add(new MyInbox(from, toList, subject, messageContent));
                    }
                } catch (Exception ex) {
                    messageContent = "[Error downloading content]";
                    ex.printStackTrace();
                }
//                }


                // print out details of each message
//                System.out.println("Message #" + (i + 1) + ":");
//                System.out.println("\t From: " + from);
//                System.out.println("\t To: " + toList);
//                System.out.println("\t CC: " + ccList);
//                System.out.println("\t Subject: " + subject);
//                System.out.println("\t Sent Date: " + sentDate);
//                System.out.println("\t Message: " + messageContent);
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            FrmMain.isValid = false;
            JOptionPane.showMessageDialog(null, "Enter valid username and password", "Invalid Credentials", JOptionPane.ERROR_MESSAGE);
//            System.out.println("Could not connect to the message store");
//            ex.printStackTrace();
        }
    }

    /**
     * Returns a list of addresses in String format separated by comma
     *
     * @param address an array of Address objects
     * @return a string represents a list of addresses
     */
    private static String parseAddresses(Address[] address) {
        StringBuilder listAddress = new StringBuilder();

        if (address != null) {
            for (Address value : address) {
                listAddress.append(value.toString()).append(", ");
            }
        }
        if (listAddress.length() > 1) {
            listAddress = new StringBuilder(listAddress.substring(0, listAddress.length() - 2));
        }

        return listAddress.toString();
    }

    /**
     * Test downloading e-mail messages
     */
    public static void main(String[] args) {
        // for POP3
        //String protocol = "pop3";
        //String host = "pop.gmail.com";
        //String port = "995";

        // for IMAP
        String protocol = "imap";
        String host = "localhost";
        String port = "143";

//        String userName = "account1@bilal.com";
//        String password = "123";
        String userName = FrmMain.username;
        String password = FrmMain.password;

        System.out.println("Downloading emails from " + userName + "...");

        EmailReceiver receiver = new EmailReceiver();
        receiver.downloadEmails(protocol, host, port, userName, password);
    }
}