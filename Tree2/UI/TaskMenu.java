package Tree2.UI;



import Tree2.src.Exceptions.StoppedTaskException;
import Tree2.src.Tree.TaskTreeNode;
import UI.RequestTaskName;
import client.src.client.Client;
import client.src.client.exception.NoSuchUserException;
import client.src.tree.TaskTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Ilia Komarov on 28.02.2016.
 */
public class TaskMenu extends JPopupMenu {

    //TODO client на всех операциях с деревом
    public TaskMenu(JTree jTree) {
        RequestTaskName req = new RequestTaskName();

        JMenuItem item = new JMenuItem("Add task");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                if (req.showDialog() == 1) {
                    client.src.tree.TaskTreeNode newChild = null;
                    try {
                        newChild = client.src.tree.TaskTreeNode.getInstance(req.getTaskName());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
                    client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
                    client.src.client.Client client = Client.getClient();
                    try {
                        if(node.getTask().getName().split("/").length == 2) {
                            newChild.getTask().changeName(newChild.getTask().getName() + "/general");
                            client.addTask(newChild, node.getTask().getId(), "general");
                        }
                        else client.addTask(newChild, node.getTask().getId(), "");
                    } catch (NoSuchUserException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (node != null) model.insertNodeInto(newChild, node, node.getChildCount());
                    jTree.repaint();
                }
            }
        });
        add(item);

        item = new JMenuItem("Remove task");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
                client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
                client.src.client.Client client = Client.getClient();
                try {
                    if(node.getTask().getName().split("/").length == 2) {
                        client.deleteTask(node.getTask().getId(), "general");
                    }
                    else client.deleteTask(node.getTask().getId(), "");
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
                    client.src.tree.TaskTreeNode pathForLocation = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
                    DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
                    client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
                    client.src.client.Client client = Client.getClient();
                    try {
                        if(node.getTask().getName().split("/").length == 2) client.rename(req.getTaskName() + "/general", node.getTask().getId(), "general");
                        else client.rename(req.getTaskName(), node.getTask().getId(), "");
                    } catch (NoSuchUserException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (pathForLocation != null) pathForLocation.renameTask(req.getTaskName() + "/general");
                    jTree.repaint();
                }
            }
        });
        add(item);

        client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
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
                    jTree.repaint();
                }
            });
            add(item);

            item = new JMenuItem("Stop task");
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
                    client.src.client.Client client = Client.getClient();
                    try {
                        if(node.getTask().getName().split("/").length == 2) client.stopTask(node.getTask().getId(), "general");
                        else client.stopTask(node.getTask().getId(), "");
                    } catch (NoSuchUserException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (node != null) try {
                        node.stopTask();
                    } catch (StoppedTaskException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    jTree.repaint();
                }
            });
            add(item);
        } else {
            item = new JMenuItem("Start task");
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
                    client.src.client.Client client = Client.getClient();
                    try {
                        if(node.getTask().getName().split("/").length == 2) client.startTask(node.getTask().getId(), "general");
                        else client.startTask(node.getTask().getId(), "general");
                    } catch (NoSuchUserException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (node != null) try {
                        node.startTask();
                    } catch (StoppedTaskException e1) {
                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    jTree.repaint();
                }
            });
            add(item);
        }
    }
}
