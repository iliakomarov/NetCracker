package client.src.client;


import client.src.client.loader.TreeLoader;
import client.src.communications.AddTask;
import client.src.communications.DeleteTask;
import client.src.communications.GetTree;
import client.src.communications.Message;
import client.src.info.Task;
import client.src.tree.TreeNode;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;

/**
 * Created by Fadeev on 12.11.2015.
 */
public class Client {
    private static String hostName = "localhost";
    private static int portNumber = 9999;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    public Client(){
        Socket clientSocket = null;

        try {
            clientSocket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public DataOutputStream getOutputStream() {
        return outputStream;
    }


    public DataInputStream getInputStream(){
        return inputStream;
    }

    public Message getResponse() throws IOException{
        String xml = "";

        getInputStream().read();
        char currChar;

        try {
            while ((currChar = (char)getInputStream().read()) != '*')

                xml += currChar;

        } catch (IOException e) {
            e.getMessage();
        }

        Marshall marshall = new Marshall();
        Message message = null;
        try {
            message =(Message) marshall.unmarshall(xml, Message.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return message;
    }

    public void updateTrees() throws IOException{
        String xml = "";


        char currChar = '.';

        try {
            while ((currChar = (char)getInputStream().read()) != '*')

                xml += currChar;

        } catch (IOException e) {
            e.getMessage();
        }

        FileWriter fileWriter = new FileWriter(new File("C:\\Users\\Fadeev\\IdeaProjects\\SocketClientCracker\\trees.xml"));
        fileWriter.write(xml);
        fileWriter.close();
    }


    /*public void sendRequest(RequestImpl request) throws IOException{
        Marshall marshall = new Marshall();
        getOutputStream().write("r".getBytes());
        marshall.marshall(request, getOutputStream(), RequestImpl.class);
        getOutputStream().write("*".getBytes());
    }*/

    public Object getObject(Class aClass) throws IOException{
        String xml = "";

        char startChar = (char)getInputStream().read();
        char currChar = '.';

        try {
            while ((currChar = (char)getInputStream().read()) != '*')

                xml += currChar;

        } catch (IOException e) {
            e.getMessage();
        }

        Marshall marshall = new Marshall();
        TreeNode object = null;
        try {
            object =(TreeNode) marshall.unmarshall(xml, aClass);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return object;
    }

    public void sendObject(Object object, DataOutputStream dataOutputStream, Class aClass) throws IOException{
        getOutputStream().write("m".getBytes());
        Marshall marshall = new Marshall();
        marshall.marshall(object, getOutputStream(), aClass);
        getOutputStream().write("*".getBytes());
    }


    public TreeNode getTree(String name){

        try {
            sendObject(new GetTree(name, "getTree"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            sendObject(new GetTree(name, "getTree"), getOutputStream(), GetTree.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message response = null;
        try {
            response = (Message) getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TreeNode treeNode = null;
        if (response.getMessage().equals("ok")){
            try {
                updateTrees();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            treeNode = TreeLoader.loadTree(name, "C:\\Users\\Fadeev\\IdeaProjects\\SocketClientCracker\\trees.xml");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }


        return treeNode;
    }


    public boolean addTask(Object userObject, Task parent, String treeName){

        try {
            sendObject(new Message("addTask"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            sendObject(new AddTask((Task)userObject, parent, treeName, "addTask"), getOutputStream(), AddTask.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message message = null;
        try {
            message = getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (message.getMessage().equals("ok")){
                return true;
        }
        return false;
    }

    public boolean deleteTask(String nodeName, String treeName){
        try {
            sendObject(new Message("deleteTask"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            sendObject(new DeleteTask(nodeName, treeName, "deleteTask"), getOutputStream(), DeleteTask.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message message = null;
        try {
            message = getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (message.getMessage().equals("ok")){
            return true;
        }
        return false;
    }


}
