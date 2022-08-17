import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class FrmInbox extends JFrame {
    private JPanel panel1;
    private JTable table1;
    private JLabel lblFrom;
    private JLabel lblDate;
    private JLabel lblSubject;
    private JButton btnLogout;
    private JButton btnAttachment;
    private JButton btnDelete;
    private JTextArea txtMessage;
    private JButton btnComposeEmail;

    FrmInbox() {
        super("Inbox");
        btnAttachment.setEnabled(false);
        setContentPane(panel1);
//        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setVisible(true);

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("From");
        model.addColumn("Subject");
        model.addColumn("Date");

        EmailReceiver.downloadEmails("imap", "localhost", "143", FrmMain.username, FrmMain.password);
        for (MyInbox inbox : EmailReceiver.inbox) {
            model.addRow(new Object[]{inbox.from, inbox.subject, inbox.date});
        }

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow();
                txtMessage.setText("Message: " + EmailReceiver.inbox.get(row).message);
                lblFrom.setText("From: " + EmailReceiver.inbox.get(row).from);
                lblDate.setText("Date: " + EmailReceiver.inbox.get(row).date);
                lblSubject.setText("Subject: " + EmailReceiver.inbox.get(row).subject);

                btnAttachment.setEnabled(!EmailReceiver.inbox.get(row).attachment.equals("No"));

            }
        });

        btnComposeEmail.addActionListener(e -> {
            new FrmComposeMail().setVisible(true);
//            setVisible(false);
        });

        btnDelete.addActionListener(e -> {
            int row = table1.getSelectedRow();

            int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure want to delete this email?","Delete", JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION){
                try {
                    EmailReceiver.inbox.get(row).message1.setFlag(Flags.Flag.DELETED, true);
                    EmailReceiver.inbox.remove(row);
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(null, "Message Deleted successfully");
                } catch (MessagingException ex) {
                    JOptionPane.showMessageDialog(null, "Message can't be deleted");
                    ex.printStackTrace();
                }
                btnAttachment.setEnabled(false);
            }
            else{
                // Do nothing
            }
        } );

        btnAttachment.addActionListener(e -> {
            int row = table1.getSelectedRow();
            try {
//                EmailReceiver.inbox.get(row).part.saveFile("D:/Attachments/" + EmailReceiver.inbox.get(row).part.getFileName().replaceFirst("D:/", ""));
                EmailReceiver.inbox.get(row).part.saveFile("D:/Attachments" + File.separator + EmailReceiver.inbox.get(row).part.getFileName());

                JOptionPane.showMessageDialog(null, "Attachment downloaded successfully");
            } catch (MessagingException ex) {
                JOptionPane.showMessageDialog(null, "Attachment can't successfully");
                ex.printStackTrace();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        table1.setModel(model);
//        setSize(1000, 400);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        btnLogout.addActionListener(e -> {
            try {
                EmailReceiver.folderInbox.close(false);
                EmailReceiver.store.close();
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
            new FrmMain().setVisible(true);
            setVisible(false);
        });


    }

    public static void main(String[] args) {
        new FrmInbox().setVisible(true);
    }

}