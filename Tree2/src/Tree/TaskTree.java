package Tree;

import Info.*;

import javax.swing.tree.*;

/**
 * Created by ������ on 09.11.2015.
 */
public class TaskTree extends DefaultTreeModel {

    public TaskTree(TaskTreeNode root) {
        super(root);
    }

    public boolean isBusy(int id)
    {
        return true;
    }//��������� � ��������� �������
    public boolean addTaskByID(int id, String task)
    {
        return true;
    }//���������� ������

    public boolean deleteNodeByID(int id)
    {
        return true;
    }// �������� ���� ������ � �����������
    public boolean changeTaskByID (int id, String newName )
    {
        return true;
    }// ��������� ������ �� ��
    public Info infoByID (int id)
    {
        return null;
    }//����� ���������� �� ��
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
    }//��������� ����������, ��������� �� ID ????

}

