package Tree2.src.UI;

import client.Client;
import client.src.tree.TreeNode;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/**
 * Created by Fadeev on 01.03.2016.
 */
public class AddForm extends JFrame {

    private JTextArea userObject;
    private JTextField treeName;
    private JButton ok;
    private JButton cancel;
    private String userTask;
    private String parent;
    private Logger logger = Logger.getLogger("AddForm");

    public AddForm(String parent){
        this.parent = parent;

        userObject = new JTextArea();
        ok = new JButton("OK");
        cancel = new JButton("Cancel");
        this.setLayout(null);

        cancel.setBounds(10, 210, 100, 50);
        ok.setBounds(285, 210, 100, 50);
        userObject.setBounds(0, 0, 400, 200);


        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUserTask(userObject.getText());
                client.src.client.Client client = TabbedPaneExample.getClient();
                logger.info("Client:" + client.toString());
                logger.info("parent:" + parent);
                client.addTask(userTask, parent, "Ilya");
                dispose();
            }
        });


        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        this.add(userObject);
        this.add(ok);
        this.add(cancel);
    }

    public  void showFrame(String parent){

    }


    public static void main( String args[] )
    {

    }

    public String getUserTask() {
        return userTask;
    }

    public void setUserTask(String userTask) {
        this.userTask = userTask;
    }
}
