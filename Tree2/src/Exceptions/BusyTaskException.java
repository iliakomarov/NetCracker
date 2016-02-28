package Tree2.src.Exceptions;

/**
 * Created by Ilia Komarov on 14.02.2016.
 */
public class BusyTaskException extends Exception {
    public BusyTaskException() {
        super("Task is busy!");
    }
}
