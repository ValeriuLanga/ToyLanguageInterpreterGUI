package Model.Exceptions;

public class NotExistingException extends RuntimeException {
    public NotExistingException(String message) {
        super(message);
    }
}
