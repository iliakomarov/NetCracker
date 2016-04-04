package server.src.server.session;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;
import server.src.Exceptions.StoppedTaskException;
import server.src.Exceptions.SuchUserAlreadyExist;
import server.src.communications.*;
import server.src.loader.TreeLoader;
import server.src.server.HttpServer;
import server.src.server.Marshall;
import server.src.server.session.protocols.TTP;
import server.src.tree.TaskTree;
import server.src.tree.TaskTreeNode;
import org.xml.sax.SAXException;
import server.src.tree.User;


import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.Socket;
import java.rmi.UnmarshalException;

/**
 * Created by Fadeev on 12.11.2015.
 */
public class Session implements Runnable {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private TaskTree treeNode;
    private User currUser;
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
                } else if (message.getMessage().equals("getTree") && isLogIn) {

                    message = new Message("ok");

                    GetTree getTree = null;
                    try {
                        getTree = (GetTree) TTP.getObject(getInputStream(), GetTree.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (getTree.getNameTree().equals("general")) treeNode = TreeLoader.loadTree("general");
                        else treeNode = TreeLoader.loadTree(getCurrUser().getName());

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

                } else if (message.getMessage().equals("addTask") && isLogIn) {

                    AddTask addTask = null;
                    try {
                        addTask = (AddTask) TTP.getObject(getInputStream(), AddTask.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    try {

                        TaskTreeNode currTreeNode = null;
                        TaskTree tree = null;

                        if (addTask.getTreeName().equals("general")) tree = TreeLoader.loadTree("general");
                        else tree = TreeLoader.loadTree(getCurrUser().getName());

                        TaskTreeNode findedNode = tree.getRoot().seekForTaskByID(addTask.getId());
                        addTask.getUserObject().setParentID(addTask.getId());
                        findedNode.add(addTask.getUserObject());

                        if (addTask.getTreeName().equals("general")) TreeLoader.updateTree(tree, "general");
                        else TreeLoader.updateTree(tree, getCurrUser().getName());

                        if (addTask.getTreeName().equals("general")){
                            HttpServer.sendToAll(tree);
                        }
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

                } else if (message.getMessage().equals("deleteTask") && isLogIn) {
                    DeleteTask deleteTask = null;

                    deleteTask = (DeleteTask) TTP.getObject(getInputStream(), DeleteTask.class);

                    try {

                        TaskTree tree = null;

                        if (deleteTask.getTreeName().equals("general")) tree = TreeLoader.loadTree("general");
                        else tree = TreeLoader.loadTree(getCurrUser().getName());
                        TaskTreeNode findedNode = tree.getRoot().seekForTaskByID(deleteTask.getId());
                        TaskTreeNode parent = tree.getRoot().seekForTaskByID(findedNode.getParentID());
                        findedNode.setParent(parent);
                        parent.remove(findedNode);
                        if (deleteTask.getTreeName().equals("general")) TreeLoader.updateTree(tree, "general");
                        else TreeLoader.updateTree(tree, getCurrUser().getName());

                        if (deleteTask.getTreeName().equals("general")){
                            HttpServer.sendToAll(tree);
                        }

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

                } else if (message.getMessage().equals("login")){
                    LogIn logIn = (LogIn)TTP.getObject(getInputStream(), LogIn.class);

                    try {
                        User user = User.logIn(logIn.getLogIn(), logIn.getPassword());
                        if (user != null){
                            setCurrUser(user);
                            setIsLogIn(true);
                            TTP.sendResponse(new Message("ok"), getOutputStream());
                        }
                        else {
                            TTP.sendResponse(new Message("fail"), getOutputStream());
                        }
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }


                }else if (message.getMessage().equals("logout") && isLogIn){
                    setCurrUser(null);
                    setIsLogIn(false);
                }else if (message.getMessage().equals("registration") && !isLogIn()){
                    Registration registration = (Registration)TTP.getObject(getInputStream(), Registration.class);
                    try {
                        Document document = TreeLoader.getDocument(new File("").getAbsolutePath() + File.separator + "trees.xml");
                        NodeList treeList = document.getElementsByTagName("taskTree");

                        Node node = TreeLoader.findTreeByUserName(treeList, registration.getName());
                        Node node1 = TreeLoader.findTreeByUserLogin(treeList, registration.getLogin());

                        if (TreeLoader.findTreeByUserName(treeList, registration.getName()) != null || TreeLoader.findTreeByUserLogin(treeList, registration.getLogin()) != null) {
                            TTP.sendResponse(new Message("fail"), getOutputStream());
                        }
                        else {
                            throw new SAXException();
                        }

                    } catch (ParserConfigurationException e) {
                        User.Registration(registration.getName(), registration.getSurname(), registration.getLogin(), registration.getPassword());
                    } catch (SAXException e) {
                        User.Registration(registration.getName(), registration.getSurname(), registration.getLogin(), registration.getPassword());
                    }
                    catch (UnmarshalException e){
                        User.Registration(registration.getName(), registration.getSurname(), registration.getLogin(), registration.getPassword());
                    }


                    TTP.sendResponse(new Message("ok"), getOutputStream());
                }else if (message.getMessage().equals("registration") && isLogIn()){
                    TTP.sendResponse(new Message("logout"), getOutputStream());
                }
                else if (message.getMessage().equals("rename") && isLogIn) {

                    Rename rename = null;
                    try {
                        rename = (Rename) TTP.getObject(getInputStream(), Rename.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    try {

                        TaskTreeNode currTreeNode = null;

                        TaskTree tree = null;

                        if (rename.getTreeName().equals("general")) tree = TreeLoader.loadTree("general");
                        else tree = TreeLoader.loadTree(getCurrUser().getName());
                        TaskTreeNode findedNode = tree.getRoot().seekForTaskByID(rename.getTaskId());
                        findedNode.getTask().changeName(rename.getNewName());
                        if (rename.getTreeName().equals("general")) TreeLoader.updateTree(tree, "general");
                        else TreeLoader.updateTree(tree, getCurrUser().getName());

                        if (rename.getTreeName().equals("general")){
                            HttpServer.sendToAll(tree);
                        }
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

                }else if (message.getMessage().equals("starttask") && isLogIn) {

                    StartTask startTask = null;
                    try {
                        startTask = (StartTask) TTP.getObject(getInputStream(), StartTask.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    try {

                        TaskTreeNode currTreeNode = null;

                        TaskTree tree = null;

                        if (startTask.getTreeName().equals("general")) tree = TreeLoader.loadTree("general");
                        else tree = TreeLoader.loadTree(getCurrUser().getName());
                        TaskTreeNode findedNode = tree.getRoot().seekForTaskByID(startTask.getTaskId());
                        try {
                            findedNode.getTask().startTask();
                        } catch (StoppedTaskException e) {
                            e.printStackTrace();
                        }
                        if (startTask.getTreeName().equals("general")) TreeLoader.updateTree(tree, "general");
                        else TreeLoader.updateTree(tree, getCurrUser().getName());

                        if (startTask.getTreeName().equals("general")){
                            HttpServer.sendToAll(tree);
                        }
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

                }else if (message.getMessage().equals("stoptask") && isLogIn) {

                    StopTask stopTask = null;
                    try {
                        stopTask = (StopTask) TTP.getObject(getInputStream(), StopTask.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    try {

                        TaskTreeNode currTreeNode = null;

                        TaskTree tree = null;

                        if (stopTask.getTreeName().equals("general")) tree = TreeLoader.loadTree("general");
                        else tree = TreeLoader.loadTree(getCurrUser().getName());
                        TaskTreeNode findedNode = tree.getRoot().seekForTaskByID(stopTask.getTaskId());
                        try {
                            findedNode.getTask().stopTask();
                        } catch (StoppedTaskException e) {
                            e.printStackTrace();
                        }
                        if (stopTask.getTreeName().equals("general")) TreeLoader.updateTree(tree, "general");
                        else TreeLoader.updateTree(tree, getCurrUser().getName());

                        if (stopTask.getTreeName().equals("general")){
                            HttpServer.sendToAll(tree);
                        }
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

                }else if (message.getMessage().equals("pausetask") && isLogIn) {

                    PauseTask pauseTask = null;
                    try {
                        pauseTask = (PauseTask) TTP.getObject(getInputStream(), PauseTask.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    try {

                        TaskTreeNode currTreeNode = null;

                        TaskTree tree = null;

                        if (pauseTask.getTreeName().equals("general")) tree = TreeLoader.loadTree("general");
                        else tree = TreeLoader.loadTree(getCurrUser().getName());
                        TaskTreeNode findedNode = tree.getRoot().seekForTaskByID(pauseTask.getTaskId());
                        try {
                            findedNode.getTask().pauseTask();
                        } catch (StoppedTaskException e) {
                            e.printStackTrace();
                        }
                        if (pauseTask.getTreeName().equals("general")) TreeLoader.updateTree(tree, "general");
                        else TreeLoader.updateTree(tree, getCurrUser().getName());

                        if (pauseTask.getTreeName().equals("general")){
                            HttpServer.sendToAll(tree);
                        }
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

                }
                else if (!isLogIn){
                    TTP.sendResponse(new Message("User not found!"), getOutputStream());
                }

                System.out.println(message.getMessage());
                message = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public DataInputStream getInputStream() {
        return this.inputStream;
    }

    public DataOutputStream getOutputStream() {
        return this.outputStream;
    }

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    public boolean isLogIn() {
        return isLogIn;
    }

    public void setIsLogIn(boolean isLogIn) {
        this.isLogIn = isLogIn;
    }
}
