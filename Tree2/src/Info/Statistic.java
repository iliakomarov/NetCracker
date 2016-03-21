package Tree2.src.Info;


import Tree2.src.Exceptions.BusyTaskException;
import Tree2.src.Tree.TaskTree;
import Tree2.src.Tree.TaskTreeNode;

import javax.swing.tree.MutableTreeNode;
import java.util.*;

/**
 * Created by Степан on 11.11.2015.
 */
public class Statistic {
    private ArrayList<Info> info;
    private int paramsCount;

    public Statistic(TaskTree tree) throws BusyTaskException {
        info = new ArrayList<>();
        MutableTreeNode root = (MutableTreeNode) tree.getRoot();
        Queue q = new LinkedList();
        Enumeration en = root.children();
        while (en.hasMoreElements()) {
            q.add(en.nextElement());
        }
        while (!q.isEmpty()) {
            TaskTreeNode n = (TaskTreeNode) q.remove();
            Info fullInfo = n.getFullInfo();
            paramsCount = fullInfo.getLength();
            info.add(fullInfo);
            for (int i = 0; i < n.getChildCount(); i++) {
                q.add(n.getChildAt(i));
            }
        }
    }

    public int getSize() {
        return info.size();
    }

    public Info getInfoAt(int index) {
        return info.get(index);
    }

    public int getParamsCount() {
        return paramsCount;
    }
}
