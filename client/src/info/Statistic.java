package client.src.info;

import client.src.tree.TaskTree;
import client.src.tree.TaskTreeNode;
import server.src.Exceptions.BusyTaskException;

import javax.swing.tree.MutableTreeNode;
import java.util.*;

/**
 * Created by Ñòåïàí on 11.11.2015.
 */
public class Statistic {
    private ArrayList<Info> info;
    private int paramsCount;

    public Statistic(TaskTree tree) throws BusyTaskException, Tree2.src.Exceptions.BusyTaskException {
        info = new ArrayList<>();
        MutableTreeNode root = (MutableTreeNode) tree.getRoot();
        LinkedList q = new LinkedList();
        Enumeration en = root.children();
        while (en.hasMoreElements()) {
            q.addFirst(en.nextElement());
        }
        while (!q.isEmpty()) {
            TaskTreeNode n = (TaskTreeNode) q.removeFirst();
            Info fullInfo = n.getFullInfo();
            paramsCount = fullInfo.getLength();
            String s="";
            for(int i=0;i<n.getLevel()-1;i++) s+="   ";
            fullInfo.setInfoAt(0,s+fullInfo.getInfoAt(0).toString());
            info.add(fullInfo);
            final int childCount = n.getChildCount();
            for (int i = 0; i < childCount; i++) {
                q.addFirst(n.getChildAt(childCount-i-1));
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
