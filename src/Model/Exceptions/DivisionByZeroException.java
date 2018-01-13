package Model.Exceptions;

public class DivisionByZeroException extends Exception {
    public DivisionByZeroException() {
        super("Division by zero!");
    }
}
