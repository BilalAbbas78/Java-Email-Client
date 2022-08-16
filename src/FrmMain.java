import javax.mail.PasswordAuthentication;
import javax.swing.*;
import java.awt.event.*;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class FrmMain extends JFrame {
    private JPanel contentPane;
    private JButton btnLogin;
    private JButton btnExit;
    private JTextField txtUsername;
    private JTextField txtPassword;
    private JLabel lblPassword;
    private JLabel lblUsername;

    public static String username, password;

    public static boolean isValid = false;

    public static void main(String[] args) {
        FrmMain dialog = new FrmMain();
//        dialog.pack();
        dialog.setVisible(true);
    }

    public FrmMain() {
        setContentPane(contentPane);
//        setModal(true);
        getRootPane().setDefaultButton(btnLogin);

        btnLogin.addActionListener(e -> {
            onLogin();
        });
        btnExit.addActionListener(e -> onExit());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });

        contentPane.registerKeyboardAction(e -> onExit(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setSize(230, 200);
        setTitle("Login");
    }

    private void onLogin() {
        username = txtUsername.getText();
        password = txtPassword.getText();
        EmailReceiver.downloadEmails("imap", "localhost", "143", txtUsername.getText(), txtPassword.getText());
        if (isValid) {
            new FrmDashboard().setVisible(true);
            setVisible(false);
        }
    }

    private void onExit() {
        // add your code here if necessary
        dispose();
    }
}
