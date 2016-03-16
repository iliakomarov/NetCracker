package client.src.communications;

import server.src.communications.Message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Fadeev on 16.03.2016.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Registration extends Message {
    @XmlElement
    private String name;
    @XmlElement
    private String surname;
    @XmlElement
    private String login;
    @XmlElement
    private String password;

    public Registration(){}

    public Registration(String name, String surname, String login, String password, String message){
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.setMessage(message);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
