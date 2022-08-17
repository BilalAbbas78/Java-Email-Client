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
    private JLabel lblMesage;
    private JButton btnBack;
    private JButton btnAttachment;
    private JButton btnDelete;

    FrmInbox() {
        super("Inbox");
        btnAttachment.setEnabled(false);
        setContentPane(panel1);
//        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setVisible(true);

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("From");
        model.addColumn("Date");
        model.addColumn("Subject");

        EmailReceiver.downloadEmails("imap", "localhost", "143", FrmMain.username, FrmMain.password);
        for (MyInbox inbox : EmailReceiver.inbox) {
            model.addRow(new Object[]{inbox.from, inbox.date, inbox.subject});
        }

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow();
                lblMesage.setText("Message: " + EmailReceiver.inbox.get(row).message);
                lblFrom.setText("From: " + EmailReceiver.inbox.get(row).from);
                lblDate.setText("Date: " + EmailReceiver.inbox.get(row).date);
                lblSubject.setText("Subject: " + EmailReceiver.inbox.get(row).subject);

                btnAttachment.setEnabled(!EmailReceiver.inbox.get(row).attachment.equals("No"));

            }
        });

        btnDelete.addActionListener(e -> {
            int row = table1.getSelectedRow();
            try {
                EmailReceiver.inbox.get(row).message1.setFlag(Flags.Flag.DELETED, true);
                EmailReceiver.inbox.remove(row);
                model.removeRow(row);
                JOptionPane.showMessageDialog(null, "Message Deleted successfully");
            } catch (MessagingException ex) {
                JOptionPane.showMessageDialog(null, "Message can't be deleted");
                ex.printStackTrace();
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

//        table1.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                int row = table1.getSelectedRow();
//                lblMesage.setText("Message: " + EmailReceiver.inbox.get(row).message);
//                lblFrom.setText("From: " + EmailReceiver.inbox.get(row).from);
//                lblDate.setText("Date: " + EmailReceiver.inbox.get(row).date);
//                lblSubject.setText("Subject: " + EmailReceiver.inbox.get(row).subject);
//            }
//        });



//        for (int i = 0; i < EmailReceiver.inbox.size(); i++) {
//            model.addRow(new Object[]{EmailReceiver.inbox.get(i).from, EmailReceiver.inbox.get(i).to, EmailReceiver.inbox.get(i).subject});
//        }

        table1.setModel(model);
//        setSize(1000, 400);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        btnBack.addActionListener(e -> {
            try {
                EmailReceiver.folderInbox.close(false);
                EmailReceiver.store.close();
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
            new FrmDashboard().setVisible(true);
            setVisible(false);
        });


    }

    public static void main(String[] args) {
        new FrmInbox().setVisible(true);
    }

}