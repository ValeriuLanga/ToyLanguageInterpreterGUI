package Model.Exceptions;

public class UnknownOperationException extends Exception {
    public UnknownOperationException(String operator) {
        super("Unknown operation: " + operator);
    }

    public UnknownOperationException(char operator) {
        super("Unknown operator: " + operator);
    }
}
