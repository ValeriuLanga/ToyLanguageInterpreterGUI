package Model.Expressions;

import Model.Heap.HeapInterface;
import Model.SymbolTable.SymbolTableInterface;

public class ConstantExpression implements Expression {
    private int constant;

    public ConstantExpression(int value){
        this.constant = value;
    }

    @Override
    public int eval(SymbolTableInterface<String, Integer> SymTable, HeapInterface<Integer, Integer> heap) {
        return this.constant;
    }

    @Override
    public String toString() {
        return String.valueOf(constant);
    }
}
