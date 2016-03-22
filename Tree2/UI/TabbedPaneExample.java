package Tree2.UI;


import Tree2.src.Exceptions.BusyTaskException;
import Tree2.src.Info.Statistic;
import Tree2.src.Tree.TaskTree;
import Tree2.src.Tree.TaskTreeNode;
import Tree2.src.Tree.User;
import UI.Authorization;
import UI.RequestTaskName;
import client.src.client.Client;
import client.src.client.exception.NoSuchUserException;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
        client.src.client.Client client = Client.getClient();
        client.src.tree.TaskTree model = null;
        try{


            model = client.getTree(treeName);
        }
        catch (NoSuchUserException e){
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
            panel.setLayout(new BorderLayout());
            panel.add(jTree, BorderLayout.CENTER);
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(panel);
            tabbedPane.addTab("Task", scrollPane);
            jTree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    if (nodeMenu != null) nodeMenu.setVisible(false);
                    try {
                        nodeMenu = new TaskMenu(jTree);
                    }catch (NullPointerException e1){
                        System.out.println(e1.getMessage());
                    }
                    nodeMenu.setLocation(MouseInfo.getPointerInfo().getLocation());
                    nodeMenu.setVisible(true);
                }
            });
            scrollPane.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (nodeMenu != null) nodeMenu.setVisible(false);
                }
            });
            tabbedPane.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (nodeMenu != null) nodeMenu.setVisible(false);
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

        menuAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (req.showDialog() == 1) {
                    TaskTreeNode newChild = TaskTreeNode.getInstance(req.getTaskName());
                    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) model.getRoot();
                    model.insertNodeInto(newChild, node, node.getChildCount());
                }
                updateTree(); //TODO client
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
                    e1.printStackTrace();//TODO exception
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
                System.exit(0);
            }
        });


        menu.add(menuAdd);
        menu.add(menuStat);
        menu.add(menuUser);
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
                        tabbedPane.remove(1);
                        try {
                            makeTree("general");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (NoSuchUserException e) {
                            e.printStackTrace();
                        }
                        updateTree();
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

            makeTree("");
            updateTree();
            makeTree("general");
            updateTree();
            makeMenu();

            //updateTree(); //TODO client
        }
        catch (Exception e){

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