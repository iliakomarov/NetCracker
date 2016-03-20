package server.src.server.session;


import server.src.Exceptions.StoppedTaskException;
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
                } else if (message.getMessage().equals("getTree") && isLogIn) {

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

                } else if (message.getMessage().equals("addTask") && isLogIn) {

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

                } else if (message.getMessage().equals("login")){
                    LogIn logIn = (LogIn)TTP.getObject(getInputStream(), LogIn.class);

                    try {
                        if (User.logIn(logIn.getLogIn(), logIn.getPassword())){
                            setCurrUser(logIn.getLogIn());
                            setIsLogIn(true);
                        }
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }

                    TTP.sendResponse(new Message("ok"), getOutputStream());
                }else if (message.getMessage().equals("logout") && isLogIn){
                    setCurrUser("");
                    setIsLogIn(false);
                }else if (message.getMessage().equals("registration") && !isLogIn()){
                    Registration registration = (Registration)TTP.getObject(getInputStream(), Registration.class);
                    try {
                        User.Registration(registration.getName(), registration.getSurname(), registration.getLogin(), registration.getPassword());
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }

                    TTP.sendResponse(new Message("fail"), getOutputStream());
                }else if (message.getMessage().equals("rename") && isLogIn) {

                    Rename rename = null;
                    try {
                        rename = (Rename) TTP.getObject(getInputStream(), Rename.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                    try {

                        TaskTreeNode currTreeNode = null;

                        TaskTree tree = TreeLoader.loadTree(rename.getTreeName());
                        TaskTreeNode findedNode = tree.getRoot().seekForTaskByID(rename.getTaskId());
                        findedNode.getTask().changeName(rename.getNewName());
                        TreeLoader.updateTree(tree, rename.getTreeName());

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

                        TaskTree tree = TreeLoader.loadTree(startTask.getTreeName());
                        TaskTreeNode findedNode = tree.getRoot().seekForTaskByID(startTask.getTaskId());
                        try {
                            findedNode.getTask().startTask();
                        } catch (StoppedTaskException e) {
                            e.printStackTrace();
                        }
                        TreeLoader.updateTree(tree, startTask.getTreeName());

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

                        TaskTree tree = TreeLoader.loadTree(stopTask.getTreeName());
                        TaskTreeNode findedNode = tree.getRoot().seekForTaskByID(stopTask.getTaskId());
                        try {
                            findedNode.getTask().stopTask();
                        } catch (StoppedTaskException e) {
                            e.printStackTrace();
                        }
                        TreeLoader.updateTree(tree, stopTask.getTreeName());

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

                }
                else if (!isLogIn){
                    TTP.sendResponse(new Message("User not found!"), getOutputStream());
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
