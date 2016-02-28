package client.src.communications;

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
    @XmlElement(name = "nodeName")
    private String nodeName;


    @XmlElement(name = "name")
    private String name;

    public DeleteTask() {
        this.nodeName = "root";

    }

    public DeleteTask(String nodeName, String name, String message) {
        this.nodeName = nodeName;
        this.name = name;
        super.setMessage(message);
    }


    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
