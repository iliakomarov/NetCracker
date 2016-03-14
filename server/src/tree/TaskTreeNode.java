package server.src.tree;





import server.src.Exceptions.BusyTaskException;
import server.src.Exceptions.NoSuchTaskWithIDException;
import server.src.Exceptions.StoppedTaskException;
import server.src.info.Info;
import server.src.info.Task;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.lang.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

/**
 * Created by Степан on 11.11.2015.
 */

/*

 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Task.class, Vector.class, DefaultMutableTreeNode.class})
public class TaskTreeNode extends DefaultMutableTreeNode{

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

    public TaskTreeNode getParent(){
        return (TaskTreeNode)parent;
    }

    public Info getSimpleInfo()
    {
        return getTask().getSimpleInfo();
    }

    public Info getFullInfo() throws BusyTaskException, server.src.Exceptions.BusyTaskException {
        return getTask().getFullInfo();
    }

    public TaskTreeNode(){

    }

    public boolean changeTaskName(String taskname) {
        return getTask().changeName(taskname);
    }

    public long getWorkingTime() throws BusyTaskException, server.src.Exceptions.BusyTaskException {
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
            DefaultMutableTreeNode n = (DefaultMutableTreeNode)q.remove();
            if (n instanceof TaskTreeNode) {
                TaskTreeNode f=(TaskTreeNode)n;
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

    public void setParent(TaskTreeNode parent) {
        this.parent = parent;
    }


    @XmlElementWrapper(name = "children")
    @XmlElement(name = "child")
    public Vector getChildren(){
        return children;
    }

    public void setChildren(Vector children){
        this.children = children;
    }

}

