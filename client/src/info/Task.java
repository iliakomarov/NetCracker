package client.src.info;


import Tree2.src.Exceptions.BusyTaskException;
import Tree2.src.Exceptions.StoppedTaskException;
import client.src.generations.IDGenerator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ������ on 11.11.2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Task {
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "busy")
    private boolean busy;
    @XmlElement(name = "stopped")
    private boolean stopped;
    @XmlElement(name = "creationDate")
    private Date creationDate;
    @XmlElement(name = "usingDate")
    private ArrayList<Date[]> usingDate;

    public Task(){}

    private Task(String name) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("client\\src\\lastID.txt");
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        int lastID = dataInputStream.readInt();
        IDGenerator generator = IDGenerator.getInstance();
        generator.setId(lastID);
        dataInputStream.close();
        this.id = generator.getId();
        this.name=name;
        this.busy=this.stopped=false;
        this.creationDate = new Date();
        this.usingDate=new ArrayList<>();
    }

    public static Task getInstance(String name) throws IOException {
        return new Task(name);
    }

    public boolean startTask() throws StoppedTaskException {
        checkStop();
        Date[] date = new Date[2];
        date[0] = new Date();
        if (usingDate == null) usingDate = new ArrayList<>();
        usingDate.add(date);
        busy = true;
        return true;
    }

    public boolean pauseTask() throws StoppedTaskException {
        checkStop();
        usingDate.get(usingDate.size() - 1)[1] = new Date();
        busy = false;
        return true;
    }

    public void checkStop() throws StoppedTaskException {
        if (stopped) throw new StoppedTaskException();
    }

    public void checkBusy() throws BusyTaskException {
        if (busy) throw new BusyTaskException();
    }

    public boolean stopTask() throws StoppedTaskException {
        pauseTask();
        stopped = true;
        return true;
    }
    public boolean changeName(String name)
    {
        this.name = name;
        return true;
    }

    public int getId()
    {
        return this.id;
    }

    public client.src.info.Info getFullInfo() throws BusyTaskException //TODO info
    {
        long time=getWorkingTime();
        long ms = time % 1000;
        time = time / 1000;
        long sec = time % 60;
        time= time / 60;
        long min = time % 60;
        time = time / 60;
        long hours = time % 24;
        time = time / 24;
        long days = time;

        return new client.src.info.Info(id,name,new Date(creationDate.getTime()).toString(),isBusy(),isStopped(),new String(days+"d "+hours+"h "+min+"m "+sec+"s "+ms+"ms"));
    }

    public client.src.info.Info getSimpleInfo() //TODO info
    {
        return new client.src.info.Info(id,name);
    }

    public boolean isBusy() {
        return busy;
    }

    public boolean isStopped() {
        return stopped;
    }

    public long getWorkingTime() throws BusyTaskException {
        checkBusy();
        long time = 0;
        if (usingDate != null) {
            for (Date[] d : usingDate) {
                time += d[1].getTime() - d[0].getTime();
            }
        }
        return time;
    }

    public String getName(){
        return this.name;
    }


    public String toString(){
        return getName();
    }

}
