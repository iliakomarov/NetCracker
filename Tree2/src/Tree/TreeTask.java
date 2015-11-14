package Tree;

import Info.Statistic;

import javax.swing.tree.DefaultTreeModel;

/**
 * Created by ������ on 09.11.2015.
 */
public class TreeTask extends DefaultTreeModel {
    int treeID=0;
    public boolean isBusy(int id);//��������� � ��������� �������
    public boolean addTaskByID(int id, String task); //���������� ������
    public boolean deleteNodeByID (int id );// �������� ���� ������ � �����������
    public boolean changeTaskByID (int id, String newName );   // ��������� ������ �� ��
    public Info infoByID (int id);//����� ���������� �� ��
    //  public Info moreinfoByID(int id);
    public TreeTask loadTree (String source); //
    public boolean saveTree(String source);
    public Statistic getStatistic (int id);//��������� ����������, ��������� �� ID ????

}

