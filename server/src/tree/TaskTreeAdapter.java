package server.src.tree;

import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by Fadeev on 13.03.2016.
 */
public class TaskTreeAdapter extends XmlAdapter<AdaptedTaskTree, TaskTree> {

    @Override
    public TaskTree unmarshal(AdaptedTaskTree adaptedTaskTree) throws Exception {
        return new TaskTree((DefaultMutableTreeNode)adaptedTaskTree.getRoot());
    }

    @Override
    public AdaptedTaskTree marshal(TaskTree taskTree) throws Exception {
        AdaptedTaskTree adaptedCustomer = new AdaptedTaskTree();
        adaptedCustomer.setRoot((DefaultMutableTreeNode) taskTree.getRoot());
        adaptedCustomer.setAsksAllowsChildren(taskTree.getAsksAllowsChildren());
        adaptedCustomer.setListenerList(taskTree.getListenerList());
        return adaptedCustomer;
    }

}
