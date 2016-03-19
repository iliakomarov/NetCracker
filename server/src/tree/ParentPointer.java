package server.src.tree;

/**
 * Created by Fadeev on 19.03.2016.
 */
public class ParentPointer {
    private int parentID;
    private boolean allowsChildren;
    private Object userObject;
    private TaskTreeNode node;


    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public boolean isAllowsChildren() {
        return allowsChildren;
    }

    public void setAllowsChildren(boolean allowsChildren) {
        this.allowsChildren = allowsChildren;
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public TaskTreeNode getNode() {
        return node;
    }

    public void setNode(TaskTreeNode node) {
        this.node = node;
    }
}
