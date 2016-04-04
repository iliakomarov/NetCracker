package client.src.com.company;


import Tree2.src.Exceptions.StoppedTaskException;
import client.src.client.Client;
import client.src.client.exception.NoSuchUserException;
import client.src.info.Task;
import client.src.tree.TaskTree;
import client.src.tree.TaskTreeNode;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        try {
            client.LogIn("d", "def");
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }
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

                    try {
                        sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        thread.setDaemon(true);
        thread.start();
        /*try {

            TaskTreeNode taskTreeNode = TaskTreeNode.getInstance("Test task from client!");
            //taskTreeNode.getTask().startTask();
            client.addTask(taskTreeNode, 0, "general");
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }*/

        try {
            client.startTask(53, "general");
            client.pauseTask(53, "general");
            client.stopTask(53, "general");
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }

        /*TaskTree tree = null;
        try {
            tree = client.getTree("general");
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }*/

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //client.deleteTask(3, "def");



    }


}
