package Tree2.src.Exceptions;

/**
 * Created by Ilia Komarov on 13.03.2016.
 */
public class WrongNameException extends IllegalArgumentException {
    public WrongNameException() {
        super("Task with the same name exists!");
    }
}
