package client.src.com.company;


import client.src.client.Client;
import client.src.tree.TaskTree;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        TaskTree tree = client.getTree("def");
        //tree = client.getTree("Ilya");
        //client.deleteTask(0, "def");
        /*client.addTask("1", "root", "Ilya");
        client.addTask("2", "root", "Ilya");
        client.addTask("3", "root", "Ilya");*/
        //client.addTask(TaskTreeNode.getInstance("Test test test test task from client!"), 2, "def");
        //client.deleteTask(3, "def");

    }
}
