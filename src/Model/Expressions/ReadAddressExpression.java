package Model.Expressions;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;
import Model.Heap.HeapInterface;
import Model.SymbolTable.SymbolTableInterface;

public class ReadAddressExpression implements Expression {
    private String variableName;

    public ReadAddressExpression(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public int eval(SymbolTableInterface<String, Integer> SymbolTable, HeapInterface<Integer, Integer> heap) throws DivisionByZeroException, UnknownOperationException {
        // return the value of the address which 'variableName' has in the SymbolTable
        return heap.get(SymbolTable.get(variableName));
    }

    @Override
    public String toString(){
        return "ReadHeap (" + variableName + ")";
    }

}
