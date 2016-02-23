package Exceptions;

import java.util.NoSuchElementException;

/**
 * Created by Ilia Komarov on 21.02.2016.
 */
public class NoSuchTaskWithIDException extends NoSuchElementException{
    public NoSuchTaskWithIDException() {
        super("Нет элемента с таким ID!");
    }
}
