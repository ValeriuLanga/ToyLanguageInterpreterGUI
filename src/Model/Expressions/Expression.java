package Model.Expressions;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;
import Model.Heap.HeapInterface;
import Model.SymbolTable.SymbolTableInterface;

public interface Expression {
    public int eval(SymbolTableInterface<String, Integer> SymbolTable, HeapInterface<Integer, Integer> heap) throws DivisionByZeroException, UnknownOperationException;
}
