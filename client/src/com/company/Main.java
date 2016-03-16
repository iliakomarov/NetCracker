package client.src.com.company;


import client.src.client.Client;
import client.src.client.exception.NoSuchUserException;
import client.src.tree.TaskTree;
import client.src.tree.TaskTreeNode;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.LogIn("a", "a");
        //client.LogOut();
        /*try {
            client.Registration("general", "a", "a", "a");
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }*/
        //client.LogIn("a", "a");
        /*try {
            TaskTree tree = client.getTree("ilya");
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }*/
        //tree = client.getTree("Ilya");
        //client.deleteTask(0, "def");
        /*client.addTask("1", "root", "Ilya");
        client.addTask("2", "root", "Ilya");
        client.addTask("3", "root", "Ilya");*/

        Thread thread = new Thread() {

            public void run() {

                while (true) {
                    if (client.isRefreshGeneralTree()) {
                        client.setIsRefreshGeneralTree(false);
                        System.out.println("Tree was refresh!");
                    }
                }
            }
        };
        //thread.setDaemon(true);
        thread.start();
        try {
            client.addTask(TaskTreeNode.getInstance("Test test test test task from client!"), 0, "general");
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }
        //client.deleteTask(3, "def");

    }


}
