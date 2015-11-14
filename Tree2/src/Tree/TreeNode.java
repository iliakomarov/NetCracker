package Tree;
import Info.Task;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;
import java.lang.*;
import java.util.*;
/**
 * Created by Степан on 11.11.2015.
 */

/*

 */
public class TreeNode extends DefaultMutableTreeNode{
    private int id;
    private Task task;

    private TreeNode(int id, String taskName)
    {
        this.id=id;
        this.task=Task.newTask(taskName);
    }

    public static TreeNode newTreeNode(int id, String taskName)
    {
        return new TreeNode(id, taskName);
    }
}

