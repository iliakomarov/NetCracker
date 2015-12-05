package Info;

import Exсeptions.StoppedTaskException;
import Generations.IDGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Степан on 11.11.2015.
 */
public class Task  {
    private int id;
    private String name;
    private boolean busy, stopped;
    private Date creationDate;
    private ArrayList<Date[]> usingDate;

    private Task(String name)
    {
        this.id = IDGenerator.getInstance();
        this.name=name;
        this.busy=this.stopped=false;
        this.creationDate = new Date(); //TODO проверить дату new Date
        this.usingDate=new ArrayList<>();
    }

    public static Task getInstance(String name)
    {
        return new Task(name);
    }

    public boolean startTask() throws StoppedTaskException {
        checkStop();
        Date[] date = new Date[2];
        date[0] = new Date();
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

    public Info getFullInfo() //TODO info
    {
        return null;
    }

    public Info getSimpleInfo() //TODO info
    {
        return null;
    }

    public boolean isBusy() {
        return busy;
    }

    public boolean isStopped() {
        return stopped;
    }

    public long getWorkingTime() throws StoppedTaskException {
        checkStop();
        long time = 0;
        for (Date[] d : usingDate) {
            time += d[1].getTime() - d[0].getTime();
        }
        return time;
    }
}
