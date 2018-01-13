package Model.OutputList;

public interface OutputListInterface<T> {
    public void     addElement(T element);
    public T        getElementOnPosition(int index);
    public void     removeElement(T element);
    public int      getSize();
    public boolean  isEmpty();
}
