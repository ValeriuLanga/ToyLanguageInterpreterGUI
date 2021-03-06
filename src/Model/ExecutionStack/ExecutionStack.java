package Model.ExecutionStack;

import java.util.LinkedList;
import java.util.List;

public class ExecutionStack<T> implements ExecutionStackInterface<T> {
    private LinkedList<T> stack;

    public ExecutionStack(){
        stack = new LinkedList<T>();
    }

    public T pop(){
        return stack.pop();
    }

    public boolean isEmpty(){
        return stack.size() == 0;
    }

    @Override
    public void push(T element){
        stack.push(element);
    }

    @Override
    public T peek()
    {
        return stack.peek();
    }

    @Override
    public List<T> getUnderlayingList() {
        return stack;
    }

    @Override
    public Iterable<T> getAsIterable() {
        return stack;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for(T element : stack) {
            stringBuilder.append("\n");
            stringBuilder.append(element);
        }

        return stringBuilder.toString();
    }
}
