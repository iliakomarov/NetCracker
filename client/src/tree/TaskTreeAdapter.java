package client.src.tree;

import server.src.tree.*;
import server.src.tree.AdaptedTaskTree;

import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by Fadeev on 13.03.2016.
 */
public class TaskTreeAdapter extends XmlAdapter<client.src.tree.AdaptedTaskTree, TaskTree> {

    @Override
    public TaskTree unmarshal(client.src.tree.AdaptedTaskTree adaptedTaskTree) throws Exception {
        return new TaskTree((DefaultMutableTreeNode)adaptedTaskTree.getRoot());
    }

    @Override
    public client.src.tree.AdaptedTaskTree marshal(TaskTree taskTree) throws Exception {
        client.src.tree.AdaptedTaskTree adaptedCustomer = new client.src.tree.AdaptedTaskTree();
        adaptedCustomer.setRoot((DefaultMutableTreeNode) taskTree.getRoot());
        adaptedCustomer.setAsksAllowsChildren(taskTree.getAsksAllowsChildren());
        adaptedCustomer.setListenerList(taskTree.getListenerList());
        return adaptedCustomer;
    }

}
