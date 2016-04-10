package server.src.generations;

import java.io.*;

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

    public int getId() throws IOException {

        FileInputStream fileInputStream = new FileInputStream(new File("").getAbsolutePath() + File.separator + "lastID.txt");
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        this.id = dataInputStream.readInt();
        int lastID = this.id;
        FileOutputStream fileOutputStream = new FileOutputStream(new File("").getAbsolutePath() + File.separator + "lastID.txt");
        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
        lastID += 1;
        dataOutputStream.writeInt(lastID);
        return lastID;
    }

}
