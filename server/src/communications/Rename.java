package server.src.communications;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Fadeev on 21.03.2016.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Rename extends Message {
    @XmlElement(name = "newName")
    private String newName;

    @XmlElement(name = "taskId")
    private int taskId;

    @XmlElement(name = "treeName")
    private String treeName;

    public Rename(String newName, int taskId, String treeName, String message){
        this.newName = newName;
        this.taskId = taskId;
        this.treeName = treeName;
        this.setMessage(message);
    }

    public Rename(){}

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }
}
