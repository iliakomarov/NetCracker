package Tree2.src.Tree;


import Tree2.src.Exceptions.*;
import Tree2.src.Info.Info;
import Tree2.src.Info.InfoAvailable;
import Tree2.src.Info.Task;

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

    public TaskTreeNode(String taskName) {
        super(Task.getInstance(taskName));
    }

    public TaskTreeNode(User user) {
        super(user);
    }

    public static TaskTreeNode getInstance(String taskName) {
        return new TaskTreeNode(taskName);
    }

    public Task getTask() {
        if (userObject instanceof Task) return (Task) userObject;
        throw new NoTaskException();
    }

    public Info getSimpleInfo()
    {
        return ((InfoAvailable) userObject).getSimpleInfo();
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
        if (getChildCount() > 0) {
            for (Object child : children) {
                TaskTreeNode node = (TaskTreeNode) child;
                String name = node.getTask().getName();
                if (name.compareTo(taskname) == 0) throw new WrongNameException();
            }
        }
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

    public boolean renameTask(String name) {
        return getTask().changeName(name);
    }

    public TaskTreeNode seekForTaskByID(int info) {
        Queue q = new LinkedList();
        q.add(this);
        while (!q.isEmpty()) {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) q.remove();
            if (n instanceof TaskTreeNode) {
                TaskTreeNode f = (TaskTreeNode) n;
                if (f.getTask().getId() == info) {
                    return f;
                }
            }
            for (int i = 0; i < n.getChildCount(); i++) {
                q.add(n.getChildAt(i));
            }
        }
        throw new NoSuchTaskWithIDException();
    }
}

