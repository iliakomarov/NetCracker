package Tree2.src.Info;

/**
 * Created by Степан on 14.11.2015.
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
}
