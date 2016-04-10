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
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Vector;

/**
 * Created by Ilia Komarov on 28.02.2016.
 */
public class TaskMenu extends JPopupMenu {
    private int currentTab;
    public TaskMenu(JTree jTree) {

        RequestTaskName req = new RequestTaskName();
        JMenuItem item = new JMenuItem("Add task");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                boolean isSameName = false;
                if (req.showDialog() == 1) {
                    client.src.tree.TaskTreeNode newChild = null;
                    try {
                        newChild = client.src.tree.TaskTreeNode.getInstance(req.getTaskName());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
                    client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
                    Vector<client.src.tree.TaskTreeNode> children = node.getChildren();
                    if (children != null)
                    for (client.src.tree.TaskTreeNode child : children) {
                        String s = child.getTask().getName();
                        String s1 = req.getTaskName();
                        if (child.getTask().getName().equals(s1)) {
                            isSameName = true;
                            break;
                        } else {
                            isSameName = false;
                        }
                    }
                    if (!isSameName) {
                        client.src.client.Client client = Client.getClient();
                        try {
                            if (getCurrentTab() == 1) {
                                client.addTask(newChild, node.getTask().getId(), "general");
                            } else {
                                client.addTask(newChild, node.getTask().getId(), "");
                            }
                        } catch (NoSuchUserException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        if (node != null) model.insertNodeInto(newChild, node, node.getChildCount());
                        jTree.repaint();
                        if (node != null) jTree.expandPath(new TreePath(model.getPathToRoot(newChild)));
                    }
                    else {
                        JOptionPane.showConfirmDialog(null, "You can not create a task with the same name!", "Warning", JOptionPane.DEFAULT_OPTION);
                    }
                }
            }
        });
        add(item);

        item = new JMenuItem("Remove task");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                if (JOptionPane.showConfirmDialog(null, "Are you sure to remove task?", "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
                    client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
                    client.src.client.Client client = Client.getClient();
                    try {
                        if (getCurrentTab() == 1) {
                            client.deleteTask(node.getTask().getId(), "general");
                        } else client.deleteTask(node.getTask().getId(), "");
                    } catch (NoSuchUserException e1) {
                        e1.printStackTrace();
                    }
                    if (node != null && node.getParent() != null) model.removeNodeFromParent(node);
                    jTree.repaint();
                    setVisible(false);
                }
            }
        });
        add(item);

        item = new JMenuItem("Rename task");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                client.src.tree.TaskTreeNode pathForLocation = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
                if (pathForLocation != null) req.setTaskName(pathForLocation.getTask().getName());
                if (req.showDialog() == 1) {
                    DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
                    client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
                    if (node.getTask().getName().indexOf("root") < 0) {
                        client.src.client.Client client = Client.getClient();
                        try {
                            if (getCurrentTab() == 1)
                                client.rename(req.getTaskName(), node.getTask().getId(), "general");
                            else client.rename(req.getTaskName(), node.getTask().getId(), "");
                        } catch (NoSuchUserException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        if (getCurrentTab() == 1) {
                            if (pathForLocation != null) pathForLocation.renameTask(req.getTaskName());
                        } else pathForLocation.renameTask(req.getTaskName());
                        jTree.repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Root can not be renamed!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        add(item);

        client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
        if (node != null && node.getTask() != null && !node.getTask().isStopped()) {
            if (node.getTask().isBusy()) {
                item = new JMenuItem("Pause task");
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
                        if (node != null) try {
                            node.pauseTask();
                        } catch (StoppedTaskException e1) {
                            JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        jTree.repaint();
                    }
                });
                add(item);

                item = new JMenuItem("Complete task");
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) jTree.getLastSelectedPathComponent();
                        client.src.client.Client client = Client.getClient();
                        try {
                            if (getCurrentTab() == 1)
                                client.stopTask(node.getTask().getId(), "general");
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
                            if (getCurrentTab() == 1)
                                client.startTask(node.getTask().getId(), "general");
                            else client.startTask(node.getTask().getId(), "");
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
        for (int i = 0; i < this.getComponentCount(); i++) {
            final Component comp = this.getComponent(i);
            final Color col = comp.getBackground();
            comp.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    comp.setBackground(Color.LIGHT_GRAY);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    comp.setBackground(col);
                }
            });
        }
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
    }
}
