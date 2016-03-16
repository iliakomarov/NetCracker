package server.src.server.session;


import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import server.src.communications.AddTask;
import server.src.communications.DeleteTask;
import server.src.communications.GetTree;
import server.src.communications.Message;
import server.src.info.Task;
import server.src.loader.TreeLoader;
import server.src.server.Marshall;
import server.src.server.session.protocols.TTP;
import server.src.tree.TaskTree;
import server.src.tree.TaskTreeNode;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import server.src.tree.User;


import javax.jws.soap.SOAPBinding;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.Socket;

/**
 * Created by Fadeev on 12.11.2015.
 */
public class Session implements Runnable {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private TaskTree treeNode;
    private String currUser;
    private boolean isLogIn;

    public Session(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = new DataInputStream(this.socket.getInputStream());
        outputStream = new DataOutputStream(this.socket.getOutputStream());
    }

    //TODO Serialize all nodes in loop
    //TODO Create xml with trees

    @Override
    public void run() {

        Message message = null;
        try {
            while ((message = (Message) TTP.getObject(getInputStream(), Message.class)) != null) {
                Marshall marshall = new Marshall();
                System.out.println(message.getMessage());

                if (message == null) {
                    message = new Message("Illegalrequest");
                    try {
                        TTP.sendResponse(message, getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (message.getMessage().equals("getTree")) {

                    message = new Message("ok");

                    GetTree getTree = null;
                    try {
                        getTree = (GetTree) TTP.getObject(getInputStream(), GetTree.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        treeNode = TreeLoader.loadTree(getTree.getNameTree());
                        TTP.sendResponse(new Message("ok"), getOutputStream());
                        TTP.sendObject(treeNode, getOutputStream(), TaskTree.class);

                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }


                } else if (message.getMessage().equals("addTask")) {
                    AddTask addTask = null;
                    try {
                        addTask = (AddTask) TTP.getObject(getInputStream(), AddTask.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {

                        TaskTreeNode currTreeNode = null;

                        TaskTree tree = TreeLoader.loadTree(addTask.getTreeName());
                        TaskTreeNode findedNode = tree.getRoot().seekForTaskByID(addTask.getId());
                        addTask.getUserObject().setParentID(addTask.getId());
                        findedNode.add(addTask.getUserObject());
                        TreeLoader.updateTree(tree, addTask.getTreeName());

                        TTP.sendResponse(new Message("ok"), getOutputStream());

                    } catch (IllegalStateException e) {
                        try {
                            TTP.sendResponse(new Message("fail"), getOutputStream());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                } else if (message.getMessage().equals("deleteTask")) {
                    DeleteTask deleteTask = null;

                    deleteTask = (DeleteTask) TTP.getObject(getInputStream(), DeleteTask.class);

                    try {

                        TaskTree tree = TreeLoader.loadTree(deleteTask.getTreeName());
                        TaskTreeNode findedNode = tree.getRoot().seekForTaskByID(deleteTask.getId());
                        TaskTreeNode parent = tree.getRoot().seekForTaskByID(findedNode.getParentID());
                        findedNode.setParent(parent);
                        parent.remove(findedNode);
                        TreeLoader.updateTree(tree, deleteTask.getTreeName());
                        TTP.sendResponse(new Message("ok"), getOutputStream());

                    } catch (IllegalStateException e) {
                        try {
                            TTP.sendResponse(new Message("fail"), getOutputStream());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }

                }

                System.out.println(message.getMessage());
                message = null;
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }

    }


    public DataInputStream getInputStream() {
        return this.inputStream;
    }

    public DataOutputStream getOutputStream() {
        return this.outputStream;
    }

    public String getCurrUser() {
        return currUser;
    }

    public void setCurrUser(String currUser) {
        this.currUser = currUser;
    }

    public boolean isLogIn() {
        return isLogIn;
    }

    public void setIsLogIn(boolean isLogIn) {
        this.isLogIn = isLogIn;
    }
}
