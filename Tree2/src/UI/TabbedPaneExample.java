package UI;
import Tree.TaskTree;
import Tree.User;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

public class TabbedPaneExample extends JFrame
{
    private	JTabbedPane tabbedPane;
    private JTree tree;
    private TaskMenu nodeMenu;

    private void makeTree()
    {
        TaskTree model=new TaskTree(new User());
        model.addTask("Math");
        model.addTask("Physics");
        model.addTask("Programming");
        model.seekForTaskByID(0).addSubtask("Algebra");
        model.seekForTaskByID(0).addSubtask("Geometry");
        model.seekForTaskByID(1).addSubtask("Hydrodynamics");
        model.seekForTaskByID(1).addSubtask("Electricity");
        model.seekForTaskByID(1).addSubtask("Mechanics");
        model.seekForTaskByID(2).addSubtask("Java");
        model.seekForTaskByID(2).addSubtask("SQL");
        model.seekForTaskByID(2).addSubtask("C++");
        model.seekForTaskByID(2).addSubtask("Scilab");
        //create the tree by passing in the root node
        tree = new JTree(model);
        tree.setBackground(Color.GRAY);
    }

    public TabbedPaneExample()
    {
        setTitle( "Task Tree" );
        setSize( 800, 600 );
        setBackground( Color.gray );

        JPanel topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout() );
        getContentPane().add( topPanel );

        tabbedPane = new JTabbedPane();
        makeTree();
        DefaultMutableTreeNode root=(DefaultMutableTreeNode)tree.getModel().getRoot();

        for (int i=0;i<root.getChildCount();i++) {
            JPanel panel=new JPanel();
            JTree jTree = new JTree(root.getChildAt(i), true);
            jTree.setLocation(panel.getLocation());
            panel.setLayout(new FlowLayout(0));
            panel.add(jTree);
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(panel);
            tabbedPane.addTab("Task", scrollPane);
            jTree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    if (nodeMenu!=null) nodeMenu.setVisible(false);
                    nodeMenu = new TaskMenu(jTree);
                    nodeMenu.setLocation(MouseInfo.getPointerInfo().getLocation());
                    nodeMenu.setVisible(true);
                }
            });
        }
        topPanel.add( tabbedPane, BorderLayout.CENTER );

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                nodeMenu.setVisible(false);
                dispose();
            }
        });
    }

    // Main method to get things started
    public static void main( String args[] )
    {
        // Create an instance of the test application
        TabbedPaneExample mainFrame	= new TabbedPaneExample();
        mainFrame.pack();
        mainFrame.setVisible( true );
    }
}