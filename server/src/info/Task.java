package server.src.info;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Степан on 11.11.2015.
 */
public class Task  {
    private String name;
    private boolean busy, stopped;
    private Date creationDate;
    private ArrayList<Date[]> usingDate;

    public Task(){
        this.name = "Default task name";
    }

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
    public boolean startTask(){return false;}
    public boolean pauseTask(){return false;}
    public boolean stopTask(){return false;}
    public boolean changeName(String name){return false;}
    public Info getFullInfo(){return new Info();}
    public Info getSimpleInfo(){return new Info();}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
