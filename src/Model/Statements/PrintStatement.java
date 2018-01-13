package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;
import Model.Expressions.Expression;
import Model.Heap.HeapInterface;
import Model.OutputList.OutputListInterface;
import Model.ProgramState;
import Model.SymbolTable.SymbolTableInterface;

public class PrintStatement implements Statement {
    private Expression expression;

    public PrintStatement(Expression e) {
        this.expression = e;
    }

    @Override
    public ProgramState execute (ProgramState currentState) throws DivisionByZeroException, UnknownOperationException {
        OutputListInterface<Integer> list = currentState.getOutputList();
        SymbolTableInterface<String, Integer> symbolTable = currentState.getSymbolTable();
        HeapInterface<Integer, Integer> heap = currentState.getHeap();

        int res = this.expression.eval(symbolTable, heap);
        list.addElement(res);

        return null;
    }

    @Override
    public String toString() {
        return "Print(" + expression.toString() + ");";
    }
}
