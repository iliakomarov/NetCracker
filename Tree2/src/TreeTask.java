import Info.Task;
import Statistics.Statistic;
import Tree.TreeNode;

/**
 * Created by ������ on 09.11.2015.
 */
public interface TreeTask {
    int id=0;
    int numberTask=0;
    public TreeNode searchTaskByNumber(int numberTask); //����� �� ������
    public TreeNode newTask(int id, String task);// ������� �� � ������ � ���� ������
    public Task isBusy(int  id);//��������� � ��������� �������
    public TreeNode deleteNodeById (int id );// �������� ����
    public String changeTask (int id );   // ��������� ������ �� ��
    public Task infoById ( int id);//����� ���������� �� ��
    public Task printTime(double time);//����� ������� �����������
    public TreeNode loadTree (String sourse); //
    public Task moreInfo (String sourse);//
    public Statistic  getStatistic (int id);//��������� ����������, ��������� �� ID ????


}

