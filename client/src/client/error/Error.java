package client.src.client.error;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Fadeev on 17.11.2015.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Error {

    @XmlElement(name = "type")
    private String type;
    @XmlElement(name = "message")
    private String message;

    public Error(){}

    public Error(String type, String message){
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        return "Error(" + getType() + ", " + getMessage() + ")";
    }

}
