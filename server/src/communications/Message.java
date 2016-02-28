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
public class Message implements Runnable {
    @XmlElement(name = "message")
    private String message;
    public  void run(){}
    public Message(){}

    public Message(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
