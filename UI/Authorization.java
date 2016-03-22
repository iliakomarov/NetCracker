package UI;

import client.src.client.Client;

import javax.swing.*;
import java.awt.event.*;

public class Authorization extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldLogin;
    private JPasswordField passwordPasswordField;
    private JButton buttonReg;

    public Authorization() {
        setTitle("Authorization");
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

        buttonReg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registration dialog = new Registration();
                dialog.pack();
                dialog.setVisible(true);
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
        client.LogIn(textFieldLogin.getText(), passwordPasswordField.getText());
        dispose();//TODO client
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Authorization dialog = new Authorization();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
