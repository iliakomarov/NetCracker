package UI;
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
    private	JPanel panel1;
    private JTree tree;
    private JPopupMenu menu=new JPopupMenu();

    private void makeTree()
    {
        //create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        //create the child nodes
        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
        vegetableNode.add(new DefaultMutableTreeNode("Capsicum"));
        vegetableNode.add(new DefaultMutableTreeNode("Carrot"));
        vegetableNode.add(new DefaultMutableTreeNode("Tomato"));
        vegetableNode.add(new DefaultMutableTreeNode("Potato"));

        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
        fruitNode.add(new DefaultMutableTreeNode("Banana"));
        fruitNode.add(new DefaultMutableTreeNode("Mango"));
        fruitNode.add(new DefaultMutableTreeNode("Apple"));
        fruitNode.add(new DefaultMutableTreeNode("Grapes"));
        fruitNode.add(new DefaultMutableTreeNode("Orange"));
        fruitNode.add(new DefaultMutableTreeNode("Apple"));
        fruitNode.add(new DefaultMutableTreeNode("Grapes"));
        fruitNode.add(new DefaultMutableTreeNode("Orange"));


        //add the child nodes to the root node
        root.add(vegetableNode);
        root.add(fruitNode);

        //create the tree by passing in the root node
        tree = new JTree(root);
    }

    public TabbedPaneExample()
    {
        // NOTE: to reduce the amount of code in this example, it uses
        // panels with a NULL layout.  This is NOT suitable for
        // production code since it may not display correctly for
        // a look-and-feel.

        setTitle( "Tabbed Pane Application" );
        setSize( 300, 200 );
        setBackground( Color.gray );

        JPanel topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout() );
        getContentPane().add( topPanel );

        // Create a tabbed pane
        tabbedPane = new JTabbedPane();
        makeTree();
        TreeModel model=tree.getModel();
        TreeNode root=(DefaultMutableTreeNode)model.getRoot();
        for (int i=0;i<root.getChildCount();i++) {
            JPanel panel1=new JPanel();
            JTree jTree = new JTree(root.getChildAt(i), true);
            jTree.setLocation(panel1.getLocation());
            panel1.add(jTree, BorderLayout.EAST);
            tabbedPane.addTab("Page", new JScrollPane(panel1));
            jTree.addTreeSelectionListener(new TreeSelectionListener() {
                ArrayList<JMenuItem> items;
                private void fillItems()
                {
                    items=new ArrayList<>();

                    JMenuItem item = new JMenuItem("Add task");
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            DefaultMutableTreeNode newChild = new DefaultMutableTreeNode("Added" + Math.random());
                            Point p=MouseInfo.getPointerInfo().getLocation();
                            DefaultMutableTreeNode pathForLocation = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                            if (pathForLocation!=null) pathForLocation.add(newChild);
                            jTree.repaint();
                            menu.setVisible(false);
                        }
                    });
                    items.add(item);

                    item = new JMenuItem("Remove task");
                    items.add(item);

                    item = new JMenuItem("Start task");
                    items.add(item);

                    item = new JMenuItem("Rename task");
                    item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            DefaultMutableTreeNode newChild = new DefaultMutableTreeNode("Added" + Math.random());
                            Point p=MouseInfo.getPointerInfo().getLocation();
                            DefaultMutableTreeNode pathForLocation = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                            if (pathForLocation!=null) pathForLocation.setUserObject(Math.random());
                            jTree.repaint();
                            menu.setVisible(false);
                        }
                    });
                    items.add(item);

                }
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    menu.setVisible(false);
                    menu=new JPopupMenu();
                    fillItems();
                    for (JMenuItem item: items)
                        menu.add(item);
                    menu.setLocation(MouseInfo.getPointerInfo().getLocation());
                    menu.setVisible(true);
                }
            });
        }
        topPanel.add( tabbedPane, BorderLayout.CENTER );

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menu.setVisible(false);
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