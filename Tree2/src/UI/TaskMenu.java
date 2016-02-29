package UI;

import Exceptions.StoppedTaskException;
import Tree.TaskTreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Ilia Komarov on 28.02.2016.
 */
public class TaskMenu extends JPopupMenu {

    public TaskMenu(JTree jTree) {
        RequestTaskName req = new RequestTaskName();

        JMenuItem item = new JMenuItem("Add task");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                if (req.showDialog() == 1) {
                    DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(req.getTaskName());
                    DefaultTreeModel model = (DefaultTreeModel) jTree.getModel();
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
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
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                if (node != null && node.getParent() != null) model.removeNodeFromParent(node);
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

        TaskTreeNode node = (TaskTreeNode) jTree.getLastSelectedPathComponent();
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
        }



    }
}
