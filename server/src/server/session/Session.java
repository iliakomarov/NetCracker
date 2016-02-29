package server.src.server.session;



import server.src.communications.AddTask;
import server.src.communications.DeleteTask;
import server.src.communications.GetTree;
import server.src.communications.Message;
import server.src.loader.TreeLoader;
import server.src.server.Marshall;
import server.src.server.session.protocols.TTP;
import server.src.tree.TreeNode;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;

/**
 * Created by Fadeev on 12.11.2015.
 */
public class Session implements Runnable {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private TreeNode treeNode;

    public Session(Socket socket) throws IOException{
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
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }

                try {

                    Document document = TreeLoader.marshalTree(treeNode, getTree.getNameTree());

                    StringWriter stringWriter = new StringWriter();
                    TreeLoader.toXML(document, stringWriter);


                    TTP.sendResponse(message, getOutputStream());
                    getOutputStream().write(stringWriter.toString().getBytes());
                    getOutputStream().write('*');
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (TransformerConfigurationException e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
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

                    TreeNode currTreeNode = TreeLoader.loadTree(addTask.getName());

                    TreeNode findedTreeNode = TreeLoader.findNode(currTreeNode,addTask.getParent());
                    findedTreeNode.add(new TreeNode(addTask.getUserObject(), true));
                    TreeLoader.updateTree(currTreeNode, addTask.getName());

                    System.out.println(findedTreeNode.getUserObject());

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
                }
            }else if (message.getMessage().equals("deleteTask")){
                DeleteTask deleteTask = null;

                deleteTask = (DeleteTask)TTP.getObject(getInputStream(), DeleteTask.class);

                try {

                    TreeNode currTreeNode = TreeLoader.loadTree(deleteTask.getName());
                    TreeNode findedTreeNode = TreeLoader.findNode(currTreeNode, deleteTask.getNodeName());
                    currTreeNode = (TreeNode)findedTreeNode.getParent();
                    currTreeNode.remove(findedTreeNode);
                    currTreeNode = (TreeNode)currTreeNode.getRoot();
                    TreeLoader.updateTree(currTreeNode, deleteTask.getName());

                    System.out.println(findedTreeNode.getUserObject());

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

    public DataOutputStream getOutputStream(){
        return this.outputStream;
    }
}
