package client.src.communications;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Fadeev on 08.04.2016.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GetId extends Message {
    @XmlElement(name = "id")
    private Integer id;
    public GetId(){}
    public GetId(String message){
        super(message);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
