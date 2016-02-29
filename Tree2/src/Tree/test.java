package Tree2.src.Tree;

import Tree2.src.Info.Task;

import java.util.Enumeration;

/**
 * Created by Ilia Komarov on 14.02.2016.
 */
public class test {
    public static void main(String[] args)
    {
        try {
            TaskTreeNode node = TaskTreeNode.getInstance("Learning");
            node.startTask();
            node.addSubtask("Math");
            node.addSubtask("Physics");
            Thread.currentThread().sleep(1000);
            node.pauseTask();
            node.startTask();
            Thread.currentThread().sleep(500);
            node.stopTask();
            System.out.println(node.getFullInfo());
            Enumeration children = node.children();
            for (int i=0;i<node.getChildCount();i++)
            {
                TaskTreeNode nod=(TaskTreeNode)(children.nextElement());
                Task task=nod.getTask();
                System.out.println(task.getFullInfo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
