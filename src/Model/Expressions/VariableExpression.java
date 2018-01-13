package Model.Expressions;

import Model.Exceptions.NotExistingException;
import Model.Heap.HeapInterface;
import Model.SymbolTable.SymbolTableInterface;

public class VariableExpression implements Expression {
    private String varName;

    public VariableExpression(String name) {
        this.varName = name;
    }

    @Override
    public int eval(SymbolTableInterface<String, Integer> SymTable, HeapInterface<Integer, Integer> heap) throws NotExistingException {

        if (SymTable.contains(this.varName))
            return SymTable.get(this.varName);
        else {
            throw new NotExistingException("Not found!");
        }
    }

    @Override
    public String toString(){
        return this.varName;
    }
}
