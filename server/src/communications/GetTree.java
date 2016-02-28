package server.src.communications;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Fadeev on 24.02.2016.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GetTree extends Message {
    @XmlElement(name = "nameTree")
    private String nameTree;

    public GetTree(){}

    public GetTree(String nameTree){
        this.nameTree = nameTree;
    }


    public String getNameTree() {
        return nameTree;
    }

    public void setNameTree(String nameTree) {
        this.nameTree = nameTree;
    }
}
