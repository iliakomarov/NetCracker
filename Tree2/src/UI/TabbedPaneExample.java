package Tree2.src.UI;

import Tree2.src.Tree.TaskTree;
import Tree2.src.Tree.User;
import client.Client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

public class TabbedPaneExample extends JFrame
{
    private static 	JTabbedPane tabbedPane;
    private JTree tree;
    private static TaskMenu nodeMenu;
    private static client.src.client.Client client = null;

    public static client.src.client.Client getClient(){
        if (client == null) return client = new client.src.client.Client();
        else return client;
    }

    private void makeTree()
    {

        //TaskTree model=new TaskTree(new User());

        client.src.client.Client cl = getClient();
        TaskTree model=new TaskTree(cl.getTree("Ilya"));

        //create the tree by passing in the root node
        tree = new JTree(model);

       /* tree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                    Rectangle pathBounds = tree.getUI().getPathBounds(tree, path);
                    if (pathBounds != null && pathBounds.contains(e.getX(), e.getY())) {
                        JPopupMenu menu = new JPopupMenu();
                        menu.add(new JMenuItem("Test"));
                        menu.show(tree, pathBounds.x, pathBounds.y + pathBounds.height);
                    }
                }
            }
        });*/

        tree.setBackground(Color.GRAY);
    }

    public static JPanel topPanel = new JPanel();

    public TabbedPaneExample()
    {
        setTitle( "Task Tree" );
        setSize( 800, 600 );
        setBackground( Color.gray );


        topPanel.setLayout( new BorderLayout() );
        getContentPane().add( topPanel );

        tabbedPane = new JTabbedPane();
        makeTree();
        refreshTree(tree);
        topPanel.add( tabbedPane, BorderLayout.CENTER );

        /*setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                nodeMenu.setVisible(false);
                dispose();
            }
        });*/
    }

    public static void refreshTree(JTree tree){
        if (tabbedPane.getTabCount() > 0) tabbedPane.remove(0);

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


    }

    // Main method to get things started
    public static void main( String args[] )
    {
        // Create an instance of the test application
        TabbedPaneExample mainFrame	= new TabbedPaneExample();
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo ( null );
        mainFrame.setSize(new Dimension(800, 600));
        mainFrame.setVisible( true );
    }
}