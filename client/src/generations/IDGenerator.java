package client.src.generations;

/**
 * Created by Степан on 11.11.2015.
 */
public class IDGenerator {
    private int id;
    private static IDGenerator instance;

    private IDGenerator() {
        id = 1;
    }

    public static IDGenerator getInstance() {
        if (instance == null) return instance = new IDGenerator();
        else return instance;
    }

    public void setId(int num){
        id = num;
    }

    public int getId() {
        return id++;
    }

}
