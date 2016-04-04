package client.src.info;

/**
 * Created by Ñòåïàí on 14.11.2015.
 */
public class Info {
    private Object[] info;
    private int length;
    public Info(Object... info)
    {
        this.length=info.length;
        this.info=info;
    }
    public String toString()
    {
        StringBuilder res=new StringBuilder("");
        for (Object obj: info)
        {
            res.append(obj.toString()+" ");
        }
        return res.toString();
    }

    public int getLength() {
        return length;
    }

    public Object getInfoAt(int index) {
        return info[index];
    }
    
    public void setInfoAt(int index, Object i) {
        if (index!=info.length) {
            info[index] = i;
        }
        else {
            Object[] info1 = new Object[++length];
            System.arraycopy(info,0,info1,0,length-1);
            info1[info1.length-1]=i;
            info=info1;
        }
    }
}
