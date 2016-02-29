package Tree;

import Exceptions.NoSuchTaskWithIDException;
import Info.*;

import javax.swing.tree.*;

/**
 * Created by Степан on 09.11.2015.
 */
public class TaskTree extends DefaultTreeModel {

    public TaskTree(DefaultMutableTreeNode root) {
        super(root);
    }

    public TaskTree(User user) { this(new DefaultMutableTreeNode(user)); }

    public boolean addTask(String taskname)
    {
        ((MutableTreeNode)this.getRoot()).insert(TaskTreeNode.getInstance(taskname),0);
        //return ((TaskTreeNode)this.getRoot()).addSubtask(taskname);
        return true;
    }

    public TaskTreeNode seekForTaskByID(int id)
    {
        MutableTreeNode root=(MutableTreeNode)this.getRoot();
        for (int i=0;i<root.getChildCount(); i++)
        {
            TaskTreeNode child= (TaskTreeNode) root.getChildAt(i);
            try
            {
                return child.seekForTaskByID(id);
            }
            catch(NoSuchTaskWithIDException ex) {}
        }
        throw new NoSuchTaskWithIDException();
    }
}

