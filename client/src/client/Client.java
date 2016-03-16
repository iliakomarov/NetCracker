package client.src.client;


import client.src.client.loader.TreeLoader;
import client.src.communications.AddTask;
import client.src.communications.DeleteTask;
import client.src.communications.GetTree;
import client.src.communications.Message;
import client.src.tree.TaskTree;
import client.src.tree.TaskTreeNode;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
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
        Object object = null;
        try {
            object = marshall.unmarshall(xml, aClass);
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


    public TaskTree getTree(String name) throws IOException {

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
            response =  getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TaskTree tree = (TaskTree)getObject(TaskTree.class);
        TaskTreeNode treeNode = null;



        return tree;
    }


    public boolean addTask(TaskTreeNode userObject, int id, String treeName){

        try {
            sendObject(new Message("addTask"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            sendObject(new AddTask(userObject, id, treeName, "addTask"), getOutputStream(), AddTask.class);
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

    public boolean deleteTask(int id, String treeName){
        try {
            sendObject(new Message("deleteTask"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            sendObject(new DeleteTask(id, treeName, "deleteTask"), getOutputStream(), DeleteTask.class);
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
