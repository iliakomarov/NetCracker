package client.src.client.clientinterface;

/**
 * Created by Fadeev on 07.11.2015.
 */
public interface Client {
    boolean logIn(String login, String password);
    boolean logOut(String login);
    void getTree();
    boolean load(String name);
    boolean save(String name);
    boolean create(String name);
    boolean delete(String name);
    boolean addTask(String name);
    boolean deleteTask(String name);
    boolean startTask(String name);
    boolean stopTask(String name);
    boolean pauseTask(String name);
    boolean renameTask(String oldName, String newName);
    void getStat();


}
