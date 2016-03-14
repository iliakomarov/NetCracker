package server.src.communications;


import client.src.communications.Message;
import server.src.info.Task;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AddTask extends Message {

    @XmlElement(name = "parent")
    private Task parent;

    @XmlElement(name = "userObject")
    private Task userObject;

    @XmlElement(name = "treeName")
    private String treeName;

    public AddTask() {
        this.parent = null;
        this.userObject = Task.getInstance("root");


    }

    public AddTask(Task data, Task parent, String name) {
        this.parent = parent;
        this.userObject = data;
        this.treeName = name;
    }

    public AddTask(Task data, Task parent, String name, String message) {
        this.parent = parent;
        this.userObject = data;
        this.treeName = name;
        super.setMessage(message);
    }


    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

    public Task getUserObject() {
        return userObject;
    }

    public void setUserObject(Task userObject) {
        this.userObject = userObject;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }
}