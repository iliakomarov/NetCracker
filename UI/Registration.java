package UI;

import client.src.client.Client;
import client.src.client.exception.NoSuchUserException;

import javax.swing.*;
import java.awt.event.*;

public class Registration extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;

    public Registration() {
        setTitle("Registration");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        Client client = Client.getClient();
        try {
            if(passwordField1.getText().equals(passwordField2.getText())) {
                client.Registration(textField1.getText(), textField2.getText(), textField3.getText(), passwordField1.getText());
            }
            else {
                JOptionPane.showMessageDialog(null, "Password should equal!");
            }
        } catch (NoSuchUserException e) {
            JOptionPane.showMessageDialog(null, "User already exist!");
        }
        dispose(); //TODO client
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Registration dialog = new Registration();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
