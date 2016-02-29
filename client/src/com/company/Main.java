package client.src.com.company;


import client.src.client.Client;
import client.src.tree.TreeNode;

public class Main {

    public static void main(String[] args) {
        Client client = new Client();
        TreeNode tree = client.getTree("Vlad");
        //tree = client.getTree("Ilya");
        //client.deleteTask("1", "Ilya");
        /*client.addTask("1", "root", "Ilya");
        client.addTask("2", "root", "Ilya");
        client.addTask("3", "root", "Ilya");*/
        client.addTask("dghfd", "8963", "Ilya");
    }
}
