package Model.ExecutionStack;
import java.util.List;

public interface ExecutionStackInterface<T> {
    public void push(T element);
    public T pop();
    public boolean isEmpty();
    public T peek();
    public List<T> getUnderlayingList();
    public Iterable<T> getAsIterable();
}
