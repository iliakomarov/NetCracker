package client.src.client;


import client.src.client.exception.NoSuchUserException;
import client.src.client.exception.RegistrationException;
import client.src.communications.*;
import client.src.generations.IDGenerator;
import client.src.tree.TaskTree;
import client.src.tree.TaskTreeNode;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.Socket;
import java.rmi.UnmarshalException;

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

    public Client() throws IOException {
        Socket clientSocket = null;


        FileReader fileReader = new FileReader(new File("").getAbsolutePath() + File.separator + "config.conf");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        try {
            String[] serverSocket = bufferedReader.readLine().split(":");
            this.hostName = serverSocket[0];
            portNumber = Integer.parseInt(serverSocket[1]);
        }
        catch (NullPointerException e){
            System.out.println("Socket set on default(localhost:9999)");
            this.hostName = "localhost";//default
            portNumber = 9999;//default
        }




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
        if (client == null) try {
            return client = new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
        else return client;

        return null;
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


    public static TaskTreeNode treeNode = null;

    public static void setParents(TaskTreeNode root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            treeNode = (TaskTreeNode) root.getChildAt(i);
            treeNode.setParent(root);
                setParents(treeNode);
        }

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
            try{
                tree = (TaskTree) getObject(TaskTree.class);
            }
            catch (ClassCastException e){
                tree = (TaskTree) getObject(TaskTree.class);
            }

        }else {
            throw new NoSuchUserException("User not exist or request has wrong data!");
        }


        setParents(tree.getRoot());

        return tree;
    }


    public boolean addTask(TaskTreeNode userObject, int id, String treeName) throws NoSuchUserException, IOException {

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


    public boolean LogIn(String login, String password) throws NoSuchUserException {
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

        if (message.getMessage().equals("fail")){
            throw new NoSuchUserException("User not found!");
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

    public boolean Registration(String name, String surname, String login, String password) throws NoSuchUserException, RegistrationException {
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

        if (message.getMessage().equals("fail")){
            throw new NoSuchUserException("User already exist!");
        }

        if (message.getMessage().equals("logout")){
            throw new RegistrationException("Log out!");
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

    public boolean rename(String newName, int id, String treeName) throws NoSuchUserException, IOException {

        try {
            sendObject(new Message("rename"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }



        try {
            sendObject(new Rename(newName, id, treeName, "rename"), getOutputStream(),Rename.class);
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


    public boolean startTask(int id, String treeName) throws NoSuchUserException, IOException {

        try {
            sendObject(new Message("starttask"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }



        try {
            sendObject(new StartTask(id, treeName, "starttask"), getOutputStream(),StartTask.class);
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


    public boolean stopTask(int id, String treeName) throws NoSuchUserException, IOException {

        try {
            sendObject(new Message("stoptask"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }



        try {
            sendObject(new StopTask(id, treeName, "stoptask"), getOutputStream(),StopTask.class);
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

    public boolean pauseTask(int id, String treeName) throws NoSuchUserException, IOException {

        try {
            sendObject(new Message("pausetask"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }



        try {
            sendObject(new PauseTask(id, treeName, "pausetask"), getOutputStream(),PauseTask.class);
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


    public Integer getId(){
        try {
            sendObject(new Message("id"), getOutputStream(), Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Integer id = 0;
        GetId getId = null;
        try {
            getId = (GetId)getObject(GetId.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getId.getId();
    }

}
