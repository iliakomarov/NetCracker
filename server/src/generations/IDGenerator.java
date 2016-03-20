package server.src.generations;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Степан on 11.11.2015.
 */
public class IDGenerator implements Serializable {
    private static int id;
    private static IDGenerator instance;

    private IDGenerator() {
        id = 1;
    }

    public static IDGenerator getInstance() {
        if (instance == null) return instance = new IDGenerator();
        else return instance;
    }

    public static void setId(int num){
        id = num;
    }

    public int getId() {
        return id++;
    }
}
