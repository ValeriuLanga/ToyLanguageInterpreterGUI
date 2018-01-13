package Model.Expressions;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;
import Model.Heap.HeapInterface;
import Model.SymbolTable.SymbolTableInterface;

public class BooleanExpression implements Expression {

    private String comparator;  // i.e. one of < > == <= >= !=
    private Expression expression1;
    private Expression expression2;

    public BooleanExpression(String comparator, Expression expression1, Expression expression2) {
        this.comparator = comparator;
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public int eval(SymbolTableInterface<String, Integer> symbolTable, HeapInterface<Integer, Integer> heap) throws DivisionByZeroException, UnknownOperationException {

        int value1 = expression1.eval(symbolTable, heap);
        int value2 = expression2.eval(symbolTable, heap);

        // switch according to comparator
        // note that we'll use the c convention where 0 denotes false
        // and 1 denotes true
        switch( comparator ){
            case ">" : return (value1 > value2) ? 1 : 0;
            case ">=": return (value1 >= value2) ? 1 : 0;
            case "==": return (value1 == value2) ? 1 : 0;
            case "!=": return (value1 != value2) ? 1 : 0;
            case "<" : return (value1 < value2) ? 1 : 0;
            case "<=": return (value1 <= value2) ? 1 : 0;

            default : throw new UnknownOperationException(comparator);
        }
    }

    @Override
    public String toString() {
        return "(" + expression1.toString() + comparator + expression2.toString() + ')';
    }
}
