package client.src.communications;



import client.src.info.Task;
import client.src.tree.TaskTreeNode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AddTask extends Message {

    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "userObject")
    private TaskTreeNode userObject;

    @XmlElement(name = "treeName")
    private String treeName;

    public AddTask() throws IOException {
        this.userObject = TaskTreeNode.getInstance("root");


    }

    public AddTask(TaskTreeNode data, int id, String name) {
        this.userObject = data;
        this.treeName = name;
        this.id = id;
    }

    public AddTask(TaskTreeNode data, int id, String name, String message) {
        this.userObject = data;
        this.treeName = name;
        this.id = id;
        super.setMessage(message);
    }


    public TaskTreeNode getUserObject() {
        return userObject;
    }

    public void setUserObject(TaskTreeNode userObject) {
        this.userObject = userObject;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}