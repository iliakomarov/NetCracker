package server.src.communications;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Fadeev on 28.02.2016.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteTask extends Message {
    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "treeName")
    private String treeName;

    public DeleteTask(){}

    public DeleteTask(int id, String treeName, String message) {
        this.id = id;
        this.treeName = treeName;
        super.setMessage(message);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }
}
