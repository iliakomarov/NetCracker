package Tree;

import Info.*;

import javax.swing.tree.*;

/**
 * Created by Степан on 09.11.2015.
 */
public class TaskTree extends DefaultTreeModel {

    public TaskTree(TaskTreeNode root) {
        super(root);
    }

    public TaskTree(User user) { this(new TaskTreeNode(user)); }

    public boolean addTask(String taskname)
    {
        ((TaskTreeNode)this.getRoot()).addSubtask(taskname);
    }

    public TaskTreeNode seekForTaskByID(int id)
    {
        return ((TaskTreeNode)this.getRoot()).seekForTaskByID(id);
    }
}

