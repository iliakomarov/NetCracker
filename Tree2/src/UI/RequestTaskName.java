package Tree2.src.UI;

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

    public int showDialog()
    {
        pack();
        setVisible(true);
        return status;
    }

    private String taskName;

    public RequestTaskName() {
        contentPane = new JPanel();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK = new JButton("OK");
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel = new JButton("Cancel");
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
        taskName=textField1.getText();
        textField1.setText("");
        status=1;
        setVisible(false);
        //dispose();
    }

    private void onCancel() {
// add your code here if necessary
        status=0;
        textField1.setText("");
        setVisible(false);
        //dispose();
    }

}
