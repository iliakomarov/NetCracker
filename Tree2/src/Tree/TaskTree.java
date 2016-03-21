package Tree2.src.Tree;


import Tree2.src.Exceptions.BusyTaskException;
import Tree2.src.Exceptions.NoSuchTaskWithIDException;
import Tree2.src.Info.Statistic;

import javax.swing.tree.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Степан on 09.11.2015.
 */
public class TaskTree extends DefaultTreeModel {

    public TaskTree(DefaultMutableTreeNode root) {
        super(root);
    }

    public TaskTree(User user) {
        this(new DefaultMutableTreeNode(user));
    }

    public boolean addTask(String taskname)
    {
        ((MutableTreeNode) this.getRoot()).insert(TaskTreeNode.getInstance(taskname), 0);
        //return ((TaskTreeNode)this.getRoot()).addSubtask(taskname);
        return true;
    }

    public TaskTreeNode seekForTaskByID(int id)
    {
        MutableTreeNode root = (MutableTreeNode) this.getRoot();
        for (int i = 0; i < root.getChildCount(); i++) {
            TaskTreeNode child = (TaskTreeNode) root.getChildAt(i);
            try {
                return child.seekForTaskByID(id);
            } catch (NoSuchTaskWithIDException ex) {
            }
        }
        throw new NoSuchTaskWithIDException();
    }

    public void getSimpleTree(TaskTree tree)
    {
        Queue q = new LinkedList();
        q.add(this);
        while (!q.isEmpty()) {
            TaskTreeNode n = (TaskTreeNode) q.remove();
            n.setUserObject(n.getSimpleInfo());
            for (int i = 0; i < n.getChildCount(); i++) {
                q.add(n.getChildAt(i));
            }
        }
        //TODO simple tree with simple info
    }

    public Statistic getStatistic() throws BusyTaskException {
        return new Statistic(this);
    }


}

