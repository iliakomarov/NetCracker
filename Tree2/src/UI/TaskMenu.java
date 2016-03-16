package Tree2.src.UI;



import Tree2.src.Exceptions.StoppedTaskException;
import Tree2.src.Tree.TaskTreeNode;
import client.src.client.Client;
import client.src.client.exception.NoSuchUserException;
import client.src.info.Task;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Ilia Komarov on 28.02.2016.
 */
public class TaskMenu extends JPopupMenu {
    private Logger logger = Logger.getLogger("TaskMenu");

    //TODO Refresh tree

    public TaskMenu(JTree jTree) {
        RequestTaskName req = new RequestTaskName();

        JMenuItem item = new JMenuItem("Add task");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                //if (req.showDialog() == 1) {
                    /*DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(req.getTaskName());
                    DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();*/
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                    logger.info("Selected node:" + (String)node.getUserObject());
                    AddForm addForm = new AddForm((Tree2.src.Info.Task)node.getUserObject());
                    addForm.pack();
                    addForm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    addForm.setLocationRelativeTo(null);
                    addForm.setSize(new Dimension(400, 300));
                    addForm.setResizable(false);
                    addForm.setVisible(true);
                jTree.repaint();
                    /*if (node != null) model.insertNodeInto(newChild, node, node.getChildCount());
                    */
                //}
            }
        });
        add(item);

        item = new JMenuItem("Remove task");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                //DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                Client client = Client.getClient();
                logger.info("Delete:" + node.getUserObject());
                try {
                    client.deleteTask(0, "Ilya");
                } catch (NoSuchUserException e1) {
                    e1.printStackTrace();
                }
                try {
                    TabbedPaneExample.refreshTree(new JTree(client.getTree("Ilya")));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (NoSuchUserException e1) {
                    e1.printStackTrace();
                }
                //if (node != null && node.getParent() != null) model.removeNodeFromParent(node);
                jTree.repaint();
            }
        });
        add(item);

        item = new JMenuItem("Rename task");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                if (req.showDialog() == 1) {
                    DefaultMutableTreeNode pathForLocation = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                    if (pathForLocation != null) pathForLocation.setUserObject(req.getTaskName());
                    jTree.repaint();
                }
            }
        });
        add(item);


        /*TaskTreeNode node = (TaskTreeNode) jTree.getLastSelectedPathComponent();
        if (node.getTask().isBusy()) {
            item = new JMenuItem("Pause task");
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    TaskTreeNode node = (TaskTreeNode) jTree.getLastSelectedPathComponent();
                    if (node != null) try {
                        node.pauseTask();
                    } catch (StoppedTaskException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            add(item);

            item = new JMenuItem("Stop task");
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    TaskTreeNode node = (TaskTreeNode) jTree.getLastSelectedPathComponent();
                    if (node != null) try {
                        node.stopTask();
                    } catch (StoppedTaskException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            add(item);
        } else {
            item = new JMenuItem("Start task");
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    TaskTreeNode node = (TaskTreeNode) jTree.getLastSelectedPathComponent();
                    if (node != null) try {
                        node.startTask();
                    } catch (StoppedTaskException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            add(item);
        }*/



    }
}
