package client.src.client;


import client.src.client.exception.NoSuchUserException;
import client.src.client.loader.TreeLoader;
import client.src.communications.*;
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
    private static Client client;
    private ConnectionToServer server;
    private boolean isRefreshGeneralTree;

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
            this.server = new ConnectionToServer(hostName, portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread refreshGeneralTree = new Thread(){
            public void run(){
                while (true){
                    if (server.isEdit()){
                        server.setIsEdit(false);
                        setIsRefreshGeneralTree(true);
                        System.out.println("!!!!");
                    }

                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        refreshGeneralTree.setDaemon(true);
        refreshGeneralTree.start();
    }


    public static client.src.client.Client getClient() {
        if (client == null) return client = new client.src.client.Client();
        else return client;
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


    public TaskTree getTree(String name) throws IOException, NoSuchUserException {

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
        TaskTree tree = null;
        if (!response.getMessage().equals("User not found!")){
            tree = (TaskTree)getObject(TaskTree.class);
        }else {
            throw new NoSuchUserException("User not exist or request has wrong data!");
        }

        return tree;
    }


    public boolean addTask(TaskTreeNode userObject, int id, String treeName) throws NoSuchUserException {

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

        if (message.getMessage().equals("User not found!")){
            throw new NoSuchUserException("User not found!");
        }

        if (message.getMessage().equals("ok")){
                return true;
        }
        return false;
    }

    public boolean deleteTask(int id, String treeName) throws NoSuchUserException {
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

        if (message.getMessage().equals("User not found!")){
            throw new NoSuchUserException("User not found!");
        }

        if (message.getMessage().equals("ok")){
            return true;
        }
        return false;
    }


    public boolean LogIn(String login, String password){
        try {
            sendObject(new Message("login"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            sendObject(new LogIn(login, password, "login"), getOutputStream(), LogIn.class);
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

    public void LogOut(){
        try {
            sendObject(new Message("logout"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean Registration(String name, String surname, String login, String password) throws NoSuchUserException {
        try {
            sendObject(new Message("registration"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            sendObject(new Registration(name, surname, login, password, "registration"), getOutputStream(), Registration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Message message = null;
        try {
            message = getResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (message.getMessage().equals("User not found!")){
            throw new NoSuchUserException("User not found!");
        }

        if (message.getMessage().equals("ok")){
            return true;
        }
        return false;
    }

    public boolean isRefreshGeneralTree() {
        return isRefreshGeneralTree;
    }

    public void setIsRefreshGeneralTree(boolean isRefreshGeneralTree) {
        this.isRefreshGeneralTree = isRefreshGeneralTree;
    }
}
