package server.src.Exceptions;

/**
 * Created by Fadeev on 28.03.2016.
 */
public class SuchUserAlreadyExist extends Exception {
    public SuchUserAlreadyExist(String message){
        super(message);
    }
}
