package Exceptions;

/**
 * Created by Ilia Komarov on 04.12.2015.
 */
public class StoppedTaskException extends Exception {
    public StoppedTaskException() {
        super("Task is stopped!");
    }
}
