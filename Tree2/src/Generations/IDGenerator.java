package Tree2.src.Generations;

import java.io.Serializable;

/**
 * Created by ������ on 11.11.2015.
 */
public class IDGenerator {
    private int id;
    private static IDGenerator instance;

    private IDGenerator() {
        id = 1;
    }

    public static int getInstance() {
        if (instance == null) instance = new IDGenerator();
        return instance.getId();
    }

    public int getId() {
        return id++;
    }
}
