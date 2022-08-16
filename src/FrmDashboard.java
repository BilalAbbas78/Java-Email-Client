import javax.swing.*;

public class FrmDashboard extends JFrame {
    private JButton btnInbox;
    private JPanel panel1;
    private JButton btnComposeMail;

    public FrmDashboard() {
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        btnInbox.addActionListener(e -> {
            new FrmInbox().setVisible(true);
            setVisible(false);
        });

        btnComposeMail.addActionListener(e -> {
            new FrmComposeMail().setVisible(true);
            setVisible(false);
        });
    }
}
