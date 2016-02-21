package Tree;

import Info.*;

import javax.swing.tree.*;

/**
 * Created by Степан on 09.11.2015.
 */
public class TaskTree extends DefaultTreeModel {

    public TaskTree(TaskTreeNode root) {
        super(root);
    }

    public boolean isBusy(int id)
    {
        return true;
    }//сообщение о занятости задачей
    public boolean addTaskByID(int id, String task)
    {
        return true;
    }//добавление задачи

    public boolean deleteNodeByID(int id)
    {
        return true;
    }// удаление узла вместе с подзадачами
    public boolean changeTaskByID (int id, String newName )
    {
        return true;
    }// изменение задачи по ИД
    public Info infoByID (int id)
    {
        return null;
    }//вывод информации по ИД
    //  public Info moreinfoByID(int id);
    public TaskTree loadTree(String source)
    {
        return null;
    }//
    public boolean saveTree(String source)
    {
        return true;
    }
    public Statistic getStatistic (int id)
    {
        return null;
    }//получение статистики, собранной по ID ????

}

