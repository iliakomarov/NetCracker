package server.src.tree;

import javax.swing.event.EventListenerList;
import javax.swing.tree.TreeNode;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Fadeev on 13.03.2016.
 */
public class AdaptedTaskTree {
    private TreeNode root;
    private EventListenerList listenerList;
    private boolean asksAllowsChildren;


    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
    public EventListenerList getListenerList() {
        return listenerList;
    }

    public void setListenerList(EventListenerList listenerList) {
        this.listenerList = listenerList;
    }
    public boolean isAsksAllowsChildren() {
        return asksAllowsChildren;
    }

    public void setAsksAllowsChildren(boolean asksAllowsChildren) {
        this.asksAllowsChildren = asksAllowsChildren;
    }
}
