package Tree2.UI;


import Tree2.src.Exceptions.BusyTaskException;
import UI.Authorization;
import UI.RequestTaskName;
import client.src.client.Client;
import client.src.client.exception.NoSuchUserException;
import client.src.tree.TaskTree;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.rmi.UnmarshalException;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

public class TabbedPaneExample extends JFrame {
    private RequestTaskName req = new RequestTaskName();
    private JTabbedPane tabbedPane;
    private JTree tree;
    private TaskMenu nodeMenu;
    private JMenuBar menu;
    private JFrame statistic;


    private void makeTree(String treeName) throws IOException, NoSuchUserException {
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                nodeMenu.setCurrentTab(tabbedPane.getSelectedIndex());
            }
        });
        client.src.client.Client client = Client.getClient();
        client.src.tree.TaskTree model = null;
        try {


            model = client.getTree(treeName);
        } catch (NoSuchUserException e) {
            System.out.println(e.getMessage());
        } catch (UnmarshalException e) {
            System.out.println(e.getMessage());
        }

        //create the tree by passing in the root node
        tree = new JTree(model);
        tree.setBackground(Color.GRAY);
    }

    private void updateTree() {
        //tabbedPane.removeAll();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        //for (int i = 0; i < root.getChildCount(); i++) {
        JPanel panel = new JPanel();
        JTree jTree = new JTree(root, true);
        jTree.setLocation(panel.getLocation());
        jTree.setSize(240, 160);
        panel.setLayout(new FlowLayout(0));
        panel.add(jTree);
        panel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(panel);
        TaskTree model = (TaskTree) tree.getModel();

        tabbedPane.addTab(model.getUser().getName(), scrollPane);
        jTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (nodeMenu != null) nodeMenu.setVisible(false);
                try {
                    nodeMenu = new TaskMenu(jTree);

                } catch (NullPointerException e1) {
                    System.out.println(e1.getMessage());
                }
                nodeMenu.setLocation(MouseInfo.getPointerInfo().getLocation());
                nodeMenu.setVisible(true);
                int a = tabbedPane.getSelectedIndex();
                nodeMenu.setCurrentTab(tabbedPane.getSelectedIndex());

            }
        });
        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (nodeMenu != null) nodeMenu.setVisible(false);
                jTree.clearSelection();
            }
        });
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (nodeMenu != null) nodeMenu.setVisible(false);
                jTree.clearSelection();
            }
        });
        //}

    }

    private void makeMenu() {
        menu = new JMenuBar();
        JMenu menuAdd = new JMenu("Add task");
        JMenu menuStat = new JMenu("Statistics");
        JMenu menuUser = new JMenu("Change user");
        JMenu menuLogin = new JMenu("Log In");
        JMenu menuLogout = new JMenu("Log Out");

        menuAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (req.showDialog() == 1) {
                    client.src.tree.TaskTreeNode newChild = null;
                    try {
                        newChild = client.src.tree.TaskTreeNode.getInstance(req.getTaskName());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                    client.src.tree.TaskTreeNode node = (client.src.tree.TaskTreeNode) tree.getLastSelectedPathComponent();
                    client.src.client.Client client = Client.getClient();
                    try {
                        if (tabbedPane.getSelectedIndex() == 1) {
                            client.addTask(newChild, node.getTask().getId(), "general");
                        } else client.addTask(newChild, node.getTask().getId(), "");
                    } catch (NoSuchUserException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (node != null) model.insertNodeInto(newChild, node, node.getChildCount());
                    tree.repaint();
                }
            }
        });


        menuStat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                client.src.info.Statistic stat = null;
                try {
                    client.src.tree.TaskTree client = null;
                    int a = tabbedPane.getSelectedIndex();
                    if (tabbedPane.getSelectedIndex() == 1) client = Client.getClient().getTree("general");
                    else client = Client.getClient().getTree("");
                    stat = client.getStatistic();
                } catch (BusyTaskException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);//TODO exception
                } catch (server.src.Exceptions.BusyTaskException e1) {
                    e1.printStackTrace();
                } catch (NoSuchUserException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (statistic != null) statistic.dispose();
                statistic = new StatisticWindow(stat);
                statistic.setVisible(true);
            }
        });
        menuUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //dispose(); //TODO client
                Authorization dialog = new Authorization();
                dialog.pack();
                dialog.setVisible(true);
                String login = dialog.getTextFieldLogin().getText();
                setTitle("Task tracker" + " | " + login);
                try {
                    tabbedPane.removeAll();
                    makeTree("");
                    updateTree();
                    makeTree("general");
                    updateTree();
                    makeMenu();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (NoSuchUserException e1) {
                    e1.printStackTrace();
                }

                //System.exit(0);
            }
        });


        menuLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Client client = Client.getClient();
                client.LogOut();
                tabbedPane.removeAll();
                setTitle("Task tracker");

            }
        });
        //menu.add(menuAdd);
        menu.add(menuStat);
        menu.add(menuUser);
        menu.add(menuLogout);
        setJMenuBar(menu);
    }

    public TabbedPaneExample() throws IOException, NoSuchUserException {
        setTitle("Task Tracker");
        setBackground(Color.gray);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        getContentPane().add(topPanel);
        tabbedPane = new JTabbedPane();
        Thread thread = new Thread() {

            public void run() {
                Client client = Client.getClient();
                while (true) {
                    if (client.isRefreshGeneralTree()) {
                        client.setIsRefreshGeneralTree(false);
                        String[] title = getTitle().split(" ");
                        setTitle(title[0] + " " + title[1] + " " + title[2] + " " + title[3] + " | " + "General tree was refresh at " + new Date());
                        System.out.println("Tree was refresh!");
                    }

                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        thread.setDaemon(true);
        thread.start();
        try {
            Authorization dialog = new Authorization();
            dialog.pack();
            dialog.setVisible(true);
            String login = dialog.getTextFieldLogin().getText();
            setTitle("Task tracker" + " | " + login);
            makeTree("");
            updateTree();
            makeTree("general");
            updateTree();
            makeMenu();
            //updateTree(); //TODO client
        } catch (Exception e) {
            e.printStackTrace();
        }

        topPanel.add(tabbedPane, BorderLayout.CENTER);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (nodeMenu != null) {
                    nodeMenu.setVisible(false);
                }
                dispose();
                System.exit(0);
            }
        });
    }

    // Main method to get things started
    public static void main(String args[]) {
        // Create an instance of the test application
        TabbedPaneExample mainFrame = null;
        try {
            mainFrame = new TabbedPaneExample();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setSize(new Dimension(800, 600));
        mainFrame.setMinimumSize(new Dimension(640, 480));
        mainFrame.setVisible(true);
    }
}
