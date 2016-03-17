package Tree2.src.UI;

import client.src.client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Authorization extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldLogin;
    private JPasswordField passwordPasswordField;
    private JButton buttonReg;

    public Authorization() {

        contentPane = new JPanel();
        buttonOK = new JButton("OK");
        buttonCancel = new JButton("Cancel");
        textFieldLogin = new JTextField();
        passwordPasswordField = new JPasswordField();
        buttonReg = new JButton("Registration");

        contentPane.setLayout(null);
        contentPane.setBounds(0, 0, 500, 200);
        buttonOK.setBounds(10, 100, 80, 30);
        buttonCancel.setBounds(100, 100, 80, 30);
        buttonReg.setBounds(355, 100, 120, 30);
        textFieldLogin.setBounds(10, 10, 465, 20);
        passwordPasswordField.setBounds(10, 40, 465, 20);

        contentPane.add(buttonOK, BorderLayout.NORTH);
        contentPane.add(buttonCancel, BorderLayout.CENTER);
        contentPane.add(textFieldLogin);
        contentPane.add(passwordPasswordField);
        contentPane.add(buttonReg, BorderLayout.EAST);


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
// add your code here
        String login = textFieldLogin.getText();
        String password = passwordPasswordField.getText();
        Client client = Client.getClient();
        if (client.LogIn(login, password)){
            try {
                TabbedPaneExample mainFrame = new TabbedPaneExample();
                mainFrame.pack();
                mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setSize(new Dimension(800, 600));
                mainFrame.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //dispose();//TODO client
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Authorization dialog = new Authorization();
        dialog.pack();
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setBounds(0, 0, 500, 200);
        dialog.setVisible(true);
        //System.exit(0);
    }
}
