package client.src.tree;

import client.src.info.Statistic;
import server.src.Exceptions.BusyTaskException;
import server.src.Exceptions.NoSuchTaskWithIDException;

import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.IOException;

/**
 * Created by Степан on 09.11.2015.
 */
@XmlRootElement
@XmlJavaTypeAdapter(TaskTreeAdapter.class)

public class TaskTree extends DefaultTreeModel {

    private User user;

    public TaskTree(DefaultMutableTreeNode root) {
        super(root);
    }

    public TaskTree(User user) {
        this(new DefaultMutableTreeNode(user));
    }

    public TaskTree() {
        super(new DefaultMutableTreeNode());
    }

    @XmlElement(name = "taskTreeNode")
    public TaskTreeNode getRoot() {
        return (TaskTreeNode) root;
    }

    public void setRoot(TaskTreeNode root) {
        this.root = root;
    }


    public EventListenerList getListenerList() {
        return listenerList;
    }

    public boolean getAsksAllowsChildren() {
        return asksAllowsChildren();
    }

    public boolean addTask(String taskname) throws IOException {
        ((MutableTreeNode) this.getRoot()).insert(TaskTreeNode.getInstance(taskname), 0);
        //return ((TaskTreeNode)this.getRoot()).addSubtask(taskname);
        return true;
    }

    public TaskTreeNode seekForTaskByID(int id) {
        MutableTreeNode root = this.getRoot();
        for (int i = 0; i < root.getChildCount(); i++) {
            TaskTreeNode child = (TaskTreeNode) root.getChildAt(i);
            try {
                return child.seekForTaskByID(id);
            } catch (NoSuchTaskWithIDException ex) {
            }
        }
        throw new NoSuchTaskWithIDException();
    }

    public TaskTreeNode seekForTaskByName(String name) {
        MutableTreeNode root = this.getRoot();
        for (int i = 0; i < root.getChildCount(); i++) {
            TaskTreeNode child = (TaskTreeNode) root.getChildAt(i);
            try {
                return child.seekForTaskByName(name);
            } catch (NoSuchTaskWithIDException ex) {
            }
        }
        throw new NoSuchTaskWithIDException();
    }

    @XmlElement
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Statistic getStatistic() throws BusyTaskException, Tree2.src.Exceptions.BusyTaskException {
        return new Statistic(this);
    }
}

