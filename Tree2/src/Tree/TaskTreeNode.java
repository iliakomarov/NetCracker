package Tree;

import Exceptions.BusyTaskException;
import Exceptions.StoppedTaskException;
import Info.*;

import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.*;

/**
 * Created by Степан on 11.11.2015.
 */

/*

 */
public class TaskTreeNode extends DefaultMutableTreeNode {

    private TaskTreeNode(String taskName) {
        super(Task.getInstance(taskName));
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
}

