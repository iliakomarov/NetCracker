package client.src.tree;







import Tree2.src.Exceptions.BusyTaskException;
import Tree2.src.Exceptions.NoSuchTaskWithIDException;
import Tree2.src.Exceptions.StoppedTaskException;
import client.src.info.Info;
import client.src.info.Task;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.xml.bind.annotation.*;
import java.io.IOException;
import java.io.Serializable;
import java.lang.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

/**
 * Created by Ñòåïàí on 11.11.2015.
 */

/*

 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Task.class, Vector.class, DefaultMutableTreeNode.class})
public class TaskTreeNode extends DefaultMutableTreeNode{

    @XmlElement(name = "parentID")
    private int parentID;

    private TaskTreeNode(String taskName) throws IOException {
        super(Task.getInstance(taskName));

    }

    public TaskTreeNode(client.src.tree.User user)
    {
        super(user);
    }

    public static TaskTreeNode getInstance(String taskName) throws IOException {

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

    public Info getFullInfo() throws BusyTaskException, BusyTaskException {
        Info fullInfo = getTask().getFullInfo();

        long time=getWorkingTime();
        long ms = time % 1000;
        time = time / 1000;
        long sec = time % 60;
        time= time / 60;
        long min = time % 60;
        time = time / 60;
        long hours = time % 24;
        time = time / 24;
        long days = time;
        String t=new String(days + "d " + hours + "h " + min + "m " + sec + "s " + ms + "ms");

        fullInfo.setInfoAt(fullInfo.getLength(),t);
        return fullInfo;
    }

    public TaskTreeNode(){
    }

    public boolean changeTaskName(String taskname) {
        return getTask().changeName(taskname);
    }

    public long getWorkingTime() throws BusyTaskException, BusyTaskException {
        long time = getTask().getWorkingTime();
        for (Object child : children) {
            TaskTreeNode node = (TaskTreeNode) child;
            Task task = node.getTask();
            time += task.getWorkingTime();
        }
        return time;
    }

    public boolean addSubtask(String taskname) throws IOException {
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
            if (n instanceof server.src.tree.TaskTreeNode) {
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

    public TaskTreeNode seekForTaskByName(String name) {
        Queue q = new LinkedList();
        q.add(this);
        while (!q.isEmpty()) {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode)q.remove();
            if (n instanceof server.src.tree.TaskTreeNode) {
                TaskTreeNode f=(TaskTreeNode)n;
                if (f.getTask().getName().equals(name)) {
                    return f;
                }
            }
            for (int i = 0; i < n.getChildCount(); i++) {
                q.add(n.getChildAt(i));
            }
        }
        throw new NoSuchTaskWithIDException();
    }

    public void setParent(server.src.tree.TaskTreeNode parent) {
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

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public boolean renameTask(String name) {
        return getTask().changeName(name);
    }
}

