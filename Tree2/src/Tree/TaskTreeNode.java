package Tree2.src.Tree;

import Tree2.src.Exceptions.BusyTaskException;
import Tree2.src.Exceptions.NoSuchTaskWithIDException;
import Tree2.src.Exceptions.StoppedTaskException;
import Tree2.src.Info.Info;
import Tree2.src.Info.Task;
import sun.reflect.generics.tree.Tree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Степан on 11.11.2015.
 */

/*

 */
public class TaskTreeNode extends DefaultMutableTreeNode {

    private TaskTreeNode(String taskName) {
        super(Task.getInstance(taskName));
    }

    public TaskTreeNode(User user)
    {
        super(user);
    }

    public static TaskTreeNode getInstance(String taskName) {
        return new TaskTreeNode(taskName);
    }

    public Task getTask() {
        return (Task)userObject;
    }

    public Info getSimpleInfo()
    {
        return getTask().getSimpleInfo();
    }

    public Info getFullInfo() throws BusyTaskException {
        return getTask().getFullInfo();
    }

    public boolean changeTaskName(String taskname) {
        return getTask().changeName(taskname);
    }

    public long getWorkingTime() throws BusyTaskException {
        long time = getTask().getWorkingTime();
        for (Object child : children) {
            TaskTreeNode node = (TaskTreeNode) child;
            Task task = node.getTask();
            time += task.getWorkingTime();
        }
        return time;
    }

    public boolean addSubtask(String taskname) {
        add(TaskTreeNode.getInstance(taskname));
        return true;
    }

    public boolean removeTask() throws StoppedTaskException {
        getTask().checkStop();
        removeFromParent();
        return true;
    }

    public boolean startTask() throws StoppedTaskException {
        return getTask().startTask();
    }

    public boolean pauseTask() throws StoppedTaskException {
        return getTask().pauseTask();
    }

    public boolean stopTask() throws StoppedTaskException {
        return getTask().stopTask();
    }

    public TaskTreeNode seekForTaskByID(int info) {
        Queue q = new LinkedList();
        q.add(this);
        while (!q.isEmpty()) {
            TaskTreeNode n = (TaskTreeNode) q.remove();
            if (n.getTask().getId() == info) {
                return n;
            }
            for (int i=0;i<n.getChildCount();i++) {
                q.add(n.getChildAt(i));
            }
        }
        throw new NoSuchTaskWithIDException();
    }
}

