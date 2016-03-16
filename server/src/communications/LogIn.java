package server.src.communications;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Fadeev on 16.03.2016.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LogIn extends Message {
    @XmlElement(name = "login")
    private String logIn;
    @XmlElement(name = "password")
    private String password;

    public LogIn(){}

    public LogIn(String logIn, String password, String message){
        this.logIn = logIn;
        this.password = password;
        this.setMessage(message);
    }


    public String getLogIn() {
        return logIn;
    }

    public void setLogIn(String logIn) {
        this.logIn = logIn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
