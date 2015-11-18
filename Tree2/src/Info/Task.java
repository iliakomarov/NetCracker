package Info;

import Generations.IDGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Степан on 11.11.2015.
 */
public class Task  {
    private String name;
    private boolean busy, stopped;
    private Date creationDate;
    private ArrayList<Date[]> usingDate;

    private Task(String name)
    {
        this.name=name;
        this.busy=this.stopped=false;
        this.creationDate= Calendar.getInstance().getTime();
        this.usingDate=new ArrayList<>();
        /*Date[] date=new Date[2];
        date[0]=Calendar.getInstance().getTime();
        usingDate.add(date);*/
    }
    public static Task newTask(String name)
    {
        return new Task(name);
    }
    public boolean startTask()
    {
        return true;
    }
    public boolean pauseTask()
    {
        return true;
    }
    public boolean stopTask()
    {
        return true;
    }
    public boolean changeName(String name)
    {
        return true;
    }
    public Info getFullInfo()
    {
        return null;
    }
    public Info getSimpleInfo()
    {
        return null;
    }

}
