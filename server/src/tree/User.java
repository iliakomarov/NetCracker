package server.src.tree;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import server.src.info.Task;
import server.src.loader.TreeLoader;
import server.src.server.Marshall;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Ilia Komarov on 23.02.2016.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(TaskTree.class)
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

    public static User logIn(String login, String password) throws ParserConfigurationException, SAXException, IOException, JAXBException {
        int treeIndex = 0;
        User user = null;
        Document document = TreeLoader.getDocument("server\\src\\trees.xml");
        NodeList treeList = document.getElementsByTagName("taskTree");
        Node node = TreeLoader.findTreeByUserLogin(treeList, login);
        for (int i = 0; i < treeList.getLength(); i++) {
            if (treeList.item(i) == node) {
                treeIndex = i;
            }
        }

        Element element = (Element) document.getElementsByTagName("taskTree").item(treeIndex);

        String xml = TreeLoader.nodeToString(element);

        Marshall marshall = new Marshall();

        TaskTree tree = (TaskTree)marshall.unmarshall(xml, TaskTree.class);
        user = tree.getUser();

        if (user.getPassword().equals(password)){
            return user;
        }

        return null;

    }


    public static boolean Registration(String name, String surname, String login, String password) throws ParserConfigurationException, SAXException, IOException {
        TaskTree tree = new TaskTree(TaskTreeNode.getInstance("root"));
        User user = new User(name, surname, login, password);
        tree.setUser(user);
        Document document = TreeLoader.getDocument("server\\src\\trees.xml");
        TreeLoader.addTree(tree, name, document);

        return true;
    }


}
