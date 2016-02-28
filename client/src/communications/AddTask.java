package client.src.communications;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AddTask extends Message {

    @XmlElement(name = "parent")
    private String parent;

    @XmlElement(name = "userObject")
    private String userObject;

    @XmlElement(name = "name")
    private String name;

    public AddTask() {
        this.parent = "root";
        this.userObject = "root";


    }

    public AddTask(String data, String parent, String name) {
        this.parent = parent;
        this.userObject = data;
        this.name = name;
    }

    public AddTask(String data, String parent, String name, String message) {
        this.parent = parent;
        this.userObject = data;
        this.name = name;
        super.setMessage(message);
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getUserObject() {
        return userObject;
    }

    public void setUserObject(String userObject) {
        this.userObject = userObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}