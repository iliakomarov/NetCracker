package UI;


import client.src.client.Client;

import javax.swing.*;
import java.awt.event.*;

public class RequestTaskName extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private int status;

    public String getTaskName() {
        return taskName;
    }

    public int showDialog() {
        pack();
        textField1.setText(taskName);
        setVisible(true);
        return status;
    }
    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    private String taskName;

    public RequestTaskName()
    {
        this("");
    }

    public RequestTaskName(String name) {
        textField1.setText(name);
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

        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        client.src.client.Client client = Client.getClient();

        taskName = textField1.getText();
        textField1.setText("");
        status = 1;
        setVisible(false);
        //dispose();
    }

    private void onCancel() {
// add your code here if necessary
        status = 0;
        textField1.setText("");
        setVisible(false);
        //dispose();
    }

    public void setTextField1(String name) {
        textField1.setText(name);
    }

}
