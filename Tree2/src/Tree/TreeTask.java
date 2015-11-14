package Tree;

import Info.Statistic;

import javax.swing.tree.DefaultTreeModel;

/**
 * Created by Степан on 09.11.2015.
 */
public class TreeTask extends DefaultTreeModel {
    int treeID=0;
    public boolean isBusy(int id);//сообщение о занятости задачей
    public boolean addTaskByID(int id, String task); //добавление задачи
    public boolean deleteNodeByID (int id );// удаление узла вместе с подзадачами
    public boolean changeTaskByID (int id, String newName );   // изменение задачи по ИД
    public Info infoByID (int id);//вывод информации по ИД
    //  public Info moreinfoByID(int id);
    public TreeTask loadTree (String source); //
    public boolean saveTree(String source);
    public Statistic getStatistic (int id);//получение статистики, собранной по ID ????

}

