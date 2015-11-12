import Info.Task;
import Statistics.Statistic;
import Tree.TreeNode;

/**
 * Created by Степан on 09.11.2015.
 */
public interface TreeTask {
    int id=0;
    int numberTask=0;
    public TreeNode searchTaskByNumber(int numberTask); //поиск по номеру
    public TreeNode newTask(int id, String task);// выводит ИД и задачу в виде текста
    public Task isBusy(int  id);//сообщение о занятости задачей
    public TreeNode deleteNodeById (int id );// удаление узла
    public String changeTask (int id );   // изменение задачи по ИД
    public Task infoById ( int id);//вывод информации по ИД
    public Task printTime(double time);//вывод времени пользования
    public TreeNode loadTree (String sourse); //
    public Task moreInfo (String sourse);//
    public Statistic  getStatistic (int id);//получение статистики, собранной по ID ????


}

