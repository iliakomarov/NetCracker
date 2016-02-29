package Tree2.src.Tree;

import Tree2.src.Tree.TaskTree;

/**
 * Created by Ilia Komarov on 23.02.2016.
 */
public class User {
    private String name, surname, login, password;
    private TaskTree privateTree;

    public User()
    {
        this("def","def","def","def");
    }
    public User(String name, String surname, String login, String password) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.privateTree=new TaskTree(this);
    }
}
