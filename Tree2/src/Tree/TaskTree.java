package Tree2.src.Tree;



import sun.reflect.generics.tree.Tree;

import javax.swing.tree.DefaultTreeModel;

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

        return true;
    }

    public TaskTreeNode seekForTaskByID(int id)
    {
        return ((TaskTreeNode)this.getRoot()).seekForTaskByID(id);
    }
}

