package Model.OutputList;

import Model.Exceptions.NotExistingException;

import java.util.LinkedList;
import java.util.List;

public class OutputList<T> implements OutputListInterface<T> {
    private LinkedList<T> outputList;

    public OutputList(){
        outputList = new LinkedList<>();
    }

    @Override
    public void addElement(T element){
        outputList.addLast(element);
    }

    @Override
    public T getElementOnPosition(int index) throws NotExistingException {
        T element;

        try{
            element = outputList.get(index);
        }
        catch(IndexOutOfBoundsException i){
            throw new NotExistingException("Invalid Parameter!");
        }

        return element;
    }

    @Override
    public void removeElement(T element) throws NotExistingException{
        try{
            outputList.remove(element);
        }
        catch (Exception e){
            throw new NotExistingException("Invalid Parameter!");
        }
    }

    @Override
    public int getSize()
    {
        return outputList.size();
    }

    @Override
    public boolean isEmpty(){
        return outputList.isEmpty();
    }

   public List<T> getOutputList(){
       LinkedList<T> newList = new LinkedList<T>(outputList);
       return newList;
    }

    @Override
    public String toString(){
       StringBuilder stringBuilder = new StringBuilder();

       for(T element : outputList) {
           stringBuilder.append(String.valueOf(element));
           stringBuilder.append(" ");
       }
       return stringBuilder.toString();
    }

}
