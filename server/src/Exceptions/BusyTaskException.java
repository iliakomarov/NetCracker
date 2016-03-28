package server.src.Exceptions;

import client.src.communications.Message;

/**
 * Created by Ilia Komarov on 14.02.2016.
 */
public class BusyTaskException extends Exception {
    public BusyTaskException() {
        super("Task is busy!");
    }
    public BusyTaskException(String message){
        super(message);
    }
}
