package server.src.tree;


import server.src.info.Task;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Степан on 11.11.2015.
 */

/*

 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TreeNode extends DefaultMutableTreeNode{
    private int parentId;
    @XmlElement(name = "currParent")
    private String currNameParent;
    private Task task;

    public TreeNode(){
        super("root", true);
    }

    public TreeNode(Object o, boolean allowsChildren){
        super(o, allowsChildren);
        TreeNode treeNode = null;


    }

    public TreeNode(Object data){
        super(data);
    }

    private TreeNode(int id, String taskName)
    {
        this.parentId =id;
        this.task=Task.newTask(taskName);
    }

    public static TreeNode newTreeNode(int id, String taskName)
    {
        return new TreeNode(id, taskName);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getCurrParent() {
        return currNameParent;
    }

    public void setCurrParent(String currParent) {
        this.currNameParent = currParent;
    }
}

