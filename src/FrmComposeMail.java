import javax.mail.MessagingException;
import javax.swing.*;

public class FrmComposeMail extends JFrame {
    private JPanel myPanel;
    private JTextField txtTo;
    private JLabel lblTo;
    private JLabel lblMessage;
    private JTextArea txtMessage;
    private JButton btnSend;
    private JButton btnBack;
    private JTextField txtSubject;
    private JLabel lblSubject;

    public static String to, subject, message;

    FrmComposeMail() {
        setTitle("Compose Mail");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(myPanel);

        btnSend.addActionListener(e -> {
            to = txtTo.getText();
            message = txtMessage.getText();
            subject = txtSubject.getText();

            if (to.equals("") || message.equals("") || subject.equals("")) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields");
            } else {
                try {
                    new EmailSender();
                } catch (MessagingException ex) {
                    JOptionPane.showMessageDialog(null, "Message can't send");
                }
                JOptionPane.showMessageDialog(null, "Message sent successfully");
                txtTo.setText("");
                txtMessage.setText("");
                txtSubject.setText("");
            }
//            EmailSender.sendMessage(FrmMain.username, to, message);
        });

        btnBack.addActionListener(e -> {
            new FrmDashboard().setVisible(true);
            setVisible(false);
        });
    }

    public static void main(String[] args) {
        new FrmComposeMail().setVisible(true);
    }
}
