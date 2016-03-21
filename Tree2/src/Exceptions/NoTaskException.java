package Tree2.src.Exceptions;

/**
 * Created by Ilia Komarov on 14.03.2016.
 */
public class NoTaskException extends ClassCastException {
    public NoTaskException() {
        super("There's no task!");
    }
}
