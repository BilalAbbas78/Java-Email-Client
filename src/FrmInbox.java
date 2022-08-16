import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrmInbox extends JFrame {
    private JPanel panel1;
    private JTable table1;
    private JLabel lblFrom;
    private JLabel lblTo;
    private JLabel lblSubject;
    private JLabel lblMesage;
    private JButton btnBack;

    FrmInbox() {
        super("Inbox");
        setContentPane(panel1);
//        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setVisible(true);

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("From");
        model.addColumn("To");
        model.addColumn("Subject");

        EmailReceiver.downloadEmails("imap", "localhost", "143", FrmMain.username, FrmMain.password);
        for (MyInbox inbox : EmailReceiver.inbox) {
            model.addRow(new Object[]{inbox.from, inbox.to, inbox.subject});
        }

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow();
                lblMesage.setText("Message: " + EmailReceiver.inbox.get(row).message);
                lblFrom.setText("From: " + EmailReceiver.inbox.get(row).from);
                lblTo.setText("To: " + EmailReceiver.inbox.get(row).to);
                lblSubject.setText("Subject: " + EmailReceiver.inbox.get(row).subject);
            }
        });

        table1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int row = table1.getSelectedRow();
                lblMesage.setText("Message: " + EmailReceiver.inbox.get(row).message);
                lblFrom.setText("From: " + EmailReceiver.inbox.get(row).from);
                lblTo.setText("To: " + EmailReceiver.inbox.get(row).to);
                lblSubject.setText("Subject: " + EmailReceiver.inbox.get(row).subject);
            }
        });



//        for (int i = 0; i < EmailReceiver.inbox.size(); i++) {
//            model.addRow(new Object[]{EmailReceiver.inbox.get(i).from, EmailReceiver.inbox.get(i).to, EmailReceiver.inbox.get(i).subject});
//        }

        table1.setModel(model);
//        setSize(1000, 400);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        btnBack.addActionListener(e -> {
            new FrmDashboard().setVisible(true);
            setVisible(false);
        });


    }

    public static void main(String[] args) {
        new FrmInbox().setVisible(true);
    }

}