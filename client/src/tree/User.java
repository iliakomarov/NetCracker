package client.src.tree;




import client.src.info.Task;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.bind.annotation.*;
import java.util.Vector;

/**
 * Created by Ilia Komarov on 23.02.2016.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(Task.class)
public class User {
    @XmlElement
    private String name;
    @XmlElement
    private String surname;
    @XmlElement
    private String login;
    @XmlElement
    private String password;
    //@XmlElement
    //private TaskTree privateTree;

    public User() {
        this("def", "def", "def", "def");
    }

    public User(String name, String surname, String login, String password) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        //this.privateTree = new TaskTree(this);
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

    /*public TaskTree getPrivateTree() {
        return privateTree;
    }

    public void setPrivateTree(TaskTree privateTree) {
        this.privateTree = privateTree;
    }*/
}
