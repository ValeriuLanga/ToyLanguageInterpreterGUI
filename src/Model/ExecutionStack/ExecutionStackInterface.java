package Model.ExecutionStack;

public interface ExecutionStackInterface<T> {
    public void push(T element);
    public T pop();
    public boolean isEmpty();
    public T peek();
}
